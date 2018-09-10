package org.icddrb.dhis.android.sdk.ui.dialogs;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.List;
import org.icddrb.dhis.android.sdk.R;
import org.icddrb.dhis.android.sdk.controllers.DhisController;
import org.icddrb.dhis.android.sdk.controllers.tracker.TrackerController;
import org.icddrb.dhis.android.sdk.events.LoadingMessageEvent;
import org.icddrb.dhis.android.sdk.events.UiEvent;
import org.icddrb.dhis.android.sdk.events.UiEvent.UiEventType;
import org.icddrb.dhis.android.sdk.job.JobExecutor;
import org.icddrb.dhis.android.sdk.job.NetworkJob;
import org.icddrb.dhis.android.sdk.network.APIException;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.persistence.preferences.ResourceType;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.AbsTextWatcher;
import org.icddrb.dhis.android.sdk.ui.fragments.progressdialog.ProgressDialogFragment;

public class QueryTrackedEntityInstancesResultDialogFragment extends DialogFragment implements OnItemClickListener, OnClickListener {
    private static final String EXTRA_ORGUNIT = "extra:orgUnit";
    private static final String EXTRA_SELECTALL = "extra:selectAll";
    private static final String EXTRA_TRACKEDENTITYINSTANCESLIST = "extra:trackedEntityInstances";
    private static final String EXTRA_TRACKEDENTITYINSTANCESSELECTED = "extra:trackedEntityInstancesSelected";
    private static final String TAG = QueryTrackedEntityInstancesResultDialogFragment.class.getSimpleName();
    private QueryTrackedEntityInstancesResultDialogAdapter mAdapter;
    private int mDialogId;
    private TextView mDialogLabel;
    private EditText mFilter;
    private ListView mListView;
    private Button mSelectAllButton;
    private ProgressDialogFragment progressDialogFragment;

    /* renamed from: org.icddrb.dhis.android.sdk.ui.dialogs.QueryTrackedEntityInstancesResultDialogFragment$1 */
    class C08961 extends AbsTextWatcher {
        C08961() {
        }

        public void afterTextChanged(Editable s) {
            QueryTrackedEntityInstancesResultDialogFragment.this.mAdapter.getFilter().filter(s.toString());
        }
    }

    /* renamed from: org.icddrb.dhis.android.sdk.ui.dialogs.QueryTrackedEntityInstancesResultDialogFragment$2 */
    class C08972 implements OnClickListener {
        C08972() {
        }

        public void onClick(View v) {
            QueryTrackedEntityInstancesResultDialogFragment.this.dismiss();
        }
    }

    static class ParameterParcelable implements Parcelable {
        public static final Creator<ParameterParcelable> CREATOR = new C08991();
        public static final String TAG = ParameterParcelable.class.getSimpleName();
        private List<TrackedEntityInstance> trackedEntityInstances;

        /* renamed from: org.icddrb.dhis.android.sdk.ui.dialogs.QueryTrackedEntityInstancesResultDialogFragment$ParameterParcelable$1 */
        static class C08991 implements Creator<ParameterParcelable> {
            C08991() {
            }

            public ParameterParcelable createFromParcel(Parcel in) {
                return new ParameterParcelable(in);
            }

            public ParameterParcelable[] newArray(int size) {
                return new ParameterParcelable[size];
            }
        }

        public ParameterParcelable(List<TrackedEntityInstance> trackedEntityInstances) {
            Log.d(TAG, "parcelputting " + trackedEntityInstances.size());
            this.trackedEntityInstances = trackedEntityInstances;
        }

        protected ParameterParcelable(Parcel in) {
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeList(this.trackedEntityInstances);
        }

        public List<TrackedEntityInstance> getTrackedEntityInstances() {
            return this.trackedEntityInstances;
        }
    }

    public static QueryTrackedEntityInstancesResultDialogFragment newInstance(List<TrackedEntityInstance> trackedEntityInstances, String orgUnit) {
        QueryTrackedEntityInstancesResultDialogFragment dialogFragment = new QueryTrackedEntityInstancesResultDialogFragment();
        Bundle args = new Bundle();
        Parcel parcel1 = Parcel.obtain();
        ParameterParcelable parcelable1 = new ParameterParcelable((List) trackedEntityInstances);
        parcelable1.writeToParcel(parcel1, 1);
        Parcel parcel2 = Parcel.obtain();
        ParameterParcelable parcelable2 = new ParameterParcelable(new ArrayList());
        parcelable2.writeToParcel(parcel2, 1);
        args.putParcelable("extra:trackedEntityInstancesSelected", parcelable2);
        args.putParcelable("extra:trackedEntityInstances", parcelable1);
        args.putString("extra:orgUnit", orgUnit);
        args.putBoolean("extra:selectAll", false);
        dialogFragment.setArguments(args);
        Dhis2Application.getEventBus().register(dialogFragment);
        return dialogFragment;
    }

    private List<TrackedEntityInstance> getTrackedEntityInstances() {
        return ((ParameterParcelable) getArguments().getParcelable("extra:trackedEntityInstances")).getTrackedEntityInstances();
    }

    private List<TrackedEntityInstance> getSelectedTrackedEntityInstances() {
        return ((ParameterParcelable) getArguments().getParcelable("extra:trackedEntityInstancesSelected")).getTrackedEntityInstances();
    }

