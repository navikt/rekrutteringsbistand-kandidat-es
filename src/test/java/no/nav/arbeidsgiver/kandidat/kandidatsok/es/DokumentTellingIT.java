package no.nav.arbeidsgiver.kandidat.kandidatsok.es;

import no.nav.arbeidsgiver.kandidat.kandidatsok.domene.es.EsCvObjectMother;
import no.nav.arbeidsgiver.kandidat.kandidatsok.testsupport.ElasticSearchIntegrationTestExtension;
import no.nav.arbeidsgiver.kandidat.kandidatsok.testsupport.ElasticSearchTestConfiguration;
import no.nav.arbeidsgiver.kandidatsok.es.client.EsIndexerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ElasticSearchIntegrationTestExtension.class)
public class DokumentTellingIT {

    private EsIndexerService esIndexerService = ElasticSearchTestConfiguration.indexerCvService();

    @BeforeEach
    public void before() {
        esIndexerService.createIndex(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);

        esIndexerService.bulkIndex(List.of(
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
                EsCvObjectMother.giveMeCvFritattForAgKandidatsok2(),
                EsCvObjectMother.giveMeCvFritattForKandidatsok()
        ), ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);
    }

    @AfterEach
    public void after() {
        esIndexerService.deleteIndex(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);
    }

    @Test
    public void testDokumentTelling() {
        assertThat(esIndexerService.antallIndeksert(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME)).isEqualTo(12);
        assertThat(esIndexerService.antallIndeksertSynligForArbeidsgiver(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME)).isEqualTo(6);
        assertThat(esIndexerService.antallIndeksertSynligForVeileder(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME)).isEqualTo(7);
    }

}
