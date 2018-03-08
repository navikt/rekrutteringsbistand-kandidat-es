package no.nav.arbeid.cv.es.domene;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.util.Date;
import java.util.Objects;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

public class EsYrkeserfaring {

  @Field(type = FieldType.text, store = true, index = true)
  private Date fraDato;

  @Field(type = FieldType.text, store = true, index = true)
  private Date tilDato;

  @Field(type = FieldType.text, store = true, index = true)
  private String arbeidsgiver;

  @Field(type = FieldType.text, store = true, index = true)
  private String organisasjonsnummer;

  @Field(type = FieldType.text, store = true, index = true)
  private String stillingstittel;

  @Field(type = FieldType.text, store = true, index = true)
  private String beskrivelse;

  @Field(type = FieldType.text, store = true, index = true)
  private String sokekategori;

  @Field(type = FieldType.keyword, store = true, index = true)
  private String styrkKode;

  @Field(type = FieldType.text, store = true, index = true)
  private String styrkKodeTekst;

  @Field(type = FieldType.text, store = true, index = true)
  private String naceKode;

  public EsYrkeserfaring() {
  }

  public EsYrkeserfaring(Date fraDato, Date tilDato, String arbeidsgiver,
      String organisasjonsnummer, String stillingstittel, String beskrivelse,
      String sokekategori, String styrkKode, String styrkKodeTekst, String naceKode) {
    this.fraDato = fraDato;
    this.tilDato = tilDato;
    this.arbeidsgiver = arbeidsgiver;
    this.organisasjonsnummer = organisasjonsnummer;
    this.stillingstittel = stillingstittel;
    this.beskrivelse = beskrivelse;
    this.sokekategori = sokekategori;
    this.styrkKode = styrkKode;
    this.styrkKodeTekst = styrkKodeTekst;
    this.naceKode = naceKode;
  }

  public Date getFraDato() {
    return fraDato;
  }

  public Date getTilDato() {
    return tilDato;
  }

  public String getArbeidsgiver() {
    return arbeidsgiver;
  }

  public String getOrganisasjonsnummer() {
    return organisasjonsnummer;
  }

  public String getStillingstittel() {
    return stillingstittel;
  }

  public String getBeskrivelse() {
    return beskrivelse;
  }

  public String getSokekategori() {
    return sokekategori;
  }

  public String getStyrkKode() {
    return styrkKode;
  }

  public String getStyrkKodeTekst() {
    return styrkKodeTekst;
  }

  public String getNaceKode() {
    return naceKode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EsYrkeserfaring that = (EsYrkeserfaring) o;
    return Objects.equals(fraDato, that.fraDato) &&
        Objects.equals(tilDato, that.tilDato) &&
        Objects.equals(arbeidsgiver, that.arbeidsgiver) &&
        Objects.equals(organisasjonsnummer, that.organisasjonsnummer) &&
        Objects.equals(stillingstittel, that.stillingstittel) &&
        Objects.equals(beskrivelse, that.beskrivelse) &&
        Objects.equals(sokekategori, that.sokekategori) &&
        Objects.equals(styrkKode, that.styrkKode) &&
        Objects.equals(styrkKodeTekst, that.styrkKodeTekst) &&
        Objects.equals(naceKode, that.naceKode);
  }

  @Override
  public int hashCode() {

    return Objects
        .hash(fraDato, tilDato, arbeidsgiver, organisasjonsnummer, stillingstittel, beskrivelse,
            sokekategori, styrkKode, styrkKodeTekst, naceKode);
  }

  @Override
  public String toString() {
    return "EsYrkeserfaring{" +
        "fraDato=" + fraDato +
        ", tilDato=" + tilDato +
        ", arbeidsgiver='" + arbeidsgiver + '\'' +
        ", organisasjonsnummer='" + organisasjonsnummer + '\'' +
        ", stillingstittel='" + stillingstittel + '\'' +
        ", beskrivelse='" + beskrivelse + '\'' +
        ", sokekategori='" + sokekategori + '\'' +
        ", styrkKode='" + styrkKode + '\'' +
        ", styrkKodeTekst='" + styrkKodeTekst + '\'' +
        ", naceKode='" + naceKode + '\'' +
        '}';
  }

}
