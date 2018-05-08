package no.nav.arbeid.cv.es.domene;

/**
 * ApplicationException representerer feil somm det ikke gir mening å rekjøre.
 */
public class ApplicationException extends RuntimeException {
    public ApplicationException(String message, Throwable source) {
        super(message, source);
    }
}
