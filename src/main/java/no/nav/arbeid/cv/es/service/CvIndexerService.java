package no.nav.arbeid.cv.es.service;

import no.nav.arbeid.cv.events.CvEvent;

public interface CvIndexerService {

  public void indekser(CvEvent cvEvent);

}
