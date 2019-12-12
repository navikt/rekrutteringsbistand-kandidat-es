package no.nav.arbeid.cv.kandidatsok.es.domene;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsYrkeJobbonsker {

    private String styrkKode;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String styrkBeskrivelse;

    private List<String> sokeTitler = new ArrayList<>();

    private boolean primaertJobbonske;

    public EsYrkeJobbonsker() {
    }

    public EsYrkeJobbonsker(String styrkKode, String styrkBeskrivelse, boolean primaertJobbonske,
                            List<String> sokeTitler) {
        this.styrkKode = styrkKode;
        this.styrkBeskrivelse = styrkBeskrivelse;
        this.primaertJobbonske = primaertJobbonske;
        this.sokeTitler.add(styrkBeskrivelse);
        this.sokeTitler.addAll(sokeTitler);
    }

    public String getStyrkKode() {
        return styrkKode;
    }

    public String getStyrkBeskrivelse() {
        return styrkBeskrivelse;
    }

    public List<String> getSokeTitler() {
        return sokeTitler;
    }

    public boolean isPrimaertJobbonske() {
        return primaertJobbonske;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EsYrkeJobbonsker that = (EsYrkeJobbonsker) o;
        return primaertJobbonske == that.primaertJobbonske
                && Objects.equals(styrkKode, that.styrkKode)
                && Objects.equals(styrkBeskrivelse, that.styrkBeskrivelse);
    }

    @Override
    public int hashCode() {

        return Objects.hash(styrkKode, styrkBeskrivelse, primaertJobbonske);
    }

    @Override
    public String toString() {
        return "EsYrkeJobbonsker{" + "styrkKode='" + styrkKode + '\'' + ", styrkBeskrivelse='"
                + styrkBeskrivelse + '\'' + ", primaertJobbonske=" + primaertJobbonske + '}';
    }
}
