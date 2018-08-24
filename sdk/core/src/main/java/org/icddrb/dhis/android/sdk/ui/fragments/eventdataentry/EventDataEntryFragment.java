package org.icddrb.dhis.android.sdk.ui.fragments.eventdataentry;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.raizlabs.android.dbflow.structure.Model;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.icddrb.dhis.android.sdk.C0845R;
import org.icddrb.dhis.android.sdk.controllers.ErrorType;
import org.icddrb.dhis.android.sdk.controllers.GpsController;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.controllers.tracker.TrackerController;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.loaders.DbLoader;
import org.icddrb.dhis.android.sdk.persistence.models.DataValue;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.Program;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramIndicator;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramRule;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramStage;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramStageDataElement;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.ui.adapters.SectionAdapter;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.DataEntryRowTypes;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.IndicatorRow;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.Row;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.RunProgramRulesEvent;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.events.OnCompleteEventClick;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.events.OnDetailedInfoButtonClick;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.DataEntryFragment;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.DataEntryFragmentSection;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.HideLoadingDialogEvent;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.RefreshListViewEvent;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.RowValueChangedEvent;
import org.icddrb.dhis.android.sdk.utils.UiUtils;
import org.icddrb.dhis.android.sdk.utils.comparators.EventDateComparator;
import org.icddrb.dhis.android.sdk.utils.services.ProgramIndicatorService;
import org.icddrb.dhis.android.sdk.utils.services.VariableService;
import org.joda.time.DateTime;

public class EventDataEntryFragment extends DataEntryFragment<EventDataEntryFragmentForm> {
    public static final String ENROLLMENT_ID = "extra:EnrollmentId";
    public static final String EVENT_ID = "extra:EventId";
    public static final String ORG_UNIT_ID = "extra:orgUnitId";
    public static final String PROGRAM_ID = "extra:ProgramId";
    public static final String PROGRAM_STAGE_ID = "extra:ProgramStageId";
    public static final String TAG = EventDataEntryFragment.class.getSimpleName();
    private EventDataEntryFragmentForm form;
    private IndicatorEvaluatorThread indicatorEvaluatorThread;
    private ImageView nextSectionButton;
    private ImageView previousSectionButton;
    private Map<String, List<ProgramIndicator>> programIndicatorsForDataElements;
    private Map<String, List<ProgramRule>> programRulesForDataElements;
    private EventSaveThread saveThread;
    private DateTime scheduledDueDate;
    private Spinner spinner;
    private SectionAdapter spinnerAdapter;
    private View spinnerContainer;

    /* renamed from: org.icddrb.dhis.android.sdk.ui.fragments.eventdataentry.EventDataEntryFragment$1 */
    class C09061 extends Thread {
        C09061() {
        }

        public void run() {
            EventDataEntryFragment.this.saveThread.kill();
            EventDataEntryFragment.this.indicatorEvaluatorThread.kill();
            EventDataEntryFragment.this.indicatorEvaluatorThread = null;
            EventDataEntryFragment.this.saveThread = null;
        }
    }

    /* renamed from: org.icddrb.dhis.android.sdk.ui.fragments.eventdataentry.EventDataEntryFragment$2 */
    class C09072 implements OnClickListener {
        C09072() {
        }

        public void onClick(View v) {
            int currentPosition = EventDataEntryFragment.this.spinner.getSelectedItemPosition();
            if (currentPosition - 1 >= 0) {
                EventDataEntryFragment.this.spinner.setSelection(currentPosition - 1);
            }
        }
    }

    /* renamed from: org.icddrb.dhis.android.sdk.ui.fragments.eventdataentry.EventDataEntryFragment$3 */
    class C09083 implements OnClickListener {
        C09083() {
        }

        public void onClick(View v) {
            int currentPosition = EventDataEntryFragment.this.spinner.getSelectedItemPosition();
            if (currentPosition + 1 < EventDataEntryFragment.this.spinnerAdapter.getCount()) {
                EventDataEntryFragment.this.spinner.setSelection(currentPosition + 1);
            }
        }
    }

    /* renamed from: org.icddrb.dhis.android.sdk.ui.fragments.eventdataentry.EventDataEntryFragment$5 */
    class C09115 implements DialogInterface.OnClickListener {
        C09115() {
        }

        public void onClick(DialogInterface dialog, int which) {
        }
    }

    /* renamed from: org.icddrb.dhis.android.sdk.ui.fragments.eventdataentry.EventDataEntryFragment$8 */
    class C09148 implements DialogInterface.OnClickListener {
        C09148() {
        }

        public void onClick(DialogInterface dialog, int which) {
            EventDataEntryFragment.this.goBackToPreviousActivity();
        }
    }

    /* renamed from: org.icddrb.dhis.android.sdk.ui.fragments.eventdataentry.EventDataEntryFragment$9 */
    class C09159 implements DialogInterface.OnClickListener {
        C09159() {
        }

