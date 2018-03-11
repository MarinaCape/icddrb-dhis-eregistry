package org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.autocompleterow;

import org.icddrb.dhis.android.sdk.persistence.models.BaseValue;
import org.icddrb.dhis.android.sdk.ui.dialogs.AutoCompleteDialogFragment;
import org.icddrb.dhis.android.sdk.utils.api.ValueType;

public interface AutoCompleteOnOptionSelectedListener extends AutoCompleteDialogFragment.OnOptionSelectedListener {
    void setValue(BaseValue value);
    void setValueType(ValueType valueType); // Norway

}
