package org.icddrb.dhis.android.eregistry.fragments.enrollment;

import java.util.List;
import java.util.Map;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.OrganisationUnit;
import org.icddrb.dhis.android.sdk.persistence.models.Program;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.IndicatorRow;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.Row;

class EnrollmentDataEntryFragmentForm {
    private Map<String, String> dataElementNames;
    private List<Row> dataEntryRows;
    private Enrollment enrollment;
    private List<IndicatorRow> indicatorRows;
    private OrganisationUnit organisationUnit;
    private boolean outOfTrackedEntityAttributeGeneratedValues;
    private Program program;
    private Map<String, TrackedEntityAttributeValue> trackedEntityAttributeValueMap;
    private TrackedEntityInstance trackedEntityInstance;

    EnrollmentDataEntryFragmentForm() {
    }

    public List<Row> getDataEntryRows() {
        return this.dataEntryRows;
    }

    public void setDataEntryRows(List<Row> dataEntryRows) {
        this.dataEntryRows = dataEntryRows;
    }

    public Enrollment getEnrollment() {
        return this.enrollment;
    }

    public Program getProgram() {
        return this.program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public OrganisationUnit getOrganisationUnit() {
        return this.organisationUnit;
    }

    public void setOrganisationUnit(OrganisationUnit organisationUnit) {
        this.organisationUnit = organisationUnit;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    public Map<String, String> getDataElementNames() {
        return this.dataElementNames;
    }

    public void setDataElementNames(Map<String, String> dataElementNames) {
        this.dataElementNames = dataElementNames;
    }

    public TrackedEntityInstance getTrackedEntityInstance() {
        return this.trackedEntityInstance;
    }

    public void setTrackedEntityInstance(TrackedEntityInstance trackedEntityInstance) {
        this.trackedEntityInstance = trackedEntityInstance;
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
