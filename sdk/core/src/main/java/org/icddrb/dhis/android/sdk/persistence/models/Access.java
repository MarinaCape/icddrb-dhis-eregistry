package org.icddrb.dhis.android.sdk.persistence.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.icddrb.dhis.android.sdk.utils.StringUtils;
import org.icddrb.dhis.android.sdk.utils.support.expression.Expression;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class Access {
    @JsonProperty("delete")
    boolean delete;
    @JsonProperty("externalize")
    boolean externalize;
    @JsonProperty("manage")
    boolean manage;
    @JsonProperty("read")
    boolean read;
    @JsonProperty("update")
    boolean update;
    @JsonProperty("write")
    boolean write;

    public Access(Access access) {
        this.manage = access.manage;
        this.externalize = access.externalize;
        this.write = access.write;
        this.read = access.read;
        this.update = access.update;
        this.delete = access.delete;
    }

    static Access provideDefaultAccess() {
        Access access = new Access();
        access.setManage(true);
        access.setExternalize(true);
        access.setWrite(true);
        access.setUpdate(true);
        access.setRead(true);
        access.setDelete(true);
        return access;
    }

    @JsonIgnore
    public boolean isDelete() {
        return this.delete;
    }

    @JsonIgnore
    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    @JsonIgnore
    public boolean isExternalize() {
        return this.externalize;
    }

    @JsonIgnore
    public void setExternalize(boolean externalize) {
        this.externalize = externalize;
    }

    @JsonIgnore
    public boolean isManage() {
        return this.manage;
    }

    @JsonIgnore
    public void setManage(boolean manage) {
        this.manage = manage;
    }

    @JsonIgnore
    public boolean isRead() {
        return this.read;
    }

    @JsonIgnore
    public void setRead(boolean read) {
        this.read = read;
    }

    @JsonIgnore
    public boolean isUpdate() {
        return this.update;
    }

    @JsonIgnore
    public void setUpdate(boolean update) {
        this.update = update;
    }

    @JsonIgnore
    public boolean isWrite() {
        return this.write;
    }

    @JsonIgnore
    public void setWrite(boolean write) {
        this.write = write;
    }

    @JsonIgnore
    public String toString() {
        return StringUtils.create().append("Access {").append("manage=").append(Boolean.valueOf(this.manage)).append(", externalize=").append(Boolean.valueOf(this.externalize)).append(", write=").append(Boolean.valueOf(this.write)).append(", read=").append(Boolean.valueOf(this.read)).append(", update=").append(Boolean.valueOf(this.update)).append(", delete=").append(Boolean.valueOf(this.delete)).append(Expression.EXP_CLOSE).build();
    }
}
