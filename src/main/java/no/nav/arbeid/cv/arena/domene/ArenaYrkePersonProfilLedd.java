package no.nav.arbeid.cv.arena.domene;

public class ArenaYrkePersonProfilLedd {

  private String styrkKode;
  private String styrkBeskrivelse;
  private boolean primaertJobbonske;

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

