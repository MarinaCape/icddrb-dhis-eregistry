package org.icddrb.dhis.android.sdk.job;

import android.os.AsyncTask;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import org.icddrb.dhis.android.sdk.utils.Preconditions;

public final class JobExecutor {
    private static final int MAX_RUNNING_JOBS = 64;
    private static final String TAG = JobExecutor.class.getSimpleName();
    private static JobExecutor mJobExecutor;
    private Map<Integer, Job> mPendingJobIds = new HashMap();
    private Queue<Job> mPendingJobs = new LinkedList();
    private Map<Integer, Job> mRunningJobIds = new HashMap();
    private Queue<Job> mRunningJobs = new LinkedList();

    private JobExecutor() {
    }

    public static JobExecutor getInstance() {
        if (mJobExecutor == null) {
            mJobExecutor = new JobExecutor();
        }
        return mJobExecutor;
    }

    private static <T> void run(AsyncTask<Void, Void, T> task) {
        task.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new Void[0]);
    }

    public static <T> Job enqueueJob(Job<T> job) {
        Preconditions.isNull(job, "Job object must not be null");
        JobExecutor executor = getInstance();
        if (!executor.isJobEnqueued(job.getJobId())) {
            executor.enqueuePendingJob(job);
            executor.executeNextJob();
        }
        return job;
    }

    public static boolean isJobRunning(int jobId) {
        return getInstance().isJobEnqueued(jobId);
    }

    private void executeNextJob() {
        if (this.mRunningJobIds.size() < 64 && this.mPendingJobs.size() > 0) {
            Job job = (Job) this.mPendingJobs.peek();
            dequeuePendingJob(job);
            onStartJob(job);
        }
    }

    <T> void onStartJob(Job<T> job) {
        bindJob(job);
        run(job);
    }

    void onFinishJob(Job job) {
        unbindJob(job);
        executeNextJob();
    }

    <T> void bindJob(Job<T> job) {
        job.onBind(this);
        enqueueRunningJob(job);
    }

    <T> void unbindJob(Job<T> job) {
        job.onUnbind();
        dequeueRunningJob(job);
    }

    private void enqueuePendingJob(Job job) {
        this.mPendingJobIds.put(Integer.valueOf(job.getJobId()), job);
        this.mPendingJobs.add(job);
    }

    private void dequeuePendingJob(Job job) {
        this.mPendingJobIds.remove(Integer.valueOf(job.getJobId()));
        this.mPendingJobs.remove(job);
    }

    private void enqueueRunningJob(Job job) {
        this.mRunningJobIds.put(Integer.valueOf(job.getJobId()), job);
        this.mRunningJobs.add(job);
    }

    public void dequeueRunningJob(Job job) {
        this.mRunningJobIds.remove(Integer.valueOf(job.getJobId()));
        this.mRunningJobs.remove(job);
    }

    private boolean isJobEnqueued(int jobId) {
        return (this.mPendingJobIds.get(Integer.valueOf(jobId)) == null && this.mRunningJobIds.get(Integer.valueOf(jobId)) == null) ? false : true;
    }
}
