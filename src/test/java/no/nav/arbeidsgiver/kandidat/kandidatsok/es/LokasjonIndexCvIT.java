package no.nav.arbeidsgiver.kandidat.kandidatsok.es;

import no.nav.arbeidsgiver.kandidat.kandidatsok.domene.es.EsCvObjectMother;
import no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.sok.EsCv;
import no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.sok.Sokekriterier;
import no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.sok.SokekriterierVeiledere;
import no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.sok.Sokeresultat;
import no.nav.arbeidsgiver.kandidat.kandidatsok.testsupport.ElasticSearchIntegrationTestExtension;
import no.nav.arbeidsgiver.kandidat.kandidatsok.testsupport.ElasticSearchTestConfiguration;
import no.nav.arbeidsgiver.kandidatsok.es.client.EsIndexerService;
import no.nav.arbeidsgiver.kandidatsok.es.client.EsSokService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ElasticSearchIntegrationTestExtension.class)
public class LokasjonIndexCvIT {

    private EsSokService sokClient = ElasticSearchTestConfiguration.esSokService(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);

    private EsIndexerService indexerClient = ElasticSearchTestConfiguration.indexerCvService();

    @BeforeEach
    public void before() {
        indexerClient.createIndex(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);

        indexerClient.bulkIndex(List.of(
                EsCvObjectMother.giveMeEsCv(), // Lier, Viken
                EsCvObjectMother.giveMeEsCv2(), // Lier, Viken
                EsCvObjectMother.giveMeEsCv3(), // Drammen, Viken
                EsCvObjectMother.giveMeEsCv4(), // Oslo, Oslo
                EsCvObjectMother.giveMeEsCv5() // null, null
        ), ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);
    }

    @AfterEach
    public void after() {
        indexerClient.deleteIndex(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);
    }


    @Test
    public void testUtenSokekriterierReturnererAlleKommuner() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med().bygg());

        List<EsCv> cver = sokeresultat.getCver();
        assertThat(cver.size()).isEqualTo(5);

        List<String> kommuneNavn = cver.stream().map(EsCv::getKommuneNavn).collect(Collectors.toList());
        assertThat(kommuneNavn).containsExactlyInAnyOrder("Lier", "Lier", "Drammen", "Oslo", null);
    }

    @Test
    public void testUtenSokekriterierReturnererAlleFylker() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med().bygg());

        List<EsCv> cver = sokeresultat.getCver();
        assertThat(cver.size()).isEqualTo(5);

        List<String> fylkeNavn = cver.stream().map(EsCv::getFylkeNavn).collect(Collectors.toList());
        assertThat(fylkeNavn).containsExactlyInAnyOrder("Viken", "Viken", "Viken", "Oslo", null);
    }


}
