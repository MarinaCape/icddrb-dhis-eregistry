package org.icddrb.dhis.android.eregistry.ui.rows.programoverview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import org.icddrb.dhis.android.eregistry.C0773R;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.events.OnRowClick.ITEM_STATUS;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.OrganisationUnit;
import org.icddrb.dhis.android.sdk.utils.Utils;
import org.icddrb.dhis.android.sdk.utils.support.DateUtils;
import org.joda.time.LocalDate;

public class ProgramStageEventRow implements ProgramStageRow {
    private static final String TAG = ProgramStageEventRow.class.getSimpleName();
    private final Event event;
    private boolean hasFailed = false;
    private EventViewHolder holder;
    private boolean isSynchronized = false;
    private ProgramStageLabelRow labelRow;
    private String message;
    private int status;

    private static class EventViewHolder {
        public final TextView date;
        public final OnProgramStageEventInternalClickListener listener;
        public final TextView orgUnit;
        public final ImageButton statusButton;

        private EventViewHolder(TextView orgUnit, TextView date, ImageButton statusButton, OnProgramStageEventInternalClickListener listener) {
            this.orgUnit = orgUnit;
            this.date = date;
            this.statusButton = statusButton;
            this.listener = listener;
        }
    }

    private static class OnProgramStageEventInternalClickListener implements OnClickListener, OnLongClickListener {
        private Event event;
        private String message;
        private ITEM_STATUS status;
        private ImageButton statusButton;

        private OnProgramStageEventInternalClickListener() {
        }

        public void setEvent(Event event) {
            this.event = event;
        }

        public void setStatusButton(ImageButton statusButton) {
            this.statusButton = statusButton;
        }

        public void setStatus(ITEM_STATUS status) {
            this.status = status;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void onClick(View view) {
            if (view.getId() == C0773R.id.eventbackground) {
                Dhis2Application.getEventBus().post(new OnProgramStageEventClick(this.event, this.statusButton, false, "", this.status, false, view));
            } else if (view.getId() == C0773R.id.statusButton) {
                Dhis2Application.getEventBus().post(new OnProgramStageEventClick(this.event, this.statusButton, true, this.message, this.status, false, view));
            }
        }

        public boolean onLongClick(View view) {
            Dhis2Application.getEventBus().post(new OnProgramStageEventClick(this.event, this.statusButton, false, "", this.status, true, view));
            return true;
        }
    }

    public ProgramStageEventRow(Event event) {
        this.event = event;
    }

    public View getView(LayoutInflater inflater, View convertView, ViewGroup container) {
        View view;
        ImageButton statusButton = null;
        if (convertView == null || !(convertView.getTag() instanceof EventViewHolder)) {
            View root = inflater.inflate(C0773R.layout.eventlayout, container, false);
            statusButton = (ImageButton) root.findViewById(C0773R.id.statusButton);
            this.holder = new EventViewHolder((TextView) root.findViewById(C0773R.id.organisationunit), (TextView) root.findViewById(C0773R.id.date), statusButton, new OnProgramStageEventInternalClickListener());
            root.findViewById(C0773R.id.eventbackground).setOnClickListener(this.holder.listener);
            root.findViewById(C0773R.id.eventbackground).setOnLongClickListener(this.holder.listener);
            root.setTag(this.holder);
            view = root;
        } else {
            view = convertView;
            this.holder = (EventViewHolder) view.getTag();
        }
        if (this.holder.statusButton != null) {
            if (hasFailed()) {
                this.holder.statusButton.setEnabled(true);
                this.holder.statusButton.setVisibility(0);
                this.holder.statusButton.setBackgroundResource(C0773R.drawable.ic_event_error);
                this.holder.statusButton.setTag(Integer.valueOf(C0773R.drawable.ic_event_error));
                this.holder.listener.setStatusButton(statusButton);
                this.holder.listener.setStatus(ITEM_STATUS.ERROR);
                this.holder.statusButton.setOnClickListener(this.holder.listener);
            } else if (!isSynchronized()) {
                this.holder.statusButton.setEnabled(true);
                this.holder.statusButton.setVisibility(0);
                this.holder.statusButton.setBackgroundResource(C0773R.drawable.ic_legacy_offline);
                this.holder.statusButton.setTag(Integer.valueOf(C0773R.drawable.ic_legacy_offline));
                this.holder.listener.setStatusButton(statusButton);
                this.holder.listener.setStatus(ITEM_STATUS.OFFLINE);
                this.holder.statusButton.setOnClickListener(this.holder.listener);
            } else if (isSynchronized()) {
                this.holder.statusButton.setEnabled(true);
                this.holder.statusButton.setVisibility(0);
                this.holder.statusButton.setBackgroundResource(C0773R.drawable.ic_from_server);
                this.holder.statusButton.setTag(Integer.valueOf(C0773R.drawable.ic_from_server));
                this.holder.listener.setStatusButton(statusButton);
                this.holder.listener.setStatus(ITEM_STATUS.SENT);
                this.holder.statusButton.setOnClickListener(this.holder.listener);
            }
        }
        this.holder.listener.setEvent(getEvent());
        this.holder.listener.setMessage(getMessage());
        if (this.event.getOrganisationUnitId() != null) {
            OrganisationUnit organisationUnit = MetaDataController.getOrganisationUnit(this.event.getOrganisationUnitId());
            if (organisationUnit != null) {
                this.holder.orgUnit.setText(organisationUnit.getLabel());
            }
        } else {
            this.holder.orgUnit.setText("");
        }
        String date = "";
        if (this.event.getEventDate() != null) {
            date = this.event.getEventDate();
        } else {
            date = this.event.getDueDate();
        }
        if (date != null) {
            date = Utils.removeTimeFromDateString(date);
        } else {
            date = "";
        }
        this.holder.date.setText(date);
        LocalDate eventDate = null;
        if (this.event.getEventDate() != null) {
            eventDate = new LocalDate(DateUtils.parseDate(this.event.getEventDate()));
        }
        view.findViewById(C0773R.id.eventbackground).setBackgroundColor(inflater.getContext().getResources().getColor(getStatusColor(eventDate, new LocalDate(DateUtils.parseDate(this.event.getDueDate())), new LocalDate(DateUtils.parseDate(DateUtils.getMediumDateString())))));
        return view;
    }

    private int getStatusColor(LocalDate eventDate, LocalDate dueDate, LocalDate now) {
        if (this.event.getStatus().equals("COMPLETED")) {
            return C0773R.color.stage_completed;
        }
        if (this.event.getStatus().equals(Event.STATUS_SKIPPED)) {
            return C0773R.color.stage_skipped;
        }
        if (eventDate != null) {
            return C0773R.color.stage_executed;
        }
        if (dueDate.isBefore(now)) {
            return C0773R.color.stage_overdue;
        }
        return C0773R.color.stage_ontime;
    }

    public Event getEvent() {
        return this.event;
    }

    public ProgramStageLabelRow getLabelRow() {
        return this.labelRow;
    }

    public void setLabelRow(ProgramStageLabelRow labelRow) {
        this.labelRow = labelRow;
    }

    public boolean hasFailed() {
        return this.hasFailed;
    }

    public void setHasFailed(boolean hasFailed) {
        this.hasFailed = hasFailed;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSynchronized(boolean isSynchronized) {
        this.isSynchronized = isSynchronized;
    }

    public boolean isSynchronized() {
        return this.isSynchronized;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message;
    }
}
