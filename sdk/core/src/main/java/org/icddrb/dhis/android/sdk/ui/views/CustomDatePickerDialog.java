package org.icddrb.dhis.android.sdk.ui.views;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomDatePickerDialog extends DatePickerDialog {
    private TextView customTextView;
    private CharSequence title;

    public CustomDatePickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
        super(context, callBack, year, monthOfYear, dayOfMonth);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (VERSION.SDK_INT > 23) {
            tryAddCustomTitleView();
        }
    }

    public void setPermanentTitle(CharSequence title) {
        this.title = title;
        setTitle(title);
    }

    public void setCustomTitle(View customTitleView) {
        super.setCustomTitle(customTitleView);
    }

    public void onDateChanged(DatePicker view, int year, int month, int day) {
        super.onDateChanged(view, year, month, day);
        setTitle(this.title);
    }

    private void tryAddCustomTitleView() {
        try {
            this.customTextView = new TextView(getContext());
            this.customTextView.setText(this.title);
            ((LinearLayout) getDatePicker().getParent().getParent().getParent()).addView(this.customTextView, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
