package no.nav.arbeid.cv.es.domene;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "cver", type = "cv")
public class EsCv {

  @Id
  private Long personId;
  @Field(type = FieldType.Date, store = true, index = true)
  private Date fodselsdato;
  @Field(type = FieldType.text, store = true, index = true)
  private String fodselsnummer;
  @Field(type = FieldType.text, store = true, index = true)
  private String etternavn;
  @Field(type = FieldType.text, store = true, index = true)
  private String fornavn;
  @Field(type = FieldType.keyword, store = true, index = true)
  private String formidlingsgruppekode;
  @Field(type = FieldType.text, store = true, index = true)
  private String epostadresse;
  @Field(type = FieldType.text, store = true, index = true)
  private String beskrivelse;
  @Field(type = FieldType.Date, store = true, index = true)
  private Date regDato;
  @Field(type = FieldType.Date, store = true, index = true)
  private Date modDato;

  @Field(type = FieldType.Nested, store = true, index = true)
  private List<EsUtdanning> utdanning = new ArrayList<>();
  @Field(type = FieldType.Nested, store = true, index = true)
  private List<EsYrke> yrker = new ArrayList<>();

  public EsCv() {

  }


  public EsCv(Long personId, Date fodselsdato, String fodselsnummer, String etternavn,
      String fornavn, String formidlingsgruppekode, String epostadresse, String beskrivelse,
      Date regDato, Date modDato) {
    this.personId = personId;
    this.fodselsdato = fodselsdato;
    this.fodselsnummer = fodselsnummer;
    this.etternavn = etternavn;
    this.fornavn = fornavn;
    this.formidlingsgruppekode = formidlingsgruppekode;
    this.epostadresse = epostadresse;
    this.beskrivelse = beskrivelse;
    this.regDato = regDato;
    this.modDato = modDato;
  }

  public Long getPersonId() {
    return personId;
  }

  public Date getFodselsdato() {
    return fodselsdato;
  }

  public String getFodselsnummer() {
    return fodselsnummer;
  }

  public String getEtternavn() {
    return etternavn;
  }

  public String getFornavn() {
    return fornavn;
  }

  public String getFormidlingsgruppekode() {
    return formidlingsgruppekode;
  }

  public String getEpostadresse() {
    return epostadresse;
  }

  public String getBeskrivelse() {
    return beskrivelse;
  }

  public Date getRegDato() {
    return regDato;
  }

  public Date getModDato() {
    return modDato;
  }

  public List<EsUtdanning> getUtdanning() {
    return Collections.unmodifiableList(utdanning);
  }

  public List<EsYrke> getYrker() {
    return Collections.unmodifiableList(yrker);
  }

  public void addUtdanning(EsUtdanning utdanning) {
    this.utdanning.add(utdanning);
  }

  public void addUtdanninger(Collection<EsUtdanning> utdanninger) {
    this.utdanning.addAll(utdanninger);
  }

  public void addYrke(EsYrke yrke) {
    this.yrker.add(yrke);
  }

  public void addYrker(Collection<EsYrke> yrker) {
    this.yrker.addAll(yrker);
  }

  @Override
  public String toString() {
    return "EsCv [personId=" + personId + ", fodselsdato=" + fodselsdato + ", fodselsnummer="
        + fodselsnummer + ", etternavn=" + etternavn + ", fornavn=" + fornavn
        + ", formidlingsgruppekode=" + formidlingsgruppekode + ", epostadresse=" + epostadresse
        + ", beskrivelse=" + beskrivelse + ", regDato=" + regDato + ", modDato=" + modDato
        + ", utdanning=" + utdanning + ", yrker=" + yrker + "]\n\n";
  }



}
