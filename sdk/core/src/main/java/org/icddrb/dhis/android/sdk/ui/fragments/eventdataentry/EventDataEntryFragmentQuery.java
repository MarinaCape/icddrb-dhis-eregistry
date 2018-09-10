package org.icddrb.dhis.android.sdk.ui.fragments.eventdataentry;

import android.content.Context;
import android.text.TextUtils;
import com.crashlytics.android.Crashlytics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.icddrb.dhis.android.sdk.R;
import org.icddrb.dhis.android.sdk.controllers.DhisController;
import org.icddrb.dhis.android.sdk.controllers.GpsController;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.controllers.tracker.TrackerController;
import org.icddrb.dhis.android.sdk.persistence.loaders.Query;
import org.icddrb.dhis.android.sdk.persistence.models.DataElement;
import org.icddrb.dhis.android.sdk.persistence.models.DataValue;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramIndicator;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramStage;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramStageDataElement;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramStageSection;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.DataEntryRowFactory;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.EventCoordinatesRow;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.EventDatePickerRow;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.EventDueDatePickerRow;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.IndicatorRow;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.Row;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.StatusRow;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.DataEntryFragmentSection;
import org.icddrb.dhis.android.sdk.utils.api.ValueType;
import org.icddrb.dhis.android.sdk.utils.services.ProgramIndicatorService;
import org.icddrb.dhis.android.sdk.utils.support.DateUtils;
import org.joda.time.LocalDate;

class EventDataEntryFragmentQuery implements Query<EventDataEntryFragmentForm> {
    private static final String CLASS_TAG = EventDataEntryFragmentQuery.class.getSimpleName();
    private static final String DEFAULT_SECTION = "defaultSection";
    private static final String EMPTY_FIELD = "";
    private final long enrollmentId;
    private final long eventId;
    private final String orgUnitId;
    private final String programId;
    private final String programStageId;

    EventDataEntryFragmentQuery(String orgUnitId, String programId, String programStageId, long eventId, long enrollmentId) {
        this.orgUnitId = orgUnitId;
        this.programId = programId;
        this.programStageId = programStageId;
        this.eventId = eventId;
        this.enrollmentId = enrollmentId;
    }

