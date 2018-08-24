package org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry;

import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Locale;
import org.icddrb.dhis.android.sdk.C0845R;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.models.BaseValue;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.RowValueChangedEvent;
import org.joda.time.LocalTime;

public class TimePickerRow extends Row {
    private static final String EMPTY_FIELD = "";

    private static class ClearButtonListener implements OnClickListener {
        private final TextView textView;
        private BaseValue value;

        public ClearButtonListener(TextView textView) {
            this.textView = textView;
        }

        public void setBaseValue(BaseValue value) {
            this.value = value;
        }

        public void onClick(View view) {
            this.textView.setText("");
            this.value.setValue("");
            Dhis2Application.getEventBus().post(new RowValueChangedEvent(this.value, DataEntryRowTypes.DATE.toString()));
        }
    }

    private static class OnEditTextClickListener implements OnClickListener {
        private final Context context;
        private final TimeSetListener listener;

        public OnEditTextClickListener(Context context, TimeSetListener listener) {
            this.context = context;
            this.listener = listener;
        }

        public void onClick(View view) {
            LocalTime currentTime = new LocalTime();
            new TimePickerDialog(this.context, this.listener, currentTime.getHourOfDay(), currentTime.getMinuteOfHour(), false).show();
        }
    }

    private class TimePickerRowHolder {
        final ImageButton clearButton;
        final ClearButtonListener clearButtonListener;
        final TextView errorLabel;
        final OnEditTextClickListener invokerListener;
        final TextView mandatoryIndicator;
        final TextView pickerInvoker;
        final TextView textLabel;
        final TimeSetListener timeSetListener = new TimeSetListener(this.pickerInvoker);
        final TextView warningLabel;

        public TimePickerRowHolder(View root, Context context) {
            this.textLabel = (TextView) root.findViewById(C0845R.id.text_label);
            this.mandatoryIndicator = (TextView) root.findViewById(C0845R.id.mandatory_indicator);
            this.warningLabel = (TextView) root.findViewById(C0845R.id.warning_label);
            this.errorLabel = (TextView) root.findViewById(C0845R.id.error_label);
            this.pickerInvoker = (TextView) root.findViewById(C0845R.id.date_picker_text_view);
            this.clearButton = (ImageButton) root.findViewById(C0845R.id.clear_text_view);
            this.invokerListener = new OnEditTextClickListener(context, this.timeSetListener);
            this.clearButtonListener = new ClearButtonListener(this.pickerInvoker);
            this.clearButton.setOnClickListener(this.clearButtonListener);
            this.pickerInvoker.setOnClickListener(this.invokerListener);
        }

        public void updateViews(String label, BaseValue baseValue) {
            this.timeSetListener.setBaseValue(baseValue);
            this.clearButtonListener.setBaseValue(baseValue);
            this.textLabel.setText(label);
            this.pickerInvoker.setText(baseValue.getValue());
        }
    }

    private static class TimeSetListener implements OnTimeSetListener {
        private final TextView textView;
        private BaseValue value;

        public TimeSetListener(TextView textView) {
            this.textView = textView;
        }

        public void setBaseValue(BaseValue value) {
            this.value = value;
        }

        public void onTimeSet(TimePicker view, int hour, int minute) {
            String newValue = new SimpleDateFormat("h:mm a", Locale.US).format(new Time(hour, minute, 0));
            this.textView.setText(newValue);
            this.value.setValue(newValue);
            Dhis2Application.getEventBus().post(new RowValueChangedEvent(this.value, DataEntryRowTypes.TIME.toString()));
        }
    }

    public TimePickerRow(String label, boolean mandatory, String warning, BaseValue value) {
        this.mLabel = label;
        this.mMandatory = mandatory;
        this.mValue = value;
        this.mWarning = warning;
        checkNeedsForDescriptionButton();
    }

    public View getView(FragmentManager fragmentManager, LayoutInflater inflater, View convertView, ViewGroup container) {
        TimePickerRowHolder holder;
        View view;
        if (convertView == null || !(convertView.getTag() instanceof TimePickerRowHolder)) {
            View root = inflater.inflate(C0845R.layout.listview_row_timepicker, container, false);
            holder = new TimePickerRowHolder(root, inflater.getContext());
            root.setTag(holder);
            view = root;
        } else {
            view = convertView;
            holder = (TimePickerRowHolder) view.getTag();
        }
        if (isEditable()) {
            holder.clearButton.setEnabled(true);
            holder.pickerInvoker.setEnabled(true);
        } else {
            holder.clearButton.setEnabled(false);
            holder.pickerInvoker.setEnabled(false);
        }
        holder.updateViews(this.mLabel, this.mValue);
        if (this.mWarning == null) {
            holder.warningLabel.setVisibility(8);
        } else {
            holder.warningLabel.setVisibility(0);
            holder.warningLabel.setText(this.mWarning);
        }
        if (this.mError == null) {
            holder.errorLabel.setVisibility(8);
        } else {
            holder.errorLabel.setVisibility(0);
            holder.errorLabel.setText(this.mError);
        }
        if (this.mMandatory) {
            holder.mandatoryIndicator.setVisibility(0);
        } else {
            holder.mandatoryIndicator.setVisibility(8);
        }
        return view;
    }

    public int getViewType() {
        return DataEntryRowTypes.TIME.ordinal();
    }
}
