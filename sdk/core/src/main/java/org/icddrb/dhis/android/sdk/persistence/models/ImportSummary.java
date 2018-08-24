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
import java.util.List;

public class ImportSummary extends BaseModel {
    public static final String ERROR = "ERROR";
    public static final String OK = "OK";
    public static final String SUCCESS = "SUCCESS";
    @JsonProperty("conflicts")
    List<Conflict> conflicts;
    @JsonProperty("description")
    String description;
    @JsonProperty("href")
    String href;
    int id;
    @JsonProperty("importCount")
    ImportCount importCount;
    @JsonProperty("reference")
    String reference;
    @JsonProperty("status")
    String status;

    public final class Adapter extends ModelAdapter<ImportSummary> {
        public Class<ImportSummary> getModelClass() {
            return ImportSummary.class;
        }

        public String getTableName() {
            return "ImportSummary";
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `ImportSummary` (`STATUS`, `DESCRIPTION`, `importCount`, `REFERENCE`, `HREF`) VALUES (?, ?, ?, ?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, ImportSummary model) {
            if (model.status != null) {
                statement.bindString(1, model.status);
            } else {
                statement.bindNull(1);
            }
            if (model.description != null) {
                statement.bindString(2, model.description);
            } else {
                statement.bindNull(2);
            }
            if (model.importCount != null) {
                model.importCount.save();
                statement.bindLong(3, (long) model.importCount.id);
            } else {
                statement.bindNull(3);
            }
            if (model.reference != null) {
                statement.bindString(4, model.reference);
            } else {
                statement.bindNull(4);
            }
            if (model.href != null) {
                statement.bindString(5, model.href);
            } else {
                statement.bindNull(5);
            }
        }

        public void bindToContentValues(ContentValues contentValues, ImportSummary model) {
            contentValues.put("id", Integer.valueOf(model.id));
            if (model.status != null) {
                contentValues.put("status", model.status);
            } else {
                contentValues.putNull("status");
            }
            if (model.description != null) {
                contentValues.put("description", model.description);
            } else {
                contentValues.putNull("description");
            }
            if (model.importCount != null) {
                model.importCount.save();
                contentValues.put(Table.IMPORTCOUNT_IMPORTCOUNT, Integer.valueOf(model.importCount.id));
            } else {
                contentValues.putNull(Table.IMPORTCOUNT_IMPORTCOUNT);
            }
            if (model.reference != null) {
                contentValues.put(Table.REFERENCE, model.reference);
            } else {
                contentValues.putNull(Table.REFERENCE);
            }
            if (model.href != null) {
                contentValues.put(Table.HREF, model.href);
            } else {
                contentValues.putNull(Table.HREF);
            }
        }

        public void bindToInsertValues(ContentValues contentValues, ImportSummary model) {
            if (model.status != null) {
                contentValues.put("status", model.status);
            } else {
                contentValues.putNull("status");
            }
            if (model.description != null) {
                contentValues.put("description", model.description);
            } else {
                contentValues.putNull("description");
            }
            if (model.importCount != null) {
                model.importCount.save();
                contentValues.put(Table.IMPORTCOUNT_IMPORTCOUNT, Integer.valueOf(model.importCount.id));
            } else {
                contentValues.putNull(Table.IMPORTCOUNT_IMPORTCOUNT);
            }
            if (model.reference != null) {
                contentValues.put(Table.REFERENCE, model.reference);
            } else {
                contentValues.putNull(Table.REFERENCE);
            }
            if (model.href != null) {
                contentValues.put(Table.HREF, model.href);
            } else {
                contentValues.putNull(Table.HREF);
            }
        }

        public boolean exists(ImportSummary model) {
            return model.id > 0;
        }

        public void loadFromCursor(Cursor cursor, ImportSummary model) {
            int indexid = cursor.getColumnIndex("id");
            if (indexid != -1) {
                model.id = cursor.getInt(indexid);
            }
            int indexstatus = cursor.getColumnIndex("status");
            if (indexstatus != -1) {
                if (cursor.isNull(indexstatus)) {
                    model.status = null;
                } else {
                    model.status = cursor.getString(indexstatus);
                }
            }
            int indexdescription = cursor.getColumnIndex("description");
            if (indexdescription != -1) {
                if (cursor.isNull(indexdescription)) {
                    model.description = null;
                } else {
                    model.description = cursor.getString(indexdescription);
                }
            }
            int indeximportCount = cursor.getColumnIndex(Table.IMPORTCOUNT_IMPORTCOUNT);
            if (!(indeximportCount == -1 || cursor.isNull(indeximportCount))) {
                model.importCount = (ImportCount) new Select().from(ImportCount.class).where().and(Condition.column("id").is(Integer.valueOf(cursor.getInt(indeximportCount)))).querySingle();
            }
            int indexreference = cursor.getColumnIndex(Table.REFERENCE);
            if (indexreference != -1) {
                if (cursor.isNull(indexreference)) {
                    model.reference = null;
                } else {
                    model.reference = cursor.getString(indexreference);
                }
            }
            int indexhref = cursor.getColumnIndex(Table.HREF);
            if (indexhref == -1) {
                return;
            }
            if (cursor.isNull(indexhref)) {
                model.href = null;
            } else {
                model.href = cursor.getString(indexhref);
            }
        }

        public void updateAutoIncrement(ImportSummary model, long id) {
            model.id = (int) id;
        }

        public long getAutoIncrementingId(ImportSummary model) {
            return (long) model.id;
        }

        public String getAutoIncrementingColumnName() {
            return "id";
        }

        public ConditionQueryBuilder<ImportSummary> getPrimaryModelWhere(ImportSummary model) {
            return new ConditionQueryBuilder(ImportSummary.class, Condition.column("id").is(Integer.valueOf(model.id)));
        }

        public ConditionQueryBuilder<ImportSummary> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(ImportSummary.class, Condition.column("id").is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return String.format("CREATE TABLE IF NOT EXISTS `ImportSummary`(`id` INTEGER PRIMARY KEY AUTOINCREMENT, `status` TEXT, `description` TEXT,  `importCount` INTEGER, `reference` TEXT, `href` TEXT, FOREIGN KEY(`importCount`) REFERENCES `%1s` (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION );", new Object[]{FlowManager.getTableName(ImportCount.class)});
        }

        public final ImportSummary newInstance() {
            return new ImportSummary();
        }
    }

