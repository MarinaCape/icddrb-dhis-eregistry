package org.icddrb.dhis.android.sdk.synchronization.domain.enrollment;


import org.icddrb.dhis.android.sdk.network.APIException;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.FailedItem;
import org.icddrb.dhis.android.sdk.persistence.models.ImportSummary;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.synchronization.domain.common.Synchronizer;
import org.icddrb.dhis.android.sdk.synchronization.domain.event.EventSynchronizer;
import org.icddrb.dhis.android.sdk.synchronization.domain.event.IEventRepository;
import org.icddrb.dhis.android.sdk.synchronization.domain.faileditem.IFailedItemRepository;

import java.util.Collections;
import java.util.List;

import static org.icddrb.dhis.android.sdk.persistence.models.FailedItem.TRACKEDENTITYINSTANCE;

public class EnrollmentSynchronizer extends Synchronizer {
    IEnrollmentRepository mEnrollmentRepository;
    IEventRepository mEventRepository;
    IFailedItemRepository mFailedItemRepository;
    Boolean canUpload = true;
    public EnrollmentSynchronizer(IEnrollmentRepository enrollmentRepository,
            IEventRepository eventRepository,
            IFailedItemRepository failedItemRepository) {
        super(failedItemRepository);

        mEnrollmentRepository = enrollmentRepository;
        mEventRepository = eventRepository;
        mFailedItemRepository = failedItemRepository;
    }

    public void sync(Enrollment enrollment) {
        boolean existsOnServerPreviously = enrollment.getCreated() != null;

        if (existsOnServerPreviously) {
            syncEvents(enrollment.getLocalId());
            if (canUpload && syncEnrollment(enrollment))
                changeEnrollmentToSynced(enrollment);
        } else {
            if (canUpload && syncEnrollment(enrollment))
            {
                syncEvents(enrollment.getLocalId());

                if ((enrollment.getStatus().equals(Enrollment.CANCELLED) ||
                        enrollment.getStatus().equals(Enrollment.COMPLETED))) {
                    //Send again because new enrollment is create as Active on server then
                    // Its necessary to change status from Active to Cancelled or Completed
                    if (syncEnrollment(enrollment));
                        changeEnrollmentToSynced(enrollment);
                }
                else{
                    changeEnrollmentToSynced(enrollment);
                }
            }else{
                if(syncEvents(enrollment.getLocalId())){
                    changeEnrollmentToSynced(enrollment);
                }
            }
        }
    }


    public void sync(List<Enrollment> enrollments){
        sync(enrollments, true);
    }
    public void sync(List<Enrollment> enrollments, Boolean canUpload) {
        Collections.sort(enrollments, new Enrollment.EnrollmentComparator());
        this.canUpload = canUpload;
        for (Enrollment enrollment : enrollments) {
            sync(enrollment);
        }
    }

    private boolean syncEnrollment(Enrollment enrollment) {
        boolean isSyncSuccess = true;

        try {
            ImportSummary importSummary = mEnrollmentRepository.sync(enrollment);

            if (importSummary.isSuccessOrOK()) {
                isSyncSuccess = true;

            } else if (importSummary.isError()) {
                super.handleImportSummaryError(importSummary, FailedItem.ENROLLMENT, 200,
                        enrollment.getLocalId());
                isSyncSuccess = false;
            }
        } catch (APIException api) {
            super.handleSerializableItemException(api, FailedItem.ENROLLMENT,
                    enrollment.getLocalId());
            isSyncSuccess = false;
        }

        return isSyncSuccess;
    }

    private void changeEnrollmentToSynced(Enrollment enrollment) {
        enrollment.setFromServer(true);
        mEnrollmentRepository.save(enrollment);
        super.clearFailedItem(FailedItem.ENROLLMENT, enrollment.getLocalId());
    }


    private boolean syncEvents(long enrollmentId) {
        EventSynchronizer eventSynchronizer = new EventSynchronizer(mEventRepository,
                mFailedItemRepository);

        List<Event> events = mEventRepository.getEventsByEnrollment(enrollmentId);
        List<Event> eventsToBeRemoved = mEventRepository.getEventsByEnrollmentToBeRemoved(
                enrollmentId);

        if (eventsToBeRemoved != null && eventsToBeRemoved.size() > 0) {
            eventSynchronizer.syncRemovedEvents(eventsToBeRemoved);
        }

        if (events != null && events.size() > 0) {
            eventSynchronizer.sync(events);
        }

        return true;
    }
}
