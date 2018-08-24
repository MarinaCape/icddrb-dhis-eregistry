package org.icddrb.dhis.android.sdk.ui.adapters.rows.events;

import android.widget.Button;
import android.widget.TextView;
import org.icddrb.dhis.android.sdk.persistence.models.Event;

public class OnCompleteEventClick {
    private String action;
    private Button complete;
    private Event event;
    private String label;
    private TextView tv;

    public OnCompleteEventClick(String label, String action, Event event, Button complete, TextView tv) {
        this.label = label;
        this.action = action;
        this.event = event;
        this.complete = complete;
        this.tv = tv;
    }

    public String getAction() {
        return this.action;
    }

    public Button getComplete() {
        return this.complete;
    }

    public TextView getTv() {
        return this.tv;
    }

    public Event getEvent() {
        return this.event;
    }

    public String getLabel() {
        return this.label;
    }
}
