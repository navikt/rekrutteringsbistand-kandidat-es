package no.nav.arbeid.cv.es.rest;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import no.nav.arbeid.cv.es.domene.EsCv;

public class EsCvResource extends Resource<EsCv> {

  public EsCvResource(EsCv content, Iterable<Link> links) {
    super(content, links);
  }

  public EsCvResource(EsCv content, Link... links) {
    super(content, links);
  }

}

