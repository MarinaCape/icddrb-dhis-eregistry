package org.icddrb.dhis.android.eregistry.fragments.selectprogram;

import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.joda.time.DateTime;

public interface IEnroller {
    void showEnrollmentFragment(TrackedEntityInstance trackedEntityInstance, DateTime dateTime, DateTime dateTime2);
}
