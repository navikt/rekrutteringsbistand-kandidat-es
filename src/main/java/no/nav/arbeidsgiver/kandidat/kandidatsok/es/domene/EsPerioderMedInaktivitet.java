package no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class EsPerioderMedInaktivitet {

    private Date startdatoForInneværendeInaktivePeriode;
    private List<Date> sluttdatoerForInaktivePerioderPåToÅrEllerMer = new ArrayList<>();

    public EsPerioderMedInaktivitet(Date startdatoForInneværendeInaktivePeriode, List<Date> sluttdatoerForInaktivePerioderPåToÅrEllerMer) {
        this.startdatoForInneværendeInaktivePeriode = startdatoForInneværendeInaktivePeriode;
        this.sluttdatoerForInaktivePerioderPåToÅrEllerMer.addAll(sluttdatoerForInaktivePerioderPåToÅrEllerMer);
    }

    public List<Date> getSluttdatoerForInaktivePerioderPåToÅrEllerMer() {
        return sluttdatoerForInaktivePerioderPåToÅrEllerMer;
    }

    public Date getStartdatoForInneværendeInaktivePeriode() {
        return startdatoForInneværendeInaktivePeriode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EsPerioderMedInaktivitet that = (EsPerioderMedInaktivitet) o;
        return Objects.equals(getStartdatoForInneværendeInaktivePeriode(), that.getStartdatoForInneværendeInaktivePeriode()) && getSluttdatoerForInaktivePerioderPåToÅrEllerMer().equals(that.getSluttdatoerForInaktivePerioderPåToÅrEllerMer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStartdatoForInneværendeInaktivePeriode(), getSluttdatoerForInaktivePerioderPåToÅrEllerMer());
    }
}
