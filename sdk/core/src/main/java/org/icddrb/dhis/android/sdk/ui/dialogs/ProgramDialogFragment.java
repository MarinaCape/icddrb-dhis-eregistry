package org.icddrb.dhis.android.sdk.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import com.raizlabs.android.dbflow.structure.Model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.icddrb.dhis.android.sdk.R;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.persistence.loaders.DbLoader;
import org.icddrb.dhis.android.sdk.persistence.loaders.Query;
import org.icddrb.dhis.android.sdk.persistence.models.OrganisationUnitProgramRelationship;
import org.icddrb.dhis.android.sdk.persistence.models.Program;
import org.icddrb.dhis.android.sdk.ui.dialogs.AutoCompleteDialogAdapter.OptionAdapterValue;
import org.icddrb.dhis.android.sdk.ui.dialogs.AutoCompleteDialogFragment.OnOptionSelectedListener;
import org.icddrb.dhis.android.sdk.utils.api.ProgramType;

public class ProgramDialogFragment extends AutoCompleteDialogFragment implements LoaderCallbacks<List<OptionAdapterValue>> {
    public static final int ID = 921345;
    private static final int LOADER_ID = 1;
    private static final String ORGANISATIONUNITID = "organisationUnitId";
    private static final String PROGRAMTYPE = "programType";

    static class ProgramQuery implements Query<List<OptionAdapterValue>> {
        private final ProgramType[] mKinds;
        private final String mOrgUnitId;

        public ProgramQuery(String orgUnitId, ProgramType[] kinds) {
            this.mOrgUnitId = orgUnitId;
            this.mKinds = kinds;
        }

        public List<OptionAdapterValue> query(Context context) {
            List<Program> programs = MetaDataController.getProgramsForOrganisationUnit(this.mOrgUnitId, this.mKinds);
            List<OptionAdapterValue> values = new ArrayList();
            if (!(programs == null || programs.isEmpty())) {
                for (Program program : programs) {
                    values.add(new OptionAdapterValue(program.getUid(), program.getName()));
                }
            }
            Collections.sort(values);
            return values;
        }
    }

    public static ProgramDialogFragment newInstance(OnOptionSelectedListener listener, String orgUnitId, ProgramType... programKinds) {
        ProgramDialogFragment fragment = new ProgramDialogFragment();
        Bundle args = new Bundle();
        args.putString("organisationUnitId", orgUnitId);
        if (programKinds != null) {
            String[] programKindStrings = new String[programKinds.length];
            for (int i = 0; i < programKinds.length; i++) {
                programKindStrings[i] = programKinds[i].name();
            }
            args.putStringArray("programType", programKindStrings);
        }
        fragment.setArguments(args);
        fragment.setOnOptionSetListener(listener);
        return fragment;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setDialogLabel(R.string.dialog_programs);
        setDialogId(ID);
        this.mProgressBar.setVisibility(0);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(1, getArguments(), this);
    }

    public Loader<List<OptionAdapterValue>> onCreateLoader(int id, Bundle args) {
        if (1 != id || !isAdded()) {
            return null;
        }
        String organisationUnitId = args.getString("organisationUnitId");
        String[] kinds = args.getStringArray("programType");
        ProgramType[] types = null;
        if (kinds != null) {
            types = new ProgramType[kinds.length];
            for (int i = 0; i < kinds.length; i++) {
                types[i] = ProgramType.valueOf(kinds[i]);
            }
        }
        List<Class<? extends Model>> modelsToTrack = new ArrayList();
        modelsToTrack.add(Program.class);
        modelsToTrack.add(OrganisationUnitProgramRelationship.class);
        return new DbLoader(getActivity().getBaseContext(), modelsToTrack, new ProgramQuery(organisationUnitId, types));
    }

    public void onLoadFinished(Loader<List<OptionAdapterValue>> loader, List<OptionAdapterValue> data) {
        if (1 == loader.getId()) {
            getAdapter().swapData(data);
            if (MetaDataController.isDataLoaded(getActivity())) {
                this.mProgressBar.setVisibility(8);
            }
            if (data.isEmpty()) {
                setNoItemsTextViewVisibility(0);
                setTextToNoItemsTextView(getString(R.string.no_programs));
            }
        }
    }

    public void onLoaderReset(Loader<List<OptionAdapterValue>> loader) {
        getAdapter().swapData(null);
    }
}
