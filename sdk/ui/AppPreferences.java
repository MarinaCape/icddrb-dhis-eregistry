package org.icddrb.dhis.client.sdk.ui;

public interface AppPreferences {
    String getAPiVersion();

    int getBackgroundSyncFrequency();

    boolean getBackgroundSyncState();

    boolean getCrashReportsState();

    long getLastSynced();

    boolean getSyncNotifications();

    void setApiVersion(String str);

    void setBackgroundSyncFrequency(int i);

    void setBackgroundSyncState(Boolean bool);

    void setCrashReportsState(Boolean bool);

    boolean setLastSynced(long j);

    void setSyncNotifications(Boolean bool);
}
