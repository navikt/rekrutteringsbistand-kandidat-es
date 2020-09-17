package no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsAnnenErfaring {

    private Date fraDato;

    private Date tilDato;

    private String beskrivelse;

    private String rolle;

    public EsAnnenErfaring() {
    }

    public EsAnnenErfaring(Date fraDato, Date tilDato, String beskrivelse) {
        this.fraDato = fraDato;
        this.tilDato = tilDato;
        this.beskrivelse = beskrivelse;
    }

    public EsAnnenErfaring(Date fraDato, Date tilDato, String beskrivelse, String rolle) {
        this(fraDato, tilDato, beskrivelse);
        this.rolle = rolle;
    }

    public Date getFraDato() {
        return fraDato;
    }

    public Date getTilDato() {
        return tilDato;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public String getRolle() {
        return rolle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EsAnnenErfaring that = (EsAnnenErfaring) o;
        return Objects.equals(fraDato, that.fraDato) && Objects.equals(tilDato, that.tilDato)
                && Objects.equals(beskrivelse, that.beskrivelse) && Objects.equals(rolle, that.rolle);
    }

    @Override
    public int hashCode() {

        return Objects.hash(fraDato, tilDato, beskrivelse, rolle);
    }

    @Override
    public String toString() {
        return "EsAnnenErfaring{" + "fraDato=" + fraDato + ", tilDato=" + tilDato + ", beskrivelse='"
                + beskrivelse + '\'' + ", rolle='" + rolle + '\'' + '}';
    }

}
