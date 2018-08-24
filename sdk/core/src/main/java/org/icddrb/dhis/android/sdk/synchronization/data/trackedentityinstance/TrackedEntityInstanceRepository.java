package org.icddrb.dhis.android.sdk.synchronization.data.trackedentityinstance;

import java.util.List;
import java.util.Map;
import org.icddrb.dhis.android.sdk.persistence.models.BaseIdentifiableObject;
import org.icddrb.dhis.android.sdk.persistence.models.ImportSummary;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.synchronization.domain.trackedentityinstance.ITrackedEntityInstanceRepository;
import org.joda.time.DateTime;

public class TrackedEntityInstanceRepository implements ITrackedEntityInstanceRepository {
    TrackedEntityInstanceLocalDataSource mLocalDataSource;
    TrackedEntityInstanceRemoteDataSource mRemoteDataSource;

    public TrackedEntityInstanceRepository(TrackedEntityInstanceLocalDataSource localDataSource, TrackedEntityInstanceRemoteDataSource remoteDataSource) {
        this.mLocalDataSource = localDataSource;
        this.mRemoteDataSource = remoteDataSource;
    }

    public void save(TrackedEntityInstance trackedEntityInstance) {
        this.mLocalDataSource.save(trackedEntityInstance);
    }

    public ImportSummary sync(TrackedEntityInstance trackedEntityInstance) {
        ImportSummary importSummary = this.mRemoteDataSource.save(trackedEntityInstance);
        if (importSummary.isSuccessOrOK()) {
            updateTrackedEntityInstanceTimestamp(trackedEntityInstance);
        }
        return importSummary;
    }

    public List<ImportSummary> sync(List<TrackedEntityInstance> trackedEntityInstanceList) {
        List<ImportSummary> importSummaries = this.mRemoteDataSource.save((List) trackedEntityInstanceList);
        Map<String, TrackedEntityInstance> trackedEntityInstanceMap = BaseIdentifiableObject.toMap(trackedEntityInstanceList);
        if (importSummaries != null) {
            DateTime dateTime = this.mRemoteDataSource.getServerTime();
            for (ImportSummary importSummary : importSummaries) {
                if (importSummary.isSuccessOrOK()) {
                    System.out.println("IMPORT SUMMARY(teibatch): " + importSummary.getDescription() + importSummary.getHref() + " " + importSummary.getReference());
                    TrackedEntityInstance trackedEntityInstance = (TrackedEntityInstance) trackedEntityInstanceMap.get(importSummary.getReference());
                    if (trackedEntityInstance != null) {
                        updateTrackedEntityInstanceTimestamp(trackedEntityInstance, dateTime.toString(), dateTime.toString());
                    }
                }
            }
        }
        return importSummaries;
    }

    private void updateTrackedEntityInstanceTimestamp(TrackedEntityInstance trackedEntityInstance) {
        TrackedEntityInstance remoteTrackedEntityInstance = this.mRemoteDataSource.getTrackedEntityInstance(trackedEntityInstance.getTrackedEntityInstance());
        if (trackedEntityInstance.getRelationships() != null && trackedEntityInstance.getRelationships().size() == 0) {
            trackedEntityInstance.setRelationships(null);
            trackedEntityInstance.getRelationships();
        }
        updateTrackedEntityInstanceTimestamp(trackedEntityInstance, remoteTrackedEntityInstance.getCreated(), remoteTrackedEntityInstance.getLastUpdated());
    }

    private void updateTrackedEntityInstanceTimestamp(TrackedEntityInstance trackedEntityInstance, String createdDate, String lastUpdated) {
        trackedEntityInstance.setCreated(createdDate);
        trackedEntityInstance.setLastUpdated(lastUpdated);
        this.mLocalDataSource.save(trackedEntityInstance);
    }

    public TrackedEntityInstance getTrackedEntityInstance(String trackedEntityInstanceUid) {
        return this.mLocalDataSource.getTrackedEntityInstance(trackedEntityInstanceUid);
    }

    public List<TrackedEntityInstance> getAllLocalTeis() {
        return this.mLocalDataSource.getAllLocalTeis();
    }
}
