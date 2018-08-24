package org.icddrb.dhis.android.sdk.controllers;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import com.squareup.okhttp.HttpUrl;
import org.icddrb.dhis.android.sdk.events.UiEvent;
import org.icddrb.dhis.android.sdk.events.UiEvent.UiEventType;
import org.icddrb.dhis.android.sdk.job.Job;
import org.icddrb.dhis.android.sdk.job.JobExecutor;
import org.icddrb.dhis.android.sdk.job.NetworkJob;
import org.icddrb.dhis.android.sdk.network.APIException;
import org.icddrb.dhis.android.sdk.network.Credentials;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.models.UserAccount;
import org.icddrb.dhis.android.sdk.persistence.preferences.ResourceType;

public final class DhisService extends Service {
    public static final int CONFIRM_USER = 2;
    public static final int LOG_IN = 1;
    public static final int LOG_OUT = 3;
    public static final int SYNC_DASHBOARDS = 5;
    public static final int SYNC_DASHBOARD_CONTENT = 6;
    public static final int SYNC_INTERPRETATIONS = 7;
    private final IBinder mBinder = new ServiceBinder();

    public class ServiceBinder extends Binder {
        public DhisService getService() {
            return DhisService.this;
        }
    }

    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return 1;
    }

    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    public static void logInUser(final HttpUrl serverUrl, final Credentials credentials) {
        JobExecutor.enqueueJob(new NetworkJob<UserAccount>(1, ResourceType.USERS) {
            public UserAccount execute() throws APIException {
                return DhisController.logInUser(serverUrl, credentials);
            }
        });
    }

    public static void logOutUser(final Context context, final boolean hardLogout) {
        JobExecutor.enqueueJob(new Job<UiEvent>(3) {
            public UiEvent inBackground() {
                DhisController.logOutUser(context, hardLogout);
                return new UiEvent(UiEventType.USER_LOG_OUT);
            }

            public void onFinish(UiEvent result) {
                Dhis2Application.getEventBus().post(result);
            }
        });
    }

    public static void confirmUser(final Credentials credentials) {
        JobExecutor.enqueueJob(new NetworkJob<UserAccount>(2, ResourceType.USERS) {
            public UserAccount execute() throws APIException {
                return DhisController.confirmUser(credentials);
            }
        });
    }

    public static void syncDashboardContents() {
        JobExecutor.enqueueJob(new NetworkJob<Object>(6, ResourceType.DASHBOARDS_CONTENT) {
            public Object execute() throws APIException {
                return new Object();
            }
        });
    }

    public static void syncDashboardsAndContent() {
        JobExecutor.enqueueJob(new NetworkJob<Object>(5, ResourceType.DASHBOARDS) {
            public Object execute() throws APIException {
                return new Object();
            }
        });
    }

    public static void syncDashboards() {
        JobExecutor.enqueueJob(new NetworkJob<Object>(5, ResourceType.DASHBOARDS) {
            public Object execute() throws APIException {
                return new Object();
            }
        });
    }

    public static void forceSynchronize(final Context context) {
        JobExecutor.enqueueJob(new NetworkJob<Object>(0, null) {
            public Object execute() throws APIException {
                DhisController.forceSynchronize(context);
                return new Object();
            }
        });
    }

    public static void synchronize(final Context context, final SyncStrategy syncStrategy) {
        JobExecutor.enqueueJob(new NetworkJob<Object>(0, null) {
            public Object execute() throws APIException {
                DhisController.synchronize(context, syncStrategy);
                return new Object();
            }
        });
    }

    public static void synchronizeRemotelyDeletedData(final Context context) {
        JobExecutor.enqueueJob(new NetworkJob<Object>(0, null) {
            public Object execute() throws APIException {
                DhisController.syncRemotelyDeletedData(context);
                return new Object();
            }
        });
    }

    public static Job loadData(final Context context) {
        return JobExecutor.enqueueJob(new NetworkJob<Object>(0, null) {
            public Object execute() throws APIException {
                DhisController.loadData(context, SyncStrategy.DOWNLOAD_ONLY_NEW);
                return new Object();
            }
        });
    }

    public static void sendData() {
        JobExecutor.enqueueJob(new NetworkJob<Object>(0, null) {
            public Object execute() throws APIException {
                DhisController.sendData();
                return new Object();
            }
        });
    }

    public static void loadInitialData(final Context context) {
        JobExecutor.enqueueJob(new NetworkJob<Object>(0, null) {
            public Object execute() throws APIException {
                LoadingController.loadInitialData(context, DhisController.getInstance().getDhisApi());
                return new Object();
            }
        });
    }

    public static void syncInterpretations() {
        JobExecutor.enqueueJob(new NetworkJob<Object>(7, ResourceType.INTERPRETATIONS) {
            public Object execute() throws APIException {
                return new Object();
            }
        });
    }

    public boolean isJobRunning(int jobId) {
        return JobExecutor.isJobRunning(jobId);
    }
}
