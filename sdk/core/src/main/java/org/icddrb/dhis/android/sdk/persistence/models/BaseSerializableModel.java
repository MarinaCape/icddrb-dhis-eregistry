package org.icddrb.dhis.android.sdk.persistence.models;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties({"modelAdapter"})
public abstract class BaseSerializableModel extends BaseIdentifiableObject {
    @JsonIgnore
    boolean fromServer = true;
    @JsonProperty("id")
    String id;
    @JsonIgnore
    long localId = -1;

    protected BaseSerializableModel() {
    }

    @JsonAnySetter
    public void handleUnknown(String key, Object value) {
    }

    public BaseSerializableModel(BaseSerializableModel baseSerializableModel) {
        super(baseSerializableModel);
        this.id = baseSerializableModel.id;
        this.fromServer = baseSerializableModel.fromServer;
        this.localId = baseSerializableModel.localId;
    }

    public boolean isFromServer() {
        return this.fromServer;
    }

    public void setFromServer(boolean fromServer) {
        this.fromServer = fromServer;
    }

    public long getLocalId() {
        return this.localId;
    }

    public void setLocalId(long localId) {
        this.localId = localId;
    }

    public String getUid() {
        return this.id;
    }

    public void setUid(String id) {
        this.id = id;
    }
}
