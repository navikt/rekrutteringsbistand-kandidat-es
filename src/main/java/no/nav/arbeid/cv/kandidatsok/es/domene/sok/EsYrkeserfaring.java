package no.nav.arbeid.cv.kandidatsok.es.domene.sok;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.elasticsearch.mapping.annotations.ElasticDateField;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsYrkeserfaring {

  private String styrkKodeStillingstittel;

  private int yrkeserfaringManeder;
  
  private Date fraDato;

  public EsYrkeserfaring() {}

  public EsYrkeserfaring(String styrkKodeStillingstittel, int yrkeserfaringManeder, Date fraDato) {
    this.styrkKodeStillingstittel = styrkKodeStillingstittel;
    this.yrkeserfaringManeder = yrkeserfaringManeder;
    this.fraDato = fraDato;
  }

  public String getStyrkKodeStillingstittel() {
    return styrkKodeStillingstittel;
  }

  public int getYrkeserfaringManeder() {
    return yrkeserfaringManeder;
  }
  
  public Date getFraDato() {
    return fraDato;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EsYrkeserfaring that = (EsYrkeserfaring) o;
    return Objects.equals(styrkKodeStillingstittel, that.styrkKodeStillingstittel)
        && Objects.equals(yrkeserfaringManeder, that.yrkeserfaringManeder)
        && Objects.equals(fraDato, that.fraDato);
  }

  @Override
  public int hashCode() {

    return Objects.hash(styrkKodeStillingstittel, yrkeserfaringManeder, fraDato);
  }

  @Override
  public String toString() {
    return "EsYrkeserfaring{" + " styrkKodeStillingstittel='" + styrkKodeStillingstittel + '\''
        + ", yrkeserfaringManeder='" + yrkeserfaringManeder + '\''
        + ", fraDato='" + fraDato + '\'' + '}';
  }

}
