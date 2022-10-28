package no.nav.arbeidsgiver.kandidat.kandidatsok.es;

import no.nav.arbeidsgiver.kandidat.kandidatsok.domene.es.EsCvObjectMother;
import no.nav.arbeidsgiver.kandidat.kandidatsok.domene.es.KandidatsokTransformer;
import no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsCv;
import no.nav.arbeidsgiver.kandidat.kandidatsok.testsupport.ElasticSearchTestConfiguration;
import no.nav.arbeidsgiver.kandidat.kandidatsok.testsupport.ElasticSearchIntegrationTestExtension;
import no.nav.arbeidsgiver.kandidatsok.es.client.EsIndexerService;
import no.nav.arbeidsgiver.kandidatsok.es.client.EsSokService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ElasticSearchIntegrationTestExtension.class)
public class AliasIndexingIT {

    private EsSokService sokClient = ElasticSearchTestConfiguration.esSokService(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);

    private EsIndexerService indexerClient = ElasticSearchTestConfiguration.indexerCvService();

    private KandidatsokTransformer kandidatsokTransformer = new KandidatsokTransformer();
    private List<EsCv> alleIndekserteCver = List.of(
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
            EsCvObjectMother.giveMeCvFritattForKandidatsok());


    @BeforeEach
    public void before() {
        indexerClient.createIndex("cvindex_4.1.21");
        indexerClient.updateIndexAlias(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME, "cvindex*", "cvindex_4.1.21");

        indexerAlleCVene("cvindex_4.1.21");
    }

    @AfterEach
    public void after() {
        try {
            indexerClient.deleteIndex("cvindex_4.1.21");
            indexerClient.deleteIndex("cvindex_4.1.22");
        } catch (Exception e) {
            // Ignore
        }
    }

    private void indexerAlleCVene(String indexName) {
        indexerClient.bulkIndex(alleIndekserteCver, indexName);
    }

    @Test
    public void storStyggTest() {
        //indexeringMotAliasFungerer() {
        indexerAlleCVene(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);
        Optional<EsCv> enEsCv = sokClient.veilederHent(alleIndekserteCver.get(0).getKandidatnr());
        assertThat(enEsCv).isPresent();

        //indexSwitchingFungerer() {
        indexerClient.createIndex("cvindex_4.1.22");
        indexerClient.updateIndexAlias(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME, "cvindex*", "cvindex_4.1.22");
        Optional<EsCv> enAnnenCv = sokClient.veilederHent(alleIndekserteCver.get(1).getKandidatnr());
        assertThat(enAnnenCv).isEmpty();

        indexerAlleCVene("cvindex_4.1.22");
        Optional<EsCv> enTredjeCv = sokClient.veilederHent(alleIndekserteCver.get(2).getKandidatnr());
        assertThat(enTredjeCv).isPresent();
    }
}
