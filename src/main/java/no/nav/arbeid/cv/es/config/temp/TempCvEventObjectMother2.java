package no.nav.arbeid.cv.es.config.temp;

import java.util.ArrayList;
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

public class TempCvEventObjectMother2 {

  public static CvEvent giveMeCvEvent() {

    Utdanning utdanning = new Utdanning(
        "1999-08-20",
        "2002-06-20",
        "Hamar Katedralskole",
        "296647",
        "Studiespesialisering",
        "Studiespesialisering med realfag"
    );

    ArrayList<Utdanning> utdanningsListe = new ArrayList<>();
    utdanningsListe.add(utdanning);
    Yrkeserfaring yrkeserfaring1 = new Yrkeserfaring(
        "2000-01-01",
        "2002-01-01",
        "Kodesentralen Vardø",
        "5746.07",
        "Programvareutvikler",
        "Fullstackutvikler",
        "YRKE_ORGNR",
        "YRKE_NACEKODE"
    );

    Yrkeserfaring yrkeserfaring2 = new Yrkeserfaring(
        "2003-01-01",
        "2003-04-01",
        "Programvarefabrikken Førde",
        "5746.07",
        "Systemutvikler",
        "Utvikling av nytt kandidatsøk",
        "YRKE_ORGNR",
        "YRKE_NACEKODE"
    );

    Yrkeserfaring yrkeserfaring3 = new Yrkeserfaring(
        "2003-04-01",
        "2003-07-01",
        "Tjenestetest Norge",
        "6859.02",
        "Systemtester",
        "Automatiske tester av nytt kandidatsøk",
        "YRKE_ORGNR",
        "YRKE_NACEKODE"
    );

    Yrkeserfaring yrkeserfaring4 = new Yrkeserfaring(
        "2005-08-01",
        "2016-07-01",
        "lagerarbeiderne L. H.",
        "8659.03",
        "Lagermedarbeider",
        "Lagermedarbeider",
        "YRKE_ORGNR",
        "YRKE_NACEKODE"
    );

    Yrkeserfaring yrkeserfaring5 = new Yrkeserfaring(
        "2016-06-01",
        "2017-04-01",
        "lagerarbeiderne L. H.",
        "8659.03",
        "Truckfører lager",
        "Stortruck",
        "YRKE_ORGNR",
        "YRKE_NACEKODE"
    );

    Yrkeserfaring yrkeserfaring6 = new Yrkeserfaring(
        "2017-10-01",
        null,
        "Awesome coders AS",
        "5746.07",
        "Javautvikler",
        "Javautvikler",
        "YRKE_ORGNR",
        "YRKE_NACEKODE"
    );

    ArrayList<Yrkeserfaring> yrkeserfaringsListe = new ArrayList<>();
    yrkeserfaringsListe.add(yrkeserfaring1);
    yrkeserfaringsListe.add(yrkeserfaring2);
    yrkeserfaringsListe.add(yrkeserfaring3);
    yrkeserfaringsListe.add(yrkeserfaring4);
    yrkeserfaringsListe.add(yrkeserfaring5);
    yrkeserfaringsListe.add(yrkeserfaring6);

    Kompetanse kompetanse1 = new Kompetanse(
        "2016-03-14",
        "265478",
        "Javautvikler",
        null,
        null
    );

    Kompetanse kompetanse2 = new Kompetanse(
        "2016-03-14",
        "265478",
        "Programvareutvikler",
        "Programvareutvikler",
        null
    );

    Kompetanse kompetanse3 = new Kompetanse(
        "2016-03-14",
        "475136",
        "Lagermedarbeider",
        "Lagermedarbeider",
        null
    );

    Kompetanse kompetanse4 = new Kompetanse(
        "2016-03-14",
        "475869",
        "Truckfører",
        "Truckfører",
        null
    );

    ArrayList<Kompetanse> kompetanseList = new ArrayList<>();
    kompetanseList.add(kompetanse1);
    kompetanseList.add(kompetanse2);
    kompetanseList.add(kompetanse3);
    kompetanseList.add(kompetanse4);



    Sertifikat sertifikat1 = new Sertifikat(
        "1994-08-01",
        null,
        "V1.6050",
        "Førerkort: Kl. A (tung motorsykkel)",
        null,
        ""
    );

    Sertifikat sertifikat2 = new Sertifikat(
        "1991-01-01",
        null,
        "V1.6070",
        "Førerkort: Kl. BE (personbil/varebil og tilhenger)",
        null,
        ""
    );

    Sertifikat sertifikat3 = new Sertifikat(
        "1996-02-01",
        "2020-12-01",
        "V1.6110",
        "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn",
        "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn",
        ""
    );
    Sertifikat sertifikat4 = new Sertifikat(
        "1995-01-01",
        null,
        "A1.6820",
        "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn",
        "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn",
        ""
    );
    Sertifikat sertifikat5 = new Sertifikat(
        "1995-01-01",
        null,
        "A1.6820",
        "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn",
        "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn",
        ""
    );

    ArrayList<Sertifikat> sertifikatListe = new ArrayList<>();
    sertifikatListe.add(sertifikat1);
    sertifikatListe.add(sertifikat2);
    sertifikatListe.add(sertifikat3);
    sertifikatListe.add(sertifikat4);
    sertifikatListe.add(sertifikat5);

    Forerkort forerkort1 = new Forerkort(
        "1994-08-01",
        null,
        "V1.6050",
        "Førerkort: Kl. A (tung motorsykkel)",
        null,
        ""
    );

    Forerkort forerkort2 = new Forerkort(
        "1991-01-01",
        null,
        "V1.6070",
        "Førerkort: Kl. BE (personbil/varebil og tilhenger)",
        null,
        ""
    );

    Forerkort forerkort3 = new Forerkort(
        "1996-02-01",
        "2020-12-01",
        "V1.6110",
        "Førerkort: Kl. CE (lastebil og tilhenger)",
        null,
        ""
    );

    Forerkort forerkort4 = new Forerkort(
        "1996-02-01",
        "2020-12-01",
        "V1.6145",
        "Førerkort: Kl. DE (buss og tilhenger)",
        null,
        ""
    );

    ArrayList<Forerkort> forerkortListe = new ArrayList<>();
    forerkortListe.add(forerkort1);
    forerkortListe.add(forerkort2);
    forerkortListe.add(forerkort3);
    forerkortListe.add(forerkort4);


    Sprak sprak = new Sprak(
        "2012-12-01",
        "Språk kode",
        "Språk kode tekst",
        "Språk alternativ tekst",
        "Språk beskrivelse"
    );

    ArrayList<Sprak> sprakListe = new ArrayList<>();
    sprakListe.add(sprak);

    Kurs kurs1 = new Kurs(
        "2012-12-01",
        null,
        "Akseloppretting",
        "Easy-Laser",
        new Omfang(null, null),
        null
    );

    Kurs kurs2 = new Kurs(
        "2015-06-01",
        null,
        "Varme arbeider Sertifikat",
        "Norsk brannvernforening",
        new Omfang(5, "ÅR"),
        null
    );

    Kurs kurs3 = new Kurs(
        "2016-02-01",
        null,
        "Flensarbeid for Norsk Olje og Gass",
        "Torqlite Europa a/s",
        new Omfang(4, "ÅR"),
        null
    );


    ArrayList<Kurs> kursListe = new ArrayList<>();
    kursListe.add(kurs1);
    kursListe.add(kurs2);
    kursListe.add(kurs3);

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
        "05236984567",
        "KARI",
        "NORDMANN",
        "1980-02-10",
        false,
        "ARBS",
        "unnasluntrer2@mailinator.com",
        "NO",
        2L,
        null,
        "",
        "J",
        "2016-05-30",
        "Dinvei 2",
        "",
        "",
        "1337",
        "HUSKER IKKE",
        "NO",
        301,
        false,
        false,
        false,
        "",
        utdanningsListe,
        yrkeserfaringsListe,
        kompetanseList,
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