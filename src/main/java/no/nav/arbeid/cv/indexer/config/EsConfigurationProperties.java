package no.nav.arbeid.cv.indexer.config;

public class EsConfigurationProperties implements EsProperties {

  private String user;
  private String password;

  private String trustStoreFilename;
  private String trustStorePass;

  private String keyStoreFilename;
  private String keyStorePass;

  private String hostname;
  private int port = 443;
  private String scheme = "https";

  @Override
  public String getUser() {
    return user;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getTrustStoreFilename() {
    return trustStoreFilename;
  }

  @Override
  public String getTrustStorePass() {
    return trustStorePass;
  }

  @Override
  public String getKeyStoreFilename() {
    return keyStoreFilename;
  }

  @Override
  public String getKeyStorePass() {
    return keyStorePass;
  }

  @Override
  public String getHostname() {
    return hostname;
  }

  @Override
  public int getPort() {
    return port;
  }

  @Override
  public String getScheme() {
    return scheme;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setTrustStoreFilename(String trustStoreFilename) {
    this.trustStoreFilename = trustStoreFilename;
  }

  public void setTrustStorePass(String trustStorePass) {
    this.trustStorePass = trustStorePass;
  }

  public void setHostname(String hostname) {
    this.hostname = hostname;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public void setScheme(String scheme) {
    this.scheme = scheme;
  }


}
