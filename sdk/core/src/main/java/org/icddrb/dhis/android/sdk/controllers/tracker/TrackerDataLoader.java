package org.icddrb.dhis.android.sdk.controllers.tracker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.icddrb.dhis.android.sdk.C0845R;
import org.icddrb.dhis.android.sdk.controllers.ApiEndpointContainer;
import org.icddrb.dhis.android.sdk.controllers.LoadingController;
import org.icddrb.dhis.android.sdk.controllers.ResourceController;
import org.icddrb.dhis.android.sdk.controllers.SyncStrategy;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.controllers.wrappers.EventsWrapper;
import org.icddrb.dhis.android.sdk.events.LoadingMessageEvent.EventType;
import org.icddrb.dhis.android.sdk.events.OnTeiDownloadedEvent;
import org.icddrb.dhis.android.sdk.events.UiEvent;
import org.icddrb.dhis.android.sdk.events.UiEvent.UiEventType;
import org.icddrb.dhis.android.sdk.network.APIException;
import org.icddrb.dhis.android.sdk.network.DhisApi;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.models.BaseValue;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.OrganisationUnit;
import org.icddrb.dhis.android.sdk.persistence.models.Program;
import org.icddrb.dhis.android.sdk.persistence.models.Relationship;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.persistence.models.meta.DbOperation;
import org.icddrb.dhis.android.sdk.persistence.preferences.DateTimeManager;
import org.icddrb.dhis.android.sdk.persistence.preferences.ResourceType;
import org.icddrb.dhis.android.sdk.utils.DbUtils;
import org.icddrb.dhis.android.sdk.utils.NetworkUtils;
import org.icddrb.dhis.android.sdk.utils.UiUtils;
import org.icddrb.dhis.android.sdk.utils.Utils;
import org.icddrb.dhis.android.sdk.utils.api.ProgramType;
import org.joda.time.DateTime;

final class TrackerDataLoader extends ResourceController {
    public static final String CLASS_TAG = TrackerDataLoader.class.getSimpleName();

    private TrackerDataLoader() {
    }

