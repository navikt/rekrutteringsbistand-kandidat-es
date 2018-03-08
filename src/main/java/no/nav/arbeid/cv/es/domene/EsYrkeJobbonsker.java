package no.nav.arbeid.cv.es.domene;

import java.util.Objects;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

public class EsYrkeJobbonsker {

  @Field(type = FieldType.text, store = true, index = true)
  private String styrkKode;

  @Field(type = FieldType.text, store = true, index = true)
  private String styrkBeskrivelse;

  @Field(type = FieldType.Boolean, store = true, index = true)
  private boolean primaertJobbonske;

  public EsYrkeJobbonsker() {
  }

  public EsYrkeJobbonsker(String styrkKode, String styrkBeskrivelse, boolean primaertJobbonske) {
    this.styrkKode = styrkKode;
    this.styrkBeskrivelse = styrkBeskrivelse;
    this.primaertJobbonske = primaertJobbonske;
  }

  public String getStyrkKode() {
    return styrkKode;
  }

  public String getStyrkBeskrivelse() {
    return styrkBeskrivelse;
  }

  public boolean isPrimaertJobbonske() {
    return primaertJobbonske;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EsYrkeJobbonsker that = (EsYrkeJobbonsker) o;
    return primaertJobbonske == that.primaertJobbonske &&
        Objects.equals(styrkKode, that.styrkKode) &&
        Objects.equals(styrkBeskrivelse, that.styrkBeskrivelse);
  }

  @Override
  public int hashCode() {

    return Objects.hash(styrkKode, styrkBeskrivelse, primaertJobbonske);
  }

  @Override
  public String toString() {
    return "EsYrkeJobbonsker{" +
        "styrkKode='" + styrkKode + '\'' +
        ", styrkBeskrivelse='" + styrkBeskrivelse + '\'' +
        ", primaertJobbonske=" + primaertJobbonske +
        '}';
  }
}
