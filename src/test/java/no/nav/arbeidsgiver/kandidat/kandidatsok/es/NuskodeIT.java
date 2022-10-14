package no.nav.arbeidsgiver.kandidat.kandidatsok.es;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.arbeidsgiver.kandidat.kandidatsok.domene.es.EsCvMedNuskodeEttsiffer;
import no.nav.arbeidsgiver.kandidat.kandidatsok.domene.es.EsCvObjectMother;
import no.nav.arbeidsgiver.kandidat.kandidatsok.domene.es.KandidatsokTransformer;
import no.nav.arbeidsgiver.kandidat.kandidatsok.testsupport.ElasticSearchIntegrationTestExtension;
import no.nav.arbeidsgiver.kandidat.kandidatsok.testsupport.ElasticSearchTestConfiguration;
import no.nav.arbeidsgiver.kandidatsok.es.client.EsIndexerService;
import no.nav.arbeidsgiver.kandidatsok.es.client.EsSokService;
import no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.sok.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Testklasse laget i forbindelse med fix av bug ""Filtering på høyere utdanning fjerner aktuelle kandidater"
 */
@ExtendWith(ElasticSearchIntegrationTestExtension.class)
public class NuskodeIT {

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
    public void sokPaaKandidatKompetanseNuskodeEttSifferIngenUtdanning() {
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
    public void sokPaaKandidatKompetanseNuskodeEttSifferDoktorgrad() {
        Sokeresultat sokeresultatDoktorgrad = sokClient.veilederSok(
                SokekriterierVeiledere.med().utdanningsniva(Collections.singletonList("Doktorgrad")).bygg());

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
    public void sokPaaKandidatKompetanseNuskodeEttSifferBachelorogMaster() {
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
