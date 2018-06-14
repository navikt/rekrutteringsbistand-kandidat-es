package no.nav.arbeid.cv.indexer.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.arbeid.cv.events.CvEvent;
import no.nav.arbeid.cv.events.Forerkort;
import no.nav.arbeid.cv.events.GeografiJobbonsker;
import no.nav.arbeid.cv.events.Kompetanse;
import no.nav.arbeid.cv.events.Kurs;
import no.nav.arbeid.cv.events.Sertifikat;
import no.nav.arbeid.cv.events.Sprak;
import no.nav.arbeid.cv.events.Utdanning;
import no.nav.arbeid.cv.events.Verv;
import no.nav.arbeid.cv.events.YrkeJobbonsker;
import no.nav.arbeid.cv.events.Yrkeserfaring;
import no.nav.arbeid.cv.indexer.domene.EsCv;
import no.nav.arbeid.cv.indexer.domene.EsForerkort;
import no.nav.arbeid.cv.indexer.domene.EsGeografiJobbonsker;
import no.nav.arbeid.cv.indexer.domene.EsKompetanse;
import no.nav.arbeid.cv.indexer.domene.EsKurs;
import no.nav.arbeid.cv.indexer.domene.EsSamletKompetanse;
import no.nav.arbeid.cv.indexer.domene.EsSertifikat;
import no.nav.arbeid.cv.indexer.domene.EsSprak;
import no.nav.arbeid.cv.indexer.domene.EsUtdanning;
import no.nav.arbeid.cv.indexer.domene.EsVerv;
import no.nav.arbeid.cv.indexer.domene.EsYrkeJobbonsker;
import no.nav.arbeid.cv.indexer.domene.EsYrkeserfaring;

public class EsCvTransformer {

  private static final Logger LOGGER = LoggerFactory.getLogger(EsCvTransformer.class);

  public EsCv transform(CvEvent p) {
    LOGGER.debug("Mottatt en CV med {} antall utdanning og {} antall arbeidserfaringer",
        nullSafeAntall(p.getUtdanning()), nullSafeAntall(p.getYrkeserfaring()));
    EsCv esCv = new EsCv(p.getFodselsnummer(), p.getFornavn(), p.getEtternavn(),
        this.toDate(p.getFodselsdato()), p.getFodselsdatoErDnr(), p.getFormidlingsgruppekode(),
        p.getEpostadresse(), p.getStatsborgerskap(), p.getArenaPersonId(), p.getArenaKandidatnr(),
        p.getBeskrivelse(), p.getSamtykkeStatus(), this.toDate(p.getSamtykkeDato()),
        p.getAdresselinje1(), p.getAdresselinje2(), p.getAdresselinje3(), p.getPostnr(),
        p.getPoststed(), p.getLandkode(), p.getKommunenr(), p.getDisponererBil(),
        this.toDate(p.getTidsstempel()));

    esCv.addYrkeserfaring(mapList(p.getYrkeserfaring(), this::mapYrke));
    esCv.addUtdanning(mapList(p.getUtdanning(), this::mapUtdanning));
    esCv.addKompetanse(mapList(p.getKompetanse(), this::mapKompetanse));
    esCv.addSertifikat(mapList(p.getSertifikat(), this::mapSertifikat));
    esCv.addForerkort(mapList(p.getForerkort(), this::mapForerkort));
    esCv.addSprak(mapList(p.getSprak(), this::mapSprak));
    esCv.addKurs(mapList(p.getKurs(), this::mapKurs));
    esCv.addVerv(mapList(p.getVerv(), this::mapVerv));
    esCv.addGeografiJobbonske(mapList(p.getGeografiJobbonsker(), this::mapGeografiJobbonske));
    esCv.addYrkeJobbonske(mapList(p.getYrkeJobbonsker(), this::mapYrkeJobbonske));

    esCv.addSamletKompetanse(mapList(p.getSprak(), this::mapSamletKompetanse));
    esCv.addSamletKompetanse(mapList(p.getSertifikat(), this::mapSamletKompetanse));
    esCv.addSamletKompetanse(mapList(p.getKurs(), this::mapSamletKompetanse));
    esCv.addSamletKompetanse(mapList(p.getForerkort(), this::mapSamletKompetanse));
    esCv.addSamletKompetanse(mapList(p.getKompetanse(), this::mapSamletKompetanse));

    return esCv;
  }

  private int nullSafeAntall(List<?> list) {
    return list == null ? 0 : list.size();
  }

  private <T, U> List<T> mapList(List<U> startListe, Function<U, T> mapper) {
    if (startListe == null) {
      return Collections.emptyList();
    }
    return startListe.stream().filter(Objects::nonNull).map(mapper).collect(Collectors.toList());
  }

