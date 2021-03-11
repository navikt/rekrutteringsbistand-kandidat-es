package no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsYrkeserfaring {

    private Date fraDato;

    private Date tilDato;

    private String arbeidsgiver;

    private String styrkKode;

    private String styrkKode4Siffer;

    private String styrkKode3Siffer;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String stillingstittel;

    private Set<String> stillingstitlerForTypeahead;

    private String alternativStillingstittel;

    private List<String> sokeTitler = new ArrayList<>();

    private String organisasjonsnummer;

    private String naceKode;

    private int yrkeserfaringManeder;

    private Boolean utelukketForFremtiden;

    private String beskrivelse;

    private String sted;

    public EsYrkeserfaring() {
    }

    public EsYrkeserfaring(Date fraDato, Date tilDato, String arbeidsgiver, String styrkKode,
                           String kodeverkStillingstittel, Set<String> stillingstitlerForTypeahead, String alternativStillingstittel, String beskrivelse,
                           int yrkeserfaringManeder, List<String> sokeTitler, String sted) {
        this(fraDato, tilDato, arbeidsgiver, styrkKode, kodeverkStillingstittel, stillingstitlerForTypeahead, alternativStillingstittel, null, null, yrkeserfaringManeder, false, sokeTitler, sted);
        this.beskrivelse = beskrivelse;
    }

    public EsYrkeserfaring(Date fraDato, Date tilDato, String arbeidsgiver, String styrkKode,
                           String stillingstittel, Set<String> stillingstitlerForTypeahead, String alternativStillingstittel,
                           String organisasjonsnummer, String naceKode, int yrkeserfaringManeder,
                           Boolean utelukketForFremtiden, List<String> sokeTitler, String sted) {
        this.fraDato = fraDato;
        this.tilDato = tilDato;
        this.arbeidsgiver = arbeidsgiver;
        this.styrkKode = styrkKode;
        styrkKode4Siffer = (styrkKode == null ? null
                : (styrkKode.length() <= 3 ? null : styrkKode.substring(0, 4)));
        styrkKode3Siffer = (styrkKode == null ? null
                : (styrkKode.length() <= 2 ? null : styrkKode.substring(0, 3)));
        this.stillingstittel = stillingstittel;
        this.stillingstitlerForTypeahead = stillingstitlerForTypeahead;
        this.alternativStillingstittel = alternativStillingstittel;
        this.organisasjonsnummer = organisasjonsnummer;
        this.naceKode = naceKode;
        this.yrkeserfaringManeder = yrkeserfaringManeder;
        this.utelukketForFremtiden = utelukketForFremtiden;
        this.sokeTitler.addAll(sokeTitler);
        this.sted = sted;
    }

    public EsYrkeserfaring(Date fraDato, Date tilDato, String arbeidsgiver, String styrkKode,
                           String stillingstittel, Set<String> stillingstitlerForTypeahead, String alternativStillingstittel,
                           String organisasjonsnummer, String naceKode, Boolean utelukketForFremtiden,
                           List<String> sokeTitler, String sted) {
        this.fraDato = fraDato;
        this.tilDato = tilDato;
        this.arbeidsgiver = arbeidsgiver;
        this.styrkKode = styrkKode;
        styrkKode4Siffer = (styrkKode == null ? null
                : (styrkKode.length() <= 3 ? null : styrkKode.substring(0, 4)));
        styrkKode3Siffer = (styrkKode == null ? null
                : (styrkKode.length() <= 2 ? null : styrkKode.substring(0, 3)));
        this.stillingstittel = stillingstittel;
        this.stillingstitlerForTypeahead = stillingstitlerForTypeahead;
        this.alternativStillingstittel = alternativStillingstittel;
        this.organisasjonsnummer = organisasjonsnummer;
        this.naceKode = naceKode;
        yrkeserfaringManeder = toYrkeserfaringManeder(fraDato, tilDato);
        this.utelukketForFremtiden = utelukketForFremtiden;
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

    public String getStillingstittel() {
        return stillingstittel;
    }

    public Set<String> getStillingstitlerForTypeahead() {
        return stillingstitlerForTypeahead;
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
                && Objects.equals(stillingstittel, that.stillingstittel)
                && Objects.equals(stillingstitlerForTypeahead, that.stillingstitlerForTypeahead)
                && Objects.equals(alternativStillingstittel, that.alternativStillingstittel)
                && Objects.equals(beskrivelse, that.beskrivelse)
                && Objects.equals(organisasjonsnummer, that.organisasjonsnummer)
                && Objects.equals(naceKode, that.naceKode)
                && Objects.equals(yrkeserfaringManeder, that.yrkeserfaringManeder)
                && Objects.equals(sted, that.sted)
                && Objects.equals(utelukketForFremtiden, that.utelukketForFremtiden);
    }

    @Override
    public int hashCode() {

        return Objects.hash(fraDato, tilDato, arbeidsgiver, styrkKode, stillingstittel,
                stillingstitlerForTypeahead, alternativStillingstittel, beskrivelse, organisasjonsnummer, naceKode,
                yrkeserfaringManeder, utelukketForFremtiden, sted);
    }

    @Override
    public String toString() {
        return "EsYrkeserfaring{" + "fraDato=" + fraDato + ", tilDato=" + tilDato
                + ", arbeidsgiver='" + arbeidsgiver + '\'' + ", styrkKode='" + styrkKode + '\''
                + ", stillingstittel='" + stillingstittel + '\''
                + ", alternativStillingstittel='" + alternativStillingstittel + '\''
                + ", beskrivelse='" + beskrivelse + '\'' + ", organisasjonsnummer='"
                + organisasjonsnummer + '\'' + ", naceKode='" + naceKode + '\''
                + ", yrkeserfaringManeder='" + yrkeserfaringManeder + '\''
                + ", sted='" + sted + '\''
                + ", utelukketForFremtiden='" + utelukketForFremtiden + '\'' + '}';
    }
}
