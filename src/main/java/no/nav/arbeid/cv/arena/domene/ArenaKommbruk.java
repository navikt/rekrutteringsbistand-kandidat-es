package no.nav.arbeid.cv.arena.domene;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "KOMMBRUK")
public class ArenaKommbruk {

  @Id
  @Column(name = "KOMMBRUK_ID")
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "objekt_id", insertable = false, updatable = false)
  private ArenaPerson person;

  @Column(name = "KOMMBRUKKODE")
  private String kommbrukKode;

  @Column(name = "KOMMSTRENG")
  private String kommStreng;

  @Column(name = "DATO_FRA")
  private LocalDate fraDato;

  @Column(name = "DATO_TIL")
  private LocalDate tilDato;

  public LocalDate getFraDato() {
    return fraDato;
  }

  public LocalDate getTilDato() {
    return tilDato;
  }

  public boolean isEpost() {
    return "EPSBL".equalsIgnoreCase(kommbrukKode);
  }

  public String getEpost() {
    return kommStreng;
  }

  public boolean isGyldig() {
    LocalDate now = LocalDate.now();
    return now.isAfter(fraDato) && (tilDato == null || now.isBefore(tilDato));
  }

  public void setKommStreng(String kommStreng) {
    this.kommStreng = kommStreng;
  }

}