        public void onClick(DialogInterface dialog, int which) {
            EventDataEntryFragment.this.removeInvalidFields();
            super.doBack();
        }
    }

    public EventDataEntryFragment() {
        setProgramRuleFragmentHelper(new EventDataEntryRuleHelper(this));
    }

    public static EventDataEntryFragment newInstance(String unitId, String programId, String programStageId) {
        EventDataEntryFragment fragment = new EventDataEntryFragment();
        Bundle args = new Bundle();
        args.putString("extra:orgUnitId", unitId);
        args.putString("extra:ProgramId", programId);
        args.putString(PROGRAM_STAGE_ID, programStageId);
        fragment.setArguments(args);
        return fragment;
    }

    public static EventDataEntryFragment newInstance(String unitId, String programId, String programStageId, long eventId) {
        EventDataEntryFragment fragment = new EventDataEntryFragment();
        Bundle args = new Bundle();
        args.putString("extra:orgUnitId", unitId);
        args.putString("extra:ProgramId", programId);
        args.putString(PROGRAM_STAGE_ID, programStageId);
        args.putLong(EVENT_ID, eventId);
        fragment.setArguments(args);
        return fragment;
    }

    public static EventDataEntryFragment newInstanceWithEnrollment(String unitId, String programId, String programStageId, long enrollmentId) {
        EventDataEntryFragment fragment = new EventDataEntryFragment();
        Bundle args = new Bundle();
        args.putString("extra:orgUnitId", unitId);
        args.putString("extra:ProgramId", programId);
        args.putString(PROGRAM_STAGE_ID, programStageId);
        args.putLong("extra:EnrollmentId", enrollmentId);
        fragment.setArguments(args);
        return fragment;
    }

    public static EventDataEntryFragment newInstanceWithEnrollment(String unitId, String programId, String programStageId, long enrollmentId, long eventId) {
        EventDataEntryFragment fragment = new EventDataEntryFragment();
        Bundle args = new Bundle();
        args.putString("extra:orgUnitId", unitId);
        args.putString("extra:ProgramId", programId);
        args.putString(PROGRAM_STAGE_ID, programStageId);
        args.putLong("extra:EnrollmentId", enrollmentId);
        args.putLong(EVENT_ID, eventId);
        fragment.setArguments(args);
        return fragment;
    }

    public void onDestroyView() {
        detachSpinner();
        super.onDestroyView();
    }

    public void onDetach() {
        GpsController.disableGps();
        super.onDetach();
    }

    private void detachSpinner() {
        if (isSpinnerAttached() && this.spinnerContainer != null) {
            ((ViewGroup) this.spinnerContainer.getParent()).removeView(this.spinnerContainer);
            this.spinnerContainer = null;
            this.spinner = null;
            if (this.spinnerAdapter != null) {
                this.spinnerAdapter.swapData(null);
                this.spinnerAdapter = null;
            }
        }
    }

    private boolean isSpinnerAttached() {
        return this.spinnerContainer != null;
    }

