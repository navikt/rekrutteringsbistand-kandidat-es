package no.nav.arbeid.cv.es.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
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
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
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
import no.nav.arbeid.cv.es.domene.ApplicationException;
import no.nav.arbeid.cv.es.domene.EsCv;
import no.nav.arbeid.cv.es.domene.Sokekriterier;
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
      for (BulkItemResponse bir : bulkResponse.getItems()) {
        try {
          if (bir.getFailure() != null) {
            LOGGER.warn(bir.getFailure().getMessage(), bir.getFailure().getCause());
            esCver.stream()
                .filter(esCv -> esCv.getArenaPersonId().toString().equals(bir.getIndex()))
                .findFirst().ifPresent(
                    esCv -> LOGGER.warn("Det filet å indeksere denne CVen: " + esCv.toString()));
          }
        } catch (Exception e) {
          LOGGER.warn("Feilet å parse bulkitemresponse..", e);
        }
        LOGGER.warn("Feilet under indeksering av CVer: " + bulkResponse.buildFailureMessage());
      }
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
    }
    LOGGER.debug("BULKDELETERESPONSE: " + bulkResponse.toString());
  }

  @Override
  public List<String> typeAheadKompetanse(String prefix) throws IOException {
    return typeAhead(prefix, "samletKompetanse.samletKompetanseTekst.completion");
  }

  @Override
  public List<String> typeAheadUtdanning(String prefix) throws IOException {
    return typeAhead(prefix, "utdanning.nusKodeGrad.completion");
  }

  @Override
  public List<String> typeAheadYrkeserfaring(String prefix) throws IOException {
    return typeAhead(prefix, "yrkeserfaring.styrkKodeStillingstittel.completion");
  }

  @Override
  public List<String> typeAheadGeografi(String prefix) throws IOException {
    SearchRequest searchRequest = new SearchRequest(CV_INDEX);
    searchRequest.types(CV_TYPE);
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    CompletionSuggestionBuilder suggestionBuilder =
        SuggestBuilders.completionSuggestion("geografiJobbonsker.geografiKodeTekst.completion")
            .text(prefix).skipDuplicates(true);

    SuggestBuilder suggestBuilder = new SuggestBuilder();
    suggestBuilder.addSuggestion("typeahead", suggestionBuilder);
    searchSourceBuilder.suggest(suggestBuilder);

    searchRequest.source(searchSourceBuilder);
    SearchResponse searchResponse = esExec(() -> client.search(searchRequest));
    LOGGER.debug("SEARCHRESPONSE: " + searchResponse);
    CompletionSuggestion compSuggestion = searchResponse.getSuggest().getSuggestion("typeahead");

    return compSuggestion.getOptions().stream().map(option -> option.getHit().getSourceAsString())
        .collect(Collectors.toList());
  }

  public List<String> typeAheadYrkeJobbonsker(String prefix) throws IOException {
    return typeAhead(prefix, "yrkeJobbonsker.styrkBeskrivelse.completion");
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
    SearchResponse searchResponse = esExec(() -> client.search(searchRequest));
    LOGGER.debug("SEARCHRESPONSE: " + searchResponse);
    CompletionSuggestion compSuggestion = searchResponse.getSuggest().getSuggestion("typeahead");
    return compSuggestion.getOptions().stream().map(option -> option.getText().string())
        .collect(Collectors.toList());
  }

  @Override
  public Sokeresultat sok(String fritekst, List<String> stillingstitler, List<String> kompetanser,
      List<String> utdanninger, List<String> geografiList, String totalYrkeserfaring,
      List<String> utdanningsniva, String styrkKode, String nusKode, List<String> styrkKoder,
      List<String> nusKoder) throws IOException {
    Sokekriterier s = Sokekriterier.med().fritekst(fritekst).stillingstitler(stillingstitler)
        .kompetanser(kompetanser).utdanninger(utdanninger).geografiList(geografiList)
        .totalYrkeserfaring(totalYrkeserfaring).utdanningsniva(utdanningsniva).styrkKode(styrkKode)
        .nusKode(nusKode).styrkKoder(styrkKoder).nusKoder(nusKoder).bygg();
    return sok(s);
  }

  @Override
  public Sokeresultat sok(Sokekriterier sk) throws IOException {

    AbstractQueryBuilder<?> queryBuilder = null;
    if (StringUtils.isBlank(sk.fritekst())
        && (sk.yrkeJobbonsker() == null || sk.yrkeJobbonsker().isEmpty())
        && (sk.stillingstitler() == null || sk.stillingstitler().isEmpty())
        && (sk.kompetanser() == null || sk.kompetanser().isEmpty())
        && (sk.utdanninger() == null || sk.utdanninger().isEmpty())
        && (StringUtils.isBlank(sk.totalYrkeserfaring()))
        && (sk.utdanningsniva() == null || sk.utdanningsniva().isEmpty())
        && (sk.geografiList() == null || sk.geografiList().isEmpty())
        && (sk.styrkKoder() == null || sk.styrkKoder().isEmpty())
        && (sk.nusKoder() == null || sk.nusKoder().isEmpty())) {
      LOGGER.debug("MATCH ALL!");
      queryBuilder = QueryBuilders.matchAllQuery();

    } else {

      BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

      if (StringUtils.isNotBlank(sk.fritekst())) {
        MultiMatchQueryBuilder fritekstQueryBuilder =
            QueryBuilders.multiMatchQuery(sk.fritekst(), "fritekst");
        boolQueryBuilder.must(fritekstQueryBuilder);
        LOGGER.debug("ADDING fritekst");
      }

      if (sk.yrkeJobbonsker() != null && !sk.yrkeJobbonsker().isEmpty()) {
        BoolQueryBuilder yrkeJobbonskerBoolQueryBuilder = QueryBuilders.boolQuery();

        sk.yrkeJobbonsker().stream().filter(StringUtils::isNotBlank)
            .forEach(y -> addYrkeJobbonskerQuery(y, yrkeJobbonskerBoolQueryBuilder));

        boolQueryBuilder.must(yrkeJobbonskerBoolQueryBuilder);
        LOGGER.debug("ADDING onsket stilling");
      }

      if (sk.stillingstitler() != null && !sk.stillingstitler().isEmpty()) {
        sk.stillingstitler().stream().filter(StringUtils::isNotBlank)
            .forEach(s -> addStillingsTitlerQuery(s, boolQueryBuilder));
      }

      if (sk.kompetanser() != null && !sk.kompetanser().isEmpty()) {
        sk.kompetanser().stream().filter(StringUtils::isNotBlank)
            .forEach(k -> addKompetanseQuery(k, boolQueryBuilder));
      }

      if (sk.utdanninger() != null && !sk.utdanninger().isEmpty()) {
        sk.utdanninger().stream().filter(StringUtils::isNotBlank)
            .forEach(u -> addUtdanningerQuery(u, boolQueryBuilder));
      }

      if (sk.geografiList() != null && !sk.geografiList().isEmpty()) {
        sk.geografiList().stream().filter(StringUtils::isNotBlank)
            .forEach(g -> addGeografiQuery(g, boolQueryBuilder));
      }

      if (StringUtils.isNotBlank(sk.totalYrkeserfaring())) {
        String[] interval = sk.totalYrkeserfaring().split("-");
        RangeQueryBuilder totalErfaringQueryBuilder;
        if (interval.length == 2) {
          totalErfaringQueryBuilder = QueryBuilders.rangeQuery("totalLengdeYrkeserfaring")
              .gte(interval[0]).lte(interval[1]);
          boolQueryBuilder.must(totalErfaringQueryBuilder);
        } else if (interval.length == 1) {
          totalErfaringQueryBuilder =
              QueryBuilders.rangeQuery("totalLengdeYrkeserfaring").gte(interval[0]).lte(null);
          boolQueryBuilder.must(totalErfaringQueryBuilder);
        }
        LOGGER.debug("ADDING totalYrkeserfaringLengde");
      }

      if (sk.utdanningsniva() != null && !sk.utdanningsniva().isEmpty()) {
        BoolQueryBuilder utdanningsnivaBoolQueryBuilder = QueryBuilders.boolQuery();

        sk.utdanningsniva().stream().filter(StringUtils::isNotBlank)
            .forEach(u -> addUtdanningsnivaQuery(u, utdanningsnivaBoolQueryBuilder));

        boolQueryBuilder.must(utdanningsnivaBoolQueryBuilder);
        LOGGER.debug("ADDING utdanningsniva");
      }

      if (sk.styrkKoder() != null && !sk.styrkKoder().isEmpty()) {
        sk.styrkKoder().stream().filter(StringUtils::isNotBlank)
            .forEach(k -> addStyrkKodeQuery(k, boolQueryBuilder));
      }

      if (sk.nusKoder() != null) {
        sk.nusKoder().stream().filter(StringUtils::isNotBlank)
            .forEach(k -> addNusKodeQuery(k, boolQueryBuilder));
      }

      if (StringUtils.isNotBlank(sk.etternavn())) {
        MatchQueryBuilder etternavnQueryBuilder =
            new MatchQueryBuilder("etternavn", sk.etternavn());
        boolQueryBuilder.should(etternavnQueryBuilder);
      }
      queryBuilder = boolQueryBuilder;
    }

    final AbstractQueryBuilder<?> qb = queryBuilder;
    SearchResponse searchResponse = esExec(() -> search(qb, 0, 1000));
    return toSokeresultat(searchResponse);
  }

  private void addYrkeJobbonskerQuery(String yrkeJobbonske, BoolQueryBuilder boolQueryBuilder) {
    NestedQueryBuilder yrkeJobbonskeQueryBuilder = QueryBuilders.nestedQuery("yrkeJobbonsker",
        QueryBuilders.matchQuery("yrkeJobbonsker.styrkBeskrivelse", yrkeJobbonske), ScoreMode.None);
    boolQueryBuilder.should(yrkeJobbonskeQueryBuilder);
  }

  private void addStillingsTitlerQuery(String stillingstittel, BoolQueryBuilder boolQueryBuilder) {
    NestedQueryBuilder yrkeserfaringQueryBuilder = QueryBuilders.nestedQuery("yrkeserfaring",
        QueryBuilders.matchQuery("yrkeserfaring.styrkKodeStillingstittel", stillingstittel),
        ScoreMode.None);
    boolQueryBuilder.must(yrkeserfaringQueryBuilder);
    LOGGER.debug("ADDING yrkeserfaring");
  }

  private void addUtdanningerQuery(String utdanning, BoolQueryBuilder boolQueryBuilder) {
    NestedQueryBuilder utdanningQueryBuilder = QueryBuilders.nestedQuery("utdanning",
        QueryBuilders.matchQuery("utdanning.nusKodeGrad", utdanning), ScoreMode.None);
    boolQueryBuilder.must(utdanningQueryBuilder);
    LOGGER.debug("ADDING utdanning");
  }

  private void addKompetanseQuery(String kompetanse, BoolQueryBuilder boolQueryBuilder) {
    NestedQueryBuilder kompetanseQueryBuilder = QueryBuilders.nestedQuery("samletKompetanse",
        QueryBuilders.matchQuery("samletKompetanse.samletKompetanseTekst", kompetanse),
        ScoreMode.None);
    boolQueryBuilder.must(kompetanseQueryBuilder);
    LOGGER.debug("ADDING kompetanse");
  }

  private void addGeografiQuery(String geografi, BoolQueryBuilder boolQueryBuilder) {
    String[] geografiKoder = geografi.split("\\.");
    String regex = geografi + "|NO";

    if (geografiKoder.length > 1) {
      regex += "|" + geografiKoder[0];
      if (geografiKoder[1].length() > 4) {
        regex += "|" + geografiKoder[0] + "." + geografiKoder[1].substring(0, 4);
      }
    }

    NestedQueryBuilder geografiQueryBuilder = QueryBuilders.nestedQuery("geografiJobbonsker",
        QueryBuilders.regexpQuery("geografiJobbonsker.geografiKode", regex), ScoreMode.None);
    boolQueryBuilder.must(geografiQueryBuilder);
    LOGGER.debug("ADDING geografiJobbonske");
  }

  private void addUtdanningsnivaQuery(String utdanningsniva, BoolQueryBuilder boolQueryBuilder) {
    String regex = "";
    switch (utdanningsniva) {
      case "Grunnskole":
        regex = "[0-2][0-9]+";
        break;
      case "Videregaende":
        regex = "[3-4][0-9]+";
        break;
      case "Fagskole":
        regex = "5[0-9]+";
        break;
      case "Bachelor":
        regex = "6[0-9]+";
        break;
      case "Master":
        regex = "7[0-9]+";
        break;
      case "Doktorgrad":
        regex = "8[0-9]+";
        break;
    }
    if (!regex.equals("")) {
      NestedQueryBuilder utdanningsnivaQueryBuilder = QueryBuilders.nestedQuery("utdanning",
          QueryBuilders.regexpQuery("utdanning.nusKode", regex), ScoreMode.None);
      boolQueryBuilder.should(utdanningsnivaQueryBuilder);
    }
    if (utdanningsniva.equals("Ingen")) {
      BoolQueryBuilder boolQueryBuilder1 = QueryBuilders.boolQuery();

      NestedQueryBuilder utdanningsnivaQueryBuilder = QueryBuilders.nestedQuery("utdanning",
          QueryBuilders.existsQuery("utdanning.nusKode"), ScoreMode.None);

      boolQueryBuilder1.mustNot(utdanningsnivaQueryBuilder);
      boolQueryBuilder.should(boolQueryBuilder1);
    }
    if (utdanningsniva.equals("Fagbrev")) {
      NestedQueryBuilder kompetanseQueryBuilder = QueryBuilders.nestedQuery("kompetanse",
          QueryBuilders.matchQuery("kompetanse.kompKode", "501"), ScoreMode.None);
      boolQueryBuilder.should(kompetanseQueryBuilder);
    }
    if (utdanningsniva.equals("Fagskole")) {
      NestedQueryBuilder kompetanseQueryBuilder = QueryBuilders.nestedQuery("kompetanse",
          QueryBuilders.matchQuery("kompetanse.kompKode", "506"), ScoreMode.None);
      boolQueryBuilder.should(kompetanseQueryBuilder);
    }
  }

  private void addNusKodeQuery(String nusKode, BoolQueryBuilder boolQueryBuilder) {
    NestedQueryBuilder nusKodeQueryBuilder = QueryBuilders.nestedQuery("utdanning",
        QueryBuilders.termQuery("utdanning.nusKode", nusKode), ScoreMode.None);
    boolQueryBuilder.must(nusKodeQueryBuilder);
    LOGGER.debug("ADDING nuskode");
  }

  private void addStyrkKodeQuery(String styrkKode, BoolQueryBuilder boolQueryBuilder) {
    NestedQueryBuilder styrkKodeQueryBuilder = QueryBuilders.nestedQuery("yrkeserfaring",
        QueryBuilders.termQuery("yrkeserfaring.styrkKode", styrkKode), ScoreMode.None);
    boolQueryBuilder.must(styrkKodeQueryBuilder);
    LOGGER.debug("ADDING styrkKode");
  }

  @Override
  public Sokeresultat findByYrkeserfaringStyrkKodeTekst(String styrkBeskrivelse)
      throws IOException {

    NestedQueryBuilder yrkeserfaringQueryBuilder = new NestedQueryBuilder("yrkeserfaring",
        new MatchQueryBuilder("yrkeserfaring.styrkKodeStillingstittel", styrkBeskrivelse),
        ScoreMode.None);

    SearchResponse searchResponse = search(yrkeserfaringQueryBuilder, 0, 1000);
    return toSokeresultat(searchResponse);
  }

  @Override
  public Sokeresultat findByEtternavnAndUtdanningNusKodeGrad(String etternavn,
      String utdanningNusKodeGrad) throws IOException {

    MatchQueryBuilder etternavnQueryBuilder = new MatchQueryBuilder("etternavn", etternavn);
    NestedQueryBuilder utdanningQueryBuilder = new NestedQueryBuilder("utdanning",
        new MatchQueryBuilder("utdanning.nusKodeGrad", utdanningNusKodeGrad), ScoreMode.None);

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
      List<Aggregeringsfelt> fields = getFields(buckets);
      aggs.add(new Aggregering(aggregationName, fields));
    }

    return aggs;

  }

  List<Aggregeringsfelt> getFields(List<? extends Bucket> buckets) {
    if (!buckets.isEmpty()) {
      return buckets.stream().map(bucket -> new Aggregeringsfelt(bucket.getKeyAsString(),
          bucket.getDocCount(), getSubAggregation(bucket))).collect(Collectors.toList());
    }
    return Collections.emptyList();
  }

  private List<Aggregeringsfelt> getSubAggregation(Bucket bucket) {
    List<? extends Bucket> buckets =
        bucket.getAggregations().asList().stream().map(agg -> (Terms) agg).findFirst()
            .map(terms -> terms.getBuckets()).orElse(Collections.emptyList());
    return getFields(buckets);
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

    TermsAggregationBuilder yrkesAggregation3Siffer =
        AggregationBuilders.terms("nested").field("yrkeserfaring.styrkKode3Siffer");
    NestedAggregationBuilder nestedYrkesAggregation3Siffer =
        AggregationBuilders.nested("yrkeserfaring", "yrkeserfaring");
    nestedYrkesAggregation3Siffer.subAggregation(yrkesAggregation3Siffer);

    TermsAggregationBuilder yrkesAggregation4Siffer =
        AggregationBuilders.terms("4siffer").field("yrkeserfaring.styrkKode4Siffer");
    yrkesAggregation3Siffer.subAggregation(yrkesAggregation4Siffer);

    TermsAggregationBuilder yrkesAggregation6Siffer = AggregationBuilders.terms("6siffer")
        .field("yrkeserfaring.styrkKodeStillingstittel.keyword");
    yrkesAggregation4Siffer.subAggregation(yrkesAggregation6Siffer);

    searchSourceBuilder.aggregation(nestedYrkesAggregation3Siffer);

    TermsAggregationBuilder utdanningAggregation =
        AggregationBuilders.terms("nested").field("utdanning.nusKode");
    NestedAggregationBuilder nestedUtdanningAggregation =
        AggregationBuilders.nested("utdanning", "utdanning");
    nestedUtdanningAggregation.subAggregation(utdanningAggregation);
    searchSourceBuilder.aggregation(nestedUtdanningAggregation);

    TermsAggregationBuilder kompetanseAggregation =
        AggregationBuilders.terms("nested").field("kompetanse.kompKodeNavn.keyword");
    NestedAggregationBuilder nestedKompetanseAggregation =
        AggregationBuilders.nested("kompetanse", "kompetanse");
    nestedKompetanseAggregation.subAggregation(kompetanseAggregation);
    searchSourceBuilder.aggregation(nestedKompetanseAggregation);

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
      if (e.status().getStatus() == 404 && e.getMessage().contains("index_not_found_exception")) {
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
}
