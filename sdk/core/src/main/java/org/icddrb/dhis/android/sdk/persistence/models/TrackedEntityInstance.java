package org.icddrb.dhis.android.sdk.persistence.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.builder.Condition.Operation;
import com.raizlabs.android.dbflow.sql.builder.ConditionQueryBuilder;
import com.raizlabs.android.dbflow.sql.language.Update;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import java.io.Serializable;
import java.util.List;
import org.icddrb.dhis.android.sdk.controllers.tracker.TrackerController;
import org.icddrb.dhis.android.sdk.utils.api.CodeGenerator;

@JsonInclude(Include.NON_NULL)
public class TrackedEntityInstance extends BaseSerializableModel implements Serializable {
    @JsonProperty("attributes")
    List<TrackedEntityAttributeValue> attributes;
    @JsonProperty("orgUnit")
    String orgUnit;
    @JsonProperty("relationships")
    List<Relationship> relationships;
    @JsonProperty("trackedEntityType")
    String trackedEntity;
    @JsonIgnore
    String trackedEntityInstance;

    public final class Adapter extends ModelAdapter<TrackedEntityInstance> {
        public Class<TrackedEntityInstance> getModelClass() {
            return TrackedEntityInstance.class;
        }

        public String getTableName() {
            return "TrackedEntityInstance";
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `TrackedEntityInstance` (`NAME`, `DISPLAYNAME`, `CREATED`, `LASTUPDATED`, `ACCESS`, `ID`, `FROMSERVER`, `TRACKEDENTITYINSTANCE`, `TRACKEDENTITYTYPE`, `ORGUNIT`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, TrackedEntityInstance model) {
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
            Object modelfromServer = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.fromServer));
            if (modelfromServer != null) {
                statement.bindLong(7, (long) ((Integer) modelfromServer).intValue());
            } else {
                statement.bindNull(7);
            }
            if (model.trackedEntityInstance != null) {
                statement.bindString(8, model.trackedEntityInstance);
            } else {
                statement.bindNull(8);
            }
            if (model.trackedEntity != null) {
                statement.bindString(9, model.trackedEntity);
            } else {
                statement.bindNull(9);
            }
            if (model.orgUnit != null) {
                statement.bindString(10, model.orgUnit);
            } else {
                statement.bindNull(10);
            }
        }

