package no.nav.arbeid.cv.es.domene;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import no.nav.elasticsearch.mapping.annotations.ElasticBooleanField;
import no.nav.elasticsearch.mapping.annotations.ElasticDateField;
import no.nav.elasticsearch.mapping.annotations.ElasticDocument;
import no.nav.elasticsearch.mapping.annotations.ElasticIntegerField;
import no.nav.elasticsearch.mapping.annotations.ElasticKeywordField;
import no.nav.elasticsearch.mapping.annotations.ElasticLongField;
import no.nav.elasticsearch.mapping.annotations.ElasticNestedField;
import no.nav.elasticsearch.mapping.annotations.ElasticTextField;

@JsonIgnoreProperties(ignoreUnknown = true)
@ElasticDocument
public class EsCv {

  // Define friktest to add norwegian analyzer
  // Kan fjernes hvis fritekst-sok ikke brukes
  @ElasticTextField(analyzer = "norwegian")
  private String fritekst;

  @ElasticTextField
  private String fodselsnummer;

  @ElasticTextField
  private String fornavn;

  @ElasticTextField
  private String etternavn;

  @ElasticDateField
  private Date fodselsdato;

  @ElasticBooleanField
  private Boolean fodselsdatoErDnr;

  @ElasticKeywordField
  private String formidlingsgruppekode;

  @ElasticKeywordField
  private String epostadresse;

  @ElasticKeywordField
  private String statsborgerskap;

  @ElasticLongField
  private Long arenaPersonId;

  @ElasticKeywordField
  private String arenaKandidatnr;

  @ElasticTextField
  private String beskrivelse;

  @ElasticKeywordField
  private String samtykkeStatus;

  @ElasticDateField
  private Date samtykkeDato;

  @ElasticTextField
  private String adresselinje1;

  @ElasticTextField
  private String adresselinje2;

  @ElasticTextField
  private String adresselinje3;

  @ElasticKeywordField
  private String postnummer;

  @ElasticKeywordField
  private String poststed;

  @ElasticKeywordField
  private String landkode;

  @ElasticLongField
  private Integer kommunenummer;

  @ElasticBooleanField
  private Boolean disponererBil;

  @ElasticDateField
  private Date tidsstempel;

  @ElasticNestedField
  private List<EsUtdanning> utdanning = new ArrayList<>();

  @ElasticNestedField
  private List<EsYrkeserfaring> yrkeserfaring = new ArrayList<>();

  @ElasticNestedField
  private List<EsKompetanse> kompetanse = new ArrayList<>();

  @ElasticNestedField
  private List<EsAnnenErfaring> annenerfaring = new ArrayList<>();

  @ElasticNestedField
  private List<EsSertifikat> sertifikat = new ArrayList<>();

  @ElasticNestedField
  private List<EsForerkort> forerkort = new ArrayList<>();

  @ElasticNestedField
  private List<EsSprak> sprak = new ArrayList<>();

  @ElasticNestedField
  private List<EsKurs> kurs = new ArrayList<>();

  @ElasticNestedField
  private List<EsVerv> verv = new ArrayList<>();

  @ElasticNestedField
  private List<EsGeografiJobbonsker> geografiJobbonsker = new ArrayList<>();

  private List<EsYrkeJobbonsker> yrkeJobbonsker = new ArrayList<>();

  private List<EsHeltidDeltidJobbonsker> heltidDeltidJobbonsker = new ArrayList<>();

  private List<EsAnsettelsesforholdJobbonsker> ansettelsesforholdJobbonsker = new ArrayList<>();

  private List<EsArbeidstidsordningJobbonsker> arbeidstidsordningJobbonsker = new ArrayList<>();

  @ElasticNestedField
  private List<EsSamletKompetanse> samletKompetanse = new ArrayList<>();

  @ElasticIntegerField
  private int totalLengdeYrkeserfaring;


  public EsCv() {

  }

