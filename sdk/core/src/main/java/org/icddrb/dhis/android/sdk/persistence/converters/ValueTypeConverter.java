package org.icddrb.dhis.android.sdk.persistence.converters;

import com.raizlabs.android.dbflow.converter.TypeConverter;
import org.icddrb.dhis.android.sdk.utils.api.ValueType;

public final class ValueTypeConverter extends TypeConverter<String, ValueType> {
    public String getDBValue(ValueType model) {
        return model.toString();
    }

    public ValueType getModelValue(String data) {
        return ValueType.valueOf(data);
    }
}
