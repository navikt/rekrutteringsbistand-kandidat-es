package no.nav.arbeid.cv.arena.domene;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ANSF")
public class ArenaAnsfPersonProfilLedd extends ArenaPersonProfilLedd {

  public String getAnsettelsesforholdKode() {
    return getStrukturkode();
  }

  public String getAnsettelsesforholdKodeTekst() {
    return getElementord();
  }

}