    static void updateDataValueDataItems(Context context, DhisApi dhisApi, SyncStrategy syncStrategy) throws APIException {
        if (dhisApi != null) {
            DateTime serverDateTime = dhisApi.getSystemInfo().getServerDate();
            List<OrganisationUnit> assignedOrganisationUnits = MetaDataController.getAssignedOrganisationUnits();
            Hashtable<String, List<Program>> programsForOrganisationUnits = new Hashtable();
            List<TrackedEntityInstance> trackedEntityInstances = MetaDataController.getTrackedEntityInstancesFromServer();
            if (syncStrategy.equals(SyncStrategy.DOWNLOAD_ALL)) {
                TrackerController.updateTrackedEntityInstances(dhisApi, trackedEntityInstances, serverDateTime);
            }
            updateEventsForEnrollments(context, dhisApi, TrackerController.getActiveEnrollments(), serverDateTime);
            if (LoadingController.isLoadFlagEnabled(context, ResourceType.EVENTS)) {
                for (OrganisationUnit organisationUnit : assignedOrganisationUnits) {
                    if (!(organisationUnit.getId() == null || organisationUnit.getId().length() == Utils.randomUUID.length())) {
                        List<Program> programsForOrgUnit = new ArrayList();
                        List<Program> programsForOrgUnitFromDB = MetaDataController.getProgramsForOrganisationUnit(organisationUnit.getId(), ProgramType.WITHOUT_REGISTRATION, ProgramType.WITH_REGISTRATION);
                        if (programsForOrgUnitFromDB != null) {
                            programsForOrgUnit.addAll(programsForOrgUnitFromDB);
                        }
                        programsForOrganisationUnits.put(organisationUnit.getId(), programsForOrgUnit);
                    }
                }
                for (OrganisationUnit organisationUnit2 : assignedOrganisationUnits) {
                    if (!(organisationUnit2.getId() == null || organisationUnit2.getId().length() == Utils.randomUUID.length())) {
                        for (Program program : (List) programsForOrganisationUnits.get(organisationUnit2.getId())) {
                            if (!(program.getUid() == null || program.getUid().length() == Utils.randomUUID.length())) {
                                if (ResourceController.shouldLoad(serverDateTime, ResourceType.EVENTS, organisationUnit2.getId() + program.getUid())) {
                                    UiUtils.postProgressMessage(context.getString(C0845R.string.loading_events) + ": " + organisationUnit2.getLabel() + ": " + program.getName(), EventType.DATA);
                                    try {
                                        getEventsDataFromServer(dhisApi, syncStrategy, organisationUnit2.getId(), program.getUid(), serverDateTime);
                                    } catch (APIException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                }
            }
            UiUtils.postProgressMessage("", EventType.FINISH);
        }
    }

    static void updateEventsForEnrollments(Context context, DhisApi dhisApi, List<Enrollment> enrollments, DateTime serverDateTime) {
        DateTime lastUpdated = DateTimeManager.getInstance().getLastUpdated(ResourceType.EVENTS);
        String delimiter = ";";
        boolean failed = false;
        String trackedEntityInstanceQueryParams = "trackedEntityInstance";
        Map<String, String> QUERY_MAP_FULL = new HashMap();
        Map<String, List<Enrollment>> programToEnrollmentMap = mapActiveEnrollmentsByProgram(enrollments);
        List<Event> eventsFromServer = new ArrayList();
        if (lastUpdated != null) {
            QUERY_MAP_FULL.put("lastUpdated", lastUpdated.toString());
        }
        if (programToEnrollmentMap.keySet().size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (String programUid : programToEnrollmentMap.keySet()) {
                for (Enrollment enrollment : (List) programToEnrollmentMap.get(programUid)) {
                    sb.append(enrollment.getTrackedEntityInstance() + delimiter);
                }
                QUERY_MAP_FULL.put(trackedEntityInstanceQueryParams, sb.toString());
                try {
                    if (dhisApi.getEventsForTrackedEntityInstance(programUid, QUERY_MAP_FULL) != null) {
                        eventsFromServer.addAll(dhisApi.getEventsForTrackedEntityInstance(programUid, QUERY_MAP_FULL));
                    }
                } catch (APIException apiException) {
                    apiException.printStackTrace();
                    failed = true;
                }
            }
        }
        if (!failed) {
            ResourceController.saveResourceDataFromServer(ResourceType.EVENTS, dhisApi, (List) eventsFromServer, null, serverDateTime);
            DateTimeManager.getInstance().setLastUpdated(ResourceType.EVENTS, serverDateTime);
        }
    }

    static void deleteRemotelyDeletedTrackedEntityInstances(Context context, DhisApi dhisApi, Hashtable<String, List<Program>> myProgramsByOrganisationUnit) {
        for (String organisationUnitUid : myProgramsByOrganisationUnit.keySet()) {
            UiUtils.postProgressMessage(context.getString(C0845R.string.sync_deleted_tracked_entities) + ": " + organisationUnitUid, EventType.REMOVE_DATA);
            HashMap<String, List<TrackedEntityInstance>> mapTrackedEntityInstances = groupTrackedEntityInstancesByTrackedEntity(TrackerController.getTrackedEntityInstances(organisationUnitUid));
            for (String trackedEntity : mapTrackedEntityInstances.keySet()) {
                try {
                    deleteRemotelyDeletedTrackedEntityInstances(dhisApi, organisationUnitUid, trackedEntity, (List) mapTrackedEntityInstances.get(trackedEntity));
                } catch (APIException e) {
                    e.printStackTrace();
                }
            }
        }
        deleteRemotelyDeletedEnrollments(context, dhisApi, myProgramsByOrganisationUnit);
    }

    private static void deleteRemotelyDeletedEnrollments(Context context, DhisApi dhisApi, Hashtable<String, List<Program>> myProgramsByOrganisationUnit) {
        for (String organisationUnitUid : myProgramsByOrganisationUnit.keySet()) {
            UiUtils.postProgressMessage(context.getString(C0845R.string.sync_deleted_enrollments) + ": " + organisationUnitUid, EventType.REMOVE_DATA);
            HashMap<String, List<TrackedEntityInstance>> mapTrackedEntityInstances = groupTrackedEntityInstancesByTrackedEntity(TrackerController.getTrackedEntityInstances(organisationUnitUid));
            for (String trackedEntity : mapTrackedEntityInstances.keySet()) {
                List<TrackedEntityInstance> trackedEntityInstanceList = (List) mapTrackedEntityInstances.get(trackedEntity);
                ArrayList<Enrollment> enrollments = new ArrayList();
                for (TrackedEntityInstance trackedEntityInstance : trackedEntityInstanceList) {
                    enrollments.addAll(TrackerController.getEnrollments(trackedEntityInstance));
                }
                try {
                    deleteRemotelyDeletedEnrollments(dhisApi, organisationUnitUid, trackedEntity, enrollments);
                } catch (APIException e) {
                    e.printStackTrace();
                }
            }
        }
        deleteRemotelyDeletedRelationships(context, dhisApi, myProgramsByOrganisationUnit);
    }

    private static void deleteRemotelyDeletedRelationships(Context context, DhisApi dhisApi, Hashtable<String, List<Program>> myProgramsByOrganisationUnit) {
        for (String organisationUnitUid : myProgramsByOrganisationUnit.keySet()) {
            for (Program program : (List) myProgramsByOrganisationUnit.get(organisationUnitUid)) {
                if (!(program.getUid() == null || program.getUid().length() == Utils.randomUUID.length())) {
                    UiUtils.postProgressMessage(context.getString(C0845R.string.sync_deleted_relations) + ": " + organisationUnitUid + ": " + program.getName(), EventType.REMOVE_DATA);
                    try {
                        for (TrackedEntityInstance trackedEntityInstance : TrackerController.getTrackedEntityInstances()) {
                            if (trackedEntityInstance.getRelationships() != null && trackedEntityInstance.getRelationships().size() > 0) {
                                refreshRelationshipsByTrackedEntityInstance(dhisApi, trackedEntityInstance.getUid());
                            }
                        }
                    } catch (APIException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @NonNull
    private static HashMap<String, List<TrackedEntityInstance>> groupTrackedEntityInstancesByTrackedEntity(List<TrackedEntityInstance> localTrackedEntityInstances) {
        HashMap<String, List<TrackedEntityInstance>> mapTrackedEntityInstances = new HashMap();
        for (TrackedEntityInstance localTrackedEntityInstance : localTrackedEntityInstances) {
            String trackedEntityUid = localTrackedEntityInstance.getTrackedEntity();
            if (mapTrackedEntityInstances.containsKey(trackedEntityUid)) {
                ((List) mapTrackedEntityInstances.get(trackedEntityUid)).add(localTrackedEntityInstance);
            } else {
                List<TrackedEntityInstance> trackedEntityInstancesList = new ArrayList();
                trackedEntityInstancesList.add(localTrackedEntityInstance);
                mapTrackedEntityInstances.put(trackedEntityUid, trackedEntityInstancesList);
            }
        }
        return mapTrackedEntityInstances;
    }

    static void deleteRemotelyDeletedData(Context context, DhisApi dhisApi) throws APIException {
        Hashtable hashtable = new Hashtable();
        Hashtable<String, List<Program>> myProgramsByOrganisationUnit = MetaDataController.getAssignedProgramsByOrganisationUnit();
        for (String organisationUnitUid : myProgramsByOrganisationUnit.keySet()) {
            for (Program program : (List) myProgramsByOrganisationUnit.get(organisationUnitUid)) {
                if (!(program.getUid() == null || program.getUid().length() == Utils.randomUUID.length())) {
                    UiUtils.postProgressMessage(context.getString(C0845R.string.sync_deleted_events) + ": " + organisationUnitUid + ": " + program.getName(), EventType.REMOVE_DATA);
                    try {
                        deleteRemotelyDeletedEvents(dhisApi, organisationUnitUid, program.getUid());
                    } catch (APIException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        deleteRemotelyDeletedTrackedEntityInstances(context, dhisApi, myProgramsByOrganisationUnit);
    }

    private static void deleteRemotelyDeletedEnrollments(DhisApi dhisApi, String organisationUnitUid, String trackedEntityUid, ArrayList<Enrollment> enrollments) {
        Log.d(CLASS_TAG, "getTrackedEntityInstances");
        Map<String, String> map = new HashMap();
        map.put("fields", "enrollment");
        map.put("trackedEntity", trackedEntityUid);
        if (enrollments.size() != 0) {
            List<Enrollment> remoteEnrollments = new ArrayList();
            Map<String, List<Enrollment>> enrollmentList = dhisApi.getEnrollmentsByOrgUnit(organisationUnitUid, map);
            for (String enrollmentKey : enrollmentList.keySet()) {
                remoteEnrollments.addAll((Collection) enrollmentList.get(enrollmentKey));
            }
            List<Enrollment> localEnrollmentToBeRemoved = new ArrayList();
            Iterator it = enrollments.iterator();
            while (it.hasNext()) {
                Enrollment enrollment = (Enrollment) it.next();
                boolean isRemoved = true;
                for (Enrollment remoteEnrollment : remoteEnrollments) {
                    if (remoteEnrollment.getUid().equals(enrollment.getUid())) {
                        isRemoved = false;
                        break;
                    }
                }
                if (isRemoved) {
                    localEnrollmentToBeRemoved.add(enrollment);
                }
            }
            ResourceController.removeTrackedEntityEnrollments(localEnrollmentToBeRemoved);
        }
    }

    private static void deleteRemotelyDeletedTrackedEntityInstances(DhisApi dhisApi, String organisationUnitUid, String trackedEntityUid, List<TrackedEntityInstance> localTrackedEntityInstances) {
        Log.d(CLASS_TAG, "getTrackedEntityInstances");
        Map<String, String> map = new HashMap();
        map.put("fields", "trackedEntityInstance,[attributes]");
        map.put("trackedEntity", trackedEntityUid);
        if (localTrackedEntityInstances.size() != 0) {
            List<TrackedEntityInstance> remoteTrackedEntityInstances = new ArrayList();
            Map<String, List<TrackedEntityInstance>> trackedEntityInstancesList = dhisApi.getTrackedEntityInstances(organisationUnitUid, map);
            for (String trackedEntityInstanceKey : trackedEntityInstancesList.keySet()) {
                remoteTrackedEntityInstances.addAll((Collection) trackedEntityInstancesList.get(trackedEntityInstanceKey));
            }
            List<TrackedEntityInstance> localTrackedEntityInstancesToBeRemoved = new ArrayList();
            for (TrackedEntityInstance trackedEntityInstance : localTrackedEntityInstances) {
                boolean isRemoved = true;
                for (TrackedEntityInstance remoteTrackedEntityInstance : remoteTrackedEntityInstances) {
                    if (remoteTrackedEntityInstance.getUid().equals(trackedEntityInstance.getUid()) && remoteTrackedEntityInstance.getAttributes().size() > 1) {
                        isRemoved = false;
                        break;
                    }
                }
                if (isRemoved) {
                    localTrackedEntityInstancesToBeRemoved.add(trackedEntityInstance);
                }
            }
            ResourceController.removeTrackedEntityInstances(localTrackedEntityInstancesToBeRemoved);
        }
    }

    static void deleteRemotelyDeletedEvents(DhisApi dhisApi, String organisationUnitUid, String programUid) throws APIException {
        Log.d(CLASS_TAG, "getEventsDataFromServer");
        Map<String, String> map = new HashMap();
        map.put("fields", "[event]");
        map.put("skipPaging", BaseValue.TRUE);
        List<Event> localEvents = TrackerController.getAllConflictingAndNotConflictingEvents(organisationUnitUid, programUid, true);
        if (localEvents.size() != 0) {
            List<Event> remoteEvents = EventsWrapper.getEvents(dhisApi.getEventUids(programUid, organisationUnitUid, map));
            List<Event> eventsToBeRemoved = new ArrayList();
            for (Event localEvent : localEvents) {
                boolean isRemoved = true;
                for (Event remoteEvent : remoteEvents) {
                    if (remoteEvent.getEvent().equals(localEvent.getEvent())) {
                        isRemoved = false;
                        break;
                    }
                }
                if (isRemoved) {
                    eventsToBeRemoved.add(localEvent);
                }
            }
            ResourceController.removeEvents(eventsToBeRemoved);
        }
    }

    static Map<String, List<Enrollment>> mapActiveEnrollmentsByProgram(List<Enrollment> enrollments) {
        Map<String, List<Enrollment>> programToEnrollmentMap = new HashMap();
        for (Enrollment enrollment : enrollments) {
            if (!(enrollment == null || enrollment.getProgram() == null || !"ACTIVE".equals(enrollment.getStatus()) || TextUtils.isEmpty(enrollment.getTrackedEntityInstance()))) {
                if (programToEnrollmentMap.containsKey(enrollment.getProgram().getUid())) {
                    ((List) programToEnrollmentMap.get(enrollment.getProgram().getUid())).add(enrollment);
                } else {
                    List<Enrollment> enrollmentForProgram = new ArrayList();
                    enrollmentForProgram.add(enrollment);
                    programToEnrollmentMap.put(enrollment.getProgram().getUid(), enrollmentForProgram);
                }
            }
        }
        return programToEnrollmentMap;
    }

    static void updateEnrollments(Context context, DhisApi dhisApi, List<Enrollment> list) {
        DateTime lastUpdated = DateTimeManager.getInstance().getLastUpdated(ResourceType.ENROLLMENTS);
    }

    static void updateEvents(Context context, DhisApi dhisApi, List<Event> events) {
        DateTime lastUpdated = DateTimeManager.getInstance().getLastUpdated(ResourceType.EVENTS);
        Map<String, String> QUERY_MAP_FULL = new HashMap();
        if (events != null && events.size() > 0) {
            QUERY_MAP_FULL.put("program", "");
        }
    }

    static void getEventsDataFromServer(DhisApi dhisApi, SyncStrategy syncStrategy, String organisationUnitUid, String programUid, DateTime serverDateTime) throws APIException {
        if (dhisApi != null) {
            Log.d(CLASS_TAG, "getEventsDataFromServer");
            DateTime lastUpdated = null;
            if (syncStrategy == SyncStrategy.DOWNLOAD_ONLY_NEW) {
                DateTimeManager.getInstance().getLastUpdated(ResourceType.EVENTS, organisationUnitUid + programUid);
            }
            Map<String, String> map = new HashMap();
            map.put("fields", "[:all]");
            if (lastUpdated != null) {
                map.put("lastUpdated", lastUpdated.toString());
            }
            ResourceController.saveResourceDataFromServer(ResourceType.EVENTS, organisationUnitUid + programUid, dhisApi, (List) NetworkUtils.unwrapResponse(dhisApi.getNEvents(programUid, organisationUnitUid, map), ApiEndpointContainer.EVENTS), null, serverDateTime);
        }
    }

    static TrackedEntityInstance queryTrackedEntityInstanceDataFromServer(DhisApi dhisApi, String trackedEntityInstanceUid) throws APIException {
        Map<String, String> map = new HashMap();
        if (dhisApi == null) {
            return null;
        }
        return dhisApi.getTrackedEntityInstance(trackedEntityInstanceUid, map);
    }

    static List<TrackedEntityInstance> queryTrackedEntityInstanceDataFromServer(DhisApi dhisApi, String organisationUnitUid, String programUid, String queryString, TrackedEntityAttributeValue... params) throws APIException {
        if (dhisApi == null) {
            return null;
        }
        Map<String, String> QUERY_MAP_FULL = new HashMap();
        if (programUid != null) {
            QUERY_MAP_FULL.put("program", programUid);
        }
        List<TrackedEntityAttributeValue> valueParams = new LinkedList();
        if (params != null) {
            for (TrackedEntityAttributeValue teav : params) {
                if (!(teav == null || teav.getValue() == null || teav.getValue().isEmpty())) {
                    valueParams.add(teav);
                }
            }
        }
        for (TrackedEntityAttributeValue val : valueParams) {
            if (QUERY_MAP_FULL.containsKey("filter")) {
                QUERY_MAP_FULL.put("filter", ((String) QUERY_MAP_FULL.get("filter")) + "&filter=" + val.getTrackedEntityAttributeId() + ":LIKE:" + val.getValue());
            } else {
                QUERY_MAP_FULL.put("filter", val.getTrackedEntityAttributeId() + ":LIKE:" + val.getValue());
            }
        }
        if (!(queryString == null || queryString.isEmpty() || !valueParams.isEmpty())) {
            QUERY_MAP_FULL.put("query", "LIKE:" + queryString);
        }
        return NetworkUtils.unwrapResponse(dhisApi.getTrackedEntityInstances(organisationUnitUid, QUERY_MAP_FULL), ApiEndpointContainer.TRACKED_ENTITY_INSTANCES);
    }

    static List<TrackedEntityInstance> queryTrackedEntityInstancesDataFromAllAccessibleOrgunits(DhisApi dhisApi, String organisationUnitUid, String programUid, String queryString, boolean detailedSearch, TrackedEntityAttributeValue... params) throws APIException {
        if (dhisApi == null) {
            return null;
        }
        Map<String, String> QUERY_MAP_FULL = new HashMap();
        if (programUid != null) {
            QUERY_MAP_FULL.put("program", programUid);
        }
        List<TrackedEntityAttributeValue> valueParams = new LinkedList();
        if (params != null) {
            for (TrackedEntityAttributeValue teav : params) {
                if (!(teav == null || teav.getValue() == null || teav.getValue().isEmpty())) {
                    valueParams.add(teav);
                }
            }
        }
        for (TrackedEntityAttributeValue val : valueParams) {
            if (MetaDataController.getTrackedEntityAttribute(val.getTrackedEntityAttributeId()).getOptionSet() != null) {
                if (QUERY_MAP_FULL.containsKey("filter")) {
                    QUERY_MAP_FULL.put("filter", ((String) QUERY_MAP_FULL.get("filter")) + "&filter=" + val.getTrackedEntityAttributeId() + ":EQ:" + val.getValue());
                } else {
                    QUERY_MAP_FULL.put("filter", val.getTrackedEntityAttributeId() + ":EQ:" + val.getValue());
                }
            } else if (QUERY_MAP_FULL.containsKey("filter")) {
                QUERY_MAP_FULL.put("filter", ((String) QUERY_MAP_FULL.get("filter")) + "&filter=" + val.getTrackedEntityAttributeId() + ":LIKE:" + val.getValue());
            } else {
                QUERY_MAP_FULL.put("filter", val.getTrackedEntityAttributeId() + ":LIKE:" + val.getValue());
            }
        }
        if (!(queryString == null || queryString.isEmpty() || !valueParams.isEmpty())) {
            QUERY_MAP_FULL.put("query", "LIKE:" + queryString);
        }
        return NetworkUtils.unwrapResponse(dhisApi.getTrackedEntityInstancesFromAllAccessibleOrgUnits(organisationUnitUid, QUERY_MAP_FULL), ApiEndpointContainer.TRACKED_ENTITY_INSTANCES);
    }

    static List<TrackedEntityInstance> getTrackedEntityInstancesDataFromServer(DhisApi dhisApi, List<TrackedEntityInstance> trackedEntityInstances, boolean getEnrollments, boolean getRecursiveRelations) {
        if (trackedEntityInstances == null || dhisApi == null) {
            return null;
        }
        DateTime serverDateTime = dhisApi.getSystemInfo().getServerDate();
        List<TrackedEntityInstance> trackedEntityInstancesToReturn = new ArrayList();
        int teiIndex = 0;
        while (teiIndex < trackedEntityInstances.size()) {
            int userFriendlyIndex = (int) Math.ceil(((double) (teiIndex + 1)) / 2.0d);
            try {
                trackedEntityInstancesToReturn.add(getTrackedEntityInstanceDataFromServer(dhisApi, ((TrackedEntityInstance) trackedEntityInstances.get(teiIndex)).getTrackedEntityInstance(), getEnrollments, getRecursiveRelations, serverDateTime));
                Dhis2Application.getEventBus().post(new OnTeiDownloadedEvent(OnTeiDownloadedEvent.EventType.UPDATE, trackedEntityInstances.size(), userFriendlyIndex));
                teiIndex++;
            } catch (APIException e) {
                e.printStackTrace();
                Dhis2Application.getEventBus().post(new OnTeiDownloadedEvent(OnTeiDownloadedEvent.EventType.ERROR, trackedEntityInstances.size(), userFriendlyIndex));
                return new ArrayList();
            }
        }
        return trackedEntityInstancesToReturn;
    }

    static TrackedEntityInstance getTrackedEntityInstanceDataFromServer(DhisApi dhisApi, String uid, boolean getEnrollments, boolean getRecursiveRelations, DateTime serverDateTime) throws APIException {
        if (dhisApi == null) {
            return null;
        }
        DateTime lastUpdated = DateTimeManager.getInstance().getLastUpdated(ResourceType.TRACKEDENTITYINSTANCE, uid);
        Log.d(CLASS_TAG, "get tei " + uid);
        TrackedEntityInstance trackedEntityInstance = updateTrackedEntityInstance(dhisApi, uid, lastUpdated);
        Log.d(CLASS_TAG, "get tei1 " + uid);
        DbOperation.save(trackedEntityInstance).getModel().save();
        List<DbOperation> operations = new ArrayList();
        if (trackedEntityInstance.getAttributes() != null) {
            for (TrackedEntityAttributeValue value : trackedEntityInstance.getAttributes()) {
                if (value != null) {
                    value.setTrackedEntityInstanceId(trackedEntityInstance.getTrackedEntityInstance());
                    value.setLocalTrackedEntityInstanceId(trackedEntityInstance.getLocalId());
                    operations.add(DbOperation.save(value));
                }
            }
        }
        if (trackedEntityInstance.getRelationships() != null) {
            for (Relationship relationship : trackedEntityInstance.getRelationships()) {
                if (relationship != null) {
                    relationship.async().save();
                    operations.add(DbOperation.save(relationship));
                }
            }
        }
        DbUtils.applyBatch(operations);
        if (getRecursiveRelations && trackedEntityInstance.getRelationships() != null) {
            for (Relationship relationship2 : trackedEntityInstance.getRelationships()) {
                if (relationship2 != null) {
                    String targetTrackedEntity = relationship2.getTrackedEntityInstanceB();
                    if (trackedEntityInstance.getTrackedEntityInstance().equals(targetTrackedEntity)) {
                        targetTrackedEntity = relationship2.getTrackedEntityInstanceA();
                    }
                    if (TrackerController.getTrackedEntityInstanceByUid(targetTrackedEntity) == null) {
                        getTrackedEntityInstanceDataFromServer(dhisApi, targetTrackedEntity, getEnrollments, false, serverDateTime);
                    }
                }
            }
        }
        DateTimeManager.getInstance().setLastUpdated(ResourceType.TRACKEDENTITYINSTANCE, uid, serverDateTime);
        if (!getEnrollments) {
            return trackedEntityInstance;
        }
        getEnrollmentsDataFromServer(dhisApi, trackedEntityInstance, serverDateTime);
        return trackedEntityInstance;
    }

    private static TrackedEntityInstance updateTrackedEntityInstance(DhisApi dhisApi, String uid, DateTime lastUpdated) throws APIException {
        return dhisApi.getTrackedEntityInstance(uid, new HashMap());
    }

    static List<Enrollment> getEnrollmentsDataFromServer(DhisApi dhisApi, TrackedEntityInstance trackedEntityInstance, DateTime serverDateTime) throws APIException {
        List<Enrollment> list = null;
        if (!(trackedEntityInstance == null || dhisApi == null)) {
            DateTime lastUpdated = DateTimeManager.getInstance().getLastUpdated(ResourceType.ENROLLMENTS, trackedEntityInstance.getTrackedEntityInstance());
            if (serverDateTime == null) {
                serverDateTime = dhisApi.getSystemInfo().getServerDate();
            }
            list = NetworkUtils.unwrapResponse(dhisApi.getEnrollments(trackedEntityInstance.getTrackedEntityInstance(), ResourceController.getBasicQueryMap(lastUpdated)), ApiEndpointContainer.ENROLLMENTS);
            for (Enrollment enrollment : list) {
                enrollment.setLocalTrackedEntityInstanceId(trackedEntityInstance.getLocalId());
            }
            ResourceController.saveResourceDataFromServer(ResourceType.ENROLLMENTS, trackedEntityInstance.getTrackedEntityInstance(), dhisApi, (List) list, TrackerController.getEnrollments(trackedEntityInstance), serverDateTime);
            list = TrackerController.getEnrollments(trackedEntityInstance);
            if (list != null) {
                for (Enrollment enrollment2 : list) {
                    try {
                        getEventsDataFromServer(dhisApi, SyncStrategy.DOWNLOAD_ONLY_NEW, enrollment2, serverDateTime);
                    } catch (APIException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return list;
    }

    static void getEnrollmentDataFromServer(DhisApi dhisApi, String uid, boolean getEvents, DateTime serverDateTime) throws APIException {
        if (dhisApi != null) {
            DateTime lastUpdated = DateTimeManager.getInstance().getLastUpdated(ResourceType.ENROLLMENT, uid);
            if (serverDateTime == null) {
                serverDateTime = dhisApi.getSystemInfo().getServerDate();
            }
            Enrollment enrollment = updateEnrollment(dhisApi, uid, lastUpdated);
            DbOperation.save(enrollment).getModel().save();
            DateTimeManager.getInstance().setLastUpdated(ResourceType.ENROLLMENT, uid, serverDateTime);
            if (getEvents) {
                getEventsDataFromServer(dhisApi, SyncStrategy.DOWNLOAD_ONLY_NEW, enrollment, serverDateTime);
            }
        }
    }

    private static Enrollment updateEnrollment(DhisApi dhisApi, String uid, DateTime lastUpdated) throws APIException {
        if (dhisApi == null) {
            return null;
        }
        return dhisApi.getEnrollment(uid, new HashMap());
    }

    static void getEventsDataFromServer(DhisApi dhisApi, SyncStrategy syncStrategy, Enrollment enrollment, DateTime serverDateTime) {
        if (enrollment != null && dhisApi != null) {
            if (enrollment.getProgram() == null) {
                Log.d(CLASS_TAG, "Enrollment:" + enrollment.getUid());
                return;
            }
            DateTime lastUpdated = null;
            if (syncStrategy == SyncStrategy.DOWNLOAD_ONLY_NEW) {
                lastUpdated = DateTimeManager.getInstance().getLastUpdated(ResourceType.EVENTS, enrollment.getEnrollment());
            }
            List<Event> events = EventsWrapper.getEvents(dhisApi.getEventsForEnrollment(enrollment.getProgram().getUid(), enrollment.getStatus(), enrollment.getTrackedEntityInstance(), ResourceController.getBasicQueryMap(lastUpdated)));
            List<Event> invalidEvents = new ArrayList();
            for (Event event : events) {
                event.setLocalEnrollmentId(enrollment.getLocalId());
                if (event.getOrganisationUnitId() == null) {
                    invalidEvents.add(event);
                }
            }
            events.removeAll(invalidEvents);
            ResourceController.saveResourceDataFromServer(ResourceType.EVENTS, enrollment.getUid(), dhisApi, (List) events, TrackerController.getEventsByEnrollment(enrollment.getLocalId()), serverDateTime);
        }
    }

    static void getEventDataFromServer(DhisApi dhisApi, String uid) throws APIException {
        DateTime lastUpdated = DateTimeManager.getInstance().getLastUpdated(ResourceType.EVENTS, uid);
        DateTime serverDateTime = dhisApi.getSystemInfo().getServerDate();
        DbOperation.save(updateEvent(dhisApi, uid, lastUpdated)).getModel().save();
        DateTimeManager.getInstance().setLastUpdated(ResourceType.EVENT, uid, serverDateTime);
    }

    private static Event updateEvent(DhisApi dhisApi, String uid, DateTime lastUpdated) throws APIException {
        if (dhisApi == null) {
            return null;
        }
        return dhisApi.getEvent(uid, new HashMap());
    }

    protected static void refreshRelationshipsByTrackedEntityInstance(DhisApi dhisApi, String trackedEntityInstanceUid) {
        Log.d(CLASS_TAG, "refreshRelationshipsByTrackedEntityInstance");
        Map<String, String> map = new HashMap();
        try {
            map.put("fields", "relationships[relationship,displayName,trackedEntityInstanceA,trackedEntityInstanceB]");
            List<Relationship> localRelationships = TrackerController.getRelationships(trackedEntityInstanceUid);
            List<Relationship> remoteRelationships = dhisApi.getTrackedEntityInstance(trackedEntityInstanceUid, map).getRelationships();
            ResourceController.overwriteRelationsFromServer(remoteRelationships, localRelationships);
            if (remoteRelationships.size() == 0) {
                Dhis2Application.getEventBus().post(new UiEvent(UiEventType.SYNCING_END));
                return;
            }
            List<String> notSavedTrackedEntityInstanceUIds = getNotSavedTrackedEntityInstancesUIds(trackedEntityInstanceUid, remoteRelationships);
            if (notSavedTrackedEntityInstanceUIds.size() > 0) {
                for (String notSavedTrackedEntityInstanceUid : notSavedTrackedEntityInstanceUIds) {
                    TrackedEntityInstance remoteTrackedEntityInstance = queryTrackedEntityInstanceDataFromServer(dhisApi, notSavedTrackedEntityInstanceUid);
                    if (remoteTrackedEntityInstance != null) {
                        getTrackedEntityInstanceDataFromServer(dhisApi, remoteTrackedEntityInstance.getUid(), true, false, dhisApi.getSystemInfo().getServerDate());
                    }
                }
            }
        } catch (Exception e) {
            Log.d(CLASS_TAG, "An error occurred refreshing relations");
            e.printStackTrace();
        }
    }

    private static List<String> getNotSavedTrackedEntityInstancesUIds(String trackedEntityInstanceUid, List<Relationship> remoteRelationships) {
        List<String> notSavedTrackedEntityInstances = new ArrayList();
        for (Relationship remoteRelationship : remoteRelationships) {
            String remoteTrackedEntityInstanceA = remoteRelationship.getTrackedEntityInstanceA();
            String remoteTrackedEntityInstanceB = remoteRelationship.getTrackedEntityInstanceB();
            if (remoteTrackedEntityInstanceA.equals(trackedEntityInstanceUid)) {
                addNotSavedUids(notSavedTrackedEntityInstances, remoteTrackedEntityInstanceB);
            } else {
                addNotSavedUids(notSavedTrackedEntityInstances, remoteTrackedEntityInstanceA);
            }
        }
        return notSavedTrackedEntityInstances;
    }

    private static void addNotSavedUids(List<String> notSavedTrackedEntityInstances, String trackedEntityInstance) {
        if (TrackerController.getTrackedEntityInstanceByUid(trackedEntityInstance) == null) {
            notSavedTrackedEntityInstances.add(trackedEntityInstance);
        }
    }
}
