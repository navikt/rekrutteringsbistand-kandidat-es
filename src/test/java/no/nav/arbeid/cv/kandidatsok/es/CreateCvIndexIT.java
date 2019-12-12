package no.nav.arbeid.cv.kandidatsok.es;

import no.nav.arbeid.cv.kandidatsok.testsupport.ElasticSearchTestExtension;
import no.nav.arbeid.kandidatsok.es.client.EsIndexerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.Assert.assertTrue;

@ExtendWith(ElasticSearchTestExtension.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ElasticSearchTestConfiguration.class)
public class CreateCvIndexIT {

    @Autowired
    private EsIndexerService esIndexerService;

    @Test
    public void testCreateCvIndex() {
        this.esIndexerService.createIndex(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);

        assertTrue(this.esIndexerService.doesIndexExist(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME));

        this.esIndexerService.deleteIndex(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);
    }

}
