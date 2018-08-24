package org.icddrb.dhis.android.sdk.persistence.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raizlabs.android.dbflow.annotation.NotNull;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.builder.Condition.Operation;
import com.raizlabs.android.dbflow.sql.builder.ConditionQueryBuilder;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import java.util.ArrayList;
import java.util.List;
import org.icddrb.dhis.android.sdk.persistence.models.meta.State;
import org.icddrb.dhis.android.sdk.persistence.preferences.DateTimeManager;
import org.icddrb.dhis.android.sdk.persistence.preferences.ResourceType;
import org.joda.time.DateTime;

public final class DashboardItem extends BaseMetaDataObject {
    public static final int MAX_CONTENT = 8;
    public static final String SHAPE_DOUBLE_WIDTH = "double_width";
    public static final String SHAPE_FULL_WIDTH = "full_width";
    public static final String SHAPE_NORMAL = "normal";
    private static final String TAG = DashboardItem.class.getSimpleName();
    @JsonProperty("chart")
    DashboardElement chart;
    @JsonIgnore
    Dashboard dashboard;
    @JsonProperty("eventChart")
    DashboardElement eventChart;
    @JsonProperty("eventReport")
    DashboardElement eventReport;
    @JsonProperty("map")
    DashboardElement map;
    @JsonProperty("messages")
    boolean messages;
    @JsonProperty("reportTable")
    DashboardElement reportTable;
    @JsonProperty("reports")
    List<DashboardElement> reports;
    @JsonProperty("resources")
    List<DashboardElement> resources;
    @JsonProperty("shape")
    String shape = SHAPE_NORMAL;
    @JsonIgnore
    @NotNull
    State state = State.SYNCED;
    @JsonProperty("type")
    String type;
    @JsonProperty("users")
    List<DashboardElement> users;

    public final class Adapter extends ModelAdapter<DashboardItem> {
        public Class<DashboardItem> getModelClass() {
            return DashboardItem.class;
        }

        public String getTableName() {
            return Table.TABLE_NAME;
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `DashboardItem` (`NAME`, `DISPLAYNAME`, `CREATED`, `LASTUPDATED`, `ACCESS`, `ID`, `STATE`, `TYPE`, `SHAPE`, `dashboard`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, DashboardItem model) {
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
            if (model.type != null) {
                statement.bindString(8, model.type);
            } else {
                statement.bindNull(8);
            }
            if (model.shape != null) {
                statement.bindString(9, model.shape);
            } else {
                statement.bindNull(9);
            }
            if (model.dashboard == null) {
                statement.bindNull(10);
            } else if (model.dashboard.id != null) {
                statement.bindString(10, model.dashboard.id);
            } else {
                statement.bindNull(10);
            }
        }

        public void bindToContentValues(ContentValues contentValues, DashboardItem model) {
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
            if (model.type != null) {
                contentValues.put("type", model.type);
            } else {
                contentValues.putNull("type");
            }
            if (model.shape != null) {
                contentValues.put(Table.SHAPE, model.shape);
            } else {
                contentValues.putNull(Table.SHAPE);
            }
            if (model.dashboard == null) {
                contentValues.putNull(Table.DASHBOARD_DASHBOARD);
            } else if (model.dashboard.id != null) {
                contentValues.put(Table.DASHBOARD_DASHBOARD, model.dashboard.id);
            } else {
                contentValues.putNull(Table.DASHBOARD_DASHBOARD);
            }
        }

        public void bindToInsertValues(ContentValues contentValues, DashboardItem model) {
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
            if (model.type != null) {
                contentValues.put("type", model.type);
            } else {
                contentValues.putNull("type");
            }
            if (model.shape != null) {
                contentValues.put(Table.SHAPE, model.shape);
            } else {
                contentValues.putNull(Table.SHAPE);
            }
            if (model.dashboard == null) {
                contentValues.putNull(Table.DASHBOARD_DASHBOARD);
            } else if (model.dashboard.id != null) {
                contentValues.put(Table.DASHBOARD_DASHBOARD, model.dashboard.id);
            } else {
                contentValues.putNull(Table.DASHBOARD_DASHBOARD);
            }
        }

        public boolean exists(DashboardItem model) {
            return new Select().from(DashboardItem.class).where(getPrimaryModelWhere(model)).hasData();
        }

        public void loadFromCursor(Cursor cursor, DashboardItem model) {
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
            int indextype = cursor.getColumnIndex("type");
            if (indextype != -1) {
                if (cursor.isNull(indextype)) {
                    model.type = null;
                } else {
                    model.type = cursor.getString(indextype);
                }
            }
            int indexshape = cursor.getColumnIndex(Table.SHAPE);
            if (indexshape != -1) {
                if (cursor.isNull(indexshape)) {
                    model.shape = null;
                } else {
                    model.shape = cursor.getString(indexshape);
                }
            }
            int indexdashboard = cursor.getColumnIndex(Table.DASHBOARD_DASHBOARD);
            if (indexdashboard != -1 && !cursor.isNull(indexdashboard)) {
                model.dashboard = (Dashboard) new Select().from(Dashboard.class).where().and(Condition.column("id").is(cursor.getString(indexdashboard))).querySingle();
            }
        }

        public ConditionQueryBuilder<DashboardItem> getPrimaryModelWhere(DashboardItem model) {
            return new ConditionQueryBuilder(DashboardItem.class, Condition.column("id").is(model.id));
        }

        public ConditionQueryBuilder<DashboardItem> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(DashboardItem.class, Condition.column("id").is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return String.format("CREATE TABLE IF NOT EXISTS `DashboardItem`(`name` TEXT, `displayName` TEXT, `created` TEXT, `lastUpdated` TEXT, `access` TEXT, `id` TEXT, `state` TEXT NOT NULL ON CONFLICT FAIL, `type` TEXT, `shape` TEXT,  `dashboard` TEXT, PRIMARY KEY(`id`), FOREIGN KEY(`dashboard`) REFERENCES `%1s` (`id`) ON UPDATE NO ACTION ON DELETE CASCADE );", new Object[]{FlowManager.getTableName(Dashboard.class)});
        }

        public final DashboardItem newInstance() {
            return new DashboardItem();
        }
    }

