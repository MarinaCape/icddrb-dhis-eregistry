package org.icddrb.dhis.android.eregistry.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import org.icddrb.dhis.android.eregistry.C0773R;
import org.icddrb.dhis.android.sdk.persistence.models.Program;
import org.icddrb.dhis.android.sdk.ui.adapters.AbsAdapter;

public final class ProgramAdapter extends AbsAdapter<Program> {
    private static final String DROPDOWN = "dropDown";
    private static final String NON_DROPDOWN = "nonDropDown";

    public ProgramAdapter(LayoutInflater inflater) {
        super(inflater);
    }

    public View getDropDownView(int position, View view, ViewGroup parent) {
        if (view == null || !view.getTag().toString().equals(DROPDOWN)) {
            view = getInflater().inflate(C0773R.layout.toolbar_spinner_item_dropdown, parent, false);
            view.setTag(DROPDOWN);
        }
        ((TextView) view.findViewById(16908308)).setText(((Program) getData().get(position)).getName());
        return view;
    }

    public View getView(int position, View view, ViewGroup parent) {
        if (view == null || !view.getTag().toString().equals(NON_DROPDOWN)) {
            view = getInflater().inflate(C0773R.layout.toolbar_spinner_item_actionbar, parent, false);
            view.setTag(NON_DROPDOWN);
        }
        ((TextView) view.findViewById(16908308)).setText(((Program) getData().get(position)).getName());
        return view;
    }

    public void swapData(List<Program> data) {
        boolean notifyAdapter = this.mData != data;
        this.mData = data;
        if (notifyAdapter) {
            notifyDataSetChanged();
        }
    }
}
