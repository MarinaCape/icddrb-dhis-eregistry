package org.icddrb.dhis.android.sdk.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import org.icddrb.dhis.android.sdk.C0845R;
import org.icddrb.dhis.client.sdk.ui.AppPreferences;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.PeriodFormatterBuilder;

public class SyncDateWrapper {
    private static final long DAYS_OLD = 1;
    private static final long NEVER = 0;
    private final String DATE_FORMAT;
    private final String HOURS;
    private final String MIN_AGO;
    private final String NEVER_SYNCED;
    private final String NOW;
    private final AppPreferences appPreferences;

    public SyncDateWrapper(Context context, AppPreferences appPreferences) {
        this.appPreferences = appPreferences;
        this.DATE_FORMAT = context.getString(C0845R.string.date_format);
        this.NEVER_SYNCED = context.getString(C0845R.string.never);
        this.MIN_AGO = context.getString(C0845R.string.min_ago);
        this.HOURS = context.getString(C0845R.string.hours);
        this.NOW = context.getString(C0845R.string.now);
    }

    public void setLastSyncedNow() {
        this.appPreferences.setLastSynced(DateTime.now().getMillis());
    }

    public void clearLastSynced() {
        this.appPreferences.setLastSynced(0);
    }

    @Nullable
    public DateTime getLastSyncedDate() {
        long lastSynced = this.appPreferences.getLastSynced();
        if (lastSynced > 0) {
            return new DateTime().withMillis(lastSynced);
        }
        return null;
    }

    public long getLastSyncedLong() {
        return this.appPreferences.getLastSynced();
    }

    public String getLastSyncedString() {
        long lastSync = getLastSyncedLong();
        if (lastSync == 0) {
            return this.NEVER_SYNCED;
        }
        ReadableInstant now = DateTime.now();
        ReadableInstant lastSynced = new DateTime().withMillis(lastSync);
        if (now.minusHours(24).compareTo(lastSynced) == 0) {
            return DateTimeFormat.forPattern(this.DATE_FORMAT).print(lastSynced);
        }
        String result = new PeriodFormatterBuilder().appendHours().appendSuffix(this.HOURS).appendSeparator(" ").appendMinutes().appendSuffix(this.MIN_AGO).toFormatter().print(new Period(lastSynced, now));
        if (result.isEmpty()) {
            return this.NOW;
        }
        return result;
    }
}
