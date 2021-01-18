package no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene;

import java.util.Date;
import java.util.Objects;

public class EsGodkjenning {

    private String tittel;
    private String utsteder;
    private Date gjennomfoert;
    private Date utloeper;
    private String konseptId;

    public EsGodkjenning() {
    }

    public EsGodkjenning(String tittel, String utsteder, Date gjennomfoert, Date utloeper, String konseptId) {
        this.tittel = tittel;
        this.utsteder = utsteder;
        this.gjennomfoert = gjennomfoert;
        this.utloeper = utloeper;
        this.konseptId = konseptId;
    }

    public String getTittel() {
        return tittel;
    }

    public String getUtsteder() {
        return utsteder;
    }

    public Date getGjennomfoert() {
        return gjennomfoert;
    }

    public Date getUtloeper() {
        return utloeper;
    }

    public String getKonseptId() {
        return konseptId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EsGodkjenning that = (EsGodkjenning) o;
        return Objects.equals(tittel, that.tittel) && Objects.equals(utsteder, that.utsteder) && Objects.equals(gjennomfoert, that.gjennomfoert) && Objects.equals(utloeper, that.utloeper) && Objects.equals(konseptId, that.konseptId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tittel, utsteder, gjennomfoert, utloeper, konseptId);
    }
}
