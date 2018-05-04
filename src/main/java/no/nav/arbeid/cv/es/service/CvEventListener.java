package no.nav.arbeid.cv.es.service;

import no.nav.arbeid.cv.es.domene.ApplicationException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.errors.SerializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

import no.nav.arbeid.cv.es.config.TopicNames;
import no.nav.arbeid.cv.events.CvEvent;

import java.util.List;
import java.util.stream.Collectors;

public class CvEventListener {

  private final static Logger LOGGER = LoggerFactory.getLogger(CvEventListener.class);

  private final CvIndexerService cvIndexerService;

  public CvEventListener(CvIndexerService cvIndexerService) {
    this.cvIndexerService = cvIndexerService;
  }

  @KafkaListener(topics = TopicNames.TOPIC_CVEVENT_V3, containerFactory = "kafkaListenerContainerFactory")
  public void processConsumerRecord(List<ConsumerRecord<Long, CvEvent>> records) throws Exception {
    LOGGER.info("{} events mottatt.", records.size());

    try {
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
    } catch (ApplicationException | IllegalArgumentException | NullPointerException | SerializationException e) {
      // TODO Vurderer å la all feilhåndtering skje i errorhandleren som ligger i KafkaConfig
      MetaConsumerRecord metaConsumerRecord = beregnMetaConsumerRecord(records);
      LOGGER.error("Applikasjonsfeil ved mottak av batch med {} meldinger. Offset: {} - {}. Topic: {}, partisjon: {}: {}",
              metaConsumerRecord.antall,
              metaConsumerRecord.min,
              metaConsumerRecord.max,
              metaConsumerRecord.topic,
              metaConsumerRecord.partisjon,
              e.getMessage(),
              e);
    }
  }

  private MetaConsumerRecord beregnMetaConsumerRecord(List<ConsumerRecord<Long,CvEvent>> records) {
    MetaConsumerRecord metaConsumerRecord = new MetaConsumerRecord();
    if (records != null) {
      metaConsumerRecord.antall = records.size();
      if (records.size() > 0) {
        metaConsumerRecord.partisjon = records.get(0).partition();
        metaConsumerRecord.topic = records.get(0).topic();
      }

      for (ConsumerRecord<Long, CvEvent> record : records) {
        if (metaConsumerRecord.min > record.offset()) {
          metaConsumerRecord.min = record.offset();
        }
        if (metaConsumerRecord.max < record.offset()) {
          metaConsumerRecord.max = record.offset();
        }
      }
    }

    return metaConsumerRecord;
  }


  private static class MetaConsumerRecord {
    public long min = Long.MAX_VALUE;
    public long max = Long.MIN_VALUE;
    public int antall = 0;
    public String topic = null;
    public int partisjon = 0;
  }
}
