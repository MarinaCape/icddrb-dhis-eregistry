package org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import org.icddrb.dhis.android.sdk.R;
import org.icddrb.dhis.android.sdk.persistence.models.BaseValue;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.autocompleterow.TextRow;
import org.icddrb.dhis.android.sdk.utils.support.expression.Expression;

public class NumberEditTextRow extends TextRow {
    private static String rowTypeTemp;

    private static class NumberFilter implements InputFilter {
        private NumberFilter() {
        }

        public CharSequence filter(CharSequence str, int start, int end, Spanned spn, int spnStart, int spnEnd) {
            if (ifStartsWithPointReturnEmpty(str, spnStart, spnEnd)) {
                return "";
            }
            CharSequence x = ifStartsWithZeroesReturnEmpty(str, spn);
            if (x == null) {
                return str;
            }
            return x;
        }

        @Nullable
        private CharSequence ifStartsWithZeroesReturnEmpty(CharSequence str, Spanned spn) {
            if (str.length() <= 0 || str.charAt(0) != '0' || spn.length() <= 0 || spn.charAt(0) != '0' || (spn.length() > 1 && spn.charAt(1) != '0')) {
                return null;
            }
            if (spn.length() <= 1 || !spn.toString().contains(Expression.SEPARATOR)) {
                return "";
            }
            return str;
        }

        private boolean ifStartsWithPointReturnEmpty(CharSequence str, int spnStart, int spnEnd) {
            if (str.length() > 0 && str.charAt(0) == '.' && spnStart == 0 && spnEnd == 1) {
                return true;
            }
            return false;
        }
    }

    private class OnNumberFocusChangeListener implements OnFocusChangeListener {
        private EditText mEditText;

        public OnNumberFocusChangeListener(EditText editText) {
            this.mEditText = editText;
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                String text = trimLeftZeroes(this.mEditText.getText().toString());
                if (this.mEditText.getText() != null && text.endsWith(Expression.SEPARATOR)) {
                    text = removeLastChar(text);
                } else if (text.contains(Expression.SEPARATOR)) {
                    text = fixDecimals(text);
                }
                setText(text);
            }
        }

        @NonNull
        private String fixDecimals(String text) {
            int pointPosition = text.indexOf(Expression.SEPARATOR);
            return removeIncorrectDecimals(text.substring(0, pointPosition + 1) + trimDecimalsRightZeroes(text.substring(pointPosition + 1, text.length())));
        }

        @NonNull
        private String removeIncorrectDecimals(String text) {
            if (text.endsWith(".0")) {
                return text.substring(0, text.indexOf(".0"));
            }
            return text;
        }

        @NonNull
        private String removeLastChar(String text) {
            return text.substring(0, text.length() - 1);
        }

        private void setText(String substring) {
            this.mEditText.getText().clear();
            this.mEditText.append(substring);
        }

        private String trimLeftZeroes(String text) {
            if (!text.startsWith("0") || text.length() <= 1) {
                return text;
            }
            if (!text.contains(Expression.SEPARATOR)) {
                return new Integer(text).toString();
            }
            return new Integer(text.substring(0, text.indexOf(Expression.SEPARATOR))).toString() + text.substring(text.indexOf(Expression.SEPARATOR), text.length());
        }

        private String trimDecimalsRightZeroes(String text) {
            if (!text.endsWith("0") || text.length() <= 1) {
                return text;
            }
            return trimDecimalsRightZeroes(removeLastChar(text));
        }
    }

    public NumberEditTextRow(String label, boolean mandatory, String warning, BaseValue baseValue, DataEntryRowTypes rowType) {
        this.mLabel = label;
        this.mMandatory = mandatory;
        this.mWarning = warning;
        this.mValue = baseValue;
        this.mRowType = rowType;
        if (DataEntryRowTypes.NUMBER.equals(rowType)) {
            checkNeedsForDescriptionButton();
            return;
        }
        throw new IllegalArgumentException("Unsupported row type");
    }

    public int getViewType() {
        return DataEntryRowTypes.NUMBER.ordinal();
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
            editText.setInputType(12290);
            editText.setHint(R.string.enter_number);
            editText.setFilters(new InputFilter[]{new NumberFilter()});
            editText.setOnFocusChangeListener(new OnNumberFocusChangeListener(editText));
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
