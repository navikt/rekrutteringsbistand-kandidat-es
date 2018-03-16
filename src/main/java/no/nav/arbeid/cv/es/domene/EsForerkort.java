package no.nav.arbeid.cv.es.domene;

import java.util.Date;
import java.util.Objects;

import org.frekele.elasticsearch.mapping.annotations.ElasticBooleanField;
import org.frekele.elasticsearch.mapping.annotations.ElasticDateField;
import org.frekele.elasticsearch.mapping.annotations.ElasticKeywordField;
import org.frekele.elasticsearch.mapping.annotations.ElasticTextField;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsForerkort {

  @ElasticDateField
  private Date fraDato;

  @ElasticDateField
  private Date tilDato;

  @ElasticKeywordField
  private String klasse;

  @ElasticTextField
  private String utsteder;

  @ElasticBooleanField
  private Boolean disponererKjoretoy;

  public EsForerkort() {}

  public EsForerkort(Date fraDato, Date tilDato, String klasse, String utsteder,
      Boolean disponererKjoretoy) {
    this.fraDato = fraDato;
    this.tilDato = tilDato;
    this.klasse = klasse;
    this.utsteder = utsteder;
    this.disponererKjoretoy = disponererKjoretoy;
  }

  public Date getFraDato() {
    return fraDato;
  }

  public Date getTilDato() {
    return tilDato;
  }

  public String getKlasse() {
    return klasse;
  }

  public String getUtsteder() {
    return utsteder;
  }

  public Boolean getDisponererKjoretoy() {
    return disponererKjoretoy;
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
        && Objects.equals(klasse, that.klasse) && Objects.equals(utsteder, that.utsteder)
        && Objects.equals(disponererKjoretoy, that.disponererKjoretoy);
  }

  @Override
  public int hashCode() {

    return Objects.hash(fraDato, tilDato, klasse, utsteder, disponererKjoretoy);
  }

  @Override
  public String toString() {
    return "EsForerkort{" + "fraDato=" + fraDato + ", tilDato=" + tilDato + ", klasse='" + klasse
        + '\'' + ", utsteder='" + utsteder + '\'' + ", disponererKjoretoy=" + disponererKjoretoy
        + '}';
  }

}
