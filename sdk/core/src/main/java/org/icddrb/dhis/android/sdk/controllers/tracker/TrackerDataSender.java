package org.icddrb.dhis.android.sdk.controllers.tracker;

import android.util.Log;
import com.fasterxml.jackson.databind.JsonNode;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Join.JoinType;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.Update;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.icddrb.dhis.android.sdk.controllers.ApiEndpointContainer;
import org.icddrb.dhis.android.sdk.controllers.DhisController;
import org.icddrb.dhis.android.sdk.network.APIException;
import org.icddrb.dhis.android.sdk.network.DhisApi;
import org.icddrb.dhis.android.sdk.persistence.models.ApiResponse;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.FailedItem;
import org.icddrb.dhis.android.sdk.persistence.models.FailedItem.Table;
import org.icddrb.dhis.android.sdk.persistence.models.ImportSummary;
import org.icddrb.dhis.android.sdk.persistence.models.Relationship;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.utils.NetworkUtils;
import org.icddrb.dhis.android.sdk.utils.StringConverter;
import org.icddrb.dhis.android.sdk.utils.Utils;
import org.joda.time.DateTime;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.converter.ConversionException;

final class TrackerDataSender {
    public static final String CLASS_TAG = TrackerDataSender.class.getSimpleName();

    private TrackerDataSender() {
    }

    static void sendEventChanges(DhisApi dhisApi) throws APIException {
        if (dhisApi == null) {
            dhisApi = DhisController.getInstance().getDhisApi();
            if (dhisApi == null) {
                return;
            }
        }
        List<Event> events = new Select().from(Event.class).where(Condition.column("fromServer").is(Boolean.valueOf(false))).and(Condition.column("status").isNot(Event.STATUS_DELETED)).queryList();
        List<Event> eventsWithFailedThreshold = new Select().from(Event.class).join(FailedItem.class, JoinType.LEFT).on(Condition.column(Table.ITEMID).eq("localId")).where(Condition.column(Table.ITEMTYPE).eq("Event")).and(Condition.column(Table.FAILCOUNT).greaterThan(Integer.valueOf(3))).and(Condition.column("fromServer").is(Boolean.valueOf(false))).and(Condition.column("status").isNot(Event.STATUS_DELETED)).queryList();
        List<Event> eventsToPost = new ArrayList();
        eventsToPost.addAll(events);
        for (Event event : events) {
            for (Event failedEvent : eventsWithFailedThreshold) {
                if (event.getUid().equals(failedEvent.getUid())) {
                    eventsToPost.remove(event);
                }
            }
        }
        sendEventBatch(dhisApi, events);
    }

    static void sendEventBatch(DhisApi dhisApi, List<Event> events) throws APIException {
        if (events != null && !events.isEmpty()) {
            int i = 0;
            while (i < events.size()) {
                Event event = (Event) events.get(i);
                if (Utils.isLocal(event.getEnrollment()) && event.getEnrollment() != null) {
                    events.remove(i);
                    i--;
                }
                i++;
            }
            postEventBatch(dhisApi, events);
        }
    }

    static void postEventBatch(DhisApi dhisApi, List<Event> events) throws APIException {
        Map<String, Event> eventMap = new HashMap();
        try {
            Event event;
            Map<String, List<Event>> map = new HashMap();
            map.put(ApiEndpointContainer.EVENTS, events);
            List<ImportSummary> importSummaries = dhisApi.postEvents(map).getImportSummaries();
            for (Event event2 : events) {
                eventMap.put(event2.getUid(), event2);
            }
            if (importSummaries != null) {
                DateTime eventUploadTime = dhisApi.getSystemInfo().getServerDate();
                for (ImportSummary importSummary : importSummaries) {
                    Event event2 = (Event) eventMap.get(importSummary.getReference());
                    System.out.println("IMPORT SUMMARY: " + importSummary.getDescription());
                    if ((ImportSummary.SUCCESS.equals(importSummary.getStatus()) || ImportSummary.OK.equals(importSummary.getStatus())) && event2 != null) {
                        event2.setFromServer(true);
                        event2.setCreated(eventUploadTime.toString());
                        event2.setLastUpdated(eventUploadTime.toString());
                        event2.save();
                        clearFailedItem("Event", event2.getLocalId());
                    }
                }
            }
        } catch (APIException e) {
            sendEventChanges(dhisApi, (List) events);
        }
    }

