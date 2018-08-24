package org.icddrb.dhis.android.sdk.ui.fragments.common;

import android.app.ProgressDialog;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.icddrb.dhis.android.sdk.C0845R;
import org.icddrb.dhis.android.sdk.persistence.models.DataValue;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramRule;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramRuleAction;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramRuleVariable;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.icddrb.dhis.android.sdk.utils.comparators.ProgramRulePriorityComparator;
import org.icddrb.dhis.android.sdk.utils.services.ProgramRuleService;
import org.icddrb.dhis.android.sdk.utils.services.VariableService;
import org.icddrb.dhis.client.sdk.ui.fragments.BaseFragment;

public abstract class AbsProgramRuleFragment<D> extends BaseFragment {
    private static final String TAG = AbsProgramRuleFragment.class.getSimpleName();
    protected IProgramRuleFragmentHelper programRuleFragmentHelper;
    private ProgressDialog progressDialog;

    /* renamed from: org.icddrb.dhis.android.sdk.ui.fragments.common.AbsProgramRuleFragment$1 */
    class C09001 implements Runnable {
        C09001() {
        }

        public void run() {
            if (AbsProgramRuleFragment.this.getActivity() != null && AbsProgramRuleFragment.this.isAdded()) {
                if (AbsProgramRuleFragment.this.progressDialog != null) {
                    AbsProgramRuleFragment.this.progressDialog.dismiss();
                }
                AbsProgramRuleFragment.this.progressDialog = ProgressDialog.show(AbsProgramRuleFragment.this.getContext(), "", AbsProgramRuleFragment.this.getString(C0845R.string.please_wait), true, false);
            }
        }
    }

    /* renamed from: org.icddrb.dhis.android.sdk.ui.fragments.common.AbsProgramRuleFragment$2 */
    class C09012 implements Runnable {
        C09012() {
        }

        public void run() {
            if (AbsProgramRuleFragment.this.getActivity() != null && AbsProgramRuleFragment.this.isAdded()) {
                if (AbsProgramRuleFragment.this.progressDialog != null) {
                    AbsProgramRuleFragment.this.progressDialog.cancel();
                    AbsProgramRuleFragment.this.progressDialog.dismiss();
                    return;
                }
                Log.w("HIDE PROGRESS", "Unable to hide progress dialog: AbsProgramRuleFragment.progressDialog is null");
            }
        }
    }

    public IProgramRuleFragmentHelper getProgramRuleFragmentHelper() {
        return this.programRuleFragmentHelper;
    }

    public void setProgramRuleFragmentHelper(IProgramRuleFragmentHelper programRuleFragmentHelper) {
        this.programRuleFragmentHelper = programRuleFragmentHelper;
    }

    public void evaluateAndApplyProgramRules() {
        if (this.programRuleFragmentHelper != null && this.programRuleFragmentHelper.getEnrollment() != null && this.programRuleFragmentHelper.getEnrollment().getProgram() != null && this.programRuleFragmentHelper.getEnrollment().getProgram().getProgramRules() != null && !this.programRuleFragmentHelper.getEnrollment().getProgram().getProgramRules().isEmpty()) {
            ArrayList<String> affectedFieldsWithValue;
            List<ProgramRule> programRules;
            if (this.programRuleFragmentHelper.blockingSpinnerNeeded()) {
                VariableService.initialize(this.programRuleFragmentHelper.getEnrollment(), this.programRuleFragmentHelper.getEvent());
                this.programRuleFragmentHelper.mapFieldsToRulesAndIndicators();
                affectedFieldsWithValue = new ArrayList();
                programRules = this.programRuleFragmentHelper.getProgramRules();
                Collections.sort(programRules, new ProgramRulePriorityComparator());
            } else {
                VariableService.initialize(this.programRuleFragmentHelper.getEnrollment(), this.programRuleFragmentHelper.getEvent());
                this.programRuleFragmentHelper.mapFieldsToRulesAndIndicators();
                affectedFieldsWithValue = new ArrayList();
                programRules = this.programRuleFragmentHelper.getProgramRules();
                Collections.sort(programRules, new ProgramRulePriorityComparator());
            }
            for (ProgramRule programRule : programRules) {
                try {
                    boolean evaluatedTrue = ProgramRuleService.evaluate(programRule.getCondition());
                    for (ProgramRuleAction action : programRule.getProgramRuleActions()) {
                        if (programRule.getName().contains("gest")) {
                            Log.d("Norway - PROGRAM RULE", "evaluating program rule:  Action: " + action.getProgramRuleActionType().toString() + " Name: " + programRule.getName());
                        }
                        if (evaluatedTrue) {
                            applyProgramRuleAction(action, affectedFieldsWithValue);
                        }
                    }
                } catch (Exception e) {
                    Log.e("PROGRAM RULE", "Error evaluating program rule " + programRule.getCondition(), e);
                }
            }
            if (!affectedFieldsWithValue.isEmpty()) {
                this.programRuleFragmentHelper.showWarningHiddenValuesDialog(this.programRuleFragmentHelper.getFragment(), affectedFieldsWithValue);
            }
            this.programRuleFragmentHelper.updateUi();
        }
    }

