package no.nav.arbeid.cv.indexer.es.client;

import java.io.IOException;
import java.util.List;

import no.nav.arbeid.cv.indexer.domene.EsCv;
import no.nav.arbeid.cv.indexer.domene.Sokekriterier;
import no.nav.arbeid.cv.indexer.domene.Sokeresultat;

public interface EsIndexerClient {

  void index(EsCv esCv) throws IOException;

  void bulkIndex(List<EsCv> esCver) throws IOException;

  void bulkSlett(List<Long> arenaPersonIder) throws IOException;

  void createIndex() throws IOException;

  void deleteIndex() throws IOException;

  boolean doesIndexExist() throws IOException;

  Sokeresultat sok(Sokekriterier sk) throws IOException;

  long antallIndeksert();
}
