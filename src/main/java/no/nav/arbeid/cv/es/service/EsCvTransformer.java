package no.nav.arbeid.cv.es.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import no.nav.arbeid.cv.es.domene.EsAnnenErfaring;
import no.nav.arbeid.cv.es.domene.EsCv;
import no.nav.arbeid.cv.es.domene.EsForerkort;
import no.nav.arbeid.cv.es.domene.EsKompetanse;
import no.nav.arbeid.cv.es.domene.EsKurs;
import no.nav.arbeid.cv.es.domene.EsSertifikat;
import no.nav.arbeid.cv.es.domene.EsSprak;
import no.nav.arbeid.cv.es.domene.EsUtdanning;
import no.nav.arbeid.cv.es.domene.EsVerv;
import no.nav.arbeid.cv.es.domene.EsYrkeserfaring;
import no.nav.arbeid.cv.events.Annenerfaring;
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
        p.getFornavn(),
        p.getEtternavn(),
        toDate(p.getFodselsdato()),
        p.getFormidlingsgruppekode(),
        p.getEpostadresse(),
        p.getStatsborgerskap(),
        p.getArenaId(),
        p.getBeskrivelse()
    );
    
    esCv.addYrkeserfaring(mapYrkeListe(p.getYrkeserfaring()));
    esCv.addUtdanning(mapUtdanningListe(p.getUtdanning()));
    esCv.addKompetanse(mapKompetanseListe(p.getKompetanse()));
    esCv.addAnnenErfaring(mapAnnenErfaringListe(p.getAnnenerfaring()));
    esCv.addSertifikat(mapSertifikatListe(p.getSertifikat()));
    esCv.addForerkort(mapForerkortListe(p.getForerkort()));
    esCv.addSprak(mapSprakListe(p.getSprak()));
    esCv.addKurs(mapKursListe(p.getKurs()));
    esCv.addVerv(mapVervListe(p.getVerv()));

    return esCv;
  }

  private Collection<EsYrkeserfaring> mapYrkeListe(List<Yrkeserfaring> yrkeserfaringListe) {
    return yrkeserfaringListe.stream().map(this::mapYrke).collect(Collectors.toList());
  }

  private EsYrkeserfaring mapYrke(Yrkeserfaring yrke) {
    return new EsYrkeserfaring(
        toDate(yrke.getFraDato()),
        toDate(yrke.getFraDato()),
        yrke.getArbeidsgiver(),
        yrke.getOrganisasjonsnummer(),
        yrke.getStillingstittel(),
        yrke.getBeskrivelse(),
        yrke.getSokekategori(),
        yrke.getStyrkKode(),
        yrke.getStyrkKodeTekst(),
        yrke.getNaceKode()
    );
  }

  private Collection<EsUtdanning> mapUtdanningListe(List<Utdanning> utdanningListe) {
    return utdanningListe.stream().map(this::mapUtdanning).collect(Collectors.toList());
  }

  private EsUtdanning mapUtdanning(Utdanning utdanning) {
    return new EsUtdanning(
        toDate(utdanning.getFraDato()),
        toDate(utdanning.getFraDato()),
        utdanning.getGrad(),
        utdanning.getStudiepoeng(),
        utdanning.getUtdannelsessted(),
        utdanning.getGeografisksted(),
        utdanning.getNusKode(),
        utdanning.getNusKodeTekst()
    );
  }

  private Collection<EsKompetanse> mapKompetanseListe(List<Kompetanse> kompetanseListe) {
    return kompetanseListe.stream().map(this::mapKompetanse).collect(Collectors.toList());
  }

  private EsKompetanse mapKompetanse(Kompetanse kompetanse) {
    return new EsKompetanse(
        kompetanse.getNavn()
    );
  }

  private Collection<EsAnnenErfaring> mapAnnenErfaringListe(List<Annenerfaring> annenerfaringListe) {
    return annenerfaringListe.stream().map(this::mapAnnenErfaring).collect(Collectors.toList());
  }

  private EsAnnenErfaring mapAnnenErfaring(Annenerfaring annenerfaring) {
    return new EsAnnenErfaring(
        toDate(annenerfaring.getFraDato()),
        toDate(annenerfaring.getFraDato()),
        annenerfaring.getBeskrivelse()
    );
  }

  private Collection<EsSertifikat> mapSertifikatListe(List<Sertifikat> sertifikatListe) {
    return sertifikatListe.stream().map(this::mapSertifikat).collect(Collectors.toList());
  }

  private EsSertifikat mapSertifikat(Sertifikat sertifikat) {
    return new EsSertifikat(
        toDate(sertifikat.getFraDato()),
        toDate(sertifikat.getTilDato()),
        sertifikat.getSertifikatKode(),
        sertifikat.getSertifikatKodeTekst(),
        sertifikat.getUtsteder()
    );
  }

  private Collection<EsForerkort> mapForerkortListe(List<Forerkort> forerkortListe) {
      return forerkortListe.stream().map(this::mapForerkort).collect(Collectors.toList());
    }

  private EsForerkort mapForerkort(Forerkort forerkort) {
    return new EsForerkort(
        toDate(forerkort.getFraDato()),
        toDate(forerkort.getTilDato()),
        forerkort.getKlasse(),
        forerkort.getUtsteder(),
        forerkort.getDisponerer()
    );
  }
  
  private Collection<EsSprak> mapSprakListe(List<Sprak> sprakListe) {
      return sprakListe.stream().map(this::mapSprak).collect(Collectors.toList());
    }

  private EsSprak mapSprak(Sprak sprak) {
    return new EsSprak(
        sprak.getSprakKode(),
        sprak.getSprakKodeTekst(),
        sprak.getMuntlig(),
        sprak.getSkriftlig()
    );
  }
  
  private Collection<EsKurs> mapKursListe(List<Kurs> kursListe) {
    return kursListe.stream().map(this::mapKurs).collect(Collectors.toList());
  }

  private EsKurs mapKurs(Kurs kurs) {
    return new EsKurs(
        toDate(kurs.getFraDato()),
        toDate(kurs.getTilDato()),
        kurs.getTittel(),
        kurs.getArrangor(),
        kurs.getArrangor()
    );
  }
  
  private Collection<EsVerv> mapVervListe(List<Verv> vervListe) {
    return vervListe.stream().map(this::mapVerv).collect(Collectors.toList());
  }

  private EsVerv mapVerv(Verv verv) {
    return new EsVerv(
        toDate(verv.getFraDato()),
        toDate(verv.getTilDato()),
        verv.getOrganisasjon(),
        verv.getTittel()
    );
  }

  private LocalDate toDate(String date) {
    try {
      return LocalDate.parse(date);
    } catch (Exception e) {
      LOGGER.warn("Feilet under parsing av dato", e);
    }
    return null;
  }

}
