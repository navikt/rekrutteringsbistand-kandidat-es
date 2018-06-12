package no.nav.arbeid.cv.indexer.arena.domene;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ARBEIDSMARKEDBRUKER")
public class ArenaArbeidsmarkedsbruker implements Serializable {

  private static final long serialVersionUID = -3568532476315078850L;

  @Id
  @OneToOne
  @JoinColumn(name = "person_id")
  private ArenaPerson person;

  @Column(name = "kandidat_nr")
  private String kandidatnummer;

  public ArenaArbeidsmarkedsbruker() {}

  ArenaArbeidsmarkedsbruker(ArenaPerson person, String kandidatnummer) {
    super();
    this.person = person;
    this.kandidatnummer = kandidatnummer;
  }


  public ArenaPerson getPerson() {
    return person;
  }

  public String getKandidatnummer() {
    return kandidatnummer;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((person == null) ? 0 : person.hashCode());
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
    ArenaArbeidsmarkedsbruker other = (ArenaArbeidsmarkedsbruker) obj;
    if (person == null) {
      if (other.person != null)
        return false;
    } else if (!person.equals(other.person))
      return false;
    return true;
  }



}
