package org.icddrb.dhis.android.sdk.events;

import android.content.Context;

import org.icddrb.dhis.android.sdk.R;

public class OnTeiDownloadedEvent {
    private boolean errorHasOccured;
    private int eventNumber;
    private EventType eventType;
    private int totalNumberOfTeis;

    public enum EventType {
        START,
        UPDATE,
        ERROR,
        END
    }

    public OnTeiDownloadedEvent(EventType eventType) {
        this(eventType, -1);
    }

    public OnTeiDownloadedEvent(EventType eventType, int totalNumberOfTeis) {
        this(eventType, totalNumberOfTeis, 0);
    }

    public OnTeiDownloadedEvent(EventType eventType, int totalNumberOfTeis, int eventNumber) {
        this.eventType = eventType;
        this.totalNumberOfTeis = totalNumberOfTeis;
        this.eventNumber = eventNumber;
    }

    public String getUserFriendlyMessage(Context context) {
        switch (this.eventType) {
            case START:
                return String.format(context.getString(R.string.downloading) + " 1/%s", new Object[]{Integer.valueOf(this.totalNumberOfTeis)});
            case UPDATE:
                return String.format(context.getString(R.string.downloading) + " %s/%s", new Object[]{Integer.valueOf(this.eventNumber), Integer.valueOf(this.totalNumberOfTeis)});
            case ERROR:
                return String.format(context.getString(R.string.error_downloading) + " %s", new Object[]{Integer.valueOf(this.eventNumber)});
            case END:
                if (this.errorHasOccured) {
                    return context.getString(R.string.downloaded_with_errors);
                }
                return context.getString(R.string.download_complete);
            default:
                return context.getString(R.string.download_error_try_again);
        }
    }

    public int getMessageDuration() {
        switch (this.eventType) {
            case END:
                return -1;
            default:
                return -2;
        }
    }

    public void setErrorHasOccured(boolean errorHasOccured) {
        this.errorHasOccured = errorHasOccured;
    }

    public EventType getEventType() {
        return this.eventType;
    }
}
