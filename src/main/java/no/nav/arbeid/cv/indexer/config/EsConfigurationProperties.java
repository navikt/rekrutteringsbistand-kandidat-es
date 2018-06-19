package no.nav.arbeid.cv.indexer.config;

// @Configuration
// @ConfigurationProperties(prefix = "es")
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

  /* (non-Javadoc)
   * @see no.nav.arbeid.cv.indexer.config.EsProperties#getUser()
   */
  @Override
  public String getUser() {
    return user;
  }

  /* (non-Javadoc)
   * @see no.nav.arbeid.cv.indexer.config.EsProperties#getPassword()
   */
  @Override
  public String getPassword() {
    return password;
  }

  /* (non-Javadoc)
   * @see no.nav.arbeid.cv.indexer.config.EsProperties#getTrustStoreFilename()
   */
  @Override
  public String getTrustStoreFilename() {
    return trustStoreFilename;
  }

  /* (non-Javadoc)
   * @see no.nav.arbeid.cv.indexer.config.EsProperties#getTrustStorePass()
   */
  @Override
  public String getTrustStorePass() {
    return trustStorePass;
  }

  /* (non-Javadoc)
   * @see no.nav.arbeid.cv.indexer.config.EsProperties#getKeyStoreFilename()
   */
  @Override
  public String getKeyStoreFilename() {
    return keyStoreFilename;
  }

  /* (non-Javadoc)
   * @see no.nav.arbeid.cv.indexer.config.EsProperties#getKeyStorePass()
   */
  @Override
  public String getKeyStorePass() {
    return keyStorePass;
  }

  /* (non-Javadoc)
   * @see no.nav.arbeid.cv.indexer.config.EsProperties#getHostname()
   */
  @Override
  public String getHostname() {
    return hostname;
  }

  /* (non-Javadoc)
   * @see no.nav.arbeid.cv.indexer.config.EsProperties#getPort()
   */
  @Override
  public int getPort() {
    return port;
  }

  /* (non-Javadoc)
   * @see no.nav.arbeid.cv.indexer.config.EsProperties#getScheme()
   */
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
