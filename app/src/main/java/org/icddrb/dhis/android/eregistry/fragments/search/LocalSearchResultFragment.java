package org.icddrb.dhis.android.eregistry.fragments.search;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.raizlabs.android.dbflow.structure.Model;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import org.icddrb.dhis.android.eregistry.C0773R;
import org.icddrb.dhis.android.eregistry.activities.HolderActivity;
import org.icddrb.dhis.android.eregistry.fragments.selectprogram.EnrollmentDateSetterHelper;
import org.icddrb.dhis.android.eregistry.fragments.selectprogram.IEnroller;
import org.icddrb.dhis.android.eregistry.fragments.selectprogram.dialogs.ItemStatusDialogFragment;
import org.icddrb.dhis.android.eregistry.ui.adapters.TrackedEntityInstanceAdapter;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.controllers.tracker.TrackerController;
import org.icddrb.dhis.android.sdk.events.OnRowClick.ITEM_STATUS;
import org.icddrb.dhis.android.sdk.events.OnTrackerItemClick;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.loaders.DbLoader;
import org.icddrb.dhis.android.sdk.persistence.models.BaseSerializableModel;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.FailedItem;
import org.icddrb.dhis.android.sdk.persistence.models.Program;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.events.TrackedEntityInstanceItemRow;
import org.icddrb.dhis.android.sdk.ui.views.FloatingActionButton;
import org.icddrb.dhis.android.sdk.utils.UiUtils;
import org.joda.time.DateTime;

public class LocalSearchResultFragment extends Fragment implements LoaderCallbacks<LocalSearchResultFragmentForm>, OnClickListener, IEnroller {
    public static final String EXTRA_ATTRIBUTEVALUEMAP = "extra:AttributeValueMap";
    public static final String EXTRA_ORGUNIT = "extra:OrgUnitId";
    public static final String EXTRA_PROGRAM = "extra:ProgramId";
    private final int LOADER_ID = 1112222111;
    private HashMap<String, String> attributeValueMap;
    private CardView cardView;
    private TrackedEntityInstanceAdapter mAdapter;
    private RelativeLayout mAddLayout;
    private LocalSearchResultFragmentForm mForm;
    private FloatingActionButton mRegisterEventButton;
    private String orgUnitId;
    private String programId;
    private ProgressBar progressBar;
    private ListView searchResultsListView;

    public static LocalSearchResultFragment newInstance(String orgUnitId, String programId, HashMap<String, String> attributeValueMap) {
        LocalSearchResultFragment fragment = new LocalSearchResultFragment();
        Bundle args = new Bundle();
        args.putString("extra:OrgUnitId", orgUnitId);
        args.putString("extra:ProgramId", programId);
        args.putSerializable(EXTRA_ATTRIBUTEVALUEMAP, attributeValueMap);
        fragment.setArguments(args);
        Log.d("Norway - HashMap size", attributeValueMap.size() + "");
        return fragment;
    }

    private boolean isClientRegister(String programId) {
        return programId.equals("ZBIqxwVixn8");
    }

    public boolean isElementTest() {
        return this.programId.equals("AYpOmMHjDPK");
    }

    public boolean isMCH() {
        return this.programId.equals("WSGAb5XwJ3Y") || this.programId.equals("JrW4FqXO6Mr");
    }

    private void createEnrollment() {
        Program p = MetaDataController.getProgram(this.programId);
        if (this.mForm != null && p != null) {
            EnrollmentDateSetterHelper.createEnrollment(this, getActivity(), p.getDisplayIncidentDate(), p.getSelectEnrollmentDatesInFuture(), p.getSelectIncidentDatesInFuture(), p.getEnrollmentDateLabel(), p.getIncidentDateLabel());
        }
    }

