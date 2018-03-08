package no.nav.arbeid.cv.es.domene;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "cver", type = "cv")
public class EsCv {

  @Id
  private Long personId;

  @Field(type = FieldType.text, store = true, index = true)
  private String fornavn;

  @Field(type = FieldType.text, store = true, index = true)
  private String etternavn;

  @Field(type = FieldType.Date, store = true, index = true)
  private Date fodselsdato;

  @Field(type = FieldType.keyword, store = true, index = true)
  private String formidlingsgruppekode;

  @Field(type = FieldType.text, store = true, index = true)
  private String epostadresse;

  @Field(type = FieldType.text, store = true, index = true)
  private String statsborgerskap;

  @Field(type = FieldType.Long, store = true, index =true)
  private Long arenaId;

  @Field(type = FieldType.text, store = true, index = true)
  private String beskrivelse;


  @Field(type = FieldType.Nested, store = true, index = true)
  private List<EsUtdanning> utdanning = new ArrayList<>();

  @Field(type = FieldType.Nested, store = true, index = true)
  private List<EsYrkeserfaring> yrkeserfaring = new ArrayList<>();

  @Field(type = FieldType.Nested, store = true, index = true)
  private List<EsKompetanse> kompetanse = new ArrayList<>();

  @Field(type = FieldType.Nested, store = true, index = true)
  private List<EsAnnenErfaring> annenerfaring = new ArrayList<>();

  @Field(type = FieldType.Nested, store = true, index = true)
  private List<EsSertifikat> sertifikat = new ArrayList<>();

  @Field(type = FieldType.Nested, store = true, index = true)
  private List<EsForerkort> forerkort = new ArrayList<>();

  @Field(type = FieldType.Nested, store = true, index = true)
  private List<EsSprak> sprak = new ArrayList<>();

  @Field(type = FieldType.Nested, store = true, index = true)
  private List<EsKurs> kurs = new ArrayList<>();

  @Field(type = FieldType.Nested, store = true, index = true)
  private List<EsVerv> verv = new ArrayList<>();

  @Field(type = FieldType.Nested, store = true, index = true)
  private List<EsGeografiJobbonsker> geografiJobbonsker = new ArrayList<>();

  @Field(type = FieldType.Nested, store = true, index = true)
  private List<EsYrkeJobbonsker> yrkeJobbonsker = new ArrayList<>();

  @Field(type = FieldType.Nested, store = true, index = true)
  private List<EsHeltidDeltidJobbonsker> heltidDeltidJobbonsker = new ArrayList<>();

  @Field(type = FieldType.Nested, store = true, index = true)
  private List<EsAnsettelsesforholdJobbonsker> ansettelsesforholdJobbonsker = new ArrayList<>();

  @Field(type = FieldType.Nested, store = true, index = true)
  private List<EsArbeidstidsordningJobbonsker> arbeidstidsordningJobbonsker = new ArrayList<>();

  public EsCv() {

  }

  public EsCv(Long personId, String fornavn, String etternavn, Date fodselsdato,
      String formidlingsgruppekode, String epostadresse, String statsborgerskap,
      Long arenaId, String beskrivelse) {
    this.personId = personId;
    this.fornavn = fornavn;
  this.etternavn = etternavn;
  this.fodselsdato = fodselsdato;
  this.formidlingsgruppekode = formidlingsgruppekode;
  this.epostadresse = epostadresse;
  this.statsborgerskap = statsborgerskap;
  this.arenaId = arenaId;
  this.beskrivelse = beskrivelse;
}

  //Adderfunksjoner

  public void addUtdanning(EsUtdanning utdanning) {
    this.utdanning.add(utdanning);
  }

  public void addUtdanning(Collection<EsUtdanning> utdanningListe) {
    this.utdanning.addAll(utdanningListe);
  }

  public void addYrkeserfaring(EsYrkeserfaring yrkeserfaring) {
    this.yrkeserfaring.add(yrkeserfaring);
  }

  public void addYrkeserfaring(Collection<EsYrkeserfaring> yrkeserfaringListe) {
    this.yrkeserfaring.addAll(yrkeserfaringListe);
  }

  public void addKompetanse(EsKompetanse kompetanse) {
    this.kompetanse.add(kompetanse);
  }

  public void addKompetanse(Collection<EsKompetanse> kompetanseListe) {
    this.kompetanse.addAll(kompetanseListe);
  }

  public void addAnnenErfaring(EsAnnenErfaring annenErfaring) {
    this.annenerfaring.add(annenErfaring);
  }

  public void addAnnenErfaring(Collection<EsAnnenErfaring> annenErfaringListe) {
    this.annenerfaring.addAll(annenErfaringListe);
  }

  public void addSertifikat(EsSertifikat sertifikat) {
    this.sertifikat.add(sertifikat);
  }

  public void addSertifikat(Collection<EsSertifikat> sertifikatListe) {
    this.sertifikat.addAll(sertifikatListe);
  }

  public void addForerkort(EsForerkort forerkort) {
    this.forerkort.add(forerkort);
  }

  public void addForerkort(Collection<EsForerkort> forerkortListe) {
    this.forerkort.addAll(forerkortListe);
  }

  public void addSprak(EsSprak sprak) {
    this.sprak.add(sprak);
  }

  public void addSprak(Collection<EsSprak> sprakListe) {
    this.sprak.addAll(sprakListe);
  }

  public void addKurs(EsKurs kurs) {
    this.kurs.add(kurs);
  }

  public void addKurs(Collection<EsKurs> KursListe) {
    this.kurs.addAll(KursListe);
  }

  public void addVerv(EsVerv verv) {
    this.verv.add(verv);
  }

