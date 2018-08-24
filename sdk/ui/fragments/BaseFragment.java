package org.icddrb.dhis.client.sdk.ui.fragments;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import org.icddrb.dhis.client.sdk.ui.activities.BaseActivity;
import org.icddrb.dhis.client.sdk.ui.activities.NavigationCallback;
import org.icddrb.dhis.client.sdk.ui.activities.OnBackPressedCallback;
import org.icddrb.dhis.client.sdk.ui.activities.OnBackPressedFromFragmentCallback;

public class BaseFragment extends Fragment implements OnBackPressedCallback {
    private NavigationCallback navigationCallback;
    private OnBackPressedFromFragmentCallback onBackPressedFromFragmentCallback;

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NavigationCallback) {
            this.navigationCallback = (NavigationCallback) context;
        }
        if (context instanceof BaseActivity) {
            ((BaseActivity) context).setOnBackPressedCallback(this);
        }
        if (context instanceof OnBackPressedFromFragmentCallback) {
            this.onBackPressedFromFragmentCallback = (OnBackPressedFromFragmentCallback) context;
        }
    }

    public void onDetach() {
        this.navigationCallback = null;
        this.onBackPressedFromFragmentCallback = null;
        if (getActivity() != null && (getActivity() instanceof BaseActivity)) {
            ((BaseActivity) getActivity()).setOnBackPressedCallback(null);
        }
        super.onDetach();
    }

    protected void toggleNavigationDrawer() {
        if (this.navigationCallback != null) {
            this.navigationCallback.toggleNavigationDrawer();
        }
    }

    @Nullable
    protected Toolbar getParentToolbar() {
        if (getParentFragment() == null || !(getParentFragment() instanceof WrapperFragment)) {
            return null;
        }
        return ((WrapperFragment) getParentFragment()).getToolbar();
    }

    public boolean onBackPressed() {
        if (this.onBackPressedFromFragmentCallback == null) {
            return true;
        }
        this.onBackPressedFromFragmentCallback.onBackPressedFromFragment();
        return false;
    }
}
