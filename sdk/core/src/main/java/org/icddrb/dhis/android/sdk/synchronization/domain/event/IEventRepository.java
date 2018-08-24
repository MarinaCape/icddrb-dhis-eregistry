package org.icddrb.dhis.android.sdk.synchronization.domain.event;

import java.util.List;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.ImportSummary;

public interface IEventRepository {
    void delete(Event event);

    List<Event> getEventsByEnrollment(long j);

    List<Event> getEventsByEnrollmentToBeRemoved(long j);

    void save(Event event);

    List<ImportSummary> sync(List<Event> list);

    ImportSummary sync(Event event);

    List<ImportSummary> syncRemovedEvents(List<Event> list);

    void updateEventTimestampIfIsPushed(Event event, ImportSummary importSummary);
}
