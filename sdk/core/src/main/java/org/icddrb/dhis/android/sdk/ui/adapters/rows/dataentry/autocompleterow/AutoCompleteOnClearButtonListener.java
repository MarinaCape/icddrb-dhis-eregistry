package org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.autocompleterow;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.models.BaseValue;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.DataEntryRowTypes;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.RowValueChangedEvent;

class AutoCompleteOnClearButtonListener implements OnClickListener {
    private OptionNameCacher optionNameCacher;
    private final TextView textView;
    private BaseValue value;

    public AutoCompleteOnClearButtonListener(TextView textView) {
        this.textView = textView;
    }

    public void setValue(BaseValue value) {
        this.value = value;
    }

    public void setOptionNameCacher(OptionNameCacher optionNameCacher) {
        this.optionNameCacher = optionNameCacher;
    }

    public void onClick(View v) {
        this.value.setValue("");
        this.optionNameCacher.clearCachedOptionName();
        this.textView.setText("");
        Dhis2Application.getEventBus().post(new RowValueChangedEvent(this.value, DataEntryRowTypes.OPTION_SET.toString()));
    }
}
