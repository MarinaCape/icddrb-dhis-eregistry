package org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry;

import android.os.Handler;
import android.util.Log;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;

public class RunProgramRulesDelayedDispatcher {
    private final int DELAY_MILLIS = 1000;
    private Handler handler = new Handler();
    private RunProgramRulesEvent runProgramRulesEvent;
    private Runnable runnable;

    /* renamed from: org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.RunProgramRulesDelayedDispatcher$1 */
    class C08831 implements Runnable {
        C08831() {
        }

        public void run() {
            if (RunProgramRulesDelayedDispatcher.this.runProgramRulesEvent != null) {
                Dhis2Application.getEventBus().post(RunProgramRulesDelayedDispatcher.this.runProgramRulesEvent);
            } else {
                Log.d("RunProgramRules", "runProgramRulesEvent is null");
            }
            RunProgramRulesDelayedDispatcher.this.runnable = null;
        }
    }

    public void dispatchDelayed(RunProgramRulesEvent runProgramRulesEvent) {
        this.runProgramRulesEvent = runProgramRulesEvent;
        if (this.runnable == null) {
            initRunnable();
        } else {
            this.handler.removeCallbacks(this.runnable);
        }
        this.handler.postDelayed(this.runnable, 1000);
    }

    public void dispatchNow() {
        if (this.handler != null && this.runnable != null) {
            this.handler.removeCallbacks(this.runnable);
            this.handler.post(this.runnable);
        }
    }

    private void initRunnable() {
        this.runnable = new C08831();
    }
}
