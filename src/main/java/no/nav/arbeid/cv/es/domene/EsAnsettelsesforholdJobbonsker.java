package no.nav.arbeid.cv.es.domene;

import java.util.Objects;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

public class EsAnsettelsesforholdJobbonsker {

  @Field(type = FieldType.text, store = true, index = true)
  private String ansettelsesforholdKode;

  @Field(type = FieldType.text, store = true, index = true)
  private String ansettelsesforholdKodeTekst;

  public EsAnsettelsesforholdJobbonsker() {
  }

  public EsAnsettelsesforholdJobbonsker(String ansettelsesforholdKode,
      String ansettelsesforholdKodeTekst) {
    this.ansettelsesforholdKode = ansettelsesforholdKode;
    this.ansettelsesforholdKodeTekst = ansettelsesforholdKodeTekst;
  }

  public String getAnsettelsesforholdKode() {
    return ansettelsesforholdKode;
  }

  public String getAnsettelsesforholdKodeTekst() {
    return ansettelsesforholdKodeTekst;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EsAnsettelsesforholdJobbonsker that = (EsAnsettelsesforholdJobbonsker) o;
    return Objects.equals(ansettelsesforholdKode, that.ansettelsesforholdKode) &&
        Objects.equals(ansettelsesforholdKodeTekst, that.ansettelsesforholdKodeTekst);
  }

  @Override
  public int hashCode() {

    return Objects.hash(ansettelsesforholdKode, ansettelsesforholdKodeTekst);
  }

  @Override
  public String toString() {
    return "EsAnsettelsesforholdJobbonsker{" +
        "ansettelsesforholdKode='" + ansettelsesforholdKode + '\'' +
        ", ansettelsesforholdKodeTekst='" + ansettelsesforholdKodeTekst + '\'' +
        '}';
  }
}
