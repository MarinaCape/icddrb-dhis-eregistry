package org.icddrb.dhis.android.sdk.persistence.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import com.raizlabs.android.dbflow.annotation.NotNull;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.builder.Condition.Operation;
import com.raizlabs.android.dbflow.sql.builder.ConditionQueryBuilder;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

public final class InterpretationElement extends BaseMetaDataObject {
    public static final String TYPE_CHART = "chart";
    public static final String TYPE_DATA_SET = "dataSet";
    public static final String TYPE_MAP = "map";
    public static final String TYPE_ORGANISATION_UNIT = "organisationUnit";
    public static final String TYPE_PERIOD = "period";
    public static final String TYPE_REPORT_TABLE = "reportTable";
    Interpretation interpretation;
    @NotNull
    String type;

    public final class Adapter extends ModelAdapter<InterpretationElement> {
        public Class<InterpretationElement> getModelClass() {
            return InterpretationElement.class;
        }

        public String getTableName() {
            return Table.TABLE_NAME;
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `InterpretationElement` (`NAME`, `DISPLAYNAME`, `CREATED`, `LASTUPDATED`, `ACCESS`, `ID`, `TYPE`, `interpretation`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, InterpretationElement model) {
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
            if (model.type != null) {
                statement.bindString(7, model.type);
            } else {
                statement.bindNull(7);
            }
            if (model.interpretation == null) {
                statement.bindNull(8);
            } else if (model.interpretation.id != null) {
                statement.bindString(8, model.interpretation.id);
            } else {
                statement.bindNull(8);
            }
        }

        public void bindToContentValues(ContentValues contentValues, InterpretationElement model) {
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
            if (model.type != null) {
                contentValues.put("type", model.type);
            } else {
                contentValues.putNull("type");
            }
            if (model.interpretation == null) {
                contentValues.putNull("interpretation");
            } else if (model.interpretation.id != null) {
                contentValues.put("interpretation", model.interpretation.id);
            } else {
                contentValues.putNull("interpretation");
            }
        }

        public void bindToInsertValues(ContentValues contentValues, InterpretationElement model) {
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
            if (model.type != null) {
                contentValues.put("type", model.type);
            } else {
                contentValues.putNull("type");
            }
            if (model.interpretation == null) {
                contentValues.putNull("interpretation");
            } else if (model.interpretation.id != null) {
                contentValues.put("interpretation", model.interpretation.id);
            } else {
                contentValues.putNull("interpretation");
            }
        }

        public boolean exists(InterpretationElement model) {
            return new Select().from(InterpretationElement.class).where(getPrimaryModelWhere(model)).hasData();
        }

        public void loadFromCursor(Cursor cursor, InterpretationElement model) {
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
            int indextype = cursor.getColumnIndex("type");
            if (indextype != -1) {
                model.type = cursor.getString(indextype);
            }
            int indexinterpretation = cursor.getColumnIndex("interpretation");
            if (indexinterpretation != -1 && !cursor.isNull(indexinterpretation)) {
                model.interpretation = (Interpretation) new Select().from(Interpretation.class).where().and(Condition.column("id").is(cursor.getString(indexinterpretation))).querySingle();
            }
        }

        public ConditionQueryBuilder<InterpretationElement> getPrimaryModelWhere(InterpretationElement model) {
            return new ConditionQueryBuilder(InterpretationElement.class, Condition.column("id").is(model.id));
        }

        public ConditionQueryBuilder<InterpretationElement> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(InterpretationElement.class, Condition.column("id").is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return String.format("CREATE TABLE IF NOT EXISTS `InterpretationElement`(`name` TEXT, `displayName` TEXT, `created` TEXT, `lastUpdated` TEXT, `access` TEXT, `id` TEXT, `type` TEXT NOT NULL ON CONFLICT FAIL,  `interpretation` TEXT, PRIMARY KEY(`id`), FOREIGN KEY(`interpretation`) REFERENCES `%1s` (`id`) ON UPDATE NO ACTION ON DELETE CASCADE );", new Object[]{FlowManager.getTableName(Interpretation.class)});
        }

        public final InterpretationElement newInstance() {
            return new InterpretationElement();
        }
    }

    public final class Table {
        public static final String ACCESS = "access";
        public static final String CREATED = "created";
        public static final String DISPLAYNAME = "displayName";
        public static final String ID = "id";
        public static final String INTERPRETATION_INTERPRETATION = "interpretation";
        public static final String LASTUPDATED = "lastUpdated";
        public static final String NAME = "name";
        public static final String TABLE_NAME = "InterpretationElement";
        public static final String TYPE = "type";
    }

    public static InterpretationElement fromDashboardElement(Interpretation interpretation, DashboardElement dashboardElement, String mimeType) {
        InterpretationElement interpretationElement = new InterpretationElement();
        interpretationElement.setUid(dashboardElement.getUid());
        interpretationElement.setName(dashboardElement.getName());
        interpretationElement.setCreated(dashboardElement.getCreated());
        interpretationElement.setLastUpdated(dashboardElement.getLastUpdated());
        interpretationElement.setAccess(dashboardElement.getAccess());
        interpretationElement.setType(mimeType);
        interpretationElement.setInterpretation(interpretation);
        return interpretationElement;
    }

    public Interpretation getInterpretation() {
        return this.interpretation;
    }

    public void setInterpretation(Interpretation interpretation) {
        this.interpretation = interpretation;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
