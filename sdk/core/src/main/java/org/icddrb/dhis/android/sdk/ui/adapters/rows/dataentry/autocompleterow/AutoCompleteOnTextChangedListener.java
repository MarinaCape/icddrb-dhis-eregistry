package org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.autocompleterow;

import android.text.Editable;
import android.text.TextUtils;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.models.BaseValue;
import org.icddrb.dhis.android.sdk.persistence.models.Option;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.AbsTextWatcher;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.DataEntryRowTypes;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.RowValueChangedEvent;

public class AutoCompleteOnTextChangedListener extends AbsTextWatcher {
    private OptionNameCacher optionNameCacher;
    private String optionSetId;
    private BaseValue value;

    public void setBaseValue(BaseValue value) {
        this.value = value;
    }

    public void setOptionSetId(String optionSetId) {
        this.optionSetId = optionSetId;
    }

    public void setCachedOptionNameClearer(OptionNameCacher optionNameCacher) {
        this.optionNameCacher = optionNameCacher;
    }

    public void afterTextChanged(Editable s) {
        String newValue;
        String name = s != null ? s.toString() : "";
        String previousValue = this.value.getValue();
        if ("".equals(name)) {
            newValue = "";
        } else {
            Option option = (Option) new Select().from(Option.class).where(Condition.column("name").is(name)).and(Condition.column("optionSet").is(this.optionSetId)).querySingle();
            if (option == null) {
                newValue = "";
            } else {
                newValue = option.getCode();
            }
        }
        if (TextUtils.isEmpty(newValue)) {
            newValue = "";
        }
        if (!newValue.equals(previousValue)) {
            this.value.setValue(newValue);
            this.optionNameCacher.cacheOptionName();
            Dhis2Application.getEventBus().post(new RowValueChangedEvent(this.value, DataEntryRowTypes.OPTION_SET.toString()));
        }
    }
}
