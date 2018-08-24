package org.icddrb.dhis.android.sdk.controllers;

import com.squareup.okhttp.HttpUrl;
import java.util.HashMap;
import java.util.Map;
import org.icddrb.dhis.android.sdk.network.APIException;
import org.icddrb.dhis.android.sdk.network.Credentials;
import org.icddrb.dhis.android.sdk.network.DhisApi;
import org.icddrb.dhis.android.sdk.network.Session;
import org.icddrb.dhis.android.sdk.network.SessionManager;
import org.icddrb.dhis.android.sdk.persistence.models.UserAccount;
import org.icddrb.dhis.android.sdk.persistence.preferences.DateTimeManager;
import org.icddrb.dhis.android.sdk.persistence.preferences.LastUpdatedManager;

final class UserController {
    private final DhisApi dhisApi;

    public UserController(DhisApi dhisApi) {
        this.dhisApi = dhisApi;
    }

    public UserAccount logInUser(HttpUrl serverUrl, Credentials credentials) throws APIException {
        Map<String, String> QUERY_PARAMS = new HashMap();
        QUERY_PARAMS.put("fields", "id,created,lastUpdated,name,displayName,firstName,surname,gender,birthday,introduction,education,employer,interests,jobTitle,languages,email,phoneNumber,teiSearchOrganisationUnits[id],organisationUnits[id],userGroups");
        UserAccount userAccount = this.dhisApi.getCurrentUserAccount(QUERY_PARAMS);
        LastUpdatedManager.getInstance().put(new Session(serverUrl, credentials));
        userAccount.save();
        return userAccount;
    }

    public void logOut() {
        LastUpdatedManager.getInstance().delete();
        DateTimeManager.getInstance().delete();
        SessionManager.getInstance().delete();
    }

    public UserAccount updateUserAccount() throws APIException {
        Map<String, String> QUERY_PARAMS = new HashMap();
        QUERY_PARAMS.put("fields", "id,created,lastUpdated,name,displayName,firstName,surname,gender,birthday,introduction,education,employer,interests,jobTitle,languages,email,phoneNumber,organisationUnits[id],userGroups");
        UserAccount userAccount = this.dhisApi.getCurrentUserAccount(QUERY_PARAMS);
        userAccount.save();
        return userAccount;
    }
}
