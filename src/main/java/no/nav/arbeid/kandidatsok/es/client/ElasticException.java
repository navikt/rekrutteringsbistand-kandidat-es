package no.nav.arbeid.kandidatsok.es.client;

public class ElasticException extends RuntimeException {

    public ElasticException(Exception ioe) {
        super(ioe);
    }

}
