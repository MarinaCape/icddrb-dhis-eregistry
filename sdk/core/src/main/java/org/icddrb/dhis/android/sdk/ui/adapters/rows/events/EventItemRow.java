package org.icddrb.dhis.android.sdk.ui.adapters.rows.events;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;
import org.icddrb.dhis.android.sdk.R;
import org.icddrb.dhis.android.sdk.events.OnRowClick.ITEM_STATUS;
import org.icddrb.dhis.android.sdk.events.OnTrackerItemClick;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.utils.Preconditions;

public final class EventItemRow implements EventRow {
    private List<String> columns;
    private String mError;
    private Drawable mErrorDrawable;
    private Event mEvent;
    private String mOffline;
    private Drawable mOfflineDrawable;
    private String mSent;
    private Drawable mSentDrawable;
    private ITEM_STATUS mStatus;

    private static class OnEventInternalClickListener implements OnClickListener, OnLongClickListener {
        private Event event;
        private ITEM_STATUS status;

        private OnEventInternalClickListener() {
        }

        public void setEvent(Event event) {
            this.event = event;
        }

        public void setStatus(ITEM_STATUS status) {
            this.status = status;
        }

        public void onClick(View view) {
            if (view.getId() == R.id.event_container) {
                Dhis2Application.getEventBus().post(new OnTrackerItemClick(this.event, this.status, true));
            } else if (view.getId() == R.id.status_container) {
                Dhis2Application.getEventBus().post(new OnTrackerItemClick(this.event, this.status, false));
            }
        }

        public boolean onLongClick(View view) {
            return false;
        }
    }

    private static class ViewHolder {
        public final LinearLayout columnContainer;
        public final LinearLayout statusContainer;
        public final ImageView statusImageView;
        public final TextView statusTextView;

        private ViewHolder(LinearLayout columnContainer, ImageView statusImageView, TextView statusTextView, LinearLayout statusContainer) {
            this.columnContainer = columnContainer;
            this.statusImageView = statusImageView;
            this.statusTextView = statusTextView;
            this.statusContainer = statusContainer;
        }
    }

    public EventItemRow(Context context) {
        Preconditions.isNull(context, "Context must not be null");
        this.mOfflineDrawable = context.getResources().getDrawable(R.drawable.ic_offline);
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
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        OnEventInternalClickListener listener = new OnEventInternalClickListener();
        listener.setEvent(this.mEvent);
        listener.setStatus(this.mStatus);
        view.setOnClickListener(listener);
        view.setOnLongClickListener(listener);
        holder.statusContainer.setOnClickListener(listener);
        for (String column : this.columns) {
            ((TextView) inflater.inflate(R.layout.item_column, holder.columnContainer, false).findViewById(R.id.column_name)).setText(column);
        }
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
        return view;
    }

    public int getViewType() {
        return EventRowType.EVENT_ITEM_ROW.ordinal();
    }

    public long getId() {
        if (this.mEvent != null) {
            return this.mEvent.getLocalId();
        }
        return 0;
    }

    public boolean isEnabled() {
        return true;
    }

    public void setEvent(Event event) {
        this.mEvent = event;
    }

    public Event getmEvent() {
        return this.mEvent;
    }

    public void setStatus(ITEM_STATUS status) {
        this.mStatus = status;
    }

    public ITEM_STATUS getStatus() {
        return this.mStatus;
    }
}
