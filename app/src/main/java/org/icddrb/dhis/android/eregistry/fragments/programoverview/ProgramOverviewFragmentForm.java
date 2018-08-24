package org.icddrb.dhis.android.eregistry.fragments.programoverview;

import java.util.List;
import java.util.Map;
import org.icddrb.dhis.android.eregistry.ui.rows.programoverview.ProgramStageRow;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.Program;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramIndicator;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.IndicatorRow;

class ProgramOverviewFragmentForm {
    private String attribute1Label;
    private String attribute1Value;
    private String attribute2Label;
    private String attribute2Value;
    private String attribute3Label;
    private String attribute3Value;
    private String dateOfEnrollmentLabel;
    private String dateOfEnrollmentValue;
    private Enrollment enrollment;
    private String incidentDateLabel;
    private String incidentDateValue;
    private Program program;
    private Map<ProgramIndicator, IndicatorRow> programIndicatorRows;
    private List<ProgramStageRow> programStageRows;
    private TrackedEntityInstance trackedEntityInstance;

    ProgramOverviewFragmentForm() {
    }

    public Enrollment getEnrollment() {
        return this.enrollment;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    public Program getProgram() {
        return this.program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public TrackedEntityInstance getTrackedEntityInstance() {
        return this.trackedEntityInstance;
    }

    public void setTrackedEntityInstance(TrackedEntityInstance trackedEntityInstance) {
        this.trackedEntityInstance = trackedEntityInstance;
    }

    public String getDateOfEnrollmentLabel() {
        return this.dateOfEnrollmentLabel;
    }

    public void setDateOfEnrollmentLabel(String dateOfEnrollmentLabel) {
        this.dateOfEnrollmentLabel = dateOfEnrollmentLabel;
    }

    public String getDateOfEnrollmentValue() {
        return this.dateOfEnrollmentValue;
    }

    public void setDateOfEnrollmentValue(String dateOfEnrollmentValue) {
        this.dateOfEnrollmentValue = dateOfEnrollmentValue;
    }

    public String getIncidentDateLabel() {
        return this.incidentDateLabel;
    }

    public void setIncidentDateLabel(String incidentDateLabel) {
        this.incidentDateLabel = incidentDateLabel;
    }

    public String getIncidentDateValue() {
        return this.incidentDateValue;
    }

    public void setIncidentDateValue(String incidentDateValue) {
        this.incidentDateValue = incidentDateValue;
    }

    public String getAttribute1Label() {
        return this.attribute1Label;
    }

    public void setAttribute1Label(String attribute1Label) {
        this.attribute1Label = attribute1Label;
    }

    public String getAttribute1Value() {
        return this.attribute1Value;
    }

    public void setAttribute1Value(String attribute1Value) {
        this.attribute1Value = attribute1Value;
    }

    public String getAttribute2Label() {
        return this.attribute2Label;
    }

    public void setAttribute2Label(String attribute2Label) {
        this.attribute2Label = attribute2Label;
    }

    public String getAttribute2Value() {
        return this.attribute2Value;
    }

    public void setAttribute2Value(String attribute2Value) {
        this.attribute2Value = attribute2Value;
    }

    public String getAttribute3Label() {
        return this.attribute3Label;
    }

    public void setAttribute3Label(String attribute3Label) {
        this.attribute3Label = attribute3Label;
    }

    public String getAttribute3Value() {
        return this.attribute3Value;
    }

    public void setAttribute3Value(String attribute3Value) {
        this.attribute3Value = attribute3Value;
    }

    public List<ProgramStageRow> getProgramStageRows() {
        return this.programStageRows;
    }

    public void setProgramStageRows(List<ProgramStageRow> programStageRows) {
        this.programStageRows = programStageRows;
    }

    public Map<ProgramIndicator, IndicatorRow> getProgramIndicatorRows() {
        return this.programIndicatorRows;
    }

    public void setProgramIndicatorRows(Map<ProgramIndicator, IndicatorRow> programIndicatorRows) {
        this.programIndicatorRows = programIndicatorRows;
    }
}
