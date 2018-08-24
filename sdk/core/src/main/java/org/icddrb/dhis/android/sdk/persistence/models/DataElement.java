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
import org.icddrb.dhis.android.sdk.utils.api.ValueType;

public class DataElement extends BaseNameableObject {
    @JsonProperty("aggregationOperator")
    String aggregationOperator;
    @JsonProperty("attributeValues")
    List<AttributeValue> attributeValues;
    @JsonProperty("code")
    String code;
    @JsonProperty("dimension")
    String dimension;
    @JsonProperty("displayFormName")
    String displayFormName;
    @JsonProperty("domainType")
    String domainType;
    @JsonProperty("externalAccess")
    boolean externalAccess;
    @JsonProperty("formName")
    String formName;
    @JsonProperty("numberType")
    String numberType;
    String optionSet;
    @JsonProperty("optionSetValue")
    boolean optionSetValue;
    @JsonProperty("valueType")
    ValueType valueType;
    @JsonProperty("zeroIsSignificant")
    boolean zeroIsSignificant;

    public final class Adapter extends ModelAdapter<DataElement> {
        public Class<DataElement> getModelClass() {
            return DataElement.class;
        }

        public String getTableName() {
            return Table.TABLE_NAME;
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `DataElement` (`NAME`, `DISPLAYNAME`, `CREATED`, `LASTUPDATED`, `ACCESS`, `ID`, `SHORTNAME`, `DESCRIPTION`, `VALUETYPE`, `OPTIONSETVALUE`, `ZEROISSIGNIFICANT`, `EXTERNALACCESS`, `AGGREGATIONOPERATOR`, `FORMNAME`, `CODE`, `NUMBERTYPE`, `DOMAINTYPE`, `DIMENSION`, `DISPLAYFORMNAME`, `OPTIONSET`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, DataElement model) {
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
            if (model.shortName != null) {
                statement.bindString(7, model.shortName);
            } else {
                statement.bindNull(7);
            }
            if (model.description != null) {
                statement.bindString(8, model.description);
            } else {
                statement.bindNull(8);
            }
            ValueType modelvalueType = model.valueType;
            if (modelvalueType != null) {
                statement.bindString(9, modelvalueType.name());
            } else {
                statement.bindNull(9);
            }
            Object modeloptionSetValue = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.optionSetValue));
            if (modeloptionSetValue != null) {
                statement.bindLong(10, (long) ((Integer) modeloptionSetValue).intValue());
            } else {
                statement.bindNull(10);
            }
            Object modelzeroIsSignificant = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.zeroIsSignificant));
            if (modelzeroIsSignificant != null) {
                statement.bindLong(11, (long) ((Integer) modelzeroIsSignificant).intValue());
            } else {
                statement.bindNull(11);
            }
            Object modelexternalAccess = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.externalAccess));
            if (modelexternalAccess != null) {
                statement.bindLong(12, (long) ((Integer) modelexternalAccess).intValue());
            } else {
                statement.bindNull(12);
            }
            if (model.aggregationOperator != null) {
                statement.bindString(13, model.aggregationOperator);
            } else {
                statement.bindNull(13);
            }
            if (model.formName != null) {
                statement.bindString(14, model.formName);
            } else {
                statement.bindNull(14);
            }
            if (model.code != null) {
                statement.bindString(15, model.code);
            } else {
                statement.bindNull(15);
            }
            if (model.numberType != null) {
                statement.bindString(16, model.numberType);
            } else {
                statement.bindNull(16);
            }
            if (model.domainType != null) {
                statement.bindString(17, model.domainType);
            } else {
                statement.bindNull(17);
            }
            if (model.dimension != null) {
                statement.bindString(18, model.dimension);
            } else {
                statement.bindNull(18);
            }
            if (model.displayFormName != null) {
                statement.bindString(19, model.displayFormName);
            } else {
                statement.bindNull(19);
            }
            if (model.optionSet != null) {
                statement.bindString(20, model.optionSet);
            } else {
                statement.bindNull(20);
            }
        }

        public void bindToContentValues(ContentValues contentValues, DataElement model) {
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
            if (model.shortName != null) {
                contentValues.put("shortName", model.shortName);
            } else {
                contentValues.putNull("shortName");
            }
            if (model.description != null) {
                contentValues.put("description", model.description);
            } else {
                contentValues.putNull("description");
            }
            ValueType modelvalueType = model.valueType;
            if (modelvalueType != null) {
                contentValues.put("valueType", modelvalueType.name());
            } else {
                contentValues.putNull("valueType");
            }
            Object modeloptionSetValue = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.optionSetValue));
            if (modeloptionSetValue != null) {
                contentValues.put("optionSetValue", (Integer) modeloptionSetValue);
            } else {
                contentValues.putNull("optionSetValue");
            }
            Object modelzeroIsSignificant = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.zeroIsSignificant));
            if (modelzeroIsSignificant != null) {
                contentValues.put(Table.ZEROISSIGNIFICANT, (Integer) modelzeroIsSignificant);
            } else {
                contentValues.putNull(Table.ZEROISSIGNIFICANT);
            }
            Object modelexternalAccess = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.externalAccess));
            if (modelexternalAccess != null) {
                contentValues.put("externalAccess", (Integer) modelexternalAccess);
            } else {
                contentValues.putNull("externalAccess");
            }
            if (model.aggregationOperator != null) {
                contentValues.put(Table.AGGREGATIONOPERATOR, model.aggregationOperator);
            } else {
                contentValues.putNull(Table.AGGREGATIONOPERATOR);
            }
            if (model.formName != null) {
                contentValues.put(Table.FORMNAME, model.formName);
            } else {
                contentValues.putNull(Table.FORMNAME);
            }
            if (model.code != null) {
                contentValues.put("code", model.code);
            } else {
                contentValues.putNull("code");
            }
            if (model.numberType != null) {
                contentValues.put(Table.NUMBERTYPE, model.numberType);
            } else {
                contentValues.putNull(Table.NUMBERTYPE);
            }
            if (model.domainType != null) {
                contentValues.put(Table.DOMAINTYPE, model.domainType);
            } else {
                contentValues.putNull(Table.DOMAINTYPE);
            }
            if (model.dimension != null) {
                contentValues.put("dimension", model.dimension);
            } else {
                contentValues.putNull("dimension");
            }
            if (model.displayFormName != null) {
                contentValues.put(Table.DISPLAYFORMNAME, model.displayFormName);
            } else {
                contentValues.putNull(Table.DISPLAYFORMNAME);
            }
            if (model.optionSet != null) {
                contentValues.put("optionSet", model.optionSet);
            } else {
                contentValues.putNull("optionSet");
            }
        }

        public void bindToInsertValues(ContentValues contentValues, DataElement model) {
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
            if (model.shortName != null) {
                contentValues.put("shortName", model.shortName);
            } else {
                contentValues.putNull("shortName");
            }
            if (model.description != null) {
                contentValues.put("description", model.description);
            } else {
                contentValues.putNull("description");
            }
            ValueType modelvalueType = model.valueType;
            if (modelvalueType != null) {
                contentValues.put("valueType", modelvalueType.name());
            } else {
                contentValues.putNull("valueType");
            }
            Object modeloptionSetValue = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.optionSetValue));
            if (modeloptionSetValue != null) {
                contentValues.put("optionSetValue", (Integer) modeloptionSetValue);
            } else {
                contentValues.putNull("optionSetValue");
            }
            Object modelzeroIsSignificant = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.zeroIsSignificant));
            if (modelzeroIsSignificant != null) {
                contentValues.put(Table.ZEROISSIGNIFICANT, (Integer) modelzeroIsSignificant);
            } else {
                contentValues.putNull(Table.ZEROISSIGNIFICANT);
            }
            Object modelexternalAccess = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.externalAccess));
            if (modelexternalAccess != null) {
                contentValues.put("externalAccess", (Integer) modelexternalAccess);
            } else {
                contentValues.putNull("externalAccess");
            }
            if (model.aggregationOperator != null) {
                contentValues.put(Table.AGGREGATIONOPERATOR, model.aggregationOperator);
            } else {
                contentValues.putNull(Table.AGGREGATIONOPERATOR);
            }
            if (model.formName != null) {
                contentValues.put(Table.FORMNAME, model.formName);
            } else {
                contentValues.putNull(Table.FORMNAME);
            }
            if (model.code != null) {
                contentValues.put("code", model.code);
            } else {
                contentValues.putNull("code");
            }
            if (model.numberType != null) {
                contentValues.put(Table.NUMBERTYPE, model.numberType);
            } else {
                contentValues.putNull(Table.NUMBERTYPE);
            }
            if (model.domainType != null) {
                contentValues.put(Table.DOMAINTYPE, model.domainType);
            } else {
                contentValues.putNull(Table.DOMAINTYPE);
            }
            if (model.dimension != null) {
                contentValues.put("dimension", model.dimension);
            } else {
                contentValues.putNull("dimension");
            }
            if (model.displayFormName != null) {
                contentValues.put(Table.DISPLAYFORMNAME, model.displayFormName);
            } else {
                contentValues.putNull(Table.DISPLAYFORMNAME);
            }
            if (model.optionSet != null) {
                contentValues.put("optionSet", model.optionSet);
            } else {
                contentValues.putNull("optionSet");
            }
        }

        public boolean exists(DataElement model) {
            return new Select().from(DataElement.class).where(getPrimaryModelWhere(model)).hasData();
        }

        public void loadFromCursor(Cursor cursor, DataElement model) {
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
            int indexshortName = cursor.getColumnIndex("shortName");
            if (indexshortName != -1) {
                if (cursor.isNull(indexshortName)) {
                    model.shortName = null;
                } else {
                    model.shortName = cursor.getString(indexshortName);
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
            int indexvalueType = cursor.getColumnIndex("valueType");
            if (indexvalueType != -1) {
                if (cursor.isNull(indexvalueType)) {
                    model.valueType = null;
                } else {
                    model.valueType = ValueType.valueOf(cursor.getString(indexvalueType));
                }
            }
            int indexoptionSetValue = cursor.getColumnIndex("optionSetValue");
            if (indexoptionSetValue != -1) {
                model.optionSetValue = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexoptionSetValue)))).booleanValue();
            }
            int indexzeroIsSignificant = cursor.getColumnIndex(Table.ZEROISSIGNIFICANT);
            if (indexzeroIsSignificant != -1) {
                model.zeroIsSignificant = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexzeroIsSignificant)))).booleanValue();
            }
            int indexexternalAccess = cursor.getColumnIndex("externalAccess");
            if (indexexternalAccess != -1) {
                model.externalAccess = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexexternalAccess)))).booleanValue();
            }
            int indexaggregationOperator = cursor.getColumnIndex(Table.AGGREGATIONOPERATOR);
            if (indexaggregationOperator != -1) {
                if (cursor.isNull(indexaggregationOperator)) {
                    model.aggregationOperator = null;
                } else {
                    model.aggregationOperator = cursor.getString(indexaggregationOperator);
                }
            }
            int indexformName = cursor.getColumnIndex(Table.FORMNAME);
            if (indexformName != -1) {
                if (cursor.isNull(indexformName)) {
                    model.formName = null;
                } else {
                    model.formName = cursor.getString(indexformName);
                }
            }
            int indexcode = cursor.getColumnIndex("code");
            if (indexcode != -1) {
                if (cursor.isNull(indexcode)) {
                    model.code = null;
                } else {
                    model.code = cursor.getString(indexcode);
                }
            }
            int indexnumberType = cursor.getColumnIndex(Table.NUMBERTYPE);
            if (indexnumberType != -1) {
                if (cursor.isNull(indexnumberType)) {
                    model.numberType = null;
                } else {
                    model.numberType = cursor.getString(indexnumberType);
                }
            }
            int indexdomainType = cursor.getColumnIndex(Table.DOMAINTYPE);
            if (indexdomainType != -1) {
                if (cursor.isNull(indexdomainType)) {
                    model.domainType = null;
                } else {
                    model.domainType = cursor.getString(indexdomainType);
                }
            }
            int indexdimension = cursor.getColumnIndex("dimension");
            if (indexdimension != -1) {
                if (cursor.isNull(indexdimension)) {
                    model.dimension = null;
                } else {
                    model.dimension = cursor.getString(indexdimension);
                }
            }
            int indexdisplayFormName = cursor.getColumnIndex(Table.DISPLAYFORMNAME);
            if (indexdisplayFormName != -1) {
                if (cursor.isNull(indexdisplayFormName)) {
                    model.displayFormName = null;
                } else {
                    model.displayFormName = cursor.getString(indexdisplayFormName);
                }
            }
            int indexoptionSet = cursor.getColumnIndex("optionSet");
            if (indexoptionSet == -1) {
                return;
            }
            if (cursor.isNull(indexoptionSet)) {
                model.optionSet = null;
            } else {
                model.optionSet = cursor.getString(indexoptionSet);
            }
        }

        public ConditionQueryBuilder<DataElement> getPrimaryModelWhere(DataElement model) {
            return new ConditionQueryBuilder(DataElement.class, Condition.column("id").is(model.id));
        }

        public ConditionQueryBuilder<DataElement> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(DataElement.class, Condition.column("id").is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return "CREATE TABLE IF NOT EXISTS `DataElement`(`name` TEXT, `displayName` TEXT, `created` TEXT, `lastUpdated` TEXT, `access` TEXT, `id` TEXT, `shortName` TEXT, `description` TEXT, `valueType` TEXT, `optionSetValue` INTEGER, `zeroIsSignificant` INTEGER, `externalAccess` INTEGER, `aggregationOperator` TEXT, `formName` TEXT, `code` TEXT, `numberType` TEXT, `domainType` TEXT, `dimension` TEXT, `displayFormName` TEXT, `optionSet` TEXT, PRIMARY KEY(`id`));";
        }

        public final DataElement newInstance() {
            return new DataElement();
        }
    }

    public final class Table {
        public static final String ACCESS = "access";
        public static final String AGGREGATIONOPERATOR = "aggregationOperator";
        public static final String CODE = "code";
        public static final String CREATED = "created";
        public static final String DESCRIPTION = "description";
        public static final String DIMENSION = "dimension";
        public static final String DISPLAYFORMNAME = "displayFormName";
        public static final String DISPLAYNAME = "displayName";
        public static final String DOMAINTYPE = "domainType";
        public static final String EXTERNALACCESS = "externalAccess";
        public static final String FORMNAME = "formName";
        public static final String ID = "id";
        public static final String LASTUPDATED = "lastUpdated";
        public static final String NAME = "name";
        public static final String NUMBERTYPE = "numberType";
        public static final String OPTIONSET = "optionSet";
        public static final String OPTIONSETVALUE = "optionSetValue";
        public static final String SHORTNAME = "shortName";
        public static final String TABLE_NAME = "DataElement";
        public static final String VALUETYPE = "valueType";
        public static final String ZEROISSIGNIFICANT = "zeroIsSignificant";
    }

    @JsonProperty("optionSet")
    public void setOptionSet(Map<String, Object> optionSet) {
        this.optionSet = (String) optionSet.get("id");
    }

    public ValueType getValueType() {
        return this.valueType;
    }

    public void setValueType(ValueType valueType) {
        this.valueType = valueType;
    }

    public boolean isOptionSetValue() {
        return this.optionSetValue;
    }

    public void setOptionSetValue(boolean optionSetValue) {
        this.optionSetValue = optionSetValue;
    }

    public String getOptionSet() {
        return this.optionSet;
    }

    public void setOptionSet(String optionSet) {
        this.optionSet = optionSet;
    }

    public boolean getZeroIsSignificant() {
        return this.zeroIsSignificant;
    }

    public void setZeroIsSignificant(boolean zeroIsSignificant) {
        this.zeroIsSignificant = zeroIsSignificant;
    }

    public boolean getExternalAccess() {
        return this.externalAccess;
    }

    public void setExternalAccess(boolean externalAccess) {
        this.externalAccess = externalAccess;
    }

    public String getAggregationOperator() {
        return this.aggregationOperator;
    }

    public void setAggregationOperator(String aggregationOperator) {
        this.aggregationOperator = aggregationOperator;
    }

    public String getFormName() {
        return this.formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNumberType() {
        return this.numberType;
    }

    public void setNumberType(String numberType) {
        this.numberType = numberType;
    }

    public String getDomainType() {
        return this.domainType;
    }

    public void setDomainType(String domainType) {
        this.domainType = domainType;
    }

    public String getDimension() {
        return this.dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getDisplayFormName() {
        return this.displayFormName;
    }

    public void setDisplayFormName(String displayFormName) {
        this.displayFormName = displayFormName;
    }

    public List<AttributeValue> getAttributeValues() {
        if (this.attributeValues == null) {
            this.attributeValues = MetaDataController.getAttributeValues(this);
        }
        return this.attributeValues;
    }

    public AttributeValue getAttributeValue(String attributeId) {
        if (getAttributeValues() == null) {
            return null;
        }
        for (AttributeValue attributeValue : getAttributeValues()) {
            if (attributeValue.getAttribute().equals(attributeId)) {
                return attributeValue;
            }
        }
        return null;
    }

    public AttributeValue getAttributeValue(long id) {
        return MetaDataController.getAttributeValue(Long.valueOf(id));
    }
}
