package no.nav.arbeidsgiver.kandidat.kandidatsok.es;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.arbeidsgiver.kandidat.kandidatsok.domene.es.EsCvObjectMother;
import no.nav.arbeidsgiver.kandidat.kandidatsok.domene.es.KandidatsokTransformer;
import no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.sok.*;
import no.nav.arbeidsgiver.kandidat.kandidatsok.testsupport.ElasticSearchIntegrationTestExtension;
import no.nav.arbeidsgiver.kandidat.kandidatsok.testsupport.ElasticSearchTestConfiguration;
import no.nav.arbeidsgiver.kandidat.kandidatsok.testsupport.TestSøkKlient;
import no.nav.arbeidsgiver.kandidatsok.es.client.EsIndexerService;
import no.nav.arbeidsgiver.kandidatsok.es.client.EsSokService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.InputStreamReader;
import java.time.Instant;
import java.util.*;
import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.time.Instant.now;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static no.nav.arbeidsgiver.kandidat.kandidatsok.Stillingstittel.anleggsmaskinfører;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ElasticSearchIntegrationTestExtension.class)
public class IndexCvIT {


    private final EsSokService sokClient = ElasticSearchTestConfiguration.esSokService(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);

    final EsIndexerService indexerClient = ElasticSearchTestConfiguration.indexerCvService();

    private final ObjectMapper objectMapper = ElasticSearchTestConfiguration.objectMapper();

    private final KandidatsokTransformer kandidatsokTransformer = new KandidatsokTransformer();

    private final TestSøkKlient testSøkKlient = new TestSøkKlient();

    private static final List<no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsCv> indekserteCver = List.of(
            EsCvObjectMother.giveMeEsCv(),
            EsCvObjectMother.giveMeEsCv2(),
            EsCvObjectMother.giveMeEsCv3(),
            EsCvObjectMother.giveMeEsCv4(),
            EsCvObjectMother.giveMeEsCv5(),
            EsCvObjectMother.giveMeEsCv6(),
            EsCvObjectMother.giveMeCvForDoedPerson(),
            EsCvObjectMother.giveMeCvForKode6(),
            EsCvObjectMother.giveMeCvForKode7(),
            EsCvObjectMother.giveMeCvFritattForAgKandidatsok(),
            EsCvObjectMother.giveMeCvFritattForKandidatsok()
    );

    @BeforeEach
    public void before() {
        indexerClient.createIndex(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);
        indexerClient.bulkIndex(indekserteCver, ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);
    }

    @AfterEach
    public void after() {
        indexerClient.deleteIndex(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);
    }

    @Test
    public void skalIkkeFeileMedIllegalArgumentFraES() throws Exception {
        no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsCv cv1 = objectMapper.readValue(
                new InputStreamReader(getClass().getResourceAsStream("/utfordrende_cv1.json"),
                        ISO_8859_1),
                no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsCv.class);
        no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsCv cv2 = objectMapper.readValue(
                new InputStreamReader(getClass().getResourceAsStream("/utfordrende_cv2.json"),
                        ISO_8859_1),
                no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsCv.class);

        List<no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsCv> bulkEventer = asList(cv1, cv2);

        int antallIndeksert = indexerClient.bulkIndex(bulkEventer, ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);
        assertThat(antallIndeksert).isEqualTo(bulkEventer.size());
    }

    @Test
    @Disabled("Brukes til utforskende testing")
    public void skalLoggeFeilVedBulkIndeksereCVMedNullfelter() {
        List<no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsCv> bulkEventer =
                asList(EsCvObjectMother.giveMeEsCv(), EsCvObjectMother.giveMeEsCvMedFeil(),
                        EsCvObjectMother.giveMeEsCv2());

        bulkEventer.forEach(e -> e.setKandidatnr(e.getKandidatnr() + 9998));
        indexerClient.bulkIndex(bulkEventer, ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);
    }

    @Test
    public void skalBulkIndeksereCVerIdempotent() {
        List<no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsCv> bulkEventer =
                asList(EsCvObjectMother.giveMeEsCv(), EsCvObjectMother.giveMeEsCv2(),
                        EsCvObjectMother.giveMeEsCv3(), EsCvObjectMother.giveMeEsCv4(),
                        EsCvObjectMother.giveMeEsCv5());

        bulkEventer.forEach(e -> e.setKandidatnr(e.getKandidatnr() + 9999));

        // Bulkindekser
        var antallFørBulkIndeksering = testSøkKlient.antallKandidater(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);
        indexerClient.bulkIndex(bulkEventer, ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);
        var antallEtterBulkIndeksering = testSøkKlient.antallKandidater(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);
        assertThat(antallEtterBulkIndeksering - antallFørBulkIndeksering).isEqualTo(bulkEventer.size());

        // Bulkindekser samme eventer på nytt
        indexerClient.bulkIndex(bulkEventer, ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);
        antallEtterBulkIndeksering = testSøkKlient.antallKandidater(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);
        assertThat(antallEtterBulkIndeksering - antallFørBulkIndeksering).isEqualTo(bulkEventer.size());
    }

