package org.icddrb.dhis.client.sdk.ui.models;

import android.support.annotation.NonNull;
import org.icddrb.dhis.client.sdk.ui.models.FormEntity.Type;

public class FormEntityRadioButtons extends FormEntityCharSequence {
    public FormEntityRadioButtons(String id, String label) {
        this(id, label, null);
    }

    public FormEntityRadioButtons(String id, String label, Object tag) {
        super(id, label, tag);
    }

    @NonNull
    public Type getType() {
        return Type.RADIO_BUTTONS;
    }
}
