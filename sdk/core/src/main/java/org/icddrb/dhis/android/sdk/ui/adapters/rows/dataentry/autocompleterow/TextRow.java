package org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.autocompleterow;

import android.widget.TextView.OnEditorActionListener;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.Row;

public abstract class TextRow extends Row {
    protected OnEditorActionListener mOnEditorActionListener;

    public void setOnEditorActionListener(OnEditorActionListener onEditorActionListener) {
        this.mOnEditorActionListener = onEditorActionListener;
    }
}
