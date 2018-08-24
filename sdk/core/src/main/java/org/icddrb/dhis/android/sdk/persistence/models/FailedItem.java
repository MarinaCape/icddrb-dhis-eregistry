package org.icddrb.dhis.android.sdk.persistence.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.builder.Condition.Operation;
import com.raizlabs.android.dbflow.sql.builder.ConditionQueryBuilder;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import org.icddrb.dhis.android.sdk.controllers.tracker.TrackerController;

public class FailedItem extends BaseModel {
    private static final String CLASS_TAG = "FailedItem";
    public static final String ENROLLMENT = "Enrollment";
    public static final String EVENT = "Event";
    public static final String TRACKEDENTITYINSTANCE = "TrackedEntityInstance";
    private String errorMessage;
    private int failCount;
    private int httpStatusCode;
    protected ImportSummary importSummary;
    private long itemId;
    private String itemType;

    public final class Adapter extends ModelAdapter<FailedItem> {
        public Class<FailedItem> getModelClass() {
            return FailedItem.class;
        }

        public String getTableName() {
            return "FailedItem";
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `FailedItem` (`importSummary`, `ITEMID`, `ITEMTYPE`, `HTTPSTATUSCODE`, `ERRORMESSAGE`, `FAILCOUNT`) VALUES (?, ?, ?, ?, ?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, FailedItem model) {
            if (model.importSummary != null) {
                model.importSummary.save();
                statement.bindLong(1, (long) model.importSummary.id);
            } else {
                statement.bindNull(1);
            }
            statement.bindLong(2, model.getItemId());
            if (model.getItemType() != null) {
                statement.bindString(3, model.getItemType());
            } else {
                statement.bindNull(3);
            }
            statement.bindLong(4, (long) model.getHttpStatusCode());
            if (model.getErrorMessage() != null) {
                statement.bindString(5, model.getErrorMessage());
            } else {
                statement.bindNull(5);
            }
            statement.bindLong(6, (long) model.getFailCount());
        }

        public void bindToContentValues(ContentValues contentValues, FailedItem model) {
            if (model.importSummary != null) {
                model.importSummary.save();
                contentValues.put("importSummary", Integer.valueOf(model.importSummary.id));
            } else {
                contentValues.putNull("importSummary");
            }
            contentValues.put(Table.ITEMID, Long.valueOf(model.getItemId()));
            if (model.getItemType() != null) {
                contentValues.put(Table.ITEMTYPE, model.getItemType());
            } else {
                contentValues.putNull(Table.ITEMTYPE);
            }
            contentValues.put(Table.HTTPSTATUSCODE, Integer.valueOf(model.getHttpStatusCode()));
            if (model.getErrorMessage() != null) {
                contentValues.put(Table.ERRORMESSAGE, model.getErrorMessage());
            } else {
                contentValues.putNull(Table.ERRORMESSAGE);
            }
            contentValues.put(Table.FAILCOUNT, Integer.valueOf(model.getFailCount()));
        }

        public void bindToInsertValues(ContentValues contentValues, FailedItem model) {
            if (model.importSummary != null) {
                model.importSummary.save();
                contentValues.put("importSummary", Integer.valueOf(model.importSummary.id));
            } else {
                contentValues.putNull("importSummary");
            }
            contentValues.put(Table.ITEMID, Long.valueOf(model.getItemId()));
            if (model.getItemType() != null) {
                contentValues.put(Table.ITEMTYPE, model.getItemType());
            } else {
                contentValues.putNull(Table.ITEMTYPE);
            }
            contentValues.put(Table.HTTPSTATUSCODE, Integer.valueOf(model.getHttpStatusCode()));
            if (model.getErrorMessage() != null) {
                contentValues.put(Table.ERRORMESSAGE, model.getErrorMessage());
            } else {
                contentValues.putNull(Table.ERRORMESSAGE);
            }
            contentValues.put(Table.FAILCOUNT, Integer.valueOf(model.getFailCount()));
        }

        public boolean exists(FailedItem model) {
            return new Select().from(FailedItem.class).where(getPrimaryModelWhere(model)).hasData();
        }

        public void loadFromCursor(Cursor cursor, FailedItem model) {
            int indeximportSummary = cursor.getColumnIndex("importSummary");
            if (!(indeximportSummary == -1 || cursor.isNull(indeximportSummary))) {
                model.importSummary = (ImportSummary) new Select().from(ImportSummary.class).where().and(Condition.column("id").is(Integer.valueOf(cursor.getInt(indeximportSummary)))).querySingle();
            }
            int indexitemId = cursor.getColumnIndex(Table.ITEMID);
            if (indexitemId != -1) {
                model.setItemId(cursor.getLong(indexitemId));
            }
            int indexitemType = cursor.getColumnIndex(Table.ITEMTYPE);
            if (indexitemType != -1) {
                if (cursor.isNull(indexitemType)) {
                    model.setItemType(null);
                } else {
                    model.setItemType(cursor.getString(indexitemType));
                }
            }
            int indexhttpStatusCode = cursor.getColumnIndex(Table.HTTPSTATUSCODE);
            if (indexhttpStatusCode != -1) {
                model.setHttpStatusCode(cursor.getInt(indexhttpStatusCode));
            }
            int indexerrorMessage = cursor.getColumnIndex(Table.ERRORMESSAGE);
            if (indexerrorMessage != -1) {
                if (cursor.isNull(indexerrorMessage)) {
                    model.setErrorMessage(null);
                } else {
                    model.setErrorMessage(cursor.getString(indexerrorMessage));
                }
            }
            int indexfailCount = cursor.getColumnIndex(Table.FAILCOUNT);
            if (indexfailCount != -1) {
                model.setFailCount(cursor.getInt(indexfailCount));
            }
        }

        public ConditionQueryBuilder<FailedItem> getPrimaryModelWhere(FailedItem model) {
            return new ConditionQueryBuilder(FailedItem.class, Condition.column(Table.ITEMID).is(Long.valueOf(model.getItemId())), Condition.column(Table.ITEMTYPE).is(model.getItemType()));
        }

        public ConditionQueryBuilder<FailedItem> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(FailedItem.class, Condition.column(Table.ITEMID).is(Operation.EMPTY_PARAM), Condition.column(Table.ITEMTYPE).is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return String.format("CREATE TABLE IF NOT EXISTS `FailedItem`( `importSummary` INTEGER, `itemId` INTEGER, `itemType` TEXT, `httpStatusCode` INTEGER, `errorMessage` TEXT, `failCount` INTEGER, PRIMARY KEY(`itemId`, `itemType`), FOREIGN KEY(`importSummary`) REFERENCES `%1s` (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION );", new Object[]{FlowManager.getTableName(ImportSummary.class)});
        }

        public final FailedItem newInstance() {
            return new FailedItem();
        }
    }

    public final class Table {
        public static final String ERRORMESSAGE = "errorMessage";
        public static final String FAILCOUNT = "failCount";
        public static final String HTTPSTATUSCODE = "httpStatusCode";
        public static final String IMPORTSUMMARY_IMPORTSUMMARY = "importSummary";
        public static final String ITEMID = "itemId";
        public static final String ITEMTYPE = "itemType";
        public static final String TABLE_NAME = "FailedItem";
    }

    public BaseSerializableModel getItem() {
        if (this.itemType.equals("Event")) {
            return TrackerController.getEvent(this.itemId);
        }
        if (this.itemType.equals("Enrollment")) {
            return TrackerController.getEnrollment(this.itemId);
        }
        if (this.itemType.equals("TrackedEntityInstance")) {
            return TrackerController.getTrackedEntityInstance(this.itemId);
        }
        return null;
    }

    public ImportSummary getImportSummary() {
        return this.importSummary;
    }

    public void setImportSummary(ImportSummary importSummary) {
        this.importSummary = importSummary;
    }

    public long getItemId() {
        return this.itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public String getItemType() {
        return this.itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public int getHttpStatusCode() {
        return this.httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getFailCount() {
        return this.failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }
}
