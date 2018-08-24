package org.icddrb.dhis.android.eregistry.fragments.programoverview.registerrelationshipdialogfragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.Model;
import java.util.ArrayList;
import java.util.List;
import org.icddrb.dhis.android.eregistry.C0773R;
import org.icddrb.dhis.android.eregistry.fragments.programoverview.selectprogramdialogfragment.SelectProgramDialogFragment;
import org.icddrb.dhis.android.eregistry.fragments.search.OnlineSearchResultFragment.CallBack;
import org.icddrb.dhis.android.eregistry.fragments.selectprogram.dialogs.Action;
import org.icddrb.dhis.android.eregistry.ui.adapters.RelationshipTypeAdapter;
import org.icddrb.dhis.android.eregistry.ui.adapters.TrackedEntityInstanceAdapter;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.controllers.tracker.TrackerController;
import org.icddrb.dhis.android.sdk.persistence.loaders.DbLoader;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.Program;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramTrackedEntityAttribute;
import org.icddrb.dhis.android.sdk.persistence.models.Relationship;
import org.icddrb.dhis.android.sdk.persistence.models.Relationship.Table;
import org.icddrb.dhis.android.sdk.persistence.models.RelationshipType;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttribute;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.AbsTextWatcher;
import org.icddrb.dhis.android.sdk.ui.dialogs.AutoCompleteDialogFragment.OnOptionSelectedListener;
import org.icddrb.dhis.android.sdk.ui.views.CardTextViewButton;
import org.icddrb.dhis.android.sdk.ui.views.FloatingActionButton;
import org.icddrb.dhis.android.sdk.ui.views.FontTextView;
import org.icddrb.dhis.android.sdk.utils.UiUtils;

public class RegisterRelationshipDialogFragment extends DialogFragment implements OnClickListener, LoaderCallbacks<RegisterRelationshipDialogFragmentForm>, OnOptionSelectedListener, OnItemClickListener {
    private static final String EXTRA_ARGUMENTS = "extra:Arguments";
    private static final String EXTRA_PROGRAM = "extra:programUid";
    private static final String EXTRA_SAVED_INSTANCE_STATE = "extra:savedInstanceState";
    private static final String EXTRA_TRACKEDENTITYINSTANCEID = "extra:trackedEntityInstanceId";
    private static final int LOADER_ID = 956401663;
    private static final String TAG = RegisterRelationshipDialogFragment.class.getSimpleName();
    private static Bundle mSavedInstance;
    private FloatingActionButton createNewTEIButton;
    private TrackedEntityInstanceAdapter mAdapter;
    private int mDialogId;
    private TextView mDialogLabel;
    private EditText mFilter;
    private RegisterRelationshipDialogFragmentForm mForm;
    private ListView mListView;
    private ProgressBar mProgressBar;
    private CardTextViewButton mRelationshipTypeButton;
    private Spinner mSpinner;
    private RelationshipTypeAdapter mSpinnerAdapter;
    private FontTextView mTrackedEntityInstanceLabel;
    private FloatingActionButton searchAndDownloadButton;

    /* renamed from: org.icddrb.dhis.android.eregistry.fragments.programoverview.registerrelationshipdialogfragment.RegisterRelationshipDialogFragment$1 */
    class C07871 extends AbsTextWatcher {
        C07871() {
        }

        public void afterTextChanged(Editable s) {
            RegisterRelationshipDialogFragment.this.mAdapter.getFilter().filter(1 + s.toString());
        }
    }

    /* renamed from: org.icddrb.dhis.android.eregistry.fragments.programoverview.registerrelationshipdialogfragment.RegisterRelationshipDialogFragment$2 */
    class C07882 implements CallBack {
        C07882() {
        }

        public void onSuccess() {
            RegisterRelationshipDialogFragment.this.refresh();
        }
    }

    /* renamed from: org.icddrb.dhis.android.eregistry.fragments.programoverview.registerrelationshipdialogfragment.RegisterRelationshipDialogFragment$3 */
    class C07893 implements CallBack {
        C07893() {
        }

        public void onSuccess() {
            RegisterRelationshipDialogFragment.this.refresh();
        }
    }

