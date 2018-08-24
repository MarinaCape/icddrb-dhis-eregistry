package org.icddrb.dhis.android.eregistry.ui.rows.programoverview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public interface ProgramStageRow {
    View getView(LayoutInflater layoutInflater, View view, ViewGroup viewGroup);

    boolean hasFailed();

    boolean isSynchronized();

    void setHasFailed(boolean z);

    void setSynchronized(boolean z);
}
