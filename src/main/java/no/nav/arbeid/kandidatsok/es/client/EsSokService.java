package no.nav.arbeid.kandidatsok.es.client;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import no.nav.arbeid.cv.kandidatsok.es.domene.EsCv;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.Sokekriterier;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.SokekriterierVeiledere;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.Sokeresultat;

public interface EsSokService {

    Sokeresultat arbeidsgiverSok(Sokekriterier sokekriterier) throws IOException;
    
    Sokeresultat veilederSok(SokekriterierVeiledere sokekriterier) throws IOException;

    List<String> typeAheadKompetanse(String prefix) throws IOException;

    List<String> typeAheadUtdanning(String prefix) throws IOException;

    List<String> typeAheadYrkeserfaring(String prefix) throws IOException;

    List<String> typeAheadGeografi(String prefix) throws IOException;

    List<String> typeAheadYrkeJobbonsker(String prefix) throws IOException;

    List<String> typeAheadSprak(String prefix) throws IOException;

    Optional<EsCv> arbeidsgiverHent(String kandidatnr) throws IOException;

    Optional<EsCv> veilederHent(String kandidatnr) throws IOException;
    
    Sokeresultat arbeidsgiverHentKandidater(List<String> kandidatnummer) throws IOException;
    
    Sokeresultat veilederHentKandidater(List<String> kandidatnummer) throws IOException;

    Optional<no.nav.arbeid.cv.kandidatsok.es.domene.sok.EsCv> veilederSokPaaFnr(String fnr) throws IOException;

    

}
