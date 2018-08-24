package org.icddrb.dhis.android.sdk.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import org.icddrb.dhis.android.sdk.C0845R;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.DataEntryFragmentSection;

public final class SectionAdapter extends AbsAdapter<DataEntryFragmentSection> {
    private static final String DROPDOWN = "dropDown";
    private static final String NON_DROPDOWN = "nonDropDown";
    private List<DataEntryFragmentSection> mDataOriginalCopy;

    public SectionAdapter(LayoutInflater inflater) {
        super(inflater);
    }

    public View getDropDownView(int position, View view, ViewGroup parent) {
        if (view == null || !view.getTag().toString().equals(DROPDOWN)) {
            view = getInflater().inflate(C0845R.layout.toolbar_spinner_item_dropdown, parent, false);
            view.setTag(DROPDOWN);
        }
        ((TextView) view.findViewById(16908308)).setText(((DataEntryFragmentSection) getData().get(position)).getLabel());
        return view;
    }

    public View getView(int position, View view, ViewGroup parent) {
        if (view == null || !view.getTag().toString().equals(NON_DROPDOWN)) {
            view = getInflater().inflate(C0845R.layout.toolbar_spinner_item_actionbar, parent, false);
            view.setTag(NON_DROPDOWN);
        }
        ((TextView) view.findViewById(16908308)).setText(((DataEntryFragmentSection) getData().get(position)).getLabel());
        return view;
    }

    public void swapData(List<DataEntryFragmentSection> data) {
        boolean notifyAdapter = this.mData != data;
        this.mData = data;
        if (this.mData == null) {
            this.mDataOriginalCopy = null;
        } else {
            this.mDataOriginalCopy = new ArrayList(this.mData);
        }
        if (notifyAdapter) {
            notifyDataSetChanged();
        }
    }

    public void hideAll() {
        for (int i = 0; i < this.mData.size(); i++) {
            DataEntryFragmentSection section = (DataEntryFragmentSection) this.mData.get(i);
            if (i > 0) {
                section.setHidden(true);
                this.mData.remove(section);
                return;
            }
        }
    }

    public void hideSection(String id) {
        if (this.mData != null) {
            for (DataEntryFragmentSection section : this.mData) {
                if (section.getId() != null && section.getId().equals(id)) {
                    section.setHidden(true);
                    this.mData.remove(section);
                    return;
                }
            }
        }
    }

    public void resetHiding() {
        if (this.mData != null) {
            for (DataEntryFragmentSection s : this.mData) {
                s.setHidden(false);
            }
            this.mData = new ArrayList(this.mDataOriginalCopy);
        }
    }
}
