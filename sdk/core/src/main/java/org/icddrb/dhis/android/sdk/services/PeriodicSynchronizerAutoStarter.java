package org.icddrb.dhis.android.sdk.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PeriodicSynchronizerAutoStarter extends BroadcastReceiver {
    public static final String CLASS_TAG = "PeriodicSynchronizerAutoStarter";

    public void onReceive(Context context, Intent intent) {
        Log.e(CLASS_TAG, "PeriodicSynchronizerAutoStarter onReceive");
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Log.e(CLASS_TAG, "PeriodicSynchronizerAutoStarter onReceive BOOT_COMPLETED");
            context.startService(new Intent(context, StartPeriodicSynchronizerService.class));
        }
    }
}
