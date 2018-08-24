package org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.autocompleterow;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import org.icddrb.dhis.android.sdk.C0845R;

class AutoCompleteRowViewHolder {
    public final ImageButton clearButton;
    public final TextView errorLabel;
    public final TextView mandatoryIndicator;
    public final AutoCompleteOnClearButtonListener onClearButtonListener = new AutoCompleteOnClearButtonListener(this.valueTextView);
    public final AutoCompleteDropDownButtonListener onDropDownButtonListener = new AutoCompleteDropDownButtonListener();
    public final AutoCompleteOnOptionSelectedListener onOptionListener = new OnOptionItemSelectedListener(this.valueTextView);
    public final AutoCompleteOnTextChangedListener onTextChangedListener = new AutoCompleteOnTextChangedListener();
    public final TextView textView;
    public final TextView valueTextView;
    public final TextView warningLabel;

    AutoCompleteRowViewHolder(View view) {
        this.mandatoryIndicator = (TextView) view.findViewById(C0845R.id.mandatory_indicator);
        this.textView = (TextView) view.findViewById(C0845R.id.text_label);
        this.warningLabel = (TextView) view.findViewById(C0845R.id.warning_label);
        this.errorLabel = (TextView) view.findViewById(C0845R.id.error_label);
        this.valueTextView = (TextView) view.findViewById(C0845R.id.choose_option);
        this.clearButton = (ImageButton) view.findViewById(C0845R.id.clear_option_value);
        this.onDropDownButtonListener.setListener(this.onOptionListener);
        this.clearButton.setOnClickListener(this.onClearButtonListener);
        this.valueTextView.setOnClickListener(this.onDropDownButtonListener);
    }

    void setOnTextChangedListener() {
        this.valueTextView.addTextChangedListener(this.onTextChangedListener);
    }

    void clearOnTextChangedListener() {
        this.valueTextView.removeTextChangedListener(this.onTextChangedListener);
    }
}
