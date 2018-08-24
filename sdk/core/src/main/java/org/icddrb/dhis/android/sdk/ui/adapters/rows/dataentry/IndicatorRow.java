package org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry;

import org.icddrb.dhis.android.sdk.persistence.models.ProgramIndicator;

public final class IndicatorRow extends NonEditableTextViewRow {
    private static final String EMPTY_FIELD = "";
    private final ProgramIndicator mIndicator;

    public IndicatorRow(ProgramIndicator indicator, String value, String description) {
        super(value);
        this.mIndicator = indicator;
        this.mValue = value;
        this.mDescription = description;
        checkNeedsForDescriptionButton();
    }

    public int getViewType() {
        return DataEntryRowTypes.INDICATOR.ordinal();
    }

    public ProgramIndicator getIndicator() {
        return this.mIndicator;
    }

    public String getName() {
        if (this.mIndicator.getName() != null) {
            return this.mIndicator.getName();
        }
        return "";
    }
}
