package no.nav.arbeidsgiver.kandidat.kandidatsok.es;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.arbeidsgiver.kandidat.kandidatsok.domene.es.KandidatsokTransformer;
import no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsCv;
import no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsPerioderMedInaktivitet;
import no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.sok.SokekriterierVeiledere;
import no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.sok.Sokeresultat;
import no.nav.arbeidsgiver.kandidat.kandidatsok.testsupport.ElasticSearchTestConfiguration;
import no.nav.arbeidsgiver.kandidatsok.es.client.EsIndexerService;
import no.nav.arbeidsgiver.kandidatsok.es.client.EsSokService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class HullICvIT {

    private EsSokService sokClient = ElasticSearchTestConfiguration.esSokService(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);

    private EsIndexerService indexerClient = ElasticSearchTestConfiguration.indexerCvService();

    private ObjectMapper objectMapper = ElasticSearchTestConfiguration.objectMapper();

    private KandidatsokTransformer kandidatsokTransformer = new KandidatsokTransformer();

    @BeforeEach
    public void before() {
        indexerClient.createIndex(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);
        indexerClient.bulkIndex(List.of(
                erIJobb(),
                startdatoForNåværendeInaktivePeriodeErMerEnnToÅrSiden(),
                nyligInaktivMenAvsluttetLangInaktivPeriodeForEttÅrSiden(),
                harAldriVærtIAktivitet()
        ), ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);
    }

    @AfterEach
    public void after() {
        indexerClient.deleteIndex(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);
    }

    private static String aktørIdErIJobb = "1000";

    private static EsCv erIJobb() {
        return giveMeEsCv(aktørIdErIJobb, new EsPerioderMedInaktivitet(null, singletonList(fromLocalDate(LocalDate.now().minusYears(3)))));
    }

    private static String aktørIdStartdatoNåværendeInaktivePeriodeMerEnnToÅrSiden = "2000";

    private static EsCv startdatoForNåværendeInaktivePeriodeErMerEnnToÅrSiden() {
        return giveMeEsCv(aktørIdStartdatoNåværendeInaktivePeriodeMerEnnToÅrSiden, new EsPerioderMedInaktivitet(fromLocalDate(LocalDate.now().minusYears(5)), singletonList(fromLocalDate(LocalDate.now().minusYears(8)))));
    }

    private static String aktørIdNyligInaktivMenAvsluttetLangInaktivPeriodeForEttÅrSiden = "3000";

    private static EsCv nyligInaktivMenAvsluttetLangInaktivPeriodeForEttÅrSiden() {
        return giveMeEsCv(aktørIdNyligInaktivMenAvsluttetLangInaktivPeriodeForEttÅrSiden, new EsPerioderMedInaktivitet(fromLocalDate(LocalDate.now().minusDays(3)), singletonList(fromLocalDate(LocalDate.now().minusYears(1)))));
    }

    private static String aktørIdAldriVærtIAktivitet = "4000";

    private static EsCv harAldriVærtIAktivitet() {
        return giveMeEsCv(aktørIdAldriVærtIAktivitet, new EsPerioderMedInaktivitet(null, emptyList()));
    }

    private static EsCv giveMeEsCv(String aktørId, EsPerioderMedInaktivitet esPerioderMedInaktivitet) {
        EsCv esCv = new EsCv(aktørId, "01016012345", "OLA", "NORDMANN", Date.from(Instant.now()), false, "JOBBS",
                "unnasluntrer@mailinator.com", "(+47) 22334455", "12345678", "NO", "1L",
                "hererjeg", "N", Date.from(Instant.now()), "Minvei 1", "", "", "0654", "OSLO", "NO", 5001, false,
                Date.from(Instant.now()), 301, FALSE, null, "IKVAL", null, "0220 NAV Asker", FALSE, FALSE,
                true, false, "LEDIG_NAA", "5001", "H149390", false, "Viken", "Lier");

        esCv.addPerioderMedInaktivitet(esPerioderMedInaktivitet);
        return esCv;
    }

    private static Date fromLocalDate(LocalDate localDate) {
        return new Date(localDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli());
    }

    @Test
    public void harIkkeHullHvisErIJobb() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med().hullICv(true).bygg());

        var aktørIder = sokeresultat.getCver().stream().map(cv -> cv.getAktorId());

        assertThat(aktørIder).doesNotContain(aktørIdErIJobb);
    }

    @Test
    public void test() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med().hullICv(false).bygg());

        var aktørIder = sokeresultat.getCver().stream().map(cv -> cv.getAktorId());

        assertThat(aktørIder).contains(aktørIdErIJobb, aktørIdStartdatoNåværendeInaktivePeriodeMerEnnToÅrSiden, aktørIdNyligInaktivMenAvsluttetLangInaktivPeriodeForEttÅrSiden, aktørIdAldriVærtIAktivitet);
    }
}