    static void sendEventChanges(DhisApi dhisApi, List<Event> events) throws APIException {
        if (events != null && !events.isEmpty()) {
            Event event;
            int i = 0;
            while (i < events.size()) {
                event = (Event) events.get(i);
                if (Utils.isLocal(event.getEnrollment()) && event.getEnrollment() != null) {
                    events.remove(i);
                    i--;
                }
                i++;
            }
            Log.d(CLASS_TAG, "got this many events to send:" + events.size());
            for (Event event2 : events) {
                sendEventChanges(dhisApi, event2);
            }
        }
    }

    static void sendEventChanges(DhisApi dhisApi, Event event) throws APIException {
        if (event != null) {
            if (dhisApi == null) {
                dhisApi = DhisController.getInstance().getDhisApi();
                if (dhisApi == null) {
                    return;
                }
            }
            if (!Utils.isLocal(event.getEnrollment()) || event.getEnrollment() == null) {
                Enrollment cancelledEnrollment = TrackerController.getCancelledEnrollment(event.getEnrollment());
                if (!(cancelledEnrollment == null || cancelledEnrollment.isFromServer())) {
                    sendEnrollmentChanges(dhisApi, cancelledEnrollment, false);
                }
                Enrollment enrollment = TrackerController.getNotCancelledEnrollment(event.getEnrollment());
                if (!(enrollment == null || enrollment.isFromServer())) {
                    sendEnrollmentChanges(dhisApi, enrollment, false);
                }
                if (event.getCreated() == null) {
                    postEvent(event, dhisApi);
                } else {
                    putEvent(event, dhisApi);
                }
            }
        }
    }

    private static void postEvent(Event event, DhisApi dhisApi) throws APIException {
        try {
            Response response = dhisApi.postEvent(event);
            if (response.getStatus() == 200) {
                ImportSummary importSummary = getImportSummary(response);
                handleImportSummary(importSummary, "Event", event.getLocalId());
                if (ImportSummary.SUCCESS.equals(importSummary.getStatus()) || ImportSummary.OK.equals(importSummary.getStatus())) {
                    Header header = NetworkUtils.findLocationHeader(response.getHeaders());
                    event.setFromServer(true);
                    event.save();
                    clearFailedItem("Event", event.getLocalId());
                    UpdateEventTimestamp(event, dhisApi);
                }
            }
        } catch (APIException apiException) {
            NetworkUtils.handleEventSendException(apiException, event);
        }
    }

    private static void putEvent(Event event, DhisApi dhisApi) throws APIException {
        try {
            Response response = dhisApi.putEvent(event.getEvent(), event);
            if (response.getStatus() == 200) {
                ImportSummary importSummary = getImportSummary(response);
                handleImportSummary(importSummary, "Event", event.getLocalId());
                if (ImportSummary.SUCCESS.equals(importSummary.getStatus()) || ImportSummary.OK.equals(importSummary.getStatus())) {
                    event.setFromServer(true);
                    event.save();
                    clearFailedItem("Event", event.getLocalId());
                    UpdateEventTimestamp(event, dhisApi);
                }
            }
        } catch (APIException apiException) {
            NetworkUtils.handleEventSendException(apiException, event);
        }
    }

