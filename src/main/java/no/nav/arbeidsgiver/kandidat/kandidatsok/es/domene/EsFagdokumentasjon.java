package no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsFagdokumentasjon {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String type;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String tittel;

    // TODO dette feltet er ikke lenger i bruk - bør fjernes
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String beskrivelse;

    public EsFagdokumentasjon() {
    }

    public EsFagdokumentasjon(String type, String tittel, String beskrivelse) {
        this.type = type;
        this.tittel = tittel;
        this.beskrivelse = beskrivelse;
    }

    public String getType() {
        return type;
    }

    public String getTittel() {
        return tittel;
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
        EsFagdokumentasjon esFagdokumentasjon = (EsFagdokumentasjon) o;
        return Objects.equals(type, esFagdokumentasjon.type)
                && Objects.equals(tittel, esFagdokumentasjon.tittel);
    }

    @Override
    public int hashCode() {

        return Objects.hash(type, tittel);
    }

    @Override
    public String toString() {
        return "EsFagdokumentasjon{" + "type='" + type + '\'' + ", tittel='" + tittel + '\'' + ", beskrivelse='" + beskrivelse + '\'' + '}';
    }

    public static String getFagdokumentTypeLabel(String fagdokumentType) {
        switch (fagdokumentType) {
            case "SVENNEBREV_FAGBREV":
                return "Fagbrev/svennebrev";
            case "MESTERBREV":
                return "Mesterbrev";
            case "AUTORISASJON":
                return "Autorisasjon";
            default:
                return fagdokumentType;
        }
    }

}
