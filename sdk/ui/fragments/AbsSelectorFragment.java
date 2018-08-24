package org.icddrb.dhis.client.sdk.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

public abstract class AbsSelectorFragment extends BaseFragment {
    private FrameLayout mItemListFrameLayout;
    private FrameLayout mPickerFrameLayout;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onPause() {
        super.onPause();
    }

    public void onResume() {
        super.onResume();
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
    }

    public void onDetach() {
        super.onDetach();
    }
}
