package org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry;

import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.io.Serializable;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.persistence.models.BaseValue;
import org.icddrb.dhis.android.sdk.persistence.models.DataElement;
import org.icddrb.dhis.android.sdk.persistence.models.DataValue;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttribute;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;

public abstract class Row implements DataEntryRow, Serializable {
    private boolean editable = true;
    private boolean hideDetailedInfoButton;
    protected String mDescription;
    protected String mError;
    protected Integer mErrorStringId;
    protected String mLabel;
    protected boolean mMandatory = false;
    protected DataEntryRowTypes mRowType;
    protected BaseValue mValue;
    protected String mWarning;
    private String orgId = null;
    private boolean shouldNeverBeEdited = false;

    public abstract View getView(FragmentManager fragmentManager, LayoutInflater layoutInflater, View view, ViewGroup viewGroup);

    public abstract int getViewType();

    public void setOrg(String v) {
        this.orgId = v;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public BaseValue getValue() {
        return this.mValue;
    }

    public boolean isEditable() {
        return this.editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public Integer getValidationError() {
        return this.mErrorStringId;
    }

    public String getItemId() {
        if (this.mValue instanceof DataValue) {
            return ((DataValue) this.mValue).getDataElement();
        }
        if (this.mValue instanceof TrackedEntityAttributeValue) {
            return ((TrackedEntityAttributeValue) this.mValue).getTrackedEntityAttributeId();
        }
        return "";
    }

    public String getDescription() {
        if (this instanceof EventCoordinatesRow) {
            this.mDescription = "";
        } else if (this instanceof StatusRow) {
            this.mDescription = "";
        } else if (this instanceof IndicatorRow) {
            return this.mDescription;
        }
        String itemId = getItemId();
        DataElement dataElement = MetaDataController.getDataElement(itemId);
        if (dataElement != null) {
            this.mDescription = dataElement.getDescription();
        } else {
            TrackedEntityAttribute attribute = MetaDataController.getTrackedEntityAttribute(itemId);
            if (attribute != null) {
                this.mDescription = attribute.getDescription();
            }
        }
        return this.mDescription;
    }

    public void checkNeedsForDescriptionButton() {
        this.mDescription = getDescription();
        if (this.mDescription == null || this.mDescription.equals("")) {
            setHideDetailedInfoButton(true);
        } else {
            setHideDetailedInfoButton(false);
        }
    }

    public boolean isDetailedInfoButtonHidden() {
        return this.hideDetailedInfoButton;
    }

    public void setHideDetailedInfoButton(boolean hideDetailedInfoButton) {
        this.hideDetailedInfoButton = hideDetailedInfoButton;
    }

    public String getWarning() {
        return this.mWarning;
    }

    public void setWarning(String mWarning) {
        this.mWarning = mWarning;
    }

    public String getError() {
        return this.mError;
    }

    public void setError(String mError) {
        this.mError = mError;
    }

    public boolean isShouldNeverBeEdited() {
        return this.shouldNeverBeEdited;
    }

    public void setShouldNeverBeEdited(boolean shouldNeverBeEdited) {
        this.shouldNeverBeEdited = shouldNeverBeEdited;
    }

    public boolean isMandatory() {
        return this.mMandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mMandatory = mandatory;
    }

    public boolean isEditTextRow() {
        if (DataEntryRowTypes.TEXT.equals(this.mRowType) || DataEntryRowTypes.LONG_TEXT.equals(this.mRowType) || DataEntryRowTypes.NUMBER.equals(this.mRowType) || DataEntryRowTypes.INTEGER.equals(this.mRowType) || DataEntryRowTypes.INTEGER_NEGATIVE.equals(this.mRowType) || DataEntryRowTypes.INTEGER_ZERO_OR_POSITIVE.equals(this.mRowType) || DataEntryRowTypes.PHONE_NUMBER.equals(this.mRowType) || DataEntryRowTypes.PERCENTAGE.equals(this.mRowType) || DataEntryRowTypes.INTEGER_POSITIVE.equals(this.mRowType) || DataEntryRowTypes.INVALID_DATA_ENTRY.equals(this.mRowType)) {
            return true;
        }
        return false;
    }
}
