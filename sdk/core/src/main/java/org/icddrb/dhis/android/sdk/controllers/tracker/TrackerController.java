package org.icddrb.dhis.android.sdk.controllers.tracker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.From;
import com.raizlabs.android.dbflow.sql.language.Join.JoinType;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.queriable.StringQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.icddrb.dhis.android.sdk.R;
import org.icddrb.dhis.android.sdk.controllers.LoadingController;
import org.icddrb.dhis.android.sdk.controllers.ResourceController;
import org.icddrb.dhis.android.sdk.controllers.SyncStrategy;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.events.LoadingMessageEvent.EventType;
import org.icddrb.dhis.android.sdk.events.UiEvent;
import org.icddrb.dhis.android.sdk.events.UiEvent.UiEventType;
import org.icddrb.dhis.android.sdk.network.APIException;
import org.icddrb.dhis.android.sdk.network.DhisApi;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.models.DataValue;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.FailedItem;
import org.icddrb.dhis.android.sdk.persistence.models.OrganisationUnit;
import org.icddrb.dhis.android.sdk.persistence.models.Program;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramTrackedEntityAttribute;
import org.icddrb.dhis.android.sdk.persistence.models.Relationship;
import org.icddrb.dhis.android.sdk.persistence.models.Relationship.Table;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttribute;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.persistence.preferences.DateTimeManager;
import org.icddrb.dhis.android.sdk.persistence.preferences.ResourceType;
import org.icddrb.dhis.android.sdk.utils.UiUtils;
import org.icddrb.dhis.android.sdk.utils.api.ProgramType;
import org.icddrb.dhis.android.sdk.utils.support.DateUtils;
import org.icddrb.dhis.android.sdk.utils.support.expression.Expression;
import org.joda.time.DateTime;

public final class TrackerController extends ResourceController {
    private static final String CLASS_TAG = "DataValueController";

    private TrackerController() {
    }

    public static boolean isDataLoaded(Context context) {
        Log.d(CLASS_TAG, "isdatavaluesloaded..");
        if (context == null) {
            return false;
        }
        if (LoadingController.isLoadFlagEnabled(context, ResourceType.EVENTS) && DateTimeManager.getInstance().getLastUpdated(ResourceType.EVENTS) == null) {
            return false;
        }
        Log.d(CLASS_TAG, "data values are loaded.");
        return true;
    }

    public static TrackedEntityInstance getTrackedEntityInstanceByUid(String trackedEntityInstanceUid) {
        return (TrackedEntityInstance) new Select().from(TrackedEntityInstance.class).where(Condition.column("trackedEntityInstance").is(trackedEntityInstanceUid)).querySingle();
    }

    public static List<Relationship> getRelationships(String trackedEntityInstance) {
        return new Select().from(Relationship.class).where(Condition.column(Table.TRACKEDENTITYINSTANCEA).is(trackedEntityInstance)).or(Condition.column(Table.TRACKEDENTITYINSTANCEB).is(trackedEntityInstance)).queryList();
    }

    public static List<Enrollment> getEnrollments(String program, String organisationUnit) {
        return new Select().from(Enrollment.class).where(Condition.column("program").is(program)).and(Condition.column("orgUnit").is(organisationUnit)).orderBy(false, Enrollment.Table.ENROLLMENTDATE).queryList();
    }

    public static List<Enrollment> getEnrollmentsForProgram(String program) {
        return new Select().from(Enrollment.class).where(Condition.column("program").is(program)).orderBy(false, Enrollment.Table.ENROLLMENTDATE).queryList();
    }

    public static List<Enrollment> getEnrollments(TrackedEntityInstance trackedEntityInstance) {
        return new Select().from(Enrollment.class).where(Condition.column("localTrackedEntityInstanceId").is(Long.valueOf(trackedEntityInstance.getLocalId()))).queryList();
    }

    public static List<Enrollment> getEnrollments(TrackedEntityInstance trackedEntityInstance, String programUId, String orgUnit) {
        return new Select().from(Enrollment.class).where(Condition.column("localTrackedEntityInstanceId").is(Long.valueOf(trackedEntityInstance.getLocalId()))).and(Condition.column("status").is("COMPLETED")).and(Condition.column("program").is(programUId)).and(Condition.column("orgUnit").is(orgUnit)).queryList();
    }

