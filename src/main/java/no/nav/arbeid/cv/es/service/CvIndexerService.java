package no.nav.arbeid.cv.es.service;

import no.nav.arbeid.cv.events.CvEvent;

import java.util.List;

public interface CvIndexerService {

  public void indekser(CvEvent cvEvent);
  public void bulkIndekser(List<CvEvent> cvEventer);
  public void slett(Long arenaId);
  public void bulkSlett(List<Long> arenaIder);
}
