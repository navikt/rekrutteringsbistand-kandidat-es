package no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsSprak {

    private Date fraDato;

    private String sprakKode;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String sprakKodeTekst;

    private String alternativTekst;

    private String beskrivelse;

    private String ferdighetMuntlig;

    private String ferdighetSkriftlig;

    public EsSprak() {
    }

    public EsSprak(String spraaknavn, String ferdighetMuntlig, String ferdighetSkriftlig) {
        this.sprakKodeTekst = spraaknavn;
        this.alternativTekst = spraaknavn;
        this.beskrivelse = "";
        this.ferdighetMuntlig = ferdighetMuntlig;
        this.ferdighetSkriftlig = ferdighetSkriftlig;
        if (StringUtils.isNotBlank(ferdighetMuntlig)) {
            beskrivelse += "Muntlig: " + ferdighetMuntlig;
        }
        if (StringUtils.isNotBlank(ferdighetSkriftlig)) {
            if (StringUtils.isNotBlank(beskrivelse)) {
                beskrivelse += " ";
            }
            beskrivelse += "Skriftlig: " + ferdighetSkriftlig;
        }
    }

    public EsSprak(Date fraDato, String sprakKode, String sprakKodeTekst, String alternativTekst,
                   String beskrivelse) {
        this.fraDato = fraDato;
        this.sprakKode = sprakKode;
        this.sprakKodeTekst = sprakKodeTekst;
        this.alternativTekst = alternativTekst;
        this.beskrivelse = beskrivelse;
    }

    public Date getFraDato() {
        return fraDato;
    }

    public String getSprakKode() {
        return sprakKode;
    }

    public String getSprakKodeTekst() {
        return sprakKodeTekst;
    }

    public String getAlternativTekst() {
        return alternativTekst;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public String getFerdighetMuntlig() {
        return ferdighetMuntlig;
    }

    public String getFerdighetSkriftlig() {
        return ferdighetSkriftlig;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EsSprak esSprak = (EsSprak) o;
        return Objects.equals(fraDato, esSprak.fraDato) && Objects.equals(sprakKode, esSprak.sprakKode)
                && Objects.equals(sprakKodeTekst, esSprak.sprakKodeTekst)
                && Objects.equals(alternativTekst, esSprak.alternativTekst)
                && Objects.equals(beskrivelse, esSprak.beskrivelse)
                && Objects.equals(ferdighetMuntlig, esSprak.ferdighetMuntlig)
                && Objects.equals(ferdighetSkriftlig, esSprak.ferdighetSkriftlig);
    }

    @Override
    public int hashCode() {

        return Objects.hash(fraDato, sprakKode, sprakKodeTekst, alternativTekst, beskrivelse, ferdighetMuntlig, ferdighetSkriftlig);
    }

    @Override
    public String toString() {
        return "EsSprak{" + "fraDato=" + fraDato + ", sprakKode='" + sprakKode + '\''
                + ", sprakKodeTekst='" + sprakKodeTekst + '\'' + ", alternativTekst='" + alternativTekst
                + '\'' + ", beskrivelse='" + beskrivelse + '\''
                + ", ferdighetMuntlig='" + ferdighetMuntlig + '\'' + ", ferdighetSkriftlig='" + ferdighetSkriftlig + '\'' + '}';
    }

}
