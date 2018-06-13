package no.nav.arbeid.cv.kandidatsok.rest;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class StringResource extends Resource<String> {

  public StringResource(String content, Iterable<Link> links) {
    super(content, links);
  }

  public StringResource(String content, Link... links) {
    super(content, links);
  }

}
