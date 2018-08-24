package org.icddrb.dhis.android.eregistry.fragments.upcomingevents;

import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TableLayout.LayoutParams;
import com.raizlabs.android.dbflow.structure.Model;
import java.util.ArrayList;
import java.util.List;
import org.icddrb.dhis.android.eregistry.C0773R;
import org.icddrb.dhis.android.eregistry.activities.HolderActivity;
import org.icddrb.dhis.android.eregistry.ui.adapters.UpcomingEventAdapter;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.controllers.tracker.TrackerController;
import org.icddrb.dhis.android.sdk.persistence.loaders.DbLoader;
import org.icddrb.dhis.android.sdk.persistence.models.DataValue;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.OrganisationUnit;
import org.icddrb.dhis.android.sdk.ui.adapters.AbsAdapter;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.DatePickerRow;
import org.icddrb.dhis.android.sdk.ui.dialogs.OrgUnitDialogFragment;
import org.icddrb.dhis.android.sdk.ui.dialogs.ProgramDialogFragment;
import org.icddrb.dhis.android.sdk.ui.dialogs.UpcomingEventsDialogFilter;
import org.icddrb.dhis.android.sdk.ui.fragments.selectprogram.SelectProgramFragment;
import org.icddrb.dhis.android.sdk.ui.fragments.selectprogram.SelectProgramFragmentForm;
import org.icddrb.dhis.android.sdk.ui.views.CardTextViewButton;
import org.icddrb.dhis.android.sdk.ui.views.FloatingActionButton;
import org.icddrb.dhis.android.sdk.utils.api.ProgramType;
import org.icddrb.dhis.android.sdk.utils.support.DateUtils;
import org.joda.time.LocalDate;

public class UpcomingEventsFragment extends SelectProgramFragment implements OnItemClickListener, LoaderCallbacks<SelectProgramFragmentForm> {
    private static final String CLASS_TAG = "UpcomingEventsFragment";
    private List<OrganisationUnit> assignedOrganisationUnits;
    private DataValue endDate;
    protected CardTextViewButton filterButton;
    private FloatingActionButton mQueryButton;
    private DataValue startDate;

    /* renamed from: org.icddrb.dhis.android.eregistry.fragments.upcomingevents.UpcomingEventsFragment$1 */
    class C08351 implements OnClickListener {
        C08351() {
        }

        public void onClick(View view) {
            UpcomingEventsDialogFilter.newInstance(UpcomingEventsFragment.this).show(UpcomingEventsFragment.this.getChildFragmentManager());
        }
    }

    public UpcomingEventsFragment() {
        super("state:UpcomingEventsFragment", 2);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        getActivity().finish();
        return true;
    }

    public boolean onContextItemSelected(MenuItem item) {
        return false;
    }

    protected AbsAdapter getAdapter(Bundle savedInstanceState) {
        return new UpcomingEventAdapter(getLayoutInflater(savedInstanceState));
    }

