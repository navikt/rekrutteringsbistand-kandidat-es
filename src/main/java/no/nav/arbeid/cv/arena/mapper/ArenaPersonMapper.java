package no.nav.arbeid.cv.arena.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.arbeid.cv.arena.domene.ArenaAnsfPersonProfilLedd;
import no.nav.arbeid.cv.arena.domene.ArenaArboPersonProfilLedd;
import no.nav.arbeid.cv.arena.domene.ArenaGeoPersonProfilLedd;
import no.nav.arbeid.cv.arena.domene.ArenaHedePersonProfilLedd;
import no.nav.arbeid.cv.arena.domene.ArenaKompetanseCvLedd;
import no.nav.arbeid.cv.arena.domene.ArenaKursCvLedd;
import no.nav.arbeid.cv.arena.domene.ArenaPerson;
import no.nav.arbeid.cv.arena.domene.ArenaSertifikatCvLedd;
import no.nav.arbeid.cv.arena.domene.ArenaUtdanningCvLedd;
import no.nav.arbeid.cv.arena.domene.ArenaVervCvLedd;
import no.nav.arbeid.cv.arena.domene.ArenaYrkeCvLedd;
import no.nav.arbeid.cv.arena.domene.ArenaYrkePersonProfilLedd;
import no.nav.arbeid.cv.es.domene.EsAnsettelsesforholdJobbonsker;
import no.nav.arbeid.cv.es.domene.EsArbeidstidsordningJobbonsker;
import no.nav.arbeid.cv.es.domene.EsCv;
import no.nav.arbeid.cv.es.domene.EsForerkort;
import no.nav.arbeid.cv.es.domene.EsGeografiJobbonsker;
import no.nav.arbeid.cv.es.domene.EsHeltidDeltidJobbonsker;
import no.nav.arbeid.cv.es.domene.EsKompetanse;
import no.nav.arbeid.cv.es.domene.EsKurs;
import no.nav.arbeid.cv.es.domene.EsSamletKompetanse;
import no.nav.arbeid.cv.es.domene.EsSertifikat;
import no.nav.arbeid.cv.es.domene.EsSprak;
import no.nav.arbeid.cv.es.domene.EsUtdanning;
import no.nav.arbeid.cv.es.domene.EsVerv;
import no.nav.arbeid.cv.es.domene.EsYrkeJobbonsker;
import no.nav.arbeid.cv.es.domene.EsYrkeserfaring;

public class ArenaPersonMapper {

  private static final Logger LOGGER = LoggerFactory.getLogger(ArenaPersonMapper.class);

