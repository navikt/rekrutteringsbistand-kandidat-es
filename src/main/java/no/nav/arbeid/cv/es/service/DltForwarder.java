package no.nav.arbeid.cv.es.service;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.messaging.Message;

/**
 * Videresender kafkameldinger til feiltopic (dead letter topic)
 */
public interface DltForwarder {
    public void sendMeldingTilDLT(ConsumerRecord melding, Throwable feilkilde);
    public void sendMeldingTilDLT(Message melding, Throwable feilkilde);
}
