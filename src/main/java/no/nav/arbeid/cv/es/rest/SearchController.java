package no.nav.arbeid.cv.es.rest;

import no.nav.arbeid.cv.es.client.EsCvClient;
import no.nav.arbeid.cv.es.domene.Sokeresultat;
import no.nav.security.spring.oidc.validation.api.ProtectedWithClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/rest/kandidatsok", produces = MediaType.APPLICATION_JSON_VALUE)
@ProtectedWithClaims(issuer = "selvbetjening")
public class SearchController {

    @Autowired
    private EsCvClient client;

    @RequestMapping(path = "typeahead", method = RequestMethod.GET)
   @PreAuthorize("@arbeidsgiverService.innloggaBrukerHarArbeidsgiverrettighetIAltinn()")
    public HttpEntity<Resources<StringResource>> typeAhead(
            @RequestParam(name = "komp", required = false) String komp,
            @RequestParam(name = "utd", required = false) String utd,
            @RequestParam(name = "yrke", required = false) String yrke) throws IOException {

        if (komp == null && utd == null && yrke == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<String> list = new ArrayList<>();
        if (komp != null) {
            list.addAll(client.typeAheadKompetanse(komp));
        }
        if (utd != null) {
            list.addAll(client.typeAheadUtdanning(utd));
        }
        if (yrke != null) {
            list.addAll(client.typeAheadYrkeserfaring(yrke));
        }

        List<StringResource> resourceList =
                list.stream().map(str -> new StringResource(str)).collect(Collectors.toList());
        Resources<StringResource> resources = new Resources<>(resourceList);
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @RequestMapping(path = "sok", method = RequestMethod.GET)
    @PreAuthorize("@arbeidsgiverService.innloggaBrukerHarArbeidsgiverrettighetIAltinn()")
    public HttpEntity<SokeresultatResource> sok(
            @RequestParam(name = "fritekst", required = false) String fritekst,
            @RequestParam(name = "yrkeserfaring", required = false) String yrkeserfaring,
            @RequestParam(name = "kompetanse", required = false) String kompetanse,
            @RequestParam(name = "utdanning", required = false) String utdanning,
            @RequestParam(name = "styrkKode", required = false) String styrkKode,
            @RequestParam(name = "nusKode", required = false) String nusKode,
            @RequestParam(name = "styrkKoder", required = false) List<String> styrkKoder,
            @RequestParam(name = "nusKoder", required = false) List<String> nusKoder) throws IOException {

        Sokeresultat sokeresultat =
                client.sok(fritekst, yrkeserfaring, kompetanse, utdanning, styrkKode, nusKode, styrkKoder, nusKoder);
        SokeresultatResource sokeresultatResource = new SokeresultatResource(sokeresultat);
        return new ResponseEntity<>(sokeresultatResource, HttpStatus.OK);
    }
}
