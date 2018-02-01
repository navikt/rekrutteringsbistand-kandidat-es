package no.nav.arbeid.cv.es.domene;

import java.util.Optional;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonCreator;

public class EsUtdanning {

  @Field(type = FieldType.Integer, store = true, index = true)
  protected int cvleddnr;
  @Field(type = FieldType.text, store = true, index = true)
  protected String leddtekst1;
  @Field(type = FieldType.text, store = true, index = true)
  protected String leddtekst2;
  @Field(type = FieldType.Integer, store = true, index = true)
  protected Integer profilelementId;
  @Field(type = FieldType.keyword, store = true, index = true)
  protected String strukturkode;
  @Field(type = FieldType.text, store = true, index = true)
  protected String elementOrd;
  // protected String beskrivelse;

  public EsUtdanning() {
    
  }
  public EsUtdanning(int cvleddnr, String leddtekst1, String leddtekst2,
      Optional<Integer> profilelementId, Optional<String> strukturkode,
      Optional<String> elementOrd) {
    this.cvleddnr = cvleddnr;
    this.leddtekst1 = leddtekst1;
    this.leddtekst2 = leddtekst2;
    this.profilelementId = profilelementId.orElse(null);
    this.strukturkode = strukturkode.orElse(null);
    this.elementOrd = elementOrd.orElse(null);
  }

  public int getCvleddnr() {
    return cvleddnr;
  }

  public String getLeddtekst1() {
    return leddtekst1;
  }

  public String getLeddtekst2() {
    return leddtekst2;
  }

  public Integer getProfilelementId() {
    return profilelementId;
  }

  public String getStrukturkode() {
    return strukturkode;
  }

  public String getElementOrd() {
    return elementOrd;
  }

  @Override
  public String toString() {
    return "EsUtdanning [cvleddnr=" + cvleddnr + ", leddtekst1=" + leddtekst1 + ", leddtekst2="
        + leddtekst2 + ", profilelementId=" + profilelementId + ", strukturkode=" + strukturkode
        + ", elementOrd=" + elementOrd + "]";
  }

}
