package no.nav.arbeid.cv.es.domene;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Objects;
import no.nav.elasticsearch.mapping.annotations.ElasticCompletionField;
import no.nav.elasticsearch.mapping.annotations.ElasticKeywordField;
import no.nav.elasticsearch.mapping.annotations.ElasticTextField;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsSamletKompetanse {

  // @ElasticTextField(analyzer = "norwegian")
  @ElasticKeywordField
  @ElasticCompletionField
  private String samletKompetanseTekst;

  public EsSamletKompetanse() {
  }

  public EsSamletKompetanse(String samletKompetanseTekst) {
    this.samletKompetanseTekst = samletKompetanseTekst;
  }

  public String getSamletKompetanseTekst() { return  samletKompetanseTekst; }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EsSamletKompetanse that = (EsSamletKompetanse) o;
    return Objects.equals(samletKompetanseTekst, that.samletKompetanseTekst);
  }

  @Override
  public int hashCode() {
    return Objects.hash(samletKompetanseTekst);
  }

  @Override
  public String toString() {
    return "EsSamletKompetanse{" +
        "samletKompetanseTekst='" + samletKompetanseTekst + '\'' +
        '}';
  }

}