    public static List<Enrollment> getEnrollments(String program, TrackedEntityInstance trackedEntityInstance) {
        return new Select().from(Enrollment.class).where(Condition.column("program").is(program)).and(Condition.column("localTrackedEntityInstanceId").is(Long.valueOf(trackedEntityInstance.getLocalId()))).queryList();
    }

    public static Enrollment getLastEnrollment(String program, TrackedEntityInstance trackedEntityInstance) {
        return (Enrollment) new Select().from(Enrollment.class).where(Condition.column("program").is(program)).and(Condition.column("localTrackedEntityInstanceId").is(Long.valueOf(trackedEntityInstance.getLocalId()))).orderBy(false, "localId").querySingle();
    }

    public static Enrollment getCancelledEnrollment(String enrollment) {
        return (Enrollment) new Select().from(Enrollment.class).where(Condition.column("enrollment").is(enrollment)).and(Condition.column("status").is(Enrollment.CANCELLED)).querySingle();
    }

    public static Enrollment getNotCancelledEnrollment(String enrollment) {
        return (Enrollment) new Select().from(Enrollment.class).where(Condition.column("enrollment").is(enrollment)).and(Condition.column("status").isNot(Enrollment.CANCELLED)).querySingle();
    }

    public static Enrollment getEnrollment(String enrollment) {
        return (Enrollment) new Select().from(Enrollment.class).where(Condition.column("enrollment").is(enrollment)).querySingle();
    }

    public static Enrollment getEnrollment(long localEnrollmentId) {
        return (Enrollment) new Select().from(Enrollment.class).where(Condition.column("localId").is(Long.valueOf(localEnrollmentId))).querySingle();
    }

    public static List<Event> getScheduledEventsWithActiveEnrollments(String programId, String orgUnitId, String startDate, String endDate) {
        List<Enrollment> activeEnrollments = new Select().from(Enrollment.class).where(Condition.column("program").is(programId)).and(Condition.column("status").is("ACTIVE")).queryTableList();
        endDate = new DateTime((Object) endDate).plusDays(1).toString("YYYY-MM-dd");
        String activeEnrollmentsSqlSafeString = getSqlSafeStringFromListOfEnrollments(activeEnrollments);
        Date today = new Date();
        if (DateUtils.getDefaultDate(startDate).before(today)) {
            startDate = DateUtils.getMediumDateString(today);
        }
        StringBuilder stringBuilder = new StringBuilder();
        From from = new Select().from(Event.class);
        return new StringQuery(Event.class, stringBuilder.append(from.where(Condition.column("programId").is(programId)).and(Condition.column("organisationUnitId").is(orgUnitId)).and(Condition.column("status").isNot("COMPLETED")).and(Condition.column(Event.Table.EVENTDATE).isNull()).and(Condition.column(Event.Table.DUEDATE).isNotNull()).and(Condition.column(Event.Table.DUEDATE).between(startDate).and(endDate)).and(Condition.column("enrollment")).toString()).append(" IN ").append(activeEnrollmentsSqlSafeString).append(" ORDER BY ").append(Event.Table.DUEDATE).toString()).queryList();
    }

    public static List<Event> getAllConflictingAndNotConflictingEvents(String organisationUnitId, String programId, boolean isFromServer) {
        return new Select().from(Event.class).join(FailedItem.class, JoinType.LEFT).on(Condition.column(FailedItem.Table.ITEMID).eq("localId")).where(Condition.column("organisationUnitId").is(organisationUnitId)).and(Condition.column("programId").is(programId)).and(Condition.column("fromServer").is(Boolean.valueOf(isFromServer))).or(Condition.column(FailedItem.Table.ITEMTYPE).is("Event")).orderBy(false, "lastUpdated").queryList();
    }

