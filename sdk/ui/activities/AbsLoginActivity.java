package org.icddrb.dhis.client.sdk.ui.activities;

import android.animation.LayoutTransition;
import android.animation.LayoutTransition.TransitionListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable.Builder;
import org.icddrb.dhis.client.sdk.ui.C0935R;
import org.icddrb.dhis.client.sdk.ui.views.AbsTextWatcher;
import org.icddrb.dhis.client.sdk.utils.Preconditions;

public abstract class AbsLoginActivity extends AppCompatActivity {
    private static final String ARG_LAUNCH_MODE_CONFIRM_USER = "mode:confirmUser";
    private static final String ARG_LAUNCH_MODE_LOGIN_USER = "mode:loginUser";
    private static final String ARG_LOGIN_ACTIVITY_LAUNCH_MODE = "arg:launchMode";
    private static final String ARG_SERVER_URL = "arg:serverUrl";
    private static final String ARG_USERNAME = "arg:username";
    private static final String IS_LOADING = "state:isLoading";
    private LayoutTransition layoutTransition;
    private Animation layoutTransitionSlideIn;
    private Animation layoutTransitionSlideOut;
    private Button loginButton;
    private ViewGroup loginViewsContainer;
    private Button logoutButton;
    private OnPostAnimationRunnable onPostAnimationAction;
    private OnPostAnimationListener onPostAnimationListener;
    private EditText password;
    private CircularProgressBar progressBar;
    private EditText serverUrl;
    private EditText username;

    /* renamed from: org.icddrb.dhis.client.sdk.ui.activities.AbsLoginActivity$1 */
    class C09371 implements OnClickListener {
        C09371() {
        }

        public void onClick(View v) {
            AbsLoginActivity.this.onLogoutButtonClicked();
        }
    }

    /* renamed from: org.icddrb.dhis.client.sdk.ui.activities.AbsLoginActivity$2 */
    class C09382 implements OnClickListener {
        C09382() {
        }

        public void onClick(View v) {
            AbsLoginActivity.this.onLoginButtonClicked(AbsLoginActivity.this.serverUrl.getText(), AbsLoginActivity.this.username.getText(), AbsLoginActivity.this.password.getText());
        }
    }

    private class FieldTextWatcher extends AbsTextWatcher {
        private FieldTextWatcher() {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            AbsLoginActivity.this.onTextChanged();
        }
    }

    protected interface OnAnimationFinishListener {
        void onFinish();
    }

    private class OnPostAnimationListener implements TransitionListener, AnimationListener {
        private OnPostAnimationListener() {
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            onPostAnimation();
        }

        public void startTransition(LayoutTransition transition, ViewGroup container, View view, int type) {
        }

        public void endTransition(LayoutTransition transition, ViewGroup container, View view, int type) {
            if (type == 0 || 1 == type) {
                onPostAnimation();
            }
        }

        private void onPostAnimation() {
            if (AbsLoginActivity.this.onPostAnimationAction != null) {
                AbsLoginActivity.this.onPostAnimationAction.run();
                AbsLoginActivity.this.onPostAnimationAction = null;
            }
        }
    }

    private static class OnPostAnimationRunnable implements Runnable {
        private final OnAnimationFinishListener listener;
        private final AbsLoginActivity loginActivity;
        private final boolean showProgress;

        public OnPostAnimationRunnable(OnAnimationFinishListener listener, AbsLoginActivity loginActivity, boolean showProgress) {
            this.listener = listener;
            this.loginActivity = loginActivity;
            this.showProgress = showProgress;
        }

        public void run() {
            if (this.loginActivity != null) {
                if (this.showProgress) {
                    this.loginActivity.showProgress();
                } else {
                    this.loginActivity.hideProgress();
                }
            }
            if (this.listener != null) {
                this.listener.onFinish();
            }
        }

        public boolean isProgressBarWillBeShown() {
            return this.showProgress;
        }
    }

    protected abstract void onLoginButtonClicked(Editable editable, Editable editable2, Editable editable3);

