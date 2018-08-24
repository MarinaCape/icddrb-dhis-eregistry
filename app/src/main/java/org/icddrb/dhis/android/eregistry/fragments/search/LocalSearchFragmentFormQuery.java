package org.icddrb.dhis.android.eregistry.fragments.search;

import android.content.Context;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

public class LocalSearchFragmentFormQuery implements Query<LocalSearchFragmentForm> {
    private String TAG = getClass().getSimpleName();
    private String[] allowedRows = new String[]{"Couple number", "Household number", "Full name", "FWA Name", "Union Name", "Husband's Name", "Phone number", "Village name (English)", "Alternate family mobile number", "Birth date"};
    private String orgUnitId;
    private String programId;

    public LocalSearchFragmentFormQuery(String orgUnitId, String programId) {
        this.orgUnitId = orgUnitId;
        this.programId = programId;
    }

    public LocalSearchFragmentForm query(Context context) {
        LocalSearchFragmentForm form = new LocalSearchFragmentForm();
        form.setOrganisationUnitId(this.orgUnitId);
        form.setProgram(this.programId);
        Program program = MetaDataController.getProgram(this.programId);
        if (!(program == null || this.orgUnitId == null)) {
            List<ProgramTrackedEntityAttribute> programAttrs = program.getProgramTrackedEntityAttributes();
            List<TrackedEntityAttributeValue> values = new ArrayList();
            List<Row> dataEntryRows = new ArrayList();
            for (ProgramTrackedEntityAttribute ptea : programAttrs) {
                TrackedEntityAttribute trackedEntityAttribute = ptea.getTrackedEntityAttribute();
                TrackedEntityAttributeValue value = new TrackedEntityAttributeValue();
                value.setTrackedEntityAttributeId(trackedEntityAttribute.getUid());
                values.add(value);
                if (ptea.getMandatory()) {
                    ptea.setMandatory(!ptea.getMandatory());
                }
                if (ValueType.COORDINATE.equals(ptea.getTrackedEntityAttribute().getValueType())) {
                    GpsController.activateGps(context);
                }
                boolean isRadioButton = program.getDataEntryMethod();
                if (!isRadioButton) {
                    isRadioButton = ptea.isRenderOptionsAsRadio();
                }
                if (Arrays.asList(this.allowedRows).contains(trackedEntityAttribute.getName())) {
                    Row row = DataEntryRowFactory.createDataEntryView(ptea.getMandatory(), ptea.getAllowFutureDate(), trackedEntityAttribute.getOptionSet(), trackedEntityAttribute.getName(), value, trackedEntityAttribute.getValueType(), true, false, isRadioButton, context);
                    row.setOrg(null);
                    dataEntryRows.add(row);
                }
            }
            form.setTrackedEntityAttributeValues(values);
            form.setDataEntryRows(dataEntryRows);
        }
        return form;
    }
}
