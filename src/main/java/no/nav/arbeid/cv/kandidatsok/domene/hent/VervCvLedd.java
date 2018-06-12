package no.nav.arbeid.cv.kandidatsok.domene.hent;

import java.time.LocalDate;

public class VervCvLedd {

  private String organisasjon;
  private String tittel;
  private LocalDate fraDato;
  private LocalDate tilDato;

  public VervCvLedd() {}

  public String getOrganisasjon() {
    return organisasjon;
  }

  public void setOrganisasjon(String organisasjon) {
    this.organisasjon = organisasjon;
  }

  public String getTittel() {
    return tittel;
  }

  public void setTittel(String tittel) {
    this.tittel = tittel;
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