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
import io.fabric.sdk.android.services.common.IdManager;
import org.icddrb.dhis.android.sdk.C0845R;
import org.icddrb.dhis.android.sdk.controllers.GpsController;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.models.DataValue;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.AbsTextWatcher;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.events.OnDetailedInfoButtonClick;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.RowValueChangedEvent;

public final class EventCoordinatesRow extends Row {
    private static final String EMPTY_FIELD = "";
    private final int MAX_INPUT_LENGTH = 9;
    protected boolean latitudeError;
    protected boolean longitudeError;
    private final Event mEvent;

    private class CoordinateViewHolder {
        private final ImageButton captureCoords;
        private final View detailedInfoButton;
        private final EditText latitude;
        private final LatitudeWatcher latitudeWatcher;
        private final EditText longitude;
        private final LongitudeWatcher longitudeWatcher;
        private final OnCaptureCoordsClickListener onButtonClickListener = new OnCaptureCoordsClickListener(this.latitude, this.longitude);

        public CoordinateViewHolder(View view, View detailedInfoButton) {
            String latitudeMessage = view.getContext().getString(C0845R.string.latitude_error_message);
            String longitudeMessage = view.getContext().getString(C0845R.string.longitude_error_message);
            this.latitude = (EditText) view.findViewById(C0845R.id.latitude_edittext);
            this.longitude = (EditText) view.findViewById(C0845R.id.longitude_edittext);
            this.captureCoords = (ImageButton) view.findViewById(C0845R.id.capture_coordinates);
            this.detailedInfoButton = detailedInfoButton;
            this.latitudeWatcher = new LatitudeWatcher(this.latitude, latitudeMessage);
            this.longitudeWatcher = new LongitudeWatcher(this.longitude, longitudeMessage);
            this.latitude.addTextChangedListener(this.latitudeWatcher);
            this.longitude.addTextChangedListener(this.longitudeWatcher);
            this.captureCoords.setOnClickListener(this.onButtonClickListener);
        }

        public void updateViews(Event event) {
            String lat;
            String lon;
            this.latitudeWatcher.setEvent(event);
            this.longitudeWatcher.setEvent(event);
            if (event.getLatitude() == null) {
                lat = "";
            } else {
                lat = String.valueOf(event.getLatitude());
            }
            if (event.getLongitude() == null) {
                lon = "";
            } else {
                lon = String.valueOf(event.getLongitude());
            }
            this.latitude.setText(lat);
            this.longitude.setText(lon);
        }
    }

    private static abstract class CoordinateWatcher extends AbsTextWatcher {
        final String mCoordinateMessage;
        final EditText mEditText;
        Event mEvent;
        double value;

        public abstract void afterTextChanged(Editable editable);

        public CoordinateWatcher(EditText mEditText, String mCoordinateMessage) {
            this.mEditText = mEditText;
            this.mCoordinateMessage = mCoordinateMessage;
        }

        public void setEvent(Event mEvent) {
            this.mEvent = mEvent;
        }
    }

    private abstract class InvalidInputValueFilter implements InputFilter {
        final Event event;
        final String invalidValue = IdManager.DEFAULT_VERSION_NAME;

        public abstract CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4);