  public void addVerv(Collection<EsVerv> vervListe) {
    this.verv.addAll(vervListe);
  }


  //gettere
  public Long getPersonId() {
    return personId;
  }

  public String getFornavn() {
    return fornavn;
  }

  public String getEtternavn() {
    return etternavn;
  }

  public Date getFodselsdato() {
    return fodselsdato;
  }

  public String getFormidlingsgruppekode() {
    return formidlingsgruppekode;
  }

  public String getEpostadresse() {
    return epostadresse;
  }

  public String getStatsborgerskap() {
    return statsborgerskap;
  }

  public Long getArenaId() {
    return arenaId;
  }

  public String getBeskrivelse() {
    return beskrivelse;
  }

  public List<EsUtdanning> getUtdanning() {
    return utdanning;
  }

  public List<EsYrkeserfaring> getYrkeserfaring() {
    return yrkeserfaring;
  }

  public List<EsKompetanse> getKompetanse() {
    return kompetanse;
  }

  public List<EsAnnenErfaring> getAnnenerfaring() {
    return annenerfaring;
  }

  public List<EsSertifikat> getSertifikat() {
    return sertifikat;
  }

  public List<EsForerkort> getForerkort() {
    return forerkort;
  }

  public List<EsSprak> getSprak() {
    return sprak;
  }

  public List<EsKurs> getKurs() {
    return kurs;
  }

  public List<EsVerv> getVerv() {
    return verv;
  }

  public List<EsGeografiJobbonsker> getGeografiJobbonsker() {
    return geografiJobbonsker;
  }

  public List<EsYrkeJobbonsker> getYrkeJobbonsker() {
    return yrkeJobbonsker;
  }

  public List<EsHeltidDeltidJobbonsker> getHeltidDeltidJobbonsker() {
    return heltidDeltidJobbonsker;
  }

  public List<EsAnsettelsesforholdJobbonsker> getAnsettelsesforholdJobbonsker() {
    return ansettelsesforholdJobbonsker;
  }

  public List<EsArbeidstidsordningJobbonsker> getArbeidstidsordningJobbonsker() {
    return arbeidstidsordningJobbonsker;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EsCv esCv = (EsCv) o;
    return Objects.equals(fornavn, esCv.fornavn) &&
        Objects.equals(etternavn, esCv.etternavn) &&
        Objects.equals(fodselsdato, esCv.fodselsdato) &&
        Objects.equals(formidlingsgruppekode, esCv.formidlingsgruppekode) &&
        Objects.equals(epostadresse, esCv.epostadresse) &&
        Objects.equals(statsborgerskap, esCv.statsborgerskap) &&
        Objects.equals(arenaId, esCv.arenaId) &&
        Objects.equals(beskrivelse, esCv.beskrivelse) &&
        Objects.equals(utdanning, esCv.utdanning) &&
        Objects.equals(yrkeserfaring, esCv.yrkeserfaring) &&
        Objects.equals(kompetanse, esCv.kompetanse) &&
        Objects.equals(annenerfaring, esCv.annenerfaring) &&
        Objects.equals(sertifikat, esCv.sertifikat) &&
        Objects.equals(forerkort, esCv.forerkort) &&
        Objects.equals(sprak, esCv.sprak) &&
        Objects.equals(kurs, esCv.kurs) &&
        Objects.equals(verv, esCv.verv) &&
        Objects.equals(geografiJobbonsker, esCv.geografiJobbonsker) &&
        Objects.equals(yrkeJobbonsker, esCv.yrkeJobbonsker) &&
        Objects.equals(heltidDeltidJobbonsker, esCv.heltidDeltidJobbonsker) &&
        Objects.equals(ansettelsesforholdJobbonsker, esCv.ansettelsesforholdJobbonsker) &&
        Objects.equals(arbeidstidsordningJobbonsker, esCv.arbeidstidsordningJobbonsker);
  }

  @Override
  public int hashCode() {

    return Objects
        .hash(fornavn, etternavn, fodselsdato, formidlingsgruppekode, epostadresse, statsborgerskap,
            arenaId, beskrivelse, utdanning, yrkeserfaring, kompetanse, annenerfaring, sertifikat,
            forerkort, sprak, kurs, verv, geografiJobbonsker, yrkeJobbonsker,
            heltidDeltidJobbonsker,
            ansettelsesforholdJobbonsker, arbeidstidsordningJobbonsker);
  }


  @Override
  public String toString() {
    return "EsCv{" +
        "fornavn='" + fornavn + '\'' +
        ", etternavn='" + etternavn + '\'' +
        ", fodselsdato=" + fodselsdato +
        ", formidlingsgruppekode='" + formidlingsgruppekode + '\'' +
        ", epostadresse='" + epostadresse + '\'' +
        ", statsborgerskap='" + statsborgerskap + '\'' +
        ", arenaId=" + arenaId +
        ", beskrivelse='" + beskrivelse + '\'' +
        ", utdanning=" + utdanning +
        ", yrkeserfaring=" + yrkeserfaring +
        ", kompetanse=" + kompetanse +
        ", annenerfaring=" + annenerfaring +
        ", sertifikat=" + sertifikat +
        ", forerkort=" + forerkort +
        ", sprak=" + sprak +
        ", kurs=" + kurs +
        ", verv=" + verv +
        ", geografiJobbonsker=" + geografiJobbonsker +
        ", yrkeJobbonsker=" + yrkeJobbonsker +
        ", heltidDeltidJobbonsker=" + heltidDeltidJobbonsker +
        ", ansettelsesforholdJobbonsker=" + ansettelsesforholdJobbonsker +
        ", arbeidstidsordningJobbonsker=" + arbeidstidsordningJobbonsker +
        '}';
  }
}
