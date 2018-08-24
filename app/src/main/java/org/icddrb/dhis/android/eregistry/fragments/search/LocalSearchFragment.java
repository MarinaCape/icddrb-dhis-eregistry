package org.icddrb.dhis.android.eregistry.fragments.search;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.Model;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.icddrb.dhis.android.eregistry.C0773R;
import org.icddrb.dhis.android.eregistry.activities.HolderActivity;
import org.icddrb.dhis.android.sdk.controllers.GpsController;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.loaders.DbLoader;
import org.icddrb.dhis.android.sdk.persistence.models.FailedItem;
import org.icddrb.dhis.android.sdk.persistence.models.Option;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.ui.adapters.DataValueAdapter;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.DataEntryRowFactory;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.DataEntryRowTypes;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.EventCoordinatesRow;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.IndicatorRow;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.StatusRow;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.events.OnDetailedInfoButtonClick;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.RowValueChangedEvent;
import org.icddrb.dhis.android.sdk.utils.UiUtils;

public class LocalSearchFragment extends Fragment implements LoaderCallbacks<LocalSearchFragmentForm> {
    public static final String EXTRA_ORGUNIT = "extra:OrgUnitId";
    public static final String EXTRA_PROGRAM = "extra:ProgramId";
    public static final String EXTRA_SAVED_INSTANCE_STATE = "extra:savedInstanceState";
    private final int LOADER_ID = 99898989;
    private DataValueAdapter mAdapter;
    private LocalSearchFragmentForm mForm;
    private String orgUnitId;
    private String programId;
    private ListView trackedEntityAttributeListView;

    /* renamed from: org.icddrb.dhis.android.eregistry.fragments.search.LocalSearchFragment$1 */
    class C07931 implements OnClickListener {
        C07931() {
        }

        public void onClick(DialogInterface dialog, int i) {
            dialog.dismiss();
        }
    }

    public static LocalSearchFragment newInstance(String orgUnitId, String programId) {
        LocalSearchFragment fragment = new LocalSearchFragment();
        Bundle args = new Bundle();
        args.putString("extra:OrgUnitId", orgUnitId);
        args.putString("extra:ProgramId", programId);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        this.programId = arguments.getString("extra:ProgramId");
        this.orgUnitId = arguments.getString("extra:OrgUnitId");
        setHasOptionsMenu(true);
    }

    public void onPause() {
        super.onPause();
        Dhis2Application.getEventBus().unregister(this);
    }

    public void onResume() {
        super.onResume();
        Dhis2Application.getEventBus().register(this);
    }

    private void printOptionStatus(String state, String org, String uid) {
        System.out.println("Norway - row change (" + state + "): " + org + " uid: " + uid + " size: " + new Select().from(Option.class).where(Condition.column("optionSet").is(uid)).queryList().size());
    }

    @Subscribe
    public void onRowValueChanged(RowValueChangedEvent event) {
        if (event.getRowType() != null && event.getRowType().equals(DataEntryRowTypes.ORGANISATION_UNIT.toString())) {
            DataEntryRowFactory.updateFWADropdown(getActivity().getBaseContext(), event);
        }
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
        UiUtils.showConfirmDialog(getActivity(), getResources().getString(C0773R.string.detailed_info_dataelement), message, getResources().getString(C0773R.string.ok_option), new C07931());
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(C0773R.menu.menu_local_search, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == C0773R.id.action_search) {
            buildQuery();
            HolderActivity.navigateToLocalSearchResultFragment(getActivity(), this.mForm.getOrganisationUnitId(), this.mForm.getProgram(), this.mForm.getAttributeValues());
        } else if (id == 16908332) {
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private ActionBar getActionBar() {
        if (getActivity() != null && (getActivity() instanceof AppCompatActivity)) {
            return ((AppCompatActivity) getActivity()).getSupportActionBar();
        }
        throw new IllegalArgumentException("Fragment should be attached to ActionBarActivity");
    }

    public void buildQuery() {
        HashMap<String, String> attributeValueMap = new HashMap();
        for (TrackedEntityAttributeValue value : this.mForm.getTrackedEntityAttributeValues()) {
            if (!(value.getValue() == null || value.getValue().isEmpty())) {
                attributeValueMap.put(value.getTrackedEntityAttributeId(), value.getValue());
            }
        }
        this.mForm.setAttributeValues(attributeValueMap);
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(C0773R.layout.activity_local_search, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() instanceof AppCompatActivity) {
            getActionBar().setTitle(getString(C0773R.string.local_search));
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }
        this.trackedEntityAttributeListView = (ListView) view.findViewById(C0773R.id.localSearchAttributeListView);
        this.mAdapter = new DataValueAdapter(getChildFragmentManager(), getActivity().getLayoutInflater(), this.trackedEntityAttributeListView, getContext());
        this.trackedEntityAttributeListView.setAdapter(this.mAdapter);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = new Bundle();
        bundle.putString("extra:OrgUnitId", this.orgUnitId);
        bundle.putString("extra:ProgramId", this.programId);
        bundle.putBundle("extra:savedInstanceState", savedInstanceState);
        getLoaderManager().initLoader(99898989, bundle, this);
    }

    public Loader<LocalSearchFragmentForm> onCreateLoader(int id, Bundle args) {
        if (99898989 != id || !isAdded()) {
            return null;
        }
        String orgUnitId = args.getString("extra:OrgUnitId");
        String programId = args.getString("extra:ProgramId");
        List<Class<? extends Model>> modelsToTrack = new ArrayList();
        modelsToTrack.add(TrackedEntityInstance.class);
        modelsToTrack.add(FailedItem.class);
        return new DbLoader(getActivity().getBaseContext(), modelsToTrack, new LocalSearchFragmentFormQuery(orgUnitId, programId));
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void onLoadFinished(Loader<LocalSearchFragmentForm> loader, LocalSearchFragmentForm data) {
        if (loader.getId() == 99898989 && isAdded()) {
            this.trackedEntityAttributeListView.setVisibility(0);
            this.mForm = data;
            if (this.mForm.getDataEntryRows() != null) {
                this.mAdapter.swapData(this.mForm.getDataEntryRows());
            }
        }
    }

    public void onLoaderReset(Loader<LocalSearchFragmentForm> loader) {
    }

    public void onDetach() {
        super.onDetach();
        GpsController.disableGps();
    }
}
