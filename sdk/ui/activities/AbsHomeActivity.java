package org.icddrb.dhis.client.sdk.ui.activities;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.icddrb.dhis.client.sdk.ui.C0935R;
import org.icddrb.dhis.client.sdk.ui.SettingPreferences;
import org.icddrb.dhis.client.sdk.ui.fragments.HelpFragment;
import org.icddrb.dhis.client.sdk.ui.fragments.InformationFragment;
import org.icddrb.dhis.client.sdk.ui.fragments.WrapperFragment;
import org.icddrb.dhis.client.sdk.utils.Preconditions;

public abstract class AbsHomeActivity extends BaseActivity implements OnNavigationItemSelectedListener, DrawerListener, NavigationCallback, OnBackPressedFromFragmentCallback {
    private static final String APPS_DASHBOARD_PACKAGE = "org.icddrb.dhis.android.dashboard";
    private static final String APPS_DATA_CAPTURE_PACKAGE = "org.dhis2.mobile";
    private static final String APPS_EVENT_CAPTURE_PACKAGE = "org.icddrb.dhis.android.eventcapture";
    private static final String APPS_TRACKER_CAPTURE_PACKAGE = "org.icddrb.dhis.android.eregistry";
    private static final String APPS_TRACKER_CAPTURE_REPORTS_PACKAGE = "org.hispindia.bidtrackerreports";
    private static final int DEFAULT_ORDER_IN_CATEGORY = 100;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Runnable pendingRunnable;
    private TextView userInfo;
    private TextView username;
    private TextView usernameLetter;

    @NonNull
    protected abstract Fragment getProfileFragment();

    @NonNull
    protected abstract Fragment getSettingsFragment();

