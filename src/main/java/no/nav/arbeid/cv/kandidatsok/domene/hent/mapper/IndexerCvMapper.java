package no.nav.arbeid.cv.kandidatsok.domene.hent.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import no.nav.arbeid.cv.indexer.domene.EsAnsettelsesforholdJobbonsker;
import no.nav.arbeid.cv.indexer.domene.EsArbeidstidsordningJobbonsker;
import no.nav.arbeid.cv.indexer.domene.EsCv;
import no.nav.arbeid.cv.indexer.domene.EsGeografiJobbonsker;
import no.nav.arbeid.cv.indexer.domene.EsHeltidDeltidJobbonsker;
import no.nav.arbeid.cv.indexer.domene.EsKompetanse;
import no.nav.arbeid.cv.indexer.domene.EsKurs;
import no.nav.arbeid.cv.indexer.domene.EsSertifikat;
import no.nav.arbeid.cv.indexer.domene.EsUtdanning;
import no.nav.arbeid.cv.indexer.domene.EsVerv;
import no.nav.arbeid.cv.indexer.domene.EsYrkeJobbonsker;
import no.nav.arbeid.cv.indexer.domene.EsYrkeserfaring;
import no.nav.arbeid.cv.kandidatsok.domene.hent.Adresse;
import no.nav.arbeid.cv.kandidatsok.domene.hent.AnsfPersonProfilLedd;
import no.nav.arbeid.cv.kandidatsok.domene.hent.ArboPersonProfilLedd;
import no.nav.arbeid.cv.kandidatsok.domene.hent.GeoPersonProfilLedd;
import no.nav.arbeid.cv.kandidatsok.domene.hent.HedePersonProfilLedd;
import no.nav.arbeid.cv.kandidatsok.domene.hent.KompetanseCvLedd;
import no.nav.arbeid.cv.kandidatsok.domene.hent.KursCvLedd;
import no.nav.arbeid.cv.kandidatsok.domene.hent.Person;
import no.nav.arbeid.cv.kandidatsok.domene.hent.SertifikatCvLedd;
import no.nav.arbeid.cv.kandidatsok.domene.hent.UtdanningCvLedd;
import no.nav.arbeid.cv.kandidatsok.domene.hent.VervCvLedd;
import no.nav.arbeid.cv.kandidatsok.domene.hent.YrkeCvLedd;
import no.nav.arbeid.cv.kandidatsok.domene.hent.YrkePersonProfilLedd;

public class IndexerCvMapper {

  public Person map(EsCv cv) {
    return new Person(cv.getArenaPersonId(), mapLocalDate(cv.getFodselsdato()),
        cv.getFodselsnummer(), safeUnwrap(cv.getFodselsdatoErDnr()), cv.getFormidlingsgruppekode(),
        cv.getEtternavn(), cv.getFornavn(), cv.getStatsborgerskap(),
        mapLocalDate(cv.getSamtykkeDato()), cv.getSamtykkeStatus(),
        safeUnwrap(cv.getDisponererBil()), cv.getBeskrivelse(), cv.getEpostadresse(),
        mapAdresse(cv), cv.getArenaKandidatnr(), mapLocalDateTime(cv.getTidsstempel()),
        mapUtdanning(cv.getUtdanning()), mapYrkeserfaring(cv.getYrkeserfaring()),
        mapSertifikater(cv.getSertifikat()), mapKompetanse(cv.getKompetanse()),
        mapKurs(cv.getKurs()), mapVerv(cv.getVerv()), mapGeoJobbonsker(cv.getGeografiJobbonsker()),
        mapYrkeJobbonsker(cv.getYrkeJobbonsker()),
        mapHeltidDeltidJobbonsker(cv.getHeltidDeltidJobbonsker()),
        mapAnsettelsesforholdJobbonsker(cv.getAnsettelsesforholdJobbonsker()),
        mapArbeidstidsordningJobbonsker(cv.getArbeidstidsordningJobbonsker()));
  }

  private boolean safeUnwrap(Boolean bool) {
    return bool == null ? false : bool.booleanValue()
  }

  private LocalDate mapLocalDate(Date samtykkeDato) {
    // TODO Auto-generated method stub
    return null;
  }

  private LocalDateTime mapLocalDateTime(Date tidsstempel) {
    // TODO Auto-generated method stub
    return null;
  }

  private List<ArboPersonProfilLedd> mapArbeidstidsordningJobbonsker(
      List<EsArbeidstidsordningJobbonsker> arbeidstidsordningJobbonsker) {
    // TODO Auto-generated method stub
    return null;
  }

  private List<AnsfPersonProfilLedd> mapAnsettelsesforholdJobbonsker(
      List<EsAnsettelsesforholdJobbonsker> ansettelsesforholdJobbonsker) {
    // TODO Auto-generated method stub
    return null;
  }

  private List<HedePersonProfilLedd> mapHeltidDeltidJobbonsker(
      List<EsHeltidDeltidJobbonsker> heltidDeltidJobbonsker) {
    // TODO Auto-generated method stub
    return null;
  }

  private List<YrkePersonProfilLedd> mapYrkeJobbonsker(List<EsYrkeJobbonsker> yrkeJobbonsker) {
    // TODO Auto-generated method stub
    return null;
  }

  private List<GeoPersonProfilLedd> mapGeoJobbonsker(
      List<EsGeografiJobbonsker> geografiJobbonsker) {
    // TODO Auto-generated method stub
    return null;
  }

  private List<VervCvLedd> mapVerv(List<EsVerv> verv) {
    // TODO Auto-generated method stub
    return null;
  }

  private List<KursCvLedd> mapKurs(List<EsKurs> kurs) {
    // TODO Auto-generated method stub
    return null;
  }

  private List<KompetanseCvLedd> mapKompetanse(List<EsKompetanse> kompetanse) {
    // TODO Auto-generated method stub
    return null;
  }

  private List<SertifikatCvLedd> mapSertifikater(List<EsSertifikat> sertifikat) {
    // TODO Auto-generated method stub
    return null;
  }

  private List<YrkeCvLedd> mapYrkeserfaring(List<EsYrkeserfaring> yrkeserfaring) {
    // TODO Auto-generated method stub
    return null;
  }

  private List<UtdanningCvLedd> mapUtdanning(List<EsUtdanning> utdanning) {
    // TODO Auto-generated method stub
    return null;
  }

  private Adresse mapAdresse(EsCv cv) {
    // TODO Auto-generated method stub
    return null;
  }
}
