package org.icddrb.dhis.android.sdk.events;

import org.icddrb.dhis.android.sdk.events.OnRowClick.ITEM_STATUS;
import org.icddrb.dhis.android.sdk.persistence.models.BaseSerializableModel;

public class OnTrackerItemClick extends OnRowClick<BaseSerializableModel> {
    private final boolean onDescriptionClick;

    public OnTrackerItemClick(BaseSerializableModel item, ITEM_STATUS status, boolean description) {
        super(status, item);
        this.onDescriptionClick = description;
    }

    public boolean isOnDescriptionClick() {
        return this.onDescriptionClick;
    }
}
