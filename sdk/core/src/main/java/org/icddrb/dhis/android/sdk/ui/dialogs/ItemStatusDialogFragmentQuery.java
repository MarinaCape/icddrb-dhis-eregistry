package org.icddrb.dhis.android.sdk.ui.dialogs;

import android.content.Context;
import org.icddrb.dhis.android.sdk.controllers.tracker.TrackerController;
import org.icddrb.dhis.android.sdk.events.OnRowClick.ITEM_STATUS;
import org.icddrb.dhis.android.sdk.persistence.loaders.Query;
import org.icddrb.dhis.android.sdk.persistence.models.BaseSerializableModel;

public class ItemStatusDialogFragmentQuery implements Query<ItemStatusDialogFragmentForm> {
    public static final String TAG = ItemStatusDialogFragmentQuery.class.getSimpleName();
    private long id;
    private String type;

    public ItemStatusDialogFragmentQuery(long id, String type) {
        this.id = id;
        this.type = type;
    }

    public ItemStatusDialogFragmentForm query(Context context) {
        BaseSerializableModel item = null;
        String str = this.type;
        int obj = 0;
        switch (str.hashCode()) {
            case -546411710:
                if (str.equals("TrackedEntityInstance")) {
                    obj = 0;
                    break;
                }
                break;
            case 67338874:
                if (str.equals("Event")) {
                    obj = 2;
                    break;
                }
                break;
            case 2109554468:
                if (str.equals("Enrollment")) {
                    obj = 1;
                    break;
                }
                break;
        }
        switch (obj) {
            case 0:
                item = TrackerController.getTrackedEntityInstance(this.id);
                break;
            case 1:
                item = TrackerController.getEnrollment(this.id);
                break;
            case 2:
                item = TrackerController.getEvent(this.id);
                break;
        }
        ItemStatusDialogFragmentForm form = new ItemStatusDialogFragmentForm();
        form.setItem(item);
        form.setType(this.type);
        if (item != null) {
            boolean failed = false;
            if (TrackerController.getFailedItem(this.type, this.id) != null) {
                failed = true;
            }
            if (failed) {
                form.setStatus(ITEM_STATUS.ERROR);
            } else if (item.isFromServer()) {
                form.setStatus(ITEM_STATUS.SENT);
            } else {
                form.setStatus(ITEM_STATUS.OFFLINE);
            }
        }
        return form;
    }
}
