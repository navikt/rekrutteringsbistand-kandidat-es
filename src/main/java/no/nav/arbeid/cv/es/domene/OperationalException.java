package no.nav.arbeid.cv.es.domene;

/**
 * OperationalException representerer infrastrukturfeil.
 * Handlingen som forårsaket dette kan rekjøres og vil kanskje lykkes etter hvert.
 */
public class OperationalException extends RuntimeException {
    public OperationalException(String message, Throwable source) {
        super(message, source);
    }
}
