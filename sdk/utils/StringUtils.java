package org.icddrb.dhis.client.sdk.utils;

public class StringUtils {
    private StringUtils() {
    }

    public static boolean isEmpty(CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }
}
