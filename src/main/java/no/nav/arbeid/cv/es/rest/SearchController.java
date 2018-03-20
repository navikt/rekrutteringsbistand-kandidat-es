package no.nav.arbeid.cv.es.rest;

import java.io.IOException;
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
import no.nav.arbeid.cv.es.domene.EsCv;

@RestController
@RequestMapping(path = "/rest/kandidatsok", produces = MediaType.APPLICATION_JSON_VALUE)
public class SearchController {

  @Autowired
  private EsCvClient client;

  @RequestMapping(path = "typeahead", method = RequestMethod.GET)
  public HttpEntity<Resources<StringResource>> typeAheadKompetanse(
      @RequestParam(name = "komp", required = true) String komp) throws IOException {

    List<String> list = client.typeAheadKompetanse(komp);
    List<StringResource> resourceList =
        list.stream().map(str -> new StringResource(str)).collect(Collectors.toList());
    Resources<StringResource> resources = new Resources<>(resourceList);
    return new ResponseEntity<>(resources, HttpStatus.OK);
  }

  @RequestMapping(path = "sok", method = RequestMethod.GET)
  public HttpEntity<Resources<EsCvResource>> hentCverMedArbeidserfaring(
      @RequestParam(name = "yrkeserfaring", required = false) String yrkeserfaring,
      @RequestParam(name = "kompetanse", required = false) String kompetanse) throws IOException {

    List<EsCv> list = client.findByStillingstittelAndKompetanse(yrkeserfaring, kompetanse);
    List<EsCvResource> resourceList =
        list.stream().map(cv -> new EsCvResource(cv)).collect(Collectors.toList());
    Resources<EsCvResource> resources = new Resources<>(resourceList);
    // linkTo(methodOn(SearchController.class).listPersonsUtdanninger()).withSelfRel());
    return new ResponseEntity<>(resources, HttpStatus.OK);
  }
}