    private static void UpdateEventTimestamp(Event event, DhisApi dhisApi) throws APIException {
        try {
            Map<String, String> QUERY_PARAMS = new HashMap();
            QUERY_PARAMS.put("fields", "created,lastUpdated");
            Event updatedEvent = dhisApi.getEvent(event.getEvent(), QUERY_PARAMS);
            event.setCreated(updatedEvent.getCreated());
            event.setLastUpdated(updatedEvent.getLastUpdated());
            event.save();
        } catch (APIException apiException) {
            NetworkUtils.handleApiException(apiException);
        }
    }

    static void sendEnrollmentChanges(DhisApi dhisApi, List<Enrollment> enrollments, boolean sendEvents) throws APIException {
        if (enrollments != null && !enrollments.isEmpty()) {
            int i = 0;
            while (i < enrollments.size()) {
                if (Utils.isLocal(((Enrollment) enrollments.get(i)).getTrackedEntityInstance())) {
                    enrollments.remove(i);
                    i--;
                }
                i++;
            }
            Log.d(CLASS_TAG, "got this many enrollments to send:" + enrollments.size());
            for (Enrollment enrollment : enrollments) {
                sendEnrollmentChanges(dhisApi, enrollment, sendEvents);
            }
        }
    }

    static void sendEnrollmentChanges(DhisApi dhisApi, Enrollment enrollment, boolean sendEvents) throws APIException {
        if (enrollment != null && !Utils.isLocal(enrollment.getTrackedEntityInstance())) {
            if (dhisApi == null) {
                dhisApi = DhisController.getInstance().getDhisApi();
                if (dhisApi == null) {
                    return;
                }
            }
            TrackedEntityInstance trackedEntityInstance = TrackerController.getTrackedEntityInstance(enrollment.getTrackedEntityInstance());
            if (trackedEntityInstance != null) {
                if (!trackedEntityInstance.isFromServer()) {
                    sendTrackedEntityInstanceChanges(dhisApi, trackedEntityInstance, false);
                }
                boolean success;
                if (enrollment.getCreated() == null) {
                    success = postEnrollment(enrollment, dhisApi);
                    sendEventChanges(dhisApi, TrackerController.getEventsByEnrollment(enrollment.getLocalId()));
                    return;
                }
                success = putEnrollment(enrollment, dhisApi);
                sendEventChanges(dhisApi, TrackerController.getEventsByEnrollment(enrollment.getLocalId()));
            }
        }
    }

    private static boolean postEnrollment(Enrollment enrollment, DhisApi dhisApi) throws APIException {
        try {
            Response response = dhisApi.postEnrollment(enrollment);
            if (response.getStatus() != 200) {
                return true;
            }
            ImportSummary importSummary = getImportSummary(response);
            handleImportSummary(importSummary, "Enrollment", enrollment.getLocalId());
            if (!ImportSummary.SUCCESS.equals(importSummary.getStatus()) && !ImportSummary.OK.equals(importSummary.getStatus())) {
                return true;
            }
            enrollment.setFromServer(true);
            enrollment.save();
            clearFailedItem("Enrollment", enrollment.getLocalId());
            UpdateEnrollmentTimestamp(enrollment, dhisApi);
            return true;
        } catch (APIException apiException) {
            NetworkUtils.handleEnrollmentSendException(apiException, enrollment);
            return false;
        }
    }

    private static boolean putEnrollment(Enrollment enrollment, DhisApi dhisApi) throws APIException {
        try {
            Response response = dhisApi.putEnrollment(enrollment.getEnrollment(), enrollment);
            if (response.getStatus() != 200) {
                return true;
            }
            ImportSummary importSummary = getImportSummary(response);
            handleImportSummary(importSummary, "Enrollment", enrollment.getLocalId());
            if (!ImportSummary.SUCCESS.equals(importSummary.getStatus()) && !ImportSummary.OK.equals(importSummary.getStatus())) {
                return true;
            }
            enrollment.setFromServer(true);
            enrollment.save();
            clearFailedItem("Enrollment", enrollment.getLocalId());
            UpdateEnrollmentTimestamp(enrollment, dhisApi);
            return true;
        } catch (APIException apiException) {
            NetworkUtils.handleEnrollmentSendException(apiException, enrollment);
            return false;
        }
    }

