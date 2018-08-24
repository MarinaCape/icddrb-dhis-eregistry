package org.icddrb.dhis.android.sdk.network;

import org.icddrb.dhis.android.sdk.utils.Preconditions;

public final class Credentials {
    private String password;
    private String username;

    public Credentials(String username, String password) {
        this.username = (String) Preconditions.isNull(username, "Username must not be null");
        this.password = (String) Preconditions.isNull(password, "Password must not be null");
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }
}
