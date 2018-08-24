package org.icddrb.dhis.android.eregistry.ui.rows.programoverview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.icddrb.dhis.android.eregistry.C0773R;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.events.TrackedEntityInstanceItemRow;

public class SearchRelativeTrackedEntityInstanceItemRow extends TrackedEntityInstanceItemRow {
    private String mFourthItem;

    private static class ViewHolder {
        public final TextView firstItem;
        public final TextView fourthItem;
        public final TextView secondItem;
        public final TextView thirdItem;

        private ViewHolder(TextView firstItem, TextView secondItem, TextView thirdItem, TextView fourthItem) {
            this.firstItem = firstItem;
            this.secondItem = secondItem;
            this.thirdItem = thirdItem;
            this.fourthItem = fourthItem;
        }
    }

    public SearchRelativeTrackedEntityInstanceItemRow(Context context) {
        super(context);
    }

    public View getView(LayoutInflater inflater, View convertView, ViewGroup container) {
        View view;
        ViewHolder holder;
        boolean z;
        boolean z2 = false;
        if (convertView == null) {
            view = inflater.inflate(C0773R.layout.listview_trackedentityinstance_item, container, false);
            holder = new ViewHolder((TextView) view.findViewById(C0773R.id.first_event_item), (TextView) view.findViewById(C0773R.id.second_event_item), (TextView) view.findViewById(C0773R.id.third_event_item), (TextView) view.findViewById(C0773R.id.status_text_view));
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        if (this.columns.size() >= 1 && this.columns.get(0) != null) {
            holder.firstItem.setText((CharSequence) this.columns.get(0));
        }
        if (this.columns.size() >= 2) {
            z = true;
        } else {
            z = false;
        }
        if (this.columns.get(1) != null) {
            z2 = true;
        }
        if (z == z2) {
            holder.secondItem.setText((CharSequence) this.columns.get(1));
        }
        if (this.columns.size() >= 3 && this.columns.get(2) != null) {
            holder.thirdItem.setText((CharSequence) this.columns.get(2));
        }
        if (this.columns.size() >= 4 && this.columns.get(3) != null) {
            holder.fourthItem.setText((CharSequence) this.columns.get(3));
        }
        return view;
    }

    public String getmFourthItem() {
        return this.mFourthItem;
    }

    public void setFourthItem(String mFourthItem) {
        this.mFourthItem = mFourthItem;
    }
}
