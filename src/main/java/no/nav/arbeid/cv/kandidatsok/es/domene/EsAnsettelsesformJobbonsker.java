package no.nav.arbeid.cv.kandidatsok.es.domene;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsAnsettelsesformJobbonsker {

    private String ansettelsesformKode;

    private String ansettelsesformKodeTekst;

    public EsAnsettelsesformJobbonsker() {
    }

    public EsAnsettelsesformJobbonsker(String ansettelsesformKode,
                                       String ansettelsesformKodeTekst) {
        this.ansettelsesformKode = ansettelsesformKode;
        this.ansettelsesformKodeTekst = ansettelsesformKodeTekst;
    }

    public String getAnsettelsesformKode() {
        return ansettelsesformKode;
    }

    public String getAnsettelsesformKodeTekst() {
        return ansettelsesformKodeTekst;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EsAnsettelsesformJobbonsker that = (EsAnsettelsesformJobbonsker) o;
        return Objects.equals(ansettelsesformKode, that.ansettelsesformKode)
                && Objects.equals(ansettelsesformKodeTekst, that.ansettelsesformKodeTekst);
    }

    @Override
    public int hashCode() {

        return Objects.hash(ansettelsesformKode, ansettelsesformKodeTekst);
    }

    @Override
    public String toString() {
        return "EsAnsettelsesformJobbonsker{" + "ansettelsesformKode='" + ansettelsesformKode
                + '\'' + ", ansettelsesformKodeTekst='" + ansettelsesformKodeTekst + '\'' + '}';
    }
}
