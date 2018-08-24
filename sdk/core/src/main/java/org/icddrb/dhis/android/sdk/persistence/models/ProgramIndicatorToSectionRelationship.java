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

public final class ProgramIndicatorToSectionRelationship extends BaseModel {
    long id;
    ProgramIndicator programIndicator;
    String programSection;

    public final class Adapter extends ModelAdapter<ProgramIndicatorToSectionRelationship> {
        public Class<ProgramIndicatorToSectionRelationship> getModelClass() {
            return ProgramIndicatorToSectionRelationship.class;
        }

        public String getTableName() {
            return Table.TABLE_NAME;
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `ProgramIndicatorToSectionRelationship` (`programIndicatorId`, `PROGRAMSECTION`) VALUES (?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, ProgramIndicatorToSectionRelationship model) {
            if (model.programIndicator == null) {
                statement.bindNull(1);
            } else if (model.programIndicator.id != null) {
                statement.bindString(1, model.programIndicator.id);
            } else {
                statement.bindNull(1);
            }
            if (model.programSection != null) {
                statement.bindString(2, model.programSection);
            } else {
                statement.bindNull(2);
            }
        }

        public void bindToContentValues(ContentValues contentValues, ProgramIndicatorToSectionRelationship model) {
            contentValues.put("id", Long.valueOf(model.id));
            if (model.programIndicator == null) {
                contentValues.putNull(Table.PROGRAMINDICATOR_PROGRAMINDICATORID);
            } else if (model.programIndicator.id != null) {
                contentValues.put(Table.PROGRAMINDICATOR_PROGRAMINDICATORID, model.programIndicator.id);
            } else {
                contentValues.putNull(Table.PROGRAMINDICATOR_PROGRAMINDICATORID);
            }
            if (model.programSection != null) {
                contentValues.put(Table.PROGRAMSECTION, model.programSection);
            } else {
                contentValues.putNull(Table.PROGRAMSECTION);
            }
        }

        public void bindToInsertValues(ContentValues contentValues, ProgramIndicatorToSectionRelationship model) {
            if (model.programIndicator == null) {
                contentValues.putNull(Table.PROGRAMINDICATOR_PROGRAMINDICATORID);
            } else if (model.programIndicator.id != null) {
                contentValues.put(Table.PROGRAMINDICATOR_PROGRAMINDICATORID, model.programIndicator.id);
            } else {
                contentValues.putNull(Table.PROGRAMINDICATOR_PROGRAMINDICATORID);
            }
            if (model.programSection != null) {
                contentValues.put(Table.PROGRAMSECTION, model.programSection);
            } else {
                contentValues.putNull(Table.PROGRAMSECTION);
            }
        }

        public boolean exists(ProgramIndicatorToSectionRelationship model) {
            return model.id > 0;
        }

        public void loadFromCursor(Cursor cursor, ProgramIndicatorToSectionRelationship model) {
            int indexid = cursor.getColumnIndex("id");
            if (indexid != -1) {
                model.id = cursor.getLong(indexid);
            }
            int indexprogramIndicatorId = cursor.getColumnIndex(Table.PROGRAMINDICATOR_PROGRAMINDICATORID);
            if (!(indexprogramIndicatorId == -1 || cursor.isNull(indexprogramIndicatorId))) {
                model.programIndicator = (ProgramIndicator) new Select().from(ProgramIndicator.class).where().and(Condition.column("id").is(cursor.getString(indexprogramIndicatorId))).querySingle();
            }
            int indexprogramSection = cursor.getColumnIndex(Table.PROGRAMSECTION);
            if (indexprogramSection == -1) {
                return;
            }
            if (cursor.isNull(indexprogramSection)) {
                model.programSection = null;
            } else {
                model.programSection = cursor.getString(indexprogramSection);
            }
        }

        public void updateAutoIncrement(ProgramIndicatorToSectionRelationship model, long id) {
            model.id = id;
        }

        public long getAutoIncrementingId(ProgramIndicatorToSectionRelationship model) {
            return model.id;
        }

        public String getAutoIncrementingColumnName() {
            return "id";
        }

        public ConditionQueryBuilder<ProgramIndicatorToSectionRelationship> getPrimaryModelWhere(ProgramIndicatorToSectionRelationship model) {
            return new ConditionQueryBuilder(ProgramIndicatorToSectionRelationship.class, Condition.column("id").is(Long.valueOf(model.id)));
        }

        public ConditionQueryBuilder<ProgramIndicatorToSectionRelationship> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(ProgramIndicatorToSectionRelationship.class, Condition.column("id").is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return String.format("CREATE TABLE IF NOT EXISTS `ProgramIndicatorToSectionRelationship`(`id` INTEGER PRIMARY KEY AUTOINCREMENT,  `programIndicatorId` TEXT, `programSection` TEXT, FOREIGN KEY(`programIndicatorId`) REFERENCES `%1s` (`id`) ON UPDATE NO ACTION ON DELETE CASCADE );", new Object[]{FlowManager.getTableName(ProgramIndicator.class)});
        }

        public final ProgramIndicatorToSectionRelationship newInstance() {
            return new ProgramIndicatorToSectionRelationship();
        }
    }

    public final class Table {
        public static final String ID = "id";
        public static final String PROGRAMINDICATOR_PROGRAMINDICATORID = "programIndicatorId";
        public static final String PROGRAMSECTION = "programSection";
        public static final String TABLE_NAME = "ProgramIndicatorToSectionRelationship";
    }

    public long getId() {
        return this.id;
    }

    public String getProgramSection() {
        return this.programSection;
    }

    public void setProgramSection(String programSection) {
        this.programSection = programSection;
    }

    public ProgramIndicator getProgramIndicator() {
        return this.programIndicator;
    }

    public void setProgramIndicator(ProgramIndicator programIndicator) {
        this.programIndicator = programIndicator;
    }
}
