package org.icddrb.dhis.android.sdk.ui.fragments.selectprogram;

import java.util.List;
import org.icddrb.dhis.android.sdk.persistence.models.OrganisationUnit;
import org.icddrb.dhis.android.sdk.persistence.models.Program;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.events.EventRow;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.events.TrackedEntityInstanceDynamicColumnRows;

public class SelectProgramFragmentForm {
    private TrackedEntityInstanceDynamicColumnRows columnNames;
    private List<EventRow> eventRowList;
    private OrganisationUnit orgUnit;
    private Program program;

    public List<EventRow> getEventRowList() {
        return this.eventRowList;
    }

    public void setEventRowList(List<EventRow> eventRowList) {
        this.eventRowList = eventRowList;
    }

    public OrganisationUnit getOrgUnit() {
        return this.orgUnit;
    }

    public void setOrgUnit(OrganisationUnit orgUnit) {
        this.orgUnit = orgUnit;
    }

    public Program getProgram() {
        return this.program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public TrackedEntityInstanceDynamicColumnRows getColumnNames() {
        return this.columnNames;
    }

    public void setColumnNames(TrackedEntityInstanceDynamicColumnRows columnNames) {
        this.columnNames = columnNames;
    }
}