    protected abstract boolean onItemSelected(@NonNull MenuItem menuItem);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SettingPreferences.init(getApplicationContext());
        setContentView(C0935R.layout.activity_home);
        this.drawerLayout = (DrawerLayout) findViewById(C0935R.id.drawer_layout);
        this.drawerLayout.addDrawerListener(this);
        this.navigationView = (NavigationView) findViewById(C0935R.id.navigation_view);
        this.navigationView.setNavigationItemSelectedListener(this);
        this.navigationView.inflateMenu(C0935R.menu.menu_drawer_default);
        ViewGroup navigationHeader = (ViewGroup) getLayoutInflater().inflate(C0935R.layout.navigation_header, this.navigationView, false);
        this.usernameLetter = (TextView) navigationHeader.findViewById(C0935R.id.textview_username_letter);
        this.username = (TextView) navigationHeader.findViewById(C0935R.id.textview_username);
        this.userInfo = (TextView) navigationHeader.findViewById(C0935R.id.textview_user_info);
        this.navigationView.addHeaderView(navigationHeader);
        this.navigationView.getMenu().findItem(C0935R.id.drawer_item_dashboard).setVisible(isAppInstalled(APPS_DASHBOARD_PACKAGE));
        this.navigationView.getMenu().findItem(C0935R.id.drawer_item_data_capture).setVisible(isAppInstalled(APPS_DATA_CAPTURE_PACKAGE));
        this.navigationView.getMenu().findItem(C0935R.id.drawer_item_event_capture).setVisible(isAppInstalled(APPS_EVENT_CAPTURE_PACKAGE));
        this.navigationView.getMenu().findItem(C0935R.id.drawer_item_tracker_capture).setVisible(isAppInstalled("org.icddrb.dhis.android.eregistry"));
        this.navigationView.getMenu().findItem(C0935R.id.drawer_item_tracker_capture_reports).setVisible(isAppInstalled(APPS_TRACKER_CAPTURE_REPORTS_PACKAGE));
    }

    public boolean onNavigationItemSelected(MenuItem menuItem) {
        boolean isSelected = false;
        int menuItemId = menuItem.getItemId();
        if (menuItemId == C0935R.id.drawer_item_dashboard) {
            isSelected = openApp(APPS_DASHBOARD_PACKAGE);
        } else if (menuItemId == C0935R.id.drawer_item_data_capture) {
            isSelected = openApp(APPS_DATA_CAPTURE_PACKAGE);
        } else if (menuItemId == C0935R.id.drawer_item_event_capture) {
            isSelected = openApp(APPS_EVENT_CAPTURE_PACKAGE);
        } else if (menuItemId == C0935R.id.drawer_item_tracker_capture) {
            isSelected = openApp("org.icddrb.dhis.android.eregistry");
        } else if (menuItemId == C0935R.id.drawer_item_tracker_capture_reports) {
            isSelected = openApp(APPS_TRACKER_CAPTURE_REPORTS_PACKAGE);
        } else if (menuItemId == C0935R.id.drawer_item_profile) {
            attachFragmentDelayed(getProfileFragment());
            isSelected = true;
        } else if (menuItemId == C0935R.id.drawer_item_settings) {
            attachFragmentDelayed(getSettingsFragment());
            isSelected = true;
        } else if (menuItemId == C0935R.id.drawer_item_information) {
            attachFragment(getInformationFragment());
            isSelected = true;
        }
        isSelected = onItemSelected(menuItem) || isSelected;
        if (isSelected) {
            this.navigationView.setCheckedItem(menuItemId);
            this.drawerLayout.closeDrawers();
        }
        return isSelected;
    }

    public void onDrawerOpened(View drawerView) {
        this.pendingRunnable = null;
    }

    public void onDrawerClosed(View drawerView) {
        if (this.pendingRunnable != null) {
            new Handler().post(this.pendingRunnable);
        }
        this.pendingRunnable = null;
    }

    public void onDrawerSlide(View drawerView, float slideOffset) {
    }

    public void onDrawerStateChanged(int newState) {
    }

    public void toggleNavigationDrawer() {
        if (this.drawerLayout.isDrawerOpen(this.navigationView)) {
            this.drawerLayout.closeDrawer(this.navigationView);
        } else {
            this.drawerLayout.openDrawer(this.navigationView);
        }
    }

    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen((int) GravityCompat.START)) {
            this.drawerLayout.closeDrawer((int) GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public boolean onBackPressedFromFragment() {
        onNavigationItemSelected(this.navigationView.getMenu().getItem(0));
        return true;
    }

    protected void attachFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(C0935R.id.content_frame, fragment).commit();
    }

    protected void attachFragmentDelayed(final Fragment fragment) {
        Preconditions.isNull(fragment, "Fragment must not be null");
        this.pendingRunnable = new Runnable() {
            public void run() {
                AbsHomeActivity.this.attachFragment(fragment);
            }
        };
    }

    private boolean isAppInstalled(String packageName) {
        if (getApplicationContext().getPackageName().equals(packageName)) {
            return false;
        }
        try {
            getBaseContext().getPackageManager().getPackageInfo(packageName, 1);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    protected boolean openApp(String packageName) {
        Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null) {
            return false;
        }
        intent.addCategory("android.intent.category.LAUNCHER");
        getBaseContext().startActivity(intent);
        return true;
    }

    protected MenuItem addMenuItem(int menuItemId, @DrawableRes int icon, @StringRes int title) {
        return addMenuItem(menuItemId, ContextCompat.getDrawable(this, icon), getString(title));
    }

    protected MenuItem addMenuItem(int menuItemId, Drawable icon, CharSequence title) {
        MenuItem menuItem = this.navigationView.getMenu().add(C0935R.id.drawer_group_main, menuItemId, 100, title);
        menuItem.setIcon(icon);
        menuItem.setCheckable(true);
        return menuItem;
    }

    protected boolean removeMenuItem(int menuItemId) {
        MenuItem menuItem = getNavigationView().getMenu().findItem(menuItemId);
        if (menuItem == null) {
            return false;
        }
        getNavigationView().getMenu().removeItem(menuItem.getItemId());
        return true;
    }

    @NonNull
    protected NavigationView getNavigationView() {
        return this.navigationView;
    }

    @NonNull
    protected DrawerLayout getDrawerLayout() {
        return this.drawerLayout;
    }

    @NonNull
    protected TextView getUsernameTextView() {
        return this.username;
    }

    @NonNull
    protected TextView getUserInfoTextView() {
        return this.userInfo;
    }

    @NonNull
    protected TextView getUsernameLetterTextView() {
        return this.usernameLetter;
    }

    protected void setSynchronizedMessage(@NonNull CharSequence message) {
        this.navigationView.getMenu().findItem(C0935R.id.drawer_item_synchronized).setTitle(String.format(getString(C0935R.string.drawer_item_synchronized), new Object[]{message}));
    }

    protected Fragment getInformationFragment() {
        return WrapperFragment.newInstance(InformationFragment.class, getString(C0935R.string.drawer_item_information), new Bundle());
    }

    @NonNull
    protected Fragment getHelpFragment() {
        return WrapperFragment.newInstance(HelpFragment.class, getString(C0935R.string.drawer_item_help));
    }
}
