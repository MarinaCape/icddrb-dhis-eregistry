package org.icddrb.dhis.android.sdk.synchronization.domain.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.icddrb.dhis.android.sdk.network.APIException;
import org.icddrb.dhis.android.sdk.persistence.models.Conflict.Table;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.ImportSummary;
import org.icddrb.dhis.android.sdk.synchronization.domain.common.Synchronizer;
import org.icddrb.dhis.android.sdk.synchronization.domain.faileditem.IFailedItemRepository;

public class EventSynchronizer extends Synchronizer {
    IEventRepository mEventRepository;
    IFailedItemRepository mFailedItemRepository;

    public EventSynchronizer(IEventRepository eventRepository, IFailedItemRepository failedItemRepository) {
        super(failedItemRepository);
        this.mEventRepository = eventRepository;
        this.mFailedItemRepository = failedItemRepository;
    }

    private boolean canShowError(APIException api, Event e) {
        System.out.println("Norway - synching event status: " + api.getResponse().getStatus());
        System.out.println("Norway - synching event reason: " + api.getResponse().getReason());
        if (api.getResponse() == null || api.getResponse().getStatus() != 409 || !api.getResponse().getReason().equals(Table.TABLE_NAME)) {
            return true;
        }
        System.out.println("Norway - temporarily not showing conflict error");
        updateSyncedEventLocally(e);
        return false;
    }

    public void sync(Event event) {
        try {
            if (isDeletedAndOnlyLocalEvent(event)) {
                this.mEventRepository.delete(event);
                return;
            }
            System.out.println("Synchronizing single event " + event.getUid() + " " + event.getStatus());
            manageSyncResult(event, this.mEventRepository.sync(event));
        } catch (APIException api) {
            if (canShowError(api, event)) {
                super.handleSerializableItemException(api, "Event", event.getLocalId());
            }
        }
    }

    public void sync(List<Event> events) {
        Event event;
        if (events != null && events.size() != 0) {
            Map<String, Event> eventsMapCheck = removeDeletedAndOnlyLocalEvents(events);
            if (eventsMapCheck.values().size() != 0) {
                System.out.println("Synchronizing list of events ");
                try {
                    for (ImportSummary importSummary : this.mEventRepository.sync(new ArrayList(eventsMapCheck.values()))) {
                        event = (Event) eventsMapCheck.get(importSummary.getReference());
                        if (event != null) {
                            manageSyncResult(event, importSummary);
                        }
                    }
                } catch (APIException api) {
                    if (!(api == null || api.getResponse() == null)) {
                        List<ImportSummary> importSummaries = getImportSummary(api.getResponse());
                        if (importSummaries != null) {
                            for (ImportSummary importSummary2 : importSummaries) {
                                event = (Event) eventsMapCheck.get(importSummary2.getReference());
                                if (event != null) {
                                    this.mEventRepository.updateEventTimestampIfIsPushed(event, importSummary2);
                                    manageSyncResult(event, importSummary2);
                                    events.remove(event);
                                }
                            }
                        }
                    }
                    syncOneByOne(events);
                }
            }
        }
    }

    public void syncRemovedEvents(List<Event> events) {
        if (events != null) {
            try {
                if (events.size() != 0) {
                    Map<String, Event> eventsMapCheck = removeDeletedAndOnlyLocalEvents(events);
                    if (eventsMapCheck.values().size() != 0) {
                        System.out.println("Synchronizing removed events");
                        for (ImportSummary importSummary : this.mEventRepository.syncRemovedEvents(new ArrayList(eventsMapCheck.values()))) {
                            Event event = (Event) eventsMapCheck.get(importSummary.getReference());
                            if (event == null && importSummary.getDescription() != null) {
                                event = (Event) eventsMapCheck.get(importSummary.getDescription().replace("Deletion of event ", "").replace(" was successful", ""));
                            }
                            if (event != null) {
                                manageSyncResult(event, importSummary);
                            }
                        }
                    }
                }
            } catch (APIException e) {
                syncOneByOne(events);
            }
        }
    }

    private void manageSyncResult(Event event, ImportSummary importSummary) {
        if (importSummary.isSuccessOrOK()) {
            updateSyncedEventLocally(event);
        } else if (importSummary.isError()) {
            super.handleImportSummaryError(importSummary, "Event", 200, event.getLocalId());
        }
    }

    private void updateSyncedEventLocally(Event event) {
        if (event.isDeleted()) {
            this.mEventRepository.delete(event);
        } else {
            event.setFromServer(true);
            this.mEventRepository.save(event);
        }
        super.clearFailedItem("Event", event.getLocalId());
    }

    private void syncOneByOne(List<Event> events) {
        for (Event event : events) {
            sync(event);
        }
    }

    public boolean isDeletedAndOnlyLocalEvent(Event event) {
        return event.isDeleted() && event.getCreated() == null;
    }

    private Map<String, Event> removeDeletedAndOnlyLocalEvents(List<Event> events) {
        Map<String, Event> eventsMapCheck = new HashMap();
        for (Event event : events) {
            if (isDeletedAndOnlyLocalEvent(event)) {
                this.mEventRepository.delete(event);
            } else {
                eventsMapCheck.put(event.getUid(), event);
            }
        }
        return eventsMapCheck;
    }
}
