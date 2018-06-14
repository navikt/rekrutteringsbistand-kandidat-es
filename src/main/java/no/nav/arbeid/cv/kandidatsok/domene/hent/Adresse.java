package no.nav.arbeid.cv.kandidatsok.domene.hent;

import org.apache.commons.lang3.StringUtils;

public class Adresse {

  private String landkode;

  private String postnr;

  private String poststednavn;

  private Integer kommunenr;

  private String adrlinje1;

  private String adrlinje2;

  private String adrlinje3;

  public Adresse() {}

  public Adresse(Long id, Person person, String landkode, String postnr, String poststednavn,
      String adrlinje1, String adrlinje2, String adrlinje3, Integer kommunenr) {
    super();
    this.landkode = landkode;
    this.postnr = postnr;
    this.poststednavn = poststednavn;
    this.adrlinje1 = adrlinje1;
    this.adrlinje2 = adrlinje2;
    this.adrlinje3 = adrlinje3;
    this.kommunenr = kommunenr;
  }

  public String getLandkode() {
    return landkode;
  }

  public String getPostnr() {
    return postnr;
  }

  public String getPoststednavn() {
    return StringUtils.defaultString(poststednavn);
  }

  public Integer getKommunenr() {
    return kommunenr;
  }

  public String getAdrlinje1() {
    return StringUtils.defaultString(adrlinje1);
  }

  public String getAdrlinje2() {
    return StringUtils.defaultString(adrlinje2);
  }

  public String getAdrlinje3() {
    return StringUtils.defaultString(adrlinje3);
  }

  public void setAdrlinje1(String adrlinje1) {
    this.adrlinje1 = adrlinje1;
  }

  public void setAdrlinje2(String adrlinje2) {
    this.adrlinje2 = adrlinje2;
  }

  public void setAdrlinje3(String adrlinje3) {
    this.adrlinje3 = adrlinje3;
  }

  public void setKommunenr(Integer kommunenr) {
    this.kommunenr = kommunenr;
  }

  public void setLandkode(String landkode) {
    this.landkode = landkode;
  }

  public void setPostnr(String postnr) {
    this.postnr = postnr;
  }

  public void setPoststednavn(String poststednavn) {
    this.poststednavn = poststednavn;
  }

}
