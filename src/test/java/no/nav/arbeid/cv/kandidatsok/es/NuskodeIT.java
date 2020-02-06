package no.nav.arbeid.cv.kandidatsok.es;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import no.nav.arbeid.cv.kandidatsok.domene.es.EsCvMedNuskodeEttsiffer;
import no.nav.arbeid.cv.kandidatsok.domene.es.EsCvObjectMother;
import no.nav.arbeid.cv.kandidatsok.domene.es.KandidatsokTransformer;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.*;
import no.nav.arbeid.cv.kandidatsok.testsupport.ElasticSearchTestExtension;
import no.nav.arbeid.kandidatsok.es.client.EsIndexerHttpService;
import no.nav.arbeid.kandidatsok.es.client.EsIndexerService;
import no.nav.arbeid.kandidatsok.es.client.EsSokHttpService;
import no.nav.arbeid.kandidatsok.es.client.EsSokService;
import org.apache.http.HttpHost;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


// Testklasse laget i forbindelse med fix av bug ""Filtering på høyere utdanning fjerner aktuelle kandidater"

@ExtendWith(SpringExtension.class)
@ExtendWith(ElasticSearchTestExtension.class)
public class NuskodeIT {

    @Autowired
    private EsSokService sokClient;

    @Autowired
    private EsIndexerService indexerClient;

    @Autowired
    private ObjectMapper objectMapper;

    private KandidatsokTransformer kandidatsokTransformer = new KandidatsokTransformer();
    private static final Logger LOGGER = LoggerFactory.getLogger(NuskodeIT.class);


    @Configuration
    static class TestConfig {

        @Bean
        public RestHighLevelClient restHighLevelClient() {
            return new RestHighLevelClient(
                    RestClient.builder(new HttpHost("localhost", ElasticSearchTestExtension.getEsPort(), "http")));
        }

        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }

        @Bean
        public MeterRegistry meterRegistry() {
            Counter counter = mock(Counter.class);
            MeterRegistry meterRegistry = mock(MeterRegistry.class);
            when(meterRegistry.counter(anyString(), any(Tags.class))).thenReturn(counter);

            return meterRegistry;
        }

        @Bean
        public EsIndexerService indexerCvService(RestHighLevelClient restHighLevelClient,
                                                 ObjectMapper objectMapper,
                                                 MeterRegistry meterRegistry
        ) {
            return new EsIndexerHttpService(restHighLevelClient, objectMapper, meterRegistry,
                    WriteRequest.RefreshPolicy.IMMEDIATE, 3, 2);
        }

