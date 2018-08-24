package org.icddrb.dhis.android.eregistry.fragments.enrollmentdate;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;
import org.icddrb.dhis.android.sdk.controllers.tracker.TrackerController;
import org.icddrb.dhis.android.sdk.persistence.loaders.Query;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.EnrollmentDatePickerRow;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.IncidentDatePickerRow;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.Row;

public class EnrollmentDateFragmentQuery implements Query<EnrollmentDateFragmentForm> {
    private final long enrollmentId;

    public EnrollmentDateFragmentQuery(long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public EnrollmentDateFragmentForm query(Context context) {
        EnrollmentDateFragmentForm fragmentForm = new EnrollmentDateFragmentForm();
        Enrollment enrollment = TrackerController.getEnrollment(this.enrollmentId);
        if (enrollment != null) {
            List<Row> dataEntryRows = new ArrayList();
            dataEntryRows.add(new EnrollmentDatePickerRow(enrollment.getProgram().getEnrollmentDateLabel(), enrollment));
            if (enrollment.getProgram().getDisplayIncidentDate()) {
                dataEntryRows.add(new IncidentDatePickerRow(enrollment.getProgram().getIncidentDateLabel(), enrollment));
            }
            fragmentForm.setEnrollment(enrollment);
            fragmentForm.setDataEntryRows(dataEntryRows);
        }
        return fragmentForm;
    }
}
