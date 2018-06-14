package no.nav.arbeid.cv.kandidatsok.domene.hent;

public class AnsfPersonProfilLedd {

  private String ansettelsesforholdKode;
  private String ansettelsesforholdKodeTekst;

  public AnsfPersonProfilLedd() {}

  public AnsfPersonProfilLedd(String ansettelsesforholdKode, String ansettelsesforholdKodeTekst) {
    super();
    this.ansettelsesforholdKode = ansettelsesforholdKode;
    this.ansettelsesforholdKodeTekst = ansettelsesforholdKodeTekst;
  }

  public String getAnsettelsesforholdKode() {
    return ansettelsesforholdKode;
  }

  public void setAnsettelsesforholdKode(String ansettelsesforholdKode) {
    this.ansettelsesforholdKode = ansettelsesforholdKode;
  }

  public String getAnsettelsesforholdKodeTekst() {
    return ansettelsesforholdKodeTekst;
  }

  public void setAnsettelsesforholdKodeTekst(String ansettelsesforholdKodeTekst) {
    this.ansettelsesforholdKodeTekst = ansettelsesforholdKodeTekst;
  }

}
