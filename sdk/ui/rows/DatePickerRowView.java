package org.icddrb.dhis.client.sdk.ui.rows;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import org.icddrb.dhis.client.sdk.ui.C0935R;
import org.icddrb.dhis.client.sdk.ui.fragments.DatePickerDialogFragment;
import org.icddrb.dhis.client.sdk.ui.models.FormEntity;
import org.icddrb.dhis.client.sdk.ui.models.FormEntityDate;
import org.icddrb.dhis.client.sdk.ui.views.AbsTextWatcher;
import org.icddrb.dhis.client.sdk.ui.views.RaisedButton;
import org.icddrb.dhis.client.sdk.utils.Preconditions;

public class DatePickerRowView implements RowView {
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    private final FragmentManager fragmentManager;

    private static class DatePickerRowViewHolder extends ViewHolder {
        public final ImageButton clearButton;
        public final RaisedButton datePickerButton;
        public final RaisedButton datePickerButtonNow;
        public final EditText editText;
        public final OnButtonClickListener onButtonClickListener;
        public final OnDateSetListener onDateSetListener = new OnDateSetListener(this.editText);
        public final OnValueChangedListener onValueChangedListener = new OnValueChangedListener();
        public final TextView textViewLabel;

        public DatePickerRowViewHolder(View itemView, FragmentManager fragmentManager) {
            super(itemView);
            this.textViewLabel = (TextView) itemView.findViewById(C0935R.id.textview_row_label);
            this.editText = (EditText) itemView.findViewById(C0935R.id.row_date_picker_edit_text);
            this.datePickerButtonNow = (RaisedButton) itemView.findViewById(C0935R.id.row_date_picker_button_today);
            this.datePickerButton = (RaisedButton) itemView.findViewById(C0935R.id.row_date_picker_button_pick);
            this.clearButton = (ImageButton) itemView.findViewById(C0935R.id.button_clear);
            this.onButtonClickListener = new OnButtonClickListener(this.editText, fragmentManager, this.onDateSetListener);
            this.editText.addTextChangedListener(this.onValueChangedListener);
            this.editText.setOnClickListener(this.onButtonClickListener);
            this.clearButton.setOnClickListener(this.onButtonClickListener);
            this.datePickerButton.setOnClickListener(this.onButtonClickListener);
            this.datePickerButtonNow.setOnClickListener(this.onButtonClickListener);
        }

        public void update(FormEntityDate formEntity) {
            this.onValueChangedListener.setDataEntity(formEntity);
            this.textViewLabel.setText(formEntity.getLabel());
            this.editText.setText(formEntity.getValue());
        }
    }

    private static class OnButtonClickListener implements OnClickListener {
        private static final String EMPTY_STRING = "";
        private final Calendar calendar = Calendar.getInstance();
        private final EditText editText;
        private final FragmentManager fragmentManager;
        private final OnDateSetListener onDateSetListener;

        public OnButtonClickListener(EditText editText, FragmentManager fragmentManager, OnDateSetListener onDateSetListener) {
            this.editText = editText;
            this.fragmentManager = fragmentManager;
            this.onDateSetListener = onDateSetListener;
        }

        public void onClick(View view) {
            if (view.getId() == C0935R.id.row_date_picker_edit_text || view.getId() == C0935R.id.row_date_picker_button_pick) {
                DatePickerDialogFragment datePicker = DatePickerDialogFragment.newInstance(false);
                datePicker.setOnDateSetListener(this.onDateSetListener);
                datePicker.show(this.fragmentManager);
            } else if (view.getId() == C0935R.id.button_clear) {
                this.editText.setText("");
            } else if (view.getId() == C0935R.id.row_date_picker_button_today) {
                this.onDateSetListener.onDateSet(null, this.calendar.get(1), this.calendar.get(2), this.calendar.get(5));
            }
        }
    }

    private static class OnDateSetListener implements android.app.DatePickerDialog.OnDateSetListener {
        private final EditText editText;

        public OnDateSetListener(EditText editText) {
            this.editText = editText;
        }

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(1, year);
            calendar.set(2, monthOfYear);
            calendar.set(5, dayOfMonth);
            this.editText.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(calendar.getTime()));
        }
    }

    private static class OnValueChangedListener extends AbsTextWatcher {
        private FormEntityDate dataEntity;

        private OnValueChangedListener() {
        }

        public void setDataEntity(FormEntityDate dataEntity) {
            this.dataEntity = dataEntity;
        }

        public void afterTextChanged(Editable editable) {
            if (this.dataEntity != null) {
                this.dataEntity.setValue(editable.toString(), true);
            }
        }
    }

    public DatePickerRowView(FragmentManager fragmentManager) {
        this.fragmentManager = (FragmentManager) Preconditions.isNull(fragmentManager, "fragmentManager must not be null");
    }

    public ViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent) {
        return new DatePickerRowViewHolder(inflater.inflate(C0935R.layout.recyclerview_row_datepicker, parent, false), this.fragmentManager);
    }

    public void onBindViewHolder(ViewHolder viewHolder, FormEntity formEntity) {
        ((DatePickerRowViewHolder) viewHolder).update((FormEntityDate) formEntity);
    }
}
