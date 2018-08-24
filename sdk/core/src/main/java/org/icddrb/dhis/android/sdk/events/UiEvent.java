package org.icddrb.dhis.android.sdk.events;

public final class UiEvent {
    private final UiEventType mType;

    public enum UiEventType {
        SYNC_DASHBOARDS,
        USER_LOG_OUT,
        SYNC_INTERPRETATIONS,
        SYNCING_START,
        SYNCING_END,
        INITIAL_SYNCING_END
    }

    public UiEvent(UiEventType type) {
        this.mType = type;
    }

    public UiEventType getEventType() {
        return this.mType;
    }
}
