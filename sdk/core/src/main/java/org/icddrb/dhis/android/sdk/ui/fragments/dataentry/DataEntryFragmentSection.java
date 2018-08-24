package org.icddrb.dhis.android.sdk.ui.fragments.dataentry;

import java.util.List;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.Row;

public final class DataEntryFragmentSection {
    private boolean hidden = false;
    private final String id;
    private final String label;
    private final List<Row> rows;

    public DataEntryFragmentSection(String label, String id, List<Row> rows) {
        this.label = label;
        this.id = id;
        this.rows = rows;
    }

    public String getLabel() {
        return this.label;
    }

    public List<Row> getRows() {
        return this.rows;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public String getId() {
        return this.id;
    }
}
