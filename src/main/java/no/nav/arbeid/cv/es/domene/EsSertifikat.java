package no.nav.arbeid.cv.es.domene;

import java.util.Date;
import java.util.Objects;

import org.frekele.elasticsearch.mapping.annotations.ElasticDateField;
import org.frekele.elasticsearch.mapping.annotations.ElasticKeywordField;
import org.frekele.elasticsearch.mapping.annotations.ElasticTextField;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsSertifikat {

  @ElasticDateField
  private Date fraDato;

  @ElasticDateField
  private Date tilDato;

  @ElasticKeywordField
  private String sertifikatKode;

  @ElasticTextField
  @ElasticKeywordField
  private String sertifikatKodeTekst;

  @ElasticTextField
  private String utsteder;

  public EsSertifikat() {}

  public EsSertifikat(Date fraDato, Date tilDato, String sertifikatKode, String sertifikatKodeTekst,
      String utsteder) {
    this.fraDato = fraDato;
    this.tilDato = tilDato;
    this.sertifikatKode = sertifikatKode;
    this.sertifikatKodeTekst = sertifikatKodeTekst;
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

  public String getSertifikatKodeTekst() {
    return sertifikatKodeTekst;
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
        && Objects.equals(sertifikatKodeTekst, that.sertifikatKodeTekst)
        && Objects.equals(utsteder, that.utsteder);
  }

  @Override
  public int hashCode() {

    return Objects.hash(fraDato, tilDato, sertifikatKode, sertifikatKodeTekst, utsteder);
  }

  @Override
  public String toString() {
    return "EsSertifikat{" + "fraDato=" + fraDato + ", tilDato=" + tilDato + ", sertifikatKode='"
        + sertifikatKode + '\'' + ", sertifikatKodeTekst='" + sertifikatKodeTekst + '\''
        + ", utsteder='" + utsteder + '\'' + '}';
  }

}
