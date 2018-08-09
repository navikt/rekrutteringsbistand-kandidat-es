package no.nav.arbeid.kandidatsok.es.client;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.http.util.EntityUtils;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseException;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import no.nav.arbeid.cv.kandidatsok.es.domene.EsCv;
import no.nav.arbeid.cv.kandidatsok.es.exception.ApplicationException;
import no.nav.elasticsearch.mapping.MappingBuilder;
import no.nav.elasticsearch.mapping.MappingBuilderImpl;
import no.nav.elasticsearch.mapping.ObjectMapping;

public class EsIndexerHttpService implements EsIndexerService {

    private static final String CV_INDEX = "cvindex";
    private static final String CV_TYPE = "cvtype";

    private static final Logger LOGGER = LoggerFactory.getLogger(EsIndexerHttpService.class);

    private final RestHighLevelClient client;
    private final ObjectMapper mapper;
    private final MeterRegistry meterRegistry;

    public EsIndexerHttpService(RestHighLevelClient client, ObjectMapper objectMapper,
            MeterRegistry meterRegistry) {
        this.client = client;
        this.mapper = objectMapper;
        this.meterRegistry = meterRegistry;
    }

    @Override
    public void createIndex() throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(CV_INDEX);

        MappingBuilder mappingBuilder = new MappingBuilderImpl();
        ObjectMapping mapping = mappingBuilder.build(EsCv.class);

        // String jsonMapping = mapping.getContentAsString();
        XContentBuilder contentBuilder = mapping.getContent();
        String jsonMapping = contentBuilder.string();
        LOGGER.debug("MAPPING: " + jsonMapping);
        createIndexRequest.mapping(CV_TYPE, jsonMapping, XContentType.JSON);

        CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest);
        LOGGER.debug("CREATEINDEXRESPONSE: " + createIndexResponse);
    }

    @Override
    public void deleteIndex() throws IOException {
        DeleteIndexRequest deleteRequest = new DeleteIndexRequest(CV_INDEX);
        DeleteIndexResponse deleteIndexResponse = client.indices().delete(deleteRequest);
        LOGGER.debug("DELETERESPONSE: " + deleteIndexResponse.toString());
    }

    @Override
    public void index(EsCv esCv) throws IOException {
        String jsonString = mapper.writeValueAsString(esCv);
        LOGGER.debug("DOKUMENTET: " + jsonString);

        IndexRequest request =
                new IndexRequest(CV_INDEX, CV_TYPE, Long.toString(esCv.getArenaPersonId()));
        request.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        request.source(jsonString, XContentType.JSON);
        IndexResponse indexResponse = esExec(() -> client.index(request));
        LOGGER.debug("INDEXRESPONSE: " + indexResponse.toString());
    }

    @Override
    public void bulkIndex(List<EsCv> esCver) throws IOException {
        BulkRequest bulkRequest = Requests.bulkRequest();
        Long currentArenaId = 0l;
        try {
            for (EsCv esCv : esCver) {
                currentArenaId = esCv.getArenaPersonId();
                String jsonString = mapper.writeValueAsString(esCv);
                IndexRequest ir = Requests.indexRequest().source(jsonString, XContentType.JSON)
                        .index(CV_INDEX).type(CV_TYPE).id(Long.toString(esCv.getArenaPersonId()));

                bulkRequest.add(ir);
            }
        } catch (Exception e) {
            LOGGER.info(
                    "Greide ikke å serialisere CV til JSON for å bygge opp bulk-indekseringsrequest: {}. ArenaId: {}",
                    e.getMessage(), currentArenaId, e);
            throw new ApplicationException(
                    "Greide ikke å serialisere CV til JSON for å bygge opp bulk-indekseringsrequest. ArenaId: "
                            + currentArenaId,
                    e);
        }

        LOGGER.info("Sender bulk indexrequest med {} cv'er", esCver.size());
        bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        BulkResponse bulkResponse = esExec(() -> client.bulk(bulkRequest));
        if (bulkResponse.hasFailures()) {
            long antallFeil = 0;
            for (BulkItemResponse bir : bulkResponse.getItems()) {
                antallFeil += bir.isFailed() ? 1 : 0;
                try {
                    if (bir.getFailure() != null) {
                        Optional<EsCv> cvMedFeil = esCver.stream().filter(
                                esCv -> ("" + esCv.getArenaPersonId()).trim().equals(bir.getFailure().getId()))
                                .findFirst();

                        LOGGER.warn("Feilet ved indeksering av CV {}: " + bir.getFailure().getMessage(),
                                cvMedFeil.isPresent() && LOGGER.isTraceEnabled() ? mapper.writeValueAsString(cvMedFeil.get()) : "",
                                bir.getFailure().getCause());
                    }
                } catch (Exception e) {
                    LOGGER.warn("Feilet ved parsing av bulkitemresponse..", e);
                    meterRegistry.counter("cv.es.index.feil", Tags.of("type", "infrastruktur"))
                            .increment();
                }
                meterRegistry.counter("cv.es.index.feil", Tags.of("type", "applikasjon"))
                        .increment(antallFeil);
            }
            LOGGER.warn(
                    "Feilet under indeksering av CVer: " + bulkResponse.buildFailureMessage());
        }
        LOGGER.debug("BULKINDEX tidsbruk: " + bulkResponse.getTook());
    }

    @Override
    public void bulkSlett(List<Long> arenapersoner) throws IOException {
        BulkRequest bulkRequest = Requests.bulkRequest();

        for (Long id : arenapersoner) {
            DeleteRequest dr = Requests.deleteRequest(CV_INDEX).id(Long.toString(id)).type(CV_TYPE);

            bulkRequest.add(dr);
        }

        LOGGER.info("Sender bulksletting av {} cv'er", arenapersoner.size());
        bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        BulkResponse bulkResponse = esExec(() -> client.bulk(bulkRequest));
        if (bulkResponse.hasFailures()) {
            LOGGER.warn("Feilet under sletting av CVer: " + bulkResponse.buildFailureMessage());
            long antallFeil =
                    Arrays.stream(bulkResponse.getItems()).filter(i -> i.isFailed()).count();
            meterRegistry.counter("cv.es.slett.feil", Tags.of("type", "infrastruktur"))
                    .increment(antallFeil);
        }
        LOGGER.debug("BULKDELETERESPONSE: " + bulkResponse.toString());
    }

    @Override
    public boolean doesIndexExist() throws IOException {
        try {
            Response restResponse =
                    client.getLowLevelClient().performRequest("HEAD", "/" + CV_INDEX);
            return restResponse.getStatusLine().getStatusCode() == 200;
        } catch (ResponseException e) {
            LOGGER.info("Exception while calling isExistingIndex", e);
        }
        return false;
    }

    /**
     * Pakk inn kall mot elastic search i sjekk på om index finnes. Hvis index ikke finnes så
     * opprettes den, og kalles forsøkes på nytt
     *
     * @param fun
     * @param <T>
     * @return
     * @throws IOException
     */
    private <T> T esExec(IOSupplier<T> fun) throws IOException {
        try {
            return fun.get();
        } catch (ElasticsearchStatusException e) {
            if (e.status().getStatus() == 404
                    && e.getMessage().contains("index_not_found_exception")) {
                LOGGER.info(
                        "Greide ikke å utfore operasjon mot elastic search. Prøver å opprette index og forsøke på nytt.");
                createIndex();
                return fun.get();
            }
            throw (e);
        }
    }

    /** Tilsvarer java.functions.Supplier bare at get metoden kan kaste IOException */
    private interface IOSupplier<T> {
        T get() throws IOException;
    }

    @Override
    public long antallIndeksert() {
        long antallIndeksert = 0;
        try {
            Response response = client.getLowLevelClient().performRequest("GET",
                    String.format("/%s/%s/_count", CV_INDEX, CV_TYPE));
            if (response != null && response.getStatusLine().getStatusCode() >= 200
                    && response.getStatusLine().getStatusCode() < 300) {
                String json = EntityUtils.toString(response.getEntity());
                JsonNode countNode = mapper.readTree(json).path(("count"));
                antallIndeksert = countNode != null ? countNode.asLong() : 0;
            } else {
                LOGGER.warn("Greide ikke å hente ut antall dokumenter det i ES indeksen: {} : {}",
                        response.getStatusLine().getStatusCode(),
                        response.getStatusLine().getReasonPhrase());
            }
        } catch (Exception e) {
            LOGGER.warn("Greide ikke å hente ut antall dokumenter i ES indeksen: {}",
                    e.getMessage(), e);
        }
        return antallIndeksert;
    }
}
