package no.nav.arbeid.cv.es.config.temp;

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

public class TempCvEventObjectMother {

  public static CvEvent giveMeCvEvent() {

    Utdanning utdanning = new Utdanning(
        "2000-01-15",
        "2001-01-15",
        "grad",
        "1",
        "Utdanning utdannelsessted",
        "Utdanning geografisk sted",
        "UtdanningNusKode",
        "nusKodeTekst"
    );

    ArrayList<Utdanning> utdanningsListe = new ArrayList<>();
    utdanningsListe.add(utdanning);

    Yrkeserfaring yrkeserfaring1 = new Yrkeserfaring(
        "2000-01-15",
        "2001-01-15",
        "Yrkeserfaring arbeidsgiver",
        "1",
        "greenkeeper",
        "Yrkeserfaring beskrivelse",
        "Yrkeserfaring sokekategori",
        "2345.22",
        "Yrkeserfaring styrkkode tekst",
        "Yrkeserfaring nacekode"
    );
    
    Yrkeserfaring yrkeserfaring2 = new Yrkeserfaring(
        "2000-01-15",
        "2001-01-15",
        "Yrkeserfaring arbeidsgiver",
        "1",
        "greenkeeper",
        "Yrkeserfaring beskrivelse",
        "Yrkeserfaring sokekategori",
        "2345.21",
        "Yrkeserfaring styrkkode tekst",
        "Yrkeserfaring nacekode"
    );

    ArrayList<Yrkeserfaring> yrkeserfaringsListe = new ArrayList<>();
    yrkeserfaringsListe.add(yrkeserfaring1);
    yrkeserfaringsListe.add(yrkeserfaring2);

    ArrayList<Kompetanse> kompetanseList = new ArrayList<>();
    kompetanseList.add(new Kompetanse("navn kompetanse"));
    kompetanseList.add(new Kompetanse("navn kompetanse2"));
    kompetanseList.add(new Kompetanse("min kompetanse"));
    kompetanseList.add(new Kompetanse("min andre kompetanse"));
    kompetanseList.add(new Kompetanse("min andre kompetanse"));
    kompetanseList.add(new Kompetanse("mindre biter"));
    kompetanseList.add(new Kompetanse("minstemål"));
    kompetanseList.add(new Kompetanse("minst er best"));
    kompetanseList.add(new Kompetanse("minstemann"));

    Annenerfaring annenerfaring = new Annenerfaring(
        "2000-01-15",
        "2001-01-15",
        "Annen erfaring beskrivelse"
    );

    ArrayList<Annenerfaring> annenerfaringListe = new ArrayList<>();
    annenerfaringListe.add(annenerfaring);

    Sertifikat sertifikat = new Sertifikat(
        "2000-01-15",
        "2001-01-15",
        "sertifikatkode",
        "sertifikatkode tekst",
        "Sertifikat utsteder"
    );

    ArrayList<Sertifikat> sertifikatListe = new ArrayList<>();
    sertifikatListe.add(sertifikat);

    Forerkort forerkort = new Forerkort(
        "2000-01-15",
        "2001-01-15",
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
        "2000-01-15",
        "2001-01-15",
        "Kurs tittel",
        "Kurs arrangør",
        new Omfang(1, "Omfang enhet")
    );

    ArrayList<Kurs> kursListe = new ArrayList<>();
    kursListe.add(kurs);

    Verv verv = new Verv(
        "2000-01-15",
        "2001-01-15",
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
        "2000-01-15",
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