    @Test
    public void skalBulkSletteCVer() {
        List<String> sletteIder = asList(EsCvObjectMother.giveMeEsCv().getKandidatnr(),
                EsCvObjectMother.giveMeEsCv2().getKandidatnr());
        var kandidatnummerPåCverSomIkkeSkalSlettes = indekserteCver.stream().filter(cv ->
            !sletteIder.contains(cv.getKandidatnr())
        ).map(no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsCv::getKandidatnr).collect(toList());

        indexerClient.bulkSlettKandidatnr(sletteIder, ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);

        assertThat(sokClient.veilederHentKandidater(sletteIder).getTotaltAntallTreff()).isEqualTo(0);
        assertThat(sokClient.veilederHentKandidater(kandidatnummerPåCverSomIkkeSkalSlettes).getTotaltAntallTreff()).isEqualTo(kandidatnummerPåCverSomIkkeSkalSlettes.size());
    }

    @Test
    public void skalKunneIndeksereOppCvUtenKompetanser() {
        indexerClient.index(EsCvObjectMother.giveMeCvUtenKompetanse(), ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);
    }

    @Test
    public void hentKandidaterBevarerRekkefolge() {
        String KANDIDATNUMMER1 = "5L";
        String KANDIDATNUMMER2 = "1L";
        String KANDIDATNUMMER3 = "2L";
        List<String> kandidatnummer1 = asList(KANDIDATNUMMER1, KANDIDATNUMMER2, KANDIDATNUMMER3);
        List<String> kandidatnummer2 = asList(KANDIDATNUMMER3, KANDIDATNUMMER1, KANDIDATNUMMER2);

        Sokeresultat resultat1 = sokClient.veilederHentKandidater(kandidatnummer1);
        List<String> resultatkandidatnummer1 = resultat1.getCver().stream()
                .map(EsCv::getKandidatnr).collect(toList());
        assertThat(resultatkandidatnummer1).isEqualTo(kandidatnummer1);

        Sokeresultat resultat2 = sokClient.veilederHentKandidater(kandidatnummer2);
        List<String> resultatkandidatnummer2 = resultat2.getCver().stream()
                .map(EsCv::getKandidatnr).collect(toList());
        assertThat(resultatkandidatnummer2).isEqualTo(kandidatnummer2);
    }

    @Test
    public void hentKandidaterHandtererIkkeeksisterendeKandidatnummer() {
        String KANDIDATNUMMER1 = "5L";
        String KANDIDATNUMMER2 = "1L";
        List<String> kandidatnummer =
                asList(KANDIDATNUMMER1, "IKKEEKSISTERNDE_KANDIDATNUMMER", KANDIDATNUMMER2);

        Sokeresultat resultat = sokClient.veilederHentKandidater(kandidatnummer);
        List<String> resultatkandidatnummer = resultat.getCver().stream()
                .map(EsCv::getKandidatnr).collect(toList());
        assertThat(resultatkandidatnummer).isEqualTo(asList(KANDIDATNUMMER1, KANDIDATNUMMER2));
    }


    @Test
    public void sokPaFnrSkalGiKorrektResultat() {
        Optional<EsCv> optional = sokClient.veilederSokPaaFnr("04265983651");
        assertThat(optional).isPresent();
        assertThat(optional.get()).isEqualTo(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv3()));
    }

    @Test
    public void sokPaIkkeEksisterendeFnrSkalGiEmpty() {
        Optional<EsCv> optional = sokClient.veilederSokPaaFnr("04265983622");
        assertThat(optional).isNotPresent();
    }

    @Test
    public void sokeResultaterSkalInkludereFelterSomIkkeHarAnnotasjon() {
        assertThat(sokClient.veilederHent("2L").get().getKompetanseObj().get(0).getKompKode()).isEqualTo("265478");
    }

    @Test
    public void veilederHentEnKandidat() {
        Optional<no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsCv> esCv = sokClient.veilederHent("2L");
        assertThat(esCv.get().getKandidatnr()).isEqualTo("2L");
    }

    @Test
    public void arbeidsgiverHentEnKandidat() {
        Optional<no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsCv> esCv = sokClient.veilederHent("1L");
        assertThat(esCv.get().getKandidatnr()).isEqualTo("1L");
    }

    @Test
    public void arbeidsgiverHentEnKandidatSomIkkeFinnes() {
        Optional<no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsCv> esCv = sokClient.veilederHent("finnes-ikke");
        assertThat(esCv).isNotPresent();
    }





    private static boolean harStillingstittelAnleggsmaskinfører(EsCv cv) {
        return cv.getYrkeserfaring().stream().anyMatch(y -> anleggsmaskinfører.equals(y.getStillingstittel()));
    }

    private static Instant tildato(EsYrkeserfaring yrkeserfaring) {
        Calendar c = Calendar.getInstance();
        c.setTime(yrkeserfaring.getFraDato());
        c.add(Calendar.MONTH, yrkeserfaring.getYrkeserfaringManeder());
        return c.toInstant();
    }

    private static Instant minus(Instant instant, int antallÅr) {
        Calendar c = Calendar.getInstance();
        c.setTime(Date.from(instant));
        c.add(Calendar.YEAR, -antallÅr);
        return c.toInstant();
    }

    private static boolean yrkeserfaringAnleggsmaskinførerErNyereEnn10År(EsCv cv) {
        return cv.getYrkeserfaring().stream().filter(y -> anleggsmaskinfører.equals(y.getStillingstittel())).anyMatch(y -> tildato(y).isAfter(minus(now(), 10)));
    }
}
