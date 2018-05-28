package no.nav.arbeid.cv.arena.domene;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "profilelement")
public class ArenaProfilelement implements ArenaKode {

  @Id
  @Column(name = "profilelement_id")
  protected Integer profilelementId;

  @Column(name = "elementord")
  protected String elementOrd;

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
    return null;
  }

  @Override
  public String getBeskrivelse() {
    return null;
  }
}
