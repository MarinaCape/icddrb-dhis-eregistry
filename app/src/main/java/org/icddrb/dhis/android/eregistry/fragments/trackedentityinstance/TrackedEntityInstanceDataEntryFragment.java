package org.icddrb.dhis.android.eregistry.fragments.trackedentityinstance;

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
import org.icddrb.dhis.android.sdk.persistence.models.ProgramRule;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramTrackedEntityAttribute;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeGeneratedValue;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.ui.activities.OnBackPressedListener;
import org.icddrb.dhis.android.sdk.ui.adapters.SectionAdapter;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.DataEntryRow;
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

public class TrackedEntityInstanceDataEntryFragment extends DataEntryFragment<TrackedEntityInstanceDataEntryFragmentForm> implements OnBackPressedListener {
    private static final String EMPTY_FIELD = "";
    public static final String EXTRA_NAVIGATION = "extra:Navigation";
    public static final String ORG_UNIT_ID = "extra:orgUnitId";
    public static final String PROGRAMRULES_FORCED_TRIGGER = "forced";
    public static final String PROGRAM_ID = "extra:ProgramId";
    public static final String TAG = TrackedEntityInstanceDataEntryFragment.class.getSimpleName();
    public static final String TRACKEDENTITYINSTANCE_ID = "extra:TrackedEntityInstanceId";
    public static boolean backNavigation;
    private TrackedEntityInstanceDataEntryFragmentForm form;
    private Map<String, TrackedEntityAttributeValue> originalTrackedEntityAttributeValueMap;
    private TrackedEntityInstance originalTrackedEntityInstance = null;
    private Map<String, List<ProgramRule>> programRulesForTrackedEntityAttributes;
    private SaveThread saveThread;

