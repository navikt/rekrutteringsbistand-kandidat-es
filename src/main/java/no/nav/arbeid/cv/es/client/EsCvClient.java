package no.nav.arbeid.cv.es.client;

import java.io.IOException;
import java.util.List;
import no.nav.arbeid.cv.es.domene.EsCv;
import no.nav.arbeid.cv.es.domene.Sokeresultat;

public interface EsCvClient {

  Sokeresultat sok(String fritekst, List<String> stillingstitler, List<String> kompetanser,
      List<String> utdanninger, List<String> geografiList, String totalYrkeserfaring, String styrkKode,
      String nusKode, List<String> styrkKoder, List<String> nusKoder) throws IOException;

  void index(EsCv esCv) throws IOException;

  void createIndex() throws IOException;

  void deleteIndex() throws IOException;

  List<String> typeAheadKompetanse(String prefix) throws IOException;

  List<String> typeAheadUtdanning(String prefix) throws IOException;

  List<String> typeAheadYrkeserfaring(String prefix) throws IOException;

  List<String> typeAheadGeografi(String prefix) throws IOException;

  @Deprecated
  Sokeresultat findByYrkeserfaringStyrkKodeTekst(String styrkBeskrivelse) throws IOException;

  @Deprecated
  Sokeresultat findByEtternavnAndUtdanningNusKodeGrad(String etternavn,
      String utdanningNusKodeTekst) throws IOException;
}
