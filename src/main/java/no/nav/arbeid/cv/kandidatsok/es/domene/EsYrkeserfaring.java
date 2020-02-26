package no.nav.arbeid.cv.kandidatsok.es.domene;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import no.nav.elasticsearch.mapping.annotations.*;

import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsYrkeserfaring {

    @ElasticDateField
    private Date fraDato;

    @ElasticDateField(nullValue = "2099-12-31")
    private Date tilDato;

    @ElasticTextField(copyTo = "fritekst", analyzer = "norwegian")
    private String arbeidsgiver;

    @ElasticKeywordField
    private String styrkKode;

    @ElasticKeywordField
    private String styrkKode4Siffer;

    @ElasticKeywordField
    private String styrkKode3Siffer;

    @ElasticTextField(copyTo = "fritekst", analyzer = "norwegian")
    @ElasticKeywordField
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @ElasticCompletionField
    private String styrkKodeStillingstittel;

    @ElasticTextField(copyTo = "fritekst", analyzer = "norwegian")
    private String alternativStillingstittel;

    @ElasticTextField(analyzer = "norwegian")
    @ElasticKeywordField
    private List<String> sokeTitler = new ArrayList<>();

    @ElasticKeywordField
    private String organisasjonsnummer;

    @ElasticKeywordField
    private String naceKode;

    @ElasticIntegerField
    private int yrkeserfaringManeder;

    @ElasticBooleanField
    private Boolean utelukketForFremtiden;

    @ElasticTextField(copyTo = "fritekst", analyzer = "norwegian")
    private String beskrivelse;

    @ElasticTextField
    private String sted;

    public EsYrkeserfaring() {
    }

    public EsYrkeserfaring(Date fraDato, Date tilDato, String arbeidsgiver, String styrkKode,
                           String kodeverkStillingstittel, String alternativStillingstittel, String beskrivelse,
                           int yrkeserfaringManeder, List<String> sokeTitler, String sted) {
        this(fraDato, tilDato, arbeidsgiver, styrkKode, kodeverkStillingstittel,
                alternativStillingstittel, null, null, yrkeserfaringManeder, false, sokeTitler, sted);
        this.beskrivelse = beskrivelse;
    }

    public EsYrkeserfaring(Date fraDato, Date tilDato, String arbeidsgiver, String styrkKode,
                           String styrkKodeStillingstittel, String alternativStillingstittel,
                           String organisasjonsnummer, String naceKode, int yrkeserfaringManeder,
                           Boolean utelukketForFremtiden, List<String> sokeTitler, String sted) {
        this.fraDato = fraDato;
        this.tilDato = tilDato;
        this.arbeidsgiver = arbeidsgiver;
        this.styrkKode = styrkKode;
        this.styrkKode4Siffer = (styrkKode == null ? null
                : (styrkKode.length() <= 3 ? null : styrkKode.substring(0, 4)));
        this.styrkKode3Siffer = (styrkKode == null ? null
                : (styrkKode.length() <= 2 ? null : styrkKode.substring(0, 3)));
        this.styrkKodeStillingstittel = styrkKodeStillingstittel;
        this.alternativStillingstittel = alternativStillingstittel;
        this.organisasjonsnummer = organisasjonsnummer;
        this.naceKode = naceKode;
        this.yrkeserfaringManeder = yrkeserfaringManeder;
        this.utelukketForFremtiden = utelukketForFremtiden;
        this.sokeTitler.add(styrkKodeStillingstittel);
        this.sokeTitler.addAll(sokeTitler);
        this.sted = sted;
    }

    public EsYrkeserfaring(Date fraDato, Date tilDato, String arbeidsgiver, String styrkKode,
                           String styrkKodeStillingstittel, String alternativStillingstittel,
                           String organisasjonsnummer, String naceKode, Boolean utelukketForFremtiden,
                           List<String> sokeTitler, String sted) {
        this.fraDato = fraDato;
        this.tilDato = tilDato;
        this.arbeidsgiver = arbeidsgiver;
        this.styrkKode = styrkKode;
        this.styrkKode4Siffer = (styrkKode == null ? null
                : (styrkKode.length() <= 3 ? null : styrkKode.substring(0, 4)));
        this.styrkKode3Siffer = (styrkKode == null ? null
                : (styrkKode.length() <= 2 ? null : styrkKode.substring(0, 3)));
        this.styrkKodeStillingstittel = styrkKodeStillingstittel;
        this.alternativStillingstittel = alternativStillingstittel;
        this.organisasjonsnummer = organisasjonsnummer;
        this.naceKode = naceKode;
        this.yrkeserfaringManeder = toYrkeserfaringManeder(fraDato, tilDato);
        this.utelukketForFremtiden = utelukketForFremtiden;
        this.sokeTitler.add(styrkKodeStillingstittel);
        this.sokeTitler.addAll(sokeTitler);
        this.sted = sted;
    }

    private static int toYrkeserfaringManeder(Date fraDato, Date tilDato) {
        // Should not be possible, but will keep the check just in case
        if (fraDato == null) {
            return 0;
        }

        Calendar fraCalendar = new GregorianCalendar();
        fraCalendar.setTime(fraDato);

        // If tilDato is null, it is set to the current date
        Calendar tilCalendar = new GregorianCalendar();
        if (tilDato == null) {
            tilCalendar.setTime(new Date());
        } else {
            tilCalendar.setTime(tilDato);
        }

        int diffYear = tilCalendar.get(Calendar.YEAR) - fraCalendar.get(Calendar.YEAR);
        return diffYear * 12 + tilCalendar.get(Calendar.MONTH) - fraCalendar.get(Calendar.MONTH);
    }

    public Date getFraDato() {
        return fraDato;
    }

    public Date getTilDato() {
        return tilDato;
    }

    public String getArbeidsgiver() {
        return arbeidsgiver;
    }

    public String getStyrkKode() {
        return styrkKode;
    }

    public String getStyrkKode3Siffer() {
        return styrkKode3Siffer;
    }

    public String getStyrkKode4Siffer() {
        return styrkKode4Siffer;
    }

    public String getStyrkKodeStillingstittel() {
        return styrkKodeStillingstittel;
    }

    public String getAlternativStillingstittel() {
        return alternativStillingstittel;
    }

    public String getOrganisasjonsnummer() {
        return organisasjonsnummer;
    }

    public String getNaceKode() {
        return naceKode;
    }

    public int getYrkeserfaringManeder() {
        return yrkeserfaringManeder;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public Boolean getUtelukketForFremtiden() {
        return utelukketForFremtiden;
    }

    public List<String> getSokeTitler() {
        return sokeTitler;
    }

    public String getSted() {
        return sted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EsYrkeserfaring that = (EsYrkeserfaring) o;
        return Objects.equals(fraDato, that.fraDato) && Objects.equals(tilDato, that.tilDato)
                && Objects.equals(arbeidsgiver, that.arbeidsgiver)
                && Objects.equals(styrkKode, that.styrkKode)
                && Objects.equals(styrkKodeStillingstittel, that.styrkKodeStillingstittel)
                && Objects.equals(alternativStillingstittel, that.alternativStillingstittel)
                && Objects.equals(beskrivelse, that.beskrivelse)
                && Objects.equals(organisasjonsnummer, that.organisasjonsnummer)
                && Objects.equals(naceKode, that.naceKode)
                && Objects.equals(yrkeserfaringManeder, that.yrkeserfaringManeder)
                && Objects.equals(utelukketForFremtiden, that.utelukketForFremtiden)
                && Objects.equals(sted, that.sted);
    }

    @Override
    public int hashCode() {

        return Objects.hash(fraDato, tilDato, arbeidsgiver, styrkKode, styrkKodeStillingstittel,
                alternativStillingstittel, beskrivelse, organisasjonsnummer, naceKode,
                yrkeserfaringManeder, utelukketForFremtiden, sted);
    }

    @Override
    public String toString() {
        return "EsYrkeserfaring{" + "fraDato=" + fraDato + ", tilDato=" + tilDato
                + ", arbeidsgiver='" + arbeidsgiver + '\'' + ", styrkKode='" + styrkKode + '\''
                + ", styrkKodeStillingstittel='" + styrkKodeStillingstittel + '\''
                + ", alternativStillingstittel='" + alternativStillingstittel + '\''
                + ", beskrivelse='" + beskrivelse + '\'' + ", organisasjonsnummer='"
                + organisasjonsnummer + '\'' + ", naceKode='" + naceKode + '\''
                + ", yrkeserfaringManeder='" + yrkeserfaringManeder + '\''
                + ", sted='" + sted + '\''
                + ", utelukketForFremtiden='" + utelukketForFremtiden + '\'' + '}';
    }

}
