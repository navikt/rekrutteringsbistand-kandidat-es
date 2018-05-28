package no.nav.arbeid.cv.arena.domene;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "CVLEDD")
@Inheritance
@DiscriminatorColumn(name = "LEDDKODE", discriminatorType = DiscriminatorType.STRING)
public abstract class ArenaCvLedd {

  @EmbeddedId
  protected ArenaCvLeddPK ArenaCvLeddPK;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "PERSON_ID", insertable = false, updatable = false)
  protected ArenaPerson person;

  @Column(insertable = false, updatable = false)
  protected int cvleddnr;

  @Column
  protected String leddtekst1;

  @Column
  protected String leddtekst2;

  @Column(name = "DATO_FRA")
  protected LocalDate fraDato;

  @Column(name = "DATO_TIL")
  protected LocalDate tilDato;

  @Column
  protected String beskrivelse;

  @Column
  protected Integer omfangVerdi;

  @Column
  protected String omfangMaaleenhet;

//  @Column
//  protected String strukturkode;
//
//  @Column
//  protected String elementord;

//  @Column
//  protected String elementklassekode;

//  @Column
//  protected String nivaa;

  @Column(name = "HENSYN")
  protected String hensyn;

//  @Column
//  protected String aktiv;

  protected ArenaCvLedd(no.nav.arbeid.cv.arena.domene.ArenaCvLedd.ArenaCvLeddPK arenaCvLeddPK,
      ArenaPerson person, int cvleddnr, String leddtekst1, String leddtekst2, LocalDate fraDato,
      LocalDate tilDato, String beskrivelse, Integer omfangVerdi, String omfangMaaleenhet,
      String strukturkode, String elementord, String elementklassekode, String nivaa, String hensyn,
      String aktiv) {
    super();
    ArenaCvLeddPK = arenaCvLeddPK;
    this.person = person;
    this.cvleddnr = cvleddnr;
    this.leddtekst1 = leddtekst1;
    this.leddtekst2 = leddtekst2;
    this.fraDato = fraDato;
    this.tilDato = tilDato;
    this.beskrivelse = beskrivelse;
    this.omfangVerdi = omfangVerdi;
    this.omfangMaaleenhet = omfangMaaleenhet;
//    this.strukturkode = strukturkode;
//    this.elementord = elementord;
//    this.elementklassekode = elementklassekode;
//    this.nivaa = nivaa;
    this.hensyn = hensyn;
//    this.aktiv = aktiv;
  }

  ArenaCvLedd() {}

  @JsonIgnore
  public int getCvleddnr() {
    return cvleddnr;
  }

  @JsonIgnore
  public ArenaPerson getPerson() {
    return person;
  }

  @JsonIgnore
  public String getLeddtekst1() {
    return leddtekst1;
  }

  @JsonIgnore
  public String getLeddtekst2() {
    return leddtekst2;
  }

  public LocalDate getFraDato() {
    return fraDato;
  }

  public LocalDate getTilDato() {
    return tilDato;
  }

  @JsonIgnore
  public String getOpprinneligBeskrivelse() {
    return beskrivelse;
  }

//  @JsonIgnore
//  public String getStrukturkode() {
//    return strukturkode;
//  }
//
//  @JsonIgnore
//  public String getElementord() {
//    return elementord;
//  }

//  @JsonIgnore
//  public String getElementklassekode() {
//    return elementklassekode;
//  }

//  @JsonIgnore
//  public String getNivaa() {
//    return nivaa;
//  }

  @JsonIgnore
  public String getHensyn() {
    return hensyn;
  }

//  @JsonIgnore
//  public String getAktiv() {
//    return aktiv;
//  }

  public void setPerson(ArenaPerson arenaPerson) {
    this.person = arenaPerson;
  }

  @Embeddable
  public static class ArenaCvLeddPK implements Serializable {

    private static final long serialVersionUID = 6482069378657910533L;

    ArenaCvLeddPK() {}

    ArenaCvLeddPK(Long personId, Integer cvleddnr) {
      this.personId = personId;
      this.cvleddnr = cvleddnr;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((cvleddnr == null) ? 0 : cvleddnr.hashCode());
      result = prime * result + ((personId == null) ? 0 : personId.hashCode());
      return result;
    }

    @Column(name = "PERSON_ID")
    protected Long personId;

    @Column(name = "CVLEDDNR")
    protected Integer cvleddnr;

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      ArenaCvLeddPK other = (ArenaCvLeddPK) obj;
      if (cvleddnr == null) {
        if (other.cvleddnr != null)
          return false;
      } else if (!cvleddnr.equals(other.cvleddnr))
        return false;
      if (personId == null) {
        if (other.personId != null)
          return false;
      } else if (!personId.equals(other.personId))
        return false;
      return true;
    }

  }

}