    private static void updateEnrollmentReferences(long localId, String newReference) {
        Log.d(CLASS_TAG, "updating enrollment references");
        new Update(Event.class).set(Condition.column("enrollment").is(newReference)).where(Condition.column(Event.Table.LOCALENROLLMENTID).is(Long.valueOf(localId))).and(Condition.column("status").isNot(Event.STATUS_DELETED)).async().execute();
        new Update(Enrollment.class).set(Condition.column("enrollment").is(newReference), Condition.column("fromServer").is(Boolean.valueOf(true))).where(Condition.column("localId").is(Long.valueOf(localId))).async().execute();
    }

    private static void UpdateEnrollmentTimestamp(Enrollment enrollment, DhisApi dhisApi) throws APIException {
        try {
            Map<String, String> QUERY_PARAMS = new HashMap();
            QUERY_PARAMS.put("fields", "created,lastUpdated");
            Enrollment updatedEnrollment = dhisApi.getEnrollment(enrollment.getEnrollment(), QUERY_PARAMS);
            enrollment.setCreated(updatedEnrollment.getCreated());
            enrollment.setLastUpdated(updatedEnrollment.getLastUpdated());
            enrollment.save();
        } catch (APIException apiException) {
            NetworkUtils.handleApiException(apiException);
        }
    }

    static void sendTrackedEntityInstanceChanges(DhisApi dhisApi, boolean sendEnrollments) throws APIException {
        List trackedEntityInstances = new Select().from(TrackedEntityInstance.class).where(Condition.column("fromServer").is(Boolean.valueOf(false))).queryList();
        if (dhisApi == null) {
            dhisApi = DhisController.getInstance().getDhisApi();
            if (dhisApi == null) {
                return;
            }
        }
        if (trackedEntityInstances.size() <= 1) {
            sendTrackedEntityInstanceChanges(dhisApi, trackedEntityInstances, sendEnrollments);
        } else {
            postTrackedEntityInstanceBatch(dhisApi, trackedEntityInstances);
        }
    }

    static void sendTrackedEntityInstanceChanges(DhisApi dhisApi, List<TrackedEntityInstance> trackedEntityInstances, boolean sendEnrollments) throws APIException {
        if (trackedEntityInstances != null && !trackedEntityInstances.isEmpty()) {
            Log.d(CLASS_TAG, "got this many teis to send:" + trackedEntityInstances.size());
            for (TrackedEntityInstance trackedEntityInstance : trackedEntityInstances) {
                sendTrackedEntityInstanceChanges(dhisApi, trackedEntityInstance, sendEnrollments);
            }
        }
    }

    static void sendTrackedEntityInstanceChanges(DhisApi dhisApi, TrackedEntityInstance trackedEntityInstance, boolean sendEnrollments) throws APIException {
        if (trackedEntityInstance != null) {
            if (dhisApi == null) {
                dhisApi = DhisController.getInstance().getDhisApi();
                if (dhisApi == null) {
                    return;
                }
            }
            Map<String, TrackedEntityInstance> relatedTeis = new HashMap();
            DateTime serverDate = DhisController.getInstance().getDhisApi().getSystemInfo().getServerDate();
            relatedTeis = getRecursiveRelationatedTeis(trackedEntityInstance, relatedTeis);
            if (relatedTeis.size() > 1) {
                pushTeiWithoutRelationFirst(relatedTeis, serverDate);
                trackedEntityInstance.setCreated(serverDate.toString());
                trackedEntityInstance.setCreatedAtClient(serverDate.toString());
                trackedEntityInstance.setFromServer(true);
                sendTrackedEntityInstance(dhisApi, trackedEntityInstance, sendEnrollments);
                return;
            }
            sendTrackedEntityInstance(dhisApi, trackedEntityInstance, sendEnrollments);
        }
    }

