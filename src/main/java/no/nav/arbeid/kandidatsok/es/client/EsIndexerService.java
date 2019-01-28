package no.nav.arbeid.kandidatsok.es.client;

import java.io.IOException;
import java.util.List;

import no.nav.arbeid.cv.kandidatsok.es.domene.EsCv;

public interface EsIndexerService {

  void index(EsCv esCv) throws IOException;

  int bulkIndex(List<EsCv> esCver) throws IOException;

  void bulkSlettKandidatnr(List<String> kandidatnr) throws IOException;
  
  void bulkSlett(List<Long> arenaPersonIder) throws IOException;

  void createIndex() throws IOException;

  void deleteIndex() throws IOException;

  boolean doesIndexExist() throws IOException;

  long antallIndeksert();
  long antallIndeksertSynligForVeileder();
  long antallIndeksertSynligForArbeidsgiver();
}
