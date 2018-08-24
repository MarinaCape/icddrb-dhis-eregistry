package org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.InputDeviceCompat;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import org.icddrb.dhis.android.sdk.C0845R;
import org.icddrb.dhis.android.sdk.persistence.models.BaseValue;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.autocompleterow.TextRow;

public class PercentageEditTextRow extends TextRow {
    private static String rowTypeTemp;

    public class MinMaxInputFilter implements InputFilter {
        private Integer maxAllowed;
        private Integer minAllowed;

        public MinMaxInputFilter(Integer min) {
            this.minAllowed = min;
        }

        public MinMaxInputFilter(PercentageEditTextRow this$0, Integer min, Integer max) {
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

    public PercentageEditTextRow(String label, boolean mandatory, String warning, BaseValue baseValue, DataEntryRowTypes rowType) {
        this.mLabel = label;
        this.mMandatory = mandatory;
        this.mWarning = warning;
        this.mValue = baseValue;
        this.mRowType = rowType;
        if (DataEntryRowTypes.PERCENTAGE.equals(rowType)) {
            checkNeedsForDescriptionButton();
            return;
        }
        throw new IllegalArgumentException("Unsupported row type");
    }

    public int getViewType() {
        return DataEntryRowTypes.PERCENTAGE.ordinal();
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
            editText.setHint(C0845R.string.enter_percentage);
            editText.setFilters(new InputFilter[]{new LengthFilter(3), new MinMaxInputFilter(this, Integer.valueOf(0), Integer.valueOf(100))});
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
