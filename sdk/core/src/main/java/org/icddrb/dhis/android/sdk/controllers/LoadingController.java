package org.icddrb.dhis.android.sdk.controllers;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import java.util.List;

import org.icddrb.dhis.android.sdk.R;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.controllers.tracker.TrackerController;
import org.icddrb.dhis.android.sdk.events.LoadingMessageEvent.EventType;
import org.icddrb.dhis.android.sdk.events.UiEvent;
import org.icddrb.dhis.android.sdk.events.UiEvent.UiEventType;
import org.icddrb.dhis.android.sdk.network.APIException;
import org.icddrb.dhis.android.sdk.network.DhisApi;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.models.OrganisationUnitUser;
import org.icddrb.dhis.android.sdk.persistence.models.UnionFWA;
import org.icddrb.dhis.android.sdk.persistence.preferences.AppPreferences;
import org.icddrb.dhis.android.sdk.persistence.preferences.ResourceType;
import org.icddrb.dhis.android.sdk.utils.NetworkUtils;
import org.icddrb.dhis.android.sdk.utils.UiUtils;

public final class LoadingController {
    public static final String CLASS_TAG = LoadingController.class.getSimpleName();
    public static final String LOAD = "load";

    private LoadingController() {
    }

    public static void enableLoading(Context context, ResourceType resourceType) {
        Editor editor = context.getSharedPreferences(DhisController.PREFS_NAME, 0).edit();
        editor.putBoolean(LOAD + resourceType, true);
        editor.commit();
    }

    public static void clearLoadFlags(Context context) {
        for (ResourceType resourceType : ResourceType.values()) {
            clearLoadFlag(context, resourceType);
        }
    }

    public static void clearLoadFlag(Context context, ResourceType resourceType) {
        Editor editor = context.getSharedPreferences(DhisController.PREFS_NAME, 0).edit();
        editor.putBoolean(LOAD + resourceType, false);
        editor.commit();
    }

    public static boolean isLoadFlagEnabled(Context context, ResourceType flag) {
        return context.getSharedPreferences(DhisController.PREFS_NAME, 0).getBoolean(LOAD + flag, false);
    }

    public static boolean isInitialDataLoaded(Context context) {
        return MetaDataController.isDataLoaded(context) && TrackerController.isDataLoaded(context);
    }

    static void loadInitialData(Context context, DhisApi dhisApi) throws APIException, IllegalStateException {
        UiUtils.postProgressMessage(context.getString(R.string.finishing_up), EventType.STARTUP);
        String message;
        if (!MetaDataController.isDataLoaded(context)) {
            Log.d(CLASS_TAG, "loading initial metadata");
            loadMetaData(context, SyncStrategy.DOWNLOAD_ALL, dhisApi);
            Dhis2Application.getEventBus().post(new UiEvent(UiEventType.INITIAL_SYNCING_END));
            message = "";
            UiUtils.postProgressMessage(context.getString(R.string.finishing_up), EventType.STARTUP);
            loadDataValues(context, SyncStrategy.DOWNLOAD_ALL, dhisApi);
            loadUnionData(context, SyncStrategy.DOWNLOAD_ALL, dhisApi);
            Dhis2Application.getEventBus().post(new UiEvent(UiEventType.INITIAL_SYNCING_END));
        } else if (!TrackerController.isDataLoaded(context)) {
            Log.d(CLASS_TAG, "loading initial datavalues");
            message = "";
            UiUtils.postProgressMessage(context.getString(R.string.finishing_up), EventType.STARTUP);
            loadDataValues(context, SyncStrategy.DOWNLOAD_ALL, dhisApi);
            loadUnionData(context, SyncStrategy.DOWNLOAD_ALL, dhisApi);
        }
    }

    static void loadMetaData(Context context, SyncStrategy syncStrategy, DhisApi dhisApi) throws APIException {
        if (context == null) {
            Log.i(CLASS_TAG, "Unable to load metadata. We have no valid context.");
            return;
        }
        Log.d(CLASS_TAG, "loading metadata!");
        Dhis2Application.getEventBus().post(new UiEvent(UiEventType.SYNCING_START));
        try {
            MetaDataController.loadMetaData(context, dhisApi, syncStrategy);
            Dhis2Application.getEventBus().post(new UiEvent(UiEventType.SYNCING_END));
        } catch (APIException e) {
            Dhis2Application.getEventBus().post(new UiEvent(UiEventType.SYNCING_END));
            throw e;
        }
    }

    static void loadDataValues(Context context, SyncStrategy syncStrategy, DhisApi dhisApi) throws APIException {
        Dhis2Application.getEventBus().post(new UiEvent(UiEventType.SYNCING_START));
        try {
            TrackerController.loadDataValues(context, dhisApi, syncStrategy);
            Dhis2Application.getEventBus().post(new UiEvent(UiEventType.SYNCING_END));
        } catch (APIException e) {
            Dhis2Application.getEventBus().post(new UiEvent(UiEventType.SYNCING_END));
            throw e;
        }
    }

    static void loadUnionData(Context context, SyncStrategy syncStrategy, DhisApi dhisApi) throws APIException {
        Dhis2Application.getEventBus().post(new UiEvent(UiEventType.SYNCING_START));
        try {
            List<OrganisationUnitUser> orgOptionSets = NetworkUtils.unwrapResponse(dhisApi.getOrgsAndUsers(), ApiEndpointContainer.ORGANISATIONUNITS);
            if (orgOptionSets.size() > 0) {
                AppPreferences prefs = new AppPreferences(context);
                UnionFWA dropData = new UnionFWA();
                dropData.init(orgOptionSets);
                prefs.putDropdownInfo(dropData);
            } else {
                System.out.println("Norway - no orgs found");
            }
            Dhis2Application.getEventBus().post(new UiEvent(UiEventType.SYNCING_END));
        } catch (APIException e) {
            Dhis2Application.getEventBus().post(new UiEvent(UiEventType.SYNCING_END));
            throw e;
        }
    }

    static void syncRemotelyDeletedData(Context context, DhisApi dhisApi) throws APIException {
        Dhis2Application.getEventBus().post(new UiEvent(UiEventType.SYNCING_START));
        try {
            TrackerController.syncRemotelyDeletedData(context, dhisApi);
            Dhis2Application.getEventBus().post(new UiEvent(UiEventType.SYNCING_END));
        } catch (APIException e) {
            Dhis2Application.getEventBus().post(new UiEvent(UiEventType.SYNCING_END));
            throw e;
        }
    }
}
