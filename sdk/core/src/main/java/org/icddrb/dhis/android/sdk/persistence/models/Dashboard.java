package org.icddrb.dhis.android.sdk.persistence.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raizlabs.android.dbflow.annotation.NotNull;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.builder.Condition.Operation;
import com.raizlabs.android.dbflow.sql.builder.ConditionQueryBuilder;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import java.util.List;
import org.icddrb.dhis.android.sdk.persistence.models.meta.State;
import org.icddrb.dhis.android.sdk.persistence.preferences.DateTimeManager;
import org.icddrb.dhis.android.sdk.persistence.preferences.ResourceType;
import org.icddrb.dhis.android.sdk.utils.Preconditions;
import org.joda.time.DateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class Dashboard extends BaseMetaDataObject {
    public static int MAX_ITEMS = 40;
    @JsonProperty("dashboardItems")
    List<DashboardItem> dashboardItems;
    @JsonIgnore
    @NotNull
    State state = State.SYNCED;

    public final class Adapter extends ModelAdapter<Dashboard> {
        public Class<Dashboard> getModelClass() {
            return Dashboard.class;
        }

        public String getTableName() {
            return Table.TABLE_NAME;
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `Dashboard` (`NAME`, `DISPLAYNAME`, `CREATED`, `LASTUPDATED`, `ACCESS`, `ID`, `STATE`) VALUES (?, ?, ?, ?, ?, ?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, Dashboard model) {
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
        }

        public void bindToContentValues(ContentValues contentValues, Dashboard model) {
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
        }

        public void bindToInsertValues(ContentValues contentValues, Dashboard model) {
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
        }

        public boolean exists(Dashboard model) {
            return new Select().from(Dashboard.class).where(getPrimaryModelWhere(model)).hasData();
        }

        public void loadFromCursor(Cursor cursor, Dashboard model) {
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
        }

        public ConditionQueryBuilder<Dashboard> getPrimaryModelWhere(Dashboard model) {
            return new ConditionQueryBuilder(Dashboard.class, Condition.column("id").is(model.id));
        }

        public ConditionQueryBuilder<Dashboard> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(Dashboard.class, Condition.column("id").is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return "CREATE TABLE IF NOT EXISTS `Dashboard`(`name` TEXT, `displayName` TEXT, `created` TEXT, `lastUpdated` TEXT, `access` TEXT, `id` TEXT, `state` TEXT NOT NULL ON CONFLICT FAIL, PRIMARY KEY(`id`));";
        }

        public final Dashboard newInstance() {
            return new Dashboard();
        }
    }

    public final class Table {
        public static final String ACCESS = "access";
        public static final String CREATED = "created";
        public static final String DISPLAYNAME = "displayName";
        public static final String ID = "id";
        public static final String LASTUPDATED = "lastUpdated";
        public static final String NAME = "name";
        public static final String STATE = "state";
        public static final String TABLE_NAME = "Dashboard";
    }

    @JsonIgnore
    public static Dashboard createDashboard(String name) {
        DateTime lastUpdatedDateTime = DateTimeManager.getInstance().getLastUpdated(ResourceType.DASHBOARDS);
        Dashboard dashboard = new Dashboard();
        dashboard.setState(State.TO_POST);
        dashboard.setName(name);
        dashboard.setDisplayName(name);
        dashboard.setCreated(lastUpdatedDateTime.toString());
        dashboard.setLastUpdated(lastUpdatedDateTime.toString());
        dashboard.setAccess(Access.provideDefaultAccess());
        return dashboard;
    }

    @JsonIgnore
    public void updateDashboard(String newName) {
        setName(newName);
        setDisplayName(newName);
        if (!(this.state == State.TO_DELETE || this.state == State.TO_POST)) {
            this.state = State.TO_UPDATE;
        }
        super.save();
    }

    @JsonIgnore
    public void deleteDashboard() {
        if (this.state == State.TO_POST) {
            super.delete();
            return;
        }
        this.state = State.TO_DELETE;
        super.save();
    }

    @JsonIgnore
    public List<DashboardItem> queryRelatedDashboardItems() {
        return new Select().from(DashboardItem.class).where(Condition.column(org.icddrb.dhis.android.sdk.persistence.models.DashboardItem.Table.DASHBOARD_DASHBOARD).is(getUid())).and(Condition.column("state").isNot(State.TO_DELETE.toString())).queryList();
    }

    @JsonIgnore
    public DashboardItem getAvailableItemByType(String type) {
        List<DashboardItem> items = queryRelatedDashboardItems();
        if (items == null || items.isEmpty()) {
            return null;
        }
        for (DashboardItem item : items) {
            if (type.equals(item.getType()) && item.getContentCount() < 8) {
                return item;
            }
        }
        return null;
    }

    @JsonIgnore
    public boolean addItemContent(DashboardItemContent content) {
        DashboardItem item;
        DashboardElement element;
        Preconditions.isNull(content, "DashboardItemContent object must not be null");
        int itemsCount = getDashboardItemCount();
        if (isItemContentTypeEmbedded(content)) {
            item = DashboardItem.createDashboardItem(this, content);
            element = DashboardElement.createDashboardElement(item, content);
            itemsCount++;
        } else {
            item = getAvailableItemByType(content.getType());
            if (item == null) {
                item = DashboardItem.createDashboardItem(this, content);
                itemsCount++;
            }
            element = DashboardElement.createDashboardElement(item, content);
        }
        if (itemsCount > MAX_ITEMS) {
            return false;
        }
        item.save();
        element.save();
        return true;
    }

    private boolean isItemContentTypeEmbedded(DashboardItemContent content) {
        String type = content.getType();
        boolean z = true;
        switch (type.hashCode()) {
            case -1983070683:
                if (type.equals(DashboardItemContent.TYPE_RESOURCES)) {
                    z = true;
                    break;
                }
                break;
            case -257288454:
                if (type.equals("reportTable")) {
                    z = true;
                    break;
                }
                break;
            case 107868:
                if (type.equals("map")) {
                    z = true;
                    break;
                }
                break;
            case 64608366:
                if (type.equals(DashboardItemContent.TYPE_EVENT_REPORT)) {
                    z = true;
                    break;
                }
                break;
            case 94623710:
                if (type.equals("chart")) {
                    z = false;
                    break;
                }
                break;
            case 111578632:
                if (type.equals(DashboardItemContent.TYPE_USERS)) {
                    z = true;
                    break;
                }
                break;
            case 958137700:
                if (type.equals(DashboardItemContent.TYPE_EVENT_CHART)) {
                    z = true;
                    break;
                }
                break;
            case 1094603199:
                if (type.equals(DashboardItemContent.TYPE_REPORTS)) {
                    z = true;
                    break;
                }
                break;
        }
        switch (z) {
            case false:
            case true:
            case true:
            case true:
            case true:
                return true;
            case true:
            case true:
            case true:
                return false;
            default:
                throw new IllegalArgumentException("Unsupported DashboardItemContent type");
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
    public List<DashboardItem> getDashboardItems() {
        return this.dashboardItems;
    }

    @JsonIgnore
    public void setDashboardItems(List<DashboardItem> dashboardItems) {
        this.dashboardItems = dashboardItems;
    }

    @JsonIgnore
    public boolean hasItems() {
        return getDashboardItemCount() > 0;
    }

    @JsonIgnore
    public int getDashboardItemCount() {
        List<DashboardItem> items = queryRelatedDashboardItems();
        return items == null ? 0 : items.size();
    }
}
