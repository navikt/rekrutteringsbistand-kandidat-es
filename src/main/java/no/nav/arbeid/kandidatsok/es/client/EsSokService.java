package no.nav.arbeid.kandidatsok.es.client;

import java.io.IOException;
import java.util.List;

import no.nav.arbeid.cv.kandidatsok.es.domene.EsCv;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.Sokekriterier;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.Sokeresultat;

public interface EsSokService {

  Sokeresultat sok(Sokekriterier sokekriterier) throws IOException;

  List<String> typeAheadKompetanse(String prefix) throws IOException;

  List<String> typeAheadUtdanning(String prefix) throws IOException;

  List<String> typeAheadYrkeserfaring(String prefix) throws IOException;

  List<String> typeAheadGeografi(String prefix) throws IOException;

  List<String> typeAheadYrkeJobbonsker(String prefix) throws IOException;

  List<String> typeAheadSprak(String prefix) throws IOException;

  EsCv hent(String kandidatnr) throws IOException;

}
