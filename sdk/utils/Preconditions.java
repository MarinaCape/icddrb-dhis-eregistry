package org.icddrb.dhis.client.sdk.utils;

public class Preconditions {
    private Preconditions() {
    }

    public static <T> T isNull(T obj, String message) {
        if (obj != null) {
            return obj;
        }
        throw new IllegalArgumentException(message);
    }
}
