package org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import org.icddrb.dhis.android.sdk.R;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.models.BaseValue;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.RowValueChangedEvent;
import org.icddrb.dhis.android.sdk.ui.views.FontEditText;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.text.TextUtils.isEmpty;

public class AgeRow extends Row {
    private static final String EMPTY_FIELD = "";
    private final boolean mAllowDatesInFuture;
    private static final String DATE_FORMAT_AGE = "dd-MM-yyyy";
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public AgeRow(String label, boolean mandatory, String warning, BaseValue value, boolean allowDatesInFuture) {
        mAllowDatesInFuture = allowDatesInFuture;
        mLabel = label;
        mMandatory = mandatory;
        mValue = value;
        mWarning = warning;

        checkNeedsForDescriptionButton();
    }

    @Override
    public View getView(FragmentManager fragmentManager, LayoutInflater inflater,
                        View convertView, ViewGroup container) {
        View view;
        AgeRow.DatePickerRowHolder holder;

        if (convertView != null && convertView.getTag() instanceof AgeRow.DatePickerRowHolder) {
            view = convertView;
            holder = (AgeRow.DatePickerRowHolder) view.getTag();
        } else {
            View root = inflater.inflate(
                    R.layout.listview_row_age, container, false);
//            detailedInfoButton = root.findViewById(R.id.detailed_info_button_layout);

            holder = new AgeRow.DatePickerRowHolder(root, inflater.getContext(), mAllowDatesInFuture, mValue);


            root.setTag(holder);
            view = root;
        }

        if(!isEditable()) {
            holder.clearButton.setEnabled(false);
            holder.pickerInvoker.setEnabled(false);
        } else {
            holder.clearButton.setEnabled(true);
            holder.pickerInvoker.setEnabled(true);
        }
//      holder.detailedInfoButton.setOnClickListener(new OnDetailedInfoButtonClick(this));
        holder.updateViews(mLabel, mValue);

//        if(isDetailedInfoButtonHidden()) {
//            holder.detailedInfoButton.setVisibility(View.INVISIBLE);
//        }
//        else {
//            holder.detailedInfoButton.setVisibility(View.VISIBLE);
//        }

        if(mWarning == null) {
            holder.warningLabel.setVisibility(View.GONE);
        } else {
            holder.warningLabel.setVisibility(View.VISIBLE);
            holder.warningLabel.setText(mWarning);
        }

        if(mError == null) {
            holder.errorLabel.setVisibility(View.GONE);
        } else {
            holder.errorLabel.setVisibility(View.VISIBLE);
            holder.errorLabel.setText(mError);
        }

        if(!mMandatory) {
            holder.mandatoryIndicator.setVisibility(View.GONE);
        } else {
            holder.mandatoryIndicator.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public int getViewType() {
        return DataEntryRowTypes.DATE.ordinal();
    }


    private class DatePickerRowHolder {
        final TextView textLabel;
        final TextView mandatoryIndicator;
        final TextView warningLabel;
        final TextView errorLabel;
        final TextView pickerInvoker;
        final FontEditText years;
        final FontEditText months;
        final FontEditText days;
        final ImageButton clearButton;
        //        final View detailedInfoButton;
        final AgeRow.DateSetListener dateSetListener;
        final AgeRow.OnEditTextClickListener invokerListener;
        final AgeRow.ClearButtonListener clearButtonListener;
        DatePickerDialog picker;

        public DatePickerRowHolder(View root, Context context, boolean allowDatesInFuture, BaseValue baseValue) {
            textLabel = (TextView) root.findViewById(R.id.text_label);
            mandatoryIndicator = (TextView) root.findViewById(R.id.mandatory_indicator);
            warningLabel = (TextView) root.findViewById(R.id.warning_label);
            errorLabel = (TextView) root.findViewById(R.id.error_label);
            pickerInvoker = (TextView) root.findViewById(R.id.date_picker_text_view);
            clearButton = (ImageButton) root.findViewById(R.id.clear_text_view);
            years = (FontEditText) root.findViewById(R.id.years);
            months = root.findViewById(R.id.months);
            days = root.findViewById(R.id.days);
//            this.detailedInfoButton = detailedInfoButton;

            dateSetListener = new AgeRow.DateSetListener(pickerInvoker);

            LocalDate currentDate = new LocalDate();
            picker = new DatePickerDialog(context, dateSetListener, currentDate.getYear(), currentDate.getMonthOfYear(), currentDate.getDayOfMonth());

            if(!allowDatesInFuture) {
                picker.getDatePicker().setMaxDate(DateTime.now().getMillis());
            }
            invokerListener = new AgeRow.OnEditTextClickListener(picker);
            clearButtonListener = new AgeRow.ClearButtonListener(pickerInvoker, years, months, days);


            clearButton.setOnClickListener(clearButtonListener);
            pickerInvoker.setOnClickListener(invokerListener);
            years.setOnFocusChangeListener(listener);
            months.setOnFocusChangeListener(listener);
            days.setOnFocusChangeListener(listener);

        }

        private final View.OnFocusChangeListener listener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b)
                    handleSingleInputs();
            }
        };

