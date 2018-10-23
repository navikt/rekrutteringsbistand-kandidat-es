package no.nav.arbeid.cv.kandidatsok.es.domene;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import no.nav.elasticsearch.mapping.annotations.ElasticCompletionField;
import no.nav.elasticsearch.mapping.annotations.ElasticKeywordField;
import no.nav.elasticsearch.mapping.annotations.values.IntValue;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsSamletKompetanse {

//  @ElasticTextField(analyzer = "norwegian")
  @ElasticKeywordField
  @ElasticCompletionField(maxInputLength = @IntValue(100))
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private String samletKompetanseTekst;

  public EsSamletKompetanse() {}

  public EsSamletKompetanse(String samletKompetanseTekst) {
    this.samletKompetanseTekst = samletKompetanseTekst;
  }

  public String getSamletKompetanseTekst() {
    return samletKompetanseTekst;
  }


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
    return "EsSamletKompetanse{" + "samletKompetanseTekst='" + samletKompetanseTekst + '\'' + '}';
  }

}
