package no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.sok;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsYrkeserfaring {

    private String stillingstittel;

    private String alternativStillingstittel;

    private List<String> sokeTitler;

    private int yrkeserfaringManeder;

    private Date fraDato;

    public EsYrkeserfaring() {
    }

    public EsYrkeserfaring(String stillingstittel, String alternativStillingstittel,
                           int yrkeserfaringManeder, Date fraDato, List<String> sokeTitler) {
        this.stillingstittel = stillingstittel;
        this.alternativStillingstittel = alternativStillingstittel;
        this.yrkeserfaringManeder = yrkeserfaringManeder;
        this.fraDato = fraDato;
        this.sokeTitler = sokeTitler;
    }

    public String getStillingstittel() {
        return stillingstittel;
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
        return Objects.equals(stillingstittel, that.stillingstittel)
                && Objects.equals(alternativStillingstittel, that.alternativStillingstittel)
                && Objects.equals(yrkeserfaringManeder, that.yrkeserfaringManeder)
                && Objects.equals(fraDato, that.fraDato);
    }

    @Override
    public int hashCode() {

        return Objects.hash(stillingstittel, alternativStillingstittel,
                yrkeserfaringManeder, fraDato);
    }

    @Override
    public String toString() {
        return "EsYrkeserfaring{" + " stillingstittel='" + stillingstittel + '\''
                + " alternativStillingstittel='" + alternativStillingstittel + '\''
                + ", yrkeserfaringManeder='" + yrkeserfaringManeder + '\'' + ", fraDato='" + fraDato
                + '\'' + '}';
    }

}