        public void updateViews(String label, Object baseValue) {
            dateSetListener.setBaseValue((BaseValue) baseValue);
            clearButtonListener.setBaseValue((BaseValue)baseValue);

            if(baseValue !=null && ((BaseValue)baseValue).getValue()!=null && !baseValue.equals("") && !((BaseValue)baseValue).getValue().isEmpty()) {
                try {
                    Date date;
                    if(((BaseValue)baseValue).getValue().split("-")[0].length() < 3)
                        date = new SimpleDateFormat(DATE_FORMAT_AGE).parse(((BaseValue)baseValue).getValue());
                    else
                        date = new SimpleDateFormat(DATE_FORMAT).parse(((BaseValue)baseValue).getValue());
                    LocalDate currentDate = LocalDate.fromDateFields(date);
                    picker.updateDate(currentDate.getYear(), currentDate.getMonthOfYear() - 1 , currentDate.getDayOfMonth());
                    handleDateInput(currentDate.getYear(), currentDate.getMonthOfYear() - 1 , currentDate.getDayOfMonth());

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            pickerInvoker.setText(((BaseValue) baseValue).getValue());

            textLabel.setText(label);

        }

        private void handleSingleInputs() {

            Calendar calendar = Calendar.getInstance();

            calendar.add(Calendar.DAY_OF_MONTH, isEmpty(days.getText().toString()) ? 0 : -Integer.valueOf(days.getText().toString()));
            calendar.add(Calendar.MONTH, isEmpty(months.getText().toString()) ? 0 : -Integer.valueOf(months.getText().toString()));
            calendar.add(Calendar.YEAR, isEmpty(years.getText().toString()) ? 0 : -Integer.valueOf(years.getText().toString()));
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            String birthDate = uiDateFormat().format(calendar.getTime());
            if (!pickerInvoker.getText().toString().equals(birthDate)) {
                mValue.setValue(birthDate);
                updateViews(mLabel, mValue);
            }
        }

        private void handleDateInput(int year1, int month1, int day1) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year1);
            calendar.set(Calendar.MONTH, month1);
            calendar.set(Calendar.DAY_OF_MONTH, day1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            String result = uiDateFormat().format(calendar.getTime());

            int[] dateDifference = getDifference(calendar.getTime(), Calendar.getInstance().getTime());
            days.setText(String.valueOf(dateDifference[2]));
            months.setText(String.valueOf(dateDifference[1]));
            years.setText(String.valueOf(dateDifference[0]));

        }


        private SimpleDateFormat uiDateFormat() {
            return new SimpleDateFormat(DATE_FORMAT_AGE, Locale.US);
        }

        private int[] getDifference(Date startDate, Date endDate) {

            org.joda.time.Period interval = new org.joda.time.Period(startDate.getTime(), endDate.getTime(), org.joda.time.PeriodType.yearMonthDayTime());
            return new int[]{interval.getYears(), interval.getMonths(), interval.getDays()};

        }

    }

    //END CLASS


    private static class OnEditTextClickListener implements View.OnClickListener {
        private DatePickerDialog datePickerDialog;

        public OnEditTextClickListener(DatePickerDialog datePickerDialog) {
            this.datePickerDialog = datePickerDialog;
        }

        @Override
        public void onClick(View view) {
            datePickerDialog.show();
        }
    }

    private static class ClearButtonListener implements View.OnClickListener {
        private final TextView textView;
        private BaseValue value;
        final FontEditText years;
        final FontEditText months;
        final FontEditText days;
        public ClearButtonListener(TextView textView, FontEditText years, FontEditText months, FontEditText days) {
            this.textView = textView;
            this.days = days;
            this.months = months;
            this.years = years;
        }

        public void setBaseValue(BaseValue value) {
            this.value = value;
        }

        @Override
        public void onClick(View view) {
            textView.setText(EMPTY_FIELD);
            value.setValue(EMPTY_FIELD);
            days.setText(EMPTY_FIELD);
            months.setText(EMPTY_FIELD);
            years.setText(EMPTY_FIELD);
            Dhis2Application.getEventBus()
                    .post(new RowValueChangedEvent(value, DataEntryRowTypes.DATE.toString()));
        }
    }

    private static class DateSetListener implements DatePickerDialog.OnDateSetListener {
        private final TextView textView;
        private BaseValue value;

        public DateSetListener(TextView textView) {
            this.textView = textView;
        }

        public void setBaseValue(BaseValue value) {
            this.value = value;
        }

        @Override
        public void onDateSet(DatePicker view, int year,
                              int monthOfYear, int dayOfMonth) {
            LocalDate date = new LocalDate(year, monthOfYear + 1, dayOfMonth);
            String newValue;
            Object objValue = value;
            if(objValue instanceof TrackedEntityAttributeValue && ("UhumPu20UzS".equals(( (TrackedEntityAttributeValue)this.value).getTrackedEntityAttributeId())))
                newValue  = date.toString(DATE_FORMAT_AGE);
            else
                newValue = date.toString(DATE_FORMAT);
            textView.setText(newValue);
            value.setValue(newValue);
            System.out.println("DatePiker Saving value:" + newValue);
            value.setValue(newValue);
            Dhis2Application.getEventBus()
                    .post(new RowValueChangedEvent(value, DataEntryRowTypes.DATE.toString()));
        }

        public BaseValue getBaseValue() {
            return value;
        }
    }

}
