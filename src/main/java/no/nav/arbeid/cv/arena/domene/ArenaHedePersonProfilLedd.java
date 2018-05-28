package no.nav.arbeid.cv.arena.domene;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("HEDE")
public class ArenaHedePersonProfilLedd extends ArenaPersonProfilLedd {

  public String getHeltidDeltidKode() {
    return getStrukturkode();
  }

  public String getHeltidDeltidKodeTekst() {
    return getElementord();
  }

}
