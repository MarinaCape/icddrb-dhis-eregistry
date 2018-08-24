package org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.autocompleterow;

import org.icddrb.dhis.android.sdk.persistence.models.BaseValue;
import org.icddrb.dhis.android.sdk.ui.dialogs.AutoCompleteDialogFragment.OnOptionSelectedListener;
import org.icddrb.dhis.android.sdk.utils.api.ValueType;

public interface AutoCompleteOnOptionSelectedListener extends OnOptionSelectedListener {
    void setValue(BaseValue baseValue);

    void setValueType(ValueType valueType);
}
