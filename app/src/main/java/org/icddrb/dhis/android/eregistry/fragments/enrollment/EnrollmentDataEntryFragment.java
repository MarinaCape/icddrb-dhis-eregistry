package org.icddrb.dhis.android.eregistry.fragments.enrollment;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import com.raizlabs.android.dbflow.structure.Model;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.icddrb.dhis.android.eregistry.C0773R;
import org.icddrb.dhis.android.eregistry.activities.HolderActivity;
import org.icddrb.dhis.android.sdk.controllers.ErrorType;
import org.icddrb.dhis.android.sdk.controllers.GpsController;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.controllers.tracker.TrackerController;
import org.icddrb.dhis.android.sdk.persistence.loaders.DbLoader;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramRule;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramTrackedEntityAttribute;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttribute;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeGeneratedValue;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.ui.activities.OnBackPressedListener;
import org.icddrb.dhis.android.sdk.ui.adapters.SectionAdapter;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.DataEntryRow;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.DataEntryRowFactory;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.DataEntryRowTypes;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.Row;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.RunProgramRulesEvent;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.events.OnDetailedInfoButtonClick;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.DataEntryFragment;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.HideLoadingDialogEvent;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.RefreshListViewEvent;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.RowValueChangedEvent;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.SaveThread;
import org.icddrb.dhis.android.sdk.utils.UiUtils;

public class EnrollmentDataEntryFragment extends DataEntryFragment<EnrollmentDataEntryFragmentForm> implements OnBackPressedListener {
    private static final String EMPTY_FIELD = "";
    public static final String ENROLLMENT_DATE = "extra:enrollmentDate";
    public static final String INCIDENT_DATE = "extra:incidentDate";
    public static final String ORG_UNIT_ID = "extra:orgUnitId";
    public static final String PROGRAMRULES_FORCED_TRIGGER = "forced";
    public static final String PROGRAM_ID = "extra:ProgramId";
    public static final String TAG = EnrollmentDataEntryFragment.class.getSimpleName();
    public static final String TRACKEDENTITYINSTANCE_ID = "extra:TrackedEntityInstanceId";
    private EnrollmentDataEntryFragmentForm form;
    private Enrollment originalEnrollment = null;
    private Map<String, TrackedEntityAttributeValue> originalTrackedEntityAttributeValueMap;
    private TrackedEntityInstance originalTrackedEntityInstance = null;
    private Map<String, List<ProgramRule>> programRulesForTrackedEntityAttributes;
    private SaveThread saveThread;

