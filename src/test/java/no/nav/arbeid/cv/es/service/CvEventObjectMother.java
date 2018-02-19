package no.nav.arbeid.cv.es.service;

import java.util.ArrayList;
import no.nav.arbeid.cv.events.Annenerfaring;
import no.nav.arbeid.cv.events.AnsettelsesforholdJobbonsker;
import no.nav.arbeid.cv.events.ArbeidstidsordningJobbonsker;
import no.nav.arbeid.cv.events.CvEvent;
import no.nav.arbeid.cv.events.Forerkort;
import no.nav.arbeid.cv.events.GeografiJobbonsker;
import no.nav.arbeid.cv.events.HeltidDeltidJobbonsker;
import no.nav.arbeid.cv.events.Kompetanse;
import no.nav.arbeid.cv.events.Kurs;
import no.nav.arbeid.cv.events.Omfang;
import no.nav.arbeid.cv.events.Sertifikat;
import no.nav.arbeid.cv.events.Sprak;
import no.nav.arbeid.cv.events.Utdanning;
import no.nav.arbeid.cv.events.Verv;
import no.nav.arbeid.cv.events.YrkeJobbonsker;
import no.nav.arbeid.cv.events.Yrkeserfaring;

public class CvEventObjectMother {

  public static CvEvent giveMeCvEvent() {

    Utdanning utdanning = new Utdanning(
        "01-01-2000",
        "01-01-2001",
        "grad",
        "1",
        "Utdanning utdannelsessted",
        "Utdanning geografisk sted",
        "Utdanning nusKode",
        "Utdanning nusKodeTekst"
    );
    ArrayList<Utdanning> utdanningsListe = new ArrayList<>();
    utdanningsListe.add(utdanning);

    Yrkeserfaring yrkeserfaring = new Yrkeserfaring(
        "01-01-2000",
        "01-01-2001",
        "Yrkeserfaring arbeidsgiver",
        "1",
        "Yrkeserfaring stillingstittel",
        "Yrkeserfaring beskrivelse",
        "Yrkeserfaring sokekategori",
        "Yrkeserfaring styrkkode",
        "Yrkeserfaring styrkkode tekst",
        "Yrkeserfaring nacekode"
    );

    ArrayList<Yrkeserfaring> yrkeserfaringsListe = new ArrayList<>();
    yrkeserfaringsListe.add(yrkeserfaring);

    Kompetanse kompetanse = new Kompetanse("navn kompetanse");
    ArrayList<Kompetanse> kompetanseList = new ArrayList<>();
    kompetanseList.add(kompetanse);

    Annenerfaring annenerfaring = new Annenerfaring(
        "01-01-2000",
        "01-01-2001",
        "Annen erfaring beskrivelse"
    );

    ArrayList<Annenerfaring> annenerfaringListe = new ArrayList<>();
    annenerfaringListe.add(annenerfaring);

    Sertifikat sertifikat = new Sertifikat(
        "01-01-2000",
        "01-01-2001",
        "sertifikatkode",
        "sertifikatkode tekst",
        "Sertifikat utsteder"
    );
    ArrayList<Sertifikat> sertifikatListe = new ArrayList<>();
    sertifikatListe.add(sertifikat);

    Forerkort forerkort = new Forerkort(
        "01-01-2000",
        "01-01-2001",
        "Forerkortklasse",
        "Forerkort utsteder",
        true
    );

    ArrayList<Forerkort> forerkortListe = new ArrayList<>();
    forerkortListe.add(forerkort);

    Sprak sprak = new Sprak(
        "Språk kode",
        "Språk kode tekst",
        "Språk muntlig",
        "Språk skriftlig"
    );

    ArrayList<Sprak> sprakListe = new ArrayList<>();
    sprakListe.add(sprak);

    Kurs kurs = new Kurs(
        "01-01-2000",
        "01-01-2001",
        "Kurs tittel",
        "Kurs arrangør",
        new Omfang(1, "Omfang enhet")
    );

    ArrayList<Kurs> kursListe = new ArrayList<>();
    kursListe.add(kurs);

    Verv verv = new Verv(
        "01-01-2000",
        "01-01-2001",
        "Verv organisasjon",
        "verv tittel"
    );

    ArrayList<Verv> vervListe = new ArrayList<>();
    vervListe.add(verv);

    GeografiJobbonsker geografiJobbonsker = new GeografiJobbonsker(
        "Geografikode tekst",
        "GeografiKode"
    );

    ArrayList<GeografiJobbonsker> geografiJobbonskerListe = new ArrayList<>();
    geografiJobbonskerListe.add(geografiJobbonsker);

    YrkeJobbonsker yrkeJobbonsker = new YrkeJobbonsker(
        "Yrke jobb ønskeStyrk Kode",
        "Yrke jobb ønske Styrk beskrivelse",
        true
    );

    ArrayList<YrkeJobbonsker> yrkeJobbonskerListe = new ArrayList<>();
    yrkeJobbonskerListe.add(yrkeJobbonsker);

    HeltidDeltidJobbonsker heltidDeltidJobbonsker = new HeltidDeltidJobbonsker(
        "HeltidDeltidKode",
        "HeltidDeltidKode Tekst"

    );

    ArrayList<HeltidDeltidJobbonsker> heltidDeltidJobbonskerListe = new ArrayList<>();
    heltidDeltidJobbonskerListe.add(heltidDeltidJobbonsker);

    AnsettelsesforholdJobbonsker ansettelsesforholdJobbonsker = new AnsettelsesforholdJobbonsker(
        "Ansettelsesforhold Kode",
        "Ansettelsesforhold Kode tekst"
    );

    ArrayList<AnsettelsesforholdJobbonsker> ansettelsesforholdJobbonskerListe = new ArrayList<>();
    ansettelsesforholdJobbonskerListe.add(ansettelsesforholdJobbonsker);

    ArbeidstidsordningJobbonsker arbeidstidsordningJobbonsker = new ArbeidstidsordningJobbonsker(
        "Arbeidstidsordning Kode",
        "Arbeidstidsordning Kode Tekst"
    );
    ArrayList<ArbeidstidsordningJobbonsker> arbeidstidsordningJobbonskerListe = new ArrayList<>();
    arbeidstidsordningJobbonskerListe.add(arbeidstidsordningJobbonsker);

    return new CvEvent(
        "Test",
        "Testersen",
        "2010-01-01",
        "020202",
        "test@test.no",
        "Norge",
        1L,
        "beskrivelse CvEvent",
        utdanningsListe,
        yrkeserfaringsListe,
        kompetanseList,
        annenerfaringListe,
        sertifikatListe,
        forerkortListe,
        sprakListe,
        kursListe,
        vervListe,
        geografiJobbonskerListe,
        yrkeJobbonskerListe,
        heltidDeltidJobbonskerListe,
        ansettelsesforholdJobbonskerListe,
        arbeidstidsordningJobbonskerListe
    );
  }

}
