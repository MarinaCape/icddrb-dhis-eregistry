package org.icddrb.dhis.android.eregistry;

import android.app.Activity;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;

public class TrackerCaptureApplication extends Dhis2Application {
    public void onCreate() {
        super.onCreate();
    }

    public Class<? extends Activity> getMainActivity() {
        return new MainActivity().getClass();
    }
}
