package no.nav.arbeid.cv.arena.domene;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cv")
public class ArenaCv implements Serializable {

  private static final long serialVersionUID = -3568532476315078850L;

  @Id
  @OneToOne
  @JoinColumn(name = "person_id")
  private ArenaPerson person;

  @Column(name = "tekst_egenbeskrivelse")
  private String beskrivelse;

  @Column(name = "reg_dato")
  private LocalDate regDato;

  @Column(name = "mod_dato")
  private LocalDate modDato;

  public ArenaCv() {}


  ArenaCv(ArenaPerson person, String beskrivelse, LocalDate regDato, LocalDate modDato) {
    super();
    this.person = person;
    this.beskrivelse = beskrivelse;
    this.regDato = regDato;
    this.modDato = modDato;
  }


  public ArenaPerson getPerson() {
    return person;
  }

  public String getBeskrivelse() {
    return beskrivelse;
  }

  public LocalDate getRegDato() {
    return regDato;
  }

  public LocalDate getModDato() {
    return modDato;
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
    ArenaCv other = (ArenaCv) obj;
    if (person == null) {
      if (other.person != null)
        return false;
    } else if (!person.equals(other.person))
      return false;
    return true;
  }



}
