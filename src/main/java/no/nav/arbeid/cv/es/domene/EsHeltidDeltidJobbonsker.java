package no.nav.arbeid.cv.es.domene;

import java.util.Objects;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

public class EsHeltidDeltidJobbonsker {

  @Field(type = FieldType.text, store = true, index = true)
  private String heltidDeltidKode;

  @Field(type = FieldType.text, store = true, index = true)
  private String heltidDeltidKodeTekst;

  public EsHeltidDeltidJobbonsker() {
  }

  public EsHeltidDeltidJobbonsker(String heltidDeltidKode, String heltidDeltidKodeTekst) {
    this.heltidDeltidKode = heltidDeltidKode;
    this.heltidDeltidKodeTekst = heltidDeltidKodeTekst;
  }

  public String getHeltidDeltidKode() {
    return heltidDeltidKode;
  }

  public String getHeltidDeltidKodeTekst() {
    return heltidDeltidKodeTekst;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EsHeltidDeltidJobbonsker that = (EsHeltidDeltidJobbonsker) o;
    return Objects.equals(heltidDeltidKode, that.heltidDeltidKode) &&
        Objects.equals(heltidDeltidKodeTekst, that.heltidDeltidKodeTekst);
  }

  @Override
  public int hashCode() {

    return Objects.hash(heltidDeltidKode, heltidDeltidKodeTekst);
  }

  @Override
  public String toString() {
    return "EsHeltidDeltidJobbonsker{" +
        "heltidDeltidKode='" + heltidDeltidKode + '\'' +
        ", heltidDeltidKodeTekst='" + heltidDeltidKodeTekst + '\'' +
        '}';
  }
}
