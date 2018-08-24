package org.icddrb.dhis.android.eregistry.fragments.programoverview;

import android.content.Context;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.icddrb.dhis.android.eregistry.ui.rows.programoverview.ProgramStageEventRow;
import org.icddrb.dhis.android.eregistry.ui.rows.programoverview.ProgramStageLabelRow;
import org.icddrb.dhis.android.eregistry.ui.rows.programoverview.ProgramStageRow;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.controllers.tracker.TrackerController;
import org.icddrb.dhis.android.sdk.persistence.loaders.Query;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.Program;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramIndicator;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramStage;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttribute;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.persistence.models.UnionFWA;
import org.icddrb.dhis.android.sdk.persistence.models.UserAccount;
import org.icddrb.dhis.android.sdk.persistence.preferences.AppPreferences;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.IndicatorRow;
import org.icddrb.dhis.android.sdk.utils.Utils;
import org.icddrb.dhis.android.sdk.utils.comparators.EventDateComparator;
import org.icddrb.dhis.android.sdk.utils.services.ProgramIndicatorService;

class ProgramOverviewFragmentQuery implements Query<ProgramOverviewFragmentForm> {
    public static final String CLASS_TAG = ProgramOverviewFragmentQuery.class.getSimpleName();
    private final String mProgramId;
    private final long mTrackedEntityInstanceId;

    public ProgramOverviewFragmentQuery(String programId, long trackedEntityInstanceId) {
        this.mProgramId = programId;
        this.mTrackedEntityInstanceId = trackedEntityInstanceId;
    }

    public ProgramOverviewFragmentForm query(Context context) {
        ProgramOverviewFragmentForm programOverviewFragmentForm = new ProgramOverviewFragmentForm();
        programOverviewFragmentForm.setProgramIndicatorRows(new LinkedHashMap());
        Program program = MetaDataController.getProgram(this.mProgramId);
        TrackedEntityInstance trackedEntityInstance = TrackerController.getTrackedEntityInstance(this.mTrackedEntityInstanceId);
        programOverviewFragmentForm.setProgram(program);
        programOverviewFragmentForm.setTrackedEntityInstance(trackedEntityInstance);
        programOverviewFragmentForm.setDateOfEnrollmentLabel(program.getEnrollmentDateLabel());
        programOverviewFragmentForm.setIncidentDateLabel(program.getIncidentDateLabel());
        if (trackedEntityInstance != null) {
            List<Enrollment> enrollments = TrackerController.getEnrollments(this.mProgramId, trackedEntityInstance);
            Enrollment activeEnrollment = null;
            if (enrollments != null) {
                for (Enrollment enrollment : enrollments) {
                    if (enrollment.getStatus().equals("ACTIVE")) {
                        activeEnrollment = enrollment;
                    }
                }
            }
            if (activeEnrollment != null) {
                programOverviewFragmentForm.setEnrollment(activeEnrollment);
                programOverviewFragmentForm.setDateOfEnrollmentValue(Utils.removeTimeFromDateString(activeEnrollment.getEnrollmentDate()));
                programOverviewFragmentForm.setIncidentDateValue(Utils.removeTimeFromDateString(activeEnrollment.getIncidentDate()));
                List<TrackedEntityAttributeValue> trackedEntityAttributeValues = TrackerController.getTrackedEntityAttributeValues(trackedEntityInstance.getLocalId());
                if (trackedEntityAttributeValues != null) {
                    UnionFWA userList = new AppPreferences(context).getDropdownInfo();
                    for (TrackedEntityAttributeValue a : trackedEntityAttributeValues) {
                        TrackedEntityAttribute e = MetaDataController.getTrackedEntityAttribute(a.getTrackedEntityAttributeId());
                        if (e != null) {
                            String name = e.getName();
                            Object obj = -1;
                            switch (name.hashCode()) {
                                case -1751220544:
                                    if (name.equals("Village name (English)")) {
                                        obj = 2;
                                        break;
                                    }
                                    break;
                                case -761051333:
                                    if (name.equals("FWA Name")) {
                                        obj = 1;
                                        break;
                                    }
                                    break;
                                case 268609884:
                                    if (name.equals("Full name")) {
                                        obj = null;
                                        break;
                                    }
                                    break;
                            }
                            switch (obj) {
                                case null:
                                    programOverviewFragmentForm.setAttribute1Label(e.getDisplayName());
                                    programOverviewFragmentForm.setAttribute1Value(a.getValue());
                                    break;
                                case 1:
                                    String fwaName = userList.getFullName(a.getValue());
                                    programOverviewFragmentForm.setAttribute2Label(e.getDisplayName());
                                    if (fwaName == null) {
                                        fwaName = a.getValue();
                                    }
                                    programOverviewFragmentForm.setAttribute2Value(fwaName);
                                    break;
                                case 2:
                                    programOverviewFragmentForm.setAttribute3Label(e.getDisplayName());
                                    programOverviewFragmentForm.setAttribute3Value(a.getValue());
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
                programOverviewFragmentForm.setProgramStageRows(getProgramStageRows(activeEnrollment));
                List<ProgramIndicator> programIndicators = programOverviewFragmentForm.getProgram().getProgramIndicators();
                if (programIndicators != null) {
                    for (ProgramIndicator programIndicator : programIndicators) {
                        if (programIndicator.isDisplayInForm()) {
                            String value = ProgramIndicatorService.getProgramIndicatorValue(programOverviewFragmentForm.getEnrollment(), programIndicator);
                            if (value != null) {
                                programOverviewFragmentForm.getProgramIndicatorRows().put(programIndicator, new IndicatorRow(programIndicator, value, programIndicator.getDisplayDescription()));
                            }
                        }
                    }
                } else {
                    programOverviewFragmentForm.getProgramIndicatorRows().clear();
                }
            }
        }
        return programOverviewFragmentForm;
    }

    private boolean canAddProgramStage(ProgramStage ps) {
        UserAccount userAccount = MetaDataController.getUserAccount();
        if (userAccount.getUserGroups() == null || userAccount == null || !ps.userHasAccess(userAccount.getUserGroups())) {
            return false;
        }
        return true;
    }

    private List<ProgramStageRow> getProgramStageRows(Enrollment enrollment) {
        List<ProgramStageRow> rows = new ArrayList();
        List<Event> events = enrollment.getEvents(true);
        HashMap<String, List<Event>> eventsByStage = new HashMap();
        for (Event event : events) {
            List<Event> eventsForStage = (List) eventsByStage.get(event.getProgramStageId());
            if (eventsForStage == null) {
                eventsForStage = new ArrayList();
                eventsByStage.put(event.getProgramStageId(), eventsForStage);
            }
            eventsForStage.add(event);
        }
        for (ProgramStage programStage : MetaDataController.getProgram(this.mProgramId).getProgramStages()) {
            eventsForStage = (List) eventsByStage.get(programStage.getUid());
            ProgramStageLabelRow labelRow = new ProgramStageLabelRow(programStage);
            if (canAddProgramStage(programStage)) {
                rows.add(labelRow);
            }
            if (eventsForStage != null) {
                Collections.sort(eventsForStage, new EventDateComparator());
                for (Event event2 : eventsForStage) {
                    ProgramStageEventRow row = new ProgramStageEventRow(event2);
                    row.setLabelRow(labelRow);
                    labelRow.getEventRows().add(row);
                    rows.add(row);
                }
            }
        }
        return rows;
    }

    private static <T> boolean isListEmpty(List<T> items) {
        return items == null || items.isEmpty();
    }
}
