package no.nav.arbeid.cv.es;

import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.palantir.docker.compose.DockerComposeRule;

import no.nav.arbeid.cv.indexer.es.CvListenerSuiteTest;
import no.nav.arbeid.cv.kandidatsok.es.IndexCvSuiteTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({CvListenerSuiteTest.class, IndexCvSuiteTest.class})
public class DockerComposeTest {

  @ClassRule
  public static DockerComposeRule docker =
      DockerComposeRule.builder().file("src/test/resources/docker-compose-kafka-og-es.yml").build();

}
