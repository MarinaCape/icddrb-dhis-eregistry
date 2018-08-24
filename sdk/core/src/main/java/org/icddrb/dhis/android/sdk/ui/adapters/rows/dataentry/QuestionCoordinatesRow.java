package org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry;

import android.location.Location;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import io.fabric.sdk.android.services.common.IdManager;
import org.icddrb.dhis.android.sdk.C0845R;
import org.icddrb.dhis.android.sdk.controllers.GpsController;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.models.BaseValue;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.AbsTextWatcher;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.autocompleterow.TextRow;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.events.OnDetailedInfoButtonClick;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.RowValueChangedEvent;
import org.icddrb.dhis.client.sdk.ui.AppPreferencesImpl;

public final class QuestionCoordinatesRow extends TextRow {
    private static final String EMPTY_FIELD = "";
    public static final String UNDEFINED = "undefined";
    private final int MAX_INPUT_LENGTH = 9;

    private class CoordinateViewHolder {
        private final ImageButton captureCoords;
        public final View detailedInfoButton;
        private final TextView labelTextView;
        private final EditText latitude;
        private final LatitudeWatcher latitudeWatcher;
        private final EditText longitude;
        private final LongitudeWatcher longitudeWatcher;
        private final OnCaptureCoordsClickListener onButtonClickListener = new OnCaptureCoordsClickListener(this.latitude, this.longitude);

        public CoordinateViewHolder(View view, View detailedInfoButton) {
            String latitudeMessage = view.getContext().getString(C0845R.string.latitude_error_message);
            String longitudeMessage = view.getContext().getString(C0845R.string.longitude_error_message);
            this.labelTextView = (TextView) view.findViewById(C0845R.id.text_label);
            this.latitude = (EditText) view.findViewById(C0845R.id.latitude_edittext);
            this.longitude = (EditText) view.findViewById(C0845R.id.longitude_edittext);
            this.captureCoords = (ImageButton) view.findViewById(C0845R.id.capture_coordinates);
            this.detailedInfoButton = detailedInfoButton;
            this.latitudeWatcher = new LatitudeWatcher(this.latitude, this.longitude, latitudeMessage, longitudeMessage);
            this.longitudeWatcher = new LongitudeWatcher(this.latitude, this.longitude, latitudeMessage, longitudeMessage);
            this.latitude.addTextChangedListener(this.latitudeWatcher);
            this.longitude.addTextChangedListener(this.longitudeWatcher);
            this.captureCoords.setOnClickListener(this.onButtonClickListener);
        }

        public void updateViews(String labelText, BaseValue baseValue) {
            String lon = QuestionCoordinatesRow.getLongitudeFromValue(baseValue);
            String lat = QuestionCoordinatesRow.getLatitudeFromValue(baseValue);
            this.labelTextView.setText(labelText);
            this.latitudeWatcher.setBaseValue(baseValue);
            this.latitude.setText(lat);
            this.longitudeWatcher.setBaseValue(baseValue);
            this.longitude.setText(lon);
        }
    }

    private static abstract class CoordinateWatcher extends AbsTextWatcher {
        BaseValue mBaseValue;
        final EditText mEditTextLatitude;
        final EditText mEditTextLongitude;
        final String mLatitudeMessage;
        final String mLongitudeMessage;

        public abstract void afterTextChanged(Editable editable);

        public CoordinateWatcher(EditText mEditTextLatitude, EditText mEditTextLongitude, String mLatitudeMessage, String mLongitudeMessage) {
            this.mEditTextLatitude = mEditTextLatitude;
            this.mEditTextLongitude = mEditTextLongitude;
            this.mLongitudeMessage = mLongitudeMessage;
            this.mLatitudeMessage = mLatitudeMessage;
        }

        public void setBaseValue(BaseValue mDataValue) {
            this.mBaseValue = mDataValue;
        }
    }

    private abstract class InvalidInputValueFilter implements InputFilter {
        BaseValue baseValue;
        final String invalidValue = IdManager.DEFAULT_VERSION_NAME;

        public abstract CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4);

