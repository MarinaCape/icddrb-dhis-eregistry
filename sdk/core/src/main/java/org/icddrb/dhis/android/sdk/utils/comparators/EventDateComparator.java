package org.icddrb.dhis.android.sdk.utils.comparators;

import android.text.TextUtils;
import java.util.Comparator;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.joda.time.DateTime;
import org.joda.time.ReadableInstant;

public class EventDateComparator implements Comparator<Event> {
    public int compare(Event lhs, Event rhs) {
        if (lhs == null && rhs == null) {
            return 0;
        }
        if (lhs == null) {
            return -1;
        }
        if (rhs == null) {
            return 1;
        }
        if (TextUtils.isEmpty(lhs.getEventDate()) && TextUtils.isEmpty(rhs.getEventDate())) {
            return 0;
        }
        if (TextUtils.isEmpty(lhs.getEventDate())) {
            return -1;
        }
        if (TextUtils.isEmpty(rhs.getEventDate())) {
            return 1;
        }
        DateTime lhsDate = new DateTime(lhs.getEventDate());
        DateTime rhsDate = new DateTime(rhs.getEventDate());
        if (lhsDate == null && rhsDate == null) {
            return 0;
        }
        if (lhsDate == null) {
            return -1;
        }
        if (rhsDate == null) {
            return 1;
        }
        if (lhsDate.isBefore((ReadableInstant) rhsDate)) {
            return -1;
        }
        if (lhsDate.isAfter((ReadableInstant) rhsDate)) {
            return 1;
        }
        return 0;
    }
}
