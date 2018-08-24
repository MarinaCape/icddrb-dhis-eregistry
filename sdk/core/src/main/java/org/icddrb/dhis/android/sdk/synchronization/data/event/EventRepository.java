package org.icddrb.dhis.android.sdk.synchronization.data.event;

import android.support.annotation.Nullable;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import java.util.List;
import java.util.Map;
import org.icddrb.dhis.android.sdk.persistence.models.BaseIdentifiableObject;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.Event.Table;
import org.icddrb.dhis.android.sdk.persistence.models.ImportSummary;
import org.icddrb.dhis.android.sdk.synchronization.domain.event.IEventRepository;
import org.joda.time.DateTime;

public class EventRepository implements IEventRepository {
    EventLocalDataSource mLocalDataSource;
    EventRemoteDataSource mRemoteDataSource;

    public EventRepository(EventLocalDataSource localDataSource, EventRemoteDataSource remoteDataSource) {
        this.mLocalDataSource = localDataSource;
        this.mRemoteDataSource = remoteDataSource;
    }

    public List<Event> getEventsByEnrollment(long enrollmentId) {
        return new Select().from(Event.class).where(Condition.column(Table.LOCALENROLLMENTID).is(Long.valueOf(enrollmentId))).and(Condition.column("fromServer").is(Boolean.valueOf(false))).and(Condition.column("status").isNot(Event.STATUS_DELETED)).queryList();
    }

    public List<Event> getEventsByEnrollmentToBeRemoved(long enrollmentId) {
        return new Select().from(Event.class).where(Condition.column(Table.LOCALENROLLMENTID).is(Long.valueOf(enrollmentId))).and(Condition.column("fromServer").is(Boolean.valueOf(false))).and(Condition.column("status").is(Event.STATUS_DELETED)).queryList();
    }

    public void save(Event event) {
        this.mLocalDataSource.save(event);
    }

    public void delete(Event event) {
        this.mLocalDataSource.delete(event);
    }

    public ImportSummary sync(Event event) {
        ImportSummary importSummary = this.mRemoteDataSource.update(event);
        updateEventTimestampIfIsPushed(event, importSummary);
        return importSummary;
    }

    public void updateEventTimestampIfIsPushed(Event event, ImportSummary importSummary) {
        if ((importSummary.isSuccessOrOK() || importSummary.isConflictOnBatchPush()) && !event.isDeleted()) {
            updateEventTimestamp(event);
        }
    }

    public List<ImportSummary> sync(List<Event> events) {
        return updateEventsIfIsPushed(this.mRemoteDataSource.save((List) events), BaseIdentifiableObject.toMap(events));
    }

    @Nullable
    private List<ImportSummary> updateEventsIfIsPushed(List<ImportSummary> importSummaries, Map<String, Event> eventsMap) {
        DateTime dateTime = this.mRemoteDataSource.getServerTime();
        if (importSummaries != null) {
            for (ImportSummary importSummary : importSummaries) {
                if (importSummary.isSuccessOrOK()) {
                    System.out.println("IMPORT SUMMARY(event): " + importSummary.getDescription() + importSummary.getHref());
                    Event event = (Event) eventsMap.get(importSummary.getReference());
                    if (event != null) {
                        updateEventTimestamp(event, dateTime.toString(), dateTime.toString());
                    }
                }
            }
        }
        return importSummaries;
    }

    public List<ImportSummary> syncRemovedEvents(List<Event> events) {
        return this.mRemoteDataSource.delete(events);
    }

    private void updateEventTimestamp(Event event) {
        Event remoteEvent = this.mRemoteDataSource.getEvent(event.getEvent());
        updateEventTimestamp(event, remoteEvent.getCreated(), remoteEvent.getLastUpdated());
    }

    private void updateEventTimestamp(Event event, String createdDate, String lastUpdated) {
        event.setCreated(createdDate);
        event.setLastUpdated(lastUpdated);
        this.mLocalDataSource.save(event);
    }
}
