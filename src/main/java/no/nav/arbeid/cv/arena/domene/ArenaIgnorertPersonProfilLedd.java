package no.nav.arbeid.cv.arena.domene;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("not null")
public class ArenaIgnorertPersonProfilLedd extends ArenaPersonProfilLedd {

  public ArenaIgnorertPersonProfilLedd() {
    super();
  }

}
