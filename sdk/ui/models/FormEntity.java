package org.icddrb.dhis.client.sdk.ui.models;

import android.support.annotation.NonNull;
import org.icddrb.dhis.client.sdk.utils.Preconditions;

public abstract class FormEntity {
    private final String id;
    private final String label;
    private final Object tag;

    public enum Type {
        EDITTEXT,
        CHECKBOX,
        COORDINATES,
        RADIO_BUTTONS,
        DATE,
        FILTER,
        TEXT
    }

    @NonNull
    public abstract Type getType();

    public FormEntity(String id, String label) {
        this(id, label, null);
    }

    public FormEntity(String id, String label, Object tag) {
        this.id = (String) Preconditions.isNull(id, "id must not be null");
        this.label = (String) Preconditions.isNull(label, "label must not be null");
        this.tag = tag;
    }

    public String getId() {
        return this.id;
    }

    public String getLabel() {
        return this.label;
    }

    public Object getTag() {
        return this.tag;
    }
}
