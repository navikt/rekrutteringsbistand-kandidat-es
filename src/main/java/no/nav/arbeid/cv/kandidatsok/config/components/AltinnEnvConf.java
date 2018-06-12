package no.nav.arbeid.cv.kandidatsok.config.components;

import org.springframework.beans.factory.annotation.Value;

public class AltinnEnvConf {

  public static final String X_NAV_APIKEY = "x-nav-apiKey";
  public static final String APIKEY = "ApiKey";
  public static final String FORCEEIAUTHENTICATION = "ForceEIAuthentication";
  public static final String SUBJECT = "subject";
  public static final String ROLEDEFINITION_ID = "roleDefinitionId";
  public static final String REPORTEE = "reportee";
  public static final String SERVICE_CODE = "serviceCode";
  public static final String SERVICE_EDITION = "serviceEdition";

  @Value("${altinn.gateway.url:http://localhost:8089/api/serviceowner}")
  private String altinnUrl;

  @Value("${altinn.apikey}")
  private String apiKey;

  @Value("${altinn.gateway.apikey}")
  private String gatewayKey;

  public String getAltinnUrl() {
    return altinnUrl;
  }

  public String getApiKey() {
    return apiKey;
  }

  public String getGatewayKey() {
    return gatewayKey;
  }

}
