package org.icddrb.dhis.android.eregistry;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import org.icddrb.dhis.android.eregistry.activities.HolderActivity;
import org.icddrb.dhis.android.eregistry.fragments.selectprogram.SelectProgramFragment;
import org.icddrb.dhis.android.sdk.controllers.DhisController;
import org.icddrb.dhis.android.sdk.controllers.PeriodicSynchronizerController;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.network.Session;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.models.UserAccount;
import org.icddrb.dhis.android.sdk.utils.ScreenSizeConfigurator;
import org.icddrb.dhis.client.sdk.ui.activities.AbsHomeActivity;
import org.icddrb.dhis.client.sdk.ui.fragments.InformationFragment;
import org.icddrb.dhis.client.sdk.ui.fragments.WrapperFragment;
import org.icddrb.dhis.client.sdk.utils.StringUtils;

public class MainActivity extends AbsHomeActivity {
    private static final String APPS_DASHBOARD_PACKAGE = "org.icddrb.dhis.android.dashboard";
    private static final String APPS_DATA_CAPTURE_PACKAGE = "org.dhis2.mobile";
    private static final String APPS_EVENT_CAPTURE_PACKAGE = "org.icddrb.dhis.android.eventcapture";
    private static final String APPS_TRACKER_CAPTURE_PACKAGE = "org.icddrb.dhis.android.eregistry";
    private static final String APPS_TRACKER_CAPTURE_REPORTS_PACKAGE = "org.hispindia.bidtrackerreports";
    public static final String TAG = MainActivity.class.getSimpleName();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenSizeConfigurator.init(getWindowManager());
        if ((getIntent().getFlags() & 4194304) != 0) {
            finish();
            return;
        }
        Dhis2Application.bus.register(this);
        PeriodicSynchronizerController.activatePeriodicSynchronizer(this);
        setUpNavigationView(savedInstanceState);
    }

    private void setUpNavigationView(Bundle savedInstanceState) {
        removeMenuItem(C0773R.id.drawer_item_profile);
        addMenuItem(11, (int) C0773R.drawable.ic_add, (int) C0773R.string.enroll);
        if (savedInstanceState == null) {
            onNavigationItemSelected(getNavigationView().getMenu().findItem(11));
        }
        UserAccount userAccount = MetaDataController.getUserAccount();
        String name = "";
        if (userAccount != null) {
            if (!StringUtils.isEmpty(userAccount.getFirstName()) && !StringUtils.isEmpty(userAccount.getSurname())) {
                name = String.valueOf(userAccount.getFirstName().charAt(0)) + String.valueOf(userAccount.getSurname().charAt(0));
            } else if (userAccount.getDisplayName() != null && userAccount.getDisplayName().length() > 1) {
                name = String.valueOf(userAccount.getDisplayName().charAt(0)) + String.valueOf(userAccount.getDisplayName().charAt(1));
            }
            getUsernameTextView().setText(userAccount.getDisplayName());
            getUserInfoTextView().setText(userAccount.getEmail());
        }
        getUsernameLetterTextView().setText(name);
    }

    @NonNull
    protected Fragment getProfileFragment() {
        return new Fragment();
    }

    @NonNull
    protected Fragment getSettingsFragment() {
        return new Fragment();
    }

    protected boolean onItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() != 11) {
            return false;
        }
        attachFragment(WrapperFragment.newInstance(SelectProgramFragment.class, getString(C0773R.string.app_name)));
        return true;
    }

    public void onStart() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
        Dhis2Application.getEventBus().unregister(this);
    }

    public void onResume() {
        super.onResume();
        ScreenSizeConfigurator.init(getWindowManager());
        Dhis2Application.getEventBus().register(this);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ScreenSizeConfigurator.init(getWindowManager());
    }

    public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
        setSynchronizedMessage(DhisController.getInstance().getSyncDateWrapper().getLastSyncedString());
    }

    public boolean onNavigationItemSelected(MenuItem menuItem) {
        boolean isSelected = false;
        int menuItemId = menuItem.getItemId();
        if (menuItemId == C0773R.id.drawer_item_dashboard) {
            isSelected = openApp(APPS_DASHBOARD_PACKAGE);
        } else if (menuItemId == C0773R.id.drawer_item_data_capture) {
            isSelected = openApp(APPS_DATA_CAPTURE_PACKAGE);
        } else if (menuItemId == C0773R.id.drawer_item_event_capture) {
            isSelected = openApp(APPS_EVENT_CAPTURE_PACKAGE);
        } else if (menuItemId == C0773R.id.drawer_item_tracker_capture) {
            isSelected = openApp("org.icddrb.dhis.android.eregistry");
        } else if (menuItemId == C0773R.id.drawer_item_tracker_capture_reports) {
            isSelected = openApp(APPS_TRACKER_CAPTURE_REPORTS_PACKAGE);
        } else if (menuItemId == C0773R.id.drawer_item_profile) {
            attachFragmentDelayed(getProfileFragment());
            isSelected = true;
        } else if (menuItemId == C0773R.id.drawer_item_settings) {
            HolderActivity.navigateToSettingsFragment(this);
            isSelected = true;
        } else if (menuItemId == C0773R.id.drawer_item_information) {
            attachFragment(getInformationFragment());
            isSelected = true;
        }
        isSelected = onItemSelected(menuItem) || isSelected;
        if (isSelected) {
            getNavigationView().setCheckedItem(menuItemId);
            getDrawerLayout().closeDrawers();
        }
        return isSelected;
    }

    protected Fragment getInformationFragment() {
        Bundle args = new Bundle();
        Session session = DhisController.getInstance().getSession();
        if (!(session == null || session.getCredentials() == null)) {
            args.putString(InformationFragment.USERNAME, session.getCredentials().getUsername());
            args.putString("url", String.valueOf(session.getServerUrl()));
        }
        return WrapperFragment.newInstance(InformationFragment.class, getString(C0773R.string.drawer_item_information), args);
    }
}
