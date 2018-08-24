package org.icddrb.dhis.android.eregistry.fragments.enrollmentdate;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.icddrb.dhis.android.eregistry.C0773R;
import org.icddrb.dhis.android.sdk.controllers.ErrorType;
import org.icddrb.dhis.android.sdk.controllers.tracker.TrackerController;
import org.icddrb.dhis.android.sdk.persistence.loaders.DbLoader;
import org.icddrb.dhis.android.sdk.ui.adapters.SectionAdapter;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.Row;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.DataEntryFragment;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.RowValueChangedEvent;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.SaveThread;

@Deprecated
public class EnrollmentDateFragment extends DataEntryFragment<EnrollmentDateFragmentForm> {
    public static final String ENROLLMENT_ID = "extra:EnrollmentId";
    private static final String EXTRA_ARGUMENTS = "extra:Arguments";
    private static final String EXTRA_SAVED_INSTANCE_STATE = "extra:savedInstanceState";
    public static final String TAG = EnrollmentDateFragment.class.getSimpleName();
    private boolean edit;
    private boolean editableRows;
    private EnrollmentDateFragmentForm mForm;
    private SaveThread saveThread;

    public static EnrollmentDateFragment newInstance(long enrollmentId) {
        EnrollmentDateFragment fragment = new EnrollmentDateFragment();
        Bundle args = new Bundle();
        args.putLong("extra:EnrollmentId", enrollmentId);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.saveThread == null || this.saveThread.isKilled()) {
            this.saveThread = new SaveThread();
            this.saveThread.start();
        }
        this.saveThread.init(this);
        setHasOptionsMenu(true);
    }

    public void onDestroy() {
        this.saveThread.kill();
        super.onDestroy();
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setTitle((int) C0773R.string.enrollment);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    }

    public void onPrepareOptionsMenu(Menu menu) {
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        boolean z = false;
        if (menuItem.getItemId() == 16908332) {
            getActivity().finish();
        } else if (menuItem.getItemId() == C0773R.id.action_new_event) {
            if (this.editableRows) {
                setEditableDataEntryRows(false);
            } else {
                setEditableDataEntryRows(true);
            }
            if (!this.editableRows) {
                z = true;
            }
            this.editableRows = z;
        }
        return true;
    }

    public void setEditableDataEntryRows(boolean editable) {
        List<Row> rows = new ArrayList(this.mForm.getDataEntryRows());
        this.listViewAdapter.swapData(null);
        if (editable) {
            for (Row row : rows) {
                row.setEditable(true);
            }
        } else {
            for (Row row2 : rows) {
                row2.setEditable(false);
            }
        }
        this.listView.setAdapter(null);
        this.listViewAdapter.swapData(rows);
        this.listView.setAdapter(this.listViewAdapter);
    }

    @Subscribe
    public void onRowValueChanged(RowValueChangedEvent event) {
        flagDataChanged(true);
        if (this.mForm != null) {
            this.edit = true;
            save();
        }
    }

    public SectionAdapter getSpinnerAdapter() {
        return null;
    }

    protected HashMap<ErrorType, ArrayList<String>> getValidationErrors() {
        return null;
    }

    protected boolean isValid() {
        return true;
    }

    protected void save() {
        if (this.edit) {
            flagDataChanged(false);
            if (this.mForm != null && isAdded()) {
                this.mForm.getEnrollment().setFromServer(false);
                TrackerController.getTrackedEntityInstance(this.mForm.getEnrollment().getTrackedEntityInstance()).setFromServer(false);
                this.mForm.getEnrollment().save();
            }
            this.edit = false;
        }
    }

    protected void proceed() {
    }

    protected boolean goBack() {
        if (isValid()) {
            goBackToPreviousActivity();
        }
        return false;
    }

    private void goBackToPreviousActivity() {
        getActivity().finish();
    }

    public void onLoadFinished(Loader<EnrollmentDateFragmentForm> loader, EnrollmentDateFragmentForm data) {
        this.mForm = data;
        this.progressBar.setVisibility(8);
        this.listView.setVisibility(0);
        this.listViewAdapter.swapData(data.getDataEntryRows());
        setEditableDataEntryRows(false);
    }

    public Loader onCreateLoader(int id, Bundle args) {
        if (17 != id || !isAdded()) {
            return null;
        }
        return new DbLoader(getActivity().getBaseContext(), new ArrayList(), new EnrollmentDateFragmentQuery(args.getBundle("extra:Arguments").getLong("extra:EnrollmentId", 0)));
    }

    public void onLoaderReset(Loader loader) {
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    }
}
