package org.icddrb.dhis.android.eregistry.fragments.trackedentityinstanceprofile;

import java.util.List;
import java.util.Map;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.Program;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.Row;

public class TrackedEntityInstanceProfileFragmentForm {
    private List<Row> mDataEntryRows;
    private Enrollment mEnrollment;
    private Program mProgram;
    private TrackedEntityInstance mTrackedEntityInstance;
    private boolean outOfTrackedEntityAttributeGeneratedValues;
    private Map<String, TrackedEntityAttributeValue> trackedEntityAttributeValueMap;
    private List<TrackedEntityAttributeValue> trackedEntityAttributeValues;

    public List<TrackedEntityAttributeValue> getTrackedEntityAttributeValues() {
        return this.trackedEntityAttributeValues;
    }

    public void setTrackedEntityAttributeValues(List<TrackedEntityAttributeValue> trackedEntityAttributeValues) {
        this.trackedEntityAttributeValues = trackedEntityAttributeValues;
    }

    public Enrollment getEnrollment() {
        return this.mEnrollment;
    }

    public void setEnrollment(Enrollment mEnrollment) {
        this.mEnrollment = mEnrollment;
    }

    public Program getProgram() {
        return this.mProgram;
    }

    public void setProgram(Program mProgram) {
        this.mProgram = mProgram;
    }

    public TrackedEntityInstance getTrackedEntityInstance() {
        return this.mTrackedEntityInstance;
    }

    public void setTrackedEntityInstance(TrackedEntityInstance mTrackedEntityInstance) {
        this.mTrackedEntityInstance = mTrackedEntityInstance;
    }

    public List<Row> getDataEntryRows() {
        return this.mDataEntryRows;
    }

    public void setDataEntryRows(List<Row> mDataEntryRows) {
        this.mDataEntryRows = mDataEntryRows;
    }

    public Map<String, TrackedEntityAttributeValue> getTrackedEntityAttributeValueMap() {
        return this.trackedEntityAttributeValueMap;
    }

    public void setTrackedEntityAttributeValueMap(Map<String, TrackedEntityAttributeValue> trackedEntityAttributeValueMap) {
        this.trackedEntityAttributeValueMap = trackedEntityAttributeValueMap;
    }

    public void setOutOfTrackedEntityAttributeGeneratedValues(boolean outOfTrackedEntityAttributeGeneratedValues) {
        this.outOfTrackedEntityAttributeGeneratedValues = outOfTrackedEntityAttributeGeneratedValues;
    }

    public boolean isOutOfTrackedEntityAttributeGeneratedValues() {
        return this.outOfTrackedEntityAttributeGeneratedValues;
    }
}
