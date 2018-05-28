package no.nav.arbeid.cv.arena.domene;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.StringUtils;

@Entity
@DiscriminatorValue("JOBBØ")
public class ArenaYrkePersonProfilLedd extends ArenaPersonProfilLedd {

  public String getStyrkKode() {
    return StringUtils.removeStart(StringUtils.defaultString(getStrukturkode()), "T");
  }

  public String getStyrkBeskrivelse() {
    return getElementord();
  }

  public boolean isPrimaertJobbonske() {
    return getLeddkodeErKilde().equals("PRIMØ");
  }
}

