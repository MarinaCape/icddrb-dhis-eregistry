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
public class UserGroupAccess extends BaseModel {
    private static final String CLASS_TAG = "UserGroupAccess";
    @JsonProperty("displayName")
    String displayName;
    @JsonProperty("id")
    String id;
    String pid;
    String uniqId;
    @JsonProperty("userGroupUid")
    String userGroupUid;

    public final class Adapter extends ModelAdapter<UserGroupAccess> {
        public Class<UserGroupAccess> getModelClass() {
            return UserGroupAccess.class;
        }

        public String getTableName() {
            return "UserGroupAccess";
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `UserGroupAccess` (`PID`, `ID`, `USERGROUPUID`, `DISPLAYNAME`, `UNIQID`) VALUES (?, ?, ?, ?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, UserGroupAccess model) {
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
            if (model.userGroupUid != null) {
                statement.bindString(3, model.userGroupUid);
            } else {
                statement.bindNull(3);
            }
            if (model.displayName != null) {
                statement.bindString(4, model.displayName);
            } else {
                statement.bindNull(4);
            }
            if (model.uniqId != null) {
                statement.bindString(5, model.uniqId);
            } else {
                statement.bindNull(5);
            }
        }

        public void bindToContentValues(ContentValues contentValues, UserGroupAccess model) {
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
            if (model.userGroupUid != null) {
                contentValues.put(Table.USERGROUPUID, model.userGroupUid);
            } else {
                contentValues.putNull(Table.USERGROUPUID);
            }
            if (model.displayName != null) {
                contentValues.put("displayName", model.displayName);
            } else {
                contentValues.putNull("displayName");
            }
            if (model.uniqId != null) {
                contentValues.put("uniqid", model.uniqId);
            } else {
                contentValues.putNull("uniqid");
            }
        }

        public void bindToInsertValues(ContentValues contentValues, UserGroupAccess model) {
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
            if (model.userGroupUid != null) {
                contentValues.put(Table.USERGROUPUID, model.userGroupUid);
            } else {
                contentValues.putNull(Table.USERGROUPUID);
            }
            if (model.displayName != null) {
                contentValues.put("displayName", model.displayName);
            } else {
                contentValues.putNull("displayName");
            }
            if (model.uniqId != null) {
                contentValues.put("uniqid", model.uniqId);
            } else {
                contentValues.putNull("uniqid");
            }
        }

        public boolean exists(UserGroupAccess model) {
            return new Select().from(UserGroupAccess.class).where(getPrimaryModelWhere(model)).hasData();
        }

        public void loadFromCursor(Cursor cursor, UserGroupAccess model) {
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
            int indexuserGroupUid = cursor.getColumnIndex(Table.USERGROUPUID);
            if (indexuserGroupUid != -1) {
                if (cursor.isNull(indexuserGroupUid)) {
                    model.userGroupUid = null;
                } else {
                    model.userGroupUid = cursor.getString(indexuserGroupUid);
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

        public Object getCachingId(UserGroupAccess model) {
            return model.pid;
        }

        public String getCachingColumnName() {
            return "pid";
        }

        public Object getCachingIdFromCursorIndex(Cursor cursor, int indexpid) {
            return cursor.getString(indexpid);
        }

        public ConditionQueryBuilder<UserGroupAccess> getPrimaryModelWhere(UserGroupAccess model) {
            return new ConditionQueryBuilder(UserGroupAccess.class, Condition.column("pid").is(model.pid));
        }

        public ConditionQueryBuilder<UserGroupAccess> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(UserGroupAccess.class, Condition.column("pid").is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return "CREATE TABLE IF NOT EXISTS `UserGroupAccess`(`pid` TEXT, `id` TEXT, `userGroupUid` TEXT, `displayName` TEXT, `uniqid` TEXT, PRIMARY KEY(`pid`));";
        }

        public final UserGroupAccess newInstance() {
            return new UserGroupAccess();
        }
    }

    public final class Table {
        public static final String DISPLAYNAME = "displayName";
        public static final String ID = "id";
        public static final String PID = "pid";
        public static final String TABLE_NAME = "UserGroupAccess";
        public static final String UNIQID = "uniqid";
        public static final String USERGROUPUID = "userGroupUid";
    }

    public String toString() {
        return "uniqID: " + this.uniqId + "; id: " + this.id + "; displayName: " + this.displayName;
    }

    public String getId() {
        return this.id;
    }
}