    public EventDataEntryFragmentForm query(Context context) {
        ProgramStage stage = MetaDataController.getProgramStage(this.programStageId);
        EventDataEntryFragmentForm form = new EventDataEntryFragmentForm();
        if (!(stage == null || stage.getProgramStageSections() == null)) {
            if (DhisController.getInstance().getSession() == null) {
                Crashlytics.log("Null session. ProgramStageId: " + this.programStageId);
                Crashlytics.logException(new Exception("Null session exception"));
                DhisController.getInstance().init();
            }
            String username = DhisController.getInstance().getSession().getCredentials().getUsername();
            Event event = getEvent(this.orgUnitId, this.programId, this.eventId, this.enrollmentId, stage, username);
            form.setEvent(event);
            if (this.enrollmentId > 0) {
                Enrollment enrollment = TrackerController.getEnrollment(this.enrollmentId);
                enrollment.getEvents(true);
                List<Event> newEventsForEnrollment = new ArrayList();
                List<Event> currentEventsForEnrollment = new ArrayList();
                currentEventsForEnrollment.addAll(enrollment.getEvents());
                newEventsForEnrollment.addAll(enrollment.getEvents());
                for (Event eventForEnrollment : currentEventsForEnrollment) {
                    if (eventForEnrollment.getLocalId() == event.getLocalId()) {
                        int index = newEventsForEnrollment.indexOf(eventForEnrollment);
                        newEventsForEnrollment.remove(eventForEnrollment);
                        newEventsForEnrollment.add(index, eventForEnrollment);
                    }
                }
                enrollment.setEvents(newEventsForEnrollment);
                form.setEnrollment(enrollment);
            }
            form.setStage(stage);
            form.setSections(new ArrayList());
            form.setDataElementNames(new HashMap());
            form.setDataValues(new HashMap());
            form.setTrackedEntityAttributeValues(new HashMap());
            form.setIndicatorRows(new ArrayList());
            form.setIndicatorToIndicatorRowMap(new HashMap());
            List<Row> rows;
            if (stage.getProgramStageSections() == null || stage.getProgramStageSections().isEmpty()) {
                rows = new ArrayList();
                addStatusRow(context, form, rows);
                if (form.getEnrollment() == null || !form.getStage().isHideDueDate()) {
                    addEventDateRow(context, form, rows);
                    addCoordinateRow(form, rows);
                    populateDataEntryRows(form, stage.getProgramStageDataElements(), rows, username, context);
                    populateIndicatorRows(form, stage.getProgramIndicators(), rows);
                    form.getSections().add(new DataEntryFragmentSection(DEFAULT_SECTION, null, rows));
                } else {
                    addEventDateRow(context, form, rows);
                    addCoordinateRow(form, rows);
                    populateDataEntryRows(form, stage.getProgramStageDataElements(), rows, username, context);
                    populateIndicatorRows(form, stage.getProgramIndicators(), rows);
                    form.getSections().add(new DataEntryFragmentSection(DEFAULT_SECTION, null, rows));
                }
            } else {
                for (int i = 0; i < stage.getProgramStageSections().size(); i++) {
                    ProgramStageSection section = (ProgramStageSection) stage.getProgramStageSections().get(i);
                    if (section.getProgramStageDataElements() != null) {
                        rows = new ArrayList();
                        if (i == 0) {
                            addStatusRow(context, form, rows);
                            if (form.getEnrollment() != null) {
                                addEventDateRow(context, form, rows);
                                addCoordinateRow(form, rows);
                            } else {
                                addEventDateRow(context, form, rows);
                                addCoordinateRow(form, rows);
                            }
                        }
                        populateDataEntryRows(form, section.getProgramStageDataElements(), rows, username, context);
                        populateIndicatorRows(form, section.getProgramIndicators(), rows);
                        form.getSections().add(new DataEntryFragmentSection(section.getName(), section.getUid(), rows));
                    }
                }
            }
        }
        return form;
    }

    private static void populateTrackedEntityDataValues(EventDataEntryFragmentForm form) {
        if (form.getEnrollment() != null && !TextUtils.isEmpty(form.getEnrollment().getTrackedEntityInstance())) {
            TrackedEntityInstance trackedEntityInstance = TrackerController.getTrackedEntityInstance(form.getEnrollment().getTrackedEntityInstance());
            if (trackedEntityInstance != null) {
                List<TrackedEntityAttributeValue> trackedEntityAttributeValues = trackedEntityInstance.getAttributes();
                if (trackedEntityAttributeValues != null) {
                    for (TrackedEntityAttributeValue trackedEntityAttributeValue : trackedEntityAttributeValues) {
                        form.getTrackedEntityAttributeValues().put(trackedEntityAttributeValue.getTrackedEntityAttributeId(), trackedEntityAttributeValue);
                    }
                }
            }
        }
    }

    private static void addStatusRow(Context context, EventDataEntryFragmentForm form, List<Row> rows) {
        Event event = form.getEvent();
        if (event != null) {
            StatusRow row = new StatusRow(context, event, form.getStage());
            rows.add(row);
            form.setStatusRow(row);
        }
    }

    private static void addDueDateRow(Context context, EventDataEntryFragmentForm form, List<Row> rows) {
        rows.add(new EventDueDatePickerRow(context.getString(R.string.duedate), form.getEvent(), true));
    }

    private static void addEventDateRow(Context context, EventDataEntryFragmentForm form, List<Row> rows) {
        rows.add(new EventDatePickerRow(form.getStage().getExecutionDateLabel() == null ? context.getString(R.string.report_date) : form.getStage().getExecutionDateLabel(), form.getEvent(), false));
    }

    private static void addCoordinateRow(EventDataEntryFragmentForm form, List<Row> rows) {
        if (form.getStage() != null && form.getStage().getCaptureCoordinates()) {
            rows.add(new EventCoordinatesRow(form.getEvent()));
        }
    }

