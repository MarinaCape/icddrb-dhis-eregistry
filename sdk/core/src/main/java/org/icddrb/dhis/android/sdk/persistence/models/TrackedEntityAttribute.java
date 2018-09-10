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
import java.util.Map;
import org.icddrb.dhis.android.sdk.utils.api.ValueType;

public class TrackedEntityAttribute extends BaseNameableObject {
    @JsonProperty("confidential")
    boolean confidential;
    @JsonProperty("dimension")
    String dimension;
    @JsonProperty("displayInListNoProgram")
    boolean displayInListNoProgram;
    @JsonProperty("displayOnVisitSchedule")
    boolean displayOnVisitSchedule;
    @JsonProperty("externalAccess")
    boolean externalAccess;
    @JsonProperty("generated")
    boolean generated;
    @JsonProperty("inherit")
    boolean inherit;
    @JsonProperty("unique")
    boolean isUnique;
    String optionSet;
    @JsonProperty("optionSetValue")
    boolean optionSetValue;
    @JsonProperty("orgunitScope")
    boolean orgunitScope;
    @JsonProperty("pattern")
    String pattern;
    @JsonProperty("programScope")
    boolean programScope;
    @JsonProperty("sortOrderInListNoProgram")
    int sortOrderInListNoProgram;
    @JsonProperty("sortOrderVisitSchedule")
    int sortOrderVisitSchedule;
    @JsonProperty("valueType")
    ValueType valueType;

    public final class Adapter extends ModelAdapter<TrackedEntityAttribute> {
        public Class<TrackedEntityAttribute> getModelClass() {
            return TrackedEntityAttribute.class;
        }

        public String getTableName() {
            return Table.TABLE_NAME;
        }

