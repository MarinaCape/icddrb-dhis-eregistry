package org.icddrb.dhis.android.eregistry.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.icddrb.dhis.android.eregistry.C0773R;
import org.icddrb.dhis.android.eregistry.fragments.enrollment.EnrollmentDataEntryFragment;
import org.icddrb.dhis.android.eregistry.fragments.enrollmentdate.EnrollmentDateFragment;
import org.icddrb.dhis.android.eregistry.fragments.programoverview.ProgramOverviewFragment;
import org.icddrb.dhis.android.eregistry.fragments.search.LocalSearchFragment;
import org.icddrb.dhis.android.eregistry.fragments.search.LocalSearchResultFragment;
import org.icddrb.dhis.android.eregistry.fragments.search.OnlineSearchFragment;
import org.icddrb.dhis.android.eregistry.fragments.search.OnlineSearchResultFragment;
import org.icddrb.dhis.android.eregistry.fragments.search.OnlineSearchResultFragment.CallBack;
import org.icddrb.dhis.android.eregistry.fragments.search.OnlineSearchResultFragment.ParameterSerializible;
import org.icddrb.dhis.android.eregistry.fragments.selectprogram.SelectProgramFragment;
import org.icddrb.dhis.android.eregistry.fragments.settings.SettingsFragment;
import org.icddrb.dhis.android.eregistry.fragments.trackedentityinstance.TrackedEntityInstanceDataEntryFragment;
import org.icddrb.dhis.android.eregistry.fragments.trackedentityinstanceprofile.TrackedEntityInstanceProfileFragment;
import org.icddrb.dhis.android.eregistry.fragments.upcomingevents.UpcomingEventsFragment;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.ui.activities.OnBackPressedListener;
import org.icddrb.dhis.android.sdk.ui.fragments.eventdataentry.EventDataEntryFragment;
import org.icddrb.dhis.client.sdk.ui.activities.AbsHomeActivity;
import org.icddrb.dhis.client.sdk.ui.fragments.WrapperFragment;

public class HolderActivity extends AbsHomeActivity {
    public static final String ARG_TYPE = "arg:FragmentType";
    public static final String ARG_TYPE_DATAENTRYFRAGMENT = "arg:DataEntryFragment";
    public static final String ARG_TYPE_ENROLLMENTDATEFRAGMENT = "arg:EnrollmentDateFragment";
    public static final String ARG_TYPE_ENROLLMENTFRAGMENT = "arg:EnrollmentTypeFragment";
    public static final String ARG_TYPE_LOCALSEARCHFRAGMENT = "arg:LocalSearchFragment";
    public static final String ARG_TYPE_LOCALSEARCHRESULTFRAGMENT = "arg:LocalSearchResultFragment";
    public static final String ARG_TYPE_ONLINESEARCHFRAGMENT = "arg:OnlineSearchFragment";
    public static final String ARG_TYPE_ONLINESEARCHRESULTFRAGMENT = "arg:OnlineSearchResultFragment";
    public static final String ARG_TYPE_PROGRAMOVERVIEWFRAGMENT = "arg:ProgramOverviewFragment";
    public static final String ARG_TYPE_SETTINGSFRAGMENT = "arg:SettingsFragment";
    public static final String ARG_TYPE_TRACKEDENTITYINSTANCEDATAENTRYFRAGMENT = "arg:TrackedEntityInstanceDataEntryFragment";
    public static final String ARG_TYPE_TRACKEDENTITYINSTANCEPROFILE = "arg:TrackedEntityInstanceProfile";
    private static final String ARG_TYPE_UPCOMINGEVENTSFRAGMENT = "arg:UpcomingEventsFragment";
    public static CallBack mCallBack;
    OnBackPressedListener onBackPressedListener;

