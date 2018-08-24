package org.icddrb.dhis.android.sdk.persistence.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.builder.Condition.Operation;
import com.raizlabs.android.dbflow.sql.builder.ConditionQueryBuilder;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

public class OrganisationUnitProgramRelationship extends BaseModel {
    String organisationUnitId;
    String programId;

    public final class Adapter extends ModelAdapter<OrganisationUnitProgramRelationship> {
        public Class<OrganisationUnitProgramRelationship> getModelClass() {
            return OrganisationUnitProgramRelationship.class;
        }

        public String getTableName() {
            return Table.TABLE_NAME;
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `OrganisationUnitProgramRelationship` (`ORGANISATIONUNITID`, `PROGRAMID`) VALUES (?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, OrganisationUnitProgramRelationship model) {
            if (model.organisationUnitId != null) {
                statement.bindString(1, model.organisationUnitId);
            } else {
                statement.bindNull(1);
            }
            if (model.programId != null) {
                statement.bindString(2, model.programId);
            } else {
                statement.bindNull(2);
            }
        }

        public void bindToContentValues(ContentValues contentValues, OrganisationUnitProgramRelationship model) {
            if (model.organisationUnitId != null) {
                contentValues.put("organisationUnitId", model.organisationUnitId);
            } else {
                contentValues.putNull("organisationUnitId");
            }
            if (model.programId != null) {
                contentValues.put("programId", model.programId);
            } else {
                contentValues.putNull("programId");
            }
        }

        public void bindToInsertValues(ContentValues contentValues, OrganisationUnitProgramRelationship model) {
            if (model.organisationUnitId != null) {
                contentValues.put("organisationUnitId", model.organisationUnitId);
            } else {
                contentValues.putNull("organisationUnitId");
            }
            if (model.programId != null) {
                contentValues.put("programId", model.programId);
            } else {
                contentValues.putNull("programId");
            }
        }

        public boolean exists(OrganisationUnitProgramRelationship model) {
            return new Select().from(OrganisationUnitProgramRelationship.class).where(getPrimaryModelWhere(model)).hasData();
        }

        public void loadFromCursor(Cursor cursor, OrganisationUnitProgramRelationship model) {
            int indexorganisationUnitId = cursor.getColumnIndex("organisationUnitId");
            if (indexorganisationUnitId != -1) {
                if (cursor.isNull(indexorganisationUnitId)) {
                    model.organisationUnitId = null;
                } else {
                    model.organisationUnitId = cursor.getString(indexorganisationUnitId);
                }
            }
            int indexprogramId = cursor.getColumnIndex("programId");
            if (indexprogramId == -1) {
                return;
            }
            if (cursor.isNull(indexprogramId)) {
                model.programId = null;
            } else {
                model.programId = cursor.getString(indexprogramId);
            }
        }

        public ConditionQueryBuilder<OrganisationUnitProgramRelationship> getPrimaryModelWhere(OrganisationUnitProgramRelationship model) {
            return new ConditionQueryBuilder(OrganisationUnitProgramRelationship.class, Condition.column("organisationUnitId").is(model.organisationUnitId), Condition.column("programId").is(model.programId));
        }

        public ConditionQueryBuilder<OrganisationUnitProgramRelationship> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(OrganisationUnitProgramRelationship.class, Condition.column("organisationUnitId").is(Operation.EMPTY_PARAM), Condition.column("programId").is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return "CREATE TABLE IF NOT EXISTS `OrganisationUnitProgramRelationship`(`organisationUnitId` TEXT, `programId` TEXT, PRIMARY KEY(`organisationUnitId`, `programId`));";
        }

        public final OrganisationUnitProgramRelationship newInstance() {
            return new OrganisationUnitProgramRelationship();
        }
    }

    public final class Table {
        public static final String ORGANISATIONUNITID = "organisationUnitId";
        public static final String PROGRAMID = "programId";
        public static final String TABLE_NAME = "OrganisationUnitProgramRelationship";
    }

    public String getOrganisationUnitId() {
        return this.organisationUnitId;
    }

    public void setOrganisationUnitId(String organisationUnitId) {
        this.organisationUnitId = organisationUnitId;
    }

    public String getProgramId() {
        return this.programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }
}
