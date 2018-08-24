package org.icddrb.dhis.android.sdk.ui.fragments.common;

import android.support.v4.app.Fragment;
import java.util.ArrayList;
import java.util.List;
import org.icddrb.dhis.android.sdk.persistence.models.DataValue;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramRule;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramRuleAction;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;

public interface IProgramRuleFragmentHelper {
    void applyCreateEventRuleAction(ProgramRuleAction programRuleAction);

    void applyDisplayKeyValuePairRuleAction(ProgramRuleAction programRuleAction);

    void applyDisplayTextRuleAction(ProgramRuleAction programRuleAction);

    void applyErrorOnCompleteRuleAction(ProgramRuleAction programRuleAction);

    void applyHideFieldRuleAction(ProgramRuleAction programRuleAction, List<String> list);

    void applyHideProgramStageRuleAction(ProgramRuleAction programRuleAction);

    void applyHideSectionRuleAction(ProgramRuleAction programRuleAction);

    void applySendMessageRuleAction(ProgramRuleAction programRuleAction);

    void applySetMandatoryFieldRuleAction(ProgramRuleAction programRuleAction);

    void applyShowErrorRuleAction(ProgramRuleAction programRuleAction);

    void applyShowWarningRuleAction(ProgramRuleAction programRuleAction);

    void applyWarningOnCompleteRuleAction(ProgramRuleAction programRuleAction);

    boolean blockingSpinnerNeeded();

    void disableCalculatedFields(ProgramRuleAction programRuleAction);

    void flagDataChanged(boolean z);

    DataValue getDataElementValue(String str);

    Enrollment getEnrollment();

    Event getEvent();

    Fragment getFragment();

    ArrayList<String> getHideProgramStages();

    ArrayList<String> getProgramRuleValidationErrors();

    List<ProgramRule> getProgramRules();

    ArrayList<String> getShowOnCompleteErrors();

    ArrayList<String> getShowOnCompleteWarningErrors();

    TrackedEntityAttributeValue getTrackedEntityAttributeValue(String str);

    void initiateEvaluateProgramRules();

    void mapFieldsToRulesAndIndicators();

    void recycle();

    void saveDataElement(String str);

    void saveTrackedEntityAttribute(String str);

    void showWarningHiddenValuesDialog(Fragment fragment, ArrayList<String> arrayList);

    void updateUi();
}
