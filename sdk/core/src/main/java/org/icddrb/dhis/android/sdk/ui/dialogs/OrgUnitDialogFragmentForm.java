package org.icddrb.dhis.android.sdk.ui.dialogs;

import java.util.List;
import org.icddrb.dhis.android.sdk.persistence.models.OrganisationUnit;
import org.icddrb.dhis.android.sdk.ui.dialogs.AutoCompleteDialogAdapter.OptionAdapterValue;
import org.icddrb.dhis.android.sdk.utils.api.ProgramType;

public class OrgUnitDialogFragmentForm {
    private List<OptionAdapterValue> optionAdapterValueList;
    private List<OrganisationUnit> organisationUnits;
    private ProgramType[] programTypes;
    private Error type;

    public enum Error {
        NONE,
        NO_ASSIGNED_ORGANISATION_UNITS,
        NO_PROGRAMS_TO_ORGANSATION_UNIT
    }

    public ProgramType[] getProgramTypes() {
        return this.programTypes;
    }

    public void setProgramTypes(ProgramType[] programTypes) {
        this.programTypes = programTypes;
    }

    public List<OptionAdapterValue> getOptionAdapterValueList() {
        return this.optionAdapterValueList;
    }

    public void setOptionAdapterValueList(List<OptionAdapterValue> optionAdapterValueList) {
        this.optionAdapterValueList = optionAdapterValueList;
    }

    public List<OrganisationUnit> getOrganisationUnits() {
        return this.organisationUnits;
    }

    public void setOrganisationUnits(List<OrganisationUnit> organisationUnits) {
        this.organisationUnits = organisationUnits;
    }

    public Error getType() {
        return this.type;
    }

    public void setType(Error type) {
        this.type = type;
    }
}
