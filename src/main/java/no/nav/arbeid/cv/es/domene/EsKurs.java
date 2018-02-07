package no.nav.arbeid.cv.es.domene;

import java.time.LocalDate;
import java.util.Objects;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

public class EsKurs {

  @Field(type = FieldType.Date, store = true, index = true)
  private LocalDate fraDato;

  @Field(type = FieldType.Date, store = true, index = true)
  private LocalDate tilDato;

  @Field(type = FieldType.text, store = true, index = true)
  private String tittel;

  @Field(type = FieldType.text, store = true, index = true)
  private String arrangor;

  @Field(type = FieldType.text, store = true, index = true)
  private String omfang;

  public EsKurs(LocalDate fraDato, LocalDate tilDato, String tittel, String arrangor,
      String omfang) {
    this.fraDato = fraDato;
    this.tilDato = tilDato;
    this.tittel = tittel;
    this.arrangor = arrangor;
    this.omfang = omfang;
  }

  public LocalDate getFraDato() {
    return fraDato;
  }

  public LocalDate getTilDato() {
    return tilDato;
  }

  public String getTittel() {
    return tittel;
  }

  public String getArrangor() {
    return arrangor;
  }

  public String getOmfang() {
    return omfang;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EsKurs esKurs = (EsKurs) o;
    return Objects.equals(fraDato, esKurs.fraDato) &&
        Objects.equals(tilDato, esKurs.tilDato) &&
        Objects.equals(tittel, esKurs.tittel) &&
        Objects.equals(arrangor, esKurs.arrangor) &&
        Objects.equals(omfang, esKurs.omfang);
  }

  @Override
  public int hashCode() {

    return Objects.hash(fraDato, tilDato, tittel, arrangor, omfang);
  }

  @Override
  public String toString() {
    return "EsKurs{" +
        "fraDato=" + fraDato +
        ", tilDato=" + tilDato +
        ", tittel='" + tittel + '\'' +
        ", arrangor='" + arrangor + '\'' +
        ", omfang='" + omfang + '\'' +
        '}';
  }

}
