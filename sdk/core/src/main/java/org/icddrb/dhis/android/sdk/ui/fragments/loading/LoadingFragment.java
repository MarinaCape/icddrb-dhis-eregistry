package org.icddrb.dhis.android.sdk.ui.fragments.loading;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.squareup.otto.Subscribe;
import org.icddrb.dhis.android.sdk.R;
import org.icddrb.dhis.android.sdk.events.LoadingMessageEvent;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;

public class LoadingFragment extends Fragment {
    public static final String TAG = LoadingFragment.class.getSimpleName();
    private TextView mLoadingMessage;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Dhis2Application.bus.register(this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.mLoadingMessage = (TextView) view.findViewById(R.id.fragment_loading_text);
    }

    public void setText(CharSequence text) {
        if (this.mLoadingMessage != null) {
            this.mLoadingMessage.setText(text);
        } else {
            Log.d(TAG, "LoadingMessage is null");
        }
    }

    @Subscribe
    public void onLoadingMessageEvent(final LoadingMessageEvent event) {
        Log.d(TAG, "Message received" + event.message);
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                LoadingFragment.this.setText(event.message);
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
        Dhis2Application.bus.unregister(this);
    }
}
