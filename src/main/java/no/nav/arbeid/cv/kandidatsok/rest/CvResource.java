package no.nav.arbeid.cv.kandidatsok.rest;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import no.nav.arbeid.cv.kandidatsok.domene.es.EsCv;

public class CvResource extends Resource<EsCv> {

  public CvResource(EsCv content, Iterable<Link> links) {
    super(content, links);
  }

  public CvResource(EsCv content, Link... links) {
    super(content, links);
  }

}

