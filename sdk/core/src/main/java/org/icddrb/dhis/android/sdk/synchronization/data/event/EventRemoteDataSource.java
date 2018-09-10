package org.icddrb.dhis.android.sdk.synchronization.data.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.icddrb.dhis.android.sdk.controllers.ApiEndpointContainer;
import org.icddrb.dhis.android.sdk.network.APIException;
import org.icddrb.dhis.android.sdk.network.DhisApi;
import org.icddrb.dhis.android.sdk.persistence.models.ApiResponse;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.ImportSummary;
import org.icddrb.dhis.android.sdk.synchronization.data.common.ARemoteDataSource;

public class EventRemoteDataSource extends ARemoteDataSource {
    public EventRemoteDataSource(DhisApi dhisApi) {
        this.dhisApi = dhisApi;
    }

    public Event getEvent(String event) {
        Map<String, String> QUERY_PARAMS = new HashMap();
        QUERY_PARAMS.put("fields", "created,lastUpdated");
        return this.dhisApi.getEvent(event, QUERY_PARAMS);
    }

    public ImportSummary update(Event event) {
        if (event.isDeleted()) {
            return delete(event, this.dhisApi);
        }
        return save(event);
    }

    private ImportSummary save(Event event) {
        if (event.getCreated() == null) {
            return postEvent(event, this.dhisApi);
        }
        return putEvent(event, this.dhisApi);
    }

    public List<ImportSummary> save(List<Event> events) {
        Map<String, List<Event>> eventsMap = new HashMap();
        eventsMap.put(ApiEndpointContainer.EVENTS, events);
        return batchEvents(eventsMap, this.dhisApi);
    }

    private List<ImportSummary> batchEvents(Map<String, List<Event>> events, DhisApi dhisApi) throws APIException {
        return dhisApi.postEvents(events).getImportSummaries();
    }

    private List<ImportSummary> batchDeletedEvents(Map<String, List<Event>> events, DhisApi dhisApi) throws APIException {
        for (Event event : events.get(ApiEndpointContainer.EVENTS)) {
            event.setStatus(null);
        }
        ApiResponse apiResponse = dhisApi.postDeletedEvents(events);
        for (Event event2 : events.get(ApiEndpointContainer.EVENTS)) {
            event2.setStatus(Event.STATUS_DELETED);
        }
        return apiResponse.getImportSummaries();
    }

    private ImportSummary delete(Event event, DhisApi dhisApi) throws APIException {
        return getImportSummary(dhisApi.deleteEvent(event.getUid()));
    }

    public List<ImportSummary> delete(List<Event> events) throws APIException {
        Map<String, List<Event>> eventsMap = new HashMap();
        eventsMap.put(ApiEndpointContainer.EVENTS, events);
        return batchDeletedEvents(eventsMap, this.dhisApi);
    }

    private ImportSummary postEvent(Event event, DhisApi dhisApi) throws APIException {
        return getImportSummary(dhisApi.postEvent(event));
    }

    private ImportSummary putEvent(Event event, DhisApi dhisApi) throws APIException {
        return getImportSummary(dhisApi.putEvent(event.getEvent(), event));
    }
}
