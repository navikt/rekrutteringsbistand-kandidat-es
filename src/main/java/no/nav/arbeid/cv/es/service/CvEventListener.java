package no.nav.arbeid.cv.es.service;

import no.nav.arbeid.cv.es.domene.ApplicationException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.errors.SerializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.kafka.annotation.KafkaListener;

import no.nav.arbeid.cv.es.config.TopicNames;
import no.nav.arbeid.cv.events.CvEvent;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;

public class CvEventListener {

  private final static Logger LOGGER = LoggerFactory.getLogger(CvEventListener.class);

  private final CvIndexerService cvIndexerService;
  private final DltForwarder dltForwarder;
  private final ConversionService conversionService;

  public CvEventListener(CvIndexerService cvIndexerService, DltForwarder dltForwarder, ConversionService conversionService) {
    this.cvIndexerService = cvIndexerService;
    this.dltForwarder = dltForwarder;
    this.conversionService = conversionService;
  }

  @KafkaListener(topics = TopicNames.TOPIC_CVEVENT_V3, containerFactory = "kafkaListenerContainerFactory", errorHandler = "dltErrorHandler")
  public void processConsumerRecord(ConsumerRecord<?,?> record,
                                    @Header(KafkaHeaders.OFFSET) Integer offset) throws Exception {
    LOGGER.info("Event {} mottatt " + record, offset);

    try {
      CvEvent cvEvent = conversionService.convert(record.value(), CvEvent.class);
      cvIndexerService.indekser(cvEvent);
    } catch (ApplicationException | IllegalArgumentException | NullPointerException | SerializationException e) {
      LOGGER.info("Applikasjonsfeil ved mottak av melding {}", offset, e);
      dltForwarder.sendMeldingTilDLT(record, e);
    }
  }
}
