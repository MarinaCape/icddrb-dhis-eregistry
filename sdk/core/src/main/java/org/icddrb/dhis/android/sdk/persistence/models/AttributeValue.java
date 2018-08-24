package org.icddrb.dhis.android.sdk.persistence.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.builder.Condition.Operation;
import com.raizlabs.android.dbflow.sql.builder.ConditionQueryBuilder;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import java.util.Map;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;

public class AttributeValue extends BaseModel {
    String attribute;
    Attribute attributeObj;
    @JsonProperty("created")
    String created;
    String dataElement;
    long id;
    @JsonProperty("lastUpdated")
    String lastUpdated;
    @JsonProperty("value")
    String value;

    public final class Adapter extends ModelAdapter<AttributeValue> {
        public Class<AttributeValue> getModelClass() {
            return AttributeValue.class;
        }

        public String getTableName() {
            return Table.TABLE_NAME;
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `AttributeValue` (`DATAELEMENT`, `ATTRIBUTEID`, `VALUE`, `CREATED`, `LASTUPDATED`) VALUES (?, ?, ?, ?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, AttributeValue model) {
            if (model.dataElement != null) {
                statement.bindString(1, model.dataElement);
            } else {
                statement.bindNull(1);
            }
            if (model.attribute != null) {
                statement.bindString(2, model.attribute);
            } else {
                statement.bindNull(2);
            }
            if (model.value != null) {
                statement.bindString(3, model.value);
            } else {
                statement.bindNull(3);
            }
            if (model.created != null) {
                statement.bindString(4, model.created);
            } else {
                statement.bindNull(4);
            }
            if (model.lastUpdated != null) {
                statement.bindString(5, model.lastUpdated);
            } else {
                statement.bindNull(5);
            }
        }

        public void bindToContentValues(ContentValues contentValues, AttributeValue model) {
            contentValues.put("id", Long.valueOf(model.id));
            if (model.dataElement != null) {
                contentValues.put("dataElement", model.dataElement);
            } else {
                contentValues.putNull("dataElement");
            }
            if (model.attribute != null) {
                contentValues.put(Table.ATTRIBUTEID, model.attribute);
            } else {
                contentValues.putNull(Table.ATTRIBUTEID);
            }
            if (model.value != null) {
                contentValues.put("value", model.value);
            } else {
                contentValues.putNull("value");
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
        }

        public void bindToInsertValues(ContentValues contentValues, AttributeValue model) {
            if (model.dataElement != null) {
                contentValues.put("dataElement", model.dataElement);
            } else {
                contentValues.putNull("dataElement");
            }
            if (model.attribute != null) {
                contentValues.put(Table.ATTRIBUTEID, model.attribute);
            } else {
                contentValues.putNull(Table.ATTRIBUTEID);
            }
            if (model.value != null) {
                contentValues.put("value", model.value);
            } else {
                contentValues.putNull("value");
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
        }

        public boolean exists(AttributeValue model) {
            return model.id > 0;
        }

        public void loadFromCursor(Cursor cursor, AttributeValue model) {
            int indexid = cursor.getColumnIndex("id");
            if (indexid != -1) {
                model.id = cursor.getLong(indexid);
            }
            int indexdataElement = cursor.getColumnIndex("dataElement");
            if (indexdataElement != -1) {
                if (cursor.isNull(indexdataElement)) {
                    model.dataElement = null;
                } else {
                    model.dataElement = cursor.getString(indexdataElement);
                }
            }
            int indexattributeId = cursor.getColumnIndex(Table.ATTRIBUTEID);
            if (indexattributeId != -1) {
                if (cursor.isNull(indexattributeId)) {
                    model.attribute = null;
                } else {
                    model.attribute = cursor.getString(indexattributeId);
                }
            }
            int indexvalue = cursor.getColumnIndex("value");
            if (indexvalue != -1) {
                if (cursor.isNull(indexvalue)) {
                    model.value = null;
                } else {
                    model.value = cursor.getString(indexvalue);
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
            if (indexlastUpdated == -1) {
                return;
            }
            if (cursor.isNull(indexlastUpdated)) {
                model.lastUpdated = null;
            } else {
                model.lastUpdated = cursor.getString(indexlastUpdated);
            }
        }

        public void updateAutoIncrement(AttributeValue model, long id) {
            model.id = id;
        }

        public long getAutoIncrementingId(AttributeValue model) {
            return model.id;
        }

        public String getAutoIncrementingColumnName() {
            return "id";
        }

        public ConditionQueryBuilder<AttributeValue> getPrimaryModelWhere(AttributeValue model) {
            return new ConditionQueryBuilder(AttributeValue.class, Condition.column("id").is(Long.valueOf(model.id)));
        }

        public ConditionQueryBuilder<AttributeValue> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(AttributeValue.class, Condition.column("id").is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return "CREATE TABLE IF NOT EXISTS `AttributeValue`(`id` INTEGER PRIMARY KEY AUTOINCREMENT, `dataElement` TEXT, `attributeId` TEXT, `value` TEXT, `created` TEXT, `lastUpdated` TEXT);";
        }

        public final AttributeValue newInstance() {
            return new AttributeValue();
        }
    }

    public final class Table {
        public static final String ATTRIBUTEID = "attributeId";
        public static final String CREATED = "created";
        public static final String DATAELEMENT = "dataElement";
        public static final String ID = "id";
        public static final String LASTUPDATED = "lastUpdated";
        public static final String TABLE_NAME = "AttributeValue";
        public static final String VALUE = "value";
    }

    public Attribute getAttribute() {
        return MetaDataController.getAttribute(this.attribute);
    }

    @JsonProperty("attribute")
    public void setAttribute(Attribute attributeObj) {
        this.attribute = attributeObj.getUid();
        this.attributeObj = attributeObj;
    }

    public void setAttribute(String attributeId) {
        this.attribute = attributeId;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCreated() {
        return this.created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getLastUpdated() {
        return this.lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty("dataElement")
    public void setDataElement(Map<String, Object> dataElement) {
        this.dataElement = (String) dataElement.get("id");
    }

    public DataElement getDataElement() {
        return MetaDataController.getDataElement(this.dataElement);
    }

    public void setDataElement(String dataElement) {
        this.dataElement = dataElement;
    }

    public Attribute getAttributeObj() {
        return this.attributeObj;
    }

    public String getAttributeId() {
        return this.attribute;
    }
}
