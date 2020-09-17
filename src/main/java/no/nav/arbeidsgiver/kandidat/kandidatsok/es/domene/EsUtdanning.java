package no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsUtdanning {

    private Date fraDato;

    private Date tilDato;

    private String utdannelsessted;

    private String nusKode;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String nusKodeGrad;

    private String alternativGrad;

    // Her ligger autorisasjon, svennebrev_fagbrev, mesterbrev
    private String yrkestatus;

    private String beskrivelse;

    public EsUtdanning() {
    }

    public EsUtdanning(Date fraDato, Date tilDato, String utdannelsessted, String nusKode,
                       String alternativGrad, String beskrivelse, String yrkestatus) {
        this(fraDato, tilDato, utdannelsessted, nusKode, null, alternativGrad);
        this.yrkestatus = yrkestatus;
        this.beskrivelse = beskrivelse;
    }

    public EsUtdanning(Date fraDato, Date tilDato, String utdannelsessted, String nusKode,
                       String nusKodeGrad, String alternativGrad) {
        this.fraDato = fraDato;
        this.tilDato = tilDato;
        this.utdannelsessted = utdannelsessted;
        this.nusKode = nusKode;
        this.nusKodeGrad = nusKodeGrad;
        this.alternativGrad = alternativGrad;
    }

    public Date getFraDato() {
        return fraDato;
    }

    public Date getTilDato() {
        return tilDato;
    }

    public String getUtdannelsessted() {
        return utdannelsessted;
    }

    public String getNusKode() {
        return nusKode;
    }

    public String getNusKodeGrad() {
        return nusKodeGrad;
    }

    public String getYrkestatus() {
        return yrkestatus;
    }

    public String getAlternativGrad() {
        return alternativGrad;
    }

    public String getBeskrivelse() {
        return beskrivelse;
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
        return Objects.equals(fraDato, that.fraDato) && Objects.equals(tilDato, that.tilDato)
                && Objects.equals(utdannelsessted, that.utdannelsessted)
                && Objects.equals(nusKode, that.nusKode) && Objects.equals(nusKodeGrad, that.nusKodeGrad)
                && Objects.equals(alternativGrad, that.alternativGrad)
                && Objects.equals(beskrivelse, that.beskrivelse);
    }

    @Override
    public int hashCode() {

        return Objects.hash(fraDato, tilDato, utdannelsessted, nusKode, nusKodeGrad, alternativGrad, beskrivelse);
    }

    @Override
    public String toString() {
        return "EsUtdanning{" + "fraDato=" + fraDato + ", tilDato=" + tilDato + ", utdannelsessted='"
                + utdannelsessted + '\'' + ", nusKode='" + nusKode + '\'' + ", nusKodeGrad='" + nusKodeGrad
                + '\'' + ", alternativGrad='" + alternativGrad + '\'' + ", beskrivelse='" + beskrivelse + '\'' + '}';
    }

}
