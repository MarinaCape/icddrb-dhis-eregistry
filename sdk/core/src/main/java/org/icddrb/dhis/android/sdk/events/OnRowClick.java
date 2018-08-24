package org.icddrb.dhis.android.sdk.events;

import org.icddrb.dhis.android.sdk.persistence.models.BaseSerializableModel;

public abstract class OnRowClick<T extends BaseSerializableModel> {
    private final T item;
    private final ITEM_STATUS status;

    public enum ITEM_STATUS {
        OFFLINE,
        SENT,
        ERROR
    }

    public OnRowClick(ITEM_STATUS status, T item) {
        this.status = status;
        this.item = item;
    }

    public ITEM_STATUS getStatus() {
        return this.status;
    }

    public T getItem() {
        return this.item;
    }
}
