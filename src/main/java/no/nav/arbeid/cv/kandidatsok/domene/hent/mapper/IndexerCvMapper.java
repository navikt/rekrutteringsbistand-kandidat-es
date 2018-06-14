package no.nav.arbeid.cv.kandidatsok.domene.hent.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
import no.nav.arbeid.cv.kandidatsok.domene.hent.KursCvLedd.Omfang;
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
    return bool == null ? false : bool.booleanValue();
  }

  private LocalDate mapLocalDate(Date date) {
    return date == null ? null : date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
  }


  private LocalDateTime mapLocalDateTime(Date date) {
    return date == null ? null : date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
  }

  private List<ArboPersonProfilLedd> mapArbeidstidsordningJobbonsker(
      List<EsArbeidstidsordningJobbonsker> liste) {
    return liste == null ? null
        : liste.parallelStream().map(element -> map(element)).collect(Collectors.toList());
  }

  private ArboPersonProfilLedd map(EsArbeidstidsordningJobbonsker element) {
    return new ArboPersonProfilLedd(element.getArbeidstidsordningKode(),
        element.getArbeidstidsordningKodeTekst());
  }

  private List<AnsfPersonProfilLedd> mapAnsettelsesforholdJobbonsker(
      List<EsAnsettelsesforholdJobbonsker> liste) {
    return liste == null ? null
        : liste.parallelStream().map(element -> map(element)).collect(Collectors.toList());
  }

  private AnsfPersonProfilLedd map(EsAnsettelsesforholdJobbonsker element) {
    return new AnsfPersonProfilLedd(element.getAnsettelsesforholdKode(),
        element.getAnsettelsesforholdKodeTekst());
  }

  private List<HedePersonProfilLedd> mapHeltidDeltidJobbonsker(
      List<EsHeltidDeltidJobbonsker> liste) {
    return liste == null ? null
        : liste.parallelStream().map(element -> map(element)).collect(Collectors.toList());
  }

  private HedePersonProfilLedd map(EsHeltidDeltidJobbonsker element) {
    return new HedePersonProfilLedd(element.getHeltidDeltidKode(),
        element.getHeltidDeltidKodeTekst());
  }

  private List<YrkePersonProfilLedd> mapYrkeJobbonsker(List<EsYrkeJobbonsker> liste) {
    return liste == null ? null
        : liste.parallelStream().map(element -> map(element)).collect(Collectors.toList());
  }

  private YrkePersonProfilLedd map(EsYrkeJobbonsker element) {
    return new YrkePersonProfilLedd(element.getStyrkKode(), element.getStyrkBeskrivelse(),
        element.isPrimaertJobbonske());
  }

  private List<GeoPersonProfilLedd> mapGeoJobbonsker(List<EsGeografiJobbonsker> liste) {
    return liste == null ? null
        : liste.parallelStream().map(element -> map(element)).collect(Collectors.toList());
  }

  private GeoPersonProfilLedd map(EsGeografiJobbonsker element) {
    return new GeoPersonProfilLedd(element.getGeografiKode(), element.getGeografiKodeTekst());
  }

  private List<VervCvLedd> mapVerv(List<EsVerv> liste) {
    return liste == null ? null
        : liste.parallelStream().map(element -> map(element)).collect(Collectors.toList());
  }

  private VervCvLedd map(EsVerv element) {
    return new VervCvLedd(element.getOrganisasjon(), element.getTittel(),
        mapLocalDate(element.getFraDato()), mapLocalDate(element.getTilDato()));
  }

  private List<KursCvLedd> mapKurs(List<EsKurs> liste) {
    return liste == null ? null
        : liste.parallelStream().map(element -> map(element)).collect(Collectors.toList());
  }

  private KursCvLedd map(EsKurs element) {
    return new KursCvLedd(element.getArrangor(), element.getTittel(), element.getBeskrivelse(),
        new Omfang(element.getOmfangVerdi(), element.getOmfangEnhet()),
        mapLocalDate(element.getFraDato()), mapLocalDate(element.getTilDato()));
  }

  private List<KompetanseCvLedd> mapKompetanse(List<EsKompetanse> liste) {
    return liste == null ? null
        : liste.parallelStream().map(element -> map(element)).collect(Collectors.toList());
  }

  /* TODO: Skal kompetanse ha tilDato? */
  private KompetanseCvLedd map(EsKompetanse element) {
    return new KompetanseCvLedd(element.getKompKode(), element.getKompKodeNavn(),
        element.getAlternativtNavn(), element.getBeskrivelse(), mapLocalDate(element.getFraDato()),
        null);
  }

  private List<SertifikatCvLedd> mapSertifikater(List<EsSertifikat> liste) {
    return liste == null ? null
        : liste.parallelStream().map(element -> map(element)).collect(Collectors.toList());
  }

  private SertifikatCvLedd map(EsSertifikat element) {
    return new SertifikatCvLedd(element.getUtsteder(), element.getSertifikatKode(),
        element.getSertifikatKodeNavn(), element.getAlternativtNavn(),
        mapLocalDate(element.getFraDato()), mapLocalDate(element.getTilDato()));
  }

  private List<YrkeCvLedd> mapYrkeserfaring(List<EsYrkeserfaring> liste) {
    return liste == null ? null
        : liste.parallelStream().map(element -> map(element)).collect(Collectors.toList());
  }

  private YrkeCvLedd map(EsYrkeserfaring element) {
    return new YrkeCvLedd(element.getArbeidsgiver(), element.getAlternativStillingstittel(),
        element.getStyrkKode(), element.getStyrkKodeStillingstittel(),
        safeUnwrap(element.getUtelukketForFremtiden()), mapLocalDate(element.getFraDato()),
        mapLocalDate(element.getTilDato()));
  }

  private List<UtdanningCvLedd> mapUtdanning(List<EsUtdanning> liste) {
    return liste == null ? null
        : liste.parallelStream().map(element -> map(element)).collect(Collectors.toList());
  }

  private UtdanningCvLedd map(EsUtdanning element) {
    return new UtdanningCvLedd(element.getUtdannelsessted(), element.getAlternativGrad(),
        element.getNusKode(), element.getNusKodeGrad(), mapLocalDate(element.getFraDato()),
        mapLocalDate(element.getTilDato()));
  }

  private Adresse mapAdresse(EsCv cv) {
    return new Adresse(cv.getLandkode(), cv.getPostnummer(), cv.getPoststed(),
        cv.getAdresselinje1(), cv.getAdresselinje2(), cv.getAdresselinje3(), cv.getKommunenummer());
  }
}
