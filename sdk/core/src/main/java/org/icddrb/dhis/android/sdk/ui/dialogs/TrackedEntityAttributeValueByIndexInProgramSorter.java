package org.icddrb.dhis.android.sdk.ui.dialogs;

import java.util.Comparator;
import java.util.Map;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramTrackedEntityAttribute;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;

class TrackedEntityAttributeValueByIndexInProgramSorter implements Comparator<TrackedEntityAttributeValue> {
    private final Map<String, ProgramTrackedEntityAttribute> programTrackedEntityAttributeMap;

    TrackedEntityAttributeValueByIndexInProgramSorter(Map<String, ProgramTrackedEntityAttribute> programTrackedEntityAttributeMap) {
        this.programTrackedEntityAttributeMap = programTrackedEntityAttributeMap;
    }

    public int compare(TrackedEntityAttributeValue lhs, TrackedEntityAttributeValue rhs) {
        if (this.programTrackedEntityAttributeMap == null) {
            return 0;
        }
        ProgramTrackedEntityAttribute lhsProgramTrackedEntityAttribute = (ProgramTrackedEntityAttribute) this.programTrackedEntityAttributeMap.get(lhs.getTrackedEntityAttributeId());
        ProgramTrackedEntityAttribute rhsProgramTrackedEntityAttribute = (ProgramTrackedEntityAttribute) this.programTrackedEntityAttributeMap.get(rhs.getTrackedEntityAttributeId());
        if (lhsProgramTrackedEntityAttribute == null && rhsProgramTrackedEntityAttribute == null) {
            return 0;
        }
        if (lhsProgramTrackedEntityAttribute == null) {
            return -1;
        }
        if (rhsProgramTrackedEntityAttribute == null) {
            return 1;
        }
        if (lhsProgramTrackedEntityAttribute.getSortOrder() > rhsProgramTrackedEntityAttribute.getSortOrder()) {
            return 1;
        }
        if (rhsProgramTrackedEntityAttribute.getSortOrder() > lhsProgramTrackedEntityAttribute.getSortOrder()) {
            return -1;
        }
        return 0;
    }
}
