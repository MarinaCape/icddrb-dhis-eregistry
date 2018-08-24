package org.icddrb.dhis.android.sdk.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class StartPeriodicSynchronizerService extends Service {
    public static final String CLASS_TAG = StartPeriodicSynchronizerService.class.getSimpleName();

    public void onCreate() {
        Log.d(CLASS_TAG, "startperiodicsyncservice oncreate");
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(CLASS_TAG, "startperiodicsyncservice onstartcommand");
        PeriodicSynchronizer.getInstance();
        PeriodicSynchronizer.activatePeriodicSynchronizer(this, PeriodicSynchronizer.getInterval(this));
        return 1;
    }

    public void onStart(Context context, Intent intent, int startId) {
        Log.e(CLASS_TAG, "startperiodicsyncservice onstart");
        PeriodicSynchronizer.getInstance();
        PeriodicSynchronizer.activatePeriodicSynchronizer(context, PeriodicSynchronizer.getInterval(context));
    }

    public IBinder onBind(Intent intent) {
        Log.e(CLASS_TAG, "startperiodicsyncservice onbind");
        return null;
    }
}
