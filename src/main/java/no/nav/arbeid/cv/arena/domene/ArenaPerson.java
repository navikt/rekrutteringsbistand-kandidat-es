package no.nav.arbeid.cv.arena.domene;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "PERSON")
public class ArenaPerson implements Serializable {

  private static final long serialVersionUID = 9031002387752455069L;

  @Id
  @Column
  private Long personId;

  @Column
  private LocalDate fodselsdato;

  @Column(name = "FODSELSNR")
  private String fodselsnummer;

  // Enum? (Kan være N eller J)
  @Column(name = "STATUS_DNR")
  private String erFodselsnummerDnr;

  @Column
  private String formidlingsgruppekode;

  @Column
  private String etternavn;

  @Column
  private String fornavn;

  @Column(name = "LANDKODE_STATSBORGER")
  private String statsborgerskap;

  @Column(name = "DATO_SAMTYKKE")
  private LocalDate samtykkeDato;

  /**
   * G Samtykke/Presenteres med navn Ja både nav.no og Arena J Samtykke/Presenteres med navn Ja
   * Arena, Nei Aetat.no B Samtykke/Presenteres med navn Ja Aetat.no, Nei Arena N
   * Samtykke/Presenteres med navn Nei
   */
  @Column(name = "STATUS_SAMTYKKE")
  private String samtykkeStatus;

  // TODO: Kan være (Kan være N eller J eller null)
  @Column(name = "STATUS_BILDISP")
  private String disponererBil;

  @Column
  private LocalDateTime regDato;

  @Column
  private LocalDateTime modDato;

  // null, N, J
  @Column
  private String erDoed;

  // null, 0, 4, 5, 6, 7
  @Column
  private String frKode;

  @OneToOne(mappedBy = "person", fetch = FetchType.EAGER)
  @Fetch(FetchMode.SELECT)
  private ArenaCv cv;

  @OneToOne(mappedBy = "person", fetch = FetchType.EAGER)
  @Fetch(FetchMode.JOIN)
  private ArenaArbeidsmarkedsbruker arbeidsmarkedsbruker;

  @OneToMany(mappedBy = "person", fetch = FetchType.EAGER)
  @Fetch(FetchMode.SELECT)
  private List<ArenaPersonforhold> personforhold;

  @OneToMany(mappedBy = "person", fetch = FetchType.EAGER)
  @Fetch(FetchMode.SELECT)
  private List<ArenaAdresse> adresser;

  @OneToMany(mappedBy = "person", fetch = FetchType.EAGER)
  @Fetch(FetchMode.SELECT)
  private List<ArenaKommbruk> kommbruk;

  @OneToMany(mappedBy = "person", fetch = FetchType.EAGER)
  @Fetch(FetchMode.SELECT)
  @OrderBy("CVLEDDNR")
  private List<ArenaCvLedd> cvLedd;

  @OneToMany(mappedBy = "person", fetch = FetchType.EAGER)
  @Fetch(FetchMode.SELECT)
  private List<ArenaPersonProfilLedd> personprofilLedd;

  @SuppressWarnings("unused")
  private ArenaPerson() {}

  public ArenaPerson(Long personId, ArenaArbeidsmarkedsbruker arbeidsmarkedsbruker, ArenaCv cv,
      ArenaAdresse adresse, ArenaKommbruk epost, List<ArenaCvLedd> cvLedd,
      List<ArenaPersonProfilLedd> personprofilLedd, LocalDate fodselsdato, String fodselsnummer,
      String etternavn, String fornavn, LocalDate samtykkeDato, String samtykkeStatus,
      String disponererBil, String formidlingsgruppekode, String erFodselsnummerDnr,
      String statsborgerskap, String kandidatnummer, LocalDateTime tidsstempel) {
    super();
    this.personId = personId;
    this.arbeidsmarkedsbruker = arbeidsmarkedsbruker;
    this.cv = cv;
    this.adresser = adresse == null ? Collections.emptyList() : Collections.singletonList(adresse);
    this.kommbruk = kommbruk == null ? Collections.emptyList() : Collections.singletonList(epost);
    this.cvLedd = cvLedd;
    this.personprofilLedd = personprofilLedd;
    this.fodselsdato = fodselsdato;
    this.fodselsnummer = fodselsnummer;
    this.etternavn = etternavn;
    this.fornavn = fornavn;
    this.samtykkeDato = samtykkeDato;
    this.samtykkeStatus = samtykkeStatus;
    this.disponererBil = disponererBil;
    this.formidlingsgruppekode = formidlingsgruppekode;
    this.erFodselsnummerDnr = erFodselsnummerDnr;
    this.statsborgerskap = statsborgerskap;
    this.cvLedd.forEach(ledd -> ledd.setPerson(this));
    this.personprofilLedd.forEach(ledd -> ledd.setPerson(this));
    this.modDato = tidsstempel;
    // this.kontaktInformasjon.setPerson(this);
  }

  @JsonIgnore
  public List<ArenaPersonforhold> getPersonforhold() {
    return personforhold;
  }

  public Long getPersonId() {
    return personId;
  }

  public LocalDate getFodselsdato() {
    return fodselsdato;
  }

  public String getFodselsnummer() {
    return fodselsnummer;
  }

  public String getEtternavn() {
    return etternavn;
  }

  public String getFornavn() {
    return fornavn;
  }

  public String getFormidlingsgruppekode() {
    return formidlingsgruppekode;
  }

  public String getStatsborgerskap() {
    return statsborgerskap;
  }

  @JsonIgnore
  public String getFrKode() {
    return frKode;
  }

