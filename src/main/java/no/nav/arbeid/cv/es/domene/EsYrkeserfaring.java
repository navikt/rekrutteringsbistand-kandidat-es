package no.nav.arbeid.cv.es.domene;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;
import java.util.Objects;

import no.nav.elasticsearch.mapping.annotations.ElasticCompletionField;
import no.nav.elasticsearch.mapping.annotations.ElasticDateField;
import no.nav.elasticsearch.mapping.annotations.ElasticIntegerField;
import no.nav.elasticsearch.mapping.annotations.ElasticKeywordField;
import no.nav.elasticsearch.mapping.annotations.ElasticTextField;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsYrkeserfaring {

  @ElasticDateField
  private Date fraDato;

  @ElasticDateField
  private Date tilDato;

  @ElasticTextField
  private String arbeidsgiver;

  @ElasticKeywordField
  private String styrkKode;

  @ElasticKeywordField
  private String styrkKode4Siffer;

  @ElasticKeywordField
  private String styrkKode3Siffer;

  @ElasticTextField(copyTo = "fritekst", analyzer = "norwegian")
  @ElasticKeywordField
  @ElasticCompletionField
  private String styrkKodeStillingstittel;

  @ElasticTextField
  private String alternativStillingstittel;

  @ElasticKeywordField
  private String organisasjonsnummer;

  @ElasticKeywordField
  private String naceKode;

  @ElasticIntegerField
  private int yrkeserfaringManeder;

  public EsYrkeserfaring() {}

  public EsYrkeserfaring(Date fraDato, Date tilDato, String arbeidsgiver, String styrkKode,
      String styrkKodeStillingstittel, String alternativStillingstittel, String organisasjonsnummer,
      String naceKode, int yrkeserfaringManeder) {
    this.fraDato = fraDato;
    this.tilDato = tilDato;
    this.arbeidsgiver = arbeidsgiver;
    this.styrkKode = styrkKode;
    this.styrkKode4Siffer =
        (styrkKode == null ? null : (styrkKode.length() <= 3 ? null : styrkKode.substring(0, 4)));
    this.styrkKode3Siffer =
        (styrkKode == null ? null : (styrkKode.length() <= 2 ? null : styrkKode.substring(0, 3)));
    this.styrkKodeStillingstittel = styrkKodeStillingstittel;
    this.alternativStillingstittel = alternativStillingstittel;
    this.organisasjonsnummer = organisasjonsnummer;
    this.naceKode = naceKode;
    this.yrkeserfaringManeder = yrkeserfaringManeder;
  }

  public Date getFraDato() {
    return fraDato;
  }

  public Date getTilDato() {
    return tilDato;
  }

  public String getArbeidsgiver() {
    return arbeidsgiver;
  }

  public String getStyrkKode() {
    return styrkKode;
  }

  public String getStyrkKode3Siffer() {
    return styrkKode3Siffer;
  }

  public String getStyrkKode4Siffer() {
    return styrkKode4Siffer;
  }

  public String getStyrkKodeStillingstittel() {
    return styrkKodeStillingstittel;
  }

  public String getAlternativStillingstittel() {
    return alternativStillingstittel;
  }

  public String getOrganisasjonsnummer() {
    return organisasjonsnummer;
  }

  public String getNaceKode() {
    return naceKode;
  }

  public int getYrkeserfaringManeder() { return yrkeserfaringManeder; }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EsYrkeserfaring that = (EsYrkeserfaring) o;
    return Objects.equals(fraDato, that.fraDato) && Objects.equals(tilDato, that.tilDato)
        && Objects.equals(arbeidsgiver, that.arbeidsgiver)
        && Objects.equals(styrkKode, that.styrkKode)
        && Objects.equals(styrkKodeStillingstittel, that.styrkKodeStillingstittel)
        && Objects.equals(alternativStillingstittel, that.alternativStillingstittel)
        && Objects.equals(organisasjonsnummer, that.organisasjonsnummer)
        && Objects.equals(naceKode, that.naceKode)
        && Objects.equals(yrkeserfaringManeder, that.yrkeserfaringManeder);
  }

  @Override
  public int hashCode() {

    return Objects.hash(fraDato, tilDato, arbeidsgiver, styrkKode, styrkKodeStillingstittel,
        alternativStillingstittel, organisasjonsnummer, naceKode, yrkeserfaringManeder);
  }

  @Override
  public String toString() {
    return "EsYrkeserfaring{" + "fraDato=" + fraDato + ", tilDato=" + tilDato + ", arbeidsgiver='"
        + arbeidsgiver + '\'' + ", styrkKode='" + styrkKode + '\'' + ", styrkKodeStillingstittel='"
        + styrkKodeStillingstittel + '\'' + ", alternativStillingstittel='"
        + alternativStillingstittel + '\'' + ", organisasjonsnummer='" + organisasjonsnummer + '\''
        + ", naceKode='" + naceKode + '\'' + ", yrkeserfaringManeder='" + yrkeserfaringManeder + '\''
    + '}';
  }

}
