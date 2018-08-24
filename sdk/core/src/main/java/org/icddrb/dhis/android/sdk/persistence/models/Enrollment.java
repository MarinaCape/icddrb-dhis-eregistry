package org.icddrb.dhis.android.sdk.persistence.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.builder.Condition.Operation;
import com.raizlabs.android.dbflow.sql.builder.ConditionQueryBuilder;
import com.raizlabs.android.dbflow.sql.language.Update;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.controllers.tracker.TrackerController;
import org.icddrb.dhis.android.sdk.utils.api.CodeGenerator;
import org.joda.time.DateTime;

@JsonInclude(Include.NON_NULL)
public class Enrollment extends BaseSerializableModel {
    public static final String ACTIVE = "ACTIVE";
    public static final String CANCELLED = "CANCELLED";
    private static final String CLASS_TAG = Enrollment.class.getSimpleName();
    public static final String COMPLETED = "COMPLETED";
    @JsonIgnore
    List<TrackedEntityAttributeValue> attributes;
    @JsonIgnore
    String enrollment;
    @JsonProperty("enrollmentDate")
    String enrollmentDate;
    @JsonIgnore
    List<Event> events;
    @JsonProperty("followup")
    boolean followup;
    @JsonProperty("incidentDate")
    String incidentDate;
    @JsonIgnore
    long localTrackedEntityInstanceId;
    @JsonProperty("orgUnit")
    String orgUnit;
    @JsonProperty("program")
    String program;
    @JsonProperty("status")
    String status;
    @JsonIgnore
    String trackedEntityInstance;

    public final class Adapter extends ModelAdapter<Enrollment> {
        public Class<Enrollment> getModelClass() {
            return Enrollment.class;
        }

