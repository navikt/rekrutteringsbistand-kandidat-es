package no.nav.arbeid.kandidatsok.es.client;

import java.io.IOException;
import java.util.List;

import no.nav.arbeid.cv.kandidatsok.domene.es.EsCv;
import no.nav.arbeid.cv.kandidatsok.domene.sok.Sokekriterier;
import no.nav.arbeid.cv.kandidatsok.domene.sok.Sokeresultat;

public interface EsSokClient {

  Sokeresultat sok(Sokekriterier sokekriterier) throws IOException;

  /** @deprecated Bruk {@link #sok(Sokekriterier)} isteden */
  Sokeresultat sok(String fritekst, List<String> stillingstitler, List<String> kompetanser,
      List<String> utdanninger, List<String> geografiList, List<String> totalYrkeserfaring,
      List<String> utdanningsniva, String styrkKode, String nusKode, List<String> styrkKoder,
      List<String> nusKoder) throws IOException;

  List<String> typeAheadKompetanse(String prefix) throws IOException;

  List<String> typeAheadUtdanning(String prefix) throws IOException;

  List<String> typeAheadYrkeserfaring(String prefix) throws IOException;

  List<String> typeAheadGeografi(String prefix) throws IOException;

  List<String> typeAheadYrkeJobbonsker(String prefix) throws IOException;

  @Deprecated
  Sokeresultat findByYrkeserfaringStyrkKodeTekst(String styrkBeskrivelse) throws IOException;

  @Deprecated
  Sokeresultat findByEtternavnAndUtdanningNusKodeGrad(String etternavn,
      String utdanningNusKodeTekst) throws IOException;

  EsCv hent(String kandidatnr) throws IOException;

}
