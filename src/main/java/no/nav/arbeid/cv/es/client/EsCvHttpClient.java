package no.nav.arbeid.cv.es.client;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.frekele.elasticsearch.mapping.MappingBuilder;
import org.frekele.elasticsearch.mapping.MappingBuilderImpl;
import org.frekele.elasticsearch.mapping.ObjectMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.arbeid.cv.es.domene.EsCv;

public class EsCvHttpClient implements EsCvClient {

  private static final String CV_INDEX = "cvindex";
  private static final String CV_TYPE = "cvtype";

  private static final Logger LOGGER = LoggerFactory.getLogger(EsCvHttpClient.class);

  private final RestHighLevelClient client;

  private final ObjectMapper mapper;

  public EsCvHttpClient(RestHighLevelClient client, ObjectMapper objectMapper) {
    this.client = client;
    this.mapper = objectMapper;
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

    IndexRequest request = new IndexRequest(CV_INDEX, CV_TYPE, Long.toString(esCv.getArenaId()));
    request.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
    request.source(jsonString, XContentType.JSON);
    IndexResponse indexResponse = client.index(request);
    LOGGER.debug("INDEXRESPONSE: " + indexResponse.toString());
  }

  @Override
  public List<EsCv> findByYrkeserfaringStyrkKode(String styrk) throws IOException {

    NestedQueryBuilder yrkeserfaringQueryBuilder = new NestedQueryBuilder("yrkeserfaring",
        new TermQueryBuilder("yrkeserfaring.styrkKode", styrk), ScoreMode.None);

    SearchResponse searchResponse = search(yrkeserfaringQueryBuilder, 0, 1000);
    return toList(searchResponse);
  }

  @Override
  public List<EsCv> findByYrkeserfaringStyrkKodeTekst(String styrkBeskrivelse) throws IOException {

    NestedQueryBuilder yrkeserfaringQueryBuilder = new NestedQueryBuilder("yrkeserfaring",
        new MatchQueryBuilder("yrkeserfaring.styrkKodeTekst", styrkBeskrivelse), ScoreMode.None);

    SearchResponse searchResponse = search(yrkeserfaringQueryBuilder, 0, 1000);
    return toList(searchResponse);
  }

  @Override
  public List<EsCv> findByEtternavnAndUtdanningNusKodeTekst(String etternavn,
      String utdanningNusKodeTekst) throws IOException {

    MatchQueryBuilder etternavnQueryBuilder = new MatchQueryBuilder("etternavn", etternavn);
    NestedQueryBuilder utdanningQueryBuilder = new NestedQueryBuilder("utdanning",
        new MatchQueryBuilder("utdanning.nusKodeTekst", utdanningNusKodeTekst), ScoreMode.None);

    BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
    boolQueryBuilder.must(utdanningQueryBuilder);
    boolQueryBuilder.should(etternavnQueryBuilder);

    SearchResponse searchResponse = search(boolQueryBuilder, 0, 1000);
    return toList(searchResponse);
  }

  private SearchResponse search(AbstractQueryBuilder<?> queryBuilder, int from, int size)
      throws IOException {
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.query(queryBuilder);
    searchSourceBuilder.from(from);
    searchSourceBuilder.size(size);
    // Dette er defaulten, så egentlig unødvendig å sette:
    searchSourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
    searchSourceBuilder.sort(new FieldSortBuilder("_uid").order(SortOrder.ASC));

    TermsAggregationBuilder aggregation =
        AggregationBuilders.terms("byStyrk").field("yrkeserfaring.styrkKode");
    NestedAggregationBuilder nestedAggregation =
        AggregationBuilders.nested("nestedAgg", "yrkeserfaring");
    nestedAggregation.subAggregation(aggregation);
    searchSourceBuilder.aggregation(nestedAggregation);
    SearchRequest searchRequest = new SearchRequest();
    searchRequest.indices(CV_INDEX);
    searchRequest.source(searchSourceBuilder);

    LOGGER.debug("SEARCHREQUEST: " + searchRequest.toString());

    SearchResponse searchResponse = client.search(searchRequest);
    LOGGER.debug("SEARCHRESPONSE: " + searchResponse);

    Aggregations aggregations = searchResponse.getAggregations();
    System.out.println("AGGREG: " + aggregations.toString());
    aggregations.asList().stream()
        .forEach(agg -> System.out.println("AGGREGATIONS: " + agg.toString()));
    Nested nestedAgg = aggregations.get("nestedAgg");
    Terms byStyrkAgg = nestedAgg.getAggregations().get("byStyrk");
    List<? extends Bucket> buckets = byStyrkAgg.getBuckets();
    buckets.stream().forEach(bucket -> System.out
        .println("BUCKETNAME: " + bucket.getKeyAsString() + " - COUNT: " + bucket.getDocCount()));

    return searchResponse;
  }

  private EsCv mapEsCv(SearchHit hit) {
    try {
      return mapper.readValue(hit.getSourceAsString(), EsCv.class);
    } catch (IOException e) {
      LOGGER.warn("Klarte ikke å parse CV fra Elasticsearch, returnerer null", e);
      return null;
    }
  }

  private List<EsCv> toList(SearchResponse searchResponse) {
    return StreamSupport.stream(searchResponse.getHits().spliterator(), false)
        .map(hit -> mapEsCv(hit)).filter(Objects::nonNull).collect(Collectors.toList());
  }
}
