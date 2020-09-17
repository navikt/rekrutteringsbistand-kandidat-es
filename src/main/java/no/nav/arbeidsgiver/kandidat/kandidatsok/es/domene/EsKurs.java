package no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsKurs {

    private Date fraDato;

    private Date tilDato;

    private String tittel;

    private String arrangor;

    private String omfangEnhet;

    private Integer omfangVerdi;

    private String beskrivelse;

    public EsKurs() {
    }

    public EsKurs(Date fraDato, String tittel, String arrangor, String omfangEnhet,
                  Integer omfangVerdi) {
        this(fraDato, null, tittel, arrangor, omfangEnhet, omfangVerdi, "");
    }

    public EsKurs(Date fraDato, Date tilDato, String tittel, String arrangor, String omfangEnhet,
                  Integer omfangVerdi, String beskrivelse) {
        this.fraDato = fraDato;
        this.tilDato = tilDato;
        this.tittel = tittel;
        this.arrangor = arrangor;
        this.omfangEnhet = omfangEnhet;
        this.omfangVerdi = omfangVerdi;
        this.beskrivelse = beskrivelse;
    }

    public Date getFraDato() {
        return fraDato;
    }

    public Date getTilDato() {
        return tilDato;
    }

    public String getTittel() {
        return tittel;
    }

    public String getArrangor() {
        return arrangor;
    }

    public String getOmfangEnhet() {
        return omfangEnhet;
    }

    public Integer getOmfangVerdi() {
        return omfangVerdi;
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
        EsKurs esKurs = (EsKurs) o;
        return Objects.equals(fraDato, esKurs.fraDato) && Objects.equals(tilDato, esKurs.tilDato)
                && Objects.equals(tittel, esKurs.tittel) && Objects.equals(arrangor, esKurs.arrangor)
                && Objects.equals(omfangEnhet, esKurs.omfangEnhet)
                && Objects.equals(omfangVerdi, esKurs.omfangVerdi)
                && Objects.equals(beskrivelse, esKurs.beskrivelse);
    }

    @Override
    public int hashCode() {

        return Objects.hash(fraDato, tilDato, tittel, arrangor, omfangEnhet, omfangVerdi, beskrivelse);
    }

    @Override
    public String toString() {
        return "EsKurs{" + "fraDato=" + fraDato + ", tilDato=" + tilDato + ", tittel='" + tittel + '\''
                + ", arrangor='" + arrangor + '\'' + ", omfangEnhet='" + omfangEnhet + '\''
                + ", omfangVerdi=" + omfangVerdi + ", beskrivelse='" + beskrivelse + '\'' + '}';
    }

}
