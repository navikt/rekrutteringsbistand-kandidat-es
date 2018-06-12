package no.nav.arbeid.cv.kandidatsok.domene.sok;

import java.util.List;

public class Sokeresultat {

  private long totaltAntallTreff;

  private List<EsCv> cver;

  private List<Aggregering> aggregeringer;

  public Sokeresultat(long totaltAntallTreff, List<EsCv> cver, List<Aggregering> aggregeringer) {
    this.totaltAntallTreff = totaltAntallTreff;
    this.cver = cver;
    this.aggregeringer = aggregeringer;
  }

  public long getTotaltAntallTreff() {
    return totaltAntallTreff;
  }

  public List<EsCv> getCver() {
    return cver;
  }

  public List<Aggregering> getAggregeringer() {
    return aggregeringer;
  }

}
