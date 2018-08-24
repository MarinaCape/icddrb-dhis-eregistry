package org.icddrb.dhis.android.sdk.synchronization.domain.enrollment;

import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.synchronization.domain.event.EventSynchronizer;
import org.icddrb.dhis.android.sdk.synchronization.domain.event.IEventRepository;
import org.icddrb.dhis.android.sdk.synchronization.domain.faileditem.IFailedItemRepository;
import org.icddrb.dhis.android.sdk.synchronization.domain.trackedentityinstance.ITrackedEntityInstanceRepository;
import org.icddrb.dhis.android.sdk.synchronization.domain.trackedentityinstance.TrackedEntityInstanceSynchronizer;

public class SyncEnrollmentUseCase {
    IEnrollmentRepository mEnrollmentRepository;
    EnrollmentSynchronizer mEnrollmentSynchronizer;
    EventSynchronizer mEventSynchronizer;
    IFailedItemRepository mFailedItemRepository;
    ITrackedEntityInstanceRepository mTrackedEntityInstanceRepository;
    TrackedEntityInstanceSynchronizer mTrackedEntityInstanceSynchronizer;

    public SyncEnrollmentUseCase(IEnrollmentRepository enrollmentRepository, IEventRepository eventRepository, ITrackedEntityInstanceRepository trackedEntityInstanceRepository, IFailedItemRepository failedItemRepository) {
        this.mTrackedEntityInstanceRepository = trackedEntityInstanceRepository;
        this.mEnrollmentRepository = enrollmentRepository;
        this.mFailedItemRepository = failedItemRepository;
        this.mEnrollmentSynchronizer = new EnrollmentSynchronizer(this.mEnrollmentRepository, eventRepository, this.mFailedItemRepository);
        this.mEventSynchronizer = new EventSynchronizer(eventRepository, this.mFailedItemRepository);
        this.mTrackedEntityInstanceSynchronizer = new TrackedEntityInstanceSynchronizer(trackedEntityInstanceRepository, this.mEnrollmentRepository, eventRepository, this.mFailedItemRepository);
    }

    public void execute(Enrollment enrollment) {
        if (enrollment == null) {
            throw new IllegalArgumentException("the Enrollment to sync can not be null");
        }
        TrackedEntityInstance tei = this.mTrackedEntityInstanceRepository.getTrackedEntityInstance(enrollment.getTrackedEntityInstance());
        if (tei.isFromServer()) {
            this.mEnrollmentSynchronizer.sync(enrollment);
        } else {
            this.mTrackedEntityInstanceSynchronizer.sync(tei);
        }
    }
}
