package org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry;

import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import org.icddrb.dhis.android.sdk.C0845R;
import org.icddrb.dhis.android.sdk.persistence.models.BaseValue;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.autocompleterow.TextRow;

public class URLEditTextRow extends TextRow {
    private static String rowTypeTemp;

    private class URLWatcher extends OnTextChangeListener {
        private final EditText mEditText;
        private final TextView mErrorLabel;

        public URLWatcher(EditText editText, TextView errorLabel) {
            this.mEditText = editText;
            this.mErrorLabel = errorLabel;
        }

        public void afterTextChanged(Editable s) {
            super.afterTextChanged(s);
            validateURL(this.mEditText.getText().toString());
        }

        public void validateURL(String url) {
            if (url.matches("^(http|https)://[a-z0-9]+([-.][a-z0-9]+)*[.][a-z]{2,6}(:[0-9]{1,5})?([/].*)?$") || url.length() <= 0) {
                setError(null);
            } else {
                setError(Integer.valueOf(C0845R.string.error_url));
            }
        }

        private void setError(Integer stringId) {
            if (stringId == null) {
                this.mErrorLabel.setVisibility(8);
                this.mErrorLabel.setText("");
            } else {
                this.mErrorLabel.setVisibility(0);
                this.mErrorLabel.setText(stringId.intValue());
            }
            URLEditTextRow.this.mErrorStringId = stringId;
        }
    }

    public URLEditTextRow(String label, boolean mandatory, String warning, BaseValue baseValue, DataEntryRowTypes rowType) {
        this.mLabel = label;
        this.mMandatory = mandatory;
        this.mWarning = warning;
        this.mValue = baseValue;
        this.mRowType = rowType;
        if (DataEntryRowTypes.URL.equals(rowType)) {
            checkNeedsForDescriptionButton();
            return;
        }
        throw new IllegalArgumentException("Unsupported row type");
    }

    public int getViewType() {
        return DataEntryRowTypes.URL.ordinal();
    }

    public View getView(FragmentManager fragmentManager, LayoutInflater inflater, View convertView, ViewGroup container) {
        ValueEntryHolder holder;
        View view;
        if (convertView == null || !(convertView.getTag() instanceof ValueEntryHolder)) {
            View root = inflater.inflate(C0845R.layout.listview_row_edit_text, container, false);
            TextView label = (TextView) root.findViewById(C0845R.id.text_label);
            TextView mandatoryIndicator = (TextView) root.findViewById(C0845R.id.mandatory_indicator);
            TextView warningLabel = (TextView) root.findViewById(C0845R.id.warning_label);
            TextView errorLabel = (TextView) root.findViewById(C0845R.id.error_label);
            EditText editText = (EditText) root.findViewById(C0845R.id.edit_text_row);
            editText.setInputType(16);
            editText.setHint(C0845R.string.enter_url);
            editText.setSingleLine(true);
            URLWatcher listener = new URLWatcher(editText, errorLabel);
            listener.setRow(this);
            listener.setRowType(rowTypeTemp);
            holder = new ValueEntryHolder(label, mandatoryIndicator, warningLabel, errorLabel, editText, listener);
            holder.listener.setBaseValue(this.mValue);
            holder.editText.addTextChangedListener(listener);
            rowTypeTemp = this.mRowType.toString();
            root.setTag(holder);
            view = root;
        } else {
            view = convertView;
            holder = (ValueEntryHolder) view.getTag();
            holder.listener.onRowReused();
        }
        if (isEditable()) {
            holder.editText.setEnabled(true);
        } else {
            holder.editText.setEnabled(false);
        }
        holder.textLabel.setText(this.mLabel);
        holder.listener.setBaseValue(this.mValue);
        holder.editText.setText(this.mValue.getValue());
        holder.editText.setSelection(holder.editText.getText().length());
        if (this.mWarning == null) {
            holder.warningLabel.setVisibility(8);
        } else {
            holder.warningLabel.setVisibility(0);
            holder.warningLabel.setText(this.mWarning);
        }
        if (this.mErrorStringId == null) {
            holder.errorLabel.setVisibility(8);
        } else {
            holder.errorLabel.setVisibility(0);
            holder.errorLabel.setText(this.mErrorStringId.intValue());
        }
        if (this.mMandatory) {
            holder.mandatoryIndicator.setVisibility(0);
        } else {
            holder.mandatoryIndicator.setVisibility(8);
        }
        holder.editText.setOnEditorActionListener(this.mOnEditorActionListener);
        return view;
    }
}