  public EsCv map(ArenaPerson p) {
    EsCv esCv = new EsCv(p.getFodselsnummer(), p.getFornavn(), p.getEtternavn(),
        toDate(p.getFodselsdato()), p.isErFodselsnummerDnr(), p.getFormidlingsgruppekode(),
        p.getEpost(), p.getStatsborgerskap(), p.getPersonId(), p.getKandidatnummer(),
        p.getBeskrivelse(), p.getSamtykkeStatus(), toDate(p.getSamtykkeDato()),
        p.getAdresse() == null ? "" : p.getAdresse().getAdrlinje1(),
        p.getAdresse() == null ? "" : p.getAdresse().getAdrlinje2(),
        p.getAdresse() == null ? "" : p.getAdresse().getAdrlinje3(),
        p.getAdresse() == null ? "" : p.getAdresse().getPostnr(),
        p.getAdresse() == null ? "" : p.getAdresse().getPoststednavn(),
        p.getAdresse() == null ? null : p.getAdresse().getLandkode(),
        p.getAdresse() == null ? null : p.getAdresse().getKommunenr(), p.isDisponererBil(),
        toDate(p.getSistEndret()));
    esCv.addYrkeserfaring(
        p.getYrkeserfaring().stream().map(this::mapYrkeserfaring).collect(Collectors.toList()));
    esCv.addUtdanning(
        p.getUtdanning().stream().map(this::mapUtdanning).collect(Collectors.toList()));
    esCv.addKompetanse(p.getKompetanse().stream().filter(this::isNotSprak).map(this::mapKompetanse)
        .collect(Collectors.toList()));
    esCv.addSprak(p.getKompetanse().stream().filter(this::isSprak).map(this::mapSprak)
        .collect(Collectors.toList()));
    esCv.addSertifikat(p.getSertifikater().stream().filter(this::isNotForerkort)
        .map(this::mapSertifikat).collect(Collectors.toList()));
    esCv.addForerkort(p.getSertifikater().stream().filter(this::isForerkort).map(this::mapForerkort)
        .collect(Collectors.toList()));
    esCv.addVerv(p.getVerv().stream().map(this::mapVerv).collect(Collectors.toList()));
    esCv.addKurs(p.getKurs().stream().map(this::mapKurs).collect(Collectors.toList()));

    esCv.addYrkeJobbonske(
        p.getYrkeJobbonsker().stream().map(this::mapYrkeJobbonske).collect(Collectors.toList()));
    esCv.addGeografiJobbonske(p.getGeografiJobbonsker().stream().map(this::mapGeografiJobbonsker)
        .collect(Collectors.toList()));

    esCv.addSamletKompetanse(
        p.getKompetanse().stream().map(this::mapSamletKompetanseKomp).collect(Collectors.toList()));
    esCv.addSamletKompetanse(p.getSertifikater().stream().map(this::mapSamletKompetanseSert)
        .collect(Collectors.toList()));
    esCv.addSamletKompetanse(
        p.getKurs().stream().map(this::mapSamletKompetanseKurs).collect(Collectors.toList()));

    return esCv;
  }

  private EsSamletKompetanse mapSamletKompetanseKomp(ArenaKompetanseCvLedd a) {
    return new EsSamletKompetanse(a.getKompetanseKodeTekst());
  }

  private EsSamletKompetanse mapSamletKompetanseSert(ArenaSertifikatCvLedd a) {
    return new EsSamletKompetanse(a.getSertifikatKodeNavn());
  }

  private EsSamletKompetanse mapSamletKompetanseKurs(ArenaKursCvLedd a) {
    return new EsSamletKompetanse(a.getTittel());
  }

  private EsHeltidDeltidJobbonsker mapHeltidDeltidJobbonsker(ArenaHedePersonProfilLedd a) {
    return new EsHeltidDeltidJobbonsker(a.getHeltidDeltidKode(), a.getHeltidDeltidKodeTekst());
  }

  private EsGeografiJobbonsker mapGeografiJobbonsker(ArenaGeoPersonProfilLedd a) {
    return new EsGeografiJobbonsker(a.getGeografiKode(), a.getGeografiKodeTekst());
  }

  private EsYrkeJobbonsker mapYrkeJobbonske(ArenaYrkePersonProfilLedd a) {
    return new EsYrkeJobbonsker(a.getStyrkKode(), a.getStyrkBeskrivelse(), a.isPrimaertJobbonske());
  }

  private EsArbeidstidsordningJobbonsker mapArbeidstidsordningJobbonsker(
      ArenaArboPersonProfilLedd a) {
    return new EsArbeidstidsordningJobbonsker(a.getArbeidstidsordningKode(),
        a.getArbeidstidsordningKodeTekst());
  }

  private EsAnsettelsesforholdJobbonsker mapAnsettelsesforholdJobbonsker(
      ArenaAnsfPersonProfilLedd a) {
    return new EsAnsettelsesforholdJobbonsker(a.getAnsettelsesforholdKode(),
        a.getAnsettelsesforholdKodeTekst());
  }

  private EsVerv mapVerv(ArenaVervCvLedd a) {
    return new EsVerv(toDate(a.getFraDato()), toDate(a.getTilDato()), a.getOrganisasjon(),
        a.getTittel());
  }

