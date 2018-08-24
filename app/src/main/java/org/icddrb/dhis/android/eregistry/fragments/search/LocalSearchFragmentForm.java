package org.icddrb.dhis.android.eregistry.fragments.search;

import java.util.HashMap;
import java.util.List;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttribute;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.Row;

public class LocalSearchFragmentForm {
    private HashMap<String, String> attributeValues;
    private List<Row> dataEntryRows;
    private String organisationUnitId;
    private String program;
    private String queryString;
    private List<TrackedEntityAttributeValue> trackedEntityAttributeValues;
    private List<TrackedEntityAttribute> trackedEntityAttributes;

    public String getOrganisationUnitId() {
        return this.organisationUnitId;
    }

    public void setOrganisationUnitId(String organisationUnitId) {
        this.organisationUnitId = organisationUnitId;
    }

    public HashMap<String, String> getAttributeValues() {
        return this.attributeValues;
    }

    public void setAttributeValues(HashMap<String, String> attributeValues) {
        this.attributeValues = attributeValues;
    }

    public List<TrackedEntityAttribute> getTrackedEntityAttributes() {
        return this.trackedEntityAttributes;
    }

    public void setTrackedEntityAttributes(List<TrackedEntityAttribute> trackedEntityAttributes) {
        this.trackedEntityAttributes = trackedEntityAttributes;
    }

    public List<Row> getDataEntryRows() {
        return this.dataEntryRows;
    }

    public void setDataEntryRows(List<Row> dataEntryRows) {
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
