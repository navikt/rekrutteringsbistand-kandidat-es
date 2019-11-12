package no.nav.arbeid.cv.kandidatsok.testsupport;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

/**
 * Ensures that ElasticSearch is running before integration tests start.
 *
 * <p>On a Linux host with Docker, you may need to set an OS option for ES to work.</p>
 * <p>Add file:</p>
 * <pre>/etc/sysctl.d/01-increase_vm_max_map_count.conf</pre>
 * <p>with contents:</p>
 * <pre>vm.max_map_count = 262144</pre>
 */
public class ElasticSearchIntegrationTestExtension implements BeforeAllCallback, ExtensionContext.Store.CloseableResource {

    private static DockerComposeEnv dce = null;

    private static final Logger LOG = LoggerFactory.getLogger(ElasticSearchIntegrationTestExtension.class);

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        if (dce != null) {
            return;
        }
        LOG.info("Bring up ElasticSearch with docker-compose on port {} ..", System.getProperty("ES_PORT"));
        dce = newDockerComposeEnv();
        extensionContext.getRoot().getStore(GLOBAL).put(getClass().getName(), this);
    }

    private DockerComposeEnv newDockerComposeEnv() throws Exception {
        return DockerComposeEnv.builder("src/test/resources/docker-compose-kun-es.yml")
                .addEnvVariable("ES_PORT", System.getProperty("ES_PORT"))
                .readyOnHttpGet2xx("http://localhost:{VALUE}", "ES_PORT")
                .dockerComposeLogDir("target")
                .up();
    }

    @Override
    public void close() throws Exception {
        if (dce != null) {
            LOG.info("Shut down ElasticSearch ..");
            dce.down();
        }
    }

}
