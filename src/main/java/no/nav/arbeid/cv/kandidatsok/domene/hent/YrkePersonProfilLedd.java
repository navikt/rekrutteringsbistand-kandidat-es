package no.nav.arbeid.cv.kandidatsok.domene.hent;

public class YrkePersonProfilLedd {

  private String styrkKode;
  private String styrkBeskrivelse;
  private boolean primaertJobbonske;

  public YrkePersonProfilLedd() {}

  public YrkePersonProfilLedd(String styrkKode, String styrkBeskrivelse,
      boolean primaertJobbonske) {
    super();
    this.styrkKode = styrkKode;
    this.styrkBeskrivelse = styrkBeskrivelse;
    this.primaertJobbonske = primaertJobbonske;
  }

  public String getStyrkKode() {
    return styrkKode;
  }

  public void setStyrkKode(String styrkKode) {
    this.styrkKode = styrkKode;
  }

  public String getStyrkBeskrivelse() {
    return styrkBeskrivelse;
  }

  public void setStyrkBeskrivelse(String styrkBeskrivelse) {
    this.styrkBeskrivelse = styrkBeskrivelse;
  }

  public boolean isPrimaertJobbonske() {
    return primaertJobbonske;
  }

  public void setPrimaertJobbonske(boolean primaertJobbonske) {
    this.primaertJobbonske = primaertJobbonske;
  }
}

