package org.icddrb.dhis.android.sdk.ui.fragments.dataentry;

import org.icddrb.dhis.android.sdk.persistence.models.BaseValue;
import org.icddrb.dhis.android.sdk.persistence.models.DataValue;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.Row;

public final class RowValueChangedEvent {
    public static final String DATAVALUE = "datavalue";
    public static final String TRACKEDENTITYATTRIBUTEVALUE = "trackedentityattributevalue";
    private BaseValue baseValue;
    private String id;
    private Row row;
    private String rowType;
    private String type;

    public RowValueChangedEvent(BaseValue baseValue, String rowType) {
        this.rowType = rowType;
        if (baseValue instanceof DataValue) {
            this.type = DATAVALUE;
            this.id = ((DataValue) baseValue).getDataElement();
        } else if (baseValue instanceof TrackedEntityAttributeValue) {
            this.type = TRACKEDENTITYATTRIBUTEVALUE;
            this.id = ((TrackedEntityAttributeValue) baseValue).getTrackedEntityAttributeId();
        }
    }

    public String getType() {
        return this.type;
    }

    public String getId() {
        return this.id;
    }

    public boolean isDataValue() {
        return this.type.equals(DATAVALUE);
    }

    public BaseValue getBaseValue() {
        return this.baseValue;
    }

    public String getRowType() {
        return this.rowType;
    }

    public Row getRow() {
        return this.row;
    }

    public void setRow(Row row) {
        this.row = row;
    }
}
