package org.icddrb.dhis.android.sdk.persistence.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import com.squareup.okhttp.HttpUrl;
import org.icddrb.dhis.android.sdk.network.Credentials;
import org.icddrb.dhis.android.sdk.network.Session;
import org.icddrb.dhis.android.sdk.utils.Preconditions;

public final class LastUpdatedManager {
    private static final String PASSWORD = "key:password";
    private static final String PREFERENCES = "preferences:Session";
    private static final String SERVER_URI = "key:Uri";
    private static final String USERNAME = "key:username";
    private static LastUpdatedManager mLastUpdatedManager;
    private SharedPreferences mPrefs;

    private LastUpdatedManager(Context context) {
        this.mPrefs = context.getSharedPreferences(PREFERENCES, 0);
    }

    public static void init(Context context) {
        Preconditions.isNull(context, "Context object must not be null");
        mLastUpdatedManager = new LastUpdatedManager(context);
    }

    public static LastUpdatedManager getInstance() {
        return mLastUpdatedManager;
    }

    public Session get() {
        String serverUrlString = getString(SERVER_URI);
        String userNameString = getString(USERNAME);
        String passwordString = getString(PASSWORD);
        HttpUrl serverUrl = null;
        if (serverUrlString != null) {
            serverUrl = HttpUrl.parse(serverUrlString);
        }
        Credentials credentials = null;
        if (!(userNameString == null || passwordString == null)) {
            credentials = new Credentials(userNameString, passwordString);
        }
        return new Session(serverUrl, credentials);
    }

    public void put(Session session) {
        Preconditions.isNull(session, "Session object must not be null");
        HttpUrl serverUrl = session.getServerUrl();
        Credentials credentials = session.getCredentials();
        String url = null;
        String username = null;
        String password = null;
        if (serverUrl != null) {
            url = serverUrl.toString();
        }
        if (credentials != null) {
            username = credentials.getUsername();
            password = credentials.getPassword();
        }
        putString(SERVER_URI, url);
        putString(USERNAME, username);
        putString(PASSWORD, password);
    }

    public void delete() {
        this.mPrefs.edit().clear().apply();
    }

    public void invalidate() {
        putString(USERNAME, null);
        putString(PASSWORD, null);
    }

    public boolean isInvalid() {
        return getString(USERNAME) == null && getString(PASSWORD) == null;
    }

    private void putString(String key, String value) {
        this.mPrefs.edit().putString(key, value).apply();
    }

    private String getString(String key) {
        return this.mPrefs.getString(key, null);
    }
}