        @Bean
        public EsSokService esSokService(RestHighLevelClient restHighLevelClient, ObjectMapper objectMapper) {
            return new EsSokHttpService(restHighLevelClient, objectMapper, "cvindex");
        }
    }

    @BeforeEach
    public void before() {
        indexerClient.createIndex("cvindex");

        indexerClient.bulkIndex(List.of(
                EsCvMedNuskodeEttsiffer.giveMeEsCvNuskode8(),
                EsCvMedNuskodeEttsiffer.giveMeEsCvNuskode6(),
                EsCvMedNuskodeEttsiffer.giveMeEsCvNuskode6og7(),
                EsCvMedNuskodeEttsiffer.giveMeEsCvIngenUtdanning(),
                EsCvObjectMother.giveMeEsCv(),
                EsCvObjectMother.giveMeEsCv2(),
                EsCvObjectMother.giveMeEsCv3(),
                EsCvObjectMother.giveMeEsCv4(),
                EsCvObjectMother.giveMeEsCv5(),
                EsCvObjectMother.giveMeEsCv6(),
                EsCvObjectMother.giveMeCvForDoedPerson(),
                EsCvObjectMother.giveMeCvForKode6(),
                EsCvObjectMother.giveMeCvForKode7(),
                EsCvObjectMother.giveMeCvFritattForAgKandidatsok(),
                EsCvObjectMother.giveMeCvFritattForKandidatsok()
        ), "cvindex");
    }

    @AfterEach
    public void after() {
        indexerClient.deleteIndex("cvindex");
    }


    @Test
    public void sokPaaKandidatKompetanseNuskodeEttSifferIngenUtdanning() {
        Sokeresultat sokeresultatIngenUtdanning = sokClient.arbeidsgiverSok(
                Sokekriterier.med().utdanningsniva(Collections.singletonList("Ingen")).bygg());

        List <EsCv> cver = sokeresultatIngenUtdanning.getCver();
        assertThat(cver)
                .contains(kandidatsokTransformer.transformer(EsCvMedNuskodeEttsiffer.giveMeEsCvIngenUtdanning()));

        // Sjekk at evt. utdanninger som finnes telles som "ingen":
        for (EsCv ingenUtdanningCv: sokeresultatIngenUtdanning.getCver()) {
            ingenUtdanningCv.getUtdanning().stream().map(u -> u.getNusKode()).forEach(nusKode -> {
                assertTrue(nusKode.startsWith("0") || nusKode.startsWith("1") || nusKode.startsWith("2"));
            });
        }
    }


    @Test
    public void sokPaaKandidatKompetanseNuskodeEttSifferDoktorgrad() {
        Sokeresultat sokeresultatDoktorgrad = sokClient.arbeidsgiverSok(
                Sokekriterier.med().utdanningsniva(Collections.singletonList("Doktorgrad")).bygg());

        List <EsCv> cver = sokeresultatDoktorgrad.getCver();
        assertThat(cver)
                .contains(kandidatsokTransformer.transformer(EsCvMedNuskodeEttsiffer.giveMeEsCvNuskode8()));

        for (EsCv doktorgradCv: sokeresultatDoktorgrad.getCver()) {
            Optional<EsUtdanning> firstDoktorgrad = doktorgradCv.getUtdanning().stream().filter(esUtdanning -> esUtdanning.getNusKode().startsWith("8")).findFirst();
            assertThat(firstDoktorgrad).isNotEmpty();
        }
    }


    @Test
    public void sokPaaKandidatKompetanseNuskodeEttSifferBachelor() {
        Sokeresultat sokeresultatBachelor = sokClient.arbeidsgiverSok(
                Sokekriterier.med().utdanningsniva(Collections.singletonList("Bachelor")).bygg());

        List <EsCv> cver = sokeresultatBachelor.getCver();
        assertThat(cver)
                .contains(kandidatsokTransformer.transformer(EsCvMedNuskodeEttsiffer.giveMeEsCvNuskode6()));

        assertThat(cver)
                .doesNotContain(kandidatsokTransformer.transformer(EsCvMedNuskodeEttsiffer.giveMeEsCvNuskode6og7()));

        for (EsCv bachelorCv: sokeresultatBachelor.getCver()) {
            Optional<EsUtdanning> firstBachelor = bachelorCv.getUtdanning().stream().filter(esUtdanning -> esUtdanning.getNusKode().startsWith("6")).findFirst();
            assertThat(firstBachelor).isNotEmpty();
        }
    }

    @Test
    public void sokPaaKandidatKompetanseNuskodeEttSifferBachelorogMaster() {
        Sokeresultat sokeresultatBachelorOgMaster = sokClient.arbeidsgiverSok(
                Sokekriterier.med().utdanningsniva(asList("Bachelor", "Master")).bygg());

        List <EsCv> cver = sokeresultatBachelorOgMaster.getCver();
        assertThat(cver)
                .contains(kandidatsokTransformer.transformer(EsCvMedNuskodeEttsiffer.giveMeEsCvNuskode6()));

        assertThat(cver)
                .contains(kandidatsokTransformer.transformer(EsCvMedNuskodeEttsiffer.giveMeEsCvNuskode6og7()));

        for (EsCv bachelorEllerMasterCv: sokeresultatBachelorOgMaster.getCver()) {
            Optional<EsUtdanning> firstBachelorEllerMaster = bachelorEllerMasterCv.getUtdanning().stream().filter(esUtdanning -> esUtdanning.getNusKode().startsWith("6") || esUtdanning.getNusKode().startsWith("7")).findFirst();
                assertThat(firstBachelorEllerMaster).isNotEmpty();
        }
    }


    @Test
    public void veilederSokPaaKandidatKompetanseNuskodeEttSifferIngenUtdanning() {
        Sokeresultat sokeresultatIngenUtdanning = sokClient.veilederSok(
                SokekriterierVeiledere.med().utdanningsniva(Collections.singletonList("Ingen")).bygg());

        List <EsCv> cver = sokeresultatIngenUtdanning.getCver();
        assertThat(cver)
                .contains(kandidatsokTransformer.transformer(EsCvMedNuskodeEttsiffer.giveMeEsCvIngenUtdanning()));

        // Sjekk at evt. utdanninger som finnes telles som "ingen":
        for (EsCv ingenUtdanningCv: sokeresultatIngenUtdanning.getCver()) {
            ingenUtdanningCv.getUtdanning().stream().map(u -> u.getNusKode()).forEach(nusKode -> {
            assertTrue(nusKode.startsWith("0") || nusKode.startsWith("1") || nusKode.startsWith("2"));
        });
        }
    }

    @Test
    public void veilederSokPaaKandidatKompetanseNuskodeEttSifferBachelor() {
        Sokeresultat sokeresultatBachelor = sokClient.veilederSok(
                SokekriterierVeiledere.med().utdanningsniva(Collections.singletonList("Bachelor")).bygg());

        List <EsCv> cver = sokeresultatBachelor.getCver();
        assertThat(cver)
                .contains(kandidatsokTransformer.transformer(EsCvMedNuskodeEttsiffer.giveMeEsCvNuskode6()));

        assertThat(cver)
                .doesNotContain(kandidatsokTransformer.transformer(EsCvMedNuskodeEttsiffer.giveMeEsCvNuskode6og7()));

        for (EsCv bachelorCv: sokeresultatBachelor.getCver()) {
            Optional<EsUtdanning> firstBachelor = bachelorCv.getUtdanning().stream().filter(esUtdanning -> esUtdanning.getNusKode().startsWith("6")).findFirst();
            assertThat(firstBachelor).isNotEmpty();
        }
    }

    @Test
    public void veilederSokPaaKandidatKompetanseNuskodeEttSifferDoktorgrad() {
        Sokeresultat sokeresultatDoktorgrad = sokClient.veilederSok(
                SokekriterierVeiledere.med().utdanningsniva(Collections.singletonList("Doktorgrad")).bygg());


        List <EsCv> cver = sokeresultatDoktorgrad.getCver();
        assertThat(cver)
                .contains(kandidatsokTransformer.transformer(EsCvMedNuskodeEttsiffer.giveMeEsCvNuskode8()));

        assertThat(cver.size()).isGreaterThanOrEqualTo(1);

        for (EsCv doktorgradCv: sokeresultatDoktorgrad.getCver()) {
            Optional<EsUtdanning> firstDoktorgrad = doktorgradCv.getUtdanning().stream().filter(esUtdanning -> esUtdanning.getNusKode().startsWith("8")).findFirst();
            assertThat(firstDoktorgrad).isNotEmpty();
        }
    }

    @Test
    public void veilederSokPaaKandidatKompetanseNuskodeEttSifferBachelorogMaster() {
        Sokeresultat sokeresultatBachelorOgMaster = sokClient.veilederSok(
                SokekriterierVeiledere.med().utdanningsniva(asList("Bachelor", "Master")).bygg());

        List <EsCv> cver = sokeresultatBachelorOgMaster.getCver();
        assertThat(cver)
                .contains(kandidatsokTransformer.transformer(EsCvMedNuskodeEttsiffer.giveMeEsCvNuskode6()));

        assertThat(cver)
                .contains(kandidatsokTransformer.transformer(EsCvMedNuskodeEttsiffer.giveMeEsCvNuskode6og7()));

        for (EsCv bachelorEllerMasterCv: sokeresultatBachelorOgMaster.getCver()) {
            Optional<EsUtdanning> firstBachelorEllerMaster = bachelorEllerMasterCv.getUtdanning().stream().filter(esUtdanning -> esUtdanning.getNusKode().startsWith("6") || esUtdanning.getNusKode().startsWith("7")).findFirst();
                assertThat(firstBachelorEllerMaster).isNotEmpty();

        }
    }

}