        public InvalidInputValueFilter(Event event) {
            this.event = event;
        }
    }

    private class InvalidLatitudeInputValueFilter extends InvalidInputValueFilter {
        public InvalidLatitudeInputValueFilter(Event event) {
            super(event);
        }

        public CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
            if (charSequence == null || !charSequence.toString().trim().equals(IdManager.DEFAULT_VERSION_NAME)) {
                return null;
            }
            if (this.event.getLatitude() == null) {
                return IdManager.DEFAULT_VERSION_NAME;
            }
            return Double.toString(this.event.getLatitude().doubleValue());
        }
    }

    private class InvalidLongitudeInputValueFilter extends InvalidInputValueFilter {
        public InvalidLongitudeInputValueFilter(Event event) {
            super(event);
        }

        public CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
            if (charSequence == null || !charSequence.toString().trim().equals(IdManager.DEFAULT_VERSION_NAME)) {
                return null;
            }
            if (this.event.getLongitude() == null) {
                return IdManager.DEFAULT_VERSION_NAME;
            }
            return Double.toString(this.event.getLongitude().doubleValue());
        }
    }

    private class LatitudeWatcher extends CoordinateWatcher {
        public LatitudeWatcher(EditText mLatitude, String mLatitudeMessage) {
            super(mLatitude, mLatitudeMessage);
        }

        public void afterTextChanged(Editable s) {
            if (this.mEvent.getLatitude() != null) {
                this.value = this.mEvent.getLatitude().doubleValue();
            }
            if (s.length() > 1) {
                double newValue = Double.parseDouble(s.toString());
                if (newValue < -90.0d || newValue > 90.0d) {
                    this.mEditText.setError(this.mCoordinateMessage);
                    saveLatitude(null);
                    return;
                }
                saveLatitude(Double.valueOf(newValue));
            }
        }

        private void saveLatitude(Double newValue) {
            if (newValue == null) {
                EventCoordinatesRow.this.latitudeError = true;
                EventCoordinatesRow.this.mErrorStringId = Integer.valueOf(C0845R.string.error_location_values);
            } else {
                EventCoordinatesRow.this.latitudeError = false;
                if (!EventCoordinatesRow.this.longitudeError) {
                    EventCoordinatesRow.this.mErrorStringId = null;
                }
            }
            this.mEvent.setLatitude(newValue);
            DataValue dataValue = new DataValue();
            dataValue.setValue("" + newValue);
            Dhis2Application.getEventBus().post(new RowValueChangedEvent(dataValue, DataEntryRowTypes.EVENT_COORDINATES.toString()));
        }
    }

    private class LongitudeWatcher extends CoordinateWatcher {
        public LongitudeWatcher(EditText mLongitude, String mLongitudeMessage) {
            super(mLongitude, mLongitudeMessage);
        }

        public void afterTextChanged(Editable s) {
            if (this.mEvent.getLongitude() != null) {
                this.value = this.mEvent.getLongitude().doubleValue();
            }
            if (s.length() > 1) {
                Double newValue = Double.valueOf(Double.parseDouble(s.toString()));
                if (newValue.doubleValue() < -180.0d || newValue.doubleValue() > 180.0d) {
                    this.mEditText.setError(this.mCoordinateMessage);
                    saveLongitude(null);
                    return;
                }
                saveLongitude(newValue);
            }
        }

        private void saveLongitude(Double newValue) {
            if (newValue == null) {
                EventCoordinatesRow.this.longitudeError = true;
                EventCoordinatesRow.this.mErrorStringId = Integer.valueOf(C0845R.string.error_location_values);
            } else {
                EventCoordinatesRow.this.longitudeError = false;
                if (!EventCoordinatesRow.this.latitudeError) {
                    EventCoordinatesRow.this.mErrorStringId = null;
                }
            }
            this.mEvent.setLongitude(newValue);
            DataValue dataValue = new DataValue();
            dataValue.setValue("" + newValue);
            Dhis2Application.getEventBus().post(new RowValueChangedEvent(dataValue, DataEntryRowTypes.EVENT_COORDINATES.toString()));
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

    public EventCoordinatesRow(Event event) {
        this.mEvent = event;
    }

    public View getView(FragmentManager fragmentManager, LayoutInflater inflater, View convertView, ViewGroup container) {
        CoordinateViewHolder holder;
        View view;
        if (convertView == null || !(convertView.getTag() instanceof CoordinateViewHolder)) {
            View root = inflater.inflate(C0845R.layout.listview_row_event_coordinate_picker, container, false);
            holder = new CoordinateViewHolder(root, root.findViewById(C0845R.id.detailed_info_button_layout));
            root.setTag(holder);
            view = root;
        } else {
            view = convertView;
            holder = (CoordinateViewHolder) view.getTag();
        }
        holder.detailedInfoButton.setOnClickListener(new OnDetailedInfoButtonClick(this));
        InputFilter[] latitudeFilters = new InputFilter[2];
        InputFilter[] longitudeFilters = new InputFilter[2];
        InputFilter maxCharFilter = new LengthFilter(9);
        InputFilter invalidLatitudeFilter = new InvalidLatitudeInputValueFilter(this.mEvent);
        InputFilter invalidLongitudeFilter = new InvalidLongitudeInputValueFilter(this.mEvent);
        latitudeFilters[0] = maxCharFilter;
        latitudeFilters[1] = invalidLatitudeFilter;
        longitudeFilters[0] = maxCharFilter;
        longitudeFilters[1] = invalidLongitudeFilter;
        holder.latitude.setFilters(latitudeFilters);
        holder.longitude.setFilters(longitudeFilters);
        holder.updateViews(this.mEvent);
        return view;
    }

    public int getViewType() {
        return DataEntryRowTypes.EVENT_COORDINATES.ordinal();
    }
}
