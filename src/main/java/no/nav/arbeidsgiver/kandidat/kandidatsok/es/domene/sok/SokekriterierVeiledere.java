package no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.sok;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SokekriterierVeiledere {
    private String fritekst;
    private List<String> yrkeJobbonsker;
    private List<String> stillingstitler;
    private List<String> kompetanser;
    private List<String> utdanninger;
    private List<String> totalYrkeserfaring;
    private List<String> utdanningsniva;
    private List<String> geografiList;
    private List<String> sprak;
    private List<String> kvalifiseringsgruppeKoder;
    private List<String> navkontor;
    private List<String> veiledere;
    private List<String> hovedmaalKode;
    private List<String> oppstartKoder;
    private String etternavn;
    private Boolean maaBoInnenforGeografi;
    private List<String> forerkort;
    private Boolean tilretteleggingsbehov;
    private Boolean permittert;
    private List<String> veilTilretteleggingsbehov;
    private List<String> veilTilretteleggingsbehovUtelukkes;
    private List<String> midlertidigUtilgjengelig;
    private int antallResultater;
    private int fraIndex;
    private Integer antallAarFra;
    private Integer antallAarTil;
    private Integer antallDagerSistEndret;
    private Integer antallAarGammelYrkeserfaring;
    private boolean hullICv;

    private boolean tomtSok = true;

    private SokekriterierVeiledere() {
    }

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

    public List<String> kvalifiseringsgruppeKoder() {
        return kvalifiseringsgruppeKoder;
    }

    public List<String> navkontor() {
        return navkontor;
    }

    public List<String> veiledere() {
        return veiledere;
    }

    public List<String> hovedmaalKode() {
        return hovedmaalKode;
    }

    public List<String> oppstartKoder() {
        return oppstartKoder;
    }

    public String etternavn() {
        return etternavn;
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

    public Integer antallAarFra() {
        return antallAarFra;
    }

    public Integer antallAarTil() {
        return antallAarTil;
    }

    public boolean isAntallAarFraSet() {
        return antallAarFra != null;
    }

    public boolean isAntallAarTilSet() {
        return antallAarTil != null;
    }

    public boolean isTilretteleggingsbehovSet() {
        return tilretteleggingsbehov != null;
    }

    public Boolean getTilretteleggingsbehov() {
        return tilretteleggingsbehov;
    }

    public boolean isPermittertSet() {
        return permittert != null;
    }

    public Boolean getPermittert() {
        return permittert;
    }

    public List<String> veilTilretteleggingsbehov() {
        return veilTilretteleggingsbehov;
    }

    public List<String> veilTilretteleggingsbehovUtelukkes() {
        return veilTilretteleggingsbehovUtelukkes;
    }

    public List<String> midlertidigUtilgjengelig() {
        return midlertidigUtilgjengelig;
    }

    public Integer antallDagerSistEndret() {
        return antallDagerSistEndret;
    }

    public Integer antallAarGammelYrkeserfaring() {
        return antallAarGammelYrkeserfaring;
    }

    public boolean isHullICv() {
        return hullICv;
    }

    public void setHullICv(boolean hullICv) {
        this.hullICv = hullICv;
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
        private List<String> kvalifiseringsgruppeKoder;
        private List<String> navkontor;
        private List<String> veiledere;
        private List<String> hovedmaalKode;
        private List<String> oppstartKoder;
        private String etternavn;
        private int fra = 0;
        private int antallResultater = 100;
        private boolean maaBoInnenforGeografi;
        private List<String> forerkort;
        private Integer antallAarFra;
        private Integer antallAarTil;
        private Boolean tilretteleggingsbehov;
        private Boolean permittert;
        private List<String> veilTilretteleggingsbehov;
        private List<String> veilTilretteleggingsbehovUtelukkes;
        private List<String> midlertidigUtilgjengelig;
        private Integer antallDagerSistEndret;
        private Integer antallAarGammelYrkeserfaring;
        private boolean hullICv = false;
        private boolean tomtSok = true;

        public SokekriterierVeiledere bygg() {
            SokekriterierVeiledere s = new SokekriterierVeiledere();
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
            s.forerkort = forerkort == null ? Collections.emptyList()
                    : Collections.unmodifiableList(new ArrayList<>(forerkort));
            s.kvalifiseringsgruppeKoder = kvalifiseringsgruppeKoder == null ? Collections.emptyList()
                    : Collections.unmodifiableList(new ArrayList<>(kvalifiseringsgruppeKoder));
            s.navkontor = navkontor == null ? Collections.emptyList()
                    : Collections.unmodifiableList(new ArrayList<>(navkontor));
            s.veiledere = veiledere == null ? Collections.emptyList()
                    : Collections.unmodifiableList(new ArrayList<>(veiledere));
            s.hovedmaalKode = hovedmaalKode == null ? Collections.emptyList()
                    : Collections.unmodifiableList(new ArrayList<>(hovedmaalKode));
            s.oppstartKoder = oppstartKoder == null ? Collections.emptyList()
                    : Collections.unmodifiableList(new ArrayList<>(oppstartKoder));

            s.maaBoInnenforGeografi = maaBoInnenforGeografi;

            s.antallAarFra = antallAarFra;
            s.antallAarTil = antallAarTil;
            s.antallDagerSistEndret = antallDagerSistEndret;

            s.tilretteleggingsbehov = tilretteleggingsbehov;
            s.permittert = permittert;
            s.veilTilretteleggingsbehov = veilTilretteleggingsbehov == null ? Collections.emptyList()
                    : Collections.unmodifiableList(new ArrayList<>(veilTilretteleggingsbehov));
            s.veilTilretteleggingsbehovUtelukkes = veilTilretteleggingsbehovUtelukkes == null ? Collections.emptyList()
                    : Collections.unmodifiableList(new ArrayList<>(veilTilretteleggingsbehovUtelukkes));
            s.midlertidigUtilgjengelig = midlertidigUtilgjengelig == null ? Collections.emptyList()
                    : Collections.unmodifiableList(new ArrayList<>(midlertidigUtilgjengelig));
            s.antallAarGammelYrkeserfaring = antallAarGammelYrkeserfaring;

            s.fraIndex = fra;
            s.antallResultater = antallResultater;
            s.hullICv = hullICv;
            s.tomtSok = tomtSok;

            return s;
        }

        public Builder fritekst(String fritekst) {
            this.fritekst = fritekst;
            this.tomtSok = false;
            return this;
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

        public Builder kvalifiseringsgruppeKoder(List<String> kvalifiseringsgruppeKoder) {
            this.kvalifiseringsgruppeKoder = kvalifiseringsgruppeKoder;
            this.tomtSok = false;
            return this;
        }

        public Builder navkontor(List<String> navkontor) {
            this.navkontor = navkontor;
            this.tomtSok = false;
            return this;
        }

        public Builder veiledere(List<String> veiledere) {
            this.veiledere = veiledere;
            this.tomtSok = false;
            return this;
        }

        public Builder hovedmaalKode(List<String> hovedmaalKode) {
            this.hovedmaalKode = hovedmaalKode;
            this.tomtSok = false;
            return this;
        }

        public Builder oppstartKoder(List<String> oppstartKoder) {
            this.oppstartKoder = oppstartKoder;
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

        public Builder etternavn(String etternavn) {
            this.etternavn = etternavn;
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

        public Builder antallAarFra(int antallAarFra) {
            this.antallAarFra = antallAarFra;
            this.tomtSok = false;
            return this;
        }

        public Builder antallAarTil(int antallAarTil) {
            this.antallAarTil = antallAarTil;
            this.tomtSok = false;
            return this;
        }

        public Builder tilretteleggingsbehov(Boolean tilretteleggingsbehov) {
            this.tilretteleggingsbehov = tilretteleggingsbehov;
            if(null != tilretteleggingsbehov) {
                this.tomtSok = false;
            }
            return this;
        }

        public Builder permittert(Boolean permittert) {
            this.permittert = permittert;
            if(null != permittert) {
                this.tomtSok = false;
            }
            return this;
        }

        public Builder veilTilretteleggingsbehov(List<String> veilTilretteleggingsbehov) {
            this.veilTilretteleggingsbehov = veilTilretteleggingsbehov;
            this.tomtSok = false;
            return this;
        }

        public Builder veilTilretteleggingsbehovUtelukkes(List<String> veilTilretteleggingsbehovUtelukkes) {
            this.veilTilretteleggingsbehovUtelukkes = veilTilretteleggingsbehovUtelukkes;
            this.tomtSok = false;
            return this;
        }

        public Builder midlertidigUtilgjengelig(List<String> midlertidigUtilgjengelig) {
            this.midlertidigUtilgjengelig = midlertidigUtilgjengelig;
            this.tomtSok = false;
            return this;
        }

        public Builder antallDagerSistEndret(Integer antallDagerSistEndret) {
            this.antallDagerSistEndret = antallDagerSistEndret;
            this.tomtSok = false;
            return this;
        }

        public Builder antallAarGammelYrkeserfaring(Integer antallAarGammelYrkeserfaring) {
            this.antallAarGammelYrkeserfaring = antallAarGammelYrkeserfaring;
            return this;
        }

        public Builder hullICv(boolean hullICv) {
            this.hullICv = hullICv;
            return this;
        }
    }

}
