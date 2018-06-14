package no.nav.arbeid.cv.kandidatsok.domene.hent;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Person implements Serializable {

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

  private Adresse adresse;

  private String kandidatnummer;

  private LocalDateTime sistEndret;

  private List<UtdanningCvLedd> utdanning = new ArrayList<>();

  private List<YrkeCvLedd> yrkeserfaring = new ArrayList<>();;

  private List<SertifikatCvLedd> sertifikater = new ArrayList<>();

  private List<KompetanseCvLedd> kompetanse = new ArrayList<>();

  private List<KursCvLedd> kurs = new ArrayList<>();

  private List<VervCvLedd> verv = new ArrayList<>();

  private List<GeoPersonProfilLedd> geografiJobbonsker = new ArrayList<>();

  private List<YrkePersonProfilLedd> yrkeJobbonsker = new ArrayList<>();

  private List<HedePersonProfilLedd> heltidDeltidJobbonsker = new ArrayList<>();

  private List<AnsfPersonProfilLedd> ansettelsesforholdJobbonsker = new ArrayList<>();

  private List<ArboPersonProfilLedd> arbeidstidsordningJobbonsker = new ArrayList<>();

  public Person() {}

  public Person(Long personId, LocalDate fodselsdato, String fodselsnummer,
      boolean erFodselsnummerDnr, String formidlingsgruppekode, String etternavn, String fornavn,
      String statsborgerskap, LocalDate samtykkeDato, String samtykkeStatus, boolean disponererBil,
      String beskrivelse, String epost, Adresse adresse, String kandidatnummer,
      LocalDateTime sistEndret, List<UtdanningCvLedd> utdanning, List<YrkeCvLedd> yrkeserfaring,
      List<SertifikatCvLedd> sertifikater, List<KompetanseCvLedd> kompetanse, List<KursCvLedd> kurs,
      List<VervCvLedd> verv, List<GeoPersonProfilLedd> geografiJobbonsker,
      List<YrkePersonProfilLedd> yrkeJobbonsker, List<HedePersonProfilLedd> heltidDeltidJobbonsker,
      List<AnsfPersonProfilLedd> ansettelsesforholdJobbonsker,
      List<ArboPersonProfilLedd> arbeidstidsordningJobbonsker) {
    super();
    this.personId = personId;
    this.fodselsdato = fodselsdato;
    this.fodselsnummer = fodselsnummer;
    this.erFodselsnummerDnr = erFodselsnummerDnr;
    this.formidlingsgruppekode = formidlingsgruppekode;
    this.etternavn = etternavn;
    this.fornavn = fornavn;
    this.statsborgerskap = statsborgerskap;
    this.samtykkeDato = samtykkeDato;
    this.samtykkeStatus = samtykkeStatus;
    this.disponererBil = disponererBil;
    this.beskrivelse = beskrivelse;
    this.epost = epost;
    this.adresse = adresse;
    this.kandidatnummer = kandidatnummer;
    this.sistEndret = sistEndret;
    this.utdanning = utdanning;
    this.yrkeserfaring = yrkeserfaring;
    this.sertifikater = sertifikater;
    this.kompetanse = kompetanse;
    this.kurs = kurs;
    this.verv = verv;
    this.geografiJobbonsker = geografiJobbonsker;
    this.yrkeJobbonsker = yrkeJobbonsker;
    this.heltidDeltidJobbonsker = heltidDeltidJobbonsker;
    this.ansettelsesforholdJobbonsker = ansettelsesforholdJobbonsker;
    this.arbeidstidsordningJobbonsker = arbeidstidsordningJobbonsker;
  }

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

  public Adresse getAdresse() {
    return adresse;
  }

  public void setAdresse(Adresse adresse) {
    this.adresse = adresse;
  }

  public String getEpost() {
    return epost;
  }

  public void setEpost(String epost) {
    this.epost = epost;
  }

  public List<UtdanningCvLedd> getUtdanning() {
    return utdanning;
  }

  public void setUtdanning(List<UtdanningCvLedd> utdanning) {
    this.utdanning = utdanning;
  }

  public List<YrkeCvLedd> getYrkeserfaring() {
    return yrkeserfaring;
  }

  public void setYrkeserfaring(List<YrkeCvLedd> yrkeserfaring) {
    this.yrkeserfaring = yrkeserfaring;
  }

  public List<SertifikatCvLedd> getSertifikater() {
    return sertifikater;
  }

  public void setSertifikater(List<SertifikatCvLedd> sertifikater) {
    this.sertifikater = sertifikater;
  }

  public List<KompetanseCvLedd> getKompetanse() {
    return kompetanse;
  }

  public void setKompetanse(List<KompetanseCvLedd> kompetanse) {
    this.kompetanse = kompetanse;
  }

  public List<KursCvLedd> getKurs() {
    return kurs;
  }

  public void setKurs(List<KursCvLedd> kurs) {
    this.kurs = kurs;
  }

  public List<VervCvLedd> getVerv() {
    return verv;
  }

  public void setVerv(List<VervCvLedd> verv) {
    this.verv = verv;
  }

  public List<GeoPersonProfilLedd> getGeografiJobbonsker() {
    return geografiJobbonsker;
  }

  public void setGeografiJobbonsker(List<GeoPersonProfilLedd> geografiJobbonsker) {
    this.geografiJobbonsker = geografiJobbonsker;
  }

  public List<YrkePersonProfilLedd> getYrkeJobbonsker() {
    return yrkeJobbonsker;
  }

  public void setYrkeJobbonsker(List<YrkePersonProfilLedd> yrkeJobbonsker) {
    this.yrkeJobbonsker = yrkeJobbonsker;
  }

  public List<HedePersonProfilLedd> getHeltidDeltidJobbonsker() {
    return heltidDeltidJobbonsker;
  }

  public void setHeltidDeltidJobbonsker(List<HedePersonProfilLedd> heltidDeltidJobbonsker) {
    this.heltidDeltidJobbonsker = heltidDeltidJobbonsker;
  }

  public List<AnsfPersonProfilLedd> getAnsettelsesforholdJobbonsker() {
    return ansettelsesforholdJobbonsker;
  }

  public void setAnsettelsesforholdJobbonsker(
      List<AnsfPersonProfilLedd> ansettelsesforholdJobbonsker) {
    this.ansettelsesforholdJobbonsker = ansettelsesforholdJobbonsker;
  }

  public List<ArboPersonProfilLedd> getArbeidstidsordningJobbonsker() {
    return arbeidstidsordningJobbonsker;
  }

  public void setArbeidstidsordningJobbonsker(
      List<ArboPersonProfilLedd> arbeidstidsordningJobbonsker) {
    this.arbeidstidsordningJobbonsker = arbeidstidsordningJobbonsker;
  }

}


