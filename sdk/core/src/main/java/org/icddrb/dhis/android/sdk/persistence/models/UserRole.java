package org.icddrb.dhis.android.sdk.persistence.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
class UserRole {
    @JsonProperty("displayName")
    String displayName;
    @JsonProperty("id")
    String id;
    @JsonProperty("programs")
    List<Program> programs;

    public String getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public List<Program> getPrograms() {
        return this.programs;
    }

    public void setPrograms(List<Program> programs) {
        this.programs = programs;
    }
}