    public static void syncRemotelyDeletedData(Context context, DhisApi dhisApi) throws APIException {
        UiUtils.postProgressMessage(context.getString(R.string.synchronize_deleted_data), EventType.REMOVE_DATA);
        TrackerDataLoader.deleteRemotelyDeletedData(context, dhisApi);
        Dhis2Application.getEventBus().post(new UiEvent(UiEventType.SYNCING_END));
        UiUtils.postProgressMessage("", EventType.FINISH);
    }

    public static List<Event> getEventsByEnrollment(String enrollment) {
        return new Select().from(Event.class).where(Condition.column("enrollment").is(enrollment)).queryList();
    }

    public static List<Event> getEventsByEnrollment(long localEnrollmentId) {
        return new Select().from(Event.class).where(Condition.column(Event.Table.LOCALENROLLMENTID).is(Long.valueOf(localEnrollmentId))).and(Condition.column("status").isNot(Event.STATUS_DELETED)).queryList();
    }

    public static List<Event> getNotDeletedEvents(String organisationUnitId, String programId) {
        return new Select().from(Event.class).where(Condition.column("organisationUnitId").is(organisationUnitId)).and(Condition.column("programId").is(programId)).and(Condition.column("status").isNot(Event.STATUS_DELETED)).orderBy(false, "lastUpdated").queryList();
    }

    public static List<Event> getDeletedEvents() {
        return new Select().from(Event.class).where(Condition.column("status").is(Event.STATUS_DELETED)).orderBy(false, "lastUpdated").queryList();
    }

    public static List<Event> getEventsThatHasEnrollments(String organisationUnitId, String programId) {
        return new Select().from(Event.class).where(Condition.column("organisationUnitId").is(organisationUnitId)).and(Condition.column("programId").is(programId)).and(Condition.column("enrollment").isNotNull()).orderBy(false, "lastUpdated").queryList();
    }

    public static Event getEvent(long localId) {
        return (Event) new Select().from(Event.class).where(Condition.column("localId").is(Long.valueOf(localId))).querySingle();
    }

    public static Event getEvent(long localEnrollment, String programStage) {
        return (Event) new Select().from(Event.class).where(Condition.column(Event.Table.LOCALENROLLMENTID).is(Long.valueOf(localEnrollment)), Condition.column(Event.Table.PROGRAMSTAGEID).is(programStage)).querySingle();
    }

    public static Event getEventByUid(String event) {
        return (Event) new Select().from(Event.class).where(Condition.column("event").is(event)).querySingle();
    }

    public static DataValue getDataValue(long eventId, String dataElement) {
        return (DataValue) new Select().from(DataValue.class).where(Condition.column(DataValue.Table.LOCALEVENTID).is(Long.valueOf(eventId)), Condition.column("dataElement").is(dataElement)).querySingle();
    }

    public static TrackedEntityInstance getTrackedEntityInstance(String trackedEntityInstance) {
        return (TrackedEntityInstance) new Select().from(TrackedEntityInstance.class).where(Condition.column("trackedEntityInstance").is(trackedEntityInstance)).querySingle();
    }

    public static TrackedEntityInstance getTrackedEntityInstance(long localId) {
        return (TrackedEntityInstance) new Select().from(TrackedEntityInstance.class).where(Condition.column("localId").is(Long.valueOf(localId))).querySingle();
    }

    public static List<TrackedEntityInstance> getTrackedEntityInstances(String organisationUnitUId) {
        return new Select().from(TrackedEntityInstance.class).where(Condition.column("orgUnit").is(organisationUnitUId)).queryList();
    }

    public static List<TrackedEntityInstance> getTrackedEntityInstances() {
        return new Select().from(TrackedEntityInstance.class).queryList();
    }

    public static List<TrackedEntityAttributeValue> getProgramTrackedEntityAttributeValues(Program program, TrackedEntityInstance trackedEntityInstance) {
        List<TrackedEntityAttributeValue> programTrackedEntityAttributeValues = new ArrayList();
        for (ProgramTrackedEntityAttribute ptea : MetaDataController.getProgramTrackedEntityAttributes(program.getUid())) {
            TrackedEntityAttributeValue v = getTrackedEntityAttributeValue(ptea.getTrackedEntityAttributeId(), trackedEntityInstance.getLocalId());
            if (!(v == null || v.getValue() == null || v.getValue().isEmpty())) {
                programTrackedEntityAttributeValues.add(v);
            }
        }
        return programTrackedEntityAttributeValues;
    }

