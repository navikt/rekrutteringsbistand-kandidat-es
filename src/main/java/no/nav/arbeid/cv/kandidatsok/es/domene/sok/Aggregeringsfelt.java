package no.nav.arbeid.cv.kandidatsok.es.domene.sok;

import java.util.List;

public class Aggregeringsfelt {

  private String feltnavn;
  private Long antall;
  private List<Aggregeringsfelt> subfelt;

  public Aggregeringsfelt(String feltnavn, Long antall, List<Aggregeringsfelt> subfelt) {
    this.feltnavn = feltnavn;
    this.antall = antall;
    this.subfelt = subfelt;
  }

  public String getFeltnavn() {
    return feltnavn;
  }

  public Long getAntall() {
    return antall;
  }

  public List<Aggregeringsfelt> getSubfelt() {
    return subfelt;
  }

  @Override
  public String toString() {
    return "Aggregeringsfelt [feltnavn=" + feltnavn + ", antall=" + antall + ", subfelt=" + subfelt
        + "]";
  }


}
