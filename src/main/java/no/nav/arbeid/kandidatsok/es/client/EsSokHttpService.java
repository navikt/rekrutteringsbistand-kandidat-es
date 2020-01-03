package no.nav.arbeid.kandidatsok.es.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.NestedSortBuilder;
import org.elasticsearch.search.sort.SortMode;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class EsSokHttpService implements EsSokService, AutoCloseable {

    private static final String CV_TYPE = "cvtype";
    private static final Logger LOGGER = LoggerFactory.getLogger(EsSokHttpService.class);
    private final RestHighLevelClient client;
    private final ObjectMapper mapper;
    private final String indexName;

    public EsSokHttpService(RestHighLevelClient client, ObjectMapper objectMapper, String indexName) {
        this.client = client;
        this.mapper = objectMapper;
        this.indexName = indexName;
    }

    @Override
    public List<String> typeAheadSprak(String prefix) {
        return typeAhead(prefix, "sprak.sprakKodeTekst.completion");
    }

    @Override
    public List<String> typeAheadKompetanse(String prefix) {
        return typeAhead(prefix, "samletKompetanseObj.samletKompetanseTekst.completion");
    }

    @Override
    public List<String> typeAheadNavkontor(String searchTerm) {
        return ngramTypeAhead(searchTerm, "navkontor.text", "navkontor");
    }

    @Override
    public List<String> typeAheadUtdanning(String prefix) {
        return typeAhead(prefix, "utdanning.nusKodeGrad.completion");
    }

    @Override
    public List<String> typeAheadYrkeserfaring(String prefix) {
        return typeAhead(prefix, "yrkeserfaring.styrkKodeStillingstittel.completion");
    }

    @Override
    public List<String> typeAheadGeografi(String prefix) {
        try {
            SearchRequest searchRequest = new SearchRequest(indexName);
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
        } catch (IOException ioe) {
            throw new ElasticException(ioe);
        }
    }

    public List<String> typeAheadYrkeJobbonsker(String prefix) {
        return typeAhead(prefix, "yrkeJobbonskerObj.styrkBeskrivelse.completion");
    }

    private List<String> typeAhead(String prefix, String suggestionField) {
        try {
            SearchRequest searchRequest = new SearchRequest(indexName);
            searchRequest.types(CV_TYPE);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.fetchSource(false);
            CompletionSuggestionBuilder suggestionBuilder =
                    SuggestBuilders.completionSuggestion(suggestionField).text(prefix)
                            .skipDuplicates(true).size(100);

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
        } catch (IOException ioe) {
            throw new ElasticException(ioe);
        }
    }

    private List<String> ngramTypeAhead(String searchTerm, String searchField, String sourceField) {
        try {
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery(searchField, searchTerm).slop(5));


            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(boolQueryBuilder)
                    .aggregation(AggregationBuilders.terms(sourceField).field(sourceField))
                    .size(0);

            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices(indexName);
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = esExec(() -> client.search(searchRequest));
            LOGGER.debug("SEARCHRESPONSE: " + searchResponse);

            List<Aggregering> aggs = toAggregeringList(searchResponse);
            List<String> aggregateList = new ArrayList<>();
            aggs.forEach(a -> a.getFelt().forEach(f -> aggregateList.add(f.getFeltnavn())));
            LOGGER.debug("AGGREGATELIST: " + aggregateList);

            return aggregateList;
        } catch (IOException ioe) {
            throw new ElasticException(ioe);
        }

    }

    private void matchAllQuery(BoolQueryBuilder boolQueryBuilder) {
        boolQueryBuilder.must(QueryBuilders.matchAllQuery());
    }

    @Override
    public Sokeresultat arbeidsgiverSok(Sokekriterier sk) {
        try {

            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
            BoolQueryBuilder sortQueryBuilder = QueryBuilders.boolQuery();

            if (sk.isTomtSok()) {
                LOGGER.debug("MATCH ALL!");
                matchAllQuery(queryBuilder);
                return toSokeresultat(esExec(() -> search(UseCase.AG_SOK, queryBuilder,
                        sk.fraIndex(), sk.antallResultater(), null)));
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

            if (isNotEmpty(sk.forerkort())) {
                addForerkortToQuery(sk.forerkort(), queryBuilder);
            }

            return toSokeresultat(esExec(() -> search(UseCase.AG_SOK, queryBuilder, sk.fraIndex(),
                    sk.antallResultater(), sortQueryBuilder)));
        } catch (IOException ioe) {
            throw new ElasticException(ioe);
        }
    }

    @Override
    public Optional<EsCv> veilederSokPaaFnr(String fnr) {
        try {
            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
            queryBuilder.must(QueryBuilders.termQuery("fodselsnummer", fnr));
            addFilterForVeiledereSok(queryBuilder);

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(queryBuilder);

            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices(indexName);
            searchRequest.source(searchSourceBuilder);

            LOGGER.debug("SEARCHREQUEST: " + searchRequest.toString());

            SearchResponse searchResponse = client.search(searchRequest);
            LOGGER.debug("SEARCHRESPONSE: " + searchResponse);
            LOGGER.info("Søketid: {}", searchResponse.getTook());
            LOGGER.debug("Totalt antall treff: " + searchResponse.getHits().getTotalHits());
            List<EsCv> cver = toCvList(searchResponse);
            if (cver.size() > 1) {
                LOGGER.warn("Har fått treff på mer enn 1 kandidat ved søk med fødselsnummer");
            }
            if (cver.size() == 0) {
                return Optional.empty();
            }
            return Optional.of(cver.iterator().next());
        } catch (IOException ioe) {
            throw new ElasticException(ioe);
        }
    }

    @Override
    public Sokeresultat veilederSok(SokekriterierVeiledere sk) {

        try {
            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
            BoolQueryBuilder sortQueryBuilder = QueryBuilders.boolQuery();

            if (sk.isTomtSok()) {
                LOGGER.debug("MATCH ALL!");
                matchAllQuery(queryBuilder);
                return toSokeresultat(esExec(() -> search(UseCase.VEIL_SOK, queryBuilder,
                        sk.fraIndex(), sk.antallResultater(), null)));
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

            if (StringUtils.isNotBlank(sk.etternavn())) {
                addEtternavnToQuery(sk.etternavn(), queryBuilder);
            }

            if (isNotEmpty(sk.forerkort())) {
                addForerkortToQuery(sk.forerkort(), queryBuilder);
            }

            if (isNotEmpty(sk.kvalifiseringsgruppeKoder())) {
                addKvalifiseringsgruppeKoderToQuery(sk.kvalifiseringsgruppeKoder(), queryBuilder);
            }

            if (isNotEmpty(sk.navkontor())) {
                addNavkontorToQuery(sk.navkontor(), queryBuilder);
            }

            if (isNotEmpty(sk.veiledere())) {
                addVeiledereToQuery(sk.veiledere(), queryBuilder);
            }

            if (isNotEmpty(sk.hovedmaalKode())) {
                addHovedmalToQuery(sk.hovedmaalKode(), queryBuilder);
            }

            if (sk.isAntallAarFraSet() && sk.isAntallAarTilSet()) {
                addFodselsdatoToQuery(sk.antallAarFra(), sk.antallAarTil(), queryBuilder);
            } else if (sk.isAntallAarFraSet()) {
                addFodselsdatoToQuery(sk.antallAarFra(), null, queryBuilder);
            } else if (sk.isAntallAarTilSet()) {
                addFodselsdatoToQuery(null, sk.antallAarTil(), queryBuilder);
            }

            if (sk.isTilretteleggingsbehovSet()) {
                addFilterForTilretteleggingsbehov(queryBuilder, sk.getTilretteleggingsbehov());
            }

            return toSokeresultat(esExec(() -> search(UseCase.VEIL_SOK, queryBuilder, sk.fraIndex(),
                    sk.antallResultater(), sortQueryBuilder)));
        } catch (IOException ioe) {
            throw new ElasticException(ioe);
        }
    }

    private void addFilterForTilretteleggingsbehov(BoolQueryBuilder boolQueryBuilder, Boolean value) {
        boolQueryBuilder.filter(QueryBuilders.termQuery("tilretteleggingsbehov", value));
    }

    private void addFilterForArbeidsgivereSok(BoolQueryBuilder boolQueryBuilder) {
        boolQueryBuilder.filter(QueryBuilders.termQuery("synligForArbeidsgiverSok", Boolean.TRUE));
    }

    private void addFilterForArbeidsgivereHent(BoolQueryBuilder boolQueryBuilder) {
        BoolQueryBuilder innerQuery = QueryBuilders.boolQuery();
        innerQuery.should(QueryBuilders.termQuery("synligForArbeidsgiverSok", Boolean.TRUE));
        innerQuery.should(QueryBuilders.termQuery("synligForVeilederSok", Boolean.TRUE));
        innerQuery.minimumShouldMatch(1);
        boolQueryBuilder.filter(innerQuery);
    }

    private void addFilterForVeiledereSok(BoolQueryBuilder boolQueryBuilder) {
        boolQueryBuilder.filter(QueryBuilders.termQuery("synligForVeilederSok", Boolean.TRUE));
    }

    private void addFilterForVeiledereHent(BoolQueryBuilder boolQueryBuilder) {
        boolQueryBuilder.filter(QueryBuilders.termQuery("synligForVeilederSok", Boolean.TRUE));
    }

    private void addEtternavnToQuery(String etternavn, BoolQueryBuilder boolQueryBuilder) {
        boolQueryBuilder.should(new MatchQueryBuilder("etternavn", etternavn));
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

        BoolQueryBuilder geografiQueryBuilder = QueryBuilders.boolQuery();

        geografi.stream().filter(StringUtils::isNotBlank)
                .forEach(g -> addGeografiQuery(g, geografiQueryBuilder));

        boolQueryBuilder.must(geografiQueryBuilder);
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
        BoolQueryBuilder innerBoolQuery = QueryBuilders.boolQuery();
        kompetanser.stream().filter(StringUtils::isNotBlank)
                .forEach(k -> addKompetanseQuery(k, innerBoolQuery));
        boolQueryBuilder.must(innerBoolQuery);
    }

    private void addFodselsdatoToQuery(Integer antallAarFra, Integer antallAarTil, BoolQueryBuilder boolQueryBuilder) {
        if (antallAarFra != null && antallAarTil != null) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery("fodselsdato").gte("now-" + antallAarTil + "y/d").lte("now-" + antallAarFra + "y/d"));
        } else if (antallAarFra != null) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery("fodselsdato").gte("now-200y/d").lte("now-" + antallAarFra + "y/d"));
        } else if (antallAarTil != null) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery("fodselsdato").gte("now-" + antallAarTil + "y/d").lte("now"));
        } else {
            //noop
        }
    }

    private void addStillingstitlerToQuery(List<String> stillingstitler,
                                           BoolQueryBuilder boolQueryBuilder, BoolQueryBuilder sortQueryBuilder) {
        BoolQueryBuilder innerBoolQuery = QueryBuilders.boolQuery();
        stillingstitler.stream().filter(StringUtils::isNotBlank)
                .forEach(s -> addStillingsTittelQuery(s, innerBoolQuery, sortQueryBuilder));
        boolQueryBuilder.must(innerBoolQuery);
    }

    private void addJobbonskerToQuery(List<String> jobbonsker, BoolQueryBuilder boolQueryBuilder,
                                      BoolQueryBuilder sortQueryBuilder) {
        BoolQueryBuilder yrkeJobbonskerBoolQueryBuilder = QueryBuilders.boolQuery();

        jobbonsker.stream().filter(StringUtils::isNotBlank)
                .forEach(y -> addYrkeJobbonskerQuery(y, yrkeJobbonskerBoolQueryBuilder));

        boolQueryBuilder.must(yrkeJobbonskerBoolQueryBuilder);

        jobbonsker.stream().filter(StringUtils::isNotBlank).forEach(
                y -> addStillingsTittelQuery(y, boolQueryBuilder, sortQueryBuilder));
        LOGGER.debug("ADDING onsket stilling");
    }

    private void addFritekstToQuery(String fritekst, BoolQueryBuilder boolQueryBuilder) {
        MatchQueryBuilder fritekstQueryBuilder =
                QueryBuilders.matchQuery("fritekst", fritekst);
        boolQueryBuilder.must(fritekstQueryBuilder);
        LOGGER.debug("ADDING fritekst");
    }

    private void addForerkortToQuery(List<String> forerkort, BoolQueryBuilder boolQueryBuilder) {
        BoolQueryBuilder innerBoolQueryBuilder = QueryBuilders.boolQuery();
        Set<String> expandedForerkort = expandForerkort(forerkort);
        expandedForerkort.stream().filter(StringUtils::isNotBlank)
                .forEach(s -> addForerkortQuery(s, innerBoolQueryBuilder));
        boolQueryBuilder.must(innerBoolQueryBuilder);
    }

    private Set<String> expandForerkort(List<String> forerkort) {
        Map<String, Set<String>> forerkortMap = new HashMap<>() {
            {
                put("A1 - Lett motorsykkel", Set.of("AM - Moped"));
                put("A2 - Mellomtung motorsykkel", Set.of("AM - Moped", "A1 - Lett motorsykkel"));
                put("A - Tung motorsykkel", Set.of("AM - Moped", "A1 - Lett motorsykkel", "A2 - Mellomtung motorsykkel"));
                put("B - Personbil", Set.of("T - Traktor", "S - Snøscooter", "AM - Moped"));
                put("BE - Personbil med tilhenger", Set.of("B - Personbil", "T - Traktor", "S - Snøscooter", "AM - Moped"));
                put("C1 - Lett lastebil", Set.of("B - Personbil", "T - Traktor", "S - Snøscooter", "AM - Moped"));
                put("C1E - Lett lastebil med tilhenger", Set.of("C1 - Lett lastebil", "BE - Personbil med tilhenger", "B - Personbil", "T - Traktor", "S - Snøscooter", "AM - Moped"));
                put("C - Lastebil", Set.of("C1 - Lett lastebil", "B - Personbil", "T - Traktor", "S - Snøscooter", "AM - Moped"));
                put("CE - Lastebil med tilhenger", Set.of("C - Lastebil", "C1E - Lett lastebil med tilhenger", "C1 - Lett lastebil", "BE - Personbil med tilhenger", "B - Personbil", "T - Traktor", "S - Snøscooter", "AM - Moped"));
                put("D1 - Minibuss", Set.of("B - Personbil", "T - Traktor", "S - Snøscooter", "AM - Moped"));
                put("D1E - Minibuss med tilhenger", Set.of("D1 - Minibuss", "BE - Personbil med tilhenger", "B - Personbil", "T - Traktor", "S - Snøscooter", "AM - Moped"));
                put("D - Buss", Set.of("D1 - Minibuss", "B - Personbil", "T - Traktor", "S - Snøscooter", "AM - Moped"));
                put("DE - Buss med tilhenger", Set.of("D - Buss", "D1E - Minibuss med tilhenger", "D1 - Minibuss", "BE - Personbil med tilhenger", "B - Personbil", "T - Traktor", "S - Snøscooter", "AM - Moped"));
            }
        };
        Set<String> result = new HashSet<>();
        forerkort.forEach(f -> {
            forerkortMap.forEach((key, value) -> {
                if (value.contains(f)) {
                    result.add(key);
                }
            });
            result.add(f);
        });
        return result;
    }

    private void addKvalifiseringsgruppeKoderToQuery(List<String> kvalifiseringsgruppeKoder, BoolQueryBuilder boolQueryBuilder) {
        BoolQueryBuilder innerBoolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(innerBoolQueryBuilder);
        kvalifiseringsgruppeKoder.stream().filter(StringUtils::isNotBlank)
                .forEach(s -> addKvalifiseringsgruppekodeQuery(s, innerBoolQueryBuilder));
    }

    private void addNavkontorToQuery(List<String> navkontor, BoolQueryBuilder boolQueryBuilder) {
        BoolQueryBuilder innerBoolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(innerBoolQueryBuilder);
        navkontor.stream().filter(StringUtils::isNotBlank)
                .forEach(s -> addNavkontorQuery(s, innerBoolQueryBuilder));
    }

    private void addHovedmalToQuery(List<String> hovedmal, BoolQueryBuilder boolQueryBuilder) {
        BoolQueryBuilder innerBoolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(innerBoolQueryBuilder);
        hovedmal.stream().filter((StringUtils::isNotBlank))
                .forEach(s -> addHovedmalToQuery(s, innerBoolQueryBuilder));
    }

    private void addVeiledereToQuery(List<String> veiledere, BoolQueryBuilder boolQueryBuilder) {
        BoolQueryBuilder innerBoolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(innerBoolQueryBuilder);
        veiledere.stream().filter(StringUtils::isNotBlank)
                .forEach(s -> addVeilederToQuery(s, innerBoolQueryBuilder));
    }

    private void addYrkeJobbonskerQuery(String yrkeJobbonske, BoolQueryBuilder boolQueryBuilder) {
        boolQueryBuilder.should(QueryBuilders.matchQuery("yrkeJobbonskerObj.sokeTitler", yrkeJobbonske).operator(Operator.AND));
    }

    private void addStillingsTittelQuery(String stillingstittel, BoolQueryBuilder boolQueryBuilder,
                                         BoolQueryBuilder sortBoolQueryBuilder) {
        NestedQueryBuilder yrkeserfaringQueryBuilder = QueryBuilders.nestedQuery("yrkeserfaring",
                QueryBuilders.matchQuery("yrkeserfaring.sokeTitler", stillingstittel)
                        .operator(Operator.AND),
                ScoreMode.Total);

        boolQueryBuilder.should(yrkeserfaringQueryBuilder);
        LOGGER.debug("ADDING yrkeserfaring");

        MatchQueryBuilder matchQueryBuilder =
                QueryBuilders.matchQuery("yrkeserfaring.sokeTitler", stillingstittel);
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
        boolQueryBuilder.should(QueryBuilders.matchQuery("samletKompetanseObj.samletKompetanseTekst", kompetanse).operator(Operator.AND));
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

        if (geografiKoder.length == 1) {
            regex += "|" + geografiKoder[0] + ".*";
        } else {
            regex += "|" + geografiKoder[0];
        }

        NestedQueryBuilder geografiQueryBuilder = QueryBuilders.nestedQuery("geografiJobbonsker",
                QueryBuilders.regexpQuery("geografiJobbonsker.geografiKode", regex),
                ScoreMode.Total);
        boolQueryBuilder.should(geografiQueryBuilder);
        LOGGER.debug("ADDING geografiJobbonske");
    }

    private void addForerkortQuery(String forerkort, BoolQueryBuilder boolQueryBuilder) {
        NestedQueryBuilder forerkortQueryBuilder = QueryBuilders.nestedQuery("forerkort",
                QueryBuilders.termQuery("forerkort.forerkortKodeKlasse", forerkort),
                ScoreMode.Total);
        boolQueryBuilder.should(forerkortQueryBuilder);
        LOGGER.debug("ADDING førerkort");
    }

    private void addKvalifiseringsgruppekodeQuery(String kvalifiseringsgruppekode, BoolQueryBuilder boolQueryBuilder) {
        boolQueryBuilder.should(QueryBuilders.termQuery("kvalifiseringsgruppekode", kvalifiseringsgruppekode));
        LOGGER.debug("ADDING kvalifiseringsgruppekode");
    }

    private void addNavkontorQuery(String navkontor, BoolQueryBuilder boolQueryBuilder) {
        boolQueryBuilder.should(QueryBuilders.termQuery("navkontor", navkontor));
        LOGGER.debug("ADDING navkontor");
    }

    private void addVeilederToQuery(String veileder, BoolQueryBuilder boolQueryBuilder) {
        boolQueryBuilder.should(QueryBuilders.termQuery("veileder", veileder.toLowerCase()));
        LOGGER.debug("ADDING veileder");
    }

    private void addHovedmalToQuery(String hovedmalKode, BoolQueryBuilder boolQueryBuilder) {
        boolQueryBuilder.should(QueryBuilders.termQuery("hovedmaalkode", hovedmalKode));
        LOGGER.debug("ADDING hovedmal");
    }

    private void addKommunenummerQuery(String geografi, BoolQueryBuilder boolQueryBuilder) {
        String[] geografiKoder = geografi.split("\\.");
        String regex = "";

        if (geografiKoder.length == 1) {

            if (geografiKoder[0].length() < 4) {
                return;
            } else {
                String fylkeskode = geografiKoder[0].substring(2, 4);
                regex += fylkeskode + ".*";
            }
        } else {
            regex += geografiKoder[1].substring(0, 4);
        }

        RegexpQueryBuilder kommunenummerQueryBuilder =
                QueryBuilders.regexpQuery("kommunenummerstring", regex);
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
        if (aggregations.get(aggregationName) instanceof org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms) {
            return ((org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms) aggregations.get(aggregationName)).getBuckets();
        }
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
        searchSourceBuilder.sort(new FieldSortBuilder("tidsstempel").order(SortOrder.DESC));

        TermsAggregationBuilder kompetanseObjAggregation =
                AggregationBuilders.terms("kompetanse").field("kompetanseObj.kompKodeNavn.keyword");
        searchSourceBuilder.aggregation(kompetanseObjAggregation);

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(indexName);
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
            LOGGER.debug("Score for {} er {} ", esCv.getKandidatnr(), hit.getScore());
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

    @Override
    public Optional<no.nav.arbeid.cv.kandidatsok.es.domene.EsCv> arbeidsgiverHent(String kandidatnr) {
        return hentFelles(true, kandidatnr);
    }

    @Override
    public Optional<no.nav.arbeid.cv.kandidatsok.es.domene.EsCv> veilederHent(String kandidatnr) {
        return hentFelles(false, kandidatnr);
    }

    private Optional<no.nav.arbeid.cv.kandidatsok.es.domene.EsCv> hentFelles(boolean isAg, String kandidatnr) {

        try {
            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                    .must(QueryBuilders.termQuery("kandidatnr", kandidatnr));
            if (isAg) {
                addFilterForArbeidsgivereHent(queryBuilder);
            } else {
                addFilterForVeiledereHent(queryBuilder);
            }

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(queryBuilder).from(0).size(10)
                    .sort(new FieldSortBuilder("tidsstempel").order(SortOrder.DESC));

            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices(indexName);
            searchRequest.source(searchSourceBuilder);

            LOGGER.debug("HENTREQUEST: " + searchRequest.toString());

            SearchResponse searchResponse = client.search(searchRequest);
            LOGGER.debug("HENTRESPONSE: " + searchResponse);

            List<no.nav.arbeid.cv.kandidatsok.es.domene.EsCv> liste = StreamSupport
                    .stream(searchResponse.getHits().spliterator(), false)
                    .map(hit -> mapEsCvHent(hit)).filter(Objects::nonNull).collect(toList());
            if (liste.size() == 0) {
                LOGGER.info("Finner ikke CV for kandidat {}", kandidatnr);
                return Optional.empty();
            } else if (liste.size() > 1) {
                LOGGER.error("Fant mer enn én CV for kandidat {}. Fant {} CVer: {}", kandidatnr,
                        liste.size(),
                        liste.stream()
                                .map(cv -> String.format("(kandidatnr: %s)", cv.getKandidatnr()))
                                .collect(Collectors.joining(", ")));
            }
            return liste.stream().findFirst();
        } catch (IOException ioe) {
            throw new ElasticException(ioe);
        }
    }

    private BoolQueryBuilder kandidatnrQuery(List<String> kandidatnummer) {
        return QueryBuilders.boolQuery()
                .must(QueryBuilders.termsQuery("kandidatnr", kandidatnummer));
    }

    @Override
    public Sokeresultat arbeidsgiverHentKandidater(List<String> kandidatnummer) {
        try {
            SearchResponse searchResponse = esExec(
                    () -> search(UseCase.AG_SOK, kandidatnrQuery(kandidatnummer), 0, kandidatnummer.size(), null));
            Sokeresultat usortertSokeresultat = toSokeresultat(searchResponse);
            List<EsCv> sorterteCver = sorterSokeresultaterBasertPaaRequestRekkefolge(
                    usortertSokeresultat.getCver(), kandidatnummer);
            return new Sokeresultat(usortertSokeresultat.getTotaltAntallTreff(), sorterteCver,
                    usortertSokeresultat.getAggregeringer());
        } catch (IOException ioe) {
            throw new ElasticException(ioe);
        }
    }

    @Override
    public Sokeresultat arbeidsgiverHentKandidaterForVisning(List<String> kandidatnummer) {
        try {
            SearchResponse searchResponse = esExec(
                    () -> search(UseCase.AG_HENT, kandidatnrQuery(kandidatnummer), 0, kandidatnummer.size(), null));
            Sokeresultat usortertSokeresultat = toSokeresultat(searchResponse);
            List<EsCv> sorterteCver = sorterSokeresultaterBasertPaaRequestRekkefolge(
                    usortertSokeresultat.getCver(), kandidatnummer);
            return new Sokeresultat(usortertSokeresultat.getTotaltAntallTreff(), sorterteCver,
                    usortertSokeresultat.getAggregeringer());
        } catch (IOException ioe) {
            throw new ElasticException(ioe);
        }
    }

    @Override
    public Sokeresultat veilederHentKandidater(List<String> kandidatnummer) {
        try {
            SearchResponse searchResponse = esExec(
                    () -> search(UseCase.VEIL_SOK, kandidatnrQuery(kandidatnummer), 0, kandidatnummer.size(), null));
            Sokeresultat usortertSokeresultat = toSokeresultat(searchResponse);
            List<EsCv> sorterteCver = sorterSokeresultaterBasertPaaRequestRekkefolge(
                    usortertSokeresultat.getCver(), kandidatnummer);
            return new Sokeresultat(usortertSokeresultat.getTotaltAntallTreff(), sorterteCver,
                    usortertSokeresultat.getAggregeringer());
        } catch (IOException ioe) {
            throw new ElasticException(ioe);
        }
    }

    private List<EsCv> sorterSokeresultaterBasertPaaRequestRekkefolge(List<EsCv> cver,
                                                                      List<String> kandidatrekkefolge) {
        Map<String, EsCv> kandidater =
                cver.stream().collect(toMap(EsCv::getKandidatnr, Function.identity()));

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

    @Override
    public void close() throws IOException {
        client.close();
    }

    private static enum UseCase {
        AG_SOK, AG_HENT, VEIL_SOK, VEIL_HENT;
    }

    /**
     * Tilsvarer java.functions.Supplier bare at get metoden kan kaste IOException
     */
    private interface IOSupplier<T> {
        T get() throws IOException;
    }

}
