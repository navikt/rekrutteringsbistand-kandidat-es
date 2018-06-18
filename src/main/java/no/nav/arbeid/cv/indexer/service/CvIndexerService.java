package no.nav.arbeid.cv.indexer.service;

import java.util.List;

import no.nav.arbeid.cv.events.CvEvent;

public interface CvIndexerService {

  public void indekser(CvEvent cvEvent);

  public void bulkIndekser(List<CvEvent> cvEventer);

  /** Oppdaterer metrikken for antall CV'er som er indeksert i Elastic Search */
  void oppdaterEsGauge();

  public void slett(Long arenaId);

  public void bulkSlett(List<Long> arenaIder);
}
