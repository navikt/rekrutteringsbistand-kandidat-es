package no.nav.arbeidsgiver.kandidatsok.es.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.sok.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.TotalHits;
import org.apache.lucene.search.join.ScoreMode;
import org.opensearch.OpenSearchStatusException;
import org.opensearch.action.get.GetRequest;
import org.opensearch.action.get.GetResponse;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.index.query.*;
import org.opensearch.search.SearchHit;
import org.opensearch.search.aggregations.AggregationBuilders;
import org.opensearch.search.aggregations.Aggregations;
import org.opensearch.search.aggregations.bucket.filter.ParsedFilter;
import org.opensearch.search.aggregations.bucket.nested.Nested;
import org.opensearch.search.aggregations.bucket.terms.Terms;
import org.opensearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.opensearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.opensearch.search.builder.SearchSourceBuilder;
import org.opensearch.search.sort.FieldSortBuilder;
import org.opensearch.search.sort.NestedSortBuilder;
import org.opensearch.search.sort.SortMode;
import org.opensearch.search.sort.SortOrder;
import org.opensearch.search.suggest.SuggestBuilder;
import org.opensearch.search.suggest.SuggestBuilders;
import org.opensearch.search.suggest.completion.CompletionSuggestion;
import org.opensearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.opensearch.index.query.QueryBuilders.*;

