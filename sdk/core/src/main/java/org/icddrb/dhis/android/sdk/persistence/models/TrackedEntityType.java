package org.icddrb.dhis.android.sdk.persistence.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.builder.Condition.Operation;
import com.raizlabs.android.dbflow.sql.builder.ConditionQueryBuilder;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

public class TrackedEntityType extends BaseModel {
    @JsonProperty("id")
    String id;
    @JsonProperty("name")
    String name;

    public final class Adapter extends ModelAdapter<TrackedEntityType> {
        public Class<TrackedEntityType> getModelClass() {
            return TrackedEntityType.class;
        }

        public String getTableName() {
            return Table.TABLE_NAME;
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `TrackedEntityType` (`ID`, `NAME`) VALUES (?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, TrackedEntityType model) {
            if (model.id != null) {
                statement.bindString(1, model.id);
            } else {
                statement.bindNull(1);
            }
            if (model.name != null) {
                statement.bindString(2, model.name);
            } else {
                statement.bindNull(2);
            }
        }

        public void bindToContentValues(ContentValues contentValues, TrackedEntityType model) {
            if (model.id != null) {
                contentValues.put("id", model.id);
            } else {
                contentValues.putNull("id");
            }
            if (model.name != null) {
                contentValues.put("name", model.name);
            } else {
                contentValues.putNull("name");
            }
        }

        public void bindToInsertValues(ContentValues contentValues, TrackedEntityType model) {
            if (model.id != null) {
                contentValues.put("id", model.id);
            } else {
                contentValues.putNull("id");
            }
            if (model.name != null) {
                contentValues.put("name", model.name);
            } else {
                contentValues.putNull("name");
            }
        }

        public boolean exists(TrackedEntityType model) {
            return new Select().from(TrackedEntityType.class).where(getPrimaryModelWhere(model)).hasData();
        }

        public void loadFromCursor(Cursor cursor, TrackedEntityType model) {
            int indexid = cursor.getColumnIndex("id");
            if (indexid != -1) {
                if (cursor.isNull(indexid)) {
                    model.id = null;
                } else {
                    model.id = cursor.getString(indexid);
                }
            }
            int indexname = cursor.getColumnIndex("name");
            if (indexname == -1) {
                return;
            }
            if (cursor.isNull(indexname)) {
                model.name = null;
            } else {
                model.name = cursor.getString(indexname);
            }
        }

        public boolean hasCachingId() {
            return true;
        }

        public Object getCachingId(TrackedEntityType model) {
            return model.id;
        }

        public String getCachingColumnName() {
            return "id";
        }

        public Object getCachingIdFromCursorIndex(Cursor cursor, int indexid) {
            return cursor.getString(indexid);
        }

        public ConditionQueryBuilder<TrackedEntityType> getPrimaryModelWhere(TrackedEntityType model) {
            return new ConditionQueryBuilder(TrackedEntityType.class, Condition.column("id").is(model.id));
        }

        public ConditionQueryBuilder<TrackedEntityType> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(TrackedEntityType.class, Condition.column("id").is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return "CREATE TABLE IF NOT EXISTS `TrackedEntityType`(`id` TEXT, `name` TEXT, PRIMARY KEY(`id`));";
        }

        public final TrackedEntityType newInstance() {
            return new TrackedEntityType();
        }
    }

    public final class Table {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String TABLE_NAME = "TrackedEntityType";
    }

    @JsonAnySetter
    public void handleUnknown(String key, Object value) {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
