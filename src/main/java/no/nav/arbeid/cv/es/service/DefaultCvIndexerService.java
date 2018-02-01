package no.nav.arbeid.cv.es.service;

import no.nav.arbeid.cv.es.domene.EsCv;
import no.nav.arbeid.cv.es.repository.EsCvRepository;
import no.nav.arbeid.cv.events.CvEvent;

public class DefaultCvIndexerService implements CvIndexerService {

  private EsCvRepository esRepo;
  private EsCvTransformer transformer;

  public DefaultCvIndexerService(EsCvRepository esRepo, EsCvTransformer transformer) {
    this.esRepo = esRepo;
    this.transformer = transformer;
  }

  @Override
  public void indekser(CvEvent cvEvent) {
    EsCv esPerson = transformer.transform(cvEvent);
    esRepo.index(esPerson);
  }
}