    public void showEnrollmentFragment(TrackedEntityInstance trackedEntityInstance, DateTime enrollmentDate, DateTime incidentDate) {
        String enrollmentDateString = enrollmentDate.toString();
        String incidentDateString = null;
        if (incidentDate != null) {
            incidentDateString = incidentDate.toString();
        }
        if (trackedEntityInstance == null) {
            HolderActivity.navigateToEnrollmentDataEntryFragment(getActivity(), this.orgUnitId, this.programId, enrollmentDateString, incidentDateString);
        } else {
            HolderActivity.navigateToEnrollmentDataEntryFragment(getActivity(), this.orgUnitId, this.programId, trackedEntityInstance.getLocalId(), enrollmentDateString, incidentDateString);
        }
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        this.orgUnitId = args.getString("extra:OrgUnitId");
        this.programId = args.getString("extra:ProgramId");
        this.attributeValueMap = (HashMap) args.getSerializable(EXTRA_ATTRIBUTEVALUEMAP);
        setHasOptionsMenu(true);
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(C0773R.layout.fragment_local_search_results, container, false);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        getActivity().finish();
        return true;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() instanceof AppCompatActivity) {
            getActionBar().setDisplayShowTitleEnabled(true);
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }
        this.searchResultsListView = (ListView) view.findViewById(C0773R.id.listview_search_results);
        this.progressBar = (ProgressBar) view.findViewById(C0773R.id.local_search_progressbar);
        this.cardView = (CardView) view.findViewById(C0773R.id.search_online_cardview);
        this.mRegisterEventButton = (FloatingActionButton) view.findViewById(C0773R.id.register_new_event);
        this.mAddLayout = (RelativeLayout) view.findViewById(C0773R.id.add_layout);
        this.mAdapter = new TrackedEntityInstanceAdapter(getLayoutInflater(savedInstanceState));
        this.searchResultsListView.setAdapter(this.mAdapter);
        this.progressBar.setVisibility(0);
        this.cardView.setVisibility(8);
        this.cardView.setOnClickListener(this);
        if (isClientRegister(this.programId)) {
            this.mAddLayout.setVisibility(0);
            this.mRegisterEventButton.setOnClickListener(this);
            return;
        }
        this.mAddLayout.setVisibility(8);
        this.mRegisterEventButton.setVisibility(8);
    }

    private ActionBar getActionBar() {
        if (getActivity() != null && (getActivity() instanceof AppCompatActivity)) {
            return ((AppCompatActivity) getActivity()).getSupportActionBar();
        }
        throw new IllegalArgumentException("Fragment should be attached to ActionBarActivity");
    }

    public boolean onContextItemSelected(MenuItem item) {
        final TrackedEntityInstanceItemRow itemRow = (TrackedEntityInstanceItemRow) this.searchResultsListView.getItemAtPosition(((AdapterContextMenuInfo) item.getMenuInfo()).position);
        if (item.getTitle().toString().equals(getResources().getString(C0773R.string.go_to_programoverview_fragment))) {
            HolderActivity.navigateToProgramOverviewFragment(getActivity(), this.orgUnitId, this.programId, itemRow.getTrackedEntityInstance().getLocalId());
        } else if (item.getTitle().toString().equals(getResources().getString(C0773R.string.delete))) {
            if (itemRow.getStatus().equals(ITEM_STATUS.SENT)) {
                performSoftDeleteOfTrackedEntityInstance(itemRow.getTrackedEntityInstance());
            } else {
                UiUtils.showConfirmDialog(getActivity(), getActivity().getString(C0773R.string.confirm), getActivity().getString(C0773R.string.warning_delete_unsent_tei), getActivity().getString(C0773R.string.delete), getActivity().getString(C0773R.string.cancel), (int) C0773R.drawable.ic_event_error, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        LocalSearchResultFragment.this.performSoftDeleteOfTrackedEntityInstance(itemRow.getTrackedEntityInstance());
                        dialog.dismiss();
                    }
                });
            }
        }
        return true;
    }

    public void performSoftDeleteOfTrackedEntityInstance(TrackedEntityInstance trackedEntityInstance) {
        Enrollment activeEnrollment = null;
        for (Enrollment enrollment : TrackerController.getEnrollments(this.programId, trackedEntityInstance)) {
            if ("ACTIVE".equals(enrollment.getStatus())) {
                activeEnrollment = enrollment;
            }
        }
        if (activeEnrollment != null) {
            List<Event> eventsForActiveEnrollment = TrackerController.getEventsByEnrollment(activeEnrollment.getLocalId());
            if (eventsForActiveEnrollment != null) {
                for (Event event : eventsForActiveEnrollment) {
                    event.delete();
                }
            }
            activeEnrollment.delete();
        }
    }

    public void onPause() {
        super.onPause();
        Dhis2Application.getEventBus().unregister(this);
    }

    public void onResume() {
        super.onResume();
        Dhis2Application.getEventBus().register(this);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getLoaderManager().restartLoader(1112222111, getArguments(), this);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = new Bundle();
        bundle.putString("extra:OrgUnitId", this.orgUnitId);
        bundle.putString("extra:ProgramId", this.programId);
        bundle.putSerializable(EXTRA_ATTRIBUTEVALUEMAP, this.attributeValueMap);
        getLoaderManager().initLoader(1112222111, bundle, this);
        getActionBar().setTitle((CharSequence) "Local search results");
        for (Entry<String, String> x : this.attributeValueMap.entrySet()) {
            System.out.println("Norway - attr id: " + ((String) x.getKey()) + "  value: " + ((String) x.getValue()));
        }
    }

    public Loader<LocalSearchResultFragmentForm> onCreateLoader(int id, Bundle args) {
        if (1112222111 != id || !isAdded()) {
            return null;
        }
        String orgUnitId = args.getString("extra:OrgUnitId");
        String programId = args.getString("extra:ProgramId");
        HashMap<String, String> attributeValueMap = (HashMap) args.getSerializable(EXTRA_ATTRIBUTEVALUEMAP);
        List<Class<? extends Model>> modelsToTrack = new ArrayList();
        modelsToTrack.add(TrackedEntityInstance.class);
        modelsToTrack.add(Enrollment.class);
        modelsToTrack.add(Event.class);
        modelsToTrack.add(FailedItem.class);
        return new DbLoader(getActivity().getBaseContext(), modelsToTrack, new LocalSearchResultFragmentFormQuery(orgUnitId, programId, attributeValueMap));
    }

    public void onLoadFinished(Loader<LocalSearchResultFragmentForm> loader, LocalSearchResultFragmentForm data) {
        if (1112222111 == loader.getId()) {
            this.progressBar.setVisibility(8);
            this.mForm = data;
            this.mAdapter.setData(data.getEventRowList());
            this.mAdapter.getFilter().filter(Integer.toString(3));
        }
    }

    @Subscribe
    public void onItemClick(OnTrackerItemClick eventClick) {
        if (eventClick.isOnDescriptionClick()) {
            HolderActivity.navigateToProgramOverviewFragment(getActivity(), this.orgUnitId, this.programId, eventClick.getItem().getLocalId());
        } else {
            showStatusDialog(eventClick.getItem());
        }
    }

    public void showStatusDialog(BaseSerializableModel model) {
        ItemStatusDialogFragment.newInstance(model).show(getChildFragmentManager());
    }

    public void onLoaderReset(Loader<LocalSearchResultFragmentForm> loader) {
    }

    public void searchOnline() {
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0773R.id.register_new_event:
                break;
            case C0773R.id.search_online_cardview:
                searchOnline();
                break;
            default:
                return;
        }
        createEnrollment();
    }
}
