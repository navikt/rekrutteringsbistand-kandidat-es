package no.nav.arbeid.cv.arena.domene;

import java.time.LocalDate;

public class ArenaSertifikatCvLedd {

  private String utsteder;
  private String sertifikatKode;
  private String sertifikatKodeNavn;
  private String alternativtNavn;
  private LocalDate fraDato;
  private LocalDate tilDato;

  public ArenaSertifikatCvLedd() {}

  public String getUtsteder() {
    return utsteder;
  }

  public void setUtsteder(String utsteder) {
    this.utsteder = utsteder;
  }

  public String getSertifikatKode() {
    return sertifikatKode;
  }

  public void setSertifikatKode(String sertifikatKode) {
    this.sertifikatKode = sertifikatKode;
  }

  public String getSertifikatKodeNavn() {
    return sertifikatKodeNavn;
  }

  public void setSertifikatKodeNavn(String sertifikatKodeNavn) {
    this.sertifikatKodeNavn = sertifikatKodeNavn;
  }

  public String getAlternativtNavn() {
    return alternativtNavn;
  }

  public void setAlternativtNavn(String alternativtNavn) {
    this.alternativtNavn = alternativtNavn;
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
