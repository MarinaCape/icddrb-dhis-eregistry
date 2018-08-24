package org.icddrb.dhis.android.sdk.persistence.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseMetaDataObject extends BaseIdentifiableObject {
    @JsonProperty("id")
    String id;

    public String getUid() {
        return this.id;
    }

    public void setUid(String id) {
        this.id = id;
    }
}
