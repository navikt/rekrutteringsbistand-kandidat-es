package no.nav.arbeid.cv.es.domene;

import java.util.Objects;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

public class EsGeografiJobbonsker {

  @Field(type = FieldType.text, store = true, index = true)
  private String geografiKodeTekst;

  @Field(type = FieldType.text, store = true, index = true)
  private String geografiKode;

  public EsGeografiJobbonsker() {
  }

  public EsGeografiJobbonsker(String geografiKodeTekst, String geografiKode) {
    this.geografiKodeTekst = geografiKodeTekst;
    this.geografiKode = geografiKode;
  }

  public String getGeografiKodeTekst() {
    return geografiKodeTekst;
  }

  public String getGeografiKode() {
    return geografiKode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EsGeografiJobbonsker that = (EsGeografiJobbonsker) o;
    return Objects.equals(geografiKodeTekst, that.geografiKodeTekst) &&
        Objects.equals(geografiKode, that.geografiKode);
  }

  @Override
  public int hashCode() {

    return Objects.hash(geografiKodeTekst, geografiKode);
  }

}
