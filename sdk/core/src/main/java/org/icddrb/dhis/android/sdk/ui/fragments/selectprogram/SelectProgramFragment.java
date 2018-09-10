package org.icddrb.dhis.android.sdk.ui.fragments.selectprogram;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.squareup.otto.Subscribe;
import java.util.Arrays;
import org.icddrb.dhis.android.sdk.R;
import org.icddrb.dhis.android.sdk.controllers.DhisService;
import org.icddrb.dhis.android.sdk.controllers.SyncStrategy;
import org.icddrb.dhis.android.sdk.events.UiEvent;
import org.icddrb.dhis.android.sdk.events.UiEvent.UiEventType;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.ui.activities.SynchronisationStateHandler;
import org.icddrb.dhis.android.sdk.ui.activities.SynchronisationStateHandler.OnSynchronisationStateListener;
import org.icddrb.dhis.android.sdk.ui.adapters.AbsAdapter;
import org.icddrb.dhis.android.sdk.ui.dialogs.AutoCompleteDialogFragment.OnOptionSelectedListener;
import org.icddrb.dhis.android.sdk.ui.dialogs.OrgUnitDialogFragment;
import org.icddrb.dhis.android.sdk.ui.dialogs.ProgramDialogFragment;
import org.icddrb.dhis.android.sdk.ui.dialogs.UpcomingEventsDialogFilter.Type;
import org.icddrb.dhis.android.sdk.ui.views.CardTextViewButton;
import org.icddrb.dhis.android.sdk.utils.api.ProgramType;
import org.icddrb.dhis.client.sdk.ui.fragments.BaseFragment;

public abstract class SelectProgramFragment extends BaseFragment implements OnClickListener, OnOptionSelectedListener, OnRefreshListener, LoaderCallbacks<SelectProgramFragmentForm>, OnSynchronisationStateListener {
    public static final String TAG = SelectProgramFragment.class.getSimpleName();
    protected final int LOADER_ID;
    protected final String STATE;
    protected AbsAdapter mAdapter;
    protected ListView mListView;
    protected CardTextViewButton mOrgUnitButton;
    protected SelectProgramFragmentPreferences mPrefs;
    protected CardTextViewButton mProgramButton;
    protected ProgressBar mProgressBar;
    protected SelectProgramFragmentState mState;
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    /* renamed from: org.icddrb.dhis.android.sdk.ui.fragments.selectprogram.SelectProgramFragment$1 */
    class C09171 implements OnClickListener {
        C09171() {
        }

        public void onClick(View v) {
            OrgUnitDialogFragment.newInstance(SelectProgramFragment.this, SelectProgramFragment.this.getProgramTypes()).show(SelectProgramFragment.this.getChildFragmentManager());
        }
    }

    /* renamed from: org.icddrb.dhis.android.sdk.ui.fragments.selectprogram.SelectProgramFragment$2 */
    class C09182 implements OnClickListener {
        C09182() {
        }

        public void onClick(View v) {
            ProgramDialogFragment.newInstance(SelectProgramFragment.this, SelectProgramFragment.this.mState.getOrgUnitId(), SelectProgramFragment.this.getProgramTypes()).show(SelectProgramFragment.this.getChildFragmentManager());
        }
    }

    protected abstract AbsAdapter getAdapter(Bundle bundle);

    protected abstract ProgramType[] getProgramTypes();

    protected abstract void handleViews(int i);

    public abstract boolean onContextItemSelected(MenuItem menuItem);

