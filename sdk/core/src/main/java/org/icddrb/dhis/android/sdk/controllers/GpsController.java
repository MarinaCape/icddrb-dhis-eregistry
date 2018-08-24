package org.icddrb.dhis.android.sdk.controllers;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramRuleAction.Table;

public final class GpsController implements LocationListener {
    private static GpsController mManager;
    private final String TAG = GpsController.class.getSimpleName();
    private double mLatitude;
    private LocationManager mLocationManager;
    private double mLongitude;

    private GpsController(Context context) {
        this.mLocationManager = (LocationManager) context.getSystemService(Table.LOCATION);
    }

    public static void activateGps(Context context) {
        init(context);
        getInstance().requestLocationUpdates();
    }

    public static void disableGps() {
        try {
            getInstance().removeUpdates();
        } catch (IllegalArgumentException e) {
        }
    }

    public static void init(Context context) {
        if (mManager == null) {
            mManager = new GpsController(context);
        }
    }

    public static GpsController getInstance() {
        if (mManager != null) {
            return mManager;
        }
        throw new IllegalArgumentException("You have to call init() method first");
    }

    public void requestLocationUpdates() {
        for (String provider : this.mLocationManager.getProviders(true)) {
            Log.d(this.TAG, provider);
            this.mLocationManager.requestLocationUpdates(provider, 0, 0.0f, this, Looper.getMainLooper());
        }
    }

    public boolean isGpsAvailable() {
        return this.mLocationManager != null;
    }

    public static Location getLocation() {
        getInstance().requestLocationUpdates();
        String provider = getInstance().mLocationManager.getBestProvider(new Criteria(), false);
        Location location = null;
        if (provider != null) {
            location = getInstance().mLocationManager.getLastKnownLocation(provider);
        }
        if (location != null) {
            getInstance().mLatitude = location.getLatitude();
            getInstance().mLongitude = location.getLongitude();
        }
        location = new Location(provider);
        location.setLatitude(getInstance().getLatitude());
        location.setLongitude(getInstance().getLongitude());
        return location;
    }

    public void removeUpdates() {
        if (this.mLocationManager != null) {
            this.mLocationManager.removeUpdates(this);
        }
    }

    public void onLocationChanged(Location location) {
        this.mLatitude = location.getLatitude();
        this.mLongitude = location.getLongitude();
    }

    public void onProviderDisabled(String provider) {
        Log.d(this.TAG, "Provider disabled");
    }

    public void onProviderEnabled(String provider) {
        Log.d(this.TAG, "Provider enabled");
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public double getLatitude() {
        return this.mLatitude;
    }

    public double getLongitude() {
        return this.mLongitude;
    }
}
