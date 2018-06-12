package no.nav.arbeid.cv.indexer.service;

import java.util.List;

import no.nav.arbeid.cv.events.CvEvent;

public interface CvIndexerService {

  public void indekser(CvEvent cvEvent);

  public void bulkIndekser(List<CvEvent> cvEventer);

  public void slett(Long arenaId);

  public void bulkSlett(List<Long> arenaIder);
}
