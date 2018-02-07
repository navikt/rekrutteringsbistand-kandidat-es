package no.nav.arbeid.cv.es.service;

import java.util.ArrayList;
import no.nav.arbeid.cv.events.CvEvent;
import no.nav.arbeid.cv.events.Utdanning;
import no.nav.arbeid.cv.events.Yrkeserfaring;

public class CvEventObjectMother {

  public static CvEvent giveMeCvEvent() {

    Utdanning utdanning = new Utdanning(
        "01-01-01",
        "01-01-02",
        "grad",
        "1",
        "utdannelsessted",
        "geografisk sted",
        "nusKode",
        "nusKodeTekst"
    );
    ArrayList<Utdanning> utdanningsListe = new ArrayList<>();
    utdanningsListe.add(utdanning);

    Yrkeserfaring yrkeserfaring = new Yrkeserfaring(
        "01-01-01",
        "01-01-02",
        "arbeidsgiver",
        "1",
        "stillingstittel",
        "beskrivelse",
        "sokekategori",
        "styrkkode",
        "styrkkode tekst",
        "nacekode"
    );
    ArrayList<Yrkeserfaring> yrkeserfaringsListe = new ArrayList<>();
    yrkeserfaringsListe.add(yrkeserfaring);

    return new CvEvent(
        "Test",
        "Testersen",
        "010101",
        "020202",
        "test@test.no",
        "Norge",
        0101L,
        "beskrivelse",
        utdanningsListe,

    );
  }

    return null;
  }
}
