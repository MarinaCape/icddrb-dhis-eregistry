package org.icddrb.dhis.android.eregistry.ui.rows.upcomingevents;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.icddrb.dhis.android.eregistry.C0773R;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.events.EventRow;

public final class UpcomingEventItemRow implements EventRow {
    private String mDueDate;
    private long mEventId;
    private String mEventName;
    private String mFirstItem;
    private String mSecondItem;

    private static class ViewHolder {
        public final TextView dueDate;
        public final TextView eventName;
        public final TextView firstItem;
        public final TextView secondItem;

        private ViewHolder(TextView firstItem, TextView secondItem, TextView dueDate, TextView eventName) {
            this.firstItem = firstItem;
            this.secondItem = secondItem;
            this.dueDate = dueDate;
            this.eventName = eventName;
        }
    }

    public View getView(LayoutInflater inflater, View convertView, ViewGroup container) {
        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = inflater.inflate(C0773R.layout.listview_upcomingevent_item, container, false);
            holder = new ViewHolder((TextView) view.findViewById(C0773R.id.first_event_item), (TextView) view.findViewById(C0773R.id.second_event_item), (TextView) view.findViewById(C0773R.id.eventname), (TextView) view.findViewById(C0773R.id.duedate));
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        holder.firstItem.setText(this.mFirstItem);
        holder.secondItem.setText(this.mSecondItem);
        holder.eventName.setText(this.mEventName);
        holder.dueDate.setText(this.mDueDate);
        return view;
    }

    public int getViewType() {
        return EventRowType.EVENT_ITEM_ROW.ordinal();
    }

    public long getId() {
        return this.mEventId;
    }

    public boolean isEnabled() {
        return true;
    }

    public void setEventId(long eventId) {
        this.mEventId = eventId;
    }

    public void setDueDate(String dueDate) {
        this.mDueDate = dueDate;
    }

    public void setSecondItem(String secondItem) {
        this.mSecondItem = secondItem;
    }

    public void setEventName(String eventName) {
        this.mEventName = eventName;
    }

    public void setFirstItem(String firstItem) {
        this.mFirstItem = firstItem;
    }
}
