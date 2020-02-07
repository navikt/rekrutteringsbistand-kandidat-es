package no.nav.arbeid.cv.kandidatsok.es;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.arbeid.cv.kandidatsok.domene.es.EsCvObjectMother;
import no.nav.arbeid.cv.kandidatsok.domene.es.KandidatsokTransformer;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.EsCv;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.Sokekriterier;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.Sokeresultat;
import no.nav.arbeid.cv.kandidatsok.testsupport.ElasticSearchTestConfiguration;
import no.nav.arbeid.cv.kandidatsok.testsupport.ElasticSearchIntegrationTestExtension;
import no.nav.arbeid.kandidatsok.es.client.EsIndexerService;
import no.nav.arbeid.kandidatsok.es.client.EsSokService;
import org.assertj.core.extractor.Extractors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(ElasticSearchIntegrationTestExtension.class)
public class BulkSlettAktorIdIT {

    private EsSokService sokClient = ElasticSearchTestConfiguration.esSokService(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);

    private EsIndexerService indexerClient = ElasticSearchTestConfiguration.indexerCvService();

    private ObjectMapper objectMapper = ElasticSearchTestConfiguration.objectMapper();

    private KandidatsokTransformer kandidatsokTransformer = new KandidatsokTransformer();


    @BeforeEach
    public void before() {
        indexerClient.createIndex(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);

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
        ), ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);
    }

    @AfterEach
    public void after() {
        indexerClient.deleteIndex(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);
    }

    @Test
    public void sjekkAtSlettingByAktorIdFungerer() {
        List<String> aktorIdSletteListe = List.of(
                EsCvObjectMother.giveMeEsCv().getAktorId(),
                EsCvObjectMother.giveMeEsCv2().getAktorId());

        assertEquals(2, indexerClient.bulkSlettAktorId(aktorIdSletteListe, ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME));

        Sokeresultat sokeresultat = sokClient.arbeidsgiverSok(Sokekriterier.med().bygg());

        List<EsCv> cver = sokeresultat.getCver();

        assertThat(cver.size()).isEqualTo(4);
        assertThat(cver).extracting(Extractors.byName("kandidatnr")).containsExactlyInAnyOrder(
                "6L", "5L", "4L", "3L");
    }

    @Test
    public void sjekkAtSlettingAvIkkeEksisterendeAktorIdGir0() {
        assertEquals(0, indexerClient.bulkSlettAktorId(List.of("blah"), ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME));
    }

    @Test
    public void tomListeSkalIkkeFeileOgIkkeHaNoenEffekt() {
        assertEquals(0, indexerClient.bulkSlettAktorId(Collections.emptyList(), ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME));
    }

}
