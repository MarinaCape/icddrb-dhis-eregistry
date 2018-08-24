package org.icddrb.dhis.client.sdk.ui.views;

import android.text.Editable;
import android.text.TextWatcher;

public abstract class AbsTextWatcher implements TextWatcher {
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    public void afterTextChanged(Editable s) {
    }
}
