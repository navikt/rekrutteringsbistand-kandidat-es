package no.nav.arbeid.cv.es.altinn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Role {

    private String roleType;
    private int roleDefinitionId;
    private String roleName;
    private String roleDescription;
    private String RoleDefinitionCode;

    public String getRoleType() {
        return roleType;
    }


    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public int getRoleDefinitionId() {
        return roleDefinitionId;
    }


    public void setRoleDefinitionId(int roleDefinitionId) {
        this.roleDefinitionId = roleDefinitionId;
    }

    public String getRoleName() {
        return roleName;
    }


    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDescription() {
        return roleDescription;
    }


    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    public String getRoleDefinitionCode() {
        return RoleDefinitionCode;
    }


    public void setRoleDefinitionCode(String roleDefinitionCode) {
        RoleDefinitionCode = roleDefinitionCode;
    }
}
