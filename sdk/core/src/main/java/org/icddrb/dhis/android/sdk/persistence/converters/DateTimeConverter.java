package org.icddrb.dhis.android.sdk.persistence.converters;

import com.raizlabs.android.dbflow.converter.TypeConverter;
import org.joda.time.DateTime;

public final class DateTimeConverter extends TypeConverter<String, DateTime> {
    public String getDBValue(DateTime model) {
        if (model != null) {
            return model.toString();
        }
        return null;
    }

    public DateTime getModelValue(String data) {
        if (data != null) {
            return DateTime.parse(data);
        }
        return null;
    }
}
