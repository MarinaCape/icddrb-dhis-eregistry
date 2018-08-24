package org.icddrb.dhis.android.sdk.ui.fragments.dataentry;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView.RecyclerListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.icddrb.dhis.android.sdk.C0845R;
import org.icddrb.dhis.android.sdk.controllers.ErrorType;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.models.BaseValue;
import org.icddrb.dhis.android.sdk.ui.activities.OnBackPressedListener;
import org.icddrb.dhis.android.sdk.ui.adapters.DataValueAdapter;
import org.icddrb.dhis.android.sdk.ui.adapters.SectionAdapter;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.EventCoordinatesRow;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.IndicatorRow;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.QuestionCoordinatesRow;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.StatusRow;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.events.OnDetailedInfoButtonClick;
import org.icddrb.dhis.android.sdk.ui.fragments.common.AbsProgramRuleFragment;
import org.icddrb.dhis.android.sdk.ui.fragments.eventdataentry.RulesEvaluatorThread;
import org.icddrb.dhis.android.sdk.ui.fragments.eventdataentry.UpdateSectionsEvent;
import org.icddrb.dhis.android.sdk.utils.UiUtils;

public abstract class DataEntryFragment<D> extends AbsProgramRuleFragment<D> implements LoaderCallbacks<D>, OnItemSelectedListener, OnBackPressedListener {
    protected static final String EXTRA_ARGUMENTS = "extra:Arguments";
    protected static final String EXTRA_SAVED_INSTANCE_STATE = "extra:savedInstanceState";
    protected static final int INITIAL_POSITION = 0;
    protected static final int LOADER_ID = 17;
    public static final String TAG = DataEntryFragment.class.getSimpleName();
    private boolean hasDataChanged = false;
    protected ListView listView;
    protected DataValueAdapter listViewAdapter;
    private Parcelable listViewAdapterState;
    private Parcelable listViewState;
    protected ProgressBar progressBar;
    protected boolean refreshing = false;
    protected RulesEvaluatorThread rulesEvaluatorThread;
    protected ValidationErrorDialog validationErrorDialog;

    /* renamed from: org.icddrb.dhis.android.sdk.ui.fragments.dataentry.DataEntryFragment$1 */
    class C09031 implements RecyclerListener {
        C09031() {
        }

        public void onMovedToScrapHeap(View view) {
            if (view.hasFocus()) {
                view.clearFocus();
                ViewParent parent = view.getParent();
                if (parent != null) {
                    parent.clearChildFocus(view);
                }
            }
        }
    }

    /* renamed from: org.icddrb.dhis.android.sdk.ui.fragments.dataentry.DataEntryFragment$2 */
    class C09042 implements OnClickListener {
        C09042() {
        }

        public void onClick(View v) {
            DataEntryFragment.this.listView.smoothScrollToPosition(0);
        }
    }

    /* renamed from: org.icddrb.dhis.android.sdk.ui.fragments.dataentry.DataEntryFragment$3 */
    class C09053 implements DialogInterface.OnClickListener {
        C09053() {
        }

        public void onClick(DialogInterface dialog, int i) {
            dialog.dismiss();
        }
    }

    public abstract SectionAdapter getSpinnerAdapter();

    protected abstract HashMap<ErrorType, ArrayList<String>> getValidationErrors();

    protected abstract boolean isValid();

    public abstract void onLoadFinished(Loader<D> loader, D d);

    protected abstract void proceed();

