package no.nav.arbeid.cv.kandidatsok.domene.sok;

import java.util.ArrayList;

public class KandidatsokCvObjectMother {

  public static EsCv giveMeCv() {

    EsUtdanning utdanning = new EsUtdanning("355211", "Mekaniske fag, grunnkurs");
    ArrayList<EsUtdanning> utdanningsListe = new ArrayList<>();
    utdanningsListe.add(utdanning);

    EsYrkeserfaring yrkeserfaring1 = new EsYrkeserfaring("Anleggsmaskinfører", 24);
    EsYrkeserfaring yrkeserfaring2 = new EsYrkeserfaring("Anleggsmaskinfører", 4);
    EsYrkeserfaring yrkeserfaring3 = new EsYrkeserfaring("Anleggsmaskinfører", 3);
    EsYrkeserfaring yrkeserfaring4 = new EsYrkeserfaring("Industrimekaniker", 135);
    EsYrkeserfaring yrkeserfaring5 = new EsYrkeserfaring("Lastebil- og trailersjåfør", 10);
    EsYrkeserfaring yrkeserfaring6 = new EsYrkeserfaring("Industrimekaniker", 14);
    ArrayList<EsYrkeserfaring> yrkeserfaringsListe = new ArrayList<>();
    yrkeserfaringsListe.add(yrkeserfaring1);
    yrkeserfaringsListe.add(yrkeserfaring2);
    yrkeserfaringsListe.add(yrkeserfaring3);
    yrkeserfaringsListe.add(yrkeserfaring4);
    yrkeserfaringsListe.add(yrkeserfaring5);
    yrkeserfaringsListe.add(yrkeserfaring6);

    return new EsCv("01016012345", "ARBS", 1L, "CV51953", 301, utdanningsListe,
        yrkeserfaringsListe);
  }

}
