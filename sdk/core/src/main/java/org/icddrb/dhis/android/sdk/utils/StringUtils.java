package org.icddrb.dhis.android.sdk.utils;

public final class StringUtils {
    private StringBuilder mBuilder = new StringBuilder();

    private StringUtils() {
    }

    public static StringUtils create() {
        return new StringUtils();
    }

    public <T> StringUtils append(T item) {
        this.mBuilder.append(item);
        return this;
    }

    public String build() {
        return this.mBuilder.toString();
    }
}
