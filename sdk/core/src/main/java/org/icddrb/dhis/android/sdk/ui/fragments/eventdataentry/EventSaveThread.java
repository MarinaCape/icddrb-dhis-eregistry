package org.icddrb.dhis.android.sdk.ui.fragments.eventdataentry;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.icddrb.dhis.android.sdk.controllers.tracker.TrackerController;
import org.icddrb.dhis.android.sdk.persistence.models.DataValue;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.AsyncHelperThread;

public class EventSaveThread extends AsyncHelperThread {
    private EventDataEntryFragment dataEntryFragment;
    private HashMap<String, DataValue> dataValues = new HashMap();
    private Event event;
    private ConcurrentLinkedQueue<String> queuedDataValues = new ConcurrentLinkedQueue();
    private boolean saveEvent = false;

    public void init(EventDataEntryFragment dataEntryFragment) {
        setDataEntryFragment(dataEntryFragment);
    }

    public void setDataEntryFragment(EventDataEntryFragment dataEntryFragment) {
        this.dataEntryFragment = dataEntryFragment;
    }

    public void setEvent(Event event) {
        if (event != null) {
            this.event = event;
            if (event.getDataValues() != null) {
                for (DataValue dataValue : event.getDataValues()) {
                    this.dataValues.put(dataValue.getDataElement(), dataValue);
                }
            }
        }
    }

    protected void work() {
        if (this.dataEntryFragment != null && this.event != null) {
            if (this.event.getLocalId() < 0) {
                this.saveEvent = true;
            }
            while (this.saveEvent) {
                saveEvent();
            }
            boolean invalidateEvent = false;
            while (!this.queuedDataValues.isEmpty()) {
                saveDataValue();
                invalidateEvent = true;
            }
            if (invalidateEvent) {
                saveEvent();
            }
            this.dataEntryFragment.save();
        }
    }

    private void saveEvent() {
        if (this.event != null) {
            this.saveEvent = false;
            this.event.setFromServer(false);
            Enrollment enrollment = TrackerController.getEnrollment(this.event.getEnrollment());
            enrollment.setFromServer(false);
            enrollment.save();
            TrackedEntityInstance trackedEntityInstance = TrackerController.getTrackedEntityInstance(this.event.getTrackedEntityInstance());
            trackedEntityInstance.setFromServer(false);
            trackedEntityInstance.save();
            Event tempEvent = new Event();
            tempEvent.setLocalId(this.event.getLocalId());
            tempEvent.setEvent(this.event.getEvent());
            tempEvent.setStatus(this.event.getStatus());
            tempEvent.setLatitude(this.event.getLatitude());
            tempEvent.setLongitude(this.event.getLongitude());
            tempEvent.setTrackedEntityInstance(this.event.getTrackedEntityInstance());
            tempEvent.setLocalEnrollmentId(this.event.getLocalEnrollmentId());
            tempEvent.setEnrollment(this.event.getEnrollment());
            tempEvent.setProgramId(this.event.getProgramId());
            tempEvent.setProgramStageId(this.event.getProgramStageId());
            tempEvent.setOrganisationUnitId(this.event.getOrganisationUnitId());
            tempEvent.setEventDate(this.event.getEventDate());
            tempEvent.setDueDate(this.event.getDueDate());
            tempEvent.setFromServer(this.event.isFromServer());
            tempEvent.setUid(this.event.getUid());
            tempEvent.setName(this.event.getName());
            tempEvent.setDisplayName(this.event.getDisplayName());
            tempEvent.setCreated(this.event.getCreated());
            tempEvent.setLastUpdated(this.event.getLastUpdated());
            tempEvent.setAccess(this.event.getAccess());
            tempEvent.save();
            this.event.setLocalId(tempEvent.getLocalId());
        }
    }

    private void saveDataValue() {
        if (this.event != null) {
            DataValue dataValue = (DataValue) this.dataValues.get((String) this.queuedDataValues.poll());
            if (dataValue != null) {
                dataValue.setLocalEventId(this.event.getLocalId());
                dataValue.save();
            }
        }
    }

    public void scheduleSaveEvent() {
        this.saveEvent = true;
        super.schedule();
    }

    public void scheduleSaveDataValue(String dataValueDataElement) {
        if (!this.queuedDataValues.contains(dataValueDataElement)) {
            this.queuedDataValues.add(dataValueDataElement);
        }
        super.schedule();
    }

    public void kill() {
        super.kill();
        this.dataEntryFragment = null;
        this.event = null;
        if (this.dataValues != null) {
            this.dataValues.clear();
            this.dataValues = null;
        }
    }
}
