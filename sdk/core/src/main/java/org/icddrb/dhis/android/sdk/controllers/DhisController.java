package org.icddrb.dhis.android.sdk.controllers;

import android.content.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.squareup.okhttp.HttpUrl;
import org.apache.commons.lang3.StringUtils;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.controllers.tracker.TrackerController;
import org.icddrb.dhis.android.sdk.events.UiEvent;
import org.icddrb.dhis.android.sdk.events.UiEvent.UiEventType;
import org.icddrb.dhis.android.sdk.network.APIException;
import org.icddrb.dhis.android.sdk.network.Credentials;
import org.icddrb.dhis.android.sdk.network.DhisApi;
import org.icddrb.dhis.android.sdk.network.RepoManager;
import org.icddrb.dhis.android.sdk.network.Session;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.models.UserAccount;
import org.icddrb.dhis.android.sdk.persistence.preferences.AppPreferences;
import org.icddrb.dhis.android.sdk.persistence.preferences.DateTimeManager;
import org.icddrb.dhis.android.sdk.persistence.preferences.LastUpdatedManager;
import org.icddrb.dhis.android.sdk.synchronization.data.enrollment.EnrollmentLocalDataSource;
import org.icddrb.dhis.android.sdk.synchronization.data.enrollment.EnrollmentRemoteDataSource;
import org.icddrb.dhis.android.sdk.synchronization.data.enrollment.EnrollmentRepository;
import org.icddrb.dhis.android.sdk.synchronization.data.event.EventLocalDataSource;
import org.icddrb.dhis.android.sdk.synchronization.data.event.EventRemoteDataSource;
import org.icddrb.dhis.android.sdk.synchronization.data.event.EventRepository;
import org.icddrb.dhis.android.sdk.synchronization.data.faileditem.FailedItemRepository;
import org.icddrb.dhis.android.sdk.synchronization.data.trackedentityinstance.TrackedEntityInstanceLocalDataSource;
import org.icddrb.dhis.android.sdk.synchronization.data.trackedentityinstance.TrackedEntityInstanceRemoteDataSource;
import org.icddrb.dhis.android.sdk.synchronization.data.trackedentityinstance.TrackedEntityInstanceRepository;
import org.icddrb.dhis.android.sdk.synchronization.domain.app.SyncAppUseCase;
import org.icddrb.dhis.android.sdk.utils.SyncDateWrapper;
import org.icddrb.dhis.client.sdk.ui.AppPreferencesImpl;

public final class DhisController {
    private static final String CLASS_TAG = "Dhis2";
    private static final String CREDENTIALS = "credentials";
    public static final String LAST_UPDATED_DATAVALUES = "lastupdated_datavalues";
    public static final String LAST_UPDATED_METADATA = "lastupdated_metadata";
    private static final String PASSWORD = "password";
    public static final String PREFS_NAME = "DHIS2";
    public static final String QUEUED = "queued";
    private static final String SERVER = "server";
    private static final String USERNAME = "username";
    public static boolean hasUnSynchronizedDatavalues;
    private AppPreferencesImpl appPreferences;
    private boolean blocking = false;
    public Context context;
    private DhisApi dhisApi;
    private ObjectMapper objectMapper = getObjectMapper();
    public AppPreferences preferences;
    private Session session;
    private SyncDateWrapper syncDateWrapper;

    public static DhisController getInstance() {
        return Dhis2Application.dhisController;
    }

    public DhisController(Context context) {
        LastUpdatedManager.init(context);
        DateTimeManager.init(context);
        this.appPreferences = new AppPreferencesImpl(context);
        this.preferences = new AppPreferences(context);
        this.syncDateWrapper = new SyncDateWrapper(context, this.appPreferences);
        this.context = context;
    }

    public void init() {
        readSession();
    }

    public DhisApi getDhisApi() {
        return this.dhisApi;
    }

    public AppPreferences getAppPreferences() {
        return this.preferences;
    }

    public Context getContext() {
        return this.context;
    }

    static void syncRemotelyDeletedData(Context context) throws APIException, IllegalStateException {
        LoadingController.syncRemotelyDeletedData(context, getInstance().getDhisApi());
    }

