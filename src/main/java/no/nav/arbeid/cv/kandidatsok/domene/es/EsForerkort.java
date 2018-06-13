package no.nav.arbeid.cv.kandidatsok.domene.es;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.elasticsearch.mapping.annotations.ElasticDateField;
import no.nav.elasticsearch.mapping.annotations.ElasticKeywordField;
import no.nav.elasticsearch.mapping.annotations.ElasticTextField;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsForerkort {

  @ElasticDateField
  private Date fraDato;

  @ElasticDateField
  private Date tilDato;

  @ElasticKeywordField
  private String forerkortKode;

  @ElasticTextField
  private String forerkortKodeKlasse;

  @ElasticTextField
  private String alternativKlasse;

  @ElasticTextField
  private String utsteder;

  public EsForerkort() {}

  public EsForerkort(Date fraDato, Date tilDato, String forerkortKode, String forerkortKodeKlasse,
      String alternativKlasse, String utsteder) {
    this.fraDato = fraDato;
    this.tilDato = tilDato;
    this.forerkortKode = forerkortKode;
    this.forerkortKodeKlasse = forerkortKodeKlasse;
    this.alternativKlasse = alternativKlasse;
    this.utsteder = utsteder;
  }

  public Date getFraDato() {
    return fraDato;
  }

  public Date getTilDato() {
    return tilDato;
  }

  public String getForerkortKode() {
    return forerkortKode;
  }

  public String getForerkortKodeKlasse() {
    return forerkortKodeKlasse;
  }

  public String getAlternativKlasse() {
    return alternativKlasse;
  }

  public String getUtsteder() {
    return utsteder;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EsForerkort that = (EsForerkort) o;
    return Objects.equals(fraDato, that.fraDato) && Objects.equals(tilDato, that.tilDato)
        && Objects.equals(forerkortKode, that.forerkortKode)
        && Objects.equals(forerkortKodeKlasse, that.forerkortKodeKlasse)
        && Objects.equals(alternativKlasse, that.alternativKlasse)
        && Objects.equals(utsteder, that.utsteder);
  }

  @Override
  public int hashCode() {

    return Objects.hash(fraDato, tilDato, forerkortKode, forerkortKodeKlasse, alternativKlasse,
        utsteder);
  }

  @Override
  public String toString() {
    return "EsForerkort{" + "fraDato=" + fraDato + ", tilDato=" + tilDato + ", forerkortKode='"
        + forerkortKode + '\'' + ", forerkortKodeKlasse='" + forerkortKodeKlasse + '\''
        + ", alternativKlasse='" + alternativKlasse + '\'' + ", utsteder='" + utsteder + '\'' + '}';
  }

}
