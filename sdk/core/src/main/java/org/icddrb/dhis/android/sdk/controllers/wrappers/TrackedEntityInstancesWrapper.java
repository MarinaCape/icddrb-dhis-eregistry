package org.icddrb.dhis.android.sdk.controllers.wrappers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.icddrb.dhis.android.sdk.controllers.DhisController;
import org.icddrb.dhis.android.sdk.persistence.models.Header;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;

public class TrackedEntityInstancesWrapper {

    /* renamed from: org.icddrb.dhis.android.sdk.controllers.wrappers.TrackedEntityInstancesWrapper$1 */
    static class C08561 extends TypeReference<List<Header>> {
        C08561() {
        }
    }

    /* renamed from: org.icddrb.dhis.android.sdk.controllers.wrappers.TrackedEntityInstancesWrapper$2 */
    static class C08572 extends TypeReference<List<List<String>>> {
        C08572() {
        }
    }

    public static List<TrackedEntityInstance> parseTrackedEntityInstances(byte[] body) throws IOException {
        JsonNode node = DhisController.getInstance().getObjectMapper().readTree(body);
        JsonNode headersNode = node.get("headers");
        List<Header> headers = (List) DhisController.getInstance().getObjectMapper().readValue(headersNode.traverse(), new C08561());
        JsonNode rowsNode = node.get("rows");
        List<List<String>> rows = (List) DhisController.getInstance().getObjectMapper().readValue(rowsNode.traverse(), new C08572());
        List<TrackedEntityInstance> trackedEntityInstances = new ArrayList();
        for (List<String> row : rows) {
            TrackedEntityInstance trackedEntityInstance = new TrackedEntityInstance();
            List<TrackedEntityAttributeValue> trackedEntityAttributeValues = new ArrayList();
            trackedEntityInstance.setTrackedEntityInstance((String) row.get(0));
            trackedEntityInstance.setCreated((String) row.get(1));
            trackedEntityInstance.setLastUpdated((String) row.get(2));
            trackedEntityInstance.setOrgUnit((String) row.get(3));
            trackedEntityInstance.setTrackedEntity((String) row.get(4));
            trackedEntityInstances.add(trackedEntityInstance);
            if (row.size() > 4) {
                for (int i = 5; i < row.size(); i++) {
                    TrackedEntityAttributeValue value = new TrackedEntityAttributeValue();
                    value.setTrackedEntityAttributeId(((Header) headers.get(i)).getName());
                    value.setTrackedEntityInstanceId(trackedEntityInstance.getTrackedEntityInstance());
                    value.setValue((String) row.get(i));
                    trackedEntityAttributeValues.add(value);
                }
                trackedEntityInstance.setAttributes(trackedEntityAttributeValues);
            }
        }
        return trackedEntityInstances;
    }
}
