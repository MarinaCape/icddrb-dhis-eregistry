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

public class Attribute extends BaseMetaDataObject {
    @JsonProperty("code")
    String code;
    @JsonProperty("valueType")
    String valueType;

    public final class Adapter extends ModelAdapter<Attribute> {
        public Class<Attribute> getModelClass() {
            return Attribute.class;
        }

        public String getTableName() {
            return Table.TABLE_NAME;
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `Attribute` (`NAME`, `DISPLAYNAME`, `CREATED`, `LASTUPDATED`, `ACCESS`, `ID`, `VALUETYPE`, `CODE`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, Attribute model) {
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
            if (model.valueType != null) {
                statement.bindString(7, model.valueType);
            } else {
                statement.bindNull(7);
            }
            if (model.code != null) {
                statement.bindString(8, model.code);
            } else {
                statement.bindNull(8);
            }
        }

        public void bindToContentValues(ContentValues contentValues, Attribute model) {
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
            if (model.valueType != null) {
                contentValues.put("valueType", model.valueType);
            } else {
                contentValues.putNull("valueType");
            }
            if (model.code != null) {
                contentValues.put("code", model.code);
            } else {
                contentValues.putNull("code");
            }
        }

        public void bindToInsertValues(ContentValues contentValues, Attribute model) {
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
            if (model.valueType != null) {
                contentValues.put("valueType", model.valueType);
            } else {
                contentValues.putNull("valueType");
            }
            if (model.code != null) {
                contentValues.put("code", model.code);
            } else {
                contentValues.putNull("code");
            }
        }

        public boolean exists(Attribute model) {
            return new Select().from(Attribute.class).where(getPrimaryModelWhere(model)).hasData();
        }

        public void loadFromCursor(Cursor cursor, Attribute model) {
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
            int indexvalueType = cursor.getColumnIndex("valueType");
            if (indexvalueType != -1) {
                if (cursor.isNull(indexvalueType)) {
                    model.valueType = null;
                } else {
                    model.valueType = cursor.getString(indexvalueType);
                }
            }
            int indexcode = cursor.getColumnIndex("code");
            if (indexcode == -1) {
                return;
            }
            if (cursor.isNull(indexcode)) {
                model.code = null;
            } else {
                model.code = cursor.getString(indexcode);
            }
        }

        public ConditionQueryBuilder<Attribute> getPrimaryModelWhere(Attribute model) {
            return new ConditionQueryBuilder(Attribute.class, Condition.column("id").is(model.id));
        }

        public ConditionQueryBuilder<Attribute> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(Attribute.class, Condition.column("id").is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return "CREATE TABLE IF NOT EXISTS `Attribute`(`name` TEXT, `displayName` TEXT, `created` TEXT, `lastUpdated` TEXT, `access` TEXT, `id` TEXT, `valueType` TEXT, `code` TEXT, PRIMARY KEY(`id`));";
        }

        public final Attribute newInstance() {
            return new Attribute();
        }
    }

    public final class Table {
        public static final String ACCESS = "access";
        public static final String CODE = "code";
        public static final String CREATED = "created";
        public static final String DISPLAYNAME = "displayName";
        public static final String ID = "id";
        public static final String LASTUPDATED = "lastUpdated";
        public static final String NAME = "name";
        public static final String TABLE_NAME = "Attribute";
        public static final String VALUETYPE = "valueType";
    }

    public String getValueType() {
        return this.valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
