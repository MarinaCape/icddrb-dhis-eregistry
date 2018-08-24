package org.icddrb.dhis.android.sdk.ui.activities;

import android.support.v4.app.Fragment;

public interface INavigationHandler {
    void onBackPressed();

    void setBackPressedListener(OnBackPressedListener onBackPressedListener);

    void switchFragment(Fragment fragment, String str, boolean z);
}
