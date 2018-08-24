package org.icddrb.dhis.client.sdk.ui.models;

import android.support.annotation.NonNull;
import org.icddrb.dhis.client.sdk.ui.models.FormEntity.Type;

public class FormEntityCoordinate extends FormEntity {
    private double latitude;
    private double longitude;

    public FormEntityCoordinate(String id, String label) {
        this(id, label, null);
    }

    public FormEntityCoordinate(String id, String label, Object tag) {
        super(id, label, tag);
    }

    @NonNull
    public Type getType() {
        return Type.COORDINATES;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
