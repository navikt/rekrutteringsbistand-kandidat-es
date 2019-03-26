package no.nav.arbeid.cv.kandidatsok.es.domene;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonInclude;
import no.nav.elasticsearch.mapping.annotations.ElasticCompletionField;
import no.nav.elasticsearch.mapping.annotations.ElasticKeywordField;
import no.nav.elasticsearch.mapping.annotations.ElasticTextField;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsFagdokumentasjon {

  @ElasticTextField(copyTo = "fritekst", analyzer = "norwegian")
  private String type;

  @ElasticTextField(copyTo = "fritekst", analyzer = "norwegian")
  @ElasticKeywordField
  @ElasticCompletionField
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private String tittel;

  public EsFagdokumentasjon() {
  }

  public EsFagdokumentasjon(String type, String tittel) {
    this.type = type;
    this.tittel = tittel;
  }

  public String getType() {
    return type;
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
    EsFagdokumentasjon esFagdokumentasjon = (EsFagdokumentasjon) o;
    return Objects.equals(type, esFagdokumentasjon.type)
        && Objects.equals(tittel, esFagdokumentasjon.tittel);
  }

  @Override
  public int hashCode() {

    return Objects.hash(type, tittel);
  }

  @Override
  public String toString() {
    return "EsFagdokumentasjon{" + "type='" + type + '\'' + ", tittel='" + tittel + '\'' + '}';
  }

  public static String getFagdokumentTypeLabel(String fagdokumentType) {
    switch (fagdokumentType) {
      case "SVENNEBREV_FAGBREV":
        return "Fagbrev/Svennebrev";
      case "MESTERBREV":
        return "Mesterbrev";
      case "AUTORISASJON":
        return "Autorisasjon";
      default:
        return fagdokumentType;
    }
  }

}
