package no.nav.arbeid.cv.kandidatsok.domene.hent;

public class HedePersonProfilLedd {

  private String heltidDeltidKode;
  private String heltidDeltidKodeTekst;

  public HedePersonProfilLedd() {}

  public HedePersonProfilLedd(String heltidDeltidKode, String heltidDeltidKodeTekst) {
    super();
    this.heltidDeltidKode = heltidDeltidKode;
    this.heltidDeltidKodeTekst = heltidDeltidKodeTekst;
  }

  public String getHeltidDeltidKode() {
    return heltidDeltidKode;
  }

  public void setHeltidDeltidKode(String heltidDeltidKode) {
    this.heltidDeltidKode = heltidDeltidKode;
  }

  public String getHeltidDeltidKodeTekst() {
    return heltidDeltidKodeTekst;
  }

  public void setHeltidDeltidKodeTekst(String heltidDeltidKodeTekst) {
    this.heltidDeltidKodeTekst = heltidDeltidKodeTekst;
  }

}
