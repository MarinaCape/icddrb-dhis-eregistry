package org.icddrb.dhis.android.eregistry.fragments.programoverview.registerrelationshipdialogfragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import com.raizlabs.android.dbflow.structure.Model;
import java.util.ArrayList;
import java.util.List;
import org.icddrb.dhis.android.eregistry.C0773R;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.persistence.loaders.DbLoader;
import org.icddrb.dhis.android.sdk.persistence.loaders.Query;
import org.icddrb.dhis.android.sdk.persistence.models.Program;
import org.icddrb.dhis.android.sdk.persistence.models.RelationshipType;
import org.icddrb.dhis.android.sdk.ui.adapters.SimpleAdapter.ExtractStringCallback;
import org.icddrb.dhis.android.sdk.ui.dialogs.AutoCompleteDialogAdapter.OptionAdapterValue;
import org.icddrb.dhis.android.sdk.ui.dialogs.AutoCompleteDialogFragment;
import org.icddrb.dhis.android.sdk.ui.dialogs.AutoCompleteDialogFragment.OnOptionSelectedListener;

public class RelationshipTypesDialogFragment extends AutoCompleteDialogFragment implements LoaderCallbacks<List<OptionAdapterValue>> {
    public static final int ID = 921345130;
    private static final int LOADER_ID = 1;

    public interface OnProgramSetListener {
        void onProgramSelected(String str, String str2);
    }

    static class RelationshipTypesQuery implements Query<List<OptionAdapterValue>> {
        public List<OptionAdapterValue> query(Context context) {
            List<RelationshipType> relationshipTypes = MetaDataController.getRelationshipTypes();
            List<OptionAdapterValue> values = new ArrayList();
            if (!(relationshipTypes == null || relationshipTypes.isEmpty())) {
                for (RelationshipType relationshipType : relationshipTypes) {
                    values.add(new OptionAdapterValue(relationshipType.getUid(), relationshipType.getName()));
                }
            }
            return values;
        }
    }

    static class StringExtractor implements ExtractStringCallback<Program> {
        StringExtractor() {
        }

        public String getString(Program object) {
            return object.getName();
        }
    }

    public static RelationshipTypesDialogFragment newInstance(OnOptionSelectedListener listener) {
        RelationshipTypesDialogFragment fragment = new RelationshipTypesDialogFragment();
        fragment.setArguments(new Bundle());
        fragment.setOnOptionSetListener(listener);
        return fragment;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setDialogLabel(getString(C0773R.string.relationships));
        setDialogId(ID);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(1, getArguments(), this);
    }

    public Loader<List<OptionAdapterValue>> onCreateLoader(int id, Bundle args) {
        if (1 != id || !isAdded()) {
            return null;
        }
        List<Class<? extends Model>> modelsToTrack = new ArrayList();
        modelsToTrack.add(Program.class);
        return new DbLoader(getActivity().getBaseContext(), modelsToTrack, new RelationshipTypesQuery());
    }

    public void onLoadFinished(Loader<List<OptionAdapterValue>> loader, List<OptionAdapterValue> data) {
        if (1 == loader.getId()) {
            getAdapter().swapData(data);
        }
    }

    public void onLoaderReset(Loader<List<OptionAdapterValue>> loader) {
        getAdapter().swapData(null);
    }
}
