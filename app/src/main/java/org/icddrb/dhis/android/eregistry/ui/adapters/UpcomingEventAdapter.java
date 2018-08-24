package org.icddrb.dhis.android.eregistry.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.icddrb.dhis.android.eregistry.ui.rows.upcomingevents.EventRowType;
import org.icddrb.dhis.android.sdk.ui.adapters.AbsAdapter;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.events.EventRow;

public class UpcomingEventAdapter extends AbsAdapter<EventRow> {
    public UpcomingEventAdapter(LayoutInflater inflater) {
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
