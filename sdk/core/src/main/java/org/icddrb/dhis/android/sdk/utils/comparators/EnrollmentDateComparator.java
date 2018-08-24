package org.icddrb.dhis.android.sdk.utils.comparators;

import android.text.TextUtils;
import java.util.Comparator;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.joda.time.DateTime;
import org.joda.time.ReadableInstant;

public class EnrollmentDateComparator implements Comparator<Enrollment> {
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
        if (TextUtils.isEmpty(lhs.getEnrollmentDate()) && TextUtils.isEmpty(rhs.getEnrollmentDate())) {
            return 0;
        }
        if (TextUtils.isEmpty(lhs.getEnrollmentDate())) {
            return 1;
        }
        if (TextUtils.isEmpty(rhs.getEnrollmentDate())) {
            return -1;
        }
        DateTime lhsDate = new DateTime(lhs.getEnrollmentDate());
        DateTime rhsDate = new DateTime(rhs.getEnrollmentDate());
        if (lhsDate == null && rhsDate == null) {
            return 0;
        }
        if (lhsDate == null) {
            return 1;
        }
        if (rhsDate == null) {
            return -1;
        }
        if (lhsDate.isBefore((ReadableInstant) rhsDate)) {
            return 1;
        }
        if (lhsDate.isAfter((ReadableInstant) rhsDate)) {
            return -1;
        }
        return 0;
    }
}
