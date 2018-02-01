package no.nav.arbeid.cv.es.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.arbeid.cv.es.domene.EsCv;
import no.nav.arbeid.cv.es.domene.EsYrke;
import no.nav.arbeid.cv.events.CvEvent;
import no.nav.arbeid.cv.events.Yrkeserfaring;

public class EsCvTransformer {

  private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

  private static final Logger LOGGER = LoggerFactory.getLogger(EsCvTransformer.class);

  public EsCv transform(CvEvent p) {
    EsCv esCv = new EsCv(p.getArenaId(), toDate(p.getFodselsdato()), null, p.getEtternavn(),
        p.getFornavn(), p.getFormidlingsgruppekode(), p.getEpostadresse(), p.getBeskrivelse(),
        new Date(), new Date());
    esCv.addYrker(mapYrker(p.getYrkeserfaring()));
    return esCv;
  }

  private Collection<EsYrke> mapYrker(List<Yrkeserfaring> yrkeserfaring) {
    return yrkeserfaring.stream().map(this::mapYrke).collect(Collectors.toList());
  }

  private EsYrke mapYrke(Yrkeserfaring yrkeserfaring) {
    return new EsYrke(toDate(yrkeserfaring.getFraDato()), toDate(yrkeserfaring.getTilDato()),
        yrkeserfaring.getArbeidsgiver(), yrkeserfaring.getStillingstittel(),
        yrkeserfaring.getStyrkKode(), yrkeserfaring.getStyrkBeskrivelse());
  }

  private Date toDate(String date) {
    if (date == null || date.isEmpty()) {
      return null;
    }
    try {
      return sdf.parse(date);
    } catch (ParseException pe) {
      LOGGER.warn("Feilet under parsing av dato", pe);
      return null;
    }
  }

}
