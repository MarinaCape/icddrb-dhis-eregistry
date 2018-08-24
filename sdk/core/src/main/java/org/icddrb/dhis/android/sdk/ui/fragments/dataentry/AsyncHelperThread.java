package org.icddrb.dhis.android.sdk.ui.fragments.dataentry;

public abstract class AsyncHelperThread extends Thread {
    protected boolean doWork = false;
    private boolean killed = false;
    private boolean working = false;

    protected abstract void work();

    public void run() {
        while (!this.killed) {
            idle();
            doWork();
        }
    }

    private void doWork() {
        this.working = true;
        this.doWork = false;
        work();
        this.working = false;
    }

    private void idle() {
        while (!this.doWork) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void kill() {
        this.killed = true;
        this.doWork = false;
        while (this.working) {
            Thread.yield();
        }
    }

    public void schedule() {
        if (!this.killed) {
            this.doWork = true;
        }
    }

    public boolean isKilled() {
        return this.killed;
    }
}
