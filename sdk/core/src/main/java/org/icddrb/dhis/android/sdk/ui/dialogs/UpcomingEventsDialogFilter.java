package org.icddrb.dhis.android.sdk.ui.dialogs;

import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import org.icddrb.dhis.android.sdk.R;
import org.icddrb.dhis.android.sdk.persistence.loaders.DbLoader;
import org.icddrb.dhis.android.sdk.ui.dialogs.AutoCompleteDialogAdapter.OptionAdapterValue;
import org.icddrb.dhis.android.sdk.ui.dialogs.AutoCompleteDialogFragment.OnOptionSelectedListener;

public class UpcomingEventsDialogFilter extends AutoCompleteDialogFragment implements LoaderCallbacks<List<OptionAdapterValue>> {
    public static final int ID = 120000101;
    private static final int LOADER_ID = 2133332121;

    public enum Type {
        ACTIVE,
        UPCOMING,
        OVERDUE
    }

    public static UpcomingEventsDialogFilter newInstance(OnOptionSelectedListener optionSelectedListener) {
        UpcomingEventsDialogFilter upcomingEventsDialogFilter = new UpcomingEventsDialogFilter();
        upcomingEventsDialogFilter.setOnOptionSetListener(optionSelectedListener);
        return upcomingEventsDialogFilter;
    }

    public Loader<List<OptionAdapterValue>> onCreateLoader(int id, Bundle args) {
        if (LOADER_ID != id || !isAdded()) {
            return null;
        }
        return new DbLoader(getActivity().getBaseContext(), new ArrayList(), new UpcomingEventsDialogFilterQuery());
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setDialogLabel(R.string.choose_filter);
        setDialogId(ID);
        this.mProgressBar.setVisibility(0);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, getArguments(), this);
    }

    public void onLoadFinished(Loader<List<OptionAdapterValue>> loader, List<OptionAdapterValue> data) {
        if (loader.getId() == LOADER_ID) {
            getAdapter().swapData(data);
            this.mProgressBar.setVisibility(8);
        }
    }

    public void onLoaderReset(Loader<List<OptionAdapterValue>> loader) {
        getAdapter().swapData(null);
    }
}
