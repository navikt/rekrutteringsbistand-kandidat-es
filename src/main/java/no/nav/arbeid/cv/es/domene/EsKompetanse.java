package no.nav.arbeid.cv.es.domene;

import java.util.Objects;

import org.frekele.elasticsearch.mapping.annotations.ElasticKeywordField;
import org.frekele.elasticsearch.mapping.annotations.ElasticTextField;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsKompetanse {

  @ElasticTextField
  @ElasticKeywordField
  private String navn;

  public EsKompetanse() {}

  public EsKompetanse(String navn) {
    this.navn = navn;
  }

  public String getNavn() {
    return navn;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EsKompetanse that = (EsKompetanse) o;
    return Objects.equals(navn, that.navn);
  }

  @Override
  public int hashCode() {

    return Objects.hash(navn);
  }

  @Override
  public String toString() {
    return "EsKompetanse{" + "navn='" + navn + '\'' + '}';
  }

}
