package org.icddrb.dhis.android.sdk.persistence.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import java.util.List;
import org.icddrb.dhis.android.sdk.persistence.models.OrganisationUnitUser;
import org.icddrb.dhis.android.sdk.persistence.models.UnionFWA;
import org.icddrb.dhis.android.sdk.utils.Preconditions;

public final class AppPreferences {
    private static final String APP_PREFERENCES = "preferences:Application";
    private static final String CHOSEN_ORG = "key:chosenorg";
    private static final String DROPDOWN_INFO = "key:dropdowninfo";
    private static final String SERVER_URL = "key:serverUrl";
    private static final String USERNAME = "key:userName";
    private static final String USER_OPTION_ID = "key:useroption";
    private final SharedPreferences mPrefs;

    public AppPreferences(Context context) {
        Preconditions.isNull(context, "Context object must not be null");
        this.mPrefs = context.getSharedPreferences(APP_PREFERENCES, 0);
    }

    public void putServerUrl(String url) {
        put(SERVER_URL, url);
    }

    public String getServerUrl() {
        return get(SERVER_URL);
    }

    public void putUserName(String username) {
        put(USERNAME, username);
    }

    public String getUsername() {
        return get(USERNAME);
    }

    public void clear() {
        delete();
    }

    private void put(String key, String value) {
        this.mPrefs.edit().putString(key, value).apply();
    }

    private String get(String key) {
        return this.mPrefs.getString(key, null);
    }

    private void delete() {
        this.mPrefs.edit().clear().apply();
    }

    public void putUserOptionId(String id) {
        put(USER_OPTION_ID, id);
    }

    public String getUserOptionId() {
        return get(USER_OPTION_ID);
    }

    public void putChosenOrg(String id) {
        put(CHOSEN_ORG, id);
    }

    public String getChosenOrg() {
        return get(CHOSEN_ORG);
    }

    public void clearChosenOptions() {
        put(USER_OPTION_ID, "");
        put(CHOSEN_ORG, "");
    }

    public void putDropdownInfo(UnionFWA o) {
        put(DROPDOWN_INFO, new Gson().toJson((Object) o));
    }

    public UnionFWA getDropdownInfo() {
        Gson gson = new Gson();
        String json = get(DROPDOWN_INFO);
        return json == null ? new UnionFWA() : (UnionFWA) gson.fromJson(json, UnionFWA.class);
    }

    public String getRootUnionId(String orgId, List<OrganisationUnitUser> orgOptionSets) {
        UnionFWA o = getDropdownInfo();
        if (o != null) {
            return o.getRootUnion(orgId, orgOptionSets);
        }
        return null;
    }
}