    private static Map<String, TrackedEntityInstance> getRecursiveRelationatedTeis(TrackedEntityInstance trackedEntityInstance, Map<String, TrackedEntityInstance> relatedTeiList) {
        if (trackedEntityInstance.getRelationships() != null && trackedEntityInstance.getRelationships().size() > 0) {
            for (Relationship relationship : trackedEntityInstance.getRelationships()) {
                if (relationship.getTrackedEntityInstanceB().equals(trackedEntityInstance.getUid())) {
                    relatedTeiList = addRelatedNotPushedTeis(relatedTeiList, relationship.getTrackedEntityInstanceA());
                } else if (relationship.getTrackedEntityInstanceA().equals(trackedEntityInstance.getUid())) {
                    relatedTeiList = addRelatedNotPushedTeis(relatedTeiList, relationship.getTrackedEntityInstanceB());
                }
            }
        }
        return relatedTeiList;
    }

    private static Map<String, TrackedEntityInstance> addRelatedNotPushedTeis(Map<String, TrackedEntityInstance> relatedTeiList, String target) {
        TrackedEntityInstance relatedTrackedEntityInstance = TrackerController.getTrackedEntityInstance(target);
        if (relatedTrackedEntityInstance.isFromServer() || relatedTrackedEntityInstance.getCreated() != null || relatedTeiList.containsKey(relatedTrackedEntityInstance.getUid())) {
            return relatedTeiList;
        }
        relatedTeiList.put(relatedTrackedEntityInstance.getUid(), relatedTrackedEntityInstance);
        return getRecursiveRelationatedTeis(relatedTrackedEntityInstance, relatedTeiList);
    }

    private static void pushTeiWithoutRelationFirst(Map<String, TrackedEntityInstance> trackedEntityInstances, DateTime serverDate) {
        List<TrackedEntityInstance> trackerEntityInstancesWithRelations = new ArrayList();
        if (trackedEntityInstances.size() > 0) {
            for (TrackedEntityInstance trackedEntityInstance : trackedEntityInstances.values()) {
                trackerEntityInstancesWithRelations.add(trackedEntityInstance);
                trackedEntityInstance.setRelationships(new ArrayList());
                TrackerController.sendTrackedEntityInstanceChanges(DhisController.getInstance().getDhisApi(), trackedEntityInstance, false);
            }
            for (TrackedEntityInstance trackedEntityInstance2 : trackerEntityInstancesWithRelations) {
                if (trackedEntityInstance2.getRelationships().size() > 0) {
                    trackedEntityInstance2.setFromServer(false);
                    TrackerController.sendTrackedEntityInstanceChanges(DhisController.getInstance().getDhisApi(), trackedEntityInstance2, true);
                    trackedEntityInstance2.setCreated(serverDate.toString());
                    trackedEntityInstance2.setLastUpdated(serverDate.toString());
                    trackedEntityInstance2.save();
                }
            }
        }
    }

    private static void sendTrackedEntityInstance(DhisApi dhisApi, TrackedEntityInstance trackedEntityInstance, boolean sendEnrollments) {
        boolean success;
        if (trackedEntityInstance.getCreated() == null) {
            success = postTrackedEntityInstance(trackedEntityInstance, dhisApi);
        } else {
            success = putTrackedEntityInstance(trackedEntityInstance, dhisApi);
        }
        if (success && sendEnrollments) {
            sendEnrollmentChanges(dhisApi, TrackerController.getEnrollments(trackedEntityInstance), sendEnrollments);
        }
    }

