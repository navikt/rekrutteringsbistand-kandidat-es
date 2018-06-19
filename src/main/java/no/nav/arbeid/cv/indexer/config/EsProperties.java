package no.nav.arbeid.cv.indexer.config;

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

}
