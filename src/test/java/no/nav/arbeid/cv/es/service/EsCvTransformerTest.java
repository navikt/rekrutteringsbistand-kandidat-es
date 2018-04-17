package no.nav.arbeid.cv.es.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.Test;

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
import no.nav.arbeid.cv.events.CvEvent;

public class EsCvTransformerTest {

  private EsCvTransformer esCvTransformer = new EsCvTransformer();

  @Test
  public void toDateShouldConvertToLocalDateWithoutErrors() {

    CvEvent cvEvent = CvEventObjectMother.giveMeCvEvent();
    cvEvent.setFodselsdato("2000-01-15");
    EsCv esCv = esCvTransformer.transform(cvEvent);
    assertThat(esCv.getFodselsdato()).isEqualTo("2000-01-15");
  }

  @Test
  public void esCvTransformerShouldConvertCvEventToEsCvWithCorrectFornavnAndEtternavn() {

    CvEvent cvEvent = CvEventObjectMother.giveMeCvEvent();
    EsCv esCv = esCvTransformer.transform(cvEvent);

    assertEquals("OLA", esCv.getFornavn());
    assertEquals("NORDMANN", esCv.getEtternavn());
  }

  @Test
  public void esCvTransformerShouldConvertCvEventToEsCvWithCorrectPersonalInformation() {

    final String FODSELSNUMMER = "01016012345";
    final String FORNAVN = "OLA";
    final String ETTERNAVN = "NORDMANN";
    final String FODSELSDATO = "1960-01-01";
    final boolean DNRSTATUS = false;
    final String FORMIDLINGSGRUPPEKODE = "ARBS";
    final String EPOSTADRESSE = "unnasluntrer@mailinator.com";
    final String STATSBORGERSKAP = "NO";
    final String arenaKandNr = null;
    final Long ARENA_ID = 1L;
    final String BESKRIVELSE = "";
    final String SAMTYKKESTATUS = "J";
    final Date SAMTYKKEDATO = new Date(1464559200000L);
    final String ADRESSELINJE_1 = "Minvei 1";
    final String ADRESSELINJE_2 = "";
    final String ADRESSELINJE_3 = "";
    final String POSTNUMMER = "0654";
    final String POSTSTED = "OSLO";
    final String LANDKODE = "NO";
    final Integer KOMMUNENUMMER = 301;
    final boolean DISP_BIL = false;
    final boolean AAP = false;
    final boolean SYFO = false;
    final Date TIDSSTEMPEL = null;

    CvEvent cvEvent = CvEventObjectMother.giveMeCvEvent();
    EsCv esCv = esCvTransformer.transform(cvEvent);

    assertThat(esCv.getFodselsnummer()).isEqualTo(FODSELSNUMMER);
    assertThat(esCv.getFornavn()).isEqualTo(FORNAVN);
    assertThat(esCv.getEtternavn()).isEqualTo(ETTERNAVN);
    assertThat(esCv.getFodselsdato()).isEqualTo(FODSELSDATO);
    assertThat(esCv.getFodselsdatoErDnr()).isEqualTo(DNRSTATUS);
    assertThat(esCv.getFormidlingsgruppekode()).isEqualTo(FORMIDLINGSGRUPPEKODE);
    assertThat(esCv.getEpostadresse()).isEqualTo(EPOSTADRESSE);
    assertThat(esCv.getStatsborgerskap()).isEqualTo(STATSBORGERSKAP);
    assertThat(esCv.getArenaPersonId()).isEqualTo(ARENA_ID);
    assertThat(esCv.getArenaKandidatnr()).isEqualTo(arenaKandNr);
    assertThat(esCv.getBeskrivelse()).isEqualTo(BESKRIVELSE);
    assertThat(esCv.getSamtykkeStatus()).isEqualTo(SAMTYKKESTATUS);
    assertThat(esCv.getSamtykkeDato()).isEqualTo(SAMTYKKEDATO);
    assertThat(esCv.getAdresselinje1()).isEqualTo(ADRESSELINJE_1);
    assertThat(esCv.getAdresselinje2()).isEqualTo(ADRESSELINJE_2);
    assertThat(esCv.getAdresselinje3()).isEqualTo(ADRESSELINJE_3);
    assertThat(esCv.getPostnummer()).isEqualTo(POSTNUMMER);
    assertThat(esCv.getPoststed()).isEqualTo(POSTSTED);
    assertThat(esCv.getLandkode()).isEqualTo(LANDKODE);
    assertThat(esCv.getKommunenummer()).isEqualTo(KOMMUNENUMMER);
    assertThat(esCv.getDisponererBil()).isEqualTo(DISP_BIL);
    assertThat(esCv.getTidsstempel()).isEqualTo(TIDSSTEMPEL);
  }

