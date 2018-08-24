package org.icddrb.dhis.android.sdk.job;

public interface IJob<T> {
    T inBackground();

    void onFinish(T t);

    void onStart();
}
