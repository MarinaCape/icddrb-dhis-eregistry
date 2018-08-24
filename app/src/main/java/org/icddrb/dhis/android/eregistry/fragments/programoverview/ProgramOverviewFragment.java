package org.icddrb.dhis.android.eregistry.fragments.programoverview;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.raizlabs.android.dbflow.structure.Model;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import org.icddrb.dhis.android.eregistry.C0773R;
import org.icddrb.dhis.android.eregistry.activities.HolderActivity;
import org.icddrb.dhis.android.eregistry.fragments.programoverview.registerrelationshipdialogfragment.RegisterRelationshipDialogFragment;
import org.icddrb.dhis.android.eregistry.fragments.selectprogram.EnrollmentDateSetterHelper;
import org.icddrb.dhis.android.eregistry.fragments.selectprogram.IEnroller;
import org.icddrb.dhis.android.eregistry.fragments.selectprogram.dialogs.ItemStatusDialogFragment;
import org.icddrb.dhis.android.eregistry.ui.adapters.ProgramAdapter;
import org.icddrb.dhis.android.eregistry.ui.adapters.ProgramStageAdapter;
import org.icddrb.dhis.android.eregistry.ui.rows.programoverview.OnProgramStageEventClick;
import org.icddrb.dhis.android.eregistry.ui.rows.programoverview.ProgramStageEventRow;
import org.icddrb.dhis.android.eregistry.ui.rows.programoverview.ProgramStageLabelRow;
import org.icddrb.dhis.android.eregistry.ui.rows.programoverview.ProgramStageRow;
import org.icddrb.dhis.android.sdk.controllers.DhisController;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.controllers.tracker.TrackerController;
import org.icddrb.dhis.android.sdk.events.UiEvent;
import org.icddrb.dhis.android.sdk.events.UiEvent.UiEventType;
import org.icddrb.dhis.android.sdk.job.JobExecutor;
import org.icddrb.dhis.android.sdk.job.NetworkJob;
import org.icddrb.dhis.android.sdk.network.DhisApi;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.loaders.DbLoader;
import org.icddrb.dhis.android.sdk.persistence.models.BaseSerializableModel;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.FailedItem;
import org.icddrb.dhis.android.sdk.persistence.models.OrganisationUnit;
import org.icddrb.dhis.android.sdk.persistence.models.Program;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramRuleAction;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramStage;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramTrackedEntityAttribute;
import org.icddrb.dhis.android.sdk.persistence.models.Relationship;
import org.icddrb.dhis.android.sdk.persistence.models.RelationshipType;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttribute;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.persistence.models.UnionFWA;
import org.icddrb.dhis.android.sdk.persistence.preferences.AppPreferences;
import org.icddrb.dhis.android.sdk.persistence.preferences.ResourceType;
import org.icddrb.dhis.android.sdk.synchronization.data.enrollment.EnrollmentLocalDataSource;
import org.icddrb.dhis.android.sdk.synchronization.data.enrollment.EnrollmentRemoteDataSource;
import org.icddrb.dhis.android.sdk.synchronization.data.enrollment.EnrollmentRepository;
import org.icddrb.dhis.android.sdk.synchronization.data.event.EventLocalDataSource;
import org.icddrb.dhis.android.sdk.synchronization.data.event.EventRemoteDataSource;
import org.icddrb.dhis.android.sdk.synchronization.data.event.EventRepository;
import org.icddrb.dhis.android.sdk.synchronization.data.faileditem.FailedItemRepository;
import org.icddrb.dhis.android.sdk.synchronization.data.trackedentityinstance.TrackedEntityInstanceLocalDataSource;
import org.icddrb.dhis.android.sdk.synchronization.data.trackedentityinstance.TrackedEntityInstanceRemoteDataSource;
import org.icddrb.dhis.android.sdk.synchronization.data.trackedentityinstance.TrackedEntityInstanceRepository;
import org.icddrb.dhis.android.sdk.synchronization.domain.trackedentityinstance.SyncTrackedEntityInstanceUseCase;
import org.icddrb.dhis.android.sdk.ui.activities.OnBackPressedListener;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.IndicatorRow;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.PlainTextRow;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.events.OnDetailedInfoButtonClick;
import org.icddrb.dhis.android.sdk.ui.dialogs.AutoCompleteDialogFragment.OnOptionSelectedListener;
import org.icddrb.dhis.android.sdk.ui.dialogs.ProgramDialogFragment;
import org.icddrb.dhis.android.sdk.ui.fragments.common.AbsProgramRuleFragment;
import org.icddrb.dhis.android.sdk.ui.views.FloatingActionButton;
import org.icddrb.dhis.android.sdk.ui.views.FontTextView;
import org.icddrb.dhis.android.sdk.utils.UiUtils;
import org.icddrb.dhis.android.sdk.utils.api.ProgramType;
import org.icddrb.dhis.android.sdk.utils.comparators.EnrollmentDateComparator;
import org.icddrb.dhis.android.sdk.utils.services.ProgramRuleService;
import org.joda.time.DateTime;

public class ProgramOverviewFragment extends AbsProgramRuleFragment implements OnClickListener, OnItemClickListener, OnOptionSelectedListener, LoaderCallbacks<ProgramOverviewFragmentForm>, OnItemSelectedListener, OnRefreshListener, IEnroller, OnBackPressedListener {
    public static final String CLASS_TAG = ProgramOverviewFragment.class.getSimpleName();
    private static final String EXTRA_ARGUMENTS = "extra:Arguments";
    private static final String EXTRA_SAVED_INSTANCE_STATE = "extra:savedInstanceState";
    private static final int LOADER_ID = 578922123;
    private static final String ORG_UNIT_ID = "extra:orgUnitId";
    private static final String PROGRAM_ID = "extra:ProgramId";
    private static final String STATE = "state:UpcomingEventsFragment";
    private static final String TRACKEDENTITYINSTANCE_ID = "extra:TrackedEntityInstanceId";
    private ProgramStageAdapter adapter;
    private TextView attribute1Label;
    private TextView attribute1Value;
    private TextView attribute2Label;
    private TextView attribute2Value;
    private TextView attribute3Label;
    private TextView attribute3Value;
    private Button completeButton;
    private CardView enrollmentCardview;
    private TextView enrollmentDateLabel;
    private TextView enrollmentDateValue;
    private LinearLayout enrollmentLayout;
    private ImageView enrollmentServerStatus;
    private OnProgramStageEventClick eventLongPressed;
    private CardView eventsCardView;
    private ImageButton followupButton;
    private TextView incidentDateLabel;
    private TextView incidentDateValue;
    private ListView listView;
    private ProgramOverviewFragmentForm mForm;
    private ProgressBar mProgressBar;
    private Spinner mSpinner;
    private ProgramAdapter mSpinnerAdapter;
    private View mSpinnerContainer;
    private ProgramOverviewFragmentState mState;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayout missingEnrollmentLayout;
    private FloatingActionButton newEnrollmentButton;
    private Button newRelationshipButton;
    private Button newnewEnrollmentButton;
    private TextView noActiveEnrollment;
    private Button pregEnrollButton;
    private CardView pregcompleteCardview;
    private CardView pregenrollCardview;
    private ImageButton profileButton;
    private CardView profileCardView;
    private CardView programIndicatorCardView;
    private Button reOpenButton;
    private Button refreshRelationshipButton;
    private LinearLayout relationshipsLinearLayout;
    private Button terminateButton;

