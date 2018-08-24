package org.icddrb.dhis.android.eregistry.fragments.trackedentityinstanceprofile;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuInflater;
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
import org.icddrb.dhis.android.sdk.controllers.DhisController;
import org.icddrb.dhis.android.sdk.controllers.ErrorType;
import org.icddrb.dhis.android.sdk.controllers.GpsController;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.controllers.tracker.TrackerController;
import org.icddrb.dhis.android.sdk.persistence.loaders.DbLoader;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramRule;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramTrackedEntityAttribute;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.ui.activities.OnBackPressedListener;
import org.icddrb.dhis.android.sdk.ui.adapters.SectionAdapter;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.DataEntryRow;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.Row;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.RunProgramRulesEvent;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.events.OnDetailedInfoButtonClick;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.DataEntryFragment;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.HideLoadingDialogEvent;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.RefreshListViewEvent;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.RowValueChangedEvent;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.SaveThread;
import org.icddrb.dhis.android.sdk.utils.UiUtils;

public class TrackedEntityInstanceProfileFragment extends DataEntryFragment<TrackedEntityInstanceProfileFragmentForm> implements OnBackPressedListener {
    private static final String EXTRA_ARGUMENTS = "extra:Arguments";
    private static final String EXTRA_SAVED_INSTANCE_STATE = "extra:savedInstanceState";
    private static final int LOADER_ID = 95640;
    private static final String ORG_UNIT_ID = "extra:orgUnitId";
    public static final String PROGRAM_ID = "extra:ProgramId";
    public static final String TAG = TrackedEntityInstanceProfileFragment.class.getName();
    public static final String TRACKEDENTITYINSTANCE_ID = "extra:TrackedEntityInstanceId";
    private static final String TRACKEDENTITYINSTANCE_ORIGINAL = "extra:OriginalTEI";
    private boolean edit;
    private MenuItem editFormButton;
    private boolean editableDataEntryRows;
    private TrackedEntityInstanceProfileFragmentForm form;
    private TrackedEntityInstance originalTrackedEntityInstance = null;
    private Map<String, List<ProgramRule>> programRulesForTrackedEntityAttributes;
    private SaveThread saveThread;

    /* renamed from: org.icddrb.dhis.android.eregistry.fragments.trackedentityinstanceprofile.TrackedEntityInstanceProfileFragment$1 */
    class C08311 implements OnClickListener {
        C08311() {
        }

        public void onClick(DialogInterface dialog, int which) {
            if (TrackedEntityInstanceProfileFragment.this.validate()) {
                TrackedEntityInstanceProfileFragment.this.onDetach();
                DhisController.hasUnSynchronizedDatavalues = true;
                TrackedEntityInstanceProfileFragment.this.getActivity().finish();
            }
        }
    }

    /* renamed from: org.icddrb.dhis.android.eregistry.fragments.trackedentityinstanceprofile.TrackedEntityInstanceProfileFragment$2 */
    class C08322 implements OnClickListener {
        C08322() {
        }

        public void onClick(DialogInterface dialog, int which) {
            TrackedEntityInstanceProfileFragment.this.onDetach();
            TrackedEntityInstanceProfileFragment.this.getActivity().finish();
        }
    }

    /* renamed from: org.icddrb.dhis.android.eregistry.fragments.trackedentityinstanceprofile.TrackedEntityInstanceProfileFragment$3 */
    class C08333 implements OnClickListener {
        C08333() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    /* renamed from: org.icddrb.dhis.android.eregistry.fragments.trackedentityinstanceprofile.TrackedEntityInstanceProfileFragment$4 */
    class C08344 implements OnClickListener {
        C08344() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            TrackedEntityInstanceProfileFragment.this.getActivity().finish();
        }
    }

    public TrackedEntityInstanceProfileFragment() {
        setProgramRuleFragmentHelper(new TrackedEntityInstanceProfileRuleHelper(this));
    }

