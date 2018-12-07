package no.nav.arbeid.cv.indexer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({EsClientConfig.class, EsServiceConfig.class})
public class HeleEsServiceConfig {
  
}
