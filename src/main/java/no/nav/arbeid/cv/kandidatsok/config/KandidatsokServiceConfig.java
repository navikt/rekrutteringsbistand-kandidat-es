package no.nav.arbeid.cv.kandidatsok.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.arbeid.kandidatsok.es.client.EsSokClient;
import no.nav.arbeid.kandidatsok.es.client.EsSokHttpClient;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Configuration
public class KandidatsokServiceConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(KandidatsokServiceConfig.class);

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private EsKandidatsokConfigurationProperties props;

  @Bean
  public EsSokClient sokCvClient() {
    return new EsSokHttpClient(restHighLevelClient(), objectMapper);
  }

  @Bean
  public RestHighLevelClient restHighLevelClient() throws RuntimeException {
    try {
      final SSLContext sslContext = createSslContext();

      if (props.getScheme().equalsIgnoreCase("HTTPS")) {
        RestClientBuilder builder = RestClient
            .builder(new HttpHost(props.getHostname(), props.getPort(), props.getScheme()))
            .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
              @Override
              public HttpAsyncClientBuilder customizeHttpClient(
                  HttpAsyncClientBuilder httpClientBuilder) {

                final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(props.getUser(), props.getPassword()));

                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                return httpClientBuilder.setSSLContext(sslContext);
              }
            });
        return new RestHighLevelClient(builder);

      } else {
        RestClientBuilder builder = RestClient
            .builder(new HttpHost(props.getHostname(), props.getPort(), props.getScheme()));
        return new RestHighLevelClient(builder);

      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private SSLContext createSslContext() throws NoSuchAlgorithmException, KeyStoreException,
      KeyManagementException, IOException, CertificateException {
    if (true) {
      X509TrustManager trustAllX509Manager = mockX509TrustManager();
      SSLContext sslContext = getSslContext(trustAllX509Manager);
      return sslContext;
    }
    if (props.getTrustStoreFilename() != null) {

      // Path keyStorePath = Paths.get(props.getKeyStoreFilename());
      // KeyStore keystore = KeyStore.getInstance("jks");
      // try (InputStream is = Files.newInputStream(keyStorePath)) {
      // keystore.load(is, props.getKeyStorePass().toCharArray());
      // }

      Path trustStorePath = Paths.get(props.getTrustStoreFilename());
      KeyStore truststore = KeyStore.getInstance("jks");
      try (InputStream is = Files.newInputStream(trustStorePath)) {
        truststore.load(is, props.getTrustStorePass().toCharArray());
      }

      SSLContextBuilder sslBuilder = SSLContexts.custom();
      sslBuilder.loadTrustMaterial(truststore, null);
      // sslBuilder.loadKeyMaterial(keystore, props.getKeyStorePass().toCharArray());
      return sslBuilder.build();
    } else {
      return SSLContexts.createDefault();
    }
  }

  private SSLContext getSslContext(X509TrustManager trustAllX509Manager)
      throws NoSuchAlgorithmException, KeyManagementException {
    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, new TrustManager[] {trustAllX509Manager}, new SecureRandom());
    return sc;
  }

  private X509TrustManager mockX509TrustManager() {
    return new X509TrustManager() {
      @Override
      public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {}

      @Override
      public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {}

      @Override
      public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
      }
    };
  }

}

