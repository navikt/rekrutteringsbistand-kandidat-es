package no.nav.arbeid.cv.es.service;

import no.nav.arbeid.cv.es.altinn.AccessRightsEnum;
import no.nav.arbeid.cv.es.altinn.AltinnGateway;
import no.nav.arbeid.cv.es.altinn.Reportee;
import no.nav.arbeid.cv.es.altinn.RoleEnum;
import no.nav.arbeid.cv.es.rest.dto.Arbeidsgiver;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArbeidsgiverService {

    private final AltinnGateway altinnService;

    @Autowired
    public ArbeidsgiverService(AltinnGateway altinnService) {
        this.altinnService = altinnService;
    }

    public List<Arbeidsgiver> retrieveArbeidsgiverList(String subject) {
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

}
