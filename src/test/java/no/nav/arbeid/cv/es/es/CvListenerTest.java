package no.nav.arbeid.cv.es.es;

import com.palantir.docker.compose.DockerComposeRule;
import no.nav.arbeid.cv.es.client.EsCvClient;
import no.nav.arbeid.cv.es.config.KafkaConfig;
import no.nav.arbeid.cv.es.config.ServiceConfig;
import no.nav.arbeid.cv.es.config.TopicNames;
import no.nav.arbeid.cv.es.config.temp.*;
import no.nav.arbeid.cv.es.domene.Aggregering;
import no.nav.arbeid.cv.es.domene.ApplicationException;
import no.nav.arbeid.cv.es.domene.EsCv;
import no.nav.arbeid.cv.es.domene.Sokeresultat;
import no.nav.arbeid.cv.es.service.CvEventListener;
import no.nav.arbeid.cv.es.service.CvIndexerService;
import no.nav.arbeid.cv.es.service.DltForwarder;
import no.nav.arbeid.cv.es.service.EsCvTransformer;
import no.nav.arbeid.cv.events.CvEvent;
import org.apache.http.HttpHost;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.hamcrest.CoreMatchers;
import static org.hamcrest.CoreMatchers.equalTo;

import org.hamcrest.Matchers;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.timeout;
import static org.mockito.ArgumentMatchers.argThat;

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
import org.springframework.core.convert.ConversionService;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CvListenerTest {

  private static final String ES_DOCKER_SERVICE = "elastic_search";

  /*
   * For å kunne kjøre denne testen må Linux rekonfigureres litt.. Lag en fil i
   * /etc/sysctl.d/01-increase_vm_max_map_count.conf som inneholder følgende: vm.max_map_count =
   * 262144
   */

  // Kjører "docker-compose up" manuelt istedenfor denne ClassRule:

//  @ClassRule
//  public static DockerComposeRule docker =
//      DockerComposeRule.builder().file("src/test/resources/docker-compose-kafka-og-es.yml")
          // .waitingForHostNetworkedPort(9200, port -> SuccessOrFailure
          // .fromBoolean(port.isListeningNow(), "Internal port " + port + " was not listening"))
//          .build();

  @Autowired
  private EsCvTransformer transformer;

  @Autowired
  private EsCvClient client;

  @Autowired
  private KafkaTemplate kafkaTemplate;

  @Autowired
  private CvIndexerService cvIndexerServiceMock;

  @Autowired()
  private ConcurrentKafkaListenerContainerFactory kafkaListenerContainerFactory;

  @TestConfiguration
  @OverrideAutoConfiguration(enabled = true)
  @ImportAutoConfiguration(classes = {KafkaAutoConfiguration.class}, exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
  @Import({ServiceConfig.class, KafkaConfig.class})
  static class TestConfig {
    @Bean
    public CvIndexerService cvIndexerService() {
      return Mockito.mock(CvIndexerService.class);
    }

    @Bean
    public RestHighLevelClient restHighLevelClient() {
      return new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
    }
  }

  @Test
  public void skalMottaOgIndeksereEvents() {
    Mockito.reset(cvIndexerServiceMock);

    CvEvent publisertEvent = TempCvEventObjectMother.giveMeCvEvent();
    publisertEvent.setAdresselinje1(UUID.randomUUID().toString());
    kafkaTemplate.send(TopicNames.TOPIC_CVEVENT_V3, publisertEvent);

    ArgumentCaptor<CvEvent> mottattEventCaptor = ArgumentCaptor.forClass(CvEvent.class);
    verify(cvIndexerServiceMock, timeout(500l).times(1))
            .indekser(argThat(e -> publisertEvent.getAdresselinje1().equals(e.getAdresselinje1())));
  }

  @Test
  public void skalSendeApplikasjonsfeilTilFeilTopicOgFortsetteMedProsesseringAvEvents() {
    String feilendeAdresse = UUID.randomUUID().toString();
    String okAdresse1 =  UUID.randomUUID().toString();
    String okAdresse2 =  UUID.randomUUID().toString();

    Mockito.reset(cvIndexerServiceMock);
    Mockito.doThrow(new ApplicationException("Applikasjonsfeil", new NullPointerException()))
            .when(cvIndexerServiceMock).indekser(ArgumentMatchers.argThat(cv -> cv.getAdresselinje1().equals(feilendeAdresse)));

    CvEvent publisertEvent1 = TempCvEventObjectMother.giveMeCvEvent();
    publisertEvent1.setAdresselinje1(okAdresse1);
    CvEvent publisertEvent2 = TempCvEventObjectMother.giveMeCvEvent();
    publisertEvent2.setAdresselinje1(feilendeAdresse);
    CvEvent publisertEvent3 = TempCvEventObjectMother.giveMeCvEvent();
    publisertEvent3.setAdresselinje1(okAdresse2);
    kafkaTemplate.send(TopicNames.TOPIC_CVEVENT_V3, publisertEvent1);
    kafkaTemplate.send(TopicNames.TOPIC_CVEVENT_V3, publisertEvent2);
    kafkaTemplate.send(TopicNames.TOPIC_CVEVENT_V3, publisertEvent3);

    verify(cvIndexerServiceMock, timeout(500l).times(1))
            .indekser(argThat(e -> okAdresse1.equals(e.getAdresselinje1())));
    verify(cvIndexerServiceMock, timeout(500l).times(1))
            .indekser(argThat(e -> okAdresse2.equals(e.getAdresselinje1())));
    verify(cvIndexerServiceMock, timeout(100l).times(1))
            .indekser(argThat(e -> feilendeAdresse.equals(e.getAdresselinje1())));

    /* TODO Burde egentlig verifisere at det ble lagt en melding på feiltopic'et.
     *      Siden vi sannsynligvis ikke kommer til å bruke et eget topic for feil, så vil vi vi måtte verifisere noe annet
     *      (f.eks verifisere at vi kaller en eller annen feilhåndterer - DltForwarder er det som brukes nå)
     *      Som en kuriositet kan nevnes at hvis vi ender med å bruke et eget topic for feil, så bør det topicet være skjemaløst.
     *      Jeg har fått feil pga at jeg har prøvd å sende meldinger til feiltopic som ikke bruker riktig skjema.
     *      Da ender feilhåndteringen av giftpiller opp med selv å bli en giftpille...
     */
  }

  @Test
  public void skalKunneMottaSlettemeldinger() throws Exception {
    Mockito.reset(cvIndexerServiceMock);

    kafkaTemplate.send(TopicNames.TOPIC_CVEVENT_V3, 1l, null);
    verify(cvIndexerServiceMock, timeout(100l).times(1))
            .indekser(argThat(e -> e == null));

  }
}
