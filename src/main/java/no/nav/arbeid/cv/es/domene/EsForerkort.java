package no.nav.arbeid.cv.es.domene;

import java.util.Date;
import java.util.Objects;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

public class EsForerkort {

  @Field(type = FieldType.Date, store = true, index = true)
  private Date fraDato;

  @Field(type = FieldType.Date, store = true, index = true)
  private Date tilDato;

  @Field(type = FieldType.text, store = true, index = true)
  private String klasse;

  @Field(type = FieldType.text, store = true, index = true)
  private String utsteder;

  @Field(type = FieldType.Boolean, store = true, index = true)
  private Boolean disponererKjoretoy;

  public EsForerkort() {
  }

  public EsForerkort(Date fraDato, Date tilDato, String klasse, String utsteder,
      Boolean disponererKjoretoy) {
    this.fraDato = fraDato;
    this.tilDato = tilDato;
    this.klasse = klasse;
    this.utsteder = utsteder;
    this.disponererKjoretoy = disponererKjoretoy;
  }

  public Date getFraDato() {
    return fraDato;
  }

  public Date getTilDato() {
    return tilDato;
  }

  public String getKlasse() {
    return klasse;
  }

  public String getUtsteder() {
    return utsteder;
  }

  public Boolean getDisponererKjoretoy() {
    return disponererKjoretoy;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EsForerkort that = (EsForerkort) o;
    return Objects.equals(fraDato, that.fraDato) &&
        Objects.equals(tilDato, that.tilDato) &&
        Objects.equals(klasse, that.klasse) &&
        Objects.equals(utsteder, that.utsteder) &&
        Objects.equals(disponererKjoretoy, that.disponererKjoretoy);
  }

  @Override
  public int hashCode() {

    return Objects.hash(fraDato, tilDato, klasse, utsteder, disponererKjoretoy);
  }

  @Override
  public String toString() {
    return "EsForerkort{" +
        "fraDato=" + fraDato +
        ", tilDato=" + tilDato +
        ", klasse='" + klasse + '\'' +
        ", utsteder='" + utsteder + '\'' +
        ", disponererKjoretoy=" + disponererKjoretoy +
        '}';
  }

}
