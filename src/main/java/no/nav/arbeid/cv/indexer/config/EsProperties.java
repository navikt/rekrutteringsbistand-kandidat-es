package no.nav.arbeid.cv.indexer.config;

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

  default Optional<String> getApiKey() {
    return Optional.empty();
  }

  default Optional<String> getPathPrefix() {
    return Optional.empty();
  }

}
