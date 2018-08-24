package org.icddrb.dhis.android.sdk.network;

import java.util.HashSet;
import java.util.Set;
import org.icddrb.dhis.android.sdk.persistence.preferences.ResourceType;

public final class SessionManager {
    private static SessionManager mSessionManager;
    private final Set<ResourceType> mResources = new HashSet();

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (mSessionManager == null) {
            mSessionManager = new SessionManager();
        }
        return mSessionManager;
    }

    public void delete() {
        this.mResources.clear();
    }

    public void setResourceTypeSynced(ResourceType resourceType) {
        this.mResources.add(resourceType);
    }

    public boolean isResourceTypeSynced(ResourceType resourceType) {
        return this.mResources.contains(resourceType);
    }
}
