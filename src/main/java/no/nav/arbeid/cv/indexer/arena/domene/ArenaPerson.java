package no.nav.arbeid.cv.indexer.arena.domene;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ArenaPerson implements Serializable {

  private static final long serialVersionUID = -3355634825867252499L;

  private Long personId;

  private LocalDate fodselsdato;

  private String fodselsnummer;

  private boolean erFodselsnummerDnr;

  private String formidlingsgruppekode;

  private String etternavn;

  private String fornavn;

  private String statsborgerskap;

  private LocalDate samtykkeDato;

  /**
   * G Samtykke/Presenteres med navn Ja både nav.no og Arena J Samtykke/Presenteres med navn Ja
   * Arena, Nei Aetat.no B Samtykke/Presenteres med navn Ja Aetat.no, Nei Arena N
   * Samtykke/Presenteres med navn Nei
   */
  private String samtykkeStatus;

  private boolean disponererBil;

  private String beskrivelse;

  private String epost;

  private String mobiltelefon;

  private String telefon;

  private ArenaAdresse adresse;

  private String kandidatnummer;

  private LocalDateTime sistEndret;

  private List<ArenaUtdanningCvLedd> utdanning = new ArrayList<>();

  private List<ArenaYrkeCvLedd> yrkeserfaring = new ArrayList<>();;

  private List<ArenaSertifikatCvLedd> sertifikater = new ArrayList<>();

  private List<ArenaKompetanseCvLedd> kompetanse = new ArrayList<>();

  private List<ArenaKursCvLedd> kurs = new ArrayList<>();

  private List<ArenaVervCvLedd> verv = new ArrayList<>();

  private List<ArenaGeoPersonProfilLedd> geografiJobbonsker = new ArrayList<>();

  private List<ArenaYrkePersonProfilLedd> yrkeJobbonsker = new ArrayList<>();

  private List<ArenaHedePersonProfilLedd> heltidDeltidJobbonsker = new ArrayList<>();

  private List<ArenaAnsfPersonProfilLedd> ansettelsesforholdJobbonsker = new ArrayList<>();

  private List<ArenaArboPersonProfilLedd> arbeidstidsordningJobbonsker = new ArrayList<>();

  private ArenaPerson() {}


  public Long getPersonId() {
    return personId;
  }

  public void setPersonId(Long personId) {
    this.personId = personId;
  }

  public LocalDate getFodselsdato() {
    return fodselsdato;
  }

  public void setFodselsdato(LocalDate fodselsdato) {
    this.fodselsdato = fodselsdato;
  }

  public String getFodselsnummer() {
    return fodselsnummer;
  }

  public void setFodselsnummer(String fodselsnummer) {
    this.fodselsnummer = fodselsnummer;
  }

  public String getEtternavn() {
    return etternavn;
  }

  public void setEtternavn(String etternavn) {
    this.etternavn = etternavn;
  }

  public String getFornavn() {
    return fornavn;
  }

  public void setFornavn(String fornavn) {
    this.fornavn = fornavn;
  }

  public String getFormidlingsgruppekode() {
    return formidlingsgruppekode;
  }

  public void setFormidlingsgruppekode(String formidlingsgruppekode) {
    this.formidlingsgruppekode = formidlingsgruppekode;
  }

  public String getStatsborgerskap() {
    return statsborgerskap;
  }

  public void setStatsborgerskap(String statsborgerskap) {
    this.statsborgerskap = statsborgerskap;
  }

  public LocalDate getSamtykkeDato() {
    return samtykkeDato;
  }

  public void setSamtykkeDato(LocalDate samtykkeDato) {
    this.samtykkeDato = samtykkeDato;
  }

  public String getSamtykkeStatus() {
    return samtykkeStatus;
  }

  public void setSamtykkeStatus(String samtykkeStatus) {
    this.samtykkeStatus = samtykkeStatus;
  }

  public String getBeskrivelse() {
    return beskrivelse;
  }

  public void setBeskrivelse(String beskrivelse) {
    this.beskrivelse = beskrivelse;
  }

  public String getKandidatnummer() {
    return kandidatnummer;
  }

  public void setKandidatnummer(String kandidatnummer) {
    this.kandidatnummer = kandidatnummer;
  }

  public boolean isErFodselsnummerDnr() {
    return erFodselsnummerDnr;
  }

  public void setErFodselsnummerDnr(boolean erFodselsnummerDnr) {
    this.erFodselsnummerDnr = erFodselsnummerDnr;
  }

  public boolean isDisponererBil() {
    return disponererBil;
  }

  public void setDisponererBil(boolean disponererBil) {
    this.disponererBil = disponererBil;
  }

  public LocalDateTime getSistEndret() {
    return sistEndret;
  }

  public void setSistEndret(LocalDateTime sistEndret) {
    this.sistEndret = sistEndret;
  }

  public ArenaAdresse getAdresse() {
    return adresse;
  }

  public void setAdresse(ArenaAdresse adresse) {
    this.adresse = adresse;
  }

  public String getEpost() {
    return epost;
  }

  public void setEpost(String epost) {
    this.epost = epost;
  }

  public String getMobiltelefon() {
    return mobiltelefon;
  }

  public void setMobiltelefon(String mobiltelefon) {
    this.mobiltelefon = mobiltelefon;
  }

  public String getTelefon() {
    return telefon;
  }

  public void setTelefon(String telefon) {
    this.telefon = telefon;
  }

  public List<ArenaUtdanningCvLedd> getUtdanning() {
    return utdanning;
  }

  public void setUtdanning(List<ArenaUtdanningCvLedd> utdanning) {
    this.utdanning = utdanning;
  }

  public List<ArenaYrkeCvLedd> getYrkeserfaring() {
    return yrkeserfaring;
  }

  public void setYrkeserfaring(List<ArenaYrkeCvLedd> yrkeserfaring) {
    this.yrkeserfaring = yrkeserfaring;
  }

  public List<ArenaSertifikatCvLedd> getSertifikater() {
    return sertifikater;
  }

  public void setSertifikater(List<ArenaSertifikatCvLedd> sertifikater) {
    this.sertifikater = sertifikater;
  }

  public List<ArenaKompetanseCvLedd> getKompetanse() {
    return kompetanse;
  }

  public void setKompetanse(List<ArenaKompetanseCvLedd> kompetanse) {
    this.kompetanse = kompetanse;
  }

  public List<ArenaKursCvLedd> getKurs() {
    return kurs;
  }

  public void setKurs(List<ArenaKursCvLedd> kurs) {
    this.kurs = kurs;
  }

  public List<ArenaVervCvLedd> getVerv() {
    return verv;
  }

  public void setVerv(List<ArenaVervCvLedd> verv) {
    this.verv = verv;
  }

  public List<ArenaGeoPersonProfilLedd> getGeografiJobbonsker() {
    return geografiJobbonsker;
  }

  public void setGeografiJobbonsker(List<ArenaGeoPersonProfilLedd> geografiJobbonsker) {
    this.geografiJobbonsker = geografiJobbonsker;
  }

  public List<ArenaYrkePersonProfilLedd> getYrkeJobbonsker() {
    return yrkeJobbonsker;
  }

  public void setYrkeJobbonsker(List<ArenaYrkePersonProfilLedd> yrkeJobbonsker) {
    this.yrkeJobbonsker = yrkeJobbonsker;
  }

  public List<ArenaHedePersonProfilLedd> getHeltidDeltidJobbonsker() {
    return heltidDeltidJobbonsker;
  }

  public void setHeltidDeltidJobbonsker(List<ArenaHedePersonProfilLedd> heltidDeltidJobbonsker) {
    this.heltidDeltidJobbonsker = heltidDeltidJobbonsker;
  }

  public List<ArenaAnsfPersonProfilLedd> getAnsettelsesforholdJobbonsker() {
    return ansettelsesforholdJobbonsker;
  }

  public void setAnsettelsesforholdJobbonsker(
      List<ArenaAnsfPersonProfilLedd> ansettelsesforholdJobbonsker) {
    this.ansettelsesforholdJobbonsker = ansettelsesforholdJobbonsker;
  }

  public List<ArenaArboPersonProfilLedd> getArbeidstidsordningJobbonsker() {
    return arbeidstidsordningJobbonsker;
  }

  public void setArbeidstidsordningJobbonsker(
      List<ArenaArboPersonProfilLedd> arbeidstidsordningJobbonsker) {
    this.arbeidstidsordningJobbonsker = arbeidstidsordningJobbonsker;
  }


}


