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
import org.icddrb.dhis.android.sdk.C0845R;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.persistence.loaders.DbLoader;
import org.icddrb.dhis.android.sdk.persistence.loaders.Query;
import org.icddrb.dhis.android.sdk.persistence.models.OrganisationUnit;
import org.icddrb.dhis.android.sdk.persistence.models.OrganisationUnitProgramRelationship;
import org.icddrb.dhis.android.sdk.persistence.models.Program;
import org.icddrb.dhis.android.sdk.ui.dialogs.AutoCompleteDialogAdapter.OptionAdapterValue;
import org.icddrb.dhis.android.sdk.ui.dialogs.AutoCompleteDialogFragment.OnOptionSelectedListener;
import org.icddrb.dhis.android.sdk.ui.dialogs.OrgUnitDialogFragmentForm.Error;
import org.icddrb.dhis.android.sdk.utils.api.ProgramType;

public class OrgUnitDialogFragment extends AutoCompleteDialogFragment implements LoaderCallbacks<OrgUnitDialogFragmentForm> {
    public static final int ID = 450123;
    private static final int LOADER_ID = 1;
    private static final String PROGRAMTYPE = "programType";

    static class OrgUnitQuery implements Query<OrgUnitDialogFragmentForm> {
        private final ProgramType[] kinds;

        public OrgUnitQuery(ProgramType... kinds) {
            this.kinds = kinds;
        }

        public OrgUnitDialogFragmentForm query(Context context) {
            OrgUnitDialogFragmentForm mForm = new OrgUnitDialogFragmentForm();
            List<OrganisationUnit> orgUnits = queryUnits();
            List<OptionAdapterValue> values = new ArrayList();
            if (orgUnits.isEmpty()) {
                mForm.setType(Error.NO_ASSIGNED_ORGANISATION_UNITS);
            } else {
                for (OrganisationUnit orgUnit : orgUnits) {
                    if (hasPrograms(orgUnit.getId(), this.kinds)) {
                        values.add(new OptionAdapterValue(orgUnit.getId(), orgUnit.getLabel()));
                    } else {
                        mForm.setType(Error.NO_PROGRAMS_TO_ORGANSATION_UNIT);
                    }
                }
            }
            if (!values.isEmpty()) {
                Collections.sort(values);
                mForm.setType(Error.NONE);
            }
            mForm.setOrganisationUnits(orgUnits);
            mForm.setOptionAdapterValueList(values);
            return mForm;
        }

        private List<OrganisationUnit> queryUnits() {
            return MetaDataController.getAssignedOrganisationUnits();
        }

        private boolean hasPrograms(String unitId, ProgramType... kinds) {
            List<Program> programs = MetaDataController.getProgramsForOrganisationUnit(unitId, kinds);
            return (programs == null || programs.isEmpty()) ? false : true;
        }
    }

    public static OrgUnitDialogFragment newInstance(OnOptionSelectedListener listener, ProgramType... programKinds) {
        OrgUnitDialogFragment fragment = new OrgUnitDialogFragment();
        Bundle args = new Bundle();
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
        setDialogLabel(C0845R.string.dialog_organisation_units);
        setDialogId(ID);
        this.mProgressBar.setVisibility(0);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(1, getArguments(), this);
    }

    public Loader<OrgUnitDialogFragmentForm> onCreateLoader(int id, Bundle args) {
        if (1 != id || !isAdded()) {
            return null;
        }
        List<Class<? extends Model>> modelsToTrack = new ArrayList();
        modelsToTrack.add(OrganisationUnitProgramRelationship.class);
        modelsToTrack.add(OrganisationUnit.class);
        modelsToTrack.add(Program.class);
        String[] kinds = args.getStringArray("programType");
        ProgramType[] types = null;
        if (kinds != null) {
            types = new ProgramType[kinds.length];
            for (int i = 0; i < kinds.length; i++) {
                types[i] = ProgramType.valueOf(kinds[i]);
            }
        }
        return new DbLoader(getActivity().getBaseContext(), modelsToTrack, new OrgUnitQuery(types));
    }

    public void onLoadFinished(Loader<OrgUnitDialogFragmentForm> loader, OrgUnitDialogFragmentForm data) {
        if (loader.getId() == 1) {
            getAdapter().swapData(data.getOptionAdapterValueList());
            if (MetaDataController.isDataLoaded(getActivity())) {
                this.mProgressBar.setVisibility(8);
                if (data.getType().equals(Error.NO_ASSIGNED_ORGANISATION_UNITS)) {
                    setNoItemsTextViewVisibility(0);
                    setTextToNoItemsTextView(getString(C0845R.string.no_organisation_units));
                } else if (data.getType().equals(Error.NO_PROGRAMS_TO_ORGANSATION_UNIT)) {
                    setNoItemsTextViewVisibility(0);
                    setTextToNoItemsTextView(getString(C0845R.string.no_programs));
                } else {
                    setNoItemsTextViewVisibility(8);
                    setTextToNoItemsTextView("");
                }
            }
        }
    }

    public void onLoaderReset(Loader<OrgUnitDialogFragmentForm> loader) {
        getAdapter().swapData(null);
    }
}
