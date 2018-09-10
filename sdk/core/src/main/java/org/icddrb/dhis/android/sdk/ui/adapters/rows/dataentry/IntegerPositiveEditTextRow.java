package org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry;

import android.support.v4.app.FragmentManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import org.icddrb.dhis.android.sdk.R;
import org.icddrb.dhis.android.sdk.persistence.models.BaseValue;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.autocompleterow.TextRow;

public class IntegerPositiveEditTextRow extends TextRow {
    private static String rowTypeTemp;

    public class MinMaxInputFilter implements InputFilter {
        private Integer maxAllowed;
        private Integer minAllowed;

        public MinMaxInputFilter(Integer min) {
            this.minAllowed = min;
        }

        public MinMaxInputFilter(IntegerPositiveEditTextRow this$0, Integer min, Integer max) {
            this(min);
            this.maxAllowed = max;
        }

        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                String newVal = dest.toString().substring(0, dstart) + dest.toString().substring(dend, dest.toString().length());
                newVal = newVal.substring(0, dstart) + source.toString() + newVal.substring(dstart, newVal.length());
                if (newVal.length() > 1 && newVal.startsWith("0")) {
                    return "";
                }
                if (inRange(Integer.valueOf(Integer.parseInt(newVal)))) {
                    return null;
                }
                return "";
            } catch (NumberFormatException e) {
            }
        }

        public boolean inRange(Integer value) {
            boolean isMinOk = true;
            boolean isMaxOk = true;
            if (this.minAllowed == null && this.maxAllowed == null) {
                return true;
            }
            if (this.minAllowed != null) {
                if (value == null) {
                    isMinOk = false;
                } else {
                    isMinOk = this.minAllowed.intValue() <= value.intValue();
                }
            }
            if (this.maxAllowed != null) {
                if (value == null) {
                    isMaxOk = false;
                } else {
                    isMaxOk = value.intValue() <= this.maxAllowed.intValue();
                }
            }
            if (isMinOk && isMaxOk) {
                return true;
            }
            return false;
        }
    }

    private static class PosFilter implements InputFilter {
        private PosFilter() {
        }

        public CharSequence filter(CharSequence str, int start, int end, Spanned spn, int spnStart, int spnEnd) {
            if (str.length() > 0 && spnStart == 0 && str.charAt(0) == '0') {
                return "";
            }
            return str;
        }
    }

    public IntegerPositiveEditTextRow(String label, boolean mandatory, String warning, BaseValue baseValue, DataEntryRowTypes rowType) {
        this.mLabel = label;
        this.mMandatory = mandatory;
        this.mWarning = warning;
        this.mValue = baseValue;
        this.mRowType = rowType;
        if (DataEntryRowTypes.INTEGER_POSITIVE.equals(rowType)) {
            checkNeedsForDescriptionButton();
            return;
        }
        throw new IllegalArgumentException("Unsupported row type");
    }

    public int getViewType() {
        return DataEntryRowTypes.INTEGER_POSITIVE.ordinal();
    }

    public View getView(FragmentManager fragmentManager, LayoutInflater inflater, View convertView, ViewGroup container) {
        ValueEntryHolder holder;
        View view;
        if (convertView == null || !(convertView.getTag() instanceof ValueEntryHolder)) {
            View root = inflater.inflate(R.layout.listview_row_edit_text, container, false);
            TextView label = (TextView) root.findViewById(R.id.text_label);
            TextView mandatoryIndicator = (TextView) root.findViewById(R.id.mandatory_indicator);
            TextView warningLabel = (TextView) root.findViewById(R.id.warning_label);
            TextView errorLabel = (TextView) root.findViewById(R.id.error_label);
            EditText editText = (EditText) root.findViewById(R.id.edit_text_row);
            editText.setInputType(2);
            editText.setHint(R.string.enter_positive_integer);
            editText.setFilters(new InputFilter[]{new PosFilter()});
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
