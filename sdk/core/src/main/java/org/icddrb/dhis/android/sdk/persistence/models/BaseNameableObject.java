package org.icddrb.dhis.android.sdk.persistence.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseNameableObject extends BaseMetaDataObject {
    @JsonProperty("description")
    String description;
    @JsonProperty("shortName")
    String shortName;

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortName() {
        return this.shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
