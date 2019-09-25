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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class TypeaheadTest {

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
            return new EsSokHttpService(restHighLevelClient, objectMapper, "cvindex");
        }
    }

    @Before
    public void before() throws IOException {
        try {
            indexerClient.deleteIndex("cvindex");
        } catch (Exception e) {
            // Ignore
        }

        indexerClient.createIndex("cvindex");

        indexerClient.index(EsCvObjectMother.giveMeEsCv(), "cvindex");
        indexerClient.index(EsCvObjectMother.giveMeEsCv2(), "cvindex");
        indexerClient.index(EsCvObjectMother.giveMeEsCv3(), "cvindex");
        indexerClient.index(EsCvObjectMother.giveMeEsCv4(), "cvindex");
        indexerClient.index(EsCvObjectMother.giveMeEsCv5(), "cvindex");
        indexerClient.index(EsCvObjectMother.giveMeEsCv6(), "cvindex");
        indexerClient.index(EsCvObjectMother.giveMeCvForDoedPerson(), "cvindex");
        indexerClient.index(EsCvObjectMother.giveMeCvForKode6(), "cvindex");
        indexerClient.index(EsCvObjectMother.giveMeCvForKode7(), "cvindex");
        indexerClient.index(EsCvObjectMother.giveMeCvFritattForAgKandidatsok(), "cvindex");
        indexerClient.index(EsCvObjectMother.giveMeCvFritattForKandidatsok(), "cvindex");
    }

    @After
    public void after() throws IOException {
        indexerClient.deleteIndex("cvindex");
    }

    @Test
    public void typeAheadArbeidserfaring() throws IOException {
        List<String> liste = sokClient.typeAheadYrkeserfaring("Butikk");
        assertThat(liste.size()).isEqualTo(5);
        assertThat(liste).containsExactly("Butikkmedarbeider",
                "Butikkmedarbeider(dagligvarer)",
                "Butikkmedarbeider(elektronikk)",
                "Butikkmedarbeider(klesbutikk)",
                "Butikkmedarbeider(trevare)");
    }

    @Test
    public void typeAheadKompetanse() throws IOException {
        List<String> liste = sokClient.typeAheadKompetanse("Nyhet");
        assertThat(liste.size()).isEqualTo(1);
        assertThat(liste).contains("Nyhetsanker");
    }

    @Test
    public void typeAheadGeografi() throws IOException {
        List<String> liste = sokClient.typeAheadGeografi("Bær");
        assertThat(liste.size()).isEqualTo(1);
        assertThat(liste).contains("{\"geografiKodeTekst\":\"Bærum\",\"geografiKode\":\"NO02.1219\"}");
    }

}
