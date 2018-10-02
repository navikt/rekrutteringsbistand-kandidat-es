package no.nav.arbeid.cv.kandidatsok.es.domene.sok;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sokekriterier {
  private String fritekst;
  private List<String> yrkeJobbonsker;
  private List<String> stillingstitler;
  private List<String> kompetanser;
  private List<String> utdanninger;
  private List<String> totalYrkeserfaring;
  private List<String> utdanningsniva;
  private List<String> geografiList;
  private List<String> styrkKoder;
  private List<String> nusKoder;
  private List<String> sprak;
  private String etternavn;
  private Boolean maaBoInnenforGeografi;

  private Sokekriterier() {};

  public static Builder med() {
    return new Builder();
  }

  public String fritekst() {
    return fritekst;
  }

  public List<String> sprak() {
    return sprak;
  }

  public List<String> yrkeJobbonsker() {
    return yrkeJobbonsker;
  }

  public List<String> stillingstitler() {
    return stillingstitler;
  }

  public List<String> kompetanser() {
    return kompetanser;
  }

  public List<String> utdanninger() {
    return utdanninger;
  }

  public List<String> totalYrkeserfaring() {
    return totalYrkeserfaring;
  }

  public List<String> utdanningsniva() {
    return utdanningsniva;
  }

  public List<String> geografiList() {
    return geografiList;
  }

  public List<String> styrkKoder() {
    return styrkKoder;
  }

  public List<String> nusKoder() {
    return nusKoder;
  }

  public String etternavn() {
    return etternavn;
  }

  public Boolean maaBoInnenforGeografi() {
    return maaBoInnenforGeografi;
  }

  public static class Builder {
    private String fritekst;
    private List<String> yrkeJobbonsker;
    private List<String> stillingstitler;
    private List<String> kompetanser;
    private List<String> utdanninger;
    private List<String> totalYrkeserfaring;
    private List<String> utdanningsniva;
    private List<String> sprak;
    private List<String> geografiList;
    private String styrkKode;
    private String nusKode;
    private List<String> styrkKoder;
    private List<String> nusKoder;
    private String etternavn;
    private boolean maaBoInnenforGeografi;

    public Sokekriterier bygg() {
      Sokekriterier s = new Sokekriterier();
      s.etternavn = etternavn;
      s.fritekst = fritekst;
      s.geografiList = geografiList == null ? Collections.emptyList()
          : Collections.unmodifiableList(new ArrayList<>(geografiList));
      s.kompetanser = kompetanser == null ? Collections.emptyList()
          : Collections.unmodifiableList(new ArrayList<>(kompetanser));
      s.totalYrkeserfaring = totalYrkeserfaring == null ? Collections.emptyList()
          : Collections.unmodifiableList(new ArrayList<>(totalYrkeserfaring));
      s.utdanningsniva = utdanningsniva == null ? Collections.emptyList()
          : Collections.unmodifiableList(new ArrayList<>(utdanningsniva));
      s.stillingstitler = stillingstitler == null ? Collections.emptyList()
          : Collections.unmodifiableList(new ArrayList<>(stillingstitler));
      s.yrkeJobbonsker = yrkeJobbonsker == null ? Collections.emptyList()
          : Collections.unmodifiableList(new ArrayList<>(yrkeJobbonsker));
      s.utdanninger = utdanninger == null ? Collections.emptyList()
          : Collections.unmodifiableList(new ArrayList<>(utdanninger));
      s.sprak = sprak == null ? Collections.emptyList()
          : Collections.unmodifiableList(new ArrayList<>(sprak));

      List<String> tmpNuskoder = nusKoder == null ? new ArrayList<>() : new ArrayList<>(nusKoder);
      if (StringUtils.isNotBlank(nusKode)) {
        tmpNuskoder.add(nusKode);
      }
      s.nusKoder = tmpNuskoder.isEmpty() ? null : Collections.unmodifiableList(tmpNuskoder);

      List<String> tmpStyrkkoder =
          styrkKoder == null ? new ArrayList<>() : new ArrayList<>(styrkKoder);
      if (StringUtils.isNotBlank(styrkKode)) {
        tmpStyrkkoder.add(styrkKode);
      }
      s.styrkKoder = tmpStyrkkoder.isEmpty() ? null : Collections.unmodifiableList(tmpStyrkkoder);

      s.maaBoInnenforGeografi = maaBoInnenforGeografi;

      return s;
    }

    public Builder fritekst(String fritekst) {
      this.fritekst = fritekst;
      return this;
    }

    public Builder yrkeJobbonsker(List<String> yrkeJobbonsker) {
      this.yrkeJobbonsker = yrkeJobbonsker;
      return this;
    }

    public Builder stillingstitler(List<String> stillingstitler) {
      this.stillingstitler = stillingstitler;
      return this;
    }

    public Builder sprak(List<String> sprak) {
      this.sprak = sprak;
      return this;
    }

    public Builder kompetanser(List<String> kompetanser) {
      this.kompetanser = kompetanser;
      return this;
    }

    public Builder utdanninger(List<String> utdanninger) {
      this.utdanninger = utdanninger;
      return this;
    }

    public Builder totalYrkeserfaring(List<String> totalYrkeserfaring) {
      this.totalYrkeserfaring = totalYrkeserfaring;
      return this;
    }

    public Builder utdanningsniva(List<String> utdanningsniva) {
      this.utdanningsniva = utdanningsniva;
      return this;
    }

    public Builder geografiList(List<String> geografiList) {
      this.geografiList = geografiList;
      return this;
    }

    public Builder styrkKode(String styrkKode) {
      this.styrkKode = styrkKode;
      return this;
    }

    public Builder nusKode(String nusKode) {
      this.nusKode = nusKode;
      return this;
    }

    public Builder styrkKoder(List<String> styrkKoder) {
      this.styrkKoder = styrkKoder;
      return this;
    }

    public Builder nusKoder(List<String> nusKoder) {
      this.nusKoder = nusKoder;
      return this;
    }

    public Builder etternavn(String etternavn) {
      this.etternavn = etternavn;
      return this;
    }

    public Builder maaBoInnenforGeografi(boolean maaBoInnenforGeografi) {
      this.maaBoInnenforGeografi = maaBoInnenforGeografi;
      return this;
    }
  }
}
