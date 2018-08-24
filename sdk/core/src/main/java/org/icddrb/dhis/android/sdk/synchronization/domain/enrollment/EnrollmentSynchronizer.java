package org.icddrb.dhis.android.sdk.synchronization.domain.enrollment;

import java.util.Collections;
import java.util.List;
import org.icddrb.dhis.android.sdk.network.APIException;
import org.icddrb.dhis.android.sdk.persistence.models.Conflict.Table;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment.EnrollmentComparator;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.ImportSummary;
import org.icddrb.dhis.android.sdk.synchronization.domain.common.Synchronizer;
import org.icddrb.dhis.android.sdk.synchronization.domain.event.EventSynchronizer;
import org.icddrb.dhis.android.sdk.synchronization.domain.event.IEventRepository;
import org.icddrb.dhis.android.sdk.synchronization.domain.faileditem.IFailedItemRepository;

public class EnrollmentSynchronizer extends Synchronizer {
    IEnrollmentRepository mEnrollmentRepository;
    IEventRepository mEventRepository;
    IFailedItemRepository mFailedItemRepository;

    public EnrollmentSynchronizer(IEnrollmentRepository enrollmentRepository, IEventRepository eventRepository, IFailedItemRepository failedItemRepository) {
        super(failedItemRepository);
        this.mEnrollmentRepository = enrollmentRepository;
        this.mEventRepository = eventRepository;
        this.mFailedItemRepository = failedItemRepository;
    }

    private boolean canShowError(APIException api, Enrollment e) {
        System.out.println("Norway - synching event status: " + api.getResponse().getStatus());
        System.out.println("Norway - synching event reason: " + api.getResponse().getReason());
        if (api.getResponse() == null || api.getResponse().getStatus() != 409 || !api.getResponse().getReason().equals(Table.TABLE_NAME)) {
            return true;
        }
        System.out.println("Norway - temporarily not showing conflict error");
        changeEnrollmentToSynced(e);
        return false;
    }

    public void sync(Enrollment enrollment) {
        if (enrollment.getCreated() != null) {
            syncEvents(enrollment.getLocalId());
            if (syncEnrollment(enrollment)) {
                changeEnrollmentToSynced(enrollment);
            }
        } else if (syncEnrollment(enrollment)) {
            syncEvents(enrollment.getLocalId());
            if (enrollment.getStatus().equals(Enrollment.CANCELLED) || enrollment.getStatus().equals("COMPLETED")) {
                if (syncEnrollment(enrollment)) {
                    changeEnrollmentToSynced(enrollment);
                } else {
                    changeEnrollmentToSynced(enrollment);
                }
                return;
            }
            changeEnrollmentToSynced(enrollment);
        }
    }

    public void sync(List<Enrollment> enrollments) {
        Collections.sort(enrollments, new EnrollmentComparator());
        for (Enrollment enrollment : enrollments) {
            sync(enrollment);
        }
    }

    private boolean syncEnrollment(Enrollment enrollment) {
        try {
            ImportSummary importSummary = this.mEnrollmentRepository.sync(enrollment);
            if (importSummary.isSuccessOrOK()) {
                return true;
            }
            if (!importSummary.isError()) {
                return true;
            }
            super.handleImportSummaryError(importSummary, "Enrollment", 200, enrollment.getLocalId());
            return false;
        } catch (APIException api) {
            if (!canShowError(api, enrollment)) {
                return true;
            }
            super.handleSerializableItemException(api, "Enrollment", enrollment.getLocalId());
            return false;
        }
    }

    private void changeEnrollmentToSynced(Enrollment enrollment) {
        enrollment.setFromServer(true);
        this.mEnrollmentRepository.save(enrollment);
        super.clearFailedItem("Enrollment", enrollment.getLocalId());
    }

    private void syncEvents(long enrollmentId) {
        EventSynchronizer eventSynchronizer = new EventSynchronizer(this.mEventRepository, this.mFailedItemRepository);
        List events = this.mEventRepository.getEventsByEnrollment(enrollmentId);
        List<Event> eventsToBeRemoved = this.mEventRepository.getEventsByEnrollmentToBeRemoved(enrollmentId);
        if (eventsToBeRemoved != null && eventsToBeRemoved.size() > 0) {
            eventSynchronizer.syncRemovedEvents(eventsToBeRemoved);
        }
        if (events != null && events.size() > 0) {
            eventSynchronizer.sync(events);
        }
    }
}
