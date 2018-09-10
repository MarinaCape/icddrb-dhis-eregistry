package org.icddrb.dhis.android.sdk.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import org.icddrb.dhis.android.sdk.R;
import org.icddrb.dhis.android.sdk.controllers.DhisController;
import org.icddrb.dhis.android.sdk.services.StartPeriodicSynchronizerService;

public class SplashActivity extends Activity {
    private static final int REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int REQUEST_ACCESS_FINE_STORAGE = 2;
    private static final int REQUEST_ALL_PERMISSIONS = 3;
    private int permissionsRequested = 0;

    /* renamed from: org.icddrb.dhis.android.sdk.ui.activities.SplashActivity$1 */
    class C08661 implements Runnable {
        C08661() {
        }

        public void run() {
            SplashActivity.this.startActivity(new Intent(SplashActivity.this, SplashActivity.this.getNextActivity()));
            SplashActivity.this.finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.activity_splash);
        checkPermissions();
    }

    private Class<? extends Activity> getNextActivity() {
        Class<? extends Activity> nextClass = LoginActivity.class;
        DhisController.getInstance().init();
        if (DhisController.isUserLoggedIn()) {
            try {
                String nextClassName = getPackageManager().getApplicationInfo(getPackageName(), 128).metaData.getString("nextClassName");
                if (nextClassName != null) {
                    try {
                        nextClass = Class.forName(nextClassName);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        return LoginActivity.class;
                    }
                }
            } catch (NameNotFoundException e2) {
                e2.printStackTrace();
                return nextClass;
            }
        }
        return nextClass;
    }

    private void checkPermissions() {
        boolean hasPermissionLocation;
        boolean hasPermissionStorage;
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0) {
            hasPermissionLocation = true;
        } else {
            hasPermissionLocation = false;
        }
        if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            hasPermissionStorage = true;
        } else {
            hasPermissionStorage = false;
        }
        if (!hasPermissionLocation && !hasPermissionStorage) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_FINE_LOCATION", "android.permission.WRITE_EXTERNAL_STORAGE"}, 3);
        } else if (!hasPermissionLocation) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 1);
        } else if (hasPermissionStorage) {
            continueWithNextActivity();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 2);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        this.permissionsRequested++;
        if (this.permissionsRequested == 1) {
            continueWithNextActivity();
        }
    }

    private void continueWithNextActivity() {
        new Handler().postDelayed(new C08661(), 1000);
        startService(new Intent(this, StartPeriodicSynchronizerService.class));
    }
}
