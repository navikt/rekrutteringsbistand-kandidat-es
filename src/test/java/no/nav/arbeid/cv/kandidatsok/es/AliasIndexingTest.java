package no.nav.arbeid.cv.kandidatsok.es;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.configuration.ShutdownStrategy;
import com.palantir.docker.compose.connection.DockerMachine;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import no.nav.arbeid.cv.kandidatsok.domene.es.EsCvObjectMother;
import no.nav.arbeid.cv.kandidatsok.domene.es.KandidatsokTransformer;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.SokekriterierVeiledere;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.Sokeresultat;
import no.nav.arbeid.kandidatsok.es.client.EsIndexerHttpService;
import no.nav.arbeid.kandidatsok.es.client.EsIndexerService;
import no.nav.arbeid.kandidatsok.es.client.EsSokHttpService;
import no.nav.arbeid.kandidatsok.es.client.EsSokService;
import org.apache.http.HttpHost;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class AliasIndexingTest {

    /*
     * For å kunne kjøre denne testen må Linux rekonfigureres litt.. Lag en fil i
     * /etc/sysctl.d/01-increase_vm_max_map_count.conf som inneholder følgende: vm.max_map_count =
     * 262144
     */

    // Kjører "docker-compose up" manuelt istedenfor denne ClassRule:

    @ClassRule
    public static DockerComposeRule docker =
            DockerComposeRule.builder().file("src/test/resources/docker-compose-kun-es.yml")
                    .machine(DockerMachine.localMachine()
                            .withAdditionalEnvironmentVariable("ES_PORT",
                                    System.getProperty("ES_PORT"))
                            .build())
                    .shutdownStrategy(ShutdownStrategy.KILL_DOWN).build();

    @Autowired
    private EsSokService sokClient;

    @Autowired
    private EsIndexerService indexerClient;

    @Autowired
    private ObjectMapper objectMapper;

    private KandidatsokTransformer kandidatsokTransformer = new KandidatsokTransformer();

    @Configuration
    static class TestConfig {

        @Bean
        @Autowired
        public RestHighLevelClient restHighLevelClient(@Value("${ES_PORT}") Integer port) {
            return new RestHighLevelClient(
                    RestClient.builder(new HttpHost("localhost", port, "http")));
        }

        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }

        @Bean
        public MeterRegistry meterRegistry() {
            Counter counter = mock(Counter.class);
            MeterRegistry meterRegistry = mock(MeterRegistry.class);
            when(meterRegistry.counter(anyString(), any(Tags.class))).thenReturn(counter);

            return meterRegistry;
        }

        @Bean
        public EsIndexerService indexerCvService(RestHighLevelClient restHighLevelClient,
                                                 ObjectMapper objectMapper,
                                                 MeterRegistry meterRegistry
        ) {
            return new EsIndexerHttpService(restHighLevelClient, objectMapper, meterRegistry,
                    WriteRequest.RefreshPolicy.IMMEDIATE, 3, 2);
        }

        @Bean
        public EsSokService esSokService(RestHighLevelClient restHighLevelClient, ObjectMapper objectMapper) {
            return new EsSokHttpService(restHighLevelClient, objectMapper, "current_cv");
        }
    }

    @Before
    public void before() throws IOException {
        try {
            indexerClient.deleteIndex("cv_4.1.21");
            indexerClient.deleteIndex("cv_4.1.21");
        } catch (Exception e) {
            // Ignore
        }

        indexerClient.createIndex("cv_4.1.21");
        indexerClient.updateIndexAlias("current_cv", "cv_4.1.21");

        indexerAlleCVene("cv_4.1.21");
    }

    private void indexerAlleCVene(String indexName) {
        indexerClient.index(EsCvObjectMother.giveMeEsCv(), indexName);
        indexerClient.index(EsCvObjectMother.giveMeEsCv2(), indexName);
        indexerClient.index(EsCvObjectMother.giveMeEsCv3(), indexName);
        indexerClient.index(EsCvObjectMother.giveMeEsCv4(), indexName);
        indexerClient.index(EsCvObjectMother.giveMeEsCv5(), indexName);
        indexerClient.index(EsCvObjectMother.giveMeEsCv6(), indexName);
        indexerClient.index(EsCvObjectMother.giveMeCvForDoedPerson(), indexName);
        indexerClient.index(EsCvObjectMother.giveMeCvForKode6(), indexName);
        indexerClient.index(EsCvObjectMother.giveMeCvForKode7(), indexName);
        indexerClient.index(EsCvObjectMother.giveMeCvFritattForAgKandidatsok(), indexName);
        indexerClient.index(EsCvObjectMother.giveMeCvFritattForKandidatsok(), indexName);
    }


    @Test
    public void storStyggTest() {

        //sokMotAliasFungerer() {
        assertThat(indexerClient.getTargetsForAlias("current_cv")).containsExactly("cv_4.1.21");
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med().fritekst("Awesome").bygg());
        assertThat(sokeresultat.getCver()).hasSize(1);
        assertThat(sokeresultat.getCver()).containsExactly(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv2()));

        //indexeringMotAliasFungerer() {
        indexerAlleCVene("current_cv");
        Sokeresultat sokeresultat2 = sokClient.veilederSok(SokekriterierVeiledere.med().fritekst("Awesome").bygg());
        assertThat(sokeresultat2.getCver()).hasSize(1);
        assertThat(sokeresultat2.getCver()).containsExactly(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv2()));

        //indexSwitchingFungerer() {
        indexerClient.createIndex("cv_4.1.22");
        indexerClient.updateIndexAlias("current_cv", "cv_4.1.22");
        Sokeresultat sokeresultat3 = sokClient.veilederSok(SokekriterierVeiledere.med().fritekst("Awesome").bygg());
        assertThat(sokeresultat3.getCver()).hasSize(0);

        indexerAlleCVene("cv_4.1.22");
        Sokeresultat sokeresultat4 = sokClient.veilederSok(SokekriterierVeiledere.med().fritekst("Awesome").bygg());
        assertThat(sokeresultat4.getCver()).hasSize(1);
        assertThat(sokeresultat4.getCver()).containsExactly(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv2()));
    }

    @After
    public void after() throws IOException {
        try {
            indexerClient.deleteIndex("cv_4.1.21");
            indexerClient.deleteIndex("cv_4.1.22");
        } catch (Exception e) {
            // Ignore
        }
    }


}
