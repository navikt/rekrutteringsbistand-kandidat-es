package no.nav.arbeid.cv.kandidatsok.es;

import no.nav.arbeid.cv.kandidatsok.domene.es.EsCvObjectMother;
import no.nav.arbeid.cv.kandidatsok.es.domene.EsCv;
import no.nav.arbeid.cv.kandidatsok.testsupport.ElasticSearchIntegrationTestExtension;
import no.nav.arbeid.cv.kandidatsok.testsupport.ElasticSearchTestConfiguration;
import no.nav.arbeid.kandidatsok.es.client.EsIndexerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static no.nav.arbeid.cv.kandidatsok.testsupport.ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ElasticSearchIntegrationTestExtension.class)
public class DokumentTellingIT {

    private EsIndexerService esIndexerService = ElasticSearchTestConfiguration.indexerCvService();

    @BeforeEach
    public void before() {
        esIndexerService.createIndex(DEFAULT_INDEX_NAME);

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
        ), DEFAULT_INDEX_NAME);
    }

    @AfterEach
    public void after() {
        esIndexerService.deleteIndex(DEFAULT_INDEX_NAME);
    }

    @Test
    public void testDokumentTelling() {
        assertThat(esIndexerService.antallIndeksert(DEFAULT_INDEX_NAME)).isEqualTo(12);
        assertThat(esIndexerService.antallIndeksertSynligForArbeidsgiver(DEFAULT_INDEX_NAME)).isEqualTo(6);
        assertThat(esIndexerService.antallIndeksertSynligForVeileder(DEFAULT_INDEX_NAME)).isEqualTo(7);
    }

}
