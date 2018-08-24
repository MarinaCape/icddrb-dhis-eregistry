package org.icddrb.dhis.android.eregistry.fragments.selectprogram.dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.raizlabs.android.dbflow.structure.Model;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.List;
import org.icddrb.dhis.android.eregistry.C0773R;
import org.icddrb.dhis.android.sdk.controllers.DhisController;
import org.icddrb.dhis.android.sdk.controllers.GpsController;
import org.icddrb.dhis.android.sdk.controllers.tracker.TrackerController;
import org.icddrb.dhis.android.sdk.events.UiEvent;
import org.icddrb.dhis.android.sdk.events.UiEvent.UiEventType;
import org.icddrb.dhis.android.sdk.job.JobExecutor;
import org.icddrb.dhis.android.sdk.job.NetworkJob;
import org.icddrb.dhis.android.sdk.network.APIException;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.loaders.DbLoader;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.ui.adapters.DataValueAdapter;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.AbsTextWatcher;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.EventCoordinatesRow;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.IndicatorRow;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.StatusRow;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.events.OnDetailedInfoButtonClick;
import org.icddrb.dhis.android.sdk.ui.dialogs.QueryTrackedEntityInstancesResultDialogFragment;
import org.icddrb.dhis.android.sdk.ui.views.FloatingActionButton;
import org.icddrb.dhis.android.sdk.utils.UiUtils;

public class QueryTrackedEntityInstancesDialogFragment extends DialogFragment implements OnClickListener, LoaderCallbacks<QueryTrackedEntityInstancesDialogFragmentForm> {
    private static final String EXTRA_ARGUMENTS = "extra:Arguments";
    private static final String EXTRA_DETAILED = "extra:detailed";
    private static final String EXTRA_ORGUNIT = "extra:orgUnit";
    private static final String EXTRA_PROGRAM = "extra:trackedEntityAttributes";
    private static final String EXTRA_SAVED_INSTANCE_STATE = "extra:savedInstanceState";
    private static final int LOADER_ID = 956401;
    private static final String TAG = QueryTrackedEntityInstancesDialogFragment.class.getSimpleName();
    private FragmentActivity activity = null;
    private DataValueAdapter mAdapter;
    private int mDialogId;
    private TextView mDialogLabel;
    private EditText mFilter;
    private QueryTrackedEntityInstancesDialogFragmentForm mForm;
    private ListView mListView;

    /* renamed from: org.icddrb.dhis.android.eregistry.fragments.selectprogram.dialogs.QueryTrackedEntityInstancesDialogFragment$1 */
    class C08171 extends AbsTextWatcher {
        C08171() {
        }

        public void afterTextChanged(Editable s) {
            QueryTrackedEntityInstancesDialogFragment.this.mForm.setQueryString(s.toString());
        }
    }

    /* renamed from: org.icddrb.dhis.android.eregistry.fragments.selectprogram.dialogs.QueryTrackedEntityInstancesDialogFragment$2 */
    class C08182 implements OnClickListener {
        C08182() {
        }

        public void onClick(View v) {
            QueryTrackedEntityInstancesDialogFragment.this.dismiss();
        }
    }

    /* renamed from: org.icddrb.dhis.android.eregistry.fragments.selectprogram.dialogs.QueryTrackedEntityInstancesDialogFragment$3 */
    class C08193 implements DialogInterface.OnClickListener {
        C08193() {
        }

        public void onClick(DialogInterface dialog, int i) {
            dialog.dismiss();
        }
    }

