package no.nav.arbeid.cv.es.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.arbeid.cv.es.client.EsCvClient;
import no.nav.arbeid.cv.es.domene.Sokeresultat;

@RestController
@RequestMapping(path = "/rest/kandidatsok", produces = MediaType.APPLICATION_JSON_VALUE)
public class SearchController {

  @Autowired
  private EsCvClient client;

  @RequestMapping(path = "typeahead", method = RequestMethod.GET)
  public HttpEntity<Resources<StringResource>> typeAheadKompetanse(
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
  public HttpEntity<SokeresultatResource> hentCverMedArbeidserfaring(
      @RequestParam(name = "fritekst", required = false) String fritekst,
      @RequestParam(name = "yrkeserfaring", required = false) String yrkeserfaring,
      @RequestParam(name = "kompetanse", required = false) String kompetanse,
      @RequestParam(name = "nusKode", required = false) String nusKode) throws IOException {

    Sokeresultat sokeresultat = client.sok(fritekst, yrkeserfaring, kompetanse, nusKode);
    SokeresultatResource sokeresultatResource = new SokeresultatResource(sokeresultat);
    return new ResponseEntity<>(sokeresultatResource, HttpStatus.OK);
  }
}
