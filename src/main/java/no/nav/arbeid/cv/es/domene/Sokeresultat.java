package no.nav.arbeid.cv.es.domene;

import java.util.List;

public class Sokeresultat {

  private List<EsCv> cver;

  private List<Aggregering> aggregeringer;

  public Sokeresultat(List<EsCv> cver, List<Aggregering> aggregeringer) {
    this.cver = cver;
    this.aggregeringer = aggregeringer;
  }

  public List<EsCv> getCver() {
    return cver;
  }

  public List<Aggregering> getAggregeringer() {
    return aggregeringer;
  }

}
