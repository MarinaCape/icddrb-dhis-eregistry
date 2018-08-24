package org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.autocompleterow;

import android.widget.TextView;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.models.BaseValue;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.RowValueChangedEvent;
import org.icddrb.dhis.android.sdk.utils.api.ValueType;

class OnOptionItemSelectedListener implements AutoCompleteOnOptionSelectedListener {
    private BaseValue value;
    private final TextView valueTextView;
    private ValueType valueType;

    public OnOptionItemSelectedListener(TextView valueTextView) {
        this.valueTextView = valueTextView;
    }

    public void setValue(BaseValue value) {
        this.value = value;
    }

    public void setValueType(ValueType vt) {
        this.valueType = vt;
    }

    public void onOptionSelected(int dialogId, int position, String id, String name) {
        this.valueTextView.setText(name);
        if (this.valueType.equals(ValueType.ORGANISATION_UNIT)) {
            Dhis2Application.dhisController.getAppPreferences().putChosenOrg(this.value.getValue());
        }
        Dhis2Application.getEventBus().post(new RowValueChangedEvent(this.value, this.valueType.toString()));
    }
}
