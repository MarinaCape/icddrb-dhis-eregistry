package org.icddrb.dhis.android.sdk.persistence.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import org.icddrb.dhis.android.sdk.utils.Preconditions;
import org.joda.time.DateTime;

public final class DateTimeManager {
    private static final String METADATA_UPDATE_DATETIME = "key:metaDataUpdateDateTime";
    private static final String PREFERENCES = "preferences:lastUpdated";
    private static DateTimeManager mPreferences;
    private final SharedPreferences mPrefs;

    private DateTimeManager(Context context) {
        Preconditions.isNull(context, "Context object must not be null");
        this.mPrefs = context.getSharedPreferences(PREFERENCES, 0);
    }

    public static void init(Context context) {
        mPreferences = new DateTimeManager(context);
    }

    public static DateTimeManager getInstance() {
        if (mPreferences != null) {
            return mPreferences;
        }
        throw new IllegalArgumentException("You have to call init() method first");
    }

    public void setLastUpdated(ResourceType type, DateTime dateTime) {
        setLastUpdated(type, null, dateTime);
    }

    public void setLastUpdated(ResourceType type, String salt, DateTime dateTime) {
        Preconditions.isNull(type, "ResourceType object must not be null");
        Preconditions.isNull(dateTime, "DateTime object must not be null");
        String identifier = METADATA_UPDATE_DATETIME + type.toString();
        if (salt != null) {
            identifier = identifier + salt;
        }
        putString(identifier, dateTime.toString());
    }

    public DateTime getLastUpdated(ResourceType type) {
        return getLastUpdated(type, null);
    }

    public DateTime getLastUpdated(ResourceType type, String salt) {
        String identifier = METADATA_UPDATE_DATETIME + type.toString();
        if (salt != null) {
            identifier = identifier + salt;
        }
        String dateTimeString = getString(identifier);
        if (dateTimeString != null) {
            return DateTime.parse(dateTimeString);
        }
        return null;
    }

    public void delete() {
        this.mPrefs.edit().clear().commit();
    }

    public void deleteLastUpdated(ResourceType type) {
        deleteLastUpdated(type, null);
    }

    public void deleteLastUpdated(ResourceType type, String salt) {
        String identifier = METADATA_UPDATE_DATETIME + type.toString();
        if (salt != null) {
            identifier = identifier + salt;
        }
        deleteString(identifier);
    }

    public boolean isLastUpdatedSet(ResourceType type) {
        return getLastUpdated(type) != null;
    }

    private void putString(String key, String value) {
        this.mPrefs.edit().putString(key, value).commit();
    }

    private String getString(String key) {
        return this.mPrefs.getString(key, null);
    }

    private void deleteString(String key) {
        this.mPrefs.edit().remove(key).commit();
    }
}
