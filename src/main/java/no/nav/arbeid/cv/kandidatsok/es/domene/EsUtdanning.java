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
public class EsUtdanning {

  @ElasticDateField
  private Date fraDato;

  @ElasticDateField
  private Date tilDato;

  @ElasticTextField(copyTo = "fritekst", analyzer = "norwegian")
  private String utdannelsessted;

  @ElasticKeywordField
  private String nusKode;

  @ElasticTextField(copyTo = "fritekst", analyzer = "norwegian")
  @ElasticCompletionField
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private String nusKodeGrad;

  @ElasticTextField(copyTo = "fritekst", analyzer = "norwegian")
  private String alternativGrad;

  // Her ligger autorisasjon, svennebrev_fagbrev, mesterbrev
  @ElasticTextField(copyTo = "fritekst", analyzer = "norwegian")
  private String yrkestatus;

  public EsUtdanning() {}

  public EsUtdanning(Date fraDato, Date tilDato, String utdannelsessted, String nusKode, String nusGrad,
                     String beskrivelse, String yrkestatus) {
    this(fraDato, tilDato, utdannelsessted, nusKode, nusGrad, beskrivelse);
    this.yrkestatus = yrkestatus;
  }

  public EsUtdanning(Date fraDato, Date tilDato, String utdannelsessted, String nusKode,
      String nusKodeGrad, String alternativGrad) {
    this.fraDato = fraDato;
    this.tilDato = tilDato;
    this.utdannelsessted = utdannelsessted;
    this.nusKode = nusKode;
    this.nusKodeGrad = nusKodeGrad;
    this.alternativGrad = alternativGrad;
  }

  public Date getFraDato() {
    return fraDato;
  }

  public Date getTilDato() {
    return tilDato;
  }

  public String getUtdannelsessted() {
    return utdannelsessted;
  }

  public String getNusKode() {
    return nusKode;
  }

  public String getNusKodeGrad() {
    return nusKodeGrad;
  }

  public String getYrkestatus() {
    return yrkestatus;
  }

  public String getAlternativGrad() {
    return alternativGrad;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EsUtdanning that = (EsUtdanning) o;
    return Objects.equals(fraDato, that.fraDato) && Objects.equals(tilDato, that.tilDato)
        && Objects.equals(utdannelsessted, that.utdannelsessted)
        && Objects.equals(nusKode, that.nusKode) && Objects.equals(nusKodeGrad, that.nusKodeGrad)
        && Objects.equals(alternativGrad, that.alternativGrad);
  }

  @Override
  public int hashCode() {

    return Objects.hash(fraDato, tilDato, utdannelsessted, nusKode, nusKodeGrad, alternativGrad);
  }

  @Override
  public String toString() {
    return "EsUtdanning{" + "fraDato=" + fraDato + ", tilDato=" + tilDato + ", utdannelsessted='"
        + utdannelsessted + '\'' + ", nusKode='" + nusKode + '\'' + ", nusKodeGrad='" + nusKodeGrad
        + '\'' + ", alternativGrad='" + alternativGrad + '\'' + '}';
  }

}
