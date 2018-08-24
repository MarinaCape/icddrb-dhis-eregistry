package org.icddrb.dhis.android.sdk.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.events.EventRow;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.events.EventRowType;

public class EventAdapter extends AbsAdapter<EventRow> {
    public EventAdapter(LayoutInflater inflater) {
        super(inflater);
    }

    public long getItemId(int position) {
        if (getData() != null) {
            return ((EventRow) getData().get(position)).getId();
        }
        return -1;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (getData() != null) {
            return ((EventRow) getData().get(position)).getView(getInflater(), convertView, parent);
        }
        return null;
    }

    public boolean areAllItemsEnabled() {
        return false;
    }

    public boolean isEnabled(int position) {
        return getData() != null && ((EventRow) getData().get(position)).isEnabled();
    }

    public int getViewTypeCount() {
        return EventRowType.values().length;
    }

    public int getItemViewType(int position) {
        if (getData() != null) {
            return ((EventRow) getData().get(position)).getViewType();
        }
        return 0;
    }
}
