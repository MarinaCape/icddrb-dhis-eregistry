package org.icddrb.dhis.android.eregistry.fragments.programoverview.selectprogramdialogfragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Arrays;
import org.icddrb.dhis.android.eregistry.C0773R;
import org.icddrb.dhis.android.eregistry.activities.HolderActivity;
import org.icddrb.dhis.android.eregistry.fragments.programoverview.registerrelationshipdialogfragment.RelationshipTypesDialogFragment;
import org.icddrb.dhis.android.eregistry.fragments.search.OnlineSearchResultFragment.CallBack;
import org.icddrb.dhis.android.eregistry.fragments.selectprogram.dialogs.Action;
import org.icddrb.dhis.android.sdk.ui.dialogs.AutoCompleteDialogFragment.OnOptionSelectedListener;
import org.icddrb.dhis.android.sdk.ui.dialogs.OrgUnitDialogFragment;
import org.icddrb.dhis.android.sdk.ui.dialogs.ProgramDialogFragment;
import org.icddrb.dhis.android.sdk.ui.dialogs.UpcomingEventsDialogFilter.Type;
import org.icddrb.dhis.android.sdk.ui.fragments.selectprogram.SelectProgramFragmentPreferences;
import org.icddrb.dhis.android.sdk.ui.fragments.selectprogram.SelectProgramFragmentState;
import org.icddrb.dhis.android.sdk.ui.views.CardTextViewButton;
import org.icddrb.dhis.android.sdk.ui.views.FloatingActionButton;
import org.icddrb.dhis.android.sdk.utils.api.ProgramType;

public class SelectProgramDialogFragment extends DialogFragment implements OnClickListener, OnOptionSelectedListener {
    private static final String EXTRA_ARGUMENTS = "extra:Arguments";
    private static final String EXTRA_SAVED_ACTION = "extra:savedAction";
    private static final String EXTRA_SAVED_INSTANCE_STATE = "extra:savedInstanceState";
    private static final String EXTRA_TRACKEDENTITYINSTANCEID = "extra:trackedEntityInstanceId";
    private static final String TAG = SelectProgramDialogFragment.class.getSimpleName();
    private static CallBack mCallBack;
    protected final String STATE = "state:SelectProgramFragment";
    private FloatingActionButton createNewTeiButton;
    private TextView mDialogLabel;
    protected CardTextViewButton mOrgUnitButton;
    protected SelectProgramFragmentPreferences mPrefs;
    protected CardTextViewButton mProgramButton;
    protected SelectProgramFragmentState mState;
    private FloatingActionButton searchAndDownloadButton;

    /* renamed from: org.icddrb.dhis.android.eregistry.fragments.programoverview.selectprogramdialogfragment.SelectProgramDialogFragment$1 */
    class C07911 implements OnClickListener {
        C07911() {
        }

        public void onClick(View v) {
            OrgUnitDialogFragment.newInstance(SelectProgramDialogFragment.this, SelectProgramDialogFragment.this.getProgramTypes()).show(SelectProgramDialogFragment.this.getChildFragmentManager());
        }
    }

    /* renamed from: org.icddrb.dhis.android.eregistry.fragments.programoverview.selectprogramdialogfragment.SelectProgramDialogFragment$2 */
    class C07922 implements OnClickListener {
        C07922() {
        }

        public void onClick(View v) {
            ProgramDialogFragment.newInstance(SelectProgramDialogFragment.this, SelectProgramDialogFragment.this.mState.getOrgUnitId(), SelectProgramDialogFragment.this.getProgramTypes()).show(SelectProgramDialogFragment.this.getChildFragmentManager());
        }
    }

