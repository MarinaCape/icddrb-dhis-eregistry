package org.icddrb.dhis.android.sdk.ui.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.squareup.okhttp.HttpUrl;
import com.squareup.otto.Subscribe;

import org.icddrb.dhis.android.sdk.R;
import org.icddrb.dhis.android.sdk.controllers.DhisController;
import org.icddrb.dhis.android.sdk.controllers.DhisService;
import org.icddrb.dhis.android.sdk.controllers.LoadingController;
import org.icddrb.dhis.android.sdk.events.LoadingMessageEvent;
import org.icddrb.dhis.android.sdk.events.UiEvent;
import org.icddrb.dhis.android.sdk.events.UiEvent.UiEventType;
import org.icddrb.dhis.android.sdk.job.NetworkJob.NetworkJobResult;
import org.icddrb.dhis.android.sdk.network.APIException;
import org.icddrb.dhis.android.sdk.network.Credentials;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.preferences.AppPreferences;
import org.icddrb.dhis.android.sdk.persistence.preferences.ResourceType;
import org.icddrb.dhis.android.sdk.utils.UiUtils;

public class LoginActivity extends Activity implements OnClickListener {
    private static final String CLASS_TAG = "LoginActivity";
    public static final String KEY_SAVED_SERVER_URL = "KEY:SERVER_URL";
    private boolean isPulling;
    private Button loginButton;
    private AppPreferences mPrefs;
    private EditText passwordEditText;
    private ProgressBar progressBar;
    private TextView progressText;
    private EditText serverEditText;
    private EditText usernameEditText;
    private View viewsContainer;

    /* renamed from: org.icddrb.dhis.android.sdk.ui.activities.LoginActivity$2 */
    class C08652 implements DialogInterface.OnClickListener {
        C08652() {
        }

        public void onClick(DialogInterface dialog, int which) {
            LoginActivity.this.showLoginDialog();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.mPrefs = new AppPreferences(getApplicationContext());
        setupUI();
    }

    public void onPause() {
        super.onPause();
        Dhis2Application.bus.unregister(this);
    }

    public void onResume() {
        super.onResume();
        Dhis2Application.bus.register(this);
        if (this.isPulling) {
            DhisService.loadInitialData(this);
        }
    }

    private void setupUI() {
        this.viewsContainer = findViewById(R.id.login_views_container);
        this.usernameEditText = (EditText) findViewById(R.id.username);
        this.passwordEditText = (EditText) findViewById(R.id.password);
        this.serverEditText = (EditText) findViewById(R.id.server_url);
        this.loginButton = (Button) findViewById(R.id.login_button);
        String server = "https://bd-eregistry.dhis2.org/dhis";
        String username = null;
        String password = null;
        DhisController.getInstance().init();
        if (DhisController.isUserLoggedIn()) {
            server = DhisController.getInstance().getSession().getServerUrl().toString();
            username = DhisController.getInstance().getSession().getCredentials().getUsername();
            password = DhisController.getInstance().getSession().getCredentials().getPassword();
        }
        if (server == null) {
            server = this.mPrefs.getServerUrl();
            if (server == null) {
                server = "https://";
            }
        }
        if (username == null) {
            username = "";
            password = "";
        }
        this.serverEditText.setText(server);
        this.usernameEditText.setText(username);
        this.passwordEditText.setText(password);
        this.progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        this.progressBar.setVisibility(8);
        this.progressText = (TextView) findViewById(R.id.progress_text);
        this.progressText.setVisibility(8);
        this.loginButton.setOnClickListener(this);
    }

    public void onClick(View v) {
        String username = this.usernameEditText.getText().toString();
        String password = this.passwordEditText.getText().toString();
        String serverURL = this.serverEditText.getText().toString();
        if (username.isEmpty()) {
            showLoginFailedDialog(getString(R.string.enter_username));
        } else if (password.isEmpty()) {
            showLoginFailedDialog(getString(R.string.enter_password));
        } else if (serverURL.isEmpty()) {
            showLoginFailedDialog(getString(R.string.enter_serverurl));
        } else {
            if (username.charAt(username.length() - 1) == ' ') {
                username = username.substring(0, username.length() - 1);
            }
            this.mPrefs.putServerUrl(serverURL);
            login(serverURL, username, password);
        }
    }

