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
  private String omfangEnhet;

  @Field(type = FieldType.text, store = true, index = true)
  private Integer omfangVerdi;

  public EsKurs(LocalDate fraDato, LocalDate tilDato, String tittel, String arrangor,
      String omfangEnhet, Integer omfangVerdi) {
    this.fraDato = fraDato;
    this.tilDato = tilDato;
    this.tittel = tittel;
    this.arrangor = arrangor;
    this.omfangEnhet = omfangEnhet;
    this.omfangVerdi = omfangVerdi;
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

  public String getOmfangEnhet() {
    return omfangEnhet;
  }

  public Integer getOmfangVerdi() {
    return omfangVerdi;
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
        Objects.equals(omfangEnhet, esKurs.omfangEnhet) &&
        Objects.equals(omfangVerdi, esKurs.omfangVerdi);
  }

  @Override
  public int hashCode() {

    return Objects.hash(fraDato, tilDato, tittel, arrangor, omfangEnhet, omfangVerdi);
  }

  @Override
  public String toString() {
    return "EsKurs{" +
        "fraDato=" + fraDato +
        ", tilDato=" + tilDato +
        ", tittel='" + tittel + '\'' +
        ", arrangor='" + arrangor + '\'' +
        ", omfangEnhet='" + omfangEnhet + '\'' +
        ", omfangVerdi='" + omfangVerdi + '\'' +
        '}';
  }

}
