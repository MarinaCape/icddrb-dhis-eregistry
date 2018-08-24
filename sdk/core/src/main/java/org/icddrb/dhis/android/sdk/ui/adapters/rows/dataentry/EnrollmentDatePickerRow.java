package org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry;

import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import org.icddrb.dhis.android.sdk.C0845R;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.models.DataValue;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.RowValueChangedEvent;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class EnrollmentDatePickerRow extends AbsDatePickerRow {
    private static final String TAG = "EnrollmentDatePickerRow";
    private Enrollment mEnrollment;
    private String mLabel;

    private static class ClearButtonListener implements OnClickListener {
        private Enrollment enrollment;
        private final TextView textView;

        public ClearButtonListener(TextView textView) {
            this.textView = textView;
        }

        public void setEnrollment(Enrollment enrollment) {
            this.enrollment = enrollment;
        }

        public void onClick(View view) {
            this.textView.setText("");
            this.enrollment.setEnrollmentDate("");
            Dhis2Application.getEventBus().post(new RowValueChangedEvent(null, DataEntryRowTypes.ENROLLMENT_DATE.toString()));
        }
    }

    private class DatePickerRowHolder {
        final ImageButton clearButton;
        final ClearButtonListener clearButtonListener = new ClearButtonListener(this.pickerInvoker);
        final DateSetListener dateSetListener;
        final OnEditTextClickListener invokerListener;
        final TextView pickerInvoker;
        final TextView textLabel;

        public DatePickerRowHolder(View root, Context context) {
            this.textLabel = (TextView) root.findViewById(C0845R.id.text_label);
            this.pickerInvoker = (TextView) root.findViewById(C0845R.id.date_picker_text_view);
            this.clearButton = (ImageButton) root.findViewById(C0845R.id.clear_text_view);
            this.dateSetListener = new DateSetListener(this.pickerInvoker);
            this.invokerListener = new OnEditTextClickListener(context, this.dateSetListener, false, this.pickerInvoker);
            this.clearButton.setOnClickListener(this.clearButtonListener);
            this.pickerInvoker.setOnClickListener(this.invokerListener);
        }

        public void updateViews(String label, Enrollment enrollment, String enrollmentDate) {
            this.dateSetListener.setEnrollment(enrollment);
            this.clearButtonListener.setEnrollment(enrollment);
            String eventDate = null;
            if (!(enrollment == null || enrollmentDate == null || TextUtils.isEmpty(enrollmentDate))) {
                this.dateSetListener.setEnrollmentDate(enrollmentDate);
                eventDate = DateTime.parse(enrollmentDate).toString("yyyy-MM-dd");
            }
            this.textLabel.setText(label);
            this.pickerInvoker.setText(eventDate);
        }
    }

    private class DateSetListener implements OnDateSetListener {
        private Enrollment enrollment;
        private String enrollmentDate;
        private final TextView textView;
        private DataValue value;

        public DateSetListener(TextView textView) {
            this.textView = textView;
        }

        public void setEnrollment(Enrollment enrollment) {
            this.enrollment = enrollment;
        }

        public void setEnrollmentDate(String enrollmentDate) {
            this.enrollmentDate = enrollmentDate;
        }

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            LocalDate date = new LocalDate(year, monthOfYear + 1, dayOfMonth);
            if (this.value == null) {
                this.value = new DataValue();
            }
            if (this.enrollmentDate != null) {
                this.value.setValue(this.enrollmentDate);
            }
            String newValue = date.toString("yyyy-MM-dd");
            this.textView.setText(newValue);
            if (!newValue.equals(this.value.getValue())) {
                this.value.setValue(newValue);
                if (this.enrollmentDate != null) {
                    this.enrollment.setEnrollmentDate(this.value.getValue());
                }
                Dhis2Application.getEventBus().post(new RowValueChangedEvent(this.value, DataEntryRowTypes.ENROLLMENT_DATE.toString()));
            }
        }
    }

    public EnrollmentDatePickerRow(String label, Enrollment enrollment) {
        this.mEnrollment = enrollment;
        this.mLabel = label;
        setOrg(enrollment.getOrgUnit());
    }

    public View getView(FragmentManager fragmentManager, LayoutInflater inflater, View convertView, ViewGroup container) {
        DatePickerRowHolder holder;
        View view;
        if (convertView == null || !(convertView.getTag() instanceof DatePickerRowHolder)) {
            View root = inflater.inflate(C0845R.layout.listview_row_event_datepicker, container, false);
            holder = new DatePickerRowHolder(root, inflater.getContext());
            root.setTag(holder);
            view = root;
        } else {
            view = convertView;
            holder = (DatePickerRowHolder) view.getTag();
        }
        if (isEditable()) {
            holder.clearButton.setEnabled(true);
            holder.textLabel.setEnabled(true);
            holder.pickerInvoker.setEnabled(true);
        } else {
            holder.clearButton.setEnabled(false);
            holder.textLabel.setEnabled(false);
            holder.pickerInvoker.setEnabled(false);
        }
        holder.updateViews(this.mLabel, this.mEnrollment, this.mEnrollment.getEnrollmentDate());
        return view;
    }

    public int getViewType() {
        return DataEntryRowTypes.ENROLLMENT_DATE.ordinal();
    }
}
