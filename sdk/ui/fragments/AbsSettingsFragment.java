package org.icddrb.dhis.client.sdk.ui.fragments;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v7.preference.Preference.OnPreferenceClickListener;
import android.support.v7.preference.PreferenceFragmentCompat;
import org.icddrb.dhis.client.sdk.ui.C0935R;
import org.icddrb.dhis.client.sdk.ui.SettingPreferences;
import org.icddrb.dhis.client.sdk.ui.activities.BaseActivity;
import org.icddrb.dhis.client.sdk.ui.activities.OnBackPressedCallback;
import org.icddrb.dhis.client.sdk.ui.activities.OnBackPressedFromFragmentCallback;

public abstract class AbsSettingsFragment extends PreferenceFragmentCompat implements OnPreferenceClickListener, OnPreferenceChangeListener, OnBackPressedCallback {
    private Preference backgroundSynchronization;
    private Preference crashReports;
    private OnBackPressedFromFragmentCallback onBackPressedFromFragmentCallback;
    private Preference syncNotifications;
    private ListPreference synchronizationPeriod;

    public abstract boolean onBackgroundSynchronizationChanged(boolean z);

    public abstract boolean onBackgroundSynchronizationClick();

    public abstract boolean onCrashReportsChanged(boolean z);

    public abstract boolean onCrashReportsClick();

    public abstract boolean onSyncNotificationsChanged(boolean z);

    public abstract boolean onSynchronizationPeriodChanged(String str);

    public abstract boolean onSynchronizationPeriodClick();

    public void onCreatePreferences(Bundle bundle, String string) {
        addPreferencesFromResource(C0935R.xml.preferences);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.backgroundSynchronization = findPreference(SettingPreferences.BACKGROUND_SYNCHRONIZATION);
        this.backgroundSynchronization.setOnPreferenceChangeListener(this);
        this.backgroundSynchronization.setOnPreferenceClickListener(this);
        this.synchronizationPeriod = (ListPreference) findPreference(SettingPreferences.SYNCHRONIZATION_PERIOD);
        this.synchronizationPeriod.setSummary(getString(C0935R.string.synchronization_period_description) + " " + this.synchronizationPeriod.getEntry());
        this.synchronizationPeriod.setOnPreferenceChangeListener(this);
        this.synchronizationPeriod.setOnPreferenceClickListener(this);
        this.syncNotifications = findPreference(SettingPreferences.SYNC_NOTIFICATIONS);
        this.syncNotifications.setOnPreferenceChangeListener(this);
        this.syncNotifications.setOnPreferenceClickListener(this);
        this.crashReports = findPreference("crashReports");
        this.crashReports.setOnPreferenceChangeListener(this);
        this.crashReports.setOnPreferenceClickListener(this);
    }

    public void onAttach(Context context) {
        if (context instanceof BaseActivity) {
            ((BaseActivity) context).setOnBackPressedCallback(this);
        }
        if (context instanceof OnBackPressedFromFragmentCallback) {
            this.onBackPressedFromFragmentCallback = (OnBackPressedFromFragmentCallback) context;
        }
        super.onAttach(context);
    }

    public void onDetach() {
        if (getActivity() != null && (getActivity() instanceof BaseActivity)) {
            ((BaseActivity) getActivity()).setOnBackPressedCallback(null);
        }
        this.onBackPressedFromFragmentCallback = null;
        super.onDetach();
    }

    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        boolean z = true;
        switch (key.hashCode()) {
            case -2122642367:
                if (key.equals(SettingPreferences.SYNCHRONIZATION_PERIOD)) {
                    z = true;
                    break;
                }
                break;
            case 393581394:
                if (key.equals(SettingPreferences.BACKGROUND_SYNCHRONIZATION)) {
                    z = false;
                    break;
                }
                break;
            case 1242393624:
                if (key.equals("crashReports")) {
                    z = true;
                    break;
                }
                break;
        }
        switch (z) {
            case false:
                return onBackgroundSynchronizationClick();
            case true:
                return onSynchronizationPeriodClick();
            case true:
                return onCrashReportsClick();
            default:
                return false;
        }
    }

    public boolean onPreferenceChange(Preference preference, Object object) {
        String key = preference.getKey();
        boolean z = true;
        switch (key.hashCode()) {
            case -2122642367:
                if (key.equals(SettingPreferences.SYNCHRONIZATION_PERIOD)) {
                    z = true;
                    break;
                }
                break;
            case 393581394:
                if (key.equals(SettingPreferences.BACKGROUND_SYNCHRONIZATION)) {
                    z = false;
                    break;
                }
                break;
            case 1242393624:
                if (key.equals("crashReports")) {
                    z = true;
                    break;
                }
                break;
            case 1254293005:
                if (key.equals(SettingPreferences.SYNC_NOTIFICATIONS)) {
                    z = true;
                    break;
                }
                break;
        }
        switch (z) {
            case false:
                return onBackgroundSynchronizationChanged(((Boolean) object).booleanValue());
            case true:
                if (object instanceof String) {
                    this.synchronizationPeriod.setSummary(getString(C0935R.string.synchronization_period_description) + " " + this.synchronizationPeriod.getEntries()[this.synchronizationPeriod.findIndexOfValue((String) object)]);
                    return onSynchronizationPeriodChanged((String) object);
                }
                break;
            case true:
                break;
            case true:
                return onCrashReportsChanged(((Boolean) object).booleanValue());
            default:
                return false;
        }
        if (!((Boolean) object).booleanValue()) {
            ((NotificationManager) getContext().getSystemService("notification")).cancelAll();
        }
        return onSyncNotificationsChanged(((Boolean) object).booleanValue());
    }

    protected Preference getCrashReports() {
        return this.crashReports;
    }

    protected Preference getSynchronizationPeriod() {
        return this.synchronizationPeriod;
    }

    protected Preference getBackgroundSynchronization() {
        return this.backgroundSynchronization;
    }

    public boolean onBackPressed() {
        if (this.onBackPressedFromFragmentCallback == null) {
            return true;
        }
        this.onBackPressedFromFragmentCallback.onBackPressedFromFragment();
        return false;
    }
}
