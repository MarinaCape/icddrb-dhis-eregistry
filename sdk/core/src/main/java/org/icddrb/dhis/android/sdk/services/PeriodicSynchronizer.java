package org.icddrb.dhis.android.sdk.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import org.icddrb.dhis.android.sdk.controllers.DhisController;
import org.icddrb.dhis.android.sdk.controllers.DhisService;
import org.icddrb.dhis.android.sdk.controllers.PeriodicSynchronizerController;
import org.icddrb.dhis.android.sdk.controllers.SyncStrategy;
import org.joda.time.DateTimeConstants;

public class PeriodicSynchronizer extends BroadcastReceiver {
    public static final String CLASS_TAG = "PeriodicSynchronizer";
    public static final int DEFAULT_UPDATE_FREQUENCY = 2;
    public static final int FREQUENCY_15_MINTUES = 1;
    public static final int FREQUENCY_DISABLED = 4;
    public static final int FREQUENCY_ONE_DAY = 3;
    public static final int FREQUENCY_ONE_HOUR = 2;
    public static final int FREQUENCY_ONE_MINUTE = 0;
    private static PeriodicSynchronizer periodicSynchronizer;
    private int currentInterval = 15;

    public static PeriodicSynchronizer getInstance() {
        if (periodicSynchronizer == null) {
            periodicSynchronizer = new PeriodicSynchronizer();
        }
        return periodicSynchronizer;
    }

    public void onReceive(Context context, Intent intent) {
        if (DhisController.isUserLoggedIn()) {
            DhisService.synchronize(context, SyncStrategy.DOWNLOAD_ONLY_NEW);
        } else {
            cancelPeriodicSynchronizer(context);
        }
    }

    public static void activatePeriodicSynchronizer(Context context, int minutes) {
        if (minutes > 0) {
            Log.d(CLASS_TAG, "activate periodic synchronizer " + minutes);
            AlarmManager am = (AlarmManager) context.getSystemService("alarm");
            Intent i = new Intent(context, PeriodicSynchronizer.class);
            if (PendingIntent.getBroadcast(context, 0, i, 536870912) == null) {
                am.setRepeating(0, System.currentTimeMillis(), (long) (DateTimeConstants.MILLIS_PER_MINUTE * minutes), PendingIntent.getBroadcast(context, 0, i, 134217728));
            }
        }
    }

    public static void cancelPeriodicSynchronizer(Context context) {
        Log.d(CLASS_TAG, "cancel periodic synchronizer");
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, new Intent(context, PeriodicSynchronizer.class), 536870912);
        if (sender != null) {
            ((AlarmManager) context.getSystemService("alarm")).cancel(sender);
            sender.cancel();
        }
    }

    public static int getInterval(Context context) {
        switch (PeriodicSynchronizerController.getUpdateFrequency(context)) {
            case 0:
                return 1;
            case 1:
                return 15;
            case 2:
                return 60;
            case 3:
                return 1440;
            case 4:
                return 0;
            default:
                return 2;
        }
    }

    public static int getInterval(Context context, int index) {
        switch (index) {
            case 0:
                return 1;
            case 1:
                return 15;
            case 2:
                return 60;
            case 3:
                return 1440;
            case 4:
                return 0;
            default:
                return 2;
        }
    }

    public static void reActivate(Context context) {
        int interval = getInterval(context);
        if (interval != getInstance().currentInterval) {
            getInstance();
            cancelPeriodicSynchronizer(context);
            getInstance();
            activatePeriodicSynchronizer(context, interval);
            getInstance().currentInterval = interval;
        }
    }
}