    static void postTrackedEntityInstanceBatch(DhisApi dhisApi, List<TrackedEntityInstance> trackedEntityInstances) throws APIException {
        Map<String, TrackedEntityInstance> trackedEntityInstanceMap = new HashMap();
        try {
            TrackedEntityInstance trackedEntityInstance;
            Map<String, List<TrackedEntityInstance>> map = new HashMap();
            map.put(ApiEndpointContainer.TRACKED_ENTITY_INSTANCES, trackedEntityInstances);
            List<ImportSummary> importSummaries = dhisApi.postTrackedEntityInstances(map).getImportSummaries();
            for (TrackedEntityInstance trackedEntityInstance2 : trackedEntityInstances) {
                trackedEntityInstanceMap.put(trackedEntityInstance2.getUid(), trackedEntityInstance2);
            }
            if (importSummaries != null) {
                DateTime eventUploadTime = dhisApi.getSystemInfo().getServerDate();
                for (ImportSummary importSummary : importSummaries) {
                    TrackedEntityInstance trackedEntityInstance2 = (TrackedEntityInstance) trackedEntityInstanceMap.get(importSummary.getReference());
                    System.out.println("IMPORT SUMMARY: " + importSummary.getDescription());
                    if (importSummary.isSuccessOrOK()) {
                        trackedEntityInstance2.setFromServer(true);
                        trackedEntityInstance2.setCreated(eventUploadTime.toString());
                        trackedEntityInstance2.setLastUpdated(eventUploadTime.toString());
                        trackedEntityInstance2.save();
                        clearFailedItem("TrackedEntityInstance", trackedEntityInstance2.getLocalId());
                    }
                }
            }
        } catch (APIException e) {
            sendTrackedEntityInstanceChanges(dhisApi, (List) trackedEntityInstances, false);
        }
    }

    private static boolean postTrackedEntityInstance(TrackedEntityInstance trackedEntityInstance, DhisApi dhisApi) throws APIException {
        try {
            Response response = dhisApi.postTrackedEntityInstance(trackedEntityInstance);
            if (response.getStatus() != 200) {
                return true;
            }
            ImportSummary importSummary = getImportSummary(response);
            handleImportSummary(importSummary, "TrackedEntityInstance", trackedEntityInstance.getLocalId());
            if (!ImportSummary.SUCCESS.equals(importSummary.getStatus()) && !ImportSummary.OK.equals(importSummary.getStatus())) {
                return true;
            }
            trackedEntityInstance.setFromServer(true);
            trackedEntityInstance.save();
            clearFailedItem("TrackedEntityInstance", trackedEntityInstance.getLocalId());
            UpdateTrackedEntityInstanceTimestamp(trackedEntityInstance, dhisApi);
            return true;
        } catch (APIException apiException) {
            NetworkUtils.handleTrackedEntityInstanceSendException(apiException, trackedEntityInstance);
            return false;
        }
    }

    private static boolean putTrackedEntityInstance(TrackedEntityInstance trackedEntityInstance, DhisApi dhisApi) throws APIException {
        try {
            Response response = dhisApi.putTrackedEntityInstance(trackedEntityInstance.getTrackedEntityInstance(), trackedEntityInstance);
            if (response.getStatus() != 200) {
                return true;
            }
            ImportSummary importSummary = getImportSummary(response);
            handleImportSummary(importSummary, "TrackedEntityInstance", trackedEntityInstance.getLocalId());
            if (!ImportSummary.SUCCESS.equals(importSummary.getStatus()) && !ImportSummary.OK.equals(importSummary.getStatus())) {
                return true;
            }
            trackedEntityInstance.setFromServer(true);
            trackedEntityInstance.save();
            clearFailedItem("TrackedEntityInstance", trackedEntityInstance.getLocalId());
            UpdateTrackedEntityInstanceTimestamp(trackedEntityInstance, dhisApi);
            return true;
        } catch (APIException apiException) {
            NetworkUtils.handleTrackedEntityInstanceSendException(apiException, trackedEntityInstance);
            return false;
        }
    }

