package no.nav.arbeid.cv.es.altinn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.joda.time.DateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Organization {

    private String name;
    private String organizationNumber;
    private String type;
    private DateTime lastChanged;
    private DateTime lastConfirmed;

    public void setName(String name) {
        this.name = name;
    }

    public void setOrganizationNumber(String organizationNumber) {
        this.organizationNumber = organizationNumber;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLastChanged(DateTime lastChanged) {
        this.lastChanged = lastChanged;
    }

    public void setLastConfirmed(DateTime lastConfirmed) {
        this.lastConfirmed = lastConfirmed;
    }

    public String getName() {
        return name;
    }

    public String getOrganizationNumber() {
        return organizationNumber;
    }

    public String getType() {
        return type;
    }

    public DateTime getLastChanged() {
        return lastChanged;
    }

    public DateTime getLastConfirmed() {
        return lastConfirmed;
    }
}
