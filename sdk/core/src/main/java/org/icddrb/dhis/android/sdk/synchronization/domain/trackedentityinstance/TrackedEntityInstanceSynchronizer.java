package org.icddrb.dhis.android.sdk.synchronization.domain.trackedentityinstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.icddrb.dhis.android.sdk.network.APIException;
import org.icddrb.dhis.android.sdk.persistence.models.BaseIdentifiableObject;
import org.icddrb.dhis.android.sdk.persistence.models.Conflict.Table;
import org.icddrb.dhis.android.sdk.persistence.models.ImportSummary;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.synchronization.domain.common.Synchronizer;
import org.icddrb.dhis.android.sdk.synchronization.domain.enrollment.EnrollmentSynchronizer;
import org.icddrb.dhis.android.sdk.synchronization.domain.enrollment.IEnrollmentRepository;
import org.icddrb.dhis.android.sdk.synchronization.domain.event.IEventRepository;
import org.icddrb.dhis.android.sdk.synchronization.domain.faileditem.IFailedItemRepository;

public class TrackedEntityInstanceSynchronizer extends Synchronizer {
    IEnrollmentRepository mEnrollmentRepository;
    IEventRepository mEventRepository;
    IFailedItemRepository mFailedItemRepository;
    ITrackedEntityInstanceRepository mTrackedEntityInstanceRepository;

    public TrackedEntityInstanceSynchronizer(ITrackedEntityInstanceRepository trackedEntityInstanceRepository, IEnrollmentRepository enrollmentRepository, IEventRepository eventRepository, IFailedItemRepository failedItemRepository) {
        super(failedItemRepository);
        this.mTrackedEntityInstanceRepository = trackedEntityInstanceRepository;
        this.mEnrollmentRepository = enrollmentRepository;
        this.mEventRepository = eventRepository;
        this.mFailedItemRepository = failedItemRepository;
    }

    public void sync(TrackedEntityInstance trackedEntityInstance) {
        List<TrackedEntityInstance> trackedEntityInstances = this.mTrackedEntityInstanceRepository.getAllLocalTeis();
        if (!existsRelationships(trackedEntityInstance) || trackedEntityInstances.size() <= 1) {
            syncSingleTei(trackedEntityInstance);
        } else {
            syncAllTeisInTwoSteps(trackedEntityInstances);
        }
    }

    public void sync(List<TrackedEntityInstance> trackedEntityInstances) {
        if (trackedEntityInstances != null && trackedEntityInstances.size() > 0) {
            if (existsRelationships((List) trackedEntityInstances)) {
                syncAllTeisInTwoSteps(trackedEntityInstances);
            } else if (trackedEntityInstances.size() == 1) {
                syncSingleTei((TrackedEntityInstance) trackedEntityInstances.get(0));
            } else {
                syncTeis(trackedEntityInstances);
            }
        }
    }

    private void syncAllTeisInTwoSteps(List<TrackedEntityInstance> trackedEntityInstances) {
        for (TrackedEntityInstance trackedEntityInstance : trackedEntityInstances) {
            trackedEntityInstance.setRelationships(new ArrayList());
        }
        syncTeis(trackedEntityInstances);
        for (TrackedEntityInstance trackedEntityInstance2 : trackedEntityInstances) {
            trackedEntityInstance2.setFromServer(false);
            trackedEntityInstance2.setRelationships(null);
            trackedEntityInstance2.getRelationships();
        }
        syncTeis(trackedEntityInstances);
    }

    private void syncSingleTei(TrackedEntityInstance trackedEntityInstance) {
        try {
            ImportSummary importSummary = this.mTrackedEntityInstanceRepository.sync(trackedEntityInstance);
            if (importSummary.isSuccessOrOK()) {
                syncEnrollments(trackedEntityInstance.getLocalId());
                changeTEIToSynced(trackedEntityInstance);
            } else if (importSummary.isError()) {
                super.handleImportSummaryError(importSummary, "TrackedEntityInstance", 200, trackedEntityInstance.getLocalId());
            }
        } catch (APIException api) {
            if (canShowError(api, trackedEntityInstance)) {
                super.handleSerializableItemException(api, "TrackedEntityInstance", trackedEntityInstance.getLocalId());
            }
        }
    }

    private void syncTeis(List<TrackedEntityInstance> trackedEntityInstances) {
        try {
            Map<String, TrackedEntityInstance> trackedEntityInstanceMap = BaseIdentifiableObject.toMap(trackedEntityInstances);
            for (ImportSummary importSummary : this.mTrackedEntityInstanceRepository.sync((List) trackedEntityInstances)) {
                TrackedEntityInstance trackedEntityInstance = (TrackedEntityInstance) trackedEntityInstanceMap.get(importSummary.getReference());
                if (trackedEntityInstance != null) {
                    if (importSummary.isSuccessOrOK()) {
                        syncEnrollments(trackedEntityInstance.getLocalId());
                        changeTEIToSynced(trackedEntityInstance);
                    } else if (importSummary.isError()) {
                        super.handleImportSummaryError(null, "TrackedEntityInstance", 200, 16842960);
                    }
                }
            }
        } catch (Exception e) {
            syncOneByOne(trackedEntityInstances);
        }
    }

    private void syncOneByOne(List<TrackedEntityInstance> trackedEntityInstances) {
        for (TrackedEntityInstance trackedEntityInstance : trackedEntityInstances) {
            syncSingleTei(trackedEntityInstance);
        }
    }

    private boolean canShowError(APIException api, TrackedEntityInstance ti) {
        System.out.println("Norway - synching tei status: " + api.getResponse().getStatus());
        System.out.println("Norway - synching tei reason: " + api.getResponse().getReason());
        if (api.getResponse() == null || api.getResponse().getStatus() != 409 || !api.getResponse().getReason().equals(Table.TABLE_NAME)) {
            return true;
        }
        System.out.println("Norway - temporarily not showing conflict error");
        changeTEIToSynced(ti);
        return false;
    }

    private void changeTEIToSynced(TrackedEntityInstance trackedEntityInstance) {
        trackedEntityInstance.setFromServer(true);
        this.mTrackedEntityInstanceRepository.save(trackedEntityInstance);
        super.clearFailedItem("TrackedEntityInstance", trackedEntityInstance.getLocalId());
    }

    private void syncEnrollments(long localId) {
        new EnrollmentSynchronizer(this.mEnrollmentRepository, this.mEventRepository, this.mFailedItemRepository).sync(this.mEnrollmentRepository.getEnrollmentsByTrackedEntityInstanceId(localId));
    }

    private boolean existsRelationships(TrackedEntityInstance trackedEntityInstance) {
        return trackedEntityInstance.getRelationships() != null && trackedEntityInstance.getRelationships().size() > 0;
    }

    private boolean existsRelationships(List<TrackedEntityInstance> trackedEntityInstances) {
        for (TrackedEntityInstance tei : trackedEntityInstances) {
            if (existsRelationships(tei)) {
                return true;
            }
        }
        return false;
    }
}
