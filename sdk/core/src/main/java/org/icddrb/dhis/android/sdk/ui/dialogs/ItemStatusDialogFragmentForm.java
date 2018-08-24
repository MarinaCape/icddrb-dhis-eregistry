package org.icddrb.dhis.android.sdk.ui.dialogs;

import java.util.List;
import org.icddrb.dhis.android.sdk.events.OnRowClick.ITEM_STATUS;
import org.icddrb.dhis.android.sdk.persistence.models.BaseSerializableModel;

class ItemStatusDialogFragmentForm {
    private BaseSerializableModel item;
    private ITEM_STATUS status;
    private String type;
    private List<String> values;

    ItemStatusDialogFragmentForm() {
    }

    public BaseSerializableModel getItem() {
        return this.item;
    }

    public void setItem(BaseSerializableModel item) {
        this.item = item;
    }

    public List<String> getValues() {
        return this.values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public ITEM_STATUS getStatus() {
        return this.status;
    }

    public void setStatus(ITEM_STATUS status) {
        this.status = status;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
