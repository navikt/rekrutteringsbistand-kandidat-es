package no.nav.arbeid.cv.es.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import no.nav.arbeid.cv.es.domene.EsCv;
import no.nav.arbeid.cv.es.domene.EsForerkort;
import no.nav.arbeid.cv.es.domene.EsKompetanse;
import no.nav.arbeid.cv.es.domene.EsKurs;
import no.nav.arbeid.cv.es.domene.EsSertifikat;
import no.nav.arbeid.cv.es.domene.EsSprak;
import no.nav.arbeid.cv.es.domene.EsUtdanning;
import no.nav.arbeid.cv.es.domene.EsVerv;
import no.nav.arbeid.cv.es.domene.EsYrkeserfaring;
import no.nav.arbeid.cv.events.CvEvent;
import no.nav.arbeid.cv.events.Forerkort;
import no.nav.arbeid.cv.events.Kompetanse;
import no.nav.arbeid.cv.events.Kurs;
import no.nav.arbeid.cv.events.Sertifikat;
import no.nav.arbeid.cv.events.Sprak;
import no.nav.arbeid.cv.events.Utdanning;
import no.nav.arbeid.cv.events.Verv;
import no.nav.arbeid.cv.events.Yrkeserfaring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EsCvTransformer {

  private static final Logger LOGGER = LoggerFactory.getLogger(EsCvTransformer.class);

  public EsCv transform(CvEvent p) {
    EsCv esCv = new EsCv(
        p.getFodselsnummer(),
        p.getFornavn(),
        p.getEtternavn(),
        this.toDate(p.getFodselsdato()),
        p.getDnrStatus(),
        p.getFormidlingsgruppekode(),
        p.getEpostadresse(),
        p.getStatsborgerskap(),
        p.getArenaPersonId(),
        p.getArenaKandidatnr(),
        p.getBeskrivelse(),
        p.getSamtykkeStatus(),
        this.toDate(p.getSamtykkeDato()),
        p.getAdresselinje1(),
        p.getAdresselinje2(),
        p.getAdresselinje3(),
        p.getPostnr(),
        p.getPoststed(),
        p.getLandkode(),
        p.getKommunenr(),
        p.getStatusDisponererBil(),
        p.getAap(),
        p.getSyfo(),
        this.toDate(p.getTidsstempel())
    );

    esCv.addYrkeserfaring(mapList(p.getYrkeserfaring(), this::mapYrke));
    esCv.addUtdanning(mapList(p.getUtdanning(), this::mapUtdanning));
    esCv.addKompetanse(mapList(p.getKompetanse(), this::mapKompetanse));
    esCv.addSertifikat(mapList(p.getSertifikat(), this::mapSertifikat));
    esCv.addForerkort(mapList(p.getForerkort(), this::mapForerkort));
    esCv.addSprak(mapList(p.getSprak(), this::mapSprak));
    esCv.addKurs(mapList(p.getKurs(), this::mapKurs));
    esCv.addVerv(mapList(p.getVerv(), this::mapVerv));

    return esCv;
  }

  private <T, U> List<T> mapList(List<U> startListe, Function<U, T> mapper) {
    if (startListe == null) {
      return Collections.emptyList();
    }
    return startListe.stream().filter(Objects::nonNull).map(mapper).collect(Collectors.toList());
  }

  private EsYrkeserfaring mapYrke(Yrkeserfaring yrke) {
    return new EsYrkeserfaring(
        toDate(yrke.getFraDato()),
        toDate(yrke.getTilDato()),
        yrke.getArbeidsgiver(),
        yrke.getStyrkKode(),
        yrke.getStyrkKodeStillingstittel(),
        yrke.getAlternativStillingstittel(),
        yrke.getOrganisasjonsnummer(),
        yrke.getNaceKode()
    );
  }

  private EsUtdanning mapUtdanning(Utdanning utdanning) {
    return new EsUtdanning(
        toDate(utdanning.getFraDato()),
        toDate(utdanning.getTilDato()),
        utdanning.getUtdannelsessted(),
        utdanning.getNusKode(),
        utdanning.getNusKodeGrad(),
        utdanning.getAlternativGrad()
    );
  }

  private EsKompetanse mapKompetanse(Kompetanse kompetanse) {
    return new EsKompetanse(
        this.toDate(kompetanse.getFraDato()),
        kompetanse.getKompKode(),
        kompetanse.getKompKodeNavn(),
        kompetanse.getAlternativtNavn(),
        kompetanse.getBeskrivelse()
    );
  }

  private EsSertifikat mapSertifikat(Sertifikat sertifikat) {
    return new EsSertifikat(
        toDate(sertifikat.getFraDato()),
        toDate(sertifikat.getTilDato()),
        sertifikat.getSertifikatKode(),
        sertifikat.getSertifikatKodeNavn(),
        sertifikat.getAlternativtNavn(),
        sertifikat.getUtsteder()
    );
  }

  private EsForerkort mapForerkort(Forerkort forerkort) {
    return new EsForerkort(
        toDate(forerkort.getFraDato()),
        toDate(forerkort.getTilDato()),
        forerkort.getForerkortKode(),
        forerkort.getForerkortKodeKlasse(),
        forerkort.getAlternativKlasse(),
        forerkort.getUtsteder()
    );
  }

  private EsSprak mapSprak(Sprak sprak) {
    return new EsSprak(
        this.toDate(sprak.getFraDato()),
        sprak.getSprakKode(),
        sprak.getSprakKodeTekst(),
        sprak.getAlternativTekst(),
        sprak.getBeskrivelse()
    );
  }

  private EsKurs mapKurs(Kurs kurs) {
    return new EsKurs(
        toDate(kurs.getFraDato()),
        toDate(kurs.getTilDato()),
        kurs.getTittel(),
        kurs.getArrangor(),
        kurs.getOmfang().getEnhet(),
        kurs.getOmfang().getVerdi(),
        kurs.getBeskrivelse()
    );
  }

  private EsVerv mapVerv(Verv verv) {
    return new EsVerv(
        toDate(verv.getFraDato()),
        toDate(verv.getTilDato()),
        verv.getOrganisasjon(),
        verv.getTittel()
    );
  }

  private Date toDate(String dateString) {

    if(dateString == null || dateString.equals("")) {return null;}
    try {
      DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
      return formatter.parse(dateString);
    } catch (Exception e) {
      LOGGER.warn("Feilet under parsing av dato", e);
    }
    return null;
  }

}
