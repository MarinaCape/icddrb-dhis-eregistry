package org.icddrb.dhis.android.eregistry.fragments.search;

import java.util.List;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.events.EventRow;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.events.TrackedEntityInstanceDynamicColumnRows;

public class LocalSearchResultFragmentForm {
    private TrackedEntityInstanceDynamicColumnRows columnNames;
    private List<EventRow> eventRowList;
    private String orgUnitId;
    private String programId;

    public List<EventRow> getEventRowList() {
        return this.eventRowList;
    }

    public void setEventRowList(List<EventRow> eventRowList) {
        this.eventRowList = eventRowList;
    }

    public String getOrgUnitId() {
        return this.orgUnitId;
    }

    public void setOrgUnitId(String orgUnitId) {
        this.orgUnitId = orgUnitId;
    }

    public String getProgramId() {
        return this.programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public TrackedEntityInstanceDynamicColumnRows getColumnNames() {
        return this.columnNames;
    }

    public void setColumnNames(TrackedEntityInstanceDynamicColumnRows columnNames) {
        this.columnNames = columnNames;
    }
}
