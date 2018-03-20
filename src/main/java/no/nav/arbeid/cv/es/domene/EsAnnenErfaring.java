package no.nav.arbeid.cv.es.domene;

import java.util.Date;
import java.util.Objects;

import no.nav.elasticsearch.mapping.annotations.ElasticDateField;
import no.nav.elasticsearch.mapping.annotations.ElasticTextField;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsAnnenErfaring {

  @ElasticDateField
  private Date fraDato;

  @ElasticDateField
  private Date tilDato;

  @ElasticTextField
  private String beskrivelse;

  public EsAnnenErfaring() {}

  public EsAnnenErfaring(Date fraDato, Date tilDato, String beskrivelse) {
    this.fraDato = fraDato;
    this.tilDato = tilDato;
    this.beskrivelse = beskrivelse;
  }

  public Date getFraDato() {
    return fraDato;
  }

  public Date getTilDato() {
    return tilDato;
  }

  public String getBeskrivelse() {
    return beskrivelse;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EsAnnenErfaring that = (EsAnnenErfaring) o;
    return Objects.equals(fraDato, that.fraDato) && Objects.equals(tilDato, that.tilDato)
        && Objects.equals(beskrivelse, that.beskrivelse);
  }

  @Override
  public int hashCode() {

    return Objects.hash(fraDato, tilDato, beskrivelse);
  }

  @Override
  public String toString() {
    return "EsAnnenErfaring{" + "fraDato=" + fraDato + ", tilDato=" + tilDato + ", beskrivelse='"
        + beskrivelse + '\'' + '}';
  }

}