    public static RegisterRelationshipDialogFragment newInstance(long trackedEntityInstanceId, String programUid) {
        RegisterRelationshipDialogFragment dialogFragment = new RegisterRelationshipDialogFragment();
        Bundle args = new Bundle();
        args.putLong(EXTRA_TRACKEDENTITYINSTANCEID, trackedEntityInstanceId);
        args.putString(EXTRA_PROGRAM, programUid);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(1, C0773R.style.Theme.AppCompat.Light.Dialog);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(2);
        return inflater.inflate(C0773R.layout.dialog_fragment_registerrelationship, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.mListView = (ListView) view.findViewById(C0773R.id.simple_listview);
        this.mRelationshipTypeButton = (CardTextViewButton) view.findViewById(C0773R.id.relationshiptypebutton);
        this.mRelationshipTypeButton.setOnClickListener(this);
        this.mProgressBar = (ProgressBar) view.findViewById(C0773R.id.progress_bar);
        ImageView closeDialogButton = (ImageView) view.findViewById(C0773R.id.close_dialog_button);
        this.mTrackedEntityInstanceLabel = (FontTextView) view.findViewById(C0773R.id.tei_label);
        this.mFilter = (EditText) view.findViewById(C0773R.id.filter_options);
        this.searchAndDownloadButton = (FloatingActionButton) view.findViewById(C0773R.id.search_and_download_button);
        this.searchAndDownloadButton.setOnClickListener(this);
        this.createNewTEIButton = (FloatingActionButton) view.findViewById(C0773R.id.create_new_tei_button);
        this.createNewTEIButton.setOnClickListener(this);
        this.mDialogLabel = (TextView) view.findViewById(C0773R.id.dialog_label);
        ((InputMethodManager) getActivity().getSystemService("input_method")).hideSoftInputFromWindow(this.mFilter.getWindowToken(), 0);
        this.mAdapter = new TrackedEntityInstanceAdapter(getActivity().getLayoutInflater());
        this.mListView.setAdapter(this.mAdapter);
        this.mListView.setOnItemClickListener(this);
        this.mFilter.addTextChangedListener(new C07871());
        this.mSpinner = (Spinner) view.findViewById(C0773R.id.spinner);
        this.mSpinnerAdapter = new RelationshipTypeAdapter(getLayoutInflater(savedInstanceState));
        this.mSpinner.setAdapter(this.mSpinnerAdapter);
        closeDialogButton.setOnClickListener(this);
        setDialogLabel((int) C0773R.string.register_relationship);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle argumentsBundle = new Bundle();
        argumentsBundle.putBundle("extra:Arguments", getArguments());
        argumentsBundle.putBundle("extra:savedInstanceState", savedInstanceState);
        mSavedInstance = argumentsBundle;
        getLoaderManager().initLoader(LOADER_ID, argumentsBundle, this);
    }

    public Loader<RegisterRelationshipDialogFragmentForm> onCreateLoader(int id, Bundle args) {
        if (LOADER_ID != id || !isAdded()) {
            return null;
        }
        List<Class<? extends Model>> modelsToTrack = new ArrayList();
        Bundle fragmentArguments = args.getBundle("extra:Arguments");
        return new DbLoader(getActivity().getBaseContext(), modelsToTrack, new RegisterRelationshipDialogFragmentQuery(fragmentArguments.getLong(EXTRA_TRACKEDENTITYINSTANCEID), fragmentArguments.getString(EXTRA_PROGRAM)));
    }

    public void onLoadFinished(Loader<RegisterRelationshipDialogFragmentForm> loader, RegisterRelationshipDialogFragmentForm data) {
        Log.d(TAG, "load finished");
        if (loader.getId() == LOADER_ID && isAdded()) {
            this.mProgressBar.setVisibility(8);
            this.mListView.setVisibility(0);
            this.mForm = data;
            if (this.mForm.getTrackedEntityInstance() != null) {
                List<Enrollment> enrollments = TrackerController.getEnrollments(this.mForm.getTrackedEntityInstance());
                List<TrackedEntityAttribute> attributesToShow = new ArrayList();
                String value = "";
                int i;
                if (enrollments == null || enrollments.isEmpty()) {
                    i = 0;
                    while (i < this.mForm.getTrackedEntityInstance().getAttributes().size() && i < 2) {
                        if (!(this.mForm.getTrackedEntityInstance().getAttributes().get(i) == null || ((TrackedEntityAttributeValue) this.mForm.getTrackedEntityInstance().getAttributes().get(i)).getValue() == null)) {
                            value = value + ((TrackedEntityAttributeValue) this.mForm.getTrackedEntityInstance().getAttributes().get(i)).getValue() + " ";
                        }
                        i++;
                    }
                } else {
                    Program program = null;
                    for (Enrollment e : enrollments) {
                        if (!(e == null || e.getProgram() == null || e.getProgram().getProgramTrackedEntityAttributes() == null)) {
                            program = e.getProgram();
                        }
                    }
                    List<ProgramTrackedEntityAttribute> programTrackedEntityAttributes = program.getProgramTrackedEntityAttributes();
                    i = 0;
                    while (i < programTrackedEntityAttributes.size() && i < 2) {
                        attributesToShow.add(((ProgramTrackedEntityAttribute) programTrackedEntityAttributes.get(i)).getTrackedEntityAttribute());
                        i++;
                    }
                    i = 0;
                    while (i < attributesToShow.size() && i < 2) {
                        TrackedEntityAttributeValue av = TrackerController.getTrackedEntityAttributeValue(((TrackedEntityAttribute) attributesToShow.get(i)).getUid(), this.mForm.getTrackedEntityInstance().getLocalId());
                        if (!(av == null || av.getValue() == null)) {
                            value = value + av.getValue() + " ";
                        }
                        i++;
                    }
                }
                this.mTrackedEntityInstanceLabel.setText(value);
            }
            this.mAdapter.setData(this.mForm.getRows());
            this.mAdapter.swapData(this.mForm.getRows());
        }
    }

    public void onLoaderReset(Loader<RegisterRelationshipDialogFragmentForm> loader) {
        this.mAdapter.swapData(null);
        this.mProgressBar.setVisibility(0);
    }

    public void setDialogLabel(int resourceId) {
        if (this.mDialogLabel != null) {
            this.mDialogLabel.setText(resourceId);
        }
    }

    public void setDialogLabel(CharSequence sequence) {
        if (this.mDialogLabel != null) {
            this.mDialogLabel.setText(sequence);
        }
    }

    public void setDialogId(int dialogId) {
        this.mDialogId = dialogId;
    }

    public int getDialogId() {
        return this.mDialogId;
    }

    public CharSequence getDialogLabel() {
        if (this.mDialogLabel != null) {
            return this.mDialogLabel.getText();
        }
        return null;
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
            showSelectionProgramFragment(Action.QUERY, new C07882());
        } else if (v.getId() == C0773R.id.create_new_tei_button) {
            showSelectionProgramFragment(Action.CREATE, new C07893());
        }
    }

