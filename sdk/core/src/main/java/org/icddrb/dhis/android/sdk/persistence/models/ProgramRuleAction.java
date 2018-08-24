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
import java.util.Map;
import org.icddrb.dhis.android.sdk.utils.api.ProgramRuleActionType;

public class ProgramRuleAction extends BaseMetaDataObject {
    String content;
    String data;
    String dataElement;
    boolean externalAccess;
    String location;
    String programRule;
    ProgramRuleActionType programRuleActionType;
    String programStage;
    String programStageSection;
    String trackedEntityAttribute;

    public final class Adapter extends ModelAdapter<ProgramRuleAction> {
        public Class<ProgramRuleAction> getModelClass() {
            return ProgramRuleAction.class;
        }

        public String getTableName() {
            return Table.TABLE_NAME;
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `ProgramRuleAction` (`NAME`, `DISPLAYNAME`, `CREATED`, `LASTUPDATED`, `ACCESS`, `ID`, `PROGRAMRULE`, `DATAELEMENT`, `TRACKEDENTITYATTRIBUTE`, `PROGRAMSTAGESECTION`, `PROGRAMSTAGE`, `PROGRAMRULEACTIONTYPE`, `EXTERNALACCESS`, `LOCATION`, `CONTENT`, `DATA`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, ProgramRuleAction model) {
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
            if (model.programRule != null) {
                statement.bindString(7, model.programRule);
            } else {
                statement.bindNull(7);
            }
            if (model.dataElement != null) {
                statement.bindString(8, model.dataElement);
            } else {
                statement.bindNull(8);
            }
            if (model.trackedEntityAttribute != null) {
                statement.bindString(9, model.trackedEntityAttribute);
            } else {
                statement.bindNull(9);
            }
            if (model.programStageSection != null) {
                statement.bindString(10, model.programStageSection);
            } else {
                statement.bindNull(10);
            }
            if (model.programStage != null) {
                statement.bindString(11, model.programStage);
            } else {
                statement.bindNull(11);
            }
            ProgramRuleActionType modelprogramRuleActionType = model.programRuleActionType;
            if (modelprogramRuleActionType != null) {
                statement.bindString(12, modelprogramRuleActionType.name());
            } else {
                statement.bindNull(12);
            }
            Object modelexternalAccess = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.externalAccess));
            if (modelexternalAccess != null) {
                statement.bindLong(13, (long) ((Integer) modelexternalAccess).intValue());
            } else {
                statement.bindNull(13);
            }
            if (model.location != null) {
                statement.bindString(14, model.location);
            } else {
                statement.bindNull(14);
            }
            if (model.content != null) {
                statement.bindString(15, model.content);
            } else {
                statement.bindNull(15);
            }
            if (model.data != null) {
                statement.bindString(16, model.data);
            } else {
                statement.bindNull(16);
            }
        }

