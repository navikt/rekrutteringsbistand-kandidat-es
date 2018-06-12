package no.nav.arbeid.cv.kandidatsok.altinn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Reportee {

  private String name;
  private String type;
  private String status;
  private String organizationNumber;
  private String socialSecurityNumber;

  @JsonProperty("Name")
  public String getName() {
    return name;
  }

  @JsonProperty("Type")
  public String getType() {
    return type;
  }

  @JsonProperty("Status")
  public String getStatus() {
    return status;
  }

  @JsonProperty("OrganizationNumber")
  public String getOrganizationNumber() {
    return organizationNumber;
  }

  @JsonProperty("socialSecurityNumber")
  public String getSocialSecurityNumber() {
    return socialSecurityNumber;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setOrganizationNumber(String organizationNumber) {
    this.organizationNumber = organizationNumber;
  }

  public void setSocialSecurityNumber(String socialSecurityNumber) {
    this.socialSecurityNumber = socialSecurityNumber;
  }

}
