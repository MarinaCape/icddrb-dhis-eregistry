package org.icddrb.dhis.android.sdk.persistence.converters;

import com.raizlabs.android.dbflow.converter.TypeConverter;
import org.icddrb.dhis.android.sdk.utils.api.ProgramType;

public final class ProgramTypeConverter extends TypeConverter<String, ProgramType> {
    public String getDBValue(ProgramType model) {
        return model.toString();
    }

    public ProgramType getModelValue(String data) {
        return ProgramType.valueOf(data);
    }
}