        protected final String getInsertStatementQuery() {
            return "INSERT INTO `TrackedEntityAttribute` (`NAME`, `DISPLAYNAME`, `CREATED`, `LASTUPDATED`, `ACCESS`, `ID`, `SHORTNAME`, `DESCRIPTION`, `OPTIONSET`, `ISUNIQUE`, `PROGRAMSCOPE`, `ORGUNITSCOPE`, `DISPLAYINLISTNOPROGRAM`, `DISPLAYONVISITSCHEDULE`, `EXTERNALACCESS`, `VALUETYPE`, `OPTIONSETVALUE`, `CONFIDENTIAL`, `INHERIT`, `SORTORDERVISITSCHEDULE`, `DIMENSION`, `SORTORDERINLISTNOPROGRAM`, `GENERATED`, `PATTERN`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }

        public void bindToStatement(SQLiteStatement statement, TrackedEntityAttribute model) {
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
            if (model.optionSet != null) {
                statement.bindString(9, model.optionSet);
            } else {
                statement.bindNull(9);
            }
            Object modelisUnique = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.isUnique));
            if (modelisUnique != null) {
                statement.bindLong(10, (long) ((Integer) modelisUnique).intValue());
            } else {
                statement.bindNull(10);
            }
            Object modelprogramScope = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.programScope));
            if (modelprogramScope != null) {
                statement.bindLong(11, (long) ((Integer) modelprogramScope).intValue());
            } else {
                statement.bindNull(11);
            }
            Object modelorgunitScope = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.orgunitScope));
            if (modelorgunitScope != null) {
                statement.bindLong(12, (long) ((Integer) modelorgunitScope).intValue());
            } else {
                statement.bindNull(12);
            }
            Object modeldisplayInListNoProgram = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.displayInListNoProgram));
            if (modeldisplayInListNoProgram != null) {
                statement.bindLong(13, (long) ((Integer) modeldisplayInListNoProgram).intValue());
            } else {
                statement.bindNull(13);
            }
            Object modeldisplayOnVisitSchedule = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.displayOnVisitSchedule));
            if (modeldisplayOnVisitSchedule != null) {
                statement.bindLong(14, (long) ((Integer) modeldisplayOnVisitSchedule).intValue());
            } else {
                statement.bindNull(14);
            }
            Object modelexternalAccess = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.externalAccess));
            if (modelexternalAccess != null) {
                statement.bindLong(15, (long) ((Integer) modelexternalAccess).intValue());
            } else {
                statement.bindNull(15);
            }
            ValueType modelvalueType = model.valueType;
            if (modelvalueType != null) {
                statement.bindString(16, modelvalueType.name());
            } else {
                statement.bindNull(16);
            }
            Object modeloptionSetValue = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.optionSetValue));
            if (modeloptionSetValue != null) {
                statement.bindLong(17, (long) ((Integer) modeloptionSetValue).intValue());
            } else {
                statement.bindNull(17);
            }
            Object modelconfidential = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.confidential));
            if (modelconfidential != null) {
                statement.bindLong(18, (long) ((Integer) modelconfidential).intValue());
            } else {
                statement.bindNull(18);
            }
            Object modelinherit = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.inherit));
            if (modelinherit != null) {
                statement.bindLong(19, (long) ((Integer) modelinherit).intValue());
            } else {
                statement.bindNull(19);
            }
            statement.bindLong(20, (long) model.sortOrderVisitSchedule);
            if (model.dimension != null) {
                statement.bindString(21, model.dimension);
            } else {
                statement.bindNull(21);
            }
            statement.bindLong(22, (long) model.sortOrderInListNoProgram);
            Object modelgenerated = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.generated));
            if (modelgenerated != null) {
                statement.bindLong(23, (long) ((Integer) modelgenerated).intValue());
            } else {
                statement.bindNull(23);
            }
            if (model.pattern != null) {
                statement.bindString(24, model.pattern);
                return;
            }
            statement.bindNull(24);
        }

        public void bindToContentValues(ContentValues contentValues, TrackedEntityAttribute model) {
            ContentValues contentValues2 = contentValues;
            if (model.name != null) {
                contentValues2.put("name", model.name);
            } else {
                contentValues.putNull("name");
            }
            if (model.displayName != null) {
                contentValues2 = contentValues;
                contentValues2.put("displayName", model.displayName);
            } else {
                contentValues.putNull("displayName");
            }
            if (model.created != null) {
                contentValues2 = contentValues;
                contentValues2.put("created", model.created);
            } else {
                contentValues.putNull("created");
            }
            if (model.lastUpdated != null) {
                contentValues2 = contentValues;
                contentValues2.put("lastUpdated", model.lastUpdated);
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
                contentValues2 = contentValues;
                contentValues2.put("id", model.id);
            } else {
                contentValues.putNull("id");
            }
            if (model.shortName != null) {
                contentValues2 = contentValues;
                contentValues2.put("shortName", model.shortName);
            } else {
                contentValues.putNull("shortName");
            }
            if (model.description != null) {
                contentValues2 = contentValues;
                contentValues2.put("description", model.description);
            } else {
                contentValues.putNull("description");
            }
            if (model.optionSet != null) {
                contentValues2 = contentValues;
                contentValues2.put("optionSet", model.optionSet);
            } else {
                contentValues.putNull("optionSet");
            }
            Object modelisUnique = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.isUnique));
            if (modelisUnique != null) {
                contentValues.put(Table.ISUNIQUE, (Integer) modelisUnique);
            } else {
                contentValues.putNull(Table.ISUNIQUE);
            }
            Object modelprogramScope = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.programScope));
            if (modelprogramScope != null) {
                contentValues.put(Table.PROGRAMSCOPE, (Integer) modelprogramScope);
            } else {
                contentValues.putNull(Table.PROGRAMSCOPE);
            }
            Object modelorgunitScope = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.orgunitScope));
            if (modelorgunitScope != null) {
                contentValues.put(Table.ORGUNITSCOPE, (Integer) modelorgunitScope);
            } else {
                contentValues.putNull(Table.ORGUNITSCOPE);
            }
            Object modeldisplayInListNoProgram = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.displayInListNoProgram));
            if (modeldisplayInListNoProgram != null) {
                contentValues.put(Table.DISPLAYINLISTNOPROGRAM, (Integer) modeldisplayInListNoProgram);
            } else {
                contentValues.putNull(Table.DISPLAYINLISTNOPROGRAM);
            }
            Object modeldisplayOnVisitSchedule = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.displayOnVisitSchedule));
            if (modeldisplayOnVisitSchedule != null) {
                contentValues.put(Table.DISPLAYONVISITSCHEDULE, (Integer) modeldisplayOnVisitSchedule);
            } else {
                contentValues.putNull(Table.DISPLAYONVISITSCHEDULE);
            }
            Object modelexternalAccess = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.externalAccess));
            if (modelexternalAccess != null) {
                contentValues.put("externalAccess", (Integer) modelexternalAccess);
            } else {
                contentValues.putNull("externalAccess");
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
            Object modelconfidential = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.confidential));
            if (modelconfidential != null) {
                contentValues.put(Table.CONFIDENTIAL, (Integer) modelconfidential);
            } else {
                contentValues.putNull(Table.CONFIDENTIAL);
            }
            Object modelinherit = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.inherit));
            if (modelinherit != null) {
                contentValues.put(Table.INHERIT, (Integer) modelinherit);
            } else {
                contentValues.putNull(Table.INHERIT);
            }
            contentValues.put(Table.SORTORDERVISITSCHEDULE, Integer.valueOf(model.sortOrderVisitSchedule));
            if (model.dimension != null) {
                contentValues2 = contentValues;
                contentValues2.put("dimension", model.dimension);
            } else {
                contentValues.putNull("dimension");
            }
            contentValues.put(Table.SORTORDERINLISTNOPROGRAM, Integer.valueOf(model.sortOrderInListNoProgram));
            Object modelgenerated = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.generated));
            if (modelgenerated != null) {
                contentValues.put(Table.GENERATED, (Integer) modelgenerated);
            } else {
                contentValues.putNull(Table.GENERATED);
            }
            if (model.pattern != null) {
                contentValues.put(Table.PATTERN, model.pattern);
                return;
            }
            contentValues.putNull(Table.PATTERN);
        }

        public void bindToInsertValues(ContentValues contentValues, TrackedEntityAttribute model) {
            ContentValues contentValues2 = contentValues;
            if (model.name != null) {
                contentValues2.put("name", model.name);
            } else {
                contentValues.putNull("name");
            }
            if (model.displayName != null) {
                contentValues2 = contentValues;
                contentValues2.put("displayName", model.displayName);
            } else {
                contentValues.putNull("displayName");
            }
            if (model.created != null) {
                contentValues2 = contentValues;
                contentValues2.put("created", model.created);
            } else {
                contentValues.putNull("created");
            }
            if (model.lastUpdated != null) {
                contentValues2 = contentValues;
                contentValues2.put("lastUpdated", model.lastUpdated);
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
                contentValues2 = contentValues;
                contentValues2.put("id", model.id);
            } else {
                contentValues.putNull("id");
            }
            if (model.shortName != null) {
                contentValues2 = contentValues;
                contentValues2.put("shortName", model.shortName);
            } else {
                contentValues.putNull("shortName");
            }
            if (model.description != null) {
                contentValues2 = contentValues;
                contentValues2.put("description", model.description);
            } else {
                contentValues.putNull("description");
            }
            if (model.optionSet != null) {
                contentValues2 = contentValues;
                contentValues2.put("optionSet", model.optionSet);
            } else {
                contentValues.putNull("optionSet");
            }
            Object modelisUnique = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.isUnique));
            if (modelisUnique != null) {
                contentValues.put(Table.ISUNIQUE, (Integer) modelisUnique);
            } else {
                contentValues.putNull(Table.ISUNIQUE);
            }
            Object modelprogramScope = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.programScope));
            if (modelprogramScope != null) {
                contentValues.put(Table.PROGRAMSCOPE, (Integer) modelprogramScope);
            } else {
                contentValues.putNull(Table.PROGRAMSCOPE);
            }
            Object modelorgunitScope = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.orgunitScope));
            if (modelorgunitScope != null) {
                contentValues.put(Table.ORGUNITSCOPE, (Integer) modelorgunitScope);
            } else {
                contentValues.putNull(Table.ORGUNITSCOPE);
            }
            Object modeldisplayInListNoProgram = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.displayInListNoProgram));
            if (modeldisplayInListNoProgram != null) {
                contentValues.put(Table.DISPLAYINLISTNOPROGRAM, (Integer) modeldisplayInListNoProgram);
            } else {
                contentValues.putNull(Table.DISPLAYINLISTNOPROGRAM);
            }
            Object modeldisplayOnVisitSchedule = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.displayOnVisitSchedule));
            if (modeldisplayOnVisitSchedule != null) {
                contentValues.put(Table.DISPLAYONVISITSCHEDULE, (Integer) modeldisplayOnVisitSchedule);
            } else {
                contentValues.putNull(Table.DISPLAYONVISITSCHEDULE);
            }
            Object modelexternalAccess = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.externalAccess));
            if (modelexternalAccess != null) {
                contentValues.put("externalAccess", (Integer) modelexternalAccess);
            } else {
                contentValues.putNull("externalAccess");
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
            Object modelconfidential = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.confidential));
            if (modelconfidential != null) {
                contentValues.put(Table.CONFIDENTIAL, (Integer) modelconfidential);
            } else {
                contentValues.putNull(Table.CONFIDENTIAL);
            }
            Object modelinherit = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.inherit));
            if (modelinherit != null) {
                contentValues.put(Table.INHERIT, (Integer) modelinherit);
            } else {
                contentValues.putNull(Table.INHERIT);
            }
            contentValues.put(Table.SORTORDERVISITSCHEDULE, Integer.valueOf(model.sortOrderVisitSchedule));
            if (model.dimension != null) {
                contentValues2 = contentValues;
                contentValues2.put("dimension", model.dimension);
            } else {
                contentValues.putNull("dimension");
            }
            contentValues.put(Table.SORTORDERINLISTNOPROGRAM, Integer.valueOf(model.sortOrderInListNoProgram));
            Object modelgenerated = FlowManager.getTypeConverterForClass(Boolean.class).getDBValue(Boolean.valueOf(model.generated));
            if (modelgenerated != null) {
                contentValues.put(Table.GENERATED, (Integer) modelgenerated);
            } else {
                contentValues.putNull(Table.GENERATED);
            }
            if (model.pattern != null) {
                contentValues.put(Table.PATTERN, model.pattern);
                return;
            }
            contentValues.putNull(Table.PATTERN);
        }

        public boolean exists(TrackedEntityAttribute model) {
            return new Select().from(TrackedEntityAttribute.class).where(getPrimaryModelWhere(model)).hasData();
        }

        public void loadFromCursor(Cursor cursor, TrackedEntityAttribute model) {
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
            int indexoptionSet = cursor.getColumnIndex("optionSet");
            if (indexoptionSet != -1) {
                if (cursor.isNull(indexoptionSet)) {
                    model.optionSet = null;
                } else {
                    model.optionSet = cursor.getString(indexoptionSet);
                }
            }
            int indexisUnique = cursor.getColumnIndex(Table.ISUNIQUE);
            if (indexisUnique != -1) {
                model.isUnique = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexisUnique)))).booleanValue();
            }
            int indexprogramScope = cursor.getColumnIndex(Table.PROGRAMSCOPE);
            if (indexprogramScope != -1) {
                model.programScope = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexprogramScope)))).booleanValue();
            }
            int indexorgunitScope = cursor.getColumnIndex(Table.ORGUNITSCOPE);
            if (indexorgunitScope != -1) {
                model.orgunitScope = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexorgunitScope)))).booleanValue();
            }
            int indexdisplayInListNoProgram = cursor.getColumnIndex(Table.DISPLAYINLISTNOPROGRAM);
            if (indexdisplayInListNoProgram != -1) {
                model.displayInListNoProgram = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexdisplayInListNoProgram)))).booleanValue();
            }
            int indexdisplayOnVisitSchedule = cursor.getColumnIndex(Table.DISPLAYONVISITSCHEDULE);
            if (indexdisplayOnVisitSchedule != -1) {
                model.displayOnVisitSchedule = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexdisplayOnVisitSchedule)))).booleanValue();
            }
            int indexexternalAccess = cursor.getColumnIndex("externalAccess");
            if (indexexternalAccess != -1) {
                model.externalAccess = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexexternalAccess)))).booleanValue();
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
            int indexconfidential = cursor.getColumnIndex(Table.CONFIDENTIAL);
            if (indexconfidential != -1) {
                model.confidential = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexconfidential)))).booleanValue();
            }
            int indexinherit = cursor.getColumnIndex(Table.INHERIT);
            if (indexinherit != -1) {
                model.inherit = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexinherit)))).booleanValue();
            }
            int indexsortOrderVisitSchedule = cursor.getColumnIndex(Table.SORTORDERVISITSCHEDULE);
            if (indexsortOrderVisitSchedule != -1) {
                model.sortOrderVisitSchedule = cursor.getInt(indexsortOrderVisitSchedule);
            }
            int indexdimension = cursor.getColumnIndex("dimension");
            if (indexdimension != -1) {
                if (cursor.isNull(indexdimension)) {
                    model.dimension = null;
                } else {
                    model.dimension = cursor.getString(indexdimension);
                }
            }
            int indexsortOrderInListNoProgram = cursor.getColumnIndex(Table.SORTORDERINLISTNOPROGRAM);
            if (indexsortOrderInListNoProgram != -1) {
                model.sortOrderInListNoProgram = cursor.getInt(indexsortOrderInListNoProgram);
            }
            int indexgenerated = cursor.getColumnIndex(Table.GENERATED);
            if (indexgenerated != -1) {
                model.generated = ((Boolean) FlowManager.getTypeConverterForClass(Boolean.class).getModelValue(Integer.valueOf(cursor.getInt(indexgenerated)))).booleanValue();
            }
            int indexpattern = cursor.getColumnIndex(Table.PATTERN);
            if (indexpattern == -1) {
                return;
            }
            if (cursor.isNull(indexpattern)) {
                model.pattern = null;
            } else {
                model.pattern = cursor.getString(indexpattern);
            }
        }

        public ConditionQueryBuilder<TrackedEntityAttribute> getPrimaryModelWhere(TrackedEntityAttribute model) {
            return new ConditionQueryBuilder(TrackedEntityAttribute.class, Condition.column("id").is(model.id));
        }

        public ConditionQueryBuilder<TrackedEntityAttribute> createPrimaryModelWhere() {
            return new ConditionQueryBuilder(TrackedEntityAttribute.class, Condition.column("id").is(Operation.EMPTY_PARAM));
        }

        public String getCreationQuery() {
            return "CREATE TABLE IF NOT EXISTS `TrackedEntityAttribute`(`name` TEXT, `displayName` TEXT, `created` TEXT, `lastUpdated` TEXT, `access` TEXT, `id` TEXT, `shortName` TEXT, `description` TEXT, `optionSet` TEXT, `isUnique` INTEGER, `programScope` INTEGER, `orgunitScope` INTEGER, `displayInListNoProgram` INTEGER, `displayOnVisitSchedule` INTEGER, `externalAccess` INTEGER, `valueType` TEXT, `optionSetValue` INTEGER, `confidential` INTEGER, `inherit` INTEGER, `sortOrderVisitSchedule` INTEGER, `dimension` TEXT, `sortOrderInListNoProgram` INTEGER, `generated` INTEGER, `pattern` TEXT, PRIMARY KEY(`id`));";
        }

        public final TrackedEntityAttribute newInstance() {
            return new TrackedEntityAttribute("");
        }
    }

    public final class Table {
        public static final String ACCESS = "access";
        public static final String CONFIDENTIAL = "confidential";
        public static final String CREATED = "created";
        public static final String DESCRIPTION = "description";
        public static final String DIMENSION = "dimension";
        public static final String DISPLAYINLISTNOPROGRAM = "displayInListNoProgram";
        public static final String DISPLAYNAME = "displayName";
        public static final String DISPLAYONVISITSCHEDULE = "displayOnVisitSchedule";
        public static final String EXTERNALACCESS = "externalAccess";
        public static final String GENERATED = "generated";
        public static final String ID = "id";
        public static final String INHERIT = "inherit";
        public static final String ISUNIQUE = "isUnique";
        public static final String LASTUPDATED = "lastUpdated";
        public static final String NAME = "name";
        public static final String OPTIONSET = "optionSet";
        public static final String OPTIONSETVALUE = "optionSetValue";
        public static final String ORGUNITSCOPE = "orgunitScope";
        public static final String PATTERN = "pattern";
        public static final String PROGRAMSCOPE = "programScope";
        public static final String SHORTNAME = "shortName";
        public static final String SORTORDERINLISTNOPROGRAM = "sortOrderInListNoProgram";
        public static final String SORTORDERVISITSCHEDULE = "sortOrderVisitSchedule";
        public static final String TABLE_NAME = "TrackedEntityAttribute";
        public static final String VALUETYPE = "valueType";
    }

    public TrackedEntityAttribute(String id) {
        System.out.println("Norway - TA ID: " + id);
        setUid(id);
    }

    @JsonProperty("optionSet")
    public void setOptionSet(Map<String, Object> optionSet) {
        this.optionSet = (String) optionSet.get("id");
    }

    public String getOptionSet() {
        return this.optionSet;
    }

    public void setOptionSet(String optionSet) {
        this.optionSet = optionSet;
    }

    public boolean isUnique() {
        return this.isUnique;
    }

    public void setUnique(boolean unique) {
        this.isUnique = unique;
    }

    public boolean isOrgunitScope() {
        return this.orgunitScope;
    }

    public void setOrgunitScope(boolean orgunitScope) {
        this.orgunitScope = orgunitScope;
    }

    public boolean isProgramScope() {
        return this.programScope;
    }

    public void setProgramScope(boolean programScope) {
        this.programScope = programScope;
    }

    public boolean isDisplayInListNoProgram() {
        return this.displayInListNoProgram;
    }

    public void setDisplayInListNoProgram(boolean displayInListNoProgram) {
        this.displayInListNoProgram = displayInListNoProgram;
    }

    public boolean isDisplayOnVisitSchedule() {
        return this.displayOnVisitSchedule;
    }

    public void setDisplayOnVisitSchedule(boolean displayOnVisitSchedule) {
        this.displayOnVisitSchedule = displayOnVisitSchedule;
    }

    public boolean isExternalAccess() {
        return this.externalAccess;
    }

    public void setExternalAccess(boolean externalAccess) {
        this.externalAccess = externalAccess;
    }

    public ValueType getValueType() {
        return this.valueType;
    }

    public void setValueType(ValueType valueType) {
        this.valueType = valueType;
    }

    public boolean isConfidential() {
        return this.confidential;
    }

    public void setConfidential(boolean confidential) {
        this.confidential = confidential;
    }

    public boolean isOptionSetValue() {
        return this.optionSetValue;
    }

    public void setOptionSetValue(boolean optionSetValue) {
        this.optionSetValue = optionSetValue;
    }

    public boolean isInherit() {
        return this.inherit;
    }

    public void setInherit(boolean inherit) {
        this.inherit = inherit;
    }

    public int getSortOrderVisitSchedule() {
        return this.sortOrderVisitSchedule;
    }

    public void setSortOrderVisitSchedule(int sortOrderVisitSchedule) {
        this.sortOrderVisitSchedule = sortOrderVisitSchedule;
    }

    public int getSortOrderInListNoProgram() {
        return this.sortOrderInListNoProgram;
    }

    public void setSortOrderInListNoProgram(int sortOrderInListNoProgram) {
        this.sortOrderInListNoProgram = sortOrderInListNoProgram;
    }

    public String getDimension() {
        return this.dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public boolean isGenerated() {
        return this.generated;
    }

    public void setGenerated(boolean generated) {
        this.generated = generated;
    }

    public String getPattern() {
        return this.pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
