package org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.autocompleterow;

import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import org.icddrb.dhis.android.sdk.R;
import org.icddrb.dhis.android.sdk.persistence.models.BaseValue;
import org.icddrb.dhis.android.sdk.persistence.models.Option;
import org.icddrb.dhis.android.sdk.persistence.models.OptionSet;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.DataEntryRowTypes;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.Row;
import org.icddrb.dhis.android.sdk.utils.api.ValueType;

public final class AutoCompleteRow extends Row implements OptionNameCacher {
    static final String EMPTY_FIELD = "";
    private final String mOptionSetId;
    private final ValueType mOptionValueType;
    private String mSelectedOptionName;

    public AutoCompleteRow(String label, boolean mandatory, String warning, BaseValue value, OptionSet optionSet) {
        this.mLabel = label;
        this.mValue = value;
        this.mWarning = warning;
        this.mMandatory = mandatory;
        this.mOptionSetId = optionSet.getUid();
        this.mOptionValueType = optionSet.getValueType();
        cacheOptionName();
    }

    public View getView(FragmentManager fragmentManager, LayoutInflater inflater, View convertView, ViewGroup container) {
        View view;
        AutoCompleteRowViewHolder holder;
        if (convertView == null || !(convertView.getTag() instanceof AutoCompleteRowViewHolder)) {
            view = inflater.inflate(R.layout.listview_row_autocomplete, container, false);
            holder = new AutoCompleteRowViewHolder(view);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (AutoCompleteRowViewHolder) view.getTag();
            holder.clearOnTextChangedListener();
        }
        holder.textView.setText(this.mLabel);
        holder.onTextChangedListener.setBaseValue(this.mValue);
        holder.onTextChangedListener.setOptionSetId(this.mOptionSetId);
        holder.onTextChangedListener.setCachedOptionNameClearer(this);
        holder.onDropDownButtonListener.setOptionSetId(this.mOptionSetId);
        holder.onDropDownButtonListener.setFragmentManager(fragmentManager);
        holder.onDropDownButtonListener.getListener().setValue(this.mValue);
        holder.onClearButtonListener.setValue(this.mValue);
        holder.onClearButtonListener.setOptionNameCacher(this);
        holder.onOptionListener.setValueType(this.mOptionValueType);
        holder.valueTextView.setText(this.mSelectedOptionName);
        holder.setOnTextChangedListener();
        if (isEditable()) {
            holder.valueTextView.setEnabled(true);
            holder.valueTextView.setTextColor(ViewCompat.MEASURED_STATE_MASK);
            holder.clearButton.setEnabled(true);
        } else {
            holder.valueTextView.setEnabled(false);
            holder.valueTextView.setTextColor(Color.parseColor("#C6C6C6"));
            holder.clearButton.setEnabled(false);
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
        return DataEntryRowTypes.OPTION_SET.ordinal();
    }

    public void cacheOptionName() {
        String optionSetCode = this.mValue.getValue();
        if (optionSetCode == null || "".equals(optionSetCode)) {
            this.mSelectedOptionName = "";
            return;
        }
        Option option = (Option) new Select().from(Option.class).where(Condition.column("code").is(optionSetCode)).and(Condition.column("optionSet").is(this.mOptionSetId)).querySingle();
        if (option != null) {
            this.mSelectedOptionName = option.getName();
        }
        if (this.mSelectedOptionName == null) {
            this.mSelectedOptionName = "";
        }
    }

    public void clearCachedOptionName() {
        this.mSelectedOptionName = "";
    }
}
