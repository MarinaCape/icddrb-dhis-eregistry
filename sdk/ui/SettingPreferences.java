package org.icddrb.dhis.client.sdk.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import org.icddrb.dhis.client.sdk.utils.Preconditions;

public final class SettingPreferences {
    public static final String BACKGROUND_SYNCHRONIZATION = "backgroundSynchronization";
    public static final String CRASH_REPORTS = "crashReports";
    public static final boolean DEFAULT_BACKGROUND_SYNC = false;
    public static final boolean DEFAULT_CRASH_REPORTS = false;
    private static final String DEFAULT_UPDATE_FREQUENCY = "hoursTwentyFour";
    public static final String SYNCHRONIZATION_PERIOD = "synchronizationPeriod";
    public static final String SYNC_NOTIFICATIONS = "syncNotifications";
    private static SettingPreferences settingPreferences;
    private SharedPreferences sharedPreferences;

    private SettingPreferences(Context context) {
        Preconditions.isNull(context, "context must not be null");
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void init(Context context) {
        settingPreferences = new SettingPreferences(context);
    }

    private static SettingPreferences getInstance() {
        Preconditions.isNull(settingPreferences, "call init(context) first");
        return settingPreferences;
    }

    public static boolean backgroundSynchronization() {
        return getInstance().sharedPreferences.getBoolean(BACKGROUND_SYNCHRONIZATION, false);
    }

    public static String synchronizationPeriod() {
        return getInstance().sharedPreferences.getString(SYNCHRONIZATION_PERIOD, DEFAULT_UPDATE_FREQUENCY);
    }

    public static Boolean crashReports() {
        return Boolean.valueOf(getInstance().sharedPreferences.getBoolean("crashReports", false));
    }
}
