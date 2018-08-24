package org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry;

public class KeyValueRow extends NonEditableTextViewRow {
    private String mLabel;

    public KeyValueRow(String label, String value) {
        super(value);
        this.mLabel = label;
    }

    public String getName() {
        return this.mLabel;
    }

    public int getViewType() {
        return 0;
    }
}
