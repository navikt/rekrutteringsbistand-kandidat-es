package no.nav.arbeid.cv.es.es;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.palantir.docker.compose.DockerComposeRule;

import no.nav.arbeid.cv.es.client.EsCvClient;
import no.nav.arbeid.cv.es.config.ServiceConfig;
import no.nav.arbeid.cv.es.config.temp.TempCvEventObjectMother;
import no.nav.arbeid.cv.es.domene.EsCv;
import no.nav.arbeid.cv.es.domene.Sokeresultat;
import no.nav.arbeid.cv.es.service.CvEventObjectMother;
import no.nav.arbeid.cv.es.service.EsCvTransformer;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IndexCvTest {

  private static final String ES_DOCKER_SERVICE = "elastic_search";

  /*
   * For å kunne kjøre denne testen må Linux rekonfigureres litt.. Lag en fil i
   * /etc/sysctl.d/01-increase_vm_max_map_count.conf som inneholder følgende: vm.max_map_count =
   * 262144
   */

  // Kjører "docker-compose up" manuelt istedenfor denne ClassRule:

  @ClassRule
  public static DockerComposeRule docker =
      DockerComposeRule.builder().file("src/test/resources/docker-compose-kun-es.yml")
          // .waitingForHostNetworkedPort(9200, port -> SuccessOrFailure
          // .fromBoolean(port.isListeningNow(), "Internal port " + port + " was not listening"))
          .build();

  @Autowired
  private EsCvTransformer transformer;

  @Autowired
  private EsCvClient client;

  @TestConfiguration
  @OverrideAutoConfiguration(enabled = true)
  @ImportAutoConfiguration(classes = {}, exclude = {KafkaAutoConfiguration.class,
      DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
  @Import({ServiceConfig.class})
  static class TestConfig {

    @Bean
    public RestHighLevelClient restHighLevelClient() {
      return new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
    }

  }

  @Before
  public void before() throws IOException {
    try {
      client.deleteIndex();
    } catch (Exception e) {
      // Ignore
    }

    client.createIndex();

    EsCv esCv = transformer.transform(CvEventObjectMother.giveMeCvEvent());
    client.index(esCv);
  }

  @After
  public void after() throws IOException {
    client.deleteIndex();
  }

  @Test
  public void test() throws IOException {
    Sokeresultat sokeres =
        client.findByEtternavnAndUtdanningNusKodeGrad("NORDMANN", "Mekaniske fag, grunnkurs");
    List<EsCv> list = sokeres.getCver();
    assertThat(list.size()).isEqualTo(1);
    EsCv esCv = list.get(0);
    assertThat(esCv).isEqualTo(transformer.transform(CvEventObjectMother.giveMeCvEvent()));
  }

}
