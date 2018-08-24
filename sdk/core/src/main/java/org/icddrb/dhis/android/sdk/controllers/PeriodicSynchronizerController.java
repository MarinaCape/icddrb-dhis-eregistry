package org.icddrb.dhis.android.sdk.controllers;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import org.icddrb.dhis.android.sdk.services.PeriodicSynchronizer;

public final class PeriodicSynchronizerController {
    public static final String CLASS_TAG = PeriodicSynchronizerController.class.getSimpleName();
    public static final String UPDATE_FREQUENCY = "update_frequency";

    private PeriodicSynchronizerController() {
    }

    public static int getUpdateFrequency(Context context) {
        int updateFrequency = context.getSharedPreferences(DhisController.PREFS_NAME, 0).getInt("update_frequency", 2);
        Log.e(CLASS_TAG, "updateFrequency: " + updateFrequency);
        return updateFrequency;
    }

    public static void setUpdateFrequency(Context context, int frequency) {
        Editor editor = context.getSharedPreferences(DhisController.PREFS_NAME, 0).edit();
        editor.putInt("update_frequency", frequency);
        editor.commit();
        Log.e(CLASS_TAG, "updateFrequency: " + frequency);
        PeriodicSynchronizer.reActivate(context);
    }

    public static void cancelPeriodicSynchronizer(Context context) {
        PeriodicSynchronizer.cancelPeriodicSynchronizer(context);
    }

    public static void activatePeriodicSynchronizer(Context context) {
        PeriodicSynchronizer.activatePeriodicSynchronizer(context, PeriodicSynchronizer.getInterval(context));
    }
}
