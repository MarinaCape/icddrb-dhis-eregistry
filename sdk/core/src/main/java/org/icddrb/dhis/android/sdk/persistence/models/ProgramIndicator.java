package org.icddrb.dhis.android.sdk.persistence.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.builder.Condition.Operation;
import com.raizlabs.android.dbflow.sql.builder.ConditionQueryBuilder;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import java.util.Map;
import java.util.regex.Pattern;

public class ProgramIndicator extends BaseNameableObject {
    public static final Pattern ATTRIBUTE_PATTERN = Pattern.compile("A\\{(\\w{11})\\}");
    public static final String CURRENT_DATE = "current_date";
    public static final Pattern DATAELEMENT_PATTERN = Pattern.compile("#\\{(\\w{11})\\.(\\w{11})\\}");
    public static final String ENROLLMENT_DATE = "enrollment_date";
    public static final String EVENT_DATE = "event_date";
    public static final String EXPRESSION_NOT_WELL_FORMED = "expression_not_well_formed";
    public static final String EXPRESSION_REGEXP = "(#|A|V|C)\\{(\\w+|incident_date|enrollment_date|current_date|event_date)\\.?(\\w*)\\}";
    public static final Pattern EXPRESSION_PATTERN = Pattern.compile(EXPRESSION_REGEXP);
    public static final String INCIDENT_DATE = "incident_date";
    public static final String KEY_ATTRIBUTE = "A";
    public static final String KEY_CONSTANT = "C";
    public static final String KEY_DATAELEMENT = "#";
    public static final String KEY_PROGRAM_VARIABLE = "V";
    public static final String SEPARATOR_ID = "\\.";
    public static final String SEP_OBJECT = ":";
    public static final String VALID = "valid";
    public static final Pattern VALUECOUNT_PATTERN = Pattern.compile("V\\{(value_count|zero_pos_value_count)\\}");
    public static final String VALUE_COUNT = "value_count";
    public static final String VALUE_TYPE_DATE = "date";
    public static final String VALUE_TYPE_INT = "int";
    public static final String VAR_VALUE_COUNT = "value_count";
    public static final String VAR_ZERO_POS_VALUE_COUNT = "zero_pos_value_count";
    @JsonProperty("code")
    String code;
    @JsonProperty("displayDescription")
    String displayDescription;
    @JsonProperty("displayInForm")
    boolean displayInForm;
    @JsonProperty("displayShortName")
    String displayShortName;
    @JsonProperty("expression")
    String expression;
    @JsonProperty("externalAccess")
    boolean externalAccess;
    String program;
    @JsonProperty("rootDate")
    String rootDate;
    @JsonProperty("valueType")
    String valueType;

    public final class Adapter extends ModelAdapter<ProgramIndicator> {
        public Class<ProgramIndicator> getModelClass() {
            return ProgramIndicator.class;
        }

