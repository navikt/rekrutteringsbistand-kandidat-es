package no.nav.arbeid.cv.es.service;

import static org.junit.Assert.assertEquals;

import no.nav.arbeid.cv.es.domene.EsCv;
import no.nav.arbeid.cv.events.CvEvent;
import org.junit.Test;

public class EsCvTransformerTest {


  EsCvTransformer esCvTransformer = new EsCvTransformer();


  @Test
  public void toDateShouldConvertToLocalDateWithoutErrors() {


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
    final String FODSELSDATO = "010101";
    final String FORMIDLINGSGRUPPEKODE = "020202";
    final String EPOSTADRESSE = "test@test.no";
    final String STATSBORGERSKAP = "Norge";
    final long ARENA_ID = 0101L;
    final String BESKRIVELSE = "beskrivelse CvEvent";

    CvEvent cvEvent = CvEventObjectMother.giveMeCvEvent();
    EsCv esCv = esCvTransformer.transform(cvEvent);

    assertEquals(FORNAVN, esCv.getFornavn());
    assertEquals(ETTERNAVN, esCv.getEtternavn());
    assertEquals(FODSELSDATO, esCv.getFodselsdato());
    assertEquals(FORMIDLINGSGRUPPEKODE, esCv.getFormidlingsgruppekode());
    assertEquals(EPOSTADRESSE, esCv.getEpostadresse());
    assertEquals(STATSBORGERSKAP, esCv.getStatsborgerskap());
    assertEquals(ARENA_ID, java.util.Optional.ofNullable(esCv.getArenaId()));
    assertEquals(BESKRIVELSE, esCv.getBeskrivelse());
  }

  //TODO: LocalDate klarer ikke å parse datoene i CvEvent object mother

}