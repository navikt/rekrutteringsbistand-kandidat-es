package no.nav.arbeid.kandidatsok.es.client;

import java.util.List;
import java.util.Optional;

import no.nav.arbeid.cv.kandidatsok.es.domene.EsCv;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.Sokekriterier;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.SokekriterierVeiledere;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.Sokeresultat;

public interface EsSokService {

    Sokeresultat arbeidsgiverSok(Sokekriterier sokekriterier);
    
    Sokeresultat veilederSok(SokekriterierVeiledere sokekriterier);

    List<String> typeAheadKompetanse(String prefix);

    List<String> typeAheadUtdanning(String prefix);

    List<String> typeAheadYrkeserfaring(String prefix);

    List<String> typeAheadGeografi(String prefix);

    List<String> typeAheadYrkeJobbonsker(String prefix);

    List<String> typeAheadSprak(String prefix);

    Optional<EsCv> arbeidsgiverHent(String kandidatnr);

    Optional<EsCv> veilederHent(String kandidatnr);
    
    Sokeresultat arbeidsgiverHentKandidater(List<String> kandidatnummer);
    
    Sokeresultat veilederHentKandidater(List<String> kandidatnummer);

    Optional<no.nav.arbeid.cv.kandidatsok.es.domene.sok.EsCv> veilederSokPaaFnr(String fnr);

    

}