    public void onCreate(Bundle onSavedInstanceState) {
        super.onCreate(onSavedInstanceState);
        VariableService.reset();
        if (this.saveThread == null || this.saveThread.isKilled()) {
            this.saveThread = new EventSaveThread();
            this.saveThread.start();
        }
        this.saveThread.init(this);
        if (this.indicatorEvaluatorThread == null || this.indicatorEvaluatorThread.isKilled()) {
            this.indicatorEvaluatorThread = new IndicatorEvaluatorThread();
            this.indicatorEvaluatorThread.start();
        }
        this.indicatorEvaluatorThread.init(this);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActionBar() != null) {
            getActionBar().setDisplayShowTitleEnabled(false);
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }
    }

    public void onDestroy() {
        new C09061().start();
        super.onDestroy();
    }

    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(C0845R.id.action_new_event).setVisible(false);
    }

    void hideSection(String programStageSectionId) {
        if (this.spinnerAdapter != null) {
            this.spinnerAdapter.hideSection(programStageSectionId);
        }
    }

    public Loader<EventDataEntryFragmentForm> onCreateLoader(int id, Bundle args) {
        if (17 != id || !isAdded()) {
            return null;
        }
        List<Class<? extends Model>> modelsToTrack = new ArrayList();
        Bundle fragmentArguments = args.getBundle("extra:Arguments");
        return new DbLoader(getActivity(), modelsToTrack, new EventDataEntryFragmentQuery(fragmentArguments.getString("extra:orgUnitId"), fragmentArguments.getString("extra:ProgramId"), fragmentArguments.getString(PROGRAM_STAGE_ID), fragmentArguments.getLong(EVENT_ID, -1), fragmentArguments.getLong("extra:EnrollmentId", -1)));
    }

    public void onLoadFinished(Loader<EventDataEntryFragmentForm> loader, EventDataEntryFragmentForm data) {
        if (loader.getId() == 17 && isAdded()) {
            this.progressBar.setVisibility(8);
            this.listView.setVisibility(0);
            this.form = data;
            this.saveThread.setEvent(this.form.getEvent());
            if (this.form.getStatusRow() != null) {
                this.form.getStatusRow().setFragmentActivity(getActivity());
            }
            if (data.getStage() != null && data.getStage().getCaptureCoordinates()) {
                GpsController.activateGps(getActivity().getBaseContext());
            } else if (hasCoordinateQuestion()) {
                GpsController.activateGps(getActivity().getBaseContext());
            }
            if (data.getStage() != null && data.getStage().getCaptureCoordinates()) {
                GpsController.activateGps(getActivity().getBaseContext());
            }
            if (!(data.getSections() == null || data.getSections().isEmpty())) {
                if (data.getSections().size() > 1) {
                    attachSpinner();
                    this.spinnerAdapter.swapData(data.getSections());
                } else {
                    if (this.form.getStage() != null) {
                        getActionBarToolbar().setTitle(this.form.getStage().getName());
                    }
                    this.listViewAdapter.swapData(((DataEntryFragmentSection) data.getSections().get(0)).getRows());
                }
            }
            if (this.form.getEvent() == null) {
                showErrorAndDisableEditing(getContext().getString(C0845R.string.no_event_present));
            } else if ("COMPLETED".equals(this.form.getEvent().getStatus()) && this.form.getStage().isBlockEntryForm()) {
                setEditableDataEntryRows(this.form, false, true);
            }
            initiateEvaluateProgramRules();
        }
    }

    private boolean hasCoordinateQuestion() {
        List<Row> rows = new ArrayList();
        if (this.form.getSections() != null) {
            for (DataEntryFragmentSection section : this.form.getSections()) {
                rows.addAll(section.getRows());
            }
        }
        if (this.form.getCurrentSection() != null) {
            rows.addAll(this.form.getCurrentSection().getRows());
        }
        for (Row row : rows) {
            if (row.getViewType() == DataEntryRowTypes.QUESTION_COORDINATES.ordinal()) {
                return true;
            }
        }
        return false;
    }

    private void showErrorAndDisableEditing(String extraInfo) {
        Toast.makeText(getContext(), getContext().getString(C0845R.string.error_with_form) + extraInfo + getContext().getString(C0845R.string.please_retry), 1).show();
        setEditableDataEntryRows(this.form, false, false);
    }

    public void setEditableDataEntryRows(EventDataEntryFragmentForm form, boolean editableDataEntryRows, boolean editableStatusRow) {
        List<Row> rows = new ArrayList();
        if (!(form.getSections() == null || form.getSections().isEmpty())) {
            if (form.getSections().size() > 1) {
                for (DataEntryFragmentSection section : form.getSections()) {
                    rows.addAll(section.getRows());
                }
            } else {
                rows = ((DataEntryFragmentSection) form.getSections().get(0)).getRows();
            }
        }
        this.listViewAdapter.swapData(null);
        if (editableDataEntryRows) {
            for (Row row : rows) {
                row.setEditable(true);
            }
        } else {
            for (Row row2 : rows) {
                row2.setEditable(false);
            }
        }
        if (editableStatusRow) {
            form.getStatusRow().setEditable(true);
        }
        this.listView.setAdapter(null);
        if (form.getSections() != null) {
            this.listViewAdapter.swapData(((DataEntryFragmentSection) form.getSections().get(0)).getRows());
        } else {
            Toast.makeText(getContext(), getContext().getString(C0845R.string.an_error_ocurred), 0).show();
        }
        this.listView.setAdapter(this.listViewAdapter);
    }

    private void attachSpinner() {
        if (!isSpinnerAttached()) {
            Toolbar toolbar = getActionBarToolbar();
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            this.spinnerContainer = inflater.inflate(C0845R.layout.toolbar_spinner, toolbar, false);
            this.previousSectionButton = (ImageView) this.spinnerContainer.findViewById(C0845R.id.previous_section);
            this.nextSectionButton = (ImageView) this.spinnerContainer.findViewById(C0845R.id.next_section);
            toolbar.addView(this.spinnerContainer, new LayoutParams(-1, -1));
            this.spinnerAdapter = new SectionAdapter(inflater);
            this.spinner = (Spinner) this.spinnerContainer.findViewById(C0845R.id.toolbar_spinner);
            this.spinner.setAdapter(this.spinnerAdapter);
            this.spinner.setOnItemSelectedListener(this);
            this.previousSectionButton.setOnClickListener(new C09072());
            this.nextSectionButton.setOnClickListener(new C09083());
        }
    }

    public void onLoaderReset(Loader<EventDataEntryFragmentForm> loader) {
        if (loader.getId() == 17) {
            if (this.spinnerAdapter != null) {
                this.spinnerAdapter.swapData(null);
            }
            if (this.listViewAdapter != null) {
                this.listViewAdapter.swapData(null);
            }
        }
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        selectSection(position);
    }

    private void selectSection(int position) {
        DataEntryFragmentSection section = (DataEntryFragmentSection) this.spinnerAdapter.getItem(position);
        this.form.setCurrentSection(section);
        if (section != null) {
            this.listView.smoothScrollToPosition(0);
            this.listViewAdapter.swapData(section.getRows());
        }
        updateSectionNavigationButtons();
    }

    protected boolean isValid() {
        if (this.form.getEvent() == null || this.form.getStage() == null || StringUtils.isEmpty(this.form.getEvent().getEventDate())) {
            return false;
        }
        Map<String, ProgramStageDataElement> dataElements = toMap(this.form.getStage().getProgramStageDataElements());
        for (DataEntryFragmentSection dataEntryFragmentSection : this.form.getSections()) {
            for (Row row : dataEntryFragmentSection.getRows()) {
                if (row.getValidationError() != null) {
                    return false;
                }
            }
        }
        for (DataValue dataValue : this.form.getEvent().getDataValues()) {
            ProgramStageDataElement dataElement = (ProgramStageDataElement) dataElements.get(dataValue.getDataElement());
            if (dataElement == null) {
                return false;
            }
            if (dataElement.getCompulsory() && StringUtils.isEmpty(dataValue.getValue())) {
                return false;
            }
            if (this.listViewAdapter.getMandatoryList().contains(dataElement.getDataelement()) && StringUtils.isEmpty(dataValue.getValue())) {
                return false;
            }
        }
        return true;
    }

    protected void save() {
        if (this.form != null && this.form.getEvent() != null) {
            flagDataChanged(false);
        }
    }

    protected void proceed() {
    }

    public HashMap<ErrorType, ArrayList<String>> getValidationErrors() {
        HashMap<ErrorType, ArrayList<String>> errors = new HashMap();
        if (!(this.form.getEvent() == null || this.form.getStage() == null)) {
            if (StringUtils.isEmpty(this.form.getEvent().getEventDate())) {
                String reportDateDescription = this.form.getStage().getReportDateDescription() == null ? getString(C0845R.string.report_date) : this.form.getStage().getReportDateDescription();
                if (!errors.containsKey(ErrorType.MANDATORY)) {
                    errors.put(ErrorType.MANDATORY, new ArrayList());
                }
                ((ArrayList) errors.get(ErrorType.MANDATORY)).add(reportDateDescription);
            }
            Map<String, ProgramStageDataElement> dataElements = toMap(this.form.getStage().getProgramStageDataElements());
            for (DataValue dataValue : this.form.getEvent().getDataValues()) {
                ProgramStageDataElement dataElement = (ProgramStageDataElement) dataElements.get(dataValue.getDataElement());
                String dataElementUid = dataElement.getDataElement().getUid();
                if (dataElement != null && ((dataElement.getCompulsory() || this.listViewAdapter.getMandatoryList().contains(dataElementUid)) && StringUtils.isEmpty(dataValue.getValue()))) {
                    if (!errors.containsKey(ErrorType.MANDATORY)) {
                        errors.put(ErrorType.MANDATORY, new ArrayList());
                    }
                    ((ArrayList) errors.get(ErrorType.MANDATORY)).add(MetaDataController.getDataElement(dataElement.getDataelement()).getDisplayName());
                }
            }
        }
        return errors;
    }

    private void evaluateRulesAndIndicators(String dataElement) {
        if (dataElement != null && this.form != null && this.form.getIndicatorRows() != null) {
            if (hasRules(dataElement)) {
                getProgramRuleFragmentHelper().getProgramRuleValidationErrors().clear();
                getProgramRuleFragmentHelper().getShowOnCompleteErrors().clear();
                getProgramRuleFragmentHelper().getShowOnCompleteWarningErrors().clear();
                initiateEvaluateProgramRules();
            }
            if (hasIndicators(dataElement)) {
                initiateEvaluateProgramIndicators(dataElement);
            }
        }
    }

    private boolean hasRules(String dataElement) {
        if (this.programRulesForDataElements == null) {
            return false;
        }
        return this.programRulesForDataElements.containsKey(dataElement);
    }

    private boolean hasIndicators(String dataElement) {
        if (this.programIndicatorsForDataElements == null) {
            return false;
        }
        return this.programIndicatorsForDataElements.containsKey(dataElement);
    }

    public void initiateEvaluateProgramRules() {
        if (this.rulesEvaluatorThread != null) {
            this.rulesEvaluatorThread.schedule();
        }
    }

    private synchronized void initiateEvaluateProgramIndicators(String dataElement) {
        if (this.programIndicatorsForDataElements != null) {
            this.indicatorEvaluatorThread.schedule((List) this.programIndicatorsForDataElements.get(dataElement));
        }
    }

    void evaluateAndApplyProgramIndicator(ProgramIndicator programIndicator) {
        if (VariableService.getInstance().getProgramRuleVariableMap() == null) {
            VariableService.initialize(this.form.getEnrollment(), this.form.getEvent());
        }
        updateIndicatorRow((IndicatorRow) this.form.getIndicatorToIndicatorRowMap().get(programIndicator.getUid()), this.form.getEvent());
        DataEntryFragment.refreshListView();
    }

    private static void updateIndicatorRow(IndicatorRow indicatorRow, Event event) {
        String newValue = ProgramIndicatorService.getProgramIndicatorValue(event, indicatorRow.getIndicator());
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(indicatorRow.getValue())) {
            indicatorRow.updateValue(newValue);
        }
    }

    private static Map<String, ProgramStageDataElement> toMap(List<ProgramStageDataElement> dataElements) {
        Map<String, ProgramStageDataElement> dataElementMap = new HashMap();
        if (!(dataElements == null || dataElements.isEmpty())) {
            for (ProgramStageDataElement dataElement : dataElements) {
                dataElementMap.put(dataElement.getDataelement(), dataElement);
            }
        }
        return dataElementMap;
    }

    private void updateSectionNavigationButtons() {
        if (this.nextSectionButton != null && this.previousSectionButton != null) {
            if (this.spinner.getSelectedItemPosition() - 1 < 0) {
                this.previousSectionButton.setVisibility(4);
            } else {
                this.previousSectionButton.setVisibility(0);
            }
            if (this.spinner.getSelectedItemPosition() + 1 >= this.spinnerAdapter.getCount()) {
                this.nextSectionButton.setVisibility(4);
            } else {
                this.nextSectionButton.setVisibility(0);
            }
        }
    }

    public SectionAdapter getSpinnerAdapter() {
        return this.spinnerAdapter;
    }

    public static ArrayList<String> getValidationErrors(Event event, ProgramStage programStage, Context context) {
        ArrayList<String> errors = new ArrayList();
        if (!(event == null || programStage == null)) {
            if (StringUtils.isEmpty(event.getEventDate())) {
                errors.add(programStage.getReportDateDescription() == null ? context.getString(C0845R.string.report_date) : programStage.getReportDateDescription());
            }
            Map<String, ProgramStageDataElement> dataElements = toMap(programStage.getProgramStageDataElements());
            for (DataValue dataValue : event.getDataValues()) {
                ProgramStageDataElement dataElement = (ProgramStageDataElement) dataElements.get(dataValue.getDataElement());
                if (dataElement.getCompulsory() && StringUtils.isEmpty(dataValue.getValue())) {
                    errors.add(MetaDataController.getDataElement(dataElement.getDataelement()).getDisplayName());
                }
            }
        }
        return errors;
    }

    private static ArrayList<String> getRowsErrors(Context context, EventDataEntryFragmentForm form) {
        ArrayList<String> errors = new ArrayList();
        for (DataEntryFragmentSection dataEntryFragmentSection : form.getSections()) {
            for (Row row : dataEntryFragmentSection.getRows()) {
                if (row.getValidationError() != null) {
                    Integer stringId = row.getValidationError();
                    if (stringId != null) {
                        errors.add(context.getString(stringId.intValue()));
                    }
                }
            }
        }
        return errors;
    }

    @Subscribe
    public void onHideLoadingDialog(HideLoadingDialogEvent event) {
        super.onHideLoadingDialog(event);
    }

    @Subscribe
    public void onUpdateSectionsSpinner(UpdateSectionsEvent event) {
        if (this.spinnerAdapter != null) {
            this.spinnerAdapter.notifyDataSetChanged();
            if (this.form != null && this.form.getCurrentSection() != null && this.form.getCurrentSection().isHidden()) {
                selectSection(0);
            }
        }
    }

    @Subscribe
    public void onRefreshListView(RefreshListViewEvent event) {
        super.onRefreshListView(event);
    }

    @Subscribe
    public void onDetailedInfoClick(OnDetailedInfoButtonClick eventClick) {
        super.onShowDetailedInfo(eventClick);
    }

    @Subscribe
    public void onItemClick(OnCompleteEventClick eventClick) {
        if (eventClick.getEvent().getStatus().equals("COMPLETED") || !showOnCompleteMessages(eventClick)) {
            completeEvent(eventClick);
        }
    }

    public void completeEnrollment() {
        if (this.form == null || this.form.getEnrollment() == null) {
            Log.i("ENROLLMENT", "Unable to complete enrollment. mForm or mForm.getEnrollment() is null");
        } else {
            this.form.getEnrollment().setStatus("COMPLETED");
        }
    }

    public void unCompleteEnrollment() {
        if (this.form == null || this.form.getEnrollment() == null) {
            Log.i("ENROLLMENT", "Unable to uncomplete enrollment. mForm or mForm.getEnrollment() is null");
        } else {
            this.form.getEnrollment().setStatus("ACTIVE");
        }
    }

    private void completeEvent(final OnCompleteEventClick eventClick) {
        if (!isValid()) {
            HashMap<ErrorType, ArrayList<String>> allErrors = getValidationErrors();
            allErrors.put(ErrorType.PROGRAM_RULE, getProgramRuleFragmentHelper().getProgramRuleValidationErrors());
            allErrors.put(ErrorType.INVALID_FIELD, getRowsErrors(getContext(), this.form));
            showValidationErrorDialog(allErrors);
        } else if (eventClick.getEvent().getStatus().equals("COMPLETED")) {
            eventClick.getTv().setVisibility(8);
            eventClick.getComplete().setText(C0845R.string.complete);
            eventClick.getComplete().setBackgroundColor(Color.parseColor("#FF9900"));
            this.form.getEvent().setStatus("ACTIVE");
            this.form.getEvent().setFromServer(false);
            ProgramStage currentProgramStage = MetaDataController.getProgramStage(this.form.getEvent().getProgramStageId());
            boolean isPregCloseStage = "HaOwL7bIdrs".equals(currentProgramStage.getUid());
            if (currentProgramStage.isBlockEntryForm() || isPregCloseStage) {
                setEditableDataEntryRows(this.form, true, true);
            }
            if (isPregCloseStage) {
                unCompleteEnrollment();
            }
            Dhis2Application.getEventBus().post(new RowValueChangedEvent(null, null));
        } else {
            getActivity().runOnUiThread(new Runnable() {

                /* renamed from: org.icddrb.dhis.android.sdk.ui.fragments.eventdataentry.EventDataEntryFragment$4$1 */
                class C09091 implements DialogInterface.OnClickListener {
                    C09091() {
                    }

                    public void onClick(DialogInterface dialog, int which) {
                        String labelForCompleteButton = "";
                        if (EventDataEntryFragment.this.form.getStage().isBlockEntryForm()) {
                            eventClick.getComplete().setText(EventDataEntryFragment.this.getString(C0845R.string.edit));
                        }
                        eventClick.getEvent().setStatus("COMPLETED");
                        EventDataEntryFragment.this.form.getEvent().setFromServer(false);
                        EventDataEntryFragment.this.form.getEnrollment().setFromServer(false);
                        TrackedEntityInstance trackedEntityInstance = TrackerController.getTrackedEntityInstance(EventDataEntryFragment.this.form.getEnrollment().getTrackedEntityInstance());
                        trackedEntityInstance.setFromServer(false);
                        trackedEntityInstance.save();
                        ProgramStage currentProgramStage = MetaDataController.getProgramStage(EventDataEntryFragment.this.form.getEvent().getProgramStageId());
                        if (currentProgramStage.getAllowGenerateNextVisit()) {
                            if (currentProgramStage.getRepeatable()) {
                                EventDataEntryFragment.this.showDatePicker(currentProgramStage, EventDataEntryFragment.this.calculateScheduledDate(currentProgramStage, EventDataEntryFragment.this.form.getEnrollment()));
                            } else {
                                int sortOrder = currentProgramStage.getSortOrder();
                                Program currentProgram = currentProgramStage.getProgram();
                                ProgramStage programStageToSchedule = EventDataEntryFragment.this.getNextValidProgramStage(sortOrder, currentProgram, null);
                                if (programStageToSchedule == null) {
                                    programStageToSchedule = EventDataEntryFragment.this.getFirstValidProgramStage(currentProgram, programStageToSchedule);
                                }
                                if (programStageToSchedule != null) {
                                    EventDataEntryFragment.this.showDatePicker(programStageToSchedule, EventDataEntryFragment.this.calculateScheduledDate(programStageToSchedule, EventDataEntryFragment.this.form.getEnrollment()));
                                }
                            }
                        }
                        boolean isPregCloseStage = "HaOwL7bIdrs".equals(currentProgramStage.getUid());
                        if (currentProgramStage.isBlockEntryForm() || isPregCloseStage) {
                            EventDataEntryFragment.this.setEditableDataEntryRows(EventDataEntryFragment.this.form, false, true);
                        }
                        eventClick.getEvent().setCompletedDate(new DateTime().toString());
                        Dhis2Application.getEventBus().post(new RowValueChangedEvent(null, null));
                        if (isPregCloseStage) {
                            EventDataEntryFragment.this.completeEnrollment();
                        }
                        EventDataEntryFragment.this.goBackToPreviousActivity();
                    }
                }

                public void run() {
                    UiUtils.showConfirmDialog(EventDataEntryFragment.this.getActivity(), eventClick.getLabel(), eventClick.getAction(), eventClick.getLabel(), EventDataEntryFragment.this.getActivity().getString(C0845R.string.cancel), new C09091());
                }
            });
        }
    }

    private boolean showOnCompleteMessages(final OnCompleteEventClick eventClick) {
        String message = "";
        Iterator it = getProgramRuleFragmentHelper().getShowOnCompleteErrors().iterator();
        while (it.hasNext()) {
            String value = (String) it.next();
            message = message + String.format(getString(C0845R.string.program_rule_on_complete_message_error), new Object[]{value}) + StringUtils.LF;
        }
        it = getProgramRuleFragmentHelper().getShowOnCompleteWarningErrors().iterator();
        while (it.hasNext()) {
            value = (String) it.next();
            message = message + String.format(getString(C0845R.string.program_rule_on_complete_message_warning), new Object[]{value}) + StringUtils.LF;
        }
        String title = getContext().getString(C0845R.string.program_rule_on_complete_message_title);
        if (getProgramRuleFragmentHelper().getShowOnCompleteErrors().size() > 0) {
            UiUtils.showConfirmDialog(getActivity(), title, message, getString(C0845R.string.cancel), new C09115());
            return true;
        } else if (getProgramRuleFragmentHelper().getShowOnCompleteWarningErrors().size() <= 0) {
            return false;
        } else {
            UiUtils.showConfirmDialog(getActivity(), title, message, getString(C0845R.string.ok_option), getString(C0845R.string.cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    EventDataEntryFragment.this.completeEvent(eventClick);
                }
            });
            return true;
        }
    }

    @Nullable
    private ProgramStage getFirstValidProgramStage(Program currentProgram, ProgramStage programStageToSchedule) {
        for (ProgramStage programStage : currentProgram.getProgramStages()) {
            if (programStageToSchedule != null) {
                break;
            }
            programStageToSchedule = getValidProgramStage(programStageToSchedule, programStage);
        }
        return programStageToSchedule;
    }

    @Nullable
    private ProgramStage getNextValidProgramStage(int sortOrder, Program currentProgram, ProgramStage programStageToSchedule) {
        for (ProgramStage programStage : currentProgram.getProgramStages()) {
            if (programStage.getSortOrder() >= sortOrder + 1 && programStageToSchedule == null) {
                programStageToSchedule = getValidProgramStage(programStageToSchedule, programStage);
                if (programStageToSchedule != null) {
                    return programStageToSchedule;
                }
            }
        }
        return programStageToSchedule;
    }

    private ProgramStage getValidProgramStage(ProgramStage programStageToSchedule, ProgramStage programStage) {
        if (programStage.isRepeatable()) {
            programStageToSchedule = programStage;
        } else if (TrackerController.getEvent(this.form.getEnrollment().getLocalId(), programStage.getUid()) != null) {
            if (programStage.isRepeatable()) {
                programStageToSchedule = programStage;
            } else if (hasTheCorrectNumberOfEvents(programStage)) {
                return programStage;
            }
        }
        return programStageToSchedule;
    }

    private boolean hasTheCorrectNumberOfEvents(ProgramStage programStageToSchedule) {
        List<Event> events = this.form.getEnrollment().getEvents();
        List<Event> eventForStage = new ArrayList();
        for (Event event : events) {
            if (programStageToSchedule.getUid().equals(event.getProgramStageId())) {
                eventForStage.add(event);
            }
        }
        if (eventForStage.size() == 0) {
            return true;
        }
        return false;
    }

    @Subscribe
    public void onRowValueChanged(RowValueChangedEvent event) {
        super.onRowValueChanged(event);
        if (event.getRow() == null || !event.getRow().isEditTextRow()) {
            evaluateRulesAndIndicators(event.getId());
        }
        if (event.getRowType() == null || DataEntryRowTypes.EVENT_COORDINATES.toString().equals(event.getRowType()) || DataEntryRowTypes.EVENT_DATE.toString().equals(event.getRowType())) {
            this.saveThread.scheduleSaveEvent();
            List<Event> eventsForEnrollment = new ArrayList();
            for (Event eventd : this.form.getEnrollment().getEvents()) {
                if (eventd.getUid().equals(this.form.getEvent().getUid())) {
                    eventsForEnrollment.add(this.form.getEvent());
                } else {
                    eventsForEnrollment.add(eventd);
                }
            }
            this.form.getEnrollment().setEvents(eventsForEnrollment);
        } else {
            this.saveThread.scheduleSaveDataValue(event.getId());
        }
        if (DataEntryRowTypes.EVENT_DATE.toString().equals(event.getRowType())) {
            initiateEvaluateProgramRules();
        }
    }

    @Subscribe
    public void onRunProgramRules(RunProgramRulesEvent event) {
        evaluateRulesAndIndicators(event.getId());
    }

    public EventSaveThread getSaveThread() {
        return this.saveThread;
    }

    public void setSaveThread(EventSaveThread saveThread) {
        this.saveThread = saveThread;
    }

    public EventDataEntryFragmentForm getForm() {
        return this.form;
    }

    public void setForm(EventDataEntryFragmentForm form) {
        this.form = form;
    }

    public Map<String, List<ProgramRule>> getProgramRulesForDataElements() {
        return this.programRulesForDataElements;
    }

    public void setProgramRulesForDataElements(Map<String, List<ProgramRule>> programRulesForDataElements) {
        this.programRulesForDataElements = programRulesForDataElements;
    }

    public Map<String, List<ProgramIndicator>> getProgramIndicatorsForDataElements() {
        return this.programIndicatorsForDataElements;
    }

    public void setProgramIndicatorsForDataElements(Map<String, List<ProgramIndicator>> programIndicatorsForDataElements) {
        this.programIndicatorsForDataElements = programIndicatorsForDataElements;
    }

    private void showDatePicker(final ProgramStage programStage, DateTime scheduledDueDate) {
        int standardInterval = 0;
        if (programStage.getStandardInterval() > 0) {
            standardInterval = programStage.getStandardInterval();
        }
        final DatePickerDialog enrollmentDatePickerDialog = new DatePickerDialog(getActivity(), null, scheduledDueDate.getYear(), scheduledDueDate.getMonthOfYear() - 1, scheduledDueDate.getDayOfMonth() + standardInterval);
        enrollmentDatePickerDialog.setTitle(getActivity().getString(C0845R.string.please_enter) + getContext().getString(C0845R.string.due_date_for) + programStage.getDisplayName());
        enrollmentDatePickerDialog.setCanceledOnTouchOutside(true);
        enrollmentDatePickerDialog.setButton(-1, getContext().getString(C0845R.string.ok_option), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                DatePicker dp = enrollmentDatePickerDialog.getDatePicker();
                EventDataEntryFragment.this.scheduleNewEvent(programStage, new DateTime(dp.getYear(), dp.getMonth() + 1, dp.getDayOfMonth(), 0, 0));
                EventDataEntryFragment.this.goBackToPreviousActivity();
            }
        });
        enrollmentDatePickerDialog.setButton(-2, getContext().getString(C0845R.string.cancel_option), new C09148());
        enrollmentDatePickerDialog.show();
    }

    private DateTime calculateScheduledDate(ProgramStage programStage, Enrollment enrollment) {
        DateTime dateTime = new DateTime();
        if (programStage.getPeriodType() != null && !programStage.getPeriodType().equals("")) {
            return dateTime;
        }
        List<Event> eventsForEnrollment = new ArrayList();
        eventsForEnrollment.addAll(enrollment.getEvents());
        Collections.sort(eventsForEnrollment, new EventDateComparator());
        if (eventsForEnrollment.size() > 0) {
            Event lastKnownEvent = (Event) eventsForEnrollment.get(eventsForEnrollment.size() - 1);
            if (lastKnownEvent != null) {
                return new DateTime(lastKnownEvent.getEventDate());
            }
        }
        if (programStage.getProgram().getDisplayIncidentDate()) {
            return new DateTime(enrollment.getIncidentDate());
        }
        if (programStage.getGeneratedByEnrollmentDate()) {
            return new DateTime(enrollment.getEnrollmentDate());
        }
        return dateTime;
    }

    private void goBackToPreviousActivity() {
        getActivity().finish();
    }

    public void scheduleNewEvent(ProgramStage programStage, DateTime scheduledDueDate) {
        Event event = new Event(this.form.getEnrollment().getOrgUnit(), Event.STATUS_FUTURE_VISIT, this.form.getEnrollment().getProgram().getUid(), programStage, this.form.getEnrollment().getTrackedEntityInstance(), this.form.getEnrollment().getEnrollment(), scheduledDueDate.toString());
        event.save();
        List<Event> eventsForEnrollment = this.form.getEnrollment().getEvents();
        eventsForEnrollment.add(event);
        this.form.getEnrollment().setEvents(eventsForEnrollment);
        this.form.getEnrollment().save();
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        doBack();
        return true;
    }

    public boolean doBack() {
        if (this.form.getEvent().getDueDate() == null && this.form.getEvent().getEventDate() == null) {
            this.form.getEvent().delete();
            return super.doBack();
        } else if (getRowsErrors(getContext(), this.form).size() <= 0) {
            return super.doBack();
        } else {
            showErrorAndGoBack();
            return false;
        }
    }

    private void showErrorAndGoBack() {
        UiUtils.showConfirmDialog(getActivity(), getContext().getString(C0845R.string.validation_field_title), getContext().getString(C0845R.string.validation_field_exit), getString(C0845R.string.ok_option), getString(C0845R.string.cancel), new C09159());
    }

    private void removeInvalidFields() {
        for (DataEntryFragmentSection dataEntryFragmentSection : this.form.getSections()) {
            for (Row row : dataEntryFragmentSection.getRows()) {
                if (!(row.getValidationError() == null || row.getValue() == null)) {
                    row.getValue().delete();
                }
            }
        }
    }
}
