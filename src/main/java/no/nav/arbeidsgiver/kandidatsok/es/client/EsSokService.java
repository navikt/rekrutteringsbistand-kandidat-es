package no.nav.arbeidsgiver.kandidatsok.es.client;

import no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsCv;
import no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.sok.Sokeresultat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EsSokService {

    Optional<EsCv> veilederHent(String kandidatnr);

    Sokeresultat veilederHentKandidater(List<String> kandidatnummer);

    Optional<no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.sok.EsCv> veilederSokPaaFnr(String fnr);

    Boolean harHullICv(String aktorId, LocalDate dato);
}
