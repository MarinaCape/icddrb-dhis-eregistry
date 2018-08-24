package org.icddrb.dhis.android.eregistry.ui.rows.programoverview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import org.icddrb.dhis.android.eregistry.C0773R;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramStage;

public class ProgramStageLabelRow implements ProgramStageRow {
    private List<ProgramStageEventRow> eventRows = new ArrayList();
    private boolean hasCompletedEvents = false;
    private boolean hasFailed = false;
    private ProgramStageViewHolder holder;
    private OnClickListener listener;
    private final ProgramStage programStage;

    private static class ProgramStageViewHolder {
        public final Button newEventButton;
        public final TextView programStageName;

        private ProgramStageViewHolder(TextView programStageName, Button newEventButton) {
            this.programStageName = programStageName;
            this.newEventButton = newEventButton;
        }
    }

    public ProgramStageLabelRow(ProgramStage programStage) {
        this.programStage = programStage;
    }

    public View getView(LayoutInflater inflater, View convertView, ViewGroup container) {
        View view;
        if (convertView == null || !(convertView.getTag() instanceof ProgramStageViewHolder)) {
            View root = inflater.inflate(C0773R.layout.programstagelayout, container, false);
            TextView programStageName = (TextView) root.findViewById(C0773R.id.programstagename);
            Button newEventButton = (Button) root.findViewById(C0773R.id.neweventbutton);
            newEventButton.setVisibility(4);
            newEventButton.setEnabled(false);
            newEventButton.setTag(this.programStage);
            this.holder = new ProgramStageViewHolder(programStageName, newEventButton);
            root.setTag(this.holder);
            view = root;
        } else {
            view = convertView;
            this.holder = (ProgramStageViewHolder) view.getTag();
        }
        if (this.holder.newEventButton == null || this.listener == null) {
            this.holder.newEventButton.setOnClickListener(null);
            this.holder.newEventButton.setVisibility(4);
            this.holder.newEventButton.setEnabled(false);
        } else {
            this.holder.newEventButton.setOnClickListener(this.listener);
            this.holder.newEventButton.setVisibility(0);
            this.holder.newEventButton.setEnabled(true);
            this.holder.newEventButton.setTag(this.programStage);
        }
        this.holder.programStageName.setText(this.programStage.getName());
        return view;
    }

    public boolean hasFailed() {
        return this.hasFailed;
    }

    public void setHasFailed(boolean hasFailed) {
        this.hasFailed = hasFailed;
    }

    public void setSynchronized(boolean isSynchronized) {
    }

    public boolean isSynchronized() {
        return false;
    }

    public void setButtonListener(OnClickListener listener) {
        this.listener = listener;
    }

    public ProgramStage getProgramStage() {
        return this.programStage;
    }

    public boolean getHasCompletedEvents() {
        return this.hasCompletedEvents;
    }

    public void setHasCompletedEvents(boolean hasCompletedEvents) {
        this.hasCompletedEvents = hasCompletedEvents;
    }

    public List<ProgramStageEventRow> getEventRows() {
        return this.eventRows;
    }

    public void setEventRows(List<ProgramStageEventRow> eventRows) {
        this.eventRows = eventRows;
    }
}
