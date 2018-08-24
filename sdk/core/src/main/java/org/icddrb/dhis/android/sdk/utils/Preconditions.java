package org.icddrb.dhis.android.sdk.utils;

import android.util.Log;

public class Preconditions {
    public static final String TAG = Preconditions.class.getSimpleName();

    private Preconditions() {
    }

    public static <T> T isNull(T obj, String message) {
        if (obj != null) {
            return obj;
        }
        Log.d(TAG, message);
        throw new IllegalArgumentException(message);
    }
}
