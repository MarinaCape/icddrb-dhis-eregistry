package org.icddrb.dhis.android.eregistry.fragments.trackedentityinstance;

import android.support.v4.app.Fragment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.icddrb.dhis.android.eregistry.C0773R;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.persistence.models.DataValue;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramRule;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramRuleAction;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttribute;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.icddrb.dhis.android.sdk.ui.fragments.common.IProgramRuleFragmentHelper;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.DataEntryFragment;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.ValidationErrorDialog;
import org.icddrb.dhis.android.sdk.utils.services.ProgramRuleService;

public class TrackedEntityInstanceDataEntryRuleHelper implements IProgramRuleFragmentHelper {
    private TrackedEntityInstanceDataEntryFragment mTrackedEntityInstanceDataEntryFragment;
    private ArrayList<String> programRuleValidationErrors = new ArrayList();

    public TrackedEntityInstanceDataEntryRuleHelper(TrackedEntityInstanceDataEntryFragment trackedEntityInstanceDataEntryFragment) {
        this.mTrackedEntityInstanceDataEntryFragment = trackedEntityInstanceDataEntryFragment;
    }

    public ArrayList<String> getProgramRuleValidationErrors() {
        return this.programRuleValidationErrors;
    }

    public ArrayList<String> getShowOnCompleteErrors() {
        return null;
    }

    public ArrayList<String> getShowOnCompleteWarningErrors() {
        return null;
    }

    public ArrayList<String> getHideProgramStages() {
        return null;
    }

    public void recycle() {
        this.mTrackedEntityInstanceDataEntryFragment = null;
    }

    public void initiateEvaluateProgramRules() {
        this.programRuleValidationErrors.clear();
        this.mTrackedEntityInstanceDataEntryFragment.initiateEvaluateProgramRules();
    }

    public void mapFieldsToRulesAndIndicators() {
        this.mTrackedEntityInstanceDataEntryFragment.setProgramRulesForTrackedEntityAttributes(new HashMap());
        for (ProgramRule programRule : this.mTrackedEntityInstanceDataEntryFragment.getForm().getProgram().getProgramRules()) {
            for (String trackedEntityAttribute : ProgramRuleService.getTrackedEntityAttributesInRule(programRule)) {
                List<ProgramRule> rulesForTrackedEntityAttribute = (List) this.mTrackedEntityInstanceDataEntryFragment.getProgramRulesForTrackedEntityAttributes().get(trackedEntityAttribute);
                if (rulesForTrackedEntityAttribute == null) {
                    rulesForTrackedEntityAttribute = new ArrayList();
                    rulesForTrackedEntityAttribute.add(programRule);
                    this.mTrackedEntityInstanceDataEntryFragment.getProgramRulesForTrackedEntityAttributes().put(trackedEntityAttribute, rulesForTrackedEntityAttribute);
                } else {
                    rulesForTrackedEntityAttribute.add(programRule);
                }
            }
        }
    }

    public Fragment getFragment() {
        return this.mTrackedEntityInstanceDataEntryFragment;
    }

    public List<ProgramRule> getProgramRules() {
        return this.mTrackedEntityInstanceDataEntryFragment.getForm().getProgram().getProgramRules();
    }

    public Event getEvent() {
        return null;
    }

    public void applyCreateEventRuleAction(ProgramRuleAction programRuleAction) {
    }

    public void applySendMessageRuleAction(ProgramRuleAction programRuleAction) {
    }

    public void applyDisplayKeyValuePairRuleAction(ProgramRuleAction programRuleAction) {
    }

    public void applyDisplayTextRuleAction(ProgramRuleAction programRuleAction) {
    }

    public void applySetMandatoryFieldRuleAction(ProgramRuleAction programRuleAction) {
        this.mTrackedEntityInstanceDataEntryFragment.getListViewAdapter().addMandatoryOnIndex(programRuleAction.getTrackedEntityAttribute());
    }

    public void applyHideProgramStageRuleAction(ProgramRuleAction programRuleAction) {
    }

    public void applyWarningOnCompleteRuleAction(ProgramRuleAction programRuleAction) {
    }

    public void applyErrorOnCompleteRuleAction(ProgramRuleAction programRuleAction) {
    }

