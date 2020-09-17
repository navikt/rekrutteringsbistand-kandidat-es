package no.nav.arbeidsgiver.kandidat.kandidatsok.es;

import no.nav.arbeidsgiver.kandidat.kandidatsok.testsupport.ElasticSearchTestConfiguration;
import no.nav.arbeidsgiver.kandidat.kandidatsok.testsupport.ElasticSearchIntegrationTestExtension;
import no.nav.arbeidsgiver.kandidatsok.es.client.EsIndexerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.Assert.assertTrue;

@ExtendWith(ElasticSearchIntegrationTestExtension.class)
public class CreateCvIndexIT {

    private EsIndexerService esIndexerService = ElasticSearchTestConfiguration.indexerCvService();

    @Test
    public void testCreateCvIndex() {
        this.esIndexerService.createIndex(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);

        assertTrue(this.esIndexerService.doesIndexExist(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME));

        this.esIndexerService.deleteIndex(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);
    }

}