  public EsCv(String fodselsnummer, String fornavn, String etternavn, Date fodselsdato,
      Boolean fodselsdatoErDnr, String formidlingsgruppekode, String epostadresse,
      String statsborgerskap, Long arenaPersonId, String arenaKandidatnr,
      String beskrivelse, String samtykkeStatus, Date samtykkeDato, String adresselinje1,
      String adresselinje2, String adresselinje3, String postnummer, String poststed,
      String landkode, Integer kommunenummer, Boolean disponererBil, Date tidsstempel) {
    this.fodselsnummer = fodselsnummer;
    this.fornavn = fornavn;
    this.etternavn = etternavn;
    this.fodselsdato = fodselsdato;
    this.fodselsdatoErDnr = fodselsdatoErDnr;
    this.formidlingsgruppekode = formidlingsgruppekode;
    this.epostadresse = epostadresse;
    this.statsborgerskap = statsborgerskap;
    this.arenaPersonId = arenaPersonId;
    this.arenaKandidatnr = arenaKandidatnr;
    this.beskrivelse = beskrivelse;
    this.samtykkeStatus = samtykkeStatus;
    this.samtykkeDato = samtykkeDato;
    this.adresselinje1 = adresselinje1;
    this.adresselinje2 = adresselinje2;
    this.adresselinje3 = adresselinje3;
    this.postnummer = postnummer;
    this.poststed = poststed;
    this.landkode = landkode;
    this.kommunenummer = kommunenummer;
    this.disponererBil = disponererBil;
    this.tidsstempel = tidsstempel;
  }

  // Adderfunksjoner

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
    yrkeserfaringListe.forEach(y -> this.totalLengdeYrkeserfaring += y.getYrkeserfaringManeder());
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

  public void addGeografiJobbonske(Collection<EsGeografiJobbonsker> geografiJobbonskerListe) {
    this.geografiJobbonsker.addAll(geografiJobbonskerListe);
  }

  public void addSamletKompetanse(Collection<EsSamletKompetanse> samletKompetanseListe) {
    this.samletKompetanse.addAll(samletKompetanseListe);
  }


  // gettere

