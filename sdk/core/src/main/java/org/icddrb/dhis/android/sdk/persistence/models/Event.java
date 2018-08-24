package org.icddrb.dhis.android.sdk.persistence.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.builder.Condition.Operation;
import com.raizlabs.android.dbflow.sql.builder.ConditionQueryBuilder;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.Update;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.icddrb.dhis.android.sdk.controllers.tracker.TrackerController;
import org.icddrb.dhis.android.sdk.utils.api.CodeGenerator;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({"modelAdapter"})
public class Event extends BaseSerializableModel {
    private static final String CLASS_TAG = "Event";
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_DELETED = "DELETED";
    public static final String STATUS_FUTURE_VISIT = "SCHEDULE";
    public static final String STATUS_OVERDUE = "OVERDUE";
    public static final String STATUS_SKIPPED = "SKIPPED";
    @JsonProperty("completedDate")
    String completedDate;
    @JsonProperty("dataValues")
    List<DataValue> dataValues;
    @JsonProperty("dueDate")
    String dueDate;
    @JsonInclude(Include.NON_NULL)
    @JsonProperty("enrollment")
    String enrollment;
    @JsonIgnore
    String event;
    @JsonProperty("eventDate")
    String eventDate;
    @JsonIgnore
    Double latitude;
    @JsonIgnore
    long localEnrollmentId;
    @JsonIgnore
    Double longitude;
    @JsonInclude(Include.NON_NULL)
    @JsonProperty("orgUnit")
    String organisationUnitId;
    @JsonProperty("program")
    String programId;
    @JsonProperty("programStage")
    String programStageId;
    @JsonProperty("status")
    String status;
    @JsonInclude(Include.NON_NULL)
    @JsonProperty("trackedEntityInstance")
    String trackedEntityInstance;

    public final class Adapter extends ModelAdapter<Event> {
        public Class<Event> getModelClass() {
            return Event.class;
        }

