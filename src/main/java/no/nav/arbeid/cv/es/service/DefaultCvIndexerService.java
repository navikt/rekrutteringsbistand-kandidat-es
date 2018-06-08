package no.nav.arbeid.cv.es.service;

import no.nav.arbeid.cv.es.client.EsCvClient;
import no.nav.arbeid.cv.es.domene.ApplicationException;
import no.nav.arbeid.cv.es.domene.EsCv;
import no.nav.arbeid.cv.es.domene.OperationalException;
import no.nav.arbeid.cv.events.CvEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    Long arenaId = cvEvent.getArenaPersonId();
    EsCv esPerson = transformer.transform(cvEvent);
    try {
      esCvClient.index(esPerson);
    } catch (IOException e) {
      LOGGER.info("Feil under indeksering av CV, arenaId: {} {}", arenaId, e.getMessage(), e);
      throw new OperationalException("Feil under indeksering av CV med arenaId " + arenaId, e);
    }
  }


  @Override
  public void bulkIndekser(List<CvEvent> cvEventer) {
    if (cvEventer.isEmpty())
      return;
    List<EsCv> esPersoner = new ArrayList<>(cvEventer.size());
    try {
      for (CvEvent cvEvent : cvEventer) {
        esPersoner.add(transformer.transform(cvEvent));
      }
    } catch (Exception e) {
      LOGGER.info("Feil ved transformering av {} cveventer som skal indekseres: {}", cvEventer.size(), e.getMessage(), e);
      throw new ApplicationException("Feil ved transformering av cveventer som skal indekseres: " + e.getMessage(), e);
    }

    try {
        esCvClient.bulkIndex(esPersoner);
    } catch (IOException e) {
        throw new OperationalException("Infrastrukturfeil ved bulkindeksering av cver: " + e.getMessage(), e);
    }
  }

  @Override
  public void slett(Long arenaId) {
    LOGGER.info("Slett arenaId {} fra ES indeks.", arenaId);
    bulkSlett(Arrays.asList(arenaId));
  }

  @Override
  public void bulkSlett(List<Long> arenaIder) {
    if (arenaIder.isEmpty())
      return;
    try {
          esCvClient.bulkSlett(arenaIder);
      } catch (IOException e) {
          throw new OperationalException("Infrastrukturfeil ved bulksletting av cver: " + e.getMessage(), e);
      }
  }

}