  private EsKurs mapKurs(ArenaKursCvLedd a) {
    return new EsKurs(toDate(a.getFraDato()), toDate(a.getTilDato()), a.getTittel(),
        a.getArrangor(), a.getOmfang() == null ? null : a.getOmfang().getEnhet(),
        a.getOmfang() == null ? null : a.getOmfang().getVerdi(), a.getBeskrivelse());
  }

  private EsSprak mapSprak(ArenaKompetanseCvLedd a) {
    return new EsSprak(toDate(a.getFraDato()), a.getKompetanseKode(), a.getKompetanseKodeTekst(),
        a.getAlternativTekst(), a.getBeskrivelse());
  }

  private EsSertifikat mapSertifikat(ArenaSertifikatCvLedd a) {
    return new EsSertifikat(toDate(a.getFraDato()), toDate(a.getTilDato()), a.getSertifikatKode(),
        a.getSertifikatKodeNavn(), a.getAlternativtNavn(), a.getUtsteder());
  }

  private boolean isForerkort(ArenaSertifikatCvLedd sertifikat) {
    return sertifikat.getSertifikatKode().startsWith("V1");
  }

  private boolean isNotForerkort(ArenaSertifikatCvLedd sertifikat) {
    return !(isForerkort(sertifikat));
  }

  private boolean isSprak(ArenaKompetanseCvLedd kompetanse) {
    return kompetanse.getKompetanseKode().startsWith("4");
  }

  private boolean isNotSprak(ArenaKompetanseCvLedd kompetanse) {
    return !isSprak(kompetanse);
  }

  private EsForerkort mapForerkort(ArenaSertifikatCvLedd a) {
    return new EsForerkort(toDate(a.getFraDato()), toDate(a.getTilDato()), a.getSertifikatKode(),
        a.getSertifikatKodeNavn(), a.getAlternativtNavn(), a.getUtsteder());
  }

  private EsKompetanse mapKompetanse(ArenaKompetanseCvLedd a) {
    return new EsKompetanse(toDate(a.getFraDato()), a.getKompetanseKode(),
        a.getKompetanseKodeTekst(), a.getAlternativTekst(), a.getBeskrivelse());
  }

  private EsUtdanning mapUtdanning(ArenaUtdanningCvLedd a) {
    return new EsUtdanning(toDate(a.getFraDato()), toDate(a.getTilDato()), a.getUtdannelsessted(),
        a.getNusKode(), a.getNusKodeUtdanningsnavn(), a.getAlternativtUtdanningsnavn());
  }

  private EsYrkeserfaring mapYrkeserfaring(ArenaYrkeCvLedd a) {
    return new EsYrkeserfaring(toDate(a.getFraDato()), toDate(a.getTilDato()), a.getArbeidsgiver(),
        a.getStyrkKode(), a.getStyrkKodeStillingstittel(), a.getAlternativStillingstittel(), null,
        null, toYrkeserfaringManeder(toDate(a.getFraDato()), toDate(a.getTilDato())));
  }

  private Date toDate(LocalDate localDate) {
    if (localDate == null) {
      return null;
    }
    return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
  }

  private Date toDate(LocalDateTime localDateTime) {
    if (localDateTime == null) {
      return null;
    }
    return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
  }

  private int toYrkeserfaringManeder(Date fraDato, Date tilDato) {
    // Should not be possible, but will keep the check just in case
    if (fraDato == null) {
      return 0;
    }

    Calendar fraCalendar = new GregorianCalendar();
    fraCalendar.setTime(fraDato);

    // If tilDato is null, it is set to the current date
    Calendar tilCalendar = new GregorianCalendar();
    if (tilDato == null) {
      tilCalendar.setTime(new Date());
    } else {
      tilCalendar.setTime(tilDato);
    }

    int diffYear = tilCalendar.get(Calendar.YEAR) - fraCalendar.get(Calendar.YEAR);
    return diffYear * 12 + tilCalendar.get(Calendar.MONTH) - fraCalendar.get(Calendar.MONTH);
  }
}
