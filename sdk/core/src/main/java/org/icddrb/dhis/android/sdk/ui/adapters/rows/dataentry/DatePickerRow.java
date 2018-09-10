package org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.InputDeviceCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;
import org.icddrb.dhis.android.sdk.R;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.models.BaseValue;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.RowValueChangedEvent;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class DatePickerRow extends Row {
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String EMPTY_FIELD = "";
    private static final Pattern sPattern = Pattern.compile("^\\d{2}\\-\\d{2}\\-\\d{4}$");
    private final boolean mAllowDatesInFuture;
    private final String mType;

    private static class ClearButtonListener implements OnClickListener {
        private final EditText editText;
        private final TextView textView;
        private BaseValue value;

        public ClearButtonListener(TextView textView, EditText text) {
            this.textView = textView;
            this.editText = text;
        }

        public void setBaseValue(BaseValue value) {
            this.value = value;
        }

        public void onClick(View view) {
            this.textView.setText("");
            this.value.setValue("");
            if (this.editText != null) {
                this.editText.setText("");
            }
            Dhis2Application.getEventBus().post(new RowValueChangedEvent(this.value, DataEntryRowTypes.DATE.toString()));
        }
    }

    private class DatePickerRowHolder {
        final ImageButton clearButton;
        final ClearButtonListener clearButtonListener;
        final DateSetListener dateSetListener = new DateSetListener(this.pickerInvoker, this.editText);
        final EditText editText;
        final TextView errorLabel;
        final OnEditTextClickListener invokerListener;
        final TextView mandatoryIndicator;
        DatePickerDialog picker;
        final TextView pickerInvoker;
        final TextView textLabel;
        final TextView warningLabel;

        public DatePickerRowHolder(View root, Context context, boolean allowDatesInFuture, BaseValue baseValue, String type) {
            this.textLabel = (TextView) root.findViewById(R.id.text_label);
            this.mandatoryIndicator = (TextView) root.findViewById(R.id.mandatory_indicator);
            this.warningLabel = (TextView) root.findViewById(R.id.warning_label);
            this.errorLabel = (TextView) root.findViewById(R.id.error_label);
            this.pickerInvoker = (TextView) root.findViewById(R.id.date_picker_text_view);
            this.clearButton = (ImageButton) root.findViewById(R.id.clear_text_view);
            this.editText = (EditText) root.findViewById(R.id.age_text_row);
            LocalDate currentDate = new LocalDate();
            this.picker = new DatePickerDialog(context, this.dateSetListener, currentDate.getYear(), currentDate.getMonthOfYear() - 1, currentDate.getDayOfMonth());
            if (!allowDatesInFuture) {
                this.picker.getDatePicker().setMaxDate(DateTime.now().getMillis());
            }
            this.invokerListener = new OnEditTextClickListener(this.picker);
            this.clearButtonListener = new ClearButtonListener(this.pickerInvoker, this.editText);
            this.clearButton.setOnClickListener(this.clearButtonListener);
            this.pickerInvoker.setOnClickListener(this.invokerListener);
            if ("age".equals(type)) {
                this.editText.setVisibility(0);
                this.editText.setInputType(InputDeviceCompat.SOURCE_TOUCHSCREEN);
                this.editText.setTag(null);
                this.editText.addTextChangedListener(new TextWatcher(DatePickerRow.this) {

                    /* renamed from: org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.DatePickerRow$DatePickerRowHolder$1$1 */
                    class C08711 extends BaseValue {
                        C08711() {
                        }

                        public void handleUnknown(String key, Object value) {
                            super.handleUnknown(key, value);
                        }
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    public void afterTextChanged(Editable s) {
                        if (DatePickerRowHolder.this.editText.getTag() == null && !s.toString().isEmpty()) {
                            LocalDate date = new LocalDate();
                            int year = date.getYear() - Integer.valueOf(s.toString()).intValue();
                            String d = String.valueOf(year) + "-" + String.valueOf(date.getMonthOfYear()) + "-" + String.valueOf(date.getDayOfMonth());
                            DatePickerRowHolder.this.picker.updateDate(year, date.getMonthOfYear() - 1, date.getDayOfMonth());
                            DatePickerRowHolder.this.pickerInvoker.setText(d);
                            DatePickerRowHolder.this.dateSetListener.updateBaseValue(d);
                            BaseValue b = new C08711();
                            b.setValue(d);
                            Dhis2Application.getEventBus().post(new RowValueChangedEvent(b, DataEntryRowTypes.DATE.toString()));
                        }
                    }
                });
            }
        }

        public void updateViews(String label, BaseValue baseValue) {
            this.dateSetListener.setBaseValue(baseValue);
            this.clearButtonListener.setBaseValue(baseValue);
            if (!(baseValue == null || baseValue.getValue() == null || baseValue.equals("") || baseValue.getValue().isEmpty())) {
                try {
                    LocalDate birthDate = LocalDate.fromDateFields(new SimpleDateFormat(!DatePickerRow.this.isDayFirst(baseValue.getValue()) ? "yyyy-MM-dd" : "dd-MM-yyyy").parse(baseValue.getValue()));
                    this.picker.updateDate(birthDate.getYear(), birthDate.getMonthOfYear() - 1, birthDate.getDayOfMonth());
                    if (this.editText != null) {
                        int age = new LocalDate().getYear() - birthDate.getYear();
                        this.editText.setTag("machine");
                        this.editText.setText(String.valueOf(age));
                        this.editText.setTag(null);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            this.textLabel.setText(label);
            this.pickerInvoker.setText(baseValue.getValue());
        }
    }

    private static class DateSetListener implements OnDateSetListener {
        private final EditText editText;
        private final TextView textView;
        private BaseValue value;

        public DateSetListener(TextView textView, EditText text) {
            this.textView = textView;
            this.editText = text;
        }

        public void setBaseValue(BaseValue value) {
            this.value = value;
        }

        public void updateBaseValue(String value) {
            this.value.setValue(value);
        }

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String newValue = new LocalDate(year, monthOfYear + 1, dayOfMonth).toString("yyyy-MM-dd");
            this.textView.setText(newValue);
            if (this.editText != null) {
                Calendar birthDate = Calendar.getInstance();
                birthDate.set(year, monthOfYear + 1, dayOfMonth);
                this.editText.setTag("machine");
                this.editText.setText(String.valueOf(getAge(birthDate.getTime())));
                this.editText.setTag(null);
            }
            updateBaseValue(newValue);
            Dhis2Application.getEventBus().post(new RowValueChangedEvent(this.value, DataEntryRowTypes.DATE.toString()));
        }

        public BaseValue getBaseValue() {
            return this.value;
        }

        public int getAge(Date dateOfBirth) {
            Calendar today = Calendar.getInstance();
            Calendar birthDate = Calendar.getInstance();
            birthDate.setTime(dateOfBirth);
            if (birthDate.after(today)) {
                return 0;
            }
            int todayYear = today.get(1);
            int birthDateYear = birthDate.get(1);
            int todayDayOfYear = today.get(6);
            int birthDateDayOfYear = birthDate.get(6);
            int todayMonth = today.get(2);
            int birthDateMonth = birthDate.get(2);
            int todayDayOfMonth = today.get(5);
            int birthDateDayOfMonth = birthDate.get(5);
            int age = todayYear - birthDateYear;
            if (birthDateDayOfYear - todayDayOfYear > 3 || birthDateMonth > todayMonth) {
                return age - 1;
            }
            if (birthDateMonth != todayMonth || birthDateDayOfMonth <= todayDayOfMonth) {
                return age;
            }
            return age - 1;
        }
    }

    private static class OnEditTextClickListener implements OnClickListener {
        private DatePickerDialog datePickerDialog;

        public OnEditTextClickListener(DatePickerDialog datePickerDialog) {
            this.datePickerDialog = datePickerDialog;
        }

        public void onClick(View view) {
            this.datePickerDialog.show();
        }
    }

    private boolean isDayFirst(String s) {
        return sPattern.matcher(s).matches();
    }

    public DatePickerRow(String label, boolean mandatory, String warning, BaseValue value, boolean allowDatesInFuture, String type) {
        this.mAllowDatesInFuture = allowDatesInFuture;
        this.mLabel = label;
        this.mMandatory = mandatory;
        this.mValue = value;
        this.mWarning = warning;
        this.mType = type;
    }

    public DatePickerRow(String label, boolean mandatory, String warning, BaseValue value, boolean allowDatesInFuture) {
        this.mAllowDatesInFuture = allowDatesInFuture;
        this.mLabel = label;
        this.mMandatory = mandatory;
        this.mValue = value;
        this.mWarning = warning;
        this.mType = "date";
        checkNeedsForDescriptionButton();
    }

    public View getView(FragmentManager fragmentManager, LayoutInflater inflater, View convertView, ViewGroup container) {
        DatePickerRowHolder holder;
        View view;
        if (convertView == null || !(convertView.getTag() instanceof DatePickerRowHolder)) {
            View root = inflater.inflate(R.layout.listview_row_datepicker, container, false);
            holder = new DatePickerRowHolder(root, inflater.getContext(), this.mAllowDatesInFuture, this.mValue, this.mType);
            root.setTag(holder);
            view = root;
        } else {
            view = convertView;
            holder = (DatePickerRowHolder) view.getTag();
        }
        if (isEditable()) {
            holder.clearButton.setEnabled(true);
            holder.pickerInvoker.setEnabled(true);
            holder.editText.setEnabled(true);
        } else {
            holder.clearButton.setEnabled(false);
            holder.pickerInvoker.setEnabled(false);
            holder.editText.setEnabled(false);
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
        return DataEntryRowTypes.DATE.ordinal();
    }
}
