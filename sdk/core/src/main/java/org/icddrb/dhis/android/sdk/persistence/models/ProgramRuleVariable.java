package org.icddrb.dhis.android.sdk.persistence.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.builder.Condition.Operation;
import com.raizlabs.android.dbflow.sql.builder.ConditionQueryBuilder;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import java.util.List;
import java.util.Map;
import org.icddrb.dhis.android.sdk.utils.api.ProgramRuleVariableSourceType;
import org.icddrb.dhis.android.sdk.utils.api.ValueType;

public class ProgramRuleVariable extends BaseMetaDataObject {
    @JsonIgnore
    List<String> allValues;
    String dataElement;
    boolean externalAccess;
    @JsonIgnore
    boolean hasValue;
    String program;
    String programStage;
    @JsonProperty("programRuleVariableSourceType")
    ProgramRuleVariableSourceType sourceType;
    String trackedEntityAttribute;
    @JsonIgnore
    String variableEventDate;
    @JsonIgnore
    ValueType variableType;
    @JsonIgnore
    String variableValue;

    public final class Adapter extends ModelAdapter<ProgramRuleVariable> {
        public Class<ProgramRuleVariable> getModelClass() {
            return ProgramRuleVariable.class;
        }

        public String getTableName() {
            return Table.TABLE_NAME;
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `ProgramRuleVariable` (`NAME`, `DISPLAYNAME`, `CREATED`, `LASTUPDATED`, `ACCESS`, `ID`, `TRACKEDENTITYATTRIBUTE`, `DATAELEMENT`, `SOURCETYPE`, `EXTERNALACCESS`, `PROGRAM`, `PROGRAMSTAGE`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, ProgramRuleVariable model) {
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
            if (model.trackedEntityAttribute != null) {
                statement.bindString(7, model.trackedEntityAttribute);
            } else {
                statement.bindNull(7);
            }
            if (model.dataElement != null) {
                statement.bindString(8, model.dataElement);
            } else {
                statement.bindNull(8);
            }
            ProgramRuleVariableSourceType modelsourceType = model.sourceType;
            if (modelsourceType != null) {
                statement.bindString(9, modelsourceType.name());
            } else {
                statement.bindNull(9);
            }
            Object modelexternalAccess = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.externalAccess));
            if (modelexternalAccess != null) {
                statement.bindLong(10, (long) ((Integer) modelexternalAccess).intValue());
            } else {
                statement.bindNull(10);
            }
            if (model.program != null) {
                statement.bindString(11, model.program);
            } else {
                statement.bindNull(11);
            }
            if (model.programStage != null) {
                statement.bindString(12, model.programStage);
            } else {
                statement.bindNull(12);
            }
        }

