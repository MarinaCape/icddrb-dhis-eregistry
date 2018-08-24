package org.icddrb.dhis.android.sdk.ui.adapters.rows.events;

import android.view.View;
import android.view.View.OnClickListener;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.Row;

public class OnDetailedInfoButtonClick implements OnClickListener {
    final Row row;

    public OnDetailedInfoButtonClick(Row row) {
        this.row = row;
    }

    public Row getRow() {
        return this.row;
    }

    public void onClick(View view) {
        Dhis2Application.getEventBus().post(new OnDetailedInfoButtonClick(this.row));
    }
}