  @JsonIgnore
  public boolean isDoed() {
    return erDoed != null && erDoed.equalsIgnoreCase("J");
  }

  public LocalDate getSamtykkeDato() {
    return samtykkeDato;
  }

  public String getSamtykkeStatus() {
    return samtykkeStatus;
  }

  public String getBeskrivelse() {
    return StringUtils.defaultString(cv == null ? "" : cv.getBeskrivelse());
  }

  public String getKandidatnummer() {
    return arbeidsmarkedsbruker.getKandidatnummer();
  }

  public boolean isErFodselsnummerDnr() {
    return erFodselsnummerDnr != null && erFodselsnummerDnr.equalsIgnoreCase("J");
  }

  public boolean isDisponererBil() {
    return disponererBil != null && disponererBil.equalsIgnoreCase("J");
  }

  public LocalDateTime getSistEndret() {
    return modDato == null ? regDato : modDato;
  }

  @JsonIgnore
  public List<ArenaCvLedd> getCvLedd() {
    return cvLedd;
  }

  public ArenaAdresse getAdresse() {
    return adresser == null ? null
        : adresser.stream().filter(adr -> adr.isGyldig()).findFirst().orElse(null);
  }

  public String getEpost() {
    return kommbruk == null ? null
        : kommbruk.stream().filter(k -> k.isGyldig()).findFirst().map(k -> k.getEpost())
            .orElse(null);
  }

  public void setEpost(String epost) {
    if (kommbruk != null) {
      kommbruk.stream().filter(adr -> adr.isGyldig()).forEach(k -> k.setKommStreng(epost));
    }
  }

  public List<ArenaUtdanningCvLedd> getUtdanning() {
    return cvLedd.stream().filter(ledd -> ledd instanceof ArenaUtdanningCvLedd)
        .map(ledd -> (ArenaUtdanningCvLedd) ledd).collect(Collectors.toList());
  }

  public List<ArenaYrkeCvLedd> getYrkeserfaring() {
    return cvLedd.stream().filter(ledd -> ledd instanceof ArenaYrkeCvLedd)
        .map(ledd -> (ArenaYrkeCvLedd) ledd).collect(Collectors.toList());
  }

  public List<ArenaSertifikatCvLedd> getSertifikater() {
    return cvLedd.stream().filter(ledd -> ledd instanceof ArenaSertifikatCvLedd)
        .map(ledd -> (ArenaSertifikatCvLedd) ledd).collect(Collectors.toList());
  }

  public List<ArenaKompetanseCvLedd> getKompetanse() {
    return cvLedd.stream().filter(ledd -> ledd instanceof ArenaKompetanseCvLedd)
        .map(ledd -> (ArenaKompetanseCvLedd) ledd).collect(Collectors.toList());
  }

  public List<ArenaKursCvLedd> getKurs() {
    return cvLedd.stream().filter(ledd -> ledd instanceof ArenaKursCvLedd)
        .map(ledd -> (ArenaKursCvLedd) ledd).collect(Collectors.toList());
  }

  public List<ArenaVervCvLedd> getVerv() {
    return cvLedd.stream().filter(ledd -> ledd instanceof ArenaVervCvLedd)
        .map(ledd -> (ArenaVervCvLedd) ledd).collect(Collectors.toList());
  }

  @JsonIgnore
  public List<ArenaPersonProfilLedd> getPersonprofilLedd() {
    return personprofilLedd;
  }

  public List<ArenaGeoPersonProfilLedd> getGeografiJobbonsker() {
    return personprofilLedd.stream().filter(ledd -> ledd instanceof ArenaGeoPersonProfilLedd)
        .map(ledd -> (ArenaGeoPersonProfilLedd) ledd).collect(Collectors.toList());
  }

  public List<ArenaYrkePersonProfilLedd> getYrkeJobbonsker() {
    return personprofilLedd.stream().filter(ledd -> ledd instanceof ArenaYrkePersonProfilLedd)
        .map(ledd -> (ArenaYrkePersonProfilLedd) ledd).collect(Collectors.toList());
  }

  public List<ArenaHedePersonProfilLedd> getHeltidDeltidJobbonsker() {
    return personprofilLedd.stream().filter(ledd -> ledd instanceof ArenaHedePersonProfilLedd)
        .map(ledd -> (ArenaHedePersonProfilLedd) ledd).collect(Collectors.toList());
  }

  public List<ArenaAnsfPersonProfilLedd> getAnsettelsesforholdJobbonsker() {
    return personprofilLedd.stream().filter(ledd -> ledd instanceof ArenaAnsfPersonProfilLedd)
        .map(ledd -> (ArenaAnsfPersonProfilLedd) ledd).collect(Collectors.toList());
  }

  public List<ArenaArboPersonProfilLedd> getArbeidstidsordningJobbonsker() {
    return personprofilLedd.stream().filter(ledd -> ledd instanceof ArenaArboPersonProfilLedd)
        .map(ledd -> (ArenaArboPersonProfilLedd) ledd).collect(Collectors.toList());
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((personId == null) ? 0 : personId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ArenaPerson other = (ArenaPerson) obj;
    if (personId == null) {
      if (other.personId != null)
        return false;
    } else if (!personId.equals(other.personId))
      return false;
    return true;
  }

  public void setFodselsdato(LocalDate fodselsdato) {
    this.fodselsdato = fodselsdato;
  }

  public void setEtternavn(String etternavn) {
    this.etternavn = etternavn;
  }

  public void setFornavn(String fornavn) {
    this.fornavn = fornavn;
  }

  public void setFodselsnummer(String fodselsnummer) {
    this.fodselsnummer = fodselsnummer;
  }

}


