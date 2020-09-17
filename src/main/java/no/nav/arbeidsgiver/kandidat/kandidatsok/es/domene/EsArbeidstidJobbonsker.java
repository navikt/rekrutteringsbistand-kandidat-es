package no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsArbeidstidJobbonsker {

    private String arbeidstidKode;

    private String arbeidstidKodeTekst;

    public EsArbeidstidJobbonsker() {
    }

    public EsArbeidstidJobbonsker(String arbeidstidKode,
                                  String arbeidstidKodeTekst) {
        this.arbeidstidKode = arbeidstidKode;
        this.arbeidstidKodeTekst = arbeidstidKodeTekst;
    }

    public String getArbeidstidKode() {
        return arbeidstidKode;
    }

    public String getArbeidstidKodeTekst() {
        return arbeidstidKodeTekst;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EsArbeidstidJobbonsker that = (EsArbeidstidJobbonsker) o;
        return Objects.equals(arbeidstidKode, that.arbeidstidKode)
                && Objects.equals(arbeidstidKodeTekst, that.arbeidstidKodeTekst);
    }

    @Override
    public int hashCode() {

        return Objects.hash(arbeidstidKode, arbeidstidKodeTekst);
    }

    @Override
    public String toString() {
        return "EsArbeidstidsordningJobbonsker{" + "arbeidstidKode='" + arbeidstidKode
                + '\'' + ", arbeidstidKodeTekst='" + arbeidstidKodeTekst + '\'' + '}';
    }

}
