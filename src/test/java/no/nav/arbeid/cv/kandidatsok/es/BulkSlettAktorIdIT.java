package no.nav.arbeid.cv.kandidatsok.es;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import no.nav.arbeid.cv.kandidatsok.domene.es.EsCvObjectMother;
import no.nav.arbeid.cv.kandidatsok.domene.es.KandidatsokTransformer;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.EsCv;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.Sokekriterier;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.Sokeresultat;
import no.nav.arbeid.cv.kandidatsok.testsupport.ElasticSearchTestExtension;
import no.nav.arbeid.kandidatsok.es.client.EsIndexerHttpService;
import no.nav.arbeid.kandidatsok.es.client.EsIndexerService;
import no.nav.arbeid.kandidatsok.es.client.EsSokHttpService;
import no.nav.arbeid.kandidatsok.es.client.EsSokService;
import org.apache.http.HttpHost;
import org.assertj.core.extractor.Extractors;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ExtendWith(ElasticSearchTestExtension.class)
public class BulkSlettAktorIdIT {

    @Autowired
    private EsSokService sokClient;

    @Autowired
    private EsIndexerService indexerClient;

    @Autowired
    private ObjectMapper objectMapper;

    private KandidatsokTransformer kandidatsokTransformer = new KandidatsokTransformer();

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
            return new SimpleMeterRegistry();
        }

        @Bean
        public EsIndexerService indexerCvService(RestHighLevelClient restHighLevelClient,
                                                 ObjectMapper objectMapper,
                                                 MeterRegistry meterRegistry
        ) {
            return new EsIndexerHttpService(restHighLevelClient, objectMapper, meterRegistry,
                    WriteRequest.RefreshPolicy.IMMEDIATE, 1, 1);
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
    public void sjekkAtSlettingByAktorIdFungerer() {
        List<String> aktorIdSletteListe = List.of(
                EsCvObjectMother.giveMeEsCv().getAktorId(),
                EsCvObjectMother.giveMeEsCv2().getAktorId());

        assertEquals(2, indexerClient.bulkSlettAktorId(aktorIdSletteListe, "cvindex"));

        Sokeresultat sokeresultat = sokClient.arbeidsgiverSok(Sokekriterier.med().bygg());

        List<EsCv> cver = sokeresultat.getCver();

        assertThat(cver.size()).isEqualTo(4);
        assertThat(cver).extracting(Extractors.byName("kandidatnr")).containsExactlyInAnyOrder(
                "6L", "5L", "4L", "3L");
    }

    @Test
    public void sjekkAtSlettingAvIkkeEksisterendeAktorIdGir0() {
        List<String> aktorIdSletteListe = List.of(
                EsCvObjectMother.giveMeEsCv().getAktorId(),
                EsCvObjectMother.giveMeEsCv2().getAktorId());

        assertEquals(0, indexerClient.bulkSlettAktorId(List.of("blah"), "cvindex"));
    }

}
