package no.nav.arbeid.kandidatsok.es.client;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
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

import no.nav.arbeid.cv.kandidatsok.es.domene.sok.Aggregering;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.Aggregeringsfelt;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.EsCv;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.Sokekriterier;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.Sokeresultat;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class EsSokHttpService implements EsSokService {

    private static final String CV_INDEX = "cvindex";
    private static final String CV_TYPE = "cvtype";

    private static final Logger LOGGER = LoggerFactory.getLogger(EsSokHttpService.class);

    private final RestHighLevelClient client;
    private final ObjectMapper mapper;

    public EsSokHttpService(RestHighLevelClient client, ObjectMapper objectMapper) {
        this.client = client;
        this.mapper = objectMapper;
    }

    @Override
    public List<String> typeAheadSprak(String prefix) throws IOException {
        return typeAhead(prefix, "sprak.sprakKodeTekst.completion");
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
                .collect(toList());
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
                .collect(toList());
    }

    @Override
    public Sokeresultat sok(Sokekriterier sk) throws IOException {

        AbstractQueryBuilder<?> queryBuilder = null;
        if (StringUtils.isBlank(sk.fritekst())
                && (sk.yrkeJobbonsker() == null || sk.yrkeJobbonsker().isEmpty())
                && (sk.stillingstitler() == null || sk.stillingstitler().isEmpty())
                && (sk.kompetanser() == null || sk.kompetanser().isEmpty())
                && (sk.utdanninger() == null || sk.utdanninger().isEmpty())
                && (sk.totalYrkeserfaring() == null || sk.totalYrkeserfaring().isEmpty())
                && (sk.utdanningsniva() == null || sk.utdanningsniva().isEmpty())
                && (sk.geografiList() == null || sk.geografiList().isEmpty())
                && (sk.styrkKoder() == null || sk.styrkKoder().isEmpty())
                && (sk.nusKoder() == null || sk.nusKoder().isEmpty())
                && (sk.sprak() == null || sk.sprak().isEmpty())) {
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

            if (sk.sprak() != null && !sk.sprak().isEmpty()) {
                sk.sprak().stream().filter(StringUtils::isNotBlank)
                        .forEach(k -> addSprakQuery(k, boolQueryBuilder));
            }

            if (sk.utdanninger() != null && !sk.utdanninger().isEmpty()) {
                sk.utdanninger().stream().filter(StringUtils::isNotBlank)
                        .forEach(u -> addUtdanningerQuery(u, boolQueryBuilder));
            }

            if (sk.geografiList() != null && !sk.geografiList().isEmpty()) {
                sk.geografiList().stream().filter(StringUtils::isNotBlank)
                        .forEach(g -> addGeografiQuery(g, boolQueryBuilder));
            }

            if (sk.totalYrkeserfaring() != null && !sk.totalYrkeserfaring().isEmpty()) {
                BoolQueryBuilder totalYrkeserfaringBoolQueryBuilder = QueryBuilders.boolQuery();

                sk.totalYrkeserfaring().stream().filter(StringUtils::isNotBlank)
                        .forEach(te -> addTotalYrkeserfaringQuery(te, totalYrkeserfaringBoolQueryBuilder));
                boolQueryBuilder.must(totalYrkeserfaringBoolQueryBuilder);
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
        SearchResponse searchResponse = esExec(() -> search(qb, 0, 100));
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

    private void addSprakQuery(String sprak, BoolQueryBuilder boolQueryBuilder) {
        NestedQueryBuilder sprakQueryBuilder = QueryBuilders.nestedQuery("sprak",
                QueryBuilders.matchQuery("sprak.sprakKodeTekst", sprak),
                ScoreMode.None);
        boolQueryBuilder.must(sprakQueryBuilder);
        LOGGER.debug("ADDING sprak");
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

    private void addTotalYrkeserfaringQuery(String totalYrkeserfaring, BoolQueryBuilder boolQueryBuilder) {
        String[] interval = totalYrkeserfaring.split("-");
        RangeQueryBuilder totalErfaringQueryBuilder;
        if (interval.length == 2) {
            totalErfaringQueryBuilder = QueryBuilders.rangeQuery("totalLengdeYrkeserfaring")
                    .gte(interval[0]).lte(interval[1]);
            boolQueryBuilder.should(totalErfaringQueryBuilder);
        } else if (interval.length == 1) {
            totalErfaringQueryBuilder =
                    QueryBuilders.rangeQuery("totalLengdeYrkeserfaring").gte(interval[0]).lte(null);
            boolQueryBuilder.should(totalErfaringQueryBuilder);
        }
        LOGGER.debug("ADDING totalYrkeserfaringLengde");
    }

    private void addUtdanningsnivaQuery(String utdanningsniva, BoolQueryBuilder boolQueryBuilder) {
        String searchRegex = "";
        String excludeRegex = "";
        switch (utdanningsniva) {
            case "Ingen":
                searchRegex = "[0-2][0-9]+";
                excludeRegex = "[3-8][0-9]+";
                break;
            case "Videregaende":
                searchRegex = "[3-4][0-9]+";
                excludeRegex = "[5-8][0-9]+";
                break;
            case "Fagskole":
                searchRegex = "5[0-9]+";
                excludeRegex = "[6-8][0-9]+";
                break;
            case "Bachelor":
                searchRegex = "6[0-9]+";
                excludeRegex = "[7-8][0-9]+";
                break;
            case "Master":
                searchRegex = "7[0-9]+";
                excludeRegex = "8[0-9]+";
                break;
            case "Doktorgrad":
                searchRegex = "8[0-9]+";
                break;
        }
        if (!searchRegex.equals("")) {
            BoolQueryBuilder boolQueryBuilder1 = QueryBuilders.boolQuery();

            NestedQueryBuilder includeUtdanningsnivaQueryBuilder = QueryBuilders.nestedQuery("utdanning",
                    QueryBuilders.regexpQuery("utdanning.nusKode", searchRegex), ScoreMode.None);

            NestedQueryBuilder excludeUtdanningsnivaQueryBuilder1 = QueryBuilders.nestedQuery("utdanning",
                    QueryBuilders.regexpQuery("utdanning.nusKode", excludeRegex), ScoreMode.None);

            boolQueryBuilder1.must(includeUtdanningsnivaQueryBuilder);
            boolQueryBuilder1.mustNot(excludeUtdanningsnivaQueryBuilder1);

            boolQueryBuilder.should(boolQueryBuilder1);
        }
        if (utdanningsniva.equals("Ingen")) {
            BoolQueryBuilder boolQueryBuilder1 = QueryBuilders.boolQuery();

            NestedQueryBuilder utdanningsnivaQueryBuilder = QueryBuilders.nestedQuery("utdanning",
                    QueryBuilders.existsQuery("utdanning.nusKode"), ScoreMode.None);

            boolQueryBuilder1.mustNot(utdanningsnivaQueryBuilder);
            boolQueryBuilder.should(boolQueryBuilder1);
        }
        if (utdanningsniva.equals("Videregaende")) {
            BoolQueryBuilder boolQueryBuilder1 = QueryBuilders.boolQuery();

            NestedQueryBuilder includeKompetanseQueryBuilder = QueryBuilders.nestedQuery("kompetanse",
                    QueryBuilders.matchQuery("kompetanse.kompKode", "501"), ScoreMode.None);

            NestedQueryBuilder excludeUtdanningsnivaQueryBuilder = QueryBuilders.nestedQuery("utdanning",
                    QueryBuilders.regexpQuery("utdanning.nusKode", excludeRegex), ScoreMode.None);

            boolQueryBuilder1.must(includeKompetanseQueryBuilder);
            boolQueryBuilder1.mustNot(excludeUtdanningsnivaQueryBuilder);

            boolQueryBuilder.should(boolQueryBuilder1);
        }
        if (utdanningsniva.equals("Fagskole")) {
            BoolQueryBuilder boolQueryBuilder1 = QueryBuilders.boolQuery();

            NestedQueryBuilder includeKompetanseQueryBuilder = QueryBuilders.nestedQuery("kompetanse",
                    QueryBuilders.matchQuery("kompetanse.kompKode", "506"), ScoreMode.None);

            NestedQueryBuilder excludeUtdanningsnivaQueryBuilder = QueryBuilders.nestedQuery("utdanning",
                    QueryBuilders.regexpQuery("utdanning.nusKode", excludeRegex), ScoreMode.None);

            boolQueryBuilder1.must(includeKompetanseQueryBuilder);
            boolQueryBuilder1.mustNot(excludeUtdanningsnivaQueryBuilder);

            boolQueryBuilder.should(boolQueryBuilder1);
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

    private Sokeresultat toSokeresultat(SearchResponse searchResponse) {
        LOGGER.debug("Totalt antall treff: " + searchResponse.getHits().getTotalHits());
        List<EsCv> cver = toCvList(searchResponse);
        List<Aggregering> aggregeringer = toAggregeringList(searchResponse);
        return new Sokeresultat(searchResponse.getHits().getTotalHits(), cver, aggregeringer);
    }

    private List<Aggregering> toAggregeringList(SearchResponse searchResponse) {
        Aggregations aggregations = searchResponse.getAggregations();

        List<Aggregering> aggs = new ArrayList<>();
        List<String> aggregationNames =
                aggregations.asList().stream().map(agg -> agg.getName()).collect(toList());

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
                    bucket.getDocCount(), getSubAggregation(bucket))).collect(toList());
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
                .map(hit -> mapEsCv(hit)).filter(Objects::nonNull).collect(toList());
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
                LOGGER.info("Greide ikke å utfore operasjon mot elastic search, index er ikke opprette.");
                // return fun.get();
            }
            throw (e);
        }
    }

    /**
     * Tilsvarer java.functions.Supplier bare at get metoden kan kaste IOException
     */
    private interface IOSupplier<T> {
        T get() throws IOException;
    }

    @Override
    public no.nav.arbeid.cv.kandidatsok.es.domene.EsCv hent(String kandidatnr) throws IOException {
        BoolQueryBuilder queryBuilder =
                QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("arenaKandidatnr", kandidatnr));
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(10);
        // Dette er defaulten, så egentlig unødvendig å sette:
        searchSourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
        searchSourceBuilder.sort(new FieldSortBuilder("_uid").order(SortOrder.ASC));

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(CV_INDEX);
        searchRequest.source(searchSourceBuilder);

        LOGGER.debug("HENTREQUEST: " + searchRequest.toString());

        SearchResponse searchResponse = client.search(searchRequest);
        LOGGER.debug("HENTRESPONSE: " + searchResponse);

        List<no.nav.arbeid.cv.kandidatsok.es.domene.EsCv> liste =
                StreamSupport.stream(searchResponse.getHits().spliterator(), false)
                        .map(hit -> mapEsCvHent(hit)).filter(Objects::nonNull).collect(toList());
        if (liste.size() == 0) {
            LOGGER.warn("Ikke funnet CVen");
            return new no.nav.arbeid.cv.kandidatsok.es.domene.EsCv();
        } else if (liste.size() > 1) {
            LOGGER.warn("Funnet mer enn én CV!");
            return liste.stream().findFirst().orElse(new no.nav.arbeid.cv.kandidatsok.es.domene.EsCv());
        }
        return liste.stream().findFirst().orElse(new no.nav.arbeid.cv.kandidatsok.es.domene.EsCv());
    }

    @Override
    public Sokeresultat hentKandidater(List<String> kandidatnummer) throws IOException {
        SearchResponse searchResponse = esExec(() -> search(QueryBuilders.termsQuery("arenaKandidatnr", kandidatnummer), 0, 100));
        Sokeresultat usortertSokeresultat = toSokeresultat(searchResponse);
        List<EsCv> sorterteCver = sorterSokeresultater(usortertSokeresultat.getCver(), kandidatnummer);
        return new Sokeresultat(usortertSokeresultat.getTotaltAntallTreff(), sorterteCver, usortertSokeresultat.getAggregeringer());
    }

    private List<EsCv> sorterSokeresultater(List<EsCv> cver, List<String> kandidatnr) {
        Map<String, EsCv> kandidater = cver.stream()
                .collect(toMap(EsCv::getArenaKandidatnr,
                        Function.identity()));

        return kandidatnr.stream()
                .map(kandidater::get)
                .filter(Objects::nonNull)
                .collect(toList());
    }

    private no.nav.arbeid.cv.kandidatsok.es.domene.EsCv mapEsCvHent(SearchHit hit) {
        try {
            return mapper.readValue(hit.getSourceAsString(),
                    no.nav.arbeid.cv.kandidatsok.es.domene.EsCv.class);
        } catch (IOException e) {
            LOGGER.warn("Klarte ikke å parse CV fra Elasticsearch, returnerer null", e);
            return null;
        }
    }
}
