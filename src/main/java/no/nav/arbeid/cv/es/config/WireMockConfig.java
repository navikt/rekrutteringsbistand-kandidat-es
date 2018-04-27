package no.nav.arbeid.cv.es.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static no.nav.arbeid.cv.es.config.AltinnEnvConf.*;

@Profile({"dev", "test"})
@Configuration
public class WireMockConfig {

    public static final String authorized = "12345678910";
    private AltinnEnvConf altinnEnvConf;

    @Autowired
    WireMockConfig(AltinnEnvConf altinnEnvConf) {
        this.altinnEnvConf = altinnEnvConf;
    }

    @Bean
    public WireMockServer wireMockServer() {
        WireMockServer wireMockServer = new WireMockServer(8089);

        wireMockServer.stubFor(get(urlPathEqualTo("/api/serviceowner/reportees"))
                //.withQueryParam(FORCEEIAUTHENTICATION, equalTo(""))
                .withQueryParam(SUBJECT, equalTo(authorized))
                //.withQueryParam(ROLEDEFINITION_ID, equalTo("3,11, "))
                .withHeader(APIKEY, equalTo(altinnEnvConf.getApiKey()))
                .withHeader(X_NAV_APIKEY, equalTo(altinnEnvConf.getGatewayKey()))
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/hal+json")
                        .withBody("[\n" +
                                "  {\n" +
                                "    \"name\": \"AUSTBØ OG KNAPSTAD\",\n" +
                                "    \"type\": \"Business\",\n" +
                                "    \"organizationNumber\": \"811057312\",\n" +
                                "    \"status\": \"Active\"\n" +
                                "  },\n" +
                                "  {\n" +
                                "    \"name\": \"AISHA WASEEM KHAN\",\n" +
                                "    \"type\": \"Person\",\n" +
                                "    \"socialSecurityNumber\": \"" + authorized + "\"\n" +
                                "  },\n" +
                                "  {\n" +
                                "    \"name\": \"STOLMEN OG BRENNÅSEN\",\n" +
                                "    \"type\": \"Business\",\n" +
                                "    \"organizationNumber\": \"010005434\",\n" +
                                "    \"status\": \"Active\"\n" +
                                "  }\n" +
                                "]")));

        wireMockServer.start();
        return wireMockServer;
    }

}
