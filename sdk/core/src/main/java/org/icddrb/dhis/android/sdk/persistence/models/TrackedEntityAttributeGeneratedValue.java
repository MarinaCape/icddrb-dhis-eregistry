package org.icddrb.dhis.android.sdk.persistence.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.builder.Condition.Operation;
import com.raizlabs.android.dbflow.sql.builder.ConditionQueryBuilder;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

public class TrackedEntityAttributeGeneratedValue extends BaseValue {
    @JsonProperty("created")
    String created;
    @JsonProperty("expiryDate")
    String expiryDate;
    @JsonIgnore
    int id;
    @JsonProperty("ownerUid")
    TrackedEntityAttribute trackedEntityAttribute;

    public final class Adapter extends ModelAdapter<TrackedEntityAttributeGeneratedValue> {
        public Class<TrackedEntityAttributeGeneratedValue> getModelClass() {
            return TrackedEntityAttributeGeneratedValue.class;
        }

        public String getTableName() {
            return Table.TABLE_NAME;
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `TrackedEntityAttributeGeneratedValue` (`VALUE`, `CREATED`, `EXPIRYDATE`, `trackedEntityAttribute`) VALUES (?, ?, ?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, TrackedEntityAttributeGeneratedValue model) {
            if (model.value != null) {
                statement.bindString(1, model.value);
            } else {
                statement.bindNull(1);
            }
            if (model.created != null) {
                statement.bindString(2, model.created);
            } else {
                statement.bindNull(2);
            }
            if (model.expiryDate != null) {
                statement.bindString(3, model.expiryDate);
            } else {
                statement.bindNull(3);
            }
            if (model.trackedEntityAttribute == null) {
                statement.bindNull(4);
            } else if (model.trackedEntityAttribute.id != null) {
                statement.bindString(4, model.trackedEntityAttribute.id);
            } else {
                statement.bindNull(4);
            }
        }

        public void bindToContentValues(ContentValues contentValues, TrackedEntityAttributeGeneratedValue model) {
            if (model.value != null) {
                contentValues.put("value", model.value);
            } else {
                contentValues.putNull("value");
            }
            contentValues.put("id", Integer.valueOf(model.id));
            if (model.created != null) {
                contentValues.put("created", model.created);
            } else {
                contentValues.putNull("created");
            }
            if (model.expiryDate != null) {
                contentValues.put(Table.EXPIRYDATE, model.expiryDate);
            } else {
                contentValues.putNull(Table.EXPIRYDATE);
            }
            if (model.trackedEntityAttribute == null) {
                contentValues.putNull("trackedEntityAttribute");
            } else if (model.trackedEntityAttribute.id != null) {
                contentValues.put("trackedEntityAttribute", model.trackedEntityAttribute.id);
            } else {
                contentValues.putNull("trackedEntityAttribute");
            }
        }

        public void bindToInsertValues(ContentValues contentValues, TrackedEntityAttributeGeneratedValue model) {
            if (model.value != null) {
                contentValues.put("value", model.value);
            } else {
                contentValues.putNull("value");
            }
            if (model.created != null) {
                contentValues.put("created", model.created);
            } else {
                contentValues.putNull("created");
            }
            if (model.expiryDate != null) {
                contentValues.put(Table.EXPIRYDATE, model.expiryDate);
            } else {
                contentValues.putNull(Table.EXPIRYDATE);
            }
            if (model.trackedEntityAttribute == null) {
                contentValues.putNull("trackedEntityAttribute");
            } else if (model.trackedEntityAttribute.id != null) {
                contentValues.put("trackedEntityAttribute", model.trackedEntityAttribute.id);
            } else {
                contentValues.putNull("trackedEntityAttribute");
            }
        }

        public boolean exists(TrackedEntityAttributeGeneratedValue model) {
            return model.id > 0;
        }

        public void loadFromCursor(Cursor cursor, TrackedEntityAttributeGeneratedValue model) {
            int indexvalue = cursor.getColumnIndex("value");
            if (indexvalue != -1) {
                if (cursor.isNull(indexvalue)) {
                    model.value = null;
                } else {
                    model.value = cursor.getString(indexvalue);
                }
            }
            int indexid = cursor.getColumnIndex("id");
            if (indexid != -1) {
                model.id = cursor.getInt(indexid);
            }
            int indexcreated = cursor.getColumnIndex("created");
            if (indexcreated != -1) {
                if (cursor.isNull(indexcreated)) {
                    model.created = null;
                } else {
                    model.created = cursor.getString(indexcreated);
                }
            }
            int indexexpiryDate = cursor.getColumnIndex(Table.EXPIRYDATE);
            if (indexexpiryDate != -1) {
                if (cursor.isNull(indexexpiryDate)) {
                    model.expiryDate = null;
                } else {
                    model.expiryDate = cursor.getString(indexexpiryDate);
                }
            }
            int indextrackedEntityAttribute = cursor.getColumnIndex("trackedEntityAttribute");
            if (indextrackedEntityAttribute != -1 && !cursor.isNull(indextrackedEntityAttribute)) {
                model.trackedEntityAttribute = (TrackedEntityAttribute) new Select().from(TrackedEntityAttribute.class).where().and(Condition.column("id").is(cursor.getString(indextrackedEntityAttribute))).querySingle();
            }
        }

        public void updateAutoIncrement(TrackedEntityAttributeGeneratedValue model, long id) {
            model.id = (int) id;
        }

        public long getAutoIncrementingId(TrackedEntityAttributeGeneratedValue model) {
            return (long) model.id;
        }

        public String getAutoIncrementingColumnName() {
            return "id";
        }

        public ConditionQueryBuilder<TrackedEntityAttributeGeneratedValue> getPrimaryModelWhere(TrackedEntityAttributeGeneratedValue model) {
            return new ConditionQueryBuilder(TrackedEntityAttributeGeneratedValue.class, Condition.column("id").is(Integer.valueOf(model.id)));
        }

        public ConditionQueryBuilder<TrackedEntityAttributeGeneratedValue> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(TrackedEntityAttributeGeneratedValue.class, Condition.column("id").is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return String.format("CREATE TABLE IF NOT EXISTS `TrackedEntityAttributeGeneratedValue`(`value` TEXT, `id` INTEGER PRIMARY KEY AUTOINCREMENT, `created` TEXT, `expiryDate` TEXT,  `trackedEntityAttribute` TEXT, FOREIGN KEY(`trackedEntityAttribute`) REFERENCES `%1s` (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION );", new Object[]{FlowManager.getTableName(TrackedEntityAttribute.class)});
        }

        public final TrackedEntityAttributeGeneratedValue newInstance() {
            return new TrackedEntityAttributeGeneratedValue();
        }
    }

    public final class Table {
        public static final String CREATED = "created";
        public static final String EXPIRYDATE = "expiryDate";
        public static final String ID = "id";
        public static final String TABLE_NAME = "TrackedEntityAttributeGeneratedValue";
        public static final String TRACKEDENTITYATTRIBUTE_TRACKEDENTITYATTRIBUTE = "trackedEntityAttribute";
        public static final String VALUE = "value";
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExpiryDate() {
        return this.expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public TrackedEntityAttribute getTrackedEntityAttribute() {
        return this.trackedEntityAttribute;
    }

    public void setTrackedEntityAttribute(TrackedEntityAttribute trackedEntityAttribute) {
        this.trackedEntityAttribute = trackedEntityAttribute;
    }
}
