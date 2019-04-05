package no.nav.arbeid.cv.kandidatsok.es.domene.sok;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsYrkeserfaring {

    private String styrkKodeStillingstittel;

    private String alternativStillingstittel;

    private List<String> sokeTitler;

    private int yrkeserfaringManeder;

    private Date fraDato;

    public EsYrkeserfaring() {}

    public EsYrkeserfaring(String styrkKodeStillingstittel, String alternativStillingstittel,
            int yrkeserfaringManeder, Date fraDato, List<String> sokeTitler) {
        this.styrkKodeStillingstittel = styrkKodeStillingstittel;
        this.alternativStillingstittel = alternativStillingstittel;
        this.yrkeserfaringManeder = yrkeserfaringManeder;
        this.fraDato = fraDato;
        this.sokeTitler = sokeTitler;
    }

    public String getStyrkKodeStillingstittel() {
        return styrkKodeStillingstittel;
    }

    public String getAlternativStillingstittel() {
        return alternativStillingstittel;
    }

    public int getYrkeserfaringManeder() {
        return yrkeserfaringManeder;
    }

    public Date getFraDato() {
        return fraDato;
    }

    public List<String> getSokeTitler() {
        return sokeTitler;
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
        return Objects.equals(styrkKodeStillingstittel, that.styrkKodeStillingstittel)
                && Objects.equals(alternativStillingstittel, that.alternativStillingstittel)
                && Objects.equals(yrkeserfaringManeder, that.yrkeserfaringManeder)
                && Objects.equals(fraDato, that.fraDato);
    }

    @Override
    public int hashCode() {

        return Objects.hash(styrkKodeStillingstittel, alternativStillingstittel,
                yrkeserfaringManeder, fraDato);
    }

    @Override
    public String toString() {
        return "EsYrkeserfaring{" + " styrkKodeStillingstittel='" + styrkKodeStillingstittel + '\''
                + " alternativStillingstittel='" + alternativStillingstittel + '\''
                + ", yrkeserfaringManeder='" + yrkeserfaringManeder + '\'' + ", fraDato='" + fraDato
                + '\'' + '}';
    }

}
