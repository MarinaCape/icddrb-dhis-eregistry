package org.icddrb.dhis.client.sdk.ui.activities;

import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    private OnBackPressedCallback onBackPressedCallback;

    public void onBackPressed() {
        if (this.onBackPressedCallback == null) {
            super.onBackPressed();
        } else if (this.onBackPressedCallback.onBackPressed()) {
            super.onBackPressed();
        }
    }

    public void setOnBackPressedCallback(OnBackPressedCallback onBackPressedCallback) {
        this.onBackPressedCallback = onBackPressedCallback;
    }
}
