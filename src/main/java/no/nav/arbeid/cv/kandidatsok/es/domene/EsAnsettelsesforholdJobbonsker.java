package no.nav.arbeid.cv.kandidatsok.es.domene;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.elasticsearch.mapping.annotations.ElasticKeywordField;
import no.nav.elasticsearch.mapping.annotations.ElasticTextField;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsAnsettelsesforholdJobbonsker {

  @ElasticKeywordField
  private String ansettelsesforholdKode;

  @ElasticTextField
  @ElasticKeywordField
  private String ansettelsesforholdKodeTekst;

  public EsAnsettelsesforholdJobbonsker() {}

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
    return Objects.equals(ansettelsesforholdKode, that.ansettelsesforholdKode)
        && Objects.equals(ansettelsesforholdKodeTekst, that.ansettelsesforholdKodeTekst);
  }

  @Override
  public int hashCode() {

    return Objects.hash(ansettelsesforholdKode, ansettelsesforholdKodeTekst);
  }

  @Override
  public String toString() {
    return "EsAnsettelsesforholdJobbonsker{" + "ansettelsesforholdKode='" + ansettelsesforholdKode
        + '\'' + ", ansettelsesforholdKodeTekst='" + ansettelsesforholdKodeTekst + '\'' + '}';
  }
}
