package no.nav.arbeid.cv.kandidatsok.domene.sok;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsCv {

  private String fodselsnummer;

  private String formidlingsgruppekode;

  private Long arenaPersonId;

  private String arenaKandidatnr;
  private int totalLengdeYrkeserfaring;

  private List<EsUtdanning> utdanning = new ArrayList<>();

  private List<EsYrkeserfaring> yrkeserfaring = new ArrayList<>();

  public EsCv() {}

  public EsCv(String fodselsnummer, String formidlingsgruppekode, Long arenaPersonId,
      String arenaKandidatnr, int totalLengdeYrkeserfaring, List<EsUtdanning> utdanning,
      List<EsYrkeserfaring> yrkeserfaring) {
    super();
    this.fodselsnummer = fodselsnummer;
    this.formidlingsgruppekode = formidlingsgruppekode;
    this.arenaPersonId = arenaPersonId;
    this.arenaKandidatnr = arenaKandidatnr;
    this.totalLengdeYrkeserfaring = totalLengdeYrkeserfaring;
    this.utdanning = utdanning;
    this.yrkeserfaring = yrkeserfaring;
  }



  // Adderfunksjoner
  public void addUtdanning(EsUtdanning utdanning) {
    this.utdanning.add(utdanning);
  }

  public void addUtdanning(Collection<EsUtdanning> utdanningListe) {
    this.utdanning.addAll(utdanningListe);
  }

  public void addYrkeserfaring(EsYrkeserfaring yrkeserfaring) {
    this.yrkeserfaring.add(yrkeserfaring);
  }

  public void addYrkeserfaring(Collection<EsYrkeserfaring> yrkeserfaringListe) {
    yrkeserfaringListe.forEach(y -> this.totalLengdeYrkeserfaring += y.getYrkeserfaringManeder());
    this.yrkeserfaring.addAll(yrkeserfaringListe);
  }


  public String getFodselsnummer() {
    return fodselsnummer;
  }


  public String getFormidlingsgruppekode() {
    return formidlingsgruppekode;
  }


  public Long getArenaPersonId() {
    return arenaPersonId;
  }

  public String getArenaKandidatnr() {
    return arenaKandidatnr;
  }

  public int getTotalLengdeYrkeserfaring() {
    return totalLengdeYrkeserfaring;
  }

  public List<EsUtdanning> getUtdanning() {
    return utdanning;
  }

  public List<EsYrkeserfaring> getYrkeserfaring() {
    return yrkeserfaring;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EsCv esCv = (EsCv) o;
    return Objects.equals(fodselsnummer, esCv.fodselsnummer)
        && Objects.equals(formidlingsgruppekode, esCv.formidlingsgruppekode)
        && Objects.equals(arenaPersonId, esCv.arenaPersonId)
        && Objects.equals(arenaKandidatnr, esCv.arenaKandidatnr)
        && Objects.equals(utdanning, esCv.utdanning)
        && Objects.equals(yrkeserfaring, esCv.yrkeserfaring)
        && Objects.equals(totalLengdeYrkeserfaring, esCv.totalLengdeYrkeserfaring);
  }

  @Override
  public int hashCode() {

    return Objects.hash(fodselsnummer, formidlingsgruppekode, arenaPersonId, arenaKandidatnr,
        utdanning, yrkeserfaring, totalLengdeYrkeserfaring);
  }

  @Override
  public String toString() {
    return "EsCv{" + "fodselsnummer='" + fodselsnummer + '\'' + ", formidlingsgruppekode='"
        + formidlingsgruppekode + '\'' + ", arenaPersonId=" + arenaPersonId + ", arenaKandidatnr='"
        + arenaKandidatnr + '\'' + ", utdanning=" + utdanning + ", yrkeserfaring=" + yrkeserfaring
        + ", totalLengdeYrkeserfaring=" + totalLengdeYrkeserfaring + '}';
  }

}
