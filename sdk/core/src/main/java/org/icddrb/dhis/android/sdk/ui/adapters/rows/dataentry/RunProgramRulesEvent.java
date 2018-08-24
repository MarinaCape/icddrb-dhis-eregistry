package org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry;

import org.icddrb.dhis.android.sdk.persistence.models.BaseValue;
import org.icddrb.dhis.android.sdk.persistence.models.DataValue;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;

public class RunProgramRulesEvent {
    private final String id;

    public RunProgramRulesEvent(BaseValue value) {
        if (value instanceof DataValue) {
            this.id = ((DataValue) value).getDataElement();
        } else if (value instanceof TrackedEntityAttributeValue) {
            this.id = ((TrackedEntityAttributeValue) value).getTrackedEntityAttributeId();
        } else {
            this.id = "";
        }
    }

    public String getId() {
        return this.id;
    }
}