    public Enrollment getEnrollment() {
        return this.mTrackedEntityInstanceDataEntryFragment.getForm().getEnrollment();
    }

    public TrackedEntityAttributeValue getTrackedEntityAttributeValue(String id) {
        return (TrackedEntityAttributeValue) this.mTrackedEntityInstanceDataEntryFragment.getForm().getTrackedEntityAttributeValueMap().get(id);
    }

    public DataValue getDataElementValue(String uid) {
        return null;
    }

    public void saveDataElement(String uid) {
    }

    public void saveTrackedEntityAttribute(String id) {
        this.mTrackedEntityInstanceDataEntryFragment.getSaveThread().schedule();
    }

    public boolean blockingSpinnerNeeded() {
        return true;
    }

    public void updateUi() {
    }

    public void showWarningHiddenValuesDialog(Fragment fragment, ArrayList<String> affectedValues) {
        ArrayList<String> trackedEntityAttributeNames = new ArrayList();
        Iterator it = affectedValues.iterator();
        while (it.hasNext()) {
            TrackedEntityAttribute tea = MetaDataController.getTrackedEntityAttribute((String) it.next());
            if (tea != null) {
                trackedEntityAttributeNames.add(tea.getDisplayName());
            }
        }
        if (!trackedEntityAttributeNames.isEmpty()) {
            if (this.mTrackedEntityInstanceDataEntryFragment.getValidationErrorDialog() == null || !this.mTrackedEntityInstanceDataEntryFragment.getValidationErrorDialog().isVisible()) {
                this.mTrackedEntityInstanceDataEntryFragment.setValidationErrorDialog(ValidationErrorDialog.newInstance(fragment.getString(C0773R.string.warning_hidefieldwithvalue), trackedEntityAttributeNames));
                if (fragment.isAdded()) {
                    this.mTrackedEntityInstanceDataEntryFragment.getValidationErrorDialog().show(fragment.getChildFragmentManager());
                }
            }
        }
    }

    public void flagDataChanged(boolean hasChanged) {
        this.mTrackedEntityInstanceDataEntryFragment.flagDataChanged(hasChanged);
    }

    public void applyShowWarningRuleAction(ProgramRuleAction programRuleAction) {
        String uid = programRuleAction.getDataElement();
        if (uid == null) {
            uid = programRuleAction.getTrackedEntityAttribute();
        }
        this.mTrackedEntityInstanceDataEntryFragment.getListViewAdapter().showWarningOnIndex(uid, programRuleAction.getContent());
    }

    public void applyShowErrorRuleAction(ProgramRuleAction programRuleAction) {
        String uid = programRuleAction.getTrackedEntityAttribute();
        this.mTrackedEntityInstanceDataEntryFragment.getListViewAdapter().showErrorOnIndex(uid, programRuleAction.getContent());
        if (!this.programRuleValidationErrors.contains(programRuleAction.getContent())) {
            String stringValue;
            TrackedEntityAttributeValue value = getTrackedEntityAttributeValue(uid);
            if (value != null) {
                stringValue = value.getValue();
            } else {
                stringValue = "";
            }
            this.programRuleValidationErrors.add(programRuleAction.getContent() + " " + stringValue);
        }
    }

    public void applyHideFieldRuleAction(ProgramRuleAction programRuleAction, List<String> affectedFieldsWithValue) {
        this.mTrackedEntityInstanceDataEntryFragment.getListViewAdapter().hideIndex(programRuleAction.getTrackedEntityAttribute());
        TrackedEntityInstanceDataEntryFragment trackedEntityInstanceDataEntryFragment = this.mTrackedEntityInstanceDataEntryFragment;
        if (DataEntryFragment.containsValue(getTrackedEntityAttributeValue(programRuleAction.getTrackedEntityAttribute()))) {
            affectedFieldsWithValue.add(programRuleAction.getTrackedEntityAttribute());
        }
    }

    public void disableCalculatedFields(ProgramRuleAction programRuleAction) {
        this.mTrackedEntityInstanceDataEntryFragment.getListViewAdapter().disableIndex(programRuleAction.getTrackedEntityAttribute());
    }

    public void applyHideSectionRuleAction(ProgramRuleAction programRuleAction) {
    }
}
