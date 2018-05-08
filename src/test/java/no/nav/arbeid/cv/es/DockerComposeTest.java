package no.nav.arbeid.cv.es;

import com.palantir.docker.compose.DockerComposeRule;
import no.nav.arbeid.cv.es.es.CvListenerSuiteTest;
import no.nav.arbeid.cv.es.es.IndexCvSuiteTest;
import no.nav.arbeid.cv.es.rest.SearchControllerSuiteTest;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({CvListenerSuiteTest.class, IndexCvSuiteTest.class, SearchControllerSuiteTest.class})
public class DockerComposeTest {
    @ClassRule
    public static DockerComposeRule docker =
            DockerComposeRule.builder().file("src/test/resources/docker-compose-kafka-og-es.yml")
                    .build();

}
