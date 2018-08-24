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

public class Option extends BaseMetaDataObject {
    @JsonProperty("code")
    String code;
    String optionSet;
    @JsonIgnore
    int sortIndex;

    public final class Adapter extends ModelAdapter<Option> {
        public Class<Option> getModelClass() {
            return Option.class;
        }

        public String getTableName() {
            return Table.TABLE_NAME;
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `Option` (`NAME`, `DISPLAYNAME`, `CREATED`, `LASTUPDATED`, `ACCESS`, `ID`, `SORTINDEX`, `OPTIONSET`, `CODE`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, Option model) {
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
            statement.bindLong(7, (long) model.sortIndex);
            if (model.optionSet != null) {
                statement.bindString(8, model.optionSet);
            } else {
                statement.bindNull(8);
            }
            if (model.code != null) {
                statement.bindString(9, model.code);
            } else {
                statement.bindNull(9);
            }
        }

        public void bindToContentValues(ContentValues contentValues, Option model) {
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
            contentValues.put(Table.SORTINDEX, Integer.valueOf(model.sortIndex));
            if (model.optionSet != null) {
                contentValues.put("optionSet", model.optionSet);
            } else {
                contentValues.putNull("optionSet");
            }
            if (model.code != null) {
                contentValues.put("code", model.code);
            } else {
                contentValues.putNull("code");
            }
        }

        public void bindToInsertValues(ContentValues contentValues, Option model) {
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
            contentValues.put(Table.SORTINDEX, Integer.valueOf(model.sortIndex));
            if (model.optionSet != null) {
                contentValues.put("optionSet", model.optionSet);
            } else {
                contentValues.putNull("optionSet");
            }
            if (model.code != null) {
                contentValues.put("code", model.code);
            } else {
                contentValues.putNull("code");
            }
        }

        public boolean exists(Option model) {
            return new Select().from(Option.class).where(getPrimaryModelWhere(model)).hasData();
        }

        public void loadFromCursor(Cursor cursor, Option model) {
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
            int indexsortIndex = cursor.getColumnIndex(Table.SORTINDEX);
            if (indexsortIndex != -1) {
                model.sortIndex = cursor.getInt(indexsortIndex);
            }
            int indexoptionSet = cursor.getColumnIndex("optionSet");
            if (indexoptionSet != -1) {
                if (cursor.isNull(indexoptionSet)) {
                    model.optionSet = null;
                } else {
                    model.optionSet = cursor.getString(indexoptionSet);
                }
            }
            int indexcode = cursor.getColumnIndex("code");
            if (indexcode == -1) {
                return;
            }
            if (cursor.isNull(indexcode)) {
                model.code = null;
            } else {
                model.code = cursor.getString(indexcode);
            }
        }

        public ConditionQueryBuilder<Option> getPrimaryModelWhere(Option model) {
            return new ConditionQueryBuilder(Option.class, Condition.column("id").is(model.id));
        }

        public ConditionQueryBuilder<Option> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(Option.class, Condition.column("id").is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return "CREATE TABLE IF NOT EXISTS `Option`(`name` TEXT, `displayName` TEXT, `created` TEXT, `lastUpdated` TEXT, `access` TEXT, `id` TEXT, `sortIndex` INTEGER, `optionSet` TEXT, `code` TEXT, PRIMARY KEY(`id`));";
        }

        public final Option newInstance() {
            return new Option();
        }
    }

    public final class Table {
        public static final String ACCESS = "access";
        public static final String CODE = "code";
        public static final String CREATED = "created";
        public static final String DISPLAYNAME = "displayName";
        public static final String ID = "id";
        public static final String LASTUPDATED = "lastUpdated";
        public static final String NAME = "name";
        public static final String OPTIONSET = "optionSet";
        public static final String SORTINDEX = "sortIndex";
        public static final String TABLE_NAME = "Option";
    }

    @JsonProperty("optionSet")
    public void handleOptionSets(OptionSet optionSet) {
        setOptionSet(optionSet.getUid());
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOptionSet() {
        return this.optionSet;
    }

    public void setOptionSet(String optionSet) {
        this.optionSet = optionSet;
    }

    public int getSortIndex() {
        return this.sortIndex;
    }

    public void setSortIndex(int sortIndex) {
        this.sortIndex = sortIndex;
    }
}
