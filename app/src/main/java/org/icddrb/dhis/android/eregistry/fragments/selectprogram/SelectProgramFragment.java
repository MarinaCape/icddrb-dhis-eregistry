package org.icddrb.dhis.android.eregistry.fragments.selectprogram;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnCloseListener;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.TextView;
import com.raizlabs.android.dbflow.structure.Model;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.List;
import org.icddrb.dhis.android.eregistry.C0773R;
import org.icddrb.dhis.android.eregistry.activities.HolderActivity;
import org.icddrb.dhis.android.eregistry.fragments.selectprogram.dialogs.ItemStatusDialogFragment;
import org.icddrb.dhis.android.eregistry.fragments.selectprogram.dialogs.QueryTrackedEntityInstancesDialogFragment;
import org.icddrb.dhis.android.eregistry.ui.DownloadEventSnackbar;
import org.icddrb.dhis.android.eregistry.ui.adapters.TrackedEntityInstanceAdapter;
import org.icddrb.dhis.android.sdk.controllers.ApiEndpointContainer;
import org.icddrb.dhis.android.sdk.controllers.DhisController;
import org.icddrb.dhis.android.sdk.controllers.tracker.TrackerController;
import org.icddrb.dhis.android.sdk.events.OnRowClick.ITEM_STATUS;
import org.icddrb.dhis.android.sdk.events.OnTeiDownloadedEvent;
import org.icddrb.dhis.android.sdk.events.OnTeiDownloadedEvent.EventType;
import org.icddrb.dhis.android.sdk.events.OnTrackerItemClick;
import org.icddrb.dhis.android.sdk.events.UiEvent;
import org.icddrb.dhis.android.sdk.events.UiEvent.UiEventType;
import org.icddrb.dhis.android.sdk.job.JobExecutor;
import org.icddrb.dhis.android.sdk.job.NetworkJob;
import org.icddrb.dhis.android.sdk.network.APIException;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.loaders.DbLoader;
import org.icddrb.dhis.android.sdk.persistence.models.BaseSerializableModel;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.FailedItem;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.persistence.preferences.ResourceType;
import org.icddrb.dhis.android.sdk.ui.activities.SynchronisationStateHandler;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.events.OnTrackedEntityInstanceColumnClick;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.events.TrackedEntityInstanceItemRow;
import org.icddrb.dhis.android.sdk.ui.fragments.selectprogram.SelectProgramFragmentForm;
import org.icddrb.dhis.android.sdk.ui.views.FloatingActionButton;
import org.icddrb.dhis.android.sdk.utils.NetworkUtils;
import org.icddrb.dhis.android.sdk.utils.UiUtils;
import org.icddrb.dhis.android.sdk.utils.api.ProgramType;
import org.joda.time.DateTime;

public class SelectProgramFragment extends org.icddrb.dhis.android.sdk.ui.fragments.selectprogram.SelectProgramFragment implements OnQueryTextListener, OnFocusChangeListener, OnCloseListener, OnActionExpandListener, LoaderCallbacks<SelectProgramFragmentForm>, IEnroller {
    public static final String TAG = SelectProgramFragment.class.getSimpleName();
    private List<Enrollment> downloadedEnrollments;
    private List<TrackedEntityInstance> downloadedTrackedEntityInstances;
    private MenuItem item;
    private SelectProgramFragmentForm mForm;
    private FloatingActionButton mLocalSearchButton;
    private FloatingActionButton mQueryAllButton;
    private FloatingActionButton mQueryTrackedEntityInstancesButton;
    private FloatingActionButton mRegisterEventButton;
    private FloatingActionButton mUpcomingEventsButton;
    protected TextView noRowsTextView;
    private DownloadEventSnackbar snackbar;

    /* renamed from: org.icddrb.dhis.android.eregistry.fragments.selectprogram.SelectProgramFragment$1 */
    class C08091 implements OnClickListener {
        C08091() {
        }

        public void onClick(View view) {
            SelectProgramFragment.this.toggleNavigationDrawer();
        }
    }

    /* renamed from: org.icddrb.dhis.android.eregistry.fragments.selectprogram.SelectProgramFragment$3 */
    class C08113 implements Runnable {
        C08113() {
        }

        public void run() {
            Dhis2Application.getEventBus().post(new UiEvent(UiEventType.SYNCING_START));
        }
    }

