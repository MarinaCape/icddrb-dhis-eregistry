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
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.RowValueChangedEvent;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class EventDatePickerRow extends AbsDatePickerRow {
    private static final String TAG = "EventDatePickerRow";
    private final boolean mAllowDatesInFuture;
    private final Event mEvent;

    private static class ClearButtonListener implements OnClickListener {
        private Event event;
        private final TextView textView;
        private DataValue value;

        public ClearButtonListener(TextView textView) {
            this.textView = textView;
        }

        public void setEvent(Event event) {
            this.event = event;
        }

        public void onClick(View view) {
            this.textView.setText("");
            this.event.setEventDate(null);
            if (this.value == null) {
                this.value = new DataValue();
            }
            this.value.setValue("");
            Dhis2Application.getEventBus().post(new RowValueChangedEvent(this.value, DataEntryRowTypes.EVENT_DATE.toString()));
        }
    }

    private class DatePickerRowHolder {
        final ImageButton clearButton;
        final ClearButtonListener clearButtonListener = new ClearButtonListener(this.pickerInvoker);
        final DateSetListener dateSetListener;
        final OnEditTextClickListener invokerListener;
        final TextView pickerInvoker;
        final TextView textLabel;

        public DatePickerRowHolder(View root, Context context, boolean allowDatesInFuture) {
            this.textLabel = (TextView) root.findViewById(C0845R.id.text_label);
            this.pickerInvoker = (TextView) root.findViewById(C0845R.id.date_picker_text_view);
            this.clearButton = (ImageButton) root.findViewById(C0845R.id.clear_text_view);
            this.dateSetListener = new DateSetListener(this.pickerInvoker);
            this.invokerListener = new OnEditTextClickListener(context, this.dateSetListener, allowDatesInFuture, this.pickerInvoker);
            this.clearButton.setOnClickListener(this.clearButtonListener);
            this.pickerInvoker.setOnClickListener(this.invokerListener);
        }

        public void updateViews(String label, Event event) {
            this.dateSetListener.setEvent(event);
            this.clearButtonListener.setEvent(event);
            String eventDate = null;
            if (!(event == null || event.getEventDate() == null || TextUtils.isEmpty(event.getEventDate()))) {
                eventDate = DateTime.parse(event.getEventDate()).toString("yyyy-MM-dd");
            }
            this.textLabel.setText(label);
            this.pickerInvoker.setText(eventDate);
        }
    }

    private class DateSetListener implements OnDateSetListener {
        private static final String DATE_FORMAT = "YYYY-MM-dd";
        private Event event;
        private final TextView textView;
        private DataValue value;

        public DateSetListener(TextView textView) {
            this.textView = textView;
        }

        public void setEvent(Event event) {
            this.event = event;
        }

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            LocalDate date = new LocalDate(year, monthOfYear + 1, dayOfMonth);
            if (this.value == null) {
                this.value = new DataValue();
            }
            if (this.event.getEventDate() != null) {
                this.value.setValue(this.event.getEventDate());
            }
            String newValue = date.toString(DATE_FORMAT);
            this.textView.setText(newValue);
            if (this.event.getEventDate() == null || !newValue.equals(this.value.getValue())) {
                this.value.setValue(newValue);
                this.event.setEventDate(this.value.getValue());
                Dhis2Application.getEventBus().post(new RowValueChangedEvent(this.value, DataEntryRowTypes.EVENT_DATE.toString()));
            }
        }
    }

    public EventDatePickerRow(String label, Event event, boolean allowDatesInFuture) {
        this.mAllowDatesInFuture = allowDatesInFuture;
        this.mLabel = label;
        this.mEvent = event;
        checkNeedsForDescriptionButton();
    }

    public View getView(FragmentManager fragmentManager, LayoutInflater inflater, View convertView, ViewGroup container) {
        DatePickerRowHolder holder;
        View view;
        if (convertView == null || !(convertView.getTag() instanceof DatePickerRowHolder)) {
            View root = inflater.inflate(C0845R.layout.listview_row_event_datepicker, container, false);
            holder = new DatePickerRowHolder(root, inflater.getContext(), this.mAllowDatesInFuture);
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
        holder.updateViews(this.mLabel, this.mEvent);
        return view;
    }

    public int getViewType() {
        return DataEntryRowTypes.EVENT_DATE.ordinal();
    }
}
