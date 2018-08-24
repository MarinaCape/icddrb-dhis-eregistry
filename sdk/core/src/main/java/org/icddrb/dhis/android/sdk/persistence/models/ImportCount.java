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

public class ImportCount extends BaseModel {
    @JsonProperty("deleted")
    private int deleted;
    protected int id;
    @JsonProperty("ignored")
    private int ignored;
    @JsonProperty("imported")
    private int imported;
    @JsonProperty("updated")
    private int updated;

    public final class Adapter extends ModelAdapter<ImportCount> {
        public Class<ImportCount> getModelClass() {
            return ImportCount.class;
        }

        public String getTableName() {
            return Table.TABLE_NAME;
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `ImportCount` (`IMPORTED`, `UPDATED`, `IGNORED`, `DELETED`) VALUES (?, ?, ?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, ImportCount model) {
            statement.bindLong(1, (long) model.getImported());
            statement.bindLong(2, (long) model.getUpdated());
            statement.bindLong(3, (long) model.getIgnored());
            statement.bindLong(4, (long) model.getDeleted());
        }

        public void bindToContentValues(ContentValues contentValues, ImportCount model) {
            contentValues.put("id", Integer.valueOf(model.id));
            contentValues.put(Table.IMPORTED, Integer.valueOf(model.getImported()));
            contentValues.put(Table.UPDATED, Integer.valueOf(model.getUpdated()));
            contentValues.put(Table.IGNORED, Integer.valueOf(model.getIgnored()));
            contentValues.put(Table.DELETED, Integer.valueOf(model.getDeleted()));
        }

        public void bindToInsertValues(ContentValues contentValues, ImportCount model) {
            contentValues.put(Table.IMPORTED, Integer.valueOf(model.getImported()));
            contentValues.put(Table.UPDATED, Integer.valueOf(model.getUpdated()));
            contentValues.put(Table.IGNORED, Integer.valueOf(model.getIgnored()));
            contentValues.put(Table.DELETED, Integer.valueOf(model.getDeleted()));
        }

        public boolean exists(ImportCount model) {
            return model.id > 0;
        }

        public void loadFromCursor(Cursor cursor, ImportCount model) {
            int indexid = cursor.getColumnIndex("id");
            if (indexid != -1) {
                model.id = cursor.getInt(indexid);
            }
            int indeximported = cursor.getColumnIndex(Table.IMPORTED);
            if (indeximported != -1) {
                model.setImported(cursor.getInt(indeximported));
            }
            int indexupdated = cursor.getColumnIndex(Table.UPDATED);
            if (indexupdated != -1) {
                model.setUpdated(cursor.getInt(indexupdated));
            }
            int indexignored = cursor.getColumnIndex(Table.IGNORED);
            if (indexignored != -1) {
                model.setIgnored(cursor.getInt(indexignored));
            }
            int indexdeleted = cursor.getColumnIndex(Table.DELETED);
            if (indexdeleted != -1) {
                model.setDeleted(cursor.getInt(indexdeleted));
            }
        }

        public void updateAutoIncrement(ImportCount model, long id) {
            model.id = (int) id;
        }

        public long getAutoIncrementingId(ImportCount model) {
            return (long) model.id;
        }

        public String getAutoIncrementingColumnName() {
            return "id";
        }

        public ConditionQueryBuilder<ImportCount> getPrimaryModelWhere(ImportCount model) {
            return new ConditionQueryBuilder(ImportCount.class, Condition.column("id").is(Integer.valueOf(model.id)));
        }

        public ConditionQueryBuilder<ImportCount> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(ImportCount.class, Condition.column("id").is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return "CREATE TABLE IF NOT EXISTS `ImportCount`(`id` INTEGER PRIMARY KEY AUTOINCREMENT, `imported` INTEGER, `updated` INTEGER, `ignored` INTEGER, `deleted` INTEGER);";
        }

        public final ImportCount newInstance() {
            return new ImportCount();
        }
    }

    public final class Table {
        public static final String DELETED = "deleted";
        public static final String ID = "id";
        public static final String IGNORED = "ignored";
        public static final String IMPORTED = "imported";
        public static final String TABLE_NAME = "ImportCount";
        public static final String UPDATED = "updated";
    }

    @JsonAnySetter
    public void handleUnknown(String key, Object value) {
    }

    public int getId() {
        return this.id;
    }

    public int getImported() {
        return this.imported;
    }

    public int getUpdated() {
        return this.updated;
    }

    public int getIgnored() {
        return this.ignored;
    }

    public int getDeleted() {
        return this.deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public void setIgnored(int ignored) {
        this.ignored = ignored;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }

    public void setImported(int imported) {
        this.imported = imported;
    }

    public void setId(int id) {
        this.id = id;
    }
}
