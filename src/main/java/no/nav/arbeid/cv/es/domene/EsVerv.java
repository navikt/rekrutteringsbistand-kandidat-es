package no.nav.arbeid.cv.es.domene;

import java.time.LocalDate;
import java.util.Objects;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

public class EsVerv {

  @Field(type = FieldType.Date, store = true, index = true)
  private LocalDate fraDato;

  @Field(type = FieldType.Date, store = true, index = true)
  private LocalDate tilDato;

  @Field(type = FieldType.text, store = true, index = true)
  private String organisasjon;

  @Field(type = FieldType.text, store = true, index = true)
  private String tittel;

  public EsVerv(LocalDate fraDato, LocalDate tilDato, String organisasjon, String tittel) {
    this.fraDato = fraDato;
    this.tilDato = tilDato;
    this.organisasjon = organisasjon;
    this.tittel = tittel;
  }

  public LocalDate getFraDato() {
    return fraDato;
  }

  public LocalDate getTilDato() {
    return tilDato;
  }

  public String getOrganisasjon() {
    return organisasjon;
  }

  public String getTittel() {
    return tittel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EsVerv esVerv = (EsVerv) o;
    return Objects.equals(fraDato, esVerv.fraDato) &&
        Objects.equals(tilDato, esVerv.tilDato) &&
        Objects.equals(organisasjon, esVerv.organisasjon) &&
        Objects.equals(tittel, esVerv.tittel);
  }

  @Override
  public int hashCode() {

    return Objects.hash(fraDato, tilDato, organisasjon, tittel);
  }

  @Override
  public String toString() {
    return "EsVerv{" +
        "fraDato=" + fraDato +
        ", tilDato=" + tilDato +
        ", organisasjon='" + organisasjon + '\'' +
        ", tittel='" + tittel + '\'' +
        '}';
  }

}
