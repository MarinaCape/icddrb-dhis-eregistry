package org.icddrb.dhis.android.sdk.ui.dialogs;

import java.util.List;
import java.util.Map;
import org.icddrb.dhis.android.sdk.persistence.models.DataValue;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttribute;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.DataEntryRow;

class QueryTrackedEntityInstancesResultDialogFragmentForm {
    private Map<String, DataValue> attributeValues;
    private List<DataEntryRow> dataEntryRows;
    private String organisationUnit;
    private String program;
    private String queryString;
    private List<TrackedEntityAttributeValue> trackedEntityAttributeValues;
    private List<TrackedEntityAttribute> trackedEntityAttributes;

    QueryTrackedEntityInstancesResultDialogFragmentForm() {
    }

    public String getOrganisationUnit() {
        return this.organisationUnit;
    }

    public void setOrganisationUnit(String organisationUnit) {
        this.organisationUnit = organisationUnit;
    }

    public Map<String, DataValue> getAttributeValues() {
        return this.attributeValues;
    }

    public void setAttributeValues(Map<String, DataValue> attributeValues) {
        this.attributeValues = attributeValues;
    }

    public List<TrackedEntityAttribute> getTrackedEntityAttributes() {
        return this.trackedEntityAttributes;
    }

    public void setTrackedEntityAttributes(List<TrackedEntityAttribute> trackedEntityAttributes) {
        this.trackedEntityAttributes = trackedEntityAttributes;
    }

    public List<DataEntryRow> getDataEntryRows() {
        return this.dataEntryRows;
    }

    public void setDataEntryRows(List<DataEntryRow> dataEntryRows) {
        this.dataEntryRows = dataEntryRows;
    }

    public List<TrackedEntityAttributeValue> getTrackedEntityAttributeValues() {
        return this.trackedEntityAttributeValues;
    }

    public void setTrackedEntityAttributeValues(List<TrackedEntityAttributeValue> trackedEntityAttributeValues) {
        this.trackedEntityAttributeValues = trackedEntityAttributeValues;
    }

    public String getProgram() {
        return this.program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getQueryString() {
        return this.queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }
}
