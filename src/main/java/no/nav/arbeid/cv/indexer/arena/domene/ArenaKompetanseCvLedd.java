package no.nav.arbeid.cv.indexer.arena.domene;

import java.time.LocalDate;

public class ArenaKompetanseCvLedd {

  private String kompetanseKode;
  private String kompetanseKodeTekst;
  private String alternativTekst;
  private String beskrivelse;
  private LocalDate fraDato;
  private LocalDate tilDato;

  public ArenaKompetanseCvLedd() {}

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

  public LocalDate getTilDato() {
    return tilDato;
  }

  public void setTilDato(LocalDate tilDato) {
    this.tilDato = tilDato;
  }
}
