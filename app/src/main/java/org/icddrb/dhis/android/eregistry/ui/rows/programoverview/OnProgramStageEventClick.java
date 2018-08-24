package org.icddrb.dhis.android.eregistry.ui.rows.programoverview;

import android.view.View;
import android.widget.ImageButton;
import org.icddrb.dhis.android.sdk.events.OnRowClick;
import org.icddrb.dhis.android.sdk.events.OnRowClick.ITEM_STATUS;
import org.icddrb.dhis.android.sdk.persistence.models.Event;

public class OnProgramStageEventClick extends OnRowClick<Event> {
    private final String errorMessage;
    private final Event event;
    private final ImageButton hasFailedButton;
    private final boolean hasPressedFailedButton;
    private final boolean isLongPressed;
    private final View view;

    public OnProgramStageEventClick(Event event, ImageButton hasFailedButton, boolean hasPressedFailedButton, String errorMessage, ITEM_STATUS status, boolean isLongPressed, View view) {
        super(status, event);
        this.event = event;
        this.hasFailedButton = hasFailedButton;
        this.hasPressedFailedButton = hasPressedFailedButton;
        this.errorMessage = errorMessage;
        this.isLongPressed = isLongPressed;
        this.view = view;
    }

    public boolean isHasPressedFailedButton() {
        return this.hasPressedFailedButton;
    }

    public ImageButton getHasFailedButton() {
        return this.hasFailedButton;
    }

    public Event getEvent() {
        return this.event;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public boolean isLongPressed() {
        return this.isLongPressed;
    }

    public View getView() {
        return this.view;
    }
}
