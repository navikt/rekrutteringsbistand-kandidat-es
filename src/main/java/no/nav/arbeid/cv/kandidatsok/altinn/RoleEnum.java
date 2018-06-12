package no.nav.arbeid.cv.kandidatsok.altinn;

public enum RoleEnum {

  LONN_PERSONAL("Lønn og personalmedarbeider", "LOPER", 3), UTFYLLER_INSENDER("Utfyller/Innsender",
      "UTINN", 11);

  private final String description;
  private final String code;
  private final Integer roleId;

  RoleEnum(String description, String code, Integer roleId) {
    this.description = description;
    this.code = code;
    this.roleId = roleId;
  }

  public String getDescription() {
    return description;
  }

  public String getCode() {
    return code;
  }

  public Integer getRoleId() {
    return roleId;
  }
}