  public String getFodselsnummer() {
    return fodselsnummer;
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

  public Boolean getFodselsdatoErDnr() {
    return fodselsdatoErDnr;
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

  public Long getArenaPersonId() {
    return arenaPersonId;
  }

  public String getArenaKandidatnr() {
    return arenaKandidatnr;
  }

  public String getBeskrivelse() {
    return beskrivelse;
  }

  public String getSamtykkeStatus() {
    return samtykkeStatus;
  }

  public Date getSamtykkeDato() {
    return samtykkeDato;
  }

  public String getAdresselinje1() {
    return adresselinje1;
  }

  public String getAdresselinje2() {
    return adresselinje2;
  }

  public String getAdresselinje3() {
    return adresselinje3;
  }

  public String getPostnummer() {
    return postnummer;
  }

  public String getPoststed() {
    return poststed;
  }

  public String getLandkode() {
    return landkode;
  }

  public Integer getKommunenummer() {
    return kommunenummer;
  }

  public Boolean getDisponererBil() {
    return disponererBil;
  }

  public Date getTidsstempel() {
    return tidsstempel;
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

  public List<EsSamletKompetanse> getSamletKompetanse() {
    return samletKompetanse;
  }

  public int getTotalLengdeYrkeserfaring() { return totalLengdeYrkeserfaring; }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EsCv esCv = (EsCv) o;
    return Objects.equals(fodselsnummer, esCv.fodselsnummer) &&
        Objects.equals(fornavn, esCv.fornavn) &&
        Objects.equals(etternavn, esCv.etternavn) &&
        Objects.equals(fodselsdato, esCv.fodselsdato) &&
        Objects.equals(fodselsdatoErDnr, esCv.fodselsdatoErDnr) &&
        Objects.equals(formidlingsgruppekode, esCv.formidlingsgruppekode) &&
        Objects.equals(epostadresse, esCv.epostadresse) &&
        Objects.equals(statsborgerskap, esCv.statsborgerskap) &&
        Objects.equals(arenaPersonId, esCv.arenaPersonId) &&
        Objects.equals(arenaKandidatnr, esCv.arenaKandidatnr) &&
        Objects.equals(beskrivelse, esCv.beskrivelse) &&
        Objects.equals(samtykkeStatus, esCv.samtykkeStatus) &&
        Objects.equals(samtykkeDato, esCv.samtykkeDato) &&
        Objects.equals(adresselinje1, esCv.adresselinje1) &&
        Objects.equals(adresselinje2, esCv.adresselinje2) &&
        Objects.equals(adresselinje3, esCv.adresselinje3) &&
        Objects.equals(postnummer, esCv.postnummer) &&
        Objects.equals(poststed, esCv.poststed) &&
        Objects.equals(landkode, esCv.landkode) &&
        Objects.equals(kommunenummer, esCv.kommunenummer) &&
        Objects.equals(disponererBil, esCv.disponererBil) &&
        Objects.equals(tidsstempel, esCv.tidsstempel) &&
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
        Objects.equals(arbeidstidsordningJobbonsker, esCv.arbeidstidsordningJobbonsker) &&
        Objects.equals(samletKompetanse, esCv.samletKompetanse) &&
        Objects.equals(totalLengdeYrkeserfaring, esCv.totalLengdeYrkeserfaring);
  }

  @Override
  public int hashCode() {

    return Objects
        .hash(fodselsnummer, fornavn, etternavn, fodselsdato, fodselsdatoErDnr,
            formidlingsgruppekode,
            epostadresse, statsborgerskap, arenaPersonId, arenaKandidatnr, beskrivelse,
            samtykkeStatus, samtykkeDato, adresselinje1, adresselinje2, adresselinje3, postnummer,
            poststed, landkode, kommunenummer, disponererBil, tidsstempel, utdanning, yrkeserfaring,
            kompetanse, annenerfaring, sertifikat, forerkort, sprak, kurs, verv, geografiJobbonsker,
            yrkeJobbonsker, heltidDeltidJobbonsker, ansettelsesforholdJobbonsker,
            arbeidstidsordningJobbonsker, samletKompetanse, totalLengdeYrkeserfaring);
  }

  @Override
  public String toString() {
    return "EsCv{" +
        "fodselsnummer='" + fodselsnummer + '\'' +
        ", fornavn='" + fornavn + '\'' +
        ", etternavn='" + etternavn + '\'' +
        ", fodselsdato=" + fodselsdato +
        ", fodselsdatoErDnr=" + fodselsdatoErDnr +
        ", formidlingsgruppekode='" + formidlingsgruppekode + '\'' +
        ", epostadresse='" + epostadresse + '\'' +
        ", statsborgerskap='" + statsborgerskap + '\'' +
        ", arenaPersonId=" + arenaPersonId +
        ", arenaKandidatnr='" + arenaKandidatnr + '\'' +
        ", beskrivelse='" + beskrivelse + '\'' +
        ", samtykkeStatus='" + samtykkeStatus + '\'' +
        ", samtykkeDato=" + samtykkeDato +
        ", adresselinje1='" + adresselinje1 + '\'' +
        ", adresselinje2='" + adresselinje2 + '\'' +
        ", adresselinje3='" + adresselinje3 + '\'' +
        ", postnummer='" + postnummer + '\'' +
        ", poststed='" + poststed + '\'' +
        ", landkode='" + landkode + '\'' +
        ", kommunenummer=" + kommunenummer +
        ", disponererBil=" + disponererBil +
        ", tidsstempel=" + tidsstempel +
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
        ", samletKompetanse=" + samletKompetanse +
        ", totalLengdeYrkeserfaring=" + totalLengdeYrkeserfaring +
        '}';
  }

}
