package no.nav.arbeidsgiver.kandidat.kandidatsok.es;

import no.nav.arbeidsgiver.kandidat.kandidatsok.domene.es.EsCvObjectMother;
import no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsCv;
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

    private final List<no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsCv> alleIndekserteCver = List.of(
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
            EsCvObjectMother.giveMeCvFritattForKandidatsok());

    @BeforeEach
    public void before() {
        indexerClient.createIndex(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);
        indexerClient.bulkIndex(alleIndekserteCver, ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);
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
        final List<EsCv> cverSomSkalSlettes = List.of(
                alleIndekserteCver.get(0),
                alleIndekserteCver.get(1));
        assertThat(sokClient.veilederHent(cverSomSkalSlettes.get(0).getKandidatnr())).isPresent();
        assertThat(sokClient.veilederHent(cverSomSkalSlettes.get(1).getKandidatnr())).isPresent();

        final List<String> aktørIderHvisCverSkalSlettes = cverSomSkalSlettes.stream().map(EsCv::getAktorId).collect(toList());
        assertThat(indexerClient.bulkSlettAktorId(aktørIderHvisCverSkalSlettes, ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME)).isEqualTo(2);

        assertThat(sokClient.veilederHent(cverSomSkalSlettes.get(0).getKandidatnr())).isEmpty();
        assertThat(sokClient.veilederHent(cverSomSkalSlettes.get(0).getKandidatnr())).isEmpty();
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