    static void synchronize(Context context, SyncStrategy syncStrategy) throws APIException, IllegalStateException {
        Dhis2Application.getEventBus().post(new UiEvent(UiEventType.SYNCING_START));
        sendData();
        loadData(context, syncStrategy);
        getInstance().getSyncDateWrapper().setLastSyncedNow();
    }

    public static void forceSynchronize(Context context) throws APIException, IllegalStateException {
        Dhis2Application.getEventBus().post(new UiEvent(UiEventType.SYNCING_START));
        sendData();
        LoadingController.loadMetaData(context, SyncStrategy.DOWNLOAD_ALL, getInstance().getDhisApi());
        LoadingController.loadDataValues(context, SyncStrategy.DOWNLOAD_ALL, getInstance().getDhisApi());
        LoadingController.loadUnionData(context, SyncStrategy.DOWNLOAD_ALL, getInstance().getDhisApi());
        getInstance().getSyncDateWrapper().setLastSyncedNow();
    }

    static void loadData(Context context, SyncStrategy syncStrategy) throws APIException, IllegalStateException {
        LoadingController.loadMetaData(context, syncStrategy, getInstance().getDhisApi());
        LoadingController.loadDataValues(context, syncStrategy, getInstance().getDhisApi());
        LoadingController.loadUnionData(context, syncStrategy, getInstance().getDhisApi());
    }

    static void sendData() throws APIException, IllegalStateException {
        new SyncAppUseCase(new TrackedEntityInstanceRepository(new TrackedEntityInstanceLocalDataSource(), new TrackedEntityInstanceRemoteDataSource(getInstance().getDhisApi())), new EnrollmentRepository(new EnrollmentLocalDataSource(), new EnrollmentRemoteDataSource(getInstance().getDhisApi())), new EventRepository(new EventLocalDataSource(), new EventRemoteDataSource(getInstance().getDhisApi())), new FailedItemRepository()).execute();
    }

    static UserAccount logInUser(HttpUrl serverUrl, Credentials credentials) throws APIException {
        return signInUser(serverUrl, credentials);
    }

    static UserAccount confirmUser(Credentials credentials) throws APIException {
        return signInUser(getInstance().session.getServerUrl(), credentials);
    }

    static UserAccount signInUser(HttpUrl serverUrl, Credentials credentials) throws APIException {
        String username = getInstance().getAppPreferences().getUsername();
        if (!(StringUtils.isEmpty(username) || credentials.getUsername().equals(username))) {
            hardLogoutClear(getInstance().getContext());
        }
        DhisApi dhisApi = RepoManager.createService(serverUrl, credentials);
        dhisApi.getSystemInfo().save();
        UserAccount user = new UserController(dhisApi).logInUser(serverUrl, credentials);
        readSession();
        return user;
    }

    static void logOutUser(Context context, boolean hardLogout) throws APIException {
        if (hardLogout) {
            hardLogoutClear(context);
        }
        new UserController(getInstance().dhisApi).logOut();
    }

    static void hardLogoutClear(Context context) {
        readSession();
        getInstance().getAppPreferences().clearChosenOptions();
        MetaDataController.clearMetaDataLoadedFlags();
        TrackerController.clearDataValueLoadedFlags();
        MetaDataController.wipe();
        LoadingController.clearLoadFlags(context);
    }

    public static boolean isUserLoggedIn() {
        return (getInstance().getSession() == null || getInstance().getSession().getServerUrl() == null || getInstance().getSession().getCredentials() == null) ? false : true;
    }

    private static void readSession() {
        getInstance().session = LastUpdatedManager.getInstance().get();
        if (isUserLoggedIn()) {
            getInstance().dhisApi = RepoManager.createService(getInstance().getSession().getServerUrl(), getInstance().getSession().getCredentials());
        }
    }

    public static void invalidateSession() {
        LastUpdatedManager.getInstance().invalidate();
        readSession();
    }

    public Session getSession() {
        return this.session;
    }

    public ObjectMapper getObjectMapper() {
        if (this.objectMapper == null) {
            this.objectMapper = new ObjectMapper();
            this.objectMapper.registerModule(new JodaModule());
        }
        return this.objectMapper;
    }

    public SyncDateWrapper getSyncDateWrapper() {
        return getInstance().syncDateWrapper;
    }
}
