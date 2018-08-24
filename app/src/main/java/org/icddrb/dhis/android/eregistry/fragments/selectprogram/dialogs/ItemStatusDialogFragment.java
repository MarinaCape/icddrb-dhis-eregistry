package org.icddrb.dhis.android.eregistry.fragments.selectprogram.dialogs;

import android.os.Bundle;
import org.icddrb.dhis.android.sdk.controllers.DhisController;
import org.icddrb.dhis.android.sdk.job.JobExecutor;
import org.icddrb.dhis.android.sdk.job.NetworkJob;
import org.icddrb.dhis.android.sdk.persistence.models.BaseSerializableModel;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.persistence.preferences.ResourceType;
import org.icddrb.dhis.android.sdk.synchronization.data.enrollment.EnrollmentLocalDataSource;
import org.icddrb.dhis.android.sdk.synchronization.data.enrollment.EnrollmentRemoteDataSource;
import org.icddrb.dhis.android.sdk.synchronization.data.enrollment.EnrollmentRepository;
import org.icddrb.dhis.android.sdk.synchronization.data.event.EventLocalDataSource;
import org.icddrb.dhis.android.sdk.synchronization.data.event.EventRemoteDataSource;
import org.icddrb.dhis.android.sdk.synchronization.data.event.EventRepository;
import org.icddrb.dhis.android.sdk.synchronization.data.faileditem.FailedItemRepository;
import org.icddrb.dhis.android.sdk.synchronization.data.trackedentityinstance.TrackedEntityInstanceLocalDataSource;
import org.icddrb.dhis.android.sdk.synchronization.data.trackedentityinstance.TrackedEntityInstanceRemoteDataSource;
import org.icddrb.dhis.android.sdk.synchronization.data.trackedentityinstance.TrackedEntityInstanceRepository;
import org.icddrb.dhis.android.sdk.synchronization.domain.enrollment.SyncEnrollmentUseCase;
import org.icddrb.dhis.android.sdk.synchronization.domain.trackedentityinstance.SyncTrackedEntityInstanceUseCase;

public class ItemStatusDialogFragment extends org.icddrb.dhis.android.sdk.ui.dialogs.ItemStatusDialogFragment {
    public static ItemStatusDialogFragment newInstance(BaseSerializableModel item) {
        ItemStatusDialogFragment dialogFragment = new ItemStatusDialogFragment();
        Bundle args = new Bundle();
        args.putLong(org.icddrb.dhis.android.sdk.ui.dialogs.ItemStatusDialogFragment.EXTRA_ID, item.getLocalId());
        if (item instanceof TrackedEntityInstance) {
            args.putString(org.icddrb.dhis.android.sdk.ui.dialogs.ItemStatusDialogFragment.EXTRA_TYPE, "TrackedEntityInstance");
        } else if (item instanceof Enrollment) {
            args.putString(org.icddrb.dhis.android.sdk.ui.dialogs.ItemStatusDialogFragment.EXTRA_TYPE, "Enrollment");
        } else if (item instanceof Event) {
            args.putString(org.icddrb.dhis.android.sdk.ui.dialogs.ItemStatusDialogFragment.EXTRA_TYPE, "Event");
        }
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    public void sendToServer(BaseSerializableModel item, org.icddrb.dhis.android.sdk.ui.dialogs.ItemStatusDialogFragment fragment) {
        if (item instanceof TrackedEntityInstance) {
            System.out.println("Norway - item: TrackedEntityInstance");
            sendTrackedEntityInstance((TrackedEntityInstance) item);
        } else if (item instanceof Enrollment) {
            System.out.println("Norway - item: Enrollment");
            sendEnrollment((Enrollment) item);
        } else if (item instanceof Event) {
            System.out.println("Norway - item: event");
            org.icddrb.dhis.android.sdk.ui.dialogs.ItemStatusDialogFragment.sendEvent((Event) item);
        }
    }

    public static void sendTrackedEntityInstance(final TrackedEntityInstance trackedEntityInstance) {
        JobExecutor.enqueueJob(new NetworkJob<Object>(0, ResourceType.TRACKEDENTITYINSTANCE) {
            public Object execute() {
                new SyncTrackedEntityInstanceUseCase(new TrackedEntityInstanceRepository(new TrackedEntityInstanceLocalDataSource(), new TrackedEntityInstanceRemoteDataSource(DhisController.getInstance().getDhisApi())), new EnrollmentRepository(new EnrollmentLocalDataSource(), new EnrollmentRemoteDataSource(DhisController.getInstance().getDhisApi())), new EventRepository(new EventLocalDataSource(), new EventRemoteDataSource(DhisController.getInstance().getDhisApi())), new FailedItemRepository()).execute(trackedEntityInstance);
                return new Object();
            }
        });
    }

    public static void sendEnrollment(final Enrollment enrollment) {
        JobExecutor.enqueueJob(new NetworkJob<Object>(0, ResourceType.ENROLLMENT) {
            public Object execute() {
                EventLocalDataSource mLocalDataSource = new EventLocalDataSource();
                EventRemoteDataSource mRemoteDataSource = new EventRemoteDataSource(DhisController.getInstance().getDhisApi());
                new SyncEnrollmentUseCase(new EnrollmentRepository(new EnrollmentLocalDataSource(), new EnrollmentRemoteDataSource(DhisController.getInstance().getDhisApi())), new EventRepository(mLocalDataSource, mRemoteDataSource), new TrackedEntityInstanceRepository(new TrackedEntityInstanceLocalDataSource(), new TrackedEntityInstanceRemoteDataSource(DhisController.getInstance().getDhisApi())), new FailedItemRepository()).execute(enrollment);
                return new Object();
            }
        });
    }
}
