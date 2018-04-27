package no.nav.arbeid.cv.es.service;

import no.nav.arbeid.cv.es.altinn.AccessRightsEnum;
import no.nav.arbeid.cv.es.altinn.AltinnGateway;
import no.nav.arbeid.cv.es.altinn.Reportee;
import no.nav.arbeid.cv.es.altinn.RoleEnum;
import no.nav.arbeid.cv.es.domene.Arbeidsgiver;
import no.nav.arbeid.cv.es.util.FnrExtractor;
import no.nav.security.oidc.context.OIDCRequestContextHolder;
import org.apache.commons.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArbeidsgiverService {

    private final AltinnGateway altinnService;
    private final OIDCRequestContextHolder contextHolder;

    private final static Logger LOG = LoggerFactory.getLogger(ArbeidsgiverService.class);

    @Autowired
    public ArbeidsgiverService(AltinnGateway altinnService, OIDCRequestContextHolder oidcRequestContextHolder) {
        this.altinnService = altinnService;
        this.contextHolder = oidcRequestContextHolder;
    }

    public boolean innloggaBrukerHarArbeidsgiverrettighetIAltinn() {
        List<Arbeidsgiver> arbeidsgiverList = getArbeidsgiverListForReportee();

        if (arbeidsgiverList.isEmpty()) {
            LOG.warn("User doesn't seem to have the required roles for any Organization");
            return false;
        }

        return true;
    }

    private List<Arbeidsgiver> retrieveArbeidsgiverList(String subject) {
        List<Reportee> reportees = altinnService.retrieveReporteesWithRoles(subject, altinnRolesWithAccess());

        if (reportees.isEmpty()) {
            reportees = altinnService.retrieveReporteesWithRights(subject, AccessRightsEnum.REKRUTTERING);
        }

        return reportees.stream().map(this::map).collect(Collectors.toList());
    }

    private List<RoleEnum> altinnRolesWithAccess() {
        return Arrays.asList(RoleEnum.LONN_PERSONAL, RoleEnum.UTFYLLER_INSENDER);
    }

    private Arbeidsgiver map(Reportee r) {
        Arbeidsgiver arb = new Arbeidsgiver();
        arb.setNavn(WordUtils.capitalizeFully(r.getName()));
        arb.setType(r.getType());
        arb.setOrgNr(r.getOrganizationNumber());
        return arb;
    }

    private List<Arbeidsgiver> getArbeidsgiverListForReportee() {
        String fnrFromClaims = FnrExtractor.extract(contextHolder);

        return retrieveArbeidsgiverList(fnrFromClaims);
    }

}
