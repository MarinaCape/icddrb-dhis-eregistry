package org.icddrb.dhis.client.sdk.ui.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import org.icddrb.dhis.client.sdk.ui.models.FormEntity.Type;

public class FormEntityFilter extends FormEntity {
    @Nullable
    private OnFormEntityChangeListener onFormEntityChangeListener;
    @Nullable
    private Picker picker;

    public FormEntityFilter(String id, String label) {
        this(id, label, null);
    }

    public FormEntityFilter(String id, String label, Object tag) {
        super(id, label, tag);
    }

    @NonNull
    public Type getType() {
        return Type.FILTER;
    }

    @Nullable
    public Picker getPicker() {
        return this.picker;
    }

    public void setPicker(@Nullable Picker picker) {
        this.picker = picker;
        if (this.onFormEntityChangeListener != null && this.picker != null) {
            this.onFormEntityChangeListener.onFormEntityChanged(this);
        }
    }

    @Nullable
    public OnFormEntityChangeListener getOnFormEntityChangeListener() {
        return this.onFormEntityChangeListener;
    }

    public void setOnFormEntityChangeListener(@Nullable OnFormEntityChangeListener listener) {
        this.onFormEntityChangeListener = listener;
    }
}
