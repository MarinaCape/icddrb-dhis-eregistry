package org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry;

import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public interface DataEntryRow {
    Integer getValidationError();

    View getView(FragmentManager fragmentManager, LayoutInflater layoutInflater, View view, ViewGroup viewGroup);

    int getViewType();
}