    public void onBackPressed() {
        if (this.onBackPressedListener == null) {
            super.onBackPressed();
        } else if (this.onBackPressedListener.doBack()) {
            super.onBackPressed();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0773R.layout.activity_holder);
        setSupportActionBar((Toolbar) findViewById(C0773R.id.toolbar));
        addMenuItem(11, C0773R.drawable.ic_add, C0773R.string.enroll);
        if (savedInstanceState == null) {
            onNavigationItemSelected(getNavigationView().getMenu().findItem(11));
        }
        System.out.println(getIntent().getExtras().getString(ARG_TYPE));
        String argType = getIntent().getExtras().getString(ARG_TYPE);
        String org = getIntent().getExtras().getString("extra:orgUnitId");
        Object obj = -1;
        switch (argType.hashCode()) {
            case -1794377766:
                if (argType.equals(ARG_TYPE_TRACKEDENTITYINSTANCEDATAENTRYFRAGMENT)) {
                    obj = 5;
                    break;
                }
                break;
            case -1617452282:
                if (argType.equals(ARG_TYPE_ENROLLMENTDATEFRAGMENT)) {
                    obj = 6;
                    break;
                }
                break;
            case -1613989876:
                if (argType.equals(ARG_TYPE_ONLINESEARCHRESULTFRAGMENT)) {
                    obj = 8;
                    break;
                }
                break;
            case -1590541297:
                if (argType.equals(ARG_TYPE_ONLINESEARCHFRAGMENT)) {
                    obj = 7;
                    break;
                }
                break;
            case -516680631:
                if (argType.equals(ARG_TYPE_PROGRAMOVERVIEWFRAGMENT)) {
                    obj = 1;
                    break;
                }
                break;
            case -196375276:
                if (argType.equals(ARG_TYPE_DATAENTRYFRAGMENT)) {
                    obj = 3;
                    break;
                }
                break;
            case 290311452:
                if (argType.equals(ARG_TYPE_LOCALSEARCHRESULTFRAGMENT)) {
                    obj = 10;
                    break;
                }
                break;
            case 1056765586:
                if (argType.equals(ARG_TYPE_ENROLLMENTFRAGMENT)) {
                    obj = null;
                    break;
                }
                break;
            case 1256987307:
                if (argType.equals(ARG_TYPE_TRACKEDENTITYINSTANCEPROFILE)) {
                    obj = 4;
                    break;
                }
                break;
            case 1331471647:
                if (argType.equals(ARG_TYPE_LOCALSEARCHFRAGMENT)) {
                    obj = 9;
                    break;
                }
                break;
            case 1346952329:
                if (argType.equals(ARG_TYPE_UPCOMINGEVENTSFRAGMENT)) {
                    obj = 11;
                    break;
                }
                break;
            case 1627618391:
                if (argType.equals(ARG_TYPE_SETTINGSFRAGMENT)) {
                    obj = 2;
                    break;
                }
                break;
        }
        switch (obj) {
            case null:
                EnrollmentDataEntryFragment fragment = new EnrollmentDataEntryFragment();
                this.onBackPressedListener = fragment;
                fragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(C0773R.id.content_frame, fragment).commit();
                return;
            case 1:
                ProgramOverviewFragment fragment2 = new ProgramOverviewFragment();
                this.onBackPressedListener = fragment2;
                fragment2.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(C0773R.id.content_frame, fragment2).commit();
                return;
            case 2:
                this.onBackPressedListener = null;
                SettingsFragment settingsFragment = new SettingsFragment();
                settingsFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(C0773R.id.content_frame, settingsFragment).commit();
                return;
            case 3:
                EventDataEntryFragment eventDataEntryFragment = new EventDataEntryFragment();
                this.onBackPressedListener = eventDataEntryFragment;
                eventDataEntryFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(C0773R.id.content_frame, eventDataEntryFragment).commit();
                return;
            case 4:
                Fragment trackedEntityInstanceProfileFragment = new TrackedEntityInstanceProfileFragment();
                this.onBackPressedListener = trackedEntityInstanceProfileFragment;
                trackedEntityInstanceProfileFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(C0773R.id.content_frame, trackedEntityInstanceProfileFragment).commit();
                return;
            case 5:
                Fragment trackedEntityInstanceDataEntryFragment = new TrackedEntityInstanceDataEntryFragment();
                this.onBackPressedListener = trackedEntityInstanceDataEntryFragment;
                trackedEntityInstanceDataEntryFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(C0773R.id.content_frame, trackedEntityInstanceDataEntryFragment).commit();
                return;
            case 6:
                EnrollmentDateFragment enrollmentDateFragment = new EnrollmentDateFragment();
                this.onBackPressedListener = enrollmentDateFragment;
                enrollmentDateFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(C0773R.id.content_frame, enrollmentDateFragment).commit();
                return;
            case 7:
                this.onBackPressedListener = null;
                OnlineSearchFragment onlineSearchFragment = new OnlineSearchFragment();
                onlineSearchFragment.setArguments(getIntent().getExtras());
                onlineSearchFragment.runQuery();
                getSupportFragmentManager().beginTransaction().replace(C0773R.id.content_frame, onlineSearchFragment).commit();
                return;
            case 8:
                this.onBackPressedListener = null;
                OnlineSearchResultFragment onlineSearchResultFragment = new OnlineSearchResultFragment();
                onlineSearchResultFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(C0773R.id.content_frame, onlineSearchResultFragment).commit();
                return;
            case 9:
                this.onBackPressedListener = null;
                LocalSearchFragment localSearchFragment = new LocalSearchFragment();
                localSearchFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(C0773R.id.content_frame, localSearchFragment).commit();
                return;
            case 10:
                this.onBackPressedListener = null;
                LocalSearchResultFragment localSearchResultFragment = new LocalSearchResultFragment();
                localSearchResultFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(C0773R.id.content_frame, localSearchResultFragment).commit();
                return;
            case 11:
                this.onBackPressedListener = null;
                Fragment upcomingEventsFragment = new UpcomingEventsFragment();
                upcomingEventsFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(C0773R.id.content_frame, upcomingEventsFragment).commit();
                return;
            default:
                return;
        }
    }

