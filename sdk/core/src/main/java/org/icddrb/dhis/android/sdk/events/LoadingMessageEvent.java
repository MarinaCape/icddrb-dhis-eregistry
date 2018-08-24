package org.icddrb.dhis.android.sdk.events;

public class LoadingMessageEvent {
    public EventType eventType;
    public String message;

    public enum EventType {
        METADATA,
        DATA,
        REMOVE_DATA,
        STARTUP,
        FINISH
    }

    public LoadingMessageEvent(String message, EventType eventType) {
        this.message = message;
        this.eventType = eventType;
    }
}