    private static void populateDataEntryRows(EventDataEntryFragmentForm form, List<ProgramStageDataElement> dataElements, List<Row> rows, String username, Context context) {
        for (ProgramStageDataElement stageDataElement : dataElements) {
            DataValue dataValue = getDataValue(stageDataElement.getDataelement(), form.getEvent(), username);
            DataElement dataElement = MetaDataController.getDataElement(stageDataElement.getDataelement());
            if (dataElement != null) {
                String dataElementName;
                form.getDataElementNames().put(stageDataElement.getDataelement(), dataElement.getDisplayName());
                form.getDataValues().put(dataValue.getDataElement(), dataValue);
                if (TextUtils.isEmpty(dataElement.getDisplayFormName())) {
                    dataElementName = dataElement.getDisplayName();
                } else {
                    dataElementName = dataElement.getDisplayFormName();
                }
                if (ValueType.COORDINATE.equals(stageDataElement.getDataElement().getValueType())) {
                    GpsController.activateGps(context);
                }
                boolean isRadioButton = form.getStage().getProgram().getDataEntryMethod();
                if (!isRadioButton) {
                    isRadioButton = stageDataElement.isRenderOptionsAsRadio();
                }
                rows.add(DataEntryRowFactory.createDataEntryView(stageDataElement.getCompulsory(), stageDataElement.getAllowFutureDate(), dataElement.getOptionSet(), dataElementName, dataValue, dataElement.getValueType(), true, false, isRadioButton, context));
            }
        }
    }

    private static void populateIndicatorRows(EventDataEntryFragmentForm form, List<ProgramIndicator> indicators, List<Row> rows) {
        for (ProgramIndicator programIndicator : indicators) {
            IndicatorRow indicatorRow = (IndicatorRow) form.getIndicatorToIndicatorRowMap().get(programIndicator.getUid());
            if (indicatorRow == null) {
                indicatorRow = new IndicatorRow(programIndicator, ProgramIndicatorService.getProgramIndicatorValue(form.getEvent(), programIndicator), programIndicator.getDisplayDescription());
                form.getIndicatorToIndicatorRowMap().put(programIndicator.getUid(), indicatorRow);
                form.getIndicatorRows().add(indicatorRow);
            }
            rows.add(indicatorRow);
        }
    }

    private Event getEvent(String orgUnitId, String programId, long eventId, long enrollmentId, ProgramStage programStage, String username) {
        Event event;
        if (eventId < 0) {
            event = new Event(orgUnitId, "ACTIVE", programId, programStage, null, null, null);
            if (enrollmentId > 0) {
                Enrollment enrollment = TrackerController.getEnrollment(enrollmentId);
                if (enrollment != null) {
                    event.setLocalEnrollmentId(enrollmentId);
                    event.setEnrollment(enrollment.getEnrollment());
                    event.setTrackedEntityInstance(enrollment.getTrackedEntityInstance());
                    event.setDueDate(new LocalDate(DateUtils.parseDate(enrollment.getEnrollmentDate())).plusDays(programStage.getMinDaysFromStart()).toString());
                }
            }
            List<DataValue> dataValues = new ArrayList();
            for (ProgramStageDataElement dataElement : programStage.getProgramStageDataElements()) {
                dataValues.add(new DataValue(event, "", dataElement.getDataelement(), false, username));
            }
            event.setDataValues(dataValues);
        } else {
            event = TrackerController.getEvent(eventId);
            if (event == null) {
                getEvent(orgUnitId, programId, -1, enrollmentId, programStage, username);
            }
        }
        return event;
    }

    public static DataValue getDataValue(String dataElement, Event event, String username) {
        for (DataValue dataValue : event.getDataValues()) {
            if (dataValue.getDataElement().equals(dataElement)) {
                return dataValue;
            }
        }
        DataValue dataValue2 = new DataValue(event, "", dataElement, false, username);
        event.getDataValues().add(dataValue2);
        return dataValue2;
    }
}
