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
public class EsSertifikat {

  private Date fraDato;

  private Date tilDato;

  private String sertifikatKode;

  @ElasticTextField(copyTo = "fritekst", analyzer = "norwegian")
  @ElasticKeywordField
  @ElasticCompletionField
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private String sertifikatKodeNavn;

  @ElasticTextField(copyTo = "fritekst", analyzer = "norwegian")
  private String alternativtNavn;

  @ElasticTextField(copyTo = "fritekst", analyzer = "norwegian")
  private String utsteder;

  public EsSertifikat() {}

  public EsSertifikat(Date fraDato, Date tilDato, String tittel, String utsteder) {
    this(fraDato, tilDato, null, tittel, tittel, utsteder);
  }

  public EsSertifikat(Date fraDato, Date tilDato, String sertifikatKode, String sertifikatKodeNavn,
      String alternativtNavn, String utsteder) {
    this.fraDato = fraDato;
    this.tilDato = tilDato;
    this.sertifikatKode = sertifikatKode;
    this.sertifikatKodeNavn = sertifikatKodeNavn;
    this.alternativtNavn = alternativtNavn;
    this.utsteder = utsteder;
  }

  public Date getFraDato() {
    return fraDato;
  }

  public Date getTilDato() {
    return tilDato;
  }

  public String getSertifikatKode() {
    return sertifikatKode;
  }

  public String getSertifikatKodeNavn() {
    return sertifikatKodeNavn;
  }

  public String getAlternativtNavn() {
    return alternativtNavn;
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
    EsSertifikat that = (EsSertifikat) o;
    return Objects.equals(fraDato, that.fraDato) && Objects.equals(tilDato, that.tilDato)
        && Objects.equals(sertifikatKode, that.sertifikatKode)
        && Objects.equals(sertifikatKodeNavn, that.sertifikatKodeNavn)
        && Objects.equals(alternativtNavn, that.alternativtNavn)
        && Objects.equals(utsteder, that.utsteder);
  }

  @Override
  public int hashCode() {

    return Objects.hash(fraDato, tilDato, sertifikatKode, sertifikatKodeNavn, alternativtNavn,
        utsteder);
  }

  @Override
  public String toString() {
    return "EsSertifikat{" + "fraDato=" + fraDato + ", tilDato=" + tilDato + ", sertifikatKode='"
        + sertifikatKode + '\'' + ", sertifikatKodeNavn='" + sertifikatKodeNavn + '\''
        + ", alternativtNavn='" + alternativtNavn + '\'' + ", utsteder='" + utsteder + '\'' + '}';
  }

}
