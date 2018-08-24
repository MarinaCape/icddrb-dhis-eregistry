package org.icddrb.dhis.android.sdk.persistence.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.builder.Condition.Operation;
import com.raizlabs.android.dbflow.sql.builder.ConditionQueryBuilder;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import java.util.List;
import java.util.Map;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;

public class ProgramStageSection extends BaseMetaDataObject {
    @JsonProperty("dataElements")
    List<DataElement> dataElements;
    @JsonProperty("externalAccess")
    boolean externalAccess;
    @JsonProperty("programIndicators")
    List<ProgramIndicator> programIndicators;
    String programStage;
    @JsonProperty("programStageDataElements")
    List<ProgramStageDataElement> programStageDataElements;
    @JsonProperty("sortOrder")
    int sortOrder;

    public final class Adapter extends ModelAdapter<ProgramStageSection> {
        public Class<ProgramStageSection> getModelClass() {
            return ProgramStageSection.class;
        }

        public String getTableName() {
            return Table.TABLE_NAME;
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `ProgramStageSection` (`NAME`, `DISPLAYNAME`, `CREATED`, `LASTUPDATED`, `ACCESS`, `ID`, `SORTORDER`, `EXTERNALACCESS`, `PROGRAMSTAGE`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, ProgramStageSection model) {
            if (model.name != null) {
                statement.bindString(1, model.name);
            } else {
                statement.bindNull(1);
            }
            if (model.displayName != null) {
                statement.bindString(2, model.displayName);
            } else {
                statement.bindNull(2);
            }
            if (model.created != null) {
                statement.bindString(3, model.created);
            } else {
                statement.bindNull(3);
            }
            if (model.lastUpdated != null) {
                statement.bindString(4, model.lastUpdated);
            } else {
                statement.bindNull(4);
            }
            Object modelaccess = FlowManager.getTypeConverterForClass(Access.class).getDBValue(model.access);
            if (modelaccess != null) {
                statement.bindString(5, (String) modelaccess);
            } else {
                statement.bindNull(5);
            }
            if (model.id != null) {
                statement.bindString(6, model.id);
            } else {
                statement.bindNull(6);
            }
            statement.bindLong(7, (long) model.sortOrder);
            Object modelexternalAccess = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.externalAccess));
            if (modelexternalAccess != null) {
                statement.bindLong(8, (long) ((Integer) modelexternalAccess).intValue());
            } else {
                statement.bindNull(8);
            }
            if (model.programStage != null) {
                statement.bindString(9, model.programStage);
            } else {
                statement.bindNull(9);
            }
        }

