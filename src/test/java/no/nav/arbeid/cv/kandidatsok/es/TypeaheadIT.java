package no.nav.arbeid.cv.kandidatsok.es;

import no.nav.arbeid.cv.kandidatsok.domene.es.EsCvObjectMother;
import no.nav.arbeid.cv.kandidatsok.testsupport.ElasticSearchTestConfiguration;
import no.nav.arbeid.cv.kandidatsok.testsupport.ElasticSearchIntegrationTestExtension;
import no.nav.arbeid.kandidatsok.es.client.EsIndexerService;
import no.nav.arbeid.kandidatsok.es.client.EsSokService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ElasticSearchIntegrationTestExtension.class)
public class TypeaheadIT {

    private EsSokService sokClient = ElasticSearchTestConfiguration.esSokService(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);

    private EsIndexerService indexerClient = ElasticSearchTestConfiguration.indexerCvService();

    @BeforeEach
    public void before() {
        indexerClient.createIndex(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);

        indexerClient.bulkIndex(List.of(
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
        ), ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);
    }

    @AfterEach
    public void after() {
        indexerClient.deleteIndex(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);
    }

    @Test
    public void typeAheadArbeidserfaring() {
        List<String> liste = sokClient.typeAheadYrkeserfaring("Butikk");
        assertThat(liste.size()).isEqualTo(5);
        assertThat(liste).containsExactly("Butikkmedarbeider",
                "Butikkmedarbeider(dagligvarer)",
                "Butikkmedarbeider(elektronikk)",
                "Butikkmedarbeider(klesbutikk)",
                "Butikkmedarbeider(trevare)");
    }

    @Test
    public void typeAheadKompetanse() {
        List<String> liste = sokClient.typeAheadKompetanse("Nyhet");
        assertThat(liste.size()).isEqualTo(1);
        assertThat(liste).contains("Nyhetsanker");
    }

    @Test
    public void typeAheadGeografi() {
        List<String> liste = sokClient.typeAheadGeografi("Bær");
        assertThat(liste.size()).isEqualTo(1);
        assertThat(liste).contains("{\"geografiKodeTekst\":\"Bærum\",\"geografiKode\":\"NO02.1219\"}");
    }

}
