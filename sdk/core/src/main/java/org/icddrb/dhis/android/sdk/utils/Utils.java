package org.icddrb.dhis.android.sdk.utils;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import java.util.UUID;
import org.icddrb.dhis.android.sdk.controllers.DhisController;
import org.joda.time.DateTime;

public class Utils {
    private static final String CLASS_TAG = "Utils";
    public static final String randomUUID = (DhisController.QUEUED + UUID.randomUUID().toString());

    public static final int getDpPx(int dp, DisplayMetrics displayMetrics) {
        return (int) TypedValue.applyDimension(1, (float) dp, displayMetrics);
    }

    public static String removeTimeFromDateString(String dateTime) {
        if (TextUtils.isEmpty(dateTime)) {
            return null;
        }
        return new DateTime((Object) dateTime).toLocalDate().toString();
    }

    public static String getTempUid() {
        return DhisController.QUEUED + UUID.randomUUID().toString();
    }

    public static boolean isLocal(String uid) {
        if (uid == null || uid.length() == randomUUID.length()) {
            return true;
        }
        return false;
    }
}
