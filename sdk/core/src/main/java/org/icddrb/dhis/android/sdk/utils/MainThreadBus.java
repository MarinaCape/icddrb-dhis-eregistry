package org.icddrb.dhis.android.sdk.utils;

import android.os.Handler;
import android.os.Looper;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

public class MainThreadBus extends Bus {
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    public MainThreadBus(ThreadEnforcer any) {
        super(any);
    }

    public void post(final Object event) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            super.post(event);
        } else {
            this.mHandler.post(new Runnable() {
                public void run() {
                    super.post(event);
                }
            });
        }
    }
}