        public String getTableName() {
            return "Enrollment";
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `Enrollment` (`NAME`, `DISPLAYNAME`, `CREATED`, `LASTUPDATED`, `ACCESS`, `ID`, `FROMSERVER`, `ORGUNIT`, `TRACKEDENTITYINSTANCE`, `LOCALTRACKEDENTITYINSTANCEID`, `PROGRAM`, `ENROLLMENTDATE`, `INCIDENTDATE`, `FOLLOWUP`, `STATUS`, `ENROLLMENT`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, Enrollment model) {
            if (model.name != null) {
                statement.bindString(1, model.name);
            } else {
                statement.bindNull(1);
            }
            if (model.displayName != null) {
                statement.bindString(2, model.displayName);
            } else {
                statement.bindNull(2);
            }
            if (model.created != null) {
                statement.bindString(3, model.created);
            } else {
                statement.bindNull(3);
            }
            if (model.lastUpdated != null) {
                statement.bindString(4, model.lastUpdated);
            } else {
                statement.bindNull(4);
            }
            Object modelaccess = FlowManager.getTypeConverterForClass(Access.class).getDBValue(model.access);
            if (modelaccess != null) {
                statement.bindString(5, (String) modelaccess);
            } else {
                statement.bindNull(5);
            }
            if (model.id != null) {
                statement.bindString(6, model.id);
            } else {
                statement.bindNull(6);
            }
            Object modelfromServer = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.fromServer));
            if (modelfromServer != null) {
                statement.bindLong(7, (long) ((Integer) modelfromServer).intValue());
            } else {
                statement.bindNull(7);
            }
            if (model.orgUnit != null) {
                statement.bindString(8, model.orgUnit);
            } else {
                statement.bindNull(8);
            }
            if (model.trackedEntityInstance != null) {
                statement.bindString(9, model.trackedEntityInstance);
            } else {
                statement.bindNull(9);
            }
            statement.bindLong(10, model.localTrackedEntityInstanceId);
            if (model.program != null) {
                statement.bindString(11, model.program);
            } else {
                statement.bindNull(11);
            }
            if (model.enrollmentDate != null) {
                statement.bindString(12, model.enrollmentDate);
            } else {
                statement.bindNull(12);
            }
            if (model.incidentDate != null) {
                statement.bindString(13, model.incidentDate);
            } else {
                statement.bindNull(13);
            }
            Object modelfollowup = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.followup));
            if (modelfollowup != null) {
                statement.bindLong(14, (long) ((Integer) modelfollowup).intValue());
            } else {
                statement.bindNull(14);
            }
            if (model.status != null) {
                statement.bindString(15, model.status);
            } else {
                statement.bindNull(15);
            }
            if (model.enrollment != null) {
                statement.bindString(16, model.enrollment);
            } else {
                statement.bindNull(16);
            }
        }

        public void bindToContentValues(ContentValues contentValues, Enrollment model) {
            if (model.name != null) {
                contentValues.put("name", model.name);
            } else {
                contentValues.putNull("name");
            }
            if (model.displayName != null) {
                contentValues.put("displayName", model.displayName);
            } else {
                contentValues.putNull("displayName");
            }
            if (model.created != null) {
                contentValues.put("created", model.created);
            } else {
                contentValues.putNull("created");
            }
            if (model.lastUpdated != null) {
                contentValues.put("lastUpdated", model.lastUpdated);
            } else {
                contentValues.putNull("lastUpdated");
            }
            Object modelaccess = FlowManager.getTypeConverterForClass(Access.class).getDBValue(model.access);
            if (modelaccess != null) {
                contentValues.put("access", (String) modelaccess);
            } else {
                contentValues.putNull("access");
            }
            if (model.id != null) {
                contentValues.put("id", model.id);
            } else {
                contentValues.putNull("id");
            }
            Object modelfromServer = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.fromServer));
            if (modelfromServer != null) {
                contentValues.put("fromServer", (Integer) modelfromServer);
            } else {
                contentValues.putNull("fromServer");
            }
            contentValues.put("localId", Long.valueOf(model.localId));
            if (model.orgUnit != null) {
                contentValues.put("orgUnit", model.orgUnit);
            } else {
                contentValues.putNull("orgUnit");
            }
            if (model.trackedEntityInstance != null) {
                contentValues.put("trackedEntityInstance", model.trackedEntityInstance);
            } else {
                contentValues.putNull("trackedEntityInstance");
            }
            contentValues.put("localTrackedEntityInstanceId", Long.valueOf(model.localTrackedEntityInstanceId));
            if (model.program != null) {
                contentValues.put("program", model.program);
            } else {
                contentValues.putNull("program");
            }
            if (model.enrollmentDate != null) {
                contentValues.put(Table.ENROLLMENTDATE, model.enrollmentDate);
            } else {
                contentValues.putNull(Table.ENROLLMENTDATE);
            }
            if (model.incidentDate != null) {
                contentValues.put(Table.INCIDENTDATE, model.incidentDate);
            } else {
                contentValues.putNull(Table.INCIDENTDATE);
            }
            Object modelfollowup = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.followup));
            if (modelfollowup != null) {
                contentValues.put(Table.FOLLOWUP, (Integer) modelfollowup);
            } else {
                contentValues.putNull(Table.FOLLOWUP);
            }
            if (model.status != null) {
                contentValues.put("status", model.status);
            } else {
                contentValues.putNull("status");
            }
            if (model.enrollment != null) {
                contentValues.put("enrollment", model.enrollment);
            } else {
                contentValues.putNull("enrollment");
            }
        }

        public void bindToInsertValues(ContentValues contentValues, Enrollment model) {
            if (model.name != null) {
                contentValues.put("name", model.name);
            } else {
                contentValues.putNull("name");
            }
            if (model.displayName != null) {
                contentValues.put("displayName", model.displayName);
            } else {
                contentValues.putNull("displayName");
            }
            if (model.created != null) {
                contentValues.put("created", model.created);
            } else {
                contentValues.putNull("created");
            }
            if (model.lastUpdated != null) {
                contentValues.put("lastUpdated", model.lastUpdated);
            } else {
                contentValues.putNull("lastUpdated");
            }
            Object modelaccess = FlowManager.getTypeConverterForClass(Access.class).getDBValue(model.access);
            if (modelaccess != null) {
                contentValues.put("access", (String) modelaccess);
            } else {
                contentValues.putNull("access");
            }
            if (model.id != null) {
                contentValues.put("id", model.id);
            } else {
                contentValues.putNull("id");
            }
            Object modelfromServer = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.fromServer));
            if (modelfromServer != null) {
                contentValues.put("fromServer", (Integer) modelfromServer);
            } else {
                contentValues.putNull("fromServer");
            }
            if (model.orgUnit != null) {
                contentValues.put("orgUnit", model.orgUnit);
            } else {
                contentValues.putNull("orgUnit");
            }
            if (model.trackedEntityInstance != null) {
                contentValues.put("trackedEntityInstance", model.trackedEntityInstance);
            } else {
                contentValues.putNull("trackedEntityInstance");
            }
            contentValues.put("localTrackedEntityInstanceId", Long.valueOf(model.localTrackedEntityInstanceId));
            if (model.program != null) {
                contentValues.put("program", model.program);
            } else {
                contentValues.putNull("program");
            }
            if (model.enrollmentDate != null) {
                contentValues.put(Table.ENROLLMENTDATE, model.enrollmentDate);
            } else {
                contentValues.putNull(Table.ENROLLMENTDATE);
            }
            if (model.incidentDate != null) {
                contentValues.put(Table.INCIDENTDATE, model.incidentDate);
            } else {
                contentValues.putNull(Table.INCIDENTDATE);
            }
            Object modelfollowup = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.followup));
            if (modelfollowup != null) {
                contentValues.put(Table.FOLLOWUP, (Integer) modelfollowup);
            } else {
                contentValues.putNull(Table.FOLLOWUP);
            }
            if (model.status != null) {
                contentValues.put("status", model.status);
            } else {
                contentValues.putNull("status");
            }
            if (model.enrollment != null) {
                contentValues.put("enrollment", model.enrollment);
            } else {
                contentValues.putNull("enrollment");
            }
        }

        public boolean exists(Enrollment model) {
            return model.localId > 0;
        }

        public void loadFromCursor(Cursor cursor, Enrollment model) {
            int indexname = cursor.getColumnIndex("name");
            if (indexname != -1) {
                if (cursor.isNull(indexname)) {
                    model.name = null;
                } else {
                    model.name = cursor.getString(indexname);
                }
            }
            int indexdisplayName = cursor.getColumnIndex("displayName");
            if (indexdisplayName != -1) {
                if (cursor.isNull(indexdisplayName)) {
                    model.displayName = null;
                } else {
                    model.displayName = cursor.getString(indexdisplayName);
                }
            }
            int indexcreated = cursor.getColumnIndex("created");
            if (indexcreated != -1) {
                if (cursor.isNull(indexcreated)) {
                    model.created = null;
                } else {
                    model.created = cursor.getString(indexcreated);
                }
            }
            int indexlastUpdated = cursor.getColumnIndex("lastUpdated");
            if (indexlastUpdated != -1) {
                if (cursor.isNull(indexlastUpdated)) {
                    model.lastUpdated = null;
                } else {
                    model.lastUpdated = cursor.getString(indexlastUpdated);
                }
            }
            int indexaccess = cursor.getColumnIndex("access");
            if (indexaccess != -1) {
                if (cursor.isNull(indexaccess)) {
                    model.access = null;
                } else {
                    model.access = (Access) FlowManager.getTypeConverterForClass(Access.class).getModelValue(cursor.getString(indexaccess));
                }
            }
            int indexid = cursor.getColumnIndex("id");
            if (indexid != -1) {
                if (cursor.isNull(indexid)) {
                    model.id = null;
                } else {
                    model.id = cursor.getString(indexid);
                }
            }
            int indexfromServer = cursor.getColumnIndex("fromServer");
            if (indexfromServer != -1) {
                model.fromServer = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexfromServer)))).booleanValue();
            }
            int indexlocalId = cursor.getColumnIndex("localId");
            if (indexlocalId != -1) {
                model.localId = cursor.getLong(indexlocalId);
            }
            int indexorgUnit = cursor.getColumnIndex("orgUnit");
            if (indexorgUnit != -1) {
                if (cursor.isNull(indexorgUnit)) {
                    model.orgUnit = null;
                } else {
                    model.orgUnit = cursor.getString(indexorgUnit);
                }
            }
            int indextrackedEntityInstance = cursor.getColumnIndex("trackedEntityInstance");
            if (indextrackedEntityInstance != -1) {
                if (cursor.isNull(indextrackedEntityInstance)) {
                    model.trackedEntityInstance = null;
                } else {
                    model.trackedEntityInstance = cursor.getString(indextrackedEntityInstance);
                }
            }
            int indexlocalTrackedEntityInstanceId = cursor.getColumnIndex("localTrackedEntityInstanceId");
            if (indexlocalTrackedEntityInstanceId != -1) {
                model.localTrackedEntityInstanceId = cursor.getLong(indexlocalTrackedEntityInstanceId);
            }
            int indexprogram = cursor.getColumnIndex("program");
            if (indexprogram != -1) {
                if (cursor.isNull(indexprogram)) {
                    model.program = null;
                } else {
                    model.program = cursor.getString(indexprogram);
                }
            }
            int indexenrollmentDate = cursor.getColumnIndex(Table.ENROLLMENTDATE);
            if (indexenrollmentDate != -1) {
                if (cursor.isNull(indexenrollmentDate)) {
                    model.enrollmentDate = null;
                } else {
                    model.enrollmentDate = cursor.getString(indexenrollmentDate);
                }
            }
            int indexincidentDate = cursor.getColumnIndex(Table.INCIDENTDATE);
            if (indexincidentDate != -1) {
                if (cursor.isNull(indexincidentDate)) {
                    model.incidentDate = null;
                } else {
                    model.incidentDate = cursor.getString(indexincidentDate);
                }
            }
            int indexfollowup = cursor.getColumnIndex(Table.FOLLOWUP);
            if (indexfollowup != -1) {
                model.followup = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexfollowup)))).booleanValue();
            }
            int indexstatus = cursor.getColumnIndex("status");
            if (indexstatus != -1) {
                if (cursor.isNull(indexstatus)) {
                    model.status = null;
                } else {
                    model.status = cursor.getString(indexstatus);
                }
            }
            int indexenrollment = cursor.getColumnIndex("enrollment");
            if (indexenrollment == -1) {
                return;
            }
            if (cursor.isNull(indexenrollment)) {
                model.enrollment = null;
            } else {
                model.enrollment = cursor.getString(indexenrollment);
            }
        }

        public void updateAutoIncrement(Enrollment model, long id) {
            model.localId = id;
        }

        public long getAutoIncrementingId(Enrollment model) {
            return model.localId;
        }

        public String getAutoIncrementingColumnName() {
            return "localId";
        }

        public ConditionQueryBuilder<Enrollment> getPrimaryModelWhere(Enrollment model) {
            return new ConditionQueryBuilder(Enrollment.class, Condition.column("localId").is(Long.valueOf(model.localId)));
        }

        public ConditionQueryBuilder<Enrollment> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(Enrollment.class, Condition.column("localId").is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return "CREATE TABLE IF NOT EXISTS `Enrollment`(`name` TEXT, `displayName` TEXT, `created` TEXT, `lastUpdated` TEXT, `access` TEXT, `id` TEXT, `fromServer` INTEGER, `localId` INTEGER PRIMARY KEY AUTOINCREMENT, `orgUnit` TEXT, `trackedEntityInstance` TEXT, `localTrackedEntityInstanceId` INTEGER, `program` TEXT, `enrollmentDate` TEXT, `incidentDate` TEXT, `followup` INTEGER, `status` TEXT, `enrollment` TEXT UNIQUE ON CONFLICT FAIL);";
        }

        public final Enrollment newInstance() {
            return new Enrollment();
        }
    }

    public static class EnrollmentComparator implements Comparator<Enrollment> {
        public int compare(Enrollment e1, Enrollment e2) {
            if (e1.getStatus().equals(Enrollment.CANCELLED) || e1.getStatus().equals("COMPLETED")) {
                if ((e2.getStatus().equals(Enrollment.CANCELLED) || e1.getStatus().equals("COMPLETED")) && e1.getCreated() == null && e2.getCreated() != null) {
                    return 1;
                }
                return 0;
            } else if (e2.getStatus().equals(Enrollment.CANCELLED) || e1.getStatus().equals("COMPLETED")) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public final class Table {
        public static final String ACCESS = "access";
        public static final String CREATED = "created";
        public static final String DISPLAYNAME = "displayName";
        public static final String ENROLLMENT = "enrollment";
        public static final String ENROLLMENTDATE = "enrollmentDate";
        public static final String FOLLOWUP = "followup";
        public static final String FROMSERVER = "fromServer";
        public static final String ID = "id";
        public static final String INCIDENTDATE = "incidentDate";
        public static final String LASTUPDATED = "lastUpdated";
        public static final String LOCALID = "localId";
        public static final String LOCALTRACKEDENTITYINSTANCEID = "localTrackedEntityInstanceId";
        public static final String NAME = "name";
        public static final String ORGUNIT = "orgUnit";
        public static final String PROGRAM = "program";
        public static final String STATUS = "status";
        public static final String TABLE_NAME = "Enrollment";
        public static final String TRACKEDENTITYINSTANCE = "trackedEntityInstance";
    }

    public Enrollment() {
        this.enrollment = CodeGenerator.generateCode();
    }

    public Enrollment(Enrollment enrollment) {
        this.orgUnit = enrollment.orgUnit;
        this.trackedEntityInstance = enrollment.trackedEntityInstance;
        this.localTrackedEntityInstanceId = enrollment.localTrackedEntityInstanceId;
        this.program = enrollment.program;
        this.enrollmentDate = enrollment.enrollmentDate;
        this.incidentDate = enrollment.incidentDate;
        this.followup = enrollment.followup;
        this.status = enrollment.status;
        this.enrollment = enrollment.enrollment;
    }

    public Enrollment(String organisationUnit, String trackedEntityInstance, Program program, String enrollmentDate, String incidentDate) {
        this.orgUnit = organisationUnit;
        this.status = "ACTIVE";
        this.enrollment = CodeGenerator.generateCode();
        this.followup = false;
        this.fromServer = false;
        this.program = program.getUid();
        this.trackedEntityInstance = trackedEntityInstance;
        this.enrollmentDate = enrollmentDate;
        this.incidentDate = incidentDate;
        List<Event> events = new ArrayList();
        for (ProgramStage programStage : program.getProgramStages()) {
            if (programStage.getAutoGenerateEvent()) {
                String status = Event.STATUS_FUTURE_VISIT;
                DateTime dueDate = new DateTime((Object) enrollmentDate).plusDays(programStage.getMinDaysFromStart());
                events.add(new Event(organisationUnit, status, program.id, programStage, trackedEntityInstance, this.enrollment, dueDate.toString("yyyy-MM-dd")));
            }
        }
        if (!events.isEmpty()) {
            setEvents(events);
        }
    }

    @JsonProperty("enrollment")
    public String getEnrollment() {
        return this.enrollment;
    }

    @JsonProperty("enrollment")
    public void setEnrollment(String enrollment) {
        this.enrollment = enrollment;
    }

    @JsonAnySetter
    public void handleUnknown(String key, Object value) {
    }

    @JsonProperty("trackedEntityInstance")
    public String getTrackedEntityInstance() {
        return this.trackedEntityInstance;
    }

    @JsonProperty("trackedEntityInstance")
    public void setTrackedEntityInstance(String trackedEntityInstance) {
        this.trackedEntityInstance = trackedEntityInstance;
    }

    @JsonProperty("attributes")
    public List<TrackedEntityAttributeValue> getAttributes() {
        if (this.attributes == null) {
            this.attributes = new ArrayList();
            for (ProgramTrackedEntityAttribute ptea : MetaDataController.getProgramTrackedEntityAttributes(this.program)) {
                TrackedEntityAttributeValue v = TrackerController.getTrackedEntityAttributeValue(ptea.trackedEntityAttribute, this.localTrackedEntityInstanceId);
                if (!(v == null || v.getValue() == null || v.getValue().isEmpty())) {
                    this.attributes.add(v);
                }
            }
        }
        return this.attributes;
    }

    public void setAttributes(List<TrackedEntityAttributeValue> attributes) {
        this.attributes = attributes;
    }

    public List<Event> getEvents(boolean reLoad) {
        if (this.events == null || reLoad) {
            this.events = TrackerController.getEventsByEnrollment(this.localId);
        }
        return this.events;
    }

    public void save() {
        Enrollment existingEnrollment = TrackerController.getEnrollment(this.enrollment);
        if (existingEnrollment != null) {
            this.localId = existingEnrollment.localId;
        }
        if (getEnrollment() != null || TrackerController.getEnrollment(this.localId) == null) {
            super.save();
        } else {
            updateManually();
        }
        if (this.events != null) {
            for (Event event : this.events) {
                event.setLocalEnrollmentId(this.localId);
                event.save();
            }
        }
        if (this.attributes != null) {
            for (TrackedEntityAttributeValue value : this.attributes) {
                value.setLocalTrackedEntityInstanceId(this.localTrackedEntityInstanceId);
                value.save();
            }
        }
    }

    public void update() {
        save();
    }

    public void updateManually() {
        new Update(Enrollment.class).set(Condition.column("status").is(this.status), Condition.column("fromServer").is(Boolean.valueOf(this.fromServer)), Condition.column(Table.FOLLOWUP).is(Boolean.valueOf(this.followup))).where(Condition.column("localId").is(Long.valueOf(this.localId))).queryClose();
    }

    @JsonIgnore
    public Program getProgram() {
        if (this.program == null) {
            return null;
        }
        return MetaDataController.getProgram(this.program);
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getOrgUnit() {
        return this.orgUnit;
    }

    public void setOrgUnit(String orgUnit) {
        this.orgUnit = orgUnit;
    }

    public long getLocalTrackedEntityInstanceId() {
        return this.localTrackedEntityInstanceId;
    }

    public void setLocalTrackedEntityInstanceId(long localTrackedEntityInstanceId) {
        this.localTrackedEntityInstanceId = localTrackedEntityInstanceId;
    }

    public String getEnrollmentDate() {
        return this.enrollmentDate;
    }

    public void setEnrollmentDate(String enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public String getIncidentDate() {
        return this.incidentDate;
    }

    public void setIncidentDate(String incidentDate) {
        this.incidentDate = incidentDate;
    }

    public boolean getFollowup() {
        return this.followup;
    }

    public void setFollowup(boolean followup) {
        this.followup = followup;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Event> getEvents() {
        return this.events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    @JsonIgnore
    public String getUid() {
        return this.enrollment;
    }

    @JsonIgnore
    public void setUid(String uid) {
        this.enrollment = uid;
    }

    public boolean equals(Enrollment enrollment) {
        if (enrollment == null) {
            return false;
        }
        if (this.enrollmentDate == null && enrollment.getEnrollmentDate() != null) {
            return false;
        }
        if (this.enrollmentDate != null && !this.enrollmentDate.equals(enrollment.getEnrollmentDate())) {
            return false;
        }
        if (this.incidentDate == null && enrollment.getIncidentDate() != null) {
            return false;
        }
        if (this.incidentDate != null && !this.incidentDate.equals(enrollment.getIncidentDate())) {
            return false;
        }
        if (this.status == null && enrollment.getStatus() != null) {
            return false;
        }
        if (this.status == null || this.status.equals(enrollment.getStatus())) {
            return true;
        }
        return false;
    }
}
