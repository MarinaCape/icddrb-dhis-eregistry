package org.icddrb.dhis.android.sdk.controllers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import org.icddrb.dhis.android.sdk.network.DhisApi;
import org.icddrb.dhis.android.sdk.persistence.models.BaseIdentifiableObject;
import org.icddrb.dhis.android.sdk.persistence.models.BaseValue;
import org.icddrb.dhis.android.sdk.persistence.models.DataValue;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.Relationship;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.persistence.models.meta.DbOperation;
import org.icddrb.dhis.android.sdk.persistence.preferences.DateTimeManager;
import org.icddrb.dhis.android.sdk.persistence.preferences.ResourceType;
import org.icddrb.dhis.android.sdk.utils.DbUtils;
import org.joda.time.DateTime;
import org.joda.time.ReadableInstant;

public abstract class ResourceController {
    public static <T extends BaseIdentifiableObject> void saveResourceDataFromServer(ResourceType resourceType, DhisApi dhisApi, List<T> updatedItems, List<T> persistedItems, DateTime serverDateTime) {
        saveResourceDataFromServer(resourceType, null, dhisApi, updatedItems, persistedItems, serverDateTime, true);
    }

    public static <T extends BaseIdentifiableObject> void saveResourceDataFromServer(ResourceType resourceType, DhisApi dhisApi, List<T> updatedItems, List<T> persistedItems, DateTime serverDateTime, boolean keepOldValues) {
        saveResourceDataFromServer(resourceType, null, dhisApi, updatedItems, persistedItems, serverDateTime, keepOldValues);
    }

    public static <T extends BaseIdentifiableObject> void saveResourceDataFromServer(ResourceType resourceType, String salt, DhisApi dhisApi, List<T> updatedItems, List<T> persistedItems, DateTime serverDateTime) {
        saveResourceDataFromServer(resourceType, salt, dhisApi, updatedItems, persistedItems, serverDateTime, true);
    }

    public static <T extends BaseValue> void saveResourceDataFromServer(ResourceType resourceType, String salt, List<T> updatedItems, List<T> persistedItems, DateTime serverDateTime) {
        saveBaseValueDataFromServer(resourceType, salt, updatedItems, persistedItems, serverDateTime, true);
    }

    public static <T extends BaseValue> void saveBaseValueDataFromServer(ResourceType resourceType, String salt, List<T> updatedItems, List<T> persistedItems, DateTime serverDateTime, boolean keepOldValues) {
        Queue<DbOperation> operations = new LinkedList();
        operations.addAll(DbUtils.createBaseValueOperations(persistedItems, updatedItems, keepOldValues));
        DbUtils.applyBatch(operations);
        DateTimeManager.getInstance().setLastUpdated(resourceType, salt, serverDateTime);
    }

    public static <T extends BaseIdentifiableObject> void saveResourceDataFromServer(ResourceType resourceType, String salt, DhisApi dhisApi, List<T> updatedItems, List<T> persistedItems, DateTime serverDateTime, boolean keepOldValues) {
        Queue<DbOperation> operations = new LinkedList();
        operations.addAll(DbUtils.createOperations(persistedItems, updatedItems, keepOldValues));
        DbUtils.applyBatch(operations);
        DateTimeManager.getInstance().setLastUpdated(resourceType, salt, serverDateTime);
    }

    public static boolean shouldLoad(DateTime serverTime, ResourceType resource, String salt) {
        DateTime lastUpdated = DateTimeManager.getInstance().getLastUpdated(resource, salt);
        DateTime serverDateTime = serverTime;
        if (lastUpdated == null || lastUpdated.isBefore((ReadableInstant) serverDateTime)) {
            return true;
        }
        return false;
    }

    public static boolean shouldLoad(DateTime serverDateTime, ResourceType resource) {
        return shouldLoad(serverDateTime, resource, null);
    }

    public static Map<String, String> getBasicQueryMap(DateTime lastUpdated) {
        Map<String, String> map = new HashMap();
        map.put("fields", "[:all]");
        if (lastUpdated != null) {
            map.put("filter", "lastUpdated:gt:" + lastUpdated.toString());
        }
        return map;
    }

    public static void removeTrackedEntityEnrollments(List<Enrollment> list) {
        Queue<DbOperation> operations = new LinkedList();
        operations.addAll(DbUtils.removeResources(list));
        DbUtils.applyBatch(operations);
    }

    public static void removeTrackedEntityRelationships(List<Relationship> list) {
        Queue<DbOperation> operations = new LinkedList();
        for (Relationship relationship : list) {
            operations.add(DbOperation.delete(relationship));
        }
        DbUtils.applyBatch(operations);
    }

    public static void removeTrackedEntityInstances(List<TrackedEntityInstance> list) {
        Queue<DbOperation> operations = new LinkedList();
        operations.addAll(DbUtils.removeResources(list));
        for (TrackedEntityInstance trackedEntityInstance : list) {
            if (trackedEntityInstance.getRelationships() != null) {
                for (Relationship relationship : trackedEntityInstance.getRelationships()) {
                    operations.add(DbOperation.delete(relationship));
                }
            }
        }
        DbUtils.applyBatch(operations);
    }

    public static void removeEvents(List<Event> list) {
        Queue<DbOperation> operations = new LinkedList();
        operations.addAll(DbUtils.removeResources(list));
        for (Event event : list) {
            if (event.getDataValues() != null) {
                for (DataValue dataValue : event.getDataValues()) {
                    operations.add(DbOperation.delete(dataValue));
                }
            }
        }
        DbUtils.applyBatch(operations);
    }

    public static void overwriteRelationsFromServer(List<Relationship> updatedItems, List<Relationship> persistedItems) {
        Queue<DbOperation> operations = new LinkedList();
        for (Relationship relationship : persistedItems) {
            operations.add(DbOperation.delete(relationship));
        }
        for (Relationship relationship2 : updatedItems) {
            operations.add(DbOperation.save(relationship2));
        }
        DbUtils.applyBatch(operations);
    }
}
