package no.nav.arbeid.cv.es.domene;

import java.time.LocalDate;
import java.util.Objects;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

public class EsUtdanning {

  @Field(type = FieldType.Date, store = true, index = true)
  private LocalDate fraDato;
  @Field(type = FieldType.Date, store = true, index = true)
  private LocalDate tilDato;
  @Field(type = FieldType.text, store = true, index = true)
  private String grad;
  @Field(type = FieldType.text, store = true, index = true)
  private String studiepoeng;
  @Field(type = FieldType.text, store = true, index = true)
  private String utdannelsessted;
  @Field(type = FieldType.text, store = true, index = true)
  private String geografiskSted;
  @Field(type = FieldType.text, store = true, index = true)
  private String nusKode;
  @Field(type = FieldType.text, store = true, index = true)
  private String nusKodeTekst;

  public EsUtdanning(LocalDate fraDato, LocalDate tilDato, String grad, String studiepoeng,
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

  public LocalDate getFraDato() {
    return fraDato;
  }

  public LocalDate getTilDato() {
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
    return Objects.equals(fraDato, that.fraDato) &&
        Objects.equals(tilDato, that.tilDato) &&
        Objects.equals(grad, that.grad) &&
        Objects.equals(studiepoeng, that.studiepoeng) &&
        Objects.equals(utdannelsessted, that.utdannelsessted) &&
        Objects.equals(geografiskSted, that.geografiskSted) &&
        Objects.equals(nusKode, that.nusKode) &&
        Objects.equals(nusKodeTekst, that.nusKodeTekst);
  }

  @Override
  public int hashCode() {

    return Objects
        .hash(fraDato, tilDato, grad, studiepoeng, utdannelsessted, geografiskSted, nusKode,
            nusKodeTekst);
  }

  @Override
  public String toString() {
    return "EsUtdanning{" +
        "fraDato=" + fraDato +
        ", tilDato=" + tilDato +
        ", grad='" + grad + '\'' +
        ", studiepoeng='" + studiepoeng + '\'' +
        ", utdannelsessted='" + utdannelsessted + '\'' +
        ", geografiskSted='" + geografiskSted + '\'' +
        ", nusKode='" + nusKode + '\'' +
        ", nusKodeTekst='" + nusKodeTekst + '\'' +
        '}';
  }

}
