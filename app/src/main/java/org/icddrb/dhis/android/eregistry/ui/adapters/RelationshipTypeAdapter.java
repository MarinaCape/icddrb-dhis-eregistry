package org.icddrb.dhis.android.eregistry.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import org.icddrb.dhis.android.eregistry.C0773R;
import org.icddrb.dhis.android.sdk.ui.adapters.AbsAdapter;

public final class RelationshipTypeAdapter extends AbsAdapter<String> {
    private static final String DROPDOWN = "dropDown";
    private static final String NON_DROPDOWN = "nonDropDown";
    private String relationshipType;

    public RelationshipTypeAdapter(LayoutInflater inflater) {
        super(inflater);
    }

    public View getDropDownView(int position, View view, ViewGroup parent) {
        if (view == null || !view.getTag().toString().equals(DROPDOWN)) {
            view = getInflater().inflate(C0773R.layout.toolbar_spinner_item_dropdown, parent, false);
            view.setTag(DROPDOWN);
        }
        ((TextView) view.findViewById(16908308)).setText((CharSequence) getData().get(position));
        return view;
    }

    public View getView(int position, View view, ViewGroup parent) {
        if (view == null || !view.getTag().toString().equals(NON_DROPDOWN)) {
            view = getInflater().inflate(C0773R.layout.toolbar_spinner_item_darktext, parent, false);
            view.setTag(NON_DROPDOWN);
        }
        ((TextView) view.findViewById(16908308)).setText((CharSequence) getData().get(position));
        return view;
    }

    public void swapData(List<String> data, String relationshipType) {
        this.relationshipType = relationshipType;
        super.swapData(data);
    }

    public String getRelationshipType() {
        return this.relationshipType;
    }

    public void setRelationshipType(String relationshipType) {
        this.relationshipType = relationshipType;
    }
}
