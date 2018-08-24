package org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import org.joda.time.DateTime;

public abstract class AbsDatePickerRow extends Row {
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String EMPTY_FIELD = "";
    public static final String TAG = "AbsEnrollDatePickerRow";

    protected static class OnEditTextClickListener implements OnClickListener {
        private boolean allowDatesInFuture;
        private final Context context;
        private TextView dateText;
        private final OnDateSetListener listener;

        public OnEditTextClickListener(Context context, OnDateSetListener listener, boolean allowDatesInFuture, TextView dateText) {
            this.context = context;
            this.listener = listener;
            this.dateText = dateText;
            this.allowDatesInFuture = allowDatesInFuture;
        }

        public void onClick(View view) {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            if (!this.dateText.getText().toString().isEmpty()) {
                try {
                    calendar.setTime(simpleDateFormat.parse(this.dateText.getText().toString()));
                } catch (ParseException e) {
                    Log.e(AbsDatePickerRow.TAG, "Invalid date format, can't parse to put in the picker");
                    e.printStackTrace();
                }
            }
            DatePickerDialog picker = new DatePickerDialog(this.context, this.listener, calendar.get(1), calendar.get(2), calendar.get(5));
            if (!this.allowDatesInFuture) {
                picker.getDatePicker().setMaxDate(DateTime.now().getMillis());
            }
            picker.show();
        }
    }

    public abstract View getView(FragmentManager fragmentManager, LayoutInflater layoutInflater, View view, ViewGroup viewGroup);

    public AbsDatePickerRow() {
        checkNeedsForDescriptionButton();
    }
}
