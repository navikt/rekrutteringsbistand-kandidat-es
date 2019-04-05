package no.nav.arbeid.cv.kandidatsok.es.domene;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.elasticsearch.mapping.annotations.ElasticBooleanField;
import no.nav.elasticsearch.mapping.annotations.ElasticDateField;
import no.nav.elasticsearch.mapping.annotations.ElasticDocument;
import no.nav.elasticsearch.mapping.annotations.ElasticIntegerField;
import no.nav.elasticsearch.mapping.annotations.ElasticKeywordField;
import no.nav.elasticsearch.mapping.annotations.ElasticLongField;
import no.nav.elasticsearch.mapping.annotations.ElasticNestedField;
import no.nav.elasticsearch.mapping.annotations.ElasticObjectField;
import no.nav.elasticsearch.mapping.annotations.ElasticTextField;

@JsonIgnoreProperties(ignoreUnknown = true)
@ElasticDocument
public class EsCv {

    @ElasticTextField(analyzer = "norwegian")
    private String fritekst;

    @ElasticTextField
    private String fodselsnummer;

    @ElasticTextField
    private String fornavn;

    @ElasticTextField
    private String etternavn;

    @ElasticDateField
    private Date fodselsdato;

    @ElasticBooleanField
    private Boolean fodselsdatoErDnr;

    @ElasticKeywordField
    private String formidlingsgruppekode;

    @ElasticKeywordField
    private String epostadresse;

    @ElasticKeywordField
    private String mobiltelefon;

    @ElasticBooleanField
    private boolean harKontaktinformasjon;

    @ElasticKeywordField
    private String telefon;

    @ElasticKeywordField
    private String statsborgerskap;

    @ElasticKeywordField
    private String kandidatnr;

    @ElasticKeywordField
    private String arenaKandidatnr;

    @ElasticTextField(copyTo = "fritekst", analyzer = "norwegian")
    private String beskrivelse;

    @ElasticKeywordField
    private String samtykkeStatus;

    @ElasticDateField
    private Date samtykkeDato;

    @ElasticTextField
    private String adresselinje1;

    @ElasticTextField
    private String adresselinje2;

    @ElasticTextField
    private String adresselinje3;

    @ElasticKeywordField
    private String postnummer;

    @ElasticKeywordField
    private String poststed;

    @ElasticKeywordField
    private String landkode;

    @ElasticLongField
    private Integer kommunenummer;

    @ElasticKeywordField
    private Integer kommunenummerkw;

    @ElasticKeywordField
    private String kommunenummerstring;

    @ElasticBooleanField
    private Boolean disponererBil;

    @ElasticDateField
    private Date tidsstempel;

    @ElasticBooleanField
    private Boolean doed;

    @ElasticKeywordField
    private String frKode;

    @ElasticKeywordField
    private String kvalifiseringsgruppekode;

    @ElasticKeywordField
    private String hovedmaalkode;

    @ElasticTextField
    private String orgenhet;

    @ElasticBooleanField
    private Boolean fritattKandidatsok;

    @ElasticBooleanField
    private Boolean fritattAgKandidatsok;

    @ElasticNestedField
    private List<EsUtdanning> utdanning = new ArrayList<>();

    @ElasticObjectField
    private List<EsFagdokumentasjon> fagdokumentasjon = new ArrayList<>();

    @ElasticNestedField
    private List<EsYrkeserfaring> yrkeserfaring = new ArrayList<>();

    @ElasticObjectField
    private List<EsKompetanse> kompetanseObj = new ArrayList<>();

    @ElasticObjectField
    private List<EsAnnenErfaring> annenerfaringObj = new ArrayList<>();

    @ElasticObjectField
    private List<EsSertifikat> sertifikatObj = new ArrayList<>();

    @ElasticNestedField
    private List<EsForerkort> forerkort = new ArrayList<>();

    @ElasticNestedField
    private List<EsSprak> sprak = new ArrayList<>();

    @ElasticObjectField
    private List<EsKurs> kursObj = new ArrayList<>();

    @ElasticObjectField
    private List<EsVerv> vervObj = new ArrayList<>();

    @ElasticNestedField
    private List<EsGeografiJobbonsker> geografiJobbonsker = new ArrayList<>();

