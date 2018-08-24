package org.icddrb.dhis.android.sdk.persistence.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.builder.Condition.Operation;
import com.raizlabs.android.dbflow.sql.builder.ConditionQueryBuilder;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserGroup extends BaseModel {
    private static final String CLASS_TAG = "UserGroup";
    @JsonProperty("id")
    String id;
    String pid;
    String uniqId;

    public final class Adapter extends ModelAdapter<UserGroup> {
        public Class<UserGroup> getModelClass() {
            return UserGroup.class;
        }

        public String getTableName() {
            return "UserGroup";
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `UserGroup` (`PID`, `ID`, `UNIQID`) VALUES (?, ?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, UserGroup model) {
            if (model.pid != null) {
                statement.bindString(1, model.pid);
            } else {
                statement.bindNull(1);
            }
            if (model.id != null) {
                statement.bindString(2, model.id);
            } else {
                statement.bindNull(2);
            }
            if (model.uniqId != null) {
                statement.bindString(3, model.uniqId);
            } else {
                statement.bindNull(3);
            }
        }

        public void bindToContentValues(ContentValues contentValues, UserGroup model) {
            if (model.pid != null) {
                contentValues.put("pid", model.pid);
            } else {
                contentValues.putNull("pid");
            }
            if (model.id != null) {
                contentValues.put("id", model.id);
            } else {
                contentValues.putNull("id");
            }
            if (model.uniqId != null) {
                contentValues.put("uniqid", model.uniqId);
            } else {
                contentValues.putNull("uniqid");
            }
        }

        public void bindToInsertValues(ContentValues contentValues, UserGroup model) {
            if (model.pid != null) {
                contentValues.put("pid", model.pid);
            } else {
                contentValues.putNull("pid");
            }
            if (model.id != null) {
                contentValues.put("id", model.id);
            } else {
                contentValues.putNull("id");
            }
            if (model.uniqId != null) {
                contentValues.put("uniqid", model.uniqId);
            } else {
                contentValues.putNull("uniqid");
            }
        }

        public boolean exists(UserGroup model) {
            return new Select().from(UserGroup.class).where(getPrimaryModelWhere(model)).hasData();
        }

        public void loadFromCursor(Cursor cursor, UserGroup model) {
            int indexpid = cursor.getColumnIndex("pid");
            if (indexpid != -1) {
                if (cursor.isNull(indexpid)) {
                    model.pid = null;
                } else {
                    model.pid = cursor.getString(indexpid);
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
            int indexuniqid = cursor.getColumnIndex("uniqid");
            if (indexuniqid == -1) {
                return;
            }
            if (cursor.isNull(indexuniqid)) {
                model.uniqId = null;
            } else {
                model.uniqId = cursor.getString(indexuniqid);
            }
        }

        public boolean hasCachingId() {
            return true;
        }

        public Object getCachingId(UserGroup model) {
            return model.pid;
        }

        public String getCachingColumnName() {
            return "pid";
        }

        public Object getCachingIdFromCursorIndex(Cursor cursor, int indexpid) {
            return cursor.getString(indexpid);
        }

        public ConditionQueryBuilder<UserGroup> getPrimaryModelWhere(UserGroup model) {
            return new ConditionQueryBuilder(UserGroup.class, Condition.column("pid").is(model.pid));
        }

        public ConditionQueryBuilder<UserGroup> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(UserGroup.class, Condition.column("pid").is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return "CREATE TABLE IF NOT EXISTS `UserGroup`(`pid` TEXT, `id` TEXT, `uniqid` TEXT, PRIMARY KEY(`pid`));";
        }

        public final UserGroup newInstance() {
            return new UserGroup();
        }
    }

    public final class Table {
        public static final String ID = "id";
        public static final String PID = "pid";
        public static final String TABLE_NAME = "UserGroup";
        public static final String UNIQID = "uniqid";
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
