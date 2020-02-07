package no.nav.arbeid.cv.kandidatsok.es;

import no.nav.arbeid.cv.kandidatsok.domene.es.EsCvObjectMother;
import no.nav.arbeid.cv.kandidatsok.domene.es.KandidatsokTransformer;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.SokekriterierVeiledere;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.Sokeresultat;
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
public class AliasIndexingIT {

    private EsSokService sokClient = ElasticSearchTestConfiguration.esSokService(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);

    private EsIndexerService indexerClient = ElasticSearchTestConfiguration.indexerCvService();

    private KandidatsokTransformer kandidatsokTransformer = new KandidatsokTransformer();

    @BeforeEach
    public void before() {
        indexerClient.createIndex("cv_4.1.21");
        indexerClient.updateIndexAlias(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME, "cv_4.1.21");

        indexerAlleCVene("cv_4.1.21");
    }

    @AfterEach
    public void after() {
        try {
            indexerClient.deleteIndex("cv_4.1.21");
            indexerClient.deleteIndex("cv_4.1.22");
        } catch (Exception e) {
            // Ignore
        }
    }

    private void indexerAlleCVene(String indexName) {
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
        ), indexName);
    }


    @Test
    public void storStyggTest() {

        //sokMotAliasFungerer() {
        assertThat(indexerClient.getTargetsForAlias(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME)).containsExactly("cv_4.1.21");
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med().fritekst("Awesome").bygg());
        assertThat(sokeresultat.getCver()).hasSize(1);
        assertThat(sokeresultat.getCver()).containsExactly(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv2()));

        //indexeringMotAliasFungerer() {
        indexerAlleCVene(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);
        Sokeresultat sokeresultat2 = sokClient.veilederSok(SokekriterierVeiledere.med().fritekst("Awesome").bygg());
        assertThat(sokeresultat2.getCver()).hasSize(1);
        assertThat(sokeresultat2.getCver()).containsExactly(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv2()));

        //indexSwitchingFungerer() {
        indexerClient.createIndex("cv_4.1.22");
        indexerClient.updateIndexAlias(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME, "cv_4.1.22");
        Sokeresultat sokeresultat3 = sokClient.veilederSok(SokekriterierVeiledere.med().fritekst("Awesome").bygg());
        assertThat(sokeresultat3.getCver()).hasSize(0);

        indexerAlleCVene("cv_4.1.22");
        Sokeresultat sokeresultat4 = sokClient.veilederSok(SokekriterierVeiledere.med().fritekst("Awesome").bygg());
        assertThat(sokeresultat4.getCver()).hasSize(1);
        assertThat(sokeresultat4.getCver()).containsExactly(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv2()));
    }


}
