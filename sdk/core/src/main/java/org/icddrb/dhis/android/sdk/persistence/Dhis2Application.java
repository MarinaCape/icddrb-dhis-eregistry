package org.icddrb.dhis.android.sdk.persistence;

import android.app.Activity;
import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.raizlabs.android.dbflow.DatabaseHelperListener;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;
import io.fabric.sdk.android.Fabric;
import org.icddrb.dhis.android.sdk.controllers.DhisController;
import org.icddrb.dhis.android.sdk.controllers.DhisService;
import org.icddrb.dhis.android.sdk.persistence.migrations.MigrationUtil;
import org.icddrb.dhis.android.sdk.utils.MainThreadBus;

public abstract class Dhis2Application extends Application {
    public static Bus bus = new MainThreadBus(ThreadEnforcer.ANY);
    public static DhisController dhisController;

    /* renamed from: org.icddrb.dhis.android.sdk.persistence.Dhis2Application$1 */
    class C08611 implements DatabaseHelperListener {
        C08611() {
        }

        public void onOpen(SQLiteDatabase database) {
        }

        public void onCreate(SQLiteDatabase database) {
        }

        public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
            MigrationUtil.setDatabase(database);
            if (newVersion > oldVersion && DhisController.isUserLoggedIn()) {
                DhisService.forceSynchronize(Dhis2Application.this.getApplicationContext());
            }
        }
    }

    public abstract Class<? extends Activity> getMainActivity();

    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
        dhisController = new DhisController(this);
        bus.register(dhisController);
        Stetho.initializeWithDefaults(this);
        Fabric.with(this, new Crashlytics());
        FlowManager.setDatabaseListener(Dhis2Database.NAME, new C08611());
    }

    public void onTerminate() {
        super.onTerminate();
        FlowManager.destroy();
    }

    public static Bus getEventBus() {
        return bus;
    }
}