    private void refresh() {
        getLoaderManager().restartLoader(LOADER_ID, mSavedInstance, this);
    }

    private void showSelectionProgramFragment(Action action, CallBack callBack) {
        if (this.mForm != null && this.mForm.getTrackedEntityInstance() != null) {
            SelectProgramDialogFragment.newInstance(this.mForm.getTrackedEntityInstance().getLocalId(), action, callBack).show(getChildFragmentManager(), TAG);
        }
    }

    public void onOptionSelected(int dialogId, int position, String id, String name) {
        RelationshipType relationshipType = MetaDataController.getRelationshipType(id);
        if (relationshipType != null) {
            List<String> values = new ArrayList();
            this.mRelationshipTypeButton.setText(relationshipType.getDisplayName());
            values.add(new String(relationshipType.getaIsToB()));
            values.add(new String(relationshipType.getbIsToA()));
            this.mSpinnerAdapter.swapData(values, id);
            return;
        }
        this.mSpinnerAdapter.swapData(null, null);
    }

    public int registerRelationship(TrackedEntityInstance relative) {
        Relationship relationship = new Relationship();
        if (this.mSpinnerAdapter == null || relative == null || this.mSpinnerAdapter.getRelationshipType() == null) {
            return -1;
        }
        relationship.setRelationship(this.mSpinnerAdapter.getRelationshipType());
        if (this.mSpinner.getSelectedItemPosition() == 0) {
            relationship.setTrackedEntityInstanceA(this.mForm.getTrackedEntityInstance().getTrackedEntityInstance());
            relationship.setTrackedEntityInstanceB(relative.getTrackedEntityInstance());
        } else {
            relationship.setTrackedEntityInstanceB(this.mForm.getTrackedEntityInstance().getTrackedEntityInstance());
            relationship.setTrackedEntityInstanceA(relative.getTrackedEntityInstance());
        }
        RelationshipType relationshipType = (RelationshipType) new Select().from(RelationshipType.class).where(Condition.column("id").is(relationship.getRelationship())).querySingle();
        if (relationshipType != null) {
            relationship.setDisplayName(relationshipType.getDisplayName());
        }
        if (((Relationship) new Select().from(Relationship.class).where(Condition.column(Table.TRACKEDENTITYINSTANCEA).is(relationship.getTrackedEntityInstanceA())).and(Condition.column(Table.TRACKEDENTITYINSTANCEB).is(relationship.getTrackedEntityInstanceB())).and(Condition.column(Table.RELATIONSHIP).is(relationship.getRelationship())).querySingle()) != null) {
            return 0;
        }
        relationship.save();
        this.mForm.getTrackedEntityInstance().setFromServer(false);
        this.mForm.getTrackedEntityInstance().update();
        return 1;
    }

    public void showConfirmRelationshipDialog(final int position) {
        UiUtils.showConfirmDialog(getActivity(), getString(C0773R.string.confirm), getString(C0773R.string.confirm_relationship), getString(C0773R.string.yes), getString(C0773R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                switch (RegisterRelationshipDialogFragment.this.registerRelationship(TrackerController.getTrackedEntityInstance(RegisterRelationshipDialogFragment.this.mAdapter.getItemId(position)))) {
                    case -1:
                        Toast.makeText(RegisterRelationshipDialogFragment.this.getActivity(), RegisterRelationshipDialogFragment.this.getString(C0773R.string.please_select_relationshiptype), 0).show();
                        return;
                    case 0:
                        Toast.makeText(RegisterRelationshipDialogFragment.this.getActivity(), RegisterRelationshipDialogFragment.this.getString(C0773R.string.relationship_exists), 0).show();
                        return;
                    case 1:
                        RegisterRelationshipDialogFragment.this.dismiss();
                        return;
                    default:
                        return;
                }
            }
        });
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        showConfirmRelationshipDialog(position);
    }
}
