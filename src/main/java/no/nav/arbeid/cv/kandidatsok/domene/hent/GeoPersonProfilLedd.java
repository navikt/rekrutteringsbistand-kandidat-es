package no.nav.arbeid.cv.kandidatsok.domene.hent;

public class GeoPersonProfilLedd {

  private String geografiKode;
  private String geografiKodeTekst;

  public GeoPersonProfilLedd() {}

  public GeoPersonProfilLedd(String geografiKode, String geografiKodeTekst) {
    super();
    this.geografiKode = geografiKode;
    this.geografiKodeTekst = geografiKodeTekst;
  }

  public String getGeografiKode() {
    return geografiKode;
  }

  public void setGeografiKode(String geografiKode) {
    this.geografiKode = geografiKode;
  }

  public String getGeografiKodeTekst() {
    return geografiKodeTekst;
  }

  public void setGeografiKodeTekst(String geografiKodeTekst) {
    this.geografiKodeTekst = geografiKodeTekst;
  }

}
