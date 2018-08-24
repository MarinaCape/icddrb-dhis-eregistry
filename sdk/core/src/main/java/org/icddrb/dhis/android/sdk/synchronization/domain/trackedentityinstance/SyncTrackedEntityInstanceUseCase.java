package org.icddrb.dhis.android.sdk.synchronization.domain.trackedentityinstance;

import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.synchronization.domain.enrollment.IEnrollmentRepository;
import org.icddrb.dhis.android.sdk.synchronization.domain.event.IEventRepository;
import org.icddrb.dhis.android.sdk.synchronization.domain.faileditem.IFailedItemRepository;

public class SyncTrackedEntityInstanceUseCase {
    IFailedItemRepository mFailedItemRepository;
    ITrackedEntityInstanceRepository mTrackedEntityInstanceRepository;
    TrackedEntityInstanceSynchronizer mTrackedEntityInstanceSynchronizer;

    public SyncTrackedEntityInstanceUseCase(ITrackedEntityInstanceRepository trackedEntityInstanceRepository, IEnrollmentRepository enrollmentRepository, IEventRepository eventRepository, IFailedItemRepository failedItemRepository) {
        this.mTrackedEntityInstanceRepository = trackedEntityInstanceRepository;
        this.mFailedItemRepository = failedItemRepository;
        this.mTrackedEntityInstanceSynchronizer = new TrackedEntityInstanceSynchronizer(this.mTrackedEntityInstanceRepository, enrollmentRepository, eventRepository, this.mFailedItemRepository);
    }

    public void execute(TrackedEntityInstance trackedEntityInstance) {
        if (trackedEntityInstance == null) {
            throw new IllegalArgumentException("the trackedEntityInstance to sync can not be null");
        }
        this.mTrackedEntityInstanceSynchronizer.sync(trackedEntityInstance);
    }
}
