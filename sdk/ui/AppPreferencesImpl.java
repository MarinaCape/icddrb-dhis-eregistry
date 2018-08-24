package org.icddrb.dhis.client.sdk.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import org.icddrb.dhis.client.sdk.utils.Preconditions;

public class AppPreferencesImpl implements AppPreferences {
    public static final String API_VERSION = "apiVersion";
    public static final String BACKGROUND_SYNC = "background_sync";
    public static final String CRASH_REPORTS = "crashReports";
    public static final String DEFAULT_API_VERSION = "2.27";
    public static final Boolean DEFAULT_BACKGROUND_SYNC = Boolean.valueOf(true);
    public static final Boolean DEFAULT_CRASH_REPORTS = Boolean.valueOf(true);
    public static final Boolean DEFAULT_SYNC_NOTIFICATIONS = Boolean.valueOf(true);
    public static final int DEFAULT_UPDATE_FREQUENCY = 1440;
    public static final String PREFS_NAME = "preferences:application";
    public static final String SYNC_DATE = "syncDate";
    public static final String SYNC_NOTIFICATIONS = "sync_notifications";
    public static final String UPDATE_FREQUENCY = "update_frequency";
    private final SharedPreferences sharedPreferences;

    public AppPreferencesImpl(Context context) {
        Preconditions.isNull(context, "context must not be null");
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, 0);
    }

    public long getLastSynced() {
        return this.sharedPreferences.getLong(SYNC_DATE, 0);
    }

    public boolean setLastSynced(long date) {
        return this.sharedPreferences.edit().putLong(SYNC_DATE, date).commit();
    }

    public void setBackgroundSyncFrequency(int minutes) {
        Editor editor = this.sharedPreferences.edit();
        editor.putInt("update_frequency", minutes);
        editor.apply();
    }

    public int getBackgroundSyncFrequency() {
        return this.sharedPreferences.getInt("update_frequency", 1440);
    }

    public void setBackgroundSyncState(Boolean enabled) {
        Editor editor = this.sharedPreferences.edit();
        editor.putBoolean(BACKGROUND_SYNC, enabled.booleanValue());
        editor.apply();
    }

    public void setSyncNotifications(Boolean enabled) {
        Editor editor = this.sharedPreferences.edit();
        editor.putBoolean(SYNC_NOTIFICATIONS, enabled.booleanValue());
        editor.apply();
    }

    public boolean getSyncNotifications() {
        return this.sharedPreferences.getBoolean(SYNC_NOTIFICATIONS, DEFAULT_SYNC_NOTIFICATIONS.booleanValue());
    }

    public boolean getBackgroundSyncState() {
        return this.sharedPreferences.getBoolean(BACKGROUND_SYNC, DEFAULT_BACKGROUND_SYNC.booleanValue());
    }

    public boolean getCrashReportsState() {
        return this.sharedPreferences.getBoolean("crashReports", DEFAULT_CRASH_REPORTS.booleanValue());
    }

    public void setCrashReportsState(Boolean enabled) {
        Editor editor = this.sharedPreferences.edit();
        editor.putBoolean("crashReports", enabled.booleanValue());
        editor.apply();
    }

    public String getAPiVersion() {
        return this.sharedPreferences.getString(API_VERSION, DEFAULT_API_VERSION);
    }

    public void setApiVersion(String version) {
        Editor editor = this.sharedPreferences.edit();
        editor.putString(API_VERSION, version);
        editor.apply();
    }
}
