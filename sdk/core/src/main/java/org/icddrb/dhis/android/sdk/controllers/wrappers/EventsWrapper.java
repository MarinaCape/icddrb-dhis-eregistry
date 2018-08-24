package org.icddrb.dhis.android.sdk.controllers.wrappers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.icddrb.dhis.android.sdk.controllers.ApiEndpointContainer;
import org.icddrb.dhis.android.sdk.controllers.DhisController;
import org.icddrb.dhis.android.sdk.persistence.models.Event;

public class EventsWrapper {

    /* renamed from: org.icddrb.dhis.android.sdk.controllers.wrappers.EventsWrapper$1 */
    static class C08551 extends TypeReference<List<Event>> {
        C08551() {
        }
    }

    public static List<Event> getEvents(JsonNode jsonNode) {
        TypeReference typeRef = new C08551();
        try {
            if (jsonNode.has(ApiEndpointContainer.EVENTS)) {
                return (List) DhisController.getInstance().getObjectMapper().readValue(jsonNode.get(ApiEndpointContainer.EVENTS).traverse(), typeRef);
            }
            return new ArrayList();
        } catch (IOException e) {
            List<Event> events = new ArrayList();
            e.printStackTrace();
            return events;
        }
    }
}
