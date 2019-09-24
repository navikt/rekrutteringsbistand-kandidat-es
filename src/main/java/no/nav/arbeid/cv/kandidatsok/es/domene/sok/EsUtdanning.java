package no.nav.arbeid.cv.kandidatsok.es.domene.sok;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsUtdanning {

    private String nusKode;
    private String nusKodeGrad;

    public EsUtdanning() {
    }

    public EsUtdanning(String nusKode, String nusKodeGrad) {
        this.nusKode = nusKode;
        this.nusKodeGrad = nusKodeGrad;
    }


    public String getNusKode() {
        return nusKode;
    }

    public String getNusKodeGrad() {
        return nusKodeGrad;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EsUtdanning that = (EsUtdanning) o;
        return Objects.equals(nusKode, that.nusKode) && Objects.equals(nusKodeGrad, that.nusKodeGrad);
    }

    @Override
    public int hashCode() {

        return Objects.hash(nusKode, nusKodeGrad);
    }

    @Override
    public String toString() {
        return "EsUtdanning{" + " nusKode='" + nusKode + '\'' + ", nusKodeGrad='" + nusKodeGrad + '\''
                + '}';
    }

}
