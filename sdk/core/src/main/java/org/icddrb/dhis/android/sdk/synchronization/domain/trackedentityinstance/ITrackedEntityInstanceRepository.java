package org.icddrb.dhis.android.sdk.synchronization.domain.trackedentityinstance;

import java.util.List;
import org.icddrb.dhis.android.sdk.persistence.models.ImportSummary;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;

public interface ITrackedEntityInstanceRepository {
    List<TrackedEntityInstance> getAllLocalTeis();

    TrackedEntityInstance getTrackedEntityInstance(String str);

    void save(TrackedEntityInstance trackedEntityInstance);

    List<ImportSummary> sync(List<TrackedEntityInstance> list);

    ImportSummary sync(TrackedEntityInstance trackedEntityInstance);
}
