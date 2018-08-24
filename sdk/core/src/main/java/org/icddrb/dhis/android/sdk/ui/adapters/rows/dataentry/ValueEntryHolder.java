package org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry;

import android.widget.EditText;
import android.widget.TextView;

public class ValueEntryHolder {
    final EditText editText;
    final TextView errorLabel;
    final OnTextChangeListener listener;
    final TextView mandatoryIndicator;
    final TextView textLabel;
    final TextView warningLabel;

    public ValueEntryHolder(TextView textLabel, TextView mandatoryIndicator, TextView warningLabel, TextView errorLabel, EditText editText, OnTextChangeListener listener) {
        this.textLabel = textLabel;
        this.mandatoryIndicator = mandatoryIndicator;
        this.warningLabel = warningLabel;
        this.errorLabel = errorLabel;
        this.editText = editText;
        this.listener = listener;
    }
}
