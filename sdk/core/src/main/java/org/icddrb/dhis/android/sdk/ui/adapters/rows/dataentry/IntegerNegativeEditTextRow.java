package org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.InputDeviceCompat;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import org.icddrb.dhis.android.sdk.C0845R;
import org.icddrb.dhis.android.sdk.persistence.models.BaseValue;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.autocompleterow.TextRow;

public class IntegerNegativeEditTextRow extends TextRow {
    private static String rowTypeTemp;

    private static class NegInpFilter implements InputFilter {
        private NegInpFilter() {
        }

        public CharSequence filter(CharSequence str, int start, int end, Spanned spn, int spnStart, int spnEnd) {
            if (str.length() <= 0 || spnStart != 0 || str.charAt(0) == '-') {
                return str;
            }
            return "";
        }
    }

    public IntegerNegativeEditTextRow(String label, boolean mandatory, String warning, BaseValue baseValue, DataEntryRowTypes rowType) {
        this.mLabel = label;
        this.mMandatory = mandatory;
        this.mWarning = warning;
        this.mValue = baseValue;
        this.mRowType = rowType;
        if (DataEntryRowTypes.INTEGER_NEGATIVE.equals(rowType)) {
            checkNeedsForDescriptionButton();
            return;
        }
        throw new IllegalArgumentException("Unsupported row type");
    }

    public int getViewType() {
        return DataEntryRowTypes.INTEGER_NEGATIVE.ordinal();
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
            editText.setInputType(InputDeviceCompat.SOURCE_TOUCHSCREEN);
            editText.setHint(C0845R.string.enter_negative_integer);
            editText.setFilters(new InputFilter[]{new NegInpFilter()});
            editText.setSingleLine(true);
            OnTextChangeListener listener = new OnTextChangeListener();
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
        if (this.mError == null) {
            holder.errorLabel.setVisibility(8);
        } else {
            holder.errorLabel.setVisibility(0);
            holder.errorLabel.setText(this.mError);
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
