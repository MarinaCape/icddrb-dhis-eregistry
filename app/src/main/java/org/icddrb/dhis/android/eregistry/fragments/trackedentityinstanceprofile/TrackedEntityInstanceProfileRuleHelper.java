package org.icddrb.dhis.android.eregistry.fragments.trackedentityinstanceprofile;

import android.support.v4.app.Fragment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.icddrb.dhis.android.eregistry.C0773R;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.persistence.models.DataElement;
import org.icddrb.dhis.android.sdk.persistence.models.DataValue;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramRule;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramRuleAction;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.icddrb.dhis.android.sdk.ui.fragments.common.IProgramRuleFragmentHelper;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.DataEntryFragment;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.ValidationErrorDialog;
import org.icddrb.dhis.android.sdk.utils.services.ProgramRuleService;

public class TrackedEntityInstanceProfileRuleHelper implements IProgramRuleFragmentHelper {
    private TrackedEntityInstanceProfileFragment fragment;
    private ArrayList<String> programRuleValidationErrors = new ArrayList();

    public TrackedEntityInstanceProfileRuleHelper(TrackedEntityInstanceProfileFragment fragment) {
        this.fragment = fragment;
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
        this.fragment = null;
        this.programRuleValidationErrors.clear();
        this.programRuleValidationErrors = null;
    }

    public void initiateEvaluateProgramRules() {
        this.programRuleValidationErrors.clear();
        this.fragment.initiateEvaluateProgramRules();
    }

    public void mapFieldsToRulesAndIndicators() {
        this.fragment.setProgramRulesForTrackedEntityAttributes(new HashMap());
        for (ProgramRule programRule : this.fragment.getForm().getProgram().getProgramRules()) {
            for (String trackedEntityAttribute : ProgramRuleService.getTrackedEntityAttributesInRule(programRule)) {
                List<ProgramRule> rulesForTrackedEntityAttribute = (List) this.fragment.getProgramRulesForTrackedEntityAttributes().get(trackedEntityAttribute);
                if (rulesForTrackedEntityAttribute == null) {
                    rulesForTrackedEntityAttribute = new ArrayList();
                    rulesForTrackedEntityAttribute.add(programRule);
                    this.fragment.getProgramRulesForTrackedEntityAttributes().put(trackedEntityAttribute, rulesForTrackedEntityAttribute);
                } else {
                    rulesForTrackedEntityAttribute.add(programRule);
                }
            }
        }
    }

    public Fragment getFragment() {
        return this.fragment;
    }

    public void showWarningHiddenValuesDialog(Fragment parentFragment, ArrayList<String> affectedValues) {
        ArrayList<String> dataElementNames = new ArrayList();
        Iterator it = affectedValues.iterator();
        while (it.hasNext()) {
            DataElement de = MetaDataController.getDataElement((String) it.next());
            if (de != null) {
                dataElementNames.add(de.getDisplayName());
            }
        }
        if (!dataElementNames.isEmpty()) {
            if (this.fragment.getValidationErrorDialog() == null || !this.fragment.getValidationErrorDialog().isVisible()) {
                this.fragment.setValidationErrorDialog(ValidationErrorDialog.newInstance(parentFragment.getString(C0773R.string.warning_hidefieldwithvalue), dataElementNames));
                if (parentFragment.isAdded()) {
                    this.fragment.getValidationErrorDialog().show(parentFragment.getChildFragmentManager());
                }
            }
        }
    }

    public void updateUi() {
    }

    public List<ProgramRule> getProgramRules() {
        return this.fragment.getForm().getProgram().getProgramRules();
    }

    public Enrollment getEnrollment() {
        return this.fragment.getForm().getEnrollment();
    }

    public Event getEvent() {
        return null;
    }

    public void applyShowWarningRuleAction(ProgramRuleAction programRuleAction) {
        String uid = programRuleAction.getDataElement();
        if (uid == null) {
            uid = programRuleAction.getTrackedEntityAttribute();
        }
        this.fragment.getListViewAdapter().showWarningOnIndex(uid, programRuleAction.getContent());
    }

    public void applyShowErrorRuleAction(ProgramRuleAction programRuleAction) {
        String uid = programRuleAction.getDataElement();
        if (uid == null) {
            uid = programRuleAction.getTrackedEntityAttribute();
        }
        this.fragment.getListViewAdapter().showErrorOnIndex(uid, programRuleAction.getContent());
        if (!this.programRuleValidationErrors.contains(programRuleAction.getContent())) {
            this.programRuleValidationErrors.add(programRuleAction.getContent() + " " + getTrackedEntityAttributeValue(uid).getValue());
        }
    }

    public void applyHideFieldRuleAction(ProgramRuleAction programRuleAction, List<String> affectedFieldsWithValue) {
        this.fragment.getListViewAdapter().hideIndex(programRuleAction.getDataElement());
        TrackedEntityInstanceProfileFragment trackedEntityInstanceProfileFragment = this.fragment;
        if (DataEntryFragment.containsValue(getDataElementValue(programRuleAction.getDataElement()))) {
            affectedFieldsWithValue.add(programRuleAction.getDataElement());
        }
    }

    public void disableCalculatedFields(ProgramRuleAction programRuleAction) {
        this.fragment.getListViewAdapter().disableIndex(programRuleAction.getDataElement());
    }

    public void applySendMessageRuleAction(ProgramRuleAction programRuleAction) {
    }

    public void applyHideSectionRuleAction(ProgramRuleAction programRuleAction) {
    }

    public void applyCreateEventRuleAction(ProgramRuleAction programRuleAction) {
    }

    public void applyDisplayKeyValuePairRuleAction(ProgramRuleAction programRuleAction) {
    }

    public void applyDisplayTextRuleAction(ProgramRuleAction programRuleAction) {
    }

    public void applySetMandatoryFieldRuleAction(ProgramRuleAction programRuleAction) {
        this.fragment.getListViewAdapter().addMandatoryOnIndex(programRuleAction.getTrackedEntityAttribute());
    }

    public void applyHideProgramStageRuleAction(ProgramRuleAction programRuleAction) {
    }

    public void applyWarningOnCompleteRuleAction(ProgramRuleAction programRuleAction) {
    }

    public void applyErrorOnCompleteRuleAction(ProgramRuleAction programRuleAction) {
    }

    public DataValue getDataElementValue(String uid) {
        return null;
    }

    public TrackedEntityAttributeValue getTrackedEntityAttributeValue(String uid) {
        return (TrackedEntityAttributeValue) this.fragment.getForm().getTrackedEntityAttributeValueMap().get(uid);
    }

    public void flagDataChanged(boolean dataChanged) {
        this.fragment.flagDataChanged(dataChanged);
    }

    public void saveDataElement(String uid) {
    }

    public void saveTrackedEntityAttribute(String uid) {
        this.fragment.getSaveThread().schedule();
    }

    public boolean blockingSpinnerNeeded() {
        return true;
    }
}
