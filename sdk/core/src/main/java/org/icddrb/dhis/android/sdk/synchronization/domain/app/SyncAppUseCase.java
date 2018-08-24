package org.icddrb.dhis.android.sdk.synchronization.domain.app;

import org.icddrb.dhis.android.sdk.synchronization.domain.enrollment.IEnrollmentRepository;
import org.icddrb.dhis.android.sdk.synchronization.domain.event.IEventRepository;
import org.icddrb.dhis.android.sdk.synchronization.domain.faileditem.IFailedItemRepository;
import org.icddrb.dhis.android.sdk.synchronization.domain.trackedentityinstance.ITrackedEntityInstanceRepository;
import org.icddrb.dhis.android.sdk.synchronization.domain.trackedentityinstance.TrackedEntityInstanceSynchronizer;

public class SyncAppUseCase {
    IFailedItemRepository mFailedItemRepository;
    ITrackedEntityInstanceRepository mTrackedEntityInstanceRepository;
    TrackedEntityInstanceSynchronizer mTrackedEntityInstanceSynchronizer;

    public SyncAppUseCase(ITrackedEntityInstanceRepository trackedEntityInstanceRepository, IEnrollmentRepository enrollmentRepository, IEventRepository eventRepository, IFailedItemRepository failedItemRepository) {
        this.mTrackedEntityInstanceRepository = trackedEntityInstanceRepository;
        this.mFailedItemRepository = failedItemRepository;
        this.mTrackedEntityInstanceSynchronizer = new TrackedEntityInstanceSynchronizer(this.mTrackedEntityInstanceRepository, enrollmentRepository, eventRepository, this.mFailedItemRepository);
    }

    public void execute() {
        this.mTrackedEntityInstanceSynchronizer.sync(this.mTrackedEntityInstanceRepository.getAllLocalTeis());
    }
}
