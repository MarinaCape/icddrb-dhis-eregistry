package org.icddrb.dhis.android.eregistry.fragments.trackedentityinstanceprofile;

import android.content.Context;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.icddrb.dhis.android.sdk.controllers.GpsController;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.controllers.tracker.TrackerController;
import org.icddrb.dhis.android.sdk.persistence.loaders.Query;
import org.icddrb.dhis.android.sdk.persistence.models.Program;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramTrackedEntityAttribute;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.DataEntryRowFactory;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.Row;
import org.icddrb.dhis.android.sdk.utils.api.ValueType;

public class TrackedEntityInstanceProfileFragmentQuery implements Query<TrackedEntityInstanceProfileFragmentForm> {
    private TrackedEntityInstance currentTrackedEntityInstance;
    private boolean editable;
    private String mOrgId;
    private String mProgramId;
    private long mTrackedEntityInstanceId;

    public TrackedEntityInstanceProfileFragmentQuery(long mTrackedEntityInstanceId, String mProgramId, String mOrgId) {
        this.mTrackedEntityInstanceId = mTrackedEntityInstanceId;
        this.mProgramId = mProgramId;
        this.mOrgId = mOrgId;
    }

    public TrackedEntityInstanceProfileFragmentForm query(Context context) {
        TrackedEntityInstanceProfileFragmentForm mForm = new TrackedEntityInstanceProfileFragmentForm();
        Program mProgram = MetaDataController.getProgram(this.mProgramId);
        TrackedEntityInstance mTrackedEntityInstance = TrackerController.getTrackedEntityInstance(this.mTrackedEntityInstanceId);
        if (!(mProgram == null || mTrackedEntityInstance == null)) {
            this.currentTrackedEntityInstance = mTrackedEntityInstance;
            mForm.setProgram(mProgram);
            mForm.setTrackedEntityInstance(mTrackedEntityInstance);
            mForm.setTrackedEntityAttributeValueMap(new HashMap());
            List<TrackedEntityAttributeValue> trackedEntityAttributeValues = TrackerController.getProgramTrackedEntityAttributeValues(mProgram, mTrackedEntityInstance);
            List<ProgramTrackedEntityAttribute> programTrackedEntityAttributes = MetaDataController.getProgramTrackedEntityAttributes(this.mProgramId);
            if (!(trackedEntityAttributeValues == null && programTrackedEntityAttributes == null)) {
                mForm.setTrackedEntityAttributeValues(trackedEntityAttributeValues);
                List<Row> dataEntryRows = new ArrayList();
                for (int i = 0; i < programTrackedEntityAttributes.size(); i++) {
                    boolean shouldNeverBeEdited = false;
                    if (((ProgramTrackedEntityAttribute) programTrackedEntityAttributes.get(i)).getTrackedEntityAttribute().isGenerated()) {
                        shouldNeverBeEdited = true;
                    }
                    if (ValueType.COORDINATE.equals(((ProgramTrackedEntityAttribute) programTrackedEntityAttributes.get(i)).getTrackedEntityAttribute().getValueType())) {
                        GpsController.activateGps(context);
                    }
                    boolean isRadioButton = mProgram.getDataEntryMethod();
                    if (!isRadioButton) {
                        isRadioButton = ((ProgramTrackedEntityAttribute) programTrackedEntityAttributes.get(i)).isRenderOptionsAsRadio();
                    }
                    Row row = DataEntryRowFactory.createDataEntryView(((ProgramTrackedEntityAttribute) programTrackedEntityAttributes.get(i)).getMandatory(), ((ProgramTrackedEntityAttribute) programTrackedEntityAttributes.get(i)).getAllowFutureDate(), ((ProgramTrackedEntityAttribute) programTrackedEntityAttributes.get(i)).getTrackedEntityAttribute().getOptionSet(), ((ProgramTrackedEntityAttribute) programTrackedEntityAttributes.get(i)).getTrackedEntityAttribute().getName(), getTrackedEntityDataValue(((ProgramTrackedEntityAttribute) programTrackedEntityAttributes.get(i)).getTrackedEntityAttribute().getUid(), trackedEntityAttributeValues), ((ProgramTrackedEntityAttribute) programTrackedEntityAttributes.get(i)).getTrackedEntityAttribute().getValueType(), false, shouldNeverBeEdited, isRadioButton, context);
                    row.setOrg(this.mOrgId);
                    dataEntryRows.add(row);
                }
                if (trackedEntityAttributeValues != null) {
                    for (TrackedEntityAttributeValue trackedEntityAttributeValue : trackedEntityAttributeValues) {
                        mForm.getTrackedEntityAttributeValueMap().put(trackedEntityAttributeValue.getTrackedEntityAttributeId(), trackedEntityAttributeValue);
                    }
                }
                mForm.setDataEntryRows(dataEntryRows);
            }
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
        trackedEntityAttributeValue2.setLocalTrackedEntityInstanceId(this.currentTrackedEntityInstance.getLocalId());
        trackedEntityAttributeValue2.setValue("");
        trackedEntityAttributeValues.add(trackedEntityAttributeValue2);
        return trackedEntityAttributeValue2;
    }
}
