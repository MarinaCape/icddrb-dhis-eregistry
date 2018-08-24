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

public class User extends BaseMetaDataObject {
    @JsonProperty("firstName")
    String firstName;
    @JsonProperty("organisationUnits")
    List<OrganisationUnit> organisationUnits;
    @JsonProperty("surName")
    String surName;
    @JsonProperty("userCredentials")
    UserCredentials userCredentials;

    public final class Adapter extends ModelAdapter<User> {
        public Class<User> getModelClass() {
            return User.class;
        }

        public String getTableName() {
            return Table.TABLE_NAME;
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `User` (`NAME`, `DISPLAYNAME`, `CREATED`, `LASTUPDATED`, `ACCESS`, `ID`, `FIRSTNAME`, `SURNAME`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, User model) {
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
            if (model.firstName != null) {
                statement.bindString(7, model.firstName);
            } else {
                statement.bindNull(7);
            }
            if (model.surName != null) {
                statement.bindString(8, model.surName);
            } else {
                statement.bindNull(8);
            }
        }

        public void bindToContentValues(ContentValues contentValues, User model) {
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
            if (model.firstName != null) {
                contentValues.put("firstName", model.firstName);
            } else {
                contentValues.putNull("firstName");
            }
            if (model.surName != null) {
                contentValues.put(Table.SURNAME, model.surName);
            } else {
                contentValues.putNull(Table.SURNAME);
            }
        }

        public void bindToInsertValues(ContentValues contentValues, User model) {
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
            if (model.firstName != null) {
                contentValues.put("firstName", model.firstName);
            } else {
                contentValues.putNull("firstName");
            }
            if (model.surName != null) {
                contentValues.put(Table.SURNAME, model.surName);
            } else {
                contentValues.putNull(Table.SURNAME);
            }
        }

        public boolean exists(User model) {
            return new Select().from(User.class).where(getPrimaryModelWhere(model)).hasData();
        }

        public void loadFromCursor(Cursor cursor, User model) {
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
            int indexfirstName = cursor.getColumnIndex("firstName");
            if (indexfirstName != -1) {
                if (cursor.isNull(indexfirstName)) {
                    model.firstName = null;
                } else {
                    model.firstName = cursor.getString(indexfirstName);
                }
            }
            int indexsurName = cursor.getColumnIndex(Table.SURNAME);
            if (indexsurName == -1) {
                return;
            }
            if (cursor.isNull(indexsurName)) {
                model.surName = null;
            } else {
                model.surName = cursor.getString(indexsurName);
            }
        }

        public ConditionQueryBuilder<User> getPrimaryModelWhere(User model) {
            return new ConditionQueryBuilder(User.class, Condition.column("id").is(model.id));
        }

        public ConditionQueryBuilder<User> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(User.class, Condition.column("id").is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return "CREATE TABLE IF NOT EXISTS `User`(`name` TEXT, `displayName` TEXT, `created` TEXT, `lastUpdated` TEXT, `access` TEXT, `id` TEXT, `firstName` TEXT, `surName` TEXT, PRIMARY KEY(`id`));";
        }

        public final User newInstance() {
            return new User();
        }
    }

    public final class Table {
        public static final String ACCESS = "access";
        public static final String CREATED = "created";
        public static final String DISPLAYNAME = "displayName";
        public static final String FIRSTNAME = "firstName";
        public static final String ID = "id";
        public static final String LASTUPDATED = "lastUpdated";
        public static final String NAME = "name";
        public static final String SURNAME = "surName";
        public static final String TABLE_NAME = "User";
    }

    public UserCredentials getUserCredential() {
        return this.userCredentials;
    }

    public List<OrganisationUnit> getOrganisationUnits() {
        return this.organisationUnits;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurName() {
        return this.surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }
}
