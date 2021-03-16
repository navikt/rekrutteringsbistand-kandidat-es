package no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class EsPerioderMedInaktivitet {

    private Date startdatoForInneværendeInaktivePeriode;
    private List<Date> sluttdatoer = new ArrayList<>();

    public EsPerioderMedInaktivitet(Date startdatoForInneværendeInaktivePeriode, List<Date> sluttdatoer) {
        this.startdatoForInneværendeInaktivePeriode = startdatoForInneværendeInaktivePeriode;
        this.sluttdatoer.addAll(sluttdatoer);
    }

    public Date getStartdatoForInneværendeInaktivePeriode() {
        return startdatoForInneværendeInaktivePeriode;
    }

    public List<Date> getSluttdatoer() {
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
