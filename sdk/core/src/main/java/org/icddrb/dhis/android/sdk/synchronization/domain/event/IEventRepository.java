package org.icddrb.dhis.android.sdk.synchronization.domain.event;

import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.ImportSummary;

import java.util.List;

public interface IEventRepository {
    List<Event> getEventsByEnrollment(long enrollmentId);

    List<Event> getEventsByEnrollmentToBeRemoved(long enrollmentId);

    void save(Event event);

    void delete(Event event);

    ImportSummary sync(Event event);

    List<ImportSummary> sync(List<Event> event);

    List<ImportSummary> syncRemovedEvents(List<Event> event);

    void updateEventTimestampIfIsPushed(Event event,
            ImportSummary importSummary);
}
