package no.nav.arbeid.cv.kandidatsok.config;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static no.nav.arbeid.cv.kandidatsok.config.components.AltinnEnvConf.APIKEY;
import static no.nav.arbeid.cv.kandidatsok.config.components.AltinnEnvConf.FORCEEIAUTHENTICATION;
import static no.nav.arbeid.cv.kandidatsok.config.components.AltinnEnvConf.SUBJECT;
import static no.nav.arbeid.cv.kandidatsok.config.components.AltinnEnvConf.X_NAV_APIKEY;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.github.tomakehurst.wiremock.WireMockServer;

import no.nav.arbeid.cv.kandidatsok.config.components.AltinnEnvConf;

@Profile({"dev", "test"})
@Configuration
public class WireMockConfig {

  public static final String authorized = "12345678910";
  public static final String notAuthorized = "12121212121";
  private AltinnEnvConf altinnEnvConf;

  @Autowired
  WireMockConfig(AltinnEnvConf altinnEnvConf) {
    this.altinnEnvConf = altinnEnvConf;
  }

  @Bean
  public WireMockServer wireMockServer() {
    WireMockServer wireMockServer = new WireMockServer(8089);

    wireMockServer.stubFor(get(urlPathEqualTo("/api/serviceowner/reportees"))
        // .withQueryParam(FORCEEIAUTHENTICATION, equalTo(""))
        .withQueryParam(SUBJECT, equalTo(authorized))
        // .withQueryParam(ROLEDEFINITION_ID, equalTo("3,11, "))
        .withHeader(APIKEY, equalTo(altinnEnvConf.getApiKey()))
        .withHeader(X_NAV_APIKEY, equalTo(altinnEnvConf.getGatewayKey()))
        .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "application/hal+json")
            .withBody("[\n" + "  {\n" + "    \"Name\": \"AUSTBØ OG KNAPSTAD\",\n"
                + "    \"Type\": \"Business\",\n" + "    \"OrganizationNumber\": \"811057312\",\n"
                + "    \"Status\": \"Active\"\n" + "  },\n" + "  {\n"
                + "    \"Name\": \"AISHA WASEEM KHAN\",\n" + "    \"Type\": \"Person\",\n"
                + "    \"SocialSecurityNumber\": \"" + authorized + "\"\n" + "  },\n" + "  {\n"
                + "    \"Name\": \"STOLMEN OG BRENNÅSEN\",\n" + "    \"Type\": \"Business\",\n"
                + "    \"OrganizationNumber\": \"010005434\",\n" + "    \"Status\": \"Active\"\n"
                + "  }\n" + "]")));

    wireMockServer.stubFor(get(urlPathEqualTo("/api/serviceowner/reportees"))
        .withQueryParam(FORCEEIAUTHENTICATION, equalTo(""))
        .withQueryParam(SUBJECT, equalTo(notAuthorized))
        .withHeader(APIKEY, equalTo(altinnEnvConf.getApiKey()))
        .withHeader(X_NAV_APIKEY, equalTo(altinnEnvConf.getGatewayKey())).willReturn(aResponse()
            .withStatus(200).withHeader("Content-Type", "application/hal+json").withBody("[]")));

    wireMockServer.start();
    return wireMockServer;
  }

}
