package no.nav.arbeid.cv.es.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

import no.nav.arbeid.cv.es.config.TopicNames;
import no.nav.arbeid.cv.es.service.CvIndexerService;
import no.nav.arbeid.cv.events.CvEvent;

public class CvEventListener {

  private final static Logger LOGGER = LoggerFactory.getLogger(CvEventListener.class);

  private final CvIndexerService cvIndexerService;

  public CvEventListener(CvIndexerService cvIndexerService) {
    this.cvIndexerService = cvIndexerService;
  }

  @KafkaListener(topics = TopicNames.TOPIC_CVEVENT_V3)
  public void processMessage(CvEvent cvEvent) throws Exception {

    LOGGER.info("Event mottatt" + cvEvent.toString());
    cvIndexerService.indekser(cvEvent);
  }

}
