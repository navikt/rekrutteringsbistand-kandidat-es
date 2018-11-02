package no.nav.arbeid.kandidatsok.es.client;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.RegexpQueryBuilder;
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
import org.elasticsearch.search.sort.NestedSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortMode;
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
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.SokekriterierVeiledere;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.Sokeresultat;

public class EsSokHttpService implements EsSokService {

    private static enum UseCase {
        AG_SOK, AG_HENT, VEIL_SOK, VEIL_HENT;
    }

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
        CompletionSuggestionBuilder suggestionBuilder = SuggestBuilders
                .completionSuggestion("geografiJobbonsker.geografiKodeTekst.completion")
                .text(prefix).skipDuplicates(true);

        SuggestBuilder suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion("typeahead", suggestionBuilder);
        searchSourceBuilder.suggest(suggestBuilder);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = esExec(() -> client.search(searchRequest));
        LOGGER.debug("SEARCHRESPONSE: " + searchResponse);
        CompletionSuggestion compSuggestion =
                searchResponse.getSuggest().getSuggestion("typeahead");

        return compSuggestion.getOptions().stream()
                .map(option -> option.getHit().getSourceAsString()).collect(toList());
    }

    public List<String> typeAheadYrkeJobbonsker(String prefix) throws IOException {
        return typeAhead(prefix, "yrkeJobbonsker.styrkBeskrivelse.completion");
    }

    private List<String> typeAhead(String prefix, String suggestionField) throws IOException {
        SearchRequest searchRequest = new SearchRequest(CV_INDEX);
        searchRequest.types(CV_TYPE);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        CompletionSuggestionBuilder suggestionBuilder = SuggestBuilders
                .completionSuggestion(suggestionField).text(prefix).skipDuplicates(true).size(100);

        SuggestBuilder suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion("typeahead", suggestionBuilder);
        searchSourceBuilder.suggest(suggestBuilder);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = esExec(() -> client.search(searchRequest));
        LOGGER.debug("SEARCHRESPONSE: " + searchResponse);
        CompletionSuggestion compSuggestion =
                searchResponse.getSuggest().getSuggestion("typeahead");
        return compSuggestion.getOptions().stream().map(option -> option.getText().string())
                .collect(toList());
    }

    private void matchAllQuery(BoolQueryBuilder boolQueryBuilder) {
        boolQueryBuilder.must(QueryBuilders.matchAllQuery());
    }

    @Override
    public Sokeresultat arbeidsgiverSok(Sokekriterier sk) throws IOException {

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        BoolQueryBuilder sortQueryBuilder = QueryBuilders.boolQuery();

        if (sk.isTomtSok()) {
            LOGGER.debug("MATCH ALL!");
            matchAllQuery(queryBuilder);
            return toSokeresultat(esExec(() -> search(UseCase.AG_SOK, queryBuilder, sk.fraIndex(),
                    sk.antallResultater(), null)));
        }

        if (StringUtils.isNotBlank(sk.fritekst())) {
            addFritekstToQuery(sk.fritekst(), queryBuilder);
        }

        if (isNotEmpty(sk.yrkeJobbonsker())) {
            addJobbonskerToQuery(sk.yrkeJobbonsker(), queryBuilder, sortQueryBuilder);
        }

        if (isNotEmpty(sk.stillingstitler())) {
            addStillingstitlerToQuery(sk.stillingstitler(), queryBuilder, sortQueryBuilder);
        }

        if (isNotEmpty(sk.kompetanser())) {
            addKompetanserToQuery(sk.kompetanser(), queryBuilder);
        }

        if (isNotEmpty(sk.sprak())) {
            addSprakToQuery(sk.sprak(), queryBuilder);
        }

        if (isNotEmpty(sk.geografiList())) {
            addGeografiToQuery(sk.geografiList(), queryBuilder);
        }

        if (sk.maaBoInnenforGeografi()) {
            addKommunenummerToQuery(sk.geografiList(), queryBuilder);
        }

        if (isNotEmpty(sk.totalYrkeserfaring())) {
            addYrkeserfaringToQuery(sk.totalYrkeserfaring(), queryBuilder);
        }

        if (sk.isUtdanningSet()) {
            addUtdanningToQuery(sk.utdanninger(), sk.utdanningsniva(), queryBuilder);
        }

        if (isNotEmpty(sk.styrkKoder())) {
            addStyrkKoderToQuery(sk.styrkKoder(), queryBuilder);
        }

        if (isNotEmpty(sk.nusKoder())) {
            addNusKoderToQuery(sk.nusKoder(), queryBuilder);
        }

        if (StringUtils.isNotBlank(sk.etternavn())) {
            addEtternavnToQuery(sk.etternavn(), queryBuilder);
        }

        if (isNotEmpty(sk.forerkort())) {
            addForerkortToQuery(sk.forerkort(), queryBuilder);
        }

        return toSokeresultat(esExec(() -> search(UseCase.AG_SOK, queryBuilder, sk.fraIndex(),
                sk.antallResultater(), sortQueryBuilder)));
    }

    @Override
    public Sokeresultat veilederSok(SokekriterierVeiledere sk) throws IOException {

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        BoolQueryBuilder sortQueryBuilder = QueryBuilders.boolQuery();

        if (sk.isTomtSok()) {
            LOGGER.debug("MATCH ALL!");
            matchAllQuery(queryBuilder);
            return toSokeresultat(esExec(() -> search(UseCase.VEIL_SOK, queryBuilder, sk.fraIndex(),
                    sk.antallResultater(), null)));
        }

        if (StringUtils.isNotBlank(sk.fritekst())) {
            addFritekstToQuery(sk.fritekst(), queryBuilder);
        }

        if (isNotEmpty(sk.yrkeJobbonsker())) {
            addJobbonskerToQuery(sk.yrkeJobbonsker(), queryBuilder, sortQueryBuilder);
        }

        if (isNotEmpty(sk.stillingstitler())) {
            addStillingstitlerToQuery(sk.stillingstitler(), queryBuilder, sortQueryBuilder);
        }

        if (isNotEmpty(sk.kompetanser())) {
            addKompetanserToQuery(sk.kompetanser(), queryBuilder);
        }

        if (isNotEmpty(sk.sprak())) {
            addSprakToQuery(sk.sprak(), queryBuilder);
        }

        if (isNotEmpty(sk.geografiList())) {
            addGeografiToQuery(sk.geografiList(), queryBuilder);
        }

        if (sk.maaBoInnenforGeografi()) {
            addKommunenummerToQuery(sk.geografiList(), queryBuilder);
        }

        if (isNotEmpty(sk.totalYrkeserfaring())) {
            addYrkeserfaringToQuery(sk.totalYrkeserfaring(), queryBuilder);
        }

        if (sk.isUtdanningSet()) {
            addUtdanningToQuery(sk.utdanninger(), sk.utdanningsniva(), queryBuilder);
        }

        if (isNotEmpty(sk.styrkKoder())) {
            addStyrkKoderToQuery(sk.styrkKoder(), queryBuilder);
        }

        if (isNotEmpty(sk.nusKoder())) {
            addNusKoderToQuery(sk.nusKoder(), queryBuilder);
        }

        if (StringUtils.isNotBlank(sk.etternavn())) {
            addEtternavnToQuery(sk.etternavn(), queryBuilder);
        }

        if (isNotEmpty(sk.forerkort())) {
            addForerkortToQuery(sk.forerkort(), queryBuilder);
        }

        return toSokeresultat(esExec(() -> search(UseCase.VEIL_SOK, queryBuilder, sk.fraIndex(),
                sk.antallResultater(), sortQueryBuilder)));
    }

    private void addFilterForArbeidsgivereSok(BoolQueryBuilder boolQueryBuilder) {
        System.out.println("*****************************************************");
        System.out.println("****************AG FILTER FOR SØK********************");
        System.out.println("*****************************************************");

        // Valgt mustNot for å håndtere null-verdier likt som FALSE
        addFilterForArbeidsgivereHent(boolQueryBuilder);
        boolQueryBuilder.mustNot(QueryBuilders.termQuery("fritattAgKandidatsok", Boolean.TRUE));
    }

    private void addFilterForArbeidsgivereHent(BoolQueryBuilder boolQueryBuilder) {
        // Valgt mustNot for å håndtere null-verdier likt som FALSE
        boolQueryBuilder.mustNot(QueryBuilders.termQuery("doed", Boolean.TRUE));
        boolQueryBuilder.mustNot(QueryBuilders.termsQuery("frKode", "6", "7"));
        boolQueryBuilder.must(QueryBuilders.termsQuery("formidlingsgruppekode", "ARBS", "RARBS",
                "PARBS", "JOBBS"));
        boolQueryBuilder.mustNot(QueryBuilders.termQuery("fritattKandidatsok", Boolean.TRUE));
    }

    private void addFilterForVeiledereSok(BoolQueryBuilder boolQueryBuilder) {
        System.out.println("*****************************************************");
        System.out.println("*************VEILEDER FILTER FOR SØK*****************");
        System.out.println("*****************************************************");
        // Valgt mustNot for å håndtere null-verdier likt som FALSE
        addFilterForVeiledereHent(boolQueryBuilder);
    }

    private void addFilterForVeiledereHent(BoolQueryBuilder boolQueryBuilder) {
        // Valgt mustNot for å håndtere null-verdier likt som FALSE
        boolQueryBuilder.mustNot(QueryBuilders.termQuery("doed", Boolean.TRUE));
        boolQueryBuilder.mustNot(QueryBuilders.termsQuery("frKode", "6", "7"));
        boolQueryBuilder
                .must(QueryBuilders.termsQuery("formidlingsgruppekode", "ARBS", "RARBS", "PARBS"));
        boolQueryBuilder.mustNot(QueryBuilders.termQuery("fritattKandidatsok", Boolean.TRUE));
    }

    private void addEtternavnToQuery(String etternavn, BoolQueryBuilder boolQueryBuilder) {
        boolQueryBuilder.should(new MatchQueryBuilder("etternavn", etternavn));
    }

    private void addNusKoderToQuery(List<String> nusKoder, BoolQueryBuilder boolQueryBuilder) {
        nusKoder.stream().filter(StringUtils::isNotBlank)
                .forEach(k -> addNusKodeQuery(k, boolQueryBuilder));
    }

    private void addStyrkKoderToQuery(List<String> styrkKoder, BoolQueryBuilder boolQueryBuilder) {
        styrkKoder.stream().filter(StringUtils::isNotBlank)
                .forEach(k -> addStyrkKodeQuery(k, boolQueryBuilder));
    }

    private boolean isNotEmpty(List<?> list) {
        return list != null && !list.isEmpty();
    }

    private BoolQueryBuilder makeIngenUtdanningQuery() {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        addUtdanningsnivaQuery("Ingen", queryBuilder);
        return queryBuilder;
    }


    private BoolQueryBuilder makeUtdanningQuery(List<String> utdanninger,
            List<String> utdanningsniva) {

        BoolQueryBuilder utdanningerBuilder = QueryBuilders.boolQuery();
        BoolQueryBuilder utdanningsnivaBuilder = QueryBuilders.boolQuery();

        if (isNotEmpty(utdanninger)) {
            utdanninger.stream().filter(StringUtils::isNotBlank)
                    .forEach(u -> addUtdanningerQuery(u, utdanningerBuilder));
        }

        if (isNotEmpty(utdanningsniva)) {
            utdanningsniva.stream().filter(StringUtils::isNotBlank).filter(u -> !u.equals("Ingen"))
                    .forEach(u -> addUtdanningsnivaQuery(u, utdanningsnivaBuilder));
        }


        BoolQueryBuilder utdanningQueryBuilder = QueryBuilders.boolQuery();

        utdanningQueryBuilder.must(utdanningerBuilder);
        utdanningQueryBuilder.must(utdanningsnivaBuilder);

        return utdanningQueryBuilder;
    }

    private void addUtdanningToQuery(List<String> utdanninger, List<String> utdanningsniva,
            BoolQueryBuilder boolQueryBuilder) {
        if (utdanningsniva.contains("Ingen")) {
            if (utdanningsniva.size() == 1 && utdanninger.isEmpty()) {
                boolQueryBuilder.must(makeIngenUtdanningQuery());

            } else {
                BoolQueryBuilder utdanningEllerIngenQuery = QueryBuilders.boolQuery();

                utdanningEllerIngenQuery.should(makeIngenUtdanningQuery());
                utdanningEllerIngenQuery.should(makeUtdanningQuery(utdanninger, utdanningsniva));

                boolQueryBuilder.must(utdanningEllerIngenQuery);
            }

        } else {
            boolQueryBuilder.must(makeUtdanningQuery(utdanninger, utdanningsniva));
        }

        LOGGER.debug("ADDING utdanningsniva");
    }

    private void addYrkeserfaringToQuery(List<String> yrkeserfaring,
            BoolQueryBuilder boolQueryBuilder) {
        BoolQueryBuilder yrkeserfaringQueryBuilder = QueryBuilders.boolQuery();

        yrkeserfaring.stream().filter(StringUtils::isNotBlank)
                .forEach(te -> addTotalYrkeserfaringQuery(te, yrkeserfaringQueryBuilder));

        boolQueryBuilder.must(yrkeserfaringQueryBuilder);
    }

    private void addGeografiToQuery(List<String> geografi, BoolQueryBuilder boolQueryBuilder) {
        geografi.stream().filter(StringUtils::isNotBlank)
                .forEach(g -> addGeografiQuery(g, boolQueryBuilder));
    }

    private void addKommunenummerToQuery(List<String> geografi, BoolQueryBuilder boolQueryBuilder) {

        BoolQueryBuilder kommunenummerQueryBuilder = QueryBuilders.boolQuery();

        geografi.stream().filter(StringUtils::isNotBlank)
                .forEach(g -> addKommunenummerQuery(g, kommunenummerQueryBuilder));

        boolQueryBuilder.must(kommunenummerQueryBuilder);
    }

    private void addSprakToQuery(List<String> sprak, BoolQueryBuilder boolQueryBuilder) {
        sprak.stream().filter(StringUtils::isNotBlank)
                .forEach(k -> addSprakQuery(k, boolQueryBuilder));
    }

    private void addKompetanserToQuery(List<String> kompetanser,
            BoolQueryBuilder boolQueryBuilder) {
        kompetanser.stream().filter(StringUtils::isNotBlank)
                .forEach(k -> addKompetanseQuery(k, boolQueryBuilder));
    }

    private void addStillingstitlerToQuery(List<String> stillingstitler,
            BoolQueryBuilder boolQueryBuilder, BoolQueryBuilder sortQueryBuilder) {
        stillingstitler.stream().filter(StringUtils::isNotBlank)
                .forEach(s -> addStillingsTitlerQuery(s, boolQueryBuilder, true, sortQueryBuilder));
    }

    private void addJobbonskerToQuery(List<String> jobbonsker, BoolQueryBuilder boolQueryBuilder,
            BoolQueryBuilder sortQueryBuilder) {
        BoolQueryBuilder yrkeJobbonskerBoolQueryBuilder = QueryBuilders.boolQuery();

        jobbonsker.stream().filter(StringUtils::isNotBlank)
                .forEach(y -> addYrkeJobbonskerQuery(y, yrkeJobbonskerBoolQueryBuilder));

        boolQueryBuilder.must(yrkeJobbonskerBoolQueryBuilder);

        jobbonsker.stream().filter(StringUtils::isNotBlank).forEach(
                y -> addStillingsTitlerQuery(y, boolQueryBuilder, false, sortQueryBuilder));
        LOGGER.debug("ADDING onsket stilling");
    }

    private void addFritekstToQuery(String fritekst, BoolQueryBuilder boolQueryBuilder) {
        MultiMatchQueryBuilder fritekstQueryBuilder =
                QueryBuilders.multiMatchQuery(fritekst, "fritekst");
        boolQueryBuilder.must(fritekstQueryBuilder);
        LOGGER.debug("ADDING fritekst");
    }

    private void addForerkortToQuery(List<String> forerkort, BoolQueryBuilder boolQueryBuilder) {
        forerkort.stream().filter(StringUtils::isNotBlank)
                .forEach(s -> addForerkortQuery(s, boolQueryBuilder));
    }

    private void addYrkeJobbonskerQuery(String yrkeJobbonske, BoolQueryBuilder boolQueryBuilder) {
        NestedQueryBuilder yrkeJobbonskeQueryBuilder = QueryBuilders.nestedQuery("yrkeJobbonsker",
                QueryBuilders.matchQuery("yrkeJobbonsker.styrkBeskrivelse", yrkeJobbonske)
                        .operator(Operator.AND),
                ScoreMode.Total);
        boolQueryBuilder.should(yrkeJobbonskeQueryBuilder);
    }

    private void addStillingsTitlerQuery(String stillingstittel, BoolQueryBuilder boolQueryBuilder,
            boolean must, BoolQueryBuilder sortBoolQueryBuilder) {
        NestedQueryBuilder yrkeserfaringQueryBuilder = QueryBuilders.nestedQuery("yrkeserfaring",
                QueryBuilders.matchQuery("yrkeserfaring.styrkKodeStillingstittel", stillingstittel)
                        .operator(Operator.AND),
                ScoreMode.Total);
        if (must) {
            boolQueryBuilder.must(yrkeserfaringQueryBuilder);
        } else {
            boolQueryBuilder.should(yrkeserfaringQueryBuilder);
        }
        LOGGER.debug("ADDING yrkeserfaring");

        MatchQueryBuilder matchQueryBuilder =
                QueryBuilders.matchQuery("yrkeserfaring.styrkKodeStillingstittel", stillingstittel);
        sortBoolQueryBuilder.should(matchQueryBuilder);
    }

    private void addUtdanningerQuery(String utdanning, BoolQueryBuilder boolQueryBuilder) {
        NestedQueryBuilder utdanningQueryBuilder = QueryBuilders.nestedQuery("utdanning",
                QueryBuilders.matchQuery("utdanning.nusKodeGrad", utdanning).operator(Operator.AND),
                ScoreMode.Total);
        boolQueryBuilder.must(utdanningQueryBuilder);
        LOGGER.debug("ADDING utdanning");
    }

    private void addKompetanseQuery(String kompetanse, BoolQueryBuilder boolQueryBuilder) {
        NestedQueryBuilder kompetanseQueryBuilder = QueryBuilders.nestedQuery("samletKompetanse",
                QueryBuilders.matchQuery("samletKompetanse.samletKompetanseTekst", kompetanse)
                        .operator(Operator.AND),
                ScoreMode.Total);
        boolQueryBuilder.must(kompetanseQueryBuilder);
        LOGGER.debug("ADDING kompetanse");
    }

    private void addSprakQuery(String sprak, BoolQueryBuilder boolQueryBuilder) {
        NestedQueryBuilder sprakQueryBuilder = QueryBuilders.nestedQuery("sprak",
                QueryBuilders.matchQuery("sprak.sprakKodeTekst", sprak).operator(Operator.AND),
                ScoreMode.Total);
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
                QueryBuilders.regexpQuery("geografiJobbonsker.geografiKode", regex),
                ScoreMode.Total);
        boolQueryBuilder.must(geografiQueryBuilder);
        LOGGER.debug("ADDING geografiJobbonske");
    }

    private void addForerkortQuery(String forerkort, BoolQueryBuilder boolQueryBuilder) {
        NestedQueryBuilder forerkortQueryBuilder = QueryBuilders.nestedQuery("forerkort",
                QueryBuilders.termQuery("forerkort.forerkortKodeKlasse", forerkort),
                ScoreMode.Total);
        boolQueryBuilder.must(forerkortQueryBuilder);
        LOGGER.debug("ADDING førerkort");
    }

    private void addKommunenummerQuery(String geografi, BoolQueryBuilder boolQueryBuilder) {
        String[] geografiKoder = geografi.split("\\.");
        String regex = "";

        if (geografiKoder.length == 1) {

            if (geografiKoder[0].length() < 4) {
                return;
            }

            if (geografiKoder[0].startsWith("NO0")) {
                String fylkeskode = geografiKoder[0].substring(3, 4);
                regex += fylkeskode + ".*";
            } else {
                String fylkeskode = geografiKoder[0].substring(2, 4);
                regex += fylkeskode + ".*";
            }
        } else {

            if (geografiKoder[1].startsWith("0")) {
                regex += geografiKoder[1].substring(1, 4);
            } else {
                regex += geografiKoder[1].substring(0, 4);
            }
        }

        RegexpQueryBuilder kommunenummerQueryBuilder =
                QueryBuilders.regexpQuery("kommunenummerkw", regex);
        boolQueryBuilder.should(kommunenummerQueryBuilder);

        LOGGER.debug("ADDING kommunenummer");
    }

    private void addTotalYrkeserfaringQuery(String totalYrkeserfaring,
            BoolQueryBuilder boolQueryBuilder) {
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

            NestedQueryBuilder includeUtdanningsnivaQueryBuilder = QueryBuilders.nestedQuery(
                    "utdanning", QueryBuilders.regexpQuery("utdanning.nusKode", searchRegex),
                    ScoreMode.Total);

            NestedQueryBuilder excludeUtdanningsnivaQueryBuilder1 = QueryBuilders.nestedQuery(
                    "utdanning", QueryBuilders.regexpQuery("utdanning.nusKode", excludeRegex),
                    ScoreMode.Total);

            boolQueryBuilder1.must(includeUtdanningsnivaQueryBuilder);
            boolQueryBuilder1.mustNot(excludeUtdanningsnivaQueryBuilder1);

            boolQueryBuilder.should(boolQueryBuilder1);
        }
        if (utdanningsniva.equals("Ingen")) {
            BoolQueryBuilder boolQueryBuilder1 = QueryBuilders.boolQuery();

            NestedQueryBuilder utdanningsnivaQueryBuilder = QueryBuilders.nestedQuery("utdanning",
                    QueryBuilders.existsQuery("utdanning.nusKode"), ScoreMode.Total);

            boolQueryBuilder1.mustNot(utdanningsnivaQueryBuilder);
            boolQueryBuilder.should(boolQueryBuilder1);
        }
        if (utdanningsniva.equals("Videregaende")) {
            BoolQueryBuilder boolQueryBuilder1 = QueryBuilders.boolQuery();

            NestedQueryBuilder includeKompetanseQueryBuilder = QueryBuilders.nestedQuery(
                    "kompetanse", QueryBuilders.matchQuery("kompetanse.kompKode", "501"),
                    ScoreMode.Total);

            NestedQueryBuilder excludeUtdanningsnivaQueryBuilder = QueryBuilders.nestedQuery(
                    "utdanning", QueryBuilders.regexpQuery("utdanning.nusKode", excludeRegex),
                    ScoreMode.Total);

            boolQueryBuilder1.must(includeKompetanseQueryBuilder);
            boolQueryBuilder1.mustNot(excludeUtdanningsnivaQueryBuilder);

            boolQueryBuilder.should(boolQueryBuilder1);
        }
        if (utdanningsniva.equals("Fagskole")) {
            BoolQueryBuilder boolQueryBuilder1 = QueryBuilders.boolQuery();

            NestedQueryBuilder includeKompetanseQueryBuilder = QueryBuilders.nestedQuery(
                    "kompetanse", QueryBuilders.matchQuery("kompetanse.kompKode", "506"),
                    ScoreMode.Total);

            NestedQueryBuilder excludeUtdanningsnivaQueryBuilder = QueryBuilders.nestedQuery(
                    "utdanning", QueryBuilders.regexpQuery("utdanning.nusKode", excludeRegex),
                    ScoreMode.Total);

            boolQueryBuilder1.must(includeKompetanseQueryBuilder);
            boolQueryBuilder1.mustNot(excludeUtdanningsnivaQueryBuilder);

            boolQueryBuilder.should(boolQueryBuilder1);
        }
    }

    private void addNusKodeQuery(String nusKode, BoolQueryBuilder boolQueryBuilder) {
        NestedQueryBuilder nusKodeQueryBuilder = QueryBuilders.nestedQuery("utdanning",
                QueryBuilders.termQuery("utdanning.nusKode", nusKode), ScoreMode.Total);
        boolQueryBuilder.must(nusKodeQueryBuilder);
        LOGGER.debug("ADDING nuskode");
    }

    private void addStyrkKodeQuery(String styrkKode, BoolQueryBuilder boolQueryBuilder) {
        NestedQueryBuilder styrkKodeQueryBuilder = QueryBuilders.nestedQuery("yrkeserfaring",
                QueryBuilders.termQuery("yrkeserfaring.styrkKode", styrkKode), ScoreMode.Total);
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
            List<? extends Bucket> buckets =
                    getBucketsForInnerAggregation(aggregations, aggregationName);
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
        return ((Terms) ((Nested) aggregations.get(aggregationName)).getAggregations()
                .get("nested")).getBuckets();
    }

    private SearchResponse search(UseCase useCase, BoolQueryBuilder queryBuilder, int from,
            int size, BoolQueryBuilder sortQueryBuilder) throws IOException {

        switch (useCase) {
            case AG_SOK:
                addFilterForArbeidsgivereSok(queryBuilder);
                break;
            case AG_HENT:
                addFilterForArbeidsgivereHent(queryBuilder);
                break;
            case VEIL_SOK:
                addFilterForVeiledereSok(queryBuilder);
                break;
            case VEIL_HENT:
                addFilterForVeiledereHent(queryBuilder);
                break;
            default:
                addFilterForVeiledereSok(queryBuilder);
        }

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);

        // Sortering på nyeste relevante arbeidserfaring
        if (sortQueryBuilder != null && sortQueryBuilder.hasClauses()) {
            sortQueryBuilder.minimumShouldMatch(1);
            FieldSortBuilder fieldSortBuilder = new FieldSortBuilder("yrkeserfaring.tilDato")
                    .setNestedSort(
                            new NestedSortBuilder("yrkeserfaring").setFilter(sortQueryBuilder))
                    .sortMode(SortMode.MAX).order(SortOrder.DESC);

            searchSourceBuilder.sort(fieldSortBuilder);
        }

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
        LOGGER.info("Søketid: {}", searchResponse.getTook());
        return searchResponse;
    }

    private EsCv mapEsCv(SearchHit hit) {
        try {
            EsCv esCv = mapper.readValue(hit.getSourceAsString(), EsCv.class);
            LOGGER.debug("Score for {} er {} ", esCv.getArenaKandidatnr(), hit.getScore());
            esCv.setScore(hit.getScore());
            return esCv;
        } catch (IOException e) {
            LOGGER.warn("Klarte ikke å parse CV {} fra Elasticsearch, returnerer null",
                    hit.getSourceAsString(), e);
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
            if (e.status().getStatus() == 404
                    && e.getMessage().contains("index_not_found_exception")) {
                LOGGER.info(
                        "Greide ikke å utfore operasjon mot elastic search, index er ikke opprette.");
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
    public Optional<no.nav.arbeid.cv.kandidatsok.es.domene.EsCv> arbeidsgiverHent(String kandidatnr)
            throws IOException {
        return hentFelles(true, kandidatnr);
    }

    @Override
    public Optional<no.nav.arbeid.cv.kandidatsok.es.domene.EsCv> veilederHent(String kandidatnr)
            throws IOException {
        return hentFelles(false, kandidatnr);
    }

    private Optional<no.nav.arbeid.cv.kandidatsok.es.domene.EsCv> hentFelles(boolean isAg,
            String kandidatnr) throws IOException {

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("arenaKandidatnr", kandidatnr));
        if (isAg) {
            addFilterForArbeidsgivereHent(queryBuilder);
        } else {
            addFilterForVeiledereHent(queryBuilder);
        }

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
            LOGGER.warn("Finner ikke CV for kandidat {}", kandidatnr);
            return Optional.empty();
        } else if (liste.size() > 1) {
            LOGGER.error("Fant mer enn én CV for kandidat {}. Fant {} CVer: {}", kandidatnr,
                    liste.size(),
                    liste.stream()
                            .map(cv -> String.format("(person_id: %s, kandidatnr: %s)",
                                    cv.getArenaPersonId(), cv.getArenaKandidatnr()))
                            .collect(Collectors.joining(", ")));
        }
        return liste.stream().findFirst();
    }

    private BoolQueryBuilder kandidatnrQuery(List<String> kandidatnummer) {
        return QueryBuilders.boolQuery()
                .must(QueryBuilders.termsQuery("arenaKandidatnr", kandidatnummer));
    }

    @Override
    public Sokeresultat arbeidsgiverHentKandidater(List<String> kandidatnummer) throws IOException {
        SearchResponse searchResponse =
                esExec(() -> search(UseCase.AG_SOK, kandidatnrQuery(kandidatnummer), 0, 100, null));
        Sokeresultat usortertSokeresultat = toSokeresultat(searchResponse);
        List<EsCv> sorterteCver = sorterSokeresultaterBasertPaaRequestRekkefolge(
                usortertSokeresultat.getCver(), kandidatnummer);
        return new Sokeresultat(usortertSokeresultat.getTotaltAntallTreff(), sorterteCver,
                usortertSokeresultat.getAggregeringer());
    }

    @Override
    public Sokeresultat veilederHentKandidater(List<String> kandidatnummer) throws IOException {
        SearchResponse searchResponse = esExec(
                () -> search(UseCase.VEIL_SOK, kandidatnrQuery(kandidatnummer), 0, 100, null));
        Sokeresultat usortertSokeresultat = toSokeresultat(searchResponse);
        List<EsCv> sorterteCver = sorterSokeresultaterBasertPaaRequestRekkefolge(
                usortertSokeresultat.getCver(), kandidatnummer);
        return new Sokeresultat(usortertSokeresultat.getTotaltAntallTreff(), sorterteCver,
                usortertSokeresultat.getAggregeringer());
    }

    private List<EsCv> sorterSokeresultaterBasertPaaRequestRekkefolge(List<EsCv> cver,
            List<String> kandidatrekkefolge) {
        Map<String, EsCv> kandidater =
                cver.stream().collect(toMap(EsCv::getArenaKandidatnr, Function.identity()));

        return kandidatrekkefolge.stream().map(kandidater::get).filter(Objects::nonNull)
                .collect(toList());
    }

    private no.nav.arbeid.cv.kandidatsok.es.domene.EsCv mapEsCvHent(SearchHit hit) {
        try {
            return mapper.readValue(hit.getSourceAsString(),
                    no.nav.arbeid.cv.kandidatsok.es.domene.EsCv.class);
        } catch (IOException e) {
            LOGGER.warn(
                    "Klarte ikke å parse CV fra Elasticsearch id {}, docId {}, CV: {}, returnerer null",
                    hit.getId(), hit.docId(), hit.getSourceAsString(), e);
            return null;
        }
    }
}
