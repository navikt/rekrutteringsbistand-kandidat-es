package no.nav.arbeid.cv.es.domene;

import java.time.LocalDate;
import java.util.Objects;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

public class EsSertifikat {

  @Field(type = FieldType.Date, store = true, index = true)
  private LocalDate fraDato;

  @Field(type = FieldType.Date, store = true, index = true)
  private LocalDate tilDato;

  @Field(type = FieldType.text, store = true, index = true)
  private String sertifikatKode;

  @Field(type = FieldType.text, store = true, index = true)
  private String sertifikatKodeTekst;

  @Field(type = FieldType.text, store = true, index = true)
  private String utsteder;

  public EsSertifikat(LocalDate fraDato, LocalDate tilDato, String sertifikatKode,
      String sertifikatKodeTekst, String utsteder) {
    this.fraDato = fraDato;
    this.tilDato = tilDato;
    this.sertifikatKode = sertifikatKode;
    this.sertifikatKodeTekst = sertifikatKodeTekst;
    this.utsteder = utsteder;
  }

  public LocalDate getFraDato() {
    return fraDato;
  }

  public LocalDate getTilDato() {
    return tilDato;
  }

  public String getSertifikatKode() {
    return sertifikatKode;
  }

  public String getSertifikatKodeTekst() {
    return sertifikatKodeTekst;
  }

  public String getUtsteder() {
    return utsteder;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EsSertifikat that = (EsSertifikat) o;
    return Objects.equals(fraDato, that.fraDato) &&
        Objects.equals(tilDato, that.tilDato) &&
        Objects.equals(sertifikatKode, that.sertifikatKode) &&
        Objects.equals(sertifikatKodeTekst, that.sertifikatKodeTekst) &&
        Objects.equals(utsteder, that.utsteder);
  }

  @Override
  public int hashCode() {

    return Objects.hash(fraDato, tilDato, sertifikatKode, sertifikatKodeTekst, utsteder);
  }

  @Override
  public String toString() {
    return "EsSertifikat{" +
        "fraDato=" + fraDato +
        ", tilDato=" + tilDato +
        ", sertifikatKode='" + sertifikatKode + '\'' +
        ", sertifikatKodeTekst='" + sertifikatKodeTekst + '\'' +
        ", utsteder='" + utsteder + '\'' +
        '}';
  }

}
