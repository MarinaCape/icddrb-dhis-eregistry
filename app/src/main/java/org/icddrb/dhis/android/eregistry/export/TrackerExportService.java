package org.icddrb.dhis.android.eregistry.export;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Select;
import java.util.List;
import org.icddrb.dhis.android.sdk.export.ExportService;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;

public class TrackerExportService extends ExportService<TrackerExportResponse> {
    public TrackerExportResponse getResponseObject() {
        FlowManager.init(this);
        List<TrackedEntityInstance> trackedEntityInstances = new Select().from(TrackedEntityInstance.class).queryList();
        List<Enrollment> enrollments = new Select().from(Enrollment.class).queryList();
        List<Event> events = new Select().from(Event.class).queryList();
        FlowManager.destroy();
        return TrackerExportResponse.build(trackedEntityInstances, enrollments, events);
    }
}