    protected View getListViewHeader(Bundle savedInstanceState) {
        if (getActivity() instanceof AppCompatActivity) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setTitle(getString(C0773R.string.upcoming_events));
        }
        View header = getLayoutInflater(savedInstanceState).inflate(C0773R.layout.fragment_upcomingevents_header, this.mListView, false);
        this.mListView.setOnItemClickListener(this);
        this.mQueryButton = (FloatingActionButton) header.findViewById(C0773R.id.upcoming_query_button);
        this.assignedOrganisationUnits = MetaDataController.getAssignedOrganisationUnits();
        if (this.assignedOrganisationUnits != null && this.assignedOrganisationUnits.size() > 0) {
            this.mQueryButton.setOnClickListener(this);
            this.mQueryButton.hide();
            this.filterButton = (CardTextViewButton) header.findViewById(C0773R.id.select_filter);
            this.filterButton.setText((CharSequence) this.mPrefs.getFilter().second);
            this.filterButton.setOnClickListener(new C08351());
            this.startDate = new DataValue();
            this.startDate.setValue(DateUtils.getMediumDateString());
            this.endDate = new DataValue();
            this.endDate.setValue(new LocalDate(DateUtils.getMediumDateString()).plusWeeks(1).toString());
            DatePickerRow startDatePicker = new DatePickerRow(getString(C0773R.string.startdate), false, null, this.startDate, true);
            startDatePicker.setHideDetailedInfoButton(true);
            DatePickerRow endDatePicker = new DatePickerRow(getString(C0773R.string.enddate), false, null, this.endDate, true);
            endDatePicker.setHideDetailedInfoButton(true);
            LinearLayout dateFilterContainer = (LinearLayout) header.findViewById(C0773R.id.datefilterlayout);
            View view1 = startDatePicker.getView(getFragmentManager(), getActivity().getLayoutInflater(), null, dateFilterContainer);
            view1.setLayoutParams(new LayoutParams(0, -2, 1.0f));
            View view2 = endDatePicker.getView(getFragmentManager(), getActivity().getLayoutInflater(), null, dateFilterContainer);
            view2.setLayoutParams(new LayoutParams(0, -2, 1.0f));
            View detailedInfoButton1 = view1.findViewById(C0773R.id.detailed_info_button_layout);
            View detailedInfoButton2 = view2.findViewById(C0773R.id.detailed_info_button_layout);
            detailedInfoButton1.setVisibility(8);
            detailedInfoButton2.setVisibility(8);
            dateFilterContainer.addView(view1);
            dateFilterContainer.addView(view2);
        }
        return header;
    }

    protected ProgramType[] getProgramTypes() {
        return new ProgramType[]{ProgramType.WITH_REGISTRATION};
    }

    public Loader<SelectProgramFragmentForm> onCreateLoader(int id, Bundle args) {
        if (this.LOADER_ID == id && isAdded()) {
            List<Class<? extends Model>> modelsToTrack = new ArrayList();
            modelsToTrack.add(Event.class);
            if (!(this.startDate == null || this.endDate == null)) {
                return new DbLoader(getActivity().getBaseContext(), modelsToTrack, new UpcomingEventsFragmentQuery(this.mState.getOrgUnitId(), this.mState.getProgramId(), this.mState.getFilterLabel(), this.startDate.getValue(), this.endDate.getValue()));
            }
        }
        return null;
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        HolderActivity.navigateToProgramOverviewFragment(getActivity(), this.mState.getOrgUnitId(), this.mState.getProgramId(), TrackerController.getEnrollment(TrackerController.getEvent(id).getLocalEnrollmentId()).getLocalTrackedEntityInstanceId());
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0773R.id.upcoming_query_button:
                if (this.startDate.getValue() != null && !this.startDate.getValue().isEmpty() && this.endDate.getValue() != null && !this.endDate.getValue().isEmpty()) {
                    this.mProgressBar.setVisibility(0);
                    getLoaderManager().restartLoader(this.LOADER_ID, getArguments(), this);
                    return;
                }
                return;
            default:
                return;
        }
    }

    protected void handleViews(int level) {
        this.mAdapter.swapData(null);
        switch (level) {
            case 0:
                this.mQueryButton.hide();
                return;
            case 1:
                if (this.mPrefs.getFilter() == null) {
                    this.mQueryButton.hide();
                    return;
                } else {
                    this.mQueryButton.show();
                    return;
                }
            case 2:
                this.mQueryButton.show();
                return;
            default:
                return;
        }
    }

    private ActionBar getActionBar() {
        if (getActivity() != null && (getActivity() instanceof AppCompatActivity)) {
            return ((AppCompatActivity) getActivity()).getSupportActionBar();
        }
        throw new IllegalArgumentException("Fragment should be attached to ActionBarActivity");
    }

    public void stateChanged() {
    }

    public void onOptionSelected(int dialogId, int position, String id, String name) {
        switch (dialogId) {
            case OrgUnitDialogFragment.ID /*450123*/:
                onUnitSelected(id, name);
                return;
            case ProgramDialogFragment.ID /*921345*/:
                onProgramSelected(id, name);
                return;
            case UpcomingEventsDialogFilter.ID /*120000101*/:
                onFilterSelected(id, name);
                return;
            default:
                return;
        }
    }

    private void onFilterSelected(String id, String name) {
        this.filterButton.setText(name);
        this.mState.setFilter(id, name);
        this.mPrefs.putFilter(new Pair(id, name));
        handleViews(2);
    }
}
