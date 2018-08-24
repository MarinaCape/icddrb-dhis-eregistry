package org.icddrb.dhis.android.eregistry.fragments.search;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.raizlabs.android.dbflow.structure.Model;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.List;
import org.icddrb.dhis.android.eregistry.C0773R;
import org.icddrb.dhis.android.eregistry.activities.HolderActivity;
import org.icddrb.dhis.android.sdk.controllers.DhisController;
import org.icddrb.dhis.android.sdk.controllers.tracker.TrackerController;
import org.icddrb.dhis.android.sdk.job.JobExecutor;
import org.icddrb.dhis.android.sdk.job.NetworkJob;
import org.icddrb.dhis.android.sdk.network.APIException;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.loaders.DbLoader;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.ui.adapters.DataValueAdapter;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.AbsTextWatcher;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.DataEntryRowFactory;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.DataEntryRowTypes;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.EventCoordinatesRow;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.IndicatorRow;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.StatusRow;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.events.OnDetailedInfoButtonClick;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.RowValueChangedEvent;
import org.icddrb.dhis.android.sdk.ui.views.FloatingActionButton;
import org.icddrb.dhis.android.sdk.utils.UiUtils;

public class OnlineSearchFragment extends Fragment implements OnClickListener, LoaderCallbacks<OnlineSearchFragmentForm> {
    public static final String EXTRA_ARGUMENTS = "extra:Arguments";
    public static final String EXTRA_DETAILED = "extra:detailed";
    public static final String EXTRA_NAVIGATION = "extra:Navigation";
    public static final String EXTRA_ORGUNIT = "extra:orgUnit";
    public static final String EXTRA_PROGRAM = "extra:trackedEntityAttributes";
    public static final String EXTRA_SAVED_INSTANCE_STATE = "extra:savedInstanceState";
    private static final int LOADER_ID = 956401;
    public static final String TAG = OnlineSearchFragment.class.getSimpleName();
    private boolean backNavigation;
    private DataValueAdapter mAdapter;
    private int mDialogId;
    private TextView mDialogLabel;
    private EditText mFilter;
    private OnlineSearchFragmentForm mForm;
    private ListView mListView;
    private View progressBar;

    /* renamed from: org.icddrb.dhis.android.eregistry.fragments.search.OnlineSearchFragment$1 */
    class C07951 extends AbsTextWatcher {
        C07951() {
        }

        public void afterTextChanged(Editable s) {
            if (OnlineSearchFragment.this.mForm != null) {
                OnlineSearchFragment.this.mForm.setQueryString(s.toString());
            }
        }
    }

    /* renamed from: org.icddrb.dhis.android.eregistry.fragments.search.OnlineSearchFragment$2 */
    class C07962 implements DialogInterface.OnClickListener {
        C07962() {
        }

        public void onClick(DialogInterface dialog, int i) {
            dialog.dismiss();
        }
    }

