package no.nav.arbeid.cv.indexer.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.micrometer.core.instrument.MeterRegistry;
import no.nav.arbeid.kandidatsok.es.client.EsIndexerHttpService;
import no.nav.arbeid.kandidatsok.es.client.EsIndexerService;
import no.nav.arbeid.kandidatsok.es.client.EsSokHttpService;
import no.nav.arbeid.kandidatsok.es.client.EsSokService;

@Configuration
public class EsServiceConfig {

  private ObjectMapper objectMapper;
  private MeterRegistry meterRegistry;
  private RestHighLevelClient restHighLevelClient;

  public EsServiceConfig(RestHighLevelClient restHighLevelClient, ObjectMapper objectMapper,
      MeterRegistry meterRegistry) {
    this.restHighLevelClient = restHighLevelClient;
    this.objectMapper = objectMapper;
    this.meterRegistry = meterRegistry;
  }

  @Bean
  public EsSokService sokCvClient() {
    return new EsSokHttpService(restHighLevelClient, objectMapper);
  }

  @Bean
  public EsIndexerService indexerCvService() {
    return new EsIndexerHttpService(restHighLevelClient, objectMapper, meterRegistry);
  }

}

