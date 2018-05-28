package no.nav.arbeid.cv.arena.domene;

import java.time.LocalDate;
import java.util.Optional;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@DiscriminatorValue("SERT")
public class ArenaSertifikatCvLedd extends ArenaCvLedd {

  public ArenaSertifikatCvLedd(
      no.nav.arbeid.cv.arena.domene.ArenaCvLedd.ArenaCvLeddPK arenaCvLeddPK, ArenaPerson person,
      int cvleddnr, String leddtekst1, String leddtekst2, LocalDate fraDato, LocalDate tilDato,
      String beskrivelse, Integer omfangVerdi, String omfangMaaleenhet, String strukturkode,
      String elementord, String elementklassekode, String nivaa, String hensyn, String aktiv) {
    super(arenaCvLeddPK, person, cvleddnr, leddtekst1, leddtekst2, fraDato, tilDato, beskrivelse,
        omfangVerdi, omfangMaaleenhet, strukturkode, elementord, elementklassekode, nivaa, hensyn,
        aktiv);
  }

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "profilelement_id")
  @Fetch(FetchMode.JOIN)
  @NotFound(action = NotFoundAction.IGNORE)
  // @BatchSize(size = 100)
  private ArenaSertifikat sertifikat;

  @JsonIgnore
  public Optional<ArenaKode> getKode() {
    return Optional.ofNullable(sertifikat);
  }

  public ArenaSertifikatCvLedd() {}

  public String getUtsteder() {
    return StringUtils.defaultString(getLeddtekst1());
  }

  public String getSertifikatKode() {
    return getKode().map(k -> k.getStrukturkode()).orElse("");
  }

  public String getSertifikatKodeNavn() {
    return getKode().map(k -> k.getElementOrd()).orElse("");
  }

  public String getAlternativtNavn() {
    return getLeddtekst2();
  }

}
