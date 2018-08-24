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
import java.util.ArrayList;
import java.util.List;
import org.icddrb.dhis.android.sdk.persistence.models.meta.State;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class Interpretation extends BaseMetaDataObject {
    public static final String TYPE_CHART = "chart";
    public static final String TYPE_DATA_SET_REPORT = "dataSetReport";
    public static final String TYPE_MAP = "map";
    public static final String TYPE_REPORT_TABLE = "reportTable";
    @JsonProperty("chart")
    InterpretationElement chart;
    @JsonProperty("comments")
    List<InterpretationComment> comments;
    @JsonProperty("dataSet")
    InterpretationElement dataSet;
    @JsonProperty("map")
    InterpretationElement map;
    @JsonProperty("organisationUnit")
    InterpretationElement organisationUnit;
    @JsonProperty("period")
    InterpretationElement period;
    @JsonProperty("reportTable")
    InterpretationElement reportTable;
    @JsonIgnore
    @NotNull
    State state = State.SYNCED;
    @JsonProperty("text")
    String text;
    @JsonProperty("type")
    String type;
    @JsonProperty("user")
    User user;

    public final class Adapter extends ModelAdapter<Interpretation> {
        public Class<Interpretation> getModelClass() {
            return Interpretation.class;
        }

        public String getTableName() {
            return Table.TABLE_NAME;
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `Interpretation` (`NAME`, `DISPLAYNAME`, `CREATED`, `LASTUPDATED`, `ACCESS`, `ID`, `TEXT`, `TYPE`, `STATE`, `user`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, Interpretation model) {
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
            if (model.text != null) {
                statement.bindString(7, model.text);
            } else {
                statement.bindNull(7);
            }
            if (model.type != null) {
                statement.bindString(8, model.type);
            } else {
                statement.bindNull(8);
            }
            State modelstate = model.state;
            if (modelstate != null) {
                statement.bindString(9, modelstate.name());
            } else {
                statement.bindNull(9);
            }
            if (model.user == null) {
                statement.bindNull(10);
            } else if (model.user.id != null) {
                statement.bindString(10, model.user.id);
            } else {
                statement.bindNull(10);
            }
        }

        public void bindToContentValues(ContentValues contentValues, Interpretation model) {
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
            if (model.text != null) {
                contentValues.put("text", model.text);
            } else {
                contentValues.putNull("text");
            }
            if (model.type != null) {
                contentValues.put("type", model.type);
            } else {
                contentValues.putNull("type");
            }
            State modelstate = model.state;
            if (modelstate != null) {
                contentValues.put("state", modelstate.name());
            } else {
                contentValues.putNull("state");
            }
            if (model.user == null) {
                contentValues.putNull("user");
            } else if (model.user.id != null) {
                contentValues.put("user", model.user.id);
            } else {
                contentValues.putNull("user");
            }
        }

        public void bindToInsertValues(ContentValues contentValues, Interpretation model) {
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
            if (model.text != null) {
                contentValues.put("text", model.text);
            } else {
                contentValues.putNull("text");
            }
            if (model.type != null) {
                contentValues.put("type", model.type);
            } else {
                contentValues.putNull("type");
            }
            State modelstate = model.state;
            if (modelstate != null) {
                contentValues.put("state", modelstate.name());
            } else {
                contentValues.putNull("state");
            }
            if (model.user == null) {
                contentValues.putNull("user");
            } else if (model.user.id != null) {
                contentValues.put("user", model.user.id);
            } else {
                contentValues.putNull("user");
            }
        }

        public boolean exists(Interpretation model) {
            return new Select().from(Interpretation.class).where(getPrimaryModelWhere(model)).hasData();
        }

        public void loadFromCursor(Cursor cursor, Interpretation model) {
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
            int indextext = cursor.getColumnIndex("text");
            if (indextext != -1) {
                if (cursor.isNull(indextext)) {
                    model.text = null;
                } else {
                    model.text = cursor.getString(indextext);
                }
            }
            int indextype = cursor.getColumnIndex("type");
            if (indextype != -1) {
                if (cursor.isNull(indextype)) {
                    model.type = null;
                } else {
                    model.type = cursor.getString(indextype);
                }
            }
            int indexstate = cursor.getColumnIndex("state");
            if (indexstate != -1) {
                model.state = State.valueOf(cursor.getString(indexstate));
            }
            int indexuser = cursor.getColumnIndex("user");
            if (indexuser != -1 && !cursor.isNull(indexuser)) {
                model.user = (User) new Select().from(User.class).where().and(Condition.column("id").is(cursor.getString(indexuser))).querySingle();
            }
        }

        public ConditionQueryBuilder<Interpretation> getPrimaryModelWhere(Interpretation model) {
            return new ConditionQueryBuilder(Interpretation.class, Condition.column("id").is(model.id));
        }

        public ConditionQueryBuilder<Interpretation> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(Interpretation.class, Condition.column("id").is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return String.format("CREATE TABLE IF NOT EXISTS `Interpretation`(`name` TEXT, `displayName` TEXT, `created` TEXT, `lastUpdated` TEXT, `access` TEXT, `id` TEXT, `text` TEXT, `type` TEXT, `state` TEXT NOT NULL ON CONFLICT FAIL,  `user` TEXT, PRIMARY KEY(`id`), FOREIGN KEY(`user`) REFERENCES `%1s` (`id`) ON UPDATE NO ACTION ON DELETE CASCADE );", new Object[]{FlowManager.getTableName(User.class)});
        }

        public final Interpretation newInstance() {
            return new Interpretation();
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
        public static final String TABLE_NAME = "Interpretation";
        public static final String TEXT = "text";
        public static final String TYPE = "type";
        public static final String USER_USER = "user";
    }

    public static InterpretationComment addComment(Interpretation interpretation, User user, String text) {
        InterpretationComment comment = new InterpretationComment();
        comment.setAccess(Access.provideDefaultAccess());
        comment.setText(text);
        comment.setState(State.TO_POST);
        comment.setUser(user);
        comment.setInterpretation(interpretation);
        return comment;
    }

    public static Interpretation createInterpretation(DashboardItem item, User user, String text) {
        Interpretation interpretation = new Interpretation();
        interpretation.setAccess(Access.provideDefaultAccess());
        interpretation.setText(text);
        interpretation.setState(State.TO_POST);
        interpretation.setUser(user);
        String type = item.getType();
        Object obj = -1;
        switch (type.hashCode()) {
            case -257288454:
                if (type.equals("reportTable")) {
                    obj = 2;
                    break;
                }
                break;
            case 107868:
                if (type.equals("map")) {
                    obj = 1;
                    break;
                }
                break;
            case 94623710:
                if (type.equals("chart")) {
                    obj = null;
                    break;
                }
                break;
        }
        InterpretationElement element;
        switch (obj) {
            case null:
                element = InterpretationElement.fromDashboardElement(interpretation, item.getChart(), "chart");
                interpretation.setType("chart");
                interpretation.setChart(element);
                break;
            case 1:
                element = InterpretationElement.fromDashboardElement(interpretation, item.getMap(), "map");
                interpretation.setType("map");
                interpretation.setMap(element);
                break;
            case 2:
                element = InterpretationElement.fromDashboardElement(interpretation, item.getReportTable(), "reportTable");
                interpretation.setType("reportTable");
                interpretation.setReportTable(element);
                break;
            default:
                throw new IllegalArgumentException("Unsupported DashboardItem type");
        }
        return interpretation;
    }

    public void updateInterpretation(String text) {
        setText(text);
        if (!(this.state == State.TO_DELETE || this.state == State.TO_POST)) {
            this.state = State.TO_UPDATE;
        }
        super.save();
    }

    public final void deleteInterpretation() {
        if (State.TO_POST.equals(getState())) {
            super.delete();
            return;
        }
        setState(State.TO_DELETE);
        super.save();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setInterpretationElements(java.util.List<org.icddrb.dhis.android.sdk.persistence.models.InterpretationElement> r9) {
        /*
        r8 = this;
        r4 = 2;
        r3 = 1;
        r1 = -1;
        r2 = 0;
        if (r9 == 0) goto L_0x000c;
    L_0x0006:
        r5 = r9.isEmpty();
        if (r5 == 0) goto L_0x000d;
    L_0x000c:
        return;
    L_0x000d:
        r5 = r8.getType();
        if (r5 == 0) goto L_0x000c;
    L_0x0013:
        r5 = r8.getType();
        r6 = "dataSetReport";
        r5 = r5.equals(r6);
        if (r5 == 0) goto L_0x0069;
    L_0x001f:
        r6 = r9.iterator();
    L_0x0023:
        r5 = r6.hasNext();
        if (r5 == 0) goto L_0x000c;
    L_0x0029:
        r0 = r6.next();
        r0 = (org.icddrb.dhis.android.sdk.persistence.models.InterpretationElement) r0;
        r5 = r0.getType();
        r7 = r5.hashCode();
        switch(r7) {
            case -991726143: goto L_0x004d;
            case -453291650: goto L_0x0057;
            case 1443183704: goto L_0x0043;
            default: goto L_0x003a;
        };
    L_0x003a:
        r5 = r1;
    L_0x003b:
        switch(r5) {
            case 0: goto L_0x003f;
            case 1: goto L_0x0061;
            case 2: goto L_0x0065;
            default: goto L_0x003e;
        };
    L_0x003e:
        goto L_0x0023;
    L_0x003f:
        r8.setDataSet(r0);
        goto L_0x0023;
    L_0x0043:
        r7 = "dataSet";
        r5 = r5.equals(r7);
        if (r5 == 0) goto L_0x003a;
    L_0x004b:
        r5 = r2;
        goto L_0x003b;
    L_0x004d:
        r7 = "period";
        r5 = r5.equals(r7);
        if (r5 == 0) goto L_0x003a;
    L_0x0055:
        r5 = r3;
        goto L_0x003b;
    L_0x0057:
        r7 = "organisationUnit";
        r5 = r5.equals(r7);
        if (r5 == 0) goto L_0x003a;
    L_0x005f:
        r5 = r4;
        goto L_0x003b;
    L_0x0061:
        r8.setPeriod(r0);
        goto L_0x0023;
    L_0x0065:
        r8.setOrganisationUnit(r0);
        goto L_0x0023;
    L_0x0069:
        r5 = r8.getType();
        r6 = r5.hashCode();
        switch(r6) {
            case -257288454: goto L_0x0096;
            case 107868: goto L_0x008c;
            case 94623710: goto L_0x0082;
            default: goto L_0x0074;
        };
    L_0x0074:
        switch(r1) {
            case 0: goto L_0x0078;
            case 1: goto L_0x00a0;
            case 2: goto L_0x00ab;
            default: goto L_0x0077;
        };
    L_0x0077:
        goto L_0x000c;
    L_0x0078:
        r1 = r9.get(r2);
        r1 = (org.icddrb.dhis.android.sdk.persistence.models.InterpretationElement) r1;
        r8.setChart(r1);
        goto L_0x000c;
    L_0x0082:
        r3 = "chart";
        r3 = r5.equals(r3);
        if (r3 == 0) goto L_0x0074;
    L_0x008a:
        r1 = r2;
        goto L_0x0074;
    L_0x008c:
        r4 = "map";
        r4 = r5.equals(r4);
        if (r4 == 0) goto L_0x0074;
    L_0x0094:
        r1 = r3;
        goto L_0x0074;
    L_0x0096:
        r3 = "reportTable";
        r3 = r5.equals(r3);
        if (r3 == 0) goto L_0x0074;
    L_0x009e:
        r1 = r4;
        goto L_0x0074;
    L_0x00a0:
        r1 = r9.get(r2);
        r1 = (org.icddrb.dhis.android.sdk.persistence.models.InterpretationElement) r1;
        r8.setMap(r1);
        goto L_0x000c;
    L_0x00ab:
        r1 = r9.get(r2);
        r1 = (org.icddrb.dhis.android.sdk.persistence.models.InterpretationElement) r1;
        r8.setReportTable(r1);
        goto L_0x000c;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.icddrb.dhis.android.sdk.persistence.models.Interpretation.setInterpretationElements(java.util.List):void");
    }

    public List<InterpretationElement> getInterpretationElements() {
        List<InterpretationElement> elements = new ArrayList();
        String type = getType();
        Object obj = -1;
        switch (type.hashCode()) {
            case -1258431572:
                if (type.equals(TYPE_DATA_SET_REPORT)) {
                    obj = 3;
                    break;
                }
                break;
            case -257288454:
                if (type.equals("reportTable")) {
                    obj = 2;
                    break;
                }
                break;
            case 107868:
                if (type.equals("map")) {
                    obj = 1;
                    break;
                }
                break;
            case 94623710:
                if (type.equals("chart")) {
                    obj = null;
                    break;
                }
                break;
        }
        switch (obj) {
            case null:
                elements.add(getChart());
                break;
            case 1:
                elements.add(getMap());
                break;
            case 2:
                elements.add(getReportTable());
                break;
            case 3:
                elements.add(getDataSet());
                elements.add(getPeriod());
                elements.add(getOrganisationUnit());
                break;
        }
        return elements;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public InterpretationElement getChart() {
        return this.chart;
    }

    public void setChart(InterpretationElement chart) {
        this.chart = chart;
    }

    public InterpretationElement getMap() {
        return this.map;
    }

    public void setMap(InterpretationElement map) {
        this.map = map;
    }

    public InterpretationElement getReportTable() {
        return this.reportTable;
    }

    public void setReportTable(InterpretationElement reportTable) {
        this.reportTable = reportTable;
    }

    public InterpretationElement getDataSet() {
        return this.dataSet;
    }

    public void setDataSet(InterpretationElement dataSet) {
        this.dataSet = dataSet;
    }

    public InterpretationElement getPeriod() {
        return this.period;
    }

    public void setPeriod(InterpretationElement period) {
        this.period = period;
    }

    public InterpretationElement getOrganisationUnit() {
        return this.organisationUnit;
    }

    public void setOrganisationUnit(InterpretationElement organisationUnit) {
        this.organisationUnit = organisationUnit;
    }

    public List<InterpretationComment> getComments() {
        return this.comments;
    }

    public void setComments(List<InterpretationComment> comments) {
        this.comments = comments;
    }
}
