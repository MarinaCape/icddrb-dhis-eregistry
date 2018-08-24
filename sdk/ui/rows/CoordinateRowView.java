package org.icddrb.dhis.client.sdk.ui.rows;

import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import org.icddrb.dhis.client.sdk.ui.C0935R;
import org.icddrb.dhis.client.sdk.ui.models.FormEntity;
import org.icddrb.dhis.client.sdk.ui.models.FormEntityCoordinate;
import org.icddrb.dhis.client.sdk.ui.views.AbsTextWatcher;

public class CoordinateRowView implements RowView {
    private OnCoordinatesCaptured onCoordinatesCaptured;

    private static class CoordinateRowViewHolder extends ViewHolder {
        public final ImageButton captureCoordinateButton;
        public final EditText latitudeEditText;
        public final TextInputLayout latitudeTextInputLayout;
        public final EditText longitudeEditText;
        public final TextInputLayout longitudeTextInputLayout;
        public final OnCaptureCoordinatesListener onCaptureCoordinatesListener;
        public final OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener(this.latitudeTextInputLayout, this.latitudeEditText);
        public final OnValueChangedListener onValueChangedListener = new OnValueChangedListener();
        public final TextView textViewLabel;

        public CoordinateRowViewHolder(View itemView, OnCoordinatesCaptured onCoordinatesCaptured) {
            super(itemView);
            this.textViewLabel = (TextView) itemView.findViewById(C0935R.id.textview_row_label);
            this.latitudeTextInputLayout = (TextInputLayout) itemView.findViewById(C0935R.id.coordinate_row_latitude_textinputlayout);
            this.longitudeTextInputLayout = (TextInputLayout) itemView.findViewById(C0935R.id.coordinate_row_longitude_textinputlayout);
            this.latitudeEditText = (EditText) itemView.findViewById(C0935R.id.coordinate_row_latitude_edittext);
            this.longitudeEditText = (EditText) itemView.findViewById(C0935R.id.coordinate_row_longitude_edittext);
            this.captureCoordinateButton = (ImageButton) itemView.findViewById(C0935R.id.capture_coordinate_button_picker_view);
            this.latitudeTextInputLayout.setHint(itemView.getContext().getString(C0935R.string.enter_latitude));
            this.longitudeTextInputLayout.setHint(itemView.getContext().getString(C0935R.string.enter_longitude));
            this.onCaptureCoordinatesListener = new OnCaptureCoordinatesListener(onCoordinatesCaptured, this.latitudeEditText, this.longitudeEditText);
            this.latitudeEditText.setOnFocusChangeListener(this.onFocusChangeListener);
            this.latitudeEditText.addTextChangedListener(this.onValueChangedListener);
            this.longitudeEditText.setOnFocusChangeListener(this.onFocusChangeListener);
            this.longitudeEditText.addTextChangedListener(this.onValueChangedListener);
            this.captureCoordinateButton.setOnClickListener(this.onCaptureCoordinatesListener);
        }

        public void update(FormEntityCoordinate entity) {
            this.textViewLabel.setText(C0935R.string.enter_coordinates);
            this.onValueChangedListener.setDataEntity(entity);
            this.latitudeEditText.setText(String.valueOf(entity.getLatitude()));
            this.longitudeEditText.setText(String.valueOf(entity.getLongitude()));
            this.latitudeTextInputLayout.setHint(this.onFocusChangeListener.getHint());
            this.longitudeTextInputLayout.setHint(this.onFocusChangeListener.getHint());
        }
    }

    private static class OnCaptureCoordinatesListener implements OnClickListener {
        private final EditText latitudeEditText;
        private final EditText longitudeEditText;
        private final OnCoordinatesCaptured onCoordinatesCaptured;

        public OnCaptureCoordinatesListener(OnCoordinatesCaptured onCoordinatesCaptured, EditText latitudeEditText, EditText longitudeEditText) {
            this.onCoordinatesCaptured = onCoordinatesCaptured;
            this.latitudeEditText = latitudeEditText;
            this.longitudeEditText = longitudeEditText;
        }

        public void onClick(View v) {
            if (this.onCoordinatesCaptured == null) {
                this.latitudeEditText.setText(String.valueOf(0.0d));
                this.longitudeEditText.setText(String.valueOf(0.0d));
            }
        }
    }

    public interface OnCoordinatesCaptured {
        void onLatitudeChangeListener(double d);

        void onLongitudeChangeListener(double d);
    }

    private static class OnFocusChangeListener implements android.view.View.OnFocusChangeListener {
        private final EditText editText;
        private final CharSequence hint = this.textInputLayout.getHint();
        private final TextInputLayout textInputLayout;

        public OnFocusChangeListener(TextInputLayout inputLayout, EditText editText) {
            this.textInputLayout = inputLayout;
            this.editText = editText;
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                this.textInputLayout.setHint(this.hint);
            } else if (!TextUtils.isEmpty(this.editText.getText().toString())) {
                this.textInputLayout.setHint(null);
            }
        }

        public CharSequence getHint() {
            return this.hint;
        }
    }

    private static class OnValueChangedListener extends AbsTextWatcher {
        private FormEntityCoordinate dataEntity;

        private OnValueChangedListener() {
        }

        public void setDataEntity(FormEntityCoordinate dataEntity) {
            this.dataEntity = dataEntity;
        }

        public void afterTextChanged(Editable editable) {
            if (this.dataEntity == null) {
            }
        }
    }

    public void setOnCoordinatesCaptured(OnCoordinatesCaptured onCoordinatesCaptured) {
        this.onCoordinatesCaptured = onCoordinatesCaptured;
    }

    public ViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent) {
        return new CoordinateRowViewHolder(inflater.inflate(C0935R.layout.recyclerview_row_coordinate, parent, false), this.onCoordinatesCaptured);
    }

    public void onBindViewHolder(ViewHolder viewHolder, FormEntity formEntity) {
        ((CoordinateRowViewHolder) viewHolder).update((FormEntityCoordinate) formEntity);
    }
}
