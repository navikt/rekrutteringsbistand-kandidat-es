package no.nav.arbeid.cv.kandidatsok.domene.es;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.elasticsearch.mapping.annotations.ElasticCompletionField;
import no.nav.elasticsearch.mapping.annotations.ElasticDateField;
import no.nav.elasticsearch.mapping.annotations.ElasticKeywordField;
import no.nav.elasticsearch.mapping.annotations.ElasticTextField;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsKompetanse {

  @ElasticDateField
  private Date fraDato;

  @ElasticKeywordField
  private String kompKode;

  @ElasticTextField(copyTo = "fritekst", analyzer = "norwegian")
  @ElasticKeywordField
  @ElasticCompletionField
  private String kompKodeNavn;

  @ElasticTextField
  private String alternativtNavn;

  @ElasticTextField
  private String beskrivelse;

  public EsKompetanse() {}

  public EsKompetanse(Date fraDato, String kompKode, String kompKodeNavn, String alternativtNavn,
      String beskrivelse) {
    this.fraDato = fraDato;
    this.kompKode = kompKode;
    this.kompKodeNavn = kompKodeNavn;
    this.alternativtNavn = alternativtNavn;
    this.beskrivelse = beskrivelse;
  }

  public Date getFraDato() {
    return fraDato;
  }

  public String getKompKode() {
    return kompKode;
  }

  public String getKompKodeNavn() {
    return kompKodeNavn;
  }

  public String getAlternativtNavn() {
    return alternativtNavn;
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
    EsKompetanse that = (EsKompetanse) o;
    return Objects.equals(fraDato, that.fraDato) && Objects.equals(kompKode, that.kompKode)
        && Objects.equals(kompKodeNavn, that.kompKodeNavn)
        && Objects.equals(alternativtNavn, that.alternativtNavn)
        && Objects.equals(beskrivelse, that.beskrivelse);
  }

  @Override
  public int hashCode() {

    return Objects.hash(fraDato, kompKode, kompKodeNavn, alternativtNavn, beskrivelse);
  }

  @Override
  public String toString() {
    return "EsKompetanse{" + "fraDato=" + fraDato + ", kompKode='" + kompKode + '\''
        + ", kompKodeNavn='" + kompKodeNavn + '\'' + ", alternativtNavn='" + alternativtNavn + '\''
        + ", beskrivelse='" + beskrivelse + '\'' + '}';
  }

}