    @NonNull
    protected Fragment getProfileFragment() {
        return new Fragment();
    }

    @NonNull
    protected Fragment getSettingsFragment() {
        return WrapperFragment.newInstance(SettingsFragment.class, getString(C0773R.string.drawer_item_settings));
    }

    protected boolean onItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() != 11) {
            return false;
        }
        attachFragment(WrapperFragment.newInstance(SelectProgramFragment.class, getString(C0773R.string.app_name)));
        return true;
    }

    public static void navigateToEnrollmentDataEntryFragment(Activity activity, String orgUnitId, String programId, long trackedEntityInstanceId, String dateOfEnrollment, String dateOfIncident) {
        Intent intent = new Intent(activity, HolderActivity.class);
        intent.putExtra("extra:orgUnitId", orgUnitId);
        intent.putExtra("extra:ProgramId", programId);
        intent.putExtra("extra:TrackedEntityInstanceId", trackedEntityInstanceId);
        intent.putExtra(EnrollmentDataEntryFragment.ENROLLMENT_DATE, dateOfEnrollment);
        intent.putExtra(EnrollmentDataEntryFragment.INCIDENT_DATE, dateOfIncident);
        intent.putExtra(ARG_TYPE, ARG_TYPE_ENROLLMENTFRAGMENT);
        activity.startActivity(intent);
    }

    public static void navigateToEnrollmentDataEntryFragment(Activity activity, String orgUnitId, String programId, String dateOfEnrollment, String dateOfIncident) {
        Intent intent = new Intent(activity, HolderActivity.class);
        intent.putExtra("extra:orgUnitId", orgUnitId);
        intent.putExtra("extra:ProgramId", programId);
        intent.putExtra(EnrollmentDataEntryFragment.ENROLLMENT_DATE, dateOfEnrollment);
        intent.putExtra(EnrollmentDataEntryFragment.INCIDENT_DATE, dateOfIncident);
        intent.putExtra(ARG_TYPE, ARG_TYPE_ENROLLMENTFRAGMENT);
        activity.startActivity(intent);
    }

    public static void navigateToProgramOverviewFragment(Activity activity, String orgUnitId, String programId, long trackedEntityInstanceId) {
        Intent intent = new Intent(activity, HolderActivity.class);
        intent.putExtra("extra:orgUnitId", orgUnitId);
        intent.putExtra("extra:ProgramId", programId);
        intent.putExtra("extra:TrackedEntityInstanceId", trackedEntityInstanceId);
        intent.putExtra(ARG_TYPE, ARG_TYPE_PROGRAMOVERVIEWFRAGMENT);
        activity.startActivity(intent);
    }

    public static void navigateToDataEntryFragment(Activity activity, String orgUnitId, String programId, String programStageId, long localEnrollmentId, long eventId) {
        Intent intent = new Intent(activity, HolderActivity.class);
        intent.putExtra("extra:orgUnitId", orgUnitId);
        intent.putExtra("extra:ProgramId", programId);
        intent.putExtra(EventDataEntryFragment.PROGRAM_STAGE_ID, programStageId);
        intent.putExtra("extra:EnrollmentId", localEnrollmentId);
        intent.putExtra(EventDataEntryFragment.EVENT_ID, eventId);
        intent.putExtra(ARG_TYPE, ARG_TYPE_DATAENTRYFRAGMENT);
        activity.startActivity(intent);
    }

    public static void navigateToEnrollmentDateFragment(Activity activity, long enrollmentId) {
        Intent intent = new Intent(activity, HolderActivity.class);
        intent.putExtra("extra:EnrollmentId", enrollmentId);
        intent.putExtra(ARG_TYPE, ARG_TYPE_ENROLLMENTDATEFRAGMENT);
        activity.startActivity(intent);
    }

    public static void navigateToTrackedEntityInstanceProfileFragment(Activity activity, long trackedEntityInstanceId, String programId, String orgId) {
        Intent intent = new Intent(activity, HolderActivity.class);
        intent.putExtra("extra:orgUnitId", orgId);
        intent.putExtra("extra:TrackedEntityInstanceId", trackedEntityInstanceId);
        intent.putExtra("extra:ProgramId", programId);
        intent.putExtra(ARG_TYPE, ARG_TYPE_TRACKEDENTITYINSTANCEPROFILE);
        activity.startActivity(intent);
    }

    public static void navigateToDataEntryFragment(Activity activity, String orgUnitId, String programId, String programStageId, long localEnrollmentId) {
        Intent intent = new Intent(activity, HolderActivity.class);
        intent.putExtra("extra:orgUnitId", orgUnitId);
        intent.putExtra("extra:ProgramId", programId);
        intent.putExtra(EventDataEntryFragment.PROGRAM_STAGE_ID, programStageId);
        intent.putExtra("extra:EnrollmentId", localEnrollmentId);
        intent.putExtra(ARG_TYPE, ARG_TYPE_DATAENTRYFRAGMENT);
        activity.startActivity(intent);
    }

    public static void navigateToSettingsFragment(Activity activity) {
        Intent intent = new Intent(activity, HolderActivity.class);
        intent.putExtra(ARG_TYPE, ARG_TYPE_SETTINGSFRAGMENT);
        activity.startActivity(intent);
    }

    public static void navigateToOnlineSearchFragment(Activity activity, String programId, String orgUnitId, boolean backNavigation, CallBack callBack) {
        mCallBack = callBack;
        Intent intent = new Intent(activity, HolderActivity.class);
        intent.putExtra(OnlineSearchFragment.EXTRA_PROGRAM, programId);
        intent.putExtra("extra:orgUnit", orgUnitId);
        intent.putExtra("extra:Navigation", backNavigation);
        intent.putExtra(ARG_TYPE, ARG_TYPE_ONLINESEARCHFRAGMENT);
        intent.setFlags(intent.getFlags() | 1073741824);
        activity.startActivity(intent);
    }

    public static void navigateToOnlineSearchResultFragment(Activity activity, List<TrackedEntityInstance> trackedEntityInstances, String orgUnit, String program, boolean backNavigation) {
        try {
            Intent intent = new Intent(activity, HolderActivity.class);
            ParameterSerializible parameterSerializible1 = new ParameterSerializible(trackedEntityInstances);
            ParameterSerializible parameterSerializible2 = new ParameterSerializible(new ArrayList());
            intent.putExtra("extra:orgUnit", orgUnit);
            intent.putExtra(OnlineSearchResultFragment.EXTRA_SELECTALL, false);
            intent.putExtra(OnlineSearchResultFragment.EXTRA_PROGRAM, program);
            intent.putExtra(OnlineSearchResultFragment.EXTRA_TRACKEDENTITYINSTANCESSELECTED, parameterSerializible1);
            intent.putExtra(OnlineSearchResultFragment.EXTRA_TRACKEDENTITYINSTANCESLIST, parameterSerializible2);
            intent.putExtra("extra:Navigation", backNavigation);
            intent.putExtra(ARG_TYPE, ARG_TYPE_ONLINESEARCHRESULTFRAGMENT);
            intent.setFlags(intent.getFlags() | 1073741824);
            activity.startActivity(intent);
        } catch (Exception e) {
            if (activity != null) {
                Toast.makeText(activity, C0773R.string.generic_error, 1).show();
            }
        }
    }

    public static void navigateToLocalSearchFragment(Activity activity, String orgUnitId, String programId) {
        Intent intent = new Intent(activity, HolderActivity.class);
        intent.putExtra("extra:ProgramId", programId);
        intent.putExtra("extra:OrgUnitId", orgUnitId);
        intent.putExtra(ARG_TYPE, ARG_TYPE_LOCALSEARCHFRAGMENT);
        activity.startActivity(intent);
    }

    public static void navigateToLocalSearchResultFragment(Activity activity, String organisationUnitId, String program, HashMap<String, String> attributeValues) {
        Intent intent = new Intent(activity, HolderActivity.class);
        intent.putExtra("extra:OrgUnitId", organisationUnitId);
        intent.putExtra("extra:ProgramId", program);
        intent.putExtra(LocalSearchResultFragment.EXTRA_ATTRIBUTEVALUEMAP, attributeValues);
        intent.putExtra(ARG_TYPE, ARG_TYPE_LOCALSEARCHRESULTFRAGMENT);
        activity.startActivity(intent);
    }

    public static void navigateToUpcomingEventsFragment(Activity activity) {
        Intent intent = new Intent(activity, HolderActivity.class);
        intent.putExtra(ARG_TYPE, ARG_TYPE_UPCOMINGEVENTSFRAGMENT);
        activity.startActivity(intent);
    }

    public static void navigateToTrackedEntityInstanceDataEntryFragment(Activity activity, String programId, String orgUnit, boolean navigationBack, CallBack callBack) {
        mCallBack = callBack;
        Intent intent = new Intent(activity, HolderActivity.class);
        intent.putExtra("extra:ProgramId", programId);
        intent.putExtra("extra:orgUnitId", orgUnit);
        intent.putExtra("extra:Navigation", navigationBack);
        intent.putExtra(ARG_TYPE, ARG_TYPE_TRACKEDENTITYINSTANCEDATAENTRYFRAGMENT);
        activity.startActivity(intent);
    }
}