        public void bindToContentValues(ContentValues contentValues, ProgramRuleAction model) {
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
            if (model.programRule != null) {
                contentValues.put(Table.PROGRAMRULE, model.programRule);
            } else {
                contentValues.putNull(Table.PROGRAMRULE);
            }
            if (model.dataElement != null) {
                contentValues.put("dataElement", model.dataElement);
            } else {
                contentValues.putNull("dataElement");
            }
            if (model.trackedEntityAttribute != null) {
                contentValues.put("trackedEntityAttribute", model.trackedEntityAttribute);
            } else {
                contentValues.putNull("trackedEntityAttribute");
            }
            if (model.programStageSection != null) {
                contentValues.put("programStageSection", model.programStageSection);
            } else {
                contentValues.putNull("programStageSection");
            }
            if (model.programStage != null) {
                contentValues.put("programStage", model.programStage);
            } else {
                contentValues.putNull("programStage");
            }
            ProgramRuleActionType modelprogramRuleActionType = model.programRuleActionType;
            if (modelprogramRuleActionType != null) {
                contentValues.put(Table.PROGRAMRULEACTIONTYPE, modelprogramRuleActionType.name());
            } else {
                contentValues.putNull(Table.PROGRAMRULEACTIONTYPE);
            }
            Object modelexternalAccess = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.externalAccess));
            if (modelexternalAccess != null) {
                contentValues.put("externalAccess", (Integer) modelexternalAccess);
            } else {
                contentValues.putNull("externalAccess");
            }
            if (model.location != null) {
                contentValues.put(Table.LOCATION, model.location);
            } else {
                contentValues.putNull(Table.LOCATION);
            }
            if (model.content != null) {
                contentValues.put(Table.CONTENT, model.content);
            } else {
                contentValues.putNull(Table.CONTENT);
            }
            if (model.data != null) {
                contentValues.put(Table.DATA, model.data);
            } else {
                contentValues.putNull(Table.DATA);
            }
        }

        public void bindToInsertValues(ContentValues contentValues, ProgramRuleAction model) {
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
            if (model.programRule != null) {
                contentValues.put(Table.PROGRAMRULE, model.programRule);
            } else {
                contentValues.putNull(Table.PROGRAMRULE);
            }
            if (model.dataElement != null) {
                contentValues.put("dataElement", model.dataElement);
            } else {
                contentValues.putNull("dataElement");
            }
            if (model.trackedEntityAttribute != null) {
                contentValues.put("trackedEntityAttribute", model.trackedEntityAttribute);
            } else {
                contentValues.putNull("trackedEntityAttribute");
            }
            if (model.programStageSection != null) {
                contentValues.put("programStageSection", model.programStageSection);
            } else {
                contentValues.putNull("programStageSection");
            }
            if (model.programStage != null) {
                contentValues.put("programStage", model.programStage);
            } else {
                contentValues.putNull("programStage");
            }
            ProgramRuleActionType modelprogramRuleActionType = model.programRuleActionType;
            if (modelprogramRuleActionType != null) {
                contentValues.put(Table.PROGRAMRULEACTIONTYPE, modelprogramRuleActionType.name());
            } else {
                contentValues.putNull(Table.PROGRAMRULEACTIONTYPE);
            }
            Object modelexternalAccess = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.externalAccess));
            if (modelexternalAccess != null) {
                contentValues.put("externalAccess", (Integer) modelexternalAccess);
            } else {
                contentValues.putNull("externalAccess");
            }
            if (model.location != null) {
                contentValues.put(Table.LOCATION, model.location);
            } else {
                contentValues.putNull(Table.LOCATION);
            }
            if (model.content != null) {
                contentValues.put(Table.CONTENT, model.content);
            } else {
                contentValues.putNull(Table.CONTENT);
            }
            if (model.data != null) {
                contentValues.put(Table.DATA, model.data);
            } else {
                contentValues.putNull(Table.DATA);
            }
        }

        public boolean exists(ProgramRuleAction model) {
            return new Select().from(ProgramRuleAction.class).where(getPrimaryModelWhere(model)).hasData();
        }

        public void loadFromCursor(Cursor cursor, ProgramRuleAction model) {
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
            int indexprogramRule = cursor.getColumnIndex(Table.PROGRAMRULE);
            if (indexprogramRule != -1) {
                if (cursor.isNull(indexprogramRule)) {
                    model.programRule = null;
                } else {
                    model.programRule = cursor.getString(indexprogramRule);
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
            int indextrackedEntityAttribute = cursor.getColumnIndex("trackedEntityAttribute");
            if (indextrackedEntityAttribute != -1) {
                if (cursor.isNull(indextrackedEntityAttribute)) {
                    model.trackedEntityAttribute = null;
                } else {
                    model.trackedEntityAttribute = cursor.getString(indextrackedEntityAttribute);
                }
            }
            int indexprogramStageSection = cursor.getColumnIndex("programStageSection");
            if (indexprogramStageSection != -1) {
                if (cursor.isNull(indexprogramStageSection)) {
                    model.programStageSection = null;
                } else {
                    model.programStageSection = cursor.getString(indexprogramStageSection);
                }
            }
            int indexprogramStage = cursor.getColumnIndex("programStage");
            if (indexprogramStage != -1) {
                if (cursor.isNull(indexprogramStage)) {
                    model.programStage = null;
                } else {
                    model.programStage = cursor.getString(indexprogramStage);
                }
            }
            int indexprogramRuleActionType = cursor.getColumnIndex(Table.PROGRAMRULEACTIONTYPE);
            if (indexprogramRuleActionType != -1) {
                if (cursor.isNull(indexprogramRuleActionType)) {
                    model.programRuleActionType = null;
                } else {
                    model.programRuleActionType = ProgramRuleActionType.valueOf(cursor.getString(indexprogramRuleActionType));
                }
            }
            int indexexternalAccess = cursor.getColumnIndex("externalAccess");
            if (indexexternalAccess != -1) {
                model.externalAccess = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexexternalAccess)))).booleanValue();
            }
            int indexlocation = cursor.getColumnIndex(Table.LOCATION);
            if (indexlocation != -1) {
                if (cursor.isNull(indexlocation)) {
                    model.location = null;
                } else {
                    model.location = cursor.getString(indexlocation);
                }
            }
            int indexcontent = cursor.getColumnIndex(Table.CONTENT);
            if (indexcontent != -1) {
                if (cursor.isNull(indexcontent)) {
                    model.content = null;
                } else {
                    model.content = cursor.getString(indexcontent);
                }
            }
            int indexdata = cursor.getColumnIndex(Table.DATA);
            if (indexdata == -1) {
                return;
            }
            if (cursor.isNull(indexdata)) {
                model.data = null;
            } else {
                model.data = cursor.getString(indexdata);
            }
        }

        public ConditionQueryBuilder<ProgramRuleAction> getPrimaryModelWhere(ProgramRuleAction model) {
            return new ConditionQueryBuilder(ProgramRuleAction.class, Condition.column("id").is(model.id));
        }

        public ConditionQueryBuilder<ProgramRuleAction> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(ProgramRuleAction.class, Condition.column("id").is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return "CREATE TABLE IF NOT EXISTS `ProgramRuleAction`(`name` TEXT, `displayName` TEXT, `created` TEXT, `lastUpdated` TEXT, `access` TEXT, `id` TEXT, `programRule` TEXT, `dataElement` TEXT, `trackedEntityAttribute` TEXT, `programStageSection` TEXT, `programStage` TEXT, `programRuleActionType` TEXT, `externalAccess` INTEGER, `location` TEXT, `content` TEXT, `data` TEXT, PRIMARY KEY(`id`));";
        }

        public final ProgramRuleAction newInstance() {
            return new ProgramRuleAction();
        }
    }

    public final class Table {
        public static final String ACCESS = "access";
        public static final String CONTENT = "content";
        public static final String CREATED = "created";
        public static final String DATA = "data";
        public static final String DATAELEMENT = "dataElement";
        public static final String DISPLAYNAME = "displayName";
        public static final String EXTERNALACCESS = "externalAccess";
        public static final String ID = "id";
        public static final String LASTUPDATED = "lastUpdated";
        public static final String LOCATION = "location";
        public static final String NAME = "name";
        public static final String PROGRAMRULE = "programRule";
        public static final String PROGRAMRULEACTIONTYPE = "programRuleActionType";
        public static final String PROGRAMSTAGE = "programStage";
        public static final String PROGRAMSTAGESECTION = "programStageSection";
        public static final String TABLE_NAME = "ProgramRuleAction";
        public static final String TRACKEDENTITYATTRIBUTE = "trackedEntityAttribute";
    }

    @JsonProperty("programRule")
    public void setProgramRule(Map<String, Object> programRule) {
        this.programRule = (String) programRule.get("id");
    }

    @JsonProperty("dataElement")
    public void setDataElement(Map<String, Object> dataElement) {
        this.dataElement = (String) dataElement.get("id");
    }

    @JsonProperty("trackedEntityAttribute")
    public void setTrackedEntityAttribute(Map<String, Object> trackedEntityAttribute) {
        this.trackedEntityAttribute = (String) trackedEntityAttribute.get("id");
    }

    @JsonProperty("programStageSection")
    public void setProgramStageSection(Map<String, Object> programStageSection) {
        this.programStageSection = (String) programStageSection.get("id");
    }

    @JsonProperty("programStage")
    public void setProgramStage(Map<String, Object> programStage) {
        this.programStage = (String) programStage.get("id");
    }

    public String getProgramStageSection() {
        return this.programStageSection;
    }

    public void setProgramStageSection(String programStageSection) {
        this.programStageSection = programStageSection;
    }

    public String getTrackedEntityAttribute() {
        return this.trackedEntityAttribute;
    }

    public void setTrackedEntityAttribute(String trackedEntityAttribute) {
        this.trackedEntityAttribute = trackedEntityAttribute;
    }

    public String getDataElement() {
        return this.dataElement;
    }

    public void setDataElement(String dataElement) {
        this.dataElement = dataElement;
    }

    public String getProgramRule() {
        return this.programRule;
    }

    public void setProgramRule(String programRule) {
        this.programRule = programRule;
    }

    public boolean getExternalAccess() {
        return this.externalAccess;
    }

    public ProgramRuleActionType getProgramRuleActionType() {
        return this.programRuleActionType;
    }

    public void setProgramRuleActionType(ProgramRuleActionType programRuleActionType) {
        this.programRuleActionType = programRuleActionType;
    }

    public boolean isExternalAccess() {
        return this.externalAccess;
    }

    public void setExternalAccess(boolean externalAccess) {
        this.externalAccess = externalAccess;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getProgramStage() {
        return this.programStage;
    }
}
