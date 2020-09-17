package no.nav.arbeidsgiver.kandidat.indexer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.MeterRegistry;
import no.nav.arbeidsgiver.kandidatsok.es.client.EsIndexerHttpService;
import no.nav.arbeidsgiver.kandidatsok.es.client.EsIndexerService;
import no.nav.arbeidsgiver.kandidatsok.es.client.EsSokHttpService;
import no.nav.arbeidsgiver.kandidatsok.es.client.EsSokService;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RestHighLevelClient;

public class EsServiceConfig {

    private ObjectMapper objectMapper;
    private MeterRegistry meterRegistry;
    private RestHighLevelClient restHighLevelClient;
    private final int numberOfShards;
    private final int numberOfReplicas;

    public EsServiceConfig(RestHighLevelClient restHighLevelClient, ObjectMapper objectMapper,
                           MeterRegistry meterRegistry, int numberOfShards, int numberOfReplicas) {
        this.restHighLevelClient = restHighLevelClient;
        this.objectMapper = objectMapper;
        this.meterRegistry = meterRegistry;
        this.numberOfShards = numberOfShards;
        this.numberOfReplicas = numberOfReplicas;
    }

    public EsSokService sokCvClient(String indexName) {
        return new EsSokHttpService(restHighLevelClient, objectMapper, indexName);
    }

    public EsIndexerService indexerCvService() {
        return new EsIndexerHttpService(restHighLevelClient, objectMapper, meterRegistry,
                WriteRequest.RefreshPolicy.NONE, numberOfShards, numberOfReplicas);
    }

}

