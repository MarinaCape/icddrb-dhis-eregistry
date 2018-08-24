package org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.icddrb.dhis.android.sdk.C0845R;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.models.BaseValue;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.RowValueChangedEvent;

public class DateTimePickerRow extends Row {
    private static final String EMPTY_FIELD = "";
    final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm";
    private final boolean mAllowDatesInFuture;
    Context mContext;

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
            Dhis2Application.getEventBus().post(new RowValueChangedEvent(this.value, DataEntryRowTypes.TIME.toString()));
        }
    }

    private class DatePickerRowHolder {
        boolean allowDatesInFuture;
        private Button cancel = ((Button) this.dialogView.findViewById(C0845R.id.cancel));
        final ImageButton clearButton;
        final ClearButtonListener clearButtonListener = new ClearButtonListener(this.pickerInvoker);
        private DatePicker datePicker = ((DatePicker) this.dialogView.findViewById(C0845R.id.datePicker));
        private View dialogView;
        final TextView errorLabel;
        OnEditTextClickListener invokerListener;
        AlertDialog mAlertDialog;
        final TextView mandatoryIndicator;
        private Button okButton = ((Button) this.dialogView.findViewById(C0845R.id.ok_button));
        final TextView pickerInvoker;
        final TextView textLabel;
        private TimePicker timePicker = ((TimePicker) this.dialogView.findViewById(C0845R.id.timePicker));
        final TextView warningLabel;

        public DatePickerRowHolder(View root, Context context, boolean allowDatesInFuture) {
            this.textLabel = (TextView) root.findViewById(C0845R.id.text_label);
            this.mandatoryIndicator = (TextView) root.findViewById(C0845R.id.mandatory_indicator);
            this.warningLabel = (TextView) root.findViewById(C0845R.id.warning_label);
            this.errorLabel = (TextView) root.findViewById(C0845R.id.error_label);
            this.pickerInvoker = (TextView) root.findViewById(C0845R.id.date_picker_text_view);
            this.clearButton = (ImageButton) root.findViewById(C0845R.id.clear_text_view);
            this.allowDatesInFuture = allowDatesInFuture;
            DateTimePickerRow.this.mContext = context;
            this.dialogView = View.inflate(DateTimePickerRow.this.mContext, C0845R.layout.time_date_picker, null);
            this.clearButton.setOnClickListener(this.clearButtonListener);
        }

        private AlertDialog createDialog(Context context, final TextView pickerInvoker, final BaseValue baseValue) {
            String VALUE_FORMAT = "%s-%s-%sT%s:%s";
            final AlertDialog alertDialog = new Builder(context).create();
            this.timePicker.setIs24HourView(Boolean.valueOf(true));
            if (baseValue == null || baseValue.getValue() == null || baseValue.getValue().equals("")) {
                Calendar calendar = Calendar.getInstance();
                this.datePicker.init(calendar.get(1), calendar.get(2), calendar.get(5), new OnDateChangedListener() {
                    public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                        System.out.println("TimeDatePiker onDateChanged");
                        DatePickerRowHolder.this.saveValue(DatePickerRowHolder.this.timePicker, "%s-%s-%sT%s:%s", datePicker, pickerInvoker, baseValue);
                    }
                });
            } else {
                this.timePicker.setCurrentHour(Integer.valueOf(DateTimePickerRow.this.getDateType(baseValue, 11)));
                this.timePicker.setCurrentMinute(Integer.valueOf(DateTimePickerRow.this.getDateType(baseValue, 12)));
                this.datePicker.init(DateTimePickerRow.this.getDateType(baseValue, 1), DateTimePickerRow.this.getDateType(baseValue, 2), DateTimePickerRow.this.getDateType(baseValue, 5), new OnDateChangedListener() {
                    public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                        System.out.println("TimeDatePiker onDateChanged");
                        DatePickerRowHolder.this.saveValue(DatePickerRowHolder.this.timePicker, "%s-%s-%sT%s:%s", datePicker, pickerInvoker, baseValue);
                    }
                });
            }
            try {
                LinearLayout linearLayout = (LinearLayout) this.datePicker.getChildAt(0);
                if (linearLayout != null) {
                    linearLayout.setLayoutParams(new LayoutParams(-1, -1));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.cancel.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            this.okButton.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    DatePickerRowHolder.this.saveValue(DatePickerRowHolder.this.timePicker, "%s-%s-%sT%s:%s", DatePickerRowHolder.this.datePicker, pickerInvoker, baseValue);
                    alertDialog.dismiss();
                }
            });
            alertDialog.setView(this.dialogView);
            return alertDialog;
        }

        public void updateViews(String label, DatePickerRowHolder holder, BaseValue baseValue) {
            if (!(DateTimePickerRow.this.mValue == null || DateTimePickerRow.this.mValue.getValue() == null || DateTimePickerRow.this.mValue.getValue().equals(""))) {
                holder.datePicker.updateDate(DateTimePickerRow.this.getDateType(DateTimePickerRow.this.mValue, 1), DateTimePickerRow.this.getDateType(DateTimePickerRow.this.mValue, 2), DateTimePickerRow.this.getDateType(DateTimePickerRow.this.mValue, 5));
                holder.timePicker.setCurrentHour(Integer.valueOf(DateTimePickerRow.this.getDateType(DateTimePickerRow.this.mValue, 11)));
                holder.timePicker.setCurrentMinute(Integer.valueOf(DateTimePickerRow.this.getDateType(DateTimePickerRow.this.mValue, 12)));
            }
            this.clearButtonListener.setBaseValue(baseValue);
            this.textLabel.setText(label);
            this.pickerInvoker.setText(getValueToRender(baseValue.getValue()));
            this.invokerListener = new OnEditTextClickListener(this.mAlertDialog, this.allowDatesInFuture, this.pickerInvoker, DateTimePickerRow.this.mValue);
            this.pickerInvoker.setOnClickListener(this.invokerListener);
        }

        private String getValueToRender(String value) {
            if (value == null || value.isEmpty()) {
                return null;
            }
            String DATE_FORMAT_RENDER = "yyyy-MM-dd HH:mm";
            SimpleDateFormat toDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            String dateToRender = "";
            try {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(toDateFormat.parse(value));
            } catch (ParseException e) {
                e.printStackTrace();
                return dateToRender;
            }
        }

        private void saveValue(TimePicker timePicker, String DATE_FORMAT, DatePicker datePicker, TextView textView, BaseValue value) {
            String newValue = getFormattedValue(timePicker, DATE_FORMAT, datePicker);
            String textValue = getValueToRender(newValue);
            System.out.println("TimeDatePiker Saving value:" + newValue);
            if (!newValue.equals(value.getValue())) {
                textView.setText(textValue);
                value.setValue(newValue);
                Dhis2Application.getEventBus().post(new RowValueChangedEvent(value, DataEntryRowTypes.TIME.toString()));
            }
        }

        private String getFormattedValue(TimePicker timePicker, String DATE_FORMAT, DatePicker datePicker) {
            return String.format(DATE_FORMAT, new Object[]{Integer.valueOf(datePicker.getYear()), DateTimePickerRow.this.getFixedString(datePicker.getMonth() + 1), DateTimePickerRow.this.getFixedString(datePicker.getDayOfMonth()), DateTimePickerRow.this.getFixedString(timePicker.getCurrentHour().intValue()), DateTimePickerRow.this.getFixedString(timePicker.getCurrentMinute().intValue())});
        }
    }

    private class OnEditTextClickListener implements OnClickListener {
        private final boolean allowDatesInFuture;
        BaseValue baseValue;
        private final AlertDialog mAlertDialog;
        TextView pickerInvoker;

        public OnEditTextClickListener(AlertDialog alertDialog, boolean allowDatesInFuture, TextView pickerInvoker, BaseValue baseValue) {
            this.mAlertDialog = alertDialog;
            this.allowDatesInFuture = allowDatesInFuture;
            this.pickerInvoker = pickerInvoker;
            this.baseValue = baseValue;
        }

        public void onClick(View view) {
            this.mAlertDialog.show();
        }
    }

    public DateTimePickerRow(String label, boolean mandatory, String warning, BaseValue value, boolean allowDatesInFuture) {
        this.mAllowDatesInFuture = allowDatesInFuture;
        this.mLabel = label;
        this.mMandatory = mandatory;
        this.mValue = value;
        this.mWarning = warning;
        checkNeedsForDescriptionButton();
    }

    public View getView(FragmentManager fragmentManager, LayoutInflater inflater, View convertView, ViewGroup container) {
        DatePickerRowHolder holder;
        View view;
        if (convertView == null || !(convertView.getTag() instanceof DatePickerRowHolder)) {
            View root = inflater.inflate(C0845R.layout.listview_row_datepicker, container, false);
            holder = new DatePickerRowHolder(root, inflater.getContext(), this.mAllowDatesInFuture);
            holder.mAlertDialog = holder.createDialog(this.mContext, holder.pickerInvoker, this.mValue);
            root.setTag(holder);
            view = root;
        } else {
            view = convertView;
            holder = (DatePickerRowHolder) view.getTag();
        }
        if (isEditable()) {
            holder.clearButton.setEnabled(true);
            holder.pickerInvoker.setEnabled(true);
        } else {
            holder.clearButton.setEnabled(false);
            holder.pickerInvoker.setEnabled(false);
        }
        holder.updateViews(this.mLabel, holder, this.mValue);
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

    private int getDateType(BaseValue value, int type) {
        return getDateFromValue(value).get(type);
    }

    private Calendar getDateFromValue(BaseValue value) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = simpleDateFormat.parse(value.getValue());
            calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
            return calendar;
        }
    }

    private String getFixedString(int number) {
        if (String.valueOf(number).length() == 1) {
            return "0" + number;
        }
        return "" + number;
    }
}
