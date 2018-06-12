package no.nav.arbeid.cv.kandidatsok.domene.sok;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsYrkeserfaring {

  private String styrkKodeStillingstittel;

  private int yrkeserfaringManeder;

  public EsYrkeserfaring() {}

  public EsYrkeserfaring(String styrkKodeStillingstittel, int yrkeserfaringManeder) {
    this.styrkKodeStillingstittel = styrkKodeStillingstittel;
    this.yrkeserfaringManeder = yrkeserfaringManeder;
  }

  public String getStyrkKodeStillingstittel() {
    return styrkKodeStillingstittel;
  }

  public int getYrkeserfaringManeder() {
    return yrkeserfaringManeder;
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
        && Objects.equals(yrkeserfaringManeder, that.yrkeserfaringManeder);
  }

  @Override
  public int hashCode() {

    return Objects.hash(styrkKodeStillingstittel, yrkeserfaringManeder);
  }

  @Override
  public String toString() {
    return "EsYrkeserfaring{" + " styrkKodeStillingstittel='" + styrkKodeStillingstittel + '\''
        + ", yrkeserfaringManeder='" + yrkeserfaringManeder + '\'' + '}';
  }

}
