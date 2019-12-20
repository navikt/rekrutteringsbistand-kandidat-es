package no.nav.arbeid.kandidatsok.es.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import no.nav.arbeid.cv.kandidatsok.es.domene.EsCv;
import no.nav.arbeid.cv.kandidatsok.es.exception.ApplicationException;
import no.nav.arbeid.cv.kandidatsok.es.exception.OperationalException;
import no.nav.elasticsearch.mapping.MappingBuilder;
import no.nav.elasticsearch.mapping.MappingBuilderImpl;
import no.nav.elasticsearch.mapping.ObjectMapping;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
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
import org.elasticsearch.client.*;
import org.elasticsearch.common.xcontent.*;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.DeleteByQueryRequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class EsIndexerHttpService implements EsIndexerService, AutoCloseable {

    private static final String CV_TYPE = "cvtype";

    private static final Logger LOGGER = LoggerFactory.getLogger(EsIndexerHttpService.class);

    private final RestHighLevelClient client;
    private final ObjectMapper mapper;
    private final MeterRegistry meterRegistry;
    private final int numberOfShards;
    private final int numberOfReplicas;

    private WriteRequest.RefreshPolicy refreshPolicy = WriteRequest.RefreshPolicy.NONE;

    public EsIndexerHttpService(RestHighLevelClient client, ObjectMapper objectMapper,
                                MeterRegistry meterRegistry, WriteRequest.RefreshPolicy refreshPolicy,
                                int numberOfShards, int numberOfReplicas) {
        this.client = client;
        this.mapper = objectMapper;
        this.meterRegistry = meterRegistry;
        this.refreshPolicy = refreshPolicy;
        this.numberOfShards = numberOfShards;
        this.numberOfReplicas = numberOfReplicas;
    }

    @Override
    public void createIndex(String indexName) {
        try {
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);

            XContentBuilder settingsContentBuilder = XContentFactory.jsonBuilder();
            settingsContentBuilder.startObject();
            {
                settingsContentBuilder.startObject("analysis");
                {
                    settingsContentBuilder.startObject("analyzer");
                    {
                        settingsContentBuilder.startObject("norwegian_ngram_text_analyzer");
                        {
                            settingsContentBuilder.field("type", "custom");
                            settingsContentBuilder.field("tokenizer", "standard");
                            settingsContentBuilder.array("filter", "lowercase", "norwegian_edge_ngrams");
                        }
                        settingsContentBuilder.endObject();
                        settingsContentBuilder.startObject("norwegian_ngram_text_search_analyzer");
                        {
                            settingsContentBuilder.field("type", "custom");
                            settingsContentBuilder.field("tokenizer", "standard");
                            settingsContentBuilder.array("filter", "lowercase");
                        }
                        settingsContentBuilder.endObject();
                    }
                    settingsContentBuilder.endObject();
                    settingsContentBuilder.startObject("filter");
                    {
                        settingsContentBuilder.startObject("norwegian_edge_ngrams");
                        {
                            settingsContentBuilder.field("type", "edge_ngram");
                            settingsContentBuilder.field("min_gram", 1);
                            settingsContentBuilder.field("max_gram", 15);
                        }
                        settingsContentBuilder.endObject();
                    }
                    settingsContentBuilder.endObject();
                }
                settingsContentBuilder.endObject();
                settingsContentBuilder.startObject("index");
                {
                    settingsContentBuilder.field("number_of_shards", numberOfShards);
                    settingsContentBuilder.field("number_of_replicas", numberOfReplicas);
                }
                settingsContentBuilder.endObject();
            }
            settingsContentBuilder.endObject();

            createIndexRequest.settings(settingsContentBuilder);
            MappingBuilder mappingBuilder = new MappingBuilderImpl();
            ObjectMapping mapping = mappingBuilder.build(EsCv.class);

            // String jsonMapping = mapping.getContentAsString();
            XContentBuilder contentBuilder = mapping.getContent();
            String jsonMapping = contentBuilder.string();
            LOGGER.debug("MAPPING: {}", jsonMapping);
            createIndexRequest.mapping(CV_TYPE, jsonMapping, XContentType.JSON);

            CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest);
            LOGGER.info("CREATEINDEXRESPONSE: index={}, acknowledged={}", createIndexResponse.index(), createIndexResponse.isAcknowledged());
        } catch (IOException ioe) {
            LOGGER.error("Feilet å lage index", ioe);
            throw new ElasticException(ioe);
        }
    }

    @Override
    public void deleteIndex(String indexName) {
        try {
            DeleteIndexRequest deleteRequest = new DeleteIndexRequest(indexName);
            DeleteIndexResponse deleteIndexResponse = client.indices().delete(deleteRequest);
            LOGGER.info("DELETERESPONSE: index={}, acknowledged={}", indexName, deleteIndexResponse.isAcknowledged());
        } catch (IOException ioe) {
            LOGGER.error("Feilet å slette index", ioe);
            throw new ElasticException(ioe);
        }
    }

    @Override
    public void index(EsCv esCv, String indexName) {
        try {
            String jsonString = mapper.writeValueAsString(esCv);
            LOGGER.debug("DOKUMENTET: " + jsonString);

            IndexRequest request = new IndexRequest(indexName, CV_TYPE, esCv.getKandidatnr());
            request.setRefreshPolicy(refreshPolicy);
            request.source(jsonString, XContentType.JSON);
            IndexResponse indexResponse = esExec(() -> client.index(request), indexName);
            LOGGER.debug("INDEXRESPONSE: " + indexResponse.toString());
        } catch (IOException ioe) {
            throw new ElasticException(ioe);
        }
    }

    @Override
    public int bulkIndex(List<EsCv> esCver, String indexName) {
        BulkRequest bulkRequest = Requests.bulkRequest();
        String currentKandidatnr = "";
        try {
            for (EsCv esCv : esCver) {
                currentKandidatnr = esCv.getKandidatnr();
                String jsonString = mapper.writeValueAsString(esCv);
                IndexRequest ir = Requests.indexRequest().source(jsonString, XContentType.JSON)
                        .index(indexName).type(CV_TYPE).id(currentKandidatnr);

                bulkRequest.add(ir);
            }
        } catch (Exception e) {
            LOGGER.info(
                    "Greide ikke å serialisere CV til JSON for å bygge opp bulk-indekseringsrequest: {}. Kandidatnr: {}",
                    e.getMessage(), currentKandidatnr, e);
            throw new ApplicationException(
                    "Greide ikke å serialisere CV til JSON for å bygge opp bulk-indekseringsrequest. Kandidatnr: "
                            + currentKandidatnr,
                    e);
        }

        LOGGER.info("Sender bulk indexrequest med {} cv'er", esCver.size());
        bulkRequest.setRefreshPolicy(refreshPolicy);
        BulkResponse bulkResponse = esExec(() -> client.bulk(bulkRequest), indexName);
        int antallIndeksert = esCver.size();

        if (bulkResponse.hasFailures()) {
            long antallFeil = 0;
            for (BulkItemResponse bir : bulkResponse.getItems()) {
                if (bir.getFailure() != null && bir.getFailure().getType().equals("unavailable_shards_exception")) {
                    LOGGER.warn("Kaster OperationalException for å trigge retry grunnet feil mot ES: {}", bulkResponse.buildFailureMessage());
                    throw new OperationalException("Unavailable shards i kandidatsok ES", bir.getFailure().getCause());
                }
                antallFeil += bir.isFailed() ? 1 : 0;
                try {
                    if (bir.getFailure() != null) {
                        Optional<EsCv> cvMedFeil = esCver.stream().filter(
                                esCv -> (esCv.getKandidatnr()).trim().equals(bir.getFailure().getId()))
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
            antallIndeksert -= antallFeil;
            LOGGER.warn(
                    "Feilet under indeksering av CVer: " + bulkResponse.buildFailureMessage());
        }
        LOGGER.debug("BULKINDEX tidsbruk: " + bulkResponse.getTook());
        return antallIndeksert;
    }

    @Override
    public int bulkSlettAktorId(List<String> aktorIdListe, String indexName)
    {
        try {
            final XContentBuilder jqb = XContentFactory.jsonBuilder();
            jqb.startObject().startObject("query").startObject("bool");
            jqb.startArray("should");
            for (String aktorId: aktorIdListe) {
                jqb.startObject().startObject("term").field("aktorId", aktorId).endObject().endObject();
            }
            jqb.endArray();
            jqb.field("minimum_should_match", 1);
            jqb.endObject().endObject().endObject();

            StringEntity body = new StringEntity(jqb.string(), ContentType.APPLICATION_JSON);

            Response deleteResponse = esExec(
                    () -> client.getLowLevelClient().performRequest(
                            "POST", "/" + indexName + "/_delete_by_query",
                            Map.of("refresh", refreshPolicy.getValue()), body), indexName);

            String jsonString = EntityUtils.toString(deleteResponse.getEntity());
            XContentParser parser = XContentType.JSON.xContent().createParser(NamedXContentRegistry.EMPTY, jsonString);
            Map<String, Object> esDeleteResponseData = parser.map();
            int numDeleted = Integer.valueOf(esDeleteResponseData.get("deleted").toString());
            LOGGER.debug("Slettet {} ES-CV-er basert på aktorId-er", numDeleted);

            // TODO inspect ES response for failures !
            return numDeleted;
        } catch(Exception e) {
            throw new ElasticException(e);
        }
    }

    @Override
    public void bulkSlettKandidatnr(List<String> kandidatnr, String indexName) {
        BulkRequest bulkRequest = Requests.bulkRequest();

        for (String id : kandidatnr) {
            DeleteRequest dr = Requests.deleteRequest(indexName).id(id).type(CV_TYPE);

            bulkRequest.add(dr);
        }

        LOGGER.info("Sender bulksletting av {} cv'er", kandidatnr.size());
        bulkRequest.setRefreshPolicy(refreshPolicy);
        BulkResponse bulkResponse = esExec(() -> client.bulk(bulkRequest), indexName);
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
    public boolean doesIndexExist(String indexName) {
        try {
            Response restResponse =
                    client.getLowLevelClient().performRequest("HEAD", "/" + indexName);
            return restResponse.getStatusLine().getStatusCode() == 200;
        } catch (ResponseException e) {
            LOGGER.info("Exception while calling isExistingIndex", e);
        } catch (IOException ioe) {
            throw new ElasticException(ioe);
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
    private <T> T esExec(IOSupplier<T> fun, String indexName) {
        try {
            try {
                return fun.get();
            } catch (ElasticsearchStatusException ese) {
                if (ese.status().getStatus() == 404
                        && ese.getMessage().contains("index_not_found_exception")) {
                    LOGGER.info(
                            "Greide ikke å utfore operasjon mot elastic search. Prøver å opprette index og forsøke på nytt.");
                    createIndex(indexName);
                    return fun.get();
                }
                throw (ese);
            }
        } catch (IOException ioe) {
            throw new ElasticException(ioe);
        }
    }

    /**
     * Tilsvarer java.functions.Supplier bare at get metoden kan kaste IOException
     */
    private interface IOSupplier<T> {
        T get() throws IOException;
    }

    @Override
    public long antallIndeksert(String indexName) {
        return indexQuery(null, indexName);
    }

    private long indexQuery(String query, String indexName) {
        Map<String, String> params = new HashMap<>();
        if (query != null) {
            params.put("q", query);
        }

        long antallIndeksert = 0;
        try {
            Response response = client.getLowLevelClient().performRequest("GET",
                    String.format("/%s/%s/_count", indexName, CV_TYPE), params);
            if (response != null && response.getStatusLine().getStatusCode() >= 200
                    && response.getStatusLine().getStatusCode() < 300) {
                String json = EntityUtils.toString(response.getEntity());
                JsonNode countNode = mapper.readTree(json).path(("count"));
                antallIndeksert = countNode != null ? countNode.asLong() : 0;
            } else {
                LOGGER.warn("Greide ikke å hente ut antall dokumenter i ES indeksen {}: {} : {}",
                        query,
                        response.getStatusLine().getStatusCode(),
                        response.getStatusLine().getReasonPhrase());
            }
        } catch (Exception e) {
            LOGGER.warn("Greide ikke å hente ut antall dokumenter i ES indeksen {}: {}",
                    query, e.getMessage(), e);
        }
        return antallIndeksert;
    }

    @Override
    public long antallIndeksertSynligForVeileder(String indexName) {
        return indexQuery("synligForVeilederSok:true", indexName);
    }

    @Override
    public long antallIndeksertSynligForArbeidsgiver(String indexName) {
        return indexQuery("synligForArbeidsgiverSok:true", indexName);
    }

    @Override
    public Collection<String> getTargetsForAlias(String alias) {
        try {
            Response response;
            try {
                response = client.getLowLevelClient().performRequest("GET", "/*/_alias/" + alias);
            } catch (ResponseException e) {
                if (e.getResponse().getStatusLine().getStatusCode() == 404) {
                    return Collections.emptySet();
                }
                throw e;
            }
            String jsonString = EntityUtils.toString(response.getEntity());
            XContentParser parser =
                    XContentType.JSON.xContent().createParser(NamedXContentRegistry.EMPTY, jsonString);
            return parser.map().keySet();
        } catch (IOException ioe) {
            throw new ElasticException(ioe);
        }
    }

    @Override
    public Response updateIndexAlias(String alias, String indexName) {
        try {
            XContentBuilder builder = jsonBuilder()
                    .startObject()
                    .startArray("actions")
                    .startObject()
                    .startObject("remove").field("index", "*").field("alias", alias).endObject()
                    .endObject()
                    .startObject()
                    .startObject("add").field("index", indexName).field("alias", alias).endObject()
                    .endObject()
                    .endArray()
                    .endObject();

            StringEntity entity = new StringEntity(builder.string(), ContentType.APPLICATION_JSON);
            Response response = client.getLowLevelClient().performRequest("POST", "/_aliases",
                    Collections.emptyMap(), entity);
            LOGGER.info("Setter alias {} til å peke mot {}", alias, indexName);
            return response;
        } catch (IOException ioe) {
            throw new ElasticException(ioe);
        }
    }

    @Override
    public void close() throws IOException {
        client.close();
    }

}
