/*
 *  Copyright (c) 2016, University of Oslo
 *  * All rights reserved.
 *  *
 *  * Redistribution and use in source and binary forms, with or without
 *  * modification, are permitted provided that the following conditions are met:
 *  * Redistributions of source code must retain the above copyright notice, this
 *  * list of conditions and the following disclaimer.
 *  *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *  * this list of conditions and the following disclaimer in the documentation
 *  * and/or other materials provided with the distribution.
 *  * Neither the name of the HISP project nor the names of its contributors may
 *  * be used to endorse or promote products derived from this software without
 *  * specific prior written permission.
 *  *
 *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package org.icddrb.dhis.android.eregistry.fragments.selectprogram.dialogs;

import android.os.Bundle;

import org.icddrb.dhis.android.sdk.controllers.DhisController;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.controllers.tracker.TrackerController;
import org.icddrb.dhis.android.sdk.job.JobExecutor;
import org.icddrb.dhis.android.sdk.job.NetworkJob;
import org.icddrb.dhis.android.sdk.persistence.models.BaseSerializableModel;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.FailedItem;
import org.icddrb.dhis.android.sdk.persistence.models.OrganisationUnit;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.persistence.preferences.ResourceType;
import org.icddrb.dhis.android.sdk.synchronization.data.enrollment.EnrollmentLocalDataSource;
import org.icddrb.dhis.android.sdk.synchronization.data.enrollment.EnrollmentRemoteDataSource;
import org.icddrb.dhis.android.sdk.synchronization.data.enrollment.EnrollmentRepository;
import org.icddrb.dhis.android.sdk.synchronization.data.event.EventLocalDataSource;
import org.icddrb.dhis.android.sdk.synchronization.data.event.EventRemoteDataSource;
import org.icddrb.dhis.android.sdk.synchronization.data.event.EventRepository;
import org.icddrb.dhis.android.sdk.synchronization.data.faileditem.FailedItemRepository;
import org.icddrb.dhis.android.sdk.synchronization.data.trackedentityinstance
        .TrackedEntityInstanceLocalDataSource;
import org.icddrb.dhis.android.sdk.synchronization.data.trackedentityinstance
        .TrackedEntityInstanceRemoteDataSource;
import org.icddrb.dhis.android.sdk.synchronization.data.trackedentityinstance
        .TrackedEntityInstanceRepository;
import org.icddrb.dhis.android.sdk.synchronization.domain.enrollment.IEnrollmentRepository;
import org.icddrb.dhis.android.sdk.synchronization.domain.enrollment.SyncEnrollmentUseCase;
import org.icddrb.dhis.android.sdk.synchronization.domain.event.IEventRepository;
import org.icddrb.dhis.android.sdk.synchronization.domain.event.SyncEventUseCase;
import org.icddrb.dhis.android.sdk.synchronization.domain.trackedentityinstance
        .ITrackedEntityInstanceRepository;
import org.icddrb.dhis.android.sdk.synchronization.domain.trackedentityinstance
        .SyncTrackedEntityInstanceUseCase;

import java.util.List;

/**
 * Created by erling on 9/21/15.
 */
