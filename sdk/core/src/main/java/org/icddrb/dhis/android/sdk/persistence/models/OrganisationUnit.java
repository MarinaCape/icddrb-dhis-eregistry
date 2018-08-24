package org.icddrb.dhis.android.sdk.persistence.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.builder.Condition.Operation;
import com.raizlabs.android.dbflow.sql.builder.ConditionQueryBuilder;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import java.util.List;
import java.util.Map;

public class OrganisationUnit extends BaseModel {
    @JsonProperty("id")
    String id;
    @JsonProperty("displayName")
    String label;
    @JsonProperty("level")
    int level;
    String parent;
    @JsonProperty("programs")
    List<Program> programs;
    @JsonIgnore
    TYPE type;

    public final class Adapter extends ModelAdapter<OrganisationUnit> {
        public Class<OrganisationUnit> getModelClass() {
            return OrganisationUnit.class;
        }

        public String getTableName() {
            return Table.TABLE_NAME;
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `OrganisationUnit` (`ID`, `DISPLAYNAME`, `LEVEL`, `PARENT`, `TYPE`) VALUES (?, ?, ?, ?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, OrganisationUnit model) {
            if (model.id != null) {
                statement.bindString(1, model.id);
            } else {
                statement.bindNull(1);
            }
            if (model.label != null) {
                statement.bindString(2, model.label);
            } else {
                statement.bindNull(2);
            }
            statement.bindLong(3, (long) model.level);
            if (model.parent != null) {
                statement.bindString(4, model.parent);
            } else {
                statement.bindNull(4);
            }
            TYPE modeltype = model.type;
            if (modeltype != null) {
                statement.bindString(5, modeltype.name());
            } else {
                statement.bindNull(5);
            }
        }

        public void bindToContentValues(ContentValues contentValues, OrganisationUnit model) {
            if (model.id != null) {
                contentValues.put("id", model.id);
            } else {
                contentValues.putNull("id");
            }
            if (model.label != null) {
                contentValues.put("displayName", model.label);
            } else {
                contentValues.putNull("displayName");
            }
            contentValues.put(Table.LEVEL, Integer.valueOf(model.level));
            if (model.parent != null) {
                contentValues.put(Table.PARENT, model.parent);
            } else {
                contentValues.putNull(Table.PARENT);
            }
            TYPE modeltype = model.type;
            if (modeltype != null) {
                contentValues.put("type", modeltype.name());
            } else {
                contentValues.putNull("type");
            }
        }

        public void bindToInsertValues(ContentValues contentValues, OrganisationUnit model) {
            if (model.id != null) {
                contentValues.put("id", model.id);
            } else {
                contentValues.putNull("id");
            }
            if (model.label != null) {
                contentValues.put("displayName", model.label);
            } else {
                contentValues.putNull("displayName");
            }
            contentValues.put(Table.LEVEL, Integer.valueOf(model.level));
            if (model.parent != null) {
                contentValues.put(Table.PARENT, model.parent);
            } else {
                contentValues.putNull(Table.PARENT);
            }
            TYPE modeltype = model.type;
            if (modeltype != null) {
                contentValues.put("type", modeltype.name());
            } else {
                contentValues.putNull("type");
            }
        }

        public boolean exists(OrganisationUnit model) {
            return new Select().from(OrganisationUnit.class).where(getPrimaryModelWhere(model)).hasData();
        }

        public void loadFromCursor(Cursor cursor, OrganisationUnit model) {
            int indexid = cursor.getColumnIndex("id");
            if (indexid != -1) {
                if (cursor.isNull(indexid)) {
                    model.id = null;
                } else {
                    model.id = cursor.getString(indexid);
                }
            }
            int indexdisplayName = cursor.getColumnIndex("displayName");
            if (indexdisplayName != -1) {
                if (cursor.isNull(indexdisplayName)) {
                    model.label = null;
                } else {
                    model.label = cursor.getString(indexdisplayName);
                }
            }
            int indexlevel = cursor.getColumnIndex(Table.LEVEL);
            if (indexlevel != -1) {
                model.level = cursor.getInt(indexlevel);
            }
            int indexparent = cursor.getColumnIndex(Table.PARENT);
            if (indexparent != -1) {
                if (cursor.isNull(indexparent)) {
                    model.parent = null;
                } else {
                    model.parent = cursor.getString(indexparent);
                }
            }
            int indextype = cursor.getColumnIndex("type");
            if (indextype == -1) {
                return;
            }
            if (cursor.isNull(indextype)) {
                model.type = null;
            } else {
                model.type = TYPE.valueOf(cursor.getString(indextype));
            }
        }

        public boolean hasCachingId() {
            return true;
        }

        public Object getCachingId(OrganisationUnit model) {
            return model.id;
        }

        public String getCachingColumnName() {
            return "id";
        }

        public Object getCachingIdFromCursorIndex(Cursor cursor, int indexid) {
            return cursor.getString(indexid);
        }

        public ConditionQueryBuilder<OrganisationUnit> getPrimaryModelWhere(OrganisationUnit model) {
            return new ConditionQueryBuilder(OrganisationUnit.class, Condition.column("id").is(model.id));
        }

        public ConditionQueryBuilder<OrganisationUnit> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(OrganisationUnit.class, Condition.column("id").is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return "CREATE TABLE IF NOT EXISTS `OrganisationUnit`(`id` TEXT, `displayName` TEXT, `level` INTEGER, `parent` TEXT, `type` TEXT, PRIMARY KEY(`id`));";
        }

        public final OrganisationUnit newInstance() {
            return new OrganisationUnit();
        }
    }

    public enum TYPE {
        ASSIGNED,
        SEARCH
    }

    public final class Table {
        public static final String DISPLAYNAME = "displayName";
        public static final String ID = "id";
        public static final String LEVEL = "level";
        public static final String PARENT = "parent";
        public static final String TABLE_NAME = "OrganisationUnit";
        public static final String TYPE = "type";
    }

    @JsonProperty("parent")
    public void unpackNameFromNestedObject(Map<String, String> p) {
        this.parent = (String) p.get("id");
    }

    @JsonAnySetter
    public void handleUnknown(String key, Object value) {
    }

    public String getParent() {
        return this.parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<Program> getPrograms() {
        return this.programs;
    }

    public TYPE getType() {
        return this.type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public void setPrograms(List<Program> programs) {
        this.programs = programs;
    }
}
