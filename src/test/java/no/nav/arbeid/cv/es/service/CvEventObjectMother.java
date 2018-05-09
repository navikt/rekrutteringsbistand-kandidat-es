package no.nav.arbeid.cv.es.service;

import no.nav.arbeid.cv.events.*;

import java.util.ArrayList;

public class CvEventObjectMother {

  public static CvEvent giveMeCvEvent() {

    Utdanning utdanning = new Utdanning(
        "1988-08-20",
        "1989-06-20",
        "Otta vgs. Otta",
        "355211",
        "Mekaniske fag, grunnkurs",
        "GK maskin/mekaniker"
    );

    ArrayList<Utdanning> utdanningsListe = new ArrayList<>();
    utdanningsListe.add(utdanning);
    Yrkeserfaring yrkeserfaring1 = new Yrkeserfaring(
        "2000-01-01",
        "2002-01-01",
        "Stentransport, Kragerø",
        "8342.01",
        "Anleggsmaskinfører",
        "maskinkjører og maskintransport",
        "YRKE_ORGNR",
        "YRKE_NACEKODE",
        false
    );

    Yrkeserfaring yrkeserfaring2 = new Yrkeserfaring(
        "2003-01-01",
        "2003-04-01",
        "AF-Pihl, Hammerfest",
        "8342.01",
        "Anleggsmaskinfører",
        "maskinkjører og maskintransport",
        "YRKE_ORGNR",
        "YRKE_NACEKODE",
        false
    );

    Yrkeserfaring yrkeserfaring3 = new Yrkeserfaring(
        "2003-04-01",
        "2003-07-01",
        "O.K. Hagalia, Slependen",
        "8342.01",
        "Anleggsmaskinfører",
        "maskinkjører og maskintransport",
        "YRKE_ORGNR",
        "YRKE_NACEKODE",
        false
    );

    Yrkeserfaring yrkeserfaring4 = new Yrkeserfaring(
        "2005-08-01",
        "2016-07-01",
        "Vard Group,avd.Brevik",
        "7233.03",
        "Industrimekaniker",
        "Industrimekaniker",
        "YRKE_ORGNR",
        "YRKE_NACEKODE",
        false
    );

    Yrkeserfaring yrkeserfaring5 = new Yrkeserfaring(
        "2016-06-01",
        "2017-04-01",
        "MTM anlegg",
        "8332.03",
        "Lastebil- og trailersjåfør",
        "Sjåfør kl. 2",
        "YRKE_ORGNR",
        "YRKE_NACEKODE",
        false
    );

    Yrkeserfaring yrkeserfaring6 = new Yrkeserfaring(
        "2017-10-01",
        null,
        "NLI  Grenland",
        "7233.03",
        "Industrimekaniker",
        "Industrimekaniker",
        "YRKE_ORGNR",
        "YRKE_NACEKODE",
        false
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
        "3020813",
        "Maskin- og kranførerarbeid",
        null,
        null
    );

    Kompetanse kompetanse2 = new Kompetanse(
        "2016-03-14",
        "3281301",
        "Mekanisk arbeid generelt",
        "Mekanisk arbeid generelt",
        null
    );

    Kompetanse kompetanse3 = new Kompetanse(
        "2016-03-14",
        "3220201",
        "Landtransport generelt",
        "Landtransport generelt",
        null
    );

    Kompetanse kompetanse4 = new Kompetanse(
        "2016-03-14",
        "212",
        "Industri (bransje)",
        "Mekanisk industri (bransje)",
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
        "01016012345",
        "OLA",
        "NORDMANN",
        "1960-01-01",
        false,
        "ARBS",
        "unnasluntrer@mailinator.com",
        "NO",
        1L,
        null,
        "",
        "J",
        "2016-05-30",
        "Minvei 1",
        "",
        "",
        "0654",
        "OSLO",
        "NO",
        301,
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
