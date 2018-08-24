package org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry;

public class PlainTextRow extends NonEditableTextViewRow {
    public PlainTextRow(String value) {
        super(value);
    }

    public String getName() {
        return this.mLabel;
    }

    public int getViewType() {
        return 0;
    }
}