    public static OnlineSearchFragment newInstance(String program, String orgUnit) {
        OnlineSearchFragment dialogFragment = new OnlineSearchFragment();
        Bundle args = new Bundle();
        args.putString("extra:orgUnit", orgUnit);
        args.putString(EXTRA_PROGRAM, program);
        args.putBoolean(EXTRA_DETAILED, false);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    private String getOrgUnit() {
        return getArguments().getString("extra:orgUnit");
    }

    private String getProgram() {
        return getArguments().getString(EXTRA_PROGRAM);
    }

    @Subscribe
    public void onRowValueChanged(RowValueChangedEvent event) {
        if (event.getRowType() != null && event.getRowType().equals(DataEntryRowTypes.ORGANISATION_UNIT.toString())) {
            DataEntryRowFactory.updateFWADropdown(getActivity().getBaseContext(), event);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(C0773R.menu.menu_online_search, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == C0773R.id.action_load_to_device) {
            this.progressBar.setVisibility(0);
            runQuery();
        } else if (id == 16908332) {
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onPause() {
        super.onPause();
        Dhis2Application.getEventBus().unregister(this);
    }

    public void onResume() {
        super.onResume();
        Dhis2Application.getEventBus().register(this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(C0773R.layout.dialog_fragment_teiqueryresult, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.mListView = (ListView) view.findViewById(C0773R.id.simple_listview);
        View header = getLayoutInflater(savedInstanceState).inflate(C0773R.layout.fragmentdialog_querytei_header, this.mListView, false);
        if (getActivity() instanceof AppCompatActivity) {
            getActionBar().setDisplayShowTitleEnabled(true);
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }
        ((FloatingActionButton) header.findViewById(C0773R.id.detailed_search_button)).setOnClickListener(this);
        this.mListView.addHeaderView(header, TAG, false);
        this.mFilter = (EditText) view.findViewById(C0773R.id.filter_options);
        this.mDialogLabel = (TextView) view.findViewById(C0773R.id.dialog_label);
        UiUtils.hideKeyboard(getActivity());
        this.mAdapter = new DataValueAdapter(getChildFragmentManager(), getActivity().getLayoutInflater(), this.mListView, getContext());
        this.mListView.setAdapter(this.mAdapter);
        this.mFilter.addTextChangedListener(new C07951());
        this.progressBar = view.findViewById(C0773R.id.progress_bar);
    }

    private ActionBar getActionBar() {
        if (getActivity() != null && (getActivity() instanceof AppCompatActivity)) {
            return ((AppCompatActivity) getActivity()).getSupportActionBar();
        }
        throw new IllegalArgumentException("Fragment should be attached to ActionBarActivity");
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle argumentsBundle = new Bundle();
        argumentsBundle.putBundle("extra:Arguments", getArguments());
        argumentsBundle.putBundle("extra:savedInstanceState", savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, argumentsBundle, this);
        getActionBar().setTitle(getString(C0773R.string.download_entities_title));
        getActionBar().setTitle((CharSequence) "Global Search");
    }

    public Loader<OnlineSearchFragmentForm> onCreateLoader(int id, Bundle args) {
        if (LOADER_ID != id || !isAdded()) {
            return null;
        }
        List<Class<? extends Model>> modelsToTrack = new ArrayList();
        Bundle fragmentArguments = args.getBundle("extra:Arguments");
        String programId = fragmentArguments.getString(EXTRA_PROGRAM);
        String orgUnitId = fragmentArguments.getString("extra:orgUnit");
        this.backNavigation = fragmentArguments.getBoolean("extra:Navigation");
        return new DbLoader(getActivity().getBaseContext(), modelsToTrack, new OnlineSearchFragmentQuery(orgUnitId, programId));
    }

    public void onLoadFinished(Loader<OnlineSearchFragmentForm> loader, OnlineSearchFragmentForm data) {
        Log.d(TAG, "load finished");
        if (loader.getId() == LOADER_ID && isAdded()) {
            this.mListView.setVisibility(0);
            this.mForm = data;
            if (this.mForm.getDataEntryRows() != null && getArguments().getBoolean(EXTRA_DETAILED)) {
                this.mAdapter.swapData(this.mForm.getDataEntryRows());
            }
        }
    }

    public void onLoaderReset(Loader<OnlineSearchFragmentForm> loader) {
    }

    @Subscribe
    public void onShowDetailedInfo(OnDetailedInfoButtonClick eventClick) {
        String message = "";
        if (eventClick.getRow() instanceof EventCoordinatesRow) {
            message = getResources().getString(C0773R.string.detailed_info_coordinate_row);
        } else if (eventClick.getRow() instanceof StatusRow) {
            message = getResources().getString(C0773R.string.detailed_info_status_row);
        } else if (eventClick.getRow() instanceof IndicatorRow) {
            message = "";
        } else {
            message = eventClick.getRow().getDescription();
        }
        UiUtils.showConfirmDialog(getActivity(), getResources().getString(C0773R.string.detailed_info_dataelement), message, getResources().getString(C0773R.string.ok_option), new C07962());
    }

    public void toggleDetailedSearch(View v) {
        FloatingActionButton button = (FloatingActionButton) v;
        boolean current = getArguments().getBoolean(EXTRA_DETAILED);
        if (current) {
            button.setImageResource(C0773R.drawable.ic_new);
            this.mAdapter.swapData(null);
        } else {
            button.setImageResource(C0773R.drawable.ic_close_dialog);
            if (!(this.mForm == null || this.mForm.getDataEntryRows() == null)) {
                this.mAdapter.swapData(this.mForm.getDataEntryRows());
            }
        }
        getArguments().putBoolean(EXTRA_DETAILED, !current);
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

    public DataValueAdapter getAdapter() {
        return this.mAdapter;
    }

    public void onClick(View v) {
        if (v.getId() == C0773R.id.detailed_search_button) {
            toggleDetailedSearch(v);
        }
    }

    public void runQuery() {
        List<TrackedEntityAttributeValue> searchValues = new ArrayList();
        if (!(this.mForm == null || this.mForm.getTrackedEntityAttributeValues() == null || this.mForm.getOrganisationUnit() == null || this.mForm.getProgram() == null)) {
            for (TrackedEntityAttributeValue value : this.mForm.getTrackedEntityAttributeValues()) {
                searchValues.add(value);
            }
        }
        if (this.mForm != null) {
            try {
                queryTrackedEntityInstances(getChildFragmentManager(), this.mForm.getOrganisationUnit(), this.mForm.getProgram(), this.mForm.getQueryString(), true, (TrackedEntityAttributeValue[]) searchValues.toArray(new TrackedEntityAttributeValue[0]));
                return;
            } catch (Exception e) {
                showQueryError();
                return;
            }
        }
        showQueryError();
    }

    private void showQueryError() {
        if (this.progressBar != null) {
            this.progressBar.setVisibility(4);
            Toast.makeText(getContext(), getContext().getString(C0773R.string.search_error), 0).show();
        }
    }

    @SuppressLint({"StaticFieldLeak"})
    public void queryTrackedEntityInstances(FragmentManager fragmentManager, String orgUnit, String program, String queryString, boolean detailedSearch, TrackedEntityAttributeValue... params) throws APIException {
        final boolean z = detailedSearch;
        final String str = orgUnit;
        final String str2 = program;
        final String str3 = queryString;
        final TrackedEntityAttributeValue[] trackedEntityAttributeValueArr = params;
        JobExecutor.enqueueJob(new NetworkJob<Object>(1, null) {
            public Object execute() throws APIException {
                List<TrackedEntityInstance> trackedEntityInstancesQueryResult;
                if (z) {
                    trackedEntityInstancesQueryResult = TrackerController.queryTrackedEntityInstancesDataFromAllAccessibleOrgUnits(DhisController.getInstance().getDhisApi(), str, str2, str3, z, trackedEntityAttributeValueArr);
                } else {
                    trackedEntityInstancesQueryResult = TrackerController.queryTrackedEntityInstancesDataFromServer(DhisController.getInstance().getDhisApi(), str, str2, str3, trackedEntityAttributeValueArr);
                }
                OnlineSearchFragment.this.showOnlineSearchResultFragment(trackedEntityInstancesQueryResult, str, str2, OnlineSearchFragment.this.backNavigation);
                return new Object();
            }
        });
    }

    public void showOnlineSearchResultFragment(List<TrackedEntityInstance> trackedEntityInstances, String orgUnit, String programId, boolean backNavigation) {
        if (getActivity() != null && isAdded()) {
            final List<TrackedEntityInstance> list = trackedEntityInstances;
            final String str = orgUnit;
            final String str2 = programId;
            final boolean z = backNavigation;
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    OnlineSearchFragment.this.progressBar.setVisibility(4);
                    HolderActivity.navigateToOnlineSearchResultFragment(OnlineSearchFragment.this.getActivity(), list, str, str2, z);
                }
            });
        }
    }
}
