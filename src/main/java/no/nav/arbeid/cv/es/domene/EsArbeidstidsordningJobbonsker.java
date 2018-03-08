package no.nav.arbeid.cv.es.domene;

import java.util.Objects;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

public class EsArbeidstidsordningJobbonsker {

  @Field(type = FieldType.text, store = true, index = true)
  private String arbeidstidsordningKode;

  @Field(type = FieldType.text, store = true, index = true)
  private String arbeidstidsordningKodeTekst;

  public EsArbeidstidsordningJobbonsker() {
  }

  public EsArbeidstidsordningJobbonsker(String arbeidstidsordningKode,
      String arbeidstidsordningKodeTekst) {
    this.arbeidstidsordningKode = arbeidstidsordningKode;
    this.arbeidstidsordningKodeTekst = arbeidstidsordningKodeTekst;
  }

  public String getArbeidstidsordningKode() {
    return arbeidstidsordningKode;
  }

  public String getArbeidstidsordningKodeTekst() {
    return arbeidstidsordningKodeTekst;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EsArbeidstidsordningJobbonsker that = (EsArbeidstidsordningJobbonsker) o;
    return Objects.equals(arbeidstidsordningKode, that.arbeidstidsordningKode) &&
        Objects.equals(arbeidstidsordningKodeTekst, that.arbeidstidsordningKodeTekst);
  }

  @Override
  public int hashCode() {

    return Objects.hash(arbeidstidsordningKode, arbeidstidsordningKodeTekst);
  }

  @Override
  public String toString() {
    return "EsArbeidstidsordningJobbonsker{" +
        "arbeidstidsordningKode='" + arbeidstidsordningKode + '\'' +
        ", arbeidstidsordningKodeTekst='" + arbeidstidsordningKodeTekst + '\'' +
        '}';
  }

}