    protected TrackedEntityInstanceAdapter getAdapter(Bundle savedInstanceState) {
        return new TrackedEntityInstanceAdapter(getLayoutInflater(savedInstanceState));
    }

    protected View getListViewHeader(Bundle savedInstanceState) {
        if (getActivity() instanceof AppCompatActivity) {
            Toolbar toolbar = getParentToolbar();
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new C08091());
        }
        View header = getLayoutInflater(savedInstanceState).inflate(C0773R.layout.fragment_select_program_header, this.mListView, false);
        this.mRegisterEventButton = (FloatingActionButton) header.findViewById(C0773R.id.register_new_event);
        this.mQueryTrackedEntityInstancesButton = (FloatingActionButton) header.findViewById(C0773R.id.query_trackedentityinstances_button);
        this.mQueryAllButton = (FloatingActionButton) header.findViewById(C0773R.id.query_all_button);
        this.mUpcomingEventsButton = (FloatingActionButton) header.findViewById(C0773R.id.upcoming_events_button);
        this.mLocalSearchButton = (FloatingActionButton) header.findViewById(C0773R.id.local_search_button);
        this.noRowsTextView = (TextView) header.findViewById(C0773R.id.textview_no_items);
        this.noRowsTextView.setText(getString(C0773R.string.specify_search_criteria));
        this.mRegisterEventButton.setOnClickListener(this);
        this.mQueryTrackedEntityInstancesButton.setOnClickListener(this);
        this.mQueryAllButton.setOnClickListener(this);
        this.mUpcomingEventsButton.setOnClickListener(this);
        this.mLocalSearchButton.setOnClickListener(this);
        this.mRegisterEventButton.hide();
        this.mUpcomingEventsButton.hide();
        this.mQueryTrackedEntityInstancesButton.hide();
        this.mQueryAllButton.hide();
        this.mLocalSearchButton.hide();
        this.noRowsTextView.setVisibility(8);
        return header;
    }

    protected ProgramType[] getProgramTypes() {
        return new ProgramType[]{ProgramType.WITH_REGISTRATION};
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getLoaderManager().restartLoader(this.LOADER_ID, getArguments(), this);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(C0773R.menu.menu_select_program, menu);
        this.item = menu.findItem(C0773R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(this.item);
        MenuItemCompat.setOnActionExpandListener(this.item, this);
        searchView.setOnQueryTextListener(this);
        searchView.setOnQueryTextFocusChangeListener(this);
        searchView.setOnCloseListener(this);
    }

    public Loader<SelectProgramFragmentForm> onCreateLoader(int id, Bundle args) {
        if (this.LOADER_ID != id || !isAdded()) {
            return null;
        }
        List<Class<? extends Model>> modelsToTrack = new ArrayList();
        modelsToTrack.add(TrackedEntityInstance.class);
        modelsToTrack.add(Enrollment.class);
        modelsToTrack.add(Event.class);
        modelsToTrack.add(FailedItem.class);
        return new DbLoader(getActivity().getBaseContext(), modelsToTrack, new SelectProgramFragmentQuery(this.mState.getOrgUnitId(), this.mState.getProgramId()));
    }

    @Subscribe
    public void onItemClick(OnTrackerItemClick eventClick) {
        if (eventClick.isOnDescriptionClick()) {
            HolderActivity.navigateToProgramOverviewFragment(getActivity(), this.mState.getOrgUnitId(), this.mState.getProgramId(), eventClick.getItem().getLocalId());
        } else {
            showStatusDialog(eventClick.getItem());
        }
    }

    @Subscribe
    public void onReceivedUiEvent(UiEvent uiEvent) {
        if (uiEvent.getEventType() == UiEventType.SYNCING_END) {
            getLoaderManager().restartLoader(this.LOADER_ID, getArguments(), this);
        }
        super.onReceivedUiEvent(uiEvent);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0773R.id.local_search_button:
                HolderActivity.navigateToLocalSearchFragment(getActivity(), this.mState.getOrgUnitId(), this.mState.getProgramId());
                return;
            case C0773R.id.query_all_button:
                downloadAll(this.mState.getOrgUnitId(), this.mState.getProgramId());
                return;
            case C0773R.id.query_trackedentityinstances_button:
                showOnlineSearchFragment(this.mState.getOrgUnitId(), this.mState.getProgramId());
                return;
            case C0773R.id.register_new_event:
                createEnrollment();
                return;
            case C0773R.id.upcoming_events_button:
                HolderActivity.navigateToUpcomingEventsFragment(getActivity());
                return;
            default:
                return;
        }
    }

    private void createEnrollment() {
        if (this.mForm != null && this.mForm.getProgram() != null) {
            EnrollmentDateSetterHelper.createEnrollment(this, getActivity(), this.mForm.getProgram().getDisplayIncidentDate(), this.mForm.getProgram().getSelectEnrollmentDatesInFuture(), this.mForm.getProgram().getSelectIncidentDatesInFuture(), this.mForm.getProgram().getEnrollmentDateLabel(), this.mForm.getProgram().getIncidentDateLabel());
        }
    }

    public void showEnrollmentFragment(TrackedEntityInstance trackedEntityInstance, DateTime enrollmentDate, DateTime incidentDate) {
        String enrollmentDateString = enrollmentDate.toString();
        String incidentDateString = null;
        if (incidentDate != null) {
            incidentDateString = incidentDate.toString();
        }
        if (trackedEntityInstance == null) {
            HolderActivity.navigateToEnrollmentDataEntryFragment(getActivity(), this.mState.getOrgUnitId(), this.mState.getProgramId(), enrollmentDateString, incidentDateString);
        } else {
            HolderActivity.navigateToEnrollmentDataEntryFragment(getActivity(), this.mState.getOrgUnitId(), this.mState.getProgramId(), trackedEntityInstance.getLocalId(), enrollmentDateString, incidentDateString);
        }
    }

    private static final void showQueryTrackedEntityInstancesDialog(FragmentManager fragmentManager, String orgUnit, String program) {
        QueryTrackedEntityInstancesDialogFragment.newInstance(program, orgUnit).show(fragmentManager);
    }

    public final void showOnlineSearchFragment() {
        showOnlineSearchFragment(this.mState.getOrgUnitId(), this.mState.getProgramId());
    }

    private final void showOnlineSearchFragment(String orgUnit, String program) throws APIException {
        HolderActivity.navigateToOnlineSearchFragment(getActivity(), program, orgUnit, false, null);
    }

    public final void downloadAll() {
        downloadAll(this.mState.getOrgUnitId(), this.mState.getProgramId());
    }

    @SuppressLint({"StaticFieldLeak"})
    public final void downloadAll(String orgUnit, String program) throws APIException {
        final TrackedEntityAttributeValue[] params = (TrackedEntityAttributeValue[]) new ArrayList().toArray(new TrackedEntityAttributeValue[0]);
        final String str = orgUnit;
        final String str2 = program;
        JobExecutor.enqueueJob(new NetworkJob<Object>(1, null) {
            public Object execute() throws APIException {
                String unionId = Dhis2Application.dhisController.getAppPreferences().getRootUnionId(str, NetworkUtils.unwrapResponse(Dhis2Application.dhisController.getDhisApi().getOrgsAndUsers(), ApiEndpointContainer.ORGANISATIONUNITS));
                System.out.println("Norway - current orgId: " + str + " main union: " + unionId);
                if (unionId == null) {
                    unionId = str;
                }
                SelectProgramFragment.this.initiateDownload(TrackerController.queryTrackedEntityInstancesDataFromAllAccessibleOrgUnits(DhisController.getInstance().getDhisApi(), str, str2, unionId, true, params), str2);
                return new Object();
            }
        });
    }

    @SuppressLint({"StaticFieldLeak"})
    private final void initiateDownload(final List<TrackedEntityInstance> trackedEntityInstancesQueryResult, String programId) {
        Dhis2Application.getEventBus().post(new OnTeiDownloadedEvent(EventType.START, trackedEntityInstancesQueryResult.size()));
        Log.d(TAG, "loading: " + trackedEntityInstancesQueryResult.size());
        this.downloadedTrackedEntityInstances = new ArrayList();
        this.downloadedEnrollments = new ArrayList();
        getActivity().runOnUiThread(new C08113());
        JobExecutor.enqueueJob(new NetworkJob<Object>(0, ResourceType.TRACKEDENTITYINSTANCE) {
            public Object execute() throws APIException {
                SynchronisationStateHandler.getInstance().changeState(true);
                List<TrackedEntityInstance> trackedEntityInstances = TrackerController.getTrackedEntityInstancesDataFromServer(DhisController.getInstance().getDhisApi(), trackedEntityInstancesQueryResult, true, true);
                if (trackedEntityInstances != null) {
                    if (SelectProgramFragment.this.downloadedTrackedEntityInstances == null) {
                        SelectProgramFragment.this.downloadedTrackedEntityInstances = new ArrayList();
                    }
                    SelectProgramFragment.this.downloadedTrackedEntityInstances.addAll(trackedEntityInstances);
                }
                for (int i = 0; i < SelectProgramFragment.this.downloadedTrackedEntityInstances.size(); i++) {
                    List<Enrollment> enrollments = TrackerController.getEnrollmentDataFromServer(DhisController.getInstance().getDhisApi(), (TrackedEntityInstance) SelectProgramFragment.this.downloadedTrackedEntityInstances.get(i), null);
                    if (enrollments != null) {
                        if (SelectProgramFragment.this.downloadedEnrollments == null) {
                            SelectProgramFragment.this.downloadedEnrollments = new ArrayList();
                        }
                        SelectProgramFragment.this.downloadedEnrollments.addAll(enrollments);
                    }
                    Dhis2Application.getEventBus().post(new OnTeiDownloadedEvent(EventType.UPDATE, trackedEntityInstances.size(), (int) Math.ceil(((double) ((SelectProgramFragment.this.downloadedTrackedEntityInstances.size() + i) + 1)) / 2.0d)));
                }
                Dhis2Application.getEventBus().post(new OnTeiDownloadedEvent(EventType.END, trackedEntityInstancesQueryResult.size()));
                Dhis2Application.getEventBus().post(new UiEvent(UiEventType.SYNCING_END));
                SynchronisationStateHandler.getInstance().changeState(false);
                return new Object();
            }
        });
    }

    public void showStatusDialog(BaseSerializableModel model) {
        ItemStatusDialogFragment.newInstance(model).show(getChildFragmentManager());
    }

    protected void handleViews(int level) {
        this.mAdapter.swapData(null);
        switch (level) {
            case 0:
                this.mRegisterEventButton.hide();
                this.mUpcomingEventsButton.hide();
                this.mQueryTrackedEntityInstancesButton.hide();
                this.mQueryAllButton.hide();
                this.mLocalSearchButton.hide();
                return;
            case 1:
                if (this.mState.isClientRegister() || this.mState.isMCH()) {
                    this.mRegisterEventButton.hide();
                } else {
                    this.mRegisterEventButton.show();
                }
                this.mUpcomingEventsButton.hide();
                this.mQueryTrackedEntityInstancesButton.hide();
                this.mQueryAllButton.show();
                this.mLocalSearchButton.show();
                return;
            default:
                return;
        }
    }

    public void onLoadFinished(Loader<SelectProgramFragmentForm> loader, SelectProgramFragmentForm data) {
        if (this.LOADER_ID == loader.getId()) {
            this.mProgressBar.setVisibility(8);
            this.mForm = data;
            ((TrackedEntityInstanceAdapter) this.mAdapter).setData(data.getEventRowList());
            this.mAdapter.swapData(data.getEventRowList());
            if (data.getProgram() == null || !data.getProgram().isDisplayFrontPageList()) {
                this.noRowsTextView.setVisibility(0);
                if (this.item != null) {
                    this.item.setVisible(false);
                    return;
                }
                return;
            }
            this.noRowsTextView.setVisibility(8);
            if (this.item != null) {
                this.item.setVisible(true);
            }
        }
    }

    public boolean onQueryTextSubmit(String query) {
        Log.d(TAG, query);
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getActivity().getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 2);
        }
        return true;
    }

    public boolean onQueryTextChange(String newText) {
        Log.d(TAG, newText);
        ((TrackedEntityInstanceAdapter) this.mAdapter).getFilter().filter(1 + newText);
        return true;
    }

    public void onFocusChange(View view, boolean hasFocus) {
        if ((view instanceof SearchView) && ((SearchView) view).getQuery().length() == 0) {
            ((TrackedEntityInstanceAdapter) this.mAdapter).getFilter().filter("");
        }
    }

    private void setFocusSortColumn(int column) {
        TrackedEntityInstanceAdapter teiAdapter = this.mAdapter;
        View view = this.mForm.getColumnNames().getView();
        switch (column) {
            case 1:
                if (teiAdapter.getFilteredColumn() != column && teiAdapter.isListIsReversed(column)) {
                    return;
                }
                return;
            default:
                return;
        }
    }

    @Subscribe
    public void onItemClick(OnTrackedEntityInstanceColumnClick eventClick) {
        Log.d(TAG, "COLUMN CLICKED : " + eventClick.getColumnClicked());
        switch (eventClick.getColumnClicked()) {
            case 1:
                Log.d(TAG, "Filter column 3");
                ((TrackedEntityInstanceAdapter) this.mAdapter).getFilter().filter("3");
                setFocusSortColumn(1);
                return;
            case 2:
                Log.d(TAG, "Filter column 4");
                ((TrackedEntityInstanceAdapter) this.mAdapter).getFilter().filter("4");
                return;
            case 3:
                Log.d(TAG, "Filter column 5");
                ((TrackedEntityInstanceAdapter) this.mAdapter).getFilter().filter("5");
                return;
            case 4:
                Log.d(TAG, "Filter column 2");
                ((TrackedEntityInstanceAdapter) this.mAdapter).getFilter().filter("2");
                return;
            default:
                return;
        }
    }

    @Subscribe
    public void onTeiDownloaded(OnTeiDownloadedEvent event) {
        if (getActivity() != null && isAdded()) {
            if (this.snackbar == null) {
                this.snackbar = new DownloadEventSnackbar(this);
            }
            this.snackbar.show(event);
        }
    }

    public boolean onMenuItemActionExpand(MenuItem item) {
        Log.d(TAG, "onMenuItemActionExpand");
        return true;
    }

    public boolean onMenuItemActionCollapse(MenuItem item) {
        ((TrackedEntityInstanceAdapter) this.mAdapter).getFilter().filter("");
        return true;
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        new MenuInflater(getActivity()).inflate(C0773R.menu.menu_selected_trackedentityinstance, menu);
        MenuItem menuItem = menu.findItem(C0773R.id.action_delete);
        if (menuItem != null) {
            TrackedEntityInstanceItemRow itemRow = (TrackedEntityInstanceItemRow) this.mListView.getItemAtPosition(((AdapterContextMenuInfo) menuItem.getMenuInfo()).position);
            if (itemRow != null && itemRow.getStatus().equals(ITEM_STATUS.SENT)) {
                menuItem.setTitle(C0773R.string.remove);
            }
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        final TrackedEntityInstanceItemRow itemRow = (TrackedEntityInstanceItemRow) this.mListView.getItemAtPosition(((AdapterContextMenuInfo) item.getMenuInfo()).position);
        Log.d(TAG, "" + itemRow.getTrackedEntityInstance().getTrackedEntityInstance());
        if (item.getItemId() == C0773R.id.action_overview) {
            HolderActivity.navigateToProgramOverviewFragment(getActivity(), this.mState.getOrgUnitId(), this.mState.getProgramId(), itemRow.getTrackedEntityInstance().getLocalId());
        } else if (item.getItemId() == C0773R.id.action_delete) {
            if (itemRow.getStatus().equals(ITEM_STATUS.SENT)) {
                UiUtils.showConfirmDialog(getActivity(), getActivity().getString(C0773R.string.remove_report_entity_dialog_title), getActivity().getString(C0773R.string.remove_report_entity_dialog_message), getActivity().getString(C0773R.string.remove), getActivity().getString(C0773R.string.cancel), (int) C0773R.drawable.ic_warning_black_24dp, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SelectProgramFragment.this.performSoftDeleteOfTrackedEntityInstance(itemRow.getTrackedEntityInstance());
                        dialog.dismiss();
                    }
                });
            } else {
                UiUtils.showConfirmDialog(getActivity(), getActivity().getString(C0773R.string.confirm), getActivity().getString(C0773R.string.warning_delete_unsent_tei), getActivity().getString(C0773R.string.delete), getActivity().getString(C0773R.string.cancel), (int) C0773R.drawable.ic_event_error, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SelectProgramFragment.this.performSoftDeleteOfTrackedEntityInstance(itemRow.getTrackedEntityInstance());
                        dialog.dismiss();
                    }
                });
            }
        }
        return true;
    }

    public void performSoftDeleteOfTrackedEntityInstance(TrackedEntityInstance trackedEntityInstance) {
        Enrollment activeEnrollment = null;
        for (Enrollment enrollment : TrackerController.getEnrollments(this.mState.getProgramId(), trackedEntityInstance)) {
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

    public boolean onClose() {
        ((TrackedEntityInstanceAdapter) this.mAdapter).getFilter().filter("");
        return false;
    }

    public void stateChanged() {
        super.stateChanged();
    }
}
