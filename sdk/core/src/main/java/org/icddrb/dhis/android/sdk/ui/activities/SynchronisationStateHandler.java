package org.icddrb.dhis.android.sdk.ui.activities;

public class SynchronisationStateHandler {
    private static SynchronisationStateHandler mInstance;
    private OnSynchronisationStateListener mListener;
    private boolean mState;

    public interface OnSynchronisationStateListener {
        void stateChanged();
    }

    private SynchronisationStateHandler() {
    }

    public static SynchronisationStateHandler getInstance() {
        if (mInstance == null) {
            mInstance = new SynchronisationStateHandler();
        }
        return mInstance;
    }

    public void setListener(OnSynchronisationStateListener listener) {
        this.mListener = listener;
    }

    public void removeListener() {
        this.mListener = null;
    }

    public void changeState(boolean state) {
        if (this.mListener != null) {
            this.mState = state;
            notifyStateChange();
        }
    }

    public boolean getState() {
        return this.mState;
    }

    private void notifyStateChange() {
        this.mListener.stateChanged();
    }
}
