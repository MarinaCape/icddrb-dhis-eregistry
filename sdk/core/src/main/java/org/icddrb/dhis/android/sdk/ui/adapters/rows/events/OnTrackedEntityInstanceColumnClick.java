package org.icddrb.dhis.android.sdk.ui.adapters.rows.events;

public class OnTrackedEntityInstanceColumnClick {
    public static final int FIRST_COLUMN = 1;
    public static final int SECOND_COLUMN = 2;
    public static final int STATUS_COLUMN = 4;
    public static final int THIRD_COLUMN = 3;
    private final int columnClicked;

    public OnTrackedEntityInstanceColumnClick(int columnClicked) {
        this.columnClicked = columnClicked;
    }

    public int getColumnClicked() {
        return this.columnClicked;
    }
}
