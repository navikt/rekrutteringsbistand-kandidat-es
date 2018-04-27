package no.nav.arbeid.cv.es.rest;

import no.nav.arbeid.cv.es.rest.dto.Arbeidsgiver;
import no.nav.arbeid.cv.es.service.ArbeidsgiverService;
import no.nav.arbeid.cv.es.util.FnrExtractor;
import no.nav.security.oidc.filter.OIDCRequestContextHolder;
import no.nav.security.spring.oidc.validation.api.ProtectedWithClaims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "rest/arbeidsgiver")
@ProtectedWithClaims(issuer = "selvbetjening")
public class ArbeidsgiverController {

    private final static Logger LOG = LoggerFactory.getLogger(ArbeidsgiverController.class);

    private final ArbeidsgiverService arbeidsgiverService;
    private final OIDCRequestContextHolder contextHolder;

    @Autowired
    public ArbeidsgiverController(ArbeidsgiverService arbeidsgiverService, OIDCRequestContextHolder oidcRequestContextHolder) {
        this.arbeidsgiverService = arbeidsgiverService;
        this.contextHolder = oidcRequestContextHolder;
    }

    @GetMapping(path = "/rettighet")
    public ResponseEntity harRettighetIAltinn() {
        List<Arbeidsgiver> arbeidsgiverList = getArbeidsgiverListForReportee();

        if (arbeidsgiverList.isEmpty()) {
            LOG.warn("User doesn't seem to have the required roles for any Organization");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        }

        return new ResponseEntity(HttpStatus.OK);

    }

    private List<Arbeidsgiver> getArbeidsgiverListForReportee() {
        String fnrFromClaims = FnrExtractor.extract(contextHolder);

        return arbeidsgiverService.retrieveArbeidsgiverList(fnrFromClaims);
    }

}
