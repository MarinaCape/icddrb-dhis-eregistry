package org.icddrb.dhis.client.sdk.ui.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public abstract class FormEntityCharSequence extends FormEntity {
    private static final String EMPTY_STRING = "";
    @Nullable
    private OnFormEntityChangeListener onFormEntityChangeListener;
    @NonNull
    private CharSequence value;

    public FormEntityCharSequence(String id, String label) {
        this(id, label, null);
    }

    public FormEntityCharSequence(String id, String label, Object tag) {
        super(id, label, tag);
        this.value = "";
    }

    @Nullable
    public OnFormEntityChangeListener getOnFormEntityChangeListener() {
        return this.onFormEntityChangeListener;
    }

    public void setOnFormEntityChangeListener(@Nullable OnFormEntityChangeListener listener) {
        this.onFormEntityChangeListener = listener;
    }

    @NonNull
    public CharSequence getValue() {
        return this.value;
    }

    public void setValue(@Nullable CharSequence value, boolean notifyListeners) {
        CharSequence newValue = value;
        if (newValue == null) {
            newValue = "";
        }
        if (!this.value.equals(newValue)) {
            this.value = newValue;
            if (this.onFormEntityChangeListener != null && notifyListeners) {
                this.onFormEntityChangeListener.onFormEntityChanged(this);
            }
        }
    }
}
