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
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class EsCvTransformerTest {

  private EsCvTransformer esCvTransformer = new EsCvTransformer();

  @Test
  public void toDateShouldConvertToLocalDateWithoutErrors() {
    CvEvent cvEvent = CvEventObjectMother.giveMeCvEvent();
    cvEvent.setFodselsdato("2000-01-01");
    EsCv esCv = esCvTransformer.transform(cvEvent);
    assertThat(esCv.getFodselsdato()).isEqualTo("2000-01-01");
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
//    final String FODSELSDATO = "2010-01-01";
    final String FORMIDLINGSGRUPPEKODE = "020202";
    final String EPOSTADRESSE = "test@test.no";
    final String STATSBORGERSKAP = "Norge";
    final Long ARENA_ID = 1L;
    final String BESKRIVELSE = "beskrivelse CvEvent";

    CvEvent cvEvent = CvEventObjectMother.giveMeCvEvent();
    EsCv esCv = esCvTransformer.transform(cvEvent);

    assertEquals(FORNAVN, esCv.getFornavn());
    assertEquals(ETTERNAVN, esCv.getEtternavn());
//    assertEquals(FODSELSDATO, esCv.getFodselsdato());
    assertEquals(FORMIDLINGSGRUPPEKODE, esCv.getFormidlingsgruppekode());
    assertEquals(EPOSTADRESSE, esCv.getEpostadresse());
    assertEquals(STATSBORGERSKAP, esCv.getStatsborgerskap());
    assertEquals(ARENA_ID, esCv.getArenaId());
    assertEquals(BESKRIVELSE, esCv.getBeskrivelse());
  }

  //TODO: LocalDate klarer ikke å parse datoene i CvEvent object mother


  @Test
  public void esCvTransformerShouldConvertCvEventToEsCvWithCorrectYrkeserfaring() {

    CvEvent cvEvent = CvEventObjectMother.giveMeCvEvent();
    EsCv esCv = esCvTransformer.transform(cvEvent);

    List<EsYrkeserfaring> yrkeserfaring = esCv.getYrkeserfaring();

    assertThat(yrkeserfaring).hasSize(1);
//    Assertions.assertThat(yrkeserfaring.get(0).getFraDato()).isEqualTo("01-01-2000");
//    Assertions.assertThat(yrkeserfaring.get(0).getTilDato()).isEqualTo("01-01-2000");
    assertThat(yrkeserfaring.get(0).getArbeidsgiver()).isEqualTo("Yrkeserfaring arbeidsgiver");
    assertThat(yrkeserfaring.get(0).getBeskrivelse()).isEqualTo("Yrkeserfaring beskrivelse");
    assertThat(yrkeserfaring.get(0).getNaceKode()).isEqualTo("Yrkeserfaring nacekode");
    assertThat(yrkeserfaring.get(0).getOrganisasjonsnummer()).isEqualTo("1");
    assertThat(yrkeserfaring.get(0).getSokekategori()).isEqualTo("Yrkeserfaring sokekategori");
    assertThat(yrkeserfaring.get(0).getStillingstittel())
        .isEqualTo("Yrkeserfaring stillingstittel");
    assertThat(yrkeserfaring.get(0).getStyrkKode()).isEqualTo("Yrkeserfaring styrkkode");
    assertThat(yrkeserfaring.get(0).getStyrkKodeTekst()).isEqualTo("Yrkeserfaring styrkkode tekst");

  }

  @Test
  public void esCvTransformerShouldConvertCvEventToEsCvWithCorrectUtdanning() {

    CvEvent cvEvent = CvEventObjectMother.giveMeCvEvent();
    EsCv esCv = esCvTransformer.transform(cvEvent);

    List<EsUtdanning> utdanning = esCv.getUtdanning();

    Assertions.assertThat(utdanning.size()).isEqualTo(1);
//    Assertions.assertThat(utdanning.get(0).getFraDato()).isEqualTo("01-01-2000");
//    Assertions.assertThat(utdanning.get(0).getTilDato()).isEqualTo("01-01-2001");
    Assertions.assertThat(utdanning.get(0).getGrad()).isEqualTo("grad");
    Assertions.assertThat(utdanning.get(0).getStudiepoeng()).isEqualTo("1");
    Assertions.assertThat(utdanning.get(0).getUtdannelsessted())
        .isEqualTo("Utdanning utdannelsessted");
    Assertions.assertThat(utdanning.get(0).getGeografiskSted())
        .isEqualTo("Utdanning geografisk sted");
    Assertions.assertThat(utdanning.get(0).getNusKode()).isEqualTo("Utdanning nusKode");
    Assertions.assertThat(utdanning.get(0).getNusKodeTekst()).isEqualTo("Utdanning nusKodeTekst");

  }
  
  @Test
  public void esCvTransformerShouldConvertCvEventToEsCvWithCorrectKompetanse() {
    
    CvEvent cvEvent = CvEventObjectMother.giveMeCvEvent();
    EsCv esCv = esCvTransformer.transform(cvEvent);

    List<EsKompetanse> kompetanse = esCv.getKompetanse();

    Assertions.assertThat(kompetanse).hasSize(1);
    Assertions.assertThat(kompetanse.get(0).getNavn()).isEqualTo("navn kompetanse");
  }

  @Test
  public void esCvTransformerShouldConvertCvEventToEsCvWithCorrectAnnenerfaring() {

    CvEvent cvEvent = CvEventObjectMother.giveMeCvEvent();
    EsCv esCv = esCvTransformer.transform(cvEvent);

    List<EsAnnenErfaring> annenerfaring = esCv.getAnnenerfaring();

    Assertions.assertThat(annenerfaring).hasSize(1);
//    Assertions.assertThat(annenerfaring.get(0).getFraDato()).isEqualTo("01-01-2000");
//    Assertions.assertThat(annenerfaring.get(0).getTilDato()).isEqualTo("01-01-2001");
    Assertions.assertThat(annenerfaring.get(0).getBeskrivelse()).isEqualTo("Annen erfaring beskrivelse");
  }
  
  @Test
  public void esCvTransformerShouldConvertCvEventToEsCvWithCorrectSertifikat() {

    CvEvent cvEvent = CvEventObjectMother.giveMeCvEvent();
    EsCv esCv = esCvTransformer.transform(cvEvent);

    List<EsSertifikat> sertifikat = esCv.getSertifikat();

    Assertions.assertThat(sertifikat).hasSize(1);
//    Assertions.assertThat(sertifikat.get(0).getFraDato()).isEqualTo("01-01-2000");
//    Assertions.assertThat(sertifikat.get(0).getTilDato()).isEqualTo("01-01-2001");
    Assertions.assertThat(sertifikat.get(0).getSertifikatKode()).isEqualTo("sertifikatkode");
    Assertions.assertThat(sertifikat.get(0).getSertifikatKodeTekst()).isEqualTo("sertifikatkode tekst");
    Assertions.assertThat(sertifikat.get(0).getUtsteder()).isEqualTo("Sertifikat utsteder");
  }
  
  @Test
  public void esCvTransformerShouldConvertCvEventToEsCvWithCorrectForerkort() {

    CvEvent cvEvent = CvEventObjectMother.giveMeCvEvent();
    EsCv esCv = esCvTransformer.transform(cvEvent);

    List<EsForerkort> Forerkort = esCv.getForerkort();

    Assertions.assertThat(Forerkort).hasSize(1);
//    Assertions.assertThat(Forerkort.get(0).getFraDato()).isEqualTo("01-01-2000");
//    Assertions.assertThat(Forerkort.get(0).getTilDato()).isEqualTo("01-01-2001");
    Assertions.assertThat(Forerkort.get(0).getKlasse()).isEqualTo("Forerkortklasse");
    Assertions.assertThat(Forerkort.get(0).getUtsteder()).isEqualTo("Forerkort utsteder");
    Assertions.assertThat(Forerkort.get(0).getDisponererKjoretoy()).isEqualTo(true);
  }
  
  @Test
  public void esCvTransformerShouldConvertCvEventToEsCvWithCorrectSprak() {

    CvEvent cvEvent = CvEventObjectMother.giveMeCvEvent();
    EsCv esCv = esCvTransformer.transform(cvEvent);

    List<EsSprak> Sprak = esCv.getSprak();

    Assertions.assertThat(Sprak).hasSize(1);
    Assertions.assertThat(Sprak.get(0).getSprakKode()).isEqualTo("Språk kode");
    Assertions.assertThat(Sprak.get(0).getSprakKodeTekst()).isEqualTo("Språk kode tekst");
    Assertions.assertThat(Sprak.get(0).getMuntlig()).isEqualTo("Språk muntlig");
    Assertions.assertThat(Sprak.get(0).getSkriftlig()).isEqualTo("Språk skriftlig");
  }
  
  @Test
  public void esCvTransformerShouldConvertCvEventToEsCvWithCorrectKurs() {

    CvEvent cvEvent = CvEventObjectMother.giveMeCvEvent();
    EsCv esCv = esCvTransformer.transform(cvEvent);

    List<EsKurs> kurs = esCv.getKurs();

    Assertions.assertThat(kurs).hasSize(1);
//    Assertions.assertThat(kurs.get(0).getFraDato()).isEqualTo("01-01-2000");
//    Assertions.assertThat(kurs.get(0).getTilDato()).isEqualTo("01-01-2001");
    Assertions.assertThat(kurs.get(0).getTittel()).isEqualTo("Kurs tittel");
    Assertions.assertThat(kurs.get(0).getArrangor()).isEqualTo("Kurs arrangør");
    Assertions.assertThat(kurs.get(0).getOmfangEnhet()).isEqualTo("Omfang enhet");
    Assertions.assertThat(kurs.get(0).getOmfangVerdi()).isEqualTo(1);
  }
  
  
  @Test
  public void esCvTransformerShouldConvertCvEventToEsCvWithCorrectVerv() {

    CvEvent cvEvent = CvEventObjectMother.giveMeCvEvent();
    EsCv esCv = esCvTransformer.transform(cvEvent);

    List<EsVerv> verv = esCv.getVerv();

    Assertions.assertThat(verv).hasSize(1);
//    Assertions.assertThat(verv.get(0).getFraDato()).isEqualTo("01-01-2000");
//    Assertions.assertThat(verv.get(0).getTilDato()).isEqualTo("01-01-2001");
    Assertions.assertThat(verv.get(0).getOrganisasjon()).isEqualTo("Verv organisasjon");
    Assertions.assertThat(verv.get(0).getTittel()).isEqualTo("verv tittel");
  }

}