    private String getOrgUnit() {
        return getArguments().getString("extra:orgUnit");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(1, R.style.Theme_AppCompat_Light_Dialog);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(2);
        return inflater.inflate(R.layout.dialog_fragment_teiqueryresult, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.mListView = (ListView) view.findViewById(R.id.simple_listview);
        ImageView closeDialogButton = (ImageView) view.findViewById(R.id.close_dialog_button);
        this.mFilter = (EditText) view.findViewById(R.id.filter_options);
        this.mDialogLabel = (TextView) view.findViewById(R.id.dialog_label);
        ((InputMethodManager) getActivity().getSystemService("input_method")).hideSoftInputFromWindow(this.mFilter.getWindowToken(), 0);
        this.mAdapter = new QueryTrackedEntityInstancesResultDialogAdapter(LayoutInflater.from(getActivity()), getSelectedTrackedEntityInstances(), null, getContext());
        this.mListView.setAdapter(this.mAdapter);
        this.mListView.setOnItemClickListener(this);
        this.mFilter.addTextChangedListener(new C08961());
        this.mSelectAllButton = (Button) view.findViewById(R.id.teiqueryresult_selectall);
        this.mSelectAllButton.setOnClickListener(this);
        this.mSelectAllButton.setVisibility(0);
        if (getArguments().getBoolean("extra:selectAll")) {
            this.mSelectAllButton.setText(getString(R.string.deselect_all));
        }
        closeDialogButton.setOnClickListener(new C08972());
        setDialogLabel(R.string.select_to_load);
        getAdapter().swapData(getTrackedEntityInstances());
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        TrackedEntityInstance value = this.mAdapter.getItem(position);
        List<TrackedEntityInstance> selected = getSelectedTrackedEntityInstances();
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBoxTeiQuery);
        if (checkBox.isChecked()) {
            selected.remove(value);
            checkBox.setChecked(false);
            return;
        }
        selected.add(value);
        checkBox.setChecked(true);
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

    public QueryTrackedEntityInstancesResultDialogAdapter getAdapter() {
        return this.mAdapter;
    }

    public void show(FragmentManager fragmentManager) {
        if (fragmentManager != null) {
            show(fragmentManager, TAG);
        }
    }

    public void onClick(View v) {
        if (v.getId() == R.id.teiqueryresult_selectall) {
            toggleSelectAll();
        }
    }

    public void toggleSelectAll() {
        Bundle arguments = getArguments();
        boolean selectall = arguments.getBoolean("extra:selectAll");
        if (selectall) {
            this.mSelectAllButton.setText(getText(R.string.select_all));
            deselectAll();
        } else {
            this.mSelectAllButton.setText(getText(R.string.deselect_all));
            selectAll();
        }
        arguments.putBoolean("extra:selectAll", !selectall);
    }

    public void selectAll() {
        List<TrackedEntityInstance> allTrackedEntityInstances = this.mAdapter.getData();
        List<TrackedEntityInstance> selectedTrackedEntityInstances = getSelectedTrackedEntityInstances();
        selectedTrackedEntityInstances.clear();
        selectedTrackedEntityInstances.addAll(allTrackedEntityInstances);
        View view = null;
        for (int i = 0; i < allTrackedEntityInstances.size(); i++) {
            view = this.mAdapter.getView(i, view, null);
            ((CheckBox) view.findViewById(R.id.checkBoxTeiQuery)).setChecked(true);
        }
        refreshListView();
    }

    public void deselectAll() {
        List<TrackedEntityInstance> allTrackedEntityInstances = this.mAdapter.getData();
        getSelectedTrackedEntityInstances().clear();
        View view = null;
        for (int i = 0; i < allTrackedEntityInstances.size(); i++) {
            view = this.mAdapter.getView(i, view, null);
            ((CheckBox) view.findViewById(R.id.checkBoxTeiQuery)).setChecked(false);
        }
        refreshListView();
    }

    public void refreshListView() {
        int start = this.mListView.getFirstVisiblePosition();
        int end = this.mListView.getLastVisiblePosition();
        for (int pos = 0; pos <= end - start; pos++) {
            View view = this.mListView.getChildAt(pos);
            if (view != null) {
                int adapterPosition = view.getId();
                if (adapterPosition >= 0 && adapterPosition < this.mAdapter.getCount() && !view.hasFocus()) {
                    this.mAdapter.getView(adapterPosition, view, this.mListView);
                }
            }
        }
    }

    public void initiateLoading() {
        Dhis2Application.getEventBus().post(new UiEvent(UiEventType.SYNCING_START));
        Log.d(TAG, "loading: " + getSelectedTrackedEntityInstances().size());
        JobExecutor.enqueueJob(new NetworkJob<Object>(0, ResourceType.TRACKEDENTITYINSTANCE) {
            public Object execute() throws APIException {
                TrackerController.getTrackedEntityInstancesDataFromServer(DhisController.getInstance().getDhisApi(), QueryTrackedEntityInstancesResultDialogFragment.this.getSelectedTrackedEntityInstances(), true, true);
                Dhis2Application.getEventBus().post(new UiEvent(UiEventType.SYNCING_END));
                return new Object();
            }
        });
        dismiss();
    }

    @Subscribe
    public void onLoadingMessageEvent(LoadingMessageEvent event) {
        Log.d(TAG, "Message received" + event.message);
        if (this.progressDialogFragment != null && this.progressDialogFragment.getDialog() != null && this.progressDialogFragment.getDialog().isShowing()) {
            ((ProgressDialog) this.progressDialogFragment.getDialog()).setMessage(event.message);
        }
    }
}
