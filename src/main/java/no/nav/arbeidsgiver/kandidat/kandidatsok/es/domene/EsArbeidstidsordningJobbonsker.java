package no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsArbeidstidsordningJobbonsker {

    private String arbeidstidsordningKode;

    private String arbeidstidsordningKodeTekst;

    public EsArbeidstidsordningJobbonsker() {
    }

    public EsArbeidstidsordningJobbonsker(String arbeidstidsordningKode,
                                          String arbeidstidsordningKodeTekst) {
        this.arbeidstidsordningKode = arbeidstidsordningKode;
        this.arbeidstidsordningKodeTekst = arbeidstidsordningKodeTekst;
    }

    public String getArbeidstidsordningKode() {
        return arbeidstidsordningKode;
    }

    public String getArbeidstidsordningKodeTekst() {
        return arbeidstidsordningKodeTekst;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EsArbeidstidsordningJobbonsker that = (EsArbeidstidsordningJobbonsker) o;
        return Objects.equals(arbeidstidsordningKode, that.arbeidstidsordningKode)
                && Objects.equals(arbeidstidsordningKodeTekst, that.arbeidstidsordningKodeTekst);
    }

    @Override
    public int hashCode() {

        return Objects.hash(arbeidstidsordningKode, arbeidstidsordningKodeTekst);
    }

    @Override
    public String toString() {
        return "EsArbeidstidsordningJobbonsker{" + "arbeidstidsordningKode='" + arbeidstidsordningKode
                + '\'' + ", arbeidstidsordningKodeTekst='" + arbeidstidsordningKodeTekst + '\'' + '}';
    }

}
