package no.nav.arbeid.cv.kandidatsok.es;

import no.nav.arbeid.cv.kandidatsok.domene.es.EsCvObjectMother;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.EsCv;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.Sokekriterier;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.Sokeresultat;
import no.nav.arbeid.cv.kandidatsok.testsupport.ElasticSearchIntegrationTestExtension;
import no.nav.arbeid.cv.kandidatsok.testsupport.ElasticSearchTestConfiguration;
import no.nav.arbeid.kandidatsok.es.client.EsIndexerService;
import no.nav.arbeid.kandidatsok.es.client.EsSokService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.stream.Collectors;

import static no.nav.arbeid.cv.kandidatsok.testsupport.ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ElasticSearchIntegrationTestExtension.class)
public class LokasjonIndexCvIT {

    private EsSokService sokClient = ElasticSearchTestConfiguration.esSokService(DEFAULT_INDEX_NAME);

    private EsIndexerService indexerClient = ElasticSearchTestConfiguration.indexerCvService();

    @BeforeEach
    public void before() {
        indexerClient.createIndex(DEFAULT_INDEX_NAME);

        indexerClient.bulkIndex(List.of(
                EsCvObjectMother.giveMeEsCv(), // Lier, Viken
                EsCvObjectMother.giveMeEsCv2(), // Lier, Viken
                EsCvObjectMother.giveMeEsCv3(), // Drammen, Viken
                EsCvObjectMother.giveMeEsCv4(), // Oslo, Oslo
                EsCvObjectMother.giveMeEsCv5() // null, null
        ), DEFAULT_INDEX_NAME);
    }

    @AfterEach
    public void after() {
        indexerClient.deleteIndex(DEFAULT_INDEX_NAME);
    }


    @Test
    public void testUtenSokekriterierReturnererAlleKommuner() {
        Sokeresultat sokeresultat = sokClient.arbeidsgiverSok(Sokekriterier.med().bygg());

        List<EsCv> cver = sokeresultat.getCver();
        assertThat(cver.size()).isEqualTo(5);

        List<String> kommuneNavn = cver.stream().map(EsCv::getKommuneNavn).collect(Collectors.toList());
        assertThat(kommuneNavn).containsExactlyInAnyOrder("Lier", "Lier", "Drammen", "Oslo", null);
    }

    @Test
    public void testUtenSokekriterierReturnererAlleFylker() {
        Sokeresultat sokeresultat = sokClient.arbeidsgiverSok(Sokekriterier.med().bygg());

        List<EsCv> cver = sokeresultat.getCver();
        assertThat(cver.size()).isEqualTo(5);

        List<String> fylkeNavn = cver.stream().map(EsCv::getFylkeNavn).collect(Collectors.toList());
        assertThat(fylkeNavn).containsExactlyInAnyOrder("Viken", "Viken", "Viken", "Oslo", null);
    }


}
