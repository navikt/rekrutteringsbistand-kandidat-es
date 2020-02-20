package no.nav.arbeid.kandidatsok.es.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import no.nav.arbeid.cv.indexer.config.StaticMappingProvider;
import no.nav.arbeid.cv.kandidatsok.es.domene.EsCv;
import no.nav.arbeid.cv.kandidatsok.es.exception.ApplicationException;
import no.nav.arbeid.cv.kandidatsok.es.exception.OperationalException;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest.AliasActions;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class EsIndexerHttpService implements EsIndexerService, AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(EsIndexerHttpService.class);

    private final RestHighLevelClient client;
    private final ObjectMapper mapper;
    private final MeterRegistry meterRegistry;
    private final int numberOfShards;
    private final int numberOfReplicas;

    private WriteRequest.RefreshPolicy refreshPolicy;

    public EsIndexerHttpService(RestHighLevelClient client, ObjectMapper objectMapper,
                                MeterRegistry meterRegistry, WriteRequest.RefreshPolicy refreshPolicy,
                                int numberOfShards, int numberOfReplicas) {
        this.client = client;
        this.mapper = objectMapper;
        this.meterRegistry = meterRegistry;
        this.refreshPolicy = refreshPolicy != null ? refreshPolicy : WriteRequest.RefreshPolicy.NONE;
        this.numberOfShards = numberOfShards;
        this.numberOfReplicas = numberOfReplicas;
    }

    @Override
    public void createIndex(String indexName) {
        try {
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);

            final Map<String, Object> settings = StaticMappingProvider.cvSettings();
            final Map<String, Object> indexSettings = (Map<String, Object>) settings.computeIfAbsent("index", k -> new HashMap<String, Object>());
            indexSettings.put("number_of_shards", numberOfShards);
            indexSettings.put("number_of_replicas", numberOfReplicas);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Settings: {}", Strings.toString(XContentFactory.contentBuilder(XContentType.JSON).map(settings).map(settings)));
            }
            createIndexRequest.settings(settings);

            final Map<String, Object> mapping = StaticMappingProvider.cvMapping();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Mapping: {}", Strings.toString(XContentFactory.contentBuilder(XContentType.JSON).map(mapping)));
            }
            createIndexRequest.mapping(mapping);

            CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
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
            AcknowledgedResponse deleteIndexResponse = client.indices().delete(deleteRequest, RequestOptions.DEFAULT);
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

            IndexRequest request = new IndexRequest(indexName).id(esCv.getKandidatnr());
            request.setRefreshPolicy(refreshPolicy);
            request.source(jsonString, XContentType.JSON);
            IndexResponse indexResponse = esExec(() -> client.index(request, RequestOptions.DEFAULT), indexName);
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
                        .index(indexName).id(currentKandidatnr);

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
        BulkResponse bulkResponse = esExec(() -> client.bulk(bulkRequest, RequestOptions.DEFAULT), indexName);
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
    public int bulkSlettAktorId(List<String> aktorIdListe, String indexName) {
        // Dette er essensielt, ellers risikere man at alle dokumenter i indeks slettes (en bool query uten clauses matcher
        // tydeligvis alt, på tross av at 'minimum_should_match' er satt til 1).
        if (aktorIdListe.isEmpty()) {
            return 0;
        }

        BoolQueryBuilder deleteQueryBuilder = QueryBuilders.boolQuery().minimumShouldMatch(1);
        aktorIdListe.forEach(aktorId -> {
            deleteQueryBuilder.should(QueryBuilders.termQuery("aktorId", aktorId));
        });

        DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest(indexName);
        deleteByQueryRequest.setQuery(deleteQueryBuilder);
        deleteByQueryRequest.setRefresh(refreshPolicy != WriteRequest.RefreshPolicy.NONE);

        // TODO inspect response for failures ! If at least one failure, then fail the entire request.
        BulkByScrollResponse bulkByScrollResponse = esExec(() -> client.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT), indexName);
        return (int)bulkByScrollResponse.getDeleted();
    }


    @Override
    public void bulkSlettKandidatnr(List<String> kandidatnr, String indexName) {
        BulkRequest bulkRequest = Requests.bulkRequest();

        for (String id : kandidatnr) {
            bulkRequest.add(Requests.deleteRequest(indexName).id(id));
        }

        LOGGER.info("Sender bulksletting av {} cv'er", kandidatnr.size());
        bulkRequest.setRefreshPolicy(refreshPolicy);
        BulkResponse bulkResponse = esExec(() -> client.bulk(bulkRequest, RequestOptions.DEFAULT), indexName);
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
            return client.indices().exists(new GetIndexRequest(indexName), RequestOptions.DEFAULT);
//            Response restResponse =
//                    client.getLowLevelClient().performRequest(new Request("HEAD", "/" + indexName));
//            return restResponse.getStatusLine().getStatusCode() == 200;
        } catch (ResponseException e) {
            LOGGER.info("Exception while calling isExistingIndex", e);
        } catch (IOException ioe) {
            throw new ElasticException(ioe);
        }
        return false;
    }

    /**
     * Pakk inn kall mot elastic search i sjekk på om index finnes. Hvis index ikke finnes så
     * opprettes den, og kallet forsøkes på nytt
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
                throw ese;
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
        return tellDokumenter(null, indexName);
    }

    private long tellDokumenter(String query, String indexName) {
        try {
            CountRequest countReqest = new CountRequest(indexName);
            if (query != null) {
                countReqest.source(SearchSourceBuilder.searchSource()
                        .query(QueryBuilders.queryStringQuery(query)));
            }

            CountResponse count = client.count(countReqest, RequestOptions.DEFAULT);
            return count.getCount();
        } catch (Exception e) {
            LOGGER.warn("Greide ikke å hente ut antall dokumenter for indeks {}, spørring '{}': {}",
                    indexName, query != null ? query : "<no query>", e.getMessage(), e);
            return 0L;
        }
    }

    @Override
    public long antallIndeksertSynligForVeileder(String indexName) {
        return tellDokumenter("synligForVeilederSok:true", indexName);
    }

    @Override
    public long antallIndeksertSynligForArbeidsgiver(String indexName) {
        return tellDokumenter("synligForArbeidsgiverSok:true", indexName);
    }

    @Override
    public Collection<String> getTargetsForAlias(String alias, String indexPattern) {
        try {
            GetAliasesResponse aliases = client.indices().getAlias(new GetAliasesRequest(alias).indices(indexPattern), RequestOptions.DEFAULT);
            return aliases.getAliases().keySet();
        } catch (IOException io) {
            throw new ElasticException(io);
        }
    }

    @Override
    public boolean updateIndexAlias(String alias, String removeForIndexPattern, String addForIndexName) {

        IndicesAliasesRequest request = new IndicesAliasesRequest();
        request.addAliasAction(new AliasActions(AliasActions.Type.REMOVE).index(removeForIndexPattern).alias(alias));
        request.addAliasAction(new AliasActions(AliasActions.Type.ADD).index(addForIndexName).alias(alias));
        try {
            AcknowledgedResponse response = client.indices().updateAliases(request, RequestOptions.DEFAULT);
            LOGGER.info("Satt alias {} til å peke mot {}: {}", alias, addForIndexName, response.isAcknowledged() ? "ACK" : "ERR");
            return response.isAcknowledged();
        } catch (IOException e) {
            throw new ElasticException(e);
        }

    }

    @Override
    public void close() throws IOException {
        client.close();
    }

}
