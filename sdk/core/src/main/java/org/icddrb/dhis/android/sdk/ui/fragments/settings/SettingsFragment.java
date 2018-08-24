package org.icddrb.dhis.android.sdk.ui.fragments.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.okhttp.HttpUrl;
import com.squareup.otto.Subscribe;
import org.icddrb.dhis.android.sdk.C0845R;
import org.icddrb.dhis.android.sdk.controllers.DhisController;
import org.icddrb.dhis.android.sdk.controllers.DhisService;
import org.icddrb.dhis.android.sdk.controllers.PeriodicSynchronizerController;
import org.icddrb.dhis.android.sdk.controllers.SyncStrategy;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.events.LoadingMessageEvent;
import org.icddrb.dhis.android.sdk.events.LoadingMessageEvent.EventType;
import org.icddrb.dhis.android.sdk.events.UiEvent;
import org.icddrb.dhis.android.sdk.events.UiEvent.UiEventType;
import org.icddrb.dhis.android.sdk.network.Credentials;
import org.icddrb.dhis.android.sdk.network.Session;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.preferences.AppPreferences;
import org.icddrb.dhis.android.sdk.ui.activities.LoginActivity;
import org.icddrb.dhis.android.sdk.utils.UiUtils;

public class SettingsFragment extends Fragment implements OnClickListener, OnItemSelectedListener {
    public static final String TAG = SettingsFragment.class.getSimpleName();
    private Button logoutButton;
    private ProgressBar mProgressBar;
    private LoadingMessageEvent progressMessage;
    private TextView syncTextView;
    private Button synchronizeButton;
    private Button synchronizeRemovedEventsButton;
    private Spinner updateFrequencySpinner;

    /* renamed from: org.icddrb.dhis.android.sdk.ui.fragments.settings.SettingsFragment$1 */
    class C09221 implements DialogInterface.OnClickListener {

        /* renamed from: org.icddrb.dhis.android.sdk.ui.fragments.settings.SettingsFragment$1$1 */
        class C09211 implements DialogInterface.OnClickListener {
            C09211() {
            }

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }

        C09221() {
        }

        public void onClick(DialogInterface dialog, int which) {
            if (DhisController.hasUnSynchronizedDatavalues) {
                UiUtils.showErrorDialog(SettingsFragment.this.getActivity(), SettingsFragment.this.getString(C0845R.string.error_message), SettingsFragment.this.getString(C0845R.string.unsynchronized_data_values), new C09211());
                return;
            }
            Session session = DhisController.getInstance().getSession();
            if (session != null) {
                HttpUrl httpUrl = session.getServerUrl();
                Credentials c = session.getCredentials();
                if (httpUrl != null) {
                    new AppPreferences(SettingsFragment.this.getActivity().getApplicationContext()).putServerUrl(httpUrl.toString());
                }
                if (c != null) {
                    new AppPreferences(SettingsFragment.this.getActivity().getApplicationContext()).putUserName(c.getUsername());
                }
            }
            DhisService.logOutUser(SettingsFragment.this.getActivity(), false);
            if (VERSION.SDK_INT >= 15) {
                SettingsFragment.this.startActivity(new Intent(SettingsFragment.this.getActivity().getApplicationContext(), LoginActivity.class));
                SettingsFragment.this.getActivity().finishAffinity();
                return;
            }
            SettingsFragment.this.startActivity(new Intent(SettingsFragment.this.getActivity().getApplicationContext(), LoginActivity.class));
            SettingsFragment.this.getActivity().finish();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(C0845R.layout.fragment_settings, container, false);
        if (getActionBar() != null) {
            getActionBar().setTitle(getString(C0845R.string.settings));
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }
        this.updateFrequencySpinner = (Spinner) view.findViewById(C0845R.id.settings_update_frequency_spinner);
        this.updateFrequencySpinner.setSelection(PeriodicSynchronizerController.getUpdateFrequency(getActivity()));
        this.updateFrequencySpinner.setOnItemSelectedListener(this);
        this.synchronizeButton = (Button) view.findViewById(C0845R.id.settings_sync_button);
        this.synchronizeRemovedEventsButton = (Button) view.findViewById(C0845R.id.settings_sync_remotely_deleted_events_button);
        this.logoutButton = (Button) view.findViewById(C0845R.id.settings_soft_logout_button);
        this.mProgressBar = (ProgressBar) view.findViewById(C0845R.id.settings_progessbar);
        this.syncTextView = (TextView) view.findViewById(C0845R.id.settings_sync_textview);
        this.mProgressBar.setVisibility(8);
        this.logoutButton.setOnClickListener(this);
        this.synchronizeButton.setOnClickListener(this);
        this.synchronizeRemovedEventsButton.setOnClickListener(this);
        return view;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        getActivity().finish();
        return true;
    }

