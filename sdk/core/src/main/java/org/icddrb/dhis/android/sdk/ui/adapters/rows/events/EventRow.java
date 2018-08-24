package org.icddrb.dhis.android.sdk.ui.adapters.rows.events;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public interface EventRow {
    long getId();

    View getView(LayoutInflater layoutInflater, View view, ViewGroup viewGroup);

    int getViewType();

    boolean isEnabled();
}
