package no.nav.arbeid.cv.kandidatsok.es.domene.sok;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsCv {

    private String fodselsnummer;

    private String fornavn;

    private String etternavn;

    private Date fodselsdato;

    private Boolean fodselsdatoErDnr;

    private String poststed;
        
    private String epostadresse;

    private String mobiltelefon;

    private String telefon;

    private String formidlingsgruppekode;

    private String kandidatnr;

    private int totalLengdeYrkeserfaring;

    private String kvalifiseringsgruppekode;

    private List<EsUtdanning> utdanning = new ArrayList<>();

    private List<EsYrkeserfaring> yrkeserfaring = new ArrayList<>();

    private float score = Float.NEGATIVE_INFINITY; // Default fra Elasticsearch..

    public EsCv() {}

    public EsCv(String fodselsnummer, String fornavn, String etternavn, Date fodselsdato, Boolean fodselsdatoErDnr,
                String poststed, String epostadresse, String telefon, String mobiltelefon, 
                String formidlingsgruppekode, String kandidatnr,
                int totalLengdeYrkeserfaring, String kvalifiseringsgruppekode, List<EsUtdanning> utdanning,
                List<EsYrkeserfaring> yrkeserfaring) {
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
        this.utdanning = utdanning;
        this.yrkeserfaring = yrkeserfaring;
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

    public String getKandidatnr() {
        return kandidatnr;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EsCv esCv = (EsCv) o;
        return totalLengdeYrkeserfaring == esCv.totalLengdeYrkeserfaring &&
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
                Objects.equals(utdanning, esCv.utdanning) &&
                Objects.equals(yrkeserfaring, esCv.yrkeserfaring);

    }

    @Override
    public int hashCode() {
        return Objects.hash(
                fodselsnummer, fornavn, etternavn, fodselsdato, fodselsdatoErDnr, poststed,
                epostadresse, telefon, mobiltelefon,
                formidlingsgruppekode, kandidatnr, totalLengdeYrkeserfaring,
                kvalifiseringsgruppekode, utdanning, yrkeserfaring
        );
    }

    @Override
    public String toString() {
        return "EsCv{" +
                "fodselsnummer='" + fodselsnummer + '\'' +
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
                ", utdanning=" + utdanning +
                ", yrkeserfaring=" + yrkeserfaring +
                ", score=" + score +
                '}';
    }

}
