package no.nav.arbeid.cv.indexer.service;

import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import no.nav.arbeid.cv.events.CvEvent;
import no.nav.arbeid.cv.indexer.domene.ApplicationException;
import no.nav.arbeid.cv.indexer.domene.EsCv;
import no.nav.arbeid.cv.indexer.domene.OperationalException;
import no.nav.arbeid.cv.indexer.es.client.EsIndexerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class DefaultCvIndexerService implements CvIndexerService {

  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCvIndexerService.class);

  private EsIndexerClient esCvClient;
  private EsCvTransformer transformer;
  private MeterRegistry meterRegistry;
  private DistributionSummary bulkSummary;
  private AtomicLong antallIndekserteCVer;

  public DefaultCvIndexerService(EsIndexerClient esRepo, EsCvTransformer transformer, MeterRegistry meterRegistry) {
    this.esCvClient = esRepo;
    this.transformer = transformer;
    this.meterRegistry = meterRegistry;

    antallIndekserteCVer = meterRegistry.gauge("cv.es.index", new AtomicLong(0));
    bulkSummary =  DistributionSummary.builder("cv.es.bulkindekser")
            .description("Antall CV'en mottatt i bulk")
            .register(meterRegistry);

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
    Timer.builder("cv.es.bulkindekser").publishPercentileHistogram().register(meterRegistry).record(() -> {
      if (cvEventer.isEmpty())
        return;
      List<EsCv> esPersoner = new ArrayList<>(cvEventer.size());
      try {
        for (CvEvent cvEvent : cvEventer) {
          esPersoner.add(transformer.transform(cvEvent));
        }
      } catch (Exception e) {
        LOGGER.info("Feil ved transformering av {} cveventer som skal indekseres: {}",
                cvEventer.size(), e.getMessage(), e);
        meterRegistry.counter("cv.es.index.feil", Tags.of("type", "applikasjon")).increment();
        throw new ApplicationException(
                "Feil ved transformering av cveventer som skal indekseres: " + e.getMessage(), e);
      }

      try {
        esCvClient.bulkIndex(esPersoner);
        oppdaterEsGauge();
      } catch (IOException e) {
        meterRegistry.counter("cv.es.index.feil", Tags.of("type", "infrastruktur")).increment();
        throw new OperationalException(
                "Infrastrukturfeil ved bulkindeksering av cver: " + e.getMessage(), e);
      }
      meterRegistry.counter("cv.es.indekser").increment(cvEventer.size());
      bulkSummary.record(cvEventer.size());
    });
  }

  @Override
  public void oppdaterEsGauge() {
    long antallIndeksert = esCvClient.antallIndeksert();
    antallIndekserteCVer.set(antallIndeksert);
  }

  @Override
  public void slett(Long arenaId) {
    LOGGER.info("Slett arenaId {} fra ES indeks.", arenaId);
    bulkSlett(Arrays.asList(arenaId));
  }

  @Override
  public void bulkSlett(List<Long> arenaIder) {
    Timer.builder("cv.es.bulkslett").publishPercentileHistogram().register(meterRegistry).record(() -> {
      if (arenaIder.isEmpty())
        return;
      try {
        esCvClient.bulkSlett(arenaIder);
        oppdaterEsGauge();
      } catch (IOException e) {
        meterRegistry.counter("cv.es.slett.feil", Tags.of("type", "infrastruktur")).increment();
        throw new OperationalException(
            "Infrastrukturfeil ved bulksletting av cver: " + e.getMessage(), e);
      }
      meterRegistry.counter("cv.es.slett").increment(arenaIder.size());
    });
  }

}
