package org.icddrb.dhis.android.sdk.persistence.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.builder.Condition.Operation;
import com.raizlabs.android.dbflow.sql.builder.ConditionQueryBuilder;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

public class Conflict extends BaseModel {
    protected int id;
    int importSummary;
    @JsonProperty("object")
    String object;
    @JsonProperty("value")
    String value;

    public final class Adapter extends ModelAdapter<Conflict> {
        public Class<Conflict> getModelClass() {
            return Conflict.class;
        }

        public String getTableName() {
            return Table.TABLE_NAME;
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `Conflict` (`IMPORTSUMMARY`, `OBJECT`, `VALUE`) VALUES (?, ?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, Conflict model) {
            statement.bindLong(1, (long) model.importSummary);
            if (model.object != null) {
                statement.bindString(2, model.object);
            } else {
                statement.bindNull(2);
            }
            if (model.value != null) {
                statement.bindString(3, model.value);
            } else {
                statement.bindNull(3);
            }
        }

        public void bindToContentValues(ContentValues contentValues, Conflict model) {
            contentValues.put("id", Integer.valueOf(model.id));
            contentValues.put("importSummary", Integer.valueOf(model.importSummary));
            if (model.object != null) {
                contentValues.put(Table.OBJECT, model.object);
            } else {
                contentValues.putNull(Table.OBJECT);
            }
            if (model.value != null) {
                contentValues.put("value", model.value);
            } else {
                contentValues.putNull("value");
            }
        }

        public void bindToInsertValues(ContentValues contentValues, Conflict model) {
            contentValues.put("importSummary", Integer.valueOf(model.importSummary));
            if (model.object != null) {
                contentValues.put(Table.OBJECT, model.object);
            } else {
                contentValues.putNull(Table.OBJECT);
            }
            if (model.value != null) {
                contentValues.put("value", model.value);
            } else {
                contentValues.putNull("value");
            }
        }

        public boolean exists(Conflict model) {
            return model.id > 0;
        }

        public void loadFromCursor(Cursor cursor, Conflict model) {
            int indexid = cursor.getColumnIndex("id");
            if (indexid != -1) {
                model.id = cursor.getInt(indexid);
            }
            int indeximportSummary = cursor.getColumnIndex("importSummary");
            if (indeximportSummary != -1) {
                model.importSummary = cursor.getInt(indeximportSummary);
            }
            int indexobject = cursor.getColumnIndex(Table.OBJECT);
            if (indexobject != -1) {
                if (cursor.isNull(indexobject)) {
                    model.object = null;
                } else {
                    model.object = cursor.getString(indexobject);
                }
            }
            int indexvalue = cursor.getColumnIndex("value");
            if (indexvalue == -1) {
                return;
            }
            if (cursor.isNull(indexvalue)) {
                model.value = null;
            } else {
                model.value = cursor.getString(indexvalue);
            }
        }

        public void updateAutoIncrement(Conflict model, long id) {
            model.id = (int) id;
        }

        public long getAutoIncrementingId(Conflict model) {
            return (long) model.id;
        }

        public String getAutoIncrementingColumnName() {
            return "id";
        }

        public ConditionQueryBuilder<Conflict> getPrimaryModelWhere(Conflict model) {
            return new ConditionQueryBuilder(Conflict.class, Condition.column("id").is(Integer.valueOf(model.id)));
        }

        public ConditionQueryBuilder<Conflict> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(Conflict.class, Condition.column("id").is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return "CREATE TABLE IF NOT EXISTS `Conflict`(`id` INTEGER PRIMARY KEY AUTOINCREMENT, `importSummary` INTEGER, `object` TEXT, `value` TEXT);";
        }

        public final Conflict newInstance() {
            return new Conflict();
        }
    }

    public final class Table {
        public static final String ID = "id";
        public static final String IMPORTSUMMARY = "importSummary";
        public static final String OBJECT = "object";
        public static final String TABLE_NAME = "Conflict";
        public static final String VALUE = "value";
    }

    @JsonAnySetter
    public void handleUnknown(String key, Object value) {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImportSummary() {
        return this.importSummary;
    }

    public void setImportSummary(int importSummary) {
        this.importSummary = importSummary;
    }

    public String getObject() {
        return this.object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
