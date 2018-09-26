package org.icddrb.dhis.android.eregistry.fragments.search;

import android.content.Context;
import android.util.Log;

import org.icddrb.dhis.android.sdk.controllers.GpsController;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.persistence.loaders.Query;
import org.icddrb.dhis.android.sdk.persistence.models.Program;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramTrackedEntityAttribute;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttribute;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.DataEntryRowFactory;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.Row;
import org.icddrb.dhis.android.sdk.utils.api.ValueType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocalSearchFragmentFormQuery implements Query<LocalSearchFragmentForm> {
    private String TAG = this.getClass().getSimpleName();
    private String orgUnitId;
    private String programId;
    /*private String[] allowedRows = {"Couple number", "Household number", "Full name", "FWA Name",
            "Union Name", "Husband's Name", "Phone number", "Village name (English)",
            "Alternate family mobile number","Birth date"};*/
    private String[] allowedRows = {"ACroxpb6PGX", "pspz23dzwmO", "QWTcaK2mXeD", "xFSghu1nCGg",
            "OhmSPuuHj53", "etJ8MaVKH2g", "KSSF2lnMKca", "N8skKU3roph",
            "YFz51ppXbLr","JBLD3c3KJGX"};
    public LocalSearchFragmentFormQuery(String orgUnitId, String programId) {
        this.orgUnitId = orgUnitId;
        this.programId = programId;
    }

    @Override
    public LocalSearchFragmentForm query(Context context) {
        LocalSearchFragmentForm form = new LocalSearchFragmentForm();
        form.setOrganisationUnitId(orgUnitId);
        form.setProgram(programId);

        Log.d(TAG, orgUnitId + programId);

        Program program = MetaDataController.getProgram(programId);
        if (program == null || orgUnitId == null) {
            return form;
        }
        List<ProgramTrackedEntityAttribute> programAttrs =
                program.getProgramTrackedEntityAttributes();
        List<TrackedEntityAttributeValue> values = new ArrayList<>();
        List<Row> dataEntryRows = new ArrayList<>();
        for (ProgramTrackedEntityAttribute ptea : programAttrs) {
            TrackedEntityAttribute trackedEntityAttribute = ptea.getTrackedEntityAttribute();
            TrackedEntityAttributeValue value = new TrackedEntityAttributeValue();
            value.setTrackedEntityAttributeId(trackedEntityAttribute.getUid());
            values.add(value);

            //System.out.println("Norway - tea: "+trackedEntityAttribute.getName() + " va: "+value.getValue());

            if (ptea.getMandatory()) {
                ptea.setMandatory(
                        !ptea.getMandatory()); // HACK to skip mandatory fields in search form
            }
            if (ValueType.COORDINATE.equals(ptea.getTrackedEntityAttribute().getValueType())) {
                GpsController.activateGps(context);
            }
            boolean isRadioButton = program.getDataEntryMethod();
            if(!isRadioButton){
                isRadioButton = ptea.isRenderOptionsAsRadio();
            }

            if (Arrays.asList(allowedRows).contains(trackedEntityAttribute.getUid())) {
                Row row = DataEntryRowFactory.createDataEntryView(ptea.getMandatory(),
                        ptea.getAllowFutureDate(), trackedEntityAttribute.getOptionSet(),
                        trackedEntityAttribute.getName(), value, trackedEntityAttribute.getValueType(),
                        true, false, isRadioButton, context);
                dataEntryRows.add(row);
            }
        }
        form.setTrackedEntityAttributeValues(values);
        form.setDataEntryRows(dataEntryRows);
        return form;
    }
}
