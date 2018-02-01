package no.nav.arbeid.cv.es.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import no.nav.arbeid.cv.es.events.CvEventListener;
import no.nav.arbeid.cv.es.repository.EsCvRepository;
import no.nav.arbeid.cv.es.service.CvIndexerService;
import no.nav.arbeid.cv.es.service.DefaultCvIndexerService;
import no.nav.arbeid.cv.es.service.EsCvTransformer;

@Configuration
@EnableElasticsearchRepositories(basePackages = "no.nav.arbeid.cv.es.repository")
public class ServiceConfig {

  @Autowired
  private EsCvRepository esRepo;

  @Bean
  public EsCvTransformer esCvTransformer() {
    return new EsCvTransformer();
  }

  @Bean
  public CvIndexerService cvIndexerService() {
    return new DefaultCvIndexerService(esRepo, esCvTransformer());
  }

  @Bean
  public CvEventListener cvEventListener() {
    return new CvEventListener(cvIndexerService());
  }
}
