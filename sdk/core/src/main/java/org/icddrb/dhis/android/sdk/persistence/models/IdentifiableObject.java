package org.icddrb.dhis.android.sdk.persistence.models;

import java.util.Comparator;
import org.joda.time.DateTime;

public interface IdentifiableObject {
    public static final Comparator<IdentifiableObject> CREATED_COMPARATOR = new CreatedComparator();
    public static final Comparator<IdentifiableObject> DISPLAY_NAME_COMPARATOR = new NameComparator();

    public static class CreatedComparator implements Comparator<IdentifiableObject> {
        public int compare(IdentifiableObject first, IdentifiableObject second) {
            if (!(first == null || first.getCreated() == null || second == null || second.getCreated() == null)) {
                if (first.getCreated().isAfter(second.getCreated())) {
                    return 1;
                }
                if (second.getCreated().isAfter(first.getCreated())) {
                    return -1;
                }
            }
            return 0;
        }
    }

    public static class NameComparator implements Comparator<IdentifiableObject> {
        public int compare(IdentifiableObject first, IdentifiableObject second) {
            if (first == null || first.getDisplayName() == null || second == null || second.getDisplayName() == null) {
                return 0;
            }
            return first.getDisplayName().compareTo(second.getDisplayName());
        }
    }

    Access getAccess();

    DateTime getCreated();

    String getDisplayName();

    long getId();

    DateTime getLastUpdated();

    String getName();

    String getUId();

    void setAccess(Access access);

    void setCreated(DateTime dateTime);

    void setDisplayName(String str);

    void setId(long j);

    void setLastUpdated(DateTime dateTime);

    void setName(String str);

    void setUId(String str);
}
