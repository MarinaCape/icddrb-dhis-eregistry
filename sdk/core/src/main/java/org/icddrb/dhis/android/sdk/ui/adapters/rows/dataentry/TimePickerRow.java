/*
 *  Copyright (c) 2016, University of Oslo
 *  * All rights reserved.
 *  *
 *  * Redistribution and use in source and binary forms, with or without
 *  * modification, are permitted provided that the following conditions are met:
 *  * Redistributions of source code must retain the above copyright notice, this
 *  * list of conditions and the following disclaimer.
 *  *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *  * this list of conditions and the following disclaimer in the documentation
 *  * and/or other materials provided with the distribution.
 *  * Neither the name of the HISP project nor the names of its contributors may
 *  * be used to endorse or promote products derived from this software without
 *  * specific prior written permission.
 *  *
 *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry;

import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import org.icddrb.dhis.android.sdk.R;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.models.BaseValue;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.RowValueChangedEvent;
import org.joda.time.LocalTime;

import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class TimePickerRow extends Row {
    private static final String EMPTY_FIELD = "";

    public TimePickerRow(String label, boolean mandatory, String warning, BaseValue value) {
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
        TimePickerRowHolder holder;

        if (convertView != null && convertView.getTag() instanceof TimePickerRowHolder) {
            view = convertView;
            holder = (TimePickerRowHolder) view.getTag();
        } else {
            View root = inflater.inflate(
                    R.layout.listview_row_timepicker, container, false);

            holder = new TimePickerRowHolder(root, inflater.getContext());


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
        holder.updateViews(mLabel, mValue);

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
        return DataEntryRowTypes.TIME.ordinal();
    }


    private class TimePickerRowHolder {
        final TextView textLabel;
        final TextView mandatoryIndicator;
        final TextView warningLabel;
        final TextView errorLabel;
        final TextView pickerInvoker;
        final ImageButton clearButton;
        final TimeSetListener timeSetListener;
        final OnEditTextClickListener invokerListener;
        final ClearButtonListener clearButtonListener;

        public TimePickerRowHolder(View root, Context context) {
            textLabel = (TextView) root.findViewById(R.id.text_label);
            mandatoryIndicator = (TextView) root.findViewById(R.id.mandatory_indicator);
            warningLabel = (TextView) root.findViewById(R.id.warning_label);
            errorLabel = (TextView) root.findViewById(R.id.error_label);
            pickerInvoker = (TextView) root.findViewById(R.id.date_picker_text_view);
            clearButton = (ImageButton) root.findViewById(R.id.clear_text_view);

            timeSetListener = new TimeSetListener(pickerInvoker);
            invokerListener = new OnEditTextClickListener(context, timeSetListener);
            clearButtonListener = new ClearButtonListener(pickerInvoker);

            clearButton.setOnClickListener(clearButtonListener);
            pickerInvoker.setOnClickListener(invokerListener);
        }

        public void updateViews(String label, BaseValue baseValue) {
            timeSetListener.setBaseValue(baseValue);
            clearButtonListener.setBaseValue(baseValue);

            textLabel.setText(label);
            pickerInvoker.setText(baseValue.getValue());
        }
    }

    private static class OnEditTextClickListener implements OnClickListener {
        private final Context context;
        private final TimeSetListener listener;

        public OnEditTextClickListener(Context context, TimeSetListener listener) {
            this.context = context;
            this.listener = listener;
        }

        @Override
        public void onClick(View view) {
            LocalTime currentTime = new LocalTime();
            TimePickerDialog picker = new TimePickerDialog(context, listener,
                    currentTime.getHourOfDay(), currentTime.getMinuteOfHour(), false);
            picker.show();
        }
    }

    private static class ClearButtonListener implements OnClickListener {
        private final TextView textView;
        private BaseValue value;

        public ClearButtonListener(TextView textView) {
            this.textView = textView;
        }

        public void setBaseValue(BaseValue value) {
            this.value = value;
        }

        @Override
        public void onClick(View view) {
            textView.setText(EMPTY_FIELD);
            value.setValue(EMPTY_FIELD);
            Dhis2Application.getEventBus()
                    .post(new RowValueChangedEvent(value, DataEntryRowTypes.DATE.toString()));
        }
    }

    private static class TimeSetListener implements TimePickerDialog.OnTimeSetListener {
        private final TextView textView;
        private BaseValue value;

        public TimeSetListener(TextView textView) {
            this.textView = textView;
        }

        public void setBaseValue(BaseValue value) {
            this.value = value;
        }

        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {
            Time tme = new Time(hour, minute, 0);//seconds by default set to zero
            Format formatter = new SimpleDateFormat("h:mm a", Locale.US);
            String newValue = formatter.format(tme);
            textView.setText(newValue);
            value.setValue(newValue);
            Dhis2Application.getEventBus().post(new RowValueChangedEvent(value, DataEntryRowTypes.TIME.toString()));
        }
    }
}