    public static TrackedEntityAttributeValue getTrackedEntityAttributeValue(String trackedEntityAttribute, String trackedEntityInstance) {
        return (TrackedEntityAttributeValue) new Select().from(TrackedEntityAttributeValue.class).where(Condition.column(TrackedEntityAttributeValue.Table.TRACKEDENTITYATTRIBUTEID).is(trackedEntityAttribute), Condition.column(TrackedEntityAttributeValue.Table.TRACKEDENTITYINSTANCEID).is(trackedEntityInstance)).querySingle();
    }

    public static List<TrackedEntityAttributeValue> getTrackedEntityAttributeValues(String trackedEntityInstance) {
        return new Select().from(TrackedEntityAttributeValue.class).where(Condition.column(TrackedEntityAttributeValue.Table.TRACKEDENTITYINSTANCEID).is(trackedEntityInstance)).queryList();
    }

    public static TrackedEntityAttributeValue getTrackedEntityAttributeValue(String trackedEntityAttribute, long trackedEntityInstance) {
        List<TrackedEntityAttributeValue> trackedEntityAttributeValue = new Select().from(TrackedEntityAttributeValue.class).where(Condition.column(TrackedEntityAttributeValue.Table.TRACKEDENTITYATTRIBUTEID).eq(trackedEntityAttribute)).and(Condition.column("localTrackedEntityInstanceId").eq(Long.valueOf(trackedEntityInstance))).queryList();
        if (trackedEntityAttributeValue == null || trackedEntityAttributeValue.size() <= 0) {
            return null;
        }
        return (TrackedEntityAttributeValue) trackedEntityAttributeValue.get(0);
    }

    public static List<TrackedEntityAttributeValue> getTrackedEntityAttributeValues(long trackedEntityInstance) {
        return new Select().from(TrackedEntityAttributeValue.class).where(Condition.column("localTrackedEntityInstanceId").is(Long.valueOf(trackedEntityInstance))).orderBy(TrackedEntityAttributeValue.Table.TRACKEDENTITYATTRIBUTEID).queryList();
    }

    public static List<TrackedEntityAttributeValue> getVisibleTrackedEntityAttributeValues(long trackedEntityInstance) {
        return new Select().from(TrackedEntityAttributeValue.class).join(TrackedEntityAttribute.class, JoinType.LEFT).on(Condition.column("id").eq(TrackedEntityAttributeValue.Table.TRACKEDENTITYATTRIBUTEID)).where(Condition.column("localTrackedEntityInstanceId").is(Long.valueOf(trackedEntityInstance))).and(Condition.column(TrackedEntityAttribute.Table.DISPLAYINLISTNOPROGRAM).is(Boolean.valueOf(true))).orderBy(true, TrackedEntityAttribute.Table.SORTORDERINLISTNOPROGRAM).queryList();
    }

    public static List<FailedItem> getFailedItems() {
        List<FailedItem> failedItems = new Select().from(FailedItem.class).queryList();
        if (failedItems == null || failedItems.size() <= 0) {
            return null;
        }
        return failedItems;
    }

    public static List<FailedItem> getFailedItems(String type) {
        return new Select().from(FailedItem.class).where(Condition.column(FailedItem.Table.ITEMTYPE).is(type)).queryList();
    }

    public static FailedItem getFailedItem(String type, long id) {
        return (FailedItem) new Select().from(FailedItem.class).where(Condition.column(FailedItem.Table.ITEMTYPE).is(type), Condition.column(FailedItem.Table.ITEMID).is(Long.valueOf(id))).querySingle();
    }

