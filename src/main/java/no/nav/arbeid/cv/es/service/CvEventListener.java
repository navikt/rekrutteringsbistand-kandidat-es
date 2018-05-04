package no.nav.arbeid.cv.es.service;

import no.nav.arbeid.cv.es.config.TopicNames;
import no.nav.arbeid.cv.events.CvEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.retry.support.RetryTemplate;

import java.util.List;
import java.util.stream.Collectors;

public class CvEventListener {

  private final static Logger LOGGER = LoggerFactory.getLogger(CvEventListener.class);

  private final CvIndexerService cvIndexerService;
  private final RetryTemplate retryTemplate;

  public CvEventListener(CvIndexerService cvIndexerService, RetryTemplate retryTemplate) {
    this.cvIndexerService = cvIndexerService;
    this.retryTemplate = retryTemplate;
  }

  @KafkaListener(topics = TopicNames.TOPIC_CVEVENT_V3, containerFactory = "kafkaListenerContainerFactory")
  public void processConsumerRecord(List<ConsumerRecord<Long, CvEvent>> records) throws Exception {
    LOGGER.info("{} events mottatt.", records.size());

    retryTemplate.execute(context -> {
      List<CvEvent> indekserEvents = records.stream()
              .filter(r -> r.value() != null)
              .map(r -> r.value())
              .collect(Collectors.toList());
      List<Long> arenaIderSomSkalSlettes = records.stream()
              .filter(r -> r.value() == null)
              .map(r -> r.key())
              .collect(Collectors.toList());

      cvIndexerService.bulkIndekser(indekserEvents);
      cvIndexerService.bulkSlett(arenaIderSomSkalSlettes);
      return null;
    });
    LOGGER.info("Hva nå?");
  }
}
