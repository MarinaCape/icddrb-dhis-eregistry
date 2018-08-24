package org.icddrb.dhis.android.sdk.persistence.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.builder.Condition.Operation;
import com.raizlabs.android.dbflow.sql.builder.ConditionQueryBuilder;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import java.io.Serializable;

@JsonIgnoreProperties({"modelAdapter"})
public class Relationship extends BaseModel implements Serializable {
    @JsonProperty
    String displayName;
    @JsonProperty
    String relationship;
    @JsonProperty
    String trackedEntityInstanceA;
    @JsonProperty
    String trackedEntityInstanceB;

    public final class Adapter extends ModelAdapter<Relationship> {
        public Class<Relationship> getModelClass() {
            return Relationship.class;
        }

        public String getTableName() {
            return Table.TABLE_NAME;
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `Relationship` (`RELATIONSHIP`, `TRACKEDENTITYINSTANCEA`, `TRACKEDENTITYINSTANCEB`, `DISPLAYNAME`) VALUES (?, ?, ?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, Relationship model) {
            if (model.relationship != null) {
                statement.bindString(1, model.relationship);
            } else {
                statement.bindNull(1);
            }
            if (model.trackedEntityInstanceA != null) {
                statement.bindString(2, model.trackedEntityInstanceA);
            } else {
                statement.bindNull(2);
            }
            if (model.trackedEntityInstanceB != null) {
                statement.bindString(3, model.trackedEntityInstanceB);
            } else {
                statement.bindNull(3);
            }
            if (model.displayName != null) {
                statement.bindString(4, model.displayName);
            } else {
                statement.bindNull(4);
            }
        }

        public void bindToContentValues(ContentValues contentValues, Relationship model) {
            if (model.relationship != null) {
                contentValues.put(Table.RELATIONSHIP, model.relationship);
            } else {
                contentValues.putNull(Table.RELATIONSHIP);
            }
            if (model.trackedEntityInstanceA != null) {
                contentValues.put(Table.TRACKEDENTITYINSTANCEA, model.trackedEntityInstanceA);
            } else {
                contentValues.putNull(Table.TRACKEDENTITYINSTANCEA);
            }
            if (model.trackedEntityInstanceB != null) {
                contentValues.put(Table.TRACKEDENTITYINSTANCEB, model.trackedEntityInstanceB);
            } else {
                contentValues.putNull(Table.TRACKEDENTITYINSTANCEB);
            }
            if (model.displayName != null) {
                contentValues.put("displayName", model.displayName);
            } else {
                contentValues.putNull("displayName");
            }
        }

        public void bindToInsertValues(ContentValues contentValues, Relationship model) {
            if (model.relationship != null) {
                contentValues.put(Table.RELATIONSHIP, model.relationship);
            } else {
                contentValues.putNull(Table.RELATIONSHIP);
            }
            if (model.trackedEntityInstanceA != null) {
                contentValues.put(Table.TRACKEDENTITYINSTANCEA, model.trackedEntityInstanceA);
            } else {
                contentValues.putNull(Table.TRACKEDENTITYINSTANCEA);
            }
            if (model.trackedEntityInstanceB != null) {
                contentValues.put(Table.TRACKEDENTITYINSTANCEB, model.trackedEntityInstanceB);
            } else {
                contentValues.putNull(Table.TRACKEDENTITYINSTANCEB);
            }
            if (model.displayName != null) {
                contentValues.put("displayName", model.displayName);
            } else {
                contentValues.putNull("displayName");
            }
        }

        public boolean exists(Relationship model) {
            return new Select().from(Relationship.class).where(getPrimaryModelWhere(model)).hasData();
        }

        public void loadFromCursor(Cursor cursor, Relationship model) {
            int indexrelationship = cursor.getColumnIndex(Table.RELATIONSHIP);
            if (indexrelationship != -1) {
                if (cursor.isNull(indexrelationship)) {
                    model.relationship = null;
                } else {
                    model.relationship = cursor.getString(indexrelationship);
                }
            }
            int indextrackedEntityInstanceA = cursor.getColumnIndex(Table.TRACKEDENTITYINSTANCEA);
            if (indextrackedEntityInstanceA != -1) {
                if (cursor.isNull(indextrackedEntityInstanceA)) {
                    model.trackedEntityInstanceA = null;
                } else {
                    model.trackedEntityInstanceA = cursor.getString(indextrackedEntityInstanceA);
                }
            }
            int indextrackedEntityInstanceB = cursor.getColumnIndex(Table.TRACKEDENTITYINSTANCEB);
            if (indextrackedEntityInstanceB != -1) {
                if (cursor.isNull(indextrackedEntityInstanceB)) {
                    model.trackedEntityInstanceB = null;
                } else {
                    model.trackedEntityInstanceB = cursor.getString(indextrackedEntityInstanceB);
                }
            }
            int indexdisplayName = cursor.getColumnIndex("displayName");
            if (indexdisplayName == -1) {
                return;
            }
            if (cursor.isNull(indexdisplayName)) {
                model.displayName = null;
            } else {
                model.displayName = cursor.getString(indexdisplayName);
            }
        }

        public ConditionQueryBuilder<Relationship> getPrimaryModelWhere(Relationship model) {
            return new ConditionQueryBuilder(Relationship.class, Condition.column(Table.RELATIONSHIP).is(model.relationship), Condition.column(Table.TRACKEDENTITYINSTANCEA).is(model.trackedEntityInstanceA), Condition.column(Table.TRACKEDENTITYINSTANCEB).is(model.trackedEntityInstanceB));
        }

        public ConditionQueryBuilder<Relationship> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(Relationship.class, Condition.column(Table.RELATIONSHIP).is(Operation.EMPTY_PARAM), Condition.column(Table.TRACKEDENTITYINSTANCEA).is(Operation.EMPTY_PARAM), Condition.column(Table.TRACKEDENTITYINSTANCEB).is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return "CREATE TABLE IF NOT EXISTS `Relationship`(`relationship` TEXT, `trackedEntityInstanceA` TEXT, `trackedEntityInstanceB` TEXT, `displayName` TEXT, PRIMARY KEY(`relationship`, `trackedEntityInstanceA`, `trackedEntityInstanceB`));";
        }

        public final Relationship newInstance() {
            return new Relationship();
        }
    }

    public final class Table {
        public static final String DISPLAYNAME = "displayName";
        public static final String RELATIONSHIP = "relationship";
        public static final String TABLE_NAME = "Relationship";
        public static final String TRACKEDENTITYINSTANCEA = "trackedEntityInstanceA";
        public static final String TRACKEDENTITYINSTANCEB = "trackedEntityInstanceB";
    }

    @JsonAnySetter
    public void handleUnknown(String key, Object value) {
    }

    public String getRelationship() {
        return this.relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getTrackedEntityInstanceA() {
        return this.trackedEntityInstanceA;
    }

    public void setTrackedEntityInstanceA(String trackedEntityInstanceA) {
        this.trackedEntityInstanceA = trackedEntityInstanceA;
    }

    public String getTrackedEntityInstanceB() {
        return this.trackedEntityInstanceB;
    }

    public void setTrackedEntityInstanceB(String trackedEntityInstanceB) {
        this.trackedEntityInstanceB = trackedEntityInstanceB;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