    public final class Table {
        public static final String ACCESS = "access";
        public static final String CREATED = "created";
        public static final String DASHBOARD_DASHBOARD = "dashboard";
        public static final String DISPLAYNAME = "displayName";
        public static final String ID = "id";
        public static final String LASTUPDATED = "lastUpdated";
        public static final String NAME = "name";
        public static final String SHAPE = "shape";
        public static final String STATE = "state";
        public static final String TABLE_NAME = "DashboardItem";
        public static final String TYPE = "type";
    }

    @JsonIgnore
    public static DashboardItem createDashboardItem(Dashboard dashboard, DashboardItemContent content) {
        DateTime lastUpdatedDateTime = DateTimeManager.getInstance().getLastUpdated(ResourceType.DASHBOARDS);
        DashboardItem item = new DashboardItem();
        item.setCreated(lastUpdatedDateTime.toString());
        item.setLastUpdated(lastUpdatedDateTime.toString());
        item.setState(State.TO_POST);
        item.setDashboard(dashboard);
        item.setAccess(Access.provideDefaultAccess());
        item.setType(content.getType());
        return item;
    }

    @JsonIgnore
    public void deleteDashboardItem() {
        if (this.state == State.TO_POST) {
            super.delete();
            return;
        }
        this.state = State.TO_DELETE;
        super.save();
    }

    @JsonIgnore
    public long getContentCount() {
        List<DashboardElement> elements = new Select().from(DashboardElement.class).where(Condition.column(org.icddrb.dhis.android.sdk.persistence.models.DashboardElement.Table.DASHBOARDITEM_DASHBOARDITEM).is(getUid())).and(Condition.column("state").isNot(State.TO_DELETE.toString())).queryList();
        return elements == null ? 0 : (long) elements.size();
    }

    @JsonIgnore
    public List<DashboardElement> queryRelatedDashboardElements() {
        if (TextUtils.isEmpty(getType())) {
            return new ArrayList();
        }
        List<DashboardElement> elements = new Select().from(DashboardElement.class).where(Condition.column(org.icddrb.dhis.android.sdk.persistence.models.DashboardElement.Table.DASHBOARDITEM_DASHBOARDITEM).is(getUid())).and(Condition.column("state").isNot(State.TO_DELETE.toString())).queryList();
        if (elements == null) {
            return new ArrayList();
        }
        return elements;
    }

    @JsonIgnore
    public List<DashboardElement> getDashboardElements() {
        List<DashboardElement> elements = new ArrayList();
        if (!TextUtils.isEmpty(getType())) {
            String type = getType();
            Object obj = -1;
            switch (type.hashCode()) {
                case -1983070683:
                    if (type.equals(DashboardItemContent.TYPE_RESOURCES)) {
                        obj = 7;
                        break;
                    }
                    break;
                case -257288454:
                    if (type.equals("reportTable")) {
                        obj = 3;
                        break;
                    }
                    break;
                case 107868:
                    if (type.equals("map")) {
                        obj = 2;
                        break;
                    }
                    break;
                case 64608366:
                    if (type.equals(DashboardItemContent.TYPE_EVENT_REPORT)) {
                        obj = 4;
                        break;
                    }
                    break;
                case 94623710:
                    if (type.equals("chart")) {
                        obj = null;
                        break;
                    }
                    break;
                case 111578632:
                    if (type.equals(DashboardItemContent.TYPE_USERS)) {
                        obj = 5;
                        break;
                    }
                    break;
                case 958137700:
                    if (type.equals(DashboardItemContent.TYPE_EVENT_CHART)) {
                        obj = 1;
                        break;
                    }
                    break;
                case 1094603199:
                    if (type.equals(DashboardItemContent.TYPE_REPORTS)) {
                        obj = 6;
                        break;
                    }
                    break;
            }
            switch (obj) {
                case null:
                    elements.add(getChart());
                    break;
                case 1:
                    elements.add(getEventChart());
                    break;
                case 2:
                    elements.add(getMap());
                    break;
                case 3:
                    elements.add(getReportTable());
                    break;
                case 4:
                    elements.add(getEventReport());
                    break;
                case 5:
                    elements.addAll(getUsers());
                    break;
                case 6:
                    elements.addAll(getReports());
                    break;
                case 7:
                    elements.addAll(getResources());
                    break;
                default:
                    break;
            }
        }
        return elements;
    }

