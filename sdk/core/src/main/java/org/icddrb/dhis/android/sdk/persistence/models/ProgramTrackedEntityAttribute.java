package org.icddrb.dhis.android.sdk.persistence.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.builder.Condition.Operation;
import com.raizlabs.android.dbflow.sql.builder.ConditionQueryBuilder;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;

public class ProgramTrackedEntityAttribute extends BaseModel {
    private static final String CLASS_TAG = ProgramTrackedEntityAttribute.class.getSimpleName();
    @JsonProperty("allowFutureDate")
    boolean allowFutureDate;
    @JsonProperty("displayInList")
    boolean displayInList;
    @JsonProperty("mandatory")
    boolean mandatory;
    @JsonIgnore
    String program;
    @JsonProperty("renderOptionsAsRadio")
    boolean renderOptionsAsRadio;
    int sortOrder;
    String trackedEntityAttribute;

    public final class Adapter extends ModelAdapter<ProgramTrackedEntityAttribute> {
        public Class<ProgramTrackedEntityAttribute> getModelClass() {
            return ProgramTrackedEntityAttribute.class;
        }

        public String getTableName() {
            return Table.TABLE_NAME;
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `ProgramTrackedEntityAttribute` (`TRACKEDENTITYATTRIBUTE`, `SORTORDER`, `ALLOWFUTUREDATE`, `DISPLAYINLIST`, `MANDATORY`, `PROGRAM`, `RENDEROPTIONSASRADIO`) VALUES (?, ?, ?, ?, ?, ?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, ProgramTrackedEntityAttribute model) {
            if (model.trackedEntityAttribute != null) {
                statement.bindString(1, model.trackedEntityAttribute);
            } else {
                statement.bindNull(1);
            }
            statement.bindLong(2, (long) model.sortOrder);
            Object modelallowFutureDate = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.allowFutureDate));
            if (modelallowFutureDate != null) {
                statement.bindLong(3, (long) ((Integer) modelallowFutureDate).intValue());
            } else {
                statement.bindNull(3);
            }
            Object modeldisplayInList = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.displayInList));
            if (modeldisplayInList != null) {
                statement.bindLong(4, (long) ((Integer) modeldisplayInList).intValue());
            } else {
                statement.bindNull(4);
            }
            Object modelmandatory = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.mandatory));
            if (modelmandatory != null) {
                statement.bindLong(5, (long) ((Integer) modelmandatory).intValue());
            } else {
                statement.bindNull(5);
            }
            if (model.program != null) {
                statement.bindString(6, model.program);
            } else {
                statement.bindNull(6);
            }
            Object modelrenderOptionsAsRadio = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.renderOptionsAsRadio));
            if (modelrenderOptionsAsRadio != null) {
                statement.bindLong(7, (long) ((Integer) modelrenderOptionsAsRadio).intValue());
            } else {
                statement.bindNull(7);
            }
        }

        public void bindToContentValues(ContentValues contentValues, ProgramTrackedEntityAttribute model) {
            if (model.trackedEntityAttribute != null) {
                contentValues.put("trackedEntityAttribute", model.trackedEntityAttribute);
            } else {
                contentValues.putNull("trackedEntityAttribute");
            }
            contentValues.put("sortOrder", Integer.valueOf(model.sortOrder));
            Object modelallowFutureDate = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.allowFutureDate));
            if (modelallowFutureDate != null) {
                contentValues.put("allowFutureDate", (Integer) modelallowFutureDate);
            } else {
                contentValues.putNull("allowFutureDate");
            }
            Object modeldisplayInList = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.displayInList));
            if (modeldisplayInList != null) {
                contentValues.put(Table.DISPLAYINLIST, (Integer) modeldisplayInList);
            } else {
                contentValues.putNull(Table.DISPLAYINLIST);
            }
            Object modelmandatory = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.mandatory));
            if (modelmandatory != null) {
                contentValues.put(Table.MANDATORY, (Integer) modelmandatory);
            } else {
                contentValues.putNull(Table.MANDATORY);
            }
            if (model.program != null) {
                contentValues.put("program", model.program);
            } else {
                contentValues.putNull("program");
            }
            Object modelrenderOptionsAsRadio = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.renderOptionsAsRadio));
            if (modelrenderOptionsAsRadio != null) {
                contentValues.put("renderOptionsAsRadio", (Integer) modelrenderOptionsAsRadio);
            } else {
                contentValues.putNull("renderOptionsAsRadio");
            }
        }

        public void bindToInsertValues(ContentValues contentValues, ProgramTrackedEntityAttribute model) {
            if (model.trackedEntityAttribute != null) {
                contentValues.put("trackedEntityAttribute", model.trackedEntityAttribute);
            } else {
                contentValues.putNull("trackedEntityAttribute");
            }
            contentValues.put("sortOrder", Integer.valueOf(model.sortOrder));
            Object modelallowFutureDate = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.allowFutureDate));
            if (modelallowFutureDate != null) {
                contentValues.put("allowFutureDate", (Integer) modelallowFutureDate);
            } else {
                contentValues.putNull("allowFutureDate");
            }
            Object modeldisplayInList = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.displayInList));
            if (modeldisplayInList != null) {
                contentValues.put(Table.DISPLAYINLIST, (Integer) modeldisplayInList);
            } else {
                contentValues.putNull(Table.DISPLAYINLIST);
            }
            Object modelmandatory = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.mandatory));
            if (modelmandatory != null) {
                contentValues.put(Table.MANDATORY, (Integer) modelmandatory);
            } else {
                contentValues.putNull(Table.MANDATORY);
            }
            if (model.program != null) {
                contentValues.put("program", model.program);
            } else {
                contentValues.putNull("program");
            }
            Object modelrenderOptionsAsRadio = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.renderOptionsAsRadio));
            if (modelrenderOptionsAsRadio != null) {
                contentValues.put("renderOptionsAsRadio", (Integer) modelrenderOptionsAsRadio);
            } else {
                contentValues.putNull("renderOptionsAsRadio");
            }
        }

        public boolean exists(ProgramTrackedEntityAttribute model) {
            return new Select().from(ProgramTrackedEntityAttribute.class).where(getPrimaryModelWhere(model)).hasData();
        }

        public void loadFromCursor(Cursor cursor, ProgramTrackedEntityAttribute model) {
            int indextrackedEntityAttribute = cursor.getColumnIndex("trackedEntityAttribute");
            if (indextrackedEntityAttribute != -1) {
                if (cursor.isNull(indextrackedEntityAttribute)) {
                    model.trackedEntityAttribute = null;
                } else {
                    model.trackedEntityAttribute = cursor.getString(indextrackedEntityAttribute);
                }
            }
            int indexsortOrder = cursor.getColumnIndex("sortOrder");
            if (indexsortOrder != -1) {
                model.sortOrder = cursor.getInt(indexsortOrder);
            }
            int indexallowFutureDate = cursor.getColumnIndex("allowFutureDate");
            if (indexallowFutureDate != -1) {
                model.allowFutureDate = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexallowFutureDate)))).booleanValue();
            }
            int indexdisplayInList = cursor.getColumnIndex(Table.DISPLAYINLIST);
            if (indexdisplayInList != -1) {
                model.displayInList = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexdisplayInList)))).booleanValue();
            }
            int indexmandatory = cursor.getColumnIndex(Table.MANDATORY);
            if (indexmandatory != -1) {
                model.mandatory = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexmandatory)))).booleanValue();
            }
            int indexprogram = cursor.getColumnIndex("program");
            if (indexprogram != -1) {
                if (cursor.isNull(indexprogram)) {
                    model.program = null;
                } else {
                    model.program = cursor.getString(indexprogram);
                }
            }
            int indexrenderOptionsAsRadio = cursor.getColumnIndex("renderOptionsAsRadio");
            if (indexrenderOptionsAsRadio != -1) {
                model.renderOptionsAsRadio = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexrenderOptionsAsRadio)))).booleanValue();
            }
        }

        public ConditionQueryBuilder<ProgramTrackedEntityAttribute> getPrimaryModelWhere(ProgramTrackedEntityAttribute model) {
            return new ConditionQueryBuilder(ProgramTrackedEntityAttribute.class, Condition.column("trackedEntityAttribute").is(model.trackedEntityAttribute), Condition.column("program").is(model.program));
        }

        public ConditionQueryBuilder<ProgramTrackedEntityAttribute> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(ProgramTrackedEntityAttribute.class, Condition.column("trackedEntityAttribute").is(Operation.EMPTY_PARAM), Condition.column("program").is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return "CREATE TABLE IF NOT EXISTS `ProgramTrackedEntityAttribute`(`trackedEntityAttribute` TEXT, `sortOrder` INTEGER, `allowFutureDate` INTEGER, `displayInList` INTEGER, `mandatory` INTEGER, `program` TEXT, `renderOptionsAsRadio` INTEGER, PRIMARY KEY(`trackedEntityAttribute`, `program`));";
        }

        public final ProgramTrackedEntityAttribute newInstance() {
            return new ProgramTrackedEntityAttribute();
        }
    }

    public final class Table {
        public static final String ALLOWFUTUREDATE = "allowFutureDate";
        public static final String DISPLAYINLIST = "displayInList";
        public static final String MANDATORY = "mandatory";
        public static final String PROGRAM = "program";
        public static final String RENDEROPTIONSASRADIO = "renderOptionsAsRadio";
        public static final String SORTORDER = "sortOrder";
        public static final String TABLE_NAME = "ProgramTrackedEntityAttribute";
        public static final String TRACKEDENTITYATTRIBUTE = "trackedEntityAttribute";
    }

    @JsonAnySetter
    public void handleUnknown(String key, Object value) {
    }

    @JsonProperty("trackedEntityAttribute")
    public void setTrackedEntityAttribute(TrackedEntityAttribute trackedEntityAttribute) {
        trackedEntityAttribute.async().save();
        this.trackedEntityAttribute = trackedEntityAttribute.id;
    }

    public boolean getAllowFutureDate() {
        return this.allowFutureDate;
    }

    public void setAllowFutureDate(boolean allowFutureDate) {
        this.allowFutureDate = allowFutureDate;
    }

    public boolean getDisplayInList() {
        return this.displayInList;
    }

    public void setDisplayInList(boolean displayInList) {
        this.displayInList = displayInList;
    }

    public boolean getMandatory() {
        return this.mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public String getProgram() {
        return this.program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public TrackedEntityAttribute getTrackedEntityAttribute() {
        return MetaDataController.getTrackedEntityAttribute(this.trackedEntityAttribute);
    }

    public void setTrackedEntityAttribute(String trackedEntityAttribute) {
        this.trackedEntityAttribute = trackedEntityAttribute;
    }

    public String getTrackedEntityAttributeId() {
        return this.trackedEntityAttribute;
    }

    public int getSortOrder() {
        return this.sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public boolean isRenderOptionsAsRadio() {
        return this.renderOptionsAsRadio;
    }

    public void setRenderOptionsAsRadio(boolean renderOptionsAsRadio) {
        this.renderOptionsAsRadio = renderOptionsAsRadio;
    }
}
