package no.nav.arbeid.cv.arena.domene;

import java.time.LocalDate;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.StringUtils;

@Entity
@DiscriminatorValue("KURS")
public class ArenaKursCvLedd extends ArenaCvLedd {

  public ArenaKursCvLedd(no.nav.arbeid.cv.arena.domene.ArenaCvLedd.ArenaCvLeddPK arenaCvLeddPK,
      ArenaPerson person, int cvleddnr, String leddtekst1, String leddtekst2, LocalDate fraDato,
      LocalDate tilDato, String beskrivelse, Integer omfangVerdi, String omfangMaaleenhet,
      String strukturkode, String elementord, String elementklassekode, String nivaa, String hensyn,
      String aktiv) {
    super(arenaCvLeddPK, person, cvleddnr, leddtekst1, leddtekst2, fraDato, tilDato, beskrivelse,
        omfangVerdi, omfangMaaleenhet, strukturkode, elementord, elementklassekode, nivaa, hensyn,
        aktiv);
  }

  public ArenaKursCvLedd() {}

  public static class Omfang {

    private Integer verdi;
    private String enhet;

    public Omfang(Integer verdi, String enhet) {
      this.verdi = verdi;
      this.enhet = enhet;
    }

    public Integer getVerdi() {
      return verdi;
    }

    public String getEnhet() {
      return enhet;
    }
  }
  
  public String getArrangor() {
    return StringUtils.defaultString(getLeddtekst1());
  }

  public String getTittel() {
    return StringUtils.defaultString(getLeddtekst2());
  }

  public Omfang getOmfang() {
    return new Omfang(omfangVerdi, omfangMaaleenhet);
  }
  
  public String getBeskrivelse() {
    return getOpprinneligBeskrivelse();
  }
}