  private EsYrkeserfaring mapYrke(Yrkeserfaring yrke) {
    Date fraDato = toDate(yrke.getFraDato());
    Date tilDato = toDate(yrke.getTilDato());
    return new EsYrkeserfaring(fraDato, tilDato, yrke.getArbeidsgiver(), yrke.getStyrkKode(),
        yrke.getStyrkKodeStillingstittel(), yrke.getAlternativStillingstittel(),
        yrke.getOrganisasjonsnummer(), yrke.getNaceKode(),
        this.toYrkeserfaringManeder(fraDato, tilDato), yrke.getUtelukketForFremtiden());
  }

  private EsUtdanning mapUtdanning(Utdanning utdanning) {
    return new EsUtdanning(toDate(utdanning.getFraDato()), toDate(utdanning.getTilDato()),
        utdanning.getUtdannelsessted(), utdanning.getNusKode(), utdanning.getNusKodeGrad(),
        utdanning.getAlternativGrad());
  }

  private EsKompetanse mapKompetanse(Kompetanse kompetanse) {
    return new EsKompetanse(this.toDate(kompetanse.getFraDato()), kompetanse.getKompKode(),
        kompetanse.getKompKodeNavn(), kompetanse.getAlternativtNavn(), kompetanse.getBeskrivelse());
  }

  private EsSertifikat mapSertifikat(Sertifikat sertifikat) {
    return new EsSertifikat(toDate(sertifikat.getFraDato()), toDate(sertifikat.getTilDato()),
        sertifikat.getSertifikatKode(), sertifikat.getSertifikatKodeNavn(),
        sertifikat.getAlternativtNavn(), sertifikat.getUtsteder());
  }

  private EsForerkort mapForerkort(Forerkort forerkort) {
    return new EsForerkort(toDate(forerkort.getFraDato()), toDate(forerkort.getTilDato()),
        forerkort.getForerkortKode(), forerkort.getForerkortKodeKlasse(),
        forerkort.getAlternativKlasse(), forerkort.getUtsteder());
  }

  private EsSprak mapSprak(Sprak sprak) {
    return new EsSprak(this.toDate(sprak.getFraDato()), sprak.getSprakKode(),
        sprak.getSprakKodeTekst(), sprak.getAlternativTekst(), sprak.getBeskrivelse());
  }

  private EsKurs mapKurs(Kurs kurs) {
    return new EsKurs(toDate(kurs.getFraDato()), toDate(kurs.getTilDato()), kurs.getTittel(),
        kurs.getArrangor(), kurs.getOmfang().getEnhet(), kurs.getOmfang().getVerdi(),
        kurs.getBeskrivelse());
  }

  private EsVerv mapVerv(Verv verv) {
    return new EsVerv(toDate(verv.getFraDato()), toDate(verv.getTilDato()), verv.getOrganisasjon(),
        verv.getTittel());
  }

  private EsGeografiJobbonsker mapGeografiJobbonske(GeografiJobbonsker geografiJobbonsker) {
    return new EsGeografiJobbonsker(geografiJobbonsker.getGeografiKodeTekst(),
        geografiJobbonsker.getGeografiKode());
  }

  private EsYrkeJobbonsker mapYrkeJobbonske(YrkeJobbonsker yrkeJobbonsker) {
    return new EsYrkeJobbonsker(yrkeJobbonsker.getStyrkKode(), yrkeJobbonsker.getStyrkBeskrivelse(),
        yrkeJobbonsker.getPrimaertJobbonske());
  }

  private EsSamletKompetanse mapSamletKompetanse(Sprak sprak) {
    return new EsSamletKompetanse(sprak.getSprakKodeTekst());
  }

  private EsSamletKompetanse mapSamletKompetanse(Sertifikat sertifikat) {
    return new EsSamletKompetanse(sertifikat.getSertifikatKodeNavn());
  }

  private EsSamletKompetanse mapSamletKompetanse(Kurs kurs) {
    return new EsSamletKompetanse(kurs.getTittel());
  }

  private EsSamletKompetanse mapSamletKompetanse(Forerkort forerkort) {
    return new EsSamletKompetanse(forerkort.getForerkortKodeKlasse());
  }

  private EsSamletKompetanse mapSamletKompetanse(Kompetanse kompetanse) {
    return new EsSamletKompetanse(kompetanse.getKompKodeNavn());
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

  private Date toDate(String dateString) {

    if (dateString == null || dateString.equals("")) {
      return null;
    }
    try {
      DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
      return formatter.parse(dateString);
    } catch (Exception e) {
      LOGGER.warn("Feilet under parsing av dato", e);
    }
    return null;
  }

}
