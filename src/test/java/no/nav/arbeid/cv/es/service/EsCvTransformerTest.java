package no.nav.arbeid.cv.es.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.List;
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
import org.junit.Test;

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

    assertEquals("Test", esCv.getFornavn());
    assertEquals("Testersen", esCv.getEtternavn());
  }

  @Test
  public void esCvTransformerShouldConvertCvEventToEsCvWithCorrectPersonalInformation() {

    final String FORNAVN = "Test";
    final String ETTERNAVN = "Testersen";
    final String FODSELSDATO = "2000-01-15";
    final String FORMIDLINGSGRUPPEKODE = "020202";
    final String EPOSTADRESSE = "test@test.no";
    final String STATSBORGERSKAP = "Norge";
    final Long ARENA_ID = 1L;
    final String BESKRIVELSE = "beskrivelse CvEvent";

    CvEvent cvEvent = CvEventObjectMother.giveMeCvEvent();
    EsCv esCv = esCvTransformer.transform(cvEvent);

    assertThat(esCv.getPersonId()).isEqualTo(ARENA_ID);
    assertThat(esCv.getFornavn()).isEqualTo(FORNAVN);
    assertThat(esCv.getEtternavn()).isEqualTo(ETTERNAVN);
    assertThat(esCv.getFodselsdato()).isEqualTo(FODSELSDATO);
    assertThat(esCv.getFormidlingsgruppekode()).isEqualTo(FORMIDLINGSGRUPPEKODE);
    assertThat(esCv.getEpostadresse()).isEqualTo(EPOSTADRESSE);
    assertThat(esCv.getStatsborgerskap()).isEqualTo(STATSBORGERSKAP);
    assertThat(esCv.getArenaId()).isEqualTo(ARENA_ID);
    assertThat(esCv.getBeskrivelse()).isEqualTo(BESKRIVELSE);
  }

  @Test
  public void esCvTransformerShouldConvertCvEventToEsCvWithCorrectYrkeserfaring() {

    CvEvent cvEvent = CvEventObjectMother.giveMeCvEvent();
    EsCv esCv = esCvTransformer.transform(cvEvent);

    List<EsYrkeserfaring> yrkeserfaring = esCv.getYrkeserfaring();

    assertThat(yrkeserfaring).hasSize(1);
    assertThat(yrkeserfaring.get(0).getFraDato()).isEqualTo("2000-01-15");
    assertThat(yrkeserfaring.get(0).getTilDato()).isEqualTo("2000-01-15");
    assertThat(yrkeserfaring.get(0).getArbeidsgiver()).isEqualTo("Yrkeserfaring arbeidsgiver");
    assertThat(yrkeserfaring.get(0).getBeskrivelse()).isEqualTo("Yrkeserfaring beskrivelse");
    assertThat(yrkeserfaring.get(0).getNaceKode()).isEqualTo("Yrkeserfaring nacekode");
    assertThat(yrkeserfaring.get(0).getOrganisasjonsnummer()).isEqualTo("1");
    assertThat(yrkeserfaring.get(0).getSokekategori()).isEqualTo("Yrkeserfaring sokekategori");
    assertThat(yrkeserfaring.get(0).getStillingstittel())
        .isEqualTo("greenkeeper");
    assertThat(yrkeserfaring.get(0).getStyrkKode()).isEqualTo("Yrkeserfaring styrkkode");
    assertThat(yrkeserfaring.get(0).getStyrkKodeTekst()).isEqualTo("Yrkeserfaring styrkkode tekst");

  }

  @Test
  public void esCvTransformerShouldConvertCvEventToEsCvWithCorrectUtdanning() {

    CvEvent cvEvent = CvEventObjectMother.giveMeCvEvent();
    EsCv esCv = esCvTransformer.transform(cvEvent);

    List<EsUtdanning> utdanning = esCv.getUtdanning();

    assertThat(utdanning).hasSize(1);
    assertThat(utdanning.get(0).getFraDato()).isEqualTo("2000-01-15");
    assertThat(utdanning.get(0).getTilDato()).isEqualTo("2001-01-15");
    assertThat(utdanning.get(0).getGrad()).isEqualTo("grad");
    assertThat(utdanning.get(0).getStudiepoeng()).isEqualTo("1");
    assertThat(utdanning.get(0).getUtdannelsessted())
        .isEqualTo("Utdanning utdannelsessted");
    assertThat(utdanning.get(0).getGeografiskSted())
        .isEqualTo("Utdanning geografisk sted");
    assertThat(utdanning.get(0).getNusKode()).isEqualTo("Utdanning nusKode");
    assertThat(utdanning.get(0).getNusKodeTekst()).isEqualTo("nusKodeTekst");

  }

  @Test
  public void esCvTransformerShouldConvertCvEventToEsCvWithCorrectKompetanse() {

    CvEvent cvEvent = CvEventObjectMother.giveMeCvEvent();
    EsCv esCv = esCvTransformer.transform(cvEvent);

    List<EsKompetanse> kompetanse = esCv.getKompetanse();

    assertThat(kompetanse).hasSize(1);
    assertThat(kompetanse.get(0).getNavn()).isEqualTo("navn kompetanse");
  }

  @Test
  public void esCvTransformerShouldConvertCvEventToEsCvWithCorrectAnnenerfaring() {

    CvEvent cvEvent = CvEventObjectMother.giveMeCvEvent();
    EsCv esCv = esCvTransformer.transform(cvEvent);

    List<EsAnnenErfaring> annenerfaring = esCv.getAnnenerfaring();

    assertThat(annenerfaring).hasSize(1);
    assertThat(annenerfaring.get(0).getFraDato()).isEqualTo("2000-01-15");
    assertThat(annenerfaring.get(0).getTilDato()).isEqualTo("2001-01-15");
    assertThat(annenerfaring.get(0).getBeskrivelse()).isEqualTo("Annen erfaring beskrivelse");
  }

  @Test
  public void esCvTransformerShouldConvertCvEventToEsCvWithCorrectSertifikat() {

    CvEvent cvEvent = CvEventObjectMother.giveMeCvEvent();
    EsCv esCv = esCvTransformer.transform(cvEvent);

    List<EsSertifikat> sertifikat = esCv.getSertifikat();

    assertThat(sertifikat).hasSize(1);
    assertThat(sertifikat.get(0).getFraDato()).isEqualTo("2000-01-15");
    assertThat(sertifikat.get(0).getTilDato()).isEqualTo("2001-01-15");
    assertThat(sertifikat.get(0).getSertifikatKode()).isEqualTo("sertifikatkode");
    assertThat(sertifikat.get(0).getSertifikatKodeTekst()).isEqualTo("sertifikatkode tekst");
    assertThat(sertifikat.get(0).getUtsteder()).isEqualTo("Sertifikat utsteder");
  }

  @Test
  public void esCvTransformerShouldConvertCvEventToEsCvWithCorrectForerkort() {

    CvEvent cvEvent = CvEventObjectMother.giveMeCvEvent();
    EsCv esCv = esCvTransformer.transform(cvEvent);

    List<EsForerkort> Forerkort = esCv.getForerkort();

    assertThat(Forerkort).hasSize(1);
    assertThat(Forerkort.get(0).getFraDato()).isEqualTo("2000-01-15");
    assertThat(Forerkort.get(0).getTilDato()).isEqualTo("2001-01-15");
    assertThat(Forerkort.get(0).getKlasse()).isEqualTo("Forerkortklasse");
    assertThat(Forerkort.get(0).getUtsteder()).isEqualTo("Forerkort utsteder");
    assertThat(Forerkort.get(0).getDisponererKjoretoy()).isEqualTo(true);
  }

  @Test
  public void esCvTransformerShouldConvertCvEventToEsCvWithCorrectSprak() {

    CvEvent cvEvent = CvEventObjectMother.giveMeCvEvent();
    EsCv esCv = esCvTransformer.transform(cvEvent);

    List<EsSprak> Sprak = esCv.getSprak();

    assertThat(Sprak).hasSize(1);
    assertThat(Sprak.get(0).getSprakKode()).isEqualTo("Språk kode");
    assertThat(Sprak.get(0).getSprakKodeTekst()).isEqualTo("Språk kode tekst");
    assertThat(Sprak.get(0).getMuntlig()).isEqualTo("Språk muntlig");
    assertThat(Sprak.get(0).getSkriftlig()).isEqualTo("Språk skriftlig");
  }

  @Test
  public void esCvTransformerShouldConvertCvEventToEsCvWithCorrectKurs() {

    CvEvent cvEvent = CvEventObjectMother.giveMeCvEvent();
    EsCv esCv = esCvTransformer.transform(cvEvent);

    List<EsKurs> kurs = esCv.getKurs();

    assertThat(kurs).hasSize(1);
    assertThat(kurs.get(0).getFraDato()).isEqualTo("2000-01-15");
    assertThat(kurs.get(0).getTilDato()).isEqualTo("2001-01-15");
    assertThat(kurs.get(0).getTittel()).isEqualTo("Kurs tittel");
    assertThat(kurs.get(0).getArrangor()).isEqualTo("Kurs arrangør");
    assertThat(kurs.get(0).getOmfangEnhet()).isEqualTo("Omfang enhet");
    assertThat(kurs.get(0).getOmfangVerdi()).isEqualTo(1);
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
