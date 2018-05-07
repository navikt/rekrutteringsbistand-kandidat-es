package no.nav.arbeid.cv.es;

import com.palantir.docker.compose.DockerComposeRule;
import no.nav.arbeid.cv.es.es.CvListenerTest;
import no.nav.arbeid.cv.es.es.IndexCvTest;
import no.nav.arbeid.cv.es.rest.SearchControllerTest;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({CvListenerTest.class, IndexCvTest.class, SearchControllerTest.class})
public class DockerComposeTest {
    @ClassRule
    public static DockerComposeRule docker =
            DockerComposeRule.builder().file("src/test/resources/docker-compose-kafka-og-es.yml")
                    .build();

}
