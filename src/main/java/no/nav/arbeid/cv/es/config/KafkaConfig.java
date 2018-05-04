package no.nav.arbeid.cv.es.config;

import no.nav.arbeid.cv.es.domene.OperationalException;
import no.nav.arbeid.cv.es.service.DltForwarder;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerAwareBatchErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableRetry
public class KafkaConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConfig.class);

    @Bean(name="kafkaListenerContainerFactory")
    @Autowired
    public ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(ConsumerFactory consumerFactory,
            @Value("${kafka.consumer.concurrency}") int concurrency,
            @Value("${kafka.retry.max}") int maxRetries,
            @Value("${kafka.retry.initial_interval}") int initialInterval,
            @Value("${kafka.retry.multiplier}") double multiplier,
            @Value("${kafka.retry.max_interval}") int maxInterval,
            DltForwarder dltForwarder) {
        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory();
        factory.setConcurrency(concurrency);

        ConsumerFactory f = new DefaultKafkaConsumerFactory(consumerFactory.getConfigurationProperties());
        factory.setConsumerFactory(f);
        factory.setBatchListener(true);

        factory.getContainerProperties().setBatchErrorHandler(batchErrorHandler());

        // TODO usikker på om vi egentlig bør gå for ackmode manual_immediate og kalle acknowledge manuelt - the jury is still out!
        /* retrytemplate støtter ikke batch...
        factory.setRetryTemplate(retryTemplate(maxRetries, initialInterval, multiplier, maxInterval));
        factory.setRecoveryCallback(context -> {
            LOGGER.error("Har rekjørt melding {} ganger. Gir opp og sender den til feiltopic.",
                    context.getRetryCount(), context.getLastThrowable());
            ConsumerRecord<?, ?> record = (ConsumerRecord) context.getAttribute(RetryingMessageListenerAdapter.CONTEXT_RECORD);

            dltForwarder.sendMeldingTilDLT(record, context.getLastThrowable());
            return null;
        });
        */
        return factory;
    }

    private RetryTemplate retryTemplate(int maxRetries,
                                        int initialInterval,
                                        double multiplier,
                                        int maxInterval) {
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(retryPolicy(maxRetries));
        retryTemplate.setBackOffPolicy(backoffPolicy(initialInterval, multiplier, maxInterval));
        retryTemplate.setThrowLastExceptionOnExhausted(false);
        return retryTemplate;
    }

    private RetryPolicy retryPolicy(int maxRetries) {
        SimpleRetryPolicy policy = new SimpleRetryPolicy(maxRetries);
        return policy;
    }

    private BackOffPolicy backoffPolicy(int initialInterval,
                                        double multiplier,
                                        int maxInterval) {
        ExponentialBackOffPolicy policy = new ExponentialBackOffPolicy();
        policy.setInitialInterval(initialInterval);
        policy.setMultiplier(multiplier);
        policy.setMaxInterval(maxInterval);

        return policy;
    }


    public ContainerAwareBatchErrorHandler batchErrorHandler() {
        return new ContainerAwareBatchErrorHandler() {

            @Override
            public void handle(Exception thrownException, ConsumerRecords<?, ?> data, Consumer<?, ?> consumer) {
                LOGGER.error("Uventet feil mottat: {}", thrownException.getMessage(), thrownException);
            }

            @Override
            public void handle(Exception e, ConsumerRecords<?, ?> consumerRecords, Consumer<?, ?> consumer, MessageListenerContainer messageListenerContainer) {
                boolean skalRekjore = skalRekjoreBatch(e, consumerRecords);
                MetaConsumerRecord mcr = beregnMetaConsumerRecord(consumerRecords);

                if (skalRekjore) {
                    LOGGER.info("Infrastrukturfeil ved mottak av batch med {} meldinger. Offset: {} - {}. Topic: {}, partisjon: {}: {}. Meldingene vil rekjøres.",
                            mcr.antall,
                            mcr.min,
                            mcr.max,
                            mcr.topicsSomString,
                            mcr.partisjonerSomString,
                            e.getMessage(),
                            e);

                    mcr.minPrTopicPartition.forEach((topicPartition, aLong) -> LOGGER.info("Partisjon: {}, offset: {}", topicPartition, aLong));
                    mcr.minPrTopicPartition.forEach(consumer::seek);
                    // TODO Burde pause messageListenerContainer, vente gitt antall sekunder, og resume av messageListenerContainer?
                    //      Mulig vi må håndtere retries bedre... Vi kan f.eks legge på en header på meldingene som sier hvor mange ganger
                    //      de har blitt rekjørt i tilfelle vi ønsker å gi opp etter noen dager (kan uansett være nyttig å logger hvor mange ganger/hvor lenge en batch blir rekjørt)
                } else {
                    LOGGER.error("Applikasjonsfeil ved mottak av batch med {} meldinger. Offset: {} - {}. Topic: {}, partisjon: {}: {}",
                            mcr.antall,
                            mcr.min,
                            mcr.max,
                            mcr.topicsSomString,
                            mcr.partisjonerSomString,
                            e.getMessage(),
                            e);
                    mcr.maxPrTopicPartition.forEach((topicPartition, offset) -> consumer.seek(topicPartition, offset+1));
                }

            }

            @Override
            public void handle(Exception thrownException, ConsumerRecords<?, ?> data) {
                LOGGER.error("Uventet feil mottattt: {}", thrownException.getMessage(), thrownException);
            }

            boolean skalRekjoreBatch(Exception exception, ConsumerRecords<?,?> records) {
                Throwable cause = (exception instanceof ListenerExecutionFailedException) ? exception.getCause() : exception;
                if (cause instanceof OperationalException) {
                    return true;
                }

                return false;
            }
        };

    }

    public MetaConsumerRecord beregnMetaConsumerRecord(ConsumerRecords<?, ?> records) {
        MetaConsumerRecord metaConsumerRecord = new MetaConsumerRecord();
        if (records != null) {
            metaConsumerRecord.antall = records.count();
            if (!records.isEmpty()) {
                metaConsumerRecord.partisjonerSomString =
                        records.partitions().stream()
                                .map(tp -> "" + tp.partition())
                                .collect(Collectors.joining(","));
                metaConsumerRecord.topicsSomString =
                        records.partitions().stream()
                                .map(tp -> tp.topic())
                                .collect(Collectors.joining(","));
            }

            for (ConsumerRecord<?, ?> record : records) {
                TopicPartition tp = new TopicPartition(record.topic(), record.partition());
                Long currentMinTp = metaConsumerRecord.minPrTopicPartition.get(tp);
                if (currentMinTp == null || currentMinTp > record.offset()) {
                    metaConsumerRecord.minPrTopicPartition.put(tp, record.offset());
                }
                Long currentMaxTp = metaConsumerRecord.maxPrTopicPartition.get(tp);
                if (currentMaxTp == null || currentMaxTp < record.offset()) {
                    metaConsumerRecord.maxPrTopicPartition.put(tp, record.offset());
                }

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


    public static class MetaConsumerRecord {
        public long min = Long.MAX_VALUE;
        public long max = Long.MIN_VALUE;
        public int antall = 0;
        public String topicsSomString = null;
        public String partisjonerSomString = null;

        public Map<TopicPartition, Long> minPrTopicPartition = new HashMap<>();
        public Map<TopicPartition, Long> maxPrTopicPartition = new HashMap<>();
    }


}