    private static void updateTrackedEntityInstanceReferences(long localId, String newTrackedEntityInstanceReference, String oldTempTrackedEntityInstanceReference) {
        new Update(TrackedEntityAttributeValue.class).set(Condition.column(TrackedEntityAttributeValue.Table.TRACKEDENTITYINSTANCEID).is(newTrackedEntityInstanceReference)).where(Condition.column("localTrackedEntityInstanceId").is(Long.valueOf(localId))).async().execute();
        new Update(Event.class).set(Condition.column("trackedEntityInstance").is(newTrackedEntityInstanceReference)).where(Condition.column("trackedEntityInstance").is(oldTempTrackedEntityInstanceReference)).async().execute();
        new Update(Enrollment.class).set(Condition.column("trackedEntityInstance").is(newTrackedEntityInstanceReference)).where(Condition.column("trackedEntityInstance").is(oldTempTrackedEntityInstanceReference)).async().execute();
        long updated = new Update(Relationship.class).set(Condition.column(Relationship.Table.TRACKEDENTITYINSTANCEA).is(newTrackedEntityInstanceReference)).where(Condition.column(Relationship.Table.TRACKEDENTITYINSTANCEA).is(oldTempTrackedEntityInstanceReference)).count() + new Update(Relationship.class).set(Condition.column(Relationship.Table.TRACKEDENTITYINSTANCEB).is(newTrackedEntityInstanceReference)).where(Condition.column(Relationship.Table.TRACKEDENTITYINSTANCEB).is(oldTempTrackedEntityInstanceReference)).count();
        Log.d(CLASS_TAG, "updated relationships: " + updated);
        boolean hasValidRelationship = false;
        if (Utils.isLocal(oldTempTrackedEntityInstanceReference)) {
            List<Relationship> teiIsB = new Select().from(Relationship.class).where(Condition.column(Relationship.Table.TRACKEDENTITYINSTANCEB).is(newTrackedEntityInstanceReference)).queryList();
            List<Relationship> teiIsA = new Select().from(Relationship.class).where(Condition.column(Relationship.Table.TRACKEDENTITYINSTANCEA).is(newTrackedEntityInstanceReference)).queryList();
            if (teiIsB != null) {
                for (Relationship relationship : teiIsB) {
                    if (!Utils.isLocal(relationship.getTrackedEntityInstanceA())) {
                        hasValidRelationship = true;
                    }
                }
            }
            if (teiIsA != null) {
                for (Relationship relationship2 : teiIsA) {
                    if (!Utils.isLocal(relationship2.getTrackedEntityInstanceB())) {
                        hasValidRelationship = true;
                    }
                }
            }
        }
        boolean fullySynced = !hasValidRelationship || updated <= 0;
        new Update(TrackedEntityInstance.class).set(Condition.column("trackedEntityInstance").is(newTrackedEntityInstanceReference), Condition.column("fromServer").is(Boolean.valueOf(fullySynced))).where(Condition.column("localId").is(Long.valueOf(localId))).async().execute();
    }

    private static void UpdateTrackedEntityInstanceTimestamp(TrackedEntityInstance trackedEntityInstance, DhisApi dhisApi) throws APIException {
        try {
            Map<String, String> QUERY_PARAMS = new HashMap();
            QUERY_PARAMS.put("fields", "created,lastUpdated");
            TrackedEntityInstance updatedTrackedEntityInstance = dhisApi.getTrackedEntityInstance(trackedEntityInstance.getTrackedEntityInstance(), QUERY_PARAMS);
            trackedEntityInstance.setCreated(updatedTrackedEntityInstance.getCreated());
            trackedEntityInstance.setLastUpdated(updatedTrackedEntityInstance.getLastUpdated());
            trackedEntityInstance.save();
        } catch (APIException apiException) {
            NetworkUtils.handleApiException(apiException);
        }
    }

    static void clearFailedItem(String type, long id) {
        FailedItem item = TrackerController.getFailedItem(type, id);
        if (item != null) {
            item.async().delete();
        }
    }