    public void login(String serverUrl, String username, String password) {
        showProgress();
        HttpUrl serverUri = HttpUrl.parse(serverUrl);
        if (serverUri == null) {
            showLoginFailedDialog(getString(R.string.invalid_server_url));
        } else {
            DhisService.logInUser(serverUri, new Credentials(username, password));
        }
    }

    @Subscribe
    public void onReceivedUiEvent(UiEvent uiEvent) {
        if (uiEvent.getEventType().equals(UiEventType.INITIAL_SYNCING_END)) {
            this.isPulling = false;
            launchMainActivity();
        }
    }

    @Subscribe
    public void onLoadingMessageEvent(final LoadingMessageEvent event) {
        runOnUiThread(new Runnable() {
            public void run() {
                LoginActivity.this.setText(event);
            }
        });
    }

    private void setText(LoadingMessageEvent event) {
        if (event != null && event.message != null) {
            this.progressText.setText(event.message);
        }
    }

    @Subscribe
    public void onLoginFinished(NetworkJobResult<ResourceType> result) {
        if (result != null && ResourceType.USERS.equals(result.getResourceType())) {
            if (result.getResponseHolder().getApiException() == null) {
                hideKeyboard();
                LoadingController.enableLoading(this, ResourceType.ASSIGNEDPROGRAMS);
                LoadingController.enableLoading(this, ResourceType.OPTIONSETS);
                LoadingController.enableLoading(this, ResourceType.PROGRAMS);
                LoadingController.enableLoading(this, ResourceType.CONSTANTS);
                LoadingController.enableLoading(this, ResourceType.PROGRAMRULES);
                LoadingController.enableLoading(this, ResourceType.PROGRAMRULEVARIABLES);
                LoadingController.enableLoading(this, ResourceType.PROGRAMRULEACTIONS);
                LoadingController.enableLoading(this, ResourceType.RELATIONSHIPTYPES);
                LoadingController.enableLoading(this, ResourceType.EVENTS);
                this.isPulling = true;
                DhisService.loadInitialData(this);
                return;
            }
            onLoginFail(result.getResponseHolder().getApiException());
        }
    }

    private void hideKeyboard() {
        ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(this.passwordEditText.getWindowToken(), 0);
    }

    private void showProgress() {
        this.viewsContainer.startAnimation(AnimationUtils.loadAnimation(this, R.anim.out_up));
        this.viewsContainer.setVisibility(8);
        this.progressBar.setVisibility(0);
        this.progressText.setVisibility(0);
    }

    private void showLoginFailedDialog(String error) {
        UiUtils.showErrorDialog((Activity) this, getString(R.string.error_message), error, new C08652());
    }

    public void onLoginFail(APIException e) {
        if (e.getResponse() == null) {
            showLoginFailedDialog("error" + ": " + e.getMessage());
        } else if (e.getResponse().getStatus() == 401) {
            showLoginFailedDialog(getString(R.string.invalid_username_or_password));
        } else {
            showLoginFailedDialog(getString(R.string.unable_to_login) + " " + e.getMessage());
        }
    }

    private void showLoginDialog() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.in_down);
        this.progressBar.setVisibility(8);
        this.progressText.setVisibility(8);
        this.viewsContainer.setVisibility(0);
        this.viewsContainer.startAnimation(anim);
    }

    public void launchMainActivity() {
        startActivity(new Intent(this, ((Dhis2Application) getApplication()).getMainActivity()));
        finish();
    }

    public void onBackPressed() {
        finish();
        System.exit(0);
        super.onBackPressed();
    }
}