        public void bindToContentValues(ContentValues contentValues, ProgramStageSection model) {
            if (model.name != null) {
                contentValues.put("name", model.name);
            } else {
                contentValues.putNull("name");
            }
            if (model.displayName != null) {
                contentValues.put("displayName", model.displayName);
            } else {
                contentValues.putNull("displayName");
            }
            if (model.created != null) {
                contentValues.put("created", model.created);
            } else {
                contentValues.putNull("created");
            }
            if (model.lastUpdated != null) {
                contentValues.put("lastUpdated", model.lastUpdated);
            } else {
                contentValues.putNull("lastUpdated");
            }
            Object modelaccess = FlowManager.getTypeConverterForClass(Access.class).getDBValue(model.access);
            if (modelaccess != null) {
                contentValues.put("access", (String) modelaccess);
            } else {
                contentValues.putNull("access");
            }
            if (model.id != null) {
                contentValues.put("id", model.id);
            } else {
                contentValues.putNull("id");
            }
            contentValues.put("sortOrder", Integer.valueOf(model.sortOrder));
            Object modelexternalAccess = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.externalAccess));
            if (modelexternalAccess != null) {
                contentValues.put("externalAccess", (Integer) modelexternalAccess);
            } else {
                contentValues.putNull("externalAccess");
            }
            if (model.programStage != null) {
                contentValues.put("programStage", model.programStage);
            } else {
                contentValues.putNull("programStage");
            }
        }

        public void bindToInsertValues(ContentValues contentValues, ProgramStageSection model) {
            if (model.name != null) {
                contentValues.put("name", model.name);
            } else {
                contentValues.putNull("name");
            }
            if (model.displayName != null) {
                contentValues.put("displayName", model.displayName);
            } else {
                contentValues.putNull("displayName");
            }
            if (model.created != null) {
                contentValues.put("created", model.created);
            } else {
                contentValues.putNull("created");
            }
            if (model.lastUpdated != null) {
                contentValues.put("lastUpdated", model.lastUpdated);
            } else {
                contentValues.putNull("lastUpdated");
            }
            Object modelaccess = FlowManager.getTypeConverterForClass(Access.class).getDBValue(model.access);
            if (modelaccess != null) {
                contentValues.put("access", (String) modelaccess);
            } else {
                contentValues.putNull("access");
            }
            if (model.id != null) {
                contentValues.put("id", model.id);
            } else {
                contentValues.putNull("id");
            }
            contentValues.put("sortOrder", Integer.valueOf(model.sortOrder));
            Object modelexternalAccess = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.externalAccess));
            if (modelexternalAccess != null) {
                contentValues.put("externalAccess", (Integer) modelexternalAccess);
            } else {
                contentValues.putNull("externalAccess");
            }
            if (model.programStage != null) {
                contentValues.put("programStage", model.programStage);
            } else {
                contentValues.putNull("programStage");
            }
        }

        public boolean exists(ProgramStageSection model) {
            return new Select().from(ProgramStageSection.class).where(getPrimaryModelWhere(model)).hasData();
        }

        public void loadFromCursor(Cursor cursor, ProgramStageSection model) {
            int indexname = cursor.getColumnIndex("name");
            if (indexname != -1) {
                if (cursor.isNull(indexname)) {
                    model.name = null;
                } else {
                    model.name = cursor.getString(indexname);
                }
            }
            int indexdisplayName = cursor.getColumnIndex("displayName");
            if (indexdisplayName != -1) {
                if (cursor.isNull(indexdisplayName)) {
                    model.displayName = null;
                } else {
                    model.displayName = cursor.getString(indexdisplayName);
                }
            }
            int indexcreated = cursor.getColumnIndex("created");
            if (indexcreated != -1) {
                if (cursor.isNull(indexcreated)) {
                    model.created = null;
                } else {
                    model.created = cursor.getString(indexcreated);
                }
            }
            int indexlastUpdated = cursor.getColumnIndex("lastUpdated");
            if (indexlastUpdated != -1) {
                if (cursor.isNull(indexlastUpdated)) {
                    model.lastUpdated = null;
                } else {
                    model.lastUpdated = cursor.getString(indexlastUpdated);
                }
            }
            int indexaccess = cursor.getColumnIndex("access");
            if (indexaccess != -1) {
                if (cursor.isNull(indexaccess)) {
                    model.access = null;
                } else {
                    model.access = (Access) FlowManager.getTypeConverterForClass(Access.class).getModelValue(cursor.getString(indexaccess));
                }
            }
            int indexid = cursor.getColumnIndex("id");
            if (indexid != -1) {
                if (cursor.isNull(indexid)) {
                    model.id = null;
                } else {
                    model.id = cursor.getString(indexid);
                }
            }
            int indexsortOrder = cursor.getColumnIndex("sortOrder");
            if (indexsortOrder != -1) {
                model.sortOrder = cursor.getInt(indexsortOrder);
            }
            int indexexternalAccess = cursor.getColumnIndex("externalAccess");
            if (indexexternalAccess != -1) {
                model.externalAccess = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexexternalAccess)))).booleanValue();
            }
            int indexprogramStage = cursor.getColumnIndex("programStage");
            if (indexprogramStage == -1) {
                return;
            }
            if (cursor.isNull(indexprogramStage)) {
                model.programStage = null;
            } else {
                model.programStage = cursor.getString(indexprogramStage);
            }
        }

        public ConditionQueryBuilder<ProgramStageSection> getPrimaryModelWhere(ProgramStageSection model) {
            return new ConditionQueryBuilder(ProgramStageSection.class, Condition.column("id").is(model.id));
        }

        public ConditionQueryBuilder<ProgramStageSection> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(ProgramStageSection.class, Condition.column("id").is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return "CREATE TABLE IF NOT EXISTS `ProgramStageSection`(`name` TEXT, `displayName` TEXT, `created` TEXT, `lastUpdated` TEXT, `access` TEXT, `id` TEXT, `sortOrder` INTEGER, `externalAccess` INTEGER, `programStage` TEXT, PRIMARY KEY(`id`));";
        }

        public final ProgramStageSection newInstance() {
            return new ProgramStageSection();
        }
    }

    public final class Table {
        public static final String ACCESS = "access";
        public static final String CREATED = "created";
        public static final String DISPLAYNAME = "displayName";
        public static final String EXTERNALACCESS = "externalAccess";
        public static final String ID = "id";
        public static final String LASTUPDATED = "lastUpdated";
        public static final String NAME = "name";
        public static final String PROGRAMSTAGE = "programStage";
        public static final String SORTORDER = "sortOrder";
        public static final String TABLE_NAME = "ProgramStageSection";
    }

    @JsonProperty("programStage")
    public void setProgramStage(Map<String, Object> programStage) {
        this.programStage = (String) programStage.get("id");
    }

    public List<ProgramStageDataElement> getProgramStageDataElements() {
        if (this.programStageDataElements == null) {
            this.programStageDataElements = MetaDataController.getProgramStageDataElements(this);
        }
        return this.programStageDataElements;
    }

    public void setProgramStageDataElements(List<ProgramStageDataElement> programStageDataElements) {
        this.programStageDataElements = programStageDataElements;
    }

    public List<DataElement> getDataElements() {
        return this.dataElements;
    }

    public List<ProgramIndicator> getProgramIndicators() {
        if (this.programIndicators == null) {
            this.programIndicators = MetaDataController.getProgramIndicatorsBySection(this.id);
        }
        return this.programIndicators;
    }

    public void setProgramIndicators(List<ProgramIndicator> programIndicators) {
        this.programIndicators = programIndicators;
    }

    public String getProgramStage() {
        return this.programStage;
    }

    public void setProgramStage(String programStage) {
        this.programStage = programStage;
    }

    public boolean getExternalAccess() {
        return this.externalAccess;
    }

    public void setExternalAccess(boolean externalAccess) {
        this.externalAccess = externalAccess;
    }

    public int getSortOrder() {
        return this.sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
}
