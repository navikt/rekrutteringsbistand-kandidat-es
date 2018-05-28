package no.nav.arbeid.cv.arena.domene;

import java.time.LocalDate;

public class ArenaKursCvLedd {

  private String arrangor;
  private String tittel;
  private String beskrivelse;
  private Omfang omfang;
  private LocalDate fraDato;
  private LocalDate tilDato;

  public ArenaKursCvLedd() {}

  public static class Omfang {

    private Integer verdi;
    private String enhet;

    public Omfang() {}

    public Integer getVerdi() {
      return verdi;
    }

    public void setVerdi(Integer verdi) {
      this.verdi = verdi;
    }

    public String getEnhet() {
      return enhet;
    }

    public void setEnhet(String enhet) {
      this.enhet = enhet;
    }
  }

  public String getArrangor() {
    return arrangor;
  }

  public void setArrangor(String arrangor) {
    this.arrangor = arrangor;
  }

  public String getTittel() {
    return tittel;
  }

  public void setTittel(String tittel) {
    this.tittel = tittel;
  }

  public Omfang getOmfang() {
    return omfang;
  }

  public void setOmfang(Omfang omfang) {
    this.omfang = omfang;
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
