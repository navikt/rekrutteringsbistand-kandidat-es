package no.nav.arbeid.cv.kandidatsok.domene.sok;

/**
 * ApplicationException representerer feil somm det ikke gir mening å rekjøre.
 */
public class ApplicationException extends RuntimeException {
  public ApplicationException(String message, Throwable source) {
    super(message, source);
  }
}
