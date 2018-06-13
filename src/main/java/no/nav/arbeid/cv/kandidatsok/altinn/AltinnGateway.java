package no.nav.arbeid.cv.kandidatsok.altinn;

import static no.nav.arbeid.cv.kandidatsok.config.components.AltinnEnvConf.APIKEY;
import static no.nav.arbeid.cv.kandidatsok.config.components.AltinnEnvConf.FORCEEIAUTHENTICATION;
import static no.nav.arbeid.cv.kandidatsok.config.components.AltinnEnvConf.ROLEDEFINITION_ID;
import static no.nav.arbeid.cv.kandidatsok.config.components.AltinnEnvConf.SERVICE_CODE;
import static no.nav.arbeid.cv.kandidatsok.config.components.AltinnEnvConf.SERVICE_EDITION;
import static no.nav.arbeid.cv.kandidatsok.config.components.AltinnEnvConf.SUBJECT;
import static no.nav.arbeid.cv.kandidatsok.config.components.AltinnEnvConf.X_NAV_APIKEY;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import no.nav.arbeid.cv.indexer.domene.OperationalException;
import no.nav.arbeid.cv.kandidatsok.config.CacheConfig;
import no.nav.arbeid.cv.kandidatsok.config.components.AltinnEnvConf;

public class AltinnGateway {

  private static final Logger LOG = LoggerFactory.getLogger(AltinnGateway.class);

  private RestTemplate restTemplate;
  private AltinnEnvConf altinnEnvConf;

  public AltinnGateway(RestTemplate restTemplate, AltinnEnvConf altinnEnvConf) {
    this.restTemplate = restTemplate;
    this.altinnEnvConf = altinnEnvConf;
  }

  @Cacheable(CacheConfig.ALTINN_RIGHTS_CACHE)
  public List<Reportee> retrieveReporteesWithRights(String subject, AccessRightsEnum right) {
    return retrieveActiveReportees(SUBJECT + "=" + subject + "&" + SERVICE_CODE + "="
        + right.getServiceCode() + "&" + SERVICE_EDITION + "=" + right.getServiceEditionCode());
  }

  @Cacheable(CacheConfig.ALTINN_ROLES_CACHE)
  public List<Reportee> retrieveReporteesWithRoles(String subject, List<RoleEnum> roles) {
    return retrieveActiveReportees(
        SUBJECT + "=" + subject + "&" + ROLEDEFINITION_ID + "=" + composeRoleParameter(roles));
  }

  // If we're adding more roles
  private String composeRoleParameter(List<RoleEnum> roleEnums) {
    StringBuilder roleParam = new StringBuilder();
    roleEnums.forEach(roleEnum -> roleParam.append(roleEnum.getRoleId()).append(","));
    return roleParam.toString();
  }

  private List<Reportee> retrieveReporteesWithRightsFallback(String subject,
      AccessRightsEnum right) {
    LOG.warn("Got unexpected response from altinn gateway");
    return Collections.emptyList();
  }

  private List<Reportee> retrieveActiveReportees(String query) {

    try {
      ResponseEntity<List<Reportee>> response = restAltinnRetrieve("/reportees", query,
          new ParameterizedTypeReference<List<Reportee>>() {});

      if (response.getStatusCode() != HttpStatus.OK) {
        return retrieveReporteesWithRightsFallback("", AccessRightsEnum.REKRUTTERING);
      }

      return (response.getBody().stream().filter(this::isActiveEnterpriseOrBusinessReportee)
          .collect(Collectors.toList()));
    } catch (Exception e) {
      LOG.error("Kall til Altinn feilet", e);
      throw new OperationalException(e.getMessage(), e);
    }


  }

  private boolean isActiveEnterpriseOrBusinessReportee(Reportee r) {
    return ("Business".equals(r.getType()) && "Active".equals(r.getStatus()));
  }


  private <T> ResponseEntity<List<T>> restAltinnRetrieve(String path, String query,
      ParameterizedTypeReference<List<T>> type) {
    StringBuilder builder = new StringBuilder();
    builder.append(altinnEnvConf.getAltinnUrl()).append(path).append("?")
        .append(FORCEEIAUTHENTICATION);

    if (query != null) {
      builder.append("&").append(query);
    }

    URI uri = URI.create(builder.toString());

    HttpHeaders headers = new HttpHeaders();
    headers.set(X_NAV_APIKEY, altinnEnvConf.getGatewayKey());
    headers.set(APIKEY, altinnEnvConf.getApiKey());

    LOG.debug("Calling to altinn with uri {}", uri.toString());

    HttpEntity<String> entity = new HttpEntity<>(null, headers);

    return restTemplate.exchange(uri, HttpMethod.GET, entity, type);
  }

}
