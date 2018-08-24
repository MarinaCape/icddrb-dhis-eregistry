package org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.autocompleterow;

import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;

class AutoCompleteDropDownButtonListener implements OnClickListener {
    private FragmentManager fragmentManager;
    private AutoCompleteOnOptionSelectedListener listener;
    private String optionSetId;

    AutoCompleteDropDownButtonListener() {
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void setOptionSetId(String optionSetId) {
        this.optionSetId = optionSetId;
    }

    public void setListener(AutoCompleteOnOptionSelectedListener listener) {
        this.listener = listener;
    }

    public AutoCompleteOnOptionSelectedListener getListener() {
        return this.listener;
    }

    public void onClick(View v) {
        OptionDialogFragment.newInstance(this.optionSetId, this.listener).show(this.fragmentManager);
    }
}
