package org.icddrb.dhis.android.sdk.ui.adapters.rows.events;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.icddrb.dhis.android.sdk.R;
import org.icddrb.dhis.android.sdk.events.OnRowClick.ITEM_STATUS;
import org.icddrb.dhis.android.sdk.events.OnTrackerItemClick;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.utils.Preconditions;

public class TrackedEntityInstanceItemRow implements EventRow, Comparator<TrackedEntityInstanceItemRow>, OnCreateContextMenuListener {
    protected List<String> columns;
    private String mError;
    private Drawable mErrorDrawable;
    private String mOffline;
    private Drawable mOfflineDrawable;
    private String mSent;
    private Drawable mSentDrawable;
    private ITEM_STATUS mStatus;
    protected TrackedEntityInstance mTrackedEntityInstance;

    private static class OnTrackedEntityInstanceInternalClickListener implements OnClickListener, OnLongClickListener {
        private ITEM_STATUS status;
        private TrackedEntityInstance trackedEntityInstance;

        private OnTrackedEntityInstanceInternalClickListener() {
        }

        public void setTrackedEntityInstance(TrackedEntityInstance trackedEntityInstance) {
            this.trackedEntityInstance = trackedEntityInstance;
        }

        public void setStatus(ITEM_STATUS status) {
            this.status = status;
        }

        public void onClick(View view) {
            if (view.getId() == R.id.event_container) {
                Dhis2Application.getEventBus().post(new OnTrackerItemClick(this.trackedEntityInstance, this.status, true));
            } else if (view.getId() == R.id.status_container) {
                Dhis2Application.getEventBus().post(new OnTrackerItemClick(this.trackedEntityInstance, this.status, false));
            }
        }

        public boolean onLongClick(View view) {
            return false;
        }
    }

    private static class ViewHolder {
        public final LinearLayout container;
        public final LinearLayout statusContainer;
        public final ImageView statusImageView;
        public final TextView statusTextView;

        public ViewHolder(LinearLayout container, ImageView statusImageView, TextView statusTextView, LinearLayout statusContainer) {
            this.container = container;
            this.statusImageView = statusImageView;
            this.statusTextView = statusTextView;
            this.statusContainer = statusContainer;
        }
    }

    public TrackedEntityInstanceItemRow(Context context) {
        Preconditions.isNull(context, "Context must not be null");
        this.mOfflineDrawable = context.getResources().getDrawable(R.drawable.ic_legacy_offline);
        this.mErrorDrawable = context.getResources().getDrawable(R.drawable.ic_event_error);
        this.mSentDrawable = context.getResources().getDrawable(R.drawable.ic_from_server);
        this.mSent = context.getResources().getString(R.string.event_sent);
        this.mError = context.getResources().getString(R.string.event_error);
        this.mOffline = context.getResources().getString(R.string.event_offline);
    }

    public View getView(LayoutInflater inflater, View convertView, ViewGroup container) {
        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = inflater.inflate(R.layout.listview_event_item, container, false);
            holder = new ViewHolder((LinearLayout) view.findViewById(R.id.dynamic_column_container), (ImageView) view.findViewById(R.id.status_image_view), (TextView) view.findViewById(R.id.status_text_view), (LinearLayout) view.findViewById(R.id.status_container));
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        view.setTag(holder);
        OnTrackedEntityInstanceInternalClickListener onTrackedEntityInstanceInternalClickListener = new OnTrackedEntityInstanceInternalClickListener();
        onTrackedEntityInstanceInternalClickListener.setTrackedEntityInstance(this.mTrackedEntityInstance);
        onTrackedEntityInstanceInternalClickListener.setStatus(this.mStatus);
        view.setOnClickListener(onTrackedEntityInstanceInternalClickListener);
        view.setOnLongClickListener(onTrackedEntityInstanceInternalClickListener);
        holder.statusContainer.setOnClickListener(onTrackedEntityInstanceInternalClickListener);
        LayoutParams params = new LayoutParams(-1, -1, 0.75f / ((float) this.columns.size()));
        holder.container.removeAllViews();
        for (String column : this.columns) {
            LinearLayout columnView = (LinearLayout) inflater.inflate(R.layout.event_item_column, holder.container, false);
            ((TextView) columnView.findViewById(R.id.column_name)).setText(column);
            holder.container.addView(columnView, params);
            holder.container.addView(inflater.inflate(R.layout.space_event_column, holder.container, false));
        }
        if (this.mStatus != null) {
            switch (this.mStatus) {
                case OFFLINE:
                    holder.statusImageView.setImageDrawable(this.mOfflineDrawable);
                    holder.statusTextView.setText(this.mOffline);
                    break;
                case ERROR:
                    holder.statusImageView.setImageDrawable(this.mErrorDrawable);
                    holder.statusTextView.setText(this.mError);
                    break;
                case SENT:
                    holder.statusImageView.setImageDrawable(this.mSentDrawable);
                    holder.statusTextView.setText(this.mSent);
                    break;
            }
        }
        return view;
    }

    public int getViewType() {
        return EventRowType.EVENT_ITEM_ROW.ordinal();
    }

    public long getId() {
        if (this.mTrackedEntityInstance != null) {
            return this.mTrackedEntityInstance.getLocalId();
        }
        return 0;
    }

    public TrackedEntityInstance getTrackedEntityInstance() {
        return this.mTrackedEntityInstance;
    }

    public boolean isEnabled() {
        return true;
    }

    public void setTrackedEntityInstance(TrackedEntityInstance trackedEntityInstance) {
        this.mTrackedEntityInstance = trackedEntityInstance;
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

    public void setStatus(ITEM_STATUS status) {
        this.mStatus = status;
    }

    public ITEM_STATUS getStatus() {
        return this.mStatus;
    }

    public int compare(TrackedEntityInstanceItemRow lhs, TrackedEntityInstanceItemRow rhs) {
        return 0;
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        Log.d("hello", "oncreatecontext");
    }
}
