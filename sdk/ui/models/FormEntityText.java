package org.icddrb.dhis.client.sdk.ui.models;

import android.support.annotation.NonNull;
import org.icddrb.dhis.client.sdk.ui.models.FormEntity.Type;

public class FormEntityText extends FormEntityCharSequence {
    public FormEntityText(String id, String label) {
        this(id, label, null);
    }

    public FormEntityText(String id, String label, Object tag) {
        super(id, label, tag);
    }

    @NonNull
    public Type getType() {
        return Type.TEXT;
    }
}
