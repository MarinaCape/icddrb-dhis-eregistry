package org.icddrb.dhis.android.sdk.ui.adapters.rows.events;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import org.icddrb.dhis.android.sdk.C0845R;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;

public class TrackedEntityInstanceDynamicColumnRows implements EventRow {
    private List<String> columns;
    private String mTitle;
    private String mTrackedEntity;
    private View view;

    private static class ViewHolder {
        public final LinearLayout columnContainer;
        public final TextView contentItem;
        public final TextView statusItem;
        public final TextView trackedEntityTitle;

        private static class OnInternalColumnRowClickListener implements OnClickListener {
            private OnInternalColumnRowClickListener() {
            }

            public void onClick(View view) {
                if (view.getId() == C0845R.id.column_name) {
                    Dhis2Application.getEventBus().post(new OnTrackedEntityInstanceColumnClick(1));
                } else if (view.getId() == C0845R.id.status_column) {
                    Dhis2Application.getEventBus().post(new OnTrackedEntityInstanceColumnClick(4));
                }
            }
        }

        private ViewHolder(TextView trackedEntityTitle, TextView contentItem, TextView statusItem, LinearLayout columnContainer) {
            this.trackedEntityTitle = trackedEntityTitle;
            this.contentItem = contentItem;
            this.statusItem = statusItem;
            this.columnContainer = columnContainer;
        }
    }

    public View getView(LayoutInflater inflater, View convertView, ViewGroup container) {
        ViewHolder holder;
        if (convertView == null) {
            this.view = inflater.inflate(C0845R.layout.listview_column_names_item, container, false);
            holder = new ViewHolder((TextView) this.view.findViewById(C0845R.id.tracked_entity_title), (TextView) this.view.findViewById(C0845R.id.column_name), (TextView) this.view.findViewById(C0845R.id.status_column), (LinearLayout) this.view.findViewById(C0845R.id.dynamic_column_container));
            for (String column : this.columns) {
                ((TextView) inflater.inflate(C0845R.layout.item_column, (LinearLayout) this.view.findViewById(C0845R.id.dynamic_column_container), false).findViewById(C0845R.id.column_name)).setText(column);
            }
            this.view.setTag(holder);
        } else {
            this.view = convertView;
            holder = (ViewHolder) this.view.getTag();
        }
        OnInternalColumnRowClickListener onInternalColumnRowClickListener = new OnInternalColumnRowClickListener();
        LayoutParams params = new LayoutParams(-1, -1, 0.75f / ((float) this.columns.size()));
        params.gravity = 16;
        holder.columnContainer.removeAllViews();
        for (String column2 : this.columns) {
            View columnView = inflater.inflate(C0845R.layout.item_column, holder.columnContainer, false);
            ((TextView) columnView.findViewById(C0845R.id.column_name)).setText(column2);
            holder.columnContainer.addView(columnView, params);
            holder.columnContainer.addView(inflater.inflate(C0845R.layout.space_column, holder.columnContainer, false));
        }
        holder.trackedEntityTitle.setText(this.mTitle);
        holder.statusItem.setOnClickListener(onInternalColumnRowClickListener);
        return this.view;
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

    public void addColumn(String column) {
        if (this.columns == null) {
            this.columns = new ArrayList();
        }
        this.columns.add(column);
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getTrackedEntity() {
        return this.mTrackedEntity;
    }

    public void setTrackedEntity(String mTrackedEntity) {
        this.mTrackedEntity = mTrackedEntity;
    }

    public View getView() {
        return this.view;
    }
}