    public static QueryTrackedEntityInstancesDialogFragment newInstance(String program, String orgUnit) {
        QueryTrackedEntityInstancesDialogFragment dialogFragment = new QueryTrackedEntityInstancesDialogFragment();
        Bundle args = new Bundle();
        args.putString("extra:orgUnit", orgUnit);
        args.putString("extra:trackedEntityAttributes", program);
        args.putBoolean("extra:detailed", false);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    private String getOrgUnit() {
        return getArguments().getString("extra:orgUnit");
    }

    private String getProgram() {
        return getArguments().getString("extra:trackedEntityAttributes");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(1, C0773R.style.Theme.AppCompat.Light.Dialog);
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
        getDialog().getWindow().setSoftInputMode(2);
        return inflater.inflate(C0773R.layout.dialog_fragment_teiqueryresult, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.mListView = (ListView) view.findViewById(C0773R.id.simple_listview);
        View header = getLayoutInflater(savedInstanceState).inflate(C0773R.layout.fragmentdialog_querytei_header, this.mListView, false);
        ((FloatingActionButton) header.findViewById(C0773R.id.detailed_search_button)).setOnClickListener(this);
        this.mListView.addHeaderView(header, TAG, false);
        ImageView closeDialogButton = (ImageView) view.findViewById(C0773R.id.close_dialog_button);
        this.mFilter = (EditText) view.findViewById(C0773R.id.filter_options);
        this.mDialogLabel = (TextView) view.findViewById(C0773R.id.dialog_label);
        ((InputMethodManager) getActivity().getSystemService("input_method")).hideSoftInputFromWindow(this.mFilter.getWindowToken(), 0);
        this.mAdapter = new DataValueAdapter(getChildFragmentManager(), getActivity().getLayoutInflater(), this.mListView, getContext());
        this.mListView.setAdapter(this.mAdapter);
        this.mFilter.addTextChangedListener(new C08171());
        closeDialogButton.setOnClickListener(new C08182());
        setDialogLabel((int) C0773R.string.query_tracked_entity_instances);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle argumentsBundle = new Bundle();
        argumentsBundle.putBundle("extra:Arguments", getArguments());
        argumentsBundle.putBundle("extra:savedInstanceState", savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, argumentsBundle, this);
    }

    public Loader<QueryTrackedEntityInstancesDialogFragmentForm> onCreateLoader(int id, Bundle args) {
        if (LOADER_ID != id || !isAdded()) {
            return null;
        }
        List<Class<? extends Model>> modelsToTrack = new ArrayList();
        Bundle fragmentArguments = args.getBundle("extra:Arguments");
        String programId = fragmentArguments.getString("extra:trackedEntityAttributes");
        return new DbLoader(getActivity().getBaseContext(), modelsToTrack, new QueryTrackedEntityInstancesDialogFragmentQuery(fragmentArguments.getString("extra:orgUnit"), programId));
    }

    public void onLoadFinished(Loader<QueryTrackedEntityInstancesDialogFragmentForm> loader, QueryTrackedEntityInstancesDialogFragmentForm data) {
        Log.d(TAG, "load finished");
        if (loader.getId() == LOADER_ID && isAdded()) {
            this.mListView.setVisibility(0);
            this.mForm = data;
            if (this.mForm.getDataEntryRows() != null && getArguments().getBoolean("extra:detailed")) {
                this.mAdapter.swapData(this.mForm.getDataEntryRows());
            }
        }
    }

    public void onLoaderReset(Loader<QueryTrackedEntityInstancesDialogFragmentForm> loader) {
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
        UiUtils.showConfirmDialog(getActivity(), getResources().getString(C0773R.string.detailed_info_dataelement), message, getResources().getString(C0773R.string.ok_option), new C08193());
    }

    public void toggleDetailedSearch(View v) {
        FloatingActionButton button = (FloatingActionButton) v;
        boolean current = getArguments().getBoolean("extra:detailed");
        if (current) {
            button.setImageResource(C0773R.drawable.ic_search);
            this.mAdapter.swapData(null);
        } else {
            button.setImageResource(C0773R.drawable.ic_delete);
            this.mAdapter.swapData(this.mForm.getDataEntryRows());
        }
        getArguments().putBoolean("extra:detailed", !current);
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

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, TAG);
    }

    public void onClick(View v) {
        if (v.getId() == C0773R.id.detailed_search_button) {
            toggleDetailedSearch(v);
        }
    }

    public void runQuery() {
        final List<TrackedEntityAttributeValue> searchValues = new ArrayList();
        if (!(this.mForm == null || this.mForm.getTrackedEntityAttributeValues() == null || this.mForm.getOrganisationUnit() == null || this.mForm.getProgram() == null)) {
            for (TrackedEntityAttributeValue value : this.mForm.getTrackedEntityAttributeValues()) {
                searchValues.add(value);
            }
        }
        new Thread() {
            public void run() {
                QueryTrackedEntityInstancesDialogFragment.this.queryTrackedEntityInstances(QueryTrackedEntityInstancesDialogFragment.this.getChildFragmentManager(), QueryTrackedEntityInstancesDialogFragment.this.mForm.getOrganisationUnit(), QueryTrackedEntityInstancesDialogFragment.this.mForm.getProgram(), QueryTrackedEntityInstancesDialogFragment.this.mForm.getQueryString(), (TrackedEntityAttributeValue[]) searchValues.toArray(new TrackedEntityAttributeValue[0]));
            }
        }.start();
    }

    public void queryTrackedEntityInstances(FragmentManager fragmentManager, String orgUnit, String program, String queryString, TrackedEntityAttributeValue... params) throws APIException {
        final String str = orgUnit;
        final String str2 = program;
        final String str3 = queryString;
        final TrackedEntityAttributeValue[] trackedEntityAttributeValueArr = params;
        final FragmentManager fragmentManager2 = fragmentManager;
        JobExecutor.enqueueJob(new NetworkJob<Object>(1, null) {
            public Object execute() throws APIException {
                Dhis2Application.getEventBus().post(new UiEvent(UiEventType.SYNCING_START));
                List<TrackedEntityInstance> trackedEntityInstancesQueryResult = TrackerController.queryTrackedEntityInstancesDataFromServer(DhisController.getInstance().getDhisApi(), str, str2, str3, trackedEntityAttributeValueArr);
                Dhis2Application.getEventBus().post(new UiEvent(UiEventType.SYNCING_END));
                QueryTrackedEntityInstancesDialogFragment.this.showTrackedEntityInstanceQueryResultDialog(fragmentManager2, trackedEntityInstancesQueryResult, str);
                return new Object();
            }
        });
    }

    public void showTrackedEntityInstanceQueryResultDialog(FragmentManager fragmentManager, final List<TrackedEntityInstance> trackedEntityInstances, final String orgUnit) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                QueryTrackedEntityInstancesResultDialogFragment dialog = QueryTrackedEntityInstancesResultDialogFragment.newInstance(trackedEntityInstances, orgUnit);
                if (QueryTrackedEntityInstancesDialogFragment.this.activity != null) {
                    dialog.show(QueryTrackedEntityInstancesDialogFragment.this.activity.getSupportFragmentManager());
                }
            }
        });
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity != null && (activity instanceof FragmentActivity)) {
            this.activity = (FragmentActivity) activity;
        }
    }

    public void onDetach() {
        super.onDetach();
        GpsController.disableGps();
    }
}
