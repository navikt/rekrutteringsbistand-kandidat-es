package no.nav.arbeid.cv.kandidatsok.es.domene;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsGeografiJobbonsker {

    private String geografiKodeTekst;

    private String geografiKode;

    public EsGeografiJobbonsker() {
    }

    public EsGeografiJobbonsker(String geografiKodeTekst, String geografiKode) {
        this.geografiKodeTekst = geografiKodeTekst;
        this.geografiKode = geografiKode;
    }

    public String getGeografiKodeTekst() {
        return geografiKodeTekst;
    }

    public String getGeografiKode() {
        return geografiKode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EsGeografiJobbonsker that = (EsGeografiJobbonsker) o;
        return Objects.equals(geografiKodeTekst, that.geografiKodeTekst)
                && Objects.equals(geografiKode, that.geografiKode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(geografiKodeTekst, geografiKode);
    }

}
