package org.icddrb.dhis.android.sdk.network;

import com.squareup.okhttp.HttpUrl;

public final class Session {
    private final Credentials credentials;
    private final HttpUrl serverUrl;

    public Session(HttpUrl serverUrl, Credentials credentials) {
        this.serverUrl = serverUrl;
        this.credentials = credentials;
    }

    public Credentials getCredentials() {
        return this.credentials;
    }

    public HttpUrl getServerUrl() {
        return this.serverUrl;
    }
}
