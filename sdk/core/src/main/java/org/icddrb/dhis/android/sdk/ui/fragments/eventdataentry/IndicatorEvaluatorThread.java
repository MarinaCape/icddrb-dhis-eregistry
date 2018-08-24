package org.icddrb.dhis.android.sdk.ui.fragments.eventdataentry;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramIndicator;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.AsyncHelperThread;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.DataEntryFragment;

class IndicatorEvaluatorThread extends AsyncHelperThread {
    private EventDataEntryFragment eventDataEntryFragment;
    private ConcurrentLinkedQueue<ProgramIndicator> queuedProgramIndicators = new ConcurrentLinkedQueue();

    IndicatorEvaluatorThread() {
    }

    void init(EventDataEntryFragment eventDataEntryFragment) {
        setEventDataEntryFragment(eventDataEntryFragment);
    }

    private void setEventDataEntryFragment(EventDataEntryFragment eventDataEntryFragment) {
        this.eventDataEntryFragment = eventDataEntryFragment;
    }

    protected void work() {
        if (this.eventDataEntryFragment != null) {
            while (!this.queuedProgramIndicators.isEmpty()) {
                this.eventDataEntryFragment.evaluateAndApplyProgramIndicator((ProgramIndicator) this.queuedProgramIndicators.poll());
            }
            EventDataEntryFragment eventDataEntryFragment = this.eventDataEntryFragment;
            DataEntryFragment.refreshListView();
        }
    }

    void schedule(List<ProgramIndicator> programIndicators) {
        for (ProgramIndicator programIndicator : programIndicators) {
            if (!this.queuedProgramIndicators.contains(programIndicator)) {
                this.queuedProgramIndicators.add(programIndicator);
            }
        }
        super.schedule();
    }

    public void kill() {
        super.kill();
        setEventDataEntryFragment(null);
        if (this.queuedProgramIndicators != null) {
            this.queuedProgramIndicators.clear();
        }
        this.queuedProgramIndicators = null;
    }
}