        public void bindToContentValues(ContentValues contentValues, ProgramRuleVariable model) {
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
            if (model.trackedEntityAttribute != null) {
                contentValues.put("trackedEntityAttribute", model.trackedEntityAttribute);
            } else {
                contentValues.putNull("trackedEntityAttribute");
            }
            if (model.dataElement != null) {
                contentValues.put("dataElement", model.dataElement);
            } else {
                contentValues.putNull("dataElement");
            }
            ProgramRuleVariableSourceType modelsourceType = model.sourceType;
            if (modelsourceType != null) {
                contentValues.put(Table.SOURCETYPE, modelsourceType.name());
            } else {
                contentValues.putNull(Table.SOURCETYPE);
            }
            Object modelexternalAccess = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.externalAccess));
            if (modelexternalAccess != null) {
                contentValues.put("externalAccess", (Integer) modelexternalAccess);
            } else {
                contentValues.putNull("externalAccess");
            }
            if (model.program != null) {
                contentValues.put("program", model.program);
            } else {
                contentValues.putNull("program");
            }
            if (model.programStage != null) {
                contentValues.put("programStage", model.programStage);
            } else {
                contentValues.putNull("programStage");
            }
        }

        public void bindToInsertValues(ContentValues contentValues, ProgramRuleVariable model) {
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
            if (model.trackedEntityAttribute != null) {
                contentValues.put("trackedEntityAttribute", model.trackedEntityAttribute);
            } else {
                contentValues.putNull("trackedEntityAttribute");
            }
            if (model.dataElement != null) {
                contentValues.put("dataElement", model.dataElement);
            } else {
                contentValues.putNull("dataElement");
            }
            ProgramRuleVariableSourceType modelsourceType = model.sourceType;
            if (modelsourceType != null) {
                contentValues.put(Table.SOURCETYPE, modelsourceType.name());
            } else {
                contentValues.putNull(Table.SOURCETYPE);
            }
            Object modelexternalAccess = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.externalAccess));
            if (modelexternalAccess != null) {
                contentValues.put("externalAccess", (Integer) modelexternalAccess);
            } else {
                contentValues.putNull("externalAccess");
            }
            if (model.program != null) {
                contentValues.put("program", model.program);
            } else {
                contentValues.putNull("program");
            }
            if (model.programStage != null) {
                contentValues.put("programStage", model.programStage);
            } else {
                contentValues.putNull("programStage");
            }
        }

        public boolean exists(ProgramRuleVariable model) {
            return new Select().from(ProgramRuleVariable.class).where(getPrimaryModelWhere(model)).hasData();
        }

        public void loadFromCursor(Cursor cursor, ProgramRuleVariable model) {
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
            int indextrackedEntityAttribute = cursor.getColumnIndex("trackedEntityAttribute");
            if (indextrackedEntityAttribute != -1) {
                if (cursor.isNull(indextrackedEntityAttribute)) {
                    model.trackedEntityAttribute = null;
                } else {
                    model.trackedEntityAttribute = cursor.getString(indextrackedEntityAttribute);
                }
            }
            int indexdataElement = cursor.getColumnIndex("dataElement");
            if (indexdataElement != -1) {
                if (cursor.isNull(indexdataElement)) {
                    model.dataElement = null;
                } else {
                    model.dataElement = cursor.getString(indexdataElement);
                }
            }
            int indexsourceType = cursor.getColumnIndex(Table.SOURCETYPE);
            if (indexsourceType != -1) {
                if (cursor.isNull(indexsourceType)) {
                    model.sourceType = null;
                } else {
                    model.sourceType = ProgramRuleVariableSourceType.valueOf(cursor.getString(indexsourceType));
                }
            }
            int indexexternalAccess = cursor.getColumnIndex("externalAccess");
            if (indexexternalAccess != -1) {
                model.externalAccess = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexexternalAccess)))).booleanValue();
            }
            int indexprogram = cursor.getColumnIndex("program");
            if (indexprogram != -1) {
                if (cursor.isNull(indexprogram)) {
                    model.program = null;
                } else {
                    model.program = cursor.getString(indexprogram);
                }
            }
            int indexprogramStage = cursor.getColumnIndex("programStage");
            if (indexprogramStage == -1) {
                return;
            }
            if (cursor.isNull(indexprogramStage)) {
                model.programStage = null;
            } else {
                model.programStage = cursor.getString(indexprogramStage);
            }
        }

        public ConditionQueryBuilder<ProgramRuleVariable> getPrimaryModelWhere(ProgramRuleVariable model) {
            return new ConditionQueryBuilder(ProgramRuleVariable.class, Condition.column("id").is(model.id));
        }

        public ConditionQueryBuilder<ProgramRuleVariable> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(ProgramRuleVariable.class, Condition.column("id").is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return "CREATE TABLE IF NOT EXISTS `ProgramRuleVariable`(`name` TEXT, `displayName` TEXT, `created` TEXT, `lastUpdated` TEXT, `access` TEXT, `id` TEXT, `trackedEntityAttribute` TEXT, `dataElement` TEXT, `sourceType` TEXT, `externalAccess` INTEGER, `program` TEXT, `programStage` TEXT, PRIMARY KEY(`id`));";
        }

        public final ProgramRuleVariable newInstance() {
            return new ProgramRuleVariable();
        }
    }

    public final class Table {
        public static final String ACCESS = "access";
        public static final String CREATED = "created";
        public static final String DATAELEMENT = "dataElement";
        public static final String DISPLAYNAME = "displayName";
        public static final String EXTERNALACCESS = "externalAccess";
        public static final String ID = "id";
        public static final String LASTUPDATED = "lastUpdated";
        public static final String NAME = "name";
        public static final String PROGRAM = "program";
        public static final String PROGRAMSTAGE = "programStage";
        public static final String SOURCETYPE = "sourceType";
        public static final String TABLE_NAME = "ProgramRuleVariable";
        public static final String TRACKEDENTITYATTRIBUTE = "trackedEntityAttribute";
    }

    @JsonProperty("programStage")
    public void setProgramStage(Map<String, Object> programStage) {
        this.programStage = (String) programStage.get("id");
    }

    @JsonProperty("program")
    public void setProgram(Map<String, Object> program) {
        this.program = (String) program.get("id");
    }

    @JsonProperty("dataElement")
    public void setDataElement(Map<String, Object> dataElement) {
        this.dataElement = (String) dataElement.get("id");
    }

    @JsonProperty("trackedEntityAttribute")
    public void setTrackedEntityAttribute(Map<String, Object> trackedEntityAttribute) {
        this.trackedEntityAttribute = (String) trackedEntityAttribute.get("id");
    }

    public String getProgramStage() {
        return this.programStage;
    }

    public void setProgramStage(String programStage) {
        this.programStage = programStage;
    }

    public String getTrackedEntityAttribute() {
        return this.trackedEntityAttribute;
    }

    public void setTrackedEntityAttribute(String trackedEntityAttribute) {
        this.trackedEntityAttribute = trackedEntityAttribute;
    }

    public String getDataElement() {
        return this.dataElement;
    }

    public void setDataElement(String dataElement) {
        this.dataElement = dataElement;
    }

    public String getProgram() {
        return this.program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public boolean getExternalAccess() {
        return this.externalAccess;
    }

    public void setExternalAccess(boolean externalAccess) {
        this.externalAccess = externalAccess;
    }

    public ProgramRuleVariableSourceType getSourceType() {
        return this.sourceType;
    }

    public void setSourceType(ProgramRuleVariableSourceType sourceType) {
        this.sourceType = sourceType;
    }

    public String getVariableValue() {
        return this.variableValue;
    }

    public void setVariableValue(String variableValue) {
        this.variableValue = variableValue;
    }

    public ValueType getVariableType() {
        return this.variableType;
    }

    public void setVariableType(ValueType variableType) {
        this.variableType = variableType;
    }

    public boolean isHasValue() {
        return this.hasValue;
    }

    public void setHasValue(boolean hasValue) {
        this.hasValue = hasValue;
    }

    public String getVariableEventDate() {
        return this.variableEventDate;
    }

    public void setVariableEventDate(String variableEventDate) {
        this.variableEventDate = variableEventDate;
    }

    public List<String> getAllValues() {
        return this.allValues;
    }

    public void setAllValues(List<String> allValues) {
        this.allValues = allValues;
    }
}
