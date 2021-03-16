package no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EsPerioderMedInaktivitet {

    private LocalDate startdatoForInneværendeInaktivePeriode;
    private List<LocalDate> sluttdatoer = new ArrayList<>();

    public EsPerioderMedInaktivitet(LocalDate startdatoForInneværendeInaktivePeriode, List<LocalDate> sluttdatoer) {
        this.startdatoForInneværendeInaktivePeriode = startdatoForInneværendeInaktivePeriode;
        this.sluttdatoer.addAll(sluttdatoer);
    }

    public LocalDate getStartdatoForInneværendeInaktivePeriode() {
        return startdatoForInneværendeInaktivePeriode;
    }

    public List<LocalDate> getSluttdatoer() {
        return sluttdatoer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EsPerioderMedInaktivitet that = (EsPerioderMedInaktivitet) o;
        return Objects.equals(getStartdatoForInneværendeInaktivePeriode(), that.getStartdatoForInneværendeInaktivePeriode()) && getSluttdatoer().equals(that.getSluttdatoer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStartdatoForInneværendeInaktivePeriode(), getSluttdatoer());
    }
}