        public InvalidInputValueFilter(BaseValue baseValue) {
            this.baseValue = baseValue;
        }
    }

    private class InvalidLatitudeInputValueFilter extends InvalidInputValueFilter {
        public InvalidLatitudeInputValueFilter(BaseValue latitude) {
            super(latitude);
        }

        public CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
            if (charSequence == null || !charSequence.toString().trim().equals(IdManager.DEFAULT_VERSION_NAME)) {
                return null;
            }
            if (this.baseValue == null || this.baseValue.getValue() == null || QuestionCoordinatesRow.getLongitudeFromValue(this.baseValue) == null) {
                return IdManager.DEFAULT_VERSION_NAME;
            }
            return QuestionCoordinatesRow.getLongitudeFromValue(this.baseValue);
        }
    }

    private class InvalidLongitudeInputValueFilter extends InvalidInputValueFilter {
        public InvalidLongitudeInputValueFilter(BaseValue longitude) {
            super(longitude);
        }

        public CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
            if (charSequence == null || !charSequence.toString().trim().equals(IdManager.DEFAULT_VERSION_NAME)) {
                return null;
            }
            if (this.baseValue == null || this.baseValue.getValue() == null || QuestionCoordinatesRow.getLatitudeFromValue(this.baseValue) == null) {
                return IdManager.DEFAULT_VERSION_NAME;
            }
            return QuestionCoordinatesRow.getLatitudeFromValue(this.baseValue);
        }
    }

    private class LatitudeWatcher extends CoordinateWatcher {
        public LatitudeWatcher(EditText mLatitude, EditText mLongitude, String mLatitudeMessage, String mLongitudeMessage) {
            super(mLatitude, mLongitude, mLatitudeMessage, mLongitudeMessage);
        }

        public void afterTextChanged(Editable s) {
            if (s.length() > 1 && !s.toString().equals(QuestionCoordinatesRow.getLatitudeFromValue(this.mBaseValue))) {
                String newValue = s.toString();
                QuestionCoordinatesRow.saveCoordinates(this.mEditTextLatitude, this.mEditTextLongitude, this.mBaseValue);
                setValidationError(newValue);
            }
        }

        private void setValidationError(String newValue) {
            if (QuestionCoordinatesRow.isInvalidLatitude(newValue)) {
                this.mEditTextLatitude.setError(this.mLatitudeMessage);
            }
            QuestionCoordinatesRow.this.mErrorStringId = null;
            if (this.mEditTextLatitude.getText().length() > 0 && QuestionCoordinatesRow.isInvalidLatitude(this.mEditTextLatitude.getText().toString())) {
                QuestionCoordinatesRow.this.mErrorStringId = Integer.valueOf(C0845R.string.error_location_values);
            }
            if (this.mEditTextLongitude.getText().length() > 0 && QuestionCoordinatesRow.isInvalidLongitude(this.mEditTextLongitude.getText().toString())) {
                QuestionCoordinatesRow.this.mErrorStringId = Integer.valueOf(C0845R.string.error_location_values);
            }
        }
    }

    private class LongitudeWatcher extends CoordinateWatcher {
        public LongitudeWatcher(EditText mLatitude, EditText mLongitude, String mLatitudeMessage, String mLongitudeMessage) {
            super(mLatitude, mLongitude, mLatitudeMessage, mLongitudeMessage);
        }

        public void afterTextChanged(Editable s) {
            if (s.length() > 1 && !s.toString().equals(QuestionCoordinatesRow.getLatitudeFromValue(this.mBaseValue))) {
                String newValue = s.toString();
                QuestionCoordinatesRow.saveCoordinates(this.mEditTextLatitude, this.mEditTextLongitude, this.mBaseValue);
                setValidationError(newValue);
            }
        }

        private void setValidationError(String newValue) {
            if (QuestionCoordinatesRow.isInvalidLongitude(newValue)) {
                this.mEditTextLongitude.setError(this.mLongitudeMessage);
            }
            QuestionCoordinatesRow.this.mErrorStringId = null;
            if (this.mEditTextLatitude.getText().length() > 0 && QuestionCoordinatesRow.isInvalidLatitude(this.mEditTextLatitude.getText().toString())) {
                QuestionCoordinatesRow.this.mErrorStringId = Integer.valueOf(C0845R.string.error_location_values);
            }
            if (this.mEditTextLongitude.getText().length() > 0 && QuestionCoordinatesRow.isInvalidLongitude(this.mEditTextLongitude.getText().toString())) {
                QuestionCoordinatesRow.this.mErrorStringId = Integer.valueOf(C0845R.string.error_location_values);
            }
        }
    }

    private static class OnCaptureCoordsClickListener implements OnClickListener {
        private final EditText mLatitude;
        private final EditText mLongitude;

        public OnCaptureCoordsClickListener(EditText latitude, EditText longitude) {
            this.mLatitude = latitude;
            this.mLongitude = longitude;
        }

        public void onClick(View v) {
            Location location = GpsController.getLocation();
            this.mLatitude.setText(String.valueOf(location.getLatitude()));
            this.mLongitude.setText(String.valueOf(location.getLongitude()));
        }
    }

    public static BaseValue saveCoordinates(EditText latitude, EditText longitude, BaseValue value) {
        value.setValue(getCoordinateValue(latitude, longitude));
        Dhis2Application.getEventBus().post(new RowValueChangedEvent(value, DataEntryRowTypes.TEXT.toString()));
        return value;
    }

    public static String getLatitudeFromValue(BaseValue baseValue) {
        if (baseValue == null || baseValue.getValue() == null) {
            return "0";
        }
        String value = baseValue.getValue();
        if (value.contains(",")) {
            String latitude = value.substring(value.indexOf(",") + 1, value.length()).replace("]", "");
            if (!latitude.equals(UNDEFINED)) {
                return latitude;
            }
        }
        return "";
    }

    public static String getLongitudeFromValue(BaseValue baseValue) {
        if (baseValue == null || baseValue.getValue() == null) {
            return "0";
        }
        String value = baseValue.getValue();
        if (value.contains(",")) {
            String longitude = value.substring(0, value.indexOf(",")).replace("[", "");
            if (!longitude.equals(UNDEFINED)) {
                return longitude;
            }
        }
        return "";
    }

    public static String getCoordinateValue(EditText latitude, EditText longitude) {
        String latitudeValue = latitude.getText().toString();
        if (!(latitudeValue == null || latitudeValue.equals("") || latitudeValue.equals(UNDEFINED) || !isInvalidLatitude(latitudeValue))) {
            latitudeValue = UNDEFINED;
        }
        String longitudeValue = longitude.getText().toString();
        if (!(longitudeValue == null || longitudeValue.equals("") || longitudeValue.equals(UNDEFINED) || !isInvalidLongitude(longitudeValue))) {
            longitudeValue = UNDEFINED;
        }
        if (new AppPreferencesImpl(latitude.getContext()).getAPiVersion().equals("2.25")) {
            return longitudeValue + "," + latitudeValue;
        }
        return "[" + longitudeValue + "," + latitudeValue + "]";
    }

    public QuestionCoordinatesRow(String label, boolean mandatory, String warning, BaseValue baseValue, DataEntryRowTypes rowType) {
        this.mLabel = label;
        this.mMandatory = mandatory;
        this.mWarning = warning;
        this.mValue = baseValue;
        this.mRowType = rowType;
    }

    public View getView(FragmentManager fragmentManager, LayoutInflater inflater, View convertView, ViewGroup container) {
        CoordinateViewHolder holder;
        View view;
        if (convertView == null || !(convertView.getTag() instanceof QuestionCoordinatesRow)) {
            View root = inflater.inflate(C0845R.layout.listview_row_event_coordinate_picker, container, false);
            root.setBackgroundColor(-1);
            holder = new CoordinateViewHolder(root, root.findViewById(C0845R.id.detailed_info_button_layout));
            root.setTag(holder);
            view = root;
        } else {
            view = convertView;
            holder = (CoordinateViewHolder) view.getTag();
        }
        holder.detailedInfoButton.setOnClickListener(new OnDetailedInfoButtonClick(this));
        if (this.mValue != null) {
            holder.updateViews(this.mLabel, this.mValue);
        }
        InputFilter[] latitudeFilters = new InputFilter[2];
        InputFilter[] longitudeFilters = new InputFilter[2];
        InputFilter maxCharFilter = new LengthFilter(9);
        InputFilter invalidLatitudeFilter = new InvalidLatitudeInputValueFilter(this.mValue);
        InputFilter invalidLongitudeFilter = new InvalidLongitudeInputValueFilter(this.mValue);
        latitudeFilters[0] = maxCharFilter;
        latitudeFilters[1] = invalidLatitudeFilter;
        longitudeFilters[0] = maxCharFilter;
        longitudeFilters[1] = invalidLongitudeFilter;
        holder.latitude.setFilters(latitudeFilters);
        holder.longitude.setFilters(longitudeFilters);
        holder.updateViews(this.mLabel, this.mValue);
        holder.latitude.setOnEditorActionListener(this.mOnEditorActionListener);
        holder.longitude.setOnEditorActionListener(this.mOnEditorActionListener);
        return view;
    }

    public int getViewType() {
        return DataEntryRowTypes.QUESTION_COORDINATES.ordinal();
    }

    private static boolean isInvalidLongitude(String newValue) {
        return Double.parseDouble(newValue) < -180.0d || Double.parseDouble(newValue) > 180.0d;
    }

    private static boolean isInvalidLatitude(String newValue) {
        return Double.parseDouble(newValue) < -90.0d || Double.parseDouble(newValue) > 90.0d;
    }
}
