package org.icddrb.dhis.android.sdk.ui.fragments.eventdataentry;

import java.util.List;
import java.util.Map;
import org.icddrb.dhis.android.sdk.persistence.models.DataValue;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramStage;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.IndicatorRow;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.StatusRow;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.DataEntryFragmentSection;

class EventDataEntryFragmentForm {
    private DataEntryFragmentSection currentSection;
    private Map<String, String> dataElementNames;
    private Map<String, DataValue> dataValues;
    private Enrollment enrollment;
    private Event event;
    private List<IndicatorRow> indicatorRows;
    private Map<String, IndicatorRow> indicatorToIndicatorRowMap;
    private List<DataEntryFragmentSection> sections;
    private ProgramStage stage;
    private StatusRow statusRow;
    private Map<String, TrackedEntityAttributeValue> trackedEntityAttributeValues;

    EventDataEntryFragmentForm() {
    }

    public Enrollment getEnrollment() {
        return this.enrollment;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return this.event;
    }

    public void setSections(List<DataEntryFragmentSection> sections) {
        this.sections = sections;
    }

    public List<DataEntryFragmentSection> getSections() {
        return this.sections;
    }

    public void setStage(ProgramStage stage) {
        this.stage = stage;
    }

    public ProgramStage getStage() {
        return this.stage;
    }

    public Map<String, DataValue> getDataValues() {
        return this.dataValues;
    }

    public void setDataValues(Map<String, DataValue> dataValues) {
        this.dataValues = dataValues;
    }

    public Map<String, String> getDataElementNames() {
        return this.dataElementNames;
    }

    public void setDataElementNames(Map<String, String> dataElementNames) {
        this.dataElementNames = dataElementNames;
    }

    public List<IndicatorRow> getIndicatorRows() {
        return this.indicatorRows;
    }

    public void setIndicatorRows(List<IndicatorRow> indicatorRows) {
        this.indicatorRows = indicatorRows;
    }

    public DataEntryFragmentSection getCurrentSection() {
        return this.currentSection;
    }

    public void setCurrentSection(DataEntryFragmentSection currentSection) {
        this.currentSection = currentSection;
    }

    public StatusRow getStatusRow() {
        return this.statusRow;
    }

    public void setStatusRow(StatusRow statusRow) {
        this.statusRow = statusRow;
    }

    public Map<String, IndicatorRow> getIndicatorToIndicatorRowMap() {
        return this.indicatorToIndicatorRowMap;
    }

    public void setIndicatorToIndicatorRowMap(Map<String, IndicatorRow> indicatorToIndicatorRowMap) {
        this.indicatorToIndicatorRowMap = indicatorToIndicatorRowMap;
    }

    public Map<String, TrackedEntityAttributeValue> getTrackedEntityAttributeValues() {
        return this.trackedEntityAttributeValues;
    }

    public void setTrackedEntityAttributeValues(Map<String, TrackedEntityAttributeValue> trackedEntityAttributeValues) {
        this.trackedEntityAttributeValues = trackedEntityAttributeValues;
    }
}