    public static void clearDataValueLoadedFlags() {
        for (OrganisationUnit organisationUnit : MetaDataController.getAssignedOrganisationUnits()) {
            if (organisationUnit.getId() != null) {
                List<Program> programsForOrgUnit = new ArrayList();
                List<Program> programsForOrgUnitSEWoR = MetaDataController.getProgramsForOrganisationUnit(organisationUnit.getId(), ProgramType.WITHOUT_REGISTRATION);
                if (programsForOrgUnitSEWoR != null) {
                    programsForOrgUnit.addAll(programsForOrgUnitSEWoR);
                }
                for (Program program : programsForOrgUnit) {
                    if (program.getUid() == null) {
                        break;
                    }
                    DateTimeManager.getInstance().deleteLastUpdated(ResourceType.EVENTS, organisationUnit.getId() + program.getUid());
                }
            } else {
                return;
            }
        }
    }

    public static void loadDataValues(Context context, DhisApi dhisApi, SyncStrategy syncStrategy) throws APIException {
        UiUtils.postProgressMessage(context.getString(R.string.loading_metadata), EventType.METADATA);
        TrackerDataLoader.updateDataValueDataItems(context, dhisApi, syncStrategy);
    }

    public static List<TrackedEntityInstance> queryTrackedEntityInstancesDataFromServer(DhisApi dhisApi, String organisationUnitUid, String programUid, String queryString, TrackedEntityAttributeValue... params) throws APIException {
        return TrackerDataLoader.queryTrackedEntityInstanceDataFromServer(dhisApi, organisationUnitUid, programUid, queryString, params);
    }

    public static List<TrackedEntityInstance> queryTrackedEntityInstancesDataFromAllAccessibleOrgUnits(DhisApi dhisApi, String organisationUnitUid, String programUid, String queryString, boolean detailedSearch, TrackedEntityAttributeValue... params) throws APIException {
        return TrackerDataLoader.queryTrackedEntityInstancesDataFromAllAccessibleOrgunits(dhisApi, organisationUnitUid, programUid, queryString, detailedSearch, params);
    }

    public static List<TrackedEntityInstance> getTrackedEntityInstancesDataFromServer(DhisApi dhisApi, List<TrackedEntityInstance> trackedEntityInstances, boolean getEnrollments, boolean getRecursiveRelations) throws APIException {
        return TrackerDataLoader.getTrackedEntityInstancesDataFromServer(dhisApi, trackedEntityInstances, getEnrollments, getRecursiveRelations);
    }

    public static void getEnrollmentDataFromServer(DhisApi dhisApi, String uid, boolean getEvents, DateTime serverDateTime) throws APIException {
        TrackerDataLoader.getEnrollmentDataFromServer(dhisApi, uid, getEvents, serverDateTime);
    }

    public static List<Enrollment> getEnrollmentDataFromServer(DhisApi dhisApi, TrackedEntityInstance trackedEntityInstance, DateTime serverDateTime) throws APIException {
        return TrackerDataLoader.getEnrollmentsDataFromServer(dhisApi, trackedEntityInstance, serverDateTime);
    }

    public static void getEventDataFromServer(DhisApi dhisApi, String uid) throws APIException {
        TrackerDataLoader.getEventDataFromServer(dhisApi, uid);
    }

    public static void sendEventChanges(DhisApi dhisApi, Event event) throws APIException {
        TrackerDataSender.sendEventChanges(dhisApi, event);
    }

    public static void sendEnrollmentChanges(DhisApi dhisApi, Enrollment enrollment, boolean sendEvents) throws APIException {
        TrackerDataSender.sendEnrollmentChanges(dhisApi, enrollment, sendEvents);
    }

    public static void sendTrackedEntityInstanceChanges(DhisApi dhisApi, TrackedEntityInstance trackedEntityInstance, boolean sendEnrollments) throws APIException {
        TrackerDataSender.sendTrackedEntityInstanceChanges(dhisApi, trackedEntityInstance, sendEnrollments);
    }

    public static List<Enrollment> getActiveEnrollments() {
        List<Enrollment> activeEnrollments = new Select().from(Enrollment.class).where(Condition.column("status").eq("ACTIVE")).queryList();
        return activeEnrollments != null ? activeEnrollments : new ArrayList();
    }

