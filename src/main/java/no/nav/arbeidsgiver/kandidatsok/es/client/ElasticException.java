package no.nav.arbeidsgiver.kandidatsok.es.client;

@Deprecated
// TODO bytt ut til å bruke ElasticSearchException
public class ElasticException extends RuntimeException {

    public ElasticException(Exception ioe) {
        super(ioe);
    }

}