    /* renamed from: org.icddrb.dhis.android.eregistry.fragments.programoverview.ProgramOverviewFragment$1 */
    class C07771 implements Runnable {
        C07771() {
        }

        public void run() {
            if (ProgramOverviewFragment.this.mSpinner != null) {
                ProgramOverviewFragment.this.mSpinner.setOnItemSelectedListener(ProgramOverviewFragment.this);
            }
        }
    }

    /* renamed from: org.icddrb.dhis.android.eregistry.fragments.programoverview.ProgramOverviewFragment$7 */
    class C07837 implements DialogInterface.OnClickListener {
        C07837() {
        }

        public void onClick(DialogInterface dialog, int which) {
            ProgramOverviewFragment.this.completeEnrollment();
        }
    }

    /* renamed from: org.icddrb.dhis.android.eregistry.fragments.programoverview.ProgramOverviewFragment$8 */
    class C07848 implements DialogInterface.OnClickListener {
        C07848() {
        }

        public void onClick(DialogInterface dialog, int which) {
            ProgramOverviewFragment.this.terminateEnrollment();
        }
    }

    /* renamed from: org.icddrb.dhis.android.eregistry.fragments.programoverview.ProgramOverviewFragment$9 */
    class C07859 implements DialogInterface.OnClickListener {
        C07859() {
        }

        public void onClick(DialogInterface dialog, int which) {
            ProgramOverviewFragment.this.createEnrollment();
        }
    }

    public ProgramOverviewFragment() {
        setProgramRuleFragmentHelper(new ProgramOverviewRuleHelper(this));
    }

    public static ProgramOverviewFragment newInstance(String orgUnitId, String programId, long trackedEntityInstanceId) {
        ProgramOverviewFragment fragment = new ProgramOverviewFragment();
        Bundle args = new Bundle();
        args.putString("extra:orgUnitId", orgUnitId);
        args.putString("extra:ProgramId", programId);
        args.putLong("extra:TrackedEntityInstanceId", trackedEntityInstanceId);
        fragment.setArguments(args);
        return fragment;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, getArguments(), this);
        this.mProgressBar.setVisibility(0);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void onDetach() {
        super.onDetach();
    }