    public static List<Event> getOverdueEventsWithActiveEnrollments(String mProgramId, String mOrgUnitId) {
        String activeEnrollmentsSqlSafeString = getSqlSafeStringFromListOfEnrollments(new Select().from(Enrollment.class).where(Condition.column("program").is(mProgramId)).and(Condition.column("status").is("ACTIVE")).queryList());
        String beginningOfTime = DateUtils.getMediumDateString(new Date(0));
        String today = DateUtils.getMediumDateString();
        StringBuilder stringBuilder = new StringBuilder();
        From from = new Select().from(Event.class);
        return new StringQuery(Event.class, stringBuilder.append(from.where(Condition.column("programId").eq(mProgramId)).and(Condition.column("organisationUnitId").eq(mOrgUnitId)).and(Condition.column(Event.Table.DUEDATE).between(beginningOfTime).and(today)).and(Condition.column("status").isNot("COMPLETED")).and(Condition.column("status").isNot(Event.STATUS_SKIPPED)).and(Condition.column("enrollment")).toString()).append(" IN ").append(activeEnrollmentsSqlSafeString).append(" ORDER BY ").append(Event.Table.DUEDATE).toString()).queryList();
    }

    @NonNull
    private static String getSqlSafeStringFromListOfEnrollments(List<Enrollment> activeEnrollments) {
        String activeEnrollmentsSqlSafeString = Expression.PAR_OPEN;
        for (int i = 0; i < activeEnrollments.size() - 1; i++) {
            activeEnrollmentsSqlSafeString = activeEnrollmentsSqlSafeString + "'" + ((Enrollment) activeEnrollments.get(i)).getEnrollment() + "', ";
        }
        if (activeEnrollments.size() > 0) {
            return activeEnrollmentsSqlSafeString + "'" + ((Enrollment) activeEnrollments.get(activeEnrollments.size() - 1)).getEnrollment() + "')";
        }
        return activeEnrollmentsSqlSafeString + Expression.PAR_CLOSE;
    }

    public static List<Event> getActiveEventsWithActiveEnrollments(String mProgramId, String mOrgUnitId, String mStartDate, String mEndDate) {
        String activeEnrollmentsSqlSafeString = getSqlSafeStringFromListOfEnrollments(new Select().from(Enrollment.class).where(Condition.column("program").is(mProgramId)).and(Condition.column("status").is("ACTIVE")).queryTableList());
        StringBuilder stringBuilder = new StringBuilder();
        From from = new Select().from(Event.class);
        return new StringQuery(Event.class, stringBuilder.append(from.where(Condition.column("programId").is(mProgramId)).and(Condition.column("organisationUnitId").is(mOrgUnitId)).and(Condition.column("status").isNot(Event.STATUS_SKIPPED)).and(Condition.column(Event.Table.EVENTDATE).isNotNull()).and(Condition.column(Event.Table.EVENTDATE).between(mStartDate).and(mEndDate)).and(Condition.column("enrollment")).toString()).append(" IN ").append(activeEnrollmentsSqlSafeString).append(" ORDER BY ").append(Event.Table.DUEDATE).toString()).queryList();
    }

    public static void refreshRelationsByTrackedEntity(DhisApi dhisApi, String trackedEntityInstance) {
        TrackerDataLoader.refreshRelationshipsByTrackedEntityInstance(dhisApi, trackedEntityInstance);
    }

    public static void updateTrackedEntityInstances(DhisApi dhisApi, List<TrackedEntityInstance> trackedEntityInstances, DateTime serverDateTime) {
        for (TrackedEntityInstance trackedEntityInstance : trackedEntityInstances) {
            TrackerDataLoader.getTrackedEntityInstanceDataFromServer(dhisApi, trackedEntityInstance.getUid(), true, true, serverDateTime);
        }
    }

    public static int countTrackedEntityAttributeValue(TrackedEntityAttributeValue value) {
        return (int) new Select().count().from(TrackedEntityAttributeValue.class).where(Condition.column("value").eq(value.getValue())).and(Condition.column(TrackedEntityAttributeValue.Table.TRACKEDENTITYATTRIBUTEID).eq(value.getTrackedEntityAttributeId())).and(Condition.column("localTrackedEntityInstanceId").isNot(Long.valueOf(value.getLocalTrackedEntityInstanceId()))).count();
    }
}