    private static void handleImportSummary(ImportSummary importSummary, String type, long id) {
        try {
            if (ImportSummary.ERROR.equals(importSummary.getStatus())) {
                Log.d(CLASS_TAG, "failed.. ");
                NetworkUtils.handleImportSummaryError(importSummary, type, 200, id);
            }
        } catch (Exception e) {
            Log.e(CLASS_TAG, "Unable to process import summary", e);
        }
    }

    private static List<ImportSummary> getImportSummaries(Response response) {
        List<ImportSummary> importSummaries = new ArrayList();
        try {
            if (DhisController.getInstance().getObjectMapper().readTree(new StringConverter().fromBody(response.getBody(), (Type) String.class)) == null) {
                return null;
            }
            String body = new StringConverter().fromBody(response.getBody(), (Type) String.class);
            Log.d(CLASS_TAG, body);
            ApiResponse apiResponse = (ApiResponse) DhisController.getInstance().getObjectMapper().readValue(body, ApiResponse.class);
            if (apiResponse == null || apiResponse.getImportSummaries() == null || apiResponse.getImportSummaries().isEmpty()) {
                return importSummaries;
            }
            return apiResponse.getImportSummaries();
        } catch (ConversionException e) {
            e.printStackTrace();
            return importSummaries;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return importSummaries;
        }
    }

    private static ImportSummary getImportSummary(Response response) {
        try {
            JsonNode node = DhisController.getInstance().getObjectMapper().readTree(new StringConverter().fromBody(response.getBody(), (Type) String.class));
            if (node == null) {
                return null;
            }
            if (node.has("response")) {
                return getPutImportSummary(response);
            }
            return getPostImportSummary(response);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ConversionException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    private static ImportSummary getPostImportSummary(Response response) {
        ImportSummary importSummary = null;
        try {
            String body = new StringConverter().fromBody(response.getBody(), (Type) String.class);
            Log.d(CLASS_TAG, body);
            return (ImportSummary) DhisController.getInstance().getObjectMapper().readValue(body, ImportSummary.class);
        } catch (IOException e) {
            e.printStackTrace();
            return importSummary;
        } catch (ConversionException e2) {
            e2.printStackTrace();
            return importSummary;
        }
    }

    private static ImportSummary getPutImportSummary(Response response) {
        try {
            String body = new StringConverter().fromBody(response.getBody(), (Type) String.class);
            Log.d(CLASS_TAG, body);
            ApiResponse apiResponse = (ApiResponse) DhisController.getInstance().getObjectMapper().readValue(body, ApiResponse.class);
            if (!(apiResponse == null || apiResponse.getImportSummaries() == null || apiResponse.getImportSummaries().isEmpty())) {
                return (ImportSummary) apiResponse.getImportSummaries().get(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ConversionException e2) {
            e2.printStackTrace();
        }
        return null;
    }

    public static void deleteLocallyDeletedEvents(DhisApi dhisApi) {
        List<Event> events = TrackerController.getDeletedEvents();
        Log.d(CLASS_TAG, "got this many events to be removed:" + events.size());
        for (Event event : events) {
            deleteEvent(dhisApi, event);
        }
    }

    static void deleteEvent(DhisApi dhisApi, Event event) throws APIException {
        if (event != null) {
            try {
                Response response = dhisApi.deleteEvent(event.getUid());
                if (response.getStatus() == 200) {
                    ImportSummary importSummary = getImportSummary(response);
                    handleImportSummary(importSummary, "Event", event.getLocalId());
                    if (ImportSummary.SUCCESS.equals(importSummary.getStatus()) || ImportSummary.OK.equals(importSummary.getStatus())) {
                        event.delete();
                        clearFailedItem("Event", event.getLocalId());
                    }
                }
            } catch (APIException apiException) {
                NetworkUtils.handleEventSendException(apiException, event);
            }
        }
    }
}
