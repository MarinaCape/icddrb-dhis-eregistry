package org.icddrb.dhis.android.sdk.ui.fragments.eventdataentry;

import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.AsyncHelperThread;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.DataEntryFragment;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.RefreshListViewEvent;

public class RulesEvaluatorThread extends AsyncHelperThread {
    private DataEntryFragment dataEntryFragment;

    public void init(DataEntryFragment dataEntryFragment) {
        setDataEntryFragment(dataEntryFragment);
    }

    public void setDataEntryFragment(DataEntryFragment dataEntryFragment) {
        this.dataEntryFragment = dataEntryFragment;
    }

    protected void work() {
        if (this.dataEntryFragment != null) {
            DataEntryFragment dataEntryFragment = this.dataEntryFragment;
            DataEntryFragment.resetHidingAndWarnings(this.dataEntryFragment.getListViewAdapter(), this.dataEntryFragment.getSpinnerAdapter());
            this.dataEntryFragment.evaluateAndApplyProgramRules();
            Dhis2Application.getEventBus().post(new RefreshListViewEvent());
        }
    }

    public void kill() {
        super.kill();
        setDataEntryFragment(null);
    }
}
