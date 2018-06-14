package no.nav.arbeid.cv.indexer.config;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.hotspot.DefaultExports;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class MetricsConfiguration {

  @Bean
  CollectorRegistry prometheusCollector() {
    return CollectorRegistry.defaultRegistry;
  }

  @PostConstruct
  public void prometheusConfig() {
    DefaultExports.initialize();
  }

}
