package no.nav.arbeid.cv.es.rest;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import no.nav.arbeid.cv.es.domene.Sokeresultat;

public class SokeresultatResource extends Resource<Sokeresultat> {

  public SokeresultatResource(Sokeresultat content, Iterable<Link> links) {
    super(content, links);
  }

  public SokeresultatResource(Sokeresultat content, Link... links) {
    super(content, links);
  }

}

