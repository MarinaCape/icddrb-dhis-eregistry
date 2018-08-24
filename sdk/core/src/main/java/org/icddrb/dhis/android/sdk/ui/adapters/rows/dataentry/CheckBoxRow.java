package org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry;

import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import org.icddrb.dhis.android.sdk.C0845R;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.models.BaseValue;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.RowValueChangedEvent;

public class CheckBoxRow extends Row {
    private static final String EMPTY_FIELD = "";
    private static final String TRUE = "true";
    private final String mLabel;

    private static class CheckBoxHolder {
        final CheckBox checkBox;
        final TextView errorLabel;
        final CheckBoxListener listener;
        final TextView mandatoryIndicator;
        final OnCheckBoxRowClickListener onCheckBoxRowClickListener;
        final View rootView;
        final TextView textLabel;
        final TextView warningLabel;

        public CheckBoxHolder(View rootView, TextView textLabel, TextView mandatoryIndicator, TextView warningLabel, TextView errorLabel, CheckBox checkBox, CheckBoxListener listener, OnCheckBoxRowClickListener onCheckBoxRowClickListener) {
            this.rootView = rootView;
            this.textLabel = textLabel;
            this.mandatoryIndicator = mandatoryIndicator;
            this.warningLabel = warningLabel;
            this.errorLabel = errorLabel;
            this.checkBox = checkBox;
            this.listener = listener;
            this.onCheckBoxRowClickListener = onCheckBoxRowClickListener;
        }
    }

    private static class CheckBoxListener implements OnCheckedChangeListener {
        private BaseValue value;

        private CheckBoxListener() {
        }

        public void setValue(BaseValue value) {
            this.value = value;
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            String newValue;
            if (isChecked) {
                newValue = "true";
            } else {
                newValue = "";
            }
            if (!newValue.toString().equals(this.value.getValue())) {
                this.value.setValue(newValue);
                Dhis2Application.getEventBus().post(new RowValueChangedEvent(this.value, DataEntryRowTypes.TRUE_ONLY.toString()));
            }
        }
    }

    private static class OnCheckBoxRowClickListener implements OnClickListener {
        CheckBox checkBox;
        boolean editable;

        private OnCheckBoxRowClickListener() {
        }

        public void setEditable(boolean editable) {
            this.editable = editable;
        }

        public void setCheckBox(CheckBox checkBox) {
            this.checkBox = checkBox;
        }

        public void onClick(View view) {
            if (this.editable) {
                this.checkBox.setChecked(!this.checkBox.isChecked());
            }
        }
    }

    public CheckBoxRow(String label, boolean mandatory, String warning, BaseValue mValue) {
        this.mLabel = label;
        this.mValue = mValue;
        this.mWarning = warning;
        this.mMandatory = mandatory;
        checkNeedsForDescriptionButton();
    }

    public View getView(FragmentManager fragmentManager, LayoutInflater inflater, View convertView, ViewGroup container) {
        CheckBoxHolder holder;
        View view;
        if (convertView == null || !(convertView.getTag() instanceof CheckBoxHolder)) {
            View root = inflater.inflate(C0845R.layout.listview_row_checkbox, container, false);
            holder = new CheckBoxHolder(root, (TextView) root.findViewById(C0845R.id.text_label), (TextView) root.findViewById(C0845R.id.mandatory_indicator), (TextView) root.findViewById(C0845R.id.warning_label), (TextView) root.findViewById(C0845R.id.error_label), (CheckBox) root.findViewById(C0845R.id.checkbox), new CheckBoxListener(), new OnCheckBoxRowClickListener());
            holder.checkBox.setOnCheckedChangeListener(holder.listener);
            holder.rootView.setOnClickListener(holder.onCheckBoxRowClickListener);
            root.setTag(holder);
            view = root;
        } else {
            view = convertView;
            holder = (CheckBoxHolder) view.getTag();
        }
        if (isEditable()) {
            holder.textLabel.setEnabled(true);
            holder.checkBox.setEnabled(true);
        } else {
            holder.checkBox.setEnabled(false);
            holder.textLabel.setEnabled(false);
        }
        holder.textLabel.setText(this.mLabel);
        holder.listener.setValue(this.mValue);
        holder.onCheckBoxRowClickListener.setCheckBox(holder.checkBox);
        holder.onCheckBoxRowClickListener.setEditable(holder.checkBox.isEnabled());
        String stringValue = this.mValue.getValue();
        if ("true".equalsIgnoreCase(stringValue)) {
            holder.checkBox.setChecked(true);
        } else if (TextUtils.isEmpty(stringValue)) {
            holder.checkBox.setChecked(false);
        }
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
        return view;
    }

    public int getViewType() {
        return DataEntryRowTypes.TRUE_ONLY.ordinal();
    }
}
