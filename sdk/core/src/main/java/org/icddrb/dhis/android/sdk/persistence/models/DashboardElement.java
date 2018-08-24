package org.icddrb.dhis.android.sdk.persistence.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.raizlabs.android.dbflow.annotation.NotNull;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.builder.Condition.Operation;
import com.raizlabs.android.dbflow.sql.builder.ConditionQueryBuilder;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import org.icddrb.dhis.android.sdk.persistence.models.meta.State;

public final class DashboardElement extends BaseMetaDataObject {
    static final String DASHBOARD_ITEM_KEY = "dashboardItem";
    @JsonIgnore
    @NotNull
    DashboardItem dashboardItem;
    @JsonIgnore
    @NotNull
    State state = State.SYNCED;

    public final class Adapter extends ModelAdapter<DashboardElement> {
        public Class<DashboardElement> getModelClass() {
            return DashboardElement.class;
        }

        public String getTableName() {
            return Table.TABLE_NAME;
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `DashboardElement` (`NAME`, `DISPLAYNAME`, `CREATED`, `LASTUPDATED`, `ACCESS`, `ID`, `STATE`, `dashboardItem`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, DashboardElement model) {
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
            State modelstate = model.state;
            if (modelstate != null) {
                statement.bindString(7, modelstate.name());
            } else {
                statement.bindNull(7);
            }
            if (model.dashboardItem == null) {
                statement.bindNull(8);
            } else if (model.dashboardItem.id != null) {
                statement.bindString(8, model.dashboardItem.id);
            } else {
                statement.bindNull(8);
            }
        }

        public void bindToContentValues(ContentValues contentValues, DashboardElement model) {
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
            State modelstate = model.state;
            if (modelstate != null) {
                contentValues.put("state", modelstate.name());
            } else {
                contentValues.putNull("state");
            }
            if (model.dashboardItem == null) {
                contentValues.putNull("dashboardItem");
            } else if (model.dashboardItem.id != null) {
                contentValues.put("dashboardItem", model.dashboardItem.id);
            } else {
                contentValues.putNull("dashboardItem");
            }
        }

        public void bindToInsertValues(ContentValues contentValues, DashboardElement model) {
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
            State modelstate = model.state;
            if (modelstate != null) {
                contentValues.put("state", modelstate.name());
            } else {
                contentValues.putNull("state");
            }
            if (model.dashboardItem == null) {
                contentValues.putNull("dashboardItem");
            } else if (model.dashboardItem.id != null) {
                contentValues.put("dashboardItem", model.dashboardItem.id);
            } else {
                contentValues.putNull("dashboardItem");
            }
        }

        public boolean exists(DashboardElement model) {
            return new Select().from(DashboardElement.class).where(getPrimaryModelWhere(model)).hasData();
        }

        public void loadFromCursor(Cursor cursor, DashboardElement model) {
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
            int indexstate = cursor.getColumnIndex("state");
            if (indexstate != -1) {
                model.state = State.valueOf(cursor.getString(indexstate));
            }
            int indexdashboardItem = cursor.getColumnIndex("dashboardItem");
            if (indexdashboardItem != -1 && !cursor.isNull(indexdashboardItem)) {
                model.dashboardItem = (DashboardItem) new Select().from(DashboardItem.class).where().and(Condition.column("id").is(cursor.getString(indexdashboardItem))).querySingle();
            }
        }

        public ConditionQueryBuilder<DashboardElement> getPrimaryModelWhere(DashboardElement model) {
            return new ConditionQueryBuilder(DashboardElement.class, Condition.column("id").is(model.id));
        }

        public ConditionQueryBuilder<DashboardElement> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(DashboardElement.class, Condition.column("id").is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return String.format("CREATE TABLE IF NOT EXISTS `DashboardElement`(`name` TEXT, `displayName` TEXT, `created` TEXT, `lastUpdated` TEXT, `access` TEXT, `id` TEXT, `state` TEXT NOT NULL ON CONFLICT FAIL,  `dashboardItem` TEXT NOT NULL ON CONFLICT FAIL, PRIMARY KEY(`id`), FOREIGN KEY(`dashboardItem`) REFERENCES `%1s` (`id`) ON UPDATE NO ACTION ON DELETE CASCADE );", new Object[]{FlowManager.getTableName(DashboardItem.class)});
        }

        public final DashboardElement newInstance() {
            return new DashboardElement();
        }
    }

    public final class Table {
        public static final String ACCESS = "access";
        public static final String CREATED = "created";
        public static final String DASHBOARDITEM_DASHBOARDITEM = "dashboardItem";
        public static final String DISPLAYNAME = "displayName";
        public static final String ID = "id";
        public static final String LASTUPDATED = "lastUpdated";
        public static final String NAME = "name";
        public static final String STATE = "state";
        public static final String TABLE_NAME = "DashboardElement";
    }

    @JsonIgnore
    public static DashboardElement createDashboardElement(DashboardItem item, DashboardItemContent content) {
        DashboardElement element = new DashboardElement();
        element.setUid(content.getUid());
        element.setName(content.getName());
        element.setCreated(content.getCreated());
        element.setLastUpdated(content.getLastUpdated());
        element.setState(State.TO_POST);
        element.setDashboardItem(item);
        return element;
    }

    @JsonIgnore
    public void deleteDashboardElement() {
        if (State.TO_POST.equals(getState())) {
            super.delete();
        } else {
            setState(State.TO_DELETE);
            super.save();
        }
        if (this.dashboardItem.getContentCount() <= 0) {
            this.dashboardItem.deleteDashboardItem();
        }
    }

    @JsonIgnore
    public State getState() {
        return this.state;
    }

    @JsonIgnore
    public void setState(State state) {
        this.state = state;
    }

    @JsonIgnore
    public DashboardItem getDashboardItem() {
        return this.dashboardItem;
    }

    @JsonIgnore
    public void setDashboardItem(DashboardItem dashboardItem) {
        this.dashboardItem = dashboardItem;
    }
}