  @Test
  public void esCvTransformerShouldConvertCvEventToEsCvWithCorrectYrkeserfaring() {

    CvEvent cvEvent = CvEventObjectMother.giveMeCvEvent();
    EsCv esCv = esCvTransformer.transform(cvEvent);

    List<EsYrkeserfaring> yrkeserfaring = esCv.getYrkeserfaring();

    assertThat(yrkeserfaring).hasSize(6);
    assertThat(yrkeserfaring.get(0).getFraDato()).isEqualTo("2000-01-01");
    assertThat(yrkeserfaring.get(0).getTilDato()).isEqualTo("2002-01-01");
    assertThat(yrkeserfaring.get(0).getArbeidsgiver()).isEqualTo("Stentransport, Kragerø");
    assertThat(yrkeserfaring.get(0).getStyrkKode()).isEqualTo("8342.01");
    assertThat(yrkeserfaring.get(0).getStyrkKodeStillingstittel()).isEqualTo("Anleggsmaskinfører");
    assertThat(yrkeserfaring.get(0).getAlternativStillingstittel()).isEqualTo("maskinkjører og maskintransport");
    assertThat(yrkeserfaring.get(0).getOrganisasjonsnummer()).isEqualTo("YRKE_ORGNR");
    assertThat(yrkeserfaring.get(0).getNaceKode())
        .isEqualTo("YRKE_NACEKODE");

  }

  @Test
  public void esCvTransformerShouldConvertCvEventToEsCvWithCorrectUtdanning() {

    CvEvent cvEvent = CvEventObjectMother.giveMeCvEvent();
    EsCv esCv = esCvTransformer.transform(cvEvent);

    List<EsUtdanning> utdanning = esCv.getUtdanning();

    assertThat(utdanning).hasSize(1);
    assertThat(utdanning.get(0).getFraDato()).isEqualTo("1988-08-20");
    assertThat(utdanning.get(0).getTilDato()).isEqualTo("1989-06-20");
    assertThat(utdanning.get(0).getUtdannelsessted()).isEqualTo("Otta vgs. Otta");
    assertThat(utdanning.get(0).getNusKode()).isEqualTo("355211");
    assertThat(utdanning.get(0).getNusKodeGrad()).isEqualTo("Mekaniske fag, grunnkurs");
    assertThat(utdanning.get(0).getAlternativGrad()).isEqualTo("GK maskin/mekaniker");
  }

  @Test
  public void esCvTransformerShouldConvertCvEventToEsCvWithCorrectKompetanse() {

    CvEvent cvEvent = CvEventObjectMother.giveMeCvEvent();
    EsCv esCv = esCvTransformer.transform(cvEvent);

    List<EsKompetanse> kompetanse = esCv.getKompetanse();

    assertThat(kompetanse).hasSize(4);
    assertThat(kompetanse.get(0).getFraDato()).isEqualTo("2016-03-14");
    assertThat(kompetanse.get(0).getKompKode()).isEqualTo("3020813");
    assertThat(kompetanse.get(0).getKompKodeNavn()).isEqualTo("Maskin- og kranførerarbeid");
    assertThat(kompetanse.get(0).getAlternativtNavn()).isEqualTo(null);
    assertThat(kompetanse.get(0).getBeskrivelse()).isEqualTo(null);
  }

  @Test
  public void esCvTransformerShouldConvertCvEventToEsCvWithCorrectAnnenerfaring() {

    CvEvent cvEvent = CvEventObjectMother.giveMeCvEvent();
    EsCv esCv = esCvTransformer.transform(cvEvent);
    List<EsAnnenErfaring> annenerfaring = esCv.getAnnenerfaring();
    assertThat(annenerfaring).hasSize(0);
  }

