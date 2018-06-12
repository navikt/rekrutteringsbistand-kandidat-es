package no.nav.arbeid.cv.kandidatsok.config;

import static java.util.Arrays.asList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import no.nav.arbeid.cv.kandidatsok.config.components.CorsInterceptor;

@Configuration
public class ApiConfiguration implements WebMvcConfigurer {

  @Autowired
  private CorsInterceptor corsInterceptor;

  @Bean
  public RestTemplate restTemplate(ClientHttpRequestInterceptor... interceptors) {
    RestTemplate template = new RestTemplate();
    template.setInterceptors(asList(interceptors));
    return template;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(corsInterceptor);
  }
}
