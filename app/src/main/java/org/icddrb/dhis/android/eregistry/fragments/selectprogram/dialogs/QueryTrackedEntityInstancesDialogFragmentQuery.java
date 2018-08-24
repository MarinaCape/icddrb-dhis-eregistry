package org.icddrb.dhis.android.eregistry.fragments.selectprogram.dialogs;

import android.content.Context;
import android.util.Log;
import java.util.ArrayList;
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

public class QueryTrackedEntityInstancesDialogFragmentQuery implements Query<QueryTrackedEntityInstancesDialogFragmentForm> {
    public static final String TAG = QueryTrackedEntityInstancesDialogFragmentQuery.class.getSimpleName();
    private String orgUnit;
    private String programId;

    public QueryTrackedEntityInstancesDialogFragmentQuery(String orgUnit, String programId) {
        this.programId = programId;
        this.orgUnit = orgUnit;
    }

    public QueryTrackedEntityInstancesDialogFragmentForm query(Context context) {
        QueryTrackedEntityInstancesDialogFragmentForm form = new QueryTrackedEntityInstancesDialogFragmentForm();
        form.setOrganisationUnit(this.orgUnit);
        form.setProgram(this.programId);
        Log.d(TAG, this.orgUnit + this.programId);
        Program program = MetaDataController.getProgram(this.programId);
        if (!(program == null || this.orgUnit == null)) {
            List<ProgramTrackedEntityAttribute> programAttrs = program.getProgramTrackedEntityAttributes();
            List<TrackedEntityAttributeValue> values = new ArrayList();
            List<Row> dataEntryRows = new ArrayList();
            for (ProgramTrackedEntityAttribute ptea : programAttrs) {
                TrackedEntityAttribute trackedEntityAttribute = ptea.getTrackedEntityAttribute();
                TrackedEntityAttributeValue value = new TrackedEntityAttributeValue();
                value.setTrackedEntityAttributeId(trackedEntityAttribute.getUid());
                values.add(value);
                if (ValueType.COORDINATE.equals(ptea.getTrackedEntityAttribute().getValueType())) {
                    GpsController.activateGps(context);
                }
                boolean isRadioButton = program.getDataEntryMethod();
                if (!isRadioButton) {
                    isRadioButton = ptea.isRenderOptionsAsRadio();
                }
                Row row = DataEntryRowFactory.createDataEntryView(ptea.getMandatory(), ptea.getAllowFutureDate(), trackedEntityAttribute.getOptionSet(), trackedEntityAttribute.getName(), value, trackedEntityAttribute.getValueType(), true, false, isRadioButton, context);
                row.setOrg(this.orgUnit);
                dataEntryRows.add(row);
            }
            form.setTrackedEntityAttributeValues(values);
            form.setDataEntryRows(dataEntryRows);
        }
        return form;
    }
}
