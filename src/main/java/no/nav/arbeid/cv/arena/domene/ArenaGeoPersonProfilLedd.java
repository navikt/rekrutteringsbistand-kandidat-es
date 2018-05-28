package no.nav.arbeid.cv.arena.domene;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("GEO")
public class ArenaGeoPersonProfilLedd extends ArenaPersonProfilLedd {

  public String getGeografiKode() {
    return getStrukturkode();
  }

  public String getGeografiKodeTekst() {
    return getElementord();
  }

}
