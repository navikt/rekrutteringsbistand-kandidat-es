package no.nav.arbeid.cv.es.client;

import java.io.IOException;
import java.util.List;

import no.nav.arbeid.cv.es.domene.EsCv;

public interface EsCvClient {

  List<EsCv> findByYrkeserfaringStyrkKode(String styrk) throws IOException;

  List<EsCv> findByYrkeserfaringStyrkKodeTekst(String styrkBeskrivelse) throws IOException;

  void index(EsCv esCv) throws IOException;

  void createIndex() throws IOException;

  void deleteIndex() throws IOException;

  List<EsCv> findByEtternavnAndUtdanningNusKodeTekst(String etternavn, String utdanningNusKodeTekst) throws IOException;
}
