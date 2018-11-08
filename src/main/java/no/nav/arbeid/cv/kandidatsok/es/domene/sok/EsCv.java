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

    private String formidlingsgruppekode;

    private Long arenaPersonId;

    private String arenaKandidatnr;

    private int totalLengdeYrkeserfaring;

    private String kvalifiseringsgruppekode;

    private List<EsUtdanning> utdanning = new ArrayList<>();

    private List<EsYrkeserfaring> yrkeserfaring = new ArrayList<>();

    private float score = Float.NEGATIVE_INFINITY; // Default fra Elasticsearch..

    public EsCv() {}

    public EsCv(String fodselsnummer, String formidlingsgruppekode, Long arenaPersonId,
            String arenaKandidatnr, int totalLengdeYrkeserfaring, String kvalifiseringsgruppekode,
            String fornavn, String etternavn, Date fodselsdato, Boolean fodselsdatoErDnr,
            List<EsUtdanning> utdanning, List<EsYrkeserfaring> yrkeserfaring) {
        super();
        this.fodselsnummer = fodselsnummer;
        this.formidlingsgruppekode = formidlingsgruppekode;
        this.arenaPersonId = arenaPersonId;
        this.arenaKandidatnr = arenaKandidatnr;
        this.totalLengdeYrkeserfaring = totalLengdeYrkeserfaring;
        this.kvalifiseringsgruppekode = kvalifiseringsgruppekode;
        this.fornavn = fornavn;
        this.etternavn = etternavn;
        this.fodselsdato = fodselsdato;
        this.fodselsdatoErDnr = fodselsdatoErDnr;
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


    public String getFormidlingsgruppekode() {
        return formidlingsgruppekode;
    }

    public String getKvalifiseringsgruppekode() {
        return kvalifiseringsgruppekode;
    }

    public Long getArenaPersonId() {
        return arenaPersonId;
    }

    public String getArenaKandidatnr() {
        return arenaKandidatnr;
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
                && Objects.equals(totalLengdeYrkeserfaring, esCv.totalLengdeYrkeserfaring)
                && Objects.equals(kvalifiseringsgruppekode, esCv.kvalifiseringsgruppekode)
                && Objects.equals(fornavn, esCv.fornavn)
                && Objects.equals(etternavn, esCv.etternavn)
                && Objects.equals(fodselsdato, esCv.fodselsdato)
                && Objects.equals(fodselsdatoErDnr, esCv.fodselsdatoErDnr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fodselsnummer, formidlingsgruppekode, arenaPersonId, arenaKandidatnr,
                utdanning, yrkeserfaring, totalLengdeYrkeserfaring, kvalifiseringsgruppekode, 
                fornavn, etternavn, fodselsdato, fodselsdatoErDnr);
    }

    @Override
    public String toString() {
        return "EsCv [fodselsnummer=" + fodselsnummer + ", fornavn=" + fornavn + ", etternavn="
                + etternavn + ", fodselsdato=" + fodselsdato + ", fodselsdatoErDnr="
                + fodselsdatoErDnr + ", formidlingsgruppekode=" + formidlingsgruppekode
                + ", arenaPersonId=" + arenaPersonId + ", arenaKandidatnr=" + arenaKandidatnr
                + ", totalLengdeYrkeserfaring=" + totalLengdeYrkeserfaring
                + ", kvalifiseringsgruppekode=" + kvalifiseringsgruppekode + ", utdanning="
                + utdanning + ", yrkeserfaring=" + yrkeserfaring + ", score=" + score + "]";
    }

    

}
