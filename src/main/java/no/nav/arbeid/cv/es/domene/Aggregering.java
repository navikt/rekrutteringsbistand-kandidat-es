package no.nav.arbeid.cv.es.domene;

import java.util.HashMap;
import java.util.Map;

public class Aggregering {

  private String navn;

  private Map<String, Long> felt = new HashMap<>();

  public Aggregering(String navn) {
    this.navn = navn;
  }

  public Aggregering(String navn, Map<String, Long> felt) {
    this.navn = navn;
    this.felt = felt;
  }

  public String getNavn() {
    return navn;
  }

  public Map<String, Long> getFelt() {
    return felt;
  }

  public void addFelt(String feltnavn, Long antall) {
    this.felt.put(feltnavn, antall);
  }
}
