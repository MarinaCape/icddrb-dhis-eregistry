package org.icddrb.dhis.android.sdk.synchronization.data.trackedentityinstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.icddrb.dhis.android.sdk.controllers.ApiEndpointContainer;
import org.icddrb.dhis.android.sdk.network.APIException;
import org.icddrb.dhis.android.sdk.network.DhisApi;
import org.icddrb.dhis.android.sdk.persistence.models.ImportSummary;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.synchronization.data.common.ARemoteDataSource;

public class TrackedEntityInstanceRemoteDataSource extends ARemoteDataSource {
    public TrackedEntityInstanceRemoteDataSource(DhisApi dhisApi) {
        this.dhisApi = dhisApi;
    }

    public TrackedEntityInstance getTrackedEntityInstance(String trackedEntityInstance) {
        Map<String, String> QUERY_PARAMS = new HashMap();
        QUERY_PARAMS.put("fields", "created,lastUpdated");
        return this.dhisApi.getTrackedEntityInstance(trackedEntityInstance, QUERY_PARAMS);
    }

    public ImportSummary save(TrackedEntityInstance trackedEntityInstance) {
        if (trackedEntityInstance.getCreated() == null) {
            return postTrackedEntityInstance(trackedEntityInstance, this.dhisApi);
        }
        return putTrackedEntityInstance(trackedEntityInstance, this.dhisApi);
    }

    public List<ImportSummary> save(List<TrackedEntityInstance> trackedEntityInstances) {
        Map<String, List<TrackedEntityInstance>> map = new HashMap();
        map.put(ApiEndpointContainer.TRACKED_ENTITY_INSTANCES, trackedEntityInstances);
        return batchTrackedEntityInstances(map, this.dhisApi);
    }

    private List<ImportSummary> batchTrackedEntityInstances(Map<String, List<TrackedEntityInstance>> trackedEntityInstances, DhisApi dhisApi) throws APIException {
        return dhisApi.postTrackedEntityInstances(trackedEntityInstances).getImportSummaries();
    }

    private ImportSummary postTrackedEntityInstance(TrackedEntityInstance trackedEntityInstance, DhisApi dhisApi) throws APIException {
        return getImportSummary(dhisApi.postTrackedEntityInstance(trackedEntityInstance));
    }

    private ImportSummary putTrackedEntityInstance(TrackedEntityInstance trackedEntityInstance, DhisApi dhisApi) throws APIException {
        return getImportSummary(dhisApi.putTrackedEntityInstance(trackedEntityInstance.getUid(), trackedEntityInstance));
    }
}
