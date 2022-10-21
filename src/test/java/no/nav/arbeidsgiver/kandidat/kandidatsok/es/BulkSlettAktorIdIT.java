package no.nav.arbeidsgiver.kandidat.kandidatsok.es;

import no.nav.arbeidsgiver.kandidat.kandidatsok.domene.es.EsCvObjectMother;
import no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.sok.EsCv;
import no.nav.arbeidsgiver.kandidat.kandidatsok.testsupport.ElasticSearchIntegrationTestExtension;
import no.nav.arbeidsgiver.kandidat.kandidatsok.testsupport.ElasticSearchTestConfiguration;
import no.nav.arbeidsgiver.kandidatsok.es.client.EsIndexerService;
import no.nav.arbeidsgiver.kandidatsok.es.client.EsSokService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(ElasticSearchIntegrationTestExtension.class)
public class BulkSlettAktorIdIT {

    private final EsSokService sokClient = ElasticSearchTestConfiguration.esSokService(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);

    private final EsIndexerService indexerClient = ElasticSearchTestConfiguration.indexerCvService();

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

    private static List<String> aktorIder(List<EsCv> cver) {
        return cver.stream().map(EsCv::getAktorId).collect(toList());
    }

    @Test
    public void sjekkAtSlettingByAktorIdFungerer() {
        // Given
        final SokekriterierVeiledere ingenSøkekriterier = SokekriterierVeiledere.med().bygg();
        List<EsCv> cverFørSletting = sokClient.veilederSok(ingenSøkekriterier).getCver();
        List<String> aktorIderFørSletting = aktorIder(cverFørSletting);
        final List<String> aktorIdSkalSlettes = List.of(
                EsCvObjectMother.giveMeEsCv().getAktorId(),
                EsCvObjectMother.giveMeEsCv2().getAktorId());
        assertThat(cverFørSletting.size()).isGreaterThan(aktorIdSkalSlettes.size());
        assertThat(aktorIderFørSletting).containsAll(aktorIdSkalSlettes);

        // When
        assertThat(indexerClient.bulkSlettAktorId(aktorIdSkalSlettes, ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME)).isEqualTo(2);

        // Then
        List<EsCv> cverEtterSletting = sokClient.veilederSok(ingenSøkekriterier).getCver();
        assertThat(cverEtterSletting.size()).isEqualTo(cverFørSletting.size() - aktorIdSkalSlettes.size());
        List<String> aktorIderEtterSletting = aktorIder(cverEtterSletting);
        assertThat(aktorIderEtterSletting).doesNotContainAnyElementsOf(aktorIdSkalSlettes);
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
