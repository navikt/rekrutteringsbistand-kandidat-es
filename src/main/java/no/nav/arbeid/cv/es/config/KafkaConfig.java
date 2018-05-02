package no.nav.arbeid.cv.es.config;

import no.nav.arbeid.cv.es.service.DltForwarder;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.kafka.listener.adapter.RetryingMessageListenerAdapter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

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

        // TODO usikker på om vi egentlig bør gå for ackmode manual_immediate og kalle acknowledge manuelt - the jury is still out!
        factory.setRetryTemplate(retryTemplate(maxRetries, initialInterval, multiplier, maxInterval));
        factory.setRecoveryCallback(context -> {
            LOGGER.error("Har rekjørt melding {} ganger. Gir opp og sender den til feiltopic.",
                    context.getRetryCount(), context.getLastThrowable());
            ConsumerRecord<?, ?> record = (ConsumerRecord) context.getAttribute(RetryingMessageListenerAdapter.CONTEXT_RECORD);

            dltForwarder.sendMeldingTilDLT(record, context.getLastThrowable());
            return null;
        });

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

    @Bean(name="dltErrorHandler")
    @Autowired
    public KafkaListenerErrorHandler dltErrorHandler(DltForwarder dltForwarder) {
        return new KafkaListenerErrorHandler() {

            @Override
            public Object handleError(Message<?> message, ListenerExecutionFailedException exception) throws Exception {
                LOGGER.error("Uventet feil ved mottak av kafkamelding. Meldingen legges på feiltopic: {}", message, exception);

                MessageHeaders headers = message.getHeaders();
                dltForwarder.sendMeldingTilDLT(message, exception);
                return null;
            }

            @Override
            public Object handleError(Message<?> message, ListenerExecutionFailedException exception, Consumer<?, ?> consumer) throws Exception {
                LOGGER.error("Uventet feil ved mottak av kafkamelding. Meldingen legges på feiltopic: {}", message, exception);
                dltForwarder.sendMeldingTilDLT(message, exception);
                return null;
            }
        };

    }

}
