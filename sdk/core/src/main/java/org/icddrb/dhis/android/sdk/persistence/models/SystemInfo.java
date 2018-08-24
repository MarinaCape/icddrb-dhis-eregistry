package org.icddrb.dhis.android.sdk.persistence.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.builder.Condition.Operation;
import com.raizlabs.android.dbflow.sql.builder.ConditionQueryBuilder;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import org.joda.time.DateTime;

public class SystemInfo extends BaseModel {
    int id = 1;
    @JsonProperty("serverDate")
    DateTime serverDate;
    @JsonProperty("version")
    String version;

    public final class Adapter extends ModelAdapter<SystemInfo> {
        public Class<SystemInfo> getModelClass() {
            return SystemInfo.class;
        }

        public String getTableName() {
            return Table.TABLE_NAME;
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `SystemInfo` (`ID`, `SERVERDATE`, `VERSION`) VALUES (?, ?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, SystemInfo model) {
            statement.bindLong(1, (long) model.id);
            Object modelserverDate = FlowManager.getTypeConverterForClass(DateTime.class).getDBValue(model.serverDate);
            if (modelserverDate != null) {
                statement.bindString(2, (String) modelserverDate);
            } else {
                statement.bindNull(2);
            }
            if (model.version != null) {
                statement.bindString(3, model.version);
            } else {
                statement.bindNull(3);
            }
        }

        public void bindToContentValues(ContentValues contentValues, SystemInfo model) {
            contentValues.put("id", Integer.valueOf(model.id));
            Object modelserverDate = FlowManager.getTypeConverterForClass(DateTime.class).getDBValue(model.serverDate);
            if (modelserverDate != null) {
                contentValues.put(Table.SERVERDATE, (String) modelserverDate);
            } else {
                contentValues.putNull(Table.SERVERDATE);
            }
            if (model.version != null) {
                contentValues.put("version", model.version);
            } else {
                contentValues.putNull("version");
            }
        }

        public void bindToInsertValues(ContentValues contentValues, SystemInfo model) {
            contentValues.put("id", Integer.valueOf(model.id));
            Object modelserverDate = FlowManager.getTypeConverterForClass(DateTime.class).getDBValue(model.serverDate);
            if (modelserverDate != null) {
                contentValues.put(Table.SERVERDATE, (String) modelserverDate);
            } else {
                contentValues.putNull(Table.SERVERDATE);
            }
            if (model.version != null) {
                contentValues.put("version", model.version);
            } else {
                contentValues.putNull("version");
            }
        }

        public boolean exists(SystemInfo model) {
            return new Select().from(SystemInfo.class).where(getPrimaryModelWhere(model)).hasData();
        }

        public void loadFromCursor(Cursor cursor, SystemInfo model) {
            int indexid = cursor.getColumnIndex("id");
            if (indexid != -1) {
                model.id = cursor.getInt(indexid);
            }
            int indexserverDate = cursor.getColumnIndex(Table.SERVERDATE);
            if (indexserverDate != -1) {
                if (cursor.isNull(indexserverDate)) {
                    model.serverDate = null;
                } else {
                    model.serverDate = (DateTime) FlowManager.getTypeConverterForClass(DateTime.class).getModelValue(cursor.getString(indexserverDate));
                }
            }
            int indexversion = cursor.getColumnIndex("version");
            if (indexversion == -1) {
                return;
            }
            if (cursor.isNull(indexversion)) {
                model.version = null;
            } else {
                model.version = cursor.getString(indexversion);
            }
        }

        public boolean hasCachingId() {
            return true;
        }

        public Object getCachingId(SystemInfo model) {
            return Integer.valueOf(model.id);
        }

        public String getCachingColumnName() {
            return "id";
        }

        public Object getCachingIdFromCursorIndex(Cursor cursor, int indexid) {
            return Integer.valueOf(cursor.getInt(indexid));
        }

        public ConditionQueryBuilder<SystemInfo> getPrimaryModelWhere(SystemInfo model) {
            return new ConditionQueryBuilder(SystemInfo.class, Condition.column("id").is(Integer.valueOf(model.id)));
        }

        public ConditionQueryBuilder<SystemInfo> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(SystemInfo.class, Condition.column("id").is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return "CREATE TABLE IF NOT EXISTS `SystemInfo`(`id` INTEGER, `serverDate` TEXT, `version` TEXT, PRIMARY KEY(`id`));";
        }

        public final SystemInfo newInstance() {
            return new SystemInfo();
        }
    }

    public final class Table {
        public static final String ID = "id";
        public static final String SERVERDATE = "serverDate";
        public static final String TABLE_NAME = "SystemInfo";
        public static final String VERSION = "version";
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

    public DateTime getServerDate() {
        return this.serverDate;
    }

    public void setServerDate(DateTime serverDate) {
        this.serverDate = serverDate;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
