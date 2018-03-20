package no.nav.arbeid.cv.es.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "es")
public class EsConfigurationProperties {

  private String user;
  private String password;
  
  private String trustStoreFilename;
  private String trustStorePass;

  private String keyStoreFilename;
  private String keyStorePass;
  
  private String hostname;
  private int port = 443;
  private String scheme = "https";

  public String getUser() {
    return user;
  }

  public String getPassword() {
    return password;
  }

  public String getTrustStoreFilename() {
    return trustStoreFilename;
  }

  public String getTrustStorePass() {
    return trustStorePass;
  }

  public String getKeyStoreFilename() {
    return keyStoreFilename;
  }

  public String getKeyStorePass() {
    return keyStorePass;
  }

  public String getHostname() {
    return hostname;
  }

  public int getPort() {
    return port;
  }

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