    public static void navigateTo(Activity currentActivity, Class<? extends Activity> target, String serverUrl, String username) {
        Preconditions.isNull(currentActivity, "Activity must not be null");
        Preconditions.isNull(target, "Target activity class must not be null");
        Preconditions.isNull(serverUrl, "ServerUrl must not be null");
        Preconditions.isNull(username, "Username must not be null");
        Intent intent = new Intent(currentActivity, target);
        intent.putExtra(ARG_LOGIN_ACTIVITY_LAUNCH_MODE, ARG_LAUNCH_MODE_CONFIRM_USER);
        intent.putExtra(ARG_SERVER_URL, serverUrl);
        intent.putExtra(ARG_USERNAME, username);
        ActivityCompat.startActivity(currentActivity, intent, null);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0935R.layout.activity_login);
        getWindow().setSoftInputMode(2);
        float progressBarStrokeWidth = (float) getResources().getDimensionPixelSize(C0935R.dimen.progressbar_stroke_width);
        this.progressBar = (CircularProgressBar) findViewById(C0935R.id.progress_bar_circular);
        this.progressBar.setIndeterminateDrawable(new Builder(this).color(ContextCompat.getColor(this, C0935R.color.color_primary_default)).style(1).strokeWidth(progressBarStrokeWidth).rotationSpeed(1.0f).sweepSpeed(1.0f).build());
        this.loginViewsContainer = (CardView) findViewById(C0935R.id.layout_login_views);
        this.loginButton = (Button) findViewById(C0935R.id.button_log_in);
        this.logoutButton = (Button) findViewById(C0935R.id.button_log_out);
        this.serverUrl = (EditText) findViewById(C0935R.id.edittext_server_url);
        this.username = (EditText) findViewById(C0935R.id.edittext_username);
        this.password = (EditText) findViewById(C0935R.id.edittext_password);
        FieldTextWatcher watcher = new FieldTextWatcher();
        this.serverUrl.addTextChangedListener(watcher);
        this.username.addTextChangedListener(watcher);
        this.password.addTextChangedListener(watcher);
        this.logoutButton.setVisibility(8);
        if (getIntent().getExtras() != null) {
            if (ARG_LAUNCH_MODE_CONFIRM_USER.equals(getIntent().getExtras().getString(ARG_LOGIN_ACTIVITY_LAUNCH_MODE, ARG_LAUNCH_MODE_LOGIN_USER))) {
                String predefinedServerUrl = getIntent().getExtras().getString(ARG_SERVER_URL);
                String predefinedUsername = getIntent().getExtras().getString(ARG_USERNAME);
                this.serverUrl.setText(predefinedServerUrl);
                this.serverUrl.setEnabled(false);
                this.username.setText(predefinedUsername);
                this.username.setEnabled(false);
                this.loginButton.setText(C0935R.string.confirm_user);
                this.logoutButton.setVisibility(0);
                this.logoutButton.setOnClickListener(new C09371());
            }
        }
        this.onPostAnimationListener = new OnPostAnimationListener();
        if (isGreaterThanOrJellyBean()) {
            this.layoutTransition = new LayoutTransition();
            this.layoutTransition.enableTransitionType(4);
            this.layoutTransition.addTransitionListener(this.onPostAnimationListener);
            ((RelativeLayout) findViewById(C0935R.id.layout_content)).setLayoutTransition(this.layoutTransition);
        } else {
            this.layoutTransitionSlideIn = AnimationUtils.loadAnimation(this, C0935R.anim.in_up);
            this.layoutTransitionSlideOut = AnimationUtils.loadAnimation(this, C0935R.anim.out_down);
            this.layoutTransitionSlideIn.setAnimationListener(this.onPostAnimationListener);
            this.layoutTransitionSlideOut.setAnimationListener(this.onPostAnimationListener);
        }
        hideProgress();
        onTextChanged();
        this.loginButton.setOnClickListener(new C09382());
    }

    protected void onPause() {
        if (this.onPostAnimationAction != null) {
            this.onPostAnimationAction.run();
            this.onPostAnimationAction = null;
        }
        super.onPause();
    }

    protected final void onSaveInstanceState(Bundle outState) {
        if (this.onPostAnimationAction != null) {
            outState.putBoolean(IS_LOADING, this.onPostAnimationAction.isProgressBarWillBeShown());
        } else {
            outState.putBoolean(IS_LOADING, this.progressBar.isShown());
        }
        super.onSaveInstanceState(outState);
    }

    protected final void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null || !savedInstanceState.getBoolean(IS_LOADING, false)) {
            hideProgress();
        } else {
            showProgress();
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void navigateTo(Class<? extends Activity> activityClass) {
        Preconditions.isNull(activityClass, "Target activity must not be null");
        ActivityCompat.startActivity(this, new Intent(this, activityClass), null);
        overridePendingTransition(C0935R.anim.activity_open_enter, C0935R.anim.activity_open_exit);
        finish();
    }

    private void showProgress() {
        if (this.layoutTransitionSlideOut != null) {
            this.loginViewsContainer.startAnimation(this.layoutTransitionSlideOut);
        }
        this.loginViewsContainer.setVisibility(8);
        this.progressBar.setVisibility(0);
    }

    private void hideProgress() {
        if (this.layoutTransitionSlideIn != null) {
            this.loginViewsContainer.startAnimation(this.layoutTransitionSlideIn);
        }
        this.loginViewsContainer.setVisibility(0);
        this.progressBar.setVisibility(8);
    }

    private void onTextChanged() {
        Button button = this.loginButton;
        boolean z = (TextUtils.isEmpty(this.serverUrl.getText()) || TextUtils.isEmpty(this.username.getText()) || TextUtils.isEmpty(this.password.getText())) ? false : true;
        button.setEnabled(z);
    }

    private boolean isAnimationInProgress() {
        boolean layoutTransitionAnimationsInProgress;
        if (this.layoutTransition == null || !this.layoutTransition.isRunning()) {
            layoutTransitionAnimationsInProgress = false;
        } else {
            layoutTransitionAnimationsInProgress = true;
        }
        boolean layoutTransitionAnimationSlideUpInProgress;
        if (this.layoutTransitionSlideIn == null || !this.layoutTransitionSlideIn.hasStarted() || this.layoutTransitionSlideIn.hasEnded()) {
            layoutTransitionAnimationSlideUpInProgress = false;
        } else {
            layoutTransitionAnimationSlideUpInProgress = true;
        }
        boolean layoutTransitionAnimationSlideOutInProgress;
        if (this.layoutTransitionSlideOut == null || !this.layoutTransitionSlideOut.hasStarted() || this.layoutTransitionSlideOut.hasEnded()) {
            layoutTransitionAnimationSlideOutInProgress = false;
        } else {
            layoutTransitionAnimationSlideOutInProgress = true;
        }
        if (layoutTransitionAnimationsInProgress || layoutTransitionAnimationSlideUpInProgress || layoutTransitionAnimationSlideOutInProgress) {
            return true;
        }
        return false;
    }

    private static boolean isGreaterThanOrJellyBean() {
        return VERSION.SDK_INT >= 16;
    }

    protected final void onStartLoading() {
        if (isAnimationInProgress()) {
            this.onPostAnimationAction = new OnPostAnimationRunnable(null, this, true);
        } else {
            showProgress();
        }
    }

    protected final void onFinishLoading() {
        onFinishLoading(null);
    }

    protected final void onFinishLoading(OnAnimationFinishListener listener) {
        if (isAnimationInProgress()) {
            this.onPostAnimationAction = new OnPostAnimationRunnable(listener, this, false);
            return;
        }
        hideProgress();
        if (listener != null) {
            listener.onFinish();
        }
    }

    protected EditText getServerUrl() {
        return this.serverUrl;
    }

    protected EditText getUsername() {
        return this.username;
    }

    protected EditText getPassword() {
        return this.password;
    }

    protected Button getLoginButton() {
        return this.loginButton;
    }

    protected Button getLogoutButton() {
        return this.logoutButton;
    }

    protected void onLogoutButtonClicked() {
    }
}