  @Test
  public void esCvTransformerShouldConvertCvEventToEsCvWithCorrectSertifikat() {

    CvEvent cvEvent = CvEventObjectMother.giveMeCvEvent();
    EsCv esCv = esCvTransformer.transform(cvEvent);

    List<EsSertifikat> sertifikat = esCv.getSertifikat();

    assertThat(sertifikat).hasSize(5);
    assertThat(sertifikat.get(0).getFraDato()).isEqualTo("1994-08-01");
    assertThat(sertifikat.get(0).getTilDato()).isEqualTo(null);
    assertThat(sertifikat.get(0).getSertifikatKode()).isEqualTo("V1.6050");
    assertThat(sertifikat.get(0).getSertifikatKodeNavn()).isEqualTo("Førerkort: Kl. A (tung motorsykkel)");
    assertThat(sertifikat.get(0).getAlternativtNavn()).isEqualTo(null);
    assertThat(sertifikat.get(0).getUtsteder()).isEqualTo("");
  }

  @Test
  public void esCvTransformerShouldConvertCvEventToEsCvWithCorrectForerkort() {

    CvEvent cvEvent = CvEventObjectMother.giveMeCvEvent();
    EsCv esCv = esCvTransformer.transform(cvEvent);

    List<EsForerkort> Forerkort = esCv.getForerkort();

    assertThat(Forerkort).hasSize(4);
    assertThat(Forerkort.get(0).getFraDato()).isEqualTo("1994-08-01");
    assertThat(Forerkort.get(0).getTilDato()).isEqualTo(null);
    assertThat(Forerkort.get(0).getForerkortKode()).isEqualTo("V1.6050");
    assertThat(Forerkort.get(0).getForerkortKodeKlasse()).isEqualTo("Førerkort: Kl. A (tung motorsykkel)");
    assertThat(Forerkort.get(0).getAlternativKlasse()).isEqualTo(null);
    assertThat(Forerkort.get(0).getUtsteder()).isEqualTo("");
  }

  @Test
  public void esCvTransformerShouldConvertCvEventToEsCvWithCorrectSprak() {

    CvEvent cvEvent = CvEventObjectMother.giveMeCvEvent();
    EsCv esCv = esCvTransformer.transform(cvEvent);

    List<EsSprak> Sprak = esCv.getSprak();

    assertThat(Sprak).hasSize(1);
    assertThat(Sprak.get(0).getFraDato()).isEqualTo("2012-12-01");
    assertThat(Sprak.get(0).getSprakKode()).isEqualTo("Språk kode");
    assertThat(Sprak.get(0).getSprakKodeTekst()).isEqualTo("Språk kode tekst");
    assertThat(Sprak.get(0).getAlternativTekst()).isEqualTo("Språk alternativ tekst");
    assertThat(Sprak.get(0).getBeskrivelse()).isEqualTo("Språk beskrivelse");
  }

  @Test
  public void esCvTransformerShouldConvertCvEventToEsCvWithCorrectKurs() {

    CvEvent cvEvent = CvEventObjectMother.giveMeCvEvent();
    EsCv esCv = esCvTransformer.transform(cvEvent);

    List<EsKurs> kurs = esCv.getKurs();

    assertThat(kurs).hasSize(3);
    assertThat(kurs.get(0).getFraDato()).isEqualTo("2012-12-01");
    assertThat(kurs.get(0).getTilDato()).isEqualTo(null);
    assertThat(kurs.get(0).getTittel()).isEqualTo("Akseloppretting");
    assertThat(kurs.get(0).getArrangor()).isEqualTo("Easy-Laser");
    assertThat(kurs.get(0).getOmfangEnhet()).isEqualTo(null);
    assertThat(kurs.get(0).getOmfangVerdi()).isEqualTo(null);
    assertThat(kurs.get(0).getBeskrivelse()).isEqualTo(null);
  }

  @Test
  public void esCvTransformerShouldConvertCvEventToEsCvWithCorrectVerv() {

    CvEvent cvEvent = CvEventObjectMother.giveMeCvEvent();
    EsCv esCv = esCvTransformer.transform(cvEvent);

    List<EsVerv> verv = esCv.getVerv();

    assertThat(verv).hasSize(1);
    assertThat(verv.get(0).getFraDato()).isEqualTo("2000-01-15");
    assertThat(verv.get(0).getTilDato()).isEqualTo("2001-01-15");
    assertThat(verv.get(0).getOrganisasjon()).isEqualTo("Verv organisasjon");
    assertThat(verv.get(0).getTittel()).isEqualTo("verv tittel");
  }

}
