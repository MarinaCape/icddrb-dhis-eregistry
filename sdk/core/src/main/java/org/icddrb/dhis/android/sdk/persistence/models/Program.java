package org.icddrb.dhis.android.sdk.persistence.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.builder.Condition.Operation;
import com.raizlabs.android.dbflow.sql.builder.ConditionQueryBuilder;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import java.util.ArrayList;
import java.util.List;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.utils.api.ProgramType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Program extends BaseMetaDataObject {
    @JsonProperty("dataEntryMethod")
    boolean dataEntryMethod;
    @JsonProperty("description")
    String description;
    @JsonProperty("displayFrontPageList")
    boolean displayFrontPageList;
    @JsonProperty("displayIncidentDate")
    boolean displayIncidentDate;
    @JsonProperty("enrollmentDateLabel")
    String enrollmentDateLabel;
    @JsonProperty("externalAccess")
    boolean extenalAccess;
    @JsonProperty("ignoreOverdueEvents")
    boolean ignoreOverdueEvents;
    @JsonProperty("incidentDateLabel")
    String incidentDateLabel;
    @JsonProperty("onlyEnrollOnce")
    boolean onlyEnrollOnce;
    @JsonProperty("programIndicators")
    List<ProgramIndicator> programIndicators;
    @JsonProperty("programStages")
    List<ProgramStage> programStages;
    @JsonProperty("programTrackedEntityAttributes")
    List<ProgramTrackedEntityAttribute> programTrackedEntityAttributes;
    @JsonProperty("programType")
    ProgramType programType;
    @JsonProperty("registration")
    boolean registration;
    @JsonProperty("relationshipFromA")
    boolean relationshipFromA;
    @JsonProperty("selectEnrollmentDatesInFuture")
    boolean selectEnrollmentDatesInFuture;
    @JsonProperty("selectIncidentDatesInFuture")
    boolean selectIncidentDatesInFuture;
    @JsonProperty("singleEvent")
    boolean singleEvent;
    @JsonProperty("trackedEntityType")
    TrackedEntityType trackedEntityType;
    List<UserGroupAccess> userGroupAccesses;
    @JsonProperty("version")
    int version;

    public final class Adapter extends ModelAdapter<Program> {
        public Class<Program> getModelClass() {
            return Program.class;
        }

        public String getTableName() {
            return Table.TABLE_NAME;
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `Program` (`NAME`, `DISPLAYNAME`, `CREATED`, `LASTUPDATED`, `ACCESS`, `ID`, `trackedEntityType`, `PROGRAMTYPE`, `VERSION`, `ENROLLMENTDATELABEL`, `DESCRIPTION`, `ONLYENROLLONCE`, `EXTERNALACCESS`, `DISPLAYINCIDENTDATE`, `INCIDENTDATELABEL`, `REGISTRATION`, `SELECTENROLLMENTDATESINFUTURE`, `DATAENTRYMETHOD`, `SINGLEEVENT`, `IGNOREOVERDUEEVENTS`, `RELATIONSHIPFROMA`, `SELECTINCIDENTDATESINFUTURE`, `DISPLAYFRONTPAGELIST`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, Program model) {
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
            if (model.trackedEntityType != null) {
                model.trackedEntityType.save();
                if (model.trackedEntityType.id != null) {
                    statement.bindString(7, model.trackedEntityType.id);
                } else {
                    statement.bindNull(7);
                }
            } else {
                statement.bindNull(7);
            }
            ProgramType modelprogramType = model.programType;
            if (modelprogramType != null) {
                statement.bindString(8, modelprogramType.name());
            } else {
                statement.bindNull(8);
            }
            statement.bindLong(9, (long) model.version);
            if (model.enrollmentDateLabel != null) {
                statement.bindString(10, model.enrollmentDateLabel);
            } else {
                statement.bindNull(10);
            }
            if (model.description != null) {
                statement.bindString(11, model.description);
            } else {
                statement.bindNull(11);
            }
            Object modelonlyEnrollOnce = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.onlyEnrollOnce));
            if (modelonlyEnrollOnce != null) {
                statement.bindLong(12, (long) ((Integer) modelonlyEnrollOnce).intValue());
            } else {
                statement.bindNull(12);
            }
            Object modelextenalAccess = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.extenalAccess));
            if (modelextenalAccess != null) {
                statement.bindLong(13, (long) ((Integer) modelextenalAccess).intValue());
            } else {
                statement.bindNull(13);
            }
            Object modeldisplayIncidentDate = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.displayIncidentDate));
            if (modeldisplayIncidentDate != null) {
                statement.bindLong(14, (long) ((Integer) modeldisplayIncidentDate).intValue());
            } else {
                statement.bindNull(14);
            }
            if (model.incidentDateLabel != null) {
                statement.bindString(15, model.incidentDateLabel);
            } else {
                statement.bindNull(15);
            }
            Object modelregistration = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.registration));
            if (modelregistration != null) {
                statement.bindLong(16, (long) ((Integer) modelregistration).intValue());
            } else {
                statement.bindNull(16);
            }
            Object modelselectEnrollmentDatesInFuture = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.selectEnrollmentDatesInFuture));
            if (modelselectEnrollmentDatesInFuture != null) {
                statement.bindLong(17, (long) ((Integer) modelselectEnrollmentDatesInFuture).intValue());
            } else {
                statement.bindNull(17);
            }
            Object modeldataEntryMethod = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.dataEntryMethod));
            if (modeldataEntryMethod != null) {
                statement.bindLong(18, (long) ((Integer) modeldataEntryMethod).intValue());
            } else {
                statement.bindNull(18);
            }
            Object modelsingleEvent = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.singleEvent));
            if (modelsingleEvent != null) {
                statement.bindLong(19, (long) ((Integer) modelsingleEvent).intValue());
            } else {
                statement.bindNull(19);
            }
            Object modelignoreOverdueEvents = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.ignoreOverdueEvents));
            if (modelignoreOverdueEvents != null) {
                statement.bindLong(20, (long) ((Integer) modelignoreOverdueEvents).intValue());
            } else {
                statement.bindNull(20);
            }
            Object modelrelationshipFromA = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.relationshipFromA));
            if (modelrelationshipFromA != null) {
                statement.bindLong(21, (long) ((Integer) modelrelationshipFromA).intValue());
            } else {
                statement.bindNull(21);
            }
            Object modelselectIncidentDatesInFuture = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.selectIncidentDatesInFuture));
            if (modelselectIncidentDatesInFuture != null) {
                statement.bindLong(22, (long) ((Integer) modelselectIncidentDatesInFuture).intValue());
            } else {
                statement.bindNull(22);
            }
            Object modeldisplayFrontPageList = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.displayFrontPageList));
            if (modeldisplayFrontPageList != null) {
                statement.bindLong(23, (long) ((Integer) modeldisplayFrontPageList).intValue());
                return;
            }
            statement.bindNull(23);
        }

        public void bindToContentValues(ContentValues contentValues, Program model) {
            ContentValues contentValues2 = contentValues;
            if (model.name != null) {
                contentValues2.put("name", model.name);
            } else {
                contentValues.putNull("name");
            }
            if (model.displayName != null) {
                contentValues2 = contentValues;
                contentValues2.put("displayName", model.displayName);
            } else {
                contentValues.putNull("displayName");
            }
            if (model.created != null) {
                contentValues2 = contentValues;
                contentValues2.put("created", model.created);
            } else {
                contentValues.putNull("created");
            }
            if (model.lastUpdated != null) {
                contentValues2 = contentValues;
                contentValues2.put("lastUpdated", model.lastUpdated);
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
                contentValues2 = contentValues;
                contentValues2.put("id", model.id);
            } else {
                contentValues.putNull("id");
            }
            if (model.trackedEntityType != null) {
                model.trackedEntityType.save();
                if (model.trackedEntityType.id != null) {
                    contentValues.put("trackedEntityType", model.trackedEntityType.id);
                } else {
                    contentValues.putNull("trackedEntityType");
                }
            } else {
                contentValues.putNull("trackedEntityType");
            }
            ProgramType modelprogramType = model.programType;
            if (modelprogramType != null) {
                contentValues.put(Table.PROGRAMTYPE, modelprogramType.name());
            } else {
                contentValues.putNull(Table.PROGRAMTYPE);
            }
            contentValues.put("version", Integer.valueOf(model.version));
            if (model.enrollmentDateLabel != null) {
                contentValues.put(Table.ENROLLMENTDATELABEL, model.enrollmentDateLabel);
            } else {
                contentValues.putNull(Table.ENROLLMENTDATELABEL);
            }
            if (model.description != null) {
                contentValues2 = contentValues;
                contentValues2.put("description", model.description);
            } else {
                contentValues.putNull("description");
            }
            Object modelonlyEnrollOnce = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.onlyEnrollOnce));
            if (modelonlyEnrollOnce != null) {
                contentValues.put(Table.ONLYENROLLONCE, (Integer) modelonlyEnrollOnce);
            } else {
                contentValues.putNull(Table.ONLYENROLLONCE);
            }
            Object modelextenalAccess = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.extenalAccess));
            if (modelextenalAccess != null) {
                contentValues.put("externalAccess", (Integer) modelextenalAccess);
            } else {
                contentValues.putNull("externalAccess");
            }
            Object modeldisplayIncidentDate = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.displayIncidentDate));
            if (modeldisplayIncidentDate != null) {
                contentValues.put(Table.DISPLAYINCIDENTDATE, (Integer) modeldisplayIncidentDate);
            } else {
                contentValues.putNull(Table.DISPLAYINCIDENTDATE);
            }
            if (model.incidentDateLabel != null) {
                contentValues.put(Table.INCIDENTDATELABEL, model.incidentDateLabel);
            } else {
                contentValues.putNull(Table.INCIDENTDATELABEL);
            }
            Object modelregistration = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.registration));
            if (modelregistration != null) {
                contentValues.put(Table.REGISTRATION, (Integer) modelregistration);
            } else {
                contentValues.putNull(Table.REGISTRATION);
            }
            Object modelselectEnrollmentDatesInFuture = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.selectEnrollmentDatesInFuture));
            if (modelselectEnrollmentDatesInFuture != null) {
                contentValues.put(Table.SELECTENROLLMENTDATESINFUTURE, (Integer) modelselectEnrollmentDatesInFuture);
            } else {
                contentValues.putNull(Table.SELECTENROLLMENTDATESINFUTURE);
            }
            Object modeldataEntryMethod = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.dataEntryMethod));
            if (modeldataEntryMethod != null) {
                contentValues.put(Table.DATAENTRYMETHOD, (Integer) modeldataEntryMethod);
            } else {
                contentValues.putNull(Table.DATAENTRYMETHOD);
            }
            Object modelsingleEvent = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.singleEvent));
            if (modelsingleEvent != null) {
                contentValues.put(Table.SINGLEEVENT, (Integer) modelsingleEvent);
            } else {
                contentValues.putNull(Table.SINGLEEVENT);
            }
            Object modelignoreOverdueEvents = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.ignoreOverdueEvents));
            if (modelignoreOverdueEvents != null) {
                contentValues.put(Table.IGNOREOVERDUEEVENTS, (Integer) modelignoreOverdueEvents);
            } else {
                contentValues.putNull(Table.IGNOREOVERDUEEVENTS);
            }
            Object modelrelationshipFromA = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.relationshipFromA));
            if (modelrelationshipFromA != null) {
                contentValues.put(Table.RELATIONSHIPFROMA, (Integer) modelrelationshipFromA);
            } else {
                contentValues.putNull(Table.RELATIONSHIPFROMA);
            }
            Object modelselectIncidentDatesInFuture = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.selectIncidentDatesInFuture));
            if (modelselectIncidentDatesInFuture != null) {
                contentValues.put(Table.SELECTINCIDENTDATESINFUTURE, (Integer) modelselectIncidentDatesInFuture);
            } else {
                contentValues.putNull(Table.SELECTINCIDENTDATESINFUTURE);
            }
            Object modeldisplayFrontPageList = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.displayFrontPageList));
            if (modeldisplayFrontPageList != null) {
                contentValues.put(Table.DISPLAYFRONTPAGELIST, (Integer) modeldisplayFrontPageList);
                return;
            }
            contentValues.putNull(Table.DISPLAYFRONTPAGELIST);
        }

        public void bindToInsertValues(ContentValues contentValues, Program model) {
            ContentValues contentValues2 = contentValues;
            if (model.name != null) {
                contentValues2.put("name", model.name);
            } else {
                contentValues.putNull("name");
            }
            if (model.displayName != null) {
                contentValues2 = contentValues;
                contentValues2.put("displayName", model.displayName);
            } else {
                contentValues.putNull("displayName");
            }
            if (model.created != null) {
                contentValues2 = contentValues;
                contentValues2.put("created", model.created);
            } else {
                contentValues.putNull("created");
            }
            if (model.lastUpdated != null) {
                contentValues2 = contentValues;
                contentValues2.put("lastUpdated", model.lastUpdated);
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
                contentValues2 = contentValues;
                contentValues2.put("id", model.id);
            } else {
                contentValues.putNull("id");
            }
            if (model.trackedEntityType != null) {
                model.trackedEntityType.save();
                if (model.trackedEntityType.id != null) {
                    contentValues.put("trackedEntityType", model.trackedEntityType.id);
                } else {
                    contentValues.putNull("trackedEntityType");
                }
            } else {
                contentValues.putNull("trackedEntityType");
            }
            ProgramType modelprogramType = model.programType;
            if (modelprogramType != null) {
                contentValues.put(Table.PROGRAMTYPE, modelprogramType.name());
            } else {
                contentValues.putNull(Table.PROGRAMTYPE);
            }
            contentValues.put("version", Integer.valueOf(model.version));
            if (model.enrollmentDateLabel != null) {
                contentValues.put(Table.ENROLLMENTDATELABEL, model.enrollmentDateLabel);
            } else {
                contentValues.putNull(Table.ENROLLMENTDATELABEL);
            }
            if (model.description != null) {
                contentValues2 = contentValues;
                contentValues2.put("description", model.description);
            } else {
                contentValues.putNull("description");
            }
            Object modelonlyEnrollOnce = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.onlyEnrollOnce));
            if (modelonlyEnrollOnce != null) {
                contentValues.put(Table.ONLYENROLLONCE, (Integer) modelonlyEnrollOnce);
            } else {
                contentValues.putNull(Table.ONLYENROLLONCE);
            }
            Object modelextenalAccess = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.extenalAccess));
            if (modelextenalAccess != null) {
                contentValues.put("externalAccess", (Integer) modelextenalAccess);
            } else {
                contentValues.putNull("externalAccess");
            }
            Object modeldisplayIncidentDate = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.displayIncidentDate));
            if (modeldisplayIncidentDate != null) {
                contentValues.put(Table.DISPLAYINCIDENTDATE, (Integer) modeldisplayIncidentDate);
            } else {
                contentValues.putNull(Table.DISPLAYINCIDENTDATE);
            }
            if (model.incidentDateLabel != null) {
                contentValues.put(Table.INCIDENTDATELABEL, model.incidentDateLabel);
            } else {
                contentValues.putNull(Table.INCIDENTDATELABEL);
            }
            Object modelregistration = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.registration));
            if (modelregistration != null) {
                contentValues.put(Table.REGISTRATION, (Integer) modelregistration);
            } else {
                contentValues.putNull(Table.REGISTRATION);
            }
            Object modelselectEnrollmentDatesInFuture = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.selectEnrollmentDatesInFuture));
            if (modelselectEnrollmentDatesInFuture != null) {
                contentValues.put(Table.SELECTENROLLMENTDATESINFUTURE, (Integer) modelselectEnrollmentDatesInFuture);
            } else {
                contentValues.putNull(Table.SELECTENROLLMENTDATESINFUTURE);
            }
            Object modeldataEntryMethod = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.dataEntryMethod));
            if (modeldataEntryMethod != null) {
                contentValues.put(Table.DATAENTRYMETHOD, (Integer) modeldataEntryMethod);
            } else {
                contentValues.putNull(Table.DATAENTRYMETHOD);
            }
            Object modelsingleEvent = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.singleEvent));
            if (modelsingleEvent != null) {
                contentValues.put(Table.SINGLEEVENT, (Integer) modelsingleEvent);
            } else {
                contentValues.putNull(Table.SINGLEEVENT);
            }
            Object modelignoreOverdueEvents = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.ignoreOverdueEvents));
            if (modelignoreOverdueEvents != null) {
                contentValues.put(Table.IGNOREOVERDUEEVENTS, (Integer) modelignoreOverdueEvents);
            } else {
                contentValues.putNull(Table.IGNOREOVERDUEEVENTS);
            }
            Object modelrelationshipFromA = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.relationshipFromA));
            if (modelrelationshipFromA != null) {
                contentValues.put(Table.RELATIONSHIPFROMA, (Integer) modelrelationshipFromA);
            } else {
                contentValues.putNull(Table.RELATIONSHIPFROMA);
            }
            Object modelselectIncidentDatesInFuture = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.selectIncidentDatesInFuture));
            if (modelselectIncidentDatesInFuture != null) {
                contentValues.put(Table.SELECTINCIDENTDATESINFUTURE, (Integer) modelselectIncidentDatesInFuture);
            } else {
                contentValues.putNull(Table.SELECTINCIDENTDATESINFUTURE);
            }
            Object modeldisplayFrontPageList = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.displayFrontPageList));
            if (modeldisplayFrontPageList != null) {
                contentValues.put(Table.DISPLAYFRONTPAGELIST, (Integer) modeldisplayFrontPageList);
                return;
            }
            contentValues.putNull(Table.DISPLAYFRONTPAGELIST);
        }

        public boolean exists(Program model) {
            return new Select().from(Program.class).where(getPrimaryModelWhere(model)).hasData();
        }

        public void loadFromCursor(Cursor cursor, Program model) {
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
            int indextrackedEntityType = cursor.getColumnIndex("trackedEntityType");
            if (!(indextrackedEntityType == -1 || cursor.isNull(indextrackedEntityType))) {
                model.trackedEntityType = (TrackedEntityType) new Select().from(TrackedEntityType.class).where().and(Condition.column("id").is(cursor.getString(indextrackedEntityType))).querySingle();
            }
            int indexprogramType = cursor.getColumnIndex(Table.PROGRAMTYPE);
            if (indexprogramType != -1) {
                if (cursor.isNull(indexprogramType)) {
                    model.programType = null;
                } else {
                    model.programType = ProgramType.valueOf(cursor.getString(indexprogramType));
                }
            }
            int indexversion = cursor.getColumnIndex("version");
            if (indexversion != -1) {
                model.version = cursor.getInt(indexversion);
            }
            int indexenrollmentDateLabel = cursor.getColumnIndex(Table.ENROLLMENTDATELABEL);
            if (indexenrollmentDateLabel != -1) {
                if (cursor.isNull(indexenrollmentDateLabel)) {
                    model.enrollmentDateLabel = null;
                } else {
                    model.enrollmentDateLabel = cursor.getString(indexenrollmentDateLabel);
                }
            }
            int indexdescription = cursor.getColumnIndex("description");
            if (indexdescription != -1) {
                if (cursor.isNull(indexdescription)) {
                    model.description = null;
                } else {
                    model.description = cursor.getString(indexdescription);
                }
            }
            int indexonlyEnrollOnce = cursor.getColumnIndex(Table.ONLYENROLLONCE);
            if (indexonlyEnrollOnce != -1) {
                model.onlyEnrollOnce = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexonlyEnrollOnce)))).booleanValue();
            }
            int indexexternalAccess = cursor.getColumnIndex("externalAccess");
            if (indexexternalAccess != -1) {
                model.extenalAccess = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexexternalAccess)))).booleanValue();
            }
            int indexdisplayIncidentDate = cursor.getColumnIndex(Table.DISPLAYINCIDENTDATE);
            if (indexdisplayIncidentDate != -1) {
                model.displayIncidentDate = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexdisplayIncidentDate)))).booleanValue();
            }
            int indexincidentDateLabel = cursor.getColumnIndex(Table.INCIDENTDATELABEL);
            if (indexincidentDateLabel != -1) {
                if (cursor.isNull(indexincidentDateLabel)) {
                    model.incidentDateLabel = null;
                } else {
                    model.incidentDateLabel = cursor.getString(indexincidentDateLabel);
                }
            }
            int indexregistration = cursor.getColumnIndex(Table.REGISTRATION);
            if (indexregistration != -1) {
                model.registration = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexregistration)))).booleanValue();
            }
            int indexselectEnrollmentDatesInFuture = cursor.getColumnIndex(Table.SELECTENROLLMENTDATESINFUTURE);
            if (indexselectEnrollmentDatesInFuture != -1) {
                model.selectEnrollmentDatesInFuture = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexselectEnrollmentDatesInFuture)))).booleanValue();
            }
            int indexdataEntryMethod = cursor.getColumnIndex(Table.DATAENTRYMETHOD);
            if (indexdataEntryMethod != -1) {
                model.dataEntryMethod = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexdataEntryMethod)))).booleanValue();
            }
            int indexsingleEvent = cursor.getColumnIndex(Table.SINGLEEVENT);
            if (indexsingleEvent != -1) {
                model.singleEvent = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexsingleEvent)))).booleanValue();
            }
            int indexignoreOverdueEvents = cursor.getColumnIndex(Table.IGNOREOVERDUEEVENTS);
            if (indexignoreOverdueEvents != -1) {
                model.ignoreOverdueEvents = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexignoreOverdueEvents)))).booleanValue();
            }
            int indexrelationshipFromA = cursor.getColumnIndex(Table.RELATIONSHIPFROMA);
            if (indexrelationshipFromA != -1) {
                model.relationshipFromA = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexrelationshipFromA)))).booleanValue();
            }
            int indexselectIncidentDatesInFuture = cursor.getColumnIndex(Table.SELECTINCIDENTDATESINFUTURE);
            if (indexselectIncidentDatesInFuture != -1) {
                model.selectIncidentDatesInFuture = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexselectIncidentDatesInFuture)))).booleanValue();
            }
            int indexdisplayFrontPageList = cursor.getColumnIndex(Table.DISPLAYFRONTPAGELIST);
            if (indexdisplayFrontPageList != -1) {
                model.displayFrontPageList = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexdisplayFrontPageList)))).booleanValue();
            }
        }

        public ConditionQueryBuilder<Program> getPrimaryModelWhere(Program model) {
            return new ConditionQueryBuilder(Program.class, Condition.column("id").is(model.id));
        }

        public ConditionQueryBuilder<Program> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(Program.class, Condition.column("id").is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return String.format("CREATE TABLE IF NOT EXISTS `Program`(`name` TEXT, `displayName` TEXT, `created` TEXT, `lastUpdated` TEXT, `access` TEXT, `id` TEXT,  `trackedEntityType` TEXT, `programType` TEXT, `version` INTEGER, `enrollmentDateLabel` TEXT, `description` TEXT, `onlyEnrollOnce` INTEGER, `externalAccess` INTEGER, `displayIncidentDate` INTEGER, `incidentDateLabel` TEXT, `registration` INTEGER, `selectEnrollmentDatesInFuture` INTEGER, `dataEntryMethod` INTEGER, `singleEvent` INTEGER, `ignoreOverdueEvents` INTEGER, `relationshipFromA` INTEGER, `selectIncidentDatesInFuture` INTEGER, `displayFrontPageList` INTEGER, PRIMARY KEY(`id`), FOREIGN KEY(`trackedEntityType`) REFERENCES `%1s` (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION );", new Object[]{FlowManager.getTableName(TrackedEntityType.class)});
        }

        public final Program newInstance() {
            return new Program();
        }
    }

    public final class Table {
        public static final String ACCESS = "access";
        public static final String CREATED = "created";
        public static final String DATAENTRYMETHOD = "dataEntryMethod";
        public static final String DESCRIPTION = "description";
        public static final String DISPLAYFRONTPAGELIST = "displayFrontPageList";
        public static final String DISPLAYINCIDENTDATE = "displayIncidentDate";
        public static final String DISPLAYNAME = "displayName";
        public static final String ENROLLMENTDATELABEL = "enrollmentDateLabel";
        public static final String EXTERNALACCESS = "externalAccess";
        public static final String ID = "id";
        public static final String IGNOREOVERDUEEVENTS = "ignoreOverdueEvents";
        public static final String INCIDENTDATELABEL = "incidentDateLabel";
        public static final String LASTUPDATED = "lastUpdated";
        public static final String NAME = "name";
        public static final String ONLYENROLLONCE = "onlyEnrollOnce";
        public static final String PROGRAMTYPE = "programType";
        public static final String REGISTRATION = "registration";
        public static final String RELATIONSHIPFROMA = "relationshipFromA";
        public static final String SELECTENROLLMENTDATESINFUTURE = "selectEnrollmentDatesInFuture";
        public static final String SELECTINCIDENTDATESINFUTURE = "selectIncidentDatesInFuture";
        public static final String SINGLEEVENT = "singleEvent";
        public static final String TABLE_NAME = "Program";
        public static final String TRACKEDENTITYTYPE_TRACKEDENTITYTYPE = "trackedEntityType";
        public static final String VERSION = "version";
    }

    @JsonProperty("userGroupAccesses")
    public void setUserGroupAccesses(List<UserGroupAccess> uga) {
        this.userGroupAccesses = new ArrayList();
        for (UserGroupAccess a : uga) {
            a.uniqId = getUid();
            a.save();
            this.userGroupAccesses.add(a);
        }
    }

    public boolean userHasAccess(List<UserGroup> userGroups) {
        if (getUserGroupAccesses().size() == 0) {
            return true;
        }
        if (getUserGroupAccesses().size() > 0) {
            for (UserGroupAccess uga : getUserGroupAccesses()) {
                for (UserGroup ug : userGroups) {
                    if (ug.getId().equals(uga.getId())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public List<UserGroupAccess> getUserGroupAccesses() {
        if (this.userGroupAccesses == null) {
            this.userGroupAccesses = MetaDataController.getUserGroupAccess(getUid());
        }
        return this.userGroupAccesses;
    }

    @JsonAnySetter
    public void handleUnknown(String key, Object value) {
    }

    public ProgramType getProgramType() {
        return this.programType;
    }

    public void setProgramType(ProgramType programType) {
        this.programType = programType;
    }

    public TrackedEntityType getTrackedEntityType() {
        return this.trackedEntityType;
    }

    public void setTrackedEntityType(TrackedEntityType trackedEntityType) {
        this.trackedEntityType = trackedEntityType;
    }

    public List<ProgramIndicator> getProgramIndicators() {
        if (this.programIndicators == null) {
            this.programIndicators = MetaDataController.getProgramIndicators(this.id);
        }
        return this.programIndicators;
    }

    public void setProgramIndicators(List<ProgramIndicator> programIndicators) {
        this.programIndicators = programIndicators;
    }

    public List<ProgramStage> getProgramStages() {
        if (this.programStages == null) {
            this.programStages = MetaDataController.getProgramStages(this.id);
        }
        return this.programStages;
    }

    public void setProgramStages(List<ProgramStage> programStages) {
        this.programStages = programStages;
    }

    public List<ProgramTrackedEntityAttribute> getProgramTrackedEntityAttributes() {
        if (this.programTrackedEntityAttributes == null) {
            this.programTrackedEntityAttributes = MetaDataController.getProgramTrackedEntityAttributes(this.id);
        }
        return this.programTrackedEntityAttributes;
    }

    public void setProgramTrackedEntityAttributes(List<ProgramTrackedEntityAttribute> programTrackedEntityAttributes) {
        this.programTrackedEntityAttributes = programTrackedEntityAttributes;
    }

    public List<ProgramRule> getProgramRules() {
        return new Select().from(ProgramRule.class).where(Condition.column("program").is(this.id)).queryList();
    }

    public boolean getRelationshipFromA() {
        return this.relationshipFromA;
    }

    public void setRelationshipFromA(boolean relationshipFromA) {
        this.relationshipFromA = relationshipFromA;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getEnrollmentDateLabel() {
        return this.enrollmentDateLabel;
    }

    public void setEnrollmentDateLabel(String enrollmentDateLabel) {
        this.enrollmentDateLabel = enrollmentDateLabel;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getOnlyEnrollOnce() {
        return this.onlyEnrollOnce;
    }

    public void setOnlyEnrollOnce(boolean onlyEnrollOnce) {
        this.onlyEnrollOnce = onlyEnrollOnce;
    }

    public boolean getExtenalAccess() {
        return this.extenalAccess;
    }

    public void setExtenalAccess(boolean extenalAccess) {
        this.extenalAccess = extenalAccess;
    }

    public boolean getDisplayIncidentDate() {
        return this.displayIncidentDate;
    }

    public void setDisplayIncidentDate(boolean displayIncidentDate) {
        this.displayIncidentDate = displayIncidentDate;
    }

    public String getIncidentDateLabel() {
        return this.incidentDateLabel;
    }

    public void setIncidentDateLabel(String incidentDateLabel) {
        this.incidentDateLabel = incidentDateLabel;
    }

    public boolean getRegistration() {
        return this.registration;
    }

    public void setRegistration(boolean registration) {
        this.registration = registration;
    }

    public boolean getSelectEnrollmentDatesInFuture() {
        return this.selectEnrollmentDatesInFuture;
    }

    public void setSelectEnrollmentDatesInFuture(boolean selectEnrollmentDatesInFuture) {
        this.selectEnrollmentDatesInFuture = selectEnrollmentDatesInFuture;
    }

    public boolean getDataEntryMethod() {
        return this.dataEntryMethod;
    }

    public void setDataEntryMethod(boolean dataEntryMethod) {
        this.dataEntryMethod = dataEntryMethod;
    }

    public boolean getSingleEvent() {
        return this.singleEvent;
    }

    public void setSingleEvent(boolean singleEvent) {
        this.singleEvent = singleEvent;
    }

    public boolean getIgnoreOverdueEvents() {
        return this.ignoreOverdueEvents;
    }

    public void setIgnoreOverdueEvents(boolean ignoreOverdueEvents) {
        this.ignoreOverdueEvents = ignoreOverdueEvents;
    }

    public boolean getSelectIncidentDatesInFuture() {
        return this.selectIncidentDatesInFuture;
    }

    public void setSelectIncidentDatesInFuture(boolean selectIncidentDatesInFuture) {
        this.selectIncidentDatesInFuture = selectIncidentDatesInFuture;
    }

    public boolean isDisplayFrontPageList() {
        return this.displayFrontPageList;
    }

    public void setDisplayFrontPageList(boolean displayFrontPageList) {
        this.displayFrontPageList = displayFrontPageList;
    }
}
