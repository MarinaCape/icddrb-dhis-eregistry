package org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry;

import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import org.icddrb.dhis.android.sdk.R;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.models.BaseValue;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.RowValueChangedEvent;

public class RadioButtonsRow extends Row {
    private static final String EMPTY_FIELD = "";
    private static final String FALSE = "false";
    public static final String FEMALE = "gender_female";
    public static final String MALE = "gender_male";
    public static final String OTHER = "gender_other";
    private static final String TRUE = "true";

    private static class BooleanRowHolder {
        final TextView errorLabel;
        final CompoundButton firstButton;
        final TextView mandatoryIndicator;
        final RadioGroup radioGroup;
        final OnCheckedChangeListener radioGroupCheckedChangeListener;
        final CompoundButton secondButton;
        final TextView textLabel;
        final DataEntryRowTypes type;
        final TextView warningLabel;

        public BooleanRowHolder(DataEntryRowTypes type, TextView textLabel, TextView mandatoryIndicator, TextView warningLabel, TextView errorLabel, CompoundButton firstButton, CompoundButton secondButton, RadioGroup radioGroup, OnCheckedChangeListener radioGroupCheckedChangeListener) {
            this.type = type;
            this.textLabel = textLabel;
            this.mandatoryIndicator = mandatoryIndicator;
            this.warningLabel = warningLabel;
            this.errorLabel = errorLabel;
            this.firstButton = firstButton;
            this.secondButton = secondButton;
            this.radioGroup = radioGroup;
            this.radioGroupCheckedChangeListener = radioGroupCheckedChangeListener;
        }

        public void updateViews(String label, BaseValue baseValue) {
            this.textLabel.setText(label);
            this.radioGroupCheckedChangeListener.setBaseValue(baseValue);
            String value = baseValue.getValue();
            if (DataEntryRowTypes.BOOLEAN.equals(this.type)) {
                if ("true".equalsIgnoreCase(value)) {
                    this.firstButton.setChecked(true);
                } else if ("false".equalsIgnoreCase(value)) {
                    this.secondButton.setChecked(true);
                }
            } else if (!DataEntryRowTypes.GENDER.equals(this.type)) {
            } else {
                if (RadioButtonsRow.MALE.equalsIgnoreCase(value)) {
                    this.firstButton.setChecked(true);
                } else if (RadioButtonsRow.FEMALE.equalsIgnoreCase(value)) {
                    this.secondButton.setChecked(true);
                }
            }
        }
    }

    private static class OnCheckedChangeListener implements android.widget.RadioGroup.OnCheckedChangeListener {
        BaseValue baseValue;

        private OnCheckedChangeListener() {
        }

        public BaseValue getBaseValue() {
            return this.baseValue;
        }

        public void setBaseValue(BaseValue baseValue) {
            this.baseValue = baseValue;
        }

        public void onCheckedChanged(RadioGroup group, int checkedId) {
            String newValue;
            if (checkedId == R.id.first_radio_button) {
                newValue = "true";
            } else if (checkedId == R.id.second_radio_button) {
                newValue = "false";
            } else {
                newValue = "";
            }
            if (!newValue.equals(this.baseValue.getValue())) {
                this.baseValue.setValue(newValue);
                Dhis2Application.getEventBus().post(new RowValueChangedEvent(this.baseValue, DataEntryRowTypes.BOOLEAN.toString()));
            }
            this.baseValue.setValue(this.baseValue.getValue());
        }
    }

    public RadioButtonsRow(String label, boolean mandatory, String warning, BaseValue baseValue, DataEntryRowTypes type) {
        if (DataEntryRowTypes.GENDER.equals(type) || DataEntryRowTypes.BOOLEAN.equals(type)) {
            this.mLabel = label;
            this.mMandatory = mandatory;
            this.mValue = baseValue;
            this.mRowType = type;
            this.mWarning = warning;
            checkNeedsForDescriptionButton();
            return;
        }
        throw new IllegalArgumentException("Unsupported row type");
    }

    public View getView(FragmentManager fragmentManager, LayoutInflater inflater, View convertView, ViewGroup container) {
        BooleanRowHolder holder;
        View view;
        if (convertView == null || !(convertView.getTag() instanceof BooleanRowHolder)) {
            View root = inflater.inflate(R.layout.listview_row_radio_buttons, container, false);
            TextView label = (TextView) root.findViewById(R.id.text_label);
            TextView mandatoryIndicator = (TextView) root.findViewById(R.id.mandatory_indicator);
            TextView warningLabel = (TextView) root.findViewById(R.id.warning_label);
            TextView errorLabel = (TextView) root.findViewById(R.id.error_label);
            CompoundButton firstButton = (CompoundButton) root.findViewById(R.id.first_radio_button);
            CompoundButton secondButton = (CompoundButton) root.findViewById(R.id.second_radio_button);
            RadioGroup radioGroup = (RadioGroup) root.findViewById(R.id.radio_group_row_radio_buttons);
            if (DataEntryRowTypes.BOOLEAN.equals(this.mRowType)) {
                firstButton.setText(R.string.yes);
                secondButton.setText(R.string.no);
            } else if (DataEntryRowTypes.GENDER.equals(this.mRowType)) {
                firstButton.setText(R.string.gender_male);
                secondButton.setText(R.string.gender_female);
            }
            OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener();
            holder = new BooleanRowHolder(this.mRowType, label, mandatoryIndicator, warningLabel, errorLabel, firstButton, secondButton, radioGroup, onCheckedChangeListener);
            holder.radioGroup.setOnCheckedChangeListener(onCheckedChangeListener);
            root.setTag(holder);
            view = root;
        } else {
            view = convertView;
            holder = (BooleanRowHolder) convertView.getTag();
            android.widget.RadioGroup.OnCheckedChangeListener listener = holder.radioGroupCheckedChangeListener;
            holder.radioGroup.setOnCheckedChangeListener(null);
            holder.radioGroup.clearCheck();
            holder.radioGroup.setOnCheckedChangeListener(listener);
            holder.updateViews(this.mLabel, this.mValue);
        }
        if (isEditable()) {
            holder.firstButton.setEnabled(true);
            holder.secondButton.setEnabled(true);
        } else {
            holder.firstButton.setEnabled(false);
            holder.secondButton.setEnabled(false);
        }
        holder.updateViews(this.mLabel, this.mValue);
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
        return this.mRowType.ordinal();
    }
}