    public static SelectProgramDialogFragment newInstance(long trackedEntityInstanceId, Action action, CallBack callBack) {
        mCallBack = callBack;
        SelectProgramDialogFragment dialogFragment = new SelectProgramDialogFragment();
        Bundle args = new Bundle();
        args.putLong(EXTRA_TRACKEDENTITYINSTANCEID, trackedEntityInstanceId);
        args.putSerializable(EXTRA_SAVED_ACTION, action);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(1, C0773R.style.Theme.AppCompat.Light.Dialog);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(2);
        return inflater.inflate(C0773R.layout.dialog_fragment_selection_program, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.mPrefs = new SelectProgramFragmentPreferences(getActivity().getApplicationContext());
        Action action = (Action) getArguments().getSerializable(EXTRA_SAVED_ACTION);
        if (action == Action.QUERY) {
            this.searchAndDownloadButton = (FloatingActionButton) view.findViewById(C0773R.id.search_and_download_button);
            this.searchAndDownloadButton.setOnClickListener(this);
            this.searchAndDownloadButton.setVisibility(0);
        } else if (action == Action.CREATE) {
            this.createNewTeiButton = (FloatingActionButton) view.findViewById(C0773R.id.create_new_tei_button);
            this.createNewTeiButton.setOnClickListener(this);
            this.createNewTeiButton.setVisibility(0);
        }
        ((ImageView) view.findViewById(C0773R.id.close_dialog_button)).setOnClickListener(this);
        this.mDialogLabel = (TextView) view.findViewById(C0773R.id.dialog_label);
        setDialogLabel(C0773R.string.download_entities_title);
        setOUAndProgramButtons(view);
        setState(savedInstanceState);
    }

    private void setState(Bundle savedInstanceState) {
        if (!(savedInstanceState == null || savedInstanceState.getParcelable("state:SelectProgramFragment") == null)) {
            this.mState = (SelectProgramFragmentState) savedInstanceState.getParcelable("state:SelectProgramFragment");
        }
        if (this.mState == null) {
            Pair<String, String> orgUnit = this.mPrefs.getOrgUnit();
            Pair<String, String> program = this.mPrefs.getProgram();
            Pair<String, String> filter = this.mPrefs.getFilter();
            this.mState = new SelectProgramFragmentState();
            if (orgUnit != null) {
                this.mState.setOrgUnit((String) orgUnit.first, (String) orgUnit.second);
                if (program != null) {
                    this.mState.setProgram((String) program.first, (String) program.second);
                }
                if (filter != null) {
                    this.mState.setFilter((String) filter.first, (String) filter.second);
                } else {
                    this.mState.setFilter("0", ((Type) Arrays.asList(Type.values()).get(0)).toString());
                }
            }
        }
        onRestoreState(true);
    }

    private void setOUAndProgramButtons(View view) {
        this.mOrgUnitButton = (CardTextViewButton) view.findViewById(C0773R.id.select_organisation_unit);
        this.mProgramButton = (CardTextViewButton) view.findViewById(C0773R.id.select_program);
        this.mOrgUnitButton.setOnClickListener(new C07911());
        this.mProgramButton.setOnClickListener(new C07922());
        this.mOrgUnitButton.setEnabled(true);
        this.mProgramButton.setEnabled(false);
    }

    public void setDialogLabel(int resourceId) {
        if (this.mDialogLabel != null) {
            this.mDialogLabel.setText(resourceId);
        }
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle argumentsBundle = new Bundle();
        argumentsBundle.putBundle("extra:Arguments", getArguments());
        argumentsBundle.putBundle("extra:savedInstanceState", savedInstanceState);
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, TAG);
    }

    public void onClick(View v) {
        if (v.getId() == C0773R.id.relationshiptypebutton) {
            RelationshipTypesDialogFragment.newInstance(this).show(getChildFragmentManager());
        } else if (v.getId() == C0773R.id.close_dialog_button) {
            dismiss();
        } else if (v.getId() == C0773R.id.search_and_download_button) {
            HolderActivity.navigateToOnlineSearchFragment(getActivity(), this.mState.getProgramId(), this.mState.getOrgUnitId(), true, mCallBack);
            dismiss();
        } else if (v.getId() == C0773R.id.create_new_tei_button) {
            HolderActivity.navigateToTrackedEntityInstanceDataEntryFragment(getActivity(), this.mState.getProgramId(), this.mState.getOrgUnitId(), true, mCallBack);
            dismiss();
        }
    }

    public void onOptionSelected(int dialogId, int position, String id, String name) {
        switch (dialogId) {
            case OrgUnitDialogFragment.ID /*450123*/:
                onUnitSelected(id, name);
                return;
            case ProgramDialogFragment.ID /*921345*/:
                onProgramSelected(id, name);
                return;
            default:
                return;
        }
    }

    public void onRestoreState(boolean hasUnits) {
        this.mOrgUnitButton.setEnabled(hasUnits);
        if (hasUnits) {
            SelectProgramFragmentState backedUpState = new SelectProgramFragmentState(this.mState);
            if (!backedUpState.isOrgUnitEmpty()) {
                onUnitSelected(backedUpState.getOrgUnitId(), backedUpState.getOrgUnitLabel());
                if (!backedUpState.isProgramEmpty()) {
                    onProgramSelected(backedUpState.getProgramId(), backedUpState.getProgramName());
                }
            }
        }
    }

    public void onUnitSelected(String orgUnitId, String orgUnitLabel) {
        this.mOrgUnitButton.setText(orgUnitLabel);
        this.mProgramButton.setEnabled(true);
        this.mState.setOrgUnit(orgUnitId, orgUnitLabel);
        this.mState.resetProgram();
        this.mPrefs.putOrgUnit(new Pair(orgUnitId, orgUnitLabel));
        this.mPrefs.putProgram(null);
    }

    public void onProgramSelected(String programId, String programName) {
        this.mProgramButton.setText(programName);
        this.mState.setProgram(programId, programName);
        this.mPrefs.putProgram(new Pair(programId, programName));
    }

    protected ProgramType[] getProgramTypes() {
        return new ProgramType[]{ProgramType.WITH_REGISTRATION};
    }
}
