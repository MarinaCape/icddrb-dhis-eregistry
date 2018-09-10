package org.icddrb.dhis.android.sdk.ui.fragments.eventdataentry;

import android.support.v4.app.Fragment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.icddrb.dhis.android.sdk.R;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.models.DataElement;
import org.icddrb.dhis.android.sdk.persistence.models.DataValue;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramIndicator;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramRule;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramRuleAction;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.icddrb.dhis.android.sdk.ui.fragments.common.IProgramRuleFragmentHelper;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.DataEntryFragment;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.RowValueChangedEvent;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.ValidationErrorDialog;
import org.icddrb.dhis.android.sdk.utils.api.ProgramRuleActionType;
import org.icddrb.dhis.android.sdk.utils.services.ProgramIndicatorService;
import org.icddrb.dhis.android.sdk.utils.services.ProgramRuleService;

public class EventDataEntryRuleHelper implements IProgramRuleFragmentHelper {
    private EventDataEntryFragment eventDataEntryFragment;
    private ArrayList<String> programRuleValidationErrors = new ArrayList();
    private ArrayList<String> showOnCompleteErrors = new ArrayList();
    private ArrayList<String> showOnCompleteWarnings = new ArrayList();

    public EventDataEntryRuleHelper(EventDataEntryFragment eventDataEntryFragment) {
        this.eventDataEntryFragment = eventDataEntryFragment;
    }

    public ArrayList<String> getProgramRuleValidationErrors() {
        return this.programRuleValidationErrors;
    }

    public ArrayList<String> getShowOnCompleteErrors() {
        return this.showOnCompleteErrors;
    }

    public ArrayList<String> getShowOnCompleteWarningErrors() {
        return this.showOnCompleteWarnings;
    }

    public ArrayList<String> getHideProgramStages() {
        return null;
    }

    public void recycle() {
        this.eventDataEntryFragment = null;
    }

    public void initiateEvaluateProgramRules() {
        this.eventDataEntryFragment.initiateEvaluateProgramRules();
    }

    public void mapFieldsToRulesAndIndicators() {
        this.eventDataEntryFragment.setProgramRulesForDataElements(new HashMap());
        this.eventDataEntryFragment.setProgramIndicatorsForDataElements(new HashMap());
        for (ProgramRule programRule : this.eventDataEntryFragment.getForm().getStage().getProgram().getProgramRules()) {
            for (String dataElement : ProgramRuleService.getDataElementsInRule(programRule)) {
                List<ProgramRule> rulesForDataElement = (List) this.eventDataEntryFragment.getProgramRulesForDataElements().get(dataElement);
                if (rulesForDataElement == null) {
                    rulesForDataElement = new ArrayList();
                    rulesForDataElement.add(programRule);
                    this.eventDataEntryFragment.getProgramRulesForDataElements().put(dataElement, rulesForDataElement);
                } else {
                    rulesForDataElement.add(programRule);
                }
            }
        }
        for (ProgramIndicator programIndicator : this.eventDataEntryFragment.getForm().getStage().getProgramIndicators()) {
            for (String dataElement2 : ProgramIndicatorService.getDataElementsInExpression(programIndicator)) {
                List<ProgramIndicator> programIndicatorsForDataElement = (List) this.eventDataEntryFragment.getProgramIndicatorsForDataElements().get(dataElement2);
                if (programIndicatorsForDataElement == null) {
                    programIndicatorsForDataElement = new ArrayList();
                    programIndicatorsForDataElement.add(programIndicator);
                    this.eventDataEntryFragment.getProgramIndicatorsForDataElements().put(dataElement2, programIndicatorsForDataElement);
                } else {
                    programIndicatorsForDataElement.add(programIndicator);
                }
            }
        }
    }

    public Fragment getFragment() {
        return this.eventDataEntryFragment;
    }

    public List<ProgramRule> getProgramRules() {
        List<ProgramRule> programRules = new ArrayList();
        if (this.eventDataEntryFragment.getForm() == null) {
            return new ArrayList();
        }
        for (ProgramRule programRule : this.eventDataEntryFragment.getForm().getStage().getProgram().getProgramRules()) {
            if (programRule.getProgramStage() == null || programRule.getProgramStage().isEmpty()) {
                programRules.add(programRule);
            } else if (getEvent() != null && programRule.getProgramStage().equals(getEvent().getProgramStageId())) {
                programRules.add(programRule);
            }
        }
        return programRules;
    }

    public Event getEvent() {
        return this.eventDataEntryFragment.getForm().getEvent();
    }

    public void applyCreateEventRuleAction(ProgramRuleAction programRuleAction) {
    }

    public void applyDisplayKeyValuePairRuleAction(ProgramRuleAction programRuleAction) {
    }

    public void applyDisplayTextRuleAction(ProgramRuleAction programRuleAction) {
    }

    public void applySendMessageRuleAction(ProgramRuleAction programRuleAction) {
    }

    public Enrollment getEnrollment() {
        return this.eventDataEntryFragment.getForm().getEnrollment();
    }

    public DataValue getDataElementValue(String dataElementId) {
        return (DataValue) this.eventDataEntryFragment.getForm().getDataValues().get(dataElementId);
    }

    public TrackedEntityAttributeValue getTrackedEntityAttributeValue(String uid) {
        return (TrackedEntityAttributeValue) this.eventDataEntryFragment.getForm().getTrackedEntityAttributeValues().get(uid);
    }

