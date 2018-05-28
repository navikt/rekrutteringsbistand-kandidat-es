package no.nav.arbeid.cv.arena.domene;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DiscriminatorFormula;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "PERSONPROFILLEDD")
@Inheritance
@DiscriminatorFormula("LEDDKODE")
public class ArenaPersonProfilLedd {

  @EmbeddedId
  protected ArenaPersonProfilLeddPK arenaPersonProfilLeddPK;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "PERSON_ID", insertable = false, updatable = false)
  protected ArenaPerson person;

  @Column
  protected int nivaa;

  @Column
  protected String strukturkode;

  // @Column
  // protected String elementord;

  @Column(insertable = false, updatable = false)
  protected String elementklassekode;

  @Column
  protected String hensyn;

  @Column
  protected String aktiv;

  @Column(insertable = false, updatable = false)
  protected String leddkodeErKilde;

  protected ArenaPersonProfilLedd() {}

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "profilelement_id", insertable = false, updatable = false)
  @NotFound(action = NotFoundAction.IGNORE)
  @Fetch(FetchMode.JOIN)
  protected ArenaProfilelement profilelement;

  @JsonIgnore
  public ArenaPerson getPerson() {
    return person;
  }

  @JsonIgnore
  public int getProfilelementId() {
    return arenaPersonProfilLeddPK.profilelementId;
  }

  @JsonIgnore
  public int getNivaa() {
    return nivaa;
  }

  @JsonIgnore
  public String getStrukturkode() {
    return strukturkode;
  }

  @JsonIgnore
  public String getElementklassekode() {
    return elementklassekode;
  }

  @JsonIgnore
  public String getLeddkode() {
    return arenaPersonProfilLeddPK.leddkode;
  }

  @JsonIgnore
  public String getLeddkodeErKilde() {
    return leddkodeErKilde;
  }

  @JsonIgnore
  public String getElementord() {
    return profilelement == null ? "" : profilelement.getElementOrd();
  }

  @JsonIgnore
  public String getAktiv() {
    return aktiv;
  }

  @JsonIgnore
  public String getHensyn() {
    return hensyn;
  }

  public void setPerson(ArenaPerson arenaPerson) {
    this.person = arenaPerson;
  }

  @Embeddable
  public static class ArenaPersonProfilLeddPK implements Serializable {

    private static final long serialVersionUID = -7075670343491091625L;

    @Column(name = "PERSON_ID")
    protected Long personId;

    @Column(name = "LEDDKODE", insertable = false, updatable = false)
    protected String leddkode;

    @Column(name = "PROFILELEMENT_ID", insertable = false, updatable = false)
    protected int profilelementId;

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((leddkode == null) ? 0 : leddkode.hashCode());
      result = prime * result + ((personId == null) ? 0 : personId.hashCode());
      result = prime * result + profilelementId;
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      ArenaPersonProfilLeddPK other = (ArenaPersonProfilLeddPK) obj;
      if (leddkode == null) {
        if (other.leddkode != null)
          return false;
      } else if (!leddkode.equals(other.leddkode))
        return false;
      if (personId == null) {
        if (other.personId != null)
          return false;
      } else if (!personId.equals(other.personId))
        return false;
      if (profilelementId != other.profilelementId)
        return false;
      return true;
    }

  }

}
