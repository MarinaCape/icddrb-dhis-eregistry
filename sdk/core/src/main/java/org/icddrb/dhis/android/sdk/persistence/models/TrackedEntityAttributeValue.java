package org.icddrb.dhis.android.sdk.persistence.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.builder.Condition.Operation;
import com.raizlabs.android.dbflow.sql.builder.ConditionQueryBuilder;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.Update;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import java.io.Serializable;
import org.icddrb.dhis.android.sdk.controllers.tracker.TrackerController;
import org.icddrb.dhis.android.sdk.utils.Utils;

public class TrackedEntityAttributeValue extends BaseValue implements Serializable {
    private static final String CLASS_TAG = TrackedEntityAttributeValue.class.getSimpleName();
    @JsonIgnore
    long localTrackedEntityInstanceId;
    @JsonProperty("attribute")
    String trackedEntityAttributeId;
    @JsonIgnore
    String trackedEntityInstanceId;

    public final class Adapter extends ModelAdapter<TrackedEntityAttributeValue> {
        public Class<TrackedEntityAttributeValue> getModelClass() {
            return TrackedEntityAttributeValue.class;
        }

        public String getTableName() {
            return Table.TABLE_NAME;
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `TrackedEntityAttributeValue` (`VALUE`, `TRACKEDENTITYATTRIBUTEID`, `TRACKEDENTITYINSTANCEID`, `LOCALTRACKEDENTITYINSTANCEID`) VALUES (?, ?, ?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, TrackedEntityAttributeValue model) {
            if (model.value != null) {
                statement.bindString(1, model.value);
            } else {
                statement.bindNull(1);
            }
            if (model.trackedEntityAttributeId != null) {
                statement.bindString(2, model.trackedEntityAttributeId);
            } else {
                statement.bindNull(2);
            }
            if (model.trackedEntityInstanceId != null) {
                statement.bindString(3, model.trackedEntityInstanceId);
            } else {
                statement.bindNull(3);
            }
            statement.bindLong(4, model.localTrackedEntityInstanceId);
        }

        public void bindToContentValues(ContentValues contentValues, TrackedEntityAttributeValue model) {
            if (model.value != null) {
                contentValues.put("value", model.value);
            } else {
                contentValues.putNull("value");
            }
            if (model.trackedEntityAttributeId != null) {
                contentValues.put(Table.TRACKEDENTITYATTRIBUTEID, model.trackedEntityAttributeId);
            } else {
                contentValues.putNull(Table.TRACKEDENTITYATTRIBUTEID);
            }
            if (model.trackedEntityInstanceId != null) {
                contentValues.put(Table.TRACKEDENTITYINSTANCEID, model.trackedEntityInstanceId);
            } else {
                contentValues.putNull(Table.TRACKEDENTITYINSTANCEID);
            }
            contentValues.put("localTrackedEntityInstanceId", Long.valueOf(model.localTrackedEntityInstanceId));
        }

        public void bindToInsertValues(ContentValues contentValues, TrackedEntityAttributeValue model) {
            if (model.value != null) {
                contentValues.put("value", model.value);
            } else {
                contentValues.putNull("value");
            }
            if (model.trackedEntityAttributeId != null) {
                contentValues.put(Table.TRACKEDENTITYATTRIBUTEID, model.trackedEntityAttributeId);
            } else {
                contentValues.putNull(Table.TRACKEDENTITYATTRIBUTEID);
            }
            if (model.trackedEntityInstanceId != null) {
                contentValues.put(Table.TRACKEDENTITYINSTANCEID, model.trackedEntityInstanceId);
            } else {
                contentValues.putNull(Table.TRACKEDENTITYINSTANCEID);
            }
            contentValues.put("localTrackedEntityInstanceId", Long.valueOf(model.localTrackedEntityInstanceId));
        }

        public boolean exists(TrackedEntityAttributeValue model) {
            return new Select().from(TrackedEntityAttributeValue.class).where(getPrimaryModelWhere(model)).hasData();
        }

        public void loadFromCursor(Cursor cursor, TrackedEntityAttributeValue model) {
            int indexvalue = cursor.getColumnIndex("value");
            if (indexvalue != -1) {
                if (cursor.isNull(indexvalue)) {
                    model.value = null;
                } else {
                    model.value = cursor.getString(indexvalue);
                }
            }
            int indextrackedEntityAttributeId = cursor.getColumnIndex(Table.TRACKEDENTITYATTRIBUTEID);
            if (indextrackedEntityAttributeId != -1) {
                if (cursor.isNull(indextrackedEntityAttributeId)) {
                    model.trackedEntityAttributeId = null;
                } else {
                    model.trackedEntityAttributeId = cursor.getString(indextrackedEntityAttributeId);
                }
            }
            int indextrackedEntityInstanceId = cursor.getColumnIndex(Table.TRACKEDENTITYINSTANCEID);
            if (indextrackedEntityInstanceId != -1) {
                if (cursor.isNull(indextrackedEntityInstanceId)) {
                    model.trackedEntityInstanceId = null;
                } else {
                    model.trackedEntityInstanceId = cursor.getString(indextrackedEntityInstanceId);
                }
            }
            int indexlocalTrackedEntityInstanceId = cursor.getColumnIndex("localTrackedEntityInstanceId");
            if (indexlocalTrackedEntityInstanceId != -1) {
                model.localTrackedEntityInstanceId = cursor.getLong(indexlocalTrackedEntityInstanceId);
            }
        }

        public ConditionQueryBuilder<TrackedEntityAttributeValue> getPrimaryModelWhere(TrackedEntityAttributeValue model) {
            return new ConditionQueryBuilder(TrackedEntityAttributeValue.class, Condition.column(Table.TRACKEDENTITYATTRIBUTEID).is(model.trackedEntityAttributeId), Condition.column(Table.TRACKEDENTITYINSTANCEID).is(model.trackedEntityInstanceId));
        }

        public ConditionQueryBuilder<TrackedEntityAttributeValue> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(TrackedEntityAttributeValue.class, Condition.column(Table.TRACKEDENTITYATTRIBUTEID).is(Operation.EMPTY_PARAM), Condition.column(Table.TRACKEDENTITYINSTANCEID).is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return "CREATE TABLE IF NOT EXISTS `TrackedEntityAttributeValue`(`value` TEXT, `trackedEntityAttributeId` TEXT, `trackedEntityInstanceId` TEXT, `localTrackedEntityInstanceId` INTEGER, PRIMARY KEY(`trackedEntityAttributeId`, `trackedEntityInstanceId`));";
        }

        public final TrackedEntityAttributeValue newInstance() {
            return new TrackedEntityAttributeValue();
        }
    }

