package no.nav.arbeid.cv.es.altinn;

import no.nav.arbeid.cv.es.config.AltinnEnvConf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static no.nav.arbeid.cv.es.config.AltinnEnvConf.*;


@Service
public class AltinnGateway {

    private static final Logger LOG = LoggerFactory.getLogger(AltinnGateway.class);

    private RestTemplate restTemplate;
    private AltinnEnvConf altinnEnvConf;

    @Autowired
    public AltinnGateway(RestTemplate restTemplate, AltinnEnvConf altinnEnvConf) {
        this.restTemplate = restTemplate;
        this.altinnEnvConf = altinnEnvConf;
    }

    public List<Reportee> retrieveReporteesWithRoles(String subject, List<RoleEnum> roles) {
        return retrieveActiveReportees(SUBJECT + "=" + subject + "&" + ROLEDEFINITION_ID + "=" + composeRoleParameter(roles));
    }

    // If we're adding more roles
    private String composeRoleParameter(List<RoleEnum> roleEnums) {
        StringBuilder roleParam = new StringBuilder();
        roleEnums.forEach(roleEnum -> roleParam.append(roleEnum.getRoleId()).append(","));
        return roleParam.toString();
    }

    private List<Reportee> retrieveReporteesWithRolesFallback(String subject, List<RoleEnum> roles) {
        LOG.warn("Got unexpected response from altinn gateway");
        return Collections.emptyList();
    }

    public List<Reportee> retrieveReporteesWithRights(String subject, AccessRightsEnum right) {
        return retrieveActiveReportees(SUBJECT + "=" + subject + "&" + SERVICE_CODE + "=" + right.getServiceCode() + "&" + SERVICE_EDITION + "=" + right.getServiceEditionCode());
    }

    private List<Reportee> retrieveReporteesWithRightsFallback(String subject, AccessRightsEnum right) {
        LOG.warn("Got unexpected response from altinn gateway");
        return Collections.emptyList();
    }

    private List<Reportee> retrieveActiveReportees(String query) {
        ResponseEntity<List<Reportee>> response = restAltinnRetrieve(
                "/reportees",
                query,
                new ParameterizedTypeReference<List<Reportee>>() {
                });

        if (response.getStatusCode() != HttpStatus.OK) {
            return retrieveReporteesWithRightsFallback("", AccessRightsEnum.REKRUTTERING);
        }

        return (response.getBody().stream()
                .filter(this::isActiveEnterpriseOrBusinessReportee)
                .collect(Collectors.toList()));
    }

    private boolean isActiveEnterpriseOrBusinessReportee(Reportee r) {
        return ("Business".equals(r.getType()) && "Active".equals(r.getStatus()));
    }


    private <T> ResponseEntity<List<T>> restAltinnRetrieve(String path, String query, ParameterizedTypeReference<List<T>> type) {
        StringBuilder builder = new StringBuilder();
        builder
                .append(altinnEnvConf.getAltinnUrl())
                .append(path)
                .append("?")
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
