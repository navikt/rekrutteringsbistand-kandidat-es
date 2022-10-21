package no.nav.arbeidsgiver.kandidatsok.es.client;

import no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsCv;
import no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.sok.Sokeresultat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EsSokService {

    List<String> typeAheadKompetanse(String prefix);

    List<String> typeAheadUtdanning(String prefix);

    List<String> typeaheadYrkeserfaring(String prefix);

    List<String> typeAheadGeografi(String prefix);

    List<String> typeAheadYrkeJobbonsker(String prefix);

    List<String> typeAheadSprak(String prefix);

    List<String> typeAheadNavkontor(String searchTerm);

    Optional<EsCv> veilederHent(String kandidatnr);

    Sokeresultat veilederHentKandidater(List<String> kandidatnummer);

    Optional<no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.sok.EsCv> veilederSokPaaFnr(String fnr);

    Boolean harHullICv(String aktorId, LocalDate dato);
}
