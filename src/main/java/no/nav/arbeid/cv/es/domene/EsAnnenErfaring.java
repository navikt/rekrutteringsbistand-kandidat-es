package no.nav.arbeid.cv.es.domene;

import java.time.LocalDate;
import java.util.Objects;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

public class EsAnnenErfaring {

  @Field(type = FieldType.text, store = true, index = true)
  private LocalDate fraDato;

  @Field(type = FieldType.text, store = true, index = true)
  private LocalDate tilDato;

  @Field(type = FieldType.text, store = true, index = true)
  private String beskrivelse;

  public EsAnnenErfaring(LocalDate fraDato, LocalDate tilDato, String beskrivelse) {
    this.fraDato = fraDato;
    this.tilDato = tilDato;
    this.beskrivelse = beskrivelse;
  }

  public LocalDate getFraDato() {
    return fraDato;
  }

  public LocalDate getTilDato() {
    return tilDato;
  }

  public String getBeskrivelse() {
    return beskrivelse;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EsAnnenErfaring that = (EsAnnenErfaring) o;
    return Objects.equals(fraDato, that.fraDato) &&
        Objects.equals(tilDato, that.tilDato) &&
        Objects.equals(beskrivelse, that.beskrivelse);
  }

  @Override
  public int hashCode() {

    return Objects.hash(fraDato, tilDato, beskrivelse);
  }

  @Override
  public String toString() {
    return "EsAnnenErfaring{" +
        "fraDato=" + fraDato +
        ", tilDato=" + tilDato +
        ", beskrivelse='" + beskrivelse + '\'' +
        '}';
  }

}
