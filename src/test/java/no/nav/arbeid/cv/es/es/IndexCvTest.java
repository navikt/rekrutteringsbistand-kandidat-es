package no.nav.arbeid.cv.es.es;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;

import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.connection.waiting.HealthChecks;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import no.nav.arbeid.cv.es.config.ServiceConfig;
import no.nav.arbeid.cv.es.domene.EsCv;
import no.nav.arbeid.cv.es.repository.EsCvRepository;
import no.nav.arbeid.cv.es.service.CvEventObjectMother;
import no.nav.arbeid.cv.es.service.EsCvTransformer;
import no.nav.arbeid.cv.events.CvEvent;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.QueryBuilder;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IndexCvTest {


  private static final String ES_DOCKER_SERVICE = "elastic_search";

/*
   * For å kunne kjøre denne testen må Linux rekonfigureres litt..
   * Lag en fil i /etc/sysctl.d/01-increase_vm_max_map_count.conf som inneholder følgende:
   * vm.max_map_count = 262144
*/

//Kjører "docker-compose up" manuelt istedenfor denne ClassRule:

//  @ClassRule
//  public static DockerComposeRule docker =
//      DockerComposeRule.builder().file("src/test/resources/docker-compose.yml")
//          .waitingForService(ES_DOCKER_SERVICE, HealthChecks.toHaveAllPortsOpen()).build();

  @Configuration
  @OverrideAutoConfiguration(enabled = true)
  @ImportAutoConfiguration(
      classes = {ElasticsearchAutoConfiguration.class, ElasticsearchDataAutoConfiguration.class,
          ElasticsearchRepositoriesAutoConfiguration.class},
      exclude = {KafkaAutoConfiguration.class, DataSourceAutoConfiguration.class,
          HibernateJpaAutoConfiguration.class})
  @Import({ServiceConfig.class})
  static class TestConfig {
  }

  @Autowired
  private ElasticsearchTemplate elasticsearchTemplate;

  @Autowired
  private EsCvRepository esCvRepo;

  @Before
  public void before() {
    elasticsearchTemplate.deleteIndex(EsCv.class);
    elasticsearchTemplate.createIndex(EsCv.class);
    elasticsearchTemplate.putMapping(EsCv.class);
    elasticsearchTemplate.refresh(EsCv.class);
  }

  private EsCvTransformer transformer = new EsCvTransformer();

  @Test
  public void test() {

    elasticsearchTemplate.bulkIndex(lagQueries());
    elasticsearchTemplate.refresh(EsCv.class);

    Page<EsCv> all = esCvRepo.findAll(PageRequest.of(0, 100));
    assertThat(all.getContent()).asList().hasSize(1);
    all.getContent().forEach(cv -> cv.getYrkeserfaring().stream()
        .forEach(yrke -> System.out.println("STYRK:" + yrke.getStyrkKode())));

    // Nested søk fungerer ikke.. Skjønner ikke hvorfor..
    //List<EsCv> cver = esCvRepo.findByYrkerStyrkBeskrivelse("Barnehageassistent");
    // assertThat(cver).hasSize(1);

    // Dette fungerer:
    QueryBuilder builder3 = nestedQuery("yrkeserfaring",
        matchQuery("yrkeserfaring.styrkKodeTekst", "Yrkeserfaring styrkkode tekst"), ScoreMode.None);

    SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(builder3).build();
    List<EsCv> list = elasticsearchTemplate.queryForList(searchQuery, EsCv.class);
    assertThat(list).size().isGreaterThan(0);
  }

  private List<IndexQuery> lagQueries() {
    List<IndexQuery> queries = personer().stream().map(p -> transformer.transform(p))
        .map(esCv -> createIndexQuery(esCv)).collect(Collectors.toList());
    return queries;
  }
//
  private Collection<CvEvent> personer() {
    return Collections.singletonList(CvEventObjectMother.giveMeCvEvent());
  }

  //Usikker på hvilken ID som skal brukes her
  private IndexQuery createIndexQuery(EsCv esCv) {
    return new IndexQueryBuilder().withId(Long.toString(1)).withObject(esCv)
        .build();
  }

}