    public void onDestroyView() {
        if (getActivity() != null && (getActivity() instanceof AppCompatActivity)) {
            getActionBar().setDisplayShowTitleEnabled(true);
            getActionBar().setDisplayHomeAsUpEnabled(false);
            getActionBar().setHomeButtonEnabled(false);
        }
        detachSpinner();
        super.onDestroyView();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void onDestroy() {
        getProgramRuleFragmentHelper().recycle();
        super.onDestroy();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(C0773R.layout.fragment_programoverview, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (getActivity() instanceof AppCompatActivity) {
            getActionBar().setDisplayShowTitleEnabled(false);
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }
        this.listView = (ListView) view.findViewById(C0773R.id.listview);
        View header = getLayoutInflater(savedInstanceState).inflate(C0773R.layout.fragment_programoverview_header, this.listView, false);
        this.mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(C0773R.id.swipe_to_refresh_layout);
        this.mSwipeRefreshLayout.setColorSchemeResources(C0773R.color.Green, C0773R.color.Blue, C0773R.color.orange);
        this.mSwipeRefreshLayout.setOnRefreshListener(this);
        this.relationshipsLinearLayout = (LinearLayout) header.findViewById(C0773R.id.relationships_linearlayout);
        this.refreshRelationshipButton = (Button) header.findViewById(C0773R.id.pullrelationshipbutton);
        this.refreshRelationshipButton.setOnClickListener(this);
        this.newRelationshipButton = (Button) header.findViewById(C0773R.id.addrelationshipbutton);
        this.newRelationshipButton.setOnClickListener(this);
        this.mProgressBar = (ProgressBar) header.findViewById(C0773R.id.progress_bar);
        this.mProgressBar.setVisibility(8);
        this.adapter = new ProgramStageAdapter(getLayoutInflater(savedInstanceState));
        this.listView.addHeaderView(header, CLASS_TAG, false);
        this.listView.setAdapter(this.adapter);
        this.listView.setOnItemClickListener(this);
        registerForContextMenu(this.listView);
        this.enrollmentServerStatus = (ImageView) header.findViewById(C0773R.id.enrollmentstatus);
        this.enrollmentLayout = (LinearLayout) header.findViewById(C0773R.id.enrollmentLayout);
        this.enrollmentDateLabel = (TextView) header.findViewById(C0773R.id.dateOfEnrollmentLabel);
        this.enrollmentDateValue = (TextView) header.findViewById(C0773R.id.dateOfEnrollmentValue);
        this.incidentDateLabel = (TextView) header.findViewById(C0773R.id.dateOfIncidentLabel);
        this.incidentDateValue = (TextView) header.findViewById(C0773R.id.dateOfIncidentValue);
        this.profileCardView = (CardView) header.findViewById(C0773R.id.profile_cardview);
        this.enrollmentCardview = (CardView) header.findViewById(C0773R.id.enrollment_cardview);
        this.pregcompleteCardview = (CardView) header.findViewById(C0773R.id.pregcomplete_cardview);
        this.pregenrollCardview = (CardView) header.findViewById(C0773R.id.pregenroll_cardview);
        this.noActiveEnrollment = (TextView) header.findViewById(C0773R.id.noactiveenrollment);
        this.programIndicatorCardView = (CardView) header.findViewById(C0773R.id.programindicators_cardview);
        this.eventsCardView = (CardView) header.findViewById(C0773R.id.events_cardview);
        this.completeButton = (Button) header.findViewById(C0773R.id.complete);
        this.pregEnrollButton = (Button) header.findViewById(C0773R.id.btn_pregenroll);
        this.reOpenButton = (Button) header.findViewById(C0773R.id.re_open);
        this.terminateButton = (Button) header.findViewById(C0773R.id.terminate);
        this.followupButton = (ImageButton) header.findViewById(C0773R.id.followupButton);
        this.profileButton = (ImageButton) header.findViewById(C0773R.id.profile_button);
        this.completeButton.setOnClickListener(this);
        this.reOpenButton.setOnClickListener(this);
        this.terminateButton.setOnClickListener(this);
        this.followupButton.setOnClickListener(this);
        this.followupButton.setVisibility(8);
        this.profileButton.setOnClickListener(this);
        this.profileCardView.setOnClickListener(this);
        this.enrollmentServerStatus.setOnClickListener(this);
        this.enrollmentLayout.setOnClickListener(this);
        this.missingEnrollmentLayout = (LinearLayout) header.findViewById(C0773R.id.missingenrollmentlayout);
        this.newEnrollmentButton = (FloatingActionButton) header.findViewById(C0773R.id.newenrollmentbutton);
        this.newEnrollmentButton.setOnClickListener(this);
        this.newnewEnrollmentButton = (Button) header.findViewById(C0773R.id.newnewenrollmentbutton);
        this.newnewEnrollmentButton.setOnClickListener(this);
        this.attribute1Label = (TextView) header.findViewById(C0773R.id.headerItem1label);
        this.attribute1Value = (TextView) header.findViewById(C0773R.id.headerItem1value);
        this.attribute2Label = (TextView) header.findViewById(C0773R.id.headerItem2label);
        this.attribute2Value = (TextView) header.findViewById(C0773R.id.headerItem2value);
        this.attribute3Label = (TextView) header.findViewById(C0773R.id.headerItem3label);
        this.attribute3Value = (TextView) header.findViewById(C0773R.id.headerItem3value);
        Bundle fragmentArguments = getArguments();
        if (!(savedInstanceState == null || savedInstanceState.getParcelable(STATE) == null)) {
            this.mState = (ProgramOverviewFragmentState) savedInstanceState.getParcelable(STATE);
        }
        if (this.mState == null) {
            this.mState = new ProgramOverviewFragmentState();
            OrganisationUnit ou = MetaDataController.getOrganisationUnit(fragmentArguments.getString("extra:orgUnitId"));
            Program program = MetaDataController.getProgram(fragmentArguments.getString("extra:ProgramId"));
            this.mState.setOrgUnit(ou.getId(), ou.getLabel());
            this.mState.setProgram(program.getUid(), program.getName());
            this.mState.setTrackedEntityInstance(fragmentArguments.getLong("extra:TrackedEntityInstanceId", -1));
        }
        attachSpinner();
        this.mSpinnerAdapter.swapData(MetaDataController.getProgramsForOrganisationUnit(fragmentArguments.getString("extra:orgUnitId"), ProgramType.WITH_REGISTRATION));
        norwayChanges(header);
        onRestoreState(true);
    }

    public void norwayChanges(View header) {
        ((CardView) header.findViewById(C0773R.id.relationships_cardview)).setVisibility(8);
        this.terminateButton.setVisibility(8);
        setPregCloseButtonState();
        setEnrollButtonState();
    }

    private void setPregCloseButtonState() {
        boolean active = this.mForm != null && "ACTIVE".equals(this.mForm.getEnrollment().getStatus());
        if (this.mState.isMCH() && active) {
            this.pregcompleteCardview.setVisibility(8);
            this.completeButton.setVisibility(8);
            return;
        }
        this.pregcompleteCardview.setVisibility(8);
        this.completeButton.setVisibility(8);
    }

    private void setEnrollButtonState() {
        boolean active;
        int i;
        if (this.mForm == null || !"ACTIVE".equals(this.mForm.getEnrollment().getStatus())) {
            active = false;
        } else {
            active = true;
        }
        boolean isMCH = this.mState.isMCH();
        if (active) {
            i = 0;
        } else {
            i = 1;
        }
        if ((i & isMCH) != 0) {
            this.pregenrollCardview.setVisibility(0);
            this.newEnrollmentButton.setVisibility(0);
            this.pregEnrollButton.setVisibility(0);
            this.pregEnrollButton.setClickable(true);
            return;
        }
        this.pregenrollCardview.setVisibility(8);
        this.newEnrollmentButton.setVisibility(8);
        this.pregEnrollButton.setVisibility(8);
        this.pregEnrollButton.setClickable(false);
    }

    public void onPause() {
        super.onPause();
        Dhis2Application.getEventBus().unregister(this);
    }

    public void onResume() {
        super.onResume();
        reloadProgramRules();
        Dhis2Application.getEventBus().register(this);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        getActivity().finish();
        return true;
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void onRestoreState(boolean hasPrograms) {
        ProgramOverviewFragmentState backedUpState = new ProgramOverviewFragmentState(this.mState);
        if (!backedUpState.isProgramEmpty()) {
            onProgramSelected(backedUpState.getProgramId(), backedUpState.getProgramName());
        }
    }

    private ActionBar getActionBar() {
        if (getActivity() != null && (getActivity() instanceof AppCompatActivity)) {
            return ((AppCompatActivity) getActivity()).getSupportActionBar();
        }
        throw new IllegalArgumentException("Fragment should be attached to ActionBarActivity");
    }

    private Toolbar getActionBarToolbar() {
        if (isAdded() && getActivity() != null) {
            return (Toolbar) getActivity().findViewById(C0773R.id.toolbar);
        }
        throw new IllegalArgumentException("Fragment should be attached to MainActivity");
    }

    private int getSpinnerIndex(String programName) {
        int index = -1;
        for (int i = 0; i < this.mSpinnerAdapter.getCount(); i++) {
            if (((Program) this.mSpinnerAdapter.getItem(i)).getName().equals(programName)) {
                index = i;
            }
        }
        return index;
    }

    private void attachSpinner() {
        if (!isSpinnerAttached()) {
            Toolbar toolbar = getActionBarToolbar();
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            this.mSpinnerContainer = inflater.inflate(C0773R.layout.toolbar_spinner_simple2, toolbar, false);
            toolbar.addView(this.mSpinnerContainer, new LayoutParams(-1, -1));
            this.mSpinnerAdapter = new ProgramAdapter(inflater);
            this.mSpinner = (Spinner) this.mSpinnerContainer.findViewById(C0773R.id.toolbar_spinner);
            this.mSpinner.setAdapter(this.mSpinnerAdapter);
            this.mSpinner.post(new C07771());
        }
    }

    private void detachSpinner() {
        if (isSpinnerAttached() && this.mSpinnerContainer != null) {
            ((ViewGroup) this.mSpinnerContainer.getParent()).removeView(this.mSpinnerContainer);
            this.mSpinnerContainer = null;
            this.mSpinner = null;
            if (this.mSpinnerAdapter != null) {
                this.mSpinnerAdapter.swapData(null);
                this.mSpinnerAdapter = null;
            }
        }
    }

    private boolean isSpinnerAttached() {
        return this.mSpinnerContainer != null;
    }

    public void onProgramSelected(String programId, String programName) {
        this.mState.setProgram(programId, programName);
        Bundle args = getArguments();
        args.putString("extra:ProgramId", programId);
        clearViews();
        getLoaderManager().restartLoader(LOADER_ID, args, this);
    }

    public Loader<ProgramOverviewFragmentForm> onCreateLoader(int id, Bundle args) {
        if (LOADER_ID != id || !isAdded()) {
            return null;
        }
        List<Class<? extends Model>> modelsToTrack = new ArrayList();
        modelsToTrack.add(Event.class);
        modelsToTrack.add(Enrollment.class);
        modelsToTrack.add(TrackedEntityInstance.class);
        modelsToTrack.add(TrackedEntityAttributeValue.class);
        modelsToTrack.add(Relationship.class);
        modelsToTrack.add(FailedItem.class);
        return new DbLoader(getActivity().getBaseContext(), modelsToTrack, new ProgramOverviewFragmentQuery(args.getString("extra:ProgramId"), args.getLong("extra:TrackedEntityInstanceId", -1)));
    }

    public void onLoadFinished(Loader<ProgramOverviewFragmentForm> loader, ProgramOverviewFragmentForm data) {
        if (LOADER_ID == loader.getId()) {
            clearViews();
            this.mForm = data;
            this.mProgressBar.setVisibility(8);
            setRefreshing(false);
            this.mSpinner.setSelection(getSpinnerIndex(this.mState.getProgramName()));
            if (this.mForm != null) {
                setRelationships(getLayoutInflater(getArguments().getBundle("extra:savedInstanceState")));
            }
            LinearLayout programEventsLayout = (LinearLayout) this.eventsCardView.findViewById(C0773R.id.programeventlayout);
            LinearLayout programIndicatorLayout = (LinearLayout) this.programIndicatorCardView.findViewById(C0773R.id.programindicatorlayout);
            initializeEventsViews(programEventsLayout);
            initializeIndicatorViews(programIndicatorLayout);
            if (this.mForm == null || this.mForm.getEnrollment() == null) {
                showNoActiveEnrollment(this.mForm);
                return;
            }
            this.enrollmentLayout.setVisibility(0);
            this.missingEnrollmentLayout.setVisibility(8);
            this.profileCardView.setClickable(true);
            this.profileButton.setClickable(true);
            this.enrollmentDateLabel.setText(data.getDateOfEnrollmentLabel());
            this.enrollmentDateValue.setText(data.getDateOfEnrollmentValue());
            if (data.getProgram().getDisplayIncidentDate()) {
                this.incidentDateLabel.setText(data.getIncidentDateLabel());
                this.incidentDateValue.setText(data.getIncidentDateValue());
            } else {
                this.incidentDateValue.setVisibility(8);
                this.incidentDateLabel.setVisibility(8);
            }
            FailedItem failedItem = TrackerController.getFailedItem("Enrollment", this.mForm.getEnrollment().getLocalId());
            if (failedItem != null && failedItem.getHttpStatusCode() >= 0) {
                this.enrollmentServerStatus.setImageResource(C0773R.drawable.ic_event_error);
            } else if (this.mForm.getEnrollment().isFromServer()) {
                this.enrollmentServerStatus.setImageResource(C0773R.drawable.ic_from_server);
            } else {
                this.enrollmentServerStatus.setImageResource(C0773R.drawable.ic_legacy_offline);
            }
            this.refreshRelationshipButton.setEnabled(this.mForm.getEnrollment().isFromServer());
            if (this.mForm.getEnrollment().getStatus().equals(Enrollment.CANCELLED)) {
                setTerminated();
            }
            if (this.mForm.getEnrollment().getFollowup()) {
                setFollowupButton(true);
            }
            if (data.getAttribute1Label() == null || data.getAttribute1Value() == null) {
                this.attribute1Label.setVisibility(8);
                this.attribute1Value.setVisibility(8);
            } else {
                this.attribute1Label.setText(data.getAttribute1Label());
                this.attribute1Value.setText(data.getAttribute1Value());
            }
            if (data.getAttribute2Label() == null || data.getAttribute2Value() == null) {
                this.attribute2Label.setVisibility(8);
                this.attribute2Value.setVisibility(8);
            } else {
                this.attribute2Label.setText(data.getAttribute2Label());
                this.attribute2Value.setText(data.getAttribute2Value());
            }
            if (data.getAttribute3Label() == null || data.getAttribute3Value() == null) {
                this.attribute3Label.setVisibility(8);
                this.attribute3Value.setVisibility(8);
            } else {
                this.attribute3Label.setText(data.getAttribute3Label());
                this.attribute3Value.setText(data.getAttribute3Value());
            }
            Map<Long, FailedItem> failedEvents = getFailedEvents();
            for (IndicatorRow indicatorRow : this.mForm.getProgramIndicatorRows().values()) {
                programIndicatorLayout.addView(indicatorRow.getView(getChildFragmentManager(), getLayoutInflater(getArguments()), null, programIndicatorLayout));
            }
            reloadProgramRules();
            List<ProgramStageRow> validRows = new ArrayList();
            for (ProgramStageRow programStageRow : this.mForm.getProgramStageRows()) {
                if (programStageRow instanceof ProgramStageLabelRow) {
                    if (!this.programRuleFragmentHelper.getHideProgramStages().contains(((ProgramStageLabelRow) programStageRow).getProgramStage().getUid())) {
                        validRows.add(programStageRow);
                    }
                } else if (programStageRow instanceof ProgramStageEventRow) {
                    if (!this.programRuleFragmentHelper.getHideProgramStages().contains(((ProgramStageEventRow) programStageRow).getEvent().getProgramStageId())) {
                        validRows.add(programStageRow);
                    }
                }
            }
            this.mForm.setProgramStageRows(validRows);
            for (ProgramStageRow row : this.mForm.getProgramStageRows()) {
                if (row instanceof ProgramStageLabelRow) {
                    ProgramStageLabelRow stageRow = (ProgramStageLabelRow) row;
                    if (stageRow.getProgramStage().getRepeatable()) {
                        stageRow.setButtonListener(this);
                    } else if (stageRow.getEventRows().size() < 1) {
                        stageRow.setButtonListener(this);
                    }
                } else if (row instanceof ProgramStageEventRow) {
                    ProgramStageEventRow eventRow = (ProgramStageEventRow) row;
                    FailedItem failedItem1 = TrackerController.getFailedItem("Event", eventRow.getEvent().getLocalId());
                    if (failedItem1 != null && failedItem1.getHttpStatusCode() >= 0) {
                        eventRow.setHasFailed(true);
                        eventRow.setMessage(((FailedItem) failedEvents.get(Long.valueOf(eventRow.getEvent().getLocalId()))).getErrorMessage());
                    } else if (eventRow.getEvent().isFromServer()) {
                        eventRow.setSynchronized(true);
                        eventRow.setMessage(getString(C0773R.string.status_sent_description));
                    } else {
                        eventRow.setSynchronized(false);
                        eventRow.setMessage(getString(C0773R.string.status_offline_description));
                    }
                }
            }
            for (IndicatorRow indicatorRow2 : this.mForm.getProgramIndicatorRows().values()) {
                programIndicatorLayout.addView(indicatorRow2.getView(getChildFragmentManager(), getLayoutInflater(getArguments()), null, programIndicatorLayout));
            }
            for (ProgramStageRow programStageRow2 : this.mForm.getProgramStageRows()) {
                programEventsLayout.addView(programStageRow2.getView(getLayoutInflater(getArguments()), null, programEventsLayout));
            }
            setPregCloseButtonState();
            setEnrollButtonState();
        }
    }

    private void initializeEventsViews(LinearLayout programEventsLayout) {
        programEventsLayout.removeAllViews();
        ((FlowLayout) this.eventsCardView.findViewById(C0773R.id.keyvalueeventlayout)).removeAllViews();
        ((LinearLayout) this.eventsCardView.findViewById(C0773R.id.texteventlayout)).removeAllViews();
        programEventsLayout.removeAllViews();
    }

    private void initializeIndicatorViews(LinearLayout programIndicatorLayout) {
        programIndicatorLayout.removeAllViews();
        ((FlowLayout) this.programIndicatorCardView.findViewById(C0773R.id.keyvaluelayout)).removeAllViews();
        ((LinearLayout) this.programIndicatorCardView.findViewById(C0773R.id.textlayout)).removeAllViews();
        programIndicatorLayout.removeAllViews();
    }

    public void setRelationships(LayoutInflater inflater) {
        this.relationshipsLinearLayout.removeAllViews();
        if (this.mForm.getTrackedEntityInstance() != null && this.mForm.getTrackedEntityInstance().getRelationships() != null) {
            ListIterator<Relationship> it = this.mForm.getTrackedEntityInstance().getRelationships().listIterator();
            while (it.hasNext()) {
                final Relationship relationship = (Relationship) it.next();
                if (relationship != null) {
                    LinearLayout ll = (LinearLayout) inflater.inflate(C0773R.layout.listview_row_relationship, null);
                    FontTextView currentTeiRelationshipLabel = (FontTextView) ll.findViewById(C0773R.id.current_tei_relationship_label);
                    FontTextView relativeLabel = (FontTextView) ll.findViewById(C0773R.id.relative_relationship_label);
                    ((Button) ll.findViewById(C0773R.id.delete_relationship)).setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            ProgramOverviewFragment.showConfirmDeleteRelationshipDialog(relationship, ProgramOverviewFragment.this.mForm.getTrackedEntityInstance(), ProgramOverviewFragment.this.getActivity());
                        }
                    });
                    RelationshipType relationshipType = MetaDataController.getRelationshipType(relationship.getRelationship());
                    if (relationshipType != null) {
                        TrackedEntityInstance relative;
                        if (this.mForm.getTrackedEntityInstance().getTrackedEntityInstance() != null && this.mForm.getTrackedEntityInstance().getTrackedEntityInstance().equals(relationship.getTrackedEntityInstanceA())) {
                            currentTeiRelationshipLabel.setText(relationshipType.getaIsToB());
                            relative = TrackerController.getTrackedEntityInstance(relationship.getTrackedEntityInstanceB());
                        } else if (this.mForm.getTrackedEntityInstance().getTrackedEntityInstance() != null && this.mForm.getTrackedEntityInstance().getTrackedEntityInstance().equals(relationship.getTrackedEntityInstanceB())) {
                            currentTeiRelationshipLabel.setText(relationshipType.getbIsToA());
                            relative = TrackerController.getTrackedEntityInstance(relationship.getTrackedEntityInstanceA());
                        }
                        relativeLabel.setText(getRelativeString(relative));
                        relativeLabel.setOnClickListener(new OnClickListener() {
                            public void onClick(View v) {
                                ProgramOverviewFragment.this.moveToRelative(relationship);
                            }
                        });
                        ll.setOnClickListener(new OnClickListener() {
                            public void onClick(View v) {
                                if (relative != null) {
                                    ProgramOverviewFragment.this.moveToRelative(relationship);
                                }
                            }
                        });
                        this.relationshipsLinearLayout.addView(ll);
                        if (it.hasNext()) {
                            View view = new View(getActivity());
                            view.setLayoutParams(new LinearLayout.LayoutParams(-1, 1));
                            view.setBackgroundColor(getResources().getColor(C0773R.color.light_grey));
                            this.relationshipsLinearLayout.addView(view);
                        }
                    }
                }
            }
        }
    }

