package no.nav.arbeid.cv.arena.domene;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ARBO")
public class ArenaArboPersonProfilLedd extends ArenaPersonProfilLedd {

  public String getArbeidstidsordningKode() {
    return getStrukturkode();
  }

  public String getArbeidstidsordningKodeTekst() {
    return getElementord();
  }
}
