package no.nav.arbeid.cv.es;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;


@SpringBootApplication
//@EnableElasticsearchRepositories(basePackages = "no.nav.arbeid.cv.es.repository")
@EnableAutoConfiguration(
    exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class, KafkaAutoConfiguration.class})
public class PamCvIndexerApplication {

  public static void main(String[] args) {
    SpringApplication.run(PamCvIndexerApplication.class, args);
  }
}