    public void saveDataElement(String id) {
        if (this.eventDataEntryFragment != null && this.eventDataEntryFragment.getSaveThread() != null) {
            this.eventDataEntryFragment.getSaveThread().scheduleSaveDataValue(id);
        }
    }

    public void saveTrackedEntityAttribute(String uid) {
        TrackedEntityAttributeValue trackedEntityAttributeValue = getTrackedEntityAttributeValue(uid);
        if (trackedEntityAttributeValue != null) {
            trackedEntityAttributeValue.save();
        }
    }

    public void applyHideSectionRuleAction(ProgramRuleAction programRuleAction) {
        this.eventDataEntryFragment.hideSection(programRuleAction.getProgramStageSection());
    }

    public void updateUi() {
        if (this.eventDataEntryFragment.getForm().getEvent() != null && this.eventDataEntryFragment.getForm().getEvent().getEventDate() == null) {
            this.eventDataEntryFragment.getListViewAdapter().hideAll();
            if (this.eventDataEntryFragment.getSpinnerAdapter() != null) {
                this.eventDataEntryFragment.getSpinnerAdapter().hideAll();
            }
        }
        this.eventDataEntryFragment.updateSections();
        EventDataEntryFragment eventDataEntryFragment = this.eventDataEntryFragment;
        DataEntryFragment.refreshListView();
    }

    public void applyShowWarningRuleAction(ProgramRuleAction programRuleAction) {
        String uid = programRuleAction.getDataElement();
        if (uid == null) {
            uid = programRuleAction.getTrackedEntityAttribute();
        }
        this.eventDataEntryFragment.getListViewAdapter().showWarningOnIndex(uid, programRuleAction.getContent());
    }

    public void applyShowErrorRuleAction(ProgramRuleAction programRuleAction) {
        String uid = programRuleAction.getDataElement();
        if (uid == null) {
            uid = programRuleAction.getTrackedEntityAttribute();
        }
        this.eventDataEntryFragment.getListViewAdapter().showErrorOnIndex(uid, programRuleAction.getContent());
        if (!this.programRuleValidationErrors.contains(programRuleAction.getContent())) {
            this.programRuleValidationErrors.add(programRuleAction.getContent() + " " + programRuleAction.getData());
        }
    }

    public void applyHideFieldRuleAction(ProgramRuleAction programRuleAction, List<String> affectedFieldsWithValue) {
        this.eventDataEntryFragment.getListViewAdapter().hideIndex(programRuleAction.getDataElement());
        DataValue dataValue = getDataElementValue(programRuleAction.getDataElement());
        if (dataValue != null) {
            EventDataEntryFragment eventDataEntryFragment = this.eventDataEntryFragment;
            if (DataEntryFragment.containsValue(dataValue)) {
                affectedFieldsWithValue.add(programRuleAction.getDataElement());
                dataValue.setValue("");
                Dhis2Application.getEventBus().post(new RowValueChangedEvent(dataValue, ""));
            }
        }
    }

    public void disableCalculatedFields(ProgramRuleAction programRuleAction) {
        this.eventDataEntryFragment.getListViewAdapter().disableIndex(programRuleAction.getDataElement());
    }

    public void showWarningHiddenValuesDialog(Fragment fragment, ArrayList<String> affectedValues) {
        ArrayList<String> dataElementNames = new ArrayList();
        Iterator it = affectedValues.iterator();
        while (it.hasNext()) {
            DataElement de = MetaDataController.getDataElement((String) it.next());
            if (de != null) {
                dataElementNames.add(de.getDisplayName());
            }
        }
        if (!dataElementNames.isEmpty()) {
            if (this.eventDataEntryFragment.getValidationErrorDialog() == null || !this.eventDataEntryFragment.getValidationErrorDialog().isVisible()) {
                this.eventDataEntryFragment.setValidationErrorDialog(ValidationErrorDialog.newInstance(fragment.getString(R.string.warning_hidefieldwithvalue), dataElementNames));
                if (fragment.isAdded()) {
                    this.eventDataEntryFragment.getValidationErrorDialog().show(fragment.getChildFragmentManager());
                }
            }
        }
    }

    public void applySetMandatoryFieldRuleAction(ProgramRuleAction programRuleAction) {
        this.eventDataEntryFragment.getListViewAdapter().addMandatoryOnIndex(programRuleAction.getDataElement());
    }

    public void applyHideProgramStageRuleAction(ProgramRuleAction programRuleAction) {
    }

    public void applyWarningOnCompleteRuleAction(ProgramRuleAction programRuleAction) {
        if (!this.showOnCompleteWarnings.contains(programRuleAction.getContent())) {
            this.showOnCompleteWarnings.add(programRuleAction.getContent());
        }
    }

    public void applyErrorOnCompleteRuleAction(ProgramRuleAction programRuleAction) {
        if (!this.showOnCompleteErrors.contains(programRuleAction.getContent())) {
            this.showOnCompleteErrors.add(programRuleAction.getContent());
        }
    }

    public void flagDataChanged(boolean hasChanged) {
        this.eventDataEntryFragment.flagDataChanged(hasChanged);
    }

    public boolean blockingSpinnerNeeded() {
        for (ProgramRule programRule : getProgramRules()) {
            for (ProgramRuleAction programRuleAction : programRule.getProgramRuleActions()) {
                if (programRuleAction.getProgramRuleActionType().equals(ProgramRuleActionType.HIDEFIELD)) {
                    return true;
                }
            }
        }
        return false;
    }
}
