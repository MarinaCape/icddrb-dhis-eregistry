package org.icddrb.dhis.android.eregistry.fragments.programoverview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.icddrb.dhis.android.eregistry.C0773R;

public class KeyValueView {
    private String mLabel;
    private String mValue;

    public KeyValueView(String label, String value) {
        this.mLabel = label;
        this.mValue = value;
    }

    public View getView(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(C0773R.layout.keyvalueview, container, false);
        TextView value = (TextView) root.findViewById(C0773R.id.value);
        ((TextView) root.findViewById(C0773R.id.label)).setText(this.mLabel);
        value.setText(this.mValue);
        return root;
    }
}