    public static TrackedEntityInstanceProfileFragment newInstance(long mTrackedEntityInstanceId, String mProgramId, Long enrollmentId) {
        TrackedEntityInstanceProfileFragment fragment = new TrackedEntityInstanceProfileFragment();
        Bundle fragmentArgs = new Bundle();
        fragmentArgs.putLong("extra:TrackedEntityInstanceId", mTrackedEntityInstanceId);
        fragmentArgs.putString("extra:ProgramId", mProgramId);
        fragment.setArguments(fragmentArgs);
        return fragment;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setTitle(getString(C0773R.string.profile));
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.saveThread == null || this.saveThread.isKilled()) {
            this.saveThread = new SaveThread();
            this.saveThread.start();
        }
        this.saveThread.init(this);
        setHasOptionsMenu(true);
        this.editableDataEntryRows = false;
    }

    public void toggleEditFormButtonIcon(boolean editing) {
        if (editing) {
            this.editFormButton.setIcon(C0773R.drawable.ic_save);
        } else {
            this.editFormButton.setIcon(C0773R.drawable.ic_edit);
        }
        this.editFormButton.getIcon().setAlpha(255);
    }

    public void onDestroy() {
        this.saveThread.kill();
        super.onDestroy();
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(C0773R.menu.menu_data_entry, menu);
        this.editFormButton = menu.findItem(C0773R.id.action_new_event);
        this.editFormButton.setEnabled(true);
        toggleEditFormButtonIcon(false);
        this.editFormButton.getIcon().setAlpha(255);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        boolean z = false;
        if (menuItem.getItemId() == 16908332) {
            doBack();
            return true;
        } else if (menuItem.getItemId() != C0773R.id.action_new_event) {
            return super.onOptionsItemSelected(menuItem);
        } else {
            if (this.editableDataEntryRows) {
                setEditableDataEntryRows(false);
            } else {
                setEditableDataEntryRows(true);
            }
            if (!this.editableDataEntryRows) {
                z = true;
            }
            this.editableDataEntryRows = z;
            proceed();
            return true;
        }
    }

    public boolean doBack() {
        if (this.edit) {
            UiUtils.showConfirmDialog(getActivity(), getString(C0773R.string.discard), getString(C0773R.string.discard_confirm_changes), getString(C0773R.string.save_and_close), getString(C0773R.string.discard), getString(C0773R.string.cancel), new C08311(), new C08322(), new C08333());
        } else {
            onDetach();
            getActivity().finish();
        }
        return false;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle argumentsBundle = new Bundle();
        argumentsBundle.putBundle("extra:Arguments", getArguments());
        argumentsBundle.putBundle("extra:savedInstanceState", savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, argumentsBundle, this);
        this.progressBar.setVisibility(0);
        this.listView.setVisibility(8);
    }

    public Loader<TrackedEntityInstanceProfileFragmentForm> onCreateLoader(int id, Bundle args) {
        if (LOADER_ID != id || !isAdded()) {
            return null;
        }
        List<Class<? extends Model>> modelsToTrack = new ArrayList();
        Bundle fragmentArguments = args.getBundle("extra:Arguments");
        String programId = fragmentArguments.getString("extra:ProgramId");
        return new DbLoader(getActivity().getBaseContext(), modelsToTrack, new TrackedEntityInstanceProfileFragmentQuery(fragmentArguments.getLong("extra:TrackedEntityInstanceId", -1), programId, fragmentArguments.getString("extra:orgUnitId", null)));
    }

    public void onLoadFinished(Loader<TrackedEntityInstanceProfileFragmentForm> loader, TrackedEntityInstanceProfileFragmentForm data) {
        if (loader.getId() == LOADER_ID && isAdded()) {
            this.progressBar.setVisibility(8);
            this.listView.setVisibility(0);
            this.form = data;
            this.listViewAdapter.swapData(this.form.getDataEntryRows());
            this.programRuleFragmentHelper.mapFieldsToRulesAndIndicators();
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(TRACKEDENTITYINSTANCE_ORIGINAL, this.originalTrackedEntityInstance);
    }

    public void onLoaderReset(Loader<TrackedEntityInstanceProfileFragmentForm> loader) {
        if (this.listViewAdapter != null) {
            this.listViewAdapter.swapData(null);
        }
    }

    public void setEditableDataEntryRows(boolean editable) {
        this.listViewAdapter.swapData(null);
        List<Row> rows = new ArrayList(this.form.getDataEntryRows());
        for (Row row : rows) {
            if (!row.isShouldNeverBeEdited()) {
                row.setEditable(editable);
            }
        }
        if (editable) {
            if (this.form.getTrackedEntityInstance().getLocalId() >= 0) {
                this.originalTrackedEntityInstance = new TrackedEntityInstance(this.form.getTrackedEntityInstance());
            }
            if (this.form.isOutOfTrackedEntityAttributeGeneratedValues()) {
                for (Row row2 : this.form.getDataEntryRows()) {
                    row2.setEditable(false);
                }
                UiUtils.showErrorDialog(getActivity(), getString(C0773R.string.error_message), getString(C0773R.string.out_of_generated_ids), new C08344());
            }
        }
        this.listViewAdapter.swapData(rows);
        this.listView.setAdapter(this.listViewAdapter);
        if (editable) {
            initiateEvaluateProgramRules();
        }
        toggleEditFormButtonIcon(editable);
    }

    public void flagDataChanged(boolean changed) {
        this.edit = changed;
    }

    @Subscribe
    public void onRowValueChanged(RowValueChangedEvent event) {
        flagDataChanged(true);
        if (this.form != null) {
            if (event.getRow() == null || !event.getRow().isEditTextRow()) {
                evaluateRules(event.getId());
            }
            this.saveThread.schedule();
        }
    }

    @Subscribe
    public void onRunProgramRules(RunProgramRulesEvent event) {
        evaluateRules(event.getId());
    }

    private void evaluateRules(String trackedEntityAttribute) {
        if (trackedEntityAttribute != null && this.form != null && hasRules(trackedEntityAttribute)) {
            getProgramRuleFragmentHelper().getProgramRuleValidationErrors().clear();
            initiateEvaluateProgramRules();
        }
    }

    public void initiateEvaluateProgramRules() {
        if (this.rulesEvaluatorThread != null) {
            this.rulesEvaluatorThread.schedule();
        }
    }

    private boolean hasRules(String trackedEntityAttribute) {
        if (this.programRulesForTrackedEntityAttributes == null) {
            return false;
        }
        return this.programRulesForTrackedEntityAttributes.containsKey(trackedEntityAttribute);
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

    public TrackedEntityInstanceProfileFragmentForm getForm() {
        return this.form;
    }

    public void setForm(TrackedEntityInstanceProfileFragmentForm form) {
        this.form = form;
    }

    public Map<String, List<ProgramRule>> getProgramRulesForTrackedEntityAttributes() {
        return this.programRulesForTrackedEntityAttributes;
    }

    public void setProgramRulesForTrackedEntityAttributes(Map<String, List<ProgramRule>> programRulesForTrackedEntityAttributes) {
        this.programRulesForTrackedEntityAttributes = programRulesForTrackedEntityAttributes;
    }

    public SectionAdapter getSpinnerAdapter() {
        return null;
    }

    public void onPrepareOptionsMenu(Menu menu) {
    }

    protected HashMap<ErrorType, ArrayList<String>> getValidationErrors() {
        HashMap<ErrorType, ArrayList<String>> errors = new HashMap();
        if (!(this.form.getEnrollment() == null || this.form.getProgram() == null)) {
            if (StringUtils.isEmpty(this.form.getEnrollment().getEnrollmentDate())) {
                String dateOfEnrollmentDescription = this.form.getProgram().getEnrollmentDateLabel() == null ? getString(C0773R.string.report_date) : this.form.getProgram().getEnrollmentDateLabel();
                if (!errors.containsKey(ErrorType.MANDATORY)) {
                    errors.put(ErrorType.MANDATORY, new ArrayList());
                }
                ((ArrayList) errors.get(ErrorType.MANDATORY)).add(dateOfEnrollmentDescription);
            }
            Map<String, ProgramTrackedEntityAttribute> dataElements = toMap(MetaDataController.getProgramTrackedEntityAttributes(this.form.getProgram().getUid()));
            for (TrackedEntityAttributeValue value : this.form.getTrackedEntityInstance().getAttributes()) {
                ProgramTrackedEntityAttribute programTrackedEntityAttribute = (ProgramTrackedEntityAttribute) dataElements.get(value.getTrackedEntityAttributeId());
                if (programTrackedEntityAttribute.getMandatory() && StringUtils.isEmpty(value.getValue())) {
                    if (!errors.containsKey(ErrorType.MANDATORY)) {
                        errors.put(ErrorType.MANDATORY, new ArrayList());
                    }
                    ((ArrayList) errors.get(ErrorType.MANDATORY)).add(programTrackedEntityAttribute.getTrackedEntityAttribute().getName());
                }
                if (!(!programTrackedEntityAttribute.getTrackedEntityAttribute().isUnique() || value.getValue() == null || value.getValue().isEmpty() || TrackerController.countTrackedEntityAttributeValue(value) == 0)) {
                    if (!errors.containsKey(ErrorType.UNIQUE)) {
                        errors.put(ErrorType.UNIQUE, new ArrayList());
                    }
                    ((ArrayList) errors.get(ErrorType.UNIQUE)).add(programTrackedEntityAttribute.getTrackedEntityAttribute().getName());
                }
            }
        }
        return errors;
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

    protected boolean isValid() {
        if (this.form.getEnrollment() == null || this.form.getProgram() == null) {
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

    protected void save() {
    }

    protected void proceed() {
        if (this.edit && validate() && this.form != null && isAdded() && this.form.getTrackedEntityInstance() != null) {
            for (TrackedEntityAttributeValue val : this.form.getTrackedEntityAttributeValues()) {
                val.save();
            }
            this.form.getTrackedEntityInstance().setFromServer(false);
            this.form.getTrackedEntityInstance().save();
            flagDataChanged(false);
        }
    }

    private boolean validate() {
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
    }

    @Subscribe
    public void onDetailedInfoClick(OnDetailedInfoButtonClick eventClick) {
        super.onShowDetailedInfo(eventClick);
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    }

    public void onDetach() {
        super.onDetach();
        GpsController.disableGps();
    }
}
