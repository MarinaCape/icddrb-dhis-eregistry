package org.icddrb.dhis.android.sdk.persistence.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.builder.Condition.Operation;
import com.raizlabs.android.dbflow.sql.builder.ConditionQueryBuilder;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;

public class ProgramStage extends BaseMetaDataObject {
    @JsonProperty("allowGenerateNextVisit")
    boolean allowGenerateNextVisit;
    @JsonProperty("autoGenerateEvent")
    boolean autoGenerateEvent;
    @JsonProperty("blockEntryForm")
    boolean blockEntryForm;
    @JsonProperty("captureCoordinates")
    boolean captureCoordinates;
    @JsonProperty("dataEntryType")
    String dataEntryType;
    @JsonProperty("defaultTemplateMessage")
    String defaultTemplateMessage;
    @JsonProperty("description")
    String description;
    @JsonProperty("displayGenerateEventBox")
    boolean displayGenerateEventBox;
    @JsonProperty("executionDateLabel")
    String executionDateLabel;
    @JsonProperty("externalAccess")
    boolean externalAccess;
    @JsonProperty("generatedByEnrollmentDate")
    boolean generatedByEnrollmentDate;
    @JsonProperty("hideDueDate")
    boolean hideDueDate;
    @JsonProperty("minDaysFromStart")
    int minDaysFromStart;
    @JsonProperty("openAfterEnrollment")
    boolean openAfterEnrollment;
    @JsonProperty("periodType")
    String periodType;
    @JsonProperty("preGenerateUID")
    boolean preGenerateUID;
    String program;
    @JsonProperty("programIndicators")
    List<ProgramIndicator> programIndicators;
    @JsonProperty("programStageDataElements")
    List<ProgramStageDataElement> programStageDataElements;
    @JsonProperty("programStageSections")
    List<ProgramStageSection> programStageSections;
    @JsonProperty("remindCompleted")
    boolean remindCompleted;
    @JsonProperty("repeatable")
    boolean repeatable;
    @JsonProperty("reportDateDescription")
    String reportDateDescription;
    @JsonProperty("sortOrder")
    int sortOrder;
    @JsonProperty("standardInterval")
    int standardInterval;
    List<UserGroupAccess> userGroupAccesses;
    @JsonProperty("validCompleteOnly")
    boolean validCompleteOnly;

    public final class Adapter extends ModelAdapter<ProgramStage> {
        public Class<ProgramStage> getModelClass() {
            return ProgramStage.class;
        }

