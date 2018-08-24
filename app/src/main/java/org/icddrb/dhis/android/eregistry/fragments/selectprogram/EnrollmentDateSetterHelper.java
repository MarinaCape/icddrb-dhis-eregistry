package org.icddrb.dhis.android.eregistry.fragments.selectprogram;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.DatePicker;
import org.icddrb.dhis.android.eregistry.C0773R;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.ui.views.CustomDatePickerDialog;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class EnrollmentDateSetterHelper {
    private final Context context;
    private final IEnroller enroller;
    private DateTime enrollmentDate;
    private final String enrollmentDateLabel;
    private final boolean enrollmentDatesInFuture;
    private DateTime incidentDate;
    private final String incidentDateLabel;
    private final boolean incidentDatesInFuture;
    private final boolean showIncidentDate;
    private TrackedEntityInstance trackedEntityInstance;

    /* renamed from: org.icddrb.dhis.android.eregistry.fragments.selectprogram.EnrollmentDateSetterHelper$2 */
    class C08062 implements OnClickListener {
        C08062() {
        }

        public void onClick(DialogInterface dialog, int which) {
        }
    }

    /* renamed from: org.icddrb.dhis.android.eregistry.fragments.selectprogram.EnrollmentDateSetterHelper$4 */
    class C08084 implements OnClickListener {
        C08084() {
        }

        public void onClick(DialogInterface dialog, int which) {
        }
    }

    public EnrollmentDateSetterHelper(IEnroller enroller, Context context, boolean showIncidentDate, boolean enrollmentDatesInFuture, boolean incidentDatesInFuture, String enrollmentDateLabel, String incidentDateLabel) {
        this.enroller = enroller;
        this.context = context;
        this.showIncidentDate = showIncidentDate;
        this.enrollmentDatesInFuture = enrollmentDatesInFuture;
        this.incidentDatesInFuture = incidentDatesInFuture;
        this.enrollmentDateLabel = enrollmentDateLabel;
        this.incidentDateLabel = incidentDateLabel;
    }

    public EnrollmentDateSetterHelper(TrackedEntityInstance trackedEntityInstance, IEnroller enroller, Context context, boolean showIncidentDate, boolean enrollmentDatesInFuture, boolean incidentDatesInFuture, String enrollmentDateLabel, String incidentDateLabel) {
        this.trackedEntityInstance = trackedEntityInstance;
        this.enroller = enroller;
        this.context = context;
        this.showIncidentDate = showIncidentDate;
        this.enrollmentDatesInFuture = enrollmentDatesInFuture;
        this.incidentDatesInFuture = incidentDatesInFuture;
        this.enrollmentDateLabel = enrollmentDateLabel;
        this.incidentDateLabel = incidentDateLabel;
    }

    public static void createEnrollment(TrackedEntityInstance trackedEntityInstance, IEnroller enroller, Context context, boolean showIncidentDate, boolean enrollmentDatesInFuture, boolean incidentDatesInFuture, String enrollmentDateLabel, String incidentDateLabel) {
        new EnrollmentDateSetterHelper(trackedEntityInstance, enroller, context, showIncidentDate, enrollmentDatesInFuture, incidentDatesInFuture, enrollmentDateLabel, incidentDateLabel).showEnrollmentDatePicker();
    }

    public static void createEnrollment(IEnroller enroller, Context context, boolean showIncidentDate, boolean enrollmentDatesInFuture, boolean incidentDatesInFuture, String enrollmentDateLabel, String incidentDateLabel) {
        new EnrollmentDateSetterHelper(enroller, context, showIncidentDate, enrollmentDatesInFuture, incidentDatesInFuture, enrollmentDateLabel, incidentDateLabel).showEnrollmentDatePicker();
    }

    private void showEnrollmentDatePicker() {
        this.enrollmentDate = new DateTime(1, 1, 1, 1, 0);
        LocalDate currentDate = new LocalDate();
        final CustomDatePickerDialog enrollmentDatePickerDialog = new CustomDatePickerDialog(this.context, null, currentDate.getYear(), currentDate.getMonthOfYear() - 1, currentDate.getDayOfMonth());
        enrollmentDatePickerDialog.setPermanentTitle(this.context.getString(C0773R.string.please_enter) + " " + this.enrollmentDateLabel);
        enrollmentDatePickerDialog.setCanceledOnTouchOutside(true);
        if (!this.enrollmentDatesInFuture) {
            enrollmentDatePickerDialog.getDatePicker().setMaxDate(DateTime.now().getMillis());
        }
        enrollmentDatePickerDialog.setButton(-1, this.context.getString(C0773R.string.ok_option), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                DatePicker dp = enrollmentDatePickerDialog.getDatePicker();
                EnrollmentDateSetterHelper.this.enrollmentDate = EnrollmentDateSetterHelper.this.enrollmentDate.withYear(dp.getYear());
                EnrollmentDateSetterHelper.this.enrollmentDate = EnrollmentDateSetterHelper.this.enrollmentDate.withMonthOfYear(dp.getMonth() + 1);
                EnrollmentDateSetterHelper.this.enrollmentDate = EnrollmentDateSetterHelper.this.enrollmentDate.withDayOfMonth(dp.getDayOfMonth());
                if (EnrollmentDateSetterHelper.this.showIncidentDate) {
                    EnrollmentDateSetterHelper.this.showIncidentDatePicker();
                } else {
                    EnrollmentDateSetterHelper.this.showEnrollmentFragment();
                }
            }
        });
        enrollmentDatePickerDialog.setButton(-2, this.context.getString(C0773R.string.cancel_option), new C08062());
        enrollmentDatePickerDialog.show();
    }

    private void showIncidentDatePicker() {
        LocalDate currentDate = new LocalDate();
        this.incidentDate = new DateTime(1, 1, 1, 1, 0);
        final CustomDatePickerDialog incidentDatePickerDialog = new CustomDatePickerDialog(this.context, null, currentDate.getYear(), currentDate.getMonthOfYear() - 1, currentDate.getDayOfMonth());
        incidentDatePickerDialog.setPermanentTitle(this.context.getString(C0773R.string.please_enter) + " " + this.incidentDateLabel);
        incidentDatePickerDialog.setCanceledOnTouchOutside(true);
        if (!this.incidentDatesInFuture) {
            incidentDatePickerDialog.getDatePicker().setMaxDate(DateTime.now().getMillis());
        }
        incidentDatePickerDialog.setButton(-1, this.context.getString(C0773R.string.ok_option), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                DatePicker dp = incidentDatePickerDialog.getDatePicker();
                EnrollmentDateSetterHelper.this.incidentDate = EnrollmentDateSetterHelper.this.incidentDate.withYear(dp.getYear());
                EnrollmentDateSetterHelper.this.incidentDate = EnrollmentDateSetterHelper.this.incidentDate.withMonthOfYear(dp.getMonth() + 1);
                EnrollmentDateSetterHelper.this.incidentDate = EnrollmentDateSetterHelper.this.incidentDate.withDayOfMonth(dp.getDayOfMonth());
                EnrollmentDateSetterHelper.this.showEnrollmentFragment();
            }
        });
        incidentDatePickerDialog.setButton(-2, this.context.getString(C0773R.string.cancel_option), new C08084());
        incidentDatePickerDialog.show();
    }

    private void showEnrollmentFragment() {
        this.enroller.showEnrollmentFragment(this.trackedEntityInstance, this.enrollmentDate, this.incidentDate);
    }
}
