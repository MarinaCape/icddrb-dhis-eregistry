package org.icddrb.dhis.android.sdk.ui.fragments.selectprogram;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.persistence.models.OrganisationUnitProgramRelationship;
import org.icddrb.dhis.android.sdk.utils.Preconditions;

public final class SelectProgramFragmentPreferences {
    private static final String FILTER_ID = "key:FilterId";
    private static final String FILTER_LABEL = "key:FilterLabel";
    private static final String ORG_UNIT_ID = "key:orgUnitId";
    private static final String ORG_UNIT_LABEL = "key:orgUnitLabel";
    private static final String PROGRAM_FRAGMENT_PREFERENCES = "preferences:programFragment";
    private static final String PROGRAM_ID = "key:programId";
    private static final String PROGRAM_LABEL = "key:programLabel";
    private final SharedPreferences mPrefs;

    public SelectProgramFragmentPreferences(Context context) {
        Preconditions.isNull(context, "Context object must not be null");
        this.mPrefs = context.getSharedPreferences(PROGRAM_FRAGMENT_PREFERENCES, 0);
    }

    public void putOrgUnit(Pair<String, String> orgUnit) {
        if (orgUnit != null) {
            put(ORG_UNIT_ID, (String) orgUnit.first);
            put(ORG_UNIT_LABEL, (String) orgUnit.second);
            return;
        }
        remove(ORG_UNIT_ID);
        remove(ORG_UNIT_LABEL);
    }

    public Pair<String, String> getOrgUnit() {
        String orgUnitId = get(ORG_UNIT_ID);
        String orgUnitLabel = get(ORG_UNIT_LABEL);
        if (MetaDataController.getOrganisationUnit(orgUnitId) != null) {
            return new Pair(orgUnitId, orgUnitLabel);
        }
        putOrgUnit(null);
        putProgram(null);
        return null;
    }

    public Pair<String, String> getFilter() {
        return new Pair(get(FILTER_ID), get(FILTER_LABEL));
    }

    public void putProgram(Pair<String, String> program) {
        if (program != null) {
            put(PROGRAM_ID, (String) program.first);
            put(PROGRAM_LABEL, (String) program.second);
            return;
        }
        remove(PROGRAM_ID);
        remove(PROGRAM_LABEL);
    }

    public void putFilter(Pair<String, String> filter) {
        if (filter != null) {
            put(FILTER_ID, (String) filter.first);
            put(FILTER_LABEL, (String) filter.second);
            return;
        }
        remove(FILTER_ID);
        remove(FILTER_LABEL);
    }

    public Pair<String, String> getProgram() {
        String orgUnitId = get(ORG_UNIT_ID);
        String programId = get(PROGRAM_ID);
        String programLabel = get(PROGRAM_LABEL);
        if (new Select().count().from(OrganisationUnitProgramRelationship.class).where(Condition.column("organisationUnitId").is(orgUnitId), Condition.column("programId").is(programId)).count() > 0) {
            return new Pair(programId, programLabel);
        }
        putProgram(null);
        return null;
    }

    private void put(String key, String value) {
        this.mPrefs.edit().putString(key, value).apply();
    }

    private String get(String key) {
        return this.mPrefs.getString(key, null);
    }

    private void delete() {
        this.mPrefs.edit().clear().apply();
    }

    private void remove(String key) {
        this.mPrefs.edit().remove(key).apply();
    }
}
