package no.nav.arbeid.cv.kandidatsok.es;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import no.nav.arbeid.cv.kandidatsok.testsupport.ElasticSearchTestExtension;
import no.nav.arbeid.kandidatsok.es.client.EsIndexerHttpService;
import no.nav.arbeid.kandidatsok.es.client.EsIndexerService;
import no.nav.arbeid.kandidatsok.es.client.EsSokHttpService;
import no.nav.arbeid.kandidatsok.es.client.EsSokService;
import org.apache.http.HttpHost;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchTestConfiguration {

    static final String DEFAULT_INDEX_NAME = "cvindex_current";

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost",
                        ElasticSearchTestExtension.getEsPort(), "http")));
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public MeterRegistry meterRegistry() {
//        Counter counter = mock(Counter.class);
//        MeterRegistry meterRegistry = mock(MeterRegistry.class);
//        when(meterRegistry.counter(anyString(), any(Tags.class))).thenReturn(counter);
//
//        return meterRegistry;
        return new SimpleMeterRegistry();
    }

    @Bean
    public EsIndexerService indexerCvService(RestHighLevelClient restHighLevelClient,
                                             ObjectMapper objectMapper,
                                             MeterRegistry meterRegistry
    ) {
        return new EsIndexerHttpService(restHighLevelClient, objectMapper, meterRegistry,
                WriteRequest.RefreshPolicy.IMMEDIATE, 1, 1);
    }

    @Bean
    public EsSokService esSokService(RestHighLevelClient restHighLevelClient, ObjectMapper objectMapper) {
        return new EsSokHttpService(restHighLevelClient, objectMapper, DEFAULT_INDEX_NAME);
    }

}
