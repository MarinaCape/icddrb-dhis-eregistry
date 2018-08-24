package org.icddrb.dhis.android.sdk.persistence.models;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Header {
    @JsonProperty("column")
    private String column;
    @JsonProperty("hidden")
    private boolean hidden;
    @JsonProperty("meta")
    private boolean meta;
    @JsonProperty("name")
    private String name;
    @JsonProperty("type")
    private String type;

    @JsonAnySetter
    public void handleUnknown(String key, Object value) {
    }

    public boolean getMeta() {
        return this.meta;
    }

    public boolean getHidden() {
        return this.hidden;
    }

    public String getType() {
        return this.type;
    }

    public String getColumn() {
        return this.column;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMeta() {
        return this.meta;
    }

    public void setMeta(boolean meta) {
        this.meta = meta;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setColumn(String column) {
        this.column = column;
    }
}
