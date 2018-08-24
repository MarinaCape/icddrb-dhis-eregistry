package org.icddrb.dhis.android.eregistry.fragments.trackedentityinstance;

import android.content.Context;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.icddrb.dhis.android.sdk.controllers.GpsController;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.controllers.tracker.TrackerController;
import org.icddrb.dhis.android.sdk.persistence.loaders.Query;
import org.icddrb.dhis.android.sdk.persistence.models.OrganisationUnit;
import org.icddrb.dhis.android.sdk.persistence.models.Program;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramTrackedEntityAttribute;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeGeneratedValue;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.DataEntryRowFactory;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.Row;
import org.icddrb.dhis.android.sdk.utils.api.ValueType;

class TrackedEntityInstanceDataEntryFragmentQuery implements Query<TrackedEntityInstanceDataEntryFragmentForm> {
    public static final String CLASS_TAG = TrackedEntityInstanceDataEntryFragmentQuery.class.getSimpleName();
    private TrackedEntityInstance currentTrackedEntityInstance;
    private final String mOrgUnitId;
    private final String mProgramId;
    private final long mTrackedEntityInstanceId;

    TrackedEntityInstanceDataEntryFragmentQuery(String mOrgUnitId, String mProgramId, long mTrackedEntityInstanceId) {
        this.mOrgUnitId = mOrgUnitId;
        this.mProgramId = mProgramId;
        this.mTrackedEntityInstanceId = mTrackedEntityInstanceId;
    }

    public TrackedEntityInstanceDataEntryFragmentForm query(Context context) {
        TrackedEntityInstanceDataEntryFragmentForm mForm = new TrackedEntityInstanceDataEntryFragmentForm();
        Program mProgram = MetaDataController.getProgram(this.mProgramId);
        OrganisationUnit mOrgUnit = MetaDataController.getOrganisationUnit(this.mOrgUnitId);
        if (!(mProgram == null || mOrgUnit == null)) {
            TrackedEntityAttributeValue trackedEntityAttributeValue;
            if (this.mTrackedEntityInstanceId < 0) {
                this.currentTrackedEntityInstance = new TrackedEntityInstance(mProgram, this.mOrgUnitId);
            } else {
                this.currentTrackedEntityInstance = TrackerController.getTrackedEntityInstance(this.mTrackedEntityInstanceId);
            }
            mForm.setProgram(mProgram);
            mForm.setOrganisationUnit(mOrgUnit);
            mForm.setDataElementNames(new HashMap());
            mForm.setDataEntryRows(new ArrayList());
            mForm.setTrackedEntityInstance(this.currentTrackedEntityInstance);
            mForm.setTrackedEntityAttributeValueMap(new HashMap());
            List<TrackedEntityAttributeValue> trackedEntityAttributeValues = new ArrayList();
            List<ProgramTrackedEntityAttribute> programTrackedEntityAttributes = mProgram.getProgramTrackedEntityAttributes();
            List<Row> dataEntryRows = new ArrayList();
            for (ProgramTrackedEntityAttribute ptea : programTrackedEntityAttributes) {
                TrackedEntityAttributeValue value = TrackerController.getTrackedEntityAttributeValue(ptea.getTrackedEntityAttributeId(), this.currentTrackedEntityInstance.getLocalId());
                if (value != null) {
                    trackedEntityAttributeValues.add(value);
                } else if (MetaDataController.getTrackedEntityAttribute(ptea.getTrackedEntityAttributeId()).isGenerated()) {
                    TrackedEntityAttributeGeneratedValue trackedEntityAttributeGeneratedValue = MetaDataController.getTrackedEntityAttributeGeneratedValue(ptea.getTrackedEntityAttribute());
                    if (trackedEntityAttributeGeneratedValue != null) {
                        trackedEntityAttributeValue = new TrackedEntityAttributeValue();
                        trackedEntityAttributeValue.setTrackedEntityAttributeId(ptea.getTrackedEntityAttribute().getUid());
                        trackedEntityAttributeValue.setTrackedEntityInstanceId(this.currentTrackedEntityInstance.getUid());
                        trackedEntityAttributeValue.setValue(trackedEntityAttributeGeneratedValue.getValue());
                        trackedEntityAttributeValues.add(trackedEntityAttributeValue);
                    } else {
                        mForm.setOutOfTrackedEntityAttributeGeneratedValues(true);
                    }
                }
            }
            for (int i = 0; i < programTrackedEntityAttributes.size(); i++) {
                boolean editable = true;
                boolean shouldNeverBeEdited = false;
                if (((ProgramTrackedEntityAttribute) programTrackedEntityAttributes.get(i)).getTrackedEntityAttribute().isGenerated()) {
                    editable = false;
                    shouldNeverBeEdited = true;
                }
                if (ValueType.COORDINATE.equals(((ProgramTrackedEntityAttribute) programTrackedEntityAttributes.get(i)).getTrackedEntityAttribute().getValueType())) {
                    GpsController.activateGps(context);
                }
                boolean isRadioButton = mProgram.getDataEntryMethod();
                if (!isRadioButton) {
                    isRadioButton = ((ProgramTrackedEntityAttribute) programTrackedEntityAttributes.get(i)).isRenderOptionsAsRadio();
                }
                Row row = DataEntryRowFactory.createDataEntryView(((ProgramTrackedEntityAttribute) programTrackedEntityAttributes.get(i)).getMandatory(), ((ProgramTrackedEntityAttribute) programTrackedEntityAttributes.get(i)).getAllowFutureDate(), ((ProgramTrackedEntityAttribute) programTrackedEntityAttributes.get(i)).getTrackedEntityAttribute().getOptionSet(), ((ProgramTrackedEntityAttribute) programTrackedEntityAttributes.get(i)).getTrackedEntityAttribute().getName(), getTrackedEntityDataValue(((ProgramTrackedEntityAttribute) programTrackedEntityAttributes.get(i)).getTrackedEntityAttribute().getUid(), trackedEntityAttributeValues), ((ProgramTrackedEntityAttribute) programTrackedEntityAttributes.get(i)).getTrackedEntityAttribute().getValueType(), editable, shouldNeverBeEdited, isRadioButton, context);
                row.setOrg(this.mOrgUnitId);
                dataEntryRows.add(row);
            }
            for (TrackedEntityAttributeValue trackedEntityAttributeValue2 : trackedEntityAttributeValues) {
                mForm.getTrackedEntityAttributeValueMap().put(trackedEntityAttributeValue2.getTrackedEntityAttributeId(), trackedEntityAttributeValue2);
            }
            mForm.setDataEntryRows(dataEntryRows);
        }
        return mForm;
    }

    public TrackedEntityAttributeValue getTrackedEntityDataValue(String trackedEntityAttribute, List<TrackedEntityAttributeValue> trackedEntityAttributeValues) {
        for (TrackedEntityAttributeValue trackedEntityAttributeValue : trackedEntityAttributeValues) {
            if (trackedEntityAttributeValue.getTrackedEntityAttributeId().equals(trackedEntityAttribute)) {
                return trackedEntityAttributeValue;
            }
        }
        TrackedEntityAttributeValue trackedEntityAttributeValue2 = new TrackedEntityAttributeValue();
        trackedEntityAttributeValue2.setTrackedEntityAttributeId(trackedEntityAttribute);
        trackedEntityAttributeValue2.setTrackedEntityInstanceId(this.currentTrackedEntityInstance.getTrackedEntityInstance());
        trackedEntityAttributeValue2.setValue("");
        trackedEntityAttributeValues.add(trackedEntityAttributeValue2);
        return trackedEntityAttributeValue2;
    }
}
