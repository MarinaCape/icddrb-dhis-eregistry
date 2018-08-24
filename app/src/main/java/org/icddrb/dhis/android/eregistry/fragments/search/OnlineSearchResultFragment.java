package org.icddrb.dhis.android.eregistry.fragments.search;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.squareup.otto.Subscribe;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.icddrb.dhis.android.eregistry.C0773R;
import org.icddrb.dhis.android.eregistry.activities.HolderActivity;
import org.icddrb.dhis.android.sdk.controllers.DhisController;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.controllers.tracker.TrackerController;
import org.icddrb.dhis.android.sdk.events.LoadingMessageEvent;
import org.icddrb.dhis.android.sdk.events.OnTeiDownloadedEvent;
import org.icddrb.dhis.android.sdk.events.OnTeiDownloadedEvent.EventType;
import org.icddrb.dhis.android.sdk.events.UiEvent;
import org.icddrb.dhis.android.sdk.events.UiEvent.UiEventType;
import org.icddrb.dhis.android.sdk.job.JobExecutor;
import org.icddrb.dhis.android.sdk.job.NetworkJob;
import org.icddrb.dhis.android.sdk.network.APIException;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.Program;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramTrackedEntityAttribute;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.persistence.preferences.ResourceType;
import org.icddrb.dhis.android.sdk.ui.activities.SynchronisationStateHandler;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.AbsTextWatcher;
import org.icddrb.dhis.android.sdk.ui.dialogs.QueryTrackedEntityInstancesResultDialogAdapter;
import org.icddrb.dhis.android.sdk.ui.fragments.progressdialog.ProgressDialogFragment;
import org.icddrb.dhis.android.sdk.ui.views.FontEditText;
import org.icddrb.dhis.android.sdk.utils.UiUtils;

public class OnlineSearchResultFragment extends Fragment implements OnItemClickListener, OnClickListener {
    public static final String EXTRA_NAVIGATION = "extra:Navigation";
    public static final String EXTRA_ORGUNIT = "extra:orgUnit";
    public static final String EXTRA_PROGRAM = "extra:Program";
    public static final String EXTRA_SELECTALL = "extra:selectAll";
    public static final String EXTRA_TRACKEDENTITYINSTANCESLIST = "extra:trackedEntityInstances";
    public static final String EXTRA_TRACKEDENTITYINSTANCESSELECTED = "extra:trackedEntityInstancesSelected";
    public static final String TAG = OnlineSearchResultFragment.class.getSimpleName();
    private boolean backNavigation;
    private List<Enrollment> downloadedEnrollments;
    private List<TrackedEntityInstance> downloadedTrackedEntityInstances;
    private FontEditText filterButton;
    private QueryTrackedEntityInstancesResultDialogAdapter mAdapter;
    private int mDialogId;
    private TextView mDialogLabel;
    private EditText mFilter;
    private ListView mListView;
    private ProgressBar mProgressBar;
    private Button mSelectAllButton;
    private String orgUnitId;
    private String programId;
    private Map<String, ProgramTrackedEntityAttribute> programTrackedEntityAttributeMap;
    private ProgressDialogFragment progressDialogFragment;
    private Button searchButton;

    public interface CallBack {
        void onSuccess();
    }

    /* renamed from: org.icddrb.dhis.android.eregistry.fragments.search.OnlineSearchResultFragment$1 */
    class C07991 extends AbsTextWatcher {
        C07991() {
        }

        public void afterTextChanged(Editable s) {
            OnlineSearchResultFragment.this.mAdapter.getFilter().filter(s.toString());
        }
    }

    /* renamed from: org.icddrb.dhis.android.eregistry.fragments.search.OnlineSearchResultFragment$2 */
    class C08002 implements Runnable {
        C08002() {
        }

        public void run() {
            Dhis2Application.getEventBus().post(new UiEvent(UiEventType.SYNCING_START));
        }
    }

    public static class ParameterParcelable implements Parcelable {
        public static final Creator<ParameterParcelable> CREATOR = new C08041();
        public static final String TAG = ParameterParcelable.class.getSimpleName();
        private List<TrackedEntityInstance> trackedEntityInstances;

        /* renamed from: org.icddrb.dhis.android.eregistry.fragments.search.OnlineSearchResultFragment$ParameterParcelable$1 */
        static class C08041 implements Creator<ParameterParcelable> {
            C08041() {
            }

            public ParameterParcelable createFromParcel(Parcel in) {
                return new ParameterParcelable(in);
            }

            public ParameterParcelable[] newArray(int size) {
                return new ParameterParcelable[size];
            }
        }

