package org.icddrb.dhis.android.eregistry.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import org.icddrb.dhis.android.eregistry.ui.rows.programoverview.ProgramStageRow;
import org.icddrb.dhis.android.sdk.ui.adapters.AbsAdapter;

public final class ProgramStageAdapter extends AbsAdapter<ProgramStageRow> {
    private static final String CLASS_TAG = ProgramStageAdapter.class.getSimpleName();

    public ProgramStageAdapter(LayoutInflater inflater) {
        super(inflater);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (getData() == null) {
            return null;
        }
        View view = ((ProgramStageRow) getData().get(position)).getView(getInflater(), convertView, parent);
        view.setId(position);
        return view;
    }

    public void swapData(List<ProgramStageRow> data) {
        boolean notifyAdapter = this.mData != data;
        this.mData = data;
        if (notifyAdapter) {
            notifyDataSetChanged();
        }
    }
}