public class EsSokHttpService implements EsSokService, AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(EsSokHttpService.class);
    private final RestHighLevelClient client;
    private final ObjectMapper mapper;
    private final String indexName;

    public EsSokHttpService(RestHighLevelClient client, ObjectMapper objectMapper, String indexName) {
        this.client = client;
        mapper = objectMapper;
        this.indexName = indexName;
    }

    @Override
    public Optional<EsCv> veilederSokPaaFnr(String fnr) {
        try {
            BoolQueryBuilder queryBuilder = boolQuery();
            queryBuilder.must(QueryBuilders.termQuery("fodselsnummer", fnr));

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(queryBuilder);

            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices(indexName);
            searchRequest.source(searchSourceBuilder);

            LOGGER.debug("SEARCHREQUEST: " + searchRequest.toString());

            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            LOGGER.debug("SEARCHRESPONSE: " + searchResponse);
            LOGGER.debug("Søketid: {}", searchResponse.getTook());
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
    public Boolean harHullICv(String aktorId, LocalDate tidspunkt) {
        var hullICvBoolQuery = boolQuery();
        addFilterForHullICv(hullICvBoolQuery, tidspunkt); // MÅ LEGGE TIL DATO
        var hullICvAggregation = AggregationBuilders.filter("hull", hullICvBoolQuery);

        var aktorIdBoolQuery = boolQuery().must(QueryBuilders.termQuery("aktorId", aktorId));

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(aktorIdBoolQuery);
        searchSourceBuilder.aggregation(hullICvAggregation);

        var request = new SearchRequest();
        request.indices(indexName);
        request.source(searchSourceBuilder);

        try {
            var response = client.search(request, RequestOptions.DEFAULT);
            var harTreff = response.getInternalResponse().hits().getHits().length > 0;

            if (harTreff) {
                var hullAggregation = response.getAggregations().get("hull");
                if (hullAggregation instanceof ParsedFilter) {
                    return ((ParsedFilter) hullAggregation).getDocCount() > 0;
                } else {
                    throw new RuntimeException("Uventet type fra aggregation");
                }
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new ElasticException(e);
        }
    }

    private void addFilterForHullICv(BoolQueryBuilder rootQueryBuilder, LocalDate tidspunkt) {
      /*
         Et hull i en CV er definert som en periode på 2 år eller med med inaktivitet i løpet av de siste 5 årene fra tidspunktet spørringen kjøres på.
         Hele algoritmen for å finne et hull er implementert delvis her i denne Elastic-search spørringen og delvis i
         applikasjonen toi-hull-i-cv og rekrutteringsbistand-kandidat-indekser: toi-hull-i.cv har ansvar for å finne de periodene med inaktivitet
         som har lang nok varighet til å kunne være et hull. rekrutteringsbistand-kandidat-indekser har ansvar for å lagre dem i Elsticsearch-indeksen,
         slik at de kan søkes i med denne spørringen.
         Grensen på 2 år er hardkodet både her og i appen toi-hull-i-cv. Grensen på 5 år er hardkodet bare her.
    */

        BoolQueryBuilder aldriVærtIAktivitet = boolQuery()
                .mustNot(existsQuery("perioderMedInaktivitet.startdatoForInnevarendeInaktivePeriode"))
                .mustNot(existsQuery("perioderMedInaktivitet.sluttdatoerForInaktivePerioderPaToArEllerMer"));

        BoolQueryBuilder erInaktivOgHarHull = boolQuery()
                .must(existsQuery("perioderMedInaktivitet.startdatoForInnevarendeInaktivePeriode"))
                .must(
                        boolQuery()
                                .should(rangeQuery("perioderMedInaktivitet.startdatoForInnevarendeInaktivePeriode").lte(tidspunkt.minusYears(2)))
                                .should(rangeQuery("perioderMedInaktivitet.sluttdatoerForInaktivePerioderPaToArEllerMer").gte(tidspunkt.minusYears(3))
                                )
                );

        BoolQueryBuilder harUtfyltCv = boolQuery()
                .should(nestedExists("yrkeserfaring"))
                .should(nestedExists("utdanning"))
                .should(nestedExists("forerkort"))
                .should(existsQuery("kursObj"))
                .should(existsQuery("fagdokumentasjon"))
                .should(existsQuery("annenerfaringObj"))
                .should(existsQuery("godkjenninger"));

        rootQueryBuilder.must(
                boolQuery()
                        .must(harUtfyltCv)
                        .must(boolQuery()
                                .should(aldriVærtIAktivitet)
                                .should(erInaktivOgHarHull)));
    }

    private NestedQueryBuilder nestedExists(String felt) {
        return nestedQuery(
                felt,
                boolQuery().filter(
                        existsQuery(felt)
                ), ScoreMode.Total);
    }

    private Sokeresultat toSokeresultat(SearchResponse searchResponse) {
        LOGGER.debug("Totalt antall treff: " + searchResponse.getHits().getTotalHits());
        List<EsCv> cver = toCvList(searchResponse);
        List<Aggregering> aggregeringer = toAggregeringList(searchResponse);
        TotalHits total = searchResponse.getHits().getTotalHits();
        if (total.relation == TotalHits.Relation.GREATER_THAN_OR_EQUAL_TO) {
            LOGGER.warn("Inaccurate total hits computation");
        }
        return new Sokeresultat(total.value, cver, aggregeringer);
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
        if (aggregations.get(aggregationName) instanceof org.opensearch.search.aggregations.bucket.terms.ParsedStringTerms) {
            return ((org.opensearch.search.aggregations.bucket.terms.ParsedStringTerms) aggregations.get(aggregationName)).getBuckets();
        }
        return ((Terms) ((Nested) aggregations.get(aggregationName)).getAggregations()
                .get("nested")).getBuckets();
    }

    private SearchResponse search(BoolQueryBuilder queryBuilder, int from,
                                  int size, BoolQueryBuilder sortQueryBuilder) throws IOException {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);
        searchSourceBuilder.trackTotalHits(true); // Consider removing this if we don't actually need accurate counts

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

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        LOGGER.debug("SEARCHRESPONSE: " + searchResponse);
        LOGGER.debug("Søketid: {}", searchResponse.getTook());
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
        } catch (OpenSearchStatusException e) {
            if (e.status().getStatus() == 404
                    && e.getMessage().contains("index_not_found_exception")) {
                LOGGER.info(
                        "Greide ikke å utfore operasjon mot elastic search, indeks er ikke opprettet.");
                // return fun.get();
            }
            throw (e);
        }
    }

    @Override
    public Optional<no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsCv> veilederHent(String kandidatnr) {
        try {
            GetResponse esCvResponse = client.get(new GetRequest(indexName, kandidatnr), RequestOptions.DEFAULT);
            if (!esCvResponse.isExists()) {
                return Optional.empty();
            }
            no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsCv esCv = mapEsCvHent(kandidatnr, esCvResponse.getSourceAsString());
            return Optional.ofNullable(esCv);
        } catch (IOException ioe) {
            throw new ElasticException(ioe);
        }
    }

    private BoolQueryBuilder kandidatnrQuery(List<String> kandidatnummer) {
        return boolQuery()
                .must(QueryBuilders.termsQuery("kandidatnr", kandidatnummer));
    }

    @Override
    public Sokeresultat veilederHentKandidater(List<String> kandidatnummer) {
        try {
            SearchResponse searchResponse = esExec(
                    () -> search(kandidatnrQuery(kandidatnummer), 0, kandidatnummer.size(), null));
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

    private no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsCv mapEsCvHent(String id, String jsonSource) {
        try {
            return mapper.readValue(jsonSource,
                    no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsCv.class);
        } catch (IOException e) {
            LOGGER.warn("Klarte ikke å parse CV fra Elasticsearch med id/kandidatnr {}, returnerer null", id, e);
            return null;
        }
    }

    @Override
    public void close() throws IOException {
        client.close();
    }

    /**
     * Tilsvarer java.functions.Supplier bare at get metoden kan kaste IOException
     */
    private interface IOSupplier<T> {
        T get() throws IOException;
    }
}