        public String getTableName() {
            return Table.TABLE_NAME;
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `ProgramStage` (`NAME`, `DISPLAYNAME`, `CREATED`, `LASTUPDATED`, `ACCESS`, `ID`, `PROGRAM`, `DATAENTRYTYPE`, `BLOCKENTRYFORM`, `REPORTDATEDESCRIPTION`, `DISPLAYGENERATEEVENTBOX`, `DESCRIPTION`, `EXTERNALACCESS`, `OPENAFTERENROLLMENT`, `CAPTURECOORDINATES`, `DEFAULTTEMPLATEMESSAGE`, `REMINDCOMPLETED`, `VALIDCOMPLETEONLY`, `SORTORDER`, `HIDEDUEDATE`, `GENERATEDBYENROLLMENTDATE`, `PREGENERATEUID`, `AUTOGENERATEEVENT`, `ALLOWGENERATENEXTVISIT`, `STANDARDINTERVAL`, `REPEATABLE`, `MINDAYSFROMSTART`, `PERIODTYPE`, `EXECUTIONDATELABEL`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, ProgramStage model) {
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
            if (model.program != null) {
                statement.bindString(7, model.program);
            } else {
                statement.bindNull(7);
            }
            if (model.dataEntryType != null) {
                statement.bindString(8, model.dataEntryType);
            } else {
                statement.bindNull(8);
            }
            Object modelblockEntryForm = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.blockEntryForm));
            if (modelblockEntryForm != null) {
                statement.bindLong(9, (long) ((Integer) modelblockEntryForm).intValue());
            } else {
                statement.bindNull(9);
            }
            if (model.reportDateDescription != null) {
                statement.bindString(10, model.reportDateDescription);
            } else {
                statement.bindNull(10);
            }
            Object modeldisplayGenerateEventBox = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.displayGenerateEventBox));
            if (modeldisplayGenerateEventBox != null) {
                statement.bindLong(11, (long) ((Integer) modeldisplayGenerateEventBox).intValue());
            } else {
                statement.bindNull(11);
            }
            if (model.description != null) {
                statement.bindString(12, model.description);
            } else {
                statement.bindNull(12);
            }
            Object modelexternalAccess = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.externalAccess));
            if (modelexternalAccess != null) {
                statement.bindLong(13, (long) ((Integer) modelexternalAccess).intValue());
            } else {
                statement.bindNull(13);
            }
            Object modelopenAfterEnrollment = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.openAfterEnrollment));
            if (modelopenAfterEnrollment != null) {
                statement.bindLong(14, (long) ((Integer) modelopenAfterEnrollment).intValue());
            } else {
                statement.bindNull(14);
            }
            Object modelcaptureCoordinates = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.captureCoordinates));
            if (modelcaptureCoordinates != null) {
                statement.bindLong(15, (long) ((Integer) modelcaptureCoordinates).intValue());
            } else {
                statement.bindNull(15);
            }
            if (model.defaultTemplateMessage != null) {
                statement.bindString(16, model.defaultTemplateMessage);
            } else {
                statement.bindNull(16);
            }
            Object modelremindCompleted = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.remindCompleted));
            if (modelremindCompleted != null) {
                statement.bindLong(17, (long) ((Integer) modelremindCompleted).intValue());
            } else {
                statement.bindNull(17);
            }
            Object modelvalidCompleteOnly = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.validCompleteOnly));
            if (modelvalidCompleteOnly != null) {
                statement.bindLong(18, (long) ((Integer) modelvalidCompleteOnly).intValue());
            } else {
                statement.bindNull(18);
            }
            statement.bindLong(19, (long) model.sortOrder);
            Object modelhideDueDate = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.hideDueDate));
            if (modelhideDueDate != null) {
                statement.bindLong(20, (long) ((Integer) modelhideDueDate).intValue());
            } else {
                statement.bindNull(20);
            }
            Object modelgeneratedByEnrollmentDate = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.generatedByEnrollmentDate));
            if (modelgeneratedByEnrollmentDate != null) {
                statement.bindLong(21, (long) ((Integer) modelgeneratedByEnrollmentDate).intValue());
            } else {
                statement.bindNull(21);
            }
            Object modelpreGenerateUID = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.preGenerateUID));
            if (modelpreGenerateUID != null) {
                statement.bindLong(22, (long) ((Integer) modelpreGenerateUID).intValue());
            } else {
                statement.bindNull(22);
            }
            Object modelautoGenerateEvent = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.autoGenerateEvent));
            if (modelautoGenerateEvent != null) {
                statement.bindLong(23, (long) ((Integer) modelautoGenerateEvent).intValue());
            } else {
                statement.bindNull(23);
            }
            Object modelallowGenerateNextVisit = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.allowGenerateNextVisit));
            if (modelallowGenerateNextVisit != null) {
                statement.bindLong(24, (long) ((Integer) modelallowGenerateNextVisit).intValue());
            } else {
                statement.bindNull(24);
            }
            statement.bindLong(25, (long) model.standardInterval);
            Object modelrepeatable = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.repeatable));
            if (modelrepeatable != null) {
                statement.bindLong(26, (long) ((Integer) modelrepeatable).intValue());
            } else {
                statement.bindNull(26);
            }
            statement.bindLong(27, (long) model.minDaysFromStart);
            if (model.periodType != null) {
                statement.bindString(28, model.periodType);
            } else {
                statement.bindNull(28);
            }
            if (model.executionDateLabel != null) {
                statement.bindString(29, model.executionDateLabel);
                return;
            }
            statement.bindNull(29);
        }

        public void bindToContentValues(ContentValues contentValues, ProgramStage model) {
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
            if (model.program != null) {
                contentValues.put("program", model.program);
            } else {
                contentValues.putNull("program");
            }
            if (model.dataEntryType != null) {
                contentValues.put(Table.DATAENTRYTYPE, model.dataEntryType);
            } else {
                contentValues.putNull(Table.DATAENTRYTYPE);
            }
            Object modelblockEntryForm = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.blockEntryForm));
            if (modelblockEntryForm != null) {
                contentValues.put(Table.BLOCKENTRYFORM, (Integer) modelblockEntryForm);
            } else {
                contentValues.putNull(Table.BLOCKENTRYFORM);
            }
            if (model.reportDateDescription != null) {
                contentValues.put(Table.REPORTDATEDESCRIPTION, model.reportDateDescription);
            } else {
                contentValues.putNull(Table.REPORTDATEDESCRIPTION);
            }
            Object modeldisplayGenerateEventBox = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.displayGenerateEventBox));
            if (modeldisplayGenerateEventBox != null) {
                contentValues.put(Table.DISPLAYGENERATEEVENTBOX, (Integer) modeldisplayGenerateEventBox);
            } else {
                contentValues.putNull(Table.DISPLAYGENERATEEVENTBOX);
            }
            if (model.description != null) {
                contentValues.put("description", model.description);
            } else {
                contentValues.putNull("description");
            }
            Object modelexternalAccess = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.externalAccess));
            if (modelexternalAccess != null) {
                contentValues.put("externalAccess", (Integer) modelexternalAccess);
            } else {
                contentValues.putNull("externalAccess");
            }
            Object modelopenAfterEnrollment = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.openAfterEnrollment));
            if (modelopenAfterEnrollment != null) {
                contentValues.put(Table.OPENAFTERENROLLMENT, (Integer) modelopenAfterEnrollment);
            } else {
                contentValues.putNull(Table.OPENAFTERENROLLMENT);
            }
            Object modelcaptureCoordinates = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.captureCoordinates));
            if (modelcaptureCoordinates != null) {
                contentValues.put(Table.CAPTURECOORDINATES, (Integer) modelcaptureCoordinates);
            } else {
                contentValues.putNull(Table.CAPTURECOORDINATES);
            }
            if (model.defaultTemplateMessage != null) {
                contentValues.put(Table.DEFAULTTEMPLATEMESSAGE, model.defaultTemplateMessage);
            } else {
                contentValues.putNull(Table.DEFAULTTEMPLATEMESSAGE);
            }
            Object modelremindCompleted = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.remindCompleted));
            if (modelremindCompleted != null) {
                contentValues.put(Table.REMINDCOMPLETED, (Integer) modelremindCompleted);
            } else {
                contentValues.putNull(Table.REMINDCOMPLETED);
            }
            Object modelvalidCompleteOnly = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.validCompleteOnly));
            if (modelvalidCompleteOnly != null) {
                contentValues.put(Table.VALIDCOMPLETEONLY, (Integer) modelvalidCompleteOnly);
            } else {
                contentValues.putNull(Table.VALIDCOMPLETEONLY);
            }
            contentValues.put("sortOrder", Integer.valueOf(model.sortOrder));
            Object modelhideDueDate = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.hideDueDate));
            if (modelhideDueDate != null) {
                contentValues.put(Table.HIDEDUEDATE, (Integer) modelhideDueDate);
            } else {
                contentValues.putNull(Table.HIDEDUEDATE);
            }
            Object modelgeneratedByEnrollmentDate = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.generatedByEnrollmentDate));
            if (modelgeneratedByEnrollmentDate != null) {
                contentValues.put(Table.GENERATEDBYENROLLMENTDATE, (Integer) modelgeneratedByEnrollmentDate);
            } else {
                contentValues.putNull(Table.GENERATEDBYENROLLMENTDATE);
            }
            Object modelpreGenerateUID = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.preGenerateUID));
            if (modelpreGenerateUID != null) {
                contentValues.put(Table.PREGENERATEUID, (Integer) modelpreGenerateUID);
            } else {
                contentValues.putNull(Table.PREGENERATEUID);
            }
            Object modelautoGenerateEvent = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.autoGenerateEvent));
            if (modelautoGenerateEvent != null) {
                contentValues.put(Table.AUTOGENERATEEVENT, (Integer) modelautoGenerateEvent);
            } else {
                contentValues.putNull(Table.AUTOGENERATEEVENT);
            }
            Object modelallowGenerateNextVisit = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.allowGenerateNextVisit));
            if (modelallowGenerateNextVisit != null) {
                contentValues.put(Table.ALLOWGENERATENEXTVISIT, (Integer) modelallowGenerateNextVisit);
            } else {
                contentValues.putNull(Table.ALLOWGENERATENEXTVISIT);
            }
            contentValues.put(Table.STANDARDINTERVAL, Integer.valueOf(model.standardInterval));
            Object modelrepeatable = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.repeatable));
            if (modelrepeatable != null) {
                contentValues.put(Table.REPEATABLE, (Integer) modelrepeatable);
            } else {
                contentValues.putNull(Table.REPEATABLE);
            }
            contentValues.put(Table.MINDAYSFROMSTART, Integer.valueOf(model.minDaysFromStart));
            if (model.periodType != null) {
                contentValues.put(Table.PERIODTYPE, model.periodType);
            } else {
                contentValues.putNull(Table.PERIODTYPE);
            }
            if (model.executionDateLabel != null) {
                contentValues.put(Table.EXECUTIONDATELABEL, model.executionDateLabel);
                return;
            }
            contentValues.putNull(Table.EXECUTIONDATELABEL);
        }

        public void bindToInsertValues(ContentValues contentValues, ProgramStage model) {
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
            if (model.program != null) {
                contentValues.put("program", model.program);
            } else {
                contentValues.putNull("program");
            }
            if (model.dataEntryType != null) {
                contentValues.put(Table.DATAENTRYTYPE, model.dataEntryType);
            } else {
                contentValues.putNull(Table.DATAENTRYTYPE);
            }
            Object modelblockEntryForm = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.blockEntryForm));
            if (modelblockEntryForm != null) {
                contentValues.put(Table.BLOCKENTRYFORM, (Integer) modelblockEntryForm);
            } else {
                contentValues.putNull(Table.BLOCKENTRYFORM);
            }
            if (model.reportDateDescription != null) {
                contentValues.put(Table.REPORTDATEDESCRIPTION, model.reportDateDescription);
            } else {
                contentValues.putNull(Table.REPORTDATEDESCRIPTION);
            }
            Object modeldisplayGenerateEventBox = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.displayGenerateEventBox));
            if (modeldisplayGenerateEventBox != null) {
                contentValues.put(Table.DISPLAYGENERATEEVENTBOX, (Integer) modeldisplayGenerateEventBox);
            } else {
                contentValues.putNull(Table.DISPLAYGENERATEEVENTBOX);
            }
            if (model.description != null) {
                contentValues.put("description", model.description);
            } else {
                contentValues.putNull("description");
            }
            Object modelexternalAccess = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.externalAccess));
            if (modelexternalAccess != null) {
                contentValues.put("externalAccess", (Integer) modelexternalAccess);
            } else {
                contentValues.putNull("externalAccess");
            }
            Object modelopenAfterEnrollment = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.openAfterEnrollment));
            if (modelopenAfterEnrollment != null) {
                contentValues.put(Table.OPENAFTERENROLLMENT, (Integer) modelopenAfterEnrollment);
            } else {
                contentValues.putNull(Table.OPENAFTERENROLLMENT);
            }
            Object modelcaptureCoordinates = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.captureCoordinates));
            if (modelcaptureCoordinates != null) {
                contentValues.put(Table.CAPTURECOORDINATES, (Integer) modelcaptureCoordinates);
            } else {
                contentValues.putNull(Table.CAPTURECOORDINATES);
            }
            if (model.defaultTemplateMessage != null) {
                contentValues.put(Table.DEFAULTTEMPLATEMESSAGE, model.defaultTemplateMessage);
            } else {
                contentValues.putNull(Table.DEFAULTTEMPLATEMESSAGE);
            }
            Object modelremindCompleted = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.remindCompleted));
            if (modelremindCompleted != null) {
                contentValues.put(Table.REMINDCOMPLETED, (Integer) modelremindCompleted);
            } else {
                contentValues.putNull(Table.REMINDCOMPLETED);
            }
            Object modelvalidCompleteOnly = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.validCompleteOnly));
            if (modelvalidCompleteOnly != null) {
                contentValues.put(Table.VALIDCOMPLETEONLY, (Integer) modelvalidCompleteOnly);
            } else {
                contentValues.putNull(Table.VALIDCOMPLETEONLY);
            }
            contentValues.put("sortOrder", Integer.valueOf(model.sortOrder));
            Object modelhideDueDate = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.hideDueDate));
            if (modelhideDueDate != null) {
                contentValues.put(Table.HIDEDUEDATE, (Integer) modelhideDueDate);
            } else {
                contentValues.putNull(Table.HIDEDUEDATE);
            }
            Object modelgeneratedByEnrollmentDate = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.generatedByEnrollmentDate));
            if (modelgeneratedByEnrollmentDate != null) {
                contentValues.put(Table.GENERATEDBYENROLLMENTDATE, (Integer) modelgeneratedByEnrollmentDate);
            } else {
                contentValues.putNull(Table.GENERATEDBYENROLLMENTDATE);
            }
            Object modelpreGenerateUID = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.preGenerateUID));
            if (modelpreGenerateUID != null) {
                contentValues.put(Table.PREGENERATEUID, (Integer) modelpreGenerateUID);
            } else {
                contentValues.putNull(Table.PREGENERATEUID);
            }
            Object modelautoGenerateEvent = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.autoGenerateEvent));
            if (modelautoGenerateEvent != null) {
                contentValues.put(Table.AUTOGENERATEEVENT, (Integer) modelautoGenerateEvent);
            } else {
                contentValues.putNull(Table.AUTOGENERATEEVENT);
            }
            Object modelallowGenerateNextVisit = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.allowGenerateNextVisit));
            if (modelallowGenerateNextVisit != null) {
                contentValues.put(Table.ALLOWGENERATENEXTVISIT, (Integer) modelallowGenerateNextVisit);
            } else {
                contentValues.putNull(Table.ALLOWGENERATENEXTVISIT);
            }
            contentValues.put(Table.STANDARDINTERVAL, Integer.valueOf(model.standardInterval));
            Object modelrepeatable = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.repeatable));
            if (modelrepeatable != null) {
                contentValues.put(Table.REPEATABLE, (Integer) modelrepeatable);
            } else {
                contentValues.putNull(Table.REPEATABLE);
            }
            contentValues.put(Table.MINDAYSFROMSTART, Integer.valueOf(model.minDaysFromStart));
            if (model.periodType != null) {
                contentValues.put(Table.PERIODTYPE, model.periodType);
            } else {
                contentValues.putNull(Table.PERIODTYPE);
            }
            if (model.executionDateLabel != null) {
                contentValues.put(Table.EXECUTIONDATELABEL, model.executionDateLabel);
                return;
            }
            contentValues.putNull(Table.EXECUTIONDATELABEL);
        }

        public boolean exists(ProgramStage model) {
            return new Select().from(ProgramStage.class).where(getPrimaryModelWhere(model)).hasData();
        }

        public void loadFromCursor(Cursor cursor, ProgramStage model) {
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
            int indexprogram = cursor.getColumnIndex("program");
            if (indexprogram != -1) {
                if (cursor.isNull(indexprogram)) {
                    model.program = null;
                } else {
                    model.program = cursor.getString(indexprogram);
                }
            }
            int indexdataEntryType = cursor.getColumnIndex(Table.DATAENTRYTYPE);
            if (indexdataEntryType != -1) {
                if (cursor.isNull(indexdataEntryType)) {
                    model.dataEntryType = null;
                } else {
                    model.dataEntryType = cursor.getString(indexdataEntryType);
                }
            }
            int indexblockEntryForm = cursor.getColumnIndex(Table.BLOCKENTRYFORM);
            if (indexblockEntryForm != -1) {
                model.blockEntryForm = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexblockEntryForm)))).booleanValue();
            }
            int indexreportDateDescription = cursor.getColumnIndex(Table.REPORTDATEDESCRIPTION);
            if (indexreportDateDescription != -1) {
                if (cursor.isNull(indexreportDateDescription)) {
                    model.reportDateDescription = null;
                } else {
                    model.reportDateDescription = cursor.getString(indexreportDateDescription);
                }
            }
            int indexdisplayGenerateEventBox = cursor.getColumnIndex(Table.DISPLAYGENERATEEVENTBOX);
            if (indexdisplayGenerateEventBox != -1) {
                model.displayGenerateEventBox = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexdisplayGenerateEventBox)))).booleanValue();
            }
            int indexdescription = cursor.getColumnIndex("description");
            if (indexdescription != -1) {
                if (cursor.isNull(indexdescription)) {
                    model.description = null;
                } else {
                    model.description = cursor.getString(indexdescription);
                }
            }
            int indexexternalAccess = cursor.getColumnIndex("externalAccess");
            if (indexexternalAccess != -1) {
                model.externalAccess = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexexternalAccess)))).booleanValue();
            }
            int indexopenAfterEnrollment = cursor.getColumnIndex(Table.OPENAFTERENROLLMENT);
            if (indexopenAfterEnrollment != -1) {
                model.openAfterEnrollment = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexopenAfterEnrollment)))).booleanValue();
            }
            int indexcaptureCoordinates = cursor.getColumnIndex(Table.CAPTURECOORDINATES);
            if (indexcaptureCoordinates != -1) {
                model.captureCoordinates = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexcaptureCoordinates)))).booleanValue();
            }
            int indexdefaultTemplateMessage = cursor.getColumnIndex(Table.DEFAULTTEMPLATEMESSAGE);
            if (indexdefaultTemplateMessage != -1) {
                if (cursor.isNull(indexdefaultTemplateMessage)) {
                    model.defaultTemplateMessage = null;
                } else {
                    model.defaultTemplateMessage = cursor.getString(indexdefaultTemplateMessage);
                }
            }
            int indexremindCompleted = cursor.getColumnIndex(Table.REMINDCOMPLETED);
            if (indexremindCompleted != -1) {
                model.remindCompleted = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexremindCompleted)))).booleanValue();
            }
            int indexvalidCompleteOnly = cursor.getColumnIndex(Table.VALIDCOMPLETEONLY);
            if (indexvalidCompleteOnly != -1) {
                model.validCompleteOnly = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexvalidCompleteOnly)))).booleanValue();
            }
            int indexsortOrder = cursor.getColumnIndex("sortOrder");
            if (indexsortOrder != -1) {
                model.sortOrder = cursor.getInt(indexsortOrder);
            }
            int indexhideDueDate = cursor.getColumnIndex(Table.HIDEDUEDATE);
            if (indexhideDueDate != -1) {
                model.hideDueDate = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexhideDueDate)))).booleanValue();
            }
            int indexgeneratedByEnrollmentDate = cursor.getColumnIndex(Table.GENERATEDBYENROLLMENTDATE);
            if (indexgeneratedByEnrollmentDate != -1) {
                model.generatedByEnrollmentDate = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexgeneratedByEnrollmentDate)))).booleanValue();
            }
            int indexpreGenerateUID = cursor.getColumnIndex(Table.PREGENERATEUID);
            if (indexpreGenerateUID != -1) {
                model.preGenerateUID = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexpreGenerateUID)))).booleanValue();
            }
            int indexautoGenerateEvent = cursor.getColumnIndex(Table.AUTOGENERATEEVENT);
            if (indexautoGenerateEvent != -1) {
                model.autoGenerateEvent = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexautoGenerateEvent)))).booleanValue();
            }
            int indexallowGenerateNextVisit = cursor.getColumnIndex(Table.ALLOWGENERATENEXTVISIT);
            if (indexallowGenerateNextVisit != -1) {
                model.allowGenerateNextVisit = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexallowGenerateNextVisit)))).booleanValue();
            }
            int indexstandardInterval = cursor.getColumnIndex(Table.STANDARDINTERVAL);
            if (indexstandardInterval != -1) {
                model.standardInterval = cursor.getInt(indexstandardInterval);
            }
            int indexrepeatable = cursor.getColumnIndex(Table.REPEATABLE);
            if (indexrepeatable != -1) {
                model.repeatable = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexrepeatable)))).booleanValue();
            }
            int indexminDaysFromStart = cursor.getColumnIndex(Table.MINDAYSFROMSTART);
            if (indexminDaysFromStart != -1) {
                model.minDaysFromStart = cursor.getInt(indexminDaysFromStart);
            }
            int indexperiodType = cursor.getColumnIndex(Table.PERIODTYPE);
            if (indexperiodType != -1) {
                if (cursor.isNull(indexperiodType)) {
                    model.periodType = null;
                } else {
                    model.periodType = cursor.getString(indexperiodType);
                }
            }
            int indexexecutionDateLabel = cursor.getColumnIndex(Table.EXECUTIONDATELABEL);
            if (indexexecutionDateLabel == -1) {
                return;
            }
            if (cursor.isNull(indexexecutionDateLabel)) {
                model.executionDateLabel = null;
            } else {
                model.executionDateLabel = cursor.getString(indexexecutionDateLabel);
            }
        }

        public ConditionQueryBuilder<ProgramStage> getPrimaryModelWhere(ProgramStage model) {
            return new ConditionQueryBuilder(ProgramStage.class, Condition.column("id").is(model.id));
        }

        public ConditionQueryBuilder<ProgramStage> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(ProgramStage.class, Condition.column("id").is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return "CREATE TABLE IF NOT EXISTS `ProgramStage`(`name` TEXT, `displayName` TEXT, `created` TEXT, `lastUpdated` TEXT, `access` TEXT, `id` TEXT, `program` TEXT, `dataEntryType` TEXT, `blockEntryForm` INTEGER, `reportDateDescription` TEXT, `displayGenerateEventBox` INTEGER, `description` TEXT, `externalAccess` INTEGER, `openAfterEnrollment` INTEGER, `captureCoordinates` INTEGER, `defaultTemplateMessage` TEXT, `remindCompleted` INTEGER, `validCompleteOnly` INTEGER, `sortOrder` INTEGER, `hideDueDate` INTEGER, `generatedByEnrollmentDate` INTEGER, `preGenerateUID` INTEGER, `autoGenerateEvent` INTEGER, `allowGenerateNextVisit` INTEGER, `standardInterval` INTEGER, `repeatable` INTEGER, `minDaysFromStart` INTEGER, `periodType` TEXT, `executionDateLabel` TEXT, PRIMARY KEY(`id`));";
        }

        public final ProgramStage newInstance() {
            return new ProgramStage();
        }
    }

    public final class Table {
        public static final String ACCESS = "access";
        public static final String ALLOWGENERATENEXTVISIT = "allowGenerateNextVisit";
        public static final String AUTOGENERATEEVENT = "autoGenerateEvent";
        public static final String BLOCKENTRYFORM = "blockEntryForm";
        public static final String CAPTURECOORDINATES = "captureCoordinates";
        public static final String CREATED = "created";
        public static final String DATAENTRYTYPE = "dataEntryType";
        public static final String DEFAULTTEMPLATEMESSAGE = "defaultTemplateMessage";
        public static final String DESCRIPTION = "description";
        public static final String DISPLAYGENERATEEVENTBOX = "displayGenerateEventBox";
        public static final String DISPLAYNAME = "displayName";
        public static final String EXECUTIONDATELABEL = "executionDateLabel";
        public static final String EXTERNALACCESS = "externalAccess";
        public static final String GENERATEDBYENROLLMENTDATE = "generatedByEnrollmentDate";
        public static final String HIDEDUEDATE = "hideDueDate";
        public static final String ID = "id";
        public static final String LASTUPDATED = "lastUpdated";
        public static final String MINDAYSFROMSTART = "minDaysFromStart";
        public static final String NAME = "name";
        public static final String OPENAFTERENROLLMENT = "openAfterEnrollment";
        public static final String PERIODTYPE = "periodType";
        public static final String PREGENERATEUID = "preGenerateUID";
        public static final String PROGRAM = "program";
        public static final String REMINDCOMPLETED = "remindCompleted";
        public static final String REPEATABLE = "repeatable";
        public static final String REPORTDATEDESCRIPTION = "reportDateDescription";
        public static final String SORTORDER = "sortOrder";
        public static final String STANDARDINTERVAL = "standardInterval";
        public static final String TABLE_NAME = "ProgramStage";
        public static final String VALIDCOMPLETEONLY = "validCompleteOnly";
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

    @JsonProperty("program")
    public void setProgram(Map<String, Object> program) {
        this.program = (String) program.get("id");
    }

    @JsonAnySetter
    public void handleUnknown(String key, Object value) {
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

    public List<ProgramStageDataElement> getProgramStageDataElements() {
        if (this.programStageDataElements == null) {
            this.programStageDataElements = MetaDataController.getProgramStageDataElements(this);
        }
        return this.programStageDataElements;
    }

    public void setProgramStageDataElements(List<ProgramStageDataElement> programStageDataElements) {
        this.programStageDataElements = programStageDataElements;
    }

    public List<ProgramStageSection> getProgramStageSections() {
        if (this.programStageSections == null) {
            this.programStageSections = MetaDataController.getProgramStageSections(this.id);
        }
        return this.programStageSections;
    }

    public void setProgramStageSections(List<ProgramStageSection> programStageSections) {
        this.programStageSections = programStageSections;
    }

    public ProgramStageDataElement getProgramStageDataElement(String dataElementId) {
        if (getProgramStageDataElements() == null) {
            return null;
        }
        for (ProgramStageDataElement programStageDataElement : getProgramStageDataElements()) {
            if (programStageDataElement.getDataElement().equals(dataElementId)) {
                return programStageDataElement;
            }
        }
        return null;
    }

    public List<ProgramIndicator> getProgramIndicators() {
        if (this.programIndicators == null) {
            this.programIndicators = MetaDataController.getProgramIndicatorsByProgramStage(this.id);
        }
        return this.programIndicators;
    }

    public void setProgramIndicators(List<ProgramIndicator> programIndicators) {
        this.programIndicators = programIndicators;
    }

    public Program getProgram() {
        return MetaDataController.getProgram(this.program);
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getDataEntryType() {
        return this.dataEntryType;
    }

    public void setDataEntryType(String dataEntryType) {
        this.dataEntryType = dataEntryType;
    }

    public boolean getBlockEntryForm() {
        return this.blockEntryForm;
    }

    public void setBlockEntryForm(boolean blockEntryForm) {
        this.blockEntryForm = blockEntryForm;
    }

    public String getReportDateDescription() {
        return this.reportDateDescription;
    }

    public void setReportDateDescription(String reportDateDescription) {
        this.reportDateDescription = reportDateDescription;
    }

    public boolean getDisplayGenerateEventBox() {
        return this.displayGenerateEventBox;
    }

    public void setDisplayGenerateEventBox(boolean displayGenerateEventBox) {
        this.displayGenerateEventBox = displayGenerateEventBox;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getExternalAccess() {
        return this.externalAccess;
    }

    public void setExternalAccess(boolean externalAccess) {
        this.externalAccess = externalAccess;
    }

    public boolean getOpenAfterEnrollment() {
        return this.openAfterEnrollment;
    }

    public void setOpenAfterEnrollment(boolean openAfterEnrollment) {
        this.openAfterEnrollment = openAfterEnrollment;
    }

    public String getDefaultTemplateMessage() {
        return this.defaultTemplateMessage;
    }

    public void setDefaultTemplateMessage(String defaultTemplateMessage) {
        this.defaultTemplateMessage = defaultTemplateMessage;
    }

    public boolean getCaptureCoordinates() {
        return this.captureCoordinates;
    }

    public void setCaptureCoordinates(boolean captureCoordinates) {
        this.captureCoordinates = captureCoordinates;
    }

    public boolean getRemindCompleted() {
        return this.remindCompleted;
    }

    public void setRemindCompleted(boolean remindCompleted) {
        this.remindCompleted = remindCompleted;
    }

    public boolean getValidCompleteOnly() {
        return this.validCompleteOnly;
    }

    public void setValidCompleteOnly(boolean validCompleteOnly) {
        this.validCompleteOnly = validCompleteOnly;
    }

    public int getSortOrder() {
        return this.sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public boolean getGeneratedByEnrollmentDate() {
        return this.generatedByEnrollmentDate;
    }

    public void setGeneratedByEnrollmentDate(boolean generatedByEnrollmentDate) {
        this.generatedByEnrollmentDate = generatedByEnrollmentDate;
    }

    public boolean getPreGenerateUID() {
        return this.preGenerateUID;
    }

    public void setPreGenerateUID(boolean preGenerateUID) {
        this.preGenerateUID = preGenerateUID;
    }

    public boolean getAutoGenerateEvent() {
        return this.autoGenerateEvent;
    }

    public void setAutoGenerateEvent(boolean autoGenerateEvent) {
        this.autoGenerateEvent = autoGenerateEvent;
    }

    public boolean getAllowGenerateNextVisit() {
        return this.allowGenerateNextVisit;
    }

    public void setAllowGenerateNextVisit(boolean allowGenerateNextVisit) {
        this.allowGenerateNextVisit = allowGenerateNextVisit;
    }

    public boolean getRepeatable() {
        return this.repeatable;
    }

    public void setRepeatable(boolean repeatable) {
        this.repeatable = repeatable;
    }

    public int getMinDaysFromStart() {
        return this.minDaysFromStart;
    }

    public void setMinDaysFromStart(int minDaysFromStart) {
        this.minDaysFromStart = minDaysFromStart;
    }

    public boolean isHideDueDate() {
        return this.hideDueDate;
    }

    public void setHideDueDate(boolean hideDueDate) {
        this.hideDueDate = hideDueDate;
    }

    public int getStandardInterval() {
        return this.standardInterval;
    }

    public void setStandardInterval(int standardInterval) {
        this.standardInterval = standardInterval;
    }

    public String getPeriodType() {
        return this.periodType;
    }

    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }

    public boolean isRepeatable() {
        return this.repeatable;
    }

    public String getExecutionDateLabel() {
        return this.executionDateLabel;
    }

    public void setExecutionDateLabel(String executionDateLabel) {
        this.executionDateLabel = executionDateLabel;
    }

    public boolean isBlockEntryForm() {
        return this.blockEntryForm;
    }

    public boolean isDisplayGenerateEventBox() {
        return this.displayGenerateEventBox;
    }

    public boolean isExternalAccess() {
        return this.externalAccess;
    }

    public boolean isOpenAfterEnrollment() {
        return this.openAfterEnrollment;
    }

    public boolean isCaptureCoordinates() {
        return this.captureCoordinates;
    }

    public boolean isRemindCompleted() {
        return this.remindCompleted;
    }

    public boolean isValidCompleteOnly() {
        return this.validCompleteOnly;
    }

    public boolean isGeneratedByEnrollmentDate() {
        return this.generatedByEnrollmentDate;
    }

    public boolean isAutoGenerateEvent() {
        return this.autoGenerateEvent;
    }

    public boolean isPreGenerateUID() {
        return this.preGenerateUID;
    }

    public boolean isAllowGenerateNextVisit() {
        return this.allowGenerateNextVisit;
    }
}
