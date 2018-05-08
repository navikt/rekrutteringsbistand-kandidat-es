package no.nav.arbeid.cv.es.service;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.arbeid.cv.es.client.EsCvClient;
import no.nav.arbeid.cv.es.domene.EsCv;
import no.nav.arbeid.cv.events.CvEvent;

public class DefaultCvIndexerService implements CvIndexerService {

  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCvIndexerService.class);

  private EsCvClient esCvClient;
  private EsCvTransformer transformer;

  public DefaultCvIndexerService(EsCvClient esRepo, EsCvTransformer transformer) {
    this.esCvClient = esRepo;
    this.transformer = transformer;
  }

  @Override
  public void indekser(CvEvent cvEvent) {
    EsCv esPerson = transformer.transform(cvEvent);
    try {
      esCvClient.index(esPerson);
    } catch (IOException e) {
      LOGGER.error("Feil under indeksering av CV", e);
    }
  }


  @Override
  public void bulkIndekser(List<CvEvent> cvEventer) {
    // TODO Implementer bulkindeksering i ES.
    for (CvEvent cvEvent: cvEventer) {
      indekser(cvEvent);
    }
  }

  @Override
  public void slett(Long arenaId) {
    // TODO implementer sletting fra indeks
    LOGGER.info("Slett arenaId {} fra ES indeks - foreløpig ikke implementert.", arenaId);
  }

  @Override
  public void bulkSlett(List<Long> arenaIder) {
    // TODO implementer bulk-sletting fra ES-indeks
    for (Long arenaId : arenaIder) {
      slett(arenaId);
    }
  }

}