    public enum Status {
        SUCCESS,
        OK,
        ERROR
    }

    public final class Table {
        public static final String DESCRIPTION = "description";
        public static final String HREF = "href";
        public static final String ID = "id";
        public static final String IMPORTCOUNT_IMPORTCOUNT = "importCount";
        public static final String REFERENCE = "reference";
        public static final String STATUS = "status";
        public static final String TABLE_NAME = "ImportSummary";
    }

    @JsonAnySetter
    public void handleUnknown(String key, Object value) {
    }

    public List<Conflict> getConflicts() {
        if (this.conflicts == null) {
            this.conflicts = new Select().from(Conflict.class).where(Condition.column("importSummary").is(Integer.valueOf(this.id))).queryList();
        }
        return this.conflicts;
    }

    public String getReference() {
        return this.reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getHref() {
        return this.href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public ImportCount getImportCount() {
        return this.importCount;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return this.id;
    }

    public boolean isSuccessOrOK() {
        return Status.SUCCESS.equals(this.status) || Status.OK.equals(this.status) || ((getStatus().equals(SUCCESS) && (getConflicts() == null || getConflicts().size() == 0)) || getStatus().equals(OK));
    }

    public boolean isError() {
        return Status.ERROR.equals(this.status) || ERROR.equals(getStatus()) || (getConflicts() != null && getConflicts().size() > 0);
    }

    public boolean isConflictOnBatchPush() {
        return getStatus().equals(SUCCESS) && getConflicts() != null && getConflicts().size() > 0;
    }
}
