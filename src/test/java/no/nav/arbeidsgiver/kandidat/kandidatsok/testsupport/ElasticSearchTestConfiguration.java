package no.nav.arbeidsgiver.kandidat.kandidatsok.testsupport;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import no.nav.arbeidsgiver.kandidatsok.es.client.EsIndexerHttpService;
import no.nav.arbeidsgiver.kandidatsok.es.client.EsIndexerService;
import no.nav.arbeidsgiver.kandidatsok.es.client.EsSokHttpService;
import no.nav.arbeidsgiver.kandidatsok.es.client.EsSokService;
import org.apache.http.HttpHost;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * Provides dependencies for running ITs on a live ES instance.
 */
public class ElasticSearchTestConfiguration {

    public static final String DEFAULT_INDEX_NAME = "cvindex_current";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final MeterRegistry METER_REGISTRY = new SimpleMeterRegistry();

    private static final RestHighLevelClient REST_HIGH_LEVEL_CLIENT = new RestHighLevelClient(RestClient.builder(
            new HttpHost("localhost", ElasticSearchIntegrationTestExtension.getEsPort(), "http")));

    public static RestHighLevelClient restHighLevelClient() {
        return REST_HIGH_LEVEL_CLIENT;
    }

    public static ObjectMapper objectMapper() {
        return OBJECT_MAPPER;
    }

    public static MeterRegistry meterRegistry() {
        return METER_REGISTRY;
    }

    public static EsIndexerService indexerCvService() {
        return new EsIndexerHttpService(REST_HIGH_LEVEL_CLIENT, objectMapper(), meterRegistry(),
                WriteRequest.RefreshPolicy.IMMEDIATE, 1, 1);
    }

    public static EsSokService esSokService(String indexName) {
        return new EsSokHttpService(REST_HIGH_LEVEL_CLIENT, OBJECT_MAPPER, indexName);
    }

}
