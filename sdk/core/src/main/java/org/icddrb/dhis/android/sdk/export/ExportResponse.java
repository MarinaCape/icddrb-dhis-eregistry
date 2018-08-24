package org.icddrb.dhis.android.sdk.export;

public class ExportResponse {
    private Throwable error;

    public Throwable getError() {
        return this.error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }
}
