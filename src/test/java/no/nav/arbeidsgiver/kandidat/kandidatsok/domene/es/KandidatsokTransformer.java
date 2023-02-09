package no.nav.arbeidsgiver.kandidat.kandidatsok.domene.es;

import no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.sok.EsCv;
import no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.sok.EsUtdanning;
import no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.sok.EsYrkeserfaring;

import java.util.List;
import java.util.stream.Collectors;

public class KandidatsokTransformer {

    public EsCv transformer(no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsCv cv) {
        return new EsCv(cv.getAktorId(), cv.getFodselsnummer(), cv.getFornavn(), cv.getEtternavn(), cv.getFodselsdato(),
                cv.getFodselsdatoErDnr(), cv.getPoststed(), cv.getEpostadresse(), cv.getTelefon(), cv.getMobiltelefon(),
                cv.getFormidlingsgruppekode(), cv.getKandidatnr(), cv.getTotalLengdeYrkeserfaring(),
                cv.getKvalifiseringsgruppekode(), cv.getHovedmaalkode(), transformerUtdListe(cv.getUtdanning()),
                transformerYrkeListe(cv.getYrkeserfaring()), cv.getOppstartKode(), cv.getForerkort(),
                cv.getKommuneNavn(), cv.getFylkeNavn(), cv.getVeilTilretteleggingsbehov());
    }

    private List<EsYrkeserfaring> transformerYrkeListe(
            List<no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsYrkeserfaring> liste) {
        return liste == null ? null
                : liste.stream().map(y -> transformer(y)).collect(Collectors.toList());
    }

    private List<EsUtdanning> transformerUtdListe(
            List<no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsUtdanning> liste) {
        return liste == null ? null
                : liste.stream().map(y -> transformer(y)).collect(Collectors.toList());
    }

    private EsUtdanning transformer(no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsUtdanning utd) {
        return new EsUtdanning(utd.getNusKode(), utd.getNusKodeGrad());
    }

    private EsYrkeserfaring transformer(no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsYrkeserfaring yrke) {
        return new EsYrkeserfaring(yrke.getStillingstittel(), yrke.getAlternativStillingstittel(), yrke.getYrkeserfaringManeder(), yrke.getFraDato(), yrke.getSokeTitler());
    }
}
