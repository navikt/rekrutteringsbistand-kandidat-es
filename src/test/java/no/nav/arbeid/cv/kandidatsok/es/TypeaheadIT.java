package no.nav.arbeid.cv.kandidatsok.es;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ExtendWith(ElasticSearchIntegrationTestExtension.class)
public class TypeaheadIT {

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

    @BeforeEach
    public void before() {
        indexerClient.createIndex("cvindex");

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
        ), "cvindex");
    }

    @AfterEach
    public void after() {
        indexerClient.deleteIndex("cvindex");
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