    private void moveToRelative(Relationship relationship) {
        if (relationship.getTrackedEntityInstanceA().equals(this.mForm.getTrackedEntityInstance().getUid())) {
            moveToRelative(relationship.getTrackedEntityInstanceB(), getActivity());
        } else {
            moveToRelative(relationship.getTrackedEntityInstanceA(), getActivity());
        }
    }

    private void moveToRelative(String trackedEntityInstanceUid, FragmentActivity activity) {
        HolderActivity.navigateToProgramOverviewFragment(activity, this.mState.getOrgUnitId(), this.mState.getProgramId(), TrackerController.getTrackedEntityInstance(trackedEntityInstanceUid).getLocalId());
    }

    private String getRelativeString(TrackedEntityInstance relative) {
        String relativeString = "";
        if (!(relative == null || relative.getAttributes() == null)) {
            List<Enrollment> enrollments = TrackerController.getEnrollments(relative);
            List<TrackedEntityAttribute> attributesToShow = new ArrayList();
            List<TrackedEntityAttributeValue> attributes = TrackerController.getVisibleTrackedEntityAttributeValues(relative.getLocalId());
            int i = 0;
            while (i < attributes.size() && i < 2) {
                relativeString = relativeString + ((TrackedEntityAttributeValue) attributes.get(i)).getValue() + " ";
                i++;
            }
            if (attributes.size() == 0) {
                if (enrollments == null || enrollments.isEmpty()) {
                    i = 0;
                    while (i < relative.getAttributes().size() && i < 2) {
                        if (!(relative.getAttributes().get(i) == null || ((TrackedEntityAttributeValue) relative.getAttributes().get(i)).getValue() == null)) {
                            relativeString = relativeString + ((TrackedEntityAttributeValue) relative.getAttributes().get(i)).getValue() + " ";
                        }
                        i++;
                    }
                } else {
                    Program program = null;
                    for (Enrollment e : enrollments) {
                        if (e != null && e.getProgram() != null && e.getProgram().getProgramTrackedEntityAttributes() != null) {
                            program = e.getProgram();
                            break;
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
                        TrackedEntityAttributeValue av = TrackerController.getTrackedEntityAttributeValue(((TrackedEntityAttribute) attributesToShow.get(i)).getUid(), relative.getLocalId());
                        if (!(av == null || av.getValue() == null)) {
                            relativeString = relativeString + av.getValue() + " ";
                        }
                        i++;
                    }
                }
            }
        }
        if (relativeString.isEmpty()) {
            return getString(C0773R.string.unknown);
        }
        return relativeString;
    }

    public static void showConfirmDeleteRelationshipDialog(final Relationship relationship, final TrackedEntityInstance trackedEntityInstance, Activity activity) {
        if (activity != null) {
            UiUtils.showConfirmDialog(activity, activity.getString(C0773R.string.confirm), activity.getString(C0773R.string.confirm_delete_relationship), activity.getString(C0773R.string.delete), activity.getString(C0773R.string.cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    relationship.delete();
                    trackedEntityInstance.setFromServer(false);
                    trackedEntityInstance.save();
                    dialog.dismiss();
                }
            });
        }
    }

