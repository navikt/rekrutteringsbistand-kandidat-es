package no.nav.arbeid.cv.indexer.domene;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.elasticsearch.mapping.annotations.ElasticKeywordField;
import no.nav.elasticsearch.mapping.annotations.ElasticTextField;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsHeltidDeltidJobbonsker {

  @ElasticKeywordField
  private String heltidDeltidKode;

  @ElasticTextField
  @ElasticKeywordField
  private String heltidDeltidKodeTekst;

  public EsHeltidDeltidJobbonsker() {}

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
    return Objects.equals(heltidDeltidKode, that.heltidDeltidKode)
        && Objects.equals(heltidDeltidKodeTekst, that.heltidDeltidKodeTekst);
  }

  @Override
  public int hashCode() {

    return Objects.hash(heltidDeltidKode, heltidDeltidKodeTekst);
  }

  @Override
  public String toString() {
    return "EsHeltidDeltidJobbonsker{" + "heltidDeltidKode='" + heltidDeltidKode + '\''
        + ", heltidDeltidKodeTekst='" + heltidDeltidKodeTekst + '\'' + '}';
  }
}