        public void bindToContentValues(ContentValues contentValues, TrackedEntityInstance model) {
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
            Object modelfromServer = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.fromServer));
            if (modelfromServer != null) {
                contentValues.put("fromServer", (Integer) modelfromServer);
            } else {
                contentValues.putNull("fromServer");
            }
            contentValues.put("localId", Long.valueOf(model.localId));
            if (model.trackedEntityInstance != null) {
                contentValues.put("trackedEntityInstance", model.trackedEntityInstance);
            } else {
                contentValues.putNull("trackedEntityInstance");
            }
            if (model.trackedEntity != null) {
                contentValues.put("trackedEntityType", model.trackedEntity);
            } else {
                contentValues.putNull("trackedEntityType");
            }
            if (model.orgUnit != null) {
                contentValues.put("orgUnit", model.orgUnit);
            } else {
                contentValues.putNull("orgUnit");
            }
        }

        public void bindToInsertValues(ContentValues contentValues, TrackedEntityInstance model) {
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
            Object modelfromServer = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.fromServer));
            if (modelfromServer != null) {
                contentValues.put("fromServer", (Integer) modelfromServer);
            } else {
                contentValues.putNull("fromServer");
            }
            if (model.trackedEntityInstance != null) {
                contentValues.put("trackedEntityInstance", model.trackedEntityInstance);
            } else {
                contentValues.putNull("trackedEntityInstance");
            }
            if (model.trackedEntity != null) {
                contentValues.put("trackedEntityType", model.trackedEntity);
            } else {
                contentValues.putNull("trackedEntityType");
            }
            if (model.orgUnit != null) {
                contentValues.put("orgUnit", model.orgUnit);
            } else {
                contentValues.putNull("orgUnit");
            }
        }

        public boolean exists(TrackedEntityInstance model) {
            return model.localId > 0;
        }

        public void loadFromCursor(Cursor cursor, TrackedEntityInstance model) {
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
            int indexfromServer = cursor.getColumnIndex("fromServer");
            if (indexfromServer != -1) {
                model.fromServer = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexfromServer)))).booleanValue();
            }
            int indexlocalId = cursor.getColumnIndex("localId");
            if (indexlocalId != -1) {
                model.localId = cursor.getLong(indexlocalId);
            }
            int indextrackedEntityInstance = cursor.getColumnIndex("trackedEntityInstance");
            if (indextrackedEntityInstance != -1) {
                if (cursor.isNull(indextrackedEntityInstance)) {
                    model.trackedEntityInstance = null;
                } else {
                    model.trackedEntityInstance = cursor.getString(indextrackedEntityInstance);
                }
            }
            int indextrackedEntityType = cursor.getColumnIndex("trackedEntityType");
            if (indextrackedEntityType != -1) {
                if (cursor.isNull(indextrackedEntityType)) {
                    model.trackedEntity = null;
                } else {
                    model.trackedEntity = cursor.getString(indextrackedEntityType);
                }
            }
            int indexorgUnit = cursor.getColumnIndex("orgUnit");
            if (indexorgUnit == -1) {
                return;
            }
            if (cursor.isNull(indexorgUnit)) {
                model.orgUnit = null;
            } else {
                model.orgUnit = cursor.getString(indexorgUnit);
            }
        }

        public void updateAutoIncrement(TrackedEntityInstance model, long id) {
            model.localId = id;
        }

        public long getAutoIncrementingId(TrackedEntityInstance model) {
            return model.localId;
        }

        public String getAutoIncrementingColumnName() {
            return "localId";
        }

        public ConditionQueryBuilder<TrackedEntityInstance> getPrimaryModelWhere(TrackedEntityInstance model) {
            return new ConditionQueryBuilder(TrackedEntityInstance.class, Condition.column("localId").is(Long.valueOf(model.localId)));
        }

        public ConditionQueryBuilder<TrackedEntityInstance> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(TrackedEntityInstance.class, Condition.column("localId").is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return "CREATE TABLE IF NOT EXISTS `TrackedEntityInstance`(`name` TEXT, `displayName` TEXT, `created` TEXT, `lastUpdated` TEXT, `access` TEXT, `id` TEXT, `fromServer` INTEGER, `localId` INTEGER PRIMARY KEY AUTOINCREMENT, `trackedEntityInstance` TEXT UNIQUE ON CONFLICT FAIL, `trackedEntityType` TEXT, `orgUnit` TEXT);";
        }

        public final TrackedEntityInstance newInstance() {
            return new TrackedEntityInstance();
        }
    }

    public final class Table {
        public static final String ACCESS = "access";
        public static final String CREATED = "created";
        public static final String DISPLAYNAME = "displayName";
        public static final String FROMSERVER = "fromServer";
        public static final String ID = "id";
        public static final String LASTUPDATED = "lastUpdated";
        public static final String LOCALID = "localId";
        public static final String NAME = "name";
        public static final String ORGUNIT = "orgUnit";
        public static final String TABLE_NAME = "TrackedEntityInstance";
        public static final String TRACKEDENTITYINSTANCE = "trackedEntityInstance";
        public static final String TRACKEDENTITYTYPE = "trackedEntityType";
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public TrackedEntityInstance() {
        this.trackedEntityInstance = CodeGenerator.generateCode();
    }

    public TrackedEntityInstance(TrackedEntityInstance trackedEntityInstance) {
        super(trackedEntityInstance);
        this.trackedEntityInstance = trackedEntityInstance.trackedEntityInstance;
        this.trackedEntity = trackedEntityInstance.trackedEntity;
        this.orgUnit = trackedEntityInstance.orgUnit;
    }

    public TrackedEntityInstance(Program program, String organisationUnit) {
        this.fromServer = false;
        this.trackedEntityInstance = CodeGenerator.generateCode();
        this.trackedEntity = program.getTrackedEntityType().getId();
        this.orgUnit = organisationUnit;
    }

    @JsonProperty("trackedEntityInstance")
    public String getTrackedEntityInstance() {
        return this.trackedEntityInstance;
    }

    @JsonProperty("trackedEntityInstance")
    public void setTrackedEntityInstance(String trackedEntityInstance) {
        this.trackedEntityInstance = trackedEntityInstance;
    }

    @JsonProperty("attributes")
    public List<TrackedEntityAttributeValue> getAttributes() {
        if (this.attributes == null) {
            this.attributes = TrackerController.getTrackedEntityAttributeValues(this.localId);
        }
        return this.attributes;
    }

    @JsonIgnore
    public void setAttributes(List<TrackedEntityAttributeValue> attributes) {
        this.attributes = attributes;
    }

    public List<Relationship> getRelationships() {
        if (this.relationships == null) {
            this.relationships = TrackerController.getRelationships(this.trackedEntityInstance);
        }
        return this.relationships;
    }

    public void setRelationships(List<Relationship> relationships) {
        this.relationships = relationships;
    }

    public void save() {
        TrackedEntityInstance existingTei = TrackerController.getTrackedEntityInstance(this.trackedEntityInstance);
        if (existingTei != null) {
            this.localId = existingTei.localId;
        }
        if (getTrackedEntityInstance() != null || TrackerController.getTrackedEntityInstance(this.localId) == null) {
            super.save();
        } else {
            updateManually();
        }
    }

    public void updateManually() {
        new Update(TrackedEntityInstance.class).set(Condition.column("fromServer").is(Boolean.valueOf(this.fromServer))).where(Condition.column("localId").is(Long.valueOf(this.localId))).queryClose();
    }

    public void update() {
        save();
    }

    public String getTrackedEntity() {
        return this.trackedEntity;
    }

    public void setTrackedEntity(String trackedEntity) {
        this.trackedEntity = trackedEntity;
    }

    public String getOrgUnit() {
        return this.orgUnit;
    }

    public void setOrgUnit(String orgUnit) {
        this.orgUnit = orgUnit;
    }

    public void setFromServer(boolean fromServer) {
        this.fromServer = fromServer;
    }

    public void setLocalId(long localId) {
        this.localId = localId;
    }

    @JsonIgnore
    public String getUid() {
        return this.trackedEntityInstance;
    }

    @JsonIgnore
    public void setUid(String uid) {
        this.trackedEntityInstance = uid;
    }
}