        public ParameterParcelable(List<TrackedEntityInstance> trackedEntityInstances) {
            Log.d(TAG, "parcelputting " + trackedEntityInstances.size());
            this.trackedEntityInstances = trackedEntityInstances;
        }

        protected ParameterParcelable(Parcel in) {
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeList(this.trackedEntityInstances);
        }

        public List<TrackedEntityInstance> getTrackedEntityInstances() {
            return this.trackedEntityInstances;
        }
    }

    public static class ParameterSerializible implements Serializable {
        private List<TrackedEntityInstance> trackedEntityInstances;

        public ParameterSerializible(List<TrackedEntityInstance> trackedEntityInstances) {
            this.trackedEntityInstances = trackedEntityInstances;
        }

        public List<TrackedEntityInstance> getTrackedEntityInstances() {
            return this.trackedEntityInstances;
        }
    }

    public static OnlineSearchResultFragment newInstance(List<TrackedEntityInstance> trackedEntityInstances, String orgUnit) {
        OnlineSearchResultFragment dialogFragment = new OnlineSearchResultFragment();
        Bundle args = new Bundle();
        ParameterSerializible parameterSerializible1 = new ParameterSerializible(trackedEntityInstances);
        ParameterSerializible parameterSerializible2 = new ParameterSerializible(new ArrayList());
        args.putSerializable(EXTRA_TRACKEDENTITYINSTANCESLIST, parameterSerializible1);
        args.putSerializable(EXTRA_TRACKEDENTITYINSTANCESSELECTED, parameterSerializible2);
        args.putString("extra:orgUnit", orgUnit);
        args.putBoolean(EXTRA_SELECTALL, false);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    private List<TrackedEntityInstance> getTrackedEntityInstances() {
        return ((ParameterSerializible) getArguments().getSerializable(EXTRA_TRACKEDENTITYINSTANCESSELECTED)).getTrackedEntityInstances();
    }

    private List<TrackedEntityInstance> getSelectedTrackedEntityInstances() {
        return ((ParameterSerializible) getArguments().getSerializable(EXTRA_TRACKEDENTITYINSTANCESLIST)).getTrackedEntityInstances();
    }

    private String getOrgUnit() {
        return getArguments().getString("extra:orgUnit");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        this.downloadedEnrollments = new ArrayList();
        this.downloadedTrackedEntityInstances = new ArrayList();
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(C0773R.menu.menu_online_search_result, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == C0773R.id.action_load_to_device) {
            Activity activity = getActivity();
            if (!this.backNavigation) {
                getActivity().finish();
            }
            initiateLoading(activity, this.programId);
        } else if (id == 16908332) {
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(C0773R.layout.dialog_fragment_teiqueryresult, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.mListView = (ListView) view.findViewById(C0773R.id.simple_listview);
        this.mProgressBar = (ProgressBar) view.findViewById(C0773R.id.progress_bar);
        if (getActivity() instanceof AppCompatActivity) {
            getActionBar().setDisplayShowTitleEnabled(true);
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }
        this.programId = getArguments().getString(EXTRA_PROGRAM);
        this.orgUnitId = getArguments().getString("extra:orgUnit");
        this.backNavigation = getArguments().getBoolean("extra:Navigation");
        Program selectedProgram = MetaDataController.getProgram(this.programId);
        this.mFilter = (EditText) view.findViewById(C0773R.id.filter_options);
        this.mDialogLabel = (TextView) view.findViewById(C0773R.id.dialog_label);
        this.filterButton = (FontEditText) view.findViewById(C0773R.id.filter_options);
        this.searchButton = (Button) view.findViewById(C0773R.id.teiqueryresult_selectall);
        UiUtils.hideKeyboard(getActivity());
        if (savedInstanceState == null) {
            this.programTrackedEntityAttributeMap = new HashMap();
            this.programTrackedEntityAttributeMap = getProgramTrackedEntityAttributes(selectedProgram);
        }
        this.mAdapter = new QueryTrackedEntityInstancesResultDialogAdapter(LayoutInflater.from(getActivity()), getSelectedTrackedEntityInstances(), this.programTrackedEntityAttributeMap, getContext());
        this.mListView.setAdapter(this.mAdapter);
        this.mListView.setOnItemClickListener(this);
        this.mFilter.addTextChangedListener(new C07991());
        this.mSelectAllButton = (Button) view.findViewById(C0773R.id.teiqueryresult_selectall);
        this.mSelectAllButton.setOnClickListener(this);
        this.mSelectAllButton.setVisibility(0);
        if (getArguments().getBoolean(EXTRA_SELECTALL)) {
            this.mSelectAllButton.setText(getString(C0773R.string.deselect_all));
        }
        getAdapter().swapData(getTrackedEntityInstances());
    }

    private Map<String, ProgramTrackedEntityAttribute> getProgramTrackedEntityAttributes(Program selectedProgram) {
        List<ProgramTrackedEntityAttribute> programTrackedEntityAttributes = selectedProgram.getProgramTrackedEntityAttributes();
        Map<String, ProgramTrackedEntityAttribute> attributeMap = new HashMap();
        for (ProgramTrackedEntityAttribute programTrackedEntityAttribute : programTrackedEntityAttributes) {
            attributeMap.put(programTrackedEntityAttribute.getTrackedEntityAttributeId(), programTrackedEntityAttribute);
        }
        return attributeMap;
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        TrackedEntityInstance value = this.mAdapter.getItem(position);
        List<TrackedEntityInstance> selected = getSelectedTrackedEntityInstances();
        CheckBox checkBox = (CheckBox) view.findViewById(C0773R.id.checkBoxTeiQuery);
        if (checkBox.isChecked()) {
            selected.remove(value);
            checkBox.setChecked(false);
            return;
        }
        selected.add(value);
        checkBox.setChecked(true);
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

    public QueryTrackedEntityInstancesResultDialogAdapter getAdapter() {
        return this.mAdapter;
    }

    public void onClick(View v) {
        if (v.getId() == C0773R.id.teiqueryresult_selectall) {
            toggleSelectAll();
        }
    }

    public void toggleSelectAll() {
        Bundle arguments = getArguments();
        boolean selectAll = arguments.getBoolean(EXTRA_SELECTALL);
        if (selectAll) {
            this.mSelectAllButton.setText(getText(C0773R.string.select_all));
            deselectAll();
        } else {
            this.mSelectAllButton.setText(getText(C0773R.string.deselect_all));
            selectAll();
        }
        arguments.putBoolean(EXTRA_SELECTALL, !selectAll);
    }

    public void selectAll() {
        List<TrackedEntityInstance> allTrackedEntityInstances = this.mAdapter.getData();
        List<TrackedEntityInstance> selectedTrackedEntityInstances = getSelectedTrackedEntityInstances();
        selectedTrackedEntityInstances.clear();
        selectedTrackedEntityInstances.addAll(allTrackedEntityInstances);
        View view = null;
        for (int i = 0; i < allTrackedEntityInstances.size(); i++) {
            view = this.mAdapter.getView(i, view, null);
            ((CheckBox) view.findViewById(C0773R.id.checkBoxTeiQuery)).setChecked(true);
        }
        refreshListView();
    }

    public void deselectAll() {
        List<TrackedEntityInstance> allTrackedEntityInstances = this.mAdapter.getData();
        getSelectedTrackedEntityInstances().clear();
        View view = null;
        for (int i = 0; i < allTrackedEntityInstances.size(); i++) {
            view = this.mAdapter.getView(i, view, null);
            ((CheckBox) view.findViewById(C0773R.id.checkBoxTeiQuery)).setChecked(false);
        }
        refreshListView();
    }

    public void refreshListView() {
        int start = this.mListView.getFirstVisiblePosition();
        int end = this.mListView.getLastVisiblePosition();
        for (int pos = 0; pos <= end - start; pos++) {
            View view = this.mListView.getChildAt(pos);
            if (view != null) {
                int adapterPosition = view.getId();
                if (adapterPosition >= 0 && adapterPosition < this.mAdapter.getCount() && !view.hasFocus()) {
                    this.mAdapter.getView(adapterPosition, view, this.mListView);
                }
            }
        }
    }

    public void initiateLoading(Activity activity, String programId) {
        this.mAdapter.swapData(null);
        this.filterButton.setVisibility(4);
        this.searchButton.setVisibility(4);
        this.mProgressBar.setVisibility(0);
        Dhis2Application.getEventBus().post(new OnTeiDownloadedEvent(EventType.START, getSelectedTrackedEntityInstances().size()));
        Log.d(TAG, "loading: " + getSelectedTrackedEntityInstances().size());
        this.downloadedTrackedEntityInstances = new ArrayList();
        this.downloadedEnrollments = new ArrayList();
        activity.runOnUiThread(new C08002());
        final String str = programId;
        final Activity activity2 = activity;
        JobExecutor.enqueueJob(new NetworkJob<Object>(0, ResourceType.TRACKEDENTITYINSTANCE) {

            /* renamed from: org.icddrb.dhis.android.eregistry.fragments.search.OnlineSearchResultFragment$3$2 */
            class C08022 implements Runnable {
                C08022() {
                }

                public void run() {
                    if (OnlineSearchResultFragment.this.backNavigation) {
                        OnlineSearchResultFragment.this.getActivity().finish();
                        HolderActivity.mCallBack.onSuccess();
                    }
                }
            }

            public Object execute() throws APIException {
                SynchronisationStateHandler.getInstance().changeState(true);
                List<TrackedEntityInstance> trackedEntityInstances = TrackerController.getTrackedEntityInstancesDataFromServer(DhisController.getInstance().getDhisApi(), OnlineSearchResultFragment.this.getSelectedTrackedEntityInstances(), true, true);
                if (trackedEntityInstances != null) {
                    if (OnlineSearchResultFragment.this.downloadedTrackedEntityInstances == null) {
                        OnlineSearchResultFragment.this.downloadedTrackedEntityInstances = new ArrayList();
                    }
                    OnlineSearchResultFragment.this.downloadedTrackedEntityInstances.addAll(trackedEntityInstances);
                }
                for (int i = 0; i < OnlineSearchResultFragment.this.downloadedTrackedEntityInstances.size(); i++) {
                    List<Enrollment> enrollments = TrackerController.getEnrollmentDataFromServer(DhisController.getInstance().getDhisApi(), (TrackedEntityInstance) OnlineSearchResultFragment.this.downloadedTrackedEntityInstances.get(i), null);
                    if (enrollments != null) {
                        if (OnlineSearchResultFragment.this.downloadedEnrollments == null) {
                            OnlineSearchResultFragment.this.downloadedEnrollments = new ArrayList();
                        }
                        OnlineSearchResultFragment.this.downloadedEnrollments.addAll(enrollments);
                    }
                    Dhis2Application.getEventBus().post(new OnTeiDownloadedEvent(EventType.UPDATE, trackedEntityInstances.size(), (int) Math.ceil(((double) ((OnlineSearchResultFragment.this.downloadedTrackedEntityInstances.size() + i) + 1)) / 2.0d)));
                }
                if (!(OnlineSearchResultFragment.this.downloadedTrackedEntityInstances == null || OnlineSearchResultFragment.this.downloadedTrackedEntityInstances.size() != 1 || OnlineSearchResultFragment.this.downloadedEnrollments == null)) {
                    final Enrollment enrollment = OnlineSearchResultFragment.this.getActiveEnrollmentByProgram(str);
                    if (enrollment != null) {
                        final TrackedEntityInstance trackedEntityInstance = (TrackedEntityInstance) OnlineSearchResultFragment.this.downloadedTrackedEntityInstances.get(0);
                        if (!OnlineSearchResultFragment.this.backNavigation) {
                            activity2.runOnUiThread(new Runnable() {
                                public void run() {
                                    HolderActivity.navigateToProgramOverviewFragment(activity2, OnlineSearchResultFragment.this.orgUnitId, enrollment.getProgram().getUid(), trackedEntityInstance.getLocalId());
                                }
                            });
                        }
                    }
                }
                Dhis2Application.getEventBus().post(new OnTeiDownloadedEvent(EventType.END, OnlineSearchResultFragment.this.getSelectedTrackedEntityInstances().size()));
                Dhis2Application.getEventBus().post(new UiEvent(UiEventType.SYNCING_END));
                SynchronisationStateHandler.getInstance().changeState(false);
                activity2.runOnUiThread(new C08022());
                return new Object();
            }
        });
    }

    private Enrollment getActiveEnrollmentByProgram(String programId) {
        for (Enrollment enrollment : this.downloadedEnrollments) {
            if (enrollment.getProgram().getUid().equals(programId)) {
                return enrollment;
            }
        }
        return null;
    }

    @Subscribe
    public void onLoadingMessageEvent(LoadingMessageEvent event) {
        Log.d(TAG, "Message received" + event.message);
        if (this.progressDialogFragment != null && this.progressDialogFragment.getDialog() != null && this.progressDialogFragment.getDialog().isShowing()) {
            ((ProgressDialog) this.progressDialogFragment.getDialog()).setMessage(event.message);
        }
    }

    private ActionBar getActionBar() {
        if (getActivity() != null && (getActivity() instanceof AppCompatActivity)) {
            return ((AppCompatActivity) getActivity()).getSupportActionBar();
        }
        throw new IllegalArgumentException("Fragment should be attached to ActionBarActivity");
    }

    public void onResume() {
        super.onResume();
        Dhis2Application.getEventBus().register(this);
    }

    public void onPause() {
        super.onPause();
        Dhis2Application.getEventBus().unregister(this);
    }
}
