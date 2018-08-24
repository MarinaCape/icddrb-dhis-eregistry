package org.icddrb.dhis.android.sdk.persistence.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.builder.Condition.Operation;
import com.raizlabs.android.dbflow.sql.builder.ConditionQueryBuilder;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.Update;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import org.icddrb.dhis.android.sdk.controllers.tracker.TrackerController;
import org.icddrb.dhis.android.sdk.utils.Utils;

@JsonIgnoreProperties({"modelAdapter"})
public class DataValue extends BaseValue {
    @JsonProperty("dataElement")
    String dataElement;
    @JsonIgnore
    String event;
    @JsonIgnore
    long localEventId = -1;
    @JsonProperty("providedElsewhere")
    boolean providedElsewhere;
    @JsonProperty("storedBy")
    String storedBy;

    public final class Adapter extends ModelAdapter<DataValue> {
        public Class<DataValue> getModelClass() {
            return DataValue.class;
        }

        public String getTableName() {
            return Table.TABLE_NAME;
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `DataValue` (`VALUE`, `LOCALEVENTID`, `EVENT`, `DATAELEMENT`, `PROVIDEDELSEWHERE`, `STOREDBY`) VALUES (?, ?, ?, ?, ?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, DataValue model) {
            if (model.value != null) {
                statement.bindString(1, model.value);
            } else {
                statement.bindNull(1);
            }
            statement.bindLong(2, model.localEventId);
            if (model.event != null) {
                statement.bindString(3, model.event);
            } else {
                statement.bindNull(3);
            }
            if (model.dataElement != null) {
                statement.bindString(4, model.dataElement);
            } else {
                statement.bindNull(4);
            }
            Object modelprovidedElsewhere = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.providedElsewhere));
            if (modelprovidedElsewhere != null) {
                statement.bindLong(5, (long) ((Integer) modelprovidedElsewhere).intValue());
            } else {
                statement.bindNull(5);
            }
            if (model.storedBy != null) {
                statement.bindString(6, model.storedBy);
            } else {
                statement.bindNull(6);
            }
        }

