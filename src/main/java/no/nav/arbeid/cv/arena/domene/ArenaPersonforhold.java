package no.nav.arbeid.cv.arena.domene;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "personforhold")
public class ArenaPersonforhold {

  @EmbeddedId
  protected ArenaPersonforholdPK arenaPersonforholdPK;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "PERSON_ID", insertable = false, updatable = false)
  protected ArenaPerson person;

  @Column(name = "PERSONFORHOLDKODE", insertable = false, updatable = false)
  private String personforholdkode;

  @Column(name = "DATO_SLUTT")
  private LocalDate sluttDato;

  @Column(name = "DATO_START")
  private LocalDate startDato;

  @Column(name = "REG_DATO")
  private LocalDate regDato;

  @Column(name = "MOD_DATO")
  private LocalDate modDato;

  public String getPersonforholdkode() {
    return personforholdkode;
  }
  
  public LocalDate getStartDato() {
    return startDato;
  }
  
  public LocalDate getSluttDato() {
    return sluttDato;
  }
  
  @Embeddable
  public static class ArenaPersonforholdPK implements Serializable {

    private static final long serialVersionUID = 6482069378657910533L;

    ArenaPersonforholdPK() {}

    ArenaPersonforholdPK(Long personId, String personforholdkode) {
      this.personId = personId;
      this.personforholdkode = personforholdkode;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((personforholdkode == null) ? 0 : personforholdkode.hashCode());
      result = prime * result + ((personId == null) ? 0 : personId.hashCode());
      return result;
    }

    @Column(name = "PERSON_ID")
    protected Long personId;

    @Column(name = "personforholdkode")
    protected String personforholdkode;

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      ArenaPersonforholdPK other = (ArenaPersonforholdPK) obj;
      if (personforholdkode == null) {
        if (other.personforholdkode != null)
          return false;
      } else if (!personforholdkode.equals(other.personforholdkode))
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
