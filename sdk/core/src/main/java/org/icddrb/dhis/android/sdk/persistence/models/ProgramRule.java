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
import java.util.regex.Pattern;

public class ProgramRule extends BaseMetaDataObject {
    public static final String CURRENT_DATE = "current_date";
    public static final Pattern DATAELEMENT_PATTERN = Pattern.compile("#\\{(\\w{11})\\.(\\w{11})\\}");
    public static final String ENROLLMENT_DATE = "enrollment_date";
    public static final Pattern EXPRESSION_PATTERN = Pattern.compile(EXPRESSION_REGEXP);
    public static final String EXPRESSION_REGEXP = "(#|A|V|C)\\{(\\w+|incident_date|enrollment_date|current_date)\\.?(\\w*)\\}";
    public static final String INCIDENT_DATE = "incident_date";
    public static final String KEY_ATTRIBUTE = "A";
    public static final String KEY_CONSTANT = "C";
    public static final String KEY_DATAELEMENT = "#";
    public static final String KEY_PROGRAM_VARIABLE = "V";
    public static final String SEPARATOR_ID = "\\.";
    public static final String VALUE_COUNT = "value_count";
    public static final String VALUE_TYPE_DATE = "date";
    public static final String VALUE_TYPE_INT = "int";
    public static final String VAR_VALUE_COUNT = "value_count";
    public static final String VAR_ZERO_POS_VALUE_COUNT = "zero_pos_value_count";
    String condition;
    boolean externalAction;
    Integer priority;
    String program;
    @JsonIgnore
    List<ProgramRuleAction> programRuleActions;
    String programStage;

    public final class Adapter extends ModelAdapter<ProgramRule> {
        public Class<ProgramRule> getModelClass() {
            return ProgramRule.class;
        }

        public String getTableName() {
            return Table.TABLE_NAME;
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `ProgramRule` (`NAME`, `DISPLAYNAME`, `CREATED`, `LASTUPDATED`, `ACCESS`, `ID`, `PROGRAMSTAGE`, `PROGRAM`, `CONDITION`, `EXTERNALACTION`, `PRIORITY`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, ProgramRule model) {
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
            if (model.programStage != null) {
                statement.bindString(7, model.programStage);
            } else {
                statement.bindNull(7);
            }
            if (model.program != null) {
                statement.bindString(8, model.program);
            } else {
                statement.bindNull(8);
            }
            if (model.condition != null) {
                statement.bindString(9, model.condition);
            } else {
                statement.bindNull(9);
            }
            Object modelexternalAction = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.externalAction));
            if (modelexternalAction != null) {
                statement.bindLong(10, (long) ((Integer) modelexternalAction).intValue());
            } else {
                statement.bindNull(10);
            }
            if (model.priority != null) {
                statement.bindLong(11, (long) model.priority.intValue());
            } else {
                statement.bindNull(11);
            }
        }

