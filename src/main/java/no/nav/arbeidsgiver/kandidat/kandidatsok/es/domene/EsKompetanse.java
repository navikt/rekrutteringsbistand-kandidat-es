package no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsKompetanse {

    private Date fraDato;

    private String kompKode;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String kompKodeNavn;

    private List<String> sokeNavn = new ArrayList<>();

    private String alternativtNavn;

    private String beskrivelse;

    public EsKompetanse() {
    }

    public EsKompetanse(String kompetanse, List<String> sokeNavn) {
        this(null, null, kompetanse, kompetanse, "", sokeNavn);
    }

    public EsKompetanse(Date fraDato, String kompKode, String kompKodeNavn, String alternativtNavn,
                        String beskrivelse, List<String> sokeNavn) {
        this.fraDato = fraDato;
        this.kompKode = kompKode;
        this.kompKodeNavn = kompKodeNavn;
        this.alternativtNavn = alternativtNavn;
        this.beskrivelse = beskrivelse;
        this.sokeNavn.add(kompKodeNavn);
        this.sokeNavn.addAll(sokeNavn);
    }

    public Date getFraDato() {
        return fraDato;
    }

    public String getKompKode() {
        return kompKode;
    }

    public String getKompKodeNavn() {
        return kompKodeNavn;
    }

    public String getAlternativtNavn() {
        return alternativtNavn;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public List<String> getSokeNavn() {
        return sokeNavn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EsKompetanse that = (EsKompetanse) o;
        return Objects.equals(fraDato, that.fraDato) && Objects.equals(kompKode, that.kompKode)
                && Objects.equals(kompKodeNavn, that.kompKodeNavn)
                && Objects.equals(alternativtNavn, that.alternativtNavn)
                && Objects.equals(alternativtNavn, that.alternativtNavn)
                && Objects.equals(beskrivelse, that.beskrivelse);
    }

    @Override
    public int hashCode() {

        return Objects.hash(fraDato, kompKode, kompKodeNavn, alternativtNavn, beskrivelse);
    }

    @Override
    public String toString() {
        return "EsKompetanse{" + "fraDato=" + fraDato + ", kompKode='" + kompKode + '\''
                + ", kompKodeNavn='" + kompKodeNavn + '\'' + ", alternativtNavn='" + alternativtNavn
                + '\'' + ", beskrivelse='" + beskrivelse + '\'' + '}';
    }

}
