package no.nav.arbeidsgiver.kandidatsok.es.client;

import no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsCv;
import no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.sok.Sokekriterier;
import no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.sok.SokekriterierVeiledere;
import no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.sok.Sokeresultat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EsSokService {

    Sokeresultat arbeidsgiverSok(Sokekriterier sokekriterier);

    Sokeresultat veilederSok(SokekriterierVeiledere sokekriterier);

    List<String> typeAheadKompetanse(String prefix);

    List<String> typeAheadUtdanning(String prefix);

    List<String> typeaheadYrkeserfaring(String prefix);

    List<String> typeAheadGeografi(String prefix);

    List<String> typeAheadYrkeJobbonsker(String prefix);

    List<String> typeAheadSprak(String prefix);

    List<String> typeAheadNavkontor(String searchTerm);

    Optional<EsCv> arbeidsgiverHent(String kandidatnr);

    Optional<EsCv> veilederHent(String kandidatnr);

    Sokeresultat arbeidsgiverHentKandidater(List<String> kandidatnummer);

    Sokeresultat arbeidsgiverHentKandidaterForVisning(List<String> kandidatnummer);

    Sokeresultat veilederHentKandidater(List<String> kandidatnummer);

    Optional<no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.sok.EsCv> veilederSokPaaFnr(String fnr);

    Boolean haddeHullICv(String aktorId, LocalDate dato);
}
