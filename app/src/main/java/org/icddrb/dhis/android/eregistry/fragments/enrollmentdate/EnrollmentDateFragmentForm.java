package org.icddrb.dhis.android.eregistry.fragments.enrollmentdate;

import java.util.List;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.Row;

public class EnrollmentDateFragmentForm {
    private List<Row> dataEntryRows;
    private Enrollment enrollment;

    public Enrollment getEnrollment() {
        return this.enrollment;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    public List<Row> getDataEntryRows() {
        return this.dataEntryRows;
    }

    public void setDataEntryRows(List<Row> dataEntryRows) {
        this.dataEntryRows = dataEntryRows;
    }
}
