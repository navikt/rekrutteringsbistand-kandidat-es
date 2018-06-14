package no.nav.arbeid.cv.indexer.service;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import no.nav.arbeid.cv.events.CvEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.retry.support.RetryTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CvEventListener implements ConsumerSeekAware {

  private final static Logger LOGGER = LoggerFactory.getLogger(CvEventListener.class);

  private final CvIndexerService cvIndexerService;
  private final RetryTemplate retryTemplate;
  private final MeterRegistry meterRegistry;

  public CvEventListener(CvIndexerService cvIndexerService, RetryTemplate retryTemplate, MeterRegistry meterRegistry) {
    this.cvIndexerService = cvIndexerService;
    this.retryTemplate = retryTemplate;
    this.meterRegistry = meterRegistry;
  }

  @KafkaListener(topics = "${ARENACV_TOPIC}", containerFactory = "kafkaListenerContainerFactory")
  public void processConsumerRecord(List<ConsumerRecord<Long, CvEvent>> records) throws Exception {
    LOGGER.info("{} events mottatt.", records.size());
    final String timerName = "cv.kafka.motta";
    Timer.Sample sample = Timer.start(meterRegistry);
    try {
      meterRegistry.counter("cv.kafka.mottatt").increment(records.size());
      retryTemplate.execute(context -> {
        List<CvEvent> indekserEvents = records.stream().filter(r -> r.value() != null)
                .map(r -> r.value()).collect(Collectors.toList());
        List<Long> arenaIderSomSkalSlettes = records.stream().filter(r -> r.value() == null)
                .map(r -> r.key()).collect(Collectors.toList());

        cvIndexerService.bulkIndekser(indekserEvents);
        cvIndexerService.bulkSlett(arenaIderSomSkalSlettes);
        return null;
      });
    } finally {
      sample.stop(Timer.builder(timerName)
              .description(null)
              .publishPercentileHistogram(true)
              .register(meterRegistry));
    }
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
