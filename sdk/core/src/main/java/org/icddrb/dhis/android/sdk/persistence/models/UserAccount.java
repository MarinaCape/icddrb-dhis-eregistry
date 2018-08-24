package org.icddrb.dhis.android.sdk.persistence.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.builder.Condition.Operation;
import com.raizlabs.android.dbflow.sql.builder.ConditionQueryBuilder;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.persistence.models.meta.State;
import org.joda.time.DateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class UserAccount extends BaseModel implements IdentifiableObject {
    private static final int LOCAL_ID = 1;
    @JsonProperty("access")
    Access access;
    @JsonProperty("birthday")
    String birthday;
    @JsonProperty("created")
    DateTime created;
    @JsonProperty("displayName")
    String displayName;
    @JsonProperty("education")
    String education;
    @JsonProperty("email")
    String email;
    @JsonProperty("employer")
    String employer;
    @JsonProperty("firstName")
    String firstName;
    @JsonProperty("gender")
    String gender;
    @JsonIgnore
    long id = 1;
    @JsonProperty("interests")
    String interests;
    @JsonProperty("introduction")
    String introduction;
    @JsonProperty("jobTitle")
    String jobTitle;
    @JsonProperty("languages")
    String languages;
    @JsonProperty("lastUpdated")
    DateTime lastUpdated;
    @JsonProperty("name")
    String name;
    @JsonProperty("organisationUnits")
    List<OrganisationUnit> organisationUnits;
    @JsonProperty("phoneNumber")
    String phoneNumber;
    @JsonIgnore
    State state = State.SYNCED;
    @JsonProperty("surname")
    String surname;
    @JsonProperty("teiSearchOrganisationUnits")
    List<OrganisationUnit> teiSearchOrganisationUnits;
    @JsonProperty("id")
    String uId;
    @JsonProperty("userCredentials")
    UserCredentials userCredentials;
    List<UserGroup> userGroups;

    public final class Adapter extends ModelAdapter<UserAccount> {
        public Class<UserAccount> getModelClass() {
            return UserAccount.class;
        }

        public String getTableName() {
            return Table.TABLE_NAME;
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `UserAccount` (`ID`, `UID`, `NAME`, `DISPLAYNAME`, `CREATED`, `LASTUPDATED`, `ACCESS`, `STATE`, `FIRSTNAME`, `SURNAME`, `GENDER`, `BIRTHDAY`, `INTRODUCTION`, `EDUCATION`, `EMPLOYER`, `INTERESTS`, `JOBTITLE`, `LANGUAGES`, `EMAIL`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, UserAccount model) {
            statement.bindLong(1, model.id);
            if (model.uId != null) {
                statement.bindString(2, model.uId);
            } else {
                statement.bindNull(2);
            }
            if (model.name != null) {
                statement.bindString(3, model.name);
            } else {
                statement.bindNull(3);
            }
            if (model.displayName != null) {
                statement.bindString(4, model.displayName);
            } else {
                statement.bindNull(4);
            }
            Object modelcreated = FlowManager.getTypeConverterForClass(DateTime.class).getDBValue(model.created);
            if (modelcreated != null) {
                statement.bindString(5, (String) modelcreated);
            } else {
                statement.bindNull(5);
            }
            Object modellastUpdated = FlowManager.getTypeConverterForClass(DateTime.class).getDBValue(model.lastUpdated);
            if (modellastUpdated != null) {
                statement.bindString(6, (String) modellastUpdated);
            } else {
                statement.bindNull(6);
            }
            Object modelaccess = FlowManager.getTypeConverterForClass(Access.class).getDBValue(model.access);
            if (modelaccess != null) {
                statement.bindString(7, (String) modelaccess);
            } else {
                statement.bindNull(7);
            }
            State modelstate = model.state;
            if (modelstate != null) {
                statement.bindString(8, modelstate.name());
            } else {
                statement.bindNull(8);
            }
            if (model.firstName != null) {
                statement.bindString(9, model.firstName);
            } else {
                statement.bindNull(9);
            }
            if (model.surname != null) {
                statement.bindString(10, model.surname);
            } else {
                statement.bindNull(10);
            }
            if (model.gender != null) {
                statement.bindString(11, model.gender);
            } else {
                statement.bindNull(11);
            }
            if (model.birthday != null) {
                statement.bindString(12, model.birthday);
            } else {
                statement.bindNull(12);
            }
            if (model.introduction != null) {
                statement.bindString(13, model.introduction);
            } else {
                statement.bindNull(13);
            }
            if (model.education != null) {
                statement.bindString(14, model.education);
            } else {
                statement.bindNull(14);
            }
            if (model.employer != null) {
                statement.bindString(15, model.employer);
            } else {
                statement.bindNull(15);
            }
            if (model.interests != null) {
                statement.bindString(16, model.interests);
            } else {
                statement.bindNull(16);
            }
            if (model.jobTitle != null) {
                statement.bindString(17, model.jobTitle);
            } else {
                statement.bindNull(17);
            }
            if (model.languages != null) {
                statement.bindString(18, model.languages);
            } else {
                statement.bindNull(18);
            }
            if (model.email != null) {
                statement.bindString(19, model.email);
            } else {
                statement.bindNull(19);
            }
        }

        public void bindToContentValues(ContentValues contentValues, UserAccount model) {
            contentValues.put("id", Long.valueOf(model.id));
            if (model.uId != null) {
                contentValues.put(Table.UID, model.uId);
            } else {
                contentValues.putNull(Table.UID);
            }
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
            Object modelcreated = FlowManager.getTypeConverterForClass(DateTime.class).getDBValue(model.created);
            if (modelcreated != null) {
                contentValues.put("created", (String) modelcreated);
            } else {
                contentValues.putNull("created");
            }
            Object modellastUpdated = FlowManager.getTypeConverterForClass(DateTime.class).getDBValue(model.lastUpdated);
            if (modellastUpdated != null) {
                contentValues.put("lastUpdated", (String) modellastUpdated);
            } else {
                contentValues.putNull("lastUpdated");
            }
            Object modelaccess = FlowManager.getTypeConverterForClass(Access.class).getDBValue(model.access);
            if (modelaccess != null) {
                contentValues.put("access", (String) modelaccess);
            } else {
                contentValues.putNull("access");
            }
            State modelstate = model.state;
            if (modelstate != null) {
                contentValues.put("state", modelstate.name());
            } else {
                contentValues.putNull("state");
            }
            if (model.firstName != null) {
                contentValues.put("firstName", model.firstName);
            } else {
                contentValues.putNull("firstName");
            }
            if (model.surname != null) {
                contentValues.put(Table.SURNAME, model.surname);
            } else {
                contentValues.putNull(Table.SURNAME);
            }
            if (model.gender != null) {
                contentValues.put(Table.GENDER, model.gender);
            } else {
                contentValues.putNull(Table.GENDER);
            }
            if (model.birthday != null) {
                contentValues.put(Table.BIRTHDAY, model.birthday);
            } else {
                contentValues.putNull(Table.BIRTHDAY);
            }
            if (model.introduction != null) {
                contentValues.put(Table.INTRODUCTION, model.introduction);
            } else {
                contentValues.putNull(Table.INTRODUCTION);
            }
            if (model.education != null) {
                contentValues.put(Table.EDUCATION, model.education);
            } else {
                contentValues.putNull(Table.EDUCATION);
            }
            if (model.employer != null) {
                contentValues.put(Table.EMPLOYER, model.employer);
            } else {
                contentValues.putNull(Table.EMPLOYER);
            }
            if (model.interests != null) {
                contentValues.put(Table.INTERESTS, model.interests);
            } else {
                contentValues.putNull(Table.INTERESTS);
            }
            if (model.jobTitle != null) {
                contentValues.put(Table.JOBTITLE, model.jobTitle);
            } else {
                contentValues.putNull(Table.JOBTITLE);
            }
            if (model.languages != null) {
                contentValues.put(Table.LANGUAGES, model.languages);
            } else {
                contentValues.putNull(Table.LANGUAGES);
            }
            if (model.email != null) {
                contentValues.put("email", model.email);
            } else {
                contentValues.putNull("email");
            }
        }

        public void bindToInsertValues(ContentValues contentValues, UserAccount model) {
            contentValues.put("id", Long.valueOf(model.id));
            if (model.uId != null) {
                contentValues.put(Table.UID, model.uId);
            } else {
                contentValues.putNull(Table.UID);
            }
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
            Object modelcreated = FlowManager.getTypeConverterForClass(DateTime.class).getDBValue(model.created);
            if (modelcreated != null) {
                contentValues.put("created", (String) modelcreated);
            } else {
                contentValues.putNull("created");
            }
            Object modellastUpdated = FlowManager.getTypeConverterForClass(DateTime.class).getDBValue(model.lastUpdated);
            if (modellastUpdated != null) {
                contentValues.put("lastUpdated", (String) modellastUpdated);
            } else {
                contentValues.putNull("lastUpdated");
            }
            Object modelaccess = FlowManager.getTypeConverterForClass(Access.class).getDBValue(model.access);
            if (modelaccess != null) {
                contentValues.put("access", (String) modelaccess);
            } else {
                contentValues.putNull("access");
            }
            State modelstate = model.state;
            if (modelstate != null) {
                contentValues.put("state", modelstate.name());
            } else {
                contentValues.putNull("state");
            }
            if (model.firstName != null) {
                contentValues.put("firstName", model.firstName);
            } else {
                contentValues.putNull("firstName");
            }
            if (model.surname != null) {
                contentValues.put(Table.SURNAME, model.surname);
            } else {
                contentValues.putNull(Table.SURNAME);
            }
            if (model.gender != null) {
                contentValues.put(Table.GENDER, model.gender);
            } else {
                contentValues.putNull(Table.GENDER);
            }
            if (model.birthday != null) {
                contentValues.put(Table.BIRTHDAY, model.birthday);
            } else {
                contentValues.putNull(Table.BIRTHDAY);
            }
            if (model.introduction != null) {
                contentValues.put(Table.INTRODUCTION, model.introduction);
            } else {
                contentValues.putNull(Table.INTRODUCTION);
            }
            if (model.education != null) {
                contentValues.put(Table.EDUCATION, model.education);
            } else {
                contentValues.putNull(Table.EDUCATION);
            }
            if (model.employer != null) {
                contentValues.put(Table.EMPLOYER, model.employer);
            } else {
                contentValues.putNull(Table.EMPLOYER);
            }
            if (model.interests != null) {
                contentValues.put(Table.INTERESTS, model.interests);
            } else {
                contentValues.putNull(Table.INTERESTS);
            }
            if (model.jobTitle != null) {
                contentValues.put(Table.JOBTITLE, model.jobTitle);
            } else {
                contentValues.putNull(Table.JOBTITLE);
            }
            if (model.languages != null) {
                contentValues.put(Table.LANGUAGES, model.languages);
            } else {
                contentValues.putNull(Table.LANGUAGES);
            }
            if (model.email != null) {
                contentValues.put("email", model.email);
            } else {
                contentValues.putNull("email");
            }
        }

        public boolean exists(UserAccount model) {
            return new Select().from(UserAccount.class).where(getPrimaryModelWhere(model)).hasData();
        }

        public void loadFromCursor(Cursor cursor, UserAccount model) {
            int indexid = cursor.getColumnIndex("id");
            if (indexid != -1) {
                model.id = cursor.getLong(indexid);
            }
            int indexuId = cursor.getColumnIndex(Table.UID);
            if (indexuId != -1) {
                if (cursor.isNull(indexuId)) {
                    model.uId = null;
                } else {
                    model.uId = cursor.getString(indexuId);
                }
            }
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
                    model.created = (DateTime) FlowManager.getTypeConverterForClass(DateTime.class).getModelValue(cursor.getString(indexcreated));
                }
            }
            int indexlastUpdated = cursor.getColumnIndex("lastUpdated");
            if (indexlastUpdated != -1) {
                if (cursor.isNull(indexlastUpdated)) {
                    model.lastUpdated = null;
                } else {
                    model.lastUpdated = (DateTime) FlowManager.getTypeConverterForClass(DateTime.class).getModelValue(cursor.getString(indexlastUpdated));
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
            int indexstate = cursor.getColumnIndex("state");
            if (indexstate != -1) {
                if (cursor.isNull(indexstate)) {
                    model.state = null;
                } else {
                    model.state = State.valueOf(cursor.getString(indexstate));
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
            int indexsurname = cursor.getColumnIndex(Table.SURNAME);
            if (indexsurname != -1) {
                if (cursor.isNull(indexsurname)) {
                    model.surname = null;
                } else {
                    model.surname = cursor.getString(indexsurname);
                }
            }
            int indexgender = cursor.getColumnIndex(Table.GENDER);
            if (indexgender != -1) {
                if (cursor.isNull(indexgender)) {
                    model.gender = null;
                } else {
                    model.gender = cursor.getString(indexgender);
                }
            }
            int indexbirthday = cursor.getColumnIndex(Table.BIRTHDAY);
            if (indexbirthday != -1) {
                if (cursor.isNull(indexbirthday)) {
                    model.birthday = null;
                } else {
                    model.birthday = cursor.getString(indexbirthday);
                }
            }
            int indexintroduction = cursor.getColumnIndex(Table.INTRODUCTION);
            if (indexintroduction != -1) {
                if (cursor.isNull(indexintroduction)) {
                    model.introduction = null;
                } else {
                    model.introduction = cursor.getString(indexintroduction);
                }
            }
            int indexeducation = cursor.getColumnIndex(Table.EDUCATION);
            if (indexeducation != -1) {
                if (cursor.isNull(indexeducation)) {
                    model.education = null;
                } else {
                    model.education = cursor.getString(indexeducation);
                }
            }
            int indexemployer = cursor.getColumnIndex(Table.EMPLOYER);
            if (indexemployer != -1) {
                if (cursor.isNull(indexemployer)) {
                    model.employer = null;
                } else {
                    model.employer = cursor.getString(indexemployer);
                }
            }
            int indexinterests = cursor.getColumnIndex(Table.INTERESTS);
            if (indexinterests != -1) {
                if (cursor.isNull(indexinterests)) {
                    model.interests = null;
                } else {
                    model.interests = cursor.getString(indexinterests);
                }
            }
            int indexjobTitle = cursor.getColumnIndex(Table.JOBTITLE);
            if (indexjobTitle != -1) {
                if (cursor.isNull(indexjobTitle)) {
                    model.jobTitle = null;
                } else {
                    model.jobTitle = cursor.getString(indexjobTitle);
                }
            }
            int indexlanguages = cursor.getColumnIndex(Table.LANGUAGES);
            if (indexlanguages != -1) {
                if (cursor.isNull(indexlanguages)) {
                    model.languages = null;
                } else {
                    model.languages = cursor.getString(indexlanguages);
                }
            }
            int indexemail = cursor.getColumnIndex("email");
            if (indexemail == -1) {
                return;
            }
            if (cursor.isNull(indexemail)) {
                model.email = null;
            } else {
                model.email = cursor.getString(indexemail);
            }
        }

        public boolean hasCachingId() {
            return true;
        }

        public Object getCachingId(UserAccount model) {
            return Long.valueOf(model.id);
        }

        public String getCachingColumnName() {
            return "id";
        }

        public Object getCachingIdFromCursorIndex(Cursor cursor, int indexid) {
            return Long.valueOf(cursor.getLong(indexid));
        }

        public ConditionQueryBuilder<UserAccount> getPrimaryModelWhere(UserAccount model) {
            return new ConditionQueryBuilder(UserAccount.class, Condition.column("id").is(Long.valueOf(model.id)));
        }

        public ConditionQueryBuilder<UserAccount> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(UserAccount.class, Condition.column("id").is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return "CREATE TABLE IF NOT EXISTS `UserAccount`(`id` INTEGER, `uId` TEXT, `name` TEXT, `displayName` TEXT, `created` TEXT, `lastUpdated` TEXT, `access` TEXT, `state` TEXT, `firstName` TEXT, `surname` TEXT, `gender` TEXT, `birthday` TEXT, `introduction` TEXT, `education` TEXT, `employer` TEXT, `interests` TEXT, `jobTitle` TEXT, `languages` TEXT, `email` TEXT, PRIMARY KEY(`id`));";
        }

        public final UserAccount newInstance() {
            return new UserAccount();
        }
    }

    public final class Table {
        public static final String ACCESS = "access";
        public static final String BIRTHDAY = "birthday";
        public static final String CREATED = "created";
        public static final String DISPLAYNAME = "displayName";
        public static final String EDUCATION = "education";
        public static final String EMAIL = "email";
        public static final String EMPLOYER = "employer";
        public static final String FIRSTNAME = "firstName";
        public static final String GENDER = "gender";
        public static final String ID = "id";
        public static final String INTERESTS = "interests";
        public static final String INTRODUCTION = "introduction";
        public static final String JOBTITLE = "jobTitle";
        public static final String LANGUAGES = "languages";
        public static final String LASTUPDATED = "lastUpdated";
        public static final String NAME = "name";
        public static final String STATE = "state";
        public static final String SURNAME = "surname";
        public static final String TABLE_NAME = "UserAccount";
        public static final String UID = "uId";
    }

    @JsonProperty("userGroups")
    public void setUserGroups(List<UserGroup> uga) {
        this.userGroups = new ArrayList();
        for (UserGroup a : uga) {
            a.uniqId = this.uId;
            a.save();
            this.userGroups.add(a);
        }
    }

    public List<Program> getPrograms() {
        Map<String, Program> programMap = new HashMap();
        if (hasOrganisationPrograms()) {
            for (OrganisationUnit o : this.organisationUnits) {
                for (Program program : o.getPrograms()) {
                    if (program.userHasAccess(this.userGroups)) {
                        programMap.put(program.getUid(), program);
                    }
                }
            }
        }
        return new ArrayList(programMap.values());
    }

    private boolean hasOrganisationPrograms() {
        if (this.organisationUnits != null && this.organisationUnits.size() > 0) {
            for (OrganisationUnit o : this.organisationUnits) {
                if (o.getPrograms().size() > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasOrganisationAccess(String orgId) {
        if (this.organisationUnits != null && this.organisationUnits.size() > 0) {
            for (OrganisationUnit o : this.organisationUnits) {
                if (o.getId().equals(orgId)) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<UserGroup> getUserGroups() {
        if (this.userGroups == null) {
            this.userGroups = MetaDataController.getUserGroups(this.uId);
        }
        return this.userGroups;
    }

    public UserCredentials getUserCredentials() {
        return this.userCredentials;
    }

    @JsonIgnore
    public static UserAccount getCurrentUserAccountFromDb() {
        return (UserAccount) new Select().from(UserAccount.class).where(Condition.column("id").is(Integer.valueOf(1))).querySingle();
    }

    @JsonIgnore
    public static User toUser(UserAccount userAccount) {
        User user = new User();
        user.setUid(userAccount.getUId());
        user.setAccess(userAccount.getAccess());
        user.setCreated(user.getCreated());
        user.setName(userAccount.getName());
        return user;
    }

    @JsonIgnore
    public long getId() {
        return this.id;
    }

    @JsonIgnore
    public void setId(long id) {
        throw new UnsupportedOperationException("You cannot set id on UserAccount");
    }

    @JsonIgnore
    public List<OrganisationUnit> getOrganisationUnits() {
        return this.organisationUnits;
    }

    @JsonIgnore
    public String getUId() {
        return this.uId;
    }

    @JsonIgnore
    public void setUId(String uId) {
        this.uId = uId;
    }

    @JsonIgnore
    public String getName() {
        return this.name;
    }

    @JsonIgnore
    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public String getDisplayName() {
        return this.displayName;
    }

    @JsonIgnore
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @JsonIgnore
    public DateTime getCreated() {
        return this.created;
    }

    @JsonIgnore
    public void setCreated(DateTime created) {
        this.created = created;
    }

    @JsonIgnore
    public DateTime getLastUpdated() {
        return this.lastUpdated;
    }

    @JsonIgnore
    public void setLastUpdated(DateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @JsonIgnore
    public Access getAccess() {
        return this.access;
    }

    @JsonIgnore
    public void setAccess(Access access) {
        this.access = access;
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
    public String getFirstName() {
        return this.firstName;
    }

    @JsonIgnore
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonIgnore
    public String getSurname() {
        return this.surname;
    }

    @JsonIgnore
    public void setSurname(String surname) {
        this.surname = surname;
    }

    @JsonIgnore
    public String getGender() {
        return this.gender;
    }

    @JsonIgnore
    public void setGender(String gender) {
        this.gender = gender;
    }

    @JsonIgnore
    public String getBirthday() {
        return this.birthday;
    }

    @JsonIgnore
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @JsonIgnore
    public String getIntroduction() {
        return this.introduction;
    }

    @JsonIgnore
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @JsonIgnore
    public String getEducation() {
        return this.education;
    }

    @JsonIgnore
    public void setEducation(String education) {
        this.education = education;
    }

    @JsonIgnore
    public String getEmployer() {
        return this.employer;
    }

    @JsonIgnore
    public void setEmployer(String employer) {
        this.employer = employer;
    }

    @JsonIgnore
    public String getInterests() {
        return this.interests;
    }

    @JsonIgnore
    public void setInterests(String interests) {
        this.interests = interests;
    }

    @JsonIgnore
    public String getJobTitle() {
        return this.jobTitle;
    }

    @JsonIgnore
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    @JsonIgnore
    public String getLanguages() {
        return this.languages;
    }

    @JsonIgnore
    public void setLanguages(String languages) {
        this.languages = languages;
    }

    @JsonIgnore
    public String getEmail() {
        return this.email;
    }

    @JsonIgnore
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIgnore
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    @JsonIgnore
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<OrganisationUnit> getTeiSearchOrganisationUnits() {
        return this.teiSearchOrganisationUnits;
    }

    public void setTeiSearchOrganisationUnits(List<OrganisationUnit> teiSearchOrganisationUnits) {
        this.teiSearchOrganisationUnits = teiSearchOrganisationUnits;
    }
}
