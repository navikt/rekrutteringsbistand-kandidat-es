package no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static java.lang.Boolean.FALSE;

public class EsCvMedNuskodeEttsiffer {

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

        EsCv esCv = new EsCv(EsCvObjectMother.nteAktorId(13), "05236984567", "KJELL", "LARSEN", d("1980-02-10"), false, "PARBS",
                "unnasluntrer2@mailinator.com", "(+47) 22334455", "12345678", "NO", "13L",
                "Dette er beskrivelsen av hva jeg har gjort i min yrkeskarriere",
                "J", d("2016-05-30"), "Dinvei 2", "", "", "2222", "HUSKER IKKE", "NO", 301,
                false, new Date(), 401, FALSE, null, "IKVAL", null, "NAV Gamle Oslo", FALSE, FALSE,
                true, true, null, "0401", "H149390", false, "Lier", "Viken");

        esCv.addUtdanning(utdanningsListe);

        return esCv;

    }

    public static EsCv giveMeEsCvNuskode6() {


        EsUtdanning utdanning = new EsUtdanning(d("1988-08-20"), d("1989-06-20"), "Otta vgs. Otta",
                "6", "Bachelor", "Videregaende");


        ArrayList<EsUtdanning> utdanningsListe = new ArrayList<>();
        utdanningsListe.add(utdanning);


        EsCv esCv = new EsCv(EsCvObjectMother.nteAktorId(14), "05236984567", "HEIDI", "MO", d("1980-02-10"), false, "PARBS",
                "unnasluntrer2@mailinator.com", "(+47) 22334455", "12345678", "NO", "14L",
                "Dette er beskrivelsen av hva jeg har gjort i min yrkeskarriere",
                "J", d("2016-05-30"), "Dinvei 2", "", "", "1122", "USIKKER", "NO", 301,
                false, new Date(), 401, FALSE, null, "IKVAL", null, "NAV Gamle Oslo", FALSE, FALSE,
                true, true, null, "0401", "H149390", false, "Lier", "Viken");

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

        EsCv esCv = new EsCv(EsCvObjectMother.nteAktorId(15), "05236984567", "RANDI", "EILERTSEN", d("1980-02-10"), false, "PARBS",
                "unnasluntrer2@mailinator.com", "(+47) 22334455", "12345678", "NO", "15L",
                "Dette er beskrivelsen av hva jeg har gjort i min yrkeskarriere",
                "J", d("2016-05-30"), "Dinvei 2", "", "", "1111", "HUSKER IKKE", "NO", 301,
                false, new Date(), 401, FALSE, null, "IKVAL", null, "NAV Gamle Oslo", FALSE, FALSE,
                true, true, null, "0401", "H149390", false, "Lier", "Viken");

        esCv.addUtdanning(utdanningsListe);

        return esCv;

    }

    public static EsCv giveMeEsCvIngenUtdanning() {

        EsUtdanning utdanning = new EsUtdanning(d("1988-08-20"), d("1989-06-20"), "Otta vgs. Otta",
                "1", "Barnehage", "");


        ArrayList<EsUtdanning> utdanningsListe = new ArrayList<>();
        utdanningsListe.add(utdanning);

        EsCv esCv = new EsCv(EsCvObjectMother.nteAktorId(16), "05236984567", "JON", "PEDERSEN", d("1980-02-10"), false, "PARBS",
                "unnasluntrer2@mailinator.com", "(+47) 22334455", "12345678", "NO", "16L",
                "Dette er beskrivelsen av hva jeg har gjort i min yrkeskarriere",
                "J", d("2016-05-30"), "Veien 2", "", "", "1111", "OSLO", "NO", 301,
                false, new Date(), 401, FALSE, null, "IKVAL", null, "NAV Gamle Oslo", FALSE, FALSE,
                true, true, null, "0401", "H149390", false, "Lier", "Viken");

        esCv.addUtdanning(utdanningsListe);

        return esCv;

    }
}
