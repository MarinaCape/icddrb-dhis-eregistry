package org.icddrb.dhis.android.eregistry.export;

import java.util.List;
import org.icddrb.dhis.android.sdk.export.ExportResponse;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;

public class TrackerExportResponse extends ExportResponse {
    List<Enrollment> enrollments;
    List<Event> events;
    List<TrackedEntityInstance> trackedEntityInstances;

    private TrackerExportResponse(List<TrackedEntityInstance> trackedEntityInstances, List<Enrollment> enrollments, List<Event> events) {
        this.trackedEntityInstances = trackedEntityInstances;
        this.enrollments = enrollments;
        this.events = events;
    }

    public List<TrackedEntityInstance> getTrackedEntityInstances() {
        return this.trackedEntityInstances;
    }

    public void setTrackedEntityInstances(List<TrackedEntityInstance> trackedEntityInstances) {
        this.trackedEntityInstances = trackedEntityInstances;
    }

    public List<Enrollment> getEnrollments() {
        return this.enrollments;
    }

    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

    public List<Event> getEvents() {
        return this.events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public static TrackerExportResponse build(List<TrackedEntityInstance> trackedEntityInstances, List<Enrollment> enrollments, List<Event> events) {
        return new TrackerExportResponse(trackedEntityInstances, enrollments, events);
    }
}
