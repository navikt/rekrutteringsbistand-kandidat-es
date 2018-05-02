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

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.timeout;

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

  @BeforeClass
  public static void spolTilSluttenAvTopic() throws Exception {
    // TODO Dette er litt klønete: vi må gjøre dette før vi fyrer opp spring og kafka message listeneren,
    //      men da har vi ikke tilgang på konfigurasjonsverdiene til spring. Bør vurdere å gjøre dette annerledes...
    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9092");
    props.put("group.id", "pam-cv-index");
    props.put("key.deserializer", "org.apache.kafka.common.serialization.LongDeserializer");
    props.put("value.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer");
    props.put("schema.registry.url", "http://localhost:8081");

    KafkaConsumer kafkaConsumer = new KafkaConsumer(props);
    kafkaConsumer.subscribe(Collections.singletonList(TopicNames.TOPIC_CVEVENT_V3));
    Collection<TopicPartition> topicPartitions =
            Collections.singletonList(new TopicPartition(TopicNames.TOPIC_CVEVENT_V3, 0));
//    kafkaConsumer.assign(topicPartitions);
    ConsumerRecords cr = kafkaConsumer.poll(10);
    while (!cr.isEmpty())
      cr = kafkaConsumer.poll(10);
    kafkaConsumer.seekToEnd(topicPartitions);
    kafkaConsumer.commitSync();
    kafkaConsumer.close();
  }

  @Test
  public void skalMottaOgIndeksereEvents() {
    CvEvent publisertEvent = TempCvEventObjectMother.giveMeCvEvent();
    kafkaTemplate.send(TopicNames.TOPIC_CVEVENT_V3, publisertEvent);

    ArgumentCaptor<CvEvent> mottattEventCaptor = ArgumentCaptor.forClass(CvEvent.class);
    verify(cvIndexerServiceMock, timeout(500l).atLeast(1)).indekser(mottattEventCaptor.capture());
    CvEvent mottattEvent = mottattEventCaptor.getValue();

    assertThat(mottattEvent.getFodselsdato()).isEqualTo(publisertEvent.getFodselsdato());
  }

  @Test
  public void skalSendeApplikasjonsfeilTilFeilTopicOgFortsetteMedProsesseringAvEvents() {
    Mockito.doThrow(new ApplicationException("Applikasjonsfeil", new NullPointerException()))
            .when(cvIndexerServiceMock).indekser(ArgumentMatchers.argThat(cv -> cv.getAdresselinje1().equals("adresselinje2")));

    CvEvent publisertEvent1 = TempCvEventObjectMother.giveMeCvEvent();
    publisertEvent1.setAdresselinje1("adresse1");
    CvEvent publisertEvent2 = TempCvEventObjectMother.giveMeCvEvent();
    publisertEvent2.setAdresselinje1("adresse2");
    CvEvent publisertEvent3 = TempCvEventObjectMother.giveMeCvEvent();
    publisertEvent3.setAdresselinje1("adresse3");
    kafkaTemplate.send(TopicNames.TOPIC_CVEVENT_V3, publisertEvent1);
    kafkaTemplate.send(TopicNames.TOPIC_CVEVENT_V3, publisertEvent2);
    kafkaTemplate.send(TopicNames.TOPIC_CVEVENT_V3, publisertEvent3);

    ArgumentCaptor<CvEvent> mottattEventCaptor = ArgumentCaptor.forClass(CvEvent.class);
    verify(cvIndexerServiceMock, timeout(500l).atLeast(1)).indekser(mottattEventCaptor.capture());
    List<CvEvent> mottatteEvents = mottattEventCaptor.getAllValues();

    assertThat(mottatteEvents.size()).isGreaterThanOrEqualTo(3);
  }

}
