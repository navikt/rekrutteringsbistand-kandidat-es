package no.nav.arbeid.cv.es.service;

import no.nav.arbeid.cv.es.config.TopicNames;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.converter.MessagingMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

@Service
public class DefaultDltForwarder implements DltForwarder {
    private KafkaTemplate kafkaTemplate;
    private MessagingMessageConverter messageConverter = new MessagingMessageConverter();

    @Autowired
    public DefaultDltForwarder(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendMeldingTilDLT(ConsumerRecord melding, Throwable feilkilde) {
        RecordHeaders producerHeaders = new RecordHeaders(melding.headers());
        String feilmelding = feilkilde.getMessage();
        String stacktrace = hentStacktrace(feilkilde);

        producerHeaders.add("Feilmelding", feilmelding.getBytes());
        producerHeaders.add("Stacktrace", stacktrace.getBytes());

        ProducerRecord pr = new ProducerRecord(TopicNames.TOPIC_CVEVENT_V3_FEIL, null,
                melding.key(), melding.value(),
                producerHeaders);
        kafkaTemplate.send(pr);
    }

    @Override
    public void sendMeldingTilDLT(Message melding, Throwable feilkilde) {
        ProducerRecord pr = messageConverter.fromMessage(melding, TopicNames.TOPIC_CVEVENT_V3_FEIL);
        String feilmelding = feilkilde.getMessage();
        String stacktrace = hentStacktrace(feilkilde);

        pr.headers().add("Feilmelding", feilmelding.getBytes());
        pr.headers().add("Stacktrace", stacktrace.getBytes());

        kafkaTemplate.send(pr);
    }

    protected String hentStacktrace(Throwable feilkilde) {
        String stacktrace = "";
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            feilkilde.printStackTrace(new PrintStream(baos));
            baos.flush();
            stacktrace = baos.toString();
        } catch (IOException e) {
            // Ingenting å gjøre med dette...
        }
        return stacktrace;
    }

}
