package no.nav.arbeid.cv.kandidatsok.domene.es;

import java.util.List;
import java.util.stream.Collectors;

import no.nav.arbeid.cv.kandidatsok.es.domene.sok.EsCv;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.EsUtdanning;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.EsYrkeserfaring;

public class KandidatsokTransformer {

  public EsCv transformer(no.nav.arbeid.cv.kandidatsok.es.domene.EsCv cv) {
    return new EsCv(cv.getFodselsnummer(), cv.getFornavn(), cv.getEtternavn(), cv.getFodselsdato(),
            cv.getFodselsdatoErDnr(), cv.getPoststed(), cv.getEpostadresse(), cv.getTelefon(), cv.getMobiltelefon(), 
            cv.getFormidlingsgruppekode(), cv.getArenaPersonId(),
            cv.getArenaKandidatnr(), cv.getTotalLengdeYrkeserfaring(), cv.getKvalifiseringsgruppekode(),
            transformerUtdListe(cv.getUtdanning()), transformerYrkeListe(cv.getYrkeserfaring()));
  }

  private List<EsYrkeserfaring> transformerYrkeListe(
      List<no.nav.arbeid.cv.kandidatsok.es.domene.EsYrkeserfaring> liste) {
    return liste == null ? null
        : liste.stream().map(y -> transformer(y)).collect(Collectors.toList());
  }

  private List<EsUtdanning> transformerUtdListe(
      List<no.nav.arbeid.cv.kandidatsok.es.domene.EsUtdanning> liste) {
    return liste == null ? null
        : liste.stream().map(y -> transformer(y)).collect(Collectors.toList());
  }

  private EsUtdanning transformer(no.nav.arbeid.cv.kandidatsok.es.domene.EsUtdanning utd) {
    return new EsUtdanning(utd.getNusKode(), utd.getNusKodeGrad());
  }

  private EsYrkeserfaring transformer(no.nav.arbeid.cv.kandidatsok.es.domene.EsYrkeserfaring yrke) {
    return new EsYrkeserfaring(yrke.getStyrkKodeStillingstittel(), yrke.getYrkeserfaringManeder(), yrke.getFraDato());
  }
}
