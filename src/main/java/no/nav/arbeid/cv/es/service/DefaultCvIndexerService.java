package no.nav.arbeid.cv.es.service;

import java.io.IOException;

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
}
