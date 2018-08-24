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
import java.util.Map;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;

public class ProgramStageDataElement extends BaseModel {
    private static final String CLASS_TAG = "ProgramStageDataElement";
    @JsonProperty("allowFutureDate")
    boolean allowFutureDate;
    @JsonProperty("allowProvidedElsewhere")
    boolean allowProvidedElsewhere;
    @JsonProperty("compulsory")
    boolean compulsory;
    String dataElement;
    DataElement dataElementObj;
    @JsonProperty("displayInReports")
    boolean displayInReports;
    String programStage;
    String programStageSection;
    @JsonProperty("renderOptionsAsRadio")
    boolean renderOptionsAsRadio;
    @JsonProperty("sortOrder")
    int sortOrder;

    public final class Adapter extends ModelAdapter<ProgramStageDataElement> {
        public Class<ProgramStageDataElement> getModelClass() {
            return ProgramStageDataElement.class;
        }

        public String getTableName() {
            return "ProgramStageDataElement";
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `ProgramStageDataElement` (`PROGRAMSTAGE`, `DATAELEMENT`, `ALLOWFUTUREDATE`, `SORTORDER`, `DISPLAYINREPORTS`, `ALLOWPROVIDEDELSEWHERE`, `COMPULSORY`, `PROGRAMSTAGESECTION`, `RENDEROPTIONSASRADIO`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, ProgramStageDataElement model) {
            if (model.programStage != null) {
                statement.bindString(1, model.programStage);
            } else {
                statement.bindNull(1);
            }
            if (model.dataElement != null) {
                statement.bindString(2, model.dataElement);
            } else {
                statement.bindNull(2);
            }
            Object modelallowFutureDate = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.allowFutureDate));
            if (modelallowFutureDate != null) {
                statement.bindLong(3, (long) ((Integer) modelallowFutureDate).intValue());
            } else {
                statement.bindNull(3);
            }
            statement.bindLong(4, (long) model.sortOrder);
            Object modeldisplayInReports = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.displayInReports));
            if (modeldisplayInReports != null) {
                statement.bindLong(5, (long) ((Integer) modeldisplayInReports).intValue());
            } else {
                statement.bindNull(5);
            }
            Object modelallowProvidedElsewhere = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.allowProvidedElsewhere));
            if (modelallowProvidedElsewhere != null) {
                statement.bindLong(6, (long) ((Integer) modelallowProvidedElsewhere).intValue());
            } else {
                statement.bindNull(6);
            }
            Object modelcompulsory = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.compulsory));
            if (modelcompulsory != null) {
                statement.bindLong(7, (long) ((Integer) modelcompulsory).intValue());
            } else {
                statement.bindNull(7);
            }
            if (model.programStageSection != null) {
                statement.bindString(8, model.programStageSection);
            } else {
                statement.bindNull(8);
            }
            Object modelrenderOptionsAsRadio = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.renderOptionsAsRadio));
            if (modelrenderOptionsAsRadio != null) {
                statement.bindLong(9, (long) ((Integer) modelrenderOptionsAsRadio).intValue());
            } else {
                statement.bindNull(9);
            }
        }

        public void bindToContentValues(ContentValues contentValues, ProgramStageDataElement model) {
            if (model.programStage != null) {
                contentValues.put("programStage", model.programStage);
            } else {
                contentValues.putNull("programStage");
            }
            if (model.dataElement != null) {
                contentValues.put("dataElement", model.dataElement);
            } else {
                contentValues.putNull("dataElement");
            }
            Object modelallowFutureDate = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.allowFutureDate));
            if (modelallowFutureDate != null) {
                contentValues.put("allowFutureDate", (Integer) modelallowFutureDate);
            } else {
                contentValues.putNull("allowFutureDate");
            }
            contentValues.put("sortOrder", Integer.valueOf(model.sortOrder));
            Object modeldisplayInReports = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.displayInReports));
            if (modeldisplayInReports != null) {
                contentValues.put(Table.DISPLAYINREPORTS, (Integer) modeldisplayInReports);
            } else {
                contentValues.putNull(Table.DISPLAYINREPORTS);
            }
            Object modelallowProvidedElsewhere = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.allowProvidedElsewhere));
            if (modelallowProvidedElsewhere != null) {
                contentValues.put(Table.ALLOWPROVIDEDELSEWHERE, (Integer) modelallowProvidedElsewhere);
            } else {
                contentValues.putNull(Table.ALLOWPROVIDEDELSEWHERE);
            }
            Object modelcompulsory = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.compulsory));
            if (modelcompulsory != null) {
                contentValues.put(Table.COMPULSORY, (Integer) modelcompulsory);
            } else {
                contentValues.putNull(Table.COMPULSORY);
            }
            if (model.programStageSection != null) {
                contentValues.put("programStageSection", model.programStageSection);
            } else {
                contentValues.putNull("programStageSection");
            }
            Object modelrenderOptionsAsRadio = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.renderOptionsAsRadio));
            if (modelrenderOptionsAsRadio != null) {
                contentValues.put("renderOptionsAsRadio", (Integer) modelrenderOptionsAsRadio);
            } else {
                contentValues.putNull("renderOptionsAsRadio");
            }
        }

        public void bindToInsertValues(ContentValues contentValues, ProgramStageDataElement model) {
            if (model.programStage != null) {
                contentValues.put("programStage", model.programStage);
            } else {
                contentValues.putNull("programStage");
            }
            if (model.dataElement != null) {
                contentValues.put("dataElement", model.dataElement);
            } else {
                contentValues.putNull("dataElement");
            }
            Object modelallowFutureDate = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.allowFutureDate));
            if (modelallowFutureDate != null) {
                contentValues.put("allowFutureDate", (Integer) modelallowFutureDate);
            } else {
                contentValues.putNull("allowFutureDate");
            }
            contentValues.put("sortOrder", Integer.valueOf(model.sortOrder));
            Object modeldisplayInReports = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.displayInReports));
            if (modeldisplayInReports != null) {
                contentValues.put(Table.DISPLAYINREPORTS, (Integer) modeldisplayInReports);
            } else {
                contentValues.putNull(Table.DISPLAYINREPORTS);
            }
            Object modelallowProvidedElsewhere = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.allowProvidedElsewhere));
            if (modelallowProvidedElsewhere != null) {
                contentValues.put(Table.ALLOWPROVIDEDELSEWHERE, (Integer) modelallowProvidedElsewhere);
            } else {
                contentValues.putNull(Table.ALLOWPROVIDEDELSEWHERE);
            }
            Object modelcompulsory = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.compulsory));
            if (modelcompulsory != null) {
                contentValues.put(Table.COMPULSORY, (Integer) modelcompulsory);
            } else {
                contentValues.putNull(Table.COMPULSORY);
            }
            if (model.programStageSection != null) {
                contentValues.put("programStageSection", model.programStageSection);
            } else {
                contentValues.putNull("programStageSection");
            }
            Object modelrenderOptionsAsRadio = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.renderOptionsAsRadio));
            if (modelrenderOptionsAsRadio != null) {
                contentValues.put("renderOptionsAsRadio", (Integer) modelrenderOptionsAsRadio);
            } else {
                contentValues.putNull("renderOptionsAsRadio");
            }
        }

        public boolean exists(ProgramStageDataElement model) {
            return new Select().from(ProgramStageDataElement.class).where(getPrimaryModelWhere(model)).hasData();
        }

        public void loadFromCursor(Cursor cursor, ProgramStageDataElement model) {
            int indexprogramStage = cursor.getColumnIndex("programStage");
            if (indexprogramStage != -1) {
                if (cursor.isNull(indexprogramStage)) {
                    model.programStage = null;
                } else {
                    model.programStage = cursor.getString(indexprogramStage);
                }
            }
            int indexdataElement = cursor.getColumnIndex("dataElement");
            if (indexdataElement != -1) {
                if (cursor.isNull(indexdataElement)) {
                    model.dataElement = null;
                } else {
                    model.dataElement = cursor.getString(indexdataElement);
                }
            }
            int indexallowFutureDate = cursor.getColumnIndex("allowFutureDate");
            if (indexallowFutureDate != -1) {
                model.allowFutureDate = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexallowFutureDate)))).booleanValue();
            }
            int indexsortOrder = cursor.getColumnIndex("sortOrder");
            if (indexsortOrder != -1) {
                model.sortOrder = cursor.getInt(indexsortOrder);
            }
            int indexdisplayInReports = cursor.getColumnIndex(Table.DISPLAYINREPORTS);
            if (indexdisplayInReports != -1) {
                model.displayInReports = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexdisplayInReports)))).booleanValue();
            }
            int indexallowProvidedElsewhere = cursor.getColumnIndex(Table.ALLOWPROVIDEDELSEWHERE);
            if (indexallowProvidedElsewhere != -1) {
                model.allowProvidedElsewhere = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexallowProvidedElsewhere)))).booleanValue();
            }
            int indexcompulsory = cursor.getColumnIndex(Table.COMPULSORY);
            if (indexcompulsory != -1) {
                model.compulsory = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexcompulsory)))).booleanValue();
            }
            int indexprogramStageSection = cursor.getColumnIndex("programStageSection");
            if (indexprogramStageSection != -1) {
                if (cursor.isNull(indexprogramStageSection)) {
                    model.programStageSection = null;
                } else {
                    model.programStageSection = cursor.getString(indexprogramStageSection);
                }
            }
            int indexrenderOptionsAsRadio = cursor.getColumnIndex("renderOptionsAsRadio");
            if (indexrenderOptionsAsRadio != -1) {
                model.renderOptionsAsRadio = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexrenderOptionsAsRadio)))).booleanValue();
            }
        }

        public ConditionQueryBuilder<ProgramStageDataElement> getPrimaryModelWhere(ProgramStageDataElement model) {
            return new ConditionQueryBuilder(ProgramStageDataElement.class, Condition.column("programStage").is(model.programStage), Condition.column("dataElement").is(model.dataElement));
        }

        public ConditionQueryBuilder<ProgramStageDataElement> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(ProgramStageDataElement.class, Condition.column("programStage").is(Operation.EMPTY_PARAM), Condition.column("dataElement").is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return "CREATE TABLE IF NOT EXISTS `ProgramStageDataElement`(`programStage` TEXT, `dataElement` TEXT, `allowFutureDate` INTEGER, `sortOrder` INTEGER, `displayInReports` INTEGER, `allowProvidedElsewhere` INTEGER, `compulsory` INTEGER, `programStageSection` TEXT, `renderOptionsAsRadio` INTEGER, PRIMARY KEY(`programStage`, `dataElement`));";
        }

        public final ProgramStageDataElement newInstance() {
            return new ProgramStageDataElement();
        }
    }

    public final class Table {
        public static final String ALLOWFUTUREDATE = "allowFutureDate";
        public static final String ALLOWPROVIDEDELSEWHERE = "allowProvidedElsewhere";
        public static final String COMPULSORY = "compulsory";
        public static final String DATAELEMENT = "dataElement";
        public static final String DISPLAYINREPORTS = "displayInReports";
        public static final String PROGRAMSTAGE = "programStage";
        public static final String PROGRAMSTAGESECTION = "programStageSection";
        public static final String RENDEROPTIONSASRADIO = "renderOptionsAsRadio";
        public static final String SORTORDER = "sortOrder";
        public static final String TABLE_NAME = "ProgramStageDataElement";
    }

    @JsonAnySetter
    public void handleUnknown(String key, Object value) {
    }

    @JsonProperty("programStage")
    public void setProgramStage(Map<String, Object> programStage) {
        this.programStage = (String) programStage.get("id");
    }

    @JsonProperty("dataElement")
    public void setDataElement(DataElement dataElement) {
        this.dataElement = dataElement.id;
        dataElement.save();
        this.dataElementObj = dataElement;
    }

    public boolean getAllowFutureDate() {
        return this.allowFutureDate;
    }

    public void setAllowFutureDate(boolean allowFutureDate) {
        this.allowFutureDate = allowFutureDate;
    }

    public int getSortOrder() {
        return this.sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public boolean getAllowProvidedElsewhere() {
        return this.allowProvidedElsewhere;
    }

    public boolean getCompulsory() {
        return this.compulsory;
    }

    public void setCompulsory(boolean compulsory) {
        this.compulsory = compulsory;
    }

    public String getProgramStage() {
        return this.programStage;
    }

    public void setProgramStage(String programStage) {
        this.programStage = programStage;
    }

    public DataElement getDataElement() {
        return MetaDataController.getDataElement(this.dataElement);
    }

    public void setDataElement(String dataElement) {
        this.dataElement = dataElement;
    }

    public String getProgramStageSection() {
        return this.programStageSection;
    }

    public void setProgramStageSection(String programStageSection) {
        this.programStageSection = programStageSection;
    }

    public boolean getDisplayInReports() {
        return this.displayInReports;
    }

    public void setDisplayInReports(boolean displayInReports) {
        this.displayInReports = displayInReports;
    }

    public boolean isAllowProvidedElsewhere() {
        return this.allowProvidedElsewhere;
    }

    public void setAllowProvidedElsewhere(boolean allowProvidedElsewhere) {
        this.allowProvidedElsewhere = allowProvidedElsewhere;
    }

    public String getDataelement() {
        return this.dataElement;
    }

    public DataElement getDataElementObj() {
        return this.dataElementObj;
    }

    public boolean isRenderOptionsAsRadio() {
        return this.renderOptionsAsRadio;
    }

    public void setRenderOptionsAsRadio(boolean renderOptionsAsRadio) {
        this.renderOptionsAsRadio = renderOptionsAsRadio;
    }
}
