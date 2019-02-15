package no.nav.arbeid.kandidatsok.es.client;

import java.io.IOException;

public class ElasticException extends RuntimeException {

    public ElasticException(Exception ioe) {
        super(ioe);
    }

}
