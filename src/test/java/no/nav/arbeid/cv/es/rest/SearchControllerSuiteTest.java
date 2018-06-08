package no.nav.arbeid.cv.es.rest;


import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.Filter;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.arbeid.cv.es.client.EsCvClient;
import no.nav.arbeid.cv.es.client.EsCvHttpClient;
import no.nav.arbeid.cv.es.domene.Sokekriterier;
import no.nav.arbeid.cv.es.domene.Sokeresultat;
import no.nav.arbeid.cv.es.service.CvEventObjectMother;
import no.nav.arbeid.cv.es.service.EsCvTransformer;
import no.nav.security.oidc.filter.OIDCTokenValidationFilter;
import no.nav.security.spring.oidc.test.JwtTokenGenerator;

@RunWith(SpringRunner.class)
@WebMvcTest(SearchController.class)
@ActiveProfiles("test")
public class SearchControllerSuiteTest {

  @Autowired
  private Filter springSecurityFilterChain;

  // @ClassRule
  // public static DockerComposeRule docker =
  // DockerComposeRule.builder().file("src/test/resources/docker-compose-kun-es.yml").build();

  @TestConfiguration
  static class AdditionalConfig {

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public EsCvClient esCvClient() {
      return new EsCvHttpClient(restHighLevelClient(), objectMapper);
    }

    @Bean
    public EsCvTransformer transformer() {
      return new EsCvTransformer();
    }

    @Bean
    public RestHighLevelClient restHighLevelClient() {
      return new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9250, "http")));
    }


  }

  @Rule
  public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private EsCvTransformer transformer;

  @MockBean
  private EsCvClient client;

  @Autowired
  private OIDCTokenValidationFilter oidcTokenValidationFilter;

  @Before
  public void setUp() throws IOException {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
        .addFilter(oidcTokenValidationFilter).addFilter(springSecurityFilterChain)
        .apply(documentationConfiguration(this.restDocumentation)).build();

    // try {
    // client.deleteIndex();
    // } catch (Exception e) {
    // // Ignore
    // }
    //
    // client.createIndex();
    //
    // EsCv esCv = transformer.transform(CvEventObjectMother.giveMeCvEvent());
    // client.index(esCv);
  }

  @Test
  public void fritekstSokUtenParametreSkalReturnereAlt() throws Exception {
    when(client.sok(any(Sokekriterier.class))).thenReturn(new Sokeresultat(1,
        Collections.singletonList(transformer.transform(CvEventObjectMother.giveMeCvEvent())),
        Collections.emptyList()));
    // when(client.sok(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
    // anyList(), anyList()))
    // .thenReturn(new Sokeresultat(Collections.emptyList(), Collections.emptyList()));

    String token = JwtTokenGenerator.createSignedJWT("12345678910").serialize();

    this.mockMvc
        .perform(get("/rest/kandidatsok/sok?fritekst=").accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
        .andExpect(status().isOk()).andDo(document("fritekstsokGreenkeeper"))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.cver", hasSize(1)))
        .andExpect(jsonPath("$.cver[0].arenaPersonId", is(1)))
        .andExpect(jsonPath("$.cver[0].etternavn", is("NORDMANN")))
        .andExpect(jsonPath("$.cver[0].fornavn", is("OLA")));

    // verify(service, times(1)).hentArenaPerson("1");
    // verifyNoMoreInteractions(service);

  }

  @Test
  public void ingenTilgangUtenOidcToken() throws Exception {

    this.mockMvc
        .perform(get("/rest/kandidatsok/sok?fritekst=").accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + "etTulleToken"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void ingenTilgangUtenAltinnRolle() throws Exception {

    String token = JwtTokenGenerator.createSignedJWT("12121212121").serialize();

    this.mockMvc
        .perform(get("/rest/kandidatsok/sok?fritekst=").accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
        .andExpect(status().isForbidden());
  }

}
