package no.nav.arbeid.cv.indexer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.micrometer.core.instrument.MeterRegistry;

@Configuration
public class HeleEsServiceConfig {

  private EsProperties props;
  private ObjectMapper objectMapper;
  private MeterRegistry meterRegistry;

  public HeleEsServiceConfig(EsProperties props, ObjectMapper objectMapper,
      MeterRegistry meterRegistry) {
    this.props = props;
    this.objectMapper = objectMapper;
    this.meterRegistry = meterRegistry;
  }

  @Bean
  public EsClientConfig esClientConfig() {
    return new EsClientConfig(props);
  }

  @Bean
  public EsServiceConfig esServiceConfig() {
    return new EsServiceConfig(esClientConfig().restHighLevelClient(), objectMapper, meterRegistry);
  }
}
