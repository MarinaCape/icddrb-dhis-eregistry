package org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry;

import android.text.Editable;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.models.BaseValue;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.AbsTextWatcher;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.RowValueChangedEvent;

/* compiled from: ValueEntryHolder */
class OnTextChangeListener extends AbsTextWatcher {
    Row row;
    String rowType;
    RunProgramRulesDelayedDispatcher runProgramRulesDelayedDispatcher = new RunProgramRulesDelayedDispatcher();
    private BaseValue value;

    OnTextChangeListener() {
    }

    public void setRowType(String type) {
        this.rowType = type;
    }

    public void setRow(Row row) {
        this.row = row;
    }

    public void setBaseValue(BaseValue value) {
        this.value = value;
    }

    public void onRowReused() {
        if (this.runProgramRulesDelayedDispatcher != null) {
            this.runProgramRulesDelayedDispatcher.dispatchNow();
        }
    }

    public void afterTextChanged(Editable s) {
        String newValue = s != null ? s.toString() : "";
        if (!newValue.equals(this.value.getValue())) {
            this.value.setValue(newValue);
            RowValueChangedEvent rowValueChangeEvent = new RowValueChangedEvent(this.value, this.rowType);
            rowValueChangeEvent.setRow(this.row);
            Dhis2Application.getEventBus().post(rowValueChangeEvent);
            this.runProgramRulesDelayedDispatcher.dispatchDelayed(new RunProgramRulesEvent(this.value));
        }
    }
}
