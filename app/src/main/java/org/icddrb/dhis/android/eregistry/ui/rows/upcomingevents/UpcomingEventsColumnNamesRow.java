package org.icddrb.dhis.android.eregistry.ui.rows.upcomingevents;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.icddrb.dhis.android.eregistry.C0773R;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.events.EventRow;

public class UpcomingEventsColumnNamesRow implements EventRow {
    private String mFirstItem;
    private String mSecondItem;
    private String mTitle;

    private static class ViewHolder {
        public final TextView firstItem;
        public final TextView secondItem;
        public final TextView title;

        private ViewHolder(TextView title, TextView firstItem, TextView secondItem) {
            this.title = title;
            this.firstItem = firstItem;
            this.secondItem = secondItem;
        }
    }

    public View getView(LayoutInflater inflater, View convertView, ViewGroup container) {
        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = inflater.inflate(C0773R.layout.listview_upcomingevents_column_names_item, container, false);
            holder = new ViewHolder((TextView) view.findViewById(C0773R.id.title_column_name), (TextView) view.findViewById(C0773R.id.first_column_name), (TextView) view.findViewById(C0773R.id.second_column_name));
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        holder.title.setText(this.mTitle);
        holder.firstItem.setText(this.mFirstItem);
        holder.secondItem.setText(this.mSecondItem);
        return view;
    }

    public int getViewType() {
        return EventRowType.COLUMN_NAMES_ROW.ordinal();
    }

    public long getId() {
        return -1;
    }

    public boolean isEnabled() {
        return false;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setSecondItem(String secondItem) {
        this.mSecondItem = secondItem;
    }

    public void setFirstItem(String firstItem) {
        this.mFirstItem = firstItem;
    }
}
