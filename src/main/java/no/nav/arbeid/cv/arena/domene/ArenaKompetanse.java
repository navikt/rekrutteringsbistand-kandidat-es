package no.nav.arbeid.cv.arena.domene;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "kompetanseelement")
public class ArenaKompetanse implements ArenaKode {

  @Id
  @Column(name = "profilelement_id")
  protected Integer profilelementId;

  @Column(name = "strukturkode")
  protected String strukturkode;

  @Column(name = "elementord")
  protected String elementOrd;

  @Column(name = "beskrivelse")
  protected String beskrivelse;

  @Override
  public Integer getProfilelementId() {
    return profilelementId;
  }

  @Override
  public String getElementOrd() {
    return elementOrd;
  }

  @Override
  public String getStrukturkode() {
    return strukturkode;
  }

  @Override
  public String getBeskrivelse() {
    return beskrivelse;
  }

}
