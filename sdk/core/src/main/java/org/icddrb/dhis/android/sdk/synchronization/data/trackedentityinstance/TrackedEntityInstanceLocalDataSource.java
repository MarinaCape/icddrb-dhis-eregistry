package org.icddrb.dhis.android.sdk.synchronization.data.trackedentityinstance;

import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import java.util.List;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;

public class TrackedEntityInstanceLocalDataSource {
    public void save(TrackedEntityInstance trackedEntityInstance) {
        trackedEntityInstance.save();
    }

    public List<TrackedEntityInstance> getAllLocalTeis() {
        return new Select().from(TrackedEntityInstance.class).where(Condition.column("fromServer").is(Boolean.valueOf(false))).queryList();
    }

    public TrackedEntityInstance getTrackedEntityInstance(String trackedEntityInstanceUid) {
        return (TrackedEntityInstance) new Select().from(TrackedEntityInstance.class).where(Condition.column("trackedEntityInstance").is(trackedEntityInstanceUid)).querySingle();
    }
}
