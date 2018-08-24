package org.icddrb.dhis.android.eregistry.fragments.programoverview.registerrelationshipdialogfragment;

import java.util.List;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.events.EventRow;

class RegisterRelationshipDialogFragmentForm {
    Enrollment enrollment;
    private String queryString;
    List<EventRow> rows;
    TrackedEntityInstance trackedEntityInstance;

    RegisterRelationshipDialogFragmentForm() {
    }

    public TrackedEntityInstance getTrackedEntityInstance() {
        return this.trackedEntityInstance;
    }

    public void setTrackedEntityInstance(TrackedEntityInstance trackedEntityInstance) {
        this.trackedEntityInstance = trackedEntityInstance;
    }

    public List<EventRow> getRows() {
        return this.rows;
    }

    public void setRows(List<EventRow> rows) {
        this.rows = rows;
    }

    public String getQueryString() {
        return this.queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }
}