    @Subscribe
    public void onItemClick(OnProgramStageEventClick eventClick) {
        if (eventClick.isHasPressedFailedButton()) {
            if (eventClick.getEvent() != null) {
                showStatusDialog(eventClick.getEvent());
            }
        } else if (eventClick.isLongPressed()) {
            this.eventLongPressed = eventClick;
            getActivity().openContextMenu(eventClick.getView());
        } else {
            showDataEntryFragment(eventClick.getEvent(), eventClick.getEvent().getProgramStageId());
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        new MenuInflater(getActivity()).inflate(C0773R.menu.long_click_event_menu, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        Event eventClicked = this.eventLongPressed.getEvent();
        switch (item.getItemId()) {
            case C0773R.id.delete_event:
                if (eventClicked == null) {
                    return true;
                }
                deleteEvent(eventClicked);
                return true;
            case C0773R.id.edit_event:
                if (eventClicked == null) {
                    return true;
                }
                showDataEntryFragment(eventClicked, eventClicked.getProgramStageId());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void deleteEvent(final Event eventItemRow) {
        UiUtils.showConfirmDialog(getActivity(), getActivity().getString(C0773R.string.confirm), getActivity().getString(C0773R.string.warning_delete_event), getActivity().getString(C0773R.string.delete), getActivity().getString(C0773R.string.cancel), (int) C0773R.drawable.ic_event_error, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                eventItemRow.setStatus(Event.STATUS_DELETED);
                eventItemRow.setFromServer(false);
                Enrollment enrollment = TrackerController.getEnrollment(eventItemRow.getEnrollment());
                enrollment.setFromServer(false);
                enrollment.save();
                TrackedEntityInstance trackedEntityInstance = TrackerController.getTrackedEntityInstance(enrollment.getTrackedEntityInstance());
                trackedEntityInstance.setFromServer(false);
                trackedEntityInstance.save();
                eventItemRow.save();
                dialog.dismiss();
            }
        });
    }

    public Map<Long, FailedItem> getFailedEvents() {
        Map<Long, FailedItem> failedItemMap = new HashMap();
        List<FailedItem> failedItems = TrackerController.getFailedItems();
        if (failedItems != null && failedItems.size() > 0) {
            for (FailedItem failedItem : failedItems) {
                if (failedItem.getItemType().equals("Event")) {
                    failedItemMap.put(Long.valueOf(failedItem.getItemId()), failedItem);
                }
            }
        }
        return failedItemMap;
    }

    public void showNoActiveEnrollment(ProgramOverviewFragmentForm mForm) {
        this.enrollmentLayout.setVisibility(8);
        this.reOpenButton.setVisibility(0);
        if (this.mState.isMCH()) {
            this.reOpenButton.setText("Re-open previous pregnancy record");
            this.noActiveEnrollment.setText("No active pregnancy record");
            this.newnewEnrollmentButton.setVisibility(0);
            this.newEnrollmentButton.setVisibility(8);
        } else {
            this.noActiveEnrollment.setText(C0773R.string.no_active_enrollment);
            this.newEnrollmentButton.setVisibility(0);
            this.newnewEnrollmentButton.setVisibility(8);
        }
        this.missingEnrollmentLayout.setVisibility(0);
        Enrollment lastEnrollment = TrackerController.getLastEnrollment(mForm.getProgram().getUid(), mForm.getTrackedEntityInstance());
        if (!(lastEnrollment == null || mForm.getProgram() == null || !mForm.getProgram().getOnlyEnrollOnce())) {
            if (lastEnrollment.getStatus().equals(Enrollment.CANCELLED)) {
                this.newEnrollmentButton.setVisibility(0);
                this.noActiveEnrollment.setText(C0773R.string.enrollment_cancelled);
            } else {
                this.newEnrollmentButton.setVisibility(8);
                this.noActiveEnrollment.setText(C0773R.string.enrollment_complete);
            }
        }
        if (getLastEnrollmentForTrackedEntityInstance() == null) {
            this.reOpenButton.setVisibility(8);
        }
        List<TrackedEntityAttributeValue> trackedEntityAttributeValues = TrackerController.getVisibleTrackedEntityAttributeValues(TrackerController.getTrackedEntityInstance(mForm.getTrackedEntityInstance().getTrackedEntityInstance()).getLocalId());
        UnionFWA userList = new AppPreferences(getContext()).getDropdownInfo();
        if (trackedEntityAttributeValues != null && trackedEntityAttributeValues.size() > 0) {
            for (TrackedEntityAttributeValue a : trackedEntityAttributeValues) {
                TrackedEntityAttribute e = MetaDataController.getTrackedEntityAttribute(a.getTrackedEntityAttributeId());
                if (e != null) {
                    String name = e.getName();
                    Object obj = -1;
                    switch (name.hashCode()) {
                        case -1768348257:
                            if (name.equals("Village name")) {
                                obj = 2;
                                break;
                            }
                            break;
                        case -761051333:
                            if (name.equals("FWA Name")) {
                                obj = 1;
                                break;
                            }
                            break;
                        case 268609884:
                            if (name.equals("Full name")) {
                                obj = null;
                                break;
                            }
                            break;
                    }
                    switch (obj) {
                        case null:
                            this.attribute1Label.setText(e.getDisplayName());
                            this.attribute1Value.setText(a.getValue());
                            break;
                        case 1:
                            String fwaName = userList.getFullName(a.getValue());
                            this.attribute2Label.setText(e.getDisplayName());
                            TextView textView = this.attribute2Value;
                            if (fwaName == null) {
                                fwaName = a.getValue();
                            }
                            textView.setText(fwaName);
                            break;
                        case 2:
                            this.attribute3Label.setText(e.getDisplayName());
                            this.attribute3Value.setText(a.getValue());
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        for (Enrollment enrollment : TrackerController.getEnrollments(TrackerController.getTrackedEntityInstance(this.mState.getTrackedEntityInstanceId()))) {
            if (((Program) this.mSpinner.getSelectedItem()).getUid().equals(enrollment.getProgram().getUid())) {
                this.profileCardView.setClickable(false);
                this.profileButton.setClickable(false);
                return;
            }
            this.profileCardView.setClickable(false);
            this.profileButton.setClickable(false);
        }
    }

    public void onLoaderReset(Loader<ProgramOverviewFragmentForm> loader) {
        clearViews();
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        ProgramStageRow row = (ProgramStageRow) this.listView.getItemAtPosition(position);
        if (row instanceof ProgramStageEventRow) {
            Event event = ((ProgramStageEventRow) row).getEvent();
            showDataEntryFragment(event, event.getProgramStageId());
        }
    }

    private void createEnrollment() {
        EnrollmentDateSetterHelper.createEnrollment(this.mForm.getTrackedEntityInstance(), this, getActivity(), this.mForm.getProgram().getDisplayIncidentDate(), this.mForm.getProgram().getSelectEnrollmentDatesInFuture(), this.mForm.getProgram().getSelectIncidentDatesInFuture(), this.mForm.getProgram().getEnrollmentDateLabel(), this.mForm.getProgram().getIncidentDateLabel());
        markParentsAsNonFromServer();
    }

    public void showEnrollmentFragment(TrackedEntityInstance trackedEntityInstance, DateTime enrollmentDate, DateTime incidentDate) {
        String enrollmentDateString = enrollmentDate.toString();
        String incidentDateString = null;
        if (incidentDate != null) {
            incidentDateString = incidentDate.toString();
        }
        getActivity().finish();
        if (trackedEntityInstance == null) {
            HolderActivity.navigateToEnrollmentDataEntryFragment(getActivity(), this.mState.getOrgUnitId(), this.mState.getProgramId(), enrollmentDateString, incidentDateString);
        } else {
            HolderActivity.navigateToEnrollmentDataEntryFragment(getActivity(), this.mState.getOrgUnitId(), this.mState.getProgramId(), trackedEntityInstance.getLocalId(), enrollmentDateString, incidentDateString);
        }
    }

    public void showDataEntryFragment(Event event, String programStage) {
        Bundle args = getArguments();
        if (event == null) {
            HolderActivity.navigateToDataEntryFragment(getActivity(), args.getString("extra:orgUnitId"), args.getString("extra:ProgramId"), programStage, this.mForm.getEnrollment().getLocalId());
            return;
        }
        HolderActivity.navigateToDataEntryFragment(getActivity(), args.getString("extra:orgUnitId"), args.getString("extra:ProgramId"), programStage, this.mForm.getEnrollment().getLocalId(), event.getLocalId());
    }

    public void completeEnrollment() {
        if (this.mForm == null || this.mForm.getEnrollment() == null) {
            Log.i("ENROLLMENT", "Unable to complete enrollment. mForm or mForm.getEnrollment() is null");
            return;
        }
        this.mForm.getEnrollment().setStatus("COMPLETED");
        markParentsAsNonFromServer();
        clearViews();
    }

    private void markParentsAsNonFromServer() {
        if (this.mForm.getEnrollment() != null) {
            this.mForm.getEnrollment().setFromServer(false);
            this.mForm.getEnrollment().async().save();
        }
        if (this.mForm.getTrackedEntityInstance() != null) {
            this.mForm.getTrackedEntityInstance().setFromServer(false);
            this.mForm.getTrackedEntityInstance().async().save();
        }
    }

    public void terminateEnrollment() {
        if (this.mForm == null || this.mForm.getEnrollment() == null) {
            Log.i("ENROLLMENT", "Unable to terminate enrollment. mForm or mForm.getEnrollment() is null");
            return;
        }
        this.mForm.getEnrollment().setStatus(Enrollment.CANCELLED);
        markParentsAsNonFromServer();
        setTerminated();
        clearViews();
    }

    public void setTerminated() {
        onProgramSelected(this.mForm.getProgram().getUid(), this.mForm.getProgram().getName());
    }

    public void toggleFollowup() {
        if (this.mForm != null && this.mForm.getEnrollment() != null) {
            this.mForm.getEnrollment().setFollowup(!this.mForm.getEnrollment().getFollowup());
            markParentsAsNonFromServer();
            setFollowupButton(this.mForm.getEnrollment().getFollowup());
        }
    }

    public void setFollowupButton(boolean enabled) {
        if (this.followupButton != null) {
            if (enabled) {
                this.followupButton.setBackgroundResource(C0773R.drawable.rounded_imagebutton_red);
            } else {
                this.followupButton.setBackgroundResource(C0773R.drawable.rounded_imagebutton_gray);
            }
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0773R.id.addrelationshipbutton:
                showAddRelationshipFragment();
                return;
            case C0773R.id.btn_pregenroll:
                UiUtils.showConfirmDialog(getActivity(), "Enroll into Pregnancy Program?", "You are about to create a new pregnancy record. Are you sure you want to complete this pregnancy record? You cannot go back or make changes.", getString(C0773R.string.yes), getString(C0773R.string.no), new C07859());
                return;
            case C0773R.id.complete:
                UiUtils.showConfirmDialog(getActivity(), getString(C0773R.string.un_enroll), getString(C0773R.string.confirm_complete_enrollment), getString(C0773R.string.un_enroll), getString(C0773R.string.cancel), new C07837());
                return;
            case C0773R.id.enrollmentLayout:
                editEnrollmentDates();
                return;
            case C0773R.id.enrollmentstatus:
                if (this.mForm != null && this.mForm.getEnrollment() != null) {
                    showStatusDialog(this.mForm.getEnrollment());
                    return;
                }
                return;
            case C0773R.id.eventbackground:
                if (this.mForm.getEnrollment().getStatus().equals("ACTIVE")) {
                    Event event = (Event) view.getTag();
                    showDataEntryFragment(event, event.getProgramStageId());
                    return;
                }
                return;
            case C0773R.id.followupButton:
                toggleFollowup();
                return;
            case C0773R.id.newenrollmentbutton:
            case C0773R.id.newnewenrollmentbutton:
                createEnrollment();
                return;
            case C0773R.id.neweventbutton:
                if (this.mForm.getEnrollment().getStatus().equals("ACTIVE")) {
                    showDataEntryFragment(null, ((ProgramStage) view.getTag()).getUid());
                    return;
                }
                return;
            case C0773R.id.profile_button:
                editTrackedEntityInstanceProfile(this.mState.getOrgUnitId());
                return;
            case C0773R.id.profile_cardview:
                editTrackedEntityInstanceProfile(this.mState.getOrgUnitId());
                return;
            case C0773R.id.pullrelationshipbutton:
                refreshRelationships();
                return;
            case C0773R.id.re_open:
                Enrollment enrollment = getLastEnrollmentForTrackedEntityInstance();
                if (enrollment != null) {
                    enrollment.setStatus("ACTIVE");
                    enrollment.setFromServer(false);
                    enrollment.async().save();
                    markParentsAsNonFromServer();
                    refreshUi();
                    return;
                }
                return;
            case C0773R.id.select_program:
                ProgramDialogFragment.newInstance(this, this.mState.getOrgUnitId(), ProgramType.WITH_REGISTRATION).show(getChildFragmentManager());
                return;
            case C0773R.id.terminate:
                UiUtils.showConfirmDialog(getActivity(), getString(C0773R.string.terminate), getString(C0773R.string.confirm_terminate_enrollment), getString(C0773R.string.yes), getString(C0773R.string.no), new C07848());
                return;
            default:
                return;
        }
    }

    private void refreshRelationships() {
        Toast.makeText(getActivity().getBaseContext(), getString(C0773R.string.refresh_relations), 0).show();
        if (this.mForm != null && this.mForm.getTrackedEntityInstance() != null) {
            refreshTrackedEntityRelationships(this.mForm.getTrackedEntityInstance().getUid());
        }
    }

    private void refreshUi() {
        getLoaderManager().restartLoader(LOADER_ID, getArguments(), this);
    }

    private Enrollment getLastEnrollmentForTrackedEntityInstance() {
        List<Enrollment> enrollments = TrackerController.getEnrollments(this.mForm.getTrackedEntityInstance(), this.mForm.getProgram().getUid(), this.mForm.getTrackedEntityInstance().getOrgUnit());
        if (enrollments == null || enrollments.size() == 0) {
            return null;
        }
        EnrollmentDateComparator comparator = new EnrollmentDateComparator();
        Collections.reverseOrder(comparator);
        Collections.sort(enrollments, comparator);
        return (Enrollment) enrollments.get(0);
    }

    private void clearViews() {
        this.adapter.swapData(null);
    }

    public void showStatusDialog(BaseSerializableModel model) {
        ItemStatusDialogFragment.newInstance(model).show(getChildFragmentManager());
    }

    private void editEnrollmentDates() {
        if (this.mForm != null && this.mForm.getEnrollment() != null) {
            HolderActivity.navigateToEnrollmentDateFragment(getActivity(), this.mForm.getEnrollment().getLocalId());
        }
    }

    private void editTrackedEntityInstanceProfile(String org) {
        HolderActivity.navigateToTrackedEntityInstanceProfileFragment(getActivity(), getArguments().getLong("extra:TrackedEntityInstanceId"), getArguments().getString("extra:ProgramId"), org);
    }

    private void showAddRelationshipFragment() {
        if (this.mForm != null && this.mForm.getTrackedEntityInstance() != null) {
            RegisterRelationshipDialogFragment.newInstance(this.mForm.getTrackedEntityInstance().getLocalId(), this.mForm.getProgram().getUid()).show(getChildFragmentManager(), CLASS_TAG);
        }
    }

    void displayKeyValuePair(ProgramRuleAction programRuleAction) {
        FlowLayout programIndicatorLayout = (FlowLayout) this.programIndicatorCardView.findViewById(C0773R.id.keyvaluelayout);
        KeyValueView keyValueView = new KeyValueView(programRuleAction.getContent(), ProgramRuleService.getCalculatedConditionValue(programRuleAction.getData()));
        FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(10, 10);
        View view = keyValueView.getView(getLayoutInflater(getArguments()), programIndicatorLayout);
        view.setLayoutParams(layoutParams);
        view.setTag(programRuleAction.getUid());
        addProgramRuleActionToView(programRuleAction, programIndicatorLayout, view);
    }

    void displayText(ProgramRuleAction programRuleAction) {
        LinearLayout programIndicatorLayout = (LinearLayout) this.programIndicatorCardView.findViewById(C0773R.id.textlayout);
        View view = new PlainTextRow(ProgramRuleService.getCalculatedConditionValue(programRuleAction.getData())).getView(getChildFragmentManager(), getLayoutInflater(getArguments()), null, programIndicatorLayout);
        view.findViewById(C0773R.id.text_label).setVisibility(8);
        view.findViewById(C0773R.id.detailed_info_button_layout).setVisibility(8);
        addProgramRuleActionToView(programRuleAction, programIndicatorLayout, view);
    }

    private void addProgramRuleActionToView(ProgramRuleAction programRuleAction, ViewGroup programIndicatorLayout, View view) {
        view.setTag(programRuleAction.getUid());
        boolean isAdded = false;
        for (int i = 0; i < programIndicatorLayout.getChildCount(); i++) {
            if (programIndicatorLayout.getChildAt(i).getTag().equals(view.getTag())) {
                isAdded = true;
            }
        }
        if (!isAdded) {
            programIndicatorLayout.addView(view);
        }
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        Program program = (Program) this.mSpinnerAdapter.getItem(position);
        onProgramSelected(program.getUid(), program.getName());
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void onOptionSelected(int dialogId, int position, String id, String name) {
        switch (dialogId) {
            case ProgramDialogFragment.ID /*921345*/:
                onProgramSelected(id, name);
                return;
            default:
                return;
        }
    }

    public void onRefresh() {
        if (isAdded()) {
            Toast.makeText(getActivity().getBaseContext(), getString(C0773R.string.syncing), 0).show();
            synchronize();
        }
    }

    protected void setRefreshing(final boolean refreshing) {
        if (this.mSwipeRefreshLayout.isRefreshing() != refreshing) {
            this.mSwipeRefreshLayout.post(new Runnable() {
                public void run() {
                    ProgramOverviewFragment.this.mSwipeRefreshLayout.setRefreshing(refreshing);
                }
            });
        }
    }

    @Subscribe
    public void onReceivedUiEvent(UiEvent uiEvent) {
        if (uiEvent.getEventType().equals(UiEventType.SYNCING_START)) {
            setRefreshing(true);
        } else if (uiEvent.getEventType().equals(UiEventType.SYNCING_END)) {
            setRefreshing(false);
        }
    }

    public void synchronize() {
        if (this.mForm != null) {
            sendTrackedEntityInstance(this.mForm.getTrackedEntityInstance());
        }
    }

    public void refreshTrackedEntityRelationships(final String trackedEntityInstance) {
        Dhis2Application.getEventBus().post(new UiEvent(UiEventType.SYNCING_START));
        JobExecutor.enqueueJob(new NetworkJob<Object>(0, ResourceType.TRACKEDENTITYINSTANCE) {
            public Object execute() {
                TrackerController.refreshRelationsByTrackedEntity(DhisController.getInstance().getDhisApi(), trackedEntityInstance);
                return new Object();
            }
        });
    }

    public void sendTrackedEntityInstance(final TrackedEntityInstance trackedEntityInstance) {
        JobExecutor.enqueueJob(new NetworkJob<Object>(0, ResourceType.TRACKEDENTITYINSTANCE) {
            public Object execute() {
                DhisApi dhisApi = DhisController.getInstance().getDhisApi();
                new SyncTrackedEntityInstanceUseCase(new TrackedEntityInstanceRepository(new TrackedEntityInstanceLocalDataSource(), new TrackedEntityInstanceRemoteDataSource(dhisApi)), new EnrollmentRepository(new EnrollmentLocalDataSource(), new EnrollmentRemoteDataSource(dhisApi)), new EventRepository(new EventLocalDataSource(), new EventRemoteDataSource(DhisController.getInstance().getDhisApi())), new FailedItemRepository()).execute(trackedEntityInstance);
                return new Object();
            }
        });
    }

    public ProgramOverviewFragmentForm getForm() {
        return this.mForm;
    }

    public void setForm(ProgramOverviewFragmentForm mForm) {
        this.mForm = mForm;
    }

    public void reloadProgramRules() {
        if (this.mForm != null) {
            this.programRuleFragmentHelper.getHideProgramStages().clear();
            evaluateAndApplyProgramRules();
        }
    }

    public boolean doBack() {
        getActivity().finish();
        return false;
    }

    @Subscribe
    public void onShowDetailedInfo(OnDetailedInfoButtonClick eventClick) {
        UiUtils.showConfirmDialog(getActivity(), getResources().getString(C0773R.string.detailed_info_dataelement), eventClick.getRow().getDescription(), getResources().getString(C0773R.string.ok_option), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
    }

    public void hideProgramStage(ProgramRuleAction programRuleAction) {
    }
}
