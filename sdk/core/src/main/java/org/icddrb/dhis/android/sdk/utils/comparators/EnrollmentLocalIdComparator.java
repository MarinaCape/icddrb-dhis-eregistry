package org.icddrb.dhis.android.sdk.utils.comparators;

import java.util.Comparator;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;

public class EnrollmentLocalIdComparator implements Comparator<Enrollment> {
    public int compare(Enrollment lhs, Enrollment rhs) {
        if (lhs == null && rhs == null) {
            return 0;
        }
        if (lhs == null) {
            return 1;
        }
        if (rhs == null) {
            return -1;
        }
        return (int) Math.ceil((double) (rhs.getLocalId() - lhs.getLocalId()));
    }
}
