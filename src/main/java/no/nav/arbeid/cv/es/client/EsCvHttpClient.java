package no.nav.arbeid.cv.es.client;

import java.io.IOException;
import java.util.ArrayList;
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
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
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
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.arbeid.cv.es.domene.Aggregering;
import no.nav.arbeid.cv.es.domene.Aggregeringsfelt;
import no.nav.arbeid.cv.es.domene.EsCv;
import no.nav.arbeid.cv.es.domene.Sokeresultat;
import no.nav.elasticsearch.mapping.MappingBuilder;
import no.nav.elasticsearch.mapping.MappingBuilderImpl;
import no.nav.elasticsearch.mapping.ObjectMapping;

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
  public List<String> typeAheadKompetanse(String prefix) throws IOException {
    return typeAhead(prefix, "kompetanse.navn.completion");
  }

  @Override
  public List<String> typeAheadUtdanning(String prefix) throws IOException {
    return typeAhead(prefix, "utdanning.nusKodeTekst.completion");
  }

  @Override
  public List<String> typeAheadYrkeserfaring(String prefix) throws IOException {
    return typeAhead(prefix, "yrkeserfaring.stillingstittel.completion");
  }

  private List<String> typeAhead(String prefix, String suggestionField) throws IOException {
    SearchRequest searchRequest = new SearchRequest(CV_INDEX);
    searchRequest.types(CV_TYPE);
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    CompletionSuggestionBuilder suggestionBuilder =
        SuggestBuilders.completionSuggestion(suggestionField).text(prefix).skipDuplicates(true);

    SuggestBuilder suggestBuilder = new SuggestBuilder();
    suggestBuilder.addSuggestion("typeahead", suggestionBuilder);
    searchSourceBuilder.suggest(suggestBuilder);

    searchRequest.source(searchSourceBuilder);
    SearchResponse searchResponse = client.search(searchRequest);
    LOGGER.debug("SEARCHRESPONSE: " + searchResponse);
    CompletionSuggestion compSuggestion = searchResponse.getSuggest().getSuggestion("typeahead");
    return compSuggestion.getOptions().stream().map(option -> option.getText().string())
        .collect(Collectors.toList());
  }

  @Override
  public Sokeresultat sok(String fritekst, String stillingstittel, String kompetanse,
      String utdanning, String styrkKode, String nusKode) throws IOException {
    AbstractQueryBuilder<?> queryBuilder = null;
    if (fritekst == null && stillingstittel == null && kompetanse == null && utdanning == null
        && styrkKode == null && nusKode == null) {
      LOGGER.debug("MATCH ALL!");
      queryBuilder = QueryBuilders.matchAllQuery();
    } else {
      BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

      if (fritekst != null) {
        MultiMatchQueryBuilder fritekstQueryBuilder =
            QueryBuilders.multiMatchQuery(fritekst, "fritekst");
        boolQueryBuilder.must(fritekstQueryBuilder);
        LOGGER.debug("ADDING fritekst");
      }

      if (stillingstittel != null) {
        NestedQueryBuilder yrkeserfaringQueryBuilder = QueryBuilders.nestedQuery("yrkeserfaring",
            QueryBuilders.matchQuery("yrkeserfaring.stillingstittel", stillingstittel),
            ScoreMode.None);
        boolQueryBuilder.must(yrkeserfaringQueryBuilder);
        LOGGER.debug("ADDING yrkeserfaring");
      }

      if (kompetanse != null) {
        NestedQueryBuilder kompetanseQueryBuilder = QueryBuilders.nestedQuery("kompetanse",
            QueryBuilders.matchQuery("kompetanse.navn", kompetanse), ScoreMode.None);
        boolQueryBuilder.must(kompetanseQueryBuilder);
        LOGGER.debug("ADDING kompetanse");
      }
      
      if (utdanning != null) {
        NestedQueryBuilder utdanningQueryBuilder = QueryBuilders.nestedQuery("utdanning",
            QueryBuilders.matchQuery("utdanning.nusKodeTekst", utdanning), ScoreMode.None);
        boolQueryBuilder.must(utdanningQueryBuilder);
        LOGGER.debug("ADDING utdanning");
      }

      if (styrkKode != null) {
        NestedQueryBuilder styrkKodeQueryBuilder = QueryBuilders.nestedQuery("yrkeserfaring",
            QueryBuilders.termQuery("yrkeserfaring.styrkKode", styrkKode), ScoreMode.None);
        boolQueryBuilder.must(styrkKodeQueryBuilder);
        LOGGER.debug("ADDING styrkKode");
      }
      if (nusKode != null) {
        NestedQueryBuilder nusKodeQueryBuilder = QueryBuilders.nestedQuery("utdanning",
            QueryBuilders.termQuery("utdanning.nusKode", nusKode), ScoreMode.None);
        boolQueryBuilder.must(nusKodeQueryBuilder);
        LOGGER.debug("ADDING nuskode");
      }
      queryBuilder = boolQueryBuilder;
    }

    SearchResponse searchResponse = search(queryBuilder, 0, 1000);
    return toSokeresultat(searchResponse);
  }

  @Override
  public Sokeresultat findByYrkeserfaringStyrkKodeTekst(String styrkBeskrivelse)
      throws IOException {

    NestedQueryBuilder yrkeserfaringQueryBuilder = new NestedQueryBuilder("yrkeserfaring",
        new MatchQueryBuilder("yrkeserfaring.styrkKodeTekst", styrkBeskrivelse), ScoreMode.None);

    SearchResponse searchResponse = search(yrkeserfaringQueryBuilder, 0, 1000);
    return toSokeresultat(searchResponse);
  }

  @Override
  public Sokeresultat findByEtternavnAndUtdanningNusKodeTekst(String etternavn,
      String utdanningNusKodeTekst) throws IOException {

    MatchQueryBuilder etternavnQueryBuilder = new MatchQueryBuilder("etternavn", etternavn);
    NestedQueryBuilder utdanningQueryBuilder = new NestedQueryBuilder("utdanning",
        new MatchQueryBuilder("utdanning.nusKodeTekst", utdanningNusKodeTekst), ScoreMode.None);

    BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
    boolQueryBuilder.must(utdanningQueryBuilder);
    boolQueryBuilder.should(etternavnQueryBuilder);

    SearchResponse searchResponse = search(boolQueryBuilder, 0, 1000);
    return toSokeresultat(searchResponse);
  }

  private Sokeresultat toSokeresultat(SearchResponse searchResponse) {
    List<EsCv> cver = toCvList(searchResponse);
    List<Aggregering> aggregeringer = toAggregeringList(searchResponse);
    return new Sokeresultat(cver, aggregeringer);
  }

  private List<Aggregering> toAggregeringList(SearchResponse searchResponse) {
    Aggregations aggregations = searchResponse.getAggregations();

    List<Aggregering> aggs = new ArrayList<>();
    List<String> aggregationNames =
        aggregations.asList().stream().map(agg -> agg.getName()).collect(Collectors.toList());
    for (String aggregationName : aggregationNames) {
      List<? extends Bucket> buckets = getBucketsForInnerAggregation(aggregations, aggregationName);
      List<Aggregeringsfelt> fields = buckets.stream()
          .map(bucket -> new Aggregeringsfelt(bucket.getKeyAsString(), bucket.getDocCount()))
          .collect(Collectors.toList());
      aggs.add(new Aggregering(aggregationName, fields));
    }

    return aggs;

  }

  private List<? extends Bucket> getBucketsForInnerAggregation(Aggregations aggregations,
      String aggregationName) {
    return ((Terms) ((Nested) aggregations.get(aggregationName)).getAggregations().get("nested"))
        .getBuckets();
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

    TermsAggregationBuilder yrkesAggregation =
        AggregationBuilders.terms("nested").field("yrkeserfaring.styrkKode");
    NestedAggregationBuilder nestedYrkesAggregation =
        AggregationBuilders.nested("yrkeserfaring", "yrkeserfaring");
    nestedYrkesAggregation.subAggregation(yrkesAggregation);
    searchSourceBuilder.aggregation(nestedYrkesAggregation);

    TermsAggregationBuilder utdanningAggregation =
        AggregationBuilders.terms("nested").field("utdanning.nusKode");
    NestedAggregationBuilder nestedUtdanningAggregation =
        AggregationBuilders.nested("utdanning", "utdanning");
    nestedUtdanningAggregation.subAggregation(utdanningAggregation);
    searchSourceBuilder.aggregation(nestedUtdanningAggregation);

    SearchRequest searchRequest = new SearchRequest();
    searchRequest.indices(CV_INDEX);
    searchRequest.source(searchSourceBuilder);

    LOGGER.debug("SEARCHREQUEST: " + searchRequest.toString());

    SearchResponse searchResponse = client.search(searchRequest);
    LOGGER.debug("SEARCHRESPONSE: " + searchResponse);
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

  private List<EsCv> toCvList(SearchResponse searchResponse) {
    return StreamSupport.stream(searchResponse.getHits().spliterator(), false)
        .map(hit -> mapEsCv(hit)).filter(Objects::nonNull).collect(Collectors.toList());
  }
}
