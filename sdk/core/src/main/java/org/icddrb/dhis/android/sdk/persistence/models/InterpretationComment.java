package org.icddrb.dhis.android.sdk.persistence.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raizlabs.android.dbflow.annotation.NotNull;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.builder.Condition.Operation;
import com.raizlabs.android.dbflow.sql.builder.ConditionQueryBuilder;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import org.icddrb.dhis.android.sdk.persistence.models.meta.State;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class InterpretationComment extends BaseMetaDataObject {
    Interpretation interpretation;
    @NotNull
    State state = State.SYNCED;
    @JsonProperty("text")
    String text;
    @JsonProperty("user")
    User user;

    public final class Adapter extends ModelAdapter<InterpretationComment> {
        public Class<InterpretationComment> getModelClass() {
            return InterpretationComment.class;
        }

        public String getTableName() {
            return Table.TABLE_NAME;
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `InterpretationComment` (`NAME`, `DISPLAYNAME`, `CREATED`, `LASTUPDATED`, `ACCESS`, `ID`, `TEXT`, `user`, `interpretation`, `STATE`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, InterpretationComment model) {
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
            if (model.user == null) {
                statement.bindNull(8);
            } else if (model.user.id != null) {
                statement.bindString(8, model.user.id);
            } else {
                statement.bindNull(8);
            }
            if (model.interpretation == null) {
                statement.bindNull(9);
            } else if (model.interpretation.id != null) {
                statement.bindString(9, model.interpretation.id);
            } else {
                statement.bindNull(9);
            }
            State modelstate = model.state;
            if (modelstate != null) {
                statement.bindString(10, modelstate.name());
            } else {
                statement.bindNull(10);
            }
        }

        public void bindToContentValues(ContentValues contentValues, InterpretationComment model) {
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
            if (model.user == null) {
                contentValues.putNull("user");
            } else if (model.user.id != null) {
                contentValues.put("user", model.user.id);
            } else {
                contentValues.putNull("user");
            }
            if (model.interpretation == null) {
                contentValues.putNull("interpretation");
            } else if (model.interpretation.id != null) {
                contentValues.put("interpretation", model.interpretation.id);
            } else {
                contentValues.putNull("interpretation");
            }
            State modelstate = model.state;
            if (modelstate != null) {
                contentValues.put("state", modelstate.name());
            } else {
                contentValues.putNull("state");
            }
        }

        public void bindToInsertValues(ContentValues contentValues, InterpretationComment model) {
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
            if (model.user == null) {
                contentValues.putNull("user");
            } else if (model.user.id != null) {
                contentValues.put("user", model.user.id);
            } else {
                contentValues.putNull("user");
            }
            if (model.interpretation == null) {
                contentValues.putNull("interpretation");
            } else if (model.interpretation.id != null) {
                contentValues.put("interpretation", model.interpretation.id);
            } else {
                contentValues.putNull("interpretation");
            }
            State modelstate = model.state;
            if (modelstate != null) {
                contentValues.put("state", modelstate.name());
            } else {
                contentValues.putNull("state");
            }
        }

        public boolean exists(InterpretationComment model) {
            return new Select().from(InterpretationComment.class).where(getPrimaryModelWhere(model)).hasData();
        }

        public void loadFromCursor(Cursor cursor, InterpretationComment model) {
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
            int indexuser = cursor.getColumnIndex("user");
            if (!(indexuser == -1 || cursor.isNull(indexuser))) {
                model.user = (User) new Select().from(User.class).where().and(Condition.column("id").is(cursor.getString(indexuser))).querySingle();
            }
            int indexinterpretation = cursor.getColumnIndex("interpretation");
            if (!(indexinterpretation == -1 || cursor.isNull(indexinterpretation))) {
                model.interpretation = (Interpretation) new Select().from(Interpretation.class).where().and(Condition.column("id").is(cursor.getString(indexinterpretation))).querySingle();
            }
            int indexstate = cursor.getColumnIndex("state");
            if (indexstate != -1) {
                model.state = State.valueOf(cursor.getString(indexstate));
            }
        }

        public ConditionQueryBuilder<InterpretationComment> getPrimaryModelWhere(InterpretationComment model) {
            return new ConditionQueryBuilder(InterpretationComment.class, Condition.column("id").is(model.id));
        }

        public ConditionQueryBuilder<InterpretationComment> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(InterpretationComment.class, Condition.column("id").is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return String.format("CREATE TABLE IF NOT EXISTS `InterpretationComment`(`name` TEXT, `displayName` TEXT, `created` TEXT, `lastUpdated` TEXT, `access` TEXT, `id` TEXT, `text` TEXT,  `user` TEXT,  `interpretation` TEXT, `state` TEXT NOT NULL ON CONFLICT FAIL, PRIMARY KEY(`id`), FOREIGN KEY(`user`) REFERENCES `%1s` (`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`interpretation`) REFERENCES `%1s` (`id`) ON UPDATE NO ACTION ON DELETE CASCADE );", new Object[]{FlowManager.getTableName(User.class), FlowManager.getTableName(Interpretation.class)});
        }

        public final InterpretationComment newInstance() {
            return new InterpretationComment();
        }
    }

    public final class Table {
        public static final String ACCESS = "access";
        public static final String CREATED = "created";
        public static final String DISPLAYNAME = "displayName";
        public static final String ID = "id";
        public static final String INTERPRETATION_INTERPRETATION = "interpretation";
        public static final String LASTUPDATED = "lastUpdated";
        public static final String NAME = "name";
        public static final String STATE = "state";
        public static final String TABLE_NAME = "InterpretationComment";
        public static final String TEXT = "text";
        public static final String USER_USER = "user";
    }

    public final void deleteComment() {
        if (State.TO_POST.equals(getState())) {
            super.delete();
            return;
        }
        setState(State.TO_DELETE);
        super.save();
    }

    public final void updateComment(String newText) {
        setText(newText);
        if (!(this.state == State.TO_DELETE || this.state == State.TO_POST)) {
            this.state = State.TO_UPDATE;
        }
        super.save();
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Interpretation getInterpretation() {
        return this.interpretation;
    }

    public void setInterpretation(Interpretation interpretation) {
        this.interpretation = interpretation;
    }

    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
