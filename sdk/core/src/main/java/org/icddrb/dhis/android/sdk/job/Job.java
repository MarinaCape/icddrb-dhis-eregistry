package org.icddrb.dhis.android.sdk.job;

import android.os.AsyncTask;
import org.icddrb.dhis.android.sdk.utils.Preconditions;

public abstract class Job<T> extends AsyncTask<Void, Void, T> implements IJob<T> {
    private JobExecutor mJobExecutor;
    private final int mJobId;

    public Job(int jobId) {
        this.mJobId = ((Integer) Preconditions.isNull(Integer.valueOf(jobId), "Job ID must not be null")).intValue();
    }

    public final void onBind(JobExecutor executor) {
        this.mJobExecutor = (JobExecutor) Preconditions.isNull(executor, "JobExecutor must not be null");
    }

    public final void onPreExecute() {
        onStart();
    }

    public void onStart() {
    }

    public final T doInBackground(Void... params) {
        return inBackground();
    }

    public final void onPostExecute(T result) {
        onFinish(result);
        this.mJobExecutor.onFinishJob(this);
    }

    public void onFinish(T t) {
    }

    public final void onUnbind() {
        this.mJobExecutor = null;
    }

    public final int getJobId() {
        return this.mJobId;
    }
}
