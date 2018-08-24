package org.icddrb.dhis.android.sdk.utils.comparators;

import java.util.Comparator;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramRule;

public class ProgramRulePriorityComparator implements Comparator<ProgramRule> {
    public int compare(ProgramRule lhs, ProgramRule rhs) {
        if (lhs == null && rhs == null) {
            return 0;
        }
        if (lhs == null) {
            return 1;
        }
        if (rhs == null) {
            return -1;
        }
        if (lhs.getPriority() == null && rhs.getPriority() == null) {
            return 0;
        }
        if (lhs.getPriority() == null) {
            return 1;
        }
        if (rhs.getPriority() == null) {
            return -1;
        }
        if (lhs.getPriority().intValue() < rhs.getPriority().intValue()) {
            return -1;
        }
        if (lhs.getPriority().equals(rhs.getPriority())) {
            return 0;
        }
        return 1;
    }
}
