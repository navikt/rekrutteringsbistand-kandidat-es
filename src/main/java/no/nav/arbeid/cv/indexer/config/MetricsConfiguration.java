package no.nav.arbeid.cv.indexer.config;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.hotspot.DefaultExports;

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