    public abstract void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenuInfo contextMenuInfo);

    public abstract Loader<SelectProgramFragmentForm> onCreateLoader(int i, Bundle bundle);

    public SelectProgramFragment() {
        this("state:SelectProgramFragment", 1);
    }

    public SelectProgramFragment(String stateName, int loaderId) {
        this.STATE = stateName;
        this.LOADER_ID = loaderId;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_program, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.mPrefs = new SelectProgramFragmentPreferences(getActivity().getApplicationContext());
        this.mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_to_refresh_layout);
        this.mSwipeRefreshLayout.setColorSchemeResources(R.color.Green, R.color.Blue, R.color.orange);
        this.mSwipeRefreshLayout.setOnRefreshListener(this);
        this.mListView = (ListView) view.findViewById(R.id.event_listview);
        this.mAdapter = getAdapter(savedInstanceState);
        View header = getListViewHeader(savedInstanceState);
        setStandardButtons(header);
        this.mListView.addHeaderView(header, TAG, false);
        this.mListView.setAdapter(this.mAdapter);
        registerForContextMenu(this.mListView);
        if (!(savedInstanceState == null || savedInstanceState.getParcelable(this.STATE) == null)) {
            this.mState = (SelectProgramFragmentState) savedInstanceState.getParcelable(this.STATE);
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

    protected View getListViewHeader(Bundle savedInstanceState) {
        return getLayoutInflater(savedInstanceState).inflate(R.layout.fragment_select_program_header, this.mListView, false);
    }

    protected void setStandardButtons(View header) {
        this.mProgressBar = (ProgressBar) header.findViewById(R.id.progress_bar);
        this.mProgressBar.setVisibility(8);
        this.mOrgUnitButton = (CardTextViewButton) header.findViewById(R.id.select_organisation_unit);
        this.mProgramButton = (CardTextViewButton) header.findViewById(R.id.select_program);
        this.mOrgUnitButton.setOnClickListener(new C09171());
        this.mProgramButton.setOnClickListener(new C09182());
        this.mOrgUnitButton.setEnabled(true);
        this.mProgramButton.setEnabled(false);
    }

    public void onPause() {
        super.onPause();
        Dhis2Application.getEventBus().unregister(this);
        SynchronisationStateHandler.getInstance().removeListener();
    }

    public void onResume() {
        super.onResume();
        SynchronisationStateHandler.getInstance().setListener(this);
        setRefreshing(SynchronisationStateHandler.getInstance().getState());
        Dhis2Application.getEventBus().register(this);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onSaveInstanceState(Bundle out) {
        out.putParcelable(this.STATE, this.mState);
        super.onSaveInstanceState(out);
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

    public void onLoaderReset(Loader<SelectProgramFragmentForm> loader) {
        this.mAdapter.swapData(null);
    }

    public void onRefreshFinished() {
        setRefreshing(false);
    }

    public void onLoadFinished(Loader<SelectProgramFragmentForm> loader, SelectProgramFragmentForm data) {
        if (this.LOADER_ID == loader.getId()) {
            this.mProgressBar.setVisibility(8);
            this.mAdapter.swapData(data.getEventRowList());
            setRefreshing(false);
        }
    }

    public void onRefresh() {
        if (isAdded()) {
            Context context = getActivity().getBaseContext();
            Toast.makeText(context, getString(R.string.syncing), 0).show();
            DhisService.synchronize(context, SyncStrategy.DOWNLOAD_ALL);
        }
    }

    protected void setRefreshing(final boolean refreshing) {
        if (this.mSwipeRefreshLayout.isRefreshing() != refreshing) {
            this.mSwipeRefreshLayout.post(new Runnable() {
                public void run() {
                    SelectProgramFragment.this.mSwipeRefreshLayout.setRefreshing(refreshing);
                }
            });
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
        handleViews(0);
    }

    public void onProgramSelected(String programId, String programName) {
        this.mProgramButton.setText(programName);
        this.mState.setProgram(programId, programName);
        this.mPrefs.putProgram(new Pair(programId, programName));
        handleViews(1);
        this.mProgressBar.setVisibility(0);
        getLoaderManager().restartLoader(this.LOADER_ID, getArguments(), this);
    }

    @Subscribe
    public void onReceivedUiEvent(UiEvent uiEvent) {
        if (uiEvent.getEventType().equals(UiEventType.SYNCING_START)) {
            setRefreshing(true);
        } else if (uiEvent.getEventType().equals(UiEventType.SYNCING_END)) {
            setRefreshing(false);
        }
    }

    public void stateChanged() {
    }
}
