package no.nav.arbeid.cv.kandidatsok.domene.es;

import no.nav.arbeid.cv.kandidatsok.es.domene.*;
import no.nav.arbeid.pam.kodeverk.ansettelse.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class EsCvObjectMother {

    private static final Logger LOGGER = LoggerFactory.getLogger(EsCvObjectMother.class);
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private static final String AKTORID1 = UUID.randomUUID().toString();
    private static final String AKTORID2 = UUID.randomUUID().toString();
    private static final String AKTORID3 = UUID.randomUUID().toString();
    private static final String AKTORID4 = UUID.randomUUID().toString();
    private static final String AKTORID5 = UUID.randomUUID().toString();
    private static final String AKTORID6 = UUID.randomUUID().toString();
    private static final String AKTORID7 = UUID.randomUUID().toString();
    private static final String AKTORID8 = UUID.randomUUID().toString();
    private static final String AKTORID9 = UUID.randomUUID().toString();
    private static final String AKTORID10 = UUID.randomUUID().toString();
    private static final String AKTORID11 = UUID.randomUUID().toString();
    private static final String AKTORID12 = UUID.randomUUID().toString();
    private static final String AKTORID13 = UUID.randomUUID().toString();
    private static final String AKTORID14 = UUID.randomUUID().toString();


    private static Date d(String string) {
        try {
            return string == null ? null : sdf.parse(string);
        } catch (ParseException e) {
            LOGGER.error("Feilet å parse " + string, e);
            return null;
        }
    }

    public static EsCv giveMeEsCvIkkeTemp() {

        EsUtdanning EsUtdanning = new EsUtdanning(d("1988-08-20"), d("1989-06-20"),
                "Otta vgs. Otta", "355211", "Mekaniske fag, grunnkurs", "GK maskin/mekaniker");

        ArrayList<EsUtdanning> EsUtdanningListe = new ArrayList<>();
        EsUtdanningListe.add(EsUtdanning);

        EsYrkeserfaring yrkeserfaring1 = new EsYrkeserfaring(d("2000-01-01"), d("2002-01-01"),
                "Stentransport, Kragerø", "8342.01", "Anleggsmaskinfører",
                "maskinkjører og maskintransport", "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring2 = new EsYrkeserfaring(d("2003-01-01"), d("2003-04-01"),
                "AF-Pihl, Hammerfest", "8342.01", "Anleggsmaskinfører",
                "maskinkjører og maskintransport", "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring3 = new EsYrkeserfaring(d("2003-04-01"), d("2003-07-01"),
                "O.K. Hagalia, Slependen", "8342.01", "Anleggsmaskinfører",
                "maskinkjører og maskintransport", "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring4 = new EsYrkeserfaring(d("2005-08-01"), d("2016-07-01"),
                "Vard Group,avd.Brevik", "7233.03", "Industrimekaniker", "Industrimekaniker",
                "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring5 = new EsYrkeserfaring(d("2016-06-01"), d("2017-04-01"),
                "MTM anlegg", "8332.03", "Lastebil- og trailersjåfør", "Sjåfør kl. 2", "YRKE_ORGNR",
                "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring6 = new EsYrkeserfaring(d("2017-10-01"), null, "NLI  Grenland",
                "7233.03", "Industrimekaniker", "Industrimekaniker", "YRKE_ORGNR", "YRKE_NACEKODE",
                false, Collections.emptyList());

        ArrayList<EsYrkeserfaring> yrkeserfaringListe = new ArrayList<>();
        yrkeserfaringListe.add(yrkeserfaring1);
        yrkeserfaringListe.add(yrkeserfaring2);
        yrkeserfaringListe.add(yrkeserfaring3);
        yrkeserfaringListe.add(yrkeserfaring4);
        yrkeserfaringListe.add(yrkeserfaring5);
        yrkeserfaringListe.add(yrkeserfaring6);

        EsKompetanse kompetanse1 = new EsKompetanse(d("2016-03-14"), "3020813",
                "Maskin- og kranførerarbeid", null, null, Collections.emptyList());

        EsKompetanse kompetanse2 = new EsKompetanse(d("2016-03-14"), "3281301",
                "Mekanisk arbeid generelt", "Mekanisk arbeid generelt", null, Collections.emptyList());

        EsKompetanse kompetanse3 = new EsKompetanse(d("2016-03-14"), "3220201",
                "Landtransport generelt", "Landtransport generelt", null, Collections.emptyList());

        EsKompetanse kompetanse4 = new EsKompetanse(d("2016-03-14"), "212", "Industri (bransje)",
                "Mekanisk industri (bransje)", null, Collections.emptyList());

        ArrayList<EsKompetanse> kompetanseListee = new ArrayList<>();
        kompetanseListee.add(kompetanse1);
        kompetanseListee.add(kompetanse2);
        kompetanseListee.add(kompetanse3);
        kompetanseListee.add(kompetanse4);

        EsSertifikat EsSertifikat1 = new EsSertifikat(d("1994-08-01"), null, "V1.6050",
                "A - Tung motorsykkel", null, "");

        EsSertifikat EsSertifikat2 = new EsSertifikat(d("1991-01-01"), null, "V1.6070",
                "BE - Personbil med tilhenger", null, "");

        EsSertifikat EsSertifikat3 = new EsSertifikat(d("1996-02-01"), d("2020-12-01"), "V1.6110",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn", "");
        EsSertifikat EsSertifikat4 = new EsSertifikat(d("1995-01-01"), null, "A1.6820",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn", "");
        EsSertifikat EsSertifikat5 = new EsSertifikat(d("1995-01-01"), null, "A1.6820",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn", "");

        ArrayList<EsSertifikat> EsSertifikatListe = new ArrayList<>();
        EsSertifikatListe.add(EsSertifikat1);
        EsSertifikatListe.add(EsSertifikat2);
        EsSertifikatListe.add(EsSertifikat3);
        EsSertifikatListe.add(EsSertifikat4);
        EsSertifikatListe.add(EsSertifikat5);

        EsForerkort forerkort2 = new EsForerkort(d("1991-01-01"), null, "V1.6070",
                "BE - Personbil med tilhenger", null, "");

        EsForerkort forerkort3 = new EsForerkort(d("1996-02-01"), d("2020-12-01"), "V1.6110",
                "CE - Lastebil med tilhenger", null, "");

        EsForerkort forerkort4 = new EsForerkort(d("1996-02-01"), d("2020-12-01"), "V1.6145",
                "DE - Buss med tilhenger", null, "");

        ArrayList<EsForerkort> forerkortListe = new ArrayList<>();
        forerkortListe.add(forerkort2);
        forerkortListe.add(forerkort3);
        forerkortListe.add(forerkort4);

        EsSprak sprak = new EsSprak(d("2012-12-01"), "Språk kode", "Språk kode tekst",
                "Språk alternativ tekst", "Språk beskrivelse");

        ArrayList<EsSprak> sprakListe = new ArrayList<>();
        sprakListe.add(sprak);

        EsKurs kurs1 = new EsKurs(d("2012-12-01"), null, "Akseloppretting", "Easy-Laser", null,
                null, null);
        EsKurs kurs2 = new EsKurs(d("2015-06-01"), null, "Varme arbeider EsSertifikat",
                "Norsk brannvernforening", "ÅR", 5, null);
        EsKurs kurs3 = new EsKurs(d("2016-02-01"), null, "Flensarbeid for Norsk Olje og Gass",
                "Torqlite Europa a/s", "ÅR", 4, null);

        ArrayList<EsKurs> kursListe = new ArrayList<>();
        kursListe.add(kurs1);
        kursListe.add(kurs2);
        kursListe.add(kurs3);

        EsVerv verv =
                new EsVerv(d("2000-01-15"), d("2001-01-15"), "EsVerv organisasjon", "verv tittel");

        ArrayList<EsVerv> vervListe = new ArrayList<>();
        vervListe.add(verv);

        EsGeografiJobbonsker geografiJobbonsker =
                new EsGeografiJobbonsker("Geografikode tekst", "GeografiKode");

        ArrayList<EsGeografiJobbonsker> geografiJobbonskerListe = new ArrayList<>();
        geografiJobbonskerListe.add(geografiJobbonsker);

        EsYrkeJobbonsker yrkeJobbonsker = new EsYrkeJobbonsker("Yrke jobb ønskeStyrk Kode",
                "Yrke jobb ønske Styrk beskrivelse", true, Collections.emptyList());

        ArrayList<EsYrkeJobbonsker> yrkeJobbonskerListe = new ArrayList<>();
        yrkeJobbonskerListe.add(yrkeJobbonsker);

        EsOmfangJobbonsker EsHeltidDeltidJobbonsker =
                new EsOmfangJobbonsker("HeltidDeltidKode", "HeltidDeltidKode Tekst");

        ArrayList<EsOmfangJobbonsker> EsHeltidDeltidJobbonskerListe = new ArrayList<>();
        EsHeltidDeltidJobbonskerListe.add(EsHeltidDeltidJobbonsker);

        EsAnsettelsesformJobbonsker EsAnsettelsesforholdJobbonsker =
                new EsAnsettelsesformJobbonsker("Ansettelsesforhold Kode",
                        "Ansettelsesforhold Kode tekst");

        ArrayList<EsAnsettelsesformJobbonsker> EsAnsettelsesforholdJobbonskerListe =
                new ArrayList<>();
        EsAnsettelsesforholdJobbonskerListe.add(EsAnsettelsesforholdJobbonsker);

        EsArbeidstidsordningJobbonsker EsArbeidstidsordningJobbonsker =
                new EsArbeidstidsordningJobbonsker("Arbeidstidsordning Kode",
                        "Arbeidstidsordning Kode Tekst");

        ArrayList<EsArbeidstidsordningJobbonsker> EsArbeidstidsordningJobbonskerListe =
                new ArrayList<>();
        EsArbeidstidsordningJobbonskerListe.add(EsArbeidstidsordningJobbonsker);

        EsCv esCv = new EsCv(AKTORID1, "03016012345", "OLA", "NORDMANN", d("1960-01-01"), false, "ARBS",
                "unnasluntrer@mailinator.com", "(+47) 22334455", "12345678", "NO", "1L",
                "", "J", d("2016-05-30"), "Minvei 1", "", "", "0654", "OSLO", "NO", 301, false,
                new Date(), 301, FALSE, null, null, null, "0316 NAV Gamle Oslo", FALSE, FALSE, "0301", "H149390", false);
        esCv.addUtdanning(EsUtdanningListe);
        esCv.addYrkeserfaring(yrkeserfaringListe);
        esCv.addKompetanse(kompetanseListee);
        esCv.addSertifikat(EsSertifikatListe);
        esCv.addForerkort(forerkortListe);
        esCv.addSprak(sprakListe);
        esCv.addKurs(kursListe);
        esCv.addVerv(vervListe);
        esCv.addGeografiJobbonske(geografiJobbonskerListe);
        esCv.addYrkeJobbonske(yrkeJobbonskerListe);
        // TODO:
        // esCv.addEsHeltidDeltidJobbonskerListe(EsHeltidDeltidJobbonskerListe);
        // esCv.addEsAnsettelsesforholdJobbonskerListe(EsAnsettelsesforholdJobbonskerListe);
        // esCv.addEsArbeidstidsordningJobbonskerListe(EsArbeidstidsordningJobbonskerListe);
        return esCv;
    }

    public static EsCv giveMeEsCv() {

        EsUtdanning utdanning = new EsUtdanning(d("1988-08-20"), d("1989-06-20"), "Otta vgs. Otta",
                "355211", "Mekaniske fag, grunnkurs", "GK maskin/mekaniker");

        EsUtdanning utdanning1 = new EsUtdanning(d("1988-08-20"), d("1989-06-20"),
                "Høyskolen i Gjøvik", "786595", "Master i sikkerhet", "Master i sikkerhet");


        ArrayList<EsUtdanning> utdanningsListe = new ArrayList<>();
        utdanningsListe.add(utdanning);
        utdanningsListe.add(utdanning1);
        EsYrkeserfaring yrkeserfaring1 = new EsYrkeserfaring(d("2000-01-01"), d("2000-01-10"),
                "Stentransport, Kragerø", "8341.01", "Anleggsmaskindrifter",
                "maskinkjører og maskintransport", "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring2 = new EsYrkeserfaring(d("2003-01-01"), d("2003-02-01"),
                "AF-Pihl, Hammerfest", "8342.01", "Anleggsmaskinfører",
                "maskinkjører og maskintransport", "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring3 = new EsYrkeserfaring(d("2003-04-01"), d("2003-05-01"),
                "O.K. Hagalia, Slependen", "8342.01", "Anleggsmaskinfører",
                "maskinkjører og maskintransport", "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring4 = new EsYrkeserfaring(d("2005-08-01"), d("2005-09-01"),
                "Vard Group,avd.Brevik", "7233.03", "Industrimekaniker", "Industrimekaniker",
                "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring5 = new EsYrkeserfaring(d("2016-06-01"), d("2016-07-01"),
                "MTM anlegg", "8332.03", "Lastebil- og trailersjåfør", "Sjåfør kl. 2", "YRKE_ORGNR",
                "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring6 = new EsYrkeserfaring(d("2017-10-01"), d("2017-12-01"),
                "NLI  Grenland", "7233.03", "Industrimekaniker", "Industrimekaniker", "YRKE_ORGNR",
                "YRKE_NACEKODE", false, Collections.emptyList());

        ArrayList<EsYrkeserfaring> yrkeserfaringsListe = new ArrayList<>();
        yrkeserfaringsListe.add(yrkeserfaring1);
        yrkeserfaringsListe.add(yrkeserfaring2);
        yrkeserfaringsListe.add(yrkeserfaring3);
        yrkeserfaringsListe.add(yrkeserfaring4);
        yrkeserfaringsListe.add(yrkeserfaring5);
        yrkeserfaringsListe.add(yrkeserfaring6);

        EsKompetanse kompetanse1 = new EsKompetanse(d("2016-03-14"), "3020813",
                "Maskin- og kranførerarbeid", null, null, Collections.emptyList());

        EsKompetanse kompetanse2 = new EsKompetanse(d("2016-03-14"), "3281301",
                "Mekanisk arbeid generelt", "Mekanisk arbeid generelt", null, Collections.emptyList());

        EsKompetanse kompetanse3 = new EsKompetanse(d("2016-03-14"), "506",
                "Landtransport generelt", "Landtransport generelt", null, Collections.emptyList());

        EsKompetanse kompetanse4 = new EsKompetanse(d("2016-03-14"), "212", "Industri (bransje)",
                "Mekanisk industri (bransje)", null, Collections.emptyList());

        ArrayList<EsKompetanse> kompetanseList = new ArrayList<>();
        kompetanseList.add(kompetanse1);
        kompetanseList.add(kompetanse2);
        kompetanseList.add(kompetanse3);
        kompetanseList.add(kompetanse4);

        EsSertifikat sertifikat1 = new EsSertifikat(d("1996-02-01"), d("2020-12-01"), "V1.6110",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 12 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 12 tonn", "");
        EsSertifikat sertifikat2 = new EsSertifikat(d("1995-01-01"), null, "A1.6820",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn", "");
        EsSertifikat sertifikat3 = new EsSertifikat(d("1995-01-01"), null, "A1.6820",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn", "");

        ArrayList<EsSertifikat> sertifikatListe = new ArrayList<>();
        sertifikatListe.add(sertifikat1);
        sertifikatListe.add(sertifikat2);
        sertifikatListe.add(sertifikat3);

        EsForerkort forerkort1 = new EsForerkort(d("1994-08-01"), null, "V1.6050",
                "A - Tung motorsykkel", null, "");

        EsForerkort forerkort2 = new EsForerkort(d("1991-01-01"), null, "V1.6070",
                "BE - Personbil med tilhenger", null, "");

        EsForerkort forerkort3 = new EsForerkort(d("1996-02-01"), d("2020-12-01"), "V1.6110",
                "CE - Lastebil med tilhenger", null, "");

        EsForerkort forerkort4 = new EsForerkort(d("1996-02-01"), d("2020-12-01"), "V1.6145",
                "DE - Buss med tilhenger", null, "");

        ArrayList<EsForerkort> forerkortListe = new ArrayList<>();
        forerkortListe.add(forerkort1);
        forerkortListe.add(forerkort2);
        forerkortListe.add(forerkort3);
        forerkortListe.add(forerkort4);


        EsSprak sprak1 =
                new EsSprak(d("2012-12-01"), "78874", "Norsk(skriftlig)", "Norwegian", "Morsmål");

        EsSprak sprak2 =
                new EsSprak(d("2012-12-01"), "78874", "Norsk(muntlig)", "Norwegian", "Morsmål");

        ArrayList<EsSprak> sprakListe = new ArrayList<>();
        sprakListe.add(sprak1);
        sprakListe.add(sprak2);

        EsKurs kurs1 = new EsKurs(d("2012-12-01"), null, "Akseloppretting", "Easy-Laser", null,
                null, null);

        EsKurs kurs2 = new EsKurs(d("2015-06-01"), null, "Varme arbeider Sertifikat",
                "Norsk brannvernforening", "ÅR", 5, null);

        EsKurs kurs3 = new EsKurs(d("2016-02-01"), null, "Flensarbeid for Norsk Olje og Gass",
                "Torqlite Europa a/s", "ÅR", 4, null);


        ArrayList<EsKurs> kursListe = new ArrayList<>();
        kursListe.add(kurs1);
        kursListe.add(kurs2);
        kursListe.add(kurs3);

        EsVerv verv =
                new EsVerv(d("2000-01-15"), d("2001-01-15"), "Verv organisasjon", "verv tittel");

        ArrayList<EsVerv> vervListe = new ArrayList<>();
        vervListe.add(verv);

        EsGeografiJobbonsker geografiJobbonsker = new EsGeografiJobbonsker("Oslo", "NO03");

        EsGeografiJobbonsker geografiJobbonsker1 =
                new EsGeografiJobbonsker("Lillehammer", "NO05.0501");

        EsGeografiJobbonsker geografiJobbonsker2 = new EsGeografiJobbonsker("Hedmark", "NO04");

        ArrayList<EsGeografiJobbonsker> geografiJobbonskerListe = new ArrayList<>();
        geografiJobbonskerListe.add(geografiJobbonsker);
        geografiJobbonskerListe.add(geografiJobbonsker1);
        geografiJobbonskerListe.add(geografiJobbonsker2);

        EsYrkeJobbonsker yrkeJobbonsker = new EsYrkeJobbonsker("Yrke jobb ønskeStyrk Kode",
                "Yrke jobb ønske Styrk beskrivelse", true, Collections.emptyList());

        ArrayList<EsYrkeJobbonsker> yrkeJobbonskerListe = new ArrayList<>();
        yrkeJobbonskerListe.add(yrkeJobbonsker);

        EsOmfangJobbonsker omfangJobbonsker =
                new EsOmfangJobbonsker(Omfang.HELTID.getId(), Omfang.HELTID.defaultTekst());

        List<EsOmfangJobbonsker> omfangJobbonskerList = new ArrayList<>();
        omfangJobbonskerList.add(omfangJobbonsker);

        EsAnsettelsesformJobbonsker ansettelsesforholdJobbonsker = new EsAnsettelsesformJobbonsker(
                Ansettelsesform.FAST.getId(), Ansettelsesform.FAST.defaultTekst());

        List<EsAnsettelsesformJobbonsker> ansettelsesforholdJobbonskerListe = new ArrayList<>();
        ansettelsesforholdJobbonskerListe.add(ansettelsesforholdJobbonsker);

        EsArbeidstidsordningJobbonsker arbeidstidsordningJobbonsker =
                new EsArbeidstidsordningJobbonsker(Arbeidstidsordning.SKIFT.getId(),
                        Arbeidstidsordning.SKIFT.defaultTekst());

        List<EsArbeidstidsordningJobbonsker> arbeidstidsordningJobbonskerListe = new ArrayList<>();
        arbeidstidsordningJobbonskerListe.add(arbeidstidsordningJobbonsker);


        EsArbeidstidJobbonsker arbeidstidJobbonsker = new EsArbeidstidJobbonsker(
                Arbeidstid.DAGTID.getId(), Arbeidstid.DAGTID.defaultTekst());
        List<EsArbeidstidJobbonsker> arbeidstidJobbonskerList = new ArrayList<>();
        arbeidstidJobbonskerList.add(arbeidstidJobbonsker);


        EsArbeidsdagerJobbonsker arbeidsdagerJobbonskerLoerdag = new EsArbeidsdagerJobbonsker(
                Arbeidsdager.LOERDAG.getId(), Arbeidsdager.LOERDAG.defaultTekst());
        EsArbeidsdagerJobbonsker arbeidsdagerJobbonskerSoendag = new EsArbeidsdagerJobbonsker(
                Arbeidsdager.SOENDAG.getId(), Arbeidsdager.SOENDAG.defaultTekst());

        List<EsArbeidsdagerJobbonsker> arbeidsdagerJobbonskerList = new ArrayList<>();
        arbeidsdagerJobbonskerList.add(arbeidsdagerJobbonskerLoerdag);
        arbeidsdagerJobbonskerList.add(arbeidsdagerJobbonskerSoendag);

        EsCv esCv = new EsCv(AKTORID2, "04016012345", "OLA", "NORDMANN", d("1960-01-01"), false, "JOBBS",
                "unnasluntrer@mailinator.com", "(+47) 22334455", "12345678", "NO", "1L",
                "hererjeg", "N", d("2016-05-30"), "Minvei 1", "", "", "0654", "OSLO", "NO", 5001, false,
                new Date(), 301, FALSE, null, "IKVAL", null, "0220 NAV Asker", FALSE, FALSE, true, false, "LEDIG_NAA", "5001", "H149390", false);
        esCv.addUtdanning(utdanningsListe);
        esCv.addYrkeserfaring(yrkeserfaringsListe);
        esCv.addKompetanse(kompetanseList);
        esCv.addSertifikat(sertifikatListe);
        esCv.addForerkort(forerkortListe);
        esCv.addSprak(sprakListe);
        esCv.addKurs(kursListe);
        esCv.addVerv(vervListe);
        esCv.addGeografiJobbonske(geografiJobbonskerListe);
        esCv.addYrkeJobbonske(yrkeJobbonskerListe);
        esCv.addArbeidstidsordningJobbonsker(arbeidstidsordningJobbonskerListe);
        esCv.addOmfangJobbonske(omfangJobbonskerList);
        esCv.addAnsettelsesformJobbonske(ansettelsesforholdJobbonskerListe);
        esCv.addArbeidsdagerJobbonsker(arbeidsdagerJobbonskerList);
        esCv.addArbeidstidJobbonsker(arbeidstidJobbonskerList);

        return esCv;
    }


    public static EsCv giveMeEsCv2() {

        EsUtdanning EsUtdanning =
                new EsUtdanning(d("1999-08-20"), d("2002-06-20"), "Hamar Katedralskole", "296647",
                        "Studiespesialisering", "Studiespesialisering med realfag");

        ArrayList<EsUtdanning> utdanningListe = new ArrayList<>();
        utdanningListe.add(EsUtdanning);
        EsYrkeserfaring yrkeserfaring1 = new EsYrkeserfaring(d("2000-01-01"), d("2002-01-01"),
                "Kodesentralen Vardø", "5746.07", "Programvareutvikler", "Fullstackutvikler",
                "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring2 = new EsYrkeserfaring(d("2003-01-01"), d("2003-07-01"),
                "Programvarefabrikken Førde", "5746.07", "Systemutvikler",
                "Utvikling av nytt kandidatsøk", "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring3 = new EsYrkeserfaring(d("2003-04-01"), d("2003-05-01"),
                "Tjenestetest Norge", "6859.02", "Systemtester",
                "Automatiske tester av nytt kandidatsøk", "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring4 = new EsYrkeserfaring(d("2005-08-01"), d("2006-07-01"),
                "lagerarbeiderne L. H.", "8659.03", "Lagermedarbeider", "Lagermedarbeider",
                "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring5 = new EsYrkeserfaring(d("2016-06-01"), d("2017-04-01"),
                "lagerarbeiderne L. H.", "8659.03", "Truckfører lager", "Stortruck", "YRKE_ORGNR",
                "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring6 =
                new EsYrkeserfaring(d("2017-10-01"), d("2019-09-26"), "Awesome coders AS", "5746.07",
                        "Javautvikler", "Javautvikler", "YRKE_ORGNR", "YRKE_NACEKODE", false, Arrays.asList("Javaprogrammerer", "Javawizard", "Javaguru"));

        ArrayList<EsYrkeserfaring> yrkeserfaringListe = new ArrayList<>();
        yrkeserfaringListe.add(yrkeserfaring1);
        yrkeserfaringListe.add(yrkeserfaring2);
        yrkeserfaringListe.add(yrkeserfaring3);
        yrkeserfaringListe.add(yrkeserfaring4);
        yrkeserfaringListe.add(yrkeserfaring5);
        yrkeserfaringListe.add(yrkeserfaring6);

        EsKompetanse kompetanse1 =
                new EsKompetanse(d("2016-03-14"), "265478", "Javautvikler", null, null, Collections.emptyList());

        EsKompetanse kompetanse2 = new EsKompetanse(d("2016-03-14"), "265478",
                "Programvareutvikler", "Programvareutvikler", null, Arrays.asList("Javaprogrammerer", "Java (8)", "JDK"));

        EsKompetanse kompetanse3 = new EsKompetanse(d("2016-03-14"), "475136", "Lagermedarbeider",
                "Lagermedarbeider", null, Collections.emptyList());

        EsKompetanse kompetanse4 =
                new EsKompetanse(d("2016-03-14"), "501", "Truckfører", "Truckfører", null, Collections.emptyList());

        ArrayList<EsKompetanse> kompetanseListe = new ArrayList<>();
        kompetanseListe.add(kompetanse1);
        kompetanseListe.add(kompetanse2);
        kompetanseListe.add(kompetanse3);
        kompetanseListe.add(kompetanse4);

        EsSertifikat EsSertifikat1 = new EsSertifikat(d("1996-02-01"), d("2020-12-01"), "V1.6110",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn", "");
        EsSertifikat EsSertifikat2 = new EsSertifikat(d("1995-01-01"), null, "A1.6820",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn", "");
        EsSertifikat EsSertifikat3 = new EsSertifikat(d("1995-01-01"), null, "A1.6820",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn", "");

        ArrayList<EsSertifikat> EsSertifikatListe = new ArrayList<>();
        EsSertifikatListe.add(EsSertifikat1);
        EsSertifikatListe.add(EsSertifikat2);
        EsSertifikatListe.add(EsSertifikat3);

        EsForerkort forerkort1 = new EsForerkort(d("1994-08-01"), null, "V1.6050",
                "A - Tung motorsykkel", null, "");

        EsForerkort forerkort2 = new EsForerkort(d("1991-01-01"), null, "V1.6070",
                "BE - Personbil med tilhenger", null, "");

        EsForerkort forerkort3 = new EsForerkort(d("1996-02-01"), d("2020-12-01"), "V1.6110",
                "CE - Lastebil med tilhenger", null, "");

        EsForerkort forerkort4 = new EsForerkort(d("1996-02-01"), d("2020-12-01"), "V1.6145",
                "DE - Buss med tilhenger", null, "");

        ArrayList<EsForerkort> forerkortListe = new ArrayList<>();
        forerkortListe.add(forerkort1);
        forerkortListe.add(forerkort2);
        forerkortListe.add(forerkort3);
        forerkortListe.add(forerkort4);


        EsSprak sprak = new EsSprak(d("2012-12-01"), "19093", "Norsk", "Norwegian", "Flytende");

        ArrayList<EsSprak> sprakListe = new ArrayList<>();
        sprakListe.add(sprak);

        EsKurs kurs1 = new EsKurs(d("2012-12-01"), null, "Akseloppretting", "Easy-Laser", null,
                null, null);

        EsKurs kurs2 = new EsKurs(d("2015-06-01"), null, "Varme arbeider EsSertifikat",
                "Norsk brannvernforening", "ÅR", 5, null);

        EsKurs kurs3 = new EsKurs(d("2016-02-01"), null, "Flensarbeid for Norsk Olje og Gass",
                "Torqlite Europa a/s", "ÅR", 4, null);


        ArrayList<EsKurs> kursListe = new ArrayList<>();
        kursListe.add(kurs1);
        kursListe.add(kurs2);
        kursListe.add(kurs3);

        EsVerv verv =
                new EsVerv(d("2000-01-15"), d("2001-01-15"), "EsVerv organisasjon", "verv tittel");

        ArrayList<EsVerv> vervListe = new ArrayList<>();
        vervListe.add(verv);

        EsGeografiJobbonsker geografiJobbonsker = new EsGeografiJobbonsker("Hedmark", "NO04");

        EsGeografiJobbonsker geografiJobbonsker1 =
                new EsGeografiJobbonsker("Stavanger", "NO11.1103");

        EsGeografiJobbonsker geografiJobbonsker2 = new EsGeografiJobbonsker("Førde", "NO14.1432");

        ArrayList<EsGeografiJobbonsker> geografiJobbonskerListe = new ArrayList<>();
        geografiJobbonskerListe.add(geografiJobbonsker);
        geografiJobbonskerListe.add(geografiJobbonsker1);
        geografiJobbonskerListe.add(geografiJobbonsker2);

        EsYrkeJobbonsker yrkeJobbonsker =
                new EsYrkeJobbonsker("Yrke jobb ønskeStyrk Kode", "Javaprogrammerer", true, Collections.emptyList());

        EsYrkeJobbonsker yrkeJobbonsker1 =
                new EsYrkeJobbonsker("Yrke jobb ønskeStyrk Kode", "Programvareutvikler", true, Collections.emptyList());

        EsYrkeJobbonsker yrkeJobbonsker2 =
                new EsYrkeJobbonsker("Yrke jobb ønskeStyrk Kode", "Bonde", true, Collections.emptyList());
        EsYrkeJobbonsker yrkeJobbonsker3 =
                new EsYrkeJobbonsker("1010.01", "Butikkmedarbeider", true, Collections.emptyList());

        ArrayList<EsYrkeJobbonsker> yrkeJobbonskerListe = new ArrayList<>();
        yrkeJobbonskerListe.add(yrkeJobbonsker);
        yrkeJobbonskerListe.add(yrkeJobbonsker1);
        yrkeJobbonskerListe.add(yrkeJobbonsker2);
        yrkeJobbonskerListe.add(yrkeJobbonsker3);

        EsOmfangJobbonsker EsHeltidDeltidJobbonsker =
                new EsOmfangJobbonsker("HeltidDeltidKode", "HeltidDeltidKode Tekst");

        ArrayList<EsOmfangJobbonsker> EsHeltidDeltidJobbonskerListe = new ArrayList<>();
        EsHeltidDeltidJobbonskerListe.add(EsHeltidDeltidJobbonsker);

        EsAnsettelsesformJobbonsker EsAnsettelsesforholdJobbonsker =
                new EsAnsettelsesformJobbonsker("Ansettelsesforhold Kode",
                        "Ansettelsesforhold Kode tekst");

        ArrayList<EsAnsettelsesformJobbonsker> EsAnsettelsesforholdJobbonskerListe =
                new ArrayList<>();
        EsAnsettelsesforholdJobbonskerListe.add(EsAnsettelsesforholdJobbonsker);

        EsArbeidstidsordningJobbonsker EsArbeidstidsordningJobbonsker =
                new EsArbeidstidsordningJobbonsker("Arbeidstidsordning Kode",
                        "Arbeidstidsordning Kode Tekst");

        ArrayList<EsArbeidstidsordningJobbonsker> EsArbeidstidsordningJobbonskerListe =
                new ArrayList<>();
        EsArbeidstidsordningJobbonskerListe.add(EsArbeidstidsordningJobbonsker);

        EsCv esCv = new EsCv(AKTORID3, "05236984567", "KARI", "NORDMANN", d("1980-02-10"), false, "PARBS",
                "unnasluntrer2@mailinator.com", "(+47) 22334455", "12345678", "NO", "2L",
                "Dette er beskrivelsen av hva jeg har gjort i min yrkeskarriere",
                "J", d("2016-05-30"), "Dinvei 2", "", "", "1337", "HUSKER IKKE", "NO", 301,
                false, new Date(), 401, FALSE, null, "IKVAL", null, "0316 NAV Gamle Oslo", FALSE, FALSE, "0401", "H149390", false);
        esCv.addUtdanning(utdanningListe);
        esCv.addYrkeserfaring(yrkeserfaringListe);
        esCv.addKompetanse(kompetanseListe);
        esCv.addSertifikat(EsSertifikatListe);
        esCv.addForerkort(forerkortListe);
        esCv.addSprak(sprakListe);
        esCv.addKurs(kursListe);
        esCv.addVerv(vervListe);
        esCv.addGeografiJobbonske(geografiJobbonskerListe);
        esCv.addYrkeJobbonske(yrkeJobbonskerListe);
        return esCv;
    }

    public static EsCv giveMeEsCv3() {

        EsUtdanning EsUtdanning = new EsUtdanning(d("1988-08-20"), d("1989-06-20"),
                "Norges Naturvitenskapelige Universitet", "456375", "Sosiologi", "Sosiologi");

        ArrayList<EsUtdanning> utdanningListe = new ArrayList<>();
        utdanningListe.add(EsUtdanning);
        EsYrkeserfaring yrkeserfaring1 = new EsYrkeserfaring(d("2000-01-01"), d("2000-02-01"),
                "Butikken i nærheten", "1010.01", "Butikkmedarbeider", "Butikkmedarbeider i Førde",
                "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring2 = new EsYrkeserfaring(d("2003-01-01"), d("2003-02-01"),
                "Butikken i nærheten", "1010.01", "Butikkmedarbeider(dagligvarer)",
                "Butikkmedarbeider(dagligvarer)", "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring3 = new EsYrkeserfaring(d("2003-04-01"), d("2003-05-01"),
                "Butikken langt unna", "1010.01", "Butikkmedarbeider(trevare)",
                "Butikkmedarbeider(trevare)", "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring4 = new EsYrkeserfaring(d("2005-08-01"), d("2005-09-01"),
                "Butikken", "4561.03", "Butikkmedarbeider(elektronikk)",
                "Butikkmedarbeider(elektronikk)", "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring5 =
                new EsYrkeserfaring(d("2016-06-01"), d("2016-07-01"), "Tvkanalen TV?", "5684.05",
                        "Presentør", "Presentør", "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring6 = new EsYrkeserfaring(d("2017-10-01"), null, "NLI  Grenland",
                "5684.05", "Nyhetsanker", "Nyhetsanker", "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        ArrayList<EsYrkeserfaring> yrkeserfaringListe = new ArrayList<>();
        yrkeserfaringListe.add(yrkeserfaring1);
        yrkeserfaringListe.add(yrkeserfaring2);
        yrkeserfaringListe.add(yrkeserfaring3);
        yrkeserfaringListe.add(yrkeserfaring4);
        yrkeserfaringListe.add(yrkeserfaring5);
        yrkeserfaringListe.add(yrkeserfaring6);

        EsKompetanse kompetanse1 =
                new EsKompetanse(d("2016-03-14"), "152424", "Presentør", null, null, Collections.emptyList());

        EsKompetanse kompetanse2 =
                new EsKompetanse(d("2016-03-14"), "152424", "Nyhetsanker", "Nyhetsanker", null, Collections.emptyList());

        EsKompetanse kompetanse3 = new EsKompetanse(d("2016-03-14"), "566895", "Butikkmedarbeider",
                "Butikkmedarbeider", null, Collections.emptyList());

        EsKompetanse kompetanse4 = new EsKompetanse(d("2016-03-14"), "566895",
                "Butikkmedarbeider(trevare)", "Butikkmedarbeider(trevare)", null, Collections.emptyList());

        ArrayList<EsKompetanse> kompetanseListe = new ArrayList<>();
        kompetanseListe.add(kompetanse1);
        kompetanseListe.add(kompetanse2);
        kompetanseListe.add(kompetanse3);
        kompetanseListe.add(kompetanse4);


        EsSertifikat EsSertifikat1 = new EsSertifikat(d("1996-02-01"), d("2020-12-01"), "V1.6110",
                "Truckførerbevis", "Truckførerbevis", "");
        EsSertifikat EsSertifikat2 = new EsSertifikat(d("1995-01-01"), null, "A1.6820",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn", "");
        EsSertifikat EsSertifikat3 = new EsSertifikat(d("1995-01-01"), null, "A1.6820",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn", "");
        EsSertifikat EsSertifikat4 = new EsSertifikat(d("1999-01-01"), null, "L2.7000",
                "Truckførerbevis T1 Lavtløftende plukktruck, palletruck m/perm. førerplass",
                "Truckførerbevis T1 Lavtløftende plukktruck, palletruck m/perm. førerplass", "");

        ArrayList<EsSertifikat> esSertifikatListe = new ArrayList<>();
        esSertifikatListe.add(EsSertifikat1);
        esSertifikatListe.add(EsSertifikat2);
        esSertifikatListe.add(EsSertifikat3);
        esSertifikatListe.add(EsSertifikat4);

        EsForerkort forerkort1 = new EsForerkort(d("1994-08-01"), null, "V1.6050",
                "A - Tung motorsykkel", null, "");

        EsForerkort forerkort2 = new EsForerkort(d("1991-01-01"), null, "V1.6070",
                "BE - Personbil med tilhenger", null, "");

        EsForerkort forerkort3 = new EsForerkort(d("1996-02-01"), d("2020-12-01"), "V1.6110",
                "CE - Lastebil med tilhenger", null, "");

        EsForerkort forerkort4 = new EsForerkort(d("1996-02-01"), d("2020-12-01"), "V1.6145",
                "T - Traktor", null, "");

        ArrayList<EsForerkort> forerkortListe = new ArrayList<>();
        forerkortListe.add(forerkort1);
        forerkortListe.add(forerkort2);
        forerkortListe.add(forerkort3);
        forerkortListe.add(forerkort4);


        EsSprak sprak = new EsSprak(d("2012-12-01"), "87392", "Engelsk", "English", "Flytende");

        ArrayList<EsSprak> sprakListe = new ArrayList<>();
        sprakListe.add(sprak);

        EsKurs kurs1 = new EsKurs(d("2012-12-01"), null, "Akseloppretting", "Easy-Laser", null,
                null, null);

        EsKurs kurs2 = new EsKurs(d("2015-06-01"), null, "Varme arbeider EsSertifikat",
                "Norsk brannvernforening", "ÅR", 5, null);

        EsKurs kurs3 = new EsKurs(d("2016-02-01"), null, "Flensarbeid for Norsk Olje og Gass",
                "Torqlite Europa a/s", "ÅR", 4, null);


        ArrayList<EsKurs> kursListe = new ArrayList<>();
        kursListe.add(kurs1);
        kursListe.add(kurs2);
        kursListe.add(kurs3);

        EsVerv verv =
                new EsVerv(d("2000-01-15"), d("2001-01-15"), "EsVerv organisasjon", "verv tittel");

        ArrayList<EsVerv> vervListe = new ArrayList<>();
        vervListe.add(verv);

        EsGeografiJobbonsker geografiJobbonsker = new EsGeografiJobbonsker("Hedmark", "NO04");

        EsGeografiJobbonsker geografiJobbonsker1 =
                new EsGeografiJobbonsker("Bergen", "NO12.1201");

        EsGeografiJobbonsker geografiJobbonsker2 = new EsGeografiJobbonsker("Hordaland", "NO12");

        ArrayList<EsGeografiJobbonsker> geografiJobbonskerListe = new ArrayList<>();
        geografiJobbonskerListe.add(geografiJobbonsker);
        geografiJobbonskerListe.add(geografiJobbonsker1);

        EsYrkeJobbonsker yrkeJobbonsker =
                new EsYrkeJobbonsker("Yrke jobb ønskeStyrk Kode", "Ordfører", true, Collections.emptyList());

        EsYrkeJobbonsker yrkeJobbonsker1 =
                new EsYrkeJobbonsker("Yrke jobb ønskeStyrk Kode", "Barnehageassistent", true, Collections.emptyList());

        EsYrkeJobbonsker yrkeJobbonsker2 =
                new EsYrkeJobbonsker("Yrke jobb ønskeStyrk Kode", "Tester", true, Collections.emptyList());

        EsYrkeJobbonsker yrkeJobbonsker3 =
                new EsYrkeJobbonsker("Yrke jobb ønskeStyrk Kode", "Konsulent (data)", true, Collections.emptyList());

        ArrayList<EsYrkeJobbonsker> yrkeJobbonskerListe = new ArrayList<>();
        yrkeJobbonskerListe.add(yrkeJobbonsker);
        yrkeJobbonskerListe.add(yrkeJobbonsker1);
        yrkeJobbonskerListe.add(yrkeJobbonsker2);
        yrkeJobbonskerListe.add(yrkeJobbonsker3);

        EsOmfangJobbonsker EsHeltidDeltidJobbonsker =
                new EsOmfangJobbonsker("HeltidDeltidKode", "HeltidDeltidKode Tekst");

        ArrayList<EsOmfangJobbonsker> EsHeltidDeltidJobbonskerListe = new ArrayList<>();
        EsHeltidDeltidJobbonskerListe.add(EsHeltidDeltidJobbonsker);

        EsAnsettelsesformJobbonsker EsAnsettelsesforholdJobbonsker =
                new EsAnsettelsesformJobbonsker("Ansettelsesforhold Kode",
                        "Ansettelsesforhold Kode tekst");

        ArrayList<EsAnsettelsesformJobbonsker> EsAnsettelsesforholdJobbonskerListe =
                new ArrayList<>();
        EsAnsettelsesforholdJobbonskerListe.add(EsAnsettelsesforholdJobbonsker);

        EsArbeidstidsordningJobbonsker EsArbeidstidsordningJobbonsker =
                new EsArbeidstidsordningJobbonsker("Arbeidstidsordning Kode",
                        "Arbeidstidsordning Kode Tekst");

        ArrayList<EsArbeidstidsordningJobbonsker> EsArbeidstidsordningJobbonskerListe =
                new ArrayList<>();
        EsArbeidstidsordningJobbonskerListe.add(EsArbeidstidsordningJobbonsker);

        EsCv esCv = new EsCv(AKTORID4, "04265983651", "HANS", "NORDMANN", d("1955-11-04"), false, "RARBS",
                "alltidmed@mailinator.com", "(+47) 22334455", "12345678", "NO", "3L",
                "Jeg jobber like godt selvstendig som i team",
                "J", d("2016-05-30"), "Minvei 1", "", "", "2323", "INGEBERG", "NO", 301, false,
                new Date(), 301, FALSE, null, "VARIG", null, "0220 NAV Asker", FALSE, FALSE, "0301", "H149390", true);
        esCv.addUtdanning(utdanningListe);
        esCv.addYrkeserfaring(yrkeserfaringListe);
        esCv.addKompetanse(kompetanseListe);
        esCv.addSertifikat(esSertifikatListe);
        esCv.addForerkort(forerkortListe);
        esCv.addSprak(sprakListe);
        esCv.addKurs(kursListe);
        esCv.addVerv(vervListe);
        esCv.addGeografiJobbonske(geografiJobbonskerListe);
        esCv.addYrkeJobbonske(yrkeJobbonskerListe);
        return esCv;
    }

    public static EsCv giveMeEsCv4() {

        EsUtdanning EsUtdanning = new EsUtdanning(d("1988-08-20"), d("1989-06-20"),
                "Norges Naturvitenskapelige Universitet", "456375", "Bygg og anlegg",
                "Bygg og anlegg");

        ArrayList<EsUtdanning> utdanningListe = new ArrayList<>();
        utdanningListe.add(EsUtdanning);
        EsYrkeserfaring yrkeserfaring1 = new EsYrkeserfaring(d("2000-01-01"), d("2002-01-01"),
                "Jokah", "1010.01", "Butikkmedarbeider", "Butikkmedarbeider", "YRKE_ORGNR",
                "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring2 = new EsYrkeserfaring(d("2003-01-01"), d("2003-04-01"),
                "Nærbutikkern", "1010.01", "Butikkmedarbeider(klesbutikk)",
                "Butikkmedarbeider(klebutikk)", "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring3 =
                new EsYrkeserfaring(d("2003-04-01"), d("2003-07-01"), "Tv tv tv", "5684.05",
                        "Nyhetspresentør", "Nyhetspresentør", "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring4 =
                new EsYrkeserfaring(d("2005-08-01"), d("2016-07-01"), "Vard Group,avd.Brevik",
                        "5684.05", "Hallovert", "Hallovert", "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring5 =
                new EsYrkeserfaring(d("2016-06-01"), d("2017-04-01"), "DN teater", "5124.46",
                        "Skuespiller", "Skuespiller", "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring6 = new EsYrkeserfaring(d("2017-10-01"), null,
                "Dukketeateret Rena", "5124.46", "Skuespiller(dukketeater)",
                "Skuespiller(dukketeater)", "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        ArrayList<EsYrkeserfaring> yrkeserfaringListe = new ArrayList<>();
        yrkeserfaringListe.add(yrkeserfaring1);
        yrkeserfaringListe.add(yrkeserfaring2);
        yrkeserfaringListe.add(yrkeserfaring3);
        yrkeserfaringListe.add(yrkeserfaring4);
        yrkeserfaringListe.add(yrkeserfaring5);
        yrkeserfaringListe.add(yrkeserfaring6);

        EsKompetanse kompetanse1 =
                new EsKompetanse(d("2016-03-14"), "152424", "Hallovert", null, null, Collections.emptyList());

        EsKompetanse kompetanse2 = new EsKompetanse(d("2016-03-14"), "566895", "Butikkmedarbeider",
                "Butikkmedarbeider", null, Collections.emptyList());

        EsKompetanse kompetanse3 = new EsKompetanse(d("2016-03-14"), "564646",
                "Butikkmedarbeider(klesbutikk)", "Butikkmedarbeider(klesbutikk)", null, Collections.emptyList());

        EsKompetanse kompetanse4 =
                new EsKompetanse(d("2016-03-14"), "506", "Skuespiller", "Skuespiller", null, Collections.emptyList());

        ArrayList<EsKompetanse> kompetanseListe = new ArrayList<>();
        kompetanseListe.add(kompetanse1);
        kompetanseListe.add(kompetanse2);
        kompetanseListe.add(kompetanse3);
        kompetanseListe.add(kompetanse4);


        EsSertifikat EsSertifikat1 = new EsSertifikat(d("1996-02-01"), d("2020-12-01"), "V1.6110",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn", "");
        EsSertifikat EsSertifikat2 = new EsSertifikat(d("1995-01-01"), null, "A1.6820",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn", "");
        EsSertifikat EsSertifikat3 = new EsSertifikat(d("1995-01-01"), null, "A1.6820",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn", "");

        ArrayList<EsSertifikat> EsSertifikatListe = new ArrayList<>();
        EsSertifikatListe.add(EsSertifikat1);
        EsSertifikatListe.add(EsSertifikat2);
        EsSertifikatListe.add(EsSertifikat3);

        EsForerkort forerkort2 = new EsForerkort(d("1991-01-01"), null, "V1.6070",
                "BE - Personbil med tilhenger", null, "");

        EsForerkort forerkort3 = new EsForerkort(d("1996-02-01"), d("2020-12-01"), "V1.6110",
                "CE - Lastebil med tilhenger", null, "");

        EsForerkort forerkort4 = new EsForerkort(d("1996-02-01"), d("2020-12-01"), "V1.6145",
                "DE - Buss med tilhenger", null, "");

        ArrayList<EsForerkort> forerkortListe = new ArrayList<>();
        // forerkortListe.add(forerkort2);
        // forerkortListe.add(forerkort3);
        forerkortListe.add(forerkort4);


        EsSprak sprak = new EsSprak(d("2012-12-01"), "78985", "Tysk", "German", "Begynner");

        ArrayList<EsSprak> sprakListe = new ArrayList<>();
        sprakListe.add(sprak);

        EsKurs kurs1 = new EsKurs(d("2012-12-01"), null, "Akseloppretting", "Easy-Laser", null,
                null, null);

        EsKurs kurs2 = new EsKurs(d("2015-06-01"), null, "Varme arbeider EsSertifikat",
                "Norsk brannvernforening", "ÅR", 5, null);

        EsKurs kurs3 = new EsKurs(d("2016-02-01"), null, "Flensarbeid for Norsk Olje og Gass",
                "Torqlite Europa a/s", "ÅR", 4, null);


        ArrayList<EsKurs> kursListe = new ArrayList<>();
        kursListe.add(kurs1);
        kursListe.add(kurs2);
        kursListe.add(kurs3);

        EsVerv verv =
                new EsVerv(d("2000-01-15"), d("2001-01-15"), "EsVerv organisasjon", "verv tittel");

        ArrayList<EsVerv> vervListe = new ArrayList<>();
        vervListe.add(verv);

        EsGeografiJobbonsker geografiJobbonsker = new EsGeografiJobbonsker("Oslo", "NO03.0301");

        EsGeografiJobbonsker geografiJobbonsker1 =
                new EsGeografiJobbonsker("Akershus", "NO02");

        EsGeografiJobbonsker geografiJobbonsker2 = new EsGeografiJobbonsker("Bærum", "NO02.1219");

        EsGeografiJobbonsker geografiJobbonsker3 = new EsGeografiJobbonsker("Norge", "NO");

        EsGeografiJobbonsker geografiJobbonsker4 = new EsGeografiJobbonsker("Karasjok", "NO02.2021");

        ArrayList<EsGeografiJobbonsker> geografiJobbonskerListe = new ArrayList<>();
        geografiJobbonskerListe.add(geografiJobbonsker);
        geografiJobbonskerListe.add(geografiJobbonsker1);
        geografiJobbonskerListe.add(geografiJobbonsker2);
        geografiJobbonskerListe.add(geografiJobbonsker3);
        geografiJobbonskerListe.add(geografiJobbonsker4);

        EsYrkeJobbonsker yrkeJobbonsker =
                new EsYrkeJobbonsker("Yrke jobb ønskeStyrk Kode", "Butikkmedarbeider", true, Collections.emptyList());

        EsYrkeJobbonsker yrkeJobbonsker1 =
                new EsYrkeJobbonsker("Yrke jobb ønskeStyrk Kode", "Butikkmedarbeider", true, Collections.emptyList());

        ArrayList<EsYrkeJobbonsker> yrkeJobbonskerListe = new ArrayList<>();
        yrkeJobbonskerListe.add(yrkeJobbonsker);
        yrkeJobbonskerListe.add(yrkeJobbonsker1);

        EsOmfangJobbonsker EsHeltidDeltidJobbonsker =
                new EsOmfangJobbonsker("HeltidDeltidKode", "HeltidDeltidKode Tekst");

        ArrayList<EsOmfangJobbonsker> EsHeltidDeltidJobbonskerListe = new ArrayList<>();
        EsHeltidDeltidJobbonskerListe.add(EsHeltidDeltidJobbonsker);

        EsAnsettelsesformJobbonsker EsAnsettelsesforholdJobbonsker =
                new EsAnsettelsesformJobbonsker("Ansettelsesforhold Kode",
                        "Ansettelsesforhold Kode tekst");

        ArrayList<EsAnsettelsesformJobbonsker> EsAnsettelsesforholdJobbonskerListe =
                new ArrayList<>();
        EsAnsettelsesforholdJobbonskerListe.add(EsAnsettelsesforholdJobbonsker);

        EsArbeidstidsordningJobbonsker EsArbeidstidsordningJobbonsker =
                new EsArbeidstidsordningJobbonsker("Arbeidstidsordning Kode",
                        "Arbeidstidsordning Kode Tekst");

        ArrayList<EsArbeidstidsordningJobbonsker> EsArbeidstidsordningJobbonskerListe =
                new ArrayList<>();
        EsArbeidstidsordningJobbonskerListe.add(EsArbeidstidsordningJobbonsker);

        EsCv esCv = new EsCv(AKTORID5, "09568410230", "HANNE", "NORDMANN", d("2002-06-04"), false, "ARBS",
                "erjegmed@mailinator.com", "(+47) 22334455", "12345678", "NO", "4L", "",
                "J", d("2016-05-30"), "Noensvei 1", "", "", "9730", "KARASJOK", "NO", 2021, false,
                new Date(), 2021, FALSE, null, "VURDI", null, "0602 NAV Drammen", FALSE, FALSE, "2021", "H149390", true);
        esCv.addUtdanning(utdanningListe);
        esCv.addYrkeserfaring(yrkeserfaringListe);
        esCv.addKompetanse(kompetanseListe);
        esCv.addSertifikat(EsSertifikatListe);
        esCv.addForerkort(forerkortListe);
        esCv.addSprak(sprakListe);
        esCv.addKurs(kursListe);
        esCv.addVerv(vervListe);
        esCv.addGeografiJobbonske(geografiJobbonskerListe);
        esCv.addYrkeJobbonske(yrkeJobbonskerListe);
        return esCv;
    }

    public static EsCv giveMeEsCv5() {

        ArrayList<EsUtdanning> utdanningListe = new ArrayList<>();


        EsYrkeserfaring yrkeserfaring1 = new EsYrkeserfaring(d("2000-01-01"), d("2000-02-01"),
                "Bankhvelvet BBL", "4865.75", "Bankhvelvoperatør", "Bankhvelvoperatør",
                "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring2 =
                new EsYrkeserfaring(d("2003-01-01"), d("2003-02-01"), "Proggehula", "5746.07",
                        "Progger", "Progger", "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring3 = new EsYrkeserfaring(d("2003-04-01"), d("2003-05-01"),
                "Test a.a.s", "6859.02", "Tester", "Tester", "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring4 =
                new EsYrkeserfaring(d("2005-08-01"), d("2005-09-01"), "K.O. kranservice", "8342.01",
                        "Kranoperatør", "Kranoperatør", "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.singletonList("Operatør av kran"));

        EsYrkeserfaring yrkeserfaring5 = new EsYrkeserfaring(d("2016-06-01"), d("2016-06-02"),
                "Lang transport A.S.", "8332.03", "Lastebil- og trailersjåfør", "Sjåfør kl. 3",
                "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring6 = new EsYrkeserfaring(d("2017-10-01"), d(null),
                "Mekken mekk", "7233.03", "Industrimekaniker", "Industrimekaniker", "YRKE_ORGNR",
                "YRKE_NACEKODE", false, Collections.emptyList());

        ArrayList<EsYrkeserfaring> yrkeserfaringListe = new ArrayList<>();
        yrkeserfaringListe.add(yrkeserfaring1);
        yrkeserfaringListe.add(yrkeserfaring2);
        yrkeserfaringListe.add(yrkeserfaring3);
        yrkeserfaringListe.add(yrkeserfaring4);
        yrkeserfaringListe.add(yrkeserfaring5);
        yrkeserfaringListe.add(yrkeserfaring6);

        EsKompetanse kompetanse1 = new EsKompetanse(d("2016-03-14"), "3020813",
                "Maskin- og kranførerarbeid(type 2 kran)", null, null, Collections.emptyList());

        EsKompetanse kompetanse2 = new EsKompetanse(d("2016-03-14"), "3281301",
                "Mekanisk arbeid spesielt", "Mekanisk arbeid spesielt", null, Collections.emptyList());

        EsKompetanse kompetanse3 = new EsKompetanse(d("2016-03-14"), "3220201",
                "Landtransport generelt", "Landtransport generelt", null, Collections.emptyList());

        EsKompetanse kompetanse4 = new EsKompetanse(d("2016-03-14"), "212", "Industri (bransje)",
                "Mekanisk industri (bransje)", null, Collections.emptyList());

        ArrayList<EsKompetanse> kompetanseListe = new ArrayList<>();
        kompetanseListe.add(kompetanse1);
        kompetanseListe.add(kompetanse2);
        kompetanseListe.add(kompetanse3);
        kompetanseListe.add(kompetanse4);


        EsSertifikat EsSertifikat1 = new EsSertifikat(d("1996-02-01"), d("2020-12-01"), "V1.6110",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn", "");
        EsSertifikat EsSertifikat2 = new EsSertifikat(d("1995-01-01"), null, "A1.6820",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn", "");
        EsSertifikat EsSertifikat3 = new EsSertifikat(d("1995-01-01"), null, "A1.6820",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn", "");

        ArrayList<EsSertifikat> EsSertifikatListe = new ArrayList<>();
        EsSertifikatListe.add(EsSertifikat1);
        EsSertifikatListe.add(EsSertifikat2);
        EsSertifikatListe.add(EsSertifikat3);

        EsForerkort forerkort1 = new EsForerkort(d("1994-08-01"), null, "V1.6050",
                "A - Tung motorsykkel", null, "");

        EsForerkort forerkort2 = new EsForerkort(d("1991-01-01"), null, "V1.6070",
                "BE - Personbil med tilhenger", null, "");

        EsForerkort forerkort3 = new EsForerkort(d("1996-02-01"), d("2020-12-01"), "V1.6110",
                "CE - Lastebil med tilhenger", null, "");

        EsForerkort forerkort4 = new EsForerkort(d("1996-02-01"), d("2020-12-01"), "V1.6145",
                "DE - Buss med tilhenger", null, "");

        ArrayList<EsForerkort> forerkortListe = new ArrayList<>();
        forerkortListe.add(forerkort1);
        // forerkortListe.add(forerkort2);
        // forerkortListe.add(forerkort3);
        forerkortListe.add(forerkort4);


        EsSprak sprak = new EsSprak(d("2012-12-01"), "78983", "Dansk", "Danish", "Uforståelig");

        ArrayList<EsSprak> sprakListe = new ArrayList<>();
        sprakListe.add(sprak);

        EsKurs kurs1 = new EsKurs(d("2012-12-01"), null, "Akseloppretting", "Easy-Laser", null,
                null, null);

        EsKurs kurs2 =
                new EsKurs(d("2015-06-01"), null, "Spring Boot", "Spring-folkene", "ÅR", 5, null);

        EsKurs kurs3 = new EsKurs(d("2016-02-01"), null, "Flensarbeid for Norsk Olje og Gass",
                "Torqlite Europa a/s", "ÅR", 4, null);


        ArrayList<EsKurs> kursListe = new ArrayList<>();
        kursListe.add(kurs1);
        kursListe.add(kurs2);
        kursListe.add(kurs3);

        EsVerv verv =
                new EsVerv(d("2000-01-15"), d("2001-01-15"), "EsVerv organisasjon", "verv tittel");

        ArrayList<EsVerv> vervListe = new ArrayList<>();
        vervListe.add(verv);

        EsGeografiJobbonsker geografiJobbonsker = new EsGeografiJobbonsker("Harstad", "NO19.1903");

        EsGeografiJobbonsker geografiJobbonsker1 = new EsGeografiJobbonsker("Sunnhordaland", "NO12.2200");

        EsGeografiJobbonsker geografiJobbonsker2 = new EsGeografiJobbonsker("Tromsø", "NO19.1902");

        EsGeografiJobbonsker geografiJobbonsker3 = new EsGeografiJobbonsker("Jessheim", "NO02.0219");

        ArrayList<EsGeografiJobbonsker> geografiJobbonskerListe = new ArrayList<>();
        geografiJobbonskerListe.add(geografiJobbonsker);
        geografiJobbonskerListe.add(geografiJobbonsker1);
        geografiJobbonskerListe.add(geografiJobbonsker2);
        geografiJobbonskerListe.add(geografiJobbonsker3);

        EsYrkeJobbonsker yrkeJobbonsker =
                new EsYrkeJobbonsker("Yrke jobb ønskeStyrk Kode", "Industrimekaniker", true, Collections.emptyList());

        EsYrkeJobbonsker yrkeJobbonsker1 =
                new EsYrkeJobbonsker("Yrke jobb ønskeStyrk Kode", "Lastebilsjåfør", true, Collections.singletonList("Trailersjåffis"));

        EsYrkeJobbonsker yrkeJobbonsker2 =
                new EsYrkeJobbonsker("Yrke jobb ønskeStyrk Kode", "Butikkmedarbeider", true, Collections.emptyList());

        EsYrkeJobbonsker yrkeJobbonsker3 =
                new EsYrkeJobbonsker("Yrke jobb ønskeStyrk Kode", "Konsulent (bank)", true, Collections.emptyList());

        ArrayList<EsYrkeJobbonsker> yrkeJobbonskerListe = new ArrayList<>();
        yrkeJobbonskerListe.add(yrkeJobbonsker);
        yrkeJobbonskerListe.add(yrkeJobbonsker1);
        yrkeJobbonskerListe.add(yrkeJobbonsker2);
        yrkeJobbonskerListe.add(yrkeJobbonsker3);

        EsOmfangJobbonsker EsHeltidDeltidJobbonsker =
                new EsOmfangJobbonsker("HeltidDeltidKode", "HeltidDeltidKode Tekst");

        ArrayList<EsOmfangJobbonsker> EsHeltidDeltidJobbonskerListe = new ArrayList<>();
        EsHeltidDeltidJobbonskerListe.add(EsHeltidDeltidJobbonsker);

        EsAnsettelsesformJobbonsker EsAnsettelsesforholdJobbonsker =
                new EsAnsettelsesformJobbonsker("Ansettelsesforhold Kode",
                        "Ansettelsesforhold Kode tekst");

        ArrayList<EsAnsettelsesformJobbonsker> EsAnsettelsesforholdJobbonskerListe =
                new ArrayList<>();
        EsAnsettelsesforholdJobbonskerListe.add(EsAnsettelsesforholdJobbonsker);

        EsArbeidstidsordningJobbonsker EsArbeidstidsordningJobbonsker =
                new EsArbeidstidsordningJobbonsker("Arbeidstidsordning Kode",
                        "Arbeidstidsordning Kode Tekst");

        ArrayList<EsArbeidstidsordningJobbonsker> EsArbeidstidsordningJobbonskerListe =
                new ArrayList<>();
        EsArbeidstidsordningJobbonskerListe.add(EsArbeidstidsordningJobbonsker);

        ArrayList<String> esVeilTilretteleggingsbehovListe =
                new ArrayList<>();
        esVeilTilretteleggingsbehovListe.add("Kat1_Kode");
        esVeilTilretteleggingsbehovListe.add("Kat2_Kode");



        EsCv esCv = new EsCv(AKTORID6, "03050316895", "BOB", "NORDMANN", d("1964-09-01"), false, "ARBS",
                "bobob@mailinator.com", "(+47) 22334455", "12345678", "NO", "5L", "", "J",
                d("2016-05-30"), "Minvei 90", "", "", "0219", "Bærum", "NO", 219, false, new Date(),
                219, FALSE, null, null, null, "0215 NAV Drøbak", FALSE, FALSE, "0219", "H149390", false);
        esCv.addUtdanning(utdanningListe);
        esCv.addYrkeserfaring(yrkeserfaringListe);
        esCv.addKompetanse(kompetanseListe);
        esCv.addSertifikat(EsSertifikatListe);
        esCv.addForerkort(forerkortListe);
        esCv.addSprak(sprakListe);
        esCv.addKurs(kursListe);
        esCv.addVerv(vervListe);
        esCv.addGeografiJobbonske(geografiJobbonskerListe);
        esCv.addYrkeJobbonske(yrkeJobbonskerListe);
        esCv.addVeilTilretteleggingsbehov(esVeilTilretteleggingsbehovListe);
        return esCv;
    }

    public static EsCv giveMeCvUtenKompetanse() {

        ArrayList<EsUtdanning> utdanningListe = new ArrayList<>();


        EsYrkeserfaring yrkeserfaring1 = new EsYrkeserfaring(d("2000-01-01"), d("2000-02-01"),
                "Bankhvelvet BBL", "4865.75", "Bankhvelvoperatør", "Bankhvelvoperatør",
                "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring2 =
                new EsYrkeserfaring(d("2003-01-01"), d("2003-02-01"), "Proggehula", "5746.07",
                        "Progger", "Progger", "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring3 = new EsYrkeserfaring(d("2003-04-01"), d("2003-05-01"),
                "Test a.a.s", "6859.02", "Tester", "Tester", "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring4 =
                new EsYrkeserfaring(d("2005-08-01"), d("2005-09-01"), "K.O. kranservice", "8342.01",
                        "Kranoperatør", "Kranoperatør", "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring5 = new EsYrkeserfaring(d("2016-06-01"), d("2016-06-02"),
                "Lang transport A.S.", "8332.03", "Lastebil- og trailersjåfør", "Sjåfør kl. 3",
                "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring6 = new EsYrkeserfaring(d("2017-10-01"), d("2017-11-01"),
                "Mekken mekk", "7233.03", "Industrimekaniker", "Industrimekaniker", "YRKE_ORGNR",
                "YRKE_NACEKODE", false, Collections.emptyList());

        ArrayList<EsYrkeserfaring> yrkeserfaringListe = new ArrayList<>();
        yrkeserfaringListe.add(yrkeserfaring1);
        yrkeserfaringListe.add(yrkeserfaring2);
        yrkeserfaringListe.add(yrkeserfaring3);
        yrkeserfaringListe.add(yrkeserfaring4);
        yrkeserfaringListe.add(yrkeserfaring5);
        yrkeserfaringListe.add(yrkeserfaring6);

        ArrayList<EsKompetanse> kompetanseListe = new ArrayList<>();


        EsSertifikat EsSertifikat1 = new EsSertifikat(d("1996-02-01"), d("2020-12-01"), "V1.6110",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn", "");
        EsSertifikat EsSertifikat2 = new EsSertifikat(d("1995-01-01"), null, "A1.6820",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn", "");
        EsSertifikat EsSertifikat3 = new EsSertifikat(d("1995-01-01"), null, "A1.6820",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn", "");

        ArrayList<EsSertifikat> EsSertifikatListe = new ArrayList<>();
        EsSertifikatListe.add(EsSertifikat1);
        EsSertifikatListe.add(EsSertifikat2);
        EsSertifikatListe.add(EsSertifikat3);

        EsForerkort forerkort1 = new EsForerkort(d("1994-08-01"), null, "V1.6050",
                "A - Tung motorsykkel", null, "");

        EsForerkort forerkort2 = new EsForerkort(d("1991-01-01"), null, "V1.6070",
                "BE - Personbil med tilhenger", null, "");

        EsForerkort forerkort3 = new EsForerkort(d("1996-02-01"), d("2020-12-01"), "V1.6110",
                "CE - Lastebil med tilhenger", null, "");

        EsForerkort forerkort4 = new EsForerkort(d("1996-02-01"), d("2020-12-01"), "V1.6145",
                "DE - Buss med tilhenger", null, "");

        ArrayList<EsForerkort> forerkortListe = new ArrayList<>();
        // forerkortListe.add(forerkort1);
        // forerkortListe.add(forerkort2);
        forerkortListe.add(forerkort3);
        // forerkortListe.add(forerkort4);


        EsSprak sprak = new EsSprak(d("2012-12-01"), "78983", "Dansk", "Danish", "Uforståelig");

        ArrayList<EsSprak> sprakListe = new ArrayList<>();
        sprakListe.add(sprak);

        EsKurs kurs1 = new EsKurs(d("2012-12-01"), null, "Akseloppretting", "Easy-Laser", null,
                null, null);

        EsKurs kurs2 =
                new EsKurs(d("2015-06-01"), null, "Spring Boot", "Spring-folkene", "ÅR", 5, null);

        EsKurs kurs3 = new EsKurs(d("2016-02-01"), null, "Flensarbeid for Norsk Olje og Gass",
                "Torqlite Europa a/s", "ÅR", 4, null);


        ArrayList<EsKurs> kursListe = new ArrayList<>();
        kursListe.add(kurs1);
        kursListe.add(kurs2);
        kursListe.add(kurs3);

        EsVerv verv =
                new EsVerv(d("2000-01-15"), d("2001-01-15"), "EsVerv organisasjon", "verv tittel");

        ArrayList<EsVerv> vervListe = new ArrayList<>();
        vervListe.add(verv);

        EsGeografiJobbonsker geografiJobbonsker = new EsGeografiJobbonsker("Harstad", "NO19.1903");

        EsGeografiJobbonsker geografiJobbonsker1 = new EsGeografiJobbonsker("Nordland", "NO18");

        EsGeografiJobbonsker geografiJobbonsker2 = new EsGeografiJobbonsker("Tromsø", "NO19.1902");

        ArrayList<EsGeografiJobbonsker> geografiJobbonskerListe = new ArrayList<>();
        geografiJobbonskerListe.add(geografiJobbonsker);
        geografiJobbonskerListe.add(geografiJobbonsker1);
        geografiJobbonskerListe.add(geografiJobbonsker2);

        EsYrkeJobbonsker yrkeJobbonsker =
                new EsYrkeJobbonsker("Yrke jobb ønskeStyrk Kode", "Industrimekaniker", true, Collections.emptyList());

        EsYrkeJobbonsker yrkeJobbonsker1 =
                new EsYrkeJobbonsker("Yrke jobb ønskeStyrk Kode", "Lastebilsjåfør", true, Collections.emptyList());

        EsYrkeJobbonsker yrkeJobbonsker2 =
                new EsYrkeJobbonsker("Yrke jobb ønskeStyrk Kode", "Butikkmedarbeider", true, Collections.emptyList());

        ArrayList<EsYrkeJobbonsker> yrkeJobbonskerListe = new ArrayList<>();
        yrkeJobbonskerListe.add(yrkeJobbonsker);
        yrkeJobbonskerListe.add(yrkeJobbonsker1);
        yrkeJobbonskerListe.add(yrkeJobbonsker2);

        EsOmfangJobbonsker EsHeltidDeltidJobbonsker =
                new EsOmfangJobbonsker("HeltidDeltidKode", "HeltidDeltidKode Tekst");

        ArrayList<EsOmfangJobbonsker> EsHeltidDeltidJobbonskerListe = new ArrayList<>();
        EsHeltidDeltidJobbonskerListe.add(EsHeltidDeltidJobbonsker);

        EsAnsettelsesformJobbonsker EsAnsettelsesforholdJobbonsker =
                new EsAnsettelsesformJobbonsker("Ansettelsesforhold Kode",
                        "Ansettelsesforhold Kode tekst");

        ArrayList<EsAnsettelsesformJobbonsker> EsAnsettelsesforholdJobbonskerListe =
                new ArrayList<>();
        EsAnsettelsesforholdJobbonskerListe.add(EsAnsettelsesforholdJobbonsker);

        EsArbeidstidsordningJobbonsker EsArbeidstidsordningJobbonsker =
                new EsArbeidstidsordningJobbonsker("Arbeidstidsordning Kode",
                        "Arbeidstidsordning Kode Tekst");

        ArrayList<EsArbeidstidsordningJobbonsker> EsArbeidstidsordningJobbonskerListe =
                new ArrayList<>();
        EsArbeidstidsordningJobbonskerListe.add(EsArbeidstidsordningJobbonsker);

        EsCv esCv = new EsCv(AKTORID7, "03050316895", "BOB", "NORDMANN", d("1964-09-01"), false, "ARBS",
                "bobob@mailinator.com", "(+47) 22334455", "12345678", "NO", "5L", "", "J",
                d("2016-05-30"), "Minvei 90", "", "", "0565", "OSLO", "NO", 301, false, new Date(),
                301, FALSE, null, null, null, null, FALSE, FALSE, "0301", "H149390", false);
        esCv.addUtdanning(utdanningListe);
        esCv.addYrkeserfaring(yrkeserfaringListe);
        esCv.addKompetanse(kompetanseListe);
        esCv.addSertifikat(EsSertifikatListe);
        esCv.addForerkort(forerkortListe);
        esCv.addSprak(sprakListe);
        esCv.addKurs(kursListe);
        esCv.addVerv(vervListe);
        esCv.addGeografiJobbonske(geografiJobbonskerListe);
        esCv.addYrkeJobbonske(yrkeJobbonskerListe);
        return esCv;
    }

    public static EsCv giveMeEsCvMedFeil() {

        EsUtdanning utdanning = new EsUtdanning(d("1988-08-20"), d("1989-06-20"), "Otta vgs. Otta",
                "355211", "Mekaniske fag, grunnkurs", "GK maskin/mekaniker");

        EsUtdanning utdanning2 = new EsUtdanning(d("1988-08-20"), d("1989-06-20"),
                "Høyskolen i Gjøvik", null, null, "Master i sikkerhet");


        ArrayList<EsUtdanning> utdanningsListe = new ArrayList<>();
        utdanningsListe.add(utdanning);
        utdanningsListe.add(utdanning2);
        EsYrkeserfaring yrkeserfaring1 = new EsYrkeserfaring(d("2000-01-01"), d("2000-01-10"),
                "Stentransport, Kragerø", "8341.01", "", "maskinkjører og maskintransport",
                "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring2 = new EsYrkeserfaring(d("2003-01-01"), d("2003-02-01"),
                "AF-Pihl, Hammerfest", "8342.01", "Anleggsmaskinfører",
                "maskinkjører og maskintransport", "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring3 = new EsYrkeserfaring(d("2003-04-01"), d("2003-05-01"),
                "O.K. Hagalia, Slependen", "8342.01", "Anleggsmaskinfører",
                "maskinkjører og maskintransport", "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring4 = new EsYrkeserfaring(d("2005-08-01"), d("2005-09-01"),
                "Vard Group,avd.Brevik", "7233.03", "Industrimekaniker", "Industrimekaniker",
                "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring5 = new EsYrkeserfaring(d("2016-06-01"), d("2016-07-01"),
                "MTM anlegg", "8332.03", "Lastebil- og trailersjåfør", "Sjåfør kl. 2", "YRKE_ORGNR",
                "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring6 = new EsYrkeserfaring(d("2017-10-01"), d("2017-12-01"),
                "NLI  Grenland", "7233.03", "Industrimekaniker", "Industrimekaniker", "YRKE_ORGNR",
                "YRKE_NACEKODE", false, Collections.emptyList());

        ArrayList<EsYrkeserfaring> yrkeserfaringsListe = new ArrayList<>();
        yrkeserfaringsListe.add(yrkeserfaring1);
        yrkeserfaringsListe.add(yrkeserfaring2);
        yrkeserfaringsListe.add(yrkeserfaring3);
        yrkeserfaringsListe.add(yrkeserfaring4);
        yrkeserfaringsListe.add(yrkeserfaring5);
        yrkeserfaringsListe.add(yrkeserfaring6);

        EsKompetanse kompetanse1 = new EsKompetanse(d("2016-03-14"), "3020813",
                "Maskin- og kranførerarbeid", null, null, Collections.emptyList());

        EsKompetanse kompetanse2 = new EsKompetanse(d("2016-03-14"), "3281301",
                "Mekanisk arbeid generelt", "Mekanisk arbeid generelt", null, Collections.emptyList());

        EsKompetanse kompetanse3 = new EsKompetanse(d("2016-03-14"), "506",
                "Landtransport generelt", "Landtransport generelt", null, Collections.emptyList());

        EsKompetanse kompetanse4 = new EsKompetanse(d("2016-03-14"), "212", "Industri (bransje)",
                "Mekanisk industri (bransje)", null, Collections.emptyList());

        ArrayList<EsKompetanse> kompetanseList = new ArrayList<>();
        kompetanseList.add(kompetanse1);
        kompetanseList.add(kompetanse2);
        kompetanseList.add(kompetanse3);
        kompetanseList.add(kompetanse4);


        EsSertifikat sertifikat1 = new EsSertifikat(d("1996-02-01"), d("2020-12-01"), "V1.6110",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 12 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 12 tonn", "");
        EsSertifikat sertifikat2 = new EsSertifikat(d("1995-01-01"), null, "A1.6820",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn", "");
        EsSertifikat sertifikat3 = new EsSertifikat(d("1995-01-01"), null, "A1.6820",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn", "");

        ArrayList<EsSertifikat> sertifikatListe = new ArrayList<>();
        sertifikatListe.add(sertifikat1);
        sertifikatListe.add(sertifikat2);
        sertifikatListe.add(sertifikat3);

        EsForerkort forerkort1 = new EsForerkort(d("1994-08-01"), null, "V1.6050",
                "A - Tung motorsykkel", null, "");

        EsForerkort forerkort2 = new EsForerkort(d("1991-01-01"), null, "V1.6070",
                "BE - Personbil med tilhenger", null, "");

        EsForerkort forerkort3 = new EsForerkort(d("1996-02-01"), d("2020-12-01"), "V1.6110",
                "CE - Lastebil med tilhenger", null, "");

        EsForerkort forerkort4 = new EsForerkort(d("1996-02-01"), d("2020-12-01"), "V1.6145",
                "DE - Buss med tilhenger", null, "");

        ArrayList<EsForerkort> forerkortListe = new ArrayList<>();
        forerkortListe.add(forerkort1);
        forerkortListe.add(forerkort2);
        // forerkortListe.add(forerkort3);
        // forerkortListe.add(forerkort4);


        EsSprak sprak1 =
                new EsSprak(d("2012-12-01"), "78874", "Norsk(skriftlig)", "Norwegian", "Morsmål");

        EsSprak sprak2 =
                new EsSprak(d("2012-12-01"), "78874", "Norsk(muntlig)", "Norwegian", "Morsmål");

        ArrayList<EsSprak> sprakListe = new ArrayList<>();
        sprakListe.add(sprak1);
        sprakListe.add(sprak2);

        EsKurs kurs1 = new EsKurs(d("2012-12-01"), null, "Akseloppretting", "Easy-Laser", null,
                null, null);

        EsKurs kurs2 = new EsKurs(d("2015-06-01"), null, "Varme arbeider Sertifikat",
                "Norsk brannvernforening", "ÅR", 5, null);

        EsKurs kurs3 = new EsKurs(d("2016-02-01"), null, "Flensarbeid for Norsk Olje og Gass",
                "Torqlite Europa a/s", "ÅR", 4, null);


        ArrayList<EsKurs> kursListe = new ArrayList<>();
        kursListe.add(kurs1);
        kursListe.add(kurs2);
        kursListe.add(kurs3);

        EsVerv verv =
                new EsVerv(d("2000-01-15"), d("2001-01-15"), "Verv organisasjon", "verv tittel");

        ArrayList<EsVerv> vervListe = new ArrayList<>();
        vervListe.add(verv);

        EsGeografiJobbonsker geografiJobbonsker = new EsGeografiJobbonsker("Hamar", "NO04.0403");

        EsGeografiJobbonsker geografiJobbonsker1 =
                new EsGeografiJobbonsker("Lillehammer", "NO05.0501");

        EsGeografiJobbonsker geografiJobbonsker2 = new EsGeografiJobbonsker("Hedmark", "NO04");

        ArrayList<EsGeografiJobbonsker> geografiJobbonskerListe = new ArrayList<>();
        geografiJobbonskerListe.add(geografiJobbonsker);
        geografiJobbonskerListe.add(geografiJobbonsker1);
        geografiJobbonskerListe.add(geografiJobbonsker2);

        EsYrkeJobbonsker yrkeJobbonsker = new EsYrkeJobbonsker("Yrke jobb ønskeStyrk Kode",
                "Yrke jobb ønske Styrk beskrivelse", true, Collections.emptyList());

        ArrayList<EsYrkeJobbonsker> yrkeJobbonskerListe = new ArrayList<>();
        yrkeJobbonskerListe.add(yrkeJobbonsker);

        EsOmfangJobbonsker heltidDeltidJobbonsker =
                new EsOmfangJobbonsker("HeltidDeltidKode", "HeltidDeltidKode Tekst");

        ArrayList<EsOmfangJobbonsker> heltidDeltidJobbonskerListe = new ArrayList<>();
        heltidDeltidJobbonskerListe.add(heltidDeltidJobbonsker);

        EsAnsettelsesformJobbonsker ansettelsesforholdJobbonsker = new EsAnsettelsesformJobbonsker(
                "Ansettelsesforhold Kode", "Ansettelsesforhold Kode tekst");

        ArrayList<EsAnsettelsesformJobbonsker> ansettelsesforholdJobbonskerListe =
                new ArrayList<>();
        ansettelsesforholdJobbonskerListe.add(ansettelsesforholdJobbonsker);

        EsArbeidstidsordningJobbonsker arbeidstidsordningJobbonsker =
                new EsArbeidstidsordningJobbonsker("Arbeidstidsordning Kode",
                        "Arbeidstidsordning Kode Tekst");

        ArrayList<EsArbeidstidsordningJobbonsker> arbeidstidsordningJobbonskerListe =
                new ArrayList<>();
        arbeidstidsordningJobbonskerListe.add(arbeidstidsordningJobbonsker);

        EsCv esCv = new EsCv(AKTORID8, "02016012345", "OLA", "NORDMANN", d("1960-01-01"), false, "ARBS",
                "unnasluntrer@mailinator.com", "(+47) 22334455", "12345678", "NO", "1L",
                "", "N", d("2016-05-30"), "Minvei 1", "", "", "0654", "OSLO", "NO", 301, false,
                new Date(), 301, FALSE, null, null, null, null, FALSE, FALSE, "0301", "H149390", false);
        esCv.addUtdanning(utdanningsListe);
        esCv.addYrkeserfaring(yrkeserfaringsListe);
        esCv.addKompetanse(kompetanseList);
        esCv.addSertifikat(sertifikatListe);
        esCv.addForerkort(forerkortListe);
        esCv.addSprak(sprakListe);
        esCv.addKurs(kursListe);
        esCv.addVerv(vervListe);
        esCv.addGeografiJobbonske(geografiJobbonskerListe);
        esCv.addYrkeJobbonske(yrkeJobbonskerListe);
        return esCv;
    }


    public static EsCv giveMeEsCv6() {


        EsUtdanning utdanning2 = new EsUtdanning(d("1988-08-20"), d("1989-06-20"), "UiO", "838838",
                "Sosialantropologiske fag", "Sosialantropologi gr. fag");


        ArrayList<EsUtdanning> utdanningsListe = new ArrayList<>();
        utdanningsListe.add(utdanning2);

        EsYrkeserfaring yrkeserfaring1 = new EsYrkeserfaring(d("2000-01-01"), d("2000-01-10"),
                "Stentransport, Kragerø", "8341.01", "", "maskinkjører og maskintransport",
                "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring2 = new EsYrkeserfaring(d("2003-01-01"), d("2003-02-01"),
                "AF-Pihl, Hammerfest", "8342.01", "Anleggsmaskinfører",
                "maskinkjører og maskintransport", "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring3 = new EsYrkeserfaring(d("2003-04-01"), d("2003-05-01"),
                "O.K. Hagalia, Slependen", "8342.01", "Anleggsmaskinfører",
                "maskinkjører og maskintransport", "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring4 = new EsYrkeserfaring(d("2005-08-01"), d("2005-09-01"),
                "Vard Group,avd.Brevik", "7233.03", "Industrimekaniker", "Industrimekaniker",
                "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring5 = new EsYrkeserfaring(d("2016-06-01"), d("2016-07-01"),
                "MTM anlegg", "8332.03", "Lastebil- og trailersjåfør", "Sjåfør kl. 2", "YRKE_ORGNR",
                "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring6 = new EsYrkeserfaring(d("2017-10-01"), d("2017-12-01"),
                "NLI  Grenland", "7233.03", "Industrimekaniker", "Industrimekaniker", "YRKE_ORGNR",
                "YRKE_NACEKODE", false, Collections.emptyList());

        ArrayList<EsYrkeserfaring> yrkeserfaringsListe = new ArrayList<>();
        yrkeserfaringsListe.add(yrkeserfaring1);
        yrkeserfaringsListe.add(yrkeserfaring2);
        yrkeserfaringsListe.add(yrkeserfaring3);
        yrkeserfaringsListe.add(yrkeserfaring4);
        yrkeserfaringsListe.add(yrkeserfaring5);
        yrkeserfaringsListe.add(yrkeserfaring6);

        EsKompetanse kompetanse1 = new EsKompetanse(d("2016-03-14"), "3020813",
                "Maskin- og kranførerarbeid", null, null, Collections.emptyList());

        EsKompetanse kompetanse2 = new EsKompetanse(d("2016-03-14"), "3281301",
                "Mekanisk arbeid generelt", "Mekanisk arbeid generelt", null, Collections.emptyList());

        EsKompetanse kompetanse3 = new EsKompetanse(d("2016-03-14"), "506",
                "Landtransport generelt", "Landtransport generelt", null, Collections.emptyList());

        EsKompetanse kompetanse4 = new EsKompetanse(d("2016-03-14"), "212", "Industri (bransje)",
                "Mekanisk industri (bransje)", null, Collections.emptyList());

        ArrayList<EsKompetanse> kompetanseList = new ArrayList<>();
        kompetanseList.add(kompetanse1);
        kompetanseList.add(kompetanse2);
        kompetanseList.add(kompetanse3);
        kompetanseList.add(kompetanse4);

        EsSertifikat sertifikat1 = new EsSertifikat(d("1994-08-01"), null, "V1.6050",
                "A - Tung motorsykkel", null, "");

        EsSertifikat sertifikat2 = new EsSertifikat(d("1991-01-01"), null, "V1.6070",
                "BE - Personbil med tilhenger", null, "");

        EsSertifikat sertifikat3 = new EsSertifikat(d("1996-02-01"), d("2020-12-01"), "V1.6110",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 12 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 12 tonn", "");
        EsSertifikat sertifikat4 = new EsSertifikat(d("1995-01-01"), null, "A1.6820",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn", "");
        EsSertifikat sertifikat5 = new EsSertifikat(d("1995-01-01"), null, "A1.6820",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn", "");

        ArrayList<EsSertifikat> sertifikatListe = new ArrayList<>();
        sertifikatListe.add(sertifikat1);
        sertifikatListe.add(sertifikat2);
        sertifikatListe.add(sertifikat3);
        sertifikatListe.add(sertifikat4);
        sertifikatListe.add(sertifikat5);

        EsForerkort forerkort1 = new EsForerkort(d("1994-08-01"), null, "V1.6050",
                "A - Tung motorsykkel", null, "");

        EsForerkort forerkort2 = new EsForerkort(d("1991-01-01"), null, "V1.6070",
                "BE - Personbil med tilhenger", null, "");

        EsForerkort forerkort3 = new EsForerkort(d("1996-02-01"), d("2020-12-01"), "V1.6110",
                "CE - Lastebil med tilhenger", null, "");

        EsForerkort forerkort4 = new EsForerkort(d("1996-02-01"), d("2020-12-01"), "V1.6145",
                "DE - Buss med tilhenger", null, "");

        ArrayList<EsForerkort> forerkortListe = new ArrayList<>();
        forerkortListe.add(forerkort1);
        // forerkortListe.add(forerkort2);
        // forerkortListe.add(forerkort3);
        // forerkortListe.add(forerkort4);


        EsSprak sprak1 =
                new EsSprak(d("2012-12-01"), "78874", "Norsk(skriftlig)", "Norwegian", "Morsmål");

        EsSprak sprak2 =
                new EsSprak(d("2012-12-01"), "78874", "Norsk(muntlig)", "Norwegian", "Morsmål");

        ArrayList<EsSprak> sprakListe = new ArrayList<>();
        sprakListe.add(sprak1);
        sprakListe.add(sprak2);

        EsKurs kurs1 = new EsKurs(d("2012-12-01"), null, "Akseloppretting", "Easy-Laser", null,
                null, null);

        EsKurs kurs2 = new EsKurs(d("2015-06-01"), null, "Varme arbeider Sertifikat",
                "Norsk brannvernforening", "ÅR", 5, null);

        EsKurs kurs3 = new EsKurs(d("2016-02-01"), null, "Flensarbeid for Norsk Olje og Gass",
                "Torqlite Europa a/s", "ÅR", 4, null);


        ArrayList<EsKurs> kursListe = new ArrayList<>();
        kursListe.add(kurs1);
        kursListe.add(kurs2);
        kursListe.add(kurs3);

        EsVerv verv =
                new EsVerv(d("2000-01-15"), d("2001-01-15"), "Verv organisasjon", "verv tittel");

        ArrayList<EsVerv> vervListe = new ArrayList<>();
        vervListe.add(verv);

        EsGeografiJobbonsker geografiJobbonsker = new EsGeografiJobbonsker("Hamar", "NO04.0403");

        EsGeografiJobbonsker geografiJobbonsker1 =
                new EsGeografiJobbonsker("Lillehammer", "NO05.0501");

        EsGeografiJobbonsker geografiJobbonsker2 = new EsGeografiJobbonsker("Voss", "NO12.2001");

        ArrayList<EsGeografiJobbonsker> geografiJobbonskerListe = new ArrayList<>();
        geografiJobbonskerListe.add(geografiJobbonsker);
        geografiJobbonskerListe.add(geografiJobbonsker1);
        geografiJobbonskerListe.add(geografiJobbonsker2);

        EsYrkeJobbonsker yrkeJobbonsker = new EsYrkeJobbonsker("Yrke jobb ønskeStyrk Kode",
                "Yrke jobb ønske Styrk beskrivelse", true, Collections.emptyList());

        ArrayList<EsYrkeJobbonsker> yrkeJobbonskerListe = new ArrayList<>();
        yrkeJobbonskerListe.add(yrkeJobbonsker);

        EsOmfangJobbonsker esOmfangJobbonsker =
                new EsOmfangJobbonsker(Omfang.HELTID.name(), Omfang.HELTID.defaultTekst());

        List<EsOmfangJobbonsker> omfangJobbonskerList = new ArrayList<>();
        omfangJobbonskerList.add(esOmfangJobbonsker);

        EsAnsettelsesformJobbonsker ansettelsesformJobbonsker = new EsAnsettelsesformJobbonsker(
                Ansettelsesform.FAST.name(), Ansettelsesform.FAST.defaultTekst());

        List<EsAnsettelsesformJobbonsker> ansettelsesformJobbonskerList = new ArrayList<>();
        ansettelsesformJobbonskerList.add(ansettelsesformJobbonsker);

        EsArbeidstidsordningJobbonsker arbeidstidsordningJobbonsker =
                new EsArbeidstidsordningJobbonsker("Arbeidstidsordning Kode",
                        "Arbeidstidsordning Kode Tekst");

        ArrayList<EsArbeidstidsordningJobbonsker> arbeidstidsordningJobbonskerListe =
                new ArrayList<>();
        arbeidstidsordningJobbonskerListe.add(arbeidstidsordningJobbonsker);

        EsCv esCv = new EsCv(AKTORID9, "01016034215", "OLA", "NORDMANN", d("1960-01-01"), false, "ARBS",
                "22339155@mailinator.com", "(+47) 22339155", "22339155", "NO", "6L",
                "", "N", d("2016-05-30"), "Minvei 1", "", "", "0654", "OSLO", "NO", 301, false,
                new Date(), 301, FALSE, "5", null, null, null, FALSE, FALSE, "0301", "H149390", true);
        esCv.addUtdanning(utdanningsListe);
        esCv.addYrkeserfaring(yrkeserfaringsListe);
        esCv.addKompetanse(kompetanseList);
        esCv.addSertifikat(sertifikatListe);
        esCv.addForerkort(forerkortListe);
        esCv.addSprak(sprakListe);
        esCv.addKurs(kursListe);
        esCv.addVerv(vervListe);
        esCv.addGeografiJobbonske(geografiJobbonskerListe);
        esCv.addYrkeJobbonske(yrkeJobbonskerListe);
        return esCv;
    }

    public static EsCv giveMeCvForDoedPerson() {


        EsUtdanning utdanning2 = new EsUtdanning(d("1988-08-20"), d("1989-06-20"), "UiO", "838838",
                "Sosialantropologiske fag", "Sosialantropologi gr. fag");


        ArrayList<EsUtdanning> utdanningsListe = new ArrayList<>();
        utdanningsListe.add(utdanning2);

        EsYrkeserfaring yrkeserfaring1 = new EsYrkeserfaring(d("2000-01-01"), d("2000-01-10"),
                "Stentransport, Kragerø", "8341.01", "", "maskinkjører og maskintransport",
                "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring2 = new EsYrkeserfaring(d("2003-01-01"), d("2003-02-01"),
                "AF-Pihl, Hammerfest", "8342.01", "Anleggsmaskinfører",
                "maskinkjører og maskintransport", "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring3 = new EsYrkeserfaring(d("2003-04-01"), d("2003-05-01"),
                "O.K. Hagalia, Slependen", "8342.01", "Anleggsmaskinfører",
                "maskinkjører og maskintransport", "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring4 = new EsYrkeserfaring(d("2005-08-01"), d("2005-09-01"),
                "Vard Group,avd.Brevik", "7233.03", "Industrimekaniker", "Industrimekaniker",
                "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring5 = new EsYrkeserfaring(d("2016-06-01"), d("2016-07-01"),
                "MTM anlegg", "8332.03", "Lastebil- og trailersjåfør", "Sjåfør kl. 2", "YRKE_ORGNR",
                "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring6 = new EsYrkeserfaring(d("2017-10-01"), d("2017-12-01"),
                "NLI  Grenland", "7233.03", "Industrimekaniker", "Industrimekaniker", "YRKE_ORGNR",
                "YRKE_NACEKODE", false, Collections.emptyList());

        ArrayList<EsYrkeserfaring> yrkeserfaringsListe = new ArrayList<>();
        yrkeserfaringsListe.add(yrkeserfaring1);
        yrkeserfaringsListe.add(yrkeserfaring2);
        yrkeserfaringsListe.add(yrkeserfaring3);
        yrkeserfaringsListe.add(yrkeserfaring4);
        yrkeserfaringsListe.add(yrkeserfaring5);
        yrkeserfaringsListe.add(yrkeserfaring6);

        EsKompetanse kompetanse1 = new EsKompetanse(d("2016-03-14"), "3020813",
                "Maskin- og kranførerarbeid", null, null, Collections.emptyList());

        EsKompetanse kompetanse2 = new EsKompetanse(d("2016-03-14"), "3281301",
                "Mekanisk arbeid generelt", "Mekanisk arbeid generelt", null, Collections.emptyList());

        EsKompetanse kompetanse3 = new EsKompetanse(d("2016-03-14"), "506",
                "Landtransport generelt", "Landtransport generelt", null, Collections.emptyList());

        EsKompetanse kompetanse4 = new EsKompetanse(d("2016-03-14"), "212", "Industri (bransje)",
                "Mekanisk industri (bransje)", null, Collections.emptyList());

        ArrayList<EsKompetanse> kompetanseList = new ArrayList<>();
        kompetanseList.add(kompetanse1);
        kompetanseList.add(kompetanse2);
        kompetanseList.add(kompetanse3);
        kompetanseList.add(kompetanse4);

        EsSertifikat sertifikat1 = new EsSertifikat(d("1994-08-01"), null, "V1.6050",
                "A - Tung motorsykkel", null, "");

        EsSertifikat sertifikat2 = new EsSertifikat(d("1991-01-01"), null, "V1.6070",
                "BE - Personbil med tilhenger", null, "");

        EsSertifikat sertifikat3 = new EsSertifikat(d("1996-02-01"), d("2020-12-01"), "V1.6110",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 12 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 12 tonn", "");
        EsSertifikat sertifikat4 = new EsSertifikat(d("1995-01-01"), null, "A1.6820",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn", "");
        EsSertifikat sertifikat5 = new EsSertifikat(d("1995-01-01"), null, "A1.6820",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn", "");

        ArrayList<EsSertifikat> sertifikatListe = new ArrayList<>();
        sertifikatListe.add(sertifikat1);
        sertifikatListe.add(sertifikat2);
        sertifikatListe.add(sertifikat3);
        sertifikatListe.add(sertifikat4);
        sertifikatListe.add(sertifikat5);

        EsForerkort forerkort1 = new EsForerkort(d("1994-08-01"), null, "V1.6050",
                "A - Tung motorsykkel", null, "");

        EsForerkort forerkort2 = new EsForerkort(d("1991-01-01"), null, "V1.6070",
                "BE - Personbil med tilhenger", null, "");

        EsForerkort forerkort3 = new EsForerkort(d("1996-02-01"), d("2020-12-01"), "V1.6110",
                "CE - Lastebil med tilhenger", null, "");

        EsForerkort forerkort4 = new EsForerkort(d("1996-02-01"), d("2020-12-01"), "V1.6145",
                "DE - Buss med tilhenger", null, "");

        ArrayList<EsForerkort> forerkortListe = new ArrayList<>();
        forerkortListe.add(forerkort1);
        forerkortListe.add(forerkort2);
        forerkortListe.add(forerkort3);
        forerkortListe.add(forerkort4);


        EsSprak sprak1 =
                new EsSprak(d("2012-12-01"), "78874", "Norsk(skriftlig)", "Norwegian", "Morsmål");

        EsSprak sprak2 =
                new EsSprak(d("2012-12-01"), "78874", "Norsk(muntlig)", "Norwegian", "Morsmål");

        ArrayList<EsSprak> sprakListe = new ArrayList<>();
        sprakListe.add(sprak1);
        sprakListe.add(sprak2);

        EsKurs kurs1 = new EsKurs(d("2012-12-01"), null, "Akseloppretting", "Easy-Laser", null,
                null, null);

        EsKurs kurs2 = new EsKurs(d("2015-06-01"), null, "Varme arbeider Sertifikat",
                "Norsk brannvernforening", "ÅR", 5, null);

        EsKurs kurs3 = new EsKurs(d("2016-02-01"), null, "Flensarbeid for Norsk Olje og Gass",
                "Torqlite Europa a/s", "ÅR", 4, null);


        ArrayList<EsKurs> kursListe = new ArrayList<>();
        kursListe.add(kurs1);
        kursListe.add(kurs2);
        kursListe.add(kurs3);

        EsVerv verv =
                new EsVerv(d("2000-01-15"), d("2001-01-15"), "Verv organisasjon", "verv tittel");

        ArrayList<EsVerv> vervListe = new ArrayList<>();
        vervListe.add(verv);

        EsGeografiJobbonsker geografiJobbonsker = new EsGeografiJobbonsker("Hamar", "NO04.0403");

        EsGeografiJobbonsker geografiJobbonsker1 =
                new EsGeografiJobbonsker("Lillehammer", "NO05.0501");

        EsGeografiJobbonsker geografiJobbonsker2 = new EsGeografiJobbonsker("Hedmark", "NO04");

        ArrayList<EsGeografiJobbonsker> geografiJobbonskerListe = new ArrayList<>();
        geografiJobbonskerListe.add(geografiJobbonsker);
        geografiJobbonskerListe.add(geografiJobbonsker1);
        geografiJobbonskerListe.add(geografiJobbonsker2);

        EsYrkeJobbonsker yrkeJobbonsker = new EsYrkeJobbonsker("Yrke jobb ønskeStyrk Kode",
                "Yrke jobb ønske Styrk beskrivelse", true, Collections.emptyList());

        ArrayList<EsYrkeJobbonsker> yrkeJobbonskerListe = new ArrayList<>();
        yrkeJobbonskerListe.add(yrkeJobbonsker);

        EsOmfangJobbonsker esOmfangJobbonsker =
                new EsOmfangJobbonsker(Omfang.HELTID.name(), Omfang.HELTID.defaultTekst());

        List<EsOmfangJobbonsker> omfangJobbonskerList = new ArrayList<>();
        omfangJobbonskerList.add(esOmfangJobbonsker);

        EsAnsettelsesformJobbonsker ansettelsesformJobbonsker = new EsAnsettelsesformJobbonsker(
                Ansettelsesform.FAST.name(), Ansettelsesform.FAST.defaultTekst());

        List<EsAnsettelsesformJobbonsker> ansettelsesformJobbonskerList = new ArrayList<>();
        ansettelsesformJobbonskerList.add(ansettelsesformJobbonsker);

        EsArbeidstidsordningJobbonsker arbeidstidsordningJobbonsker =
                new EsArbeidstidsordningJobbonsker("Arbeidstidsordning Kode",
                        "Arbeidstidsordning Kode Tekst");

        ArrayList<EsArbeidstidsordningJobbonsker> arbeidstidsordningJobbonskerListe =
                new ArrayList<>();
        arbeidstidsordningJobbonskerListe.add(arbeidstidsordningJobbonsker);

        EsCv esCv = new EsCv(AKTORID10, "01016034215", "OLA", "NORDMANN", d("1960-01-01"), false, "ARBS",
                "22339155@mailinator.com", "(+47) 22339155", "22339155", "NO", "7L",
                "", "N", d("2016-05-30"), "Minvei 1", "", "", "0654", "OSLO", "NO", 301, false,
                new Date(), 301, TRUE, null, null, null, null, FALSE, FALSE, "0301", "H149390", false);
        esCv.addUtdanning(utdanningsListe);
        esCv.addYrkeserfaring(yrkeserfaringsListe);
        esCv.addKompetanse(kompetanseList);
        esCv.addSertifikat(sertifikatListe);
        esCv.addForerkort(forerkortListe);
        esCv.addSprak(sprakListe);
        esCv.addKurs(kursListe);
        esCv.addVerv(vervListe);
        esCv.addGeografiJobbonske(geografiJobbonskerListe);
        esCv.addYrkeJobbonske(yrkeJobbonskerListe);
        return esCv;
    }

    public static EsCv giveMeCvForKode6() {


        EsUtdanning utdanning2 = new EsUtdanning(d("1988-08-20"), d("1989-06-20"), "UiO", "838838",
                "Sosialantropologiske fag", "Sosialantropologi gr. fag");


        ArrayList<EsUtdanning> utdanningsListe = new ArrayList<>();
        utdanningsListe.add(utdanning2);

        EsYrkeserfaring yrkeserfaring1 = new EsYrkeserfaring(d("2000-01-01"), d("2000-01-10"),
                "Stentransport, Kragerø", "8341.01", "", "maskinkjører og maskintransport",
                "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring2 = new EsYrkeserfaring(d("2003-01-01"), d("2003-02-01"),
                "AF-Pihl, Hammerfest", "8342.01", "Anleggsmaskinfører",
                "maskinkjører og maskintransport", "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring3 = new EsYrkeserfaring(d("2003-04-01"), d("2003-05-01"),
                "O.K. Hagalia, Slependen", "8342.01", "Anleggsmaskinfører",
                "maskinkjører og maskintransport", "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring4 = new EsYrkeserfaring(d("2005-08-01"), d("2005-09-01"),
                "Vard Group,avd.Brevik", "7233.03", "Industrimekaniker", "Industrimekaniker",
                "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring5 = new EsYrkeserfaring(d("2016-06-01"), d("2016-07-01"),
                "MTM anlegg", "8332.03", "Lastebil- og trailersjåfør", "Sjåfør kl. 2", "YRKE_ORGNR",
                "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring6 = new EsYrkeserfaring(d("2017-10-01"), d("2017-12-01"),
                "NLI  Grenland", "7233.03", "Industrimekaniker", "Industrimekaniker", "YRKE_ORGNR",
                "YRKE_NACEKODE", false, Collections.emptyList());

        ArrayList<EsYrkeserfaring> yrkeserfaringsListe = new ArrayList<>();
        yrkeserfaringsListe.add(yrkeserfaring1);
        yrkeserfaringsListe.add(yrkeserfaring2);
        yrkeserfaringsListe.add(yrkeserfaring3);
        yrkeserfaringsListe.add(yrkeserfaring4);
        yrkeserfaringsListe.add(yrkeserfaring5);
        yrkeserfaringsListe.add(yrkeserfaring6);

        EsKompetanse kompetanse1 = new EsKompetanse(d("2016-03-14"), "3020813",
                "Maskin- og kranførerarbeid", null, null, Collections.emptyList());

        EsKompetanse kompetanse2 = new EsKompetanse(d("2016-03-14"), "3281301",
                "Mekanisk arbeid generelt", "Mekanisk arbeid generelt", null, Collections.emptyList());

        EsKompetanse kompetanse3 = new EsKompetanse(d("2016-03-14"), "506",
                "Landtransport generelt", "Landtransport generelt", null, Collections.emptyList());

        EsKompetanse kompetanse4 = new EsKompetanse(d("2016-03-14"), "212", "Industri (bransje)",
                "Mekanisk industri (bransje)", null, Collections.emptyList());

        ArrayList<EsKompetanse> kompetanseList = new ArrayList<>();
        kompetanseList.add(kompetanse1);
        kompetanseList.add(kompetanse2);
        kompetanseList.add(kompetanse3);
        kompetanseList.add(kompetanse4);

        EsSertifikat sertifikat1 = new EsSertifikat(d("1994-08-01"), null, "V1.6050",
                "A - Tung motorsykkel", null, "");

        EsSertifikat sertifikat2 = new EsSertifikat(d("1991-01-01"), null, "V1.6070",
                "BE - Personbil med tilhenger", null, "");

        EsSertifikat sertifikat3 = new EsSertifikat(d("1996-02-01"), d("2020-12-01"), "V1.6110",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 12 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 12 tonn", "");
        EsSertifikat sertifikat4 = new EsSertifikat(d("1995-01-01"), null, "A1.6820",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn", "");
        EsSertifikat sertifikat5 = new EsSertifikat(d("1995-01-01"), null, "A1.6820",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn", "");

        ArrayList<EsSertifikat> sertifikatListe = new ArrayList<>();
        sertifikatListe.add(sertifikat1);
        sertifikatListe.add(sertifikat2);
        sertifikatListe.add(sertifikat3);
        sertifikatListe.add(sertifikat4);
        sertifikatListe.add(sertifikat5);

        EsForerkort forerkort1 = new EsForerkort(d("1994-08-01"), null, "V1.6050",
                "A - Tung motorsykkel", null, "");

        EsForerkort forerkort2 = new EsForerkort(d("1991-01-01"), null, "V1.6070",
                "BE - Personbil med tilhenger", null, "");

        EsForerkort forerkort3 = new EsForerkort(d("1996-02-01"), d("2020-12-01"), "V1.6110",
                "CE - Lastebil med tilhenger", null, "");

        EsForerkort forerkort4 = new EsForerkort(d("1996-02-01"), d("2020-12-01"), "V1.6145",
                "DE - Buss med tilhenger", null, "");

        ArrayList<EsForerkort> forerkortListe = new ArrayList<>();
        forerkortListe.add(forerkort1);
        forerkortListe.add(forerkort2);
        forerkortListe.add(forerkort3);
        forerkortListe.add(forerkort4);


        EsSprak sprak1 =
                new EsSprak(d("2012-12-01"), "78874", "Norsk(skriftlig)", "Norwegian", "Morsmål");

        EsSprak sprak2 =
                new EsSprak(d("2012-12-01"), "78874", "Norsk(muntlig)", "Norwegian", "Morsmål");

        ArrayList<EsSprak> sprakListe = new ArrayList<>();
        sprakListe.add(sprak1);
        sprakListe.add(sprak2);

        EsKurs kurs1 = new EsKurs(d("2012-12-01"), null, "Akseloppretting", "Easy-Laser", null,
                null, null);

        EsKurs kurs2 = new EsKurs(d("2015-06-01"), null, "Varme arbeider Sertifikat",
                "Norsk brannvernforening", "ÅR", 5, null);

        EsKurs kurs3 = new EsKurs(d("2016-02-01"), null, "Flensarbeid for Norsk Olje og Gass",
                "Torqlite Europa a/s", "ÅR", 4, null);


        ArrayList<EsKurs> kursListe = new ArrayList<>();
        kursListe.add(kurs1);
        kursListe.add(kurs2);
        kursListe.add(kurs3);

        EsVerv verv =
                new EsVerv(d("2000-01-15"), d("2001-01-15"), "Verv organisasjon", "verv tittel");

        ArrayList<EsVerv> vervListe = new ArrayList<>();
        vervListe.add(verv);

        EsGeografiJobbonsker geografiJobbonsker = new EsGeografiJobbonsker("Hamar", "NO04.0403");

        EsGeografiJobbonsker geografiJobbonsker1 =
                new EsGeografiJobbonsker("Lillehammer", "NO05.0501");

        EsGeografiJobbonsker geografiJobbonsker2 = new EsGeografiJobbonsker("Hedmark", "NO04");

        ArrayList<EsGeografiJobbonsker> geografiJobbonskerListe = new ArrayList<>();
        geografiJobbonskerListe.add(geografiJobbonsker);
        geografiJobbonskerListe.add(geografiJobbonsker1);
        geografiJobbonskerListe.add(geografiJobbonsker2);

        EsYrkeJobbonsker yrkeJobbonsker = new EsYrkeJobbonsker("Yrke jobb ønskeStyrk Kode",
                "Yrke jobb ønske Styrk beskrivelse", true, Collections.emptyList());

        ArrayList<EsYrkeJobbonsker> yrkeJobbonskerListe = new ArrayList<>();
        yrkeJobbonskerListe.add(yrkeJobbonsker);

        EsOmfangJobbonsker esOmfangJobbonsker =
                new EsOmfangJobbonsker(Omfang.HELTID.name(), Omfang.HELTID.defaultTekst());

        List<EsOmfangJobbonsker> omfangJobbonskerList = new ArrayList<>();
        omfangJobbonskerList.add(esOmfangJobbonsker);

        EsAnsettelsesformJobbonsker ansettelsesformJobbonsker = new EsAnsettelsesformJobbonsker(
                Ansettelsesform.FAST.name(), Ansettelsesform.FAST.defaultTekst());

        List<EsAnsettelsesformJobbonsker> ansettelsesformJobbonskerList = new ArrayList<>();
        ansettelsesformJobbonskerList.add(ansettelsesformJobbonsker);

        EsArbeidstidsordningJobbonsker arbeidstidsordningJobbonsker =
                new EsArbeidstidsordningJobbonsker("Arbeidstidsordning Kode",
                        "Arbeidstidsordning Kode Tekst");

        ArrayList<EsArbeidstidsordningJobbonsker> arbeidstidsordningJobbonskerListe =
                new ArrayList<>();
        arbeidstidsordningJobbonskerListe.add(arbeidstidsordningJobbonsker);

        EsCv esCv = new EsCv(AKTORID11, "01016034215", "OLA", "NORDMANN", d("1960-01-01"), false, "ARBS",
                "22339155@mailinator.com", "(+47) 22339155", "22339155", "NO", "8L",
                "", "N", d("2016-05-30"), "Minvei 1", "", "", "0654", "OSLO", "NO", 301, false,
                new Date(), 301, FALSE, "6", null, null, null, FALSE, FALSE, "0301", "H149390", false);
        esCv.addUtdanning(utdanningsListe);
        esCv.addYrkeserfaring(yrkeserfaringsListe);
        esCv.addKompetanse(kompetanseList);
        esCv.addSertifikat(sertifikatListe);
        esCv.addForerkort(forerkortListe);
        esCv.addSprak(sprakListe);
        esCv.addKurs(kursListe);
        esCv.addVerv(vervListe);
        esCv.addGeografiJobbonske(geografiJobbonskerListe);
        esCv.addYrkeJobbonske(yrkeJobbonskerListe);
        return esCv;
    }

    public static EsCv giveMeCvForKode7() {


        EsUtdanning utdanning2 = new EsUtdanning(d("1988-08-20"), d("1989-06-20"), "UiO", "838838",
                "Sosialantropologiske fag", "Sosialantropologi gr. fag");


        ArrayList<EsUtdanning> utdanningsListe = new ArrayList<>();
        utdanningsListe.add(utdanning2);

        EsYrkeserfaring yrkeserfaring1 = new EsYrkeserfaring(d("2000-01-01"), d("2000-01-10"),
                "Stentransport, Kragerø", "8341.01", "", "maskinkjører og maskintransport",
                "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring2 = new EsYrkeserfaring(d("2003-01-01"), d("2003-02-01"),
                "AF-Pihl, Hammerfest", "8342.01", "Anleggsmaskinfører",
                "maskinkjører og maskintransport", "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring3 = new EsYrkeserfaring(d("2003-04-01"), d("2003-05-01"),
                "O.K. Hagalia, Slependen", "8342.01", "Anleggsmaskinfører",
                "maskinkjører og maskintransport", "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring4 = new EsYrkeserfaring(d("2005-08-01"), d("2005-09-01"),
                "Vard Group,avd.Brevik", "7233.03", "Industrimekaniker", "Industrimekaniker",
                "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring5 = new EsYrkeserfaring(d("2016-06-01"), d("2016-07-01"),
                "MTM anlegg", "8332.03", "Lastebil- og trailersjåfør", "Sjåfør kl. 2", "YRKE_ORGNR",
                "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring6 = new EsYrkeserfaring(d("2017-10-01"), d("2017-12-01"),
                "NLI  Grenland", "7233.03", "Industrimekaniker", "Industrimekaniker", "YRKE_ORGNR",
                "YRKE_NACEKODE", false, Collections.emptyList());

        ArrayList<EsYrkeserfaring> yrkeserfaringsListe = new ArrayList<>();
        yrkeserfaringsListe.add(yrkeserfaring1);
        yrkeserfaringsListe.add(yrkeserfaring2);
        yrkeserfaringsListe.add(yrkeserfaring3);
        yrkeserfaringsListe.add(yrkeserfaring4);
        yrkeserfaringsListe.add(yrkeserfaring5);
        yrkeserfaringsListe.add(yrkeserfaring6);

        EsKompetanse kompetanse1 = new EsKompetanse(d("2016-03-14"), "3020813",
                "Maskin- og kranførerarbeid", null, null, Collections.emptyList());

        EsKompetanse kompetanse2 = new EsKompetanse(d("2016-03-14"), "3281301",
                "Mekanisk arbeid generelt", "Mekanisk arbeid generelt", null, Collections.emptyList());

        EsKompetanse kompetanse3 = new EsKompetanse(d("2016-03-14"), "506",
                "Landtransport generelt", "Landtransport generelt", null, Collections.emptyList());

        EsKompetanse kompetanse4 = new EsKompetanse(d("2016-03-14"), "212", "Industri (bransje)",
                "Mekanisk industri (bransje)", null, Collections.emptyList());

        ArrayList<EsKompetanse> kompetanseList = new ArrayList<>();
        kompetanseList.add(kompetanse1);
        kompetanseList.add(kompetanse2);
        kompetanseList.add(kompetanse3);
        kompetanseList.add(kompetanse4);

        EsSertifikat sertifikat1 = new EsSertifikat(d("1994-08-01"), null, "V1.6050",
                "A - Tung motorsykkel", null, "");

        EsSertifikat sertifikat2 = new EsSertifikat(d("1991-01-01"), null, "V1.6070",
                "BE - Personbil med tilhenger", null, "");

        EsSertifikat sertifikat3 = new EsSertifikat(d("1996-02-01"), d("2020-12-01"), "V1.6110",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 12 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 12 tonn", "");
        EsSertifikat sertifikat4 = new EsSertifikat(d("1995-01-01"), null, "A1.6820",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn", "");
        EsSertifikat sertifikat5 = new EsSertifikat(d("1995-01-01"), null, "A1.6820",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn", "");

        ArrayList<EsSertifikat> sertifikatListe = new ArrayList<>();
        sertifikatListe.add(sertifikat1);
        sertifikatListe.add(sertifikat2);
        sertifikatListe.add(sertifikat3);
        sertifikatListe.add(sertifikat4);
        sertifikatListe.add(sertifikat5);

        EsForerkort forerkort1 = new EsForerkort(d("1994-08-01"), null, "V1.6050",
                "A - Tung motorsykkel", null, "");

        EsForerkort forerkort2 = new EsForerkort(d("1991-01-01"), null, "V1.6070",
                "BE - Personbil med tilhenger", null, "");

        EsForerkort forerkort3 = new EsForerkort(d("1996-02-01"), d("2020-12-01"), "V1.6110",
                "CE - Lastebil med tilhenger", null, "");

        EsForerkort forerkort4 = new EsForerkort(d("1996-02-01"), d("2020-12-01"), "V1.6145",
                "DE - Buss med tilhenger", null, "");

        ArrayList<EsForerkort> forerkortListe = new ArrayList<>();
        forerkortListe.add(forerkort1);
        forerkortListe.add(forerkort2);
        forerkortListe.add(forerkort3);
        forerkortListe.add(forerkort4);


        EsSprak sprak1 =
                new EsSprak(d("2012-12-01"), "78874", "Norsk(skriftlig)", "Norwegian", "Morsmål");

        EsSprak sprak2 =
                new EsSprak(d("2012-12-01"), "78874", "Norsk(muntlig)", "Norwegian", "Morsmål");

        ArrayList<EsSprak> sprakListe = new ArrayList<>();
        sprakListe.add(sprak1);
        sprakListe.add(sprak2);

        EsKurs kurs1 = new EsKurs(d("2012-12-01"), null, "Akseloppretting", "Easy-Laser", null,
                null, null);

        EsKurs kurs2 = new EsKurs(d("2015-06-01"), null, "Varme arbeider Sertifikat",
                "Norsk brannvernforening", "ÅR", 5, null);

        EsKurs kurs3 = new EsKurs(d("2016-02-01"), null, "Flensarbeid for Norsk Olje og Gass",
                "Torqlite Europa a/s", "ÅR", 4, null);


        ArrayList<EsKurs> kursListe = new ArrayList<>();
        kursListe.add(kurs1);
        kursListe.add(kurs2);
        kursListe.add(kurs3);

        EsVerv verv =
                new EsVerv(d("2000-01-15"), d("2001-01-15"), "Verv organisasjon", "verv tittel");

        ArrayList<EsVerv> vervListe = new ArrayList<>();
        vervListe.add(verv);

        EsGeografiJobbonsker geografiJobbonsker = new EsGeografiJobbonsker("Hamar", "NO04.0403");

        EsGeografiJobbonsker geografiJobbonsker1 =
                new EsGeografiJobbonsker("Lillehammer", "NO05.0501");

        EsGeografiJobbonsker geografiJobbonsker2 = new EsGeografiJobbonsker("Hedmark", "NO04");

        ArrayList<EsGeografiJobbonsker> geografiJobbonskerListe = new ArrayList<>();
        geografiJobbonskerListe.add(geografiJobbonsker);
        geografiJobbonskerListe.add(geografiJobbonsker1);
        geografiJobbonskerListe.add(geografiJobbonsker2);

        EsYrkeJobbonsker yrkeJobbonsker = new EsYrkeJobbonsker("Yrke jobb ønskeStyrk Kode",
                "Yrke jobb ønske Styrk beskrivelse", true, Collections.emptyList());

        ArrayList<EsYrkeJobbonsker> yrkeJobbonskerListe = new ArrayList<>();
        yrkeJobbonskerListe.add(yrkeJobbonsker);

        EsOmfangJobbonsker esOmfangJobbonsker =
                new EsOmfangJobbonsker(Omfang.HELTID.name(), Omfang.HELTID.defaultTekst());

        List<EsOmfangJobbonsker> omfangJobbonskerList = new ArrayList<>();
        omfangJobbonskerList.add(esOmfangJobbonsker);

        EsAnsettelsesformJobbonsker ansettelsesformJobbonsker = new EsAnsettelsesformJobbonsker(
                Ansettelsesform.FAST.name(), Ansettelsesform.FAST.defaultTekst());

        List<EsAnsettelsesformJobbonsker> ansettelsesformJobbonskerList = new ArrayList<>();
        ansettelsesformJobbonskerList.add(ansettelsesformJobbonsker);

        EsArbeidstidsordningJobbonsker arbeidstidsordningJobbonsker =
                new EsArbeidstidsordningJobbonsker("Arbeidstidsordning Kode",
                        "Arbeidstidsordning Kode Tekst");

        ArrayList<EsArbeidstidsordningJobbonsker> arbeidstidsordningJobbonskerListe =
                new ArrayList<>();
        arbeidstidsordningJobbonskerListe.add(arbeidstidsordningJobbonsker);

        EsCv esCv = new EsCv(AKTORID12, "01016034215", "OLA", "NORDMANN", d("1960-01-01"), false, "ARBS",
                "22339155@mailinator.com", "(+47) 22339155", "22339155", "NO", "9L",
                "", "N", d("2016-05-30"), "Minvei 1", "", "", "0654", "OSLO", "NO", 301, false,
                new Date(), 301, FALSE, "7", null, null, null, FALSE, FALSE, "0301", "H149390", false);
        esCv.addUtdanning(utdanningsListe);
        esCv.addYrkeserfaring(yrkeserfaringsListe);
        esCv.addKompetanse(kompetanseList);
        esCv.addSertifikat(sertifikatListe);
        esCv.addForerkort(forerkortListe);
        esCv.addSprak(sprakListe);
        esCv.addKurs(kursListe);
        esCv.addVerv(vervListe);
        esCv.addGeografiJobbonske(geografiJobbonskerListe);
        esCv.addYrkeJobbonske(yrkeJobbonskerListe);
        return esCv;
    }

    public static EsCv giveMeCvFritattForKandidatsok() {


        EsUtdanning utdanning2 = new EsUtdanning(d("1988-08-20"), d("1989-06-20"), "UiO", "838838",
                "Sosialantropologiske fag", "Sosialantropologi gr. fag");


        ArrayList<EsUtdanning> utdanningsListe = new ArrayList<>();
        utdanningsListe.add(utdanning2);

        EsYrkeserfaring yrkeserfaring1 = new EsYrkeserfaring(d("2000-01-01"), d("2000-01-10"),
                "Stentransport, Kragerø", "8341.01", "", "maskinkjører og maskintransport",
                "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring2 = new EsYrkeserfaring(d("2003-01-01"), d("2003-02-01"),
                "AF-Pihl, Hammerfest", "8342.01", "Anleggsmaskinfører",
                "maskinkjører og maskintransport", "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring3 = new EsYrkeserfaring(d("2003-04-01"), d("2003-05-01"),
                "O.K. Hagalia, Slependen", "8342.01", "Anleggsmaskinfører",
                "maskinkjører og maskintransport", "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring4 = new EsYrkeserfaring(d("2005-08-01"), d("2005-09-01"),
                "Vard Group,avd.Brevik", "7233.03", "Industrimekaniker", "Industrimekaniker",
                "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring5 = new EsYrkeserfaring(d("2016-06-01"), d("2016-07-01"),
                "MTM anlegg", "8332.03", "Lastebil- og trailersjåfør", "Sjåfør kl. 2", "YRKE_ORGNR",
                "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring6 = new EsYrkeserfaring(d("2017-10-01"), d("2017-12-01"),
                "NLI  Grenland", "7233.03", "Industrimekaniker", "Industrimekaniker", "YRKE_ORGNR",
                "YRKE_NACEKODE", false, Collections.emptyList());

        ArrayList<EsYrkeserfaring> yrkeserfaringsListe = new ArrayList<>();
        yrkeserfaringsListe.add(yrkeserfaring1);
        yrkeserfaringsListe.add(yrkeserfaring2);
        yrkeserfaringsListe.add(yrkeserfaring3);
        yrkeserfaringsListe.add(yrkeserfaring4);
        yrkeserfaringsListe.add(yrkeserfaring5);
        yrkeserfaringsListe.add(yrkeserfaring6);

        EsKompetanse kompetanse1 = new EsKompetanse(d("2016-03-14"), "3020813",
                "Maskin- og kranførerarbeid", null, null, Collections.emptyList());

        EsKompetanse kompetanse2 = new EsKompetanse(d("2016-03-14"), "3281301",
                "Mekanisk arbeid generelt", "Mekanisk arbeid generelt", null, Collections.emptyList());

        EsKompetanse kompetanse3 = new EsKompetanse(d("2016-03-14"), "506",
                "Landtransport generelt", "Landtransport generelt", null, Collections.emptyList());

        EsKompetanse kompetanse4 = new EsKompetanse(d("2016-03-14"), "212", "Industri (bransje)",
                "Mekanisk industri (bransje)", null, Collections.emptyList());

        ArrayList<EsKompetanse> kompetanseList = new ArrayList<>();
        kompetanseList.add(kompetanse1);
        kompetanseList.add(kompetanse2);
        kompetanseList.add(kompetanse3);
        kompetanseList.add(kompetanse4);

        EsSertifikat sertifikat1 = new EsSertifikat(d("1994-08-01"), null, "V1.6050",
                "A - Tung motorsykkel", null, "");

        EsSertifikat sertifikat2 = new EsSertifikat(d("1991-01-01"), null, "V1.6070",
                "BE - Personbil med tilhenger", null, "");

        EsSertifikat sertifikat3 = new EsSertifikat(d("1996-02-01"), d("2020-12-01"), "V1.6110",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 12 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 12 tonn", "");
        EsSertifikat sertifikat4 = new EsSertifikat(d("1995-01-01"), null, "A1.6820",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn", "");
        EsSertifikat sertifikat5 = new EsSertifikat(d("1995-01-01"), null, "A1.6820",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn", "");

        ArrayList<EsSertifikat> sertifikatListe = new ArrayList<>();
        sertifikatListe.add(sertifikat1);
        sertifikatListe.add(sertifikat2);
        sertifikatListe.add(sertifikat3);
        sertifikatListe.add(sertifikat4);
        sertifikatListe.add(sertifikat5);

        EsForerkort forerkort1 = new EsForerkort(d("1994-08-01"), null, "V1.6050",
                "A - Tung motorsykkel", null, "");

        EsForerkort forerkort2 = new EsForerkort(d("1991-01-01"), null, "V1.6070",
                "BE - Personbil med tilhenger", null, "");

        EsForerkort forerkort3 = new EsForerkort(d("1996-02-01"), d("2020-12-01"), "V1.6110",
                "CE - Lastebil med tilhenger", null, "");

        EsForerkort forerkort4 = new EsForerkort(d("1996-02-01"), d("2020-12-01"), "V1.6145",
                "DE - Buss med tilhenger", null, "");

        ArrayList<EsForerkort> forerkortListe = new ArrayList<>();
        forerkortListe.add(forerkort1);
        forerkortListe.add(forerkort2);
        forerkortListe.add(forerkort3);
        forerkortListe.add(forerkort4);


        EsSprak sprak1 =
                new EsSprak(d("2012-12-01"), "78874", "Norsk(skriftlig)", "Norwegian", "Morsmål");

        EsSprak sprak2 =
                new EsSprak(d("2012-12-01"), "78874", "Norsk(muntlig)", "Norwegian", "Morsmål");

        ArrayList<EsSprak> sprakListe = new ArrayList<>();
        sprakListe.add(sprak1);
        sprakListe.add(sprak2);

        EsKurs kurs1 = new EsKurs(d("2012-12-01"), null, "Akseloppretting", "Easy-Laser", null,
                null, null);

        EsKurs kurs2 = new EsKurs(d("2015-06-01"), null, "Varme arbeider Sertifikat",
                "Norsk brannvernforening", "ÅR", 5, null);

        EsKurs kurs3 = new EsKurs(d("2016-02-01"), null, "Flensarbeid for Norsk Olje og Gass",
                "Torqlite Europa a/s", "ÅR", 4, null);


        ArrayList<EsKurs> kursListe = new ArrayList<>();
        kursListe.add(kurs1);
        kursListe.add(kurs2);
        kursListe.add(kurs3);

        EsVerv verv =
                new EsVerv(d("2000-01-15"), d("2001-01-15"), "Verv organisasjon", "verv tittel");

        ArrayList<EsVerv> vervListe = new ArrayList<>();
        vervListe.add(verv);

        EsGeografiJobbonsker geografiJobbonsker = new EsGeografiJobbonsker("Hamar", "NO04.0403");

        EsGeografiJobbonsker geografiJobbonsker1 =
                new EsGeografiJobbonsker("Lillehammer", "NO05.0501");

        EsGeografiJobbonsker geografiJobbonsker2 = new EsGeografiJobbonsker("Hedmark", "NO04");

        ArrayList<EsGeografiJobbonsker> geografiJobbonskerListe = new ArrayList<>();
        geografiJobbonskerListe.add(geografiJobbonsker);
        geografiJobbonskerListe.add(geografiJobbonsker1);
        geografiJobbonskerListe.add(geografiJobbonsker2);

        EsYrkeJobbonsker yrkeJobbonsker = new EsYrkeJobbonsker("Yrke jobb ønskeStyrk Kode",
                "Yrke jobb ønske Styrk beskrivelse", true, Collections.emptyList());

        ArrayList<EsYrkeJobbonsker> yrkeJobbonskerListe = new ArrayList<>();
        yrkeJobbonskerListe.add(yrkeJobbonsker);

        EsOmfangJobbonsker esOmfangJobbonsker =
                new EsOmfangJobbonsker(Omfang.HELTID.name(), Omfang.HELTID.defaultTekst());

        List<EsOmfangJobbonsker> omfangJobbonskerList = new ArrayList<>();
        omfangJobbonskerList.add(esOmfangJobbonsker);

        EsAnsettelsesformJobbonsker ansettelsesformJobbonsker = new EsAnsettelsesformJobbonsker(
                Ansettelsesform.FAST.name(), Ansettelsesform.FAST.defaultTekst());

        List<EsAnsettelsesformJobbonsker> ansettelsesformJobbonskerList = new ArrayList<>();
        ansettelsesformJobbonskerList.add(ansettelsesformJobbonsker);

        EsArbeidstidsordningJobbonsker arbeidstidsordningJobbonsker =
                new EsArbeidstidsordningJobbonsker("Arbeidstidsordning Kode",
                        "Arbeidstidsordning Kode Tekst");

        ArrayList<EsArbeidstidsordningJobbonsker> arbeidstidsordningJobbonskerListe =
                new ArrayList<>();
        arbeidstidsordningJobbonskerListe.add(arbeidstidsordningJobbonsker);

        EsCv esCv = new EsCv(AKTORID13, "01016034215", "OLA", "NORDMANN", d("1960-01-01"), false, "ARBS",
                "22339155@mailinator.com", "(+47) 22339155", "22339155", "NO", "10L",
                "", "N", d("2016-05-30"), "Minvei 1", "", "", "0654", "OSLO", "NO", 301, false,
                new Date(), 301, FALSE, null, "IKVAL", null, null, TRUE, FALSE, "0301", "H149390", false);
        esCv.addUtdanning(utdanningsListe);
        esCv.addYrkeserfaring(yrkeserfaringsListe);
        esCv.addKompetanse(kompetanseList);
        esCv.addSertifikat(sertifikatListe);
        esCv.addForerkort(forerkortListe);
        esCv.addSprak(sprakListe);
        esCv.addKurs(kursListe);
        esCv.addVerv(vervListe);
        esCv.addGeografiJobbonske(geografiJobbonskerListe);
        esCv.addYrkeJobbonske(yrkeJobbonskerListe);
        return esCv;
    }

    public static EsCv giveMeCvFritattForAgKandidatsok() {


        EsUtdanning utdanning2 = new EsUtdanning(d("1988-08-20"), d("1989-06-20"), "UiO", "838838",
                "Sosialantropologiske fag", "Sosialantropologi gr. fag");


        ArrayList<EsUtdanning> utdanningsListe = new ArrayList<>();
        utdanningsListe.add(utdanning2);

        EsYrkeserfaring yrkeserfaring1 = new EsYrkeserfaring(d("2000-01-01"), d("2000-01-10"),
                "Stentransport, Kragerø", "8341.01", "", "maskinkjører og maskintransport",
                "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring2 = new EsYrkeserfaring(d("2003-01-01"), d("2003-02-01"),
                "AF-Pihl, Hammerfest", "8342.01", "Anleggsmaskinfører",
                "maskinkjører og maskintransport", "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring3 = new EsYrkeserfaring(d("2003-04-01"), d("2003-05-01"),
                "O.K. Hagalia, Slependen", "8342.01", "Anleggsmaskinfører",
                "maskinkjører og maskintransport", "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring4 = new EsYrkeserfaring(d("2005-08-01"), d("2005-09-01"),
                "Vard Group,avd.Brevik", "7233.03", "Industrimekaniker", "Industrimekaniker",
                "YRKE_ORGNR", "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring5 = new EsYrkeserfaring(d("2016-06-01"), d("2016-07-01"),
                "MTM anlegg", "8332.03", "Lastebil- og trailersjåfør", "Sjåfør kl. 2", "YRKE_ORGNR",
                "YRKE_NACEKODE", false, Collections.emptyList());

        EsYrkeserfaring yrkeserfaring6 = new EsYrkeserfaring(d("2017-10-01"), d("2017-12-01"),
                "NLI  Grenland", "7233.03", "Industrimekaniker", "Industrimekaniker", "YRKE_ORGNR",
                "YRKE_NACEKODE", false, Collections.emptyList());

        ArrayList<EsYrkeserfaring> yrkeserfaringsListe = new ArrayList<>();
        yrkeserfaringsListe.add(yrkeserfaring1);
        yrkeserfaringsListe.add(yrkeserfaring2);
        yrkeserfaringsListe.add(yrkeserfaring3);
        yrkeserfaringsListe.add(yrkeserfaring4);
        yrkeserfaringsListe.add(yrkeserfaring5);
        yrkeserfaringsListe.add(yrkeserfaring6);

        EsKompetanse kompetanse1 = new EsKompetanse(d("2016-03-14"), "3020813",
                "Maskin- og kranførerarbeid", null, null, Collections.emptyList());

        EsKompetanse kompetanse2 = new EsKompetanse(d("2016-03-14"), "3281301",
                "Mekanisk arbeid generelt", "Mekanisk arbeid generelt", null, Collections.emptyList());

        EsKompetanse kompetanse3 = new EsKompetanse(d("2016-03-14"), "506",
                "Landtransport generelt", "Landtransport generelt", null, Collections.emptyList());

        EsKompetanse kompetanse4 = new EsKompetanse(d("2016-03-14"), "212", "Industri (bransje)",
                "Mekanisk industri (bransje)", null, Collections.emptyList());

        ArrayList<EsKompetanse> kompetanseList = new ArrayList<>();
        kompetanseList.add(kompetanse1);
        kompetanseList.add(kompetanse2);
        kompetanseList.add(kompetanse3);
        kompetanseList.add(kompetanse4);

        EsSertifikat sertifikat1 = new EsSertifikat(d("1994-08-01"), null, "V1.6050",
                "A - Tung motorsykkel", null, "");

        EsSertifikat sertifikat2 = new EsSertifikat(d("1991-01-01"), null, "V1.6070",
                "BE - Personbil med tilhenger", null, "");

        EsSertifikat sertifikat3 = new EsSertifikat(d("1996-02-01"), d("2020-12-01"), "V1.6110",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 12 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 12 tonn", "");
        EsSertifikat sertifikat4 = new EsSertifikat(d("1995-01-01"), null, "A1.6820",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn", "");
        EsSertifikat sertifikat5 = new EsSertifikat(d("1995-01-01"), null, "A1.6820",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn",
                "Yrkesbevis anleggsmaskinførere: Arb.klar maskin over 6 tonn", "");

        ArrayList<EsSertifikat> sertifikatListe = new ArrayList<>();
        sertifikatListe.add(sertifikat1);
        sertifikatListe.add(sertifikat2);
        sertifikatListe.add(sertifikat3);
        sertifikatListe.add(sertifikat4);
        sertifikatListe.add(sertifikat5);

        EsForerkort forerkort1 = new EsForerkort(d("1994-08-01"), null, "V1.6050",
                "A - Tung motorsykkel", null, "");

        EsForerkort forerkort2 = new EsForerkort(d("1991-01-01"), null, "V1.6070",
                "BE - Personbil med tilhenger", null, "");

        EsForerkort forerkort3 = new EsForerkort(d("1996-02-01"), d("2020-12-01"), "V1.6110",
                "CE - Lastebil med tilhenger", null, "");

        EsForerkort forerkort4 = new EsForerkort(d("1996-02-01"), d("2020-12-01"), "V1.6145",
                "DE - Buss med tilhenger", null, "");

        ArrayList<EsForerkort> forerkortListe = new ArrayList<>();
        forerkortListe.add(forerkort1);
        forerkortListe.add(forerkort2);
        forerkortListe.add(forerkort3);
        forerkortListe.add(forerkort4);


        EsSprak sprak1 =
                new EsSprak(d("2012-12-01"), "78874", "Norsk(skriftlig)", "Norwegian", "Morsmål");

        EsSprak sprak2 =
                new EsSprak(d("2012-12-01"), "78874", "Norsk(muntlig)", "Norwegian", "Morsmål");

        ArrayList<EsSprak> sprakListe = new ArrayList<>();
        sprakListe.add(sprak1);
        sprakListe.add(sprak2);

        EsKurs kurs1 = new EsKurs(d("2012-12-01"), null, "Akseloppretting", "Easy-Laser", null,
                null, null);

        EsKurs kurs2 = new EsKurs(d("2015-06-01"), null, "Varme arbeider Sertifikat",
                "Norsk brannvernforening", "ÅR", 5, null);

        EsKurs kurs3 = new EsKurs(d("2016-02-01"), null, "Flensarbeid for Norsk Olje og Gass",
                "Torqlite Europa a/s", "ÅR", 4, null);


        ArrayList<EsKurs> kursListe = new ArrayList<>();
        kursListe.add(kurs1);
        kursListe.add(kurs2);
        kursListe.add(kurs3);

        EsVerv verv =
                new EsVerv(d("2000-01-15"), d("2001-01-15"), "Verv organisasjon", "verv tittel");

        ArrayList<EsVerv> vervListe = new ArrayList<>();
        vervListe.add(verv);

        EsGeografiJobbonsker geografiJobbonsker = new EsGeografiJobbonsker("Hamar", "NO04.0403");

        EsGeografiJobbonsker geografiJobbonsker1 =
                new EsGeografiJobbonsker("Lillehammer", "NO05.0501");

        EsGeografiJobbonsker geografiJobbonsker2 = new EsGeografiJobbonsker("Hedmark", "NO04");

        ArrayList<EsGeografiJobbonsker> geografiJobbonskerListe = new ArrayList<>();
        geografiJobbonskerListe.add(geografiJobbonsker);
        geografiJobbonskerListe.add(geografiJobbonsker1);
        geografiJobbonskerListe.add(geografiJobbonsker2);

        EsYrkeJobbonsker yrkeJobbonsker = new EsYrkeJobbonsker("Yrke jobb ønskeStyrk Kode",
                "Yrke jobb ønske Styrk beskrivelse", true, Collections.emptyList());

        ArrayList<EsYrkeJobbonsker> yrkeJobbonskerListe = new ArrayList<>();
        yrkeJobbonskerListe.add(yrkeJobbonsker);

        EsOmfangJobbonsker esOmfangJobbonsker =
                new EsOmfangJobbonsker(Omfang.HELTID.name(), Omfang.HELTID.defaultTekst());

        List<EsOmfangJobbonsker> omfangJobbonskerList = new ArrayList<>();
        omfangJobbonskerList.add(esOmfangJobbonsker);

        EsAnsettelsesformJobbonsker ansettelsesformJobbonsker = new EsAnsettelsesformJobbonsker(
                Ansettelsesform.FAST.name(), Ansettelsesform.FAST.defaultTekst());

        List<EsAnsettelsesformJobbonsker> ansettelsesformJobbonskerList = new ArrayList<>();
        ansettelsesformJobbonskerList.add(ansettelsesformJobbonsker);

        EsArbeidstidsordningJobbonsker arbeidstidsordningJobbonsker =
                new EsArbeidstidsordningJobbonsker("Arbeidstidsordning Kode",
                        "Arbeidstidsordning Kode Tekst");

        ArrayList<EsArbeidstidsordningJobbonsker> arbeidstidsordningJobbonskerListe =
                new ArrayList<>();
        arbeidstidsordningJobbonskerListe.add(arbeidstidsordningJobbonsker);

        EsCv esCv = new EsCv(AKTORID14, "01016034215", "OLA", "NORDMANN", d("1960-01-01"), false, "ARBS",
                "22339155@mailinator.com", "(+47) 22339155", "22339155", "NO", "11L",
                "", "N", d("2016-05-30"), "Minvei 1", "", "", "0654", "OSLO", "NO", 301, false,
                new Date(), 301, FALSE, null, null, null, null, FALSE, TRUE, "0301", "H149390", false);
        esCv.addUtdanning(utdanningsListe);
        esCv.addYrkeserfaring(yrkeserfaringsListe);
        esCv.addKompetanse(kompetanseList);
        esCv.addSertifikat(sertifikatListe);
        esCv.addForerkort(forerkortListe);
        esCv.addSprak(sprakListe);
        esCv.addKurs(kursListe);
        esCv.addVerv(vervListe);
        esCv.addGeografiJobbonske(geografiJobbonskerListe);
        esCv.addYrkeJobbonske(yrkeJobbonskerListe);
        return esCv;
    }
}
