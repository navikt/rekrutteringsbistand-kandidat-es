package no.nav.arbeid.cv.kandidatsok.config;

import static java.util.Arrays.asList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import no.nav.arbeid.cv.kandidatsok.config.components.CorsInterceptor;

@Configuration
public class ApiConfiguration implements WebMvcConfigurer {

  @Value("${no.nav.arbeid.api.allowed.origins:http://localhost:9009,https://pam-cv-indexer.nais.oera-q.local,https://pam-cv-indexer-q.nav.no,https://pam-cv-indexer.nav.no}")
  private String[] allowedOrigins;

  @Bean
  public CorsInterceptor corsInterceptor() {
    return new CorsInterceptor(allowedOrigins);
  }

  @Bean
  public RestTemplate restTemplate(ClientHttpRequestInterceptor... interceptors) {
    RestTemplate template = new RestTemplate();
    template.setInterceptors(asList(interceptors));
    return template;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(corsInterceptor());
  }
}