        public void bindToContentValues(ContentValues contentValues, ProgramRule model) {
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
            if (model.programStage != null) {
                contentValues.put("programStage", model.programStage);
            } else {
                contentValues.putNull("programStage");
            }
            if (model.program != null) {
                contentValues.put("program", model.program);
            } else {
                contentValues.putNull("program");
            }
            if (model.condition != null) {
                contentValues.put(Table.CONDITION, model.condition);
            } else {
                contentValues.putNull(Table.CONDITION);
            }
            Object modelexternalAction = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.externalAction));
            if (modelexternalAction != null) {
                contentValues.put(Table.EXTERNALACTION, (Integer) modelexternalAction);
            } else {
                contentValues.putNull(Table.EXTERNALACTION);
            }
            if (model.priority != null) {
                contentValues.put(Table.PRIORITY, model.priority);
            } else {
                contentValues.putNull(Table.PRIORITY);
            }
        }

        public void bindToInsertValues(ContentValues contentValues, ProgramRule model) {
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
            if (model.programStage != null) {
                contentValues.put("programStage", model.programStage);
            } else {
                contentValues.putNull("programStage");
            }
            if (model.program != null) {
                contentValues.put("program", model.program);
            } else {
                contentValues.putNull("program");
            }
            if (model.condition != null) {
                contentValues.put(Table.CONDITION, model.condition);
            } else {
                contentValues.putNull(Table.CONDITION);
            }
            Object modelexternalAction = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.externalAction));
            if (modelexternalAction != null) {
                contentValues.put(Table.EXTERNALACTION, (Integer) modelexternalAction);
            } else {
                contentValues.putNull(Table.EXTERNALACTION);
            }
            if (model.priority != null) {
                contentValues.put(Table.PRIORITY, model.priority);
            } else {
                contentValues.putNull(Table.PRIORITY);
            }
        }

        public boolean exists(ProgramRule model) {
            return new Select().from(ProgramRule.class).where(getPrimaryModelWhere(model)).hasData();
        }

        public void loadFromCursor(Cursor cursor, ProgramRule model) {
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
            int indexprogramStage = cursor.getColumnIndex("programStage");
            if (indexprogramStage != -1) {
                if (cursor.isNull(indexprogramStage)) {
                    model.programStage = null;
                } else {
                    model.programStage = cursor.getString(indexprogramStage);
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
            int indexcondition = cursor.getColumnIndex(Table.CONDITION);
            if (indexcondition != -1) {
                if (cursor.isNull(indexcondition)) {
                    model.condition = null;
                } else {
                    model.condition = cursor.getString(indexcondition);
                }
            }
            int indexexternalAction = cursor.getColumnIndex(Table.EXTERNALACTION);
            if (indexexternalAction != -1) {
                model.externalAction = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexexternalAction)))).booleanValue();
            }
            int indexpriority = cursor.getColumnIndex(Table.PRIORITY);
            if (indexpriority == -1) {
                return;
            }
            if (cursor.isNull(indexpriority)) {
                model.priority = null;
            } else {
                model.priority = Integer.valueOf(cursor.getInt(indexpriority));
            }
        }

        public ConditionQueryBuilder<ProgramRule> getPrimaryModelWhere(ProgramRule model) {
            return new ConditionQueryBuilder(ProgramRule.class, Condition.column("id").is(model.id));
        }

        public ConditionQueryBuilder<ProgramRule> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(ProgramRule.class, Condition.column("id").is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return "CREATE TABLE IF NOT EXISTS `ProgramRule`(`name` TEXT, `displayName` TEXT, `created` TEXT, `lastUpdated` TEXT, `access` TEXT, `id` TEXT, `programStage` TEXT, `program` TEXT, `condition` TEXT, `externalAction` INTEGER, `priority` INTEGER, PRIMARY KEY(`id`));";
        }

        public final ProgramRule newInstance() {
            return new ProgramRule();
        }
    }

    public final class Table {
        public static final String ACCESS = "access";
        public static final String CONDITION = "condition";
        public static final String CREATED = "created";
        public static final String DISPLAYNAME = "displayName";
        public static final String EXTERNALACTION = "externalAction";
        public static final String ID = "id";
        public static final String LASTUPDATED = "lastUpdated";
        public static final String NAME = "name";
        public static final String PRIORITY = "priority";
        public static final String PROGRAM = "program";
        public static final String PROGRAMSTAGE = "programStage";
        public static final String TABLE_NAME = "ProgramRule";
    }

    @JsonProperty("programStage")
    public void setProgramStage(Map<String, Object> programStage) {
        this.programStage = (String) programStage.get("id");
    }

    @JsonProperty("program")
    public void setProgram(Map<String, Object> program) {
        this.program = (String) program.get("id");
    }

    public List<ProgramRuleAction> getProgramRuleActions() {
        return new Select().from(ProgramRuleAction.class).where(Condition.column(org.icddrb.dhis.android.sdk.persistence.models.ProgramRuleAction.Table.PROGRAMRULE).is(this.id)).queryList();
    }

    public void setProgramRuleActions(List<ProgramRuleAction> programRuleActions) {
        this.programRuleActions = programRuleActions;
    }

    public String getProgram() {
        return this.program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getProgramStage() {
        return this.programStage;
    }

    public void setProgramStage(String programStage) {
        this.programStage = programStage;
    }

    public boolean getExternalAction() {
        return this.externalAction;
    }

    public void setExternalAction(boolean externalAction) {
        this.externalAction = externalAction;
    }

    public String getCondition() {
        return this.condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Integer getPriority() {
        return this.priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