    /* renamed from: org.icddrb.dhis.android.eregistry.fragments.trackedentityinstance.TrackedEntityInstanceDataEntryFragment$1 */
    class C08281 implements OnClickListener {
        C08281() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            TrackedEntityInstanceDataEntryFragment.this.getActivity().finish();
        }
    }

    /* renamed from: org.icddrb.dhis.android.eregistry.fragments.trackedentityinstance.TrackedEntityInstanceDataEntryFragment$2 */
    class C08292 implements OnClickListener {
        C08292() {
        }

        public void onClick(DialogInterface dialog, int which) {
            TrackedEntityInstanceDataEntryFragment.this.discardChanges();
            TrackedEntityInstanceDataEntryFragment.this.getActivity().finish();
        }
    }

    /* renamed from: org.icddrb.dhis.android.eregistry.fragments.trackedentityinstance.TrackedEntityInstanceDataEntryFragment$3 */
    class C08303 implements OnClickListener {
        C08303() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    public TrackedEntityInstanceDataEntryFragment() {
        setProgramRuleFragmentHelper(new TrackedEntityInstanceDataEntryRuleHelper(this));
    }

    public static TrackedEntityInstanceDataEntryFragment newInstance(String unitId, String programId, String enrollmentDate, String incidentDate) {
        TrackedEntityInstanceDataEntryFragment fragment = new TrackedEntityInstanceDataEntryFragment();
        Bundle args = new Bundle();
        args.putString("extra:orgUnitId", unitId);
        args.putString("extra:ProgramId", programId);
        fragment.setArguments(args);
        return fragment;
    }

    public static TrackedEntityInstanceDataEntryFragment newInstance(String unitId, String programId, long trackedEntityInstanceId, String enrollmentDate, String incidentDate) {
        TrackedEntityInstanceDataEntryFragment fragment = new TrackedEntityInstanceDataEntryFragment();
        Bundle args = new Bundle();
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

    public Loader<TrackedEntityInstanceDataEntryFragmentForm> onCreateLoader(int id, Bundle args) {
        if (17 != id || !isAdded()) {
            return null;
        }
        List<Class<? extends Model>> modelsToTrack = new ArrayList();
        Bundle fragmentArguments = args.getBundle("extra:Arguments");
        return new DbLoader(getActivity().getBaseContext(), modelsToTrack, new TrackedEntityInstanceDataEntryFragmentQuery(fragmentArguments.getString("extra:orgUnitId"), fragmentArguments.getString("extra:ProgramId"), fragmentArguments.getLong("extra:TrackedEntityInstanceId", -1)));
    }

    public void onLoadFinished(Loader<TrackedEntityInstanceDataEntryFragmentForm> loader, TrackedEntityInstanceDataEntryFragmentForm data) {
        if (loader.getId() == 17 && isAdded()) {
            this.progressBar.setVisibility(8);
            this.listView.setVisibility(0);
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
                UiUtils.showErrorDialog(getActivity(), getString(C0773R.string.error_message), getString(C0773R.string.out_of_generated_ids), new C08281());
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

    public void onLoaderReset(Loader<TrackedEntityInstanceDataEntryFragmentForm> loader) {
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
                String dateOfEnrollmentDescription;
                if (this.form.getProgram().getEnrollmentDateLabel() == null) {
                    dateOfEnrollmentDescription = getString(C0773R.string.report_date);
                } else {
                    dateOfEnrollmentDescription = this.form.getProgram().getEnrollmentDateLabel();
                }
                if (!errors.containsKey(ErrorType.MANDATORY)) {
                    errors.put(ErrorType.MANDATORY, new ArrayList());
                }
                ((ArrayList) errors.get(ErrorType.MANDATORY)).add(dateOfEnrollmentDescription);
            }
            Map<String, ProgramTrackedEntityAttribute> dataElements = toMap(MetaDataController.getProgramTrackedEntityAttributes(this.form.getProgram().getUid()));
            for (TrackedEntityAttributeValue value : this.form.getEnrollment().getAttributes()) {
                ProgramTrackedEntityAttribute programTrackedEntityAttribute = (ProgramTrackedEntityAttribute) dataElements.get(value.getTrackedEntityAttributeId());
                if (programTrackedEntityAttribute.getMandatory() && StringUtils.isEmpty(value.getValue())) {
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
        if (this.form.getProgram() == null || this.form.getOrganisationUnit() == null) {
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
            HolderActivity.mCallBack.onSuccess();
            getActivity().finish();
        }
    }

    private boolean validate() {
        if (isMapEmpty(this.form.getTrackedEntityAttributeValueMap())) {
            UiUtils.showErrorDialog(getActivity(), getContext().getString(C0773R.string.error_message), getContext().getString(C0773R.string.profile_form_empty));
            return false;
        }
        HashMap<ErrorType, ArrayList<String>> allErrors = getValidationErrors();
        allErrors.put(ErrorType.PROGRAM_RULE, getProgramRuleFragmentHelper().getProgramRuleValidationErrors());
        allErrors.put(ErrorType.INVALID_FIELD, new ArrayList());
        for (DataEntryRow dataEntryRow : this.form.getDataEntryRows()) {
            if (dataEntryRow.getValidationError() != null) {
                ((ArrayList) allErrors.get(ErrorType.INVALID_FIELD)).add(getContext().getString(dataEntryRow.getValidationError().intValue()));
            }
        }
        if ((allErrors == null || allErrors.get(ErrorType.INVALID_FIELD) == null || ((ArrayList) allErrors.get(ErrorType.INVALID_FIELD)).size() <= 0) && ((allErrors.get(ErrorType.MANDATORY) == null || ((ArrayList) allErrors.get(ErrorType.MANDATORY)).size() <= 0) && (allErrors.get(ErrorType.PROGRAM_RULE) == null || ((ArrayList) allErrors.get(ErrorType.PROGRAM_RULE)).size() <= 0))) {
            return true;
        }
        showValidationErrorDialog(allErrors);
        return false;
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

    public TrackedEntityInstanceDataEntryFragmentForm getForm() {
        return this.form;
    }

    public void setForm(TrackedEntityInstanceDataEntryFragmentForm form) {
        this.form = form;
    }

    public Map<String, List<ProgramRule>> getProgramRulesForTrackedEntityAttributes() {
        return this.programRulesForTrackedEntityAttributes;
    }

    public void setProgramRulesForTrackedEntityAttributes(Map<String, List<ProgramRule>> programRulesForTrackedEntityAttributes) {
        this.programRulesForTrackedEntityAttributes = programRulesForTrackedEntityAttributes;
    }

    private void showConfirmDiscardDialog() {
        UiUtils.showConfirmDialog(getActivity(), getString(C0773R.string.discard), getString(C0773R.string.discard_confirm_changes), getString(C0773R.string.discard), getString(C0773R.string.cancel), new C08292(), new C08303());
    }

    private void confirmSave() {
        if (!(this.form == null || this.form.getTrackedEntityInstance() == null)) {
            if (this.form.getTrackedEntityInstance().getLocalId() < 0) {
                this.form.getTrackedEntityInstance().setFromServer(false);
                this.form.getTrackedEntityInstance().save();
            }
            for (String key : this.form.getTrackedEntityAttributeValueMap().keySet()) {
                TrackedEntityAttributeValue trackedEntityAttributeValue = (TrackedEntityAttributeValue) this.form.getTrackedEntityAttributeValueMap().get(key);
                trackedEntityAttributeValue.setLocalTrackedEntityInstanceId(this.form.getTrackedEntityInstance().getLocalId());
                trackedEntityAttributeValue.setTrackedEntityInstanceId(this.form.getTrackedEntityInstance().getTrackedEntityInstance());
                if (!(trackedEntityAttributeValue.getValue() == null || trackedEntityAttributeValue.getValue().equals(""))) {
                    trackedEntityAttributeValue.save();
                }
            }
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
