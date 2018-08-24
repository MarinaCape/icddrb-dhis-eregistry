package org.icddrb.dhis.android.sdk.persistence.models;

import java.io.Serializable;

public class UnionFWADropDownItem implements Serializable {
    private String mAlternateId;
    private String mId;
    private String mLabel;

    public UnionFWADropDownItem(String id, String label, String id2) {
        this.mId = id;
        this.mLabel = label;
        this.mAlternateId = id2;
    }

    public String getId() {
        return this.mId;
    }

    public String getLabel() {
        return this.mLabel;
    }

    public String getAlternateId() {
        return this.mAlternateId;
    }
}