    /* renamed from: org.icddrb.dhis.android.eregistry.fragments.enrollment.EnrollmentDataEntryFragment$1 */
    class C07741 implements OnClickListener {
        C07741() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            EnrollmentDataEntryFragment.this.getActivity().finish();
        }
    }

    /* renamed from: org.icddrb.dhis.android.eregistry.fragments.enrollment.EnrollmentDataEntryFragment$2 */
    class C07752 implements OnClickListener {
        C07752() {
        }

        public void onClick(DialogInterface dialog, int which) {
            EnrollmentDataEntryFragment.this.discardChanges();
            EnrollmentDataEntryFragment.this.getActivity().finish();
        }
    }

    /* renamed from: org.icddrb.dhis.android.eregistry.fragments.enrollment.EnrollmentDataEntryFragment$3 */
    class C07763 implements OnClickListener {
        C07763() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    public EnrollmentDataEntryFragment() {
        setProgramRuleFragmentHelper(new EnrollmentDataEntryRuleHelper(this));
    }

    public static EnrollmentDataEntryFragment newInstance(String unitId, String programId, String enrollmentDate, String incidentDate) {
        EnrollmentDataEntryFragment fragment = new EnrollmentDataEntryFragment();
        Bundle args = new Bundle();
        args.putString(ENROLLMENT_DATE, enrollmentDate);
        args.putString(INCIDENT_DATE, incidentDate);
        args.putString("extra:orgUnitId", unitId);
        args.putString("extra:orgUnitId", unitId);
        args.putString("extra:ProgramId", programId);
        fragment.setArguments(args);
        return fragment;
    }

    public static EnrollmentDataEntryFragment newInstance(String unitId, String programId, long trackedEntityInstanceId, String enrollmentDate, String incidentDate) {
        EnrollmentDataEntryFragment fragment = new EnrollmentDataEntryFragment();
        Bundle args = new Bundle();
        args.putString(ENROLLMENT_DATE, enrollmentDate);
        args.putString(INCIDENT_DATE, incidentDate);
        args.putString("extra:orgUnitId", unitId);
        args.putString("extra:ProgramId", programId);
        args.putLong("extra:TrackedEntityInstanceId", trackedEntityInstanceId);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.saveThread == null || this.saveThread.isKilled()) {
            this.saveThread = new SaveThread();
            this.saveThread.start();
        }
        this.saveThread.init(this);
    }

    public void onDestroy() {
        this.saveThread.kill();
        super.onDestroy();
    }

    public SectionAdapter getSpinnerAdapter() {
        return null;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() instanceof AppCompatActivity) {
            getActionBar().setDisplayShowTitleEnabled(true);
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }
    }

    public Loader<EnrollmentDataEntryFragmentForm> onCreateLoader(int id, Bundle args) {
        if (17 != id || !isAdded()) {
            return null;
        }
        List<Class<? extends Model>> modelsToTrack = new ArrayList();
        Bundle fragmentArguments = args.getBundle("extra:Arguments");
        String orgUnitId = fragmentArguments.getString("extra:orgUnitId");
        String programId = fragmentArguments.getString("extra:ProgramId");
        String enrollmentDate = fragmentArguments.getString(ENROLLMENT_DATE);
        String incidentDate = fragmentArguments.getString(INCIDENT_DATE);
        return new DbLoader(getActivity().getBaseContext(), modelsToTrack, new EnrollmentDataEntryFragmentQuery(orgUnitId, programId, fragmentArguments.getLong("extra:TrackedEntityInstanceId", -1), enrollmentDate, incidentDate, this));
    }

    public void onLoadFinished(Loader<EnrollmentDataEntryFragmentForm> loader, EnrollmentDataEntryFragmentForm data) {
        if (loader.getId() == 17 && isAdded()) {
            this.progressBar.setVisibility(8);
            this.listView.setVisibility(0);
            if (this.originalEnrollment == null) {
                this.originalEnrollment = new Enrollment(data.getEnrollment());
            }
            this.form = data;
            if (data.getTrackedEntityInstance().getLocalId() >= 0) {
                this.originalTrackedEntityInstance = new TrackedEntityInstance(data.getTrackedEntityInstance());
            }
            if (this.originalTrackedEntityAttributeValueMap == null) {
                this.originalTrackedEntityAttributeValueMap = new HashMap();
                for (TrackedEntityAttributeValue trackedEntityAttributeValue : this.form.getTrackedEntityAttributeValueMap().values()) {
                    TrackedEntityAttributeValue copiedTrackedEntityAttributeValue = new TrackedEntityAttributeValue(trackedEntityAttributeValue);
                    this.originalTrackedEntityAttributeValueMap.put(copiedTrackedEntityAttributeValue.getTrackedEntityAttributeId(), copiedTrackedEntityAttributeValue);
                }
            }
            if (data.getProgram() != null) {
                getActionBar().setTitle(this.form.getProgram().getName());
            }
            if (this.form.isOutOfTrackedEntityAttributeGeneratedValues()) {
                for (Row row : this.form.getDataEntryRows()) {
                    row.setEditable(false);
                }
                UiUtils.showErrorDialog(getActivity(), getString(C0773R.string.error_message), getString(C0773R.string.out_of_generated_ids), new C07741());
            }
            if (!(data.getDataEntryRows() == null || data.getDataEntryRows().isEmpty())) {
                this.listViewAdapter.swapData(data.getDataEntryRows());
            }
            if (data.getProgram().getProgramRules() != null && !data.getProgram().getProgramRules().isEmpty()) {
                initiateEvaluateProgramRules();
            }
        }
    }

    public void initiateEvaluateProgramRules() {
        if (this.rulesEvaluatorThread != null) {
            this.rulesEvaluatorThread.schedule();
        }
    }

    public void onLoaderReset(Loader<EnrollmentDataEntryFragmentForm> loader) {
        if (loader.getId() == 17 && this.listViewAdapter != null) {
            this.listViewAdapter.swapData(null);
        }
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
    }

    protected HashMap<ErrorType, ArrayList<String>> getValidationErrors() {
        HashMap<ErrorType, ArrayList<String>> errors = new HashMap();
        if (!(this.form.getEnrollment() == null || this.form.getProgram() == null || this.form.getOrganisationUnit() == null)) {
            if (StringUtils.isEmpty(this.form.getEnrollment().getEnrollmentDate())) {
                String dateOfEnrollmentDescription = this.form.getProgram().getEnrollmentDateLabel() == null ? getString(C0773R.string.report_date) : this.form.getProgram().getEnrollmentDateLabel();
                if (!errors.containsKey(ErrorType.MANDATORY)) {
                    errors.put(ErrorType.MANDATORY, new ArrayList());
                }
                ((ArrayList) errors.get(ErrorType.MANDATORY)).add(dateOfEnrollmentDescription);
            }
            Map<String, ProgramTrackedEntityAttribute> dataElements = toMap(MetaDataController.getProgramTrackedEntityAttributes(this.form.getProgram().getUid()));
            for (TrackedEntityAttributeValue value : this.form.getEnrollment().getAttributes()) {
                ProgramTrackedEntityAttribute programTrackedEntityAttribute = (ProgramTrackedEntityAttribute) dataElements.get(value.getTrackedEntityAttributeId());
                if ((programTrackedEntityAttribute.getMandatory() || getListViewAdapter().getMandatoryList().contains(programTrackedEntityAttribute.getTrackedEntityAttributeId())) && StringUtils.isEmpty(value.getValue())) {
                    if (!errors.containsKey(ErrorType.MANDATORY)) {
                        errors.put(ErrorType.MANDATORY, new ArrayList());
                    }
                    ((ArrayList) errors.get(ErrorType.MANDATORY)).add(programTrackedEntityAttribute.getTrackedEntityAttribute().getName());
                }
            }
        }
        return errors;
    }

    public boolean isValid() {
        if (this.form.getEnrollment() == null || this.form.getProgram() == null || this.form.getOrganisationUnit() == null || StringUtils.isEmpty(this.form.getEnrollment().getEnrollmentDate())) {
            return false;
        }
        Map<String, ProgramTrackedEntityAttribute> dataElements = toMap(MetaDataController.getProgramTrackedEntityAttributes(this.form.getProgram().getUid()));
        for (TrackedEntityAttributeValue value : this.form.getEnrollment().getAttributes()) {
            if (((ProgramTrackedEntityAttribute) dataElements.get(value.getTrackedEntityAttributeId())).getMandatory() && StringUtils.isEmpty(value.getValue())) {
                return false;
            }
        }
        return true;
    }

    private static Map<String, ProgramTrackedEntityAttribute> toMap(List<ProgramTrackedEntityAttribute> attributes) {
        Map<String, ProgramTrackedEntityAttribute> attributeMap = new HashMap();
        if (!(attributes == null || attributes.isEmpty())) {
            for (ProgramTrackedEntityAttribute attribute : attributes) {
                attributeMap.put(attribute.getTrackedEntityAttributeId(), attribute);
            }
        }
        return attributeMap;
    }

    protected void save() {
    }

    protected void proceed() {
        if (validate()) {
            confirmSave();
            showProgramOverviewFragment();
            getActivity().finish();
        }
    }

    private void showProgramOverviewFragment() {
        HolderActivity.navigateToProgramOverviewFragment(getActivity(), this.form.getOrganisationUnit().getId(), this.form.getProgram().getUid(), this.form.getTrackedEntityInstance().getLocalId());
    }

    private boolean validate() {
        if (isMapEmpty(this.form.getTrackedEntityAttributeValueMap())) {
            UiUtils.showErrorDialog(getActivity(), getContext().getString(C0773R.string.error_message), getContext().getString(C0773R.string.profile_form_empty));
            return false;
        } else if (validateUniqueValues(this.form.getTrackedEntityAttributeValueMap())) {
            ArrayList<String> programRulesValidationErrors = getProgramRuleFragmentHelper().getProgramRuleValidationErrors();
            HashMap<ErrorType, ArrayList<String>> allErrors = getValidationErrors();
            ArrayList<String> validationErrors = new ArrayList();
            for (DataEntryRow dataEntryRow : this.form.getDataEntryRows()) {
                if (dataEntryRow.getValidationError() != null) {
                    validationErrors.add(getContext().getString(dataEntryRow.getValidationError().intValue()));
                }
            }
            if (programRulesValidationErrors.isEmpty() && allErrors.isEmpty() && validationErrors.isEmpty()) {
                return true;
            }
            allErrors.put(ErrorType.PROGRAM_RULE, programRulesValidationErrors);
            allErrors.put(ErrorType.INVALID_FIELD, validationErrors);
            showValidationErrorDialog(allErrors);
            return false;
        } else {
            String listOfInvalidAttributes = " ";
            for (String value : getNotValidatedUniqueValues(this.form.getTrackedEntityAttributeValueMap())) {
                listOfInvalidAttributes = listOfInvalidAttributes + value + " ";
            }
            UiUtils.showErrorDialog(getActivity(), getContext().getString(C0773R.string.error_message), String.format(getContext().getString(C0773R.string.invalid_unique_value_form_empty), new Object[]{listOfInvalidAttributes}));
            return false;
        }
    }

    private List<String> getNotValidatedUniqueValues(Map<String, TrackedEntityAttributeValue> trackedEntityAttributeValueMap) {
        List<String> listOFUniqueFields = new ArrayList();
        for (String key : trackedEntityAttributeValueMap.keySet()) {
            TrackedEntityAttributeValue value = (TrackedEntityAttributeValue) trackedEntityAttributeValueMap.get(key);
            TrackedEntityAttribute trackedEntityAttribute = MetaDataController.getTrackedEntityAttribute(value.getTrackedEntityAttributeId());
            if (!(!trackedEntityAttribute.isUnique() || value.getValue() == null || value.getValue().isEmpty() || TrackerController.countTrackedEntityAttributeValue(value) == 0)) {
                listOFUniqueFields.add(trackedEntityAttribute.getDisplayName());
            }
        }
        return listOFUniqueFields;
    }

    private boolean validateUniqueValues(Map<String, TrackedEntityAttributeValue> trackedEntityAttributeValueMap) {
        for (String key : trackedEntityAttributeValueMap.keySet()) {
            TrackedEntityAttributeValue value = (TrackedEntityAttributeValue) trackedEntityAttributeValueMap.get(key);
            if (MetaDataController.getTrackedEntityAttribute(value.getTrackedEntityAttributeId()).isUnique() && value.getValue() != null && !value.getValue().isEmpty() && TrackerController.countTrackedEntityAttributeValue(value) != 0) {
                return false;
            }
        }
        return true;
    }

    private boolean isMapEmpty(Map<String, TrackedEntityAttributeValue> trackedEntityAttributeValueMap) {
        boolean isEmpty = true;
        for (String key : trackedEntityAttributeValueMap.keySet()) {
            TrackedEntityAttributeValue value = (TrackedEntityAttributeValue) trackedEntityAttributeValueMap.get(key);
            if (!(value.getValue() == null || value.getValue().equals(""))) {
                isEmpty = false;
            }
        }
        return isEmpty;
    }

    private void evaluateRules(String trackedEntityAttribute) {
        if (trackedEntityAttribute != null && this.form != null) {
            if ("forced".equals(trackedEntityAttribute)) {
                getProgramRuleFragmentHelper().getProgramRuleValidationErrors().clear();
                initiateEvaluateProgramRules();
            }
            if (hasRules(trackedEntityAttribute)) {
                getProgramRuleFragmentHelper().getProgramRuleValidationErrors().clear();
                initiateEvaluateProgramRules();
            }
        }
    }

    private boolean hasRules(String trackedEntityAttribute) {
        if (this.programRulesForTrackedEntityAttributes == null) {
            return false;
        }
        return this.programRulesForTrackedEntityAttributes.containsKey(trackedEntityAttribute);
    }

    @Subscribe
    public void onRowValueChanged(RowValueChangedEvent event) {
        super.onRowValueChanged(event);
        if (event.getRowType() != null && event.getRowType().equals(DataEntryRowTypes.ORGANISATION_UNIT.toString())) {
            DataEntryRowFactory.updateFWADropdown(getActivity().getBaseContext(), event);
        }
        if (event.getRow() == null || !event.getRow().isEditTextRow()) {
            evaluateRules(event.getId());
        }
        if (DataEntryRowTypes.ENROLLMENT_DATE.toString().equals(event.getRowType()) || DataEntryRowTypes.EVENT_DATE.toString().equals(event.getRowType())) {
            evaluateRules("forced");
        }
        this.saveThread.schedule();
    }

    @Subscribe
    public void onRunProgramRules(RunProgramRulesEvent event) {
        evaluateRules(event.getId());
    }

    @Subscribe
    public void onDetailedInfoClick(OnDetailedInfoButtonClick eventClick) {
        super.onShowDetailedInfo(eventClick);
    }

    @Subscribe
    public void onRefreshListView(RefreshListViewEvent event) {
        super.onRefreshListView(event);
    }

    @Subscribe
    public void onHideLoadingDialog(HideLoadingDialogEvent event) {
        super.onHideLoadingDialog(event);
    }

    public SaveThread getSaveThread() {
        return this.saveThread;
    }

    public void setSaveThread(SaveThread saveThread) {
        this.saveThread = saveThread;
    }

    public EnrollmentDataEntryFragmentForm getForm() {
        return this.form;
    }

    public void setForm(EnrollmentDataEntryFragmentForm form) {
        this.form = form;
    }

    public Map<String, List<ProgramRule>> getProgramRulesForTrackedEntityAttributes() {
        return this.programRulesForTrackedEntityAttributes;
    }

    public void setProgramRulesForTrackedEntityAttributes(Map<String, List<ProgramRule>> programRulesForTrackedEntityAttributes) {
        this.programRulesForTrackedEntityAttributes = programRulesForTrackedEntityAttributes;
    }

    private void showConfirmDiscardDialog() {
        UiUtils.showConfirmDialog(getActivity(), getString(C0773R.string.discard), getString(C0773R.string.discard_confirm_changes), getString(C0773R.string.discard), getString(C0773R.string.cancel), new C07752(), new C07763());
    }

    private void confirmSave() {
        if (!(this.form == null || this.form.getTrackedEntityInstance() == null)) {
            if (this.form.getTrackedEntityInstance().getLocalId() < 0) {
                this.form.getTrackedEntityInstance().setFromServer(false);
                this.form.getTrackedEntityInstance().save();
            }
            if (this.form.getEnrollment().getEvents() != null) {
                for (Event event : this.form.getEnrollment().getEvents()) {
                    event.setFromServer(false);
                    this.form.getEnrollment().setFromServer(false);
                    this.form.getTrackedEntityInstance().setFromServer(false);
                }
            }
            this.form.getEnrollment().setLocalTrackedEntityInstanceId(this.form.getTrackedEntityInstance().getLocalId());
            this.form.getEnrollment().setFromServer(false);
            this.form.getTrackedEntityInstance().setFromServer(false);
            this.form.getEnrollment().save();
            flagDataChanged(false);
        }
        for (ProgramTrackedEntityAttribute ptea : this.form.getProgram().getProgramTrackedEntityAttributes()) {
            if (ptea.getTrackedEntityAttribute().isGenerated()) {
                TrackedEntityAttributeGeneratedValue trackedEntityAttributeGeneratedValue = MetaDataController.getTrackedEntityAttributeGeneratedValue(TrackerController.getTrackedEntityAttributeValue(ptea.getTrackedEntityAttributeId(), this.form.getTrackedEntityInstance().getUid()).getValue());
                if (trackedEntityAttributeGeneratedValue != null) {
                    trackedEntityAttributeGeneratedValue.delete();
                } else {
                    trackedEntityAttributeGeneratedValue = MetaDataController.getTrackedEntityAttributeGeneratedValue(ptea.getTrackedEntityAttributeId());
                    if (trackedEntityAttributeGeneratedValue != null) {
                        trackedEntityAttributeGeneratedValue.delete();
                    }
                }
            }
        }
    }

    private void discardChanges() {
        if (this.form != null) {
            List<Event> events;
            if (this.originalEnrollment == null && this.form.getEnrollment() != null) {
                this.form.getEnrollment().delete();
                events = this.form.getEnrollment().getEvents();
                if (events != null) {
                    for (Event event : events) {
                        event.delete();
                    }
                }
            }
            if (!(this.originalEnrollment == null || this.originalEnrollment.getLocalId() == this.form.getEnrollment().getLocalId())) {
                this.form.getEnrollment().delete();
                events = this.form.getEnrollment().getEvents();
                if (events != null) {
                    for (Event event2 : events) {
                        event2.delete();
                    }
                }
            }
            if (!(this.originalEnrollment == null || this.originalEnrollment.equals(this.form.getEnrollment()))) {
                this.originalEnrollment.save();
            }
            if (this.originalTrackedEntityInstance == null && this.form.getTrackedEntityInstance() != null) {
                this.form.getTrackedEntityInstance().delete();
            }
            for (TrackedEntityAttributeValue newValue : this.form.getTrackedEntityAttributeValueMap().values()) {
                TrackedEntityAttributeValue originalValue = (TrackedEntityAttributeValue) this.originalTrackedEntityAttributeValueMap.get(newValue.getTrackedEntityAttributeId());
                if (originalValue == null) {
                    newValue.delete();
                } else if (newValue.getValue() != null || originalValue.getValue() != null) {
                    if (newValue.getValue() == null && originalValue.getValue() != null) {
                        originalValue.save();
                    } else if (!newValue.getValue().equals(originalValue.getValue())) {
                        originalValue.save();
                    }
                }
            }
        }
    }

    private boolean checkIfDataHasBeenEdited() {
        if (this.form == null || this.form.getEnrollment() == null) {
            return false;
        }
        if (this.originalEnrollment != null && !this.originalEnrollment.equals(this.form.getEnrollment())) {
            return true;
        }
        if (this.originalEnrollment != null && this.originalEnrollment.getLocalId() != this.form.getEnrollment().getLocalId()) {
            return true;
        }
        for (TrackedEntityAttributeValue newValue : this.form.getTrackedEntityAttributeValueMap().values()) {
            TrackedEntityAttributeValue originalValue = (TrackedEntityAttributeValue) this.originalTrackedEntityAttributeValueMap.get(newValue.getTrackedEntityAttributeId());
            if (originalValue == null) {
                return true;
            }
            if (newValue.getValue() != null || originalValue.getValue() != null) {
                if (newValue.getValue() == null && originalValue.getValue() != null) {
                    return true;
                }
                if (!newValue.getValue().equals(originalValue.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean doBack() {
        showConfirmDiscardDialog();
        return false;
    }

    public boolean onBackPressed() {
        showConfirmDiscardDialog();
        return false;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        showConfirmDiscardDialog();
        return true;
    }

    public void onDetach() {
        super.onDetach();
        GpsController.disableGps();
    }
}
