package no.nav.arbeid.cv.kandidatsok.es.domene.sok;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Sokekriterier {
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
    private Boolean maaBoInnenforGeografi;
    private List<String> forerkort;
    private int antallResultater;
    private int fraIndex;

    private boolean tomtSok = true;

    private Sokekriterier() {};

    public static Builder med() {
        return new Builder();
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

    public Boolean maaBoInnenforGeografi() {
        return maaBoInnenforGeografi;
    }

    public List<String> forerkort() {
        return forerkort;
    }

    public int fraIndex() {
        return this.fraIndex;
    }

    public int antallResultater() {
        return this.antallResultater;
    }

    public boolean isTomtSok() {
        return tomtSok;
    }

    public boolean isUtdanningsNivaPresent() {
        return utdanningsniva() != null && !utdanningsniva().isEmpty();
    }

    public boolean isUtdanningerPresent() {
        return utdanninger() != null && !utdanninger().isEmpty();
    }

    public boolean isUtdanningSet() {
        return (isUtdanningsNivaPresent()) || (isUtdanningerPresent());
    }

    public static class Builder {
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
        private int fra = 0;
        private int antallResultater = 100;
        private boolean maaBoInnenforGeografi;
        private List<String> forerkort;
        private boolean tomtSok = true;

        public Sokekriterier bygg() {
            Sokekriterier s = new Sokekriterier();
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
            s.forerkort = forerkort == null ? Collections.emptyList()
                    : Collections.unmodifiableList(new ArrayList<>(forerkort));

            List<String> tmpNuskoder =
                    nusKoder == null ? new ArrayList<>() : new ArrayList<>(nusKoder);
            if (StringUtils.isNotBlank(nusKode)) {
                tmpNuskoder.add(nusKode);
            }
            s.nusKoder = tmpNuskoder.isEmpty() ? null : Collections.unmodifiableList(tmpNuskoder);

            List<String> tmpStyrkkoder =
                    styrkKoder == null ? new ArrayList<>() : new ArrayList<>(styrkKoder);
            if (StringUtils.isNotBlank(styrkKode)) {
                tmpStyrkkoder.add(styrkKode);
            }
            s.styrkKoder =
                    tmpStyrkkoder.isEmpty() ? null : Collections.unmodifiableList(tmpStyrkkoder);

            s.maaBoInnenforGeografi = maaBoInnenforGeografi;

            s.fraIndex = fra;
            s.antallResultater = antallResultater;
            s.tomtSok = tomtSok;

            return s;
        }

        public Builder yrkeJobbonsker(List<String> yrkeJobbonsker) {
            this.yrkeJobbonsker = yrkeJobbonsker;
            this.tomtSok = false;
            return this;
        }

        public Builder stillingstitler(List<String> stillingstitler) {
            this.stillingstitler = stillingstitler;
            this.tomtSok = false;
            return this;
        }

        public Builder sprak(List<String> sprak) {
            this.sprak = sprak;
            this.tomtSok = false;
            return this;
        }

        public Builder kompetanser(List<String> kompetanser) {
            this.kompetanser = kompetanser;
            this.tomtSok = false;
            return this;
        }

        public Builder utdanninger(List<String> utdanninger) {
            this.utdanninger = utdanninger;
            this.tomtSok = false;
            return this;
        }

        public Builder totalYrkeserfaring(List<String> totalYrkeserfaring) {
            this.totalYrkeserfaring = totalYrkeserfaring;
            this.tomtSok = false;
            return this;
        }

        public Builder utdanningsniva(List<String> utdanningsniva) {
            this.utdanningsniva = utdanningsniva;
            this.tomtSok = false;
            return this;
        }

        public Builder geografiList(List<String> geografiList) {
            this.geografiList = geografiList;
            this.tomtSok = false;
            return this;
        }

        public Builder styrkKode(String styrkKode) {
            this.styrkKode = styrkKode;
            this.tomtSok = false;
            return this;
        }

        public Builder nusKode(String nusKode) {
            this.nusKode = nusKode;
            this.tomtSok = false;
            return this;
        }

        public Builder styrkKoder(List<String> styrkKoder) {
            this.styrkKoder = styrkKoder;
            this.tomtSok = false;
            return this;
        }

        public Builder nusKoder(List<String> nusKoder) {
            this.nusKoder = nusKoder;
            this.tomtSok = false;
            return this;
        }

        public Builder fraIndex(int fra) {
            this.fra = fra;
            return this;
        }

        public Builder antallResultater(int antall) {
            this.antallResultater = antall;
            return this;
        }

        public Builder maaBoInnenforGeografi(boolean maaBoInnenforGeografi) {
            this.maaBoInnenforGeografi = maaBoInnenforGeografi;
            return this;
        }

        public Builder forerkort(List<String> forerkort) {
            this.forerkort = forerkort;
            this.tomtSok = false;
            return this;
        }
    }
}
