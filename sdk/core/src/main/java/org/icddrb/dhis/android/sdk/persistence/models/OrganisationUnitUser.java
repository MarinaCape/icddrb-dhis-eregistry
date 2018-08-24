package org.icddrb.dhis.android.sdk.persistence.models;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raizlabs.android.dbflow.structure.BaseModel;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrganisationUnitUser extends BaseModel {
    @JsonProperty("id")
    String id;
    @JsonProperty("displayName")
    String label;
    @JsonProperty("level")
    int level;
    String parent;
    @JsonProperty("programs")
    List<Program> programs;
    @JsonIgnore
    TYPE type;
    @JsonProperty("users")
    List<User> users;

    public enum TYPE {
        ASSIGNED,
        SEARCH
    }

    @JsonProperty("parent")
    public void unpackNameFromNestedObject(Map<String, String> p) {
        this.parent = (String) p.get("id");
    }

    @JsonAnySetter
    public void handleUnknown(String key, Object value) {
    }

    public List<User> getUsers() {
        return this.users;
    }

    public String getParent() {
        return this.parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<Program> getPrograms() {
        return this.programs;
    }

    public TYPE getType() {
        return this.type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public void setPrograms(List<Program> programs) {
        this.programs = programs;
    }
}