    public void onClick(View view) {
        if (view.getId() == C0845R.id.settings_soft_logout_button) {
            UiUtils.showConfirmDialog(getActivity(), getString(C0845R.string.logout_title), getString(C0845R.string.soft_logout_message), getString(C0845R.string.soft_logout), getString(C0845R.string.cancel), new C09221());
        } else if (view.getId() == C0845R.id.settings_sync_button) {
            if (isAdded()) {
                context = getActivity().getBaseContext();
                Toast.makeText(context, getString(C0845R.string.syncing), 0).show();
                setProgressMessage(new LoadingMessageEvent(getString(C0845R.string.syncing), EventType.METADATA));
                new Thread() {
                    public void run() {
                        DhisService.synchronize(context, SyncStrategy.DOWNLOAD_ALL);
                    }
                }.start();
                startSync();
            }
        } else if (view.getId() == C0845R.id.settings_sync_remotely_deleted_events_button && isAdded()) {
            context = getActivity().getBaseContext();
            Toast.makeText(context, getString(C0845R.string.sync_deleted_events), 0).show();
            setProgressMessage(new LoadingMessageEvent(getString(C0845R.string.sync_deleted_events), EventType.REMOVE_DATA));
            new Thread() {
                public void run() {
                    DhisService.synchronizeRemotelyDeletedData(context);
                }
            }.start();
            startSync();
        }
    }

    private void startSync() {
        changeUiVisibility(false);
        setText(getProgressMessage());
    }

    private void endSync() {
        changeUiVisibility(true);
        this.syncTextView.setText("");
        this.synchronizeButton.setText(C0845R.string.synchronize_with_server);
        this.synchronizeRemovedEventsButton.setText(C0845R.string.synchronize_deleted_data);
    }

    private void changeUiVisibility(boolean enabled) {
        if (enabled) {
            this.mProgressBar.setVisibility(8);
        } else {
            this.mProgressBar.setVisibility(0);
        }
        this.synchronizeButton.setEnabled(enabled);
        this.synchronizeRemovedEventsButton.setEnabled(enabled);
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        PeriodicSynchronizerController.setUpdateFrequency(getActivity(), position);
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private void setText(LoadingMessageEvent event) {
        if (event != null) {
            if (event.eventType.equals(EventType.DATA) || event.eventType.equals(EventType.METADATA) || event.eventType.equals(EventType.STARTUP)) {
                changeUiVisibility(false);
                this.synchronizeButton.setText(getActivity().getApplicationContext().getString(C0845R.string.synchronizing));
            } else if (event.eventType.equals(EventType.REMOVE_DATA)) {
                this.synchronizeRemovedEventsButton.setText(getActivity().getApplicationContext().getString(C0845R.string.synchronizing));
                changeUiVisibility(false);
            } else if (event.eventType.equals(EventType.FINISH)) {
                endSync();
            }
            if (event.message != null) {
                this.syncTextView.setText(event.message);
            } else {
                Log.d(TAG, "Loading message is null");
            }
        }
    }

    @Subscribe
    public void onLoadingMessageEvent(final LoadingMessageEvent event) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                SettingsFragment.this.setProgressMessage(event);
                SettingsFragment.this.setText(event);
            }
        });
    }

    private void enableUi(boolean enable) {
        if (enable) {
            endSync();
        } else {
            startSync();
        }
    }

    @Subscribe
    public void onSynchronizationFinishedEvent(UiEvent event) {
        if (event.getEventType().equals(UiEventType.SYNCING_START)) {
            enableUi(false);
        } else if (event.getEventType().equals(UiEventType.SYNCING_END)) {
            enableUi(true);
        }
    }

    public LoadingMessageEvent getProgressMessage() {
        return this.progressMessage;
    }

    public void setProgressMessage(LoadingMessageEvent progressMessage) {
        this.progressMessage = progressMessage;
    }

    public void onPause() {
        super.onPause();
        Dhis2Application.getEventBus().unregister(this);
    }

    public void onResume() {
        super.onResume();
        Dhis2Application.getEventBus().register(this);
        enableUi(true);
        if (!MetaDataController.isDataLoaded(getActivity().getApplicationContext())) {
            LoadingMessageEvent event = new LoadingMessageEvent("", EventType.STARTUP);
            setProgressMessage(event);
            setText(event);
        }
    }

    public ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }
}
