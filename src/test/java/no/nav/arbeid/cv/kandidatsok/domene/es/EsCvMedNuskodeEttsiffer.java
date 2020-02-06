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
import java.util.Date;
import java.util.UUID;

import static java.lang.Boolean.FALSE;

public class EsCvMedNuskodeEttsiffer {

    private static final String AKTORID14 = UUID.randomUUID().toString();
    private static final String AKTORID15 = UUID.randomUUID().toString();
    private static final String AKTORID16 = UUID.randomUUID().toString();
    private static final String AKTORID17 = UUID.randomUUID().toString();


    private static final Logger LOGGER = LoggerFactory.getLogger(EsCvMedNuskodeEttsiffer.class);
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private static Date d(String string) {
        try {
            return string == null ? null : sdf.parse(string);
        } catch (ParseException e) {
            LOGGER.error("Feilet å parse " + string, e);
            return null;
        }
    }

    public static EsCv giveMeEsCvNuskode8() {


        EsUtdanning utdanning = new EsUtdanning(d("1988-08-20"), d("1989-06-20"), "Otta vgs. Otta",
                "8", "Doktorgrad", "Doktorgrad");

        ArrayList<EsUtdanning> utdanningsListe = new ArrayList<>();
        utdanningsListe.add(utdanning);


        EsCv esCv = new EsCv(AKTORID14, "05236984567", "KJELL", "LARSEN", d("1980-02-10"), false, "PARBS",
                "unnasluntrer2@mailinator.com", "(+47) 22334455", "12345678", "NO", "12L",
                "Dette er beskrivelsen av hva jeg har gjort i min yrkeskarriere",
                "J", d("2016-05-30"), "Dinvei 2", "", "", "2222", "HUSKER IKKE", "NO", 301,
                false, new Date(), 401, FALSE, null, "IKVAL", null, "NAV Gamle Oslo", FALSE, FALSE, "0401", "H149390", false);

        esCv.addUtdanning(utdanningsListe);

        return esCv;

    }

    public static EsCv giveMeEsCvNuskode6() {


        EsUtdanning utdanning = new EsUtdanning(d("1988-08-20"), d("1989-06-20"), "Otta vgs. Otta",
                "6", "Bachelor", "Videregaende");


        ArrayList<EsUtdanning> utdanningsListe = new ArrayList<>();
        utdanningsListe.add(utdanning);


        EsCv esCv = new EsCv(AKTORID15, "05236984567", "HEIDI", "MO", d("1980-02-10"), false, "PARBS",
                "unnasluntrer2@mailinator.com", "(+47) 22334455", "12345678", "NO", "13L",
                "Dette er beskrivelsen av hva jeg har gjort i min yrkeskarriere",
                "J", d("2016-05-30"), "Dinvei 2", "", "", "1122", "USIKKER", "NO", 301,
                false, new Date(), 401, FALSE, null, "IKVAL", null, "NAV Gamle Oslo", FALSE, FALSE, "0401", "H149390", false);

        esCv.addUtdanning(utdanningsListe);

        return esCv;

    }

    public static EsCv giveMeEsCvNuskode6og7() {


        EsUtdanning utdanning = new EsUtdanning(d("1988-08-20"), d("1989-06-20"), "Otta vgs. Otta",
                "6", "Bachelor", "");

        EsUtdanning utdanning2 = new EsUtdanning(d("1988-08-20"), d("1989-06-20"), "Otta vgs. Otta",
                "7", "Master", "");

        ArrayList<EsUtdanning> utdanningsListe = new ArrayList<>();
        utdanningsListe.add(utdanning);
        utdanningsListe.add(utdanning2);

        EsCv esCv = new EsCv(AKTORID16, "05236984567", "RANDI", "EILERTSEN", d("1980-02-10"), false, "PARBS",
                "unnasluntrer2@mailinator.com", "(+47) 22334455", "12345678", "NO", "14L",
                "Dette er beskrivelsen av hva jeg har gjort i min yrkeskarriere",
                "J", d("2016-05-30"), "Dinvei 2", "", "", "1111", "HUSKER IKKE", "NO", 301,
                false, new Date(), 401, FALSE, null, "IKVAL", null, "NAV Gamle Oslo", FALSE, FALSE, "0401", "H149390", false);

        esCv.addUtdanning(utdanningsListe);

        return esCv;

    }

    public static EsCv giveMeEsCvIngenUtdanning() {

        EsUtdanning utdanning = new EsUtdanning(d("1988-08-20"), d("1989-06-20"), "Otta vgs. Otta",
                "1", "Barnehage", "");


        ArrayList<EsUtdanning> utdanningsListe = new ArrayList<>();
        utdanningsListe.add(utdanning);

        EsCv esCv = new EsCv(AKTORID17, "05236984567", "JON", "PEDERSEN", d("1980-02-10"), false, "PARBS",
                "unnasluntrer2@mailinator.com", "(+47) 22334455", "12345678", "NO", "15L",
                "Dette er beskrivelsen av hva jeg har gjort i min yrkeskarriere",
                "J", d("2016-05-30"), "Veien 2", "", "", "1111", "OSLO", "NO", 301,
                false, new Date(), 401, FALSE, null, "IKVAL", null, "NAV Gamle Oslo", FALSE, FALSE, "0401", "H149390", false);

        esCv.addUtdanning(utdanningsListe);

        return esCv;

    }
}
