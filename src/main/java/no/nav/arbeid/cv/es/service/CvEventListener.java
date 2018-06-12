package no.nav.arbeid.cv.es.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.retry.support.RetryTemplate;

import no.nav.arbeid.cv.es.config.TopicNames;
import no.nav.arbeid.cv.events.CvEvent;

public class CvEventListener implements ConsumerSeekAware {

  private final static Logger LOGGER = LoggerFactory.getLogger(CvEventListener.class);

  private final CvIndexerService cvIndexerService;
  private final RetryTemplate retryTemplate;

  public CvEventListener(CvIndexerService cvIndexerService, RetryTemplate retryTemplate) {
    this.cvIndexerService = cvIndexerService;
    this.retryTemplate = retryTemplate;
  }

  @KafkaListener(topics = TopicNames.TOPIC_CVEVENT_V3,
      containerFactory = "kafkaListenerContainerFactory")
  public void processConsumerRecord(List<ConsumerRecord<Long, CvEvent>> records) throws Exception {
    LOGGER.info("{} events mottatt.", records.size());

    retryTemplate.execute(context -> {
      List<CvEvent> indekserEvents = records.stream().filter(r -> r.value() != null)
          .map(r -> r.value()).collect(Collectors.toList());
      List<Long> arenaIderSomSkalSlettes = records.stream().filter(r -> r.value() == null)
          .map(r -> r.key()).collect(Collectors.toList());

      cvIndexerService.bulkIndekser(indekserEvents);
      cvIndexerService.bulkSlett(arenaIderSomSkalSlettes);
      return null;
    });
  }


  private final ThreadLocal<ConsumerSeekCallback> seekCallBack = new ThreadLocal<>();

  @Override
  public void registerSeekCallback(ConsumerSeekCallback callback) {
    this.seekCallBack.set(callback);

  }

  @Override
  public void onPartitionsAssigned(Map<TopicPartition, Long> assignments,
      ConsumerSeekCallback callback) {
    LOGGER.info("Spoler tilbake lesing av alle partitions på topic");
    assignments.forEach((t, o) -> callback.seekToBeginning(t.topic(), t.partition()));

  }

  @Override
  public void onIdleContainer(Map<TopicPartition, Long> assignments,
      ConsumerSeekCallback callback) {

  }
}
