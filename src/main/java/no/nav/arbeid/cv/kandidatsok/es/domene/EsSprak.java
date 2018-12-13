package no.nav.arbeid.cv.kandidatsok.es.domene;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import no.nav.elasticsearch.mapping.annotations.ElasticCompletionField;
import no.nav.elasticsearch.mapping.annotations.ElasticDateField;
import no.nav.elasticsearch.mapping.annotations.ElasticKeywordField;
import no.nav.elasticsearch.mapping.annotations.ElasticTextField;

import java.util.Date;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsSprak {

  @ElasticDateField
  private Date fraDato;

  @ElasticKeywordField
  private String sprakKode;

  @ElasticTextField(copyTo = "fritekst", analyzer = "norwegian")
  @ElasticCompletionField
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private String sprakKodeTekst;

  @ElasticTextField(copyTo = "fritekst", analyzer = "norwegian")
  private String alternativTekst;

  @ElasticTextField(copyTo = "fritekst", analyzer = "norwegian")
  private String beskrivelse;

  public EsSprak() {}

  public EsSprak(Date fraDato, String sprakKode, String sprakKodeTekst, String alternativTekst,
      String beskrivelse) {
    this.fraDato = fraDato;
    this.sprakKode = sprakKode;
    this.sprakKodeTekst = sprakKodeTekst;
    this.alternativTekst = alternativTekst;
    this.beskrivelse = beskrivelse;
  }

  public Date getFraDato() {
    return fraDato;
  }

  public String getSprakKode() {
    return sprakKode;
  }

  public String getSprakKodeTekst() {
    return sprakKodeTekst;
  }

  public String getAlternativTekst() {
    return alternativTekst;
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
    EsSprak esSprak = (EsSprak) o;
    return Objects.equals(fraDato, esSprak.fraDato) && Objects.equals(sprakKode, esSprak.sprakKode)
        && Objects.equals(sprakKodeTekst, esSprak.sprakKodeTekst)
        && Objects.equals(alternativTekst, esSprak.alternativTekst)
        && Objects.equals(beskrivelse, esSprak.beskrivelse);
  }

  @Override
  public int hashCode() {

    return Objects.hash(fraDato, sprakKode, sprakKodeTekst, alternativTekst, beskrivelse);
  }

  @Override
  public String toString() {
    return "EsSprak{" + "fraDato=" + fraDato + ", sprakKode='" + sprakKode + '\''
        + ", sprakKodeTekst='" + sprakKodeTekst + '\'' + ", alternativTekst='" + alternativTekst
        + '\'' + ", beskrivelse='" + beskrivelse + '\'' + '}';
  }

}
