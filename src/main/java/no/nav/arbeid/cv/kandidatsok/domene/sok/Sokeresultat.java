package no.nav.arbeid.cv.kandidatsok.domene.sok;

import java.util.List;

public class Sokeresultat {

  private long totaltAntallTreff;

  private List<no.nav.arbeid.cv.kandidatsok.domene.es.EsCv> cver;

  private List<Aggregering> aggregeringer;

  public Sokeresultat(long totaltAntallTreff, List<no.nav.arbeid.cv.kandidatsok.domene.es.EsCv> cver, List<Aggregering> aggregeringer) {
    this.totaltAntallTreff = totaltAntallTreff;
    this.cver = cver;
    this.aggregeringer = aggregeringer;
  }

  public long getTotaltAntallTreff() {
    return totaltAntallTreff;
  }

  public List<no.nav.arbeid.cv.kandidatsok.domene.es.EsCv> getCver() {
    return cver;
  }

  public List<Aggregering> getAggregeringer() {
    return aggregeringer;
  }

}
