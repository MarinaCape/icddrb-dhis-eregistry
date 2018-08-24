package org.icddrb.dhis.android.sdk.persistence.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.builder.Condition.Operation;
import com.raizlabs.android.dbflow.sql.builder.ConditionQueryBuilder;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import java.util.List;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.utils.api.ValueType;

public class OptionSet extends BaseMetaDataObject {
    @JsonProperty("options")
    List<Option> options;
    @JsonProperty("valueType")
    ValueType valueType;
    @JsonProperty("version")
    int version;

    public final class Adapter extends ModelAdapter<OptionSet> {
        public Class<OptionSet> getModelClass() {
            return OptionSet.class;
        }

        public String getTableName() {
            return Table.TABLE_NAME;
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `OptionSet` (`NAME`, `DISPLAYNAME`, `CREATED`, `LASTUPDATED`, `ACCESS`, `ID`, `VERSION`, `VALUETYPE`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, OptionSet model) {
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
            statement.bindLong(7, (long) model.version);
            ValueType modelvalueType = model.valueType;
            if (modelvalueType != null) {
                statement.bindString(8, modelvalueType.name());
            } else {
                statement.bindNull(8);
            }
        }

        public void bindToContentValues(ContentValues contentValues, OptionSet model) {
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
            contentValues.put("version", Integer.valueOf(model.version));
            ValueType modelvalueType = model.valueType;
            if (modelvalueType != null) {
                contentValues.put("valueType", modelvalueType.name());
            } else {
                contentValues.putNull("valueType");
            }
        }

        public void bindToInsertValues(ContentValues contentValues, OptionSet model) {
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
            contentValues.put("version", Integer.valueOf(model.version));
            ValueType modelvalueType = model.valueType;
            if (modelvalueType != null) {
                contentValues.put("valueType", modelvalueType.name());
            } else {
                contentValues.putNull("valueType");
            }
        }

        public boolean exists(OptionSet model) {
            return new Select().from(OptionSet.class).where(getPrimaryModelWhere(model)).hasData();
        }

        public void loadFromCursor(Cursor cursor, OptionSet model) {
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
            int indexversion = cursor.getColumnIndex("version");
            if (indexversion != -1) {
                model.version = cursor.getInt(indexversion);
            }
            int indexvalueType = cursor.getColumnIndex("valueType");
            if (indexvalueType == -1) {
                return;
            }
            if (cursor.isNull(indexvalueType)) {
                model.valueType = null;
            } else {
                model.valueType = ValueType.valueOf(cursor.getString(indexvalueType));
            }
        }

        public ConditionQueryBuilder<OptionSet> getPrimaryModelWhere(OptionSet model) {
            return new ConditionQueryBuilder(OptionSet.class, Condition.column("id").is(model.id));
        }

        public ConditionQueryBuilder<OptionSet> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(OptionSet.class, Condition.column("id").is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return "CREATE TABLE IF NOT EXISTS `OptionSet`(`name` TEXT, `displayName` TEXT, `created` TEXT, `lastUpdated` TEXT, `access` TEXT, `id` TEXT, `version` INTEGER, `valueType` TEXT, PRIMARY KEY(`id`));";
        }

        public final OptionSet newInstance() {
            return new OptionSet();
        }
    }

    public final class Table {
        public static final String ACCESS = "access";
        public static final String CREATED = "created";
        public static final String DISPLAYNAME = "displayName";
        public static final String ID = "id";
        public static final String LASTUPDATED = "lastUpdated";
        public static final String NAME = "name";
        public static final String TABLE_NAME = "OptionSet";
        public static final String VALUETYPE = "valueType";
        public static final String VERSION = "version";
    }

    @JsonIgnore
    public List<Option> getOptions() {
        if (this.options == null) {
            this.options = MetaDataController.getOptions(this.id);
        }
        return this.options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public ValueType getValueType() {
        return this.valueType;
    }

    public void setValueType(ValueType valueType) {
        this.valueType = valueType;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
