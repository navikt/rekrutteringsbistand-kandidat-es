package no.nav.arbeid.cv.es.domene;

public class Aggregeringsfelt {

  private String feltnavn;
  private Long antall;
//  private List<Aggregeringsfelt> subfelt;

  public Aggregeringsfelt(String feltnavn, Long antall) {
    this.feltnavn = feltnavn;
    this.antall = antall;
  }

  public String getFeltnavn() {
    return feltnavn;
  }

  public Long getAntall() {
    return antall;
  }

  // public List<Aggregeringsfelt> getSubfelt() {
  // return subfelt;
  // }

}