    protected abstract void save();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (this.rulesEvaluatorThread == null || this.rulesEvaluatorThread.isKilled()) {
            this.rulesEvaluatorThread = new RulesEvaluatorThread();
            this.rulesEvaluatorThread.start();
        }
        this.rulesEvaluatorThread.init(this);
    }

    public void onDestroy() {
        super.onDestroy();
        this.rulesEvaluatorThread.kill();
        this.rulesEvaluatorThread = null;
    }

    public void onResume() {
        super.onResume();
        Dhis2Application.getEventBus().register(this);
    }

    public void onPause() {
        this.listViewState = this.listView.onSaveInstanceState();
        super.onPause();
        Dhis2Application.getEventBus().unregister(this);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(C0845R.menu.menu_data_entry, menu);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(C0845R.layout.fragment_data_entry, container, false);
    }

    public ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.progressBar = (ProgressBar) view.findViewById(C0845R.id.progress_bar);
        this.progressBar.setVisibility(8);
        this.listView = (ListView) view.findViewById(C0845R.id.datavalues_listview);
        this.listView.setRecyclerListener(new C09031());
        View upButton = getLayoutInflater(savedInstanceState).inflate(C0845R.layout.up_button_layout, this.listView, false);
        this.listViewAdapter = new DataValueAdapter(getChildFragmentManager(), getLayoutInflater(savedInstanceState), this.listView, getContext());
        this.listView.addFooterView(upButton);
        this.listView.setVisibility(0);
        this.listView.setAdapter(this.listViewAdapter);
        if (this.listViewState != null) {
            this.listView.onRestoreInstanceState(this.listViewState);
        }
        upButton.setOnClickListener(new C09042());
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            getActivity().finish();
            return true;
        } else if (menuItem.getItemId() != C0845R.id.action_new_event) {
            return super.onOptionsItemSelected(menuItem);
        } else {
            proceed();
            return true;
        }
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle argumentsBundle = new Bundle();
        argumentsBundle.putBundle("extra:Arguments", getArguments());
        argumentsBundle.putBundle("extra:savedInstanceState", savedInstanceState);
        getLoaderManager().initLoader(17, argumentsBundle, this);
        this.progressBar.setVisibility(0);
        this.listView.setVisibility(8);
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public static void resetHidingAndWarnings(DataValueAdapter dataValueAdapter, SectionAdapter sectionAdapter) {
        if (dataValueAdapter != null) {
            dataValueAdapter.resetHiding();
            dataValueAdapter.resetDisabled();
            dataValueAdapter.resetWarnings();
            dataValueAdapter.resetMandatory();
            dataValueAdapter.resetErrors();
        }
        if (sectionAdapter != null) {
            sectionAdapter.resetHiding();
        }
    }

    public static boolean containsValue(BaseValue value) {
        if (value == null || value.getValue() == null || value.getValue().isEmpty()) {
            return false;
        }
        return true;
    }

    public void updateSections() {
        Dhis2Application.getEventBus().post(new UpdateSectionsEvent());
    }

    public DataValueAdapter getListViewAdapter() {
        return this.listViewAdapter;
    }

    protected void showLoadingDialog() {
        UiUtils.showLoadingDialog(getChildFragmentManager(), C0845R.string.please_wait);
    }

    public void hideLoadingDialog() {
        UiUtils.hideLoadingDialog(getChildFragmentManager());
    }

    public static void refreshListView() {
        Dhis2Application.getEventBus().post(new RefreshListViewEvent());
    }

    public void flagDataChanged(boolean changed) {
        if (this.hasDataChanged != changed) {
            this.hasDataChanged = changed;
            if (isAdded()) {
                getActivity().invalidateOptionsMenu();
            }
        }
    }

    private void showErrorsDialog(ArrayList<String> errors) {
        if (errors.isEmpty()) {
            Toast.makeText(getContext(), C0845R.string.unable_to_complete_registration, 1).show();
            return;
        }
        this.validationErrorDialog = ValidationErrorDialog.newInstance(getActivity().getString(C0845R.string.unable_to_complete_registration) + " " + getActivity().getString(C0845R.string.review_errors), errors);
        this.validationErrorDialog.show(getChildFragmentManager());
    }

    protected void showValidationErrorDialog(HashMap<ErrorType, ArrayList<String>> errorsMap) {
        ArrayList<String> errors = new ArrayList();
        addErrors((ArrayList) errorsMap.get(ErrorType.MANDATORY), errors, getActivity().getString(C0845R.string.missing_mandatory_field));
        addErrors((ArrayList) errorsMap.get(ErrorType.UNIQUE), errors, getActivity().getString(C0845R.string.unique_value_form_empty));
        addErrors((ArrayList) errorsMap.get(ErrorType.PROGRAM_RULE), errors, getActivity().getString(C0845R.string.error_message));
        addErrors((ArrayList) errorsMap.get(ErrorType.INVALID_FIELD), errors, getActivity().getString(C0845R.string.error_message));
        showErrorsDialog(errors);
    }

    private void addErrors(ArrayList<String> programRulesErrors, ArrayList<String> errors, String errorMessage) {
        if (programRulesErrors != null) {
            Iterator it = programRulesErrors.iterator();
            while (it.hasNext()) {
                errors.add(errorMessage + ": " + ((String) it.next()));
            }
        }
    }

    protected boolean haveValuesChanged() {
        return this.hasDataChanged;
    }

    protected Toolbar getActionBarToolbar() {
        if (isAdded() && getActivity() != null) {
            return (Toolbar) getActivity().findViewById(C0845R.id.toolbar);
        }
        throw new IllegalArgumentException("Fragment should be attached to MainActivity");
    }

    public ValidationErrorDialog getValidationErrorDialog() {
        return this.validationErrorDialog;
    }

    public void setValidationErrorDialog(ValidationErrorDialog validationErrorDialog) {
        this.validationErrorDialog = validationErrorDialog;
    }

    @Subscribe
    public void onRowValueChanged(RowValueChangedEvent event) {
        flagDataChanged(true);
    }

    @Subscribe
    public void onRefreshListView(RefreshListViewEvent event) {
        int start = this.listView.getFirstVisiblePosition();
        int end = this.listView.getLastVisiblePosition();
        for (int pos = 0; pos <= end - start; pos++) {
            View view = this.listView.getChildAt(pos);
            if (view != null) {
                int adapterPosition = view.getId();
                if (adapterPosition >= 0 && adapterPosition < this.listViewAdapter.getCount()) {
                    this.listViewAdapter.getView(adapterPosition, view, this.listView);
                }
            }
        }
        this.refreshing = false;
    }

    @Subscribe
    public void onShowDetailedInfo(OnDetailedInfoButtonClick eventClick) {
        String message = "";
        if ((eventClick.getRow() instanceof EventCoordinatesRow) || (eventClick.getRow() instanceof QuestionCoordinatesRow)) {
            message = getResources().getString(C0845R.string.detailed_info_coordinate_row);
        } else if (eventClick.getRow() instanceof StatusRow) {
            message = getResources().getString(C0845R.string.detailed_info_status_row);
        } else if (eventClick.getRow() instanceof IndicatorRow) {
            message = "";
        } else {
            message = eventClick.getRow().getDescription();
        }
        UiUtils.showConfirmDialog(getActivity(), getResources().getString(C0845R.string.detailed_info_dataelement), message, getResources().getString(C0845R.string.ok_option), new C09053());
    }

    @Subscribe
    public void onHideLoadingDialog(HideLoadingDialogEvent event) {
        hideLoadingDialog();
    }

    public boolean doBack() {
        if (getActivity() != null) {
            getActivity().finish();
        }
        return false;
    }
}
