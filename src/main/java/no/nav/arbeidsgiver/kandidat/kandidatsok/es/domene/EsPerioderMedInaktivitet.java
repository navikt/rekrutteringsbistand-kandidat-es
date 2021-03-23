package no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class EsPerioderMedInaktivitet {

    private Date startdatoForInnevarendeInaktivePeriode;
    private List<Date> sluttdatoerForInaktivePerioderPaToArEllerMer = new ArrayList<>();

    public EsPerioderMedInaktivitet() {
    }

    public EsPerioderMedInaktivitet(Date startdatoForInnevarendeInaktivePeriode, List<Date> sluttdatoerForInaktivePerioderPaToArEllerMer) {
        this.startdatoForInnevarendeInaktivePeriode = startdatoForInnevarendeInaktivePeriode;
        if (sluttdatoerForInaktivePerioderPaToArEllerMer != null) {
            this.sluttdatoerForInaktivePerioderPaToArEllerMer.addAll(sluttdatoerForInaktivePerioderPaToArEllerMer);
        }
    }

    public List<Date> getSluttdatoerForInaktivePerioderPaToArEllerMer() {
        return sluttdatoerForInaktivePerioderPaToArEllerMer;
    }

    public Date getStartdatoForInnevarendeInaktivePeriode() {
        return startdatoForInnevarendeInaktivePeriode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EsPerioderMedInaktivitet that = (EsPerioderMedInaktivitet) o;
        return Objects.equals(getStartdatoForInnevarendeInaktivePeriode(), that.getStartdatoForInnevarendeInaktivePeriode()) && Objects.equals(getSluttdatoerForInaktivePerioderPaToArEllerMer(), that.getSluttdatoerForInaktivePerioderPaToArEllerMer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStartdatoForInnevarendeInaktivePeriode(), getSluttdatoerForInaktivePerioderPaToArEllerMer());
    }
}
