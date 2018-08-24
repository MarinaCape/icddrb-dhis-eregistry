package org.icddrb.dhis.android.sdk.persistence.converters;

import com.raizlabs.android.dbflow.converter.TypeConverter;
import org.icddrb.dhis.android.sdk.persistence.models.meta.State;

public final class StateConverter extends TypeConverter<String, State> {
    public String getDBValue(State model) {
        return model.toString();
    }

    public State getModelValue(String data) {
        return State.valueOf(data);
    }
}
