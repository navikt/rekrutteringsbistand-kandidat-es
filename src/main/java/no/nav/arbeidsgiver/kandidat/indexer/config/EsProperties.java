package no.nav.arbeidsgiver.kandidat.indexer.config;

import java.util.Optional;

public interface EsProperties {

    String getUser();

    String getPassword();

    String getTrustStoreFilename();

    String getTrustStorePass();

    String getKeyStoreFilename();

    String getKeyStorePass();

    String getHostname();

    int getPort();

    String getScheme();

    default int getRequestTimeoutMS() {
        return 30000;
    }

    default Optional<String> getApiKey() {
        return Optional.empty();
    }

    default Optional<String> getPathPrefix() {
        return Optional.empty();
    }

}