        public String getTableName() {
            return "Event";
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `Event` (`NAME`, `DISPLAYNAME`, `CREATED`, `LASTUPDATED`, `ACCESS`, `ID`, `FROMSERVER`, `EVENT`, `STATUS`, `LATITUDE`, `LONGITUDE`, `TRACKEDENTITYINSTANCE`, `LOCALENROLLMENTID`, `ENROLLMENT`, `PROGRAMID`, `PROGRAMSTAGEID`, `ORGANISATIONUNITID`, `EVENTDATE`, `DUEDATE`, `COMPLETEDDATE`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, Event model) {
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
            if (model.event != null) {
                statement.bindString(8, model.event);
            } else {
                statement.bindNull(8);
            }
            if (model.status != null) {
                statement.bindString(9, model.status);
            } else {
                statement.bindNull(9);
            }
            if (model.latitude != null) {
                statement.bindDouble(10, model.latitude.doubleValue());
            } else {
                statement.bindNull(10);
            }
            if (model.longitude != null) {
                statement.bindDouble(11, model.longitude.doubleValue());
            } else {
                statement.bindNull(11);
            }
            if (model.trackedEntityInstance != null) {
                statement.bindString(12, model.trackedEntityInstance);
            } else {
                statement.bindNull(12);
            }
            statement.bindLong(13, model.localEnrollmentId);
            if (model.enrollment != null) {
                statement.bindString(14, model.enrollment);
            } else {
                statement.bindNull(14);
            }
            if (model.programId != null) {
                statement.bindString(15, model.programId);
            } else {
                statement.bindNull(15);
            }
            if (model.programStageId != null) {
                statement.bindString(16, model.programStageId);
            } else {
                statement.bindNull(16);
            }
            if (model.organisationUnitId != null) {
                statement.bindString(17, model.organisationUnitId);
            } else {
                statement.bindNull(17);
            }
            if (model.eventDate != null) {
                statement.bindString(18, model.eventDate);
            } else {
                statement.bindNull(18);
            }
            if (model.dueDate != null) {
                statement.bindString(19, model.dueDate);
            } else {
                statement.bindNull(19);
            }
            if (model.completedDate != null) {
                statement.bindString(20, model.completedDate);
            } else {
                statement.bindNull(20);
            }
        }

        public void bindToContentValues(ContentValues contentValues, Event model) {
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
            if (model.event != null) {
                contentValues.put("event", model.event);
            } else {
                contentValues.putNull("event");
            }
            if (model.status != null) {
                contentValues.put("status", model.status);
            } else {
                contentValues.putNull("status");
            }
            if (model.latitude != null) {
                contentValues.put(Table.LATITUDE, model.latitude);
            } else {
                contentValues.putNull(Table.LATITUDE);
            }
            if (model.longitude != null) {
                contentValues.put(Table.LONGITUDE, model.longitude);
            } else {
                contentValues.putNull(Table.LONGITUDE);
            }
            if (model.trackedEntityInstance != null) {
                contentValues.put("trackedEntityInstance", model.trackedEntityInstance);
            } else {
                contentValues.putNull("trackedEntityInstance");
            }
            contentValues.put(Table.LOCALENROLLMENTID, Long.valueOf(model.localEnrollmentId));
            if (model.enrollment != null) {
                contentValues.put("enrollment", model.enrollment);
            } else {
                contentValues.putNull("enrollment");
            }
            if (model.programId != null) {
                contentValues.put("programId", model.programId);
            } else {
                contentValues.putNull("programId");
            }
            if (model.programStageId != null) {
                contentValues.put(Table.PROGRAMSTAGEID, model.programStageId);
            } else {
                contentValues.putNull(Table.PROGRAMSTAGEID);
            }
            if (model.organisationUnitId != null) {
                contentValues.put("organisationUnitId", model.organisationUnitId);
            } else {
                contentValues.putNull("organisationUnitId");
            }
            if (model.eventDate != null) {
                contentValues.put(Table.EVENTDATE, model.eventDate);
            } else {
                contentValues.putNull(Table.EVENTDATE);
            }
            if (model.dueDate != null) {
                contentValues.put(Table.DUEDATE, model.dueDate);
            } else {
                contentValues.putNull(Table.DUEDATE);
            }
            if (model.completedDate != null) {
                contentValues.put(Table.COMPLETEDDATE, model.completedDate);
            } else {
                contentValues.putNull(Table.COMPLETEDDATE);
            }
        }

        public void bindToInsertValues(ContentValues contentValues, Event model) {
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
            if (model.event != null) {
                contentValues.put("event", model.event);
            } else {
                contentValues.putNull("event");
            }
            if (model.status != null) {
                contentValues.put("status", model.status);
            } else {
                contentValues.putNull("status");
            }
            if (model.latitude != null) {
                contentValues.put(Table.LATITUDE, model.latitude);
            } else {
                contentValues.putNull(Table.LATITUDE);
            }
            if (model.longitude != null) {
                contentValues.put(Table.LONGITUDE, model.longitude);
            } else {
                contentValues.putNull(Table.LONGITUDE);
            }
            if (model.trackedEntityInstance != null) {
                contentValues.put("trackedEntityInstance", model.trackedEntityInstance);
            } else {
                contentValues.putNull("trackedEntityInstance");
            }
            contentValues.put(Table.LOCALENROLLMENTID, Long.valueOf(model.localEnrollmentId));
            if (model.enrollment != null) {
                contentValues.put("enrollment", model.enrollment);
            } else {
                contentValues.putNull("enrollment");
            }
            if (model.programId != null) {
                contentValues.put("programId", model.programId);
            } else {
                contentValues.putNull("programId");
            }
            if (model.programStageId != null) {
                contentValues.put(Table.PROGRAMSTAGEID, model.programStageId);
            } else {
                contentValues.putNull(Table.PROGRAMSTAGEID);
            }
            if (model.organisationUnitId != null) {
                contentValues.put("organisationUnitId", model.organisationUnitId);
            } else {
                contentValues.putNull("organisationUnitId");
            }
            if (model.eventDate != null) {
                contentValues.put(Table.EVENTDATE, model.eventDate);
            } else {
                contentValues.putNull(Table.EVENTDATE);
            }
            if (model.dueDate != null) {
                contentValues.put(Table.DUEDATE, model.dueDate);
            } else {
                contentValues.putNull(Table.DUEDATE);
            }
            if (model.completedDate != null) {
                contentValues.put(Table.COMPLETEDDATE, model.completedDate);
            } else {
                contentValues.putNull(Table.COMPLETEDDATE);
            }
        }

        public boolean exists(Event model) {
            return model.localId > 0;
        }

        public void loadFromCursor(Cursor cursor, Event model) {
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
            int indexevent = cursor.getColumnIndex("event");
            if (indexevent != -1) {
                if (cursor.isNull(indexevent)) {
                    model.event = null;
                } else {
                    model.event = cursor.getString(indexevent);
                }
            }
            int indexstatus = cursor.getColumnIndex("status");
            if (indexstatus != -1) {
                if (cursor.isNull(indexstatus)) {
                    model.status = null;
                } else {
                    model.status = cursor.getString(indexstatus);
                }
            }
            int indexlatitude = cursor.getColumnIndex(Table.LATITUDE);
            if (indexlatitude != -1) {
                if (cursor.isNull(indexlatitude)) {
                    model.latitude = null;
                } else {
                    model.latitude = Double.valueOf(cursor.getDouble(indexlatitude));
                }
            }
            int indexlongitude = cursor.getColumnIndex(Table.LONGITUDE);
            if (indexlongitude != -1) {
                if (cursor.isNull(indexlongitude)) {
                    model.longitude = null;
                } else {
                    model.longitude = Double.valueOf(cursor.getDouble(indexlongitude));
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
            int indexlocalEnrollmentId = cursor.getColumnIndex(Table.LOCALENROLLMENTID);
            if (indexlocalEnrollmentId != -1) {
                model.localEnrollmentId = cursor.getLong(indexlocalEnrollmentId);
            }
            int indexenrollment = cursor.getColumnIndex("enrollment");
            if (indexenrollment != -1) {
                if (cursor.isNull(indexenrollment)) {
                    model.enrollment = null;
                } else {
                    model.enrollment = cursor.getString(indexenrollment);
                }
            }
            int indexprogramId = cursor.getColumnIndex("programId");
            if (indexprogramId != -1) {
                if (cursor.isNull(indexprogramId)) {
                    model.programId = null;
                } else {
                    model.programId = cursor.getString(indexprogramId);
                }
            }
            int indexprogramStageId = cursor.getColumnIndex(Table.PROGRAMSTAGEID);
            if (indexprogramStageId != -1) {
                if (cursor.isNull(indexprogramStageId)) {
                    model.programStageId = null;
                } else {
                    model.programStageId = cursor.getString(indexprogramStageId);
                }
            }
            int indexorganisationUnitId = cursor.getColumnIndex("organisationUnitId");
            if (indexorganisationUnitId != -1) {
                if (cursor.isNull(indexorganisationUnitId)) {
                    model.organisationUnitId = null;
                } else {
                    model.organisationUnitId = cursor.getString(indexorganisationUnitId);
                }
            }
            int indexeventDate = cursor.getColumnIndex(Table.EVENTDATE);
            if (indexeventDate != -1) {
                if (cursor.isNull(indexeventDate)) {
                    model.eventDate = null;
                } else {
                    model.eventDate = cursor.getString(indexeventDate);
                }
            }
            int indexdueDate = cursor.getColumnIndex(Table.DUEDATE);
            if (indexdueDate != -1) {
                if (cursor.isNull(indexdueDate)) {
                    model.dueDate = null;
                } else {
                    model.dueDate = cursor.getString(indexdueDate);
                }
            }
            int indexcompletedDate = cursor.getColumnIndex(Table.COMPLETEDDATE);
            if (indexcompletedDate == -1) {
                return;
            }
            if (cursor.isNull(indexcompletedDate)) {
                model.completedDate = null;
            } else {
                model.completedDate = cursor.getString(indexcompletedDate);
            }
        }

        public void updateAutoIncrement(Event model, long id) {
            model.localId = id;
        }

        public long getAutoIncrementingId(Event model) {
            return model.localId;
        }

        public String getAutoIncrementingColumnName() {
            return "localId";
        }

        public ConditionQueryBuilder<Event> getPrimaryModelWhere(Event model) {
            return new ConditionQueryBuilder(Event.class, Condition.column("localId").is(Long.valueOf(model.localId)));
        }

        public ConditionQueryBuilder<Event> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(Event.class, Condition.column("localId").is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return "CREATE TABLE IF NOT EXISTS `Event`(`name` TEXT, `displayName` TEXT, `created` TEXT, `lastUpdated` TEXT, `access` TEXT, `id` TEXT, `fromServer` INTEGER, `localId` INTEGER PRIMARY KEY AUTOINCREMENT, `event` TEXT UNIQUE ON CONFLICT FAIL, `status` TEXT, `latitude` REAL, `longitude` REAL, `trackedEntityInstance` TEXT, `localEnrollmentId` INTEGER, `enrollment` TEXT, `programId` TEXT, `programStageId` TEXT, `organisationUnitId` TEXT, `eventDate` TEXT, `dueDate` TEXT, `completedDate` TEXT);";
        }

        public final Event newInstance() {
            return new Event();
        }
    }

    public final class Table {
        public static final String ACCESS = "access";
        public static final String COMPLETEDDATE = "completedDate";
        public static final String CREATED = "created";
        public static final String DISPLAYNAME = "displayName";
        public static final String DUEDATE = "dueDate";
        public static final String ENROLLMENT = "enrollment";
        public static final String EVENT = "event";
        public static final String EVENTDATE = "eventDate";
        public static final String FROMSERVER = "fromServer";
        public static final String ID = "id";
        public static final String LASTUPDATED = "lastUpdated";
        public static final String LATITUDE = "latitude";
        public static final String LOCALENROLLMENTID = "localEnrollmentId";
        public static final String LOCALID = "localId";
        public static final String LONGITUDE = "longitude";
        public static final String NAME = "name";
        public static final String ORGANISATIONUNITID = "organisationUnitId";
        public static final String PROGRAMID = "programId";
        public static final String PROGRAMSTAGEID = "programStageId";
        public static final String STATUS = "status";
        public static final String TABLE_NAME = "Event";
        public static final String TRACKEDENTITYINSTANCE = "trackedEntityInstance";
    }

    public Event() {
        this.event = CodeGenerator.generateCode();
    }

    public Event(String organisationUnitId, String status, String programId, ProgramStage programStage, String trackedEntityInstanceId, String enrollment, String dueDate) {
        this.event = CodeGenerator.generateCode();
        this.fromServer = false;
        this.dueDate = dueDate;
        this.organisationUnitId = organisationUnitId;
        this.programId = programId;
        this.programStageId = programStage.getUid();
        this.status = status;
        this.trackedEntityInstance = trackedEntityInstanceId;
        this.enrollment = enrollment;
        this.dataValues = new ArrayList();
    }

    @JsonProperty("event")
    public void setEvent(String event) {
        this.event = event;
    }

    @JsonProperty("event")
    public String getEvent() {
        return this.event;
    }

    public void delete() {
        if (this.dataValues != null) {
            for (DataValue dataValue : this.dataValues) {
                dataValue.delete();
            }
        }
        super.delete();
    }

    public void save() {
        Event existingEvent = TrackerController.getEventByUid(this.event);
        if (existingEvent != null) {
            this.localId = existingEvent.localId;
        }
        if (getEvent() != null || this.localId < 0) {
            super.save();
        } else {
            updateManually();
        }
        if (this.dataValues != null) {
            for (DataValue dataValue : this.dataValues) {
                dataValue.setEvent(this.event);
                dataValue.localEventId = this.localId;
                dataValue.save();
            }
        }
    }

    public String getEnrollment() {
        return this.enrollment;
    }

    private void updateManually() {
        new Update(Event.class).set(Condition.column(Table.LONGITUDE).is(this.longitude), Condition.column(Table.LATITUDE).is(this.latitude), Condition.column("status").is(this.status), Condition.column("fromServer").is(Boolean.valueOf(this.fromServer))).where(Condition.column("localId").is(Long.valueOf(this.localId))).queryClose();
    }

    public void update() {
        save();
    }

    @JsonProperty("coordinate")
    public void setCoordinate(Map<String, Object> coordinate) {
        this.latitude = Double.valueOf(((Double) coordinate.get(Table.LATITUDE)).doubleValue());
        this.longitude = Double.valueOf(((Double) coordinate.get(Table.LONGITUDE)).doubleValue());
    }

    @JsonProperty("coordinate")
    public Map<String, Object> getCoordinate() {
        Map<String, Object> coordinate = new HashMap();
        coordinate.put(Table.LATITUDE, this.latitude);
        coordinate.put(Table.LONGITUDE, this.longitude);
        return coordinate;
    }

    @JsonAnySetter
    public void handleUnknown(String key, Object value) {
    }

    public List<DataValue> getDataValues() {
        if (this.dataValues == null) {
            this.dataValues = new Select().from(DataValue.class).where(Condition.column(org.icddrb.dhis.android.sdk.persistence.models.DataValue.Table.LOCALEVENTID).is(Long.valueOf(this.localId))).queryList();
        }
        return this.dataValues;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getTrackedEntityInstance() {
        return this.trackedEntityInstance;
    }

    public void setTrackedEntityInstance(String trackedEntityInstance) {
        this.trackedEntityInstance = trackedEntityInstance;
    }

    public long getLocalEnrollmentId() {
        return this.localEnrollmentId;
    }

    public void setLocalEnrollmentId(long localEnrollmentId) {
        this.localEnrollmentId = localEnrollmentId;
    }

    public void setEnrollment(String enrollment) {
        this.enrollment = enrollment;
    }

    public String getProgramId() {
        return this.programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public String getProgramStageId() {
        return this.programStageId;
    }

    public void setProgramStageId(String programStageId) {
        this.programStageId = programStageId;
    }

    public String getOrganisationUnitId() {
        return this.organisationUnitId;
    }

    public void setOrganisationUnitId(String organisationUnitId) {
        this.organisationUnitId = organisationUnitId;
    }

    public String getEventDate() {
        return this.eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getCompletedDate() {
        return this.completedDate;
    }

    public void setCompletedDate(String completedDate) {
        this.completedDate = completedDate;
    }

    public void setDataValues(List<DataValue> dataValues) {
        this.dataValues = dataValues;
    }

    @JsonIgnore
    public String getUid() {
        return this.event;
    }

    @JsonIgnore
    public void setUid(String uid) {
        this.event = uid;
    }

    @JsonIgnore
    public boolean isDeleted() {
        return this.status.equals(STATUS_DELETED);
    }
}