public class ItemStatusDialogFragment extends org.icddrb.dhis.android.sdk.ui.dialogs.ItemStatusDialogFragment
{
    private static int idEnrrollment = 0;
    public static ItemStatusDialogFragment newInstance(BaseSerializableModel item) {
        ItemStatusDialogFragment dialogFragment = new ItemStatusDialogFragment();
        Bundle args = new Bundle();

        args.putLong(EXTRA_ID, item.getLocalId());
        if(item instanceof TrackedEntityInstance) {
            args.putString(EXTRA_TYPE, FailedItem.TRACKEDENTITYINSTANCE);
        } else if (item instanceof Enrollment) {
            args.putString(EXTRA_TYPE, FailedItem.ENROLLMENT);
        }
        else if(item instanceof Event)
        {
            args.putString(EXTRA_TYPE, FailedItem.EVENT);
        }

        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @Override
    public void sendToServer(BaseSerializableModel item, org.icddrb.dhis.android.sdk.ui.dialogs.ItemStatusDialogFragment fragment) {


        if(item instanceof TrackedEntityInstance) {
            System.out.println("Norway - item: TrackedEntityInstance");

            TrackedEntityInstance trackedEntityInstance = (TrackedEntityInstance) item;
            sendTrackedEntityInstance(trackedEntityInstance);

        } else if(item instanceof Enrollment) {
            System.out.println("Norway - item: Enrollment");

            Enrollment enrollment = (Enrollment) item;
            sendEnrollment(enrollment);
        }
        else if(item instanceof Event)
        {
            System.out.println("Norway - item: event");

            Event event = (Event) item;
            sendEvent(event);
        }
    }
    public static void sendTrackedEntityInstance(final TrackedEntityInstance trackedEntityInstance) {
        JobExecutor.enqueueJob(new NetworkJob<Object>(1,
                ResourceType.TRACKEDENTITYINSTANCE) {
            @Override
            public Object execute() {
                EventLocalDataSource mLocalDataSource = new EventLocalDataSource();
                EventRemoteDataSource mRemoteDataSource = new EventRemoteDataSource(DhisController.getInstance().getDhisApi());
                EventRepository eventRepository = new EventRepository(mLocalDataSource, mRemoteDataSource);
                FailedItemRepository failedItemRepository = new FailedItemRepository();

                OrganisationUnit orgUnit = MetaDataController.getOrganisationUnit(trackedEntityInstance.getOrgUnit());
                if(orgUnit == null || orgUnit.getType() == OrganisationUnit.TYPE.SEARCH){
                    List<Enrollment> enrollments = TrackerController.getEnrollments(trackedEntityInstance);
                    for(Enrollment enrollment: enrollments){
                        sendEnrollment(enrollment);
                    }
                    return new Object();
                }

                EnrollmentLocalDataSource enrollmentLocalDataSource = new EnrollmentLocalDataSource();
                EnrollmentRemoteDataSource enrollmentRemoteDataSource = new EnrollmentRemoteDataSource(DhisController.getInstance().getDhisApi());
                IEnrollmentRepository enrollmentRepository = new EnrollmentRepository(enrollmentLocalDataSource, enrollmentRemoteDataSource);

                TrackedEntityInstanceLocalDataSource trackedEntityInstanceLocalDataSource = new TrackedEntityInstanceLocalDataSource();
                TrackedEntityInstanceRemoteDataSource trackedEntityInstanceRemoteDataSource = new TrackedEntityInstanceRemoteDataSource(DhisController.getInstance().getDhisApi());
                ITrackedEntityInstanceRepository trackedEntityInstanceRepository = new TrackedEntityInstanceRepository(trackedEntityInstanceLocalDataSource, trackedEntityInstanceRemoteDataSource);
                SyncTrackedEntityInstanceUseCase syncTrackedEntityInstanceUseCase = new SyncTrackedEntityInstanceUseCase(trackedEntityInstanceRepository, enrollmentRepository, eventRepository, failedItemRepository);
                syncTrackedEntityInstanceUseCase.execute(trackedEntityInstance);
                return new Object();
            }

        });
    }
    public static void sendEnrollment(final Enrollment enrollment) {
        do{
            idEnrrollment++;
        }while(JobExecutor.isJobRunning(idEnrrollment));

        JobExecutor.enqueueJob(new NetworkJob<Object>(idEnrrollment,
                ResourceType.ENROLLMENT) {

            @Override
            public Object execute()  {

                EventLocalDataSource mLocalDataSource = new EventLocalDataSource();
                EventRemoteDataSource mRemoteDataSource = new EventRemoteDataSource(DhisController.getInstance().getDhisApi());
                EnrollmentLocalDataSource enrollmentLocalDataSource = new EnrollmentLocalDataSource();

                EnrollmentRemoteDataSource enrollmentRemoteDataSource = new EnrollmentRemoteDataSource(DhisController.getInstance().getDhisApi());
                IEnrollmentRepository enrollmentRepository = new EnrollmentRepository(enrollmentLocalDataSource, enrollmentRemoteDataSource);
                IEventRepository eventRepository = new EventRepository(mLocalDataSource, mRemoteDataSource);

                TrackedEntityInstanceRemoteDataSource trackedEntityInstanceRemoteDataSource = new TrackedEntityInstanceRemoteDataSource(DhisController.getInstance().getDhisApi());
                TrackedEntityInstanceLocalDataSource trackedEntityInstanceLocalDataSource = new TrackedEntityInstanceLocalDataSource();
                TrackedEntityInstanceRepository trackedEntityInstanceRepository = new TrackedEntityInstanceRepository(trackedEntityInstanceLocalDataSource, trackedEntityInstanceRemoteDataSource);

                FailedItemRepository  failedItemRepository = new FailedItemRepository ();
                OrganisationUnit orgUnit = MetaDataController.getOrganisationUnit(enrollment.getOrgUnit());
                if(orgUnit == null || orgUnit.getType() == OrganisationUnit.TYPE.SEARCH){
                    List<Event> events = TrackerController.getEventsByEnrollment(enrollment.getEnrollment());
                    for(Event event: events){
                        sendEvent(event);
                    }
                    return new Object();
                }
                SyncEnrollmentUseCase enrollmentUseCase = new SyncEnrollmentUseCase(enrollmentRepository, eventRepository, trackedEntityInstanceRepository, failedItemRepository);
                enrollmentUseCase.execute(enrollment);
                return new Object();
            }
        });
    }
}
