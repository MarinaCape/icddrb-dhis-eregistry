package org.icddrb.dhis.android.eregistry.fragments.programoverview.registerrelationshipdialogfragment;

import android.content.Context;
import com.raizlabs.android.dbflow.sql.language.Select;
import java.util.ArrayList;
import java.util.List;
import org.icddrb.dhis.android.eregistry.ui.rows.programoverview.SearchRelativeTrackedEntityInstanceItemRow;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.controllers.tracker.TrackerController;
import org.icddrb.dhis.android.sdk.persistence.loaders.Query;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.Program;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramTrackedEntityAttribute;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttribute;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.events.EventRow;

public class RegisterRelationshipDialogFragmentQuery implements Query<RegisterRelationshipDialogFragmentForm> {
    public static final String TAG = RegisterRelationshipDialogFragmentQuery.class.getSimpleName();
    private final int NUMBER_OF_ATTRIBUTES = 4;
    private String activeProgramUid;
    private long trackedEntityInstanceId;

    public RegisterRelationshipDialogFragmentQuery(long trackedEntityInstanceId, String activeProgramUid) {
        this.trackedEntityInstanceId = trackedEntityInstanceId;
        this.activeProgramUid = activeProgramUid;
    }

    public RegisterRelationshipDialogFragmentForm query(Context context) {
        RegisterRelationshipDialogFragmentForm form = new RegisterRelationshipDialogFragmentForm();
        TrackedEntityInstance trackedEntityInstance = TrackerController.getTrackedEntityInstance(this.trackedEntityInstanceId);
        if (trackedEntityInstance != null) {
            form.setTrackedEntityInstance(trackedEntityInstance);
            List<TrackedEntityInstance> trackedEntityInstances = new Select().from(TrackedEntityInstance.class).queryList();
            if (trackedEntityInstances != null) {
                List<EventRow> teiRows = new ArrayList();
                for (TrackedEntityInstance tei : trackedEntityInstances) {
                    if (!(trackedEntityInstance == null || tei.getLocalId() == this.trackedEntityInstanceId)) {
                        teiRows.add(createTrackedEntityInstanceItem(context, tei, 4, this.activeProgramUid));
                    }
                }
                form.setRows(teiRows);
            }
        }
        return form;
    }

    private SearchRelativeTrackedEntityInstanceItemRow createTrackedEntityInstanceItem(Context context, TrackedEntityInstance trackedEntityInstance, int numberOfAttributes, String activeProgramUid) {
        SearchRelativeTrackedEntityInstanceItemRow trackedEntityInstanceItemRow = new SearchRelativeTrackedEntityInstanceItemRow(context);
        trackedEntityInstanceItemRow.setTrackedEntityInstance(trackedEntityInstance);
        if (trackedEntityInstance.getAttributes() != null) {
            int i;
            String value;
            List<Enrollment> enrollments = TrackerController.getEnrollments(trackedEntityInstance);
            Program activeProgram = MetaDataController.getProgram(activeProgramUid);
            List<TrackedEntityAttribute> attributesToShow = new ArrayList();
            if (!(enrollments == null || enrollments.isEmpty())) {
                Program program = null;
                for (Enrollment e : enrollments) {
                    if (!(e == null || e.getProgram() == null || e.getProgram().getProgramTrackedEntityAttributes() == null)) {
                        program = e.getProgram();
                        if (program.getUid().equals(activeProgram.getUid())) {
                            break;
                        }
                    }
                }
                List<ProgramTrackedEntityAttribute> programTrackedEntityAttributes = program.getProgramTrackedEntityAttributes();
                i = 0;
                while (i < programTrackedEntityAttributes.size() && i < numberOfAttributes) {
                    attributesToShow.add(((ProgramTrackedEntityAttribute) programTrackedEntityAttributes.get(i)).getTrackedEntityAttribute());
                    i++;
                }
            }
            List<TrackedEntityAttributeValue> attributes = new ArrayList();
            i = 0;
            while (i < 4) {
                value = "";
                if (attributesToShow != null && attributesToShow.size() > i) {
                    TrackedEntityAttributeValue av = TrackerController.getTrackedEntityAttributeValue(((TrackedEntityAttribute) attributesToShow.get(i)).getUid(), trackedEntityInstance.getLocalId());
                    if (!(av == null || av.getValue() == null)) {
                        trackedEntityInstanceItemRow.addColumn(av.getValue());
                    }
                } else if (!(trackedEntityInstance.getAttributes().size() <= i || trackedEntityInstance.getAttributes().get(i) == null || ((TrackedEntityAttributeValue) trackedEntityInstance.getAttributes().get(i)).getValue() == null)) {
                    attributes.add(trackedEntityInstance.getAttributes().get(i));
                }
                i++;
            }
            for (ProgramTrackedEntityAttribute programTrackedEntityAttribute : MetaDataController.getProgramTrackedEntityAttributes(activeProgram.getUid())) {
                boolean hasAttribute = false;
                for (TrackedEntityAttributeValue attributeValue : attributes) {
                    if (programTrackedEntityAttribute.getTrackedEntityAttributeId().equals(attributeValue.getTrackedEntityAttributeId())) {
                        value = "";
                        if (attributeValue.getValue() != null) {
                            value = attributeValue.getValue();
                            hasAttribute = true;
                        }
                        trackedEntityInstanceItemRow.addColumn(value);
                    }
                }
                if (!hasAttribute) {
                    trackedEntityInstanceItemRow.addColumn("");
                }
            }
        }
        return trackedEntityInstanceItemRow;
    }
}
