package no.nav.arbeid.cv.kandidatsok.es.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.arbeid.cv.kandidatsok.domene.es.EsCvMedNuskodeEttsiffer;
import no.nav.arbeid.cv.kandidatsok.domene.es.EsCvObjectMother;
import no.nav.arbeid.cv.kandidatsok.domene.es.KandidatsokTransformer;
import no.nav.arbeid.cv.kandidatsok.es.NuskodeIT;
import no.nav.arbeid.cv.kandidatsok.es.domene.EsForerkort;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.EsCv;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.EsUtdanning;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.Sokekriterier;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.Sokeresultat;
import no.nav.arbeid.cv.kandidatsok.testsupport.ElasticSearchIntegrationTestExtension;
import no.nav.arbeid.cv.kandidatsok.testsupport.ElasticSearchTestConfiguration;
import no.nav.arbeid.kandidatsok.es.client.EsIndexerService;
import no.nav.arbeid.kandidatsok.es.client.EsSokService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Test av forerkort-mapping. Sjekker at Traktor og Snøscooter er uavhengig, og kommer kun opp for kandidater med T eller S lagt inn.
 */
@ExtendWith(ElasticSearchIntegrationTestExtension.class)
public class ForerkortMappingIT {


    private EsSokService sokClient = ElasticSearchTestConfiguration.esSokService(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);

    private EsIndexerService indexerClient = ElasticSearchTestConfiguration.indexerCvService();

    private ObjectMapper objectMapper = ElasticSearchTestConfiguration.objectMapper();

    private KandidatsokTransformer kandidatsokTransformer = new KandidatsokTransformer();

    private static final Logger LOGGER = LoggerFactory.getLogger(NuskodeIT.class);

    @BeforeEach
    public void before() {
        indexerClient.createIndex(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);

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
        ), ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);
    }

    @AfterEach
    public void after() {
        indexerClient.deleteIndex(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);
    }



    @Test
    public void sokPaaForerkortTraktor() {
        Sokeresultat sokeresultatForerkort = sokClient.arbeidsgiverSok(
                Sokekriterier.med().forerkort(Collections.singletonList("T - Traktor")).bygg());

        List<EsCv> cver = sokeresultatForerkort.getCver();
        assertThat(cver)
                .doesNotContain(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv5()));

        Set<String> forerkort = new HashSet<>();
        cver.forEach(cv -> cv.getForerkort().forEach(f -> forerkort.add(f.getForerkortKodeKlasse())));
        assertThat(forerkort).contains("T - Traktor");
    }

    @Test
    public void sokPaaForerkortSnoscooter() {
        Sokeresultat sokeresultatForerkort = sokClient.arbeidsgiverSok(
                Sokekriterier.med().forerkort(Collections.singletonList("S - Snøscooter")).bygg());

        List<EsCv> cver = sokeresultatForerkort.getCver();
        assertThat(cver)
                .doesNotContain(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv5()));

        Set<String> forerkort = new HashSet<>();
        cver.forEach(cv -> cv.getForerkort().forEach(f -> forerkort.add(f.getForerkortKodeKlasse())));
        assertThat(forerkort).contains("S - Snøscooter");
    }
}

