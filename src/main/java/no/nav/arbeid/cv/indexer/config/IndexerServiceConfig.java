package no.nav.arbeid.cv.indexer.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.commons.io.IOUtils;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.arbeid.cv.indexer.arena.domene.ArenaPerson;
import no.nav.arbeid.cv.indexer.arena.mapper.ArenaPersonMapper;
import no.nav.arbeid.cv.indexer.config.temp.TempCvEventObjectMother;
import no.nav.arbeid.cv.indexer.domene.EsCv;
import no.nav.arbeid.cv.indexer.es.client.EsIndexerClient;
import no.nav.arbeid.cv.indexer.es.client.EsIndexerHttpClient;
import no.nav.arbeid.cv.indexer.service.CvEventListener;
import no.nav.arbeid.cv.indexer.service.CvIndexerService;
import no.nav.arbeid.cv.indexer.service.DefaultCvIndexerService;
import no.nav.arbeid.cv.indexer.service.EsCvTransformer;

@Configuration
@EnableAsync
public class IndexerServiceConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(IndexerServiceConfig.class);

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private EsIndexerConfigurationProperties props;

  @Autowired
  private MeterRegistry meterRegistry;

  @Autowired
  private @Value("${es.legg.til.testdata}") boolean leggTilTestdata;

  @Autowired
  private CvIndexerService cvIndexerService;

  @Autowired
  private EsIndexerClient esCvClient;

  @Bean
  public EsIndexerClient esCvClient() {
    return new EsIndexerHttpClient(restHighLevelClient(), objectMapper);
  }

  @Bean
  public EsCvTransformer esCvTransformer() {
    return new EsCvTransformer();
  }

  @Bean
  public CvIndexerService cvIndexerService() {
    return new DefaultCvIndexerService(esCvClient(), esCvTransformer(), meterRegistry);
  }

  @Bean
  @Autowired
  public CvEventListener cvEventListener(CvIndexerService cvIndexerService,
      RetryTemplate retryTemplate, MeterRegistry meterRegistry) {
    return new CvEventListener(cvIndexerService, retryTemplate, meterRegistry);
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

  @Bean
  public TestDataLaster testDataLaster() {
    return new TestDataLaster(objectMapper, esCvClient());
  }

  @PostConstruct
  public void initES() throws IOException {
    try {
      // esCvClient.deleteIndex();
      if (!esCvClient.doesIndexExist()) {
        esCvClient.createIndex();
      }
    } catch (Exception e) {
      LOGGER.error("Feilet under initialisering av Elasticsearch", e);
    }

    if (leggTilTestdata) {
      testDataLaster().last();
      List<EsCv> statiskePersoner =
          Arrays.asList(esCvTransformer().transform(TempCvEventObjectMother.giveMeCvEvent()),
              esCvTransformer().transform(TempCvEventObjectMother.giveMeCvEvent2()),
              esCvTransformer().transform(TempCvEventObjectMother.giveMeCvEvent3()),
              esCvTransformer().transform(TempCvEventObjectMother.giveMeCvEvent4()),
              esCvTransformer().transform(TempCvEventObjectMother.giveMeCvEvent5()));
      esCvClient.bulkIndex(statiskePersoner);
    }

    cvIndexerService.oppdaterEsGauge();
  }

  static class TestDataLaster {
    private final ObjectMapper objectMapper;
    private final EsIndexerClient esCvClient;

    public TestDataLaster(ObjectMapper objectMapper, EsIndexerClient esCvClient) {
      this.objectMapper = objectMapper;
      this.esCvClient = esCvClient;
    }

    @Async
    public void last() throws JsonParseException, JsonMappingException, IOException {

      ClassPathResource classPathResource = new ClassPathResource("input.json");
      InputStream inputStream = classPathResource.getInputStream();

      StringWriter writer = new StringWriter();
      IOUtils.copy(inputStream, writer, "UTF-8");
      String input = writer.toString();
      List<ArenaPerson> arenapersoner =
          objectMapper.readValue(input, new TypeReference<List<ArenaPerson>>() {});

      ArenaPersonMapper mapper = new ArenaPersonMapper();
      List<EsCv> espersoner = arenapersoner.stream().map(mapper::map).collect(Collectors.toList());
      esCvClient.bulkIndex(espersoner);


    }
  }
}