    @ElasticObjectField
    private List<EsYrkeJobbonsker> yrkeJobbonskerObj = new ArrayList<>();

    @ElasticObjectField
    private List<EsOmfangJobbonsker> omfangJobbonskerObj = new ArrayList<>();

    @ElasticObjectField
    private List<EsAnsettelsesformJobbonsker> ansettelsesformJobbonskerObj = new ArrayList<>();

    @ElasticObjectField
    private List<EsArbeidstidsordningJobbonsker> arbeidstidsordningJobbonskerObj = new ArrayList<>();

    @ElasticObjectField
    private List<EsArbeidsdagerJobbonsker> arbeidsdagerJobbonskerObj = new ArrayList<>();

    @ElasticObjectField
    private List<EsArbeidstidJobbonsker> arbeidstidJobbonskerObj = new ArrayList<>();

    @ElasticObjectField
    private List<EsSamletKompetanse> samletKompetanseObj = new ArrayList<>();

    @ElasticIntegerField
    private int totalLengdeYrkeserfaring;

    @ElasticBooleanField
    private Boolean synligForArbeidsgiverSok;

    @ElasticBooleanField
    private Boolean synligForVeilederSok;

    @ElasticKeywordField
    private String oppstartKode;

    public EsCv() {
    }

    public EsCv(String fodselsnummer, String fornavn, String etternavn, Date fodselsdato,
            Boolean fodselsdatoErDnr, String formidlingsgruppekode, String epostadresse,
            String mobiltelefon, String telefon, String statsborgerskap, String kandidatnr, String beskrivelse,
            String samtykkeStatus, Date samtykkeDato, String adresselinje1, String adresselinje2,
            String adresselinje3, String postnummer, String poststed, String landkode,
            Integer kommunenummer, Boolean disponererBil, Date tidsstempel, Integer kommunenummerkw,
            Boolean doed, String frKode, String kvalifiseringsgruppekode, String hovedmaalkode, String orgenhet,
            Boolean fritattKandidatsok, Boolean fritattAgKandidatsok,
            Boolean synligForArbeidsgiverSok, Boolean synligForVeilederSok, String oppstartKode, String kommunenummerstring) {
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
        this.fritattKandidatsok = fritattKandidatsok;
        this.fritattAgKandidatsok = fritattAgKandidatsok;
        this.synligForArbeidsgiverSok = synligForArbeidsgiverSok;
        this.synligForVeilederSok = synligForVeilederSok;
        this.oppstartKode = oppstartKode;
        this.kommunenummerstring = kommunenummerstring;
    }

    public EsCv(String fodselsnummer, String fornavn, String etternavn, Date fodselsdato,
            Boolean fodselsdatoErDnr, String formidlingsgruppekode, String epostadresse,
            String mobiltelefon, String telefon, String statsborgerskap, String kandidatnr, String beskrivelse,
            String samtykkeStatus, Date samtykkeDato, String adresselinje1, String adresselinje2,
            String adresselinje3, String postnummer, String poststed, String landkode,
            Integer kommunenummer, Boolean disponererBil, Date tidsstempel, Integer kommunenummerkw,
            Boolean doed, String frKode, String kvalifiseringsgruppekode, String hovedmaalkode, String orgenhet,
            Boolean fritattKandidatsok, Boolean fritattAgKandidatsok, String kommunenummerstring) {
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
        this.fritattKandidatsok = fritattKandidatsok;
        this.fritattAgKandidatsok = fritattAgKandidatsok;
        this.synligForArbeidsgiverSok = beregnSynlighetForArbeidsgiverSokBasertPaaGamleArenaData();
        this.synligForVeilederSok = beregnSynlighetForVeilederSokBasertPaaGamleArenaData();
        this.kommunenummerstring = kommunenummerstring;
    }

    private Boolean beregnSynlighetForVeilederSokBasertPaaGamleArenaData() {
        if( Boolean.TRUE.equals(this.doed)) {
            return false;
        }
        if( "6".equals(this.frKode)) {
            return false;
        }
        if( "7".equals(this.frKode)) {
            return false;
        }
        if( !("ARBS".equals(this.formidlingsgruppekode)
                || "PARBS".equals(this.formidlingsgruppekode)
                || "RARBS".equals(this.formidlingsgruppekode))) {
            return false;
        }
        if( Boolean.TRUE.equals(this.fritattKandidatsok)) {
            return false;
        }
        return true;
    }

