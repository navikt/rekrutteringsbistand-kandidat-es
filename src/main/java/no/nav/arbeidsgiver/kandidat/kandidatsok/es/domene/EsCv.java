package no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsCv {

    private String fritekst;

    private String aktorId;

    private String fodselsnummer;

    private String fornavn;

    private String etternavn;

    private Date fodselsdato;

    private Boolean fodselsdatoErDnr;

    private String formidlingsgruppekode;

    private String epostadresse;

    private String mobiltelefon;

    private boolean harKontaktinformasjon;

    private String telefon;

    private String statsborgerskap;

    private String kandidatnr;

    private String arenaKandidatnr;

    private String beskrivelse;

    private String samtykkeStatus;

    private Date samtykkeDato;

    private String adresselinje1;

    private String adresselinje2;

    private String adresselinje3;

    private String postnummer;

    private String poststed;

    private String landkode;

    private Integer kommunenummer;

    private Integer kommunenummerkw;

    private String kommunenummerstring;

    private String fylkeNavn;

    private String kommuneNavn;

    private Boolean disponererBil;

    private Date tidsstempel; // TODO Dårlig/vagt navn, bør hete sistEndret, slik at det stemmer med CV AVRO-modellen

    private Boolean doed;

    private String frKode;

    private String kvalifiseringsgruppekode;

    private String hovedmaalkode;

    private String orgenhet;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String navkontor;

    private Boolean fritattKandidatsok;

    private Boolean fritattAgKandidatsok;

    private List<EsUtdanning> utdanning = new ArrayList<>();

    private List<EsFagdokumentasjon> fagdokumentasjon = new ArrayList<>();

    private List<EsYrkeserfaring> yrkeserfaring = new ArrayList<>();

    private List<EsKompetanse> kompetanseObj = new ArrayList<>();

    private List<EsAnnenErfaring> annenerfaringObj = new ArrayList<>();

    private List<EsSertifikat> sertifikatObj = new ArrayList<>();

    private List<EsForerkort> forerkort = new ArrayList<>();

    private List<EsSprak> sprak = new ArrayList<>();

    private List<EsKurs> kursObj = new ArrayList<>();

    private List<EsVerv> vervObj = new ArrayList<>();

    private List<EsGeografiJobbonsker> geografiJobbonsker = new ArrayList<>();

    private List<EsYrkeJobbonsker> yrkeJobbonskerObj = new ArrayList<>();

    private List<EsOmfangJobbonsker> omfangJobbonskerObj = new ArrayList<>();

    private List<EsAnsettelsesformJobbonsker> ansettelsesformJobbonskerObj = new ArrayList<>();

    private List<EsArbeidstidsordningJobbonsker> arbeidstidsordningJobbonskerObj = new ArrayList<>();

    private List<EsArbeidsdagerJobbonsker> arbeidsdagerJobbonskerObj = new ArrayList<>();

    private List<EsArbeidstidJobbonsker> arbeidstidJobbonskerObj = new ArrayList<>();

    private List<EsSamletKompetanse> samletKompetanseObj = new ArrayList<>();

    private int totalLengdeYrkeserfaring;

    private Boolean synligForArbeidsgiverSok;

    private Boolean synligForVeilederSok;

    private String oppstartKode;

    private String veileder;

    private boolean inkluderingsbehov;

    private List<EsGodkjenning> godkjenninger = new ArrayList<>();

    private EsPerioderMedInaktivitet perioderMedInaktivitet;

    public EsCv() {
    }

    public EsCv(
            String aktorId,
            String fodselsnummer,
            String fornavn,
            String etternavn,
            Date fodselsdato,

            Boolean fodselsdatoErDnr,
            String formidlingsgruppekode,
            String epostadresse,
            String mobiltelefon,
            String telefon,

            String statsborgerskap,
            String kandidatnr,
            String beskrivelse,
            String samtykkeStatus,
            Date samtykkeDato,

            String adresselinje1,
            String adresselinje2,
            String adresselinje3,
            String postnummer,
            String poststed,

            String landkode,
            Integer kommunenummer,
            Boolean disponererBil,
            Date tidsstempel,
            Integer kommunenummerkw,

            Boolean doed,
            String frKode,
            String kvalifiseringsgruppekode,
            String hovedmaalkode,
            String navkontor,
            String orgenhet,

            Boolean fritattKandidatsok,
            Boolean fritattAgKandidatsok,
            Boolean synligForArbeidsgiverSok,
            Boolean synligForVeilederSok,
            String oppstartKode,

            String kommunenummerstring,
            String veileder,
            String fylkeNavn,
            String kommuneNavn
    ) {

        this.aktorId = aktorId;
        this.fodselsnummer = fodselsnummer;
        this.fornavn = fornavn;
        this.etternavn = etternavn;
        this.fodselsdato = fodselsdato;
        this.fodselsdatoErDnr = fodselsdatoErDnr;
        this.formidlingsgruppekode = formidlingsgruppekode;
        this.epostadresse = epostadresse;
        this.mobiltelefon = mobiltelefon;
        this.telefon = telefon;
        this.harKontaktinformasjon = !StringUtils.isAllBlank(this.epostadresse, this.mobiltelefon, this.telefon);
        this.statsborgerskap = statsborgerskap;
        this.kandidatnr = kandidatnr;
        this.arenaKandidatnr = kandidatnr;
        this.beskrivelse = beskrivelse;
        this.samtykkeStatus = samtykkeStatus;
        this.samtykkeDato = samtykkeDato;
        this.adresselinje1 = adresselinje1;
        this.adresselinje2 = adresselinje2;
        this.adresselinje3 = adresselinje3;
        this.postnummer = postnummer;
        this.poststed = poststed;
        this.landkode = landkode;
        this.kommunenummer = kommunenummer;
        this.disponererBil = disponererBil;
        this.tidsstempel = tidsstempel;
        this.kommunenummerkw = kommunenummerkw;
        this.doed = doed;
        this.frKode = frKode;
        this.kvalifiseringsgruppekode = kvalifiseringsgruppekode;
        this.hovedmaalkode = hovedmaalkode;
        this.orgenhet = orgenhet;
        this.navkontor = navkontor;
        this.fritattKandidatsok = fritattKandidatsok;
        this.fritattAgKandidatsok = fritattAgKandidatsok;
        this.synligForArbeidsgiverSok = synligForArbeidsgiverSok;
        this.synligForVeilederSok = synligForVeilederSok;
        this.oppstartKode = oppstartKode;
        this.kommunenummerstring = kommunenummerstring;
        this.veileder = veileder == null ? null : veileder.toLowerCase();
        this.fylkeNavn = fylkeNavn;
        this.kommuneNavn = kommuneNavn;
    }

    public void addPerioderMedInaktivitet(EsPerioderMedInaktivitet p) {
        perioderMedInaktivitet = p;
    }

    public void addUtdanning(EsUtdanning utdanning) {
        this.utdanning.add(utdanning);
    }

    public void addUtdanning(Collection<EsUtdanning> utdanningListe) {
        this.utdanning.addAll(utdanningListe);
    }

    public void addFagdokumentasjon(Collection<EsFagdokumentasjon> fagdokumentasjonListe) {
        this.fagdokumentasjon.addAll(fagdokumentasjonListe);
        List<EsSamletKompetanse> liste = new ArrayList<>();
        fagdokumentasjonListe.forEach(f -> {
            if (f.getTittel() != null) {
                liste.add(new EsSamletKompetanse(f.getTittel()));
            } else {
                liste.add(new EsSamletKompetanse(EsFagdokumentasjon.getFagdokumentTypeLabel(f.getType())));
            }
        });
        this.addSamletKompetanse(liste);
    }

    public void addYrkeserfaring(EsYrkeserfaring yrkeserfaring) {
        this.yrkeserfaring.add(yrkeserfaring);
    }

    public void addYrkeserfaring(Collection<EsYrkeserfaring> yrkeserfaringListe) {
        yrkeserfaringListe
                .forEach(y -> this.totalLengdeYrkeserfaring += y.getYrkeserfaringManeder());
        this.yrkeserfaring.addAll(yrkeserfaringListe);
    }

    public void addKompetanse(EsKompetanse kompetanse) {
        if (kompetanse != null) {
            this.kompetanseObj.add(kompetanse);
            this.addSamletKompetanse(kompetanse.getSokeNavn().stream().map(str -> new EsSamletKompetanse(str)).collect(Collectors.toList()));
        }
    }

    public void addKompetanse(Collection<EsKompetanse> kompetanseListe) {
        if (kompetanseListe != null) {
            this.kompetanseObj.addAll(kompetanseListe);
            this.addSamletKompetanse(
                    kompetanseListe.stream().flatMap(k -> k.getSokeNavn().stream()).map(str -> new EsSamletKompetanse(str))
                            .collect(Collectors.toList()));
        }
    }

    public void addAnnenErfaring(EsAnnenErfaring annenErfaring) {
        this.annenerfaringObj.add(annenErfaring);
    }

    public void addAnnenErfaring(Collection<EsAnnenErfaring> annenErfaringListe) {
        this.annenerfaringObj.addAll(annenErfaringListe);
    }

    public void addSertifikat(EsSertifikat sertifikat) {
        if (sertifikat != null) {
            this.sertifikatObj.add(sertifikat);
            this.addSamletKompetanse(Collections
                    .singletonList(new EsSamletKompetanse(sertifikat.getSertifikatKodeNavn())));
        }
    }

    public void addSertifikat(Collection<EsSertifikat> sertifikatListe) {
        if (sertifikatListe != null) {
            this.sertifikatObj.addAll(sertifikatListe);
            this.addSamletKompetanse(sertifikatListe.stream()
                    .map(k -> new EsSamletKompetanse(k.getSertifikatKodeNavn()))
                    .collect(Collectors.toList()));
        }
    }

    public void addForerkort(EsForerkort forerkort) {
        if (forerkort != null) {
            this.forerkort.add(forerkort);
        }
    }

    public void addForerkort(Collection<EsForerkort> forerkortListe) {
        if (forerkortListe != null) {
            forerkortListe.forEach(this::addForerkort);
        }
    }

    public void addSprak(EsSprak sprak) {
        if (sprak != null) {
            this.sprak.add(sprak);
        }
    }

    public void addSprak(Collection<EsSprak> sprakListe) {
        if (sprakListe != null) {
            this.sprak.addAll(sprakListe);
        }
    }

    public void addKurs(EsKurs kurs) {
        if (kurs != null) {
            this.kursObj.add(kurs);
        }
    }

    public void addKurs(Collection<EsKurs> kursListe) {
        if (kursListe != null) {
            this.kursObj.addAll(kursListe);
        }
    }

    public void addVerv(EsVerv verv) {
        this.vervObj.add(verv);
    }

    public void addVerv(Collection<EsVerv> vervListe) {
        this.vervObj.addAll(vervListe);
    }

    public void addGeografiJobbonske(Collection<EsGeografiJobbonsker> geografiJobbonskerListe) {
        this.geografiJobbonsker.addAll(geografiJobbonskerListe);
    }

    public void addYrkeJobbonske(Collection<EsYrkeJobbonsker> yrkeJobbonskerListe) {
        this.yrkeJobbonskerObj.addAll(yrkeJobbonskerListe);
    }

    public void addOmfangJobbonske(Collection<EsOmfangJobbonsker> omfangJobbonsker) {
        this.omfangJobbonskerObj.addAll(omfangJobbonsker);
    }

    public void addAnsettelsesformJobbonske(Collection<EsAnsettelsesformJobbonsker> ansettelsesformJobbonsker) {
        this.ansettelsesformJobbonskerObj.addAll(ansettelsesformJobbonsker);
    }

    public void addArbeidstidsordningJobbonsker(Collection<EsArbeidstidsordningJobbonsker> arbeidstidsordningJobbonsker) {
        this.arbeidstidsordningJobbonskerObj.addAll(arbeidstidsordningJobbonsker);
    }

    public void addArbeidstidJobbonsker(Collection<EsArbeidstidJobbonsker> arbeidstidJobbonsker) {
        this.arbeidstidJobbonskerObj.addAll(arbeidstidJobbonsker);
    }

    public void addArbeidsdagerJobbonsker(Collection<EsArbeidsdagerJobbonsker> arbeidsdagerJobbonsker) {
        this.arbeidsdagerJobbonskerObj.addAll(arbeidsdagerJobbonsker);
    }

    private void addSamletKompetanse(Collection<EsSamletKompetanse> samletKompetanseListe) {
        this.samletKompetanseObj.addAll(samletKompetanseListe);
    }

    // gettere

    public String getAktorId() {
        return aktorId;
    }

    public String getFodselsnummer() {
        return fodselsnummer;
    }

    public String getFornavn() {
        return fornavn;
    }

    public String getEtternavn() {
        return etternavn;
    }

    public Date getFodselsdato() {
        return fodselsdato;
    }

    public Boolean getFodselsdatoErDnr() {
        return fodselsdatoErDnr;
    }

    public String getFormidlingsgruppekode() {
        return formidlingsgruppekode;
    }

    public Boolean isDoed() {
        return doed;
    }

    public Boolean isFritattKandidatsok() {
        return fritattKandidatsok;
    }

    public Boolean isFritattAgKandidatsok() {
        return fritattAgKandidatsok;
    }

    public String getFrKode() {
        return frKode;
    }

    public String getKvalifiseringsgruppekode() {
        return kvalifiseringsgruppekode;
    }

    public String getHovedmaalkode() {
        return hovedmaalkode;
    }

    public String getOrgenhet() {
        return orgenhet;
    }

    public String getVeileder() {
        return veileder;
    }

    public String getNavkontor() {
        return navkontor;
    }

    public String getEpostadresse() {
        return epostadresse;
    }

    public String getMobiltelefon() {
        return mobiltelefon;
    }

    public String getTelefon() {
        return telefon;
    }

    public boolean isHarKontaktinformasjon() {
        return harKontaktinformasjon;
    }

    public String getStatsborgerskap() {
        return statsborgerskap;
    }

    public String getKandidatnr() {
        return kandidatnr;
    }

    public void setKandidatnr(String nyArenaKandidatnr) {
        this.kandidatnr = nyArenaKandidatnr;
    }

    public String getArenaKandidatnr() {
        return arenaKandidatnr;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public String getSamtykkeStatus() {
        return samtykkeStatus;
    }

    public Date getSamtykkeDato() {
        return samtykkeDato;
    }

    public String getAdresselinje1() {
        return adresselinje1;
    }

    public String getAdresselinje2() {
        return adresselinje2;
    }

    public String getAdresselinje3() {
        return adresselinje3;
    }

    public String getPostnummer() {
        return postnummer;
    }

    public String getPoststed() {
        return poststed;
    }

    public String getLandkode() {
        return landkode;
    }

    public Integer getKommunenummer() {
        return kommunenummer;
    }

    public Integer getKommunenummerkw() {
        return kommunenummerkw;
    }

    public String getKommunenummerstring() {
        return kommunenummerstring;
    }

    public String getFylkeNavn() {
        return fylkeNavn;
    }

    public String getKommuneNavn() {
        return kommuneNavn;
    }

    public Boolean getDisponererBil() {
        return disponererBil;
    }

    public Date getTidsstempel() {
        return tidsstempel;
    }

    public Boolean isSynligForArbeidsgiverSok() {
        return synligForArbeidsgiverSok;
    }

    public Boolean isSynligForVeilederSok() {
        return synligForVeilederSok;
    }

    public List<EsUtdanning> getUtdanning() {
        return utdanning;
    }

    public List<EsFagdokumentasjon> getFagdokumentasjon() {
        return fagdokumentasjon;
    }

    public List<EsYrkeserfaring> getYrkeserfaring() {
        return yrkeserfaring;
    }

    public List<EsKompetanse> getKompetanseObj() {
        return kompetanseObj;
    }

    public List<EsAnnenErfaring> getAnnenerfaringObj() {
        return annenerfaringObj;
    }

    public List<EsSertifikat> getSertifikatObj() {
        return sertifikatObj;
    }

    public List<EsForerkort> getForerkort() {
        return forerkort;
    }

    public List<EsSprak> getSprak() {
        return sprak;
    }

    public List<EsKurs> getKursObj() {
        return kursObj;
    }

    public List<EsVerv> getVervObj() {
        return vervObj;
    }

    public List<EsGeografiJobbonsker> getGeografiJobbonsker() {
        return geografiJobbonsker;
    }

    public List<EsYrkeJobbonsker> getYrkeJobbonskerObj() {
        return yrkeJobbonskerObj;
    }

    public List<EsOmfangJobbonsker> getOmfangJobbonskerObj() {
        return omfangJobbonskerObj;
    }

    public List<EsAnsettelsesformJobbonsker> getAnsettelsesformJobbonskerObj() {
        return ansettelsesformJobbonskerObj;
    }

    public List<EsArbeidstidsordningJobbonsker> getArbeidstidsordningJobbonskerObj() {
        return arbeidstidsordningJobbonskerObj;
    }

    public List<EsArbeidstidJobbonsker> getArbeidstidJobbonskerObj() {
        return arbeidstidJobbonskerObj;
    }

    public List<EsArbeidsdagerJobbonsker> getArbeidsdagerJobbonskerObj() {
        return arbeidsdagerJobbonskerObj;
    }

    public List<EsSamletKompetanse> getSamletKompetanseObj() {
        return samletKompetanseObj;
    }

    public void setSamletKompetanseObj(List<EsSamletKompetanse> samletKompetanseObj) {
        this.samletKompetanseObj = Objects.requireNonNull(samletKompetanseObj);
    }

    public int getTotalLengdeYrkeserfaring() {
        return totalLengdeYrkeserfaring;
    }

    public String getOppstartKode() {
        return this.oppstartKode;
    }

    public boolean getInkluderingsbehov() {
        return inkluderingsbehov;
    }

    public List<EsGodkjenning> getGodkjenninger() {
        return godkjenninger;
    }

    public void addGodkjenninger(Collection<EsGodkjenning> godkjenninger) {
        if (godkjenninger == null || godkjenninger.isEmpty()) return;
        this.godkjenninger.addAll(godkjenninger);
        this.addSamletKompetanse(godkjenninger.stream().map(g -> new EsSamletKompetanse(g.getTittel())).collect(Collectors.toList()));
    }

    public EsPerioderMedInaktivitet getPerioderMedInaktivitet() {
        return perioderMedInaktivitet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EsCv esCv = (EsCv) o;
        return harKontaktinformasjon == esCv.harKontaktinformasjon && totalLengdeYrkeserfaring == esCv.totalLengdeYrkeserfaring && inkluderingsbehov == esCv.inkluderingsbehov && Objects.equals(fritekst, esCv.fritekst) && Objects.equals(aktorId, esCv.aktorId) && Objects.equals(fodselsnummer, esCv.fodselsnummer) && Objects.equals(fornavn, esCv.fornavn) && Objects.equals(etternavn, esCv.etternavn) && Objects.equals(fodselsdato, esCv.fodselsdato) && Objects.equals(fodselsdatoErDnr, esCv.fodselsdatoErDnr) && Objects.equals(formidlingsgruppekode, esCv.formidlingsgruppekode) && Objects.equals(epostadresse, esCv.epostadresse) && Objects.equals(mobiltelefon, esCv.mobiltelefon) && Objects.equals(telefon, esCv.telefon) && Objects.equals(statsborgerskap, esCv.statsborgerskap) && Objects.equals(kandidatnr, esCv.kandidatnr) && Objects.equals(arenaKandidatnr, esCv.arenaKandidatnr) && Objects.equals(beskrivelse, esCv.beskrivelse) && Objects.equals(samtykkeStatus, esCv.samtykkeStatus) && Objects.equals(samtykkeDato, esCv.samtykkeDato) && Objects.equals(adresselinje1, esCv.adresselinje1) && Objects.equals(adresselinje2, esCv.adresselinje2) && Objects.equals(adresselinje3, esCv.adresselinje3) && Objects.equals(postnummer, esCv.postnummer) && Objects.equals(poststed, esCv.poststed) && Objects.equals(landkode, esCv.landkode) && Objects.equals(kommunenummer, esCv.kommunenummer) && Objects.equals(kommunenummerkw, esCv.kommunenummerkw) && Objects.equals(kommunenummerstring, esCv.kommunenummerstring) && Objects.equals(fylkeNavn, esCv.fylkeNavn) && Objects.equals(kommuneNavn, esCv.kommuneNavn) && Objects.equals(disponererBil, esCv.disponererBil) && Objects.equals(tidsstempel, esCv.tidsstempel) && Objects.equals(doed, esCv.doed) && Objects.equals(frKode, esCv.frKode) && Objects.equals(kvalifiseringsgruppekode, esCv.kvalifiseringsgruppekode) && Objects.equals(hovedmaalkode, esCv.hovedmaalkode) && Objects.equals(orgenhet, esCv.orgenhet) && Objects.equals(navkontor, esCv.navkontor) && Objects.equals(fritattKandidatsok, esCv.fritattKandidatsok) && Objects.equals(fritattAgKandidatsok, esCv.fritattAgKandidatsok) && Objects.equals(utdanning, esCv.utdanning) && Objects.equals(fagdokumentasjon, esCv.fagdokumentasjon) && Objects.equals(yrkeserfaring, esCv.yrkeserfaring) && Objects.equals(kompetanseObj, esCv.kompetanseObj) && Objects.equals(annenerfaringObj, esCv.annenerfaringObj) && Objects.equals(sertifikatObj, esCv.sertifikatObj) && Objects.equals(forerkort, esCv.forerkort) && Objects.equals(sprak, esCv.sprak) && Objects.equals(kursObj, esCv.kursObj) && Objects.equals(vervObj, esCv.vervObj) && Objects.equals(geografiJobbonsker, esCv.geografiJobbonsker) && Objects.equals(yrkeJobbonskerObj, esCv.yrkeJobbonskerObj) && Objects.equals(omfangJobbonskerObj, esCv.omfangJobbonskerObj) && Objects.equals(ansettelsesformJobbonskerObj, esCv.ansettelsesformJobbonskerObj) && Objects.equals(arbeidstidsordningJobbonskerObj, esCv.arbeidstidsordningJobbonskerObj) && Objects.equals(arbeidsdagerJobbonskerObj, esCv.arbeidsdagerJobbonskerObj) && Objects.equals(arbeidstidJobbonskerObj, esCv.arbeidstidJobbonskerObj) && Objects.equals(samletKompetanseObj, esCv.samletKompetanseObj) && Objects.equals(synligForArbeidsgiverSok, esCv.synligForArbeidsgiverSok) && Objects.equals(synligForVeilederSok, esCv.synligForVeilederSok) && Objects.equals(oppstartKode, esCv.oppstartKode) && Objects.equals(veileder, esCv.veileder) && Objects.equals(godkjenninger, esCv.godkjenninger) && Objects.equals(perioderMedInaktivitet, esCv.perioderMedInaktivitet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fritekst, aktorId, fodselsnummer, fornavn, etternavn, fodselsdato, fodselsdatoErDnr, formidlingsgruppekode, epostadresse, mobiltelefon, harKontaktinformasjon, telefon, statsborgerskap, kandidatnr, arenaKandidatnr, beskrivelse, samtykkeStatus, samtykkeDato, adresselinje1, adresselinje2, adresselinje3, postnummer, poststed, landkode, kommunenummer, kommunenummerkw, kommunenummerstring, fylkeNavn, kommuneNavn, disponererBil, tidsstempel, doed, frKode, kvalifiseringsgruppekode, hovedmaalkode, orgenhet, navkontor, fritattKandidatsok, fritattAgKandidatsok, utdanning, fagdokumentasjon, yrkeserfaring, kompetanseObj, annenerfaringObj, sertifikatObj, forerkort, sprak, kursObj, vervObj, geografiJobbonsker, yrkeJobbonskerObj, omfangJobbonskerObj, ansettelsesformJobbonskerObj, arbeidstidsordningJobbonskerObj, arbeidsdagerJobbonskerObj, arbeidstidJobbonskerObj, samletKompetanseObj, totalLengdeYrkeserfaring, synligForArbeidsgiverSok, synligForVeilederSok, oppstartKode, veileder, inkluderingsbehov, godkjenninger, perioderMedInaktivitet);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", EsCv.class.getSimpleName() + "[", "]")
                .add("fritekst='" + fritekst + "'")
                .add("aktorId='" + aktorId + "'")
                .add("fodselsnummer='" + fodselsnummer + "'")
                .add("fornavn='" + fornavn + "'")
                .add("etternavn='" + etternavn + "'")
                .add("fodselsdato=" + fodselsdato)
                .add("fodselsdatoErDnr=" + fodselsdatoErDnr)
                .add("formidlingsgruppekode='" + formidlingsgruppekode + "'")
                .add("epostadresse='" + epostadresse + "'")
                .add("mobiltelefon='" + mobiltelefon + "'")
                .add("harKontaktinformasjon=" + harKontaktinformasjon)
                .add("telefon='" + telefon + "'")
                .add("statsborgerskap='" + statsborgerskap + "'")
                .add("kandidatnr='" + kandidatnr + "'")
                .add("arenaKandidatnr='" + arenaKandidatnr + "'")
                .add("beskrivelse='" + beskrivelse + "'")
                .add("samtykkeStatus='" + samtykkeStatus + "'")
                .add("samtykkeDato=" + samtykkeDato)
                .add("adresselinje1='" + adresselinje1 + "'")
                .add("adresselinje2='" + adresselinje2 + "'")
                .add("adresselinje3='" + adresselinje3 + "'")
                .add("postnummer='" + postnummer + "'")
                .add("poststed='" + poststed + "'")
                .add("landkode='" + landkode + "'")
                .add("kommunenummer=" + kommunenummer)
                .add("kommunenummerkw=" + kommunenummerkw)
                .add("kommunenummerstring='" + kommunenummerstring + "'")
                .add("fylkeNavn='" + fylkeNavn + "'")
                .add("kommuneNavn='" + kommuneNavn + "'")
                .add("disponererBil=" + disponererBil)
                .add("tidsstempel=" + tidsstempel)
                .add("doed=" + doed)
                .add("frKode='" + frKode + "'")
                .add("kvalifiseringsgruppekode='" + kvalifiseringsgruppekode + "'")
                .add("hovedmaalkode='" + hovedmaalkode + "'")
                .add("orgenhet='" + orgenhet + "'")
                .add("navkontor='" + navkontor + "'")
                .add("fritattKandidatsok=" + fritattKandidatsok)
                .add("fritattAgKandidatsok=" + fritattAgKandidatsok)
                .add("utdanning=" + utdanning)
                .add("fagdokumentasjon=" + fagdokumentasjon)
                .add("yrkeserfaring=" + yrkeserfaring)
                .add("kompetanseObj=" + kompetanseObj)
                .add("annenerfaringObj=" + annenerfaringObj)
                .add("sertifikatObj=" + sertifikatObj)
                .add("forerkort=" + forerkort)
                .add("sprak=" + sprak)
                .add("kursObj=" + kursObj)
                .add("vervObj=" + vervObj)
                .add("geografiJobbonsker=" + geografiJobbonsker)
                .add("yrkeJobbonskerObj=" + yrkeJobbonskerObj)
                .add("omfangJobbonskerObj=" + omfangJobbonskerObj)
                .add("ansettelsesformJobbonskerObj=" + ansettelsesformJobbonskerObj)
                .add("arbeidstidsordningJobbonskerObj=" + arbeidstidsordningJobbonskerObj)
                .add("arbeidsdagerJobbonskerObj=" + arbeidsdagerJobbonskerObj)
                .add("arbeidstidJobbonskerObj=" + arbeidstidJobbonskerObj)
                .add("samletKompetanseObj=" + samletKompetanseObj)
                .add("totalLengdeYrkeserfaring=" + totalLengdeYrkeserfaring)
                .add("synligForArbeidsgiverSok=" + synligForArbeidsgiverSok)
                .add("synligForVeilederSok=" + synligForVeilederSok)
                .add("oppstartKode='" + oppstartKode + "'")
                .add("veileder='" + veileder + "'")
                .add("inkluderingsbehov=" + inkluderingsbehov)
                .add("godkjenninger=" + godkjenninger)
                .add("perioderMedInaktivitet=" + perioderMedInaktivitet)
                .toString();
    }
}
