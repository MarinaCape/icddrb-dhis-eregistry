package org.icddrb.dhis.android.sdk.ui.adapters.rows.events;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;
import org.icddrb.dhis.android.sdk.C0845R;

public class ColumnNamesRow implements EventRow {
    private List<String> columns;
    private String mFirstItem;
    private String mTitle;

    private static class ViewHolder {
        public final TextView firstItem;
        public final TextView trackedEntityTitle;

        private ViewHolder(TextView trackedEntityTitle, TextView firstItem) {
            this.trackedEntityTitle = trackedEntityTitle;
            this.firstItem = firstItem;
        }
    }

    public View getView(LayoutInflater inflater, View convertView, ViewGroup container) {
        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = inflater.inflate(C0845R.layout.listview_column_names_item, container, false);
            LinearLayout rowContainer = (LinearLayout) view.findViewById(C0845R.id.dynamic_column_container);
            for (String column : this.columns) {
                ((TextView) inflater.inflate(C0845R.layout.item_column, rowContainer, false).findViewById(C0845R.id.column_name)).setText(column);
            }
            holder = new ViewHolder((TextView) view.findViewById(C0845R.id.tracked_entity_title), (TextView) view.findViewById(C0845R.id.column_name));
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        holder.trackedEntityTitle.setText(this.mTitle);
        holder.firstItem.setText(this.mFirstItem);
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

    public List<String> getColumns() {
        return this.columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public void setFirstItem(String firstItem) {
        this.mFirstItem = firstItem;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }
}
