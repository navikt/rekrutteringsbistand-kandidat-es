package no.nav.arbeid.cv.indexer.arena.domene;

import java.time.LocalDate;

public class ArenaYrkeCvLedd {

  private String arbeidsgiver;
  private String alternativStillingstittel;
  private String styrkKode;
  private String styrkKodeStillingstittel;
  private boolean utelukketForFremtiden;
  private LocalDate fraDato;
  private LocalDate tilDato;

  public ArenaYrkeCvLedd() {}

  public String getArbeidsgiver() {
    return arbeidsgiver;
  }

  public void setArbeidsgiver(String arbeidsgiver) {
    this.arbeidsgiver = arbeidsgiver;
  }

  public String getAlternativStillingstittel() {
    return alternativStillingstittel;
  }

  public void setAlternativStillingstittel(String alternativStillingstittel) {
    this.alternativStillingstittel = alternativStillingstittel;
  }

  public String getStyrkKode() {
    return styrkKode;
  }

  public void setStyrkKode(String styrkKode) {
    this.styrkKode = styrkKode;
  }

  public String getStyrkKodeStillingstittel() {
    return styrkKodeStillingstittel;
  }

  public void setStyrkKodeStillingstittel(String styrkKodeStillingstittel) {
    this.styrkKodeStillingstittel = styrkKodeStillingstittel;
  }

  public boolean isUtelukketForFremtiden() {
    return utelukketForFremtiden;
  }

  public void setUtelukketForFremtiden(boolean utelukketForFremtiden) {
    this.utelukketForFremtiden = utelukketForFremtiden;
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