        public void bindToContentValues(ContentValues contentValues, DataValue model) {
            if (model.value != null) {
                contentValues.put("value", model.value);
            } else {
                contentValues.putNull("value");
            }
            contentValues.put(Table.LOCALEVENTID, Long.valueOf(model.localEventId));
            if (model.event != null) {
                contentValues.put("event", model.event);
            } else {
                contentValues.putNull("event");
            }
            if (model.dataElement != null) {
                contentValues.put("dataElement", model.dataElement);
            } else {
                contentValues.putNull("dataElement");
            }
            Object modelprovidedElsewhere = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.providedElsewhere));
            if (modelprovidedElsewhere != null) {
                contentValues.put(Table.PROVIDEDELSEWHERE, (Integer) modelprovidedElsewhere);
            } else {
                contentValues.putNull(Table.PROVIDEDELSEWHERE);
            }
            if (model.storedBy != null) {
                contentValues.put(Table.STOREDBY, model.storedBy);
            } else {
                contentValues.putNull(Table.STOREDBY);
            }
        }

        public void bindToInsertValues(ContentValues contentValues, DataValue model) {
            if (model.value != null) {
                contentValues.put("value", model.value);
            } else {
                contentValues.putNull("value");
            }
            contentValues.put(Table.LOCALEVENTID, Long.valueOf(model.localEventId));
            if (model.event != null) {
                contentValues.put("event", model.event);
            } else {
                contentValues.putNull("event");
            }
            if (model.dataElement != null) {
                contentValues.put("dataElement", model.dataElement);
            } else {
                contentValues.putNull("dataElement");
            }
            Object modelprovidedElsewhere = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.providedElsewhere));
            if (modelprovidedElsewhere != null) {
                contentValues.put(Table.PROVIDEDELSEWHERE, (Integer) modelprovidedElsewhere);
            } else {
                contentValues.putNull(Table.PROVIDEDELSEWHERE);
            }
            if (model.storedBy != null) {
                contentValues.put(Table.STOREDBY, model.storedBy);
            } else {
                contentValues.putNull(Table.STOREDBY);
            }
        }

        public boolean exists(DataValue model) {
            return new Select().from(DataValue.class).where(getPrimaryModelWhere(model)).hasData();
        }

        public void loadFromCursor(Cursor cursor, DataValue model) {
            int indexvalue = cursor.getColumnIndex("value");
            if (indexvalue != -1) {
                if (cursor.isNull(indexvalue)) {
                    model.value = null;
                } else {
                    model.value = cursor.getString(indexvalue);
                }
            }
            int indexlocalEventId = cursor.getColumnIndex(Table.LOCALEVENTID);
            if (indexlocalEventId != -1) {
                model.localEventId = cursor.getLong(indexlocalEventId);
            }
            int indexevent = cursor.getColumnIndex("event");
            if (indexevent != -1) {
                if (cursor.isNull(indexevent)) {
                    model.event = null;
                } else {
                    model.event = cursor.getString(indexevent);
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
            int indexprovidedElsewhere = cursor.getColumnIndex(Table.PROVIDEDELSEWHERE);
            if (indexprovidedElsewhere != -1) {
                model.providedElsewhere = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexprovidedElsewhere)))).booleanValue();
            }
            int indexstoredBy = cursor.getColumnIndex(Table.STOREDBY);
            if (indexstoredBy == -1) {
                return;
            }
            if (cursor.isNull(indexstoredBy)) {
                model.storedBy = null;
            } else {
                model.storedBy = cursor.getString(indexstoredBy);
            }
        }

        public ConditionQueryBuilder<DataValue> getPrimaryModelWhere(DataValue model) {
            return new ConditionQueryBuilder(DataValue.class, Condition.column(Table.LOCALEVENTID).is(Long.valueOf(model.localEventId)), Condition.column("dataElement").is(model.dataElement));
        }

        public ConditionQueryBuilder<DataValue> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(DataValue.class, Condition.column(Table.LOCALEVENTID).is(Operation.EMPTY_PARAM), Condition.column("dataElement").is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return "CREATE TABLE IF NOT EXISTS `DataValue`(`value` TEXT, `localEventId` INTEGER, `event` TEXT, `dataElement` TEXT, `providedElsewhere` INTEGER, `storedBy` TEXT, PRIMARY KEY(`localEventId`, `dataElement`));";
        }

        public final DataValue newInstance() {
            return new DataValue();
        }
    }

    public final class Table {
        public static final String DATAELEMENT = "dataElement";
        public static final String EVENT = "event";
        public static final String LOCALEVENTID = "localEventId";
        public static final String PROVIDEDELSEWHERE = "providedElsewhere";
        public static final String STOREDBY = "storedBy";
        public static final String TABLE_NAME = "DataValue";
        public static final String VALUE = "value";
    }

    public DataValue(DataValue dataValue) {
        super(dataValue);
        this.localEventId = dataValue.localEventId;
        this.event = dataValue.event;
        this.dataElement = dataValue.dataElement;
        this.providedElsewhere = dataValue.providedElsewhere;
        this.storedBy = dataValue.storedBy;
    }

    public DataValue(Event event, String value, String dataElement, boolean providedElsewhere, String storedBy) {
        this.localEventId = event.getLocalId();
        this.event = event.getEvent();
        this.value = value;
        this.dataElement = dataElement;
        this.providedElsewhere = providedElsewhere;
        this.storedBy = storedBy;
    }

    private DataValue(long localEventId, String event, String value, String dataElement, boolean providedElsewhere, String storedBy) {
        this.localEventId = localEventId;
        this.event = event;
        this.value = value;
        this.dataElement = dataElement;
        this.providedElsewhere = providedElsewhere;
        this.storedBy = storedBy;
    }

    public DataValue clone() {
        return new DataValue(this.localEventId, this.event, getValue(), this.dataElement, this.providedElsewhere, this.storedBy);
    }

    public void save() {
        if (!Utils.isLocal(this.event) || TrackerController.getDataValue(this.localEventId, this.dataElement) == null) {
            super.save();
        } else {
            updateManually();
        }
    }

    public void update() {
        save();
    }

    @JsonAnySetter
    public void handleUnknown(String key, Object value) {
    }

    public long getLocalEventId() {
        return this.localEventId;
    }

    public void setLocalEventId(long localEventId) {
        this.localEventId = localEventId;
    }

    public String getEvent() {
        return this.event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getDataElement() {
        return this.dataElement;
    }

    public void setDataElement(String dataElement) {
        this.dataElement = dataElement;
    }

    public boolean getProvidedElsewhere() {
        return this.providedElsewhere;
    }

    public void setProvidedElsewhere(boolean providedElsewhere) {
        this.providedElsewhere = providedElsewhere;
    }

    public String getStoredBy() {
        return this.storedBy;
    }

    public void setStoredBy(String storedBy) {
        this.storedBy = storedBy;
    }

    public void updateManually() {
        new Update(DataValue.class).set(Condition.column("value").is(getValue())).where(Condition.column(Table.LOCALEVENTID).is(Long.valueOf(this.localEventId)), Condition.column("dataElement").is(this.dataElement)).queryClose();
    }
}