        public String getTableName() {
            return Table.TABLE_NAME;
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `ProgramIndicator` (`NAME`, `DISPLAYNAME`, `CREATED`, `LASTUPDATED`, `ACCESS`, `ID`, `SHORTNAME`, `DESCRIPTION`, `CODE`, `EXPRESSION`, `DISPLAYDESCRIPTION`, `ROOTDATE`, `EXTERNALACCESS`, `VALUETYPE`, `DISPLAYSHORTNAME`, `DISPLAYINFORM`, `PROGRAM`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, ProgramIndicator model) {
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
            if (model.shortName != null) {
                statement.bindString(7, model.shortName);
            } else {
                statement.bindNull(7);
            }
            if (model.description != null) {
                statement.bindString(8, model.description);
            } else {
                statement.bindNull(8);
            }
            if (model.code != null) {
                statement.bindString(9, model.code);
            } else {
                statement.bindNull(9);
            }
            if (model.expression != null) {
                statement.bindString(10, model.expression);
            } else {
                statement.bindNull(10);
            }
            if (model.displayDescription != null) {
                statement.bindString(11, model.displayDescription);
            } else {
                statement.bindNull(11);
            }
            if (model.rootDate != null) {
                statement.bindString(12, model.rootDate);
            } else {
                statement.bindNull(12);
            }
            Object modelexternalAccess = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.externalAccess));
            if (modelexternalAccess != null) {
                statement.bindLong(13, (long) ((Integer) modelexternalAccess).intValue());
            } else {
                statement.bindNull(13);
            }
            if (model.valueType != null) {
                statement.bindString(14, model.valueType);
            } else {
                statement.bindNull(14);
            }
            if (model.displayShortName != null) {
                statement.bindString(15, model.displayShortName);
            } else {
                statement.bindNull(15);
            }
            Object modeldisplayInForm = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.displayInForm));
            if (modeldisplayInForm != null) {
                statement.bindLong(16, (long) ((Integer) modeldisplayInForm).intValue());
            } else {
                statement.bindNull(16);
            }
            if (model.program != null) {
                statement.bindString(17, model.program);
            } else {
                statement.bindNull(17);
            }
        }

        public void bindToContentValues(ContentValues contentValues, ProgramIndicator model) {
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
            if (model.shortName != null) {
                contentValues.put("shortName", model.shortName);
            } else {
                contentValues.putNull("shortName");
            }
            if (model.description != null) {
                contentValues.put("description", model.description);
            } else {
                contentValues.putNull("description");
            }
            if (model.code != null) {
                contentValues.put("code", model.code);
            } else {
                contentValues.putNull("code");
            }
            if (model.expression != null) {
                contentValues.put(Table.EXPRESSION, model.expression);
            } else {
                contentValues.putNull(Table.EXPRESSION);
            }
            if (model.displayDescription != null) {
                contentValues.put(Table.DISPLAYDESCRIPTION, model.displayDescription);
            } else {
                contentValues.putNull(Table.DISPLAYDESCRIPTION);
            }
            if (model.rootDate != null) {
                contentValues.put(Table.ROOTDATE, model.rootDate);
            } else {
                contentValues.putNull(Table.ROOTDATE);
            }
            Object modelexternalAccess = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.externalAccess));
            if (modelexternalAccess != null) {
                contentValues.put("externalAccess", (Integer) modelexternalAccess);
            } else {
                contentValues.putNull("externalAccess");
            }
            if (model.valueType != null) {
                contentValues.put("valueType", model.valueType);
            } else {
                contentValues.putNull("valueType");
            }
            if (model.displayShortName != null) {
                contentValues.put(Table.DISPLAYSHORTNAME, model.displayShortName);
            } else {
                contentValues.putNull(Table.DISPLAYSHORTNAME);
            }
            Object modeldisplayInForm = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.displayInForm));
            if (modeldisplayInForm != null) {
                contentValues.put(Table.DISPLAYINFORM, (Integer) modeldisplayInForm);
            } else {
                contentValues.putNull(Table.DISPLAYINFORM);
            }
            if (model.program != null) {
                contentValues.put("program", model.program);
            } else {
                contentValues.putNull("program");
            }
        }

        public void bindToInsertValues(ContentValues contentValues, ProgramIndicator model) {
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
            if (model.shortName != null) {
                contentValues.put("shortName", model.shortName);
            } else {
                contentValues.putNull("shortName");
            }
            if (model.description != null) {
                contentValues.put("description", model.description);
            } else {
                contentValues.putNull("description");
            }
            if (model.code != null) {
                contentValues.put("code", model.code);
            } else {
                contentValues.putNull("code");
            }
            if (model.expression != null) {
                contentValues.put(Table.EXPRESSION, model.expression);
            } else {
                contentValues.putNull(Table.EXPRESSION);
            }
            if (model.displayDescription != null) {
                contentValues.put(Table.DISPLAYDESCRIPTION, model.displayDescription);
            } else {
                contentValues.putNull(Table.DISPLAYDESCRIPTION);
            }
            if (model.rootDate != null) {
                contentValues.put(Table.ROOTDATE, model.rootDate);
            } else {
                contentValues.putNull(Table.ROOTDATE);
            }
            Object modelexternalAccess = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.externalAccess));
            if (modelexternalAccess != null) {
                contentValues.put("externalAccess", (Integer) modelexternalAccess);
            } else {
                contentValues.putNull("externalAccess");
            }
            if (model.valueType != null) {
                contentValues.put("valueType", model.valueType);
            } else {
                contentValues.putNull("valueType");
            }
            if (model.displayShortName != null) {
                contentValues.put(Table.DISPLAYSHORTNAME, model.displayShortName);
            } else {
                contentValues.putNull(Table.DISPLAYSHORTNAME);
            }
            Object modeldisplayInForm = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.displayInForm));
            if (modeldisplayInForm != null) {
                contentValues.put(Table.DISPLAYINFORM, (Integer) modeldisplayInForm);
            } else {
                contentValues.putNull(Table.DISPLAYINFORM);
            }
            if (model.program != null) {
                contentValues.put("program", model.program);
            } else {
                contentValues.putNull("program");
            }
        }

        public boolean exists(ProgramIndicator model) {
            return new Select().from(ProgramIndicator.class).where(getPrimaryModelWhere(model)).hasData();
        }

        public void loadFromCursor(Cursor cursor, ProgramIndicator model) {
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
            int indexshortName = cursor.getColumnIndex("shortName");
            if (indexshortName != -1) {
                if (cursor.isNull(indexshortName)) {
                    model.shortName = null;
                } else {
                    model.shortName = cursor.getString(indexshortName);
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
            int indexcode = cursor.getColumnIndex("code");
            if (indexcode != -1) {
                if (cursor.isNull(indexcode)) {
                    model.code = null;
                } else {
                    model.code = cursor.getString(indexcode);
                }
            }
            int indexexpression = cursor.getColumnIndex(Table.EXPRESSION);
            if (indexexpression != -1) {
                if (cursor.isNull(indexexpression)) {
                    model.expression = null;
                } else {
                    model.expression = cursor.getString(indexexpression);
                }
            }
            int indexdisplayDescription = cursor.getColumnIndex(Table.DISPLAYDESCRIPTION);
            if (indexdisplayDescription != -1) {
                if (cursor.isNull(indexdisplayDescription)) {
                    model.displayDescription = null;
                } else {
                    model.displayDescription = cursor.getString(indexdisplayDescription);
                }
            }
            int indexrootDate = cursor.getColumnIndex(Table.ROOTDATE);
            if (indexrootDate != -1) {
                if (cursor.isNull(indexrootDate)) {
                    model.rootDate = null;
                } else {
                    model.rootDate = cursor.getString(indexrootDate);
                }
            }
            int indexexternalAccess = cursor.getColumnIndex("externalAccess");
            if (indexexternalAccess != -1) {
                model.externalAccess = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexexternalAccess)))).booleanValue();
            }
            int indexvalueType = cursor.getColumnIndex("valueType");
            if (indexvalueType != -1) {
                if (cursor.isNull(indexvalueType)) {
                    model.valueType = null;
                } else {
                    model.valueType = cursor.getString(indexvalueType);
                }
            }
            int indexdisplayShortName = cursor.getColumnIndex(Table.DISPLAYSHORTNAME);
            if (indexdisplayShortName != -1) {
                if (cursor.isNull(indexdisplayShortName)) {
                    model.displayShortName = null;
                } else {
                    model.displayShortName = cursor.getString(indexdisplayShortName);
                }
            }
            int indexdisplayInForm = cursor.getColumnIndex(Table.DISPLAYINFORM);
            if (indexdisplayInForm != -1) {
                model.displayInForm = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexdisplayInForm)))).booleanValue();
            }
            int indexprogram = cursor.getColumnIndex("program");
            if (indexprogram == -1) {
                return;
            }
            if (cursor.isNull(indexprogram)) {
                model.program = null;
            } else {
                model.program = cursor.getString(indexprogram);
            }
        }

        public ConditionQueryBuilder<ProgramIndicator> getPrimaryModelWhere(ProgramIndicator model) {
            return new ConditionQueryBuilder(ProgramIndicator.class, Condition.column("id").is(model.id));
        }

        public ConditionQueryBuilder<ProgramIndicator> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(ProgramIndicator.class, Condition.column("id").is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return "CREATE TABLE IF NOT EXISTS `ProgramIndicator`(`name` TEXT, `displayName` TEXT, `created` TEXT, `lastUpdated` TEXT, `access` TEXT, `id` TEXT, `shortName` TEXT, `description` TEXT, `code` TEXT, `expression` TEXT, `displayDescription` TEXT, `rootDate` TEXT, `externalAccess` INTEGER, `valueType` TEXT, `displayShortName` TEXT, `displayInForm` INTEGER, `program` TEXT, PRIMARY KEY(`id`));";
        }

        public final ProgramIndicator newInstance() {
            return new ProgramIndicator();
        }
    }

    public final class Table {
        public static final String ACCESS = "access";
        public static final String CODE = "code";
        public static final String CREATED = "created";
        public static final String DESCRIPTION = "description";
        public static final String DISPLAYDESCRIPTION = "displayDescription";
        public static final String DISPLAYINFORM = "displayInForm";
        public static final String DISPLAYNAME = "displayName";
        public static final String DISPLAYSHORTNAME = "displayShortName";
        public static final String EXPRESSION = "expression";
        public static final String EXTERNALACCESS = "externalAccess";
        public static final String ID = "id";
        public static final String LASTUPDATED = "lastUpdated";
        public static final String NAME = "name";
        public static final String PROGRAM = "program";
        public static final String ROOTDATE = "rootDate";
        public static final String SHORTNAME = "shortName";
        public static final String TABLE_NAME = "ProgramIndicator";
        public static final String VALUETYPE = "valueType";
    }

    @JsonProperty("program")
    public void setProgram(Map<String, Object> program) {
        this.program = (String) program.get("id");
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getExpression() {
        return this.expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getDisplayDescription() {
        return this.displayDescription;
    }

    public void setDisplayDescription(String displayDescription) {
        this.displayDescription = displayDescription;
    }

    public String getRootDate() {
        return this.rootDate;
    }

    public void setRootDate(String rootDate) {
        this.rootDate = rootDate;
    }

    public boolean getExternalAccess() {
        return this.externalAccess;
    }

    public String getValueType() {
        return this.valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getDisplayShortName() {
        return this.displayShortName;
    }

    public void setDisplayShortName(String displayShortName) {
        this.displayShortName = displayShortName;
    }

    public String getProgram() {
        return this.program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public boolean isExternalAccess() {
        return this.externalAccess;
    }

    public void setExternalAccess(boolean externalAccess) {
        this.externalAccess = externalAccess;
    }

    public boolean isDisplayInForm() {
        return this.displayInForm;
    }

    public void setDisplayInForm(boolean displayInForm) {
        this.displayInForm = displayInForm;
    }
}