    @JsonIgnore
    public void setDashboardElements(List<DashboardElement> dashboardElements) {
        if (!TextUtils.isEmpty(getType()) && dashboardElements != null && !dashboardElements.isEmpty()) {
            String type = getType();
            int i = -1;
            switch (type.hashCode()) {
                case -1983070683:
                    if (type.equals(DashboardItemContent.TYPE_RESOURCES)) {
                        i = 7;
                        break;
                    }
                    break;
                case -257288454:
                    if (type.equals("reportTable")) {
                        i = 3;
                        break;
                    }
                    break;
                case 107868:
                    if (type.equals("map")) {
                        i = 2;
                        break;
                    }
                    break;
                case 64608366:
                    if (type.equals(DashboardItemContent.TYPE_EVENT_REPORT)) {
                        i = 4;
                        break;
                    }
                    break;
                case 94623710:
                    if (type.equals("chart")) {
                        i = 0;
                        break;
                    }
                    break;
                case 111578632:
                    if (type.equals(DashboardItemContent.TYPE_USERS)) {
                        i = 5;
                        break;
                    }
                    break;
                case 958137700:
                    if (type.equals(DashboardItemContent.TYPE_EVENT_CHART)) {
                        i = 1;
                        break;
                    }
                    break;
                case 1094603199:
                    if (type.equals(DashboardItemContent.TYPE_REPORTS)) {
                        i = 6;
                        break;
                    }
                    break;
            }
            switch (i) {
                case 0:
                    setChart((DashboardElement) dashboardElements.get(0));
                    return;
                case 1:
                    setEventChart((DashboardElement) dashboardElements.get(0));
                    return;
                case 2:
                    setMap((DashboardElement) dashboardElements.get(0));
                    return;
                case 3:
                    setReportTable((DashboardElement) dashboardElements.get(0));
                    return;
                case 4:
                    setEventReport((DashboardElement) dashboardElements.get(0));
                    return;
                case 5:
                    setUsers(dashboardElements);
                    return;
                case 6:
                    setReports(dashboardElements);
                    return;
                case 7:
                    setResources(dashboardElements);
                    return;
                default:
                    return;
            }
        }
    }

    @JsonIgnore
    public String getType() {
        return this.type;
    }

    @JsonIgnore
    public void setType(String type) {
        this.type = type;
    }

    @JsonIgnore
    public String getShape() {
        return this.shape;
    }

    @JsonIgnore
    public void setShape(String shape) {
        this.shape = shape;
    }

    @JsonIgnore
    public Dashboard getDashboard() {
        return this.dashboard;
    }

    @JsonIgnore
    public void setDashboard(Dashboard dashboard) {
        this.dashboard = dashboard;
    }

    @JsonIgnore
    public List<DashboardElement> getUsers() {
        return this.users;
    }

    @JsonIgnore
    public void setUsers(List<DashboardElement> users) {
        this.users = users;
    }

    @JsonIgnore
    public List<DashboardElement> getReports() {
        return this.reports;
    }

    @JsonIgnore
    public void setReports(List<DashboardElement> reports) {
        this.reports = reports;
    }

    @JsonIgnore
    public List<DashboardElement> getResources() {
        return this.resources;
    }

    @JsonIgnore
    public void setResources(List<DashboardElement> resources) {
        this.resources = resources;
    }

    @JsonIgnore
    public DashboardElement getChart() {
        return this.chart;
    }

    @JsonIgnore
    public void setChart(DashboardElement chart) {
        this.chart = chart;
    }

    @JsonIgnore
    public DashboardElement getEventChart() {
        return this.eventChart;
    }

    @JsonIgnore
    public void setEventChart(DashboardElement eventChart) {
        this.eventChart = eventChart;
    }

    @JsonIgnore
    public DashboardElement getReportTable() {
        return this.reportTable;
    }

    @JsonIgnore
    public void setReportTable(DashboardElement reportTable) {
        this.reportTable = reportTable;
    }

    @JsonIgnore
    public DashboardElement getMap() {
        return this.map;
    }

    @JsonIgnore
    public void setMap(DashboardElement map) {
        this.map = map;
    }

    @JsonIgnore
    public DashboardElement getEventReport() {
        return this.eventReport;
    }

    @JsonIgnore
    public void setEventReport(DashboardElement eventReport) {
        this.eventReport = eventReport;
    }

    @JsonIgnore
    public boolean isMessages() {
        return this.messages;
    }

    @JsonIgnore
    public void setMessages(boolean messages) {
        this.messages = messages;
    }

    @JsonIgnore
    public State getState() {
        return this.state;
    }

    @JsonIgnore
    public void setState(State state) {
        this.state = state;
    }
}
