package org.icddrb.dhis.android.sdk.persistence.models;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raizlabs.android.dbflow.structure.BaseModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseIdentifiableObject extends BaseModel {
    @JsonProperty("access")
    Access access;
    @JsonProperty("created")
    String created;
    @JsonProperty("displayName")
    String displayName;
    @JsonProperty("lastUpdated")
    String lastUpdated;
    @JsonProperty("name")
    String name;

    protected BaseIdentifiableObject() {
    }

    public abstract String getUid();

    public abstract void setUid(String str);

    public BaseIdentifiableObject(BaseIdentifiableObject baseIdentifiableObject) {
        this.name = baseIdentifiableObject.name;
        this.displayName = baseIdentifiableObject.displayName;
        this.created = baseIdentifiableObject.created;
        this.lastUpdated = baseIdentifiableObject.lastUpdated;
        if (baseIdentifiableObject.access != null) {
            this.access = new Access(baseIdentifiableObject.access);
        }
    }

    @JsonProperty("createdAtClient")
    public void setCreatedAtClient(String created) {
        this.created = created;
    }

    @JsonProperty("lastUpdatedAtClient")
    public void setLastUpdatedAtClient(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @JsonAnySetter
    public void handleUnknown(String key, Object value) {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCreated() {
        return this.created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getLastUpdated() {
        return this.lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Access getAccess() {
        return this.access;
    }

    public void setAccess(Access access) {
        this.access = access;
    }

    public static <T extends BaseIdentifiableObject> List<T> merge(List<T> existingItems, List<T> updatedItems, List<T> persistedItems) {
        Map<String, T> updatedItemsMap = toMap(updatedItems);
        Map<String, T> persistedItemsMap = toMap(persistedItems);
        Map<String, T> existingItemsMap = new HashMap();
        if (existingItems == null || existingItems.isEmpty()) {
            return new ArrayList(existingItemsMap.values());
        }
        for (T existingItem : existingItems) {
            String id = existingItem.getUid();
            BaseIdentifiableObject updatedItem = (BaseIdentifiableObject) updatedItemsMap.get(id);
            BaseIdentifiableObject persistedItem = (BaseIdentifiableObject) persistedItemsMap.get(id);
            if (updatedItem != null) {
                if (persistedItem != null) {
                    updatedItem.setUid(persistedItem.getUid());
                }
                existingItemsMap.put(id,(T) updatedItem);
            } else if (persistedItem != null) {
                existingItemsMap.put(id, (T) persistedItem);
            }
        }
        return new ArrayList(existingItemsMap.values());
    }

    public static <T extends BaseIdentifiableObject> Map<String, T> toMap(Collection<T> objects) {
        Map<String, T> map = new HashMap();
        if (objects != null && objects.size() > 0) {
            for (T object : objects) {
                if (object.getUid() != null) {
                    map.put(object.getUid(), object);
                }
            }
        }
        return map;
    }
}