    public final class Table {
        public static final String LOCALTRACKEDENTITYINSTANCEID = "localTrackedEntityInstanceId";
        public static final String TABLE_NAME = "TrackedEntityAttributeValue";
        public static final String TRACKEDENTITYATTRIBUTEID = "trackedEntityAttributeId";
        public static final String TRACKEDENTITYINSTANCEID = "trackedEntityInstanceId";
        public static final String VALUE = "value";
    }

    public TrackedEntityAttributeValue(TrackedEntityAttributeValue trackedEntityAttributeValue) {
        super(trackedEntityAttributeValue);
        this.trackedEntityAttributeId = trackedEntityAttributeValue.getTrackedEntityAttributeId();
        this.trackedEntityInstanceId = trackedEntityAttributeValue.getTrackedEntityInstanceId();
        this.localTrackedEntityInstanceId = trackedEntityAttributeValue.getLocalTrackedEntityInstanceId();
    }

    public void save() {
        if (!Utils.isLocal(this.trackedEntityInstanceId) || TrackerController.getTrackedEntityAttributeValue(this.trackedEntityAttributeId, this.localTrackedEntityInstanceId) == null) {
            super.save();
        } else {
            updateManually();
        }
    }

    public void updateManually() {
        new Update(TrackedEntityAttributeValue.class).set(Condition.column("value").is(this.value)).where(Condition.column("localTrackedEntityInstanceId").is(Long.valueOf(this.localTrackedEntityInstanceId)), Condition.column(Table.TRACKEDENTITYATTRIBUTEID).is(this.trackedEntityAttributeId)).queryClose();
    }

    public void update() {
        save();
    }

    public String getTrackedEntityAttributeId() {
        return this.trackedEntityAttributeId;
    }

    public void setTrackedEntityAttributeId(String trackedEntityAttributeId) {
        this.trackedEntityAttributeId = trackedEntityAttributeId;
    }

    public String getTrackedEntityInstanceId() {
        return this.trackedEntityInstanceId;
    }

    public void setTrackedEntityInstanceId(String trackedEntityInstanceId) {
        this.trackedEntityInstanceId = trackedEntityInstanceId;
    }

    public long getLocalTrackedEntityInstanceId() {
        return this.localTrackedEntityInstanceId;
    }

    public void setLocalTrackedEntityInstanceId(long localTrackedEntityInstanceId) {
        this.localTrackedEntityInstanceId = localTrackedEntityInstanceId;
    }
}
