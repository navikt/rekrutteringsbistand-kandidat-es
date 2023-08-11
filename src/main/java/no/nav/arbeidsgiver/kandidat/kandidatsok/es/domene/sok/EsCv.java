package no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.sok;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsForerkort;

import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsCv {

    private String aktorId;

    private String fodselsnummer;

    private String fornavn;

    private String etternavn;

    private String fodselsdato;

    private Boolean fodselsdatoErDnr;

    private String poststed;

    private String epostadresse;

    private String mobiltelefon;

    private String telefon;

    private String formidlingsgruppekode;

    private String kandidatnr;

    private int totalLengdeYrkeserfaring;

    private String kvalifiseringsgruppekode;

    private String hovedmaalkode;

    private List<EsUtdanning> utdanning = new ArrayList<>();

    private List<EsYrkeserfaring> yrkeserfaring = new ArrayList<>();

    private List<EsForerkort> forerkort = new ArrayList<>();

    private String kommuneNavn;

    private String fylkeNavn;

    private String oppstartKode;

    private float score = Float.NEGATIVE_INFINITY; // Default fra Elasticsearch..

    public EsCv() {
    }

    public EsCv(String aktorId, String fodselsnummer, String fornavn, String etternavn, String fodselsdato, Boolean fodselsdatoErDnr,
                String poststed, String epostadresse, String telefon, String mobiltelefon,
                String formidlingsgruppekode, String kandidatnr,
                int totalLengdeYrkeserfaring, String kvalifiseringsgruppekode, String hovedmaalkode, List<EsUtdanning> utdanning,
                List<EsYrkeserfaring> yrkeserfaring, String oppstart, List<EsForerkort> forerkort, String kommuneNavn, String fylkeNavn) {
        this.aktorId = aktorId;
        this.fodselsnummer = fodselsnummer;
        this.fornavn = fornavn;
        this.etternavn = etternavn;
        this.fodselsdato = fodselsdato;
        this.fodselsdatoErDnr = fodselsdatoErDnr;
        this.poststed = poststed;
        this.epostadresse = epostadresse;
        this.telefon = telefon;
        this.mobiltelefon = mobiltelefon;
        this.formidlingsgruppekode = formidlingsgruppekode;
        this.kandidatnr = kandidatnr;
        this.totalLengdeYrkeserfaring = totalLengdeYrkeserfaring;
        this.kvalifiseringsgruppekode = kvalifiseringsgruppekode;
        this.hovedmaalkode = hovedmaalkode;
        this.utdanning = utdanning;
        this.yrkeserfaring = yrkeserfaring;
        this.oppstartKode = oppstart;
        this.forerkort = forerkort;
        this.kommuneNavn = kommuneNavn;
        this.fylkeNavn = fylkeNavn;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
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
        yrkeserfaringListe
                .forEach(y -> this.totalLengdeYrkeserfaring += y.getYrkeserfaringManeder());
        this.yrkeserfaring.addAll(yrkeserfaringListe);
    }

    public void addForerkort(Collection<EsForerkort> forerkortListe) {
       this.forerkort.addAll(forerkortListe);
    }

    public String getAktorId() {
        return aktorId;
    }

    public String getFodselsnummer() {
        return fodselsnummer;
    }

    public String getEpostadresse() {
        return epostadresse;
    }

    public String getTelefon() {
        return telefon;
    }

    public String getMobiltelefon() {
        return mobiltelefon;
    }

    public String getFormidlingsgruppekode() {
        return formidlingsgruppekode;
    }

    public String getKvalifiseringsgruppekode() {
        return kvalifiseringsgruppekode;
    }

    public String getHovedmaalkode() {
        return hovedmaalkode;
    }

    public String getKandidatnr() {
        return kandidatnr;
    }

    public String getFornavn() {
        return fornavn;
    }

    public String getEtternavn() {
        return etternavn;
    }

    public String getFodselsdato() {
        return fodselsdato;
    }

    public Boolean getFodselsdatoErDnr() {
        return fodselsdatoErDnr;
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

    public String getPoststed() {
        return poststed;
    }

    public String getOppstartKode() {
        return this.oppstartKode;
    }

    public List<EsForerkort> getForerkort() { return this.forerkort; }

    public String getKommuneNavn() {
        return kommuneNavn;
    }

    public String getFylkeNavn() {
        return fylkeNavn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EsCv esCv = (EsCv) o;
        return totalLengdeYrkeserfaring == esCv.totalLengdeYrkeserfaring &&
                Objects.equals(aktorId, esCv.aktorId) &&
                Objects.equals(fodselsnummer, esCv.fodselsnummer) &&
                Objects.equals(fornavn, esCv.fornavn) &&
                Objects.equals(etternavn, esCv.etternavn) &&
                Objects.equals(fodselsdato, esCv.fodselsdato) &&
                Objects.equals(fodselsdatoErDnr, esCv.fodselsdatoErDnr) &&
                Objects.equals(poststed, esCv.poststed) &&
                Objects.equals(epostadresse, esCv.epostadresse) &&
                Objects.equals(telefon, esCv.telefon) &&
                Objects.equals(mobiltelefon, esCv.mobiltelefon) &&
                Objects.equals(formidlingsgruppekode, esCv.formidlingsgruppekode) &&
                Objects.equals(kandidatnr, esCv.kandidatnr) &&
                Objects.equals(kvalifiseringsgruppekode, esCv.kvalifiseringsgruppekode) &&
                Objects.equals(hovedmaalkode, esCv.hovedmaalkode) &&
                Objects.equals(utdanning, esCv.utdanning) &&
                Objects.equals(yrkeserfaring, esCv.yrkeserfaring) &&
                Objects.equals(oppstartKode, esCv.oppstartKode) &&
                Objects.equals(kommuneNavn, esCv.kommuneNavn) &&
                Objects.equals(fylkeNavn, esCv.fylkeNavn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                aktorId, fodselsnummer, fornavn, etternavn, fodselsdato, fodselsdatoErDnr, poststed,
                epostadresse, telefon, mobiltelefon,
                formidlingsgruppekode, kandidatnr, totalLengdeYrkeserfaring,
                kvalifiseringsgruppekode, hovedmaalkode, utdanning, yrkeserfaring, oppstartKode, kommuneNavn, fylkeNavn
        );
    }

    @Override
    public String toString() {
        return "EsCv{" +
                "fodselsnummer='" + fodselsnummer + '\'' +
                ", aktorId='" + aktorId + '\'' +
                ", fornavn='" + fornavn + '\'' +
                ", etternavn='" + etternavn + '\'' +
                ", fodselsdato=" + fodselsdato +
                ", fodselsdatoErDnr=" + fodselsdatoErDnr +
                ", poststed='" + poststed + '\'' +
                ", epostadresse='" + epostadresse + '\'' +
                ", telefon='" + telefon + '\'' +
                ", mobiltelefon='" + mobiltelefon + '\'' +
                ", formidlingsgruppekode='" + formidlingsgruppekode + '\'' +
                ", arenaKandidatnr='" + kandidatnr + '\'' +
                ", totalLengdeYrkeserfaring=" + totalLengdeYrkeserfaring +
                ", kvalifiseringsgruppekode='" + kvalifiseringsgruppekode + '\'' +
                ", hovedmaalkode='" + hovedmaalkode + '\'' +
                ", utdanning=" + utdanning +
                ", yrkeserfaring=" + yrkeserfaring +
                ", score=" + score +
                ", oppstartKode=" + oppstartKode +
                ", forerkort=" + forerkort +
                ", kommuneNavn=" + kommuneNavn +
                ", fylkeNavn=" + fylkeNavn +
                '}';
    }
}
