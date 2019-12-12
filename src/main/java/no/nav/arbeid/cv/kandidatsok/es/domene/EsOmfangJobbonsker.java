package no.nav.arbeid.cv.kandidatsok.es.domene;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsOmfangJobbonsker {

    private String omfangKode;

    private String omfangKodeTekst;

    public EsOmfangJobbonsker() {
    }

    public EsOmfangJobbonsker(String omfangKode, String omfangKodeTekst) {
        this.omfangKode = omfangKode;
        this.omfangKodeTekst = omfangKodeTekst;
    }

    public String getOmfangKode() {
        return omfangKode;
    }

    public String getOmfangKodeTekst() {
        return omfangKodeTekst;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EsOmfangJobbonsker that = (EsOmfangJobbonsker) o;
        return Objects.equals(omfangKode, that.omfangKode)
                && Objects.equals(omfangKodeTekst, that.omfangKodeTekst);
    }

    @Override
    public int hashCode() {

        return Objects.hash(omfangKode, omfangKodeTekst);
    }

    @Override
    public String toString() {
        return "EsOmfangJobbonsker{" + "omfangKode='" + omfangKode + '\''
                + ", omfangKodeTekst='" + omfangKodeTekst + '\'' + '}';
    }
}
