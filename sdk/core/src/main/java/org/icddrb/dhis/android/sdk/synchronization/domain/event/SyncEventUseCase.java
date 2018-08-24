package org.icddrb.dhis.android.sdk.synchronization.domain.event;

import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.synchronization.domain.enrollment.EnrollmentSynchronizer;
import org.icddrb.dhis.android.sdk.synchronization.domain.enrollment.IEnrollmentRepository;
import org.icddrb.dhis.android.sdk.synchronization.domain.faileditem.IFailedItemRepository;
import org.icddrb.dhis.android.sdk.synchronization.domain.trackedentityinstance.ITrackedEntityInstanceRepository;
import org.icddrb.dhis.android.sdk.synchronization.domain.trackedentityinstance.TrackedEntityInstanceSynchronizer;

public class SyncEventUseCase {
    IEnrollmentRepository mEnrollmentRepository;
    EnrollmentSynchronizer mEnrollmentSynchronizer = new EnrollmentSynchronizer(this.mEnrollmentRepository, this.mEventRepository, this.mFailedItemRepository);
    IEventRepository mEventRepository;
    EventSynchronizer mEventSynchronizer = new EventSynchronizer(this.mEventRepository, this.mFailedItemRepository);
    IFailedItemRepository mFailedItemRepository;
    ITrackedEntityInstanceRepository mTrackedEntityInstanceRepository;
    TrackedEntityInstanceSynchronizer mTrackedEntityInstanceSynchronizer = new TrackedEntityInstanceSynchronizer(this.mTrackedEntityInstanceRepository, this.mEnrollmentRepository, this.mEventRepository, this.mFailedItemRepository);

    public SyncEventUseCase(IEventRepository eventRepository, IEnrollmentRepository enrollmentRepository, ITrackedEntityInstanceRepository trackedEntityInstanceRepository, IFailedItemRepository failedItemRepository) {
        this.mEventRepository = eventRepository;
        this.mEnrollmentRepository = enrollmentRepository;
        this.mTrackedEntityInstanceRepository = trackedEntityInstanceRepository;
        this.mFailedItemRepository = failedItemRepository;
    }

    public void execute(Event event) {
        if (event == null) {
            throw new IllegalArgumentException("the Event to sync can not be null");
        }
        this.mEventSynchronizer.sync(event);
    }
}
