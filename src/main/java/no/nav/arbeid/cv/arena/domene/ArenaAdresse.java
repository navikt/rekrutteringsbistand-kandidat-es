package no.nav.arbeid.cv.arena.domene;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "ADRESSEBRUK")
public class ArenaAdresse {

  @Id
  @Column(name = "ADRESSEBRUK_ID")
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "objekt_id", insertable = false, updatable = false)
  private ArenaPerson person;

  @Column
  private String landkode;

  @Column
  private String postnr;

  @Column
  private String poststednavn;

  @Column
  private Integer kommunenr;

  @Column
  private String adrlinje1;

  @Column
  private String adrlinje2;

  @Column
  private String adrlinje3;

  @Column(name = "DATO_FRA")
  private LocalDate fraDato;

  @Column(name = "DATO_TIL")
  private LocalDate tilDato;

  public ArenaAdresse(Long id, ArenaPerson person, String landkode, String postnr,
      String poststednavn, String adrlinje1, String adrlinje2, String adrlinje3, Integer kommunenr,
      LocalDate fraDato) {
    super();
    this.id = id;
    this.person = person;
    this.landkode = landkode;
    this.postnr = postnr;
    this.poststednavn = poststednavn;
    this.adrlinje1 = adrlinje1;
    this.adrlinje2 = adrlinje2;
    this.adrlinje3 = adrlinje3;
    this.kommunenr = kommunenr;
    this.fraDato = fraDato;
  }

  public ArenaAdresse() {}

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

  public void setPerson(ArenaPerson arenaPerson) {
    this.person = arenaPerson;
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

  @JsonIgnore
  public LocalDate getFraDato() {
    return fraDato;
  }

  @JsonIgnore
  public LocalDate getTilDato() {
    return tilDato;
  }

  @JsonIgnore
  public boolean isGyldig() {
    LocalDate now = LocalDate.now();
    return now.isAfter(fraDato) && (tilDato == null || now.isBefore(tilDato));
  }

}