    protected void applyProgramRuleAction(ProgramRuleAction programRuleAction, List<String> affectedFieldsWithValue) {
        switch (programRuleAction.getProgramRuleActionType()) {
            case HIDEFIELD:
                this.programRuleFragmentHelper.applyHideFieldRuleAction(programRuleAction, affectedFieldsWithValue);
                return;
            case HIDESECTION:
                this.programRuleFragmentHelper.applyHideSectionRuleAction(programRuleAction);
                return;
            case SHOWWARNING:
                this.programRuleFragmentHelper.applyShowWarningRuleAction(programRuleAction);
                return;
            case SHOWERROR:
                this.programRuleFragmentHelper.applyShowErrorRuleAction(programRuleAction);
                return;
            case ASSIGN:
                applyAssignRuleAction(programRuleAction);
                return;
            case CREATEEVENT:
                this.programRuleFragmentHelper.applyCreateEventRuleAction(programRuleAction);
                return;
            case DISPLAYKEYVALUEPAIR:
                this.programRuleFragmentHelper.applyDisplayKeyValuePairRuleAction(programRuleAction);
                return;
            case DISPLAYTEXT:
                this.programRuleFragmentHelper.applyDisplayTextRuleAction(programRuleAction);
                return;
            case ERRORONCOMPLETE:
                this.programRuleFragmentHelper.applyErrorOnCompleteRuleAction(programRuleAction);
                return;
            case HIDEPROGRAMSTAGE:
                this.programRuleFragmentHelper.applyHideProgramStageRuleAction(programRuleAction);
                return;
            case SETMANDATORYFIELD:
                this.programRuleFragmentHelper.applySetMandatoryFieldRuleAction(programRuleAction);
                return;
            case WARNINGONCOMPLETE:
                this.programRuleFragmentHelper.applyWarningOnCompleteRuleAction(programRuleAction);
                return;
            case SENDMESSAGE:
                this.programRuleFragmentHelper.applySendMessageRuleAction(programRuleAction);
                return;
            default:
                return;
        }
    }

    protected void applyAssignRuleAction(ProgramRuleAction programRuleAction) {
        String stringResult = ProgramRuleService.getCalculatedConditionValue(programRuleAction.getData());
        String programRuleVariableName = programRuleAction.getContent();
        if (programRuleVariableName != null) {
            ProgramRuleVariable programRuleVariable = (ProgramRuleVariable) VariableService.getInstance().getProgramRuleVariableMap().get(programRuleVariableName.substring(2, programRuleVariableName.length() - 1));
            programRuleVariable.setVariableValue(stringResult);
            programRuleVariable.setHasValue(true);
        }
        String dataElementId = programRuleAction.getDataElement();
        if (dataElementId != null) {
            DataValue dataValue = this.programRuleFragmentHelper.getDataElementValue(dataElementId);
            if (dataValue != null) {
                dataValue.setValue(stringResult);
                this.programRuleFragmentHelper.flagDataChanged(true);
                this.programRuleFragmentHelper.saveDataElement(dataElementId);
            }
        }
        String trackedEntityAttributeId = programRuleAction.getTrackedEntityAttribute();
        if (trackedEntityAttributeId != null) {
            TrackedEntityAttributeValue trackedEntityAttributeValue = this.programRuleFragmentHelper.getTrackedEntityAttributeValue(trackedEntityAttributeId);
            if (trackedEntityAttributeValue != null) {
                trackedEntityAttributeValue.setValue(stringResult);
                this.programRuleFragmentHelper.flagDataChanged(true);
                this.programRuleFragmentHelper.saveTrackedEntityAttribute(trackedEntityAttributeId);
            }
        }
        this.programRuleFragmentHelper.disableCalculatedFields(programRuleAction);
    }

    public void showBlockingProgressBar() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new C09001());
        }
    }

    public void hideBlockingProgressBar() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new C09012());
        }
    }
}
