package org.icddrb.dhis.android.eregistry.fragments.upcomingevents;

import android.content.Context;
import com.raizlabs.android.dbflow.sql.language.Select;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.icddrb.dhis.android.eregistry.C0773R;
import org.icddrb.dhis.android.eregistry.ui.rows.upcomingevents.UpcomingEventItemRow;
import org.icddrb.dhis.android.eregistry.ui.rows.upcomingevents.UpcomingEventsColumnNamesRow;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.controllers.tracker.TrackerController;
import org.icddrb.dhis.android.sdk.persistence.loaders.Query;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.Option;
import org.icddrb.dhis.android.sdk.persistence.models.Program;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramTrackedEntityAttribute;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.events.EventRow;
import org.icddrb.dhis.android.sdk.ui.dialogs.UpcomingEventsDialogFilter.Type;
import org.icddrb.dhis.android.sdk.ui.fragments.selectprogram.SelectProgramFragmentForm;

class UpcomingEventsFragmentQuery implements Query<SelectProgramFragmentForm> {
    private final String mEndDate;
    private final String mFilterLabel;
    private final int mNumAttributesToShow = 2;
    private final String mOrgUnitId;
    private final String mProgramId;
    private final String mStartDate;

    public UpcomingEventsFragmentQuery(String orgUnitId, String programId, String filterLabel, String startDate, String endDate) {
        this.mOrgUnitId = orgUnitId;
        this.mProgramId = programId;
        this.mFilterLabel = filterLabel;
        this.mStartDate = startDate;
        this.mEndDate = endDate;
    }

    public SelectProgramFragmentForm query(Context context) {
        List<EventRow> eventUpcomingEventRows = new ArrayList();
        SelectProgramFragmentForm fragmentForm = new SelectProgramFragmentForm();
        Program selectedProgram = MetaDataController.getProgram(this.mProgramId);
        if (!(selectedProgram == null || isListEmpty(selectedProgram.getProgramStages()))) {
            List<ProgramTrackedEntityAttribute> programTrackedEntityAttributes = selectedProgram.getProgramTrackedEntityAttributes();
            List<String> attributesToShow = new ArrayList();
            UpcomingEventsColumnNamesRow columnNames = new UpcomingEventsColumnNamesRow();
            String title = context.getString(C0773R.string.events);
            if (this.mFilterLabel != null) {
                title = getLabelTitle(this.mFilterLabel, context);
            }
            columnNames.setTitle(title);
            for (ProgramTrackedEntityAttribute attribute : programTrackedEntityAttributes) {
                if (attribute.getDisplayInList() && attributesToShow.size() < 2) {
                    attributesToShow.add(attribute.getTrackedEntityAttributeId());
                    if (attribute.getTrackedEntityAttribute() != null) {
                        String name = attribute.getTrackedEntityAttribute().getName();
                        if (attributesToShow.size() == 1) {
                            columnNames.setFirstItem(name);
                        } else if (attributesToShow.size() == 2) {
                            columnNames.setSecondItem(name);
                        }
                    }
                }
            }
            eventUpcomingEventRows.add(columnNames);
            List<Event> events = new ArrayList();
            if (Type.UPCOMING.toString().equalsIgnoreCase(this.mFilterLabel)) {
                events = TrackerController.getScheduledEventsWithActiveEnrollments(this.mProgramId, this.mOrgUnitId, this.mStartDate, this.mEndDate);
            } else if (Type.OVERDUE.toString().equalsIgnoreCase(this.mFilterLabel)) {
                events = TrackerController.getOverdueEventsWithActiveEnrollments(this.mProgramId, this.mOrgUnitId);
            } else if (Type.ACTIVE.toString().equalsIgnoreCase(this.mFilterLabel)) {
                events = TrackerController.getActiveEventsWithActiveEnrollments(this.mProgramId, this.mOrgUnitId, this.mStartDate, this.mEndDate);
            }
            List<Option> options = new Select().from(Option.class).queryList();
            Map<String, String> codeToName = new HashMap();
            for (Option option : options) {
                codeToName.put(option.getCode(), option.getName());
            }
            for (Event event : events) {
                eventUpcomingEventRows.add(createEventItem(event, attributesToShow, codeToName));
            }
            fragmentForm.setEventRowList(eventUpcomingEventRows);
            fragmentForm.setProgram(selectedProgram);
        }
        return fragmentForm;
    }

    private String getLabelTitle(String filterLabel, Context context) {
        if (Type.UPCOMING.toString().equalsIgnoreCase(this.mFilterLabel)) {
            return context.getString(C0773R.string.upcoming_events);
        }
        if (Type.OVERDUE.toString().equalsIgnoreCase(this.mFilterLabel)) {
            return context.getString(C0773R.string.overdue_events);
        }
        if (Type.ACTIVE.toString().equalsIgnoreCase(this.mFilterLabel)) {
            return context.getString(C0773R.string.active_events);
        }
        return "";
    }

    private UpcomingEventItemRow createEventItem(Event event, List<String> attributesToShow, Map<String, String> codeToName) {
        UpcomingEventItemRow eventItem = new UpcomingEventItemRow();
        eventItem.setEventId(event.getLocalId());
        eventItem.setDueDate(event.getDueDate());
        eventItem.setEventName(MetaDataController.getProgramStage(event.getProgramStageId()).getName());
        int i = 0;
        while (i < 2 && i < attributesToShow.size()) {
            String trackedEntityAttribute = (String) attributesToShow.get(i);
            if (trackedEntityAttribute != null) {
                TrackedEntityAttributeValue trackedEntityAttributeValue = TrackerController.getTrackedEntityAttributeValue(trackedEntityAttribute, event.getTrackedEntityInstance());
                if (trackedEntityAttributeValue != null) {
                    String code = trackedEntityAttributeValue.getValue();
                    String name = codeToName.get(code) == null ? code : (String) codeToName.get(code);
                    if (i == 0) {
                        eventItem.setFirstItem(name);
                    } else if (i == 1) {
                        eventItem.setSecondItem(name);
                    }
                }
            }
            i++;
        }
        return eventItem;
    }

    private static <T> boolean isListEmpty(List<T> items) {
        return items == null || items.isEmpty();
    }
}
