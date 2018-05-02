package no.nav.arbeid.cv.es.altinn;

public enum AccessRightsEnum {
    REKRUTTERING("5078", "1");

    private final String serviceCode;
    private final String serviceEditionCode;

    AccessRightsEnum(String serviceCode, String serviceEditionCode) {
        this.serviceCode = serviceCode;
        this.serviceEditionCode = serviceEditionCode;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public String getServiceEditionCode() {
        return serviceEditionCode;
    }
}