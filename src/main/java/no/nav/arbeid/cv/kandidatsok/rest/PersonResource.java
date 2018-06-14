package no.nav.arbeid.cv.kandidatsok.rest;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import no.nav.arbeid.cv.kandidatsok.domene.hent.Person;

public class PersonResource extends Resource<Person> {

  public PersonResource(Person content, Iterable<Link> links) {
    super(content, links);
  }

  public PersonResource(Person content, Link... links) {
    super(content, links);
  }

}

