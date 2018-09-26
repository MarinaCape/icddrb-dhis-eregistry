package org.icddrb.dhis.android.sdk.synchronization.data.trackedentityinstance;

import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.controllers.tracker.TrackerController;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.ImportSummary;
import org.icddrb.dhis.android.sdk.persistence.models.OrganisationUnit;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.synchronization.domain.trackedentityinstance.ITrackedEntityInstanceRepository;
import org.icddrb.dhis.android.sdk.ui.dialogs.ItemStatusDialogFragment;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Map;

public class TrackedEntityInstanceRepository  implements ITrackedEntityInstanceRepository {
    TrackedEntityInstanceLocalDataSource mLocalDataSource;
    TrackedEntityInstanceRemoteDataSource mRemoteDataSource;

    public TrackedEntityInstanceRepository(
            TrackedEntityInstanceLocalDataSource localDataSource,
            TrackedEntityInstanceRemoteDataSource remoteDataSource) {
        mLocalDataSource = localDataSource;
        mRemoteDataSource = remoteDataSource;
    }
    @Override
    public void save(TrackedEntityInstance trackedEntityInstance) {
        mLocalDataSource.save(trackedEntityInstance);
    }

    @Override
    public ImportSummary sync(TrackedEntityInstance trackedEntityInstance) {
        ImportSummary importSummary = mRemoteDataSource.save(trackedEntityInstance);

        if (importSummary != null && importSummary.isSuccessOrOK()) {
            updateTrackedEntityInstanceTimestamp(trackedEntityInstance);
        }

        return importSummary;
    }

    @Override
    public List<ImportSummary> sync(List<TrackedEntityInstance> trackedEntityInstanceList) {
        for(TrackedEntityInstance trackedEntityInstance: trackedEntityInstanceList){
            OrganisationUnit orgUnit = MetaDataController.getOrganisationUnit(trackedEntityInstance.getOrgUnit());
            if(orgUnit == null || orgUnit.getType() == OrganisationUnit.TYPE.SEARCH){
                List<Enrollment> enrollments = TrackerController.getEnrollments(trackedEntityInstance);
                for(Enrollment enrollment: enrollments)
                    for(Event event: TrackerController.getEventsByEnrollment(enrollment.getEnrollment()))
                        ItemStatusDialogFragment.sendEvent(event);
            }
        }
        List<ImportSummary> importSummaries = mRemoteDataSource.save(trackedEntityInstanceList);

        Map<String, TrackedEntityInstance> trackedEntityInstanceMap =
                TrackedEntityInstance.toMap(trackedEntityInstanceList);

        if (importSummaries != null) {
            DateTime dateTime = mRemoteDataSource.getServerTime();
            for (ImportSummary importSummary : importSummaries) {
                if (importSummary.isSuccessOrOK()) {
                    System.out.println("IMPORT SUMMARY(teibatch): " + importSummary.getDescription() + importSummary.getHref() +" "+ importSummary.getReference());
                    TrackedEntityInstance trackedEntityInstance = trackedEntityInstanceMap.get(importSummary.getReference());
                    if (trackedEntityInstance != null) {
                        updateTrackedEntityInstanceTimestamp(trackedEntityInstance, dateTime.toString(), dateTime.toString());
                    }
                }
            }
        }
        return importSummaries;
    }

    private void updateTrackedEntityInstanceTimestamp(TrackedEntityInstance trackedEntityInstance) {
        TrackedEntityInstance remoteTrackedEntityInstance = mRemoteDataSource.getTrackedEntityInstance(trackedEntityInstance.getTrackedEntityInstance());
        if(trackedEntityInstance.getRelationships()!=null && trackedEntityInstance.getRelationships().size()==0){
            //Restore relations before save.
            trackedEntityInstance.setRelationships(null);
            trackedEntityInstance.getRelationships();
        }
        updateTrackedEntityInstanceTimestamp(trackedEntityInstance, remoteTrackedEntityInstance.getCreated(), remoteTrackedEntityInstance.getLastUpdated());
    }

    private void updateTrackedEntityInstanceTimestamp(TrackedEntityInstance trackedEntityInstance, String createdDate, String lastUpdated) {
        trackedEntityInstance.setCreated(createdDate);
        trackedEntityInstance.setLastUpdated(lastUpdated);

        mLocalDataSource.save(trackedEntityInstance);
    }

    @Override
    public TrackedEntityInstance getTrackedEntityInstance(String trackedEntityInstanceUid) {
        return mLocalDataSource.getTrackedEntityInstance(trackedEntityInstanceUid);
    }

    @Override
    public List<TrackedEntityInstance> getAllLocalTeis() {
        return mLocalDataSource.getAllLocalTeis();
    }
}