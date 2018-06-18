package no.nav.arbeid.cv.kandidatsok.domene.hent;

import java.time.LocalDate;

public class KompetanseCvLedd {

  private String kompetanseKode;
  private String kompetanseKodeTekst;
  private String alternativTekst;
  private String beskrivelse;
  private LocalDate fraDato;

  public KompetanseCvLedd() {}

  public KompetanseCvLedd(String kompetanseKode, String kompetanseKodeTekst, String alternativTekst,
      String beskrivelse, LocalDate fraDato) {
    super();
    this.kompetanseKode = kompetanseKode;
    this.kompetanseKodeTekst = kompetanseKodeTekst;
    this.alternativTekst = alternativTekst;
    this.beskrivelse = beskrivelse;
    this.fraDato = fraDato;
  }

  public String getKompetanseKode() {
    return kompetanseKode;
  }

  public void setKompetanseKode(String kompetanseKode) {
    this.kompetanseKode = kompetanseKode;
  }

  public String getKompetanseKodeTekst() {
    return kompetanseKodeTekst;
  }

  public void setKompetanseKodeTekst(String kompetanseKodeTekst) {
    this.kompetanseKodeTekst = kompetanseKodeTekst;
  }

  public String getAlternativTekst() {
    return alternativTekst;
  }

  public void setAlternativTekst(String alternativTekst) {
    this.alternativTekst = alternativTekst;
  }

  public String getBeskrivelse() {
    return beskrivelse;
  }

  public void setBeskrivelse(String beskrivelse) {
    this.beskrivelse = beskrivelse;
  }

  public LocalDate getFraDato() {
    return fraDato;
  }

  public void setFraDato(LocalDate fraDato) {
    this.fraDato = fraDato;
  }

}
