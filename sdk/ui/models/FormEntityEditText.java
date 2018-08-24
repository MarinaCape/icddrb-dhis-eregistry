package org.icddrb.dhis.client.sdk.ui.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import org.icddrb.dhis.client.sdk.ui.models.FormEntity.Type;
import org.icddrb.dhis.client.sdk.utils.Preconditions;

public class FormEntityEditText extends FormEntityCharSequence {
    private final String hint;
    private final InputType inputType;
    private boolean isLocked;

    public enum InputType {
        TEXT,
        LONG_TEXT,
        NUMBER,
        INTEGER,
        INTEGER_NEGATIVE,
        INTEGER_ZERO_OR_POSITIVE,
        INTEGER_POSITIVE,
        URL
    }

    public FormEntityEditText(String id, String label, String hint, InputType inputType, Object tag) {
        super(id, label, tag);
        this.hint = hint;
        this.inputType = (InputType) Preconditions.isNull(inputType, "inputType must not be null");
    }

    public FormEntityEditText(String id, String label, InputType inputType) {
        this(id, label, null, inputType, null);
    }

    public FormEntityEditText(String id, String label, InputType inputType, Object tag) {
        this(id, label, null, inputType, tag);
    }

    @NonNull
    public Type getType() {
        return Type.EDITTEXT;
    }

    @Nullable
    public String getHint() {
        return this.hint;
    }

    @NonNull
    public InputType getInputType() {
        return this.inputType;
    }

    public boolean isLocked() {
        return this.isLocked;
    }

    public void setLocked(boolean locked) {
        this.isLocked = locked;
    }
}