    private Boolean beregnSynlighetForArbeidsgiverSokBasertPaaGamleArenaData() {
        if( Boolean.TRUE.equals(this.doed)) {
            return false;
        }
        if( "6".equals(this.frKode)) {
            return false;
        }
        if( "7".equals(this.frKode)) {
            return false;
        }
        if( !("JOBBS".equals(this.formidlingsgruppekode)
                || "ARBS".equals(this.formidlingsgruppekode)
                || "PARBS".equals(this.formidlingsgruppekode)
                || "RARBS".equals(this.formidlingsgruppekode))) {
            return false;
        }
        if( Boolean.TRUE.equals(this.fritattAgKandidatsok)) {
            return false;
        }
        if( Boolean.TRUE.equals(this.fritattKandidatsok)) {
            return false;
        }
        if( Boolean.FALSE.equals(this.harKontaktinformasjon)) {
            return false;
        }
        return true;
    }

    // Adderfunksjoner
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
      liste.add(new EsSamletKompetanse(EsFagdokumentasjon.getFagdokumentTypeLabel(f.getType())));
      if (f.getTittel() != null) {
        liste.add(new EsSamletKompetanse(f.getTittel()));
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
            this.addSamletKompetanse(kompetanse.getSokeNavn().stream().map(str->new EsSamletKompetanse(str)).collect(Collectors.toList()));            
        }
    }

    public void addKompetanse(Collection<EsKompetanse> kompetanseListe) {
        if (kompetanseListe != null) {
            this.kompetanseObj.addAll(kompetanseListe);
            this.addSamletKompetanse(
                    kompetanseListe.stream().flatMap(k -> k.getSokeNavn().stream()).map(str-> new EsSamletKompetanse(str))
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
            this.addSamletKompetanse(Collections
                    .singletonList(new EsSamletKompetanse(forerkort.getForerkortKodeKlasse())));
        }
    }

    public void addForerkort(Collection<EsForerkort> forerkortListe) {
        if (forerkortListe != null) {
            this.forerkort.addAll(forerkortListe);
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
//            if (StringUtils.isNotBlank(kurs.getTittel())) {
//                this.addSamletKompetanse(
//                        Collections.singletonList(new EsSamletKompetanse(kurs.getTittel())));
//            }
        }
    }

    public void addKurs(Collection<EsKurs> kursListe) {
        if (kursListe != null) {
            this.kursObj.addAll(kursListe);
            // this.addSamletKompetanse(kursListe.stream().map(s -> s.getTittel())
            // .filter(t -> StringUtils.isNotBlank(t)).map(t -> new EsSamletKompetanse(t))
            // .collect(Collectors.toList()));
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

    public void addOmfangJobbonske(Collection<EsOmfangJobbonsker> omfangJobbonsker){
        this.omfangJobbonskerObj.addAll(omfangJobbonsker);
    }

    public void addAnsettelsesformJobbonske(Collection<EsAnsettelsesformJobbonsker> ansettelsesformJobbonsker){
        this.ansettelsesformJobbonskerObj.addAll(ansettelsesformJobbonsker);
    }

    public void addArbeidstidsordningJobbonsker(Collection<EsArbeidstidsordningJobbonsker> arbeidstidsordningJobbonsker){
        this.arbeidstidsordningJobbonskerObj.addAll(arbeidstidsordningJobbonsker);
    }

    public void addArbeidstidJobbonsker(Collection<EsArbeidstidJobbonsker> arbeidstidJobbonsker){
        this.arbeidstidJobbonskerObj.addAll(arbeidstidJobbonsker);
    }

    public void addArbeidsdagerJobbonsker(Collection<EsArbeidsdagerJobbonsker> arbeidsdagerJobbonsker){
        this.arbeidsdagerJobbonskerObj.addAll(arbeidsdagerJobbonsker);
    }

    private void addSamletKompetanse(Collection<EsSamletKompetanse> samletKompetanseListe) {
        this.samletKompetanseObj.addAll(samletKompetanseListe);
    }

    // gettere

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

    public int getTotalLengdeYrkeserfaring() {
        return totalLengdeYrkeserfaring;
    }

    public String getOppstartKode() {
        return this.oppstartKode;
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
                && Objects.equals(fornavn, esCv.fornavn)
                && Objects.equals(etternavn, esCv.etternavn)
                && Objects.equals(fodselsdato, esCv.fodselsdato)
                && Objects.equals(fodselsdatoErDnr, esCv.fodselsdatoErDnr)
                && Objects.equals(doed, esCv.doed)
                && Objects.equals(frKode, esCv.frKode)
                && Objects.equals(kvalifiseringsgruppekode, esCv.kvalifiseringsgruppekode)
                && Objects.equals(hovedmaalkode, esCv.hovedmaalkode)
                && Objects.equals(orgenhet, esCv.orgenhet)
                && Objects.equals(fritattKandidatsok, esCv.fritattKandidatsok)
                && Objects.equals(fritattAgKandidatsok, esCv.fritattAgKandidatsok)
                && Objects.equals(synligForArbeidsgiverSok, esCv.synligForArbeidsgiverSok)
                && Objects.equals(synligForVeilederSok, esCv.synligForVeilederSok)
                && Objects.equals(epostadresse, esCv.epostadresse)
                && Objects.equals(mobiltelefon, esCv.mobiltelefon)
                && Objects.equals(telefon, esCv.telefon)
                && Objects.equals(statsborgerskap, esCv.statsborgerskap)
                && Objects.equals(kandidatnr, esCv.kandidatnr)
                && Objects.equals(beskrivelse, esCv.beskrivelse)
                && Objects.equals(samtykkeStatus, esCv.samtykkeStatus)
                && Objects.equals(samtykkeDato, esCv.samtykkeDato)
                && Objects.equals(adresselinje1, esCv.adresselinje1)
                && Objects.equals(adresselinje2, esCv.adresselinje2)
                && Objects.equals(adresselinje3, esCv.adresselinje3)
                && Objects.equals(postnummer, esCv.postnummer)
                && Objects.equals(poststed, esCv.poststed)
                && Objects.equals(landkode, esCv.landkode)
                && Objects.equals(kommunenummer, esCv.kommunenummer)
                && Objects.equals(kommunenummerkw, esCv.kommunenummerkw)
                && Objects.equals(kommunenummerstring, esCv.kommunenummerstring)
                && Objects.equals(disponererBil, esCv.disponererBil)
                && Objects.equals(tidsstempel, esCv.tidsstempel)
                && Objects.equals(utdanning, esCv.utdanning)
                && Objects.equals(fagdokumentasjon, esCv.fagdokumentasjon)
                && Objects.equals(yrkeserfaring, esCv.yrkeserfaring)
                && Objects.equals(kompetanseObj, esCv.kompetanseObj)
                && Objects.equals(annenerfaringObj, esCv.annenerfaringObj)
                && Objects.equals(sertifikatObj, esCv.sertifikatObj)
                && Objects.equals(forerkort, esCv.forerkort) && Objects.equals(sprak, esCv.sprak)
                && Objects.equals(kursObj, esCv.kursObj) && Objects.equals(vervObj, esCv.vervObj)
                && Objects.equals(geografiJobbonsker, esCv.geografiJobbonsker)
                && Objects.equals(yrkeJobbonskerObj, esCv.yrkeJobbonskerObj)
                && Objects.equals(omfangJobbonskerObj, esCv.omfangJobbonskerObj)
                && Objects.equals(ansettelsesformJobbonskerObj, esCv.ansettelsesformJobbonskerObj)
                && Objects.equals(arbeidstidsordningJobbonskerObj, esCv.arbeidstidsordningJobbonskerObj)
                && Objects.equals(arbeidstidJobbonskerObj, esCv.arbeidstidJobbonskerObj)
                && Objects.equals(arbeidsdagerJobbonskerObj, esCv.arbeidsdagerJobbonskerObj)
                && Objects.equals(samletKompetanseObj, esCv.samletKompetanseObj)
                && Objects.equals(totalLengdeYrkeserfaring, esCv.totalLengdeYrkeserfaring)
                && Objects.equals(oppstartKode, esCv.oppstartKode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fodselsnummer, fornavn, etternavn, fodselsdato, fodselsdatoErDnr,
                formidlingsgruppekode, doed, frKode, kvalifiseringsgruppekode, hovedmaalkode, orgenhet, fritattKandidatsok, fritattAgKandidatsok, epostadresse, mobiltelefon, telefon, statsborgerskap,
                kandidatnr, beskrivelse, samtykkeStatus, samtykkeDato, adresselinje1,
                adresselinje2, adresselinje3, postnummer, poststed, landkode, kommunenummer, kommunenummerkw, kommunenummerstring,
                disponererBil, tidsstempel, utdanning, fagdokumentasjon, yrkeserfaring, kompetanseObj, annenerfaringObj,
                sertifikatObj, forerkort, sprak, kursObj, vervObj, geografiJobbonsker, yrkeJobbonskerObj,
                omfangJobbonskerObj, ansettelsesformJobbonskerObj, arbeidstidsordningJobbonskerObj,
                arbeidstidJobbonskerObj, arbeidsdagerJobbonskerObj, samletKompetanseObj, totalLengdeYrkeserfaring,
                synligForArbeidsgiverSok, synligForVeilederSok, oppstartKode);
    }

    @Override
    public String toString() {
        return "EsCv [fritekst=" + fritekst + ", fodselsnummer=" + fodselsnummer + ", fornavn="
                + fornavn + ", etternavn=" + etternavn + ", fodselsdato=" + fodselsdato
                + ", fodselsdatoErDnr=" + fodselsdatoErDnr + ", formidlingsgruppekode="
                + formidlingsgruppekode + ", epostadresse=" + epostadresse + ", mobiltelefon="
                + mobiltelefon + ", telefon=" + telefon + ", statsborgerskap=" + statsborgerskap
                + ", arenaKandidatnr=" + kandidatnr
                + ", beskrivelse=" + beskrivelse + ", samtykkeStatus=" + samtykkeStatus
                + ", samtykkeDato=" + samtykkeDato + ", adresselinje1=" + adresselinje1
                + ", adresselinje2=" + adresselinje2 + ", adresselinje3=" + adresselinje3
                + ", postnummer=" + postnummer + ", poststed=" + poststed + ", landkode=" + landkode
                + ", kommunenummer=" + kommunenummer + ", kommunenummerkw=" + kommunenummerkw + ", kommunenummerstring=" + kommunenummerstring
                + ", disponererBil=" + disponererBil + ", tidsstempel=" + tidsstempel + ", doed="
                + doed + ", frKode=" + frKode + ", kvalifiseringsgruppekode="
                + kvalifiseringsgruppekode + ", hovedmaalkode=" + hovedmaalkode + ", orgenhet="
                + orgenhet + ", fritattKandidatsok=" + fritattKandidatsok
                + ", fritattAgKandidatsok=" + fritattAgKandidatsok
                + ", synligForArbeidsgiverSok=" + synligForArbeidsgiverSok
                + ", synligForVeilederSok=" + synligForVeilederSok
                + ", utdanning=" + utdanning + ", fagdokumentasjon=" + fagdokumentasjon
                + ", yrkeserfaring=" + yrkeserfaring + ", kompetanse=" + kompetanseObj
                + ", annenerfaring=" + annenerfaringObj + ", sertifikat=" + sertifikatObj + ", forerkort="
                + forerkort + ", sprak=" + sprak + ", kurs=" + kursObj + ", verv=" + vervObj
                + ", geografiJobbonsker=" + geografiJobbonsker + ", yrkeJobbonsker="
                + yrkeJobbonskerObj + ", omfangJobbonsker=" + omfangJobbonskerObj
                + ", ansettelsesformJobbonsker=" + ansettelsesformJobbonskerObj
                + ", arbeidstidsordningJobbonsker=" + arbeidstidsordningJobbonskerObj
                + ", arbeidsdagerJobbonsker=" + arbeidsdagerJobbonskerObj + ", arbeidstidJobbonsker="
                + arbeidstidJobbonskerObj + ", samletKompetanse=" + samletKompetanseObj
                + ", totalLengdeYrkeserfaring=" + totalLengdeYrkeserfaring
                + ", oppstartKode=" + oppstartKode + "]";
    }

    public void setKandidatnr(String nyArenaKandidatnr) {
        this.kandidatnr = nyArenaKandidatnr;
    }

}
