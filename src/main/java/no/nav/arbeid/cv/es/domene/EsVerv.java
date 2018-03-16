package no.nav.arbeid.cv.es.domene;

import java.util.Date;
import java.util.Objects;

import org.frekele.elasticsearch.mapping.annotations.ElasticDateField;
import org.frekele.elasticsearch.mapping.annotations.ElasticTextField;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsVerv {

  @ElasticDateField
  private Date fraDato;

  @ElasticDateField
  private Date tilDato;

  @ElasticTextField
  private String organisasjon;

  @ElasticTextField
  private String tittel;

  public EsVerv() {}

  public EsVerv(Date fraDato, Date tilDato, String organisasjon, String tittel) {
    this.fraDato = fraDato;
    this.tilDato = tilDato;
    this.organisasjon = organisasjon;
    this.tittel = tittel;
  }

  public Date getFraDato() {
    return fraDato;
  }

  public Date getTilDato() {
    return tilDato;
  }

  public String getOrganisasjon() {
    return organisasjon;
  }

  public String getTittel() {
    return tittel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EsVerv esVerv = (EsVerv) o;
    return Objects.equals(fraDato, esVerv.fraDato) && Objects.equals(tilDato, esVerv.tilDato)
        && Objects.equals(organisasjon, esVerv.organisasjon)
        && Objects.equals(tittel, esVerv.tittel);
  }

  @Override
  public int hashCode() {

    return Objects.hash(fraDato, tilDato, organisasjon, tittel);
  }

  @Override
  public String toString() {
    return "EsVerv{" + "fraDato=" + fraDato + ", tilDato=" + tilDato + ", organisasjon='"
        + organisasjon + '\'' + ", tittel='" + tittel + '\'' + '}';
  }

}
