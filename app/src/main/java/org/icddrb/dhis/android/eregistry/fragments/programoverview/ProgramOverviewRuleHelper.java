package org.icddrb.dhis.android.eregistry.fragments.programoverview;

import android.support.v4.app.Fragment;
import java.util.ArrayList;
import java.util.List;
import org.icddrb.dhis.android.sdk.persistence.models.DataValue;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramRule;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramRuleAction;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.icddrb.dhis.android.sdk.ui.fragments.common.IProgramRuleFragmentHelper;

class ProgramOverviewRuleHelper implements IProgramRuleFragmentHelper {
    private ArrayList<String> hideProgramStages = new ArrayList();
    private ProgramOverviewFragment programOverviewFragment;

    ProgramOverviewRuleHelper(ProgramOverviewFragment programOverviewFragment) {
        this.programOverviewFragment = programOverviewFragment;
    }

    public ArrayList<String> getProgramRuleValidationErrors() {
        return null;
    }

    public ArrayList<String> getShowOnCompleteErrors() {
        return null;
    }

    public ArrayList<String> getShowOnCompleteWarningErrors() {
        return null;
    }

    public void recycle() {
        this.programOverviewFragment = null;
    }

    public void initiateEvaluateProgramRules() {
    }

    public void mapFieldsToRulesAndIndicators() {
    }

    public Fragment getFragment() {
        return this.programOverviewFragment;
    }

    public void updateUi() {
    }

    public List<ProgramRule> getProgramRules() {
        return this.programOverviewFragment.getForm().getProgram().getProgramRules();
    }

    public ArrayList<String> getHideProgramStages() {
        return this.hideProgramStages;
    }

    public Enrollment getEnrollment() {
        return this.programOverviewFragment.getForm().getEnrollment();
    }

    public Event getEvent() {
        return null;
    }

    public void applyShowWarningRuleAction(ProgramRuleAction programRuleAction) {
    }

    public void applyShowErrorRuleAction(ProgramRuleAction programRuleAction) {
    }

    public void applyHideSectionRuleAction(ProgramRuleAction programRuleAction) {
    }

    public void applyCreateEventRuleAction(ProgramRuleAction programRuleAction) {
    }

    public void applyDisplayKeyValuePairRuleAction(ProgramRuleAction programRuleAction) {
        this.programOverviewFragment.displayKeyValuePair(programRuleAction);
    }

    public void applyDisplayTextRuleAction(ProgramRuleAction programRuleAction) {
        this.programOverviewFragment.displayText(programRuleAction);
    }

    public void applySetMandatoryFieldRuleAction(ProgramRuleAction programRuleAction) {
    }

    public void applyHideProgramStageRuleAction(ProgramRuleAction programRuleAction) {
        if (!this.hideProgramStages.contains(programRuleAction.getProgramStage())) {
            this.hideProgramStages.add(programRuleAction.getProgramStage());
        }
    }

    public void applyWarningOnCompleteRuleAction(ProgramRuleAction programRuleAction) {
    }

    public void applyErrorOnCompleteRuleAction(ProgramRuleAction programRuleAction) {
    }

    public DataValue getDataElementValue(String uid) {
        return null;
    }

    public TrackedEntityAttributeValue getTrackedEntityAttributeValue(String uid) {
        return null;
    }

    public void flagDataChanged(boolean dataChanged) {
    }

    public void saveDataElement(String uid) {
    }

    public void saveTrackedEntityAttribute(String uid) {
    }

    public boolean blockingSpinnerNeeded() {
        return true;
    }

    public void applyHideFieldRuleAction(ProgramRuleAction programRuleAction, List affectedFieldsWithValue) {
    }

    public void disableCalculatedFields(ProgramRuleAction programRuleAction) {
    }

    public void applySendMessageRuleAction(ProgramRuleAction programRuleAction) {
    }

    public void showWarningHiddenValuesDialog(Fragment fragment, ArrayList affectedValues) {
    }
}
