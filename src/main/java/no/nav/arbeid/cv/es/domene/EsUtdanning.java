package no.nav.arbeid.cv.es.domene;

import java.util.Date;
import java.util.Objects;

import org.frekele.elasticsearch.mapping.annotations.ElasticDateField;
import org.frekele.elasticsearch.mapping.annotations.ElasticKeywordField;
import org.frekele.elasticsearch.mapping.annotations.ElasticTextField;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsUtdanning {

  @ElasticDateField
  private Date fraDato;

  @ElasticDateField
  private Date tilDato;

  @ElasticTextField
  private String grad;

  @ElasticTextField
  private String studiepoeng;

  @ElasticTextField
  private String utdannelsessted;

  @ElasticTextField
  private String geografiskSted;

  @ElasticKeywordField
  private String nusKode;

  @ElasticTextField
  @ElasticKeywordField
  private String nusKodeTekst;

  public EsUtdanning() {}

  public EsUtdanning(Date fraDato, Date tilDato, String grad, String studiepoeng,
      String utdannelsessted, String geografiskSted, String nusKode, String nusKodeTekst) {
    this.fraDato = fraDato;
    this.tilDato = tilDato;
    this.grad = grad;
    this.studiepoeng = studiepoeng;
    this.utdannelsessted = utdannelsessted;
    this.geografiskSted = geografiskSted;
    this.nusKode = nusKode;
    this.nusKodeTekst = nusKodeTekst;
  }

  public Date getFraDato() {
    return fraDato;
  }

  public Date getTilDato() {
    return tilDato;
  }

  public String getGrad() {
    return grad;
  }

  public String getStudiepoeng() {
    return studiepoeng;
  }

  public String getUtdannelsessted() {
    return utdannelsessted;
  }

  public String getGeografiskSted() {
    return geografiskSted;
  }

  public String getNusKode() {
    return nusKode;
  }

  public String getNusKodeTekst() {
    return nusKodeTekst;
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
        && Objects.equals(grad, that.grad) && Objects.equals(studiepoeng, that.studiepoeng)
        && Objects.equals(utdannelsessted, that.utdannelsessted)
        && Objects.equals(geografiskSted, that.geografiskSted)
        && Objects.equals(nusKode, that.nusKode) && Objects.equals(nusKodeTekst, that.nusKodeTekst);
  }

  @Override
  public int hashCode() {

    return Objects.hash(fraDato, tilDato, grad, studiepoeng, utdannelsessted, geografiskSted,
        nusKode, nusKodeTekst);
  }

  @Override
  public String toString() {
    return "EsUtdanning{" + "fraDato=" + fraDato + ", tilDato=" + tilDato + ", grad='" + grad + '\''
        + ", studiepoeng='" + studiepoeng + '\'' + ", utdannelsessted='" + utdannelsessted + '\''
        + ", geografiskSted='" + geografiskSted + '\'' + ", nusKode='" + nusKode + '\''
        + ", nusKodeTekst='" + nusKodeTekst + '\'' + '}';
  }

}
