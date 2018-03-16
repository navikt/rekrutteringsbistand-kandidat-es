package no.nav.arbeid.cv.es.domene;

import java.util.Objects;

import org.frekele.elasticsearch.mapping.annotations.ElasticKeywordField;
import org.frekele.elasticsearch.mapping.annotations.ElasticTextField;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsSprak {

  @ElasticKeywordField
  private String sprakKode;

  @ElasticTextField
  @ElasticKeywordField
  private String sprakKodeTekst;

  @ElasticTextField
  private String muntlig;

  @ElasticTextField
  private String skriftlig;

  public EsSprak() {}

  public EsSprak(String sprakKode, String sprakKodeTekst, String muntlig, String skriftlig) {
    this.sprakKode = sprakKode;
    this.sprakKodeTekst = sprakKodeTekst;
    this.muntlig = muntlig;
    this.skriftlig = skriftlig;
  }

  public String getSprakKode() {
    return sprakKode;
  }

  public String getSprakKodeTekst() {
    return sprakKodeTekst;
  }

  public String getMuntlig() {
    return muntlig;
  }

  public String getSkriftlig() {
    return skriftlig;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EsSprak esSprak = (EsSprak) o;
    return Objects.equals(sprakKode, esSprak.sprakKode)
        && Objects.equals(sprakKodeTekst, esSprak.sprakKodeTekst)
        && Objects.equals(muntlig, esSprak.muntlig) && Objects.equals(skriftlig, esSprak.skriftlig);
  }

  @Override
  public int hashCode() {

    return Objects.hash(sprakKode, sprakKodeTekst, muntlig, skriftlig);
  }

  @Override
  public String toString() {
    return "EsSprak{" + "sprakKode='" + sprakKode + '\'' + ", sprakKodeTekst='" + sprakKodeTekst
        + '\'' + ", muntlig='" + muntlig + '\'' + ", skriftlig='" + skriftlig + '\'' + '}';
  }

}
