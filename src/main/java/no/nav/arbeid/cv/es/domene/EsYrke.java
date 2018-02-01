package no.nav.arbeid.cv.es.domene;

import java.util.Date;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

public class EsYrke {

  @Field(type = FieldType.Date, store = true, index = true)
  protected Date fraDato;
  @Field(type = FieldType.Date, store = true, index = true)
  protected Date tilDato;
  @Field(type = FieldType.text, store = true, index = true)
  protected String arbeidsgiver;
  @Field(type = FieldType.text, store = true, index = true)
  protected String stillingstittel;
  @Field(type = FieldType.keyword, store = true, index = true)
  protected String styrkKode;
  @Field(type = FieldType.text, store = true, index = true)
  protected String styrkBeskrivelse;

  public EsYrke() {}

  public EsYrke(Date fraDato, Date tilDato, String arbeidsgiver, String stillingstittel, String styrkKode,
      String styrkBeskrivelse) {
    super();
    this.fraDato = fraDato;
    this.tilDato = tilDato;
    this.arbeidsgiver = arbeidsgiver;
    this.stillingstittel = stillingstittel;
    this.styrkKode = styrkKode;
    this.styrkBeskrivelse = styrkBeskrivelse;
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

  public String getStillingstittel() {
    return stillingstittel;
  }

  public String getStyrkKode() {
    return styrkKode;
  }

  public String getStyrkBeskrivelse() {
    return styrkBeskrivelse;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((arbeidsgiver == null) ? 0 : arbeidsgiver.hashCode());
    result = prime * result + ((fraDato == null) ? 0 : fraDato.hashCode());
    result = prime * result + ((stillingstittel == null) ? 0 : stillingstittel.hashCode());
    result = prime * result + ((styrkBeskrivelse == null) ? 0 : styrkBeskrivelse.hashCode());
    result = prime * result + ((styrkKode == null) ? 0 : styrkKode.hashCode());
    result = prime * result + ((tilDato == null) ? 0 : tilDato.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    EsYrke other = (EsYrke) obj;
    if (arbeidsgiver == null) {
      if (other.arbeidsgiver != null)
        return false;
    } else if (!arbeidsgiver.equals(other.arbeidsgiver))
      return false;
    if (fraDato == null) {
      if (other.fraDato != null)
        return false;
    } else if (!fraDato.equals(other.fraDato))
      return false;
    if (stillingstittel == null) {
      if (other.stillingstittel != null)
        return false;
    } else if (!stillingstittel.equals(other.stillingstittel))
      return false;
    if (styrkBeskrivelse == null) {
      if (other.styrkBeskrivelse != null)
        return false;
    } else if (!styrkBeskrivelse.equals(other.styrkBeskrivelse))
      return false;
    if (styrkKode == null) {
      if (other.styrkKode != null)
        return false;
    } else if (!styrkKode.equals(other.styrkKode))
      return false;
    if (tilDato == null) {
      if (other.tilDato != null)
        return false;
    } else if (!tilDato.equals(other.tilDato))
      return false;
    return true;
  }


}
