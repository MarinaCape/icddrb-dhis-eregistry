package org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.RadioGroup;
import android.widget.TextView;
import java.util.List;
import org.icddrb.dhis.android.sdk.C0845R;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.models.BaseValue;
import org.icddrb.dhis.android.sdk.persistence.models.Option;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.RowValueChangedEvent;
import org.icddrb.dhis.android.sdk.ui.views.FontRadioButton;

public class RadioButtonsOptionSetRow extends Row {
    private static int BASE_ID = 1000;
    private static final String EMPTY_FIELD = "";
    private final List<Option> mOptions;

    private static class OnCheckedChangeListener implements android.widget.RadioGroup.OnCheckedChangeListener {
        BaseValue baseValue;

        private OnCheckedChangeListener() {
        }

        public void setBaseValue(BaseValue baseValue) {
            this.baseValue = baseValue;
        }

        public void onCheckedChanged(RadioGroup group, int checkedId) {
            String newValue;
            FontRadioButton fontRadioButton = (FontRadioButton) group.findViewById(checkedId);
            if (fontRadioButton != null) {
                newValue = ((Option) fontRadioButton.getTag()).getCode();
            } else {
                newValue = "";
            }
            if (!newValue.equals(this.baseValue.getValue())) {
                this.baseValue.setValue(newValue);
                Dhis2Application.getEventBus().post(new RowValueChangedEvent(this.baseValue, DataEntryRowTypes.OPTION_SET.toString()));
            }
        }
    }

    private static class RadioGroupRowHolder {
        final TextView errorLabel;
        final TextView mandatoryIndicator;
        final RadioGroup radioGroup;
        final OnCheckedChangeListener radioGroupCheckedChangeListener;
        final TextView textLabel;
        final DataEntryRowTypes type;
        final TextView warningLabel;

        public RadioGroupRowHolder(DataEntryRowTypes type, TextView textLabel, TextView mandatoryIndicator, TextView warningLabel, TextView errorLabel, RadioGroup radioGroup, OnCheckedChangeListener radioGroupCheckedChangeListener) {
            this.type = type;
            this.textLabel = textLabel;
            this.mandatoryIndicator = mandatoryIndicator;
            this.warningLabel = warningLabel;
            this.errorLabel = errorLabel;
            this.radioGroup = radioGroup;
            this.radioGroupCheckedChangeListener = radioGroupCheckedChangeListener;
        }

        public void updateViews(String label, BaseValue baseValue, List<Option> options, boolean isEditable) {
            this.textLabel.setText(label);
            this.radioGroupCheckedChangeListener.setBaseValue(baseValue);
            this.radioGroup.setOnCheckedChangeListener(null);
            this.radioGroup.clearCheck();
            this.radioGroup.removeAllViews();
            Context context = this.radioGroup.getContext();
            for (int i = 0; i < options.size(); i++) {
                Option option = (Option) options.get(i);
                FontRadioButton fontRadioButton = new FontRadioButton(context);
                fontRadioButton.setTextSize(0, context.getResources().getDimension(C0845R.dimen.medium_text_size));
                fontRadioButton.setFont(context.getResources().getString(C0845R.string.regular_font_name));
                fontRadioButton.setEnabled(isEditable);
                this.radioGroup.addView(fontRadioButton, new LayoutParams(-1, -2));
                fontRadioButton.setText(option.getDisplayName());
                fontRadioButton.setTag(option);
                fontRadioButton.setId(RadioButtonsOptionSetRow.BASE_ID + i);
                String value = baseValue.getValue();
                if (value != null && value.equals(option.getCode())) {
                    fontRadioButton.setChecked(true);
                }
            }
            this.radioGroup.setOnCheckedChangeListener(this.radioGroupCheckedChangeListener);
        }
    }

    public RadioButtonsOptionSetRow(String label, boolean mandatory, String warning, BaseValue baseValue, List<Option> options) {
        this.mLabel = label;
        this.mMandatory = mandatory;
        this.mValue = baseValue;
        this.mWarning = warning;
        this.mOptions = options;
        checkNeedsForDescriptionButton();
    }

    public View getView(FragmentManager fragmentManager, LayoutInflater inflater, View convertView, ViewGroup container) {
        RadioGroupRowHolder holder;
        View view;
        if (convertView == null || !(convertView.getTag() instanceof RadioGroupRowHolder)) {
            View root = inflater.inflate(C0845R.layout.listview_row_radio_buttons_option_set, container, false);
            holder = new RadioGroupRowHolder(this.mRowType, (TextView) root.findViewById(C0845R.id.text_label), (TextView) root.findViewById(C0845R.id.mandatory_indicator), (TextView) root.findViewById(C0845R.id.warning_label), (TextView) root.findViewById(C0845R.id.error_label), (RadioGroup) root.findViewById(C0845R.id.radio_group_row_option_set), new OnCheckedChangeListener());
            root.setTag(holder);
            view = root;
        } else {
            view = convertView;
            holder = (RadioGroupRowHolder) convertView.getTag();
        }
        holder.updateViews(this.mLabel, this.mValue, this.mOptions, isEditable());
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
        return DataEntryRowTypes.OPTION_SET.ordinal();
    }
}